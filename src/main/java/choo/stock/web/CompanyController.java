package choo.stock.web;

import choo.stock.persist.dao.Company;
import choo.stock.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;

@RestController
@RequestMapping("/company")
@AllArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping("/autoComplete")
    public ResponseEntity<?> autoCompleteCompany() {
        return null;
    }

    @GetMapping
    public ResponseEntity<?> searchCompany() {
        return null;
    }

    @PostMapping
    public ResponseEntity<?> addCompany(@RequestBody Company company) {
        String ticker = company.getTicker().trim();
        if(ObjectUtils.isEmpty(ticker)){
            throw new RuntimeException("Ticker is empty");
        }

        Company companySave = companyService.save(ticker);
        return ResponseEntity.ok(companySave);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCompany() {
        return null;
    }
}
