package testDrivers;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import soot.Local;
import soot.PointsToAnalysis;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.spark.geom.dataMgr.Obj_full_extractor;
import soot.jimple.spark.geom.dataMgr.PtSensVisitor;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.GeomQueries;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;
import soot.util.Chain;

public class Array {
	private Random ran = new Random(1);

	public static void main(String args[]) {
		new Array().onCreate();
	}

	public void onCreate() {
		method1();
	}

	public void method1() {
		String[][] arr = method2();

		/**
		 * PTA query with main->onCreate as context edge for local arr does not
		 * include allocnode returned from method2()
		 **/
		if (arr == null)
			arr = new String[2][2];

		method3(arr);
	}

	public String[][] method2() {
		if (ran.nextBoolean())
			return new String[2][2];

		return null;
	}

	public void method3(String[][] arr) {
		System.out.println(arr);
	}
}

class arrayQueryTester extends SceneTransformer {
	@Override
	protected void internalTransform(String phaseName, Map options) {
		// Obtain the points-to analyzer
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo))
			return;
		GeomPointsTo geomPts = (GeomPointsTo) pa;
		System.err.println("Query testing starts.");

		// classes
		SootClass arrayClass = Scene.v().getSootClass("mypack.Array");

		// methods
		SootMethod mainMethod = Scene.v().getMainMethod();
		SootMethod onCreate = arrayClass.getMethodByName("onCreate");
		SootMethod method1 = arrayClass.getMethodByName("method1");

		// Context edge
		CallGraph cg = Scene.v().getCallGraph();
		Iterator<Edge> it = cg.edgesOutOf(mainMethod);
		Edge context = null;
		
		while ( it.hasNext() ) {
			Edge edge = it.next();
			if ( edge.tgt() == onCreate ) {
				context = edge;
				break;
			}
		}
		
		if ( context == null )
			return;
		
		// Obtain call graph and enclosing method of querying pointer
		
		Chain<Local> locals = method1.getActiveBody().getLocals();
		Local local = null;
		VarNode vn = null;
		
		for ( Local l : locals ) {
			if ( l.getName().equals("arr") ) {
				vn = geomPts.findLocalVarNode(l);
				local = l;
				break;
			}
		}
		
		if ( vn == null ) {
			System.err.println( "Cannot find pointer arr. Abort" );
			return;
		}

		// Querying
		GeomQueries queries = new GeomQueries(geomPts);
		PtSensVisitor objFull = new Obj_full_extractor();

		if (queries.contexsByAnyCallEdge(context, local, objFull)) {
			System.out.println(vn.toString() + ":");
			objFull.debugPrint();
		}

		System.out.println();
		System.out.println();
		
		System.err.println( "Full points-to:" );
		geomPts.findInternalNode(vn).getRepresentative().print_context_sensitive_points_to(System.out);
	}
}
