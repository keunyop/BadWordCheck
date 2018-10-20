package bwf.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.platform.commons.util.StringUtils;

public class BwFilter {
	
	private String[] forbiddenWords = {"금지어"};
	
	// (금지어|금지 어|금 지어|금 지 어)
	
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
	
	// Recursive로 두글자 이상이면 재귀
	public Set<String> _makePatternSet(String fWord, String prefix, String postfix) {
		
		List<String> patternList = new ArrayList<>();
		
		Set<String> patternSet = new HashSet<>();
		
		if (prefix == null && postfix == null) {
//			patternList.add(fWord);
			patternSet.add(fWord);
		}

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
				
//				patternList.add(pattern);
				patternSet.add(pattern);
				
				String[] wordGroup = pattern.split(" ");
				
				for (int j=0; j<wordGroup.length; j++) {
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
					
//						System.out.println("### prefixBuilder: " + prefixBuilder.toString());
//						System.out.println("### postfixBuilder: " + postfixBuilder.toString());
					
//					patternList.addAll(_makePatternList(wordGroup[j], prefixBuilder.toString(), postfixBuilder.toString()));
					patternSet.addAll(_makePatternSet(wordGroup[j], prefixBuilder.toString(), postfixBuilder.toString()));
				}
			}
		}

		
		for (String string : patternSet) {
			System.out.println(string);
		}
		
		return patternSet;
	}
}
