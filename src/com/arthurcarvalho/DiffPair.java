package com.arthurcarvalho;

import static java.util.Collections.emptyList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import name.fraser.neil.plaintext.diff_match_patch;
import name.fraser.neil.plaintext.diff_match_patch.Diff;

public class DiffPair {

	private static final diff_match_patch DIFF_MATCH_PATCH = new diff_match_patch();
	private static final Pattern NEW_DIFF = Pattern.compile("(\\@\\@).*\\1");
	private static final Pattern OLD_STRING = Pattern.compile("^-.*");
	private static final Pattern NEW_STRING = Pattern.compile("^\\+.*");
	
	private String oldString;
	private String newString;

	private DiffPair() {}

	public static List<DiffPair> convert(String gitDiffOutput) {

		if (gitDiffOutput == null || "".equals(gitDiffOutput)) {
			return emptyList();
		}

		ArrayList<DiffPair> dps = new ArrayList<DiffPair>();

		String gitDiffOutputChangeable = gitDiffOutput;
		boolean stop = false;
		
		while (true) {
			Matcher matchNewDiff = NEW_DIFF.matcher(gitDiffOutputChangeable);
			
			if (stop) break;

			if (matchNewDiff.find()) {
				int startOfFirstChange = matchNewDiff.start();
				int endOfFirstChange = gitDiffOutputChangeable.length();
				
				if (matchNewDiff.find()) {
					endOfFirstChange = matchNewDiff.start();
				} else {
					
					// Stop on next iteration
					stop = true;
				}
				
				String isolatedCommit = gitDiffOutputChangeable.substring(startOfFirstChange, endOfFirstChange);
				
				List<String> commitLines = Arrays.asList(isolatedCommit.split("\\n"));
				
				DiffPair dp = new DiffPair();
				
				for (String commitLine : commitLines) {
					Matcher oldStringMatch = OLD_STRING.matcher(commitLine);
					Matcher newStringMatch = NEW_STRING.matcher(commitLine);
					
					if (oldStringMatch.find()) dp.setOldString(oldStringMatch.group());
					if (newStringMatch.find()) dp.setNewString(newStringMatch.group());
				}
				
				dps.add(dp);
				
				gitDiffOutputChangeable = gitDiffOutputChangeable.substring(endOfFirstChange);
				
			}
		}
		
		return dps;
	}

	public String getOldString() {
		return oldString;
	}

	public void setOldString(String oldString) {
		this.oldString = oldString.substring(1);
	}

	public String getNewString() {
		return newString.substring(1);
	}

	public void setNewString(String newString) {
		this.newString = newString;
	}

	@Override
	public String toString() {
		return "DiffPair [oldString=" + oldString + ", newString=" + newString + "]";
	}

	public String htmlDiff() {
		LinkedList<Diff> diffs = DIFF_MATCH_PATCH.diff_main(getOldString(), getNewString());
		DIFF_MATCH_PATCH.diff_cleanupSemantic(diffs);
		return DIFF_MATCH_PATCH.diff_prettyHtml(diffs);
	}
}