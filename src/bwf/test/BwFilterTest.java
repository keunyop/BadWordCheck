package bwf.test;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import bwf.core.BwFilter;

class BwFilterTest {

	@Test
	void testScan() {
		BwFilter bwf = new BwFilter();
//		assertEquals("", bwf.scan("�����ٶ������ٻ簡���ٶ�� ����ٻ簡���ٶ���� ��ٻ簡���ٶ�� �� ��ٻ�"));
		
		assertEquals("������|���� ��|�� ����|�� �� ��", bwf._makePatternSet("���������", null, null));
	}
}