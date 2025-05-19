package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {

        String cookie = "juqdjbgApn2IxqiuOf3I_KPHrQ2WVCVcSoHFkNUdUOvGjfIyG5VW5bJCtw_Xrxr5U7bHdR3UyOlvghZV2fvQDAddILvj1vnKvzFIE7R5RQO2RUM02_B48kVOn91gIUQaXW3Wu-gii3MvdEVax0H6T_u9SupaDiwFT4BuI79ahTs6PwmmzY9LL1xrXLwWjwBrsigRWwvVi6YGoetBuT3mLoxue9svdk48XrcUdNWqCElW9riy5nTHrn4wocjk-gyZPkF0ODruOpcGU7OclceWNJ-YHblsOMQ6HJdKqnR1qbttRJO15uggdKkBve-iLBmJsMsEqZZsTN2mX4i8pz-KPXMSYiKBqpSq8b9VtrMXljS6KkonyRoR1QfjgSKOsI6lq3So7Jjt6cvvMSrSu-XWjGEQgeRli2nvboySpO1eqdnvfq9-UjLOmKGHoPPcbLv7poYyIx_rV8NxVZ5SHDYyDRz_iaT8V0yrv_VLw1v3Ejj1kuI9DGXePE1hs21j2-Iq";

        String urlBase = "https://fenrirbox.wodbuster.com";
        String urlWeight = "/benchmarks/weights.aspx";
        Document doc = Jsoup.connect(urlBase + urlWeight).cookie(".WBAuth", cookie).get();
        var listPesos = doc.select(".benchmarks>.hecho");
        listPesos.forEach(x -> {
            System.out.println(x.select("h3").text());
            var hrefWeight = x.select(".button.tiny").attr("href");

            try {
                Document docDetail = Jsoup.connect(urlBase + hrefWeight).cookie(".WBAuth", cookie).get();
                var element = docDetail.select("table").get(1);


                System.out.println(element);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


//        System.out.println(listPesos);
    }
}