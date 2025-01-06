package choo.stock.persist.scraper;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.Dividend;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.dao.constants.Month;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DividendScraper {
    // https://finance.yahoo.com/quote/COKE/history/?frequency=1mo&filter=history&period1=1704547370&period2=1736169761
    private static final String URL = "https://finance.yahoo.com/quote/%s/history/?filter=history&period1=%d&period2=%d";

    private static final long START_TIME = 86400; // 60 * 60 * 24

    public ScrapedResult scrap(Company company) {
        ScrapedResult scrapResult = new ScrapedResult();
        scrapResult.setCompany(company);

        try {
            long endTime = System.currentTimeMillis() / 1000;

            String url = String.format(URL, company.getTicker(), START_TIME, endTime);
            Connection connection = Jsoup.connect(url);
            Document document = connection.get();

            Elements parsingDivs = document
                    .getElementsByAttributeValue("class", "table yf-j5d1ld noDl");
            Element tableElement = parsingDivs.get(0);

            Element tbody = tableElement.children().get(1);

            List<Dividend> dividends = new ArrayList<>();
            for (Element e : tbody.children()) {
                String txt = e.text();
                if (!txt.endsWith("Dividend")) {
                    continue;
                } else {
                    String[] splits = txt.split(" ");
                    int month = Month.strToNumber(splits[0]); //static 메서드 객체 생성 필요 없음
                    int day = Integer.parseInt(splits[1].replace(",", ""));
                    int year = Integer.parseInt(splits[2]);
                    String dividend = splits[3];

                    if (month < 0) {
                        throw new RuntimeException("Unexpected Month enum value: " + splits[0]);
                    } else {
                        dividends.add(Dividend.builder()
                                .date(LocalDate.of(year, month, day))
                                .dividend(dividend)
                                .build());
                    }
                }
            }
            scrapResult.setDividends(dividends);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scrapResult;

    }
}
