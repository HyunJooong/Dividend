package choo.stock;

import choo.stock.persist.dao.Company;
import choo.stock.persist.scraper.DividendScraper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class, args);
      /*  try {
            String url = "https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&period1=1704448348&period2=1736070739";
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements elements = document.getElementsByAttributeValue("class", "table yf-j5d1ld noDl");
            Element element = elements.get(0);

            Element tbody = element.children().get(1);
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                } else {
                    String[] splits = txt.split(" ");
                    String month = splits[0];
                    int day = Integer.parseInt(splits[1].replace(",", ""));
                    int year = Integer.parseInt(splits[2]);
                    String dividend = splits[3];

                    System.out.println(year + "/" + month + "/" + day + ": " + dividend);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }*/

        DividendScraper dividendScraper = new DividendScraper();
        var result  = dividendScraper.scrap(Company.builder()
                .ticker("O")
                .build());
        System.out.println(result);

    }


}
