package bwf.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BwFilter {
	
	private String[] forbiddenWords = {"������"};
	
	// (������|���� ��|�� ����|�� �� ��)
	
	public String scan(String inStr) {
		
		List<String> results = new ArrayList<>();
		
		for (String fWord : forbiddenWords) {
			if (inStr.trim().contains(fWord)) {
				results.add(fWord);
			}
		}
		
		System.out.println(results.size());
		
		return results.toString();
	}
	
	// Recursive�� �ѱ��� �̻��̸� ���
	public List<String> _makePatternList(String fWord) {
		
		List<String> patternList = new ArrayList<>();
		patternList.add(fWord);
		
		for(int i=0; i<fWord.length(); i++) {
			for (char c : fWord.toCharArray()) {
				
			}
		}
		
		
		
		
		
		
		return patternList;
	}
}
