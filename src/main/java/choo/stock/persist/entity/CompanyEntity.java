package choo.stock.persist.entity;

import choo.stock.persist.dao.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "COMPANY")
@Getter
@NoArgsConstructor
@ToString
public class CompanyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String ticker;

    public CompanyEntity(Company company){
        this.name = company.getName();
        this.ticker = company.getTicker();
    }
}
