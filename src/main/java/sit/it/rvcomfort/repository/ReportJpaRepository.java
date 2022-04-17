package sit.it.rvcomfort.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sit.it.rvcomfort.model.entity.Report;

@Repository
public interface ReportJpaRepository extends JpaRepository<Report, Integer> {

}
