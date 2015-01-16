package testDrivers;


import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
//import pldi.locking.studyConcurrent.EvaluatorB;
import soot.Main;
import soot.Pack;
import soot.PackManager;
import soot.Transform;

/**
 * Select a test case, construct the soot options, and run soot.
 * @author xiao
 *
 */
public class SootDriver {

	public static void setGlobalOptions( Vector<String> opts )
	{
		opts.add("-keep-line-number");
//		opts.add("-app");			// If you want to transform the whole application source files, please turn this on
		opts.add("-f");
		opts.add("n");				// Output nothing
	}
	
	public static void setPhaseOptionsForSparkWork(Vector<String> opts) 
	{
		opts.add("-p");
		opts.add("cg");
		opts.add("implicit-entry:false");
		
		opts.add("-p");
		opts.add("cg");
		opts.add("verbose:false");
		
		opts.add("-p");
		opts.add("cg");
		opts.add("safe-forname:false");
		
		opts.add("-p");
		opts.add("cg");
		opts.add("safe-newinstance:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("enabled:true");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("simulate-natives:true");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("verbose:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("merge-stringbuffer:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("string-constants:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("dump-types:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("dump-solution:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("simplify-offline:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("simplify-sccs:false");
	
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("dump-pag:false");
		
		opts.add("-p");
		opts.add("jb");
		opts.add("use-original-names:true");
		
		opts.add("-p");
		opts.add("jb.ulp");
		opts.add("enabled:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-pta:true");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-encoding:geom");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-worklist:PQ");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-eval:1");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-trans:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-frac-base:40");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-blocking:false");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-runs:1");
		
		opts.add("-p");
		opts.add("cg.spark");
		opts.add("geom-app-only:true");
	}
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException 
	{
		Vector<benchmark> test_cases = new Vector<benchmark>();
		
		String CommonLibFiles =
			"/home/xiao/library/activation.jar:" + 
			"/home/xiao/library/antlrworks-1.4.3.jar:" + 
			"/home/xiao/library/asm-4.0_RC1.jar:" + 
			"/home/xiao/library/bcel-5.2.jar:" + 
			"/home/xiao/library/bsf.jar:" + 
			"/home/xiao/library/commons-cli-1.2.jar:" + 
			"/home/xiao/library/commons-logging-1.1.1.jar:" + 
			"/home/xiao/library/commons-net-1.4.1.jar:" + 
			"/home/xiao/library/commons-net-3.0.1.jar:" + 
			"/home/xiao/library/jai_codec-1.1.3-alpha.jar:" + 
			"/home/xiao/library/jai-core-1.1.3-alpha.jar:" + 
			"/home/xiao/library/jakarta-oro-2.0.8.jar:" + 
			"/home/xiao/library/jakarta-regexp-1.5.jar:" + 
			"/home/xiao/library/jcommon-1.0.16.jar:" + 
			"/home/xiao/library/jdepend-2.9.1.jar:" + 
			"/home/xiao/library/jline-1.0.jar:" + 
			"/home/xiao/library/jsch-0.1.44.jar:" + 
			"/home/xiao/library/jsp-api.jar:" + 
			"/home/xiao/library/log4j-1.2.16.jar:" + 
			"/home/xiao/library/mail.jar:" + 
			"/home/xiao/library/NetRexxC.jar:" + 
			"/home/xiao/library/ojdbc6.jar:" + 
			"/home/xiao/library/resolver.jar:" + 
			"/home/xiao/library/servlet-api.jar:" + 
			"/home/xiao/library/servlet.jar:" + 
			"/home/xiao/library/SparseBitmap.jar:" + 
			"/home/xiao/library/swt.jar";
		
		String LibXalan =
			"/home/xiao/library/xalan-j_2_7_1/serializer.jar:" + 
			"/home/xiao/library/xalan-j_2_7_1/xalan.jar:" + 
			"/home/xiao/library/xalan-j_2_7_1/xercesImpl.jar:" + 
			"/home/xiao/library/xalan-j_2_7_1/xml-apis.jar:" + 
			"/home/xiao/library/xalan-j_2_7_1/xsltc.jar";
		
		String JythonFiles =
			"/home/xiao/program/benchmarks/java/jython/lib/antlr-3.1.3.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/asm-3.1.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/asm-commons-3.1.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/commons-cli-1.1.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/constantine-0.4.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/janino-2.5.12.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/jna.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/jna-posix.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/jython.jar:" + 
			"/home/xiao/program/benchmarks/java/jython/lib/servlet-api-2.4.jar";
		
		
		
		String run_test1 = "-w -cp /home/xiao/git/sootTester/classes:/opt/jdk1.4.0/jre/lib/rt.jar testCases.Hello";
		
		String run_multitest = "-w -cp /home/xiao/workspace/TA3111/bin:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar MultiThreadTest";
		
		String run_compress = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._201_compress.Main";
		
		String run_compress_1_4 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.4.0/jre/lib/rt.jar spec.benchmarks._201_compress.Main";
		
		String run_compress_1_6 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar spec.benchmarks._201_compress.Main";
		
		String run_db = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._209_db.Main";
		
		String run_db_1_6 = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar spec.benchmarks._209_db.Main";
		
		String run_jess = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._202_jess.Main";
		
		String run_mtrt = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._227_mtrt.Main";
		
		String run_raytrace = "-w -cp /home/xiao/workspace/SPECJVM_runnable/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar spec.benchmarks._205_raytrace.Main";
		
		String run_jetty = "-w -cp /home/xiao/program/benchmarks/java/jetty-4.2.27/classes/:/opt/jdk1.3.1_20/jre/lib/rt.jar org.mortbay.start.Main";
		
		String run_polyglot = "-w -cp /home/xiao/program/benchmarks/java/polyglot-1.2.0/classes:/opt/jdk1.3.1_20/jre/lib/rt.jar polyglot.main.Main";
		
		String run_jasmin = "-w -cp /home/xiao/workspace/jasmin/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar jasmin.Main";
		
		String run_jlex = "-w -cp /home/xiao/program/benchmarks/java/jlex:/opt/jdk1.3.1_20/jre/lib/rt.jar JLex.Main";
		
		String run_javacup = "-w -cp /home/xiao/workspace/java_cup/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar java_cup.Main";
		
		String run_javacc = "-w -cp /home/xiao/workspace/javacc/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar javacc";
		
		String run_j2ssh = "-w -cp /home/xiao/workspace/j2ssh/bin:/usr/share/java/ant.jar:/usr/share/java/ant-launcher.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/commons-logging.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/jce-jdk13-119.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/xercesImpl.jar:/home/xiao/program/benchmarks/java/j2ssh/lib/xmlParserAPIs.jar:/opt/jdk1.3.1_20/jre/lib/rt.jar -app -process-dir /home/xiao/workspace/j2ssh/bin";
		
		String run_jflex = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar JFlex.Main";
		
		String run_jflex_1_4 = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.4.0/jre/lib/rt.jar JFlex.Main";
		
		String run_jflex_1_5 = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar JFlex.Main";
		
		String run_jflex_1_6 = "-w -cp /home/xiao/workspace/jflex/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar JFlex.Main";
		
		String run_soot = "-w -cp /home/xiao/program/benchmarks/java/soot-c/classes:/opt/jdk1.3.1_20/jre/lib/rt.jar ca.mcgill.sable.soot.jimple.Main";
		
		String run_soot_1_4 = "-w -cp /home/xiao/program/benchmarks/java/soot-c/classes:/opt/jdk1.4.0/jre/lib/rt.jar:/opt/jdk1.4.0/jre/lib/jce.jar ca.mcgill.sable.soot.jimple.Main";
		
		String run_soot_1_5 = "-w -cp /home/xiao/program/benchmarks/java/soot-c/classes:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar ca.mcgill.sable.soot.jimple.Main";

		String run_sable_1_4 = "-w -cp /home/xiao/program/benchmarks/java/sablecc-j/classes:/opt/jdk1.4.0/jre/lib/rt.jar:/opt/jdk1.4.0/jre/lib/jce.jar ca.mcgill.sable.sablecc.SableCC";
		
		String run_sable_1_5 = "-w -cp /home/xiao/program/benchmarks/java/sablecc-j/classes:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar ca.mcgill.sable.sablecc.SableCC";

		String run_antlr = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar antlr.Tool";
		
		String run_antlr_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/opt/jdk1.4.0/jre/lib/rt.jar antlr.Tool";
		
		String run_antlr_1_5 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar antlr.Tool";
		
		String run_antlr_1_6 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/antlr/classfiles:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar antlr.Tool";
		
		String run_bloat = "-w -cp /home/xiao/workspace/bloat/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_bloat_1_4 = "-w -cp /home/xiao/workspace/bloat/bin:/opt/jdk1.4.0/jre/lib/rt.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_bloat_1_6 = "-w -cp /home/xiao/workspace/bloat/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar EDU.purdue.cs.bloat.tools.BloatBenchmark";
		
		String run_ps = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/ps/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar edu.unm.cs.oal.DaCapo.JavaPostScript.Red.executive";
		
		String run_ps_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/ps/classfiles:/opt/jdk1.4.0/jre/lib/rt.jar edu.unm.cs.oal.DaCapo.JavaPostScript.Red.executive";
		
		String run_jython = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/jython/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar org.python.util.jython";
		
		String run_jython_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/jython/classfiles:/opt/jdk1.4.0/jre/lib/rt.jar org.python.util.jython";
		
		String run_jython_1_6 = "-w -cp /home/xiao/workspace/jython/bin:" + JythonFiles + ":/home/xiao/library/jline-1.0.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar JythonMainHarness";
		
		String run_pmd = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/pmd/classfiles:/home/xiao/program/benchmarks/java/dacapo_20050224/common/classfiles:/opt/jdk1.3.1_20/jre/lib/rt.jar net.sourceforge.pmd.PMD";
		
		String run_pmd_1_4 = "-w -cp /home/xiao/program/benchmarks/java/dacapo_20050224/pmd/classfiles:/home/xiao/program/benchmarks/java/dacapo_20050224/common/classfiles:/opt/jdk1.4.0/jre/lib/rt.jar net.sourceforge.pmd.PMD";
		
		String run_lucene_1_5 = "-w -cp /home/xiao/workspace/lucene/bin:/home/xiao/library/commons-cli-1.2.jar:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar LuIndexMainHarness";
		
		String run_jedit = "-w -cp /home/xiao/workspace/jedit/bin:/opt/jdk1.3.1_20/jre/lib/rt.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_4 = "-w -cp /home/xiao/workspace/jedit/bin:/opt/jdk1.4.0/jre/lib/rt.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_5 = "-w -cp /home/xiao/workspace/jedit4/bin:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_6 = "-w -cp /home/xiao/workspace/jedit4/bin:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar org.gjt.sp.jedit.jEdit";
		
		String run_jedit_1_7 = "-w -cp /home/xiao/workspace/jedit4/bin:/opt/jdk1.7.0/jre/lib/rt.jar:/opt/jdk1.7.0/jre/lib/jce.jar org.gjt.sp.jedit.jEdit";
		
		String run_megamek = "-w -cp /home/xiao/workspace/MegaMek/bin:/home/xiao/program/benchmarks/java/MegaMek/lib/TinyXML.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/TabPanel.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/PngEncoder.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/Ostermiller.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/collections.jar:/opt/jdk1.3.1_20/jre/lib/rt.jar megamek.MegaMek";
		
		String run_megamek_1_4 = "-w -cp /home/xiao/workspace/MegaMek/bin:/home/xiao/program/benchmarks/java/MegaMek/lib/TinyXML.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/TabPanel.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/PngEncoder.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/Ostermiller.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/collections.jar:/opt/jdk1.4.0/jre/lib/rt.jar:/opt/jdk1.4.0/jre/lib/jce.jar megamek.MegaMek";

		String run_megamek_1_5 = "-w -cp /home/xiao/workspace/MegaMek/bin:/home/xiao/program/benchmarks/java/MegaMek/lib/TinyXML.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/TabPanel.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/PngEncoder.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/Ostermiller.jar:/home/xiao/program/benchmarks/java/MegaMek/lib/collections.jar:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar megamek.MegaMek";
		
		String run_vuze_1_6 = "-w -cp /home/xiao/workspace/Vuze/bin:/home/xiao/library/swt.jar:/home/xiao/library/commons-cli-1.2.jar:/home/xiao/library/log4j-1.2.16.jar:/home/xiao/workspace/myExpSoot/libs/junit-4.8.1.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar org.gudy.azureus2.ui.swt.Main";
		
		String run_jfreechart_1_7 = "-w -cp /home/xiao/workspace/jfreechart/bin:/home/xiao/library/jcommon-1.0.16.jar:/home/xiao/library/servlet.jar:/opt/jdk1.7.0/jre/lib/rt.jar:/opt/jdk1.7.0/jre/lib/jce.jar org.jfree.chart.JFreeChart";
		
		String run_ant_1_5 = "-w -cp /home/xiao/workspace/ant/bin:" + CommonLibFiles + ":" + LibXalan + ":/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar:/opt/jdk1.5.0_22/jre/lib/jsse.jar org.apache.tools.ant.Main";
		
		String run_jspider_1_5 = "-w -cp /home/xiao/workspace/jspider/bin:/home/xiao/library/log4j-1.2.16.jar:/home/xiao/library/velocity-dep-1.3.1.jar:/home/xiao/library/commons-cli-1.2.jar:/home/xiao/workspace/soot-2.4.0/libs/ant-commons-logging.jar:/home/xiao/workspace/soot-2.4.0/libs/junit-4.8.1.jar:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar net.javacoding.jspider.JSpider";
		
		String run_tomcat_1_5 = "-w -cp /home/xiao/workspace/tomcat-6.0.x/bin:/home/xiao/workspace/tomcat-6.0.x/lib/httpcore-4.0-beta3.jar:/home/xiao/workspace/tomcat-6.0.x/lib/httpclient-4.0.1.jar:/home/xiao/workspace/tomcat-6.0.x/lib/httpmime-4.0.1.jar:/home/xiao/workspace/tomcat-6.0.x/lib/ant-1.7.1.jar:/home/xiao/workspace/tomcat-6.0.x/lib/commons-logging-1.1.1.jar:/home/xiao/workspace/tomcat-6.0.x/lib/commons-codec-1.3.jar:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar:/opt/jdk1.5.0_22/jre/lib/jsse.jar driver.MainClient";
		
		String run_derby_1_5 = "-w -cp /home/xiao/workspace/derby-10.3.2.1/build:/home/xiao/workspace/derby-10.3.2.1/lib/commons-io-1.1.jar:/home/xiao/workspace/derby-10.3.2.1/lib/serializer-2.7.0.jar:/home/xiao/workspace/derby-10.3.2.1/lib/xalan-2.7.0.jar:/home/xiao/workspace/derby-10.3.2.1/lib/xercesImpl-2.7.1.jar:/home/xiao/workspace/derby-10.3.2.1/lib/commons-logging-1.1.1.jar:/opt/jdk1.5.0_22/jre/lib/rt.jar:/opt/jdk1.5.0_22/jre/lib/jce.jar derby2861.Derby2861";
		
		// Dacapo 2006 benchmarks
		String run_antlr_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/antlr.jar:/home/xiao/program/benchmarks/java/dacapo-2006/antlr-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.antlr.Main";
		
		String run_bloat_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/bloat.jar:/home/xiao/program/benchmarks/java/dacapo-2006/bloat-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.bloat.Main";
		
		String run_jython_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/jython.jar:/home/xiao/program/benchmarks/java/dacapo-2006/jython-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.jython.Main";
		
		String run_luindex_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/luindex.jar:/home/xiao/program/benchmarks/java/dacapo-2006/luindex-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.luindex.Main";
		
		String run_pmd_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/pmd.jar:/home/xiao/program/benchmarks/java/dacapo-2006/pmd-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.pmd.Main";
		
		String run_xalan_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/xalan.jar:/home/xiao/program/benchmarks/java/dacapo-2006/xalan-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.xalan.Main";
		
		String run_chart_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/chart.jar:/home/xiao/program/benchmarks/java/dacapo-2006/chart-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.chart.Main";
		
		String run_eclipse_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/eclipse.jar:/home/xiao/program/benchmarks/java/dacapo-2006/eclipse-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.eclipse.Main";
		
		String run_hsqldb_2006 = "-w -cp /home/xiao/program/benchmarks/java/dacapo-2006/hsqldb.jar:/home/xiao/program/benchmarks/java/dacapo-2006/hsqldb-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar dacapo.hsqldb.Main";
		
		
		
		// The following benchmarks are produced by tamiflex
		String run_xalan_default = "-w -p cg reflection-log:/home/xiao/program/benchmarks/java/tamiflex/out/xalan-default/refl.log -cp /home/xiao/program/benchmarks/java/tamiflex/out/xalan-default:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/xalan.jar:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/xml-apis.jar:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/serializer.jar:/home/xiao/library/xalan-j_2_7_1/xercesImpl.jar:/home/xiao/library/commons-cli-1.2.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar -main-class Harness Harness";
		
		String run_avrora_default = "-w -p cg jdkver:6 -p cg reflection-log:/home/xiao/program/benchmarks/java/tamiflex/out/avrora-default/refl.log -cp /home/xiao/program/benchmarks/java/tamiflex/out/avrora-default:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/avrora-cvs-20091224.jar:/home/xiao/library/commons-cli-1.2.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar -main-class Harness Harness";
		
		String run_luindex_default = "-w -p cg reflection-log:/home/xiao/program/benchmarks/java/tamiflex/out/luindex-default/refl.log -cp /home/xiao/program/benchmarks/java/tamiflex/out/luindex-default:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/lucene-core-2.4.jar:/home/xiao/program/benchmarks/java/tamiflex/dacapo-9.12/jar/luindex.jar:/home/xiao/library/commons-cli-1.2.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar -main-class Harness Harness";
		
		String run_rongxin_antlr = "-w -allow-phantom-refs -p cg reflection-log:/home/xiao/program/benchmarks/antlr/out/refl.log -cp /home/xiao/program/benchmarks/antlr/out:/home/xiao/program/benchmarks/java/dacapo-2006/antlr-deps.jar:/opt/jdk1.6.0_21/jre/lib/rt.jar:/opt/jdk1.6.0_21/jre/lib/jce.jar:/opt/jdk1.6.0_21/jre/lib/jsse.jar -main-class Harness Harness";
		
//		test_cases.add( new benchmark(run_test1, "Test1") );
//		test_cases.add( new benchmark(run_multitest, "multitest") );
//		test_cases.add( new benchmark(run_compress, "compress") );
//		test_cases.add( new benchmark(run_compress_1_4, "compress") );
//		test_cases.add( new benchmark(run_compress_1_6, "compress") );
//		test_cases.add( new benchmark(run_db, "db") );
//		test_cases.add( new benchmark(run_db_1_6, "db") );
//		test_cases.add( new benchmark(run_jess, "jess") );
//		test_cases.add( new benchmark(run_mtrt, "mtrt") );
//		test_cases.add( new benchmark(run_jflex, "jflex") );
//		test_cases.add( new benchmark(run_jflex_1_4, "jflex") );
//		test_cases.add( new benchmark(run_jflex_1_6, "jflex") );
//		test_cases.add( new benchmark(run_soot_1_4, "soot") );
//		test_cases.add( new benchmark(run_sable_1_4, "sable") );
//		test_cases.add( new benchmark(run_sable_1_5, "sable") );
//		test_cases.add( new benchmark(run_jetty, "jetty") );
//		test_cases.add( new benchmark(run_polyglot, "polyglot") );
//		test_cases.add( new benchmark(run_jasmin, "jasmin") );
//		test_cases.add( new benchmark(run_jlex, "jlex") );
//		test_cases.add( new benchmark(run_javacup, "javacup") );
//		test_cases.add( new benchmark(run_javacc, "javacc") );
//		test_cases.add( new benchmark(run_j2ssh, "j2ssh") );
//		test_cases.add( new benchmark(run_antlr, "antlr") );
//		test_cases.add( new benchmark(run_antlr_1_4, "antlr") );
//		test_cases.add( new benchmark(run_antlr_1_6, "antlr") );
//		test_cases.add( new benchmark(run_bloat, "bloat") );
//		test_cases.add( new benchmark(run_bloat_1_4, "bloat") );
//		test_cases.add( new benchmark(run_bloat_1_6, "bloat") );
//		test_cases.add( new benchmark(run_ps, "ps") );
//		test_cases.add( new benchmark(run_ps_1_4, "ps") );
//		test_cases.add( new benchmark(run_pmd_1_4, "pmd") );
//		test_cases.add( new benchmark(run_pmd, "pmd") );
//		test_cases.add( new benchmark(run_jython, "jython") );
//		test_cases.add( new benchmark(run_jython_1_4, "jython") );
//		test_cases.add( new benchmark(run_jedit, "jedit") );
//		test_cases.add( new benchmark(run_jedit_1_6, "jedit3") );
//		test_cases.add( new benchmark(run_megamek, "megamek") );
//		test_cases.add( new benchmark(run_megamek_1_4, "megamek") );
//		test_cases.add( new benchmark(run_megamek_1_5, "megamek") );
//		test_cases.add( new benchmark(run_vuze_1_6, "vuze") );
//		test_cases.add( new benchmark(run_jfreechart_1_7, "jfreechart") );
//		test_cases.add( new benchmark(run_ant, "ant") );
//		test_cases.add( new benchmark(run_avrora_1_5, "avrora") );
//		test_cases.add( new benchmark(run_lucene_1_5, "lucene") );
//		test_cases.add( new benchmark(run_xalan_1_6, "xalan") );
//		test_cases.add( new benchmark(run_jspider_1_5, "jspider") );
		
		
//		test_cases.add( new benchmark(run_antlr_2006, "antlr_2006") );
//		test_cases.add( new benchmark(run_bloat_2006, "bloat_2006") );
//		test_cases.add( new benchmark(run_jython_2006, "jython_2006") );
//		test_cases.add( new benchmark(run_luindex_2006, "luindex_2006") );
//		test_cases.add( new benchmark(run_pmd_2006, "pmd_2006") );
//		test_cases.add( new benchmark(run_xalan_2006, "xalan_2006") );
//		test_cases.add( new benchmark(run_chart_2006, "chart_2006") );
//		test_cases.add( new benchmark(run_eclipse_2006, "eclipse_2006") );
//		test_cases.add( new benchmark(run_hsqldb_2006, "hsqldb_2006") );
//		test_cases.add( new benchmark(run_jedit_1_4, "jedit1") );
//		test_cases.add( new benchmark(run_megamek_1_4, "megamek") );
//		test_cases.add( new benchmark(run_jedit_1_5, "jedit2") );
//		test_cases.add( new benchmark(run_batik_1_5, "batik") );
//		test_cases.add( new benchmark(run_tomcat_1_5, "tomcat") );
//		test_cases.add( new benchmark(run_derby_1_5, "derby") );
		
//		test_cases.add( new benchmark(run_xalan_default, "xalan-default") );
//		test_cases.add( new benchmark(run_avrora_default, "avrora-huge") );
//		test_cases.add( new benchmark(run_luindex_default, "luindex_default") );
		
		test_cases.add( new benchmark(run_rongxin_antlr, "antlr_2006") );
		
		boolean isDump = false;

		Vector<String> opts = new Vector<String>();
		
		for ( int i = 0; i < test_cases.size(); ++i ) {
			benchmark tc = test_cases.get(i);
			String[] other_opts = tc.bench_name.split(" ");
			opts.clear();
			
			setGlobalOptions(opts);
			setPhaseOptionsForSparkWork(opts);
			
			for ( int j = 0; j < other_opts.length; ++j ) 
				opts.add(other_opts[j]);
			
			if ( isDump ) {
				opts.add("-p");
				opts.add("cg.spark");
				opts.add("geom-dump-verbose:" + "dump/" + tc.file_name);
				Pack wjtp = PackManager.v().getPack("wjtp");
				wjtp.add(new Transform("wjtp.dpts", new dumpTransformer()));
			}
			
			Pack wjtp = PackManager.v().getPack("wjtp");
//			wjtp.add(new Transform("wjtp.muvi", new TestQueryByAnyEdge()));
//			wjtp.add(new Transform("wjtp.muvi", new CallBackTester()));
//			wjtp.add(new Transform("wjtp.muvi", new AliasTester()));
			wjtp.add(new Transform("wjtp.muvi", new HelloTester()));
			
			// We start a new thread to run soot
			Main.main( opts.toArray(new String[0]) );
		}
	}
}

class benchmark
{
	String bench_name;
	String file_name;
	
	public benchmark( String bn, String fn ) 
	{
		bench_name = bn;
		file_name = fn;
	}
}

