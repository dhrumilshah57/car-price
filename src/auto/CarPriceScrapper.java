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

    public void scrapCarPriceData(String carName, int minPrice, int maxPrice) {
        try {
            // URL for the car search
            String url = "https://www.kbb.com/car-finder/?" + (carName != null && !carName.isEmpty() ?
                    "manufacturers=" + carName.replace(" ", "%20") + "&" : "") +
                    "pricerange=" + minPrice + "-" + maxPrice;

            // Connect to the website and get its HTML document
            Document doc = Jsoup.connect(url).get();

            // Select car elements from the HTML document
            Elements carElements = doc.select("div.css-11diq1x.e1qqueke1 > div.ewtqiv33.css-jwnqcy.e11el9oi0");

            // List to store scraped car objects
            List<Car> cars = new ArrayList<>();

            // Loop through each car element and extract relevant information
            for (Element carElement : carElements) {

                // Extract car name
                String name = carElement.select("a.css-z66djy.ewtqiv30").text();

                String year =  carElement.select("a.css-z66djy.ewtqiv30").text().split(" ")[0];
                String price = carElement.select("div.css-15j21fj.e19qstch15 > div.css-n59ln1.e181er9y2> div.css-1d3w5wq.e181er9y1 > div.css-15ums5i.e181er9y0 > div.css-fpbjth.e151py7u1").text();


                String mpg = carElement.select("div.css-14q4cew.e19qstch18 > div.css-n59ln1.e181er9y2  > div.css-1d3w5wq.e181er9y1 > div.css-15ums5i.e181er9y0 > div.css-fpbjth.e151py7u1").text();


                            cars.add(new Car(name, year, price, mpg)); // Add car to the list



            }

            // Sort the list of cars by price in ascending order (as string)
            cars.sort((car1, car2) -> car1.getPrice().compareTo(car2.getPrice()));

            // Print the list of cars
            for (Car car : cars) {
             System.out.println( car.toString());
            }
        } catch (HttpStatusException e) {
            if (e.getStatusCode() == 404) {
                System.err.println("Error: The car model was not found.");
            } else {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // Method to check if the word corresponds to a model name
    public boolean checkIfModelName(String word) {
        try {

            String url = "https://www.kbb.com/car-finder";
            Document doc = Jsoup.connect(url).get();
            Elements labelElements = doc.select("#manufacturers-content > div > div > label");

// Extract model names from the label elements and check if the input word matches any of them
            for (Element labelElement : labelElements) {
                String modelName = labelElement.select(".label-container").text().trim().toLowerCase();

if(modelName.equalsIgnoreCase(word)){     return true;}
            }
        } catch (IOException e) {

            e.printStackTrace();
            return false;
            // Handle connection or parsing errors
            // You may choose to return false or throw the exception here
        }

return false;
    }

    static class Car {
        String name;
        String year;
        String price;
        String mpg;

        public Car(String name, String year, String price, String mpg) {
            this.name = name;
            this.year = year;
            this.price = price;
            this.mpg = mpg;
        }

        public String getPrice() {
            return price;
        }

        @Override
        public String toString() {
            return "Name of the Model: " + name + " Year: " + year + ", Price: " + price + ", MPG: " + mpg;
        }
    }
}
