package testDrivers;

import geomDumper.ContextTranslator;
import geomDumper.PointsToDumper;
import geomDumper.PtInfoCollector;
import geomDumper.SideEffectAnalyzer;

import java.util.Map;
import java.util.Set;
import java.util.Vector;

import soot.Main;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.Transform;
import soot.jimple.spark.geom.geomPA.GeomPointsTo;
import soot.jimple.spark.geom.geomPA.IVarAbstraction;


public class DumpDriver {
	
	public static String dump_points_to = "p1_o1";
	public static boolean dump_base_ptrs = true;
	public static boolean dump_mod_ref = false;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		// We first extract the options
		Vector<String> new_opts = new Vector<String>();
		
		for ( int i = 0; i < args.length; i++ ) {
			String opt = args[i];
			
			if (opt.equals("-dump_points_to")) {
				if ( i+1 >= args.length )
					throw new RuntimeException("Argument to -dump_points_to option is wrong");
				
				dump_points_to = args[++i];
			}
			else if (opt.equals("-dump_mod_ref") )
				dump_mod_ref = true;
			else {
				new_opts.add(opt);
			}
		}
		
		// Install the dump transformer
		Pack wjtp = PackManager.v().getPack("wjtp");
		wjtp.add(new Transform("wjtp.dpts", new dumpTransformer()));
		Main.main(new_opts.toArray(new String[0]));
	}
}

class dumpTransformer extends SceneTransformer
{

	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		GeomPointsTo gpts = (GeomPointsTo)Scene.v().getPointsToAnalysis();
		
		// Dump points-to matrix
		PointsToDumper ptDumper = new PointsToDumper();
		
		if ( DumpDriver.dump_points_to.equals("p1_oN") )
			ptDumper.dump_pointer_1cfa_object_full_result();
		else if ( DumpDriver.dump_points_to.equals("p1_o1") )
			ptDumper.dump_pointer_object_1cfa_mapped_result();
		else
			throw new RuntimeException("The points-to dump format is not supported");
		
		ptDumper = null;
		
		// Dump the base pointers for query evaluation
		boolean is_dump = true;
		PtInfoCollector queryPlan = new PtInfoCollector();
		Set<IVarAbstraction> basePointers = queryPlan.collectBasePointers(is_dump);
		gpts.keepOnly(basePointers);
		basePointers = null;
		
		// Dump Mod-Ref matrix
		if ( DumpDriver.dump_mod_ref == true ) { 
			SideEffectAnalyzer sea = new SideEffectAnalyzer(gpts);
			sea.compute(true);
			sea.evaluateSideEffectMatrix();
			sea.dumpSideEffectMatrix();
		}
	}
}
