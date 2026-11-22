package com.example.reporting_service.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name="product_reports", uniqueConstraints=@UniqueConstraint(columnNames={"order_report_id","product_id"}))
public class ProductReport {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(name="order_report_id", nullable=false) private Long orderReportId;
    @Column(name="product_id", nullable=false) private Long productId;
    @Column(name="total_sold", nullable=false) private Integer totalSold;
    @Column(precision=10, scale=2, nullable=false) private BigDecimal revenue;
    @Column(precision=10, scale=2, nullable=false) private BigDecimal cost;
    @Column(precision=10, scale=2, nullable=false) private BigDecimal profit;
    @Transient private Integer currentInventory;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Long getOrderReportId(){return orderReportId;} public void setOrderReportId(Long v){orderReportId=v;}
    public Long getProductId(){return productId;} public void setProductId(Long v){productId=v;}
    public Integer getTotalSold(){return totalSold;} public void setTotalSold(Integer v){totalSold=v;}
    public BigDecimal getRevenue(){return revenue;} public void setRevenue(BigDecimal v){revenue=v;}
    public BigDecimal getCost(){return cost;} public void setCost(BigDecimal v){cost=v;}
    public BigDecimal getProfit(){return profit;} public void setProfit(BigDecimal v){profit=v;}
    public Integer getCurrentInventory(){return currentInventory;} public void setCurrentInventory(Integer v){currentInventory=v;}
}
