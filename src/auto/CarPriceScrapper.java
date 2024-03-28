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
            String url = "https://www.kbb.com/car-finder/?manufacturers=" + carName.replace(" ", "%20")
                    + "&pricerange=" + minPrice + "-" + maxPrice;
            System.out.println(url);
            // Connect to the website and get its HTML document
            Document doc = Jsoup.connect(url).get();

            // Select car elements from the HTML document
            Elements carElements = doc.select("div.css-11diq1x.e1qqueke1");

            // List to store scraped car objects
            List<Car> cars = new ArrayList<>();

            // Loop through each car element and extract relevant information
            for (Element carElement : carElements) {
                String name = carElement.select("#vehicle_card_2 > div.css-ssaa7u.ewtqiv32 > div.css-4w1p2i.e19qstch21 > div > div.css-gk1xze.ex4y58i4 > a > h2").text(); // Car name
                String year = name.split(" ")[0]; // Car year
                String price = carElement.select("#vehicle_card_1 > div.css-ssaa7u.ewtqiv32 > div.css-1t1oons.e19qstch19 > div.css-15j21fj.e19qstch15 > div > div > div > div.css-fpbjth.e151py7u1").text(); // Car price
                String mpg = carElement.select("#vehicle_card_1 > div.css-ssaa7u.ewtqiv32 > div.css-1t1oons.e19qstch19 > div.css-14q4cew.e19qstch18 > div.css-n59ln1.e181er9y2 > div > div > div.css-fpbjth.e151py7u1").text(); // Car MPG

                // Filtering cars based on user input for min and max price
                int carPrice = Integer.parseInt(price.replaceAll("[^0-9]", ""));
                if (carPrice >= minPrice && carPrice <= maxPrice) {
                    cars.add(new Car(name, year, price, mpg)); // Add car to the list
                }
            }

            // Sort the list of cars by price in ascending order (as string)
            Collections.sort(cars, (car1, car2) -> car1.getPrice().compareTo(car2.getPrice()));

            // Print the list of cars
            for (Car car : cars) {
                System.out.println(car + "index");
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
            return name + " " + year + ", Price: " + price + ", MPG: " + mpg;
        }
    }
}