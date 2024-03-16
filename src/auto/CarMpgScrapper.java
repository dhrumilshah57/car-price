package auto;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class CarMpgScrapper {

	// A class to represent a car
	private static class Car {
		private final String model;
		private final int mpg;
		private final int year;

		public Car(String model, int mpg, int year) {
			this.model = model;
			this.mpg = mpg;
			this.year = year;
		}

		public String getModel() {
			return model;
		}

		public int getMpg() {
			return mpg;
		}

		public int getYear() {
			return year;
		}

		@Override
		public String toString() {
			return model + " || " + mpg + " mpg";
		}
	}

	public static void scrapMileage(String url, int year, int minMpg, int maxMpg) throws IOException {
		// Connect to the website and retrieve the HTML document
		String urlWithName = "https://mpgof.com/" + url;
		
		Document doc = Jsoup.connect(urlWithName).get();

		// Find all the tables with class "table"
		Elements tables = doc.select("table.table");

		// Create a list to store the car data
		List<Car> cars = new ArrayList<>();

		// Iterate through each table and extract the car data
		for (Element table : tables) {
			// Find the caption of the table to get the year
			String caption = table.selectFirst("caption").text();
			int tableYear = Integer.parseInt(caption.replaceAll("[^0-9]+", ""));

			// Check if the year matches the user input
			if (tableYear == year) {
				// Find all the rows in the table
				Elements rows = table.select("tbody tr");

				// Iterate through each row and extract the car data
				for (Element row : rows) {
					String model = row.selectFirst("td:first-child").text();

					// Check if the model name starts with the year entered by the user
					if (model.startsWith(Integer.toString(year))) {
						String mpg = row.selectFirst("td:last-child").text();
						String[] mpgValues = mpg.split(" to ");

						// Add the car data to the list if it falls within the specified mileage range
						int maxMpgValue = Integer.parseInt(mpgValues[1]);
						int minMpgValue = Integer.parseInt(mpgValues[0]);
						if ((maxMpgValue >= minMpg && maxMpgValue <= maxMpg) && (minMpgValue <= maxMpg && minMpgValue >= minMpg)) {
							cars.add(new Car(model, maxMpgValue, year));
						}
					}
				}

			}
		}
		
		// Sort the cars by mileage (in descending order) using a custom comparator
		Collections.sort(cars, new Comparator<Car>() {
			@Override
			public int compare(Car c1, Car c2) {
				return c2.getMpg() - c1.getMpg();
			}
		});

		// Print the sorted list of cars
		System.out.println("List of cars sorted by mileage (in descending order):");
		for (Car car : cars) {
			System.out.println(car);
		}
	}
}
