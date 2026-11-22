package com.example.reporting_service.repository;
import com.example.reporting_service.entity.OrderReport;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface OrderReportRepository extends JpaRepository<OrderReport, Long> { Optional<OrderReport> findByOrderId(Long orderId); }
