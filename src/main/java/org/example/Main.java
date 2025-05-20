package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        long startTime = System.nanoTime();

        String cookie = "IYYg6_uFmxlm1BH55hhKMmsZP1C5duhnT03P1YpUqMzqrgm7xFJod7QCAwZxw9CEuiW7k6AUeL6VwFXvwHqc87_vwkBvM0msOsVdwlSQoiYAh7snA_a4Tkain1bhqKpl9xiH9u-R9c_ln9tDALzEjVbF-KI6xu1zWwMu2N7FEjCaLUV_gBOyrs24xkuBfgqlSajpckQb-m4T50m-rG-gQ0qXMSYyWEVeFRqFd4kbXKwYCjajTvnYypARPuBb78RgU3FGA2lddphojtJ9vWJwj8Sam8ZV8C_0ZNJXPFXcEVSDZMYbKvS1ojLfNNkH0ESWlYtBmBkGInvxDl5aPGI9RFeATCwqqocGvsnQMvsO6YaEym__B79XEMiTpTJMwwC5Md8XMFERX7GAls5_1IVif3gpFKg_fYo0HMOtCGOtn_22xG_PLD3QeOn-A62HDmpDucpAHjp-B46kq7e7gatlw8VEXvVM9vEKXynsATIhotwcTzPvFM7HgtUzbBZoB33u";
        String urlBase = "https://fenrirbox.wodbuster.com";
        String urlWeight = "/benchmarks/weights.aspx";

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("products.csv"))) {
            csvWriter.writeNext(new String[]{"Ejercicio", "fecha", "reps", "peso", "1RM"});
            Document doc = Jsoup.connect(urlBase + urlWeight).cookie(".WBAuth", cookie).get();
            var listPesos = doc.select(".benchmarks>.hecho");
            listPesos.forEach(x -> {

                String ejercicio = x.select("h3").text();
                var hrefWeight = x.select(".button.tiny").attr("href");

                try {
                    Document docDetail = Jsoup.connect(urlBase + hrefWeight).cookie(".WBAuth", cookie).get();
                    var data = docDetail.select(":not(.scrollvisible)>table>tbody>tr");

                    for (int i = 1; i < data.size(); i++) {
                        var columns = data.get(i).select("td");
                        String fecha = columns.get(0).text();
                        String reps = columns.get(1).text();
                        String peso = columns.get(2).text();
                        String rm = columns.get(3).text();
                        csvWriter.writeNext(new String[]{ejercicio, fecha, reps, peso, rm});
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(duration/1000_000);
    }
}