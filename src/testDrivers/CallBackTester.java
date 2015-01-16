package testDrivers;

import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import soot.AnySubType;
import soot.ArrayType;
import soot.FastHierarchy;
import soot.Local;
import soot.Main;
import soot.Pack;
import soot.PackManager;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootMethod;
import soot.Transform;
import soot.Type;
import soot.Value;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.VirtualInvokeExpr;
import soot.jimple.spark.geom.dataMgr.Obj_full_extractor;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.dataRep.CgEdge;
import soot.jimple.spark.geom.dataRep.IntervalContextVar;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.GeomQueries;
import soot.jimple.spark.pag.AllocNode;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.queue.QueueReader;

public class CallBackTester extends SceneTransformer 
{

	public static void main(String[] args) 
	{
		// We first extract the options
		Vector<String> new_opts = new Vector<String>();
		
		for ( int i = 0; i < args.length; i++ ) {
			String opt = args[i];
			if (opt.equals("-dump_points_to")) {
				if ( i+1 >= args.length )
					throw new RuntimeException("Argument to -dump_points_to option is wrong");
				// skip
				++i;
			}
			else if (opt.equals("-dump_mod_ref") ) {
				// skip
			}
			else {
				new_opts.add(opt);
			}
		}
		
		// Install the dump transformer
		Pack wjtp = PackManager.v().getPack("wjtp");
		wjtp.add(new Transform("wjtp.tpts",new CallBackTester()));
		Main.main(new_opts.toArray(new String[0]));
	}
	
	@Override
	protected void internalTransform(String phaseName,
			Map<String, String> options) {
		
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo)) return;
		GeomPointsTo geomPTA = (GeomPointsTo) pa;
		geomPTA.ps.println("------------>Query testing starts<---------------");
		
		// Collect querying pointers
		Vector<Edge> callins = new Vector<Edge>();
//		Vector<Edge> callouts = new Vector<Edge>();
		Set<Node> qryNodes = new HashSet<Node>();
		
		QueueReader<Edge> edgeList = Scene.v().getCallGraph().listener();
		while (edgeList.hasNext()) {
			Edge edge = edgeList.next();
			CgEdge myEdge = geomPTA.getInternalEdgeFromSootEdge(edge);
			if ( myEdge == null ) continue;
			
			if ( edge.isInstance() && !edge.isSpecial() ) {
			
				SootMethod src_func = edge.src();
				SootMethod tgt_func = edge.tgt();
				
				if ( !src_func.isJavaLibraryMethod() &&
						tgt_func.isJavaLibraryMethod() ) {
					callins.add(edge);
				}
				
				if ( src_func.isJavaLibraryMethod() &&
						!tgt_func.isJavaLibraryMethod() ) {
//					callouts.add(edge);
					
					Stmt callsite = edge.srcStmt();
					InvokeExpr expr = callsite.getInvokeExpr();
					VarNode vn = null;
					
					if ( edge.isVirtual() ) {
						vn = geomPTA.findLocalVarNode( ((VirtualInvokeExpr)expr).getBase() );
					}
					else {
						vn = geomPTA.findLocalVarNode( ((InterfaceInvokeExpr)expr).getBase() );
					}
					
					qryNodes.add(vn);
				}
			}
		}
		
		geomPTA.ps.printf("We collect %d library calls.\n", callins.size() );
		
		// We recompute the callback base pointers
		geomPTA.ddSolve(qryNodes);
		
		// Querying
		GeomQueries queryTester = new GeomQueries(geomPTA);
		PtSensVisitor<?> objFull = new Obj_full_extractor();
		FastHierarchy typeHierarchy = Scene.v().getOrMakeFastHierarchy();
		int ans = 0;
		int ans_myquery = 0;
		
		for ( Edge in : callins ) {
			boolean keep_it = false;
			Vector<Edge> callouts = reachable_callbacks(in.tgt());
			
			for ( Edge out : callouts ) {
				Stmt callsite = out.srcStmt();
				InvokeExpr expr = callsite.getInvokeExpr();
				Value l = null;
				
				if ( out.isVirtual() ) {
					l = ((VirtualInvokeExpr)expr).getBase();
				}
				else {
					l = ((InterfaceInvokeExpr)expr).getBase();
				}
				
				if ( queryTester.contexsByAnyCallEdge(in, (Local)l, objFull) ) {
					//
					for ( Object icv_obj : objFull.outList ) {
						IntervalContextVar icv = (IntervalContextVar)icv_obj;
						AllocNode obj = (AllocNode) icv.var;
						SootMethod target = out.tgt();
						
						Type t = obj.getType();
						
						if (t == null)
							continue;
						else if (t instanceof AnySubType)
							t = ((AnySubType) t).getBase();
						else if (t instanceof ArrayType)
							t = RefType.v("java.lang.Object");
						
						if (typeHierarchy.resolveConcreteDispatch(
								((RefType) t).getSootClass(), target) == target ) {
							keep_it = true;
							break;
						}
					}
					
					if ( keep_it ) {
						break;
					}
				}
			}
			
			if ( !keep_it ) {
				++ans;
				if ( callouts.size() > 0 )
					++ans_myquery;
			}
		}
		
		geomPTA.ps.printf("%d library calls do not call back, %d are proved by geomPTA.\n", 
				ans, ans_myquery);
		geomPTA.ps.println();
	}

	Vector<Edge> reachable_callbacks(SootMethod s)
	{
		CallGraph cg = Scene.v().getCallGraph();
		Vector<Edge> reachable = new Vector<Edge>();
		Set<SootMethod> visited = new HashSet<SootMethod>();
		Deque<SootMethod> queue = new LinkedList<SootMethod>();
		
		queue.add(s);
		visited.add(s);
		
		while ( queue.size() != 0 ) {
			s = queue.removeFirst();
			for ( Iterator<Edge> eIt = cg.edgesOutOf(s); eIt.hasNext(); ) {
				Edge edge = eIt.next();
				SootMethod tgt_func = edge.tgt();
				
				if ( s.isJavaLibraryMethod() &&
						!tgt_func.isJavaLibraryMethod() &&
						edge.isInstance() && !edge.isSpecial() ) {
					reachable.add(edge);
				}
				
				if ( !visited.contains(tgt_func) ) {
					queue.add(tgt_func);
					visited.add(tgt_func);
				}
			}
		}
		
		return reachable;
	}
}
