package choo.stock.persist.scraper;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;

public interface Scraper {
    Company scrapCompanyByTicker(String ticker);

    ScrapedResult scrap(Company company);
}
