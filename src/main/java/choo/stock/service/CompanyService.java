package choo.stock.service;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.entity.DividendEntity;
import choo.stock.persist.repository.CompanyRepository;
import choo.stock.persist.repository.DividendRepository;
import choo.stock.persist.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Scraper yahooFinanceScraper;
    private final DividendRepository dividendRepository;
    private final CompanyRepository companyRepository;

    public Company save(String ticker) {
        boolean existsByTicker = companyRepository.existsByTicker(ticker);
        if (existsByTicker) {
            throw new RuntimeException("Already saved" + ticker);
        }

        return storeCompanyAndDividend(ticker);
    }

    private Company storeCompanyAndDividend(String ticker) {
        //ticker를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if(ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to find company" + ticker);
        }
        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

        //스크래핑 결과
        CompanyEntity companyEntity = companyRepository.save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult.getDividends().stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);


        return company;
    }
}
