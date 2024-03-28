package auto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AutoWebCrawler {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        WebCrawler crawler = new WebCrawler(15, "pages");
        SpellChecker spellChecker = new SpellChecker();
        FrequencyCount freqCount = new FrequencyCount();
        InvertedIndex index = new InvertedIndex();
        PageRanking pageRanking = new PageRanking();
        SpellSuggestion spellSuggestion = new SpellSuggestion("pages");
        CarPriceScrapper priceScrapper = new CarPriceScrapper();
        CarMpgScrapper mpgScrapper = new CarMpgScrapper();

        System.out.println("Welcome to the Automated Web Crawler and Analysis Tool!");

        // Step 1: Take Website Input
        System.out.println("\nPlease enter a website URL to crawl:");
        String websiteUrl = scanner.nextLine();
// Remove trailing slash if present
        if (websiteUrl.endsWith("/")) {
            websiteUrl = websiteUrl.substring(0, websiteUrl.length() - 1);
        }
        // Step 2: Crawl the Website
        System.out.println("Crawling website: " + websiteUrl);
        crawler.crawl(websiteUrl, "pages");
        System.out.println("Website crawled successfully!");

        // Step 3: Enter a word and Perform Operations
        while (true) {
            System.out.println("\nEnter a word to perform operations (or type 'exit' to quit): ");
            String word = scanner.nextLine().trim();

            if (word.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!");
                break;
            }
            System.out.println("Based on the word you entered, we can offer the following functions for you to explore and analyze:");
            Thread.sleep(2000);

            // Spell suggestion
            System.out.println("\nSpell Suggestions:");
            String userAcceptance = "";
            String suggestion = spellSuggestion.suggestWord(word);
            if (suggestion.isEmpty()) {
                System.out.println("No suggestions found for " + word + "'.");
            } else {
                System.out.println("Did you mean the word " + suggestion + " ?  (yes/no)");
                String userResponse = scanner.nextLine().trim();
                if (userResponse.equalsIgnoreCase("no")) {
                    System.out.println("Enter a new word for the operations");
                    word = scanner.nextLine().trim();

                    System.out.println("Your new word is: " + word + ". Do you want to perform other operations on that word? (yes/no)");
                    userAcceptance = scanner.nextLine().trim();
                }
            }
            Thread.sleep(2000);
if(userAcceptance.equalsIgnoreCase("no")){break;}

                // Spell checking

                System.out.println("\nSpell Checking for your word :");
                String spellChecked = spellChecker.spellCheck(word);
                System.out.println("Spell checked string for " + word + " is : " + spellChecked);
                Thread.sleep(3000);


                // Frequency count
                System.out.println("\nFrequency Count for word + " + word + ": ");
                freqCount.countFrequency("..//pages",word);
                Thread.sleep(5000);
                // Inverted indexing
                System.out.println("\nInverted Indexing for word " + word + " : ");
                index.buildIndex("pages");
                index.searchKeyword(word);
                Thread.sleep(5000);
                // Page ranking
                System.out.println("\nPage Ranking for word " + word + " : ");
                pageRanking.PageRank("pages", word);
                Thread.sleep(2000);
            }
        }

}
