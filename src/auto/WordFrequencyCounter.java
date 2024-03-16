package auto;

import java.util.*;

public class WordFrequencyCounter {
	
    public void wordFreqCounter(String word) {
//        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> wordFrequency = new HashMap<>();
        boolean acceptingInput = false;

//        System.out.println("Press 1 to start entering words");
        while (true) {
            String userInput = word;
            if (userInput.equals("1")) {
                acceptingInput = true;
                System.out.println("Enter words (press 8 to show frequency):");
            } else if (userInput.equals("8")) {
                System.out.println("Word frequency:");
                for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                break;
            } else if (acceptingInput) {
                String inputWord = userInput.trim();
                wordFrequency.put(inputWord, wordFrequency.getOrDefault(inputWord, 0) + 1);
            }
        }
    }
}
