package auto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CarPriceScrapper {

  // Method is declared to scrape the car price based on car name, min price, and max price
//  public void ScrapCarPriceData(String carName, int minPrice, int maxPrice) throws IOException {}
	public void ScrapCarPriceData(String carName, int minPrice, int maxPrice) throws IOException {
	    // The URL for the car search is defined
	    String url = "https://www.kbb.com/car-finder/?manufacturers=" + carName.replace(" ", "%20")+"&pricerange="+minPrice+"-"+maxPrice;
	    
	    // Print the URL for debugging purposes
	    System.out.println("Scraping URL: " + url);

	    // Using Jsoup to connect to the website and get its HTML document
	    try {
	        Document doc = Jsoup.connect(url).get();
	        
	        // Select the car elements from the HTML document
	        Elements carElements = doc.select("div.ewtqiv33.css-jwnqcy.e11el9oi0");

	        // Create an empty array list to store the scraped car objects
	        List<Car> cars = new ArrayList<>();
	        // Loop through each car element and extract relevant information
	        for (Element carElement : carElements) {
	          String name = carElement.select("h2.css-iqcfy5.e148eed12").text(); // Extracting car name
	          String year = name.split(" ")[0]; // Extracting car year from the car name
	          String price = carElement.select("div.css-fpbjth.e151py7u1").text(); // Extracting car price
	          String mpg = carElement.select("div.css-fpbjth.e151py7u1").text(); // Extracting car MPG

	          // Filtering cars based on user input for min price and max price
	          if (Integer.parseInt(price.replaceAll("[^0-9]", "")) >= minPrice
	                  && Integer.parseInt(price.replaceAll("[^0-9]", "")) <= maxPrice) {
	        	  
	            // Create a car object and add it to the list of cars
	            Car car = new Car(name, year, price, mpg);
	            cars.add(car);
	          }
	        }

	        // Sort the list of cars by price in descending order
	        Collections.sort(cars, (car1, car2) -> car2.getPrice().compareTo(car1.getPrice()));

	        // Print the list of cars
	        for (Car car : cars) {
	          System.out.println(car);
	        }
	    } catch (HttpStatusException e) {
	        if (e.getStatusCode() == 404) {
	            System.err.println("Error: The car model was not found.");
	        } else {
	            throw e;
	        }
	    }
	}
  // Inner class to represent a Car object
  static class Car {
    String name;
    String year;
    String price;
    String mpg;

    // Constructor for Car object
    public Car(String name, String year, String price, String mpg) {
      this.name = name;
      this.year = year;
      this.price = price;
      this.mpg = mpg;
    }

    // Getter and Setter methods for Car object properties
    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getYear() {
      return year;
    }

    public void setYear(String year) {
      this.year = year;
    }

    public String getPrice() {
      return price;
    }

    public void setPrice(String price) {
      this.price = price;
    }

    public String getMpg() {
      return mpg;
    }

    public void setMpg(String mpg) {
     
      this.mpg = mpg;
    }

    // Override toString() method to customize the string representation of Car object
    @Override
    public String toString() {
      return name + " " + year + ", Price: " + price + ", MPG: " + mpg;
   

    }
  }
}
