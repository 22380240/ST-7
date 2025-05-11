package com.mycompany.app;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.PrintWriter;

public class Task3 {
    public static void start() {
        WebDriver webDriver = new ChromeDriver();
        try {
            webDriver.get("https://api.open-meteo.com/v1/forecast?latitude=56&longitude=44&hourly=temperature_2m,rain&current=cloud_cover&timezone=Europe%2FMoscow&forecast_days=1&wind_speed_unit=ms");

            WebElement elem = webDriver.findElement(By.tagName("pre"));
            String jsonStr = elem.getText();

            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(jsonStr);

            JSONObject hourly = (JSONObject) obj.get("hourly");
            JSONArray times = (JSONArray) hourly.get("time");
            JSONArray temperatures = (JSONArray) hourly.get("temperature_2m");
            JSONArray rains = (JSONArray) hourly.get("rain");

            PrintWriter writer = new PrintWriter(new FileWriter("./result/forecast.txt"));
            writer.printf("| № | Дата/время | Температура | Осадки (мм) |\n");

            for (int i = 0; i < times.size(); i++) {
                String time = (String) times.get(i);
                double temp = (Double) temperatures.get(i);
                double rain = (Double) rains.get(i);
                writer.printf("| %d | %s | %f | %f |\n", i + 1, time, temp, rain);
            }

            writer.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        } finally {
            webDriver.quit();
        }
    }
}
