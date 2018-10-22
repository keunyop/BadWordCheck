package bwf.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.platform.commons.util.StringUtils;

public class BwFilter {
	
	private String[] forbiddenWords = {"�ý�", "����", "�߻�", "�ǻ�", "����", "����", "�ƴ�", "�ð�", "����", "����", "����", "ȯ��", "����", "����", "����",
			"���", "����", "�ü�", "����", "���", "����", "�浿", "��ȸ", "���", "�γ�", "����", "�ڻ�", "����", "��ȯ", "������", "�Ĵ�", "�Ϲ�", "��", "�ֳ�", "����",
			"����", "����", "���", "����", "����", "����", "�׿�", "�ϴ���", "ǰ��", "����", "����", "�Ǿ�", "�ǹ�", "�ǽ�", "����", "����", "����", "�Ǹ�", "����", 
			"��������", "�䰡", "ü��", "�", "Ż��"};
	
	public String scan(String inStr) {
		
		List<String> results = new ArrayList<>();
		
		for (String fWord : forbiddenWords) {
			if (inStr.trim().contains(fWord)) {
				results.add(fWord);
			}
		}
		
		return results.toString();
	}
	
	// Recursive�� �α��� �̻��̸� ���
	public Set<String> _makePatternSet(String fWord, String prefix, String postfix) {
		Set<String> patternSet = new HashSet<>();
		
		/**
		 * ������ ��ü
		 * &
		 * ������ ��ü �پ��
		 */
		if (prefix == null && postfix == null) {
			patternSet.add(fWord);
			
			StringBuilder allSpaceBuilder = new StringBuilder();
			for (char c : fWord.toCharArray()) {
				allSpaceBuilder.append(c);
				allSpaceBuilder.append(" ");
			} 
			String allSpaceStr = allSpaceBuilder.toString();
			patternSet.add(allSpaceStr.substring(0, allSpaceStr.lastIndexOf(' ')));
		}

		/**
		 * ������ �׿� ���� �� �ִ� ��� ���̽�
		 */
		if (fWord.length() > 1) {
			for(int i=1; i<fWord.length(); i++) {
				StringBuilder patternBuilder = new StringBuilder();
				if (!StringUtils.isBlank(prefix)) {
					patternBuilder.append(prefix);	
				}
				
				int cnt=1;
				for (char c : fWord.toCharArray()) {
					patternBuilder.append(c);
					
					if (cnt==i) {
						patternBuilder.append(" ");
					}
					
					cnt++;
				}
				
				if (!StringUtils.isBlank(postfix)) {
					patternBuilder.append(postfix);
				}
				
				String pattern = patternBuilder.toString();
				
				patternSet.add(pattern);
				
				String[] wordGroup = pattern.split(" ");
				
				for (int j=0; j<wordGroup.length; j++) {
					
					if (wordGroup[j].length() > 2) {
						StringBuilder prefixBuilder = new StringBuilder();
						if (j!=0) {
							for (int k=0; k<j; k++) {
								prefixBuilder.append(wordGroup[k]);
								prefixBuilder.append(" ");
							}
						}
						
						StringBuilder postfixBuilder = new StringBuilder();
						if (j != wordGroup.length-1) {
							for (int k=j+1; k<=wordGroup.length-1; k++) {
								postfixBuilder.append(" ");
								postfixBuilder.append(wordGroup[k]);
							}
						}
						
						patternSet.addAll(_makePatternSet(wordGroup[j], prefixBuilder.toString(), postfixBuilder.toString()));
					}
				}
			}
		}		
		
		return patternSet;
	}
}