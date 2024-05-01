package com.automation.extractusacityname;


import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ExtractUSACityName {
    public static void main(String[] args) {
        ChromeOptions options = new ChromeOptions();
        var driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://www.britannica.com/topic/list-of-cities-and-towns-in-the-United-States-2023068");

        try {

            var stateListContainer = driver.findElement(By.cssSelector("#content > div > div.infinite-scroll-container.article.last > article > div > div.col-auto > div > div > div > div.section-content.pl-10.pr-20.pl-sm-50.pr-sm-60.pl-lg-5.pr-lg-10.pt-10.pt-lg-0.bg-gray-50.clear-catfish-ad > div.toc.mb-20 > ul"));
            var stateListIdsElements = stateListContainer.findElements(By.tagName("li"));
            stateListIdsElements.removeFirst();

            List<String> stateListIds = new ArrayList<>();

            for (var element : stateListIdsElements) {
                var stateId = element.getAttribute("data-target");
                stateListIds.add(stateId);
            }

            List<String> cities = new ArrayList<>();

            for (var stateId : stateListIds) {
                var stateSection = driver.findElement(By.id(stateId.replace("#", "")));
                var cityList = stateSection.findElements(By.tagName("ul")).getFirst().findElements(By.tagName("li"));

                for (var city : cityList) {
                    var cityDivElement = city.findElements(By.tagName("div")).getFirst();
                    var cityAnchorElement = cityDivElement.findElements(By.tagName("a"));
                    if (cityAnchorElement.isEmpty()) {
                        cities.add(cityDivElement.getText());
                    } else {
                        cities.add(cityAnchorElement.getFirst().getText());
                    }
                }
            }

            var csv = "City Name\n\n";

            for (var city : cities) {
                csv += city + "\n";
            }

            try {
                FileWriter myWriter = new FileWriter("cities.csv");
                myWriter.write(csv);
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        driver.quit();
    }
}
