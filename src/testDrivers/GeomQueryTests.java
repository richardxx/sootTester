package testDrivers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.pag.VarNode;
import soot.jimple.toolkits.callgraph.CallGraph;
import soot.jimple.toolkits.callgraph.Edge;

public class GeomQueryTests 
{
	static GeomQueryTests tester = new GeomQueryTests();
	
	public static void main(String[] args) {
		tester.testAnyCallEdge();
		//tester.test2CFA();
	}
	
    public void testAnyCallEdge()
    {
    	NodeOne n1 = new NodeOne();
		NodeTwo n2 = new NodeTwo();
		n1.execute1();
		n2.execute2();
    }
    
    public void test1CFA() 
    {
        NodeRoot node = new NodeRoot();
        node.setClass(NodeOne.class);
        node = new NodeRoot();
        node.setClass(NodeTwo.class);
        node.doThingsEntry();
    }
    
    public void test2CFA()
    {
    	NodeOne n1 = new NodeOne();
		NodeTwo n2 = new NodeTwo();
		n1.execute1();
		n2.execute2();
    }
}

class NodeRoot {
	public Class<?> cls;
    public void setClass (Class<?> c) {
        cls = c;
    }
    
    public void doThingsEntry() 
    {
    	printValues();
    }
    
    public void printValues()
    {
    	System.err.println(this.hashCode());
    }
}

class NodeOne extends NodeRoot 
{
	public void execute1()
	{
		doThingsEntry();
	}
}

class NodeTwo extends NodeRoot 
{
	public void execute2()
	{
		doThingsEntry();
	}
}


class TestQueryByCallChain extends SceneTransformer 
{
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		// Obtain the points-to analyzer
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo)) return;
		GeomPointsTo geomPts = (GeomPointsTo) pa;
		System.err.println("Query testing starts.");

		// classes
		SootClass test1 = Scene.v().getSootClass("mypack.GeomQueryTests");
		SootClass nodeRoot = Scene.v().getSootClass("mypack.NodeRoot");
		SootClass nodeOne = Scene.v().getSootClass("mypack.NodeOne");
		SootClass nodeTwo = Scene.v().getSootClass("mypack.NodeTwo");
		
		// methods
		SootMethod mainMethod = Scene.v().getMainMethod();
		SootMethod test2CFA = test1.getMethodByName("test2CFA");
		SootMethod doThingsEntry = nodeRoot.getMethodByName("doThingsEntry");
		SootMethod printValues = nodeRoot.getMethodByName("printValues");
		SootMethod execute1 = nodeOne.getMethodByName("execute1");
		SootMethod execute2 = nodeTwo.getMethodByName("execute2");
		
		// Obtain call graph and enclosing method of querying pointer
		CallGraph cg = Scene.v().getCallGraph();
		Local l = printValues.getActiveBody().getThisLocal();
		VarNode vn = geomPts.findLocalVarNode(l);
		
		// Querying
		GeomQueries queries = new GeomQueries(geomPts);
		PtSensVisitor objFull = new Obj_full_extractor();
		
		// Try 2CFA
		Iterator<Edge> it;
		Edge[] callChain = new Edge[2];
		
		it = cg.edgesOutOf(execute2);
		callChain[0] = it.next();
		
		it = cg.edgesOutOf(doThingsEntry);
		callChain[1] = it.next();
		
		if ( queries.contextsByCallChain(callChain, l, objFull) ) {
			System.out.println();
			System.out.println(vn.toString() + ":");
			objFull.debugPrint();
		}
		
		System.out.println();
	}
}

class TestQueryByAnyEdge extends SceneTransformer 
{
	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		// Obtain the points-to analyzer
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo)) return;
		GeomPointsTo geomPTA = (GeomPointsTo) pa;
		System.out.println("\n\nQuery testing starts.");

		// Classes
		SootClass test1 = Scene.v().getSootClass("mypack.GeomQueryTests");
		SootClass nodeRoot = Scene.v().getSootClass("mypack.NodeRoot");
		SootClass nodeOne = Scene.v().getSootClass("mypack.NodeOne");
		SootClass nodeTwo = Scene.v().getSootClass("mypack.NodeTwo");
		
		// Obtain call graph and enclosing method of querying pointer
		SootMethod mainMethod = Scene.v().getMainMethod();
		SootMethod testAnyCallEdge = test1.getMethodByName("testAnyCallEdge");
		SootMethod doThingsEntry = nodeRoot.getMethodByName("doThingsEntry");
		SootMethod printValues = nodeRoot.getMethodByName("printValues");
		SootMethod execute1 = nodeOne.getMethodByName("execute1");
		SootMethod execute2 = nodeTwo.getMethodByName("execute2");
		
		// Obtain querying pointer
		Local l = printValues.getActiveBody().getThisLocal();
		VarNode vn = geomPTA.findLocalVarNode(l);
		
		// We refine its points-to information again
		Set<Node> qryNodes = new HashSet<Node>();
		qryNodes.add(vn);
		geomPTA.ddSolve(qryNodes);
		
		// Obtain query interface
		GeomQueries queries = new GeomQueries(geomPTA);
		
		// Obtain query result container
		PtSensVisitor objFull = new Obj_full_extractor();
		
		// Obtain context edges
		CallGraph cg = Scene.v().getCallGraph();
		Iterator<Edge> it = cg.edgesOutOf(testAnyCallEdge);
		while ( it.hasNext() ) {
			Edge e = it.next();
			if ( !e.isVirtual() ) continue;
			
			// Please clear the container before every query call
			if ( queries.contexsByAnyCallEdge(e, l, objFull) ) {
				// Please call function finish() after every query call
				System.out.println();
				System.out.println(vn.toString() + ":");
				objFull.debugPrint();
			}
		}
		
		System.out.println();
	}
}

