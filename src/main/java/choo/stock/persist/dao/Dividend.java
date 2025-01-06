package choo.stock.persist.dao;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Dividend {
    private LocalDate date;
    private String dividend;
}
