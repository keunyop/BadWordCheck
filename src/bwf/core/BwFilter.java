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

    final String[] FORBIDDEN_WORDS = { "�ý�", "����", "�߻�", "�ǻ�", "����", "����", "�ƴ�", "�ð�", "����", "����", "����", "ȯ��", "����",
            "����", "����", "���", "����", "�ü�", "����", "���", "����", "�浿", "��ȸ", "���", "�γ�", "����", "�ڻ�", "����", "��ȯ", "������", "�Ĵ�",
            "�Ϲ�", "�ֳ�", "����", "����", "����", "���", "����", "����", "����", "�׿�", "�ϴ���", "ǰ��", "����", "����", "�Ǿ�", "�ǹ�", "�ǽ�", "����",
            "����", "����", "�Ǹ�", "����", "��������", "�䰡", "ü��", "�", "Ż��" };

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
        Map<String, Integer> violatedWords = new HashMap<>(); // ��Ģ�� ���
        Map<String, Integer> keyWords = new HashMap<>(); // Ű���� ���

        for (String textLine : _readFile(fileName)) {
            // ��Ģ�� �˻�
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

            // Ű���� �˻�
            for (String word : textLine.split("\\s")) {
                if (!word.isEmpty() && word.length() > 1) {
                    int cnt = keyWords.containsKey(word) ? keyWords.get(word) : 0;
                    keyWords.put(word, ++cnt);
                }
            }
        }

        // ������
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
     * Recursive�ϰ� �پ��� ��Ģ�� ���� ���� 
     */
    private Set<String> _makeSubPatterns(String fWord, String prefix, String postfix) {
        Set<String> patternSet = new HashSet<>();

        /**
         * ������ ��ü & ������ ��ü �پ��
         */
        if (prefix == null && postfix == null) {
            // ������ ����
            patternSet.add(fWord);

            // ������ ��ü �پ��
            patternSet.add(_appendSpaces(fWord));
        }

        /**
         * ������ �׿� ���� �� �ִ� ��� ���̽�
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
     * �� ���ڿ� �پ�� �߰� 
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
     * ����
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
     * ��� ���
     */
    private void _printOutput(Map<String, Integer> violatedWords, Map<String, Integer> keyWords,
                              List<String> keyWordList) {

        System.out.println("### ������ ���� ��� ###");
        for (Entry<String, Integer> entry : violatedWords.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("");
        System.out.println("### Ű���� ��� ###");
        for (String key : keyWordList) {
            if (keyWords.get(key) > 1) {
                System.out.println(key + ": " + keyWords.get(key));
            }
        }
    }
}