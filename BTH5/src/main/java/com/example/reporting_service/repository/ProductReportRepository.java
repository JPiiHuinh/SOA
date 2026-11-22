package com.example.reporting_service.repository;
import com.example.reporting_service.entity.ProductReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ProductReportRepository extends JpaRepository<ProductReport, Long> {
    List<ProductReport> findByProductId(Long productId);
    List<ProductReport> findByOrderReportId(Long orderReportId);
    void deleteByOrderReportId(Long orderReportId);
}
