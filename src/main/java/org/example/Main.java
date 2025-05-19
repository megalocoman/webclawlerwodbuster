package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String cookie = "change with authcookie";
        String urlBase = "https://fenrirbox.wodbuster.com";
        String urlWeight = "/benchmarks/weights.aspx";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("products.csv"))) {
            csvWriter.writeNext(new String[] { "Ejercicio", "fecha", "reps", "peso", "1RM" });
            Document doc = Jsoup.connect(urlBase + urlWeight).cookie(".WBAuth", cookie).get();
            var listPesos = doc.select(".benchmarks>.hecho");
            listPesos.forEach(x -> {
                String ejercicio = x.select("h3").text();

                var hrefWeight = x.select(".button.tiny").attr("href");

                try {
                    Document docDetail = Jsoup.connect(urlBase + hrefWeight).cookie(".WBAuth", cookie).get();
                    var element = docDetail.select("table").get(1);
                    var data = element.select("tr");

                    for (int i = 1; i < data.size(); i++) {
                        var columns = data.get(i).select("td");
                        String fecha = columns.get(0).text();
                        String reps = columns.get(1).text();
                        String peso = columns.get(2).text();
                        String rm = columns.get(3).text();
                        System.out.println(ejercicio + "-" + fecha + "-" + reps + "-" + peso + "-" + rm);
                        csvWriter.writeNext(new String[] { ejercicio, fecha, reps, peso, rm });
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
}