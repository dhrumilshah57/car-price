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
        String green="\u001B[32m";
        String skyBlue="\u001B[36m";
        String reset="\u001B[0m";
        Scanner scanner = new Scanner(System.in);
        WebCrawler crawler = new WebCrawler(15, "pages");
        SpellChecker spellChecker = new SpellChecker();
        FrequencyCount freqCount = new FrequencyCount();
        InvertedIndex index = new InvertedIndex();
        PageRanking pageRanking = new PageRanking();
        SpellSuggestion spellSuggestion = new SpellSuggestion("pages");
        CarPriceScrapper priceScrapper = new CarPriceScrapper();
        CarMpgScrapper mpgScrapper = new CarMpgScrapper();
        System.out.println();
        decorate( green + " Welcome to the Automated Web Crawler and Analysis Tool!" + reset);
System.out.println();
        // Step 1: Take Website Input
        System.out.println("\nPlease enter a website URL to crawl:");
        String websiteUrl = scanner.nextLine();
// Remove trailing slash if present
        if (websiteUrl.endsWith("/")) {
            websiteUrl = websiteUrl.substring(0, websiteUrl.length() - 1);
        }
        // Step 2: Crawl the Website
        System.out.println("Website crawling in process: " + websiteUrl);
        crawler.crawl(websiteUrl, "pages");
                decorate(green + " Website crawled successfully!" +reset);

        // Step 3: Enter a word and Perform Operations
        while (true) {
            System.out.println();
            decorateWithLine("Enter a word (model name, year of production, feature) to perform operations (or type 'exit' to quit): ");
            String word = scanner.nextLine().trim();

            if (word.equalsIgnoreCase("exit")) {
                decorateWithLine( green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!" + reset);
                break;
            }
            else if (!word.matches("[a-zA-Z0-9]+")) {
    System.err.println("Error: Input cannot contain special characters or symbols.");
    Thread.sleep(2000);
    continue; // Restart the loop to prompt for a new input
}
System.out.println();
            decorate( "Based on the word you entered, we can offer the following functions for you to explore and analyze:" );
            Thread.sleep(2000);

            // Spell suggestion
            System.out.println();
            System.out.println( skyBlue + "\nSpell Suggestions:" + reset);
            String userAcceptance = "";
            String suggestion = spellSuggestion.suggestWord(word);
            if (suggestion.isEmpty()) {
                System.err.println("No suggestions found for " + word + "'.");
            } else {
                System.out.println("Did you mean the word " + suggestion + " ?  (yes/no)");
                String userResponse = scanner.nextLine().trim();
                if (userResponse.equalsIgnoreCase("yes")) {
                   word=suggestion;
                }
                if (!userResponse.equalsIgnoreCase("no") && !userResponse.equalsIgnoreCase("yes")) {
                    System.out.println("Please enter either yes/no");
                    userResponse = scanner.nextLine().trim();
                    if(userResponse.equalsIgnoreCase("yes")){
                        word=suggestion;
                    }
                }

                while (userResponse.equalsIgnoreCase("no")) {
                    System.out.println(
                            "Enter a new word for the operations");
                    word = scanner.nextLine().trim();

                    System.out.println("Your new word is: " + word + ". Do you want to perform other operations on that word? (yes/no)");
                    userResponse = scanner.nextLine().trim();
                }
            }
            Thread.sleep(2000);


            // Spell checking

            System.out.println(skyBlue + "\nSpell Checking for your word :"+ reset);
            String spellChecked = spellChecker.spellCheck(word);
            System.out.println("Spell checked string for " + word + " is : " + spellChecked);
            Thread.sleep(3000);


            // Frequency count
            System.out.println(skyBlue + "\nFrequency Count for word + " + word + ": " + reset);
            freqCount.countFrequency("pages", word);
            Thread.sleep(5000);
            // Inverted indexing
            System.out.println(skyBlue+ "\nInverted Indexing for word " + word + " : " +reset);
            index.buildIndex("pages");
            index.searchKeyword(word);
            Thread.sleep(5000);
            // Page ranking
            System.out.println(skyBlue + "\nPage Ranking for word " + word + " : " + reset);
            pageRanking.PageRank("pages", word);
            Thread.sleep(2000);

            boolean isModelName = priceScrapper.checkIfModelName(word);
           ;
            // Offer to scrap car mpg data based on the word
            if (isModelName) {
                System.out.println(skyBlue + "\nWould you like to scrape car mileage based on the model " + word + "? (yes/no)" + reset);
                String scrapeChoice = scanner.nextLine().trim();
                if (scrapeChoice.equalsIgnoreCase("yes")) {
                    Scanner priceScanner = new Scanner(System.in);
                    System.out.print("Enter minimum mileage (mpg) of the car you want to scrap:");
                    int minMpg = priceScanner.nextInt();

                    System.out.println();

                    // Scrap car price data
                    mpgScrapper.scrapCarMpgData(word,minMpg );
                } else {
                    Scanner priceScanner = new Scanner(System.in);
                    System.out.print("Enter car model name to perform scrapping on  mileage: ");
                    String carNamePrice = priceScanner.nextLine();
                    System.out.print("Enter minimum mileage (mpg) of the car you want to scrap: ");
                    int minMpg = priceScanner.nextInt();

                    System.out.println();

                    // Scrap car price data
                    mpgScrapper.scrapCarMpgData(word,minMpg );
                }
            }else {
                System.out.println(skyBlue + "Would you like to perform scraping on mileage." + reset);
                String mpgScrapeChoice = scanner.nextLine().trim();

                if (mpgScrapeChoice.equalsIgnoreCase("yes")) {
                    // Code for scraping car prices
                    Scanner priceScanner = new Scanner(System.in);
                    System.out.print("Enter car model name to perform scrapping on mileage: ");
                    String carNamePrice = priceScanner.nextLine();
                    System.out.print("Enter minimum mileage (mpg) of the car you want to scrap: ");
                    int minMpg = priceScanner.nextInt();

                    System.out.println();

                    // Scrap car price data
                    mpgScrapper.scrapCarMpgData(word,minMpg );
                } else {
                    decorate( green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!"+ reset);
                    break;
                }
            }

            // Offer to scrap car price data based on the word
            if (isModelName) {
                System.out.println(skyBlue + "\nWould you like to scrape car prices based on the model " + word + "? (yes/no)" + reset);
                String scrapeChoice = scanner.nextLine().trim();
                if (scrapeChoice.equalsIgnoreCase("yes")) {
                    Scanner priceScanner = new Scanner(System.in);


                    System.out.print("Enter min. price of the car: ");
                    int minPrice = priceScanner.nextInt();
                    System.out.print("Enter max. price of the car: ");
                    int maxPrice = priceScanner.nextInt();
                    System.out.println();

                    // Scrap car price data
                    priceScrapper.scrapCarPriceData(word, minPrice, maxPrice);
                } else {
                    Scanner priceScanner = new Scanner(System.in);
                    System.out.print("Enter car model name to perform scrapping: ");
                    String carNamePrice = priceScanner.nextLine();
                    System.out.print("Enter min. price of the car: ");
                    int minPrice = priceScanner.nextInt();
                    System.out.print("Enter max. price of the car: ");
                    int maxPrice = priceScanner.nextInt();
                    System.out.println();

                    // Scrap car price data
                    priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice);
                }
            }else {
                System.out.println(skyBlue + "Would you like to perform scraping on price." + reset);
                String priceScrapeChoice = scanner.nextLine().trim();

                if (priceScrapeChoice.equalsIgnoreCase("yes")) {
                    // Code for scraping car prices
                    Scanner priceScanner = new Scanner(System.in);
                    System.out.print("Enter car model name to perform scrapping: ");
                    String carNamePrice = priceScanner.nextLine();
                    System.out.print("Enter min. price of the car: ");
                    int minPrice = priceScanner.nextInt();
                    System.out.print("Enter max. price of the car: ");
                    int maxPrice = priceScanner.nextInt();
                    System.out.println();

                    // Scrap car price data
                    priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice);
                } else {
                    decorate( green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!"+ reset);
                    break;
                }
            }
        }
    }


    public static void decorate(String message){
int length=message.length() +4;
        for (int i = 0; i < length; i++) {
            System.out.print("*");
        }
        System.out.println();

        // Print message with side borders
        System.out.println( message );

        // Print bottom border
        for (int i = 0; i < length; i++) {
            System.out.print("*");
        }
    }


    public static void decorateWithLine(String message){
        int length=message.length() +4;



        // Print message with side borders
        System.out.println( message );

        // Print bottom border
        for (int i = 0; i < length; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
    }

