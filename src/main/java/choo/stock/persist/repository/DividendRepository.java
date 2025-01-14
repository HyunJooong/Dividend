package choo.stock.persist.repository;

import choo.stock.persist.entity.DividendEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendRepository extends JpaRepository<DividendEntity, Long> {

    List<DividendEntity> findAllByCompanyId(Long companyId);

    boolean existsByCompanyIdAndDate(Long companyId, LocalDate date);
}
