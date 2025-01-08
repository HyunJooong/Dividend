package choo.stock.service;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.Dividend;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.entity.DividendEntity;
import choo.stock.persist.repository.CompanyRepository;
import choo.stock.persist.repository.DividendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FinanceService {

    private final CompanyRepository companyRepository;
    private final DividendRepository dividendRepository;

    public ScrapedResult getDividendByCompanyName(String companyName) {

        // 회사명 기준으로 회사 정보 조회
        Optional<CompanyEntity> comName = Optional
                .ofNullable(companyRepository.findByName(companyName)
                        .orElseThrow(() -> new RuntimeException("존재하지 않는 회사명입니다.")));
        // 조회된 회사 id로 회사 배당금 조회
        List<DividendEntity> companyDividends = dividendRepository
                .findAllByCompanyId(comName
                        .get()
                        .getId());

        //조회된 회사 정보와 배당금 반환
        List<Dividend> dividends = new ArrayList<>();
        for (DividendEntity entity : companyDividends) {
            dividends.add(Dividend.builder()
                    .date(entity.getDate())
                    .dividend(entity.getDividend())
                    .build());
        }

        return new ScrapedResult(Company
                .builder()
                .ticker(comName.get().getTicker())
                .name(comName.get().getName())
                .build(), dividends);

    }

}
