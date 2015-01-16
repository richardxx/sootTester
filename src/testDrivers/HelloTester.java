package testDrivers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.Local;
import soot.PointsToAnalysis;
import soot.PointsToSet;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.SootField;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.pag.Node;
import soot.jimple.spark.sets.P2SetVisitor;
import soot.jimple.spark.sets.PointsToSetInternal;

public class HelloTester extends SceneTransformer {

	@Override
	protected void internalTransform(String phaseName,
			Map<String, String> options) {
		// Obtain the points-to analyzer
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo))
			return;

		System.err.println("Query testing starts.");

		// classes
		SootClass helloClass = Scene.v().getSootClass("testCases.Hello");
		
		try {
			SootField f = helloClass.getFieldByName("o");
			System.err.print(f + "\n");
			PointsToSet pts_origin = pa.reachingObjects(f);
			if ( !pts_origin.isEmpty() ) {
				PointsToSetInternal pts = (PointsToSetInternal)pts_origin;
				pts.forall(new P2SetVisitor() {
					
					@Override
					public void visit(Node n) {
						// TODO Auto-generated method stub
						System.err.print(n + "\n");
					}
				});
			}
		}
		catch (RuntimeException e) {
			System.err.print("Cannot find the field o.");
		}
	}

}

