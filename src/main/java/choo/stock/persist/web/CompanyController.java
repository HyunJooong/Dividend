package choo.stock.persist.web;

import choo.stock.persist.dao.Company;
import choo.stock.persist.entity.CompanyEntity;
import choo.stock.persist.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    /**
     * 자동완성 조회
     * @param keyword
     * @return
     */
    @GetMapping("/autoComplete")
    public ResponseEntity<?> autoCompleteCompany(@RequestParam String keyword) {

        List<String> result = companyService.autoComplete(keyword);
        return ResponseEntity.ok(result);
    }

    /**
     * 회사 정보 모두 조회
     *
     * @return
     */
    @GetMapping("/all-companies")
    public Page<CompanyEntity> searchCompany(final Pageable pageable) {
        Page<CompanyEntity> companies = companyService.getAllCompany(pageable);

        return companies;
    }

    /**
     * 회사 정보와 배당금 정보 추가
     *
     * @param company
     * @return
     */
    @PostMapping("/save")
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        String ticker = company.getTicker().trim();
        if (ObjectUtils.isEmpty(ticker)) {
            throw new RuntimeException("Ticker is empty");
        }

        Company companySave = companyService.save(ticker);
        companyService.addAutoCompleteKeyword(companySave.getName());
        return ResponseEntity.ok(companySave);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
