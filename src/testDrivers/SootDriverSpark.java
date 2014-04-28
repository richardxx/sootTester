package testDrivers;

import java.io.File;
import java.io.FileNotFoundException;
import soot.Main;

public class SootDriverSpark
{
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
		opts[offset+29] = "ignore-types:false";
		
		opts[offset+30] = "-p";
		opts[offset+31] = "cg.spark";
		opts[offset+32] = "simple-edges-bidirectional:false";
	
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
		String[] opts = new String[80];
		String[] other_opts = null;
		int offset;
		
		File pars = new File("parameters.txt");
		java.util.Scanner inp = new java.util.Scanner(pars);
		other_opts = inp.nextLine().split(" ");
		
			
		for (int j = 0; j < opts.length; ++j)
			opts[j] = null;

		offset = setGlobalOptions(opts);
		offset = setPhaseOptionsForSparkWork(opts, offset);

		for (int j = 1; j < other_opts.length; ++j)
			opts[offset + j - 1] = other_opts[j];

		Main.main(opts);
	}
}

