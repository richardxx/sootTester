package testDrivers;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import soot.Local;
import soot.PointsToAnalysis;
import soot.RefType;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootField;
import soot.SootMethod;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Stmt;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.GeomQueries;

public class AliasTester extends SceneTransformer {

	@Override
	protected void internalTransform(String phaseName,
			Map<String, String> options) {
		PointsToAnalysis pa = Scene.v().getPointsToAnalysis();
		if (!(pa instanceof GeomPointsTo)) return;
		GeomPointsTo geomPTA = (GeomPointsTo) pa;
		geomPTA.ps.println("------------>Query testing starts<---------------");
		
		final Set<Local> locals = new HashSet<Local>();
		Value[] values = new Value[2];
		long cnt_hs_alias = 0, cnt_all = 0;
		
		GeomQueries queryTester = new GeomQueries(geomPTA);
		
		for ( SootMethod sm : geomPTA.getAllReachableMethods() ) {
			if (sm.isJavaLibraryMethod())
				continue;
			if (!sm.isConcrete())
				continue;
			if (!sm.hasActiveBody()) {
				sm.retrieveActiveBody();
			}
			
			locals.clear();
			
			// We first gather all the memory access expressions
			//access_expr.clear();
			for (Iterator stmts = sm.getActiveBody().getUnits().iterator(); stmts
					.hasNext();) {
				Stmt st = (Stmt) stmts.next();
				
				if ( st instanceof AssignStmt ) {
					AssignStmt a = (AssignStmt) st;
					values[0] = a.getLeftOp();
					values[1] = a.getRightOp();
					
					for ( Value v : values ) {
						// We only care those pointers p involving in the expression: p.f
						if (v instanceof InstanceFieldRef) {
							InstanceFieldRef ifr = (InstanceFieldRef) v;
							final SootField field = ifr.getField();
							if ( !(field.getType() instanceof RefType) )
								continue;
							locals.add((Local) ifr.getBase());
						}
					}
				}
			}
			
			for (Local l1 : locals) {
				for (Local l2 : locals) {
					if ( queryTester.isAlias(l1, l2) )
						++cnt_hs_alias;
				}
			}
			
			cnt_all += locals.size() * locals.size();
		}
		
		geomPTA.ps.printf("Heap sensitive alias pairs (by Geom) : %d, Percentage = %.3f%%\n",
				cnt_hs_alias, (double) cnt_hs_alias / cnt_all * 100 );
	}

}
