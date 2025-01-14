package choo.stock.persist.scheduler;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.entity.DividendEntity;
import choo.stock.persist.repository.CompanyRepository;
import choo.stock.persist.repository.DividendRepository;
import choo.stock.persist.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final Scraper scraper;
    private final DividendRepository dividendRepository;

    @Scheduled(cron = "") // 일정 주기마다 실행
    public void financeScheduling() {
        //저장된 회사 목록을 조회
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companyEntityList) {
            ScrapedResult scrapedResult = scraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());
            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    .map(e -> new DividendEntity(company.getId(), e))
                    .forEach(e -> {
                        boolean exits = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exits) {
                            dividendRepository.save(e);
                        }
                    });
        }


    }
}
