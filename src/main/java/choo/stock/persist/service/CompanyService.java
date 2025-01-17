package choo.stock.persist.service;

import choo.stock.persist.dao.Company;
import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.entity.DividendEntity;
import choo.stock.persist.repository.CompanyRepository;
import choo.stock.persist.repository.DividendRepository;
import choo.stock.persist.scraper.Scraper;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.Trie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyService {

    private final Trie trie;
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

    /**
     * 회사 정보와 배당금 정보 추가
     *
     * @param ticker
     * @return
     */
    private Company storeCompanyAndDividend(String ticker) {
        //ticker를 기준으로 회사를 스크래핑
        Company company = yahooFinanceScraper.scrapCompanyByTicker(ticker);
        if (ObjectUtils.isEmpty(company)) {
            throw new RuntimeException("failed to find company" + ticker);
        }
        //해당 회사가 존재할 경우, 회사의 배당금 정보를 스크래핑
        ScrapedResult scrapedResult = yahooFinanceScraper.scrap(company);

        //스크래핑 결과
        CompanyEntity companyEntity = companyRepository
                .save(new CompanyEntity(company));
        List<DividendEntity> dividendEntities = scrapedResult
                .getDividends()
                .stream()
                .map(e -> new DividendEntity(companyEntity.getId(), e))
                .collect(Collectors.toList());

        dividendRepository.saveAll(dividendEntities);


        return company;
    }

    //Pageable 처리
    public Page<CompanyEntity> getAllCompany(final Pageable pageable) {

        Page<CompanyEntity> getAllCompany = companyRepository.findAll(pageable);
        return getAllCompany;
    }

    // 데이터 저장
    public void addAutoCompleteKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Keyword cannot be null or empty");
        }
        // Add the keyword to the trie
        this.trie.put(keyword, null);
    }

    //데이터 조회
    public List<String> autoComplete(String keyword) {
        return (List<String>) this.trie.prefixMap(keyword).keySet()
                .stream()
                .collect(Collectors.toList());
    }

    //데이터 삭제
    public void deleteAutoCompleteKeyword(String keyword) {
        this.trie.remove(keyword);
    }

    //이름 자동완성 조회
    public List<String> getCompanyNameByKeyword(String keyword) {
        Pageable limit = PageRequest.of(0,5); // pageable 5개까지...
        Page<CompanyEntity> companyEntities = companyRepository.findByNameStartingWithIgnoreCase(keyword, limit);
        return companyEntities.stream()
                .map(CompanyEntity::getName)
                .collect(Collectors.toList());
    }


}
