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

public class WebCrawler {
    private Set<String> visitedUrls;
    private Queue<String> urlsToVisit;
    private int maxUrlsToVisit;
    private String saveDir;

    public WebCrawler(int maxUrlsToVisit, String saveDir) {
        visitedUrls = new HashSet<String>();
        urlsToVisit = new LinkedList<String>();
        this.maxUrlsToVisit = maxUrlsToVisit;
        this.saveDir = saveDir;
    }

    public void clear() {
        visitedUrls.clear();
        urlsToVisit.clear();
    }

    public void crawl(String startingUrl, String saveDir) throws IOException {
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        urlsToVisit.add(startingUrl);
        while (!urlsToVisit.isEmpty() && visitedUrls.size() < maxUrlsToVisit) {
            String url = urlsToVisit.poll();
            if (!visitedUrls.contains(url)) {
                visitedUrls.add(url);
                System.out.println("Visiting: " + url);
                try {
                    String links = HTMLParser.parse(url, saveDir);
                    for (String nextUrl : links.split(" ")) {
                        if (!visitedUrls.contains(nextUrl)) {
                            urlsToVisit.add(nextUrl);
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Error visiting URL: " + url);
                    e.printStackTrace();
                    // Handle the error gracefully, such as logging it and continuing to the next URL.
                }
            }
        }
        System.out.println("Website is crawled!");
    }


    public static void main(String[] args) throws IOException {
        int maxUrlsToVisit = 15;
        String saveDir = "pages";
        int option = 0;
        boolean continueRunning = true;

        HashMap<String, Integer> wordFreqMap = new HashMap<>();

        WebCrawler crawler = new WebCrawler(maxUrlsToVisit, saveDir);
        InvertedIndex index = new InvertedIndex();
        PageRanking pageRanking = new PageRanking();

        FrequencyCount freqCount = new FrequencyCount();
        CarPriceScrapper priceScrapper = new CarPriceScrapper();

        Scanner scanner = new Scanner(System.in);


        while(continueRunning) {

                System.out.println("\n1: Crawl website");
                System.out.println("2: Scrap car brands according to Price");
                System.out.println("3: Scrap car brands according to MPG");
                System.out.println("4: Inverted indexing");
                System.out.println("5: Frequency count");
                System.out.println("6: Page ranking");
                System.out.println("7: Spell checking");
                System.out.println("8: Spell suggestion");
                System.out.println("9: Search words frequency");
                System.out.println("0: Terminate\n");


            System.out.println("Enter Value: ");

            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
                continue;

            }

            switch (option) {
                case 1:
                    String startingUrl;
                    do {
                        System.out.print("Enter a starting URL: ");
                        startingUrl = scanner.nextLine();

                        if (!UrlValidator.validate(startingUrl)) {
                            System.out.println("Invalid URL. Please try again.");
                        }
                    } while (!UrlValidator.validate(startingUrl));
                    crawler.clear();
                    crawler.crawl(startingUrl, saveDir);
                    continueRunning = false;

                    break;

                case 2:
                	Scanner priceScanner = new Scanner(System.in);

                	System.out.print("Enter car name: ");
                    String carNamePrice = priceScanner.nextLine();
                    System.out.print("Enter min. price: ");
                    int minPrice = priceScanner.nextInt();
                    System.out.print("Enter max. price: ");
                    int maxPrice = priceScanner.nextInt();
                    System.out.println();

                    // Scrap car price data
                    priceScrapper.scrapCarPriceData(carNamePrice, minPrice, maxPrice);
                    continueRunning = false;
                    break;



                case 3:

                	CarMpgScrapper carScrapper = new CarMpgScrapper();

                	Scanner carScanner = new Scanner(System.in);

            		// Prompt the user to enter the URL of the website to scrape
            		System.out.print("Enter the name of the Car: ");
            		String carName = carScanner.nextLine();

            		if (wordFreqMap.containsKey(carName)) {
                        wordFreqMap.put(carName, wordFreqMap.get(carName) + 1);
                    } else {
                        wordFreqMap.put(carName, 1);
                    }

            		// Prompt the user to enter the year of the cars to scrape
            		System.out.print("Enter the year of the cars to scrape: ");
            		int year = carScanner.nextInt();

            		// Prompt the user to enter the mileage range to sort by
            		System.out.print("Enter the minimum mileage (mpg): ");
            		int minMpg = carScanner.nextInt();
            		System.out.print("Enter the maximum mileage (mpg): ");
            		int maxMpg = carScanner.nextInt();
            		System.out.println();

            		// Scrape the car data and print the sorted results
            		carScrapper.scrapMileage(carName, year, minMpg, maxMpg);
                    continueRunning = false;

                    break;

                case 4:

                	Scanner indexScanner = new Scanner(System.in);

                    index.buildIndex("..//pages");

                    System.out.println("Enter the keyword to search: ");
                    String keyword = indexScanner.nextLine();
                    index.searchKeyword(keyword);
                    continueRunning = false;

                    break;

                case 5:
                    freqCount.countFrequency("..//pages","bentley");
                    continueRunning = false;
                    break;

                case 6:
                    pageRanking.PageRank("pages","bentley");
                    continueRunning = false;
                    break;

                case 7:
                	// Read text files and build dictionary trie
                    SpellChecker spellChecker = new SpellChecker();
                    String folderPath = "..//pages";
                    spellChecker.readTextFilesAndBuildDictionary(folderPath);

                    // Prompt user to input string for spell checking
                    Scanner spellScanner = new Scanner(System.in);
                    System.out.println("Enter a string to check for spelling errors:");
                    String input = spellScanner.nextLine();

                    if (wordFreqMap.containsKey(input)) {
                        wordFreqMap.put(input, wordFreqMap.get(input) + 1);
                    } else {
                        wordFreqMap.put(input, 1);
                    }

                    // Spell check the input and print the result
                    String output = spellChecker.spellCheck(input);
                    System.out.println("Spell checked string:");
                    System.out.println(output);
                    continueRunning = false;

                    break;

                case 8:
                	SpellSuggestion spellSuggestion = new SpellSuggestion("..//pages");

                	Scanner suggestionScanner = new Scanner(System.in);
                    System.out.println("Enter a word to check for suggestions:");
                    String word = suggestionScanner.nextLine();

                    if (wordFreqMap.containsKey(word)) {
                        wordFreqMap.put(word, wordFreqMap.get(word) + 1);
                    } else {
                        wordFreqMap.put(word, 1);
                    }

                    String suggestion = spellSuggestion.suggestWord(word);
                    if (suggestion.isEmpty()) {
                        System.out.println("No suggestions found for " + word);
                    } else {
                        System.out.println("Did you mean " + suggestion + " ?");
                    }
                    continueRunning = false;

                    break;

                case 9:
                	ArrayList<Map.Entry<String, Integer>> list = new ArrayList<>(wordFreqMap.entrySet());
                	Collections.sort(list, Map.Entry.<String, Integer>comparingByValue().reversed());

                	System.out.println("Word Frequencies (Descending Order): ");
                	for (Map.Entry<String, Integer> entry : list) {
                	    System.out.println(entry.getKey() + ": " + entry.getValue());
                	}
                    continueRunning = false;
                	break;

                case 0:
                	System.out.println("Thank you!");
                    continueRunning = false;
                    break;

                default:
                    System.out.println("Invalid option. Please enter a number between 0 and 9.");
                    break;
            }
        }
    }
}