package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String cookie = "1dMLJ108vs8cs5isqwXA2CVADUxUBkNBySt9xaWqa2KCtkEUrPzGMOWLlOn8DUdHSFu-ftDbYXkgIRKsmFjOjSP_7un_dKxhdg243yEoe6_q0f4UWX1Jlwp6kbmpw31YBDZ48ql5m5a77ilbg4uA9EnTClsXUUWFxixVsa5qtNaYm-v2eBH4UNMvM3vib0YqAKlQ7YQWHn36ssESvLXdBC1foSsf7ki8CZq78TEBkOkj9fj1BgqNvOXkca0s4NeZdGiukplkiF7qXI3famKeN5iupLlHSPYZCvhJf6BQiolaN7TnIWbEagZqPejxGssRRwjHf4rE1J8jY-OF0Cxtd3HnEP7aRMqUd8A3r8_zZ9AdzQQ5bwHNfJJrbUsMv8PMp9W4FvVI6jlWwAhPJKO4MGD8jveMxyzKnAjszwj_uRDf_GOwnnas67FKdVKHnEk6tBQUI4UaqDbmlp4OIGfiIW7ZCmz6vFP3FMRt-Jqcg6dNH8hRs0zPmTkhZX0BZHyj";
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