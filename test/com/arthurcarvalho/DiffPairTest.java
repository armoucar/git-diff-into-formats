package com.arthurcarvalho;

import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class DiffPairTest {
	
	String SIMPLE_DIFF = readFile(DiffPair.class.getClassLoader().getResource("simple-diff").getFile());
	String MEDIUM_DIFF = readFile(DiffPair.class.getClassLoader().getResource("medium-diff").getFile());
	
	@Test
	public void emptyDiffPairs() throws Exception {
		List<DiffPair> dp = DiffPair.convert("");
		
		assertTrue(dp.isEmpty());
	}
	
	@Test
	public void oneDiff() throws Exception {
		List<DiffPair> dps = DiffPair.convert(SIMPLE_DIFF);
		
		assertTrue(!dps.isEmpty());
		assertTrue(dps.size() == 1);
		
		DiffPair firstDp = dps.get(0);
		
		assertTrue(firstDp.getOldString() != null && !"".equals(firstDp.getOldString()));
	}
	
	@Test
	public void fourDiffs() throws Exception {
		List<DiffPair> dps = DiffPair.convert(MEDIUM_DIFF);
		
		assertTrue(!dps.isEmpty());
		assertTrue(dps.size() == 4);
		
	}
	
	static String readFile(String fileName) {
		String content = null;
		
		try {
			content = new Scanner(new File(fileName)).useDelimiter("\\Z").next();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return content;
	}
}