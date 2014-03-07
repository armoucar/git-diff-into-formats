package com.arthurcarvalho;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length < 3) {
			System.out.println("Use: java -jar gitdiff.jar [git-directory] [oldBranch] [newBranch]");
			System.exit(-1);
		}
		
		String directory = ".".equals(args[0]) ? System.getProperty("user.dir") : args[0];
		
		String oldBranch = args[1];
		String newBranch = args[2];
		
		Runtime rt = Runtime.getRuntime();
		Process pt = rt.exec("/usr/local/bin/git --git-dir=" + directory + " diff " + oldBranch + " " + newBranch);
		pt.waitFor();
		
		String error = convertStreamToString(pt.getErrorStream());
		if (!"".equals(error)) {
			System.out.println(error);
			System.exit(-1);
		}
		
		InputStream out = pt.getInputStream();
		String gitDiffOutput = convertStreamToString(out);
		
		List<DiffPair> pairs = DiffPair.convert(gitDiffOutput);
		for (DiffPair diffPair : pairs) {
			System.out.println("<p>" + diffPair.htmlDiff() + "</p>");
		}
	}
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
	    return s.hasNext() ? s.next() : "";
	}
}