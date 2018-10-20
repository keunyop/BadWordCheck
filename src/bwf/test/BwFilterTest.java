package bwf.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bwf.core.BwFilter;

class BwFilterTest {

	@Test
	void testScan() {
		BwFilter bwf = new BwFilter();
//		assertEquals("", bwf.scan("가나다라금지어마바사가나다라금 지어마바사가나다라금지 어마바사가나다라금 지 어마바사"));
		
		assertEquals("금지어|금지 어|금 지어|금 지 어", bwf._makePatternSet("금지어랑께", null, null));
	}
}