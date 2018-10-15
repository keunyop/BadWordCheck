package bwf.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bwf.core.BwFilter;

class BwFilterTest {

	@Test
	void testScan() {
		BwFilter bwf = new BwFilter();
		assertEquals("test", bwf.scan(""));
	}
}