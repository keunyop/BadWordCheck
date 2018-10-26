package bwf.core;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class BwFilter {

    final String[] FORBIDDEN_WORDS = { "시신", "거지", "야사", "의사", "자지", "보지", "아다", "씹고", "음탕", "후장", "병원", "환자", "진단",
            "증상", "증세", "재발", "방지", "시술", "본원", "상담", "고자", "충동", "후회", "고비", "인내", "참아", "자살", "음부", "고환", "오빠가", "후다",
            "니미", "애널", "에널", "해적", "몰래", "재생", "유발", "만족", "무시", "네요", "하더라", "품절", "매진", "마감", "의아", "의문", "의심", "가격",
            "정가", "구매", "판매", "매입", "지저분함", "요가", "체형", "등빨", "탈출" };

    public static void main(String... args) {
        BwFilter bwf = new BwFilter();

        try {
            bwf.scan("C:\\Users\\kylee\\Desktop\\BolgWordFilter\\post.txt");
            //			bwf.scan("post.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Prevent output from closing
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }

    public void scan(String fileName) throws IOException {
        Map<String, Integer> violatedWords = new HashMap<>(); // 금칙어 목록
        Map<String, Integer> keyWords = new HashMap<>(); // 키워드 목록

        for (String textLine : _readFile(fileName)) {
            // 금칙어 검사
            for (String fWord : FORBIDDEN_WORDS) {
                // remove all white space
                if (textLine.replaceAll("\\s", "").contains(fWord)) {
                    Set<String> subFWords = _makeSubPatterns(fWord, null, null);

                    for (String subFWord : subFWords) {
                        if (textLine.contains(subFWord)) {
                            int cnt = violatedWords.containsKey(subFWord) ? violatedWords.get(subFWord) : 0;
                            violatedWords.put(subFWord, ++cnt);
                        }
                    }
                }
            }

            // 키워드 검사
            for (String word : textLine.split("\\s")) {
                if (!word.isEmpty() && word.length() > 1) {
                    int cnt = keyWords.containsKey(word) ? keyWords.get(word) : 0;
                    keyWords.put(word, ++cnt);
                }
            }
        }

        // 결과출력
        _printOutput(violatedWords, keyWords, _sortByValue(keyWords));
    }

    /**
     * Read from file
     */
    private List<String> _readFile(String fileName) throws IOException {
        Path path = Paths.get(fileName);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }

    /**
     * Recursive하게 다양한 금칙어 패턴 생성 
     */
    private Set<String> _makeSubPatterns(String fWord, String prefix, String postfix) {
        Set<String> patternSet = new HashSet<>();

        /**
         * 금지어 자체 & 금지어 전체 뛰어쓰기
         */
        if (prefix == null && postfix == null) {
            // 금지어 차제
            patternSet.add(fWord);

            // 금지어 전체 뛰어쓰기
            patternSet.add(_appendSpaces(fWord));
        }

        /**
         * 금지어 그외 나올 수 있는 모든 케이스
         */
        if (fWord.length() > 1) {
            for (int i = 1; i < fWord.length(); i++) {
                StringBuilder patternBuilder = new StringBuilder();

                if (prefix != null && !prefix.isEmpty()) {
                    patternBuilder.append(prefix);
                }

                int cnt = 1;
                for (char c : fWord.toCharArray()) {
                    patternBuilder.append(c);

                    if (cnt == i) {
                        patternBuilder.append(" ");
                    }

                    cnt++;
                }

                if (postfix != null && !postfix.isEmpty()) {
                    patternBuilder.append(postfix);
                }

                String pattern = patternBuilder.toString();

                patternSet.add(pattern);

                String[] wordGroup = pattern.split(" ");

                for (int j = 0; j < wordGroup.length; j++) {

                    if (wordGroup[j].length() > 2) {
                        StringBuilder prefixBuilder = new StringBuilder();
                        if (j != 0) {
                            for (int k = 0; k < j; k++) {
                                prefixBuilder.append(wordGroup[k]);
                                prefixBuilder.append(" ");
                            }
                        }

                        StringBuilder postfixBuilder = new StringBuilder();
                        if (j != wordGroup.length - 1) {
                            for (int k = j + 1; k <= wordGroup.length - 1; k++) {
                                postfixBuilder.append(" ");
                                postfixBuilder.append(wordGroup[k]);
                            }
                        }

                        patternSet.addAll(
                                _makeSubPatterns(wordGroup[j], prefixBuilder.toString(), postfixBuilder.toString()));
                    }
                }
            }
        }

        return patternSet;
    }

    /**
     * 각 글자에 뛰어쓰기 추가 
     */
    private String _appendSpaces(String word) {
        StringBuilder allSpaceBuilder = new StringBuilder();

        int cnt = 1;
        for (char c : word.toCharArray()) {
            allSpaceBuilder.append(c);

            if (cnt < word.length()) {
                allSpaceBuilder.append(" ");
            }

            cnt++;
        }

        return allSpaceBuilder.toString();
    }

    /**
     * 정렬
     */
    private List<String> _sortByValue(Map<String, Integer> map) {
        List<String> list = new ArrayList<>();
        list.addAll(map.keySet());

        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return map.get(o2).compareTo(map.get(o1));
            }
        });

        return list;
    }

    /**
     * 결과 출력
     */
    private void _printOutput(Map<String, Integer> violatedWords, Map<String, Integer> keyWords,
                              List<String> keyWordList) {

        System.out.println("### 금지어 위반 목록 ###");
        for (Entry<String, Integer> entry : violatedWords.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("");
        System.out.println("### 키워드 목록 ###");
        for (String key : keyWordList) {
            if (keyWords.get(key) > 1) {
                System.out.println(key + ": " + keyWords.get(key));
            }
        }
    }
}