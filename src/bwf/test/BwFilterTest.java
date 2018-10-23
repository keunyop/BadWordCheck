package bwf.test;

import org.junit.jupiter.api.Test;

import bwf.core.BwFilter;

class BwFilterTest {

	@Test
	void testScan() {
		BwFilter bwf = new BwFilter();
		
		try {
			bwf.scan("C:\\Users\\kylee\\Desktop\\temp.txt");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}