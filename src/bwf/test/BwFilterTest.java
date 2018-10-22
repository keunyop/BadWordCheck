package bwf.test;

import java.util.Set;

import org.junit.jupiter.api.Test;

import bwf.core.BwFilter;

class BwFilterTest {

	@Test
	void testScan() {
		BwFilter bwf = new BwFilter();
		

		Set<String> f2 =  bwf._makePatternSet("금지", null, null);
		Set<String> f3 =  bwf._makePatternSet("금지어", null, null);
		Set<String> f4 =  bwf._makePatternSet("금지어다", null, null);
		Set<String> f5 =  bwf._makePatternSet("금지어랑께", null, null);
		Set<String> f6 =  bwf._makePatternSet("금지어랑께롱", null, null);
		
		for (String fw : f2) System.out.println(fw);
		System.out.println("===============");
		for (String fw : f3) System.out.println(fw);
		System.out.println("===============");
		for (String fw : f4) System.out.println(fw);
		System.out.println("===============");
		for (String fw : f5) System.out.println(fw);
		System.out.println("===============");
		for (String fw : f6) System.out.println(fw);
		
		
	}
}