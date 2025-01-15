package choo.stock.persist.scheduler;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.entity.DividendEntity;
import choo.stock.persist.repository.CompanyRepository;
import choo.stock.persist.repository.DividendRepository;
import choo.stock.persist.scraper.Scraper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
@Slf4j
@Component
@AllArgsConstructor
public class ScraperScheduler {
    private final CompanyRepository companyRepository;
    private final Scraper scraper;
    private final DividendRepository dividendRepository;

    @Scheduled(cron = "${scheduler.scrap.yahoo}") // 일정 주기마다 실행
    public void financeScheduling() {
        //저장된 회사 목록을 조회
        List<CompanyEntity> companyEntityList = companyRepository.findAll();
        // 회사마다 배당금 정보를 새로 스크래핑
        for (var company : companyEntityList) {
            log.info("scraping scheduler is started");
            ScrapedResult scrapedResult = scraper.scrap(Company.builder()
                    .name(company.getName())
                    .ticker(company.getTicker())
                    .build());
            // 스크래핑한 배당금 정보 중 데이터베이스에 없는 값은 저장
            scrapedResult.getDividends().stream()
                    //디비든 모델을 디비든 엔티티로 매핑
                    .map(e -> new DividendEntity(company.getId(), e))
                    // 엘리먼트를 하나씩 디비든 레파지토리에 삽입
                    .forEach(e -> {
                        boolean exits = dividendRepository.existsByCompanyIdAndDate(e.getCompanyId(), e.getDate());
                        if (!exits) {
                            dividendRepository.save(e);
                        }
                    });

            //연속적으로 스크래핑 대상 사이트 서버에 요청을 날리지 않도록 5초 일시정지
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }


    }
}
