package choo.stock.persist.scraper;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;

public class TossScraper implements Scraper {
    @Override
    public Company scrapCompanyByTicker(String ticker) {
        return null;
    }

    @Override
    public ScrapedResult scrap(Company company) {
        return null;
    }
}
