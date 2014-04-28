package testDrivers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import soot.Main;
import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SceneTransformer;
import soot.SootClass;
import soot.Transform;
import soot.util.Chain;

/**
 * Extract the type hierarchy from the Java program.
 * @author xiao
 *
 */
public class ExtractTypes extends SceneTransformer {
	
	public static int setGlobalOptions( String[] opts )
	{
		opts[0] = "-keep-line-number";
		return 1;
	}
	
	public static int setPhaseOptionsForSparkWork(String[] opts, int offset) 
	{
		opts[offset+0] = "-p";
		opts[offset+1] = "cg";
		opts[offset+2] = "verbose:false";
		
		opts[offset+3] = "-p";
		opts[offset+4] = "cg";
		opts[offset+5] = "safe-forname:false";
		
		opts[offset+6] = "-p";
		opts[offset+7] = "cg";
		opts[offset+8] = "safe-newinstance:false";
		
		opts[offset+9] = "-p";
		opts[offset+10] = "cg.spark";
		opts[offset+11] = "enabled:true";
		
		opts[offset+12] = "-p";
		opts[offset+13] = "cg.spark";
		opts[offset+14] = "verbose:false";
		
		opts[offset+15] = "-p";
		opts[offset+16] = "cg.spark";
		opts[offset+17] = "merge-stringbuffer:false";
		
		opts[offset+18] = "-p";
		opts[offset+19] = "cg.spark";
		opts[offset+20] = "string-constants:false";
		
		opts[offset+21] = "-p";
		opts[offset+22] = "cg.spark";
		opts[offset+23] = "dump-types:false";
		
		opts[offset+24] = "-p";
		opts[offset+25] = "cg.spark";
		opts[offset+26] = "dump-solution:false";
		
		opts[offset+27] = "-p";
		opts[offset+28] = "cg.spark";
		opts[offset+29] = "simplify-offline:false";
		
		opts[offset+30] = "-p";
		opts[offset+31] = "cg.spark";
		opts[offset+32] = "simplify-sccs:false";
	
		opts[offset+33] = "-p";
		opts[offset+34] = "cg.spark";
		opts[offset+35] = "dump-pag:false";
		
		opts[offset+36] = "-p";
		opts[offset+37] = "cg.spark";
		opts[offset+38] = "vta:false";
		
		opts[offset+39] = "-p";
		opts[offset+40] = "jb";
		opts[offset+41] = "use-original-names:true";
		
		opts[offset+42] = "-p";
		opts[offset+43] = "jb.ulp";
		opts[offset+44] = "enabled:false";
		
		return offset + 45;
	}
	
	public static void main(String[] args) throws FileNotFoundException 
	{
		Vector<benchmark> test_cases = new Vector<benchmark>();
		
		String run_list = "-w -cp /home/xiao/workspace/pointer/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar mypack.list mypack.ListExample";
		
		String run_hello = "-w -cp /home/xiao/workspace/pointer/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar mypack.hello";
		
		String run_parser = "-w -cp /home/xiao/program/soot/soot-2.4.0/classes/:/opt/jdk1.3.1_20/jre/lib/rt.jar mypack.parser_emulator mypack.node_base mypack.tree_node1 mypack.tree_node2 mypack.tree_node3";
		
		String run_compress = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._201_compress.Main";
		
		String run_compress_1_4 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar spec.benchmarks._201_compress.Main";
		
		String run_compress_1_5 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/home/xiao/library/jdk1.5.0_22/jre/lib/rt.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/jce.jar spec.benchmarks._201_compress.Main";
		
		String run_compress_1_6 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar spec.benchmarks._201_compress.Main";
		
		String run_db = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._209_db.Main";
		
		String run_db_1_6 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar spec.benchmarks._209_db.Main";
		
		String run_jess = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._202_jess.Main";
		
		String run_mtrt = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._227_mtrt.Main";
		
		String run_raytrace = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._205_raytrace.Main";
		
		String run_jflex = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar JFlex.Main";
		
		String run_jflex_1_4 = "-w -cp /home/xiao/workspace/jflex/bin:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar JFlex.Main";
		
		String run_jflex_1_6 = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar JFlex.Main";
		
		String run_soot = "-w -cp /home/xiao/program/benchmarks/java/soot-c/classes:/opt/jdk1.3.1_20/jre/lib/rt.jar ca.mcgill.sable.soot.jimple.Main";
		
		String run_sable_j = "-w -cp /home/xiao/program/benchmarks/java/sablecc-j/classes:/opt/jdk1.3.1_20/jre/lib/rt.jar ca.mcgill.sable.sablecc.SableCC";
		
		String run_jetty = "-w -cp /home/xiao/program/benchmarks/java/jetty-4.2.27/classes/:/opt/jdk1.3.1_20/jre/lib/rt.jar org.mortbay.start.Main";
		
		String run_polyglot = "-w -cp /home/xiao/program/benchmarks/java/polyglot-1.2.0/classes:/opt/jdk1.3.1_20/jre/lib/rt.jar polyglot.main.Main";
		
		String run_jasmin = "-w -cp /home/xiao/workspace/jasmin/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar jasmin.Main";
		
		String run_jlex = "-w -cp /home/xiao/program/benchmarks/java/jlex:/opt/jdk1.3.1_20/jre/lib/rt.jar JLex.Main";
		
		String run_javacup = "-w -cp /home/xiao/workspace/java_cup/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar java_cup.Main";
		
		String run_javacc = "-w -cp /home/xiao/workspace/javacc/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar javacc";
		
		String run_j2ssh = "-w -cp /home/xiao/workspace/j2ssh/bin:/usr/share/java/ant.jar:/usr/share/java/ant-launcher.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/commons-logging.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/jce-jdk13-119.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/xercesImpl.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/xmlParserAPIs.jar:/opt/jdk1.3.1_20/jre/lib/rt.jar -app -process-dir /home/xiao/workspace/j2ssh/bin";
		
		String run_antlr = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar antlr.Tool";
		
		String run_antlr_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar antlr.Tool";
		
		String run_bloat = "-w -cp /home/xiao/workspace/bloat/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_bloat_1_4 = "-w -cp /home/xiao/workspace/bloat/bin:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_bloat_1_5 = "-w -cp /home/xiao/workspace/bloat/bin:/home/xiao/library/jdk1.5.0_22/jre/lib/rt.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/jce.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_bloat_1_6 = "-w -cp /home/xiao/workspace/bloat/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_ps = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/ps/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar edu.unm.cs.oal.DaCapo.JavaPostScript.Red.executive";
		
		String run_jython = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/jython/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar org.python.util.jython";
		
		String run_jython_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/jython/classfiles:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar org.python.util.jython";
		
		String run_chart = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/chart/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar dacapo.Plotter";
		
		String run_pmd = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/pmd/classfiles:/home/xiao/program/benchmarks/java/dacapo_20050224/common/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar net.sourceforge.pmd.PMD";
		
		String run_pmd_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/pmd/classfiles:/home/xiao/program/benchmarks/java/dacapo_20050224/common/classfiles:/home/xiao/library/j2sdk1.4.0/jre/lib/rt.jar net.sourceforge.pmd.PMD";
		
		String run_xalan = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/common/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar org.apache.xalan.xslt.Process";
		
		String run_jedit = "-w -cp /home/xiao/workspace/jedit/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_5 = "-w -cp /home/xiao/workspace/jedit4/bin:/home/xiao/library/jdk1.5.0_22/jre/lib/rt.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/jce.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_6 = "-w -cp /home/xiao/workspace/jedit4/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar org.gjt.sp.jedit.jEdit";
		
		String run_megamek = "-w -cp /home/xiao/workspace/MegaMek/bin:/home/xiao/program/benchmarks/java/MegaMek/lib/TinyXML.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/TabPanel.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/PngEncoder.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/Ostermiller.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/collections.jar:/opt/jdk1.3.1_20/jre/lib/rt.jar megamek.MegaMek";
		
		String run_megamek_1_6 = "-w -cp /home/xiao/workspace/MegaMek/bin:/home/xiao/program/benchmarks/java/MegaMek/lib/TinyXML.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/TabPanel.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/PngEncoder.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/Ostermiller.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/collections.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar megamek.MegaMek";
		
		String run_vuze = "-w -cp /home/xiao/workspace/Vuze/bin:/home/xiao/library/swt.jar:/home/xiao/library/commons-cli-1.2.jar:/home/xiao/library/log4j-1.2.16.jar:/home/xiao/workspace/soot_pointer/libs/juit-4.8.1.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/rt.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/jce.jar:/home/xiao/library/jdk1.5.0_22/jre/lib/jsse.jar org.gudy.azureus2.ui.swt.Main";
		
		test_cases.add( new benchmark(run_compress, "dump/compress") );
//		test_cases.add( new benchmark(run_compress_1_4, "dump/compress") );
//		test_cases.add( new benchmark(run_compress_1_5, "dump/compress") );
//		test_cases.add( new benchmark(run_compress_1_6, "dump/compress") );
//		test_cases.add( new benchmark(run_db, "dump/db") );
//		test_cases.add( new benchmark(run_db_1_6, "dump/db") );
//		test_cases.add( new benchmark(run_jess, "dump/jess") );
//		test_cases.add( new benchmark(run_mtrt, "dump/mtrt") );
//		test_cases.add( new benchmark(run_jflex, "dump/jflex") );
//		test_cases.add( new benchmark(run_jflex_1_4, "dump/jflex") );
//		test_cases.add( new benchmark(run_jflex_1_6, "dump/jflex") );
//		test_cases.add( new benchmark(run_soot, "dump/soot") );
//		test_cases.add( new benchmark(run_sable_j, "dump/sable") );
//		test_cases.add( new benchmark(run_jetty, "dump/jetty") );
//		test_cases.add( new benchmark(run_polyglot, "dump/polyglot") );
//		test_cases.add( new benchmark(run_jasmin, "dump/jasmin") );
//		test_cases.add( new benchmark(run_jlex, "dump/jlex") );
//		test_cases.add( new benchmark(run_javacup, "dump/javacup") );
//		test_cases.add( new benchmark(run_javacc, "dump/javacc") );
//		test_cases.add( new benchmark(run_j2ssh, "dump/j2ssh") );
//		test_cases.add( new benchmark(run_antlr, "dump/antlr") );
//		test_cases.add( new benchmark(run_antlr_1_4, "dump/antlr") );
//		test_cases.add( new benchmark(run_bloat, "dump/bloat") );
//		test_cases.add( new benchmark(run_bloat_1_4, "dump/bloat") );
//		test_cases.add( new benchmark(run_bloat_1_5, "dump/bloat") );
//		test_cases.add( new benchmark(run_bloat_1_6, "dump/bloat") );
//		test_cases.add( new benchmark(run_ps, "dump/ps") );
//		test_cases.add( new benchmark(run_pmd_1_4, "dump/pmd") );
//		test_cases.add( new benchmark(run_pmd, "dump/pmd") );
//		test_cases.add( new benchmark(run_jython, "dump/jython") );
//		test_cases.add( new benchmark(run_jython_1_4, "dump/jython") );
//		test_cases.add( new benchmark(run_chart, "dump/chart") );
//		test_cases.add( new benchmark(run_xalan, "dump/xalan") );
//		test_cases.add( new benchmark(run_jedit, "dump/jedit") );
//		test_cases.add( new benchmark(run_jedit_1_5, "dump/jedit") );
//		test_cases.add( new benchmark(run_jedit_1_6, "dump/jedit") );
//		test_cases.add( new benchmark(run_megamek, "dump/megamek") );
//		test_cases.add( new benchmark(run_megamek_1_6, "dump/megamek") );
//		test_cases.add( new benchmark(run_vuze, "dump/vuze") );
		
		String[] opts = new String[80];
		String[] other_opts = null;
		int offset;

		for ( int i = 0; i < test_cases.size(); ++i ) {
			benchmark tc = test_cases.get(i);
			other_opts = tc.bench_name.split(" ");
			
			for (int j = 0; j < opts.length; ++j )
				opts[j] = null;
			
			offset = setGlobalOptions(opts);
			offset = setPhaseOptionsForSparkWork(opts, offset);
			
			for (int j = 1; j < other_opts.length; ++j ) 
				opts[ offset + j - 1 ] = other_opts[j];
			
			
			Pack packs = PackManager.v().getPack("cg");
			packs.add(new Transform("cg.typeExtractor", new ExtractTypes()));
			Main.main(opts);
		}
	}

	@Override
	protected void internalTransform(String phaseName, Map options) 
	{
		Chain<SootClass> clsList = Scene.v().getClasses();
		Map<SootClass, Integer> cl2int = new HashMap<SootClass, Integer>();
		int i;
		
		System.err.println( "I'm here" );
		
		i = 0;
		for ( SootClass sc : clsList ) {
			cl2int.put(sc, i);
			++i;
		}
		
		try {
			final PrintWriter file = new PrintWriter(new FileOutputStream(
					new File("Types", "graph.txt")));

			file.println( i );
			for ( SootClass sc : clsList ) {
				// Output the super class
				SootClass parent = sc.getSuperclass();
				if ( sc != null )
					file.println( cl2int.get(parent) );
				
				// Output all the implemented interfaces
				Chain<SootClass> interfaces = sc.getInterfaces();
				for ( SootClass interf : interfaces ) {
					file.println( cl2int.get(interf) );
				}
				
				file.println( -1 );
			}
		}
		catch (Exception e) {
			throw new RuntimeException("Cannot open file.");
		}
	}
}

