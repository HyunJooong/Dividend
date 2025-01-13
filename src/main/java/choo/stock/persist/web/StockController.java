package choo.stock.persist.web;

import choo.stock.persist.dao.ScrapedResult;
import choo.stock.persist.service.FinanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/finance")
@AllArgsConstructor
public class StockController {

    private final FinanceService financeService;

    @GetMapping("/dividend/{companyName}")
    public ResponseEntity<?> searchFinance(@PathVariable("companyName") String companyName) {
        ScrapedResult dividendByCompanyName = financeService.getDividendByCompanyName(companyName);

        return ResponseEntity.ok(dividendByCompanyName);
    }


}
