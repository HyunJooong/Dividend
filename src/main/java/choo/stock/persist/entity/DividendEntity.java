package choo.stock.persist.entity;

import choo.stock.persist.dao.Dividend;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
@Entity(name = "DIVIDEND")
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"companyId", "date"}
                )
        }
)
public class DividendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long companyId;

    private LocalDate date;

    private String dividend;

    public DividendEntity(Long companyId, Dividend dividend) {
        this.companyId = companyId;
        this.date = dividend.getDate();
        this.dividend = dividend.getDividend();
    }
}
