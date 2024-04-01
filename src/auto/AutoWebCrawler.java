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
         String yellow= "\u001B[33m";
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
        websiteUrl.replaceAll(" ","");
        if (websiteUrl.equalsIgnoreCase("exit")) {
            decorateWithLine(green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!" + reset);
            return; // Exit the program
        }
        while(websiteUrl.isBlank()) {
            System.out.println(yellow + "No website URL provided. Please enter a valid website URL or type 'exit' to quit." + reset);
            websiteUrl = scanner.nextLine();
        }
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
            decorateWithLine("Enter a car model name, year of production or feature of car to perform operations (or type 'exit' to quit): ");
            String word = scanner.nextLine().trim();

            if (word.equalsIgnoreCase("exit")) {
                decorate( green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!" + reset);
                break;
            }
            else if (!word.matches("[a-zA-Z0-9]+")) {
    System.out.println(yellow + "Error: Input cannot contain special characters or symbols." + reset);
    Thread.sleep(2000);
    continue; // Restart the loop to prompt for a new input
}
System.out.println();
            decorateWithLine( "Based on the word you entered, we can offer the following functions for you to explore and analyze:" );
            Thread.sleep(2000);

           word=performSpellSuggestion(word);
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
            System.out.println();
            while (true) {
            System.out.println(skyBlue + "Would you like to scrape cars based on mileage or price? (mileage/price)" + reset);
            String scrapeOption = scanner.nextLine().trim();

    if (scrapeOption.equalsIgnoreCase("mileage")) {
        if (isModelName) {
            System.out.println(skyBlue + "\nWould you like to search cars mileage based on the model " + word + "? (yes/no)" + reset);
            String scrapeChoice = scanner.nextLine().trim();
            if (!scrapeChoice.equalsIgnoreCase("no") && !scrapeChoice.equalsIgnoreCase("yes")) {
                System.out.println("Please enter either yes/no");
                scrapeChoice = scanner.nextLine().trim();
            }
            if (scrapeChoice.equalsIgnoreCase("yes")) {
                Scanner priceScanner = new Scanner(System.in);
                System.out.print("Enter minimum mileage (mpg) of the car you want to scrap:");
                long minMpg = priceScanner.nextLong();

                System.out.println();

                // Scrap car price data
                mpgScrapper.scrapCarMpgData(word, minMpg);
            } else if(scrapeChoice.equalsIgnoreCase("no")) {
                Scanner priceScanner = new Scanner(System.in);
                System.out.print("Enter car model name to perform scrapping on  mileage: ");
                String carNamePrice = priceScanner.nextLine();
                System.out.print("Enter minimum mileage (mpg) of the car you want to scrap: ");
                long minMpg = priceScanner.nextLong();

                System.out.println();

                // Scrap car price data
                mpgScrapper.scrapCarMpgData(word, minMpg);
            }
        } else {
            System.out.println(skyBlue + "Would you like to perform scraping on mileage." + reset);
            String mpgScrapeChoice = scanner.nextLine().trim();
            if (!mpgScrapeChoice.equalsIgnoreCase("no") && !mpgScrapeChoice.equalsIgnoreCase("yes")) {
                System.out.println("Please enter either yes/no");
                mpgScrapeChoice = scanner.nextLine().trim();
            }
            if (mpgScrapeChoice.equalsIgnoreCase("yes")) {
                // Code for scraping car prices
                Scanner priceScanner = new Scanner(System.in);
                System.out.print("Enter car model name to perform scrapping on mileage: ");
                String carNamePrice = priceScanner.nextLine();
                System.out.print("Enter minimum mileage (mpg) of the car you want to scrap: ");
                long minMpg = priceScanner.nextLong();

                System.out.println();

                // Scrap car price data
                mpgScrapper.scrapCarMpgData(word, minMpg);
            } else {
                decorate(green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!" + reset);
                break;
            }
        }
    } else if (scrapeOption.equalsIgnoreCase("price")) {
        if (isModelName) {
            System.out.println(skyBlue + "\nWould you like to scrape car prices based on the model " + word + "? (yes/no)" + reset);
            String scrapeChoice = scanner.nextLine().trim();
            if (!scrapeChoice.equalsIgnoreCase("no") && !scrapeChoice.equalsIgnoreCase("yes")) {
                System.out.println("Please enter either yes/no");
                scrapeChoice = scanner.nextLine().trim();
            }
            if (scrapeChoice.equalsIgnoreCase("yes")) {
                Scanner priceScanner = new Scanner(System.in);

                long minPrice = 0;
                long maxPrice = 0;
                while (true) {
                    try {
                        System.out.print("Enter min. price of the car: ");
                        minPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }

                while (true) {
                    try {
                        System.out.print("Enter max. price of the car: ");
                        maxPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }
                System.out.println();

                // Scrap car price data
                priceScrapper.scrapCarPriceData(word, minPrice, maxPrice);
            } else if(scrapeChoice.equalsIgnoreCase("no")) {
                Scanner priceScanner = new Scanner(System.in);
                System.out.print("Enter car model name to perform scrapping: ");
                String carNamePrice = priceScanner.nextLine();
                long minPrice = 0;
                long maxPrice = 0;
                while (true) {
                    try {
                        System.out.print("Enter min. price of the car: ");
                        minPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }

                while (true) {
                    try {
                        System.out.print("Enter max. price of the car: ");
                        maxPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }
                System.out.println();

                // Scrap car price data
                priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice);
            }
        } else {
            System.out.println(skyBlue + "Would you like to perform scraping on price ? (yes/no)" + reset);
            String priceScrapeChoice = scanner.nextLine().trim();
            if (!priceScrapeChoice.equalsIgnoreCase("no") && !priceScrapeChoice.equalsIgnoreCase("yes")) {
                System.out.println("Please enter either yes/no");
                priceScrapeChoice = scanner.nextLine().trim();
            }
            if (priceScrapeChoice.equalsIgnoreCase("yes")) {
                // Code for scraping car prices
                Scanner priceScanner = new Scanner(System.in);
                System.out.print("Enter car model name to perform scrapping: ");
                String carNamePrice = priceScanner.nextLine();
                long minPrice = 0;
                long maxPrice = 0;
                while (true) {
                    try {
                        System.out.print("Enter min. price of the car: ");
                        minPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }

                while (true) {
                    try {
                        System.out.print("Enter max. price of the car: ");
                        maxPrice = priceScanner.nextLong();
                        break; // Break the loop if input is valid
                    } catch (InputMismatchException e) {
                        System.out.println(yellow + "Invalid input. Please enter a valid numeric value for the price." + reset);
                        priceScanner.nextLine(); // Consume the invalid input
                    }
                }
                System.out.println();

                // Scrap car price data
                priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice);
            } else {
                decorate(green + "Thank you for using the Automated Web Crawler and Analysis Tool. Goodbye!" + reset);
                break;
            }
        }
    } else {
        System.out.println(yellow + "Invalid option. Please enter either 'mileage' or 'price'." + reset);
    }
    }
}
            // Offer to scrap car mpg data based on the word


            // Offer to scrap car price data based on the word


    }


    public static String performSpellSuggestion(String word) {
        String skyBlue="\u001B[36m";
        String reset="\u001B[0m";
        SpellSuggestion spellSuggestion = new SpellSuggestion("pages");
        String yellow= "\u001B[33m";
        Scanner scanner = new Scanner(System.in);
        // Spell suggestion logic
        System.out.println(skyBlue + "\nSpell Suggestions:" + reset);
        String suggestion = spellSuggestion.suggestWord(word);

        if (suggestion.isEmpty()) {
            System.out.println(yellow + "No suggestions found for this word." + reset);
            System.out.println("Enter another word for suggestions (or type 'exit' to quit): ");
            word = scanner.nextLine().trim();
            if (word == null || word.equalsIgnoreCase("exit")) {
                System.out.println("Exiting spell suggestion...");
                return null;
            }
            suggestion = performSpellSuggestion(word);
            return suggestion;


        }

        System.out.println("Did you mean the word " + suggestion + " ?  (yes/no)");
        String userResponse = scanner.nextLine().trim();

        if (userResponse.equalsIgnoreCase("yes")) {
            return suggestion;
        } else if (!userResponse.equalsIgnoreCase("no")) {
            System.out.println("Please enter either yes/no");
            return null; // Handle incorrect input
        }

        while (userResponse.equalsIgnoreCase("no")) {
            System.out.println("Enter a new word for the operations");
            word = scanner.nextLine().trim();
            System.out.println("Your new word is: " + word + ". Do you want to perform other operations on that word? (yes/no)");
            userResponse = scanner.nextLine().trim();
            if(userResponse.equalsIgnoreCase("yes")){
                return performSpellSuggestion(word); // Return the result of the recursive call
            }
        }

        return word;
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

