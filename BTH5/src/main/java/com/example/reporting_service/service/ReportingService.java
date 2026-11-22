package com.example.reporting_service.service;

import com.example.reporting_service.dto.*;
import com.example.reporting_service.entity.*;
import com.example.reporting_service.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;

@Service
public class ReportingService {
    private static final BigDecimal ZERO=BigDecimal.ZERO;
    private final OrderReportRepository orders;
    private final ProductReportRepository products;
    private final IntegrationService integration;
    public ReportingService(OrderReportRepository orders,ProductReportRepository products,IntegrationService integration){this.orders=orders;this.products=products;this.integration=integration;}

    public List<OrderReport> getOrderReports(){return orders.findAll();}
    public OrderReport getOrderReport(Long id){return orders.findById(id).orElseThrow(()->new NoSuchElementException("Không tìm thấy báo cáo đơn hàng: "+id));}
    public List<ProductReport> getProductReports(String token){return products.findAll().stream().map(p->withInventory(p,token)).toList();}
    public ProductReport getProductReport(Long id,String token){return withInventory(products.findById(id).orElseThrow(()->new NoSuchElementException("Không tìm thấy báo cáo sản phẩm: "+id)),token);}

    @Transactional
    public OrderReport createOrderReport(CreateOrderReportRequest request,String token){
        requireId(request==null?null:request.orderId(),"orderId");
        Map<String,Object> order=integration.getOrder(request.orderId(),token);
        List<Map<String,Object>> items=items(order);
        BigDecimal revenue=number(order.get("totalAmount"));
        if(revenue==null) revenue=items.stream().map(i->number(i.get("totalPrice"))).filter(Objects::nonNull).reduce(ZERO,BigDecimal::add);
        BigDecimal cost=items.stream().map(i->itemCost(i,token)).reduce(ZERO,BigDecimal::add);
        OrderReport report=orders.findByOrderId(request.orderId()).orElseGet(OrderReport::new);
        report.setOrderId(request.orderId()); report.setTotalRevenue(revenue); report.setTotalCost(cost); report.setTotalProfit(revenue.subtract(cost));
        return orders.save(report);
    }
    @Transactional
    public ProductReport createProductReport(CreateProductReportRequest request,String token){
        requireId(request==null?null:request.orderId(),"orderId"); requireId(request==null?null:request.productId(),"productId");
        Map<String,Object> order=integration.getOrder(request.orderId(),token);
        Map<String,Object> product=integration.getProduct(request.productId(),token);
        List<Map<String,Object>> matching=items(order).stream().filter(i->request.productId().equals(longValue(i.get("productId")))).toList();
        if(matching.isEmpty()) throw new IllegalArgumentException("Sản phẩm không có trong đơn hàng đã chọn");
        OrderReport orderReport=orders.findByOrderId(request.orderId()).orElseGet(()->createOrderReport(new CreateOrderReportRequest(request.orderId()),token));
        int sold=matching.stream().mapToInt(i->intValue(i.get("quantity"))).sum();
        BigDecimal revenue=matching.stream().map(i->number(i.get("totalPrice"))).filter(Objects::nonNull).reduce(ZERO,BigDecimal::add);
        if(revenue.signum()==0){BigDecimal price=number(product.get("price")); revenue=(price==null?ZERO:price).multiply(BigDecimal.valueOf(sold));}
        BigDecimal cost=matching.stream().map(i->itemCost(i,token)).reduce(ZERO,BigDecimal::add);
        ProductReport report=products.findByOrderReportId(orderReport.getId()).stream().filter(p->p.getProductId().equals(request.productId())).findFirst().orElseGet(ProductReport::new);
        report.setOrderReportId(orderReport.getId()); report.setProductId(request.productId()); report.setTotalSold(sold);
        report.setRevenue(revenue); report.setCost(cost); report.setProfit(revenue.subtract(cost));
        report.setCurrentInventory(intValue(product.get("quantity")));
        return products.save(report);
    }
    @Transactional public void deleteOrderReport(Long id){OrderReport report=getOrderReport(id);products.deleteByOrderReportId(id);orders.delete(report);}
    @Transactional public void deleteProductReport(Long id){products.delete(getProductReport(id));}

    private ProductReport withInventory(ProductReport report,String token){
        Map<String,Object> product=integration.getProduct(report.getProductId(),token);
        report.setCurrentInventory(intValue(product.get("quantity")));
        return report;
    }

    private BigDecimal itemCost(Map<String,Object> item,String token){
        Map<String,Object> product=integration.getProduct(longValue(item.get("productId")),token);
        Object unit=first(product,"cost","costPrice","cost_price","purchasePrice","purchase_price");
        BigDecimal unitCost=number(unit); if(unitCost==null) unitCost=ZERO;
        return unitCost.multiply(BigDecimal.valueOf(intValue(item.get("quantity"))));
    }
    @SuppressWarnings("unchecked") private List<Map<String,Object>> items(Map<String,Object> order){Object raw=order.get("items"); if(!(raw instanceof List<?> list)) return List.of(); return (List<Map<String,Object>>)list;}
    private static Object first(Map<String,Object> map,String...keys){for(String key:keys) if(map.containsKey(key)&&map.get(key)!=null)return map.get(key);return null;}
    private static BigDecimal number(Object value){return value==null?null:new BigDecimal(value.toString());}
    private static Long longValue(Object value){return value instanceof Number n?n.longValue():value==null?null:Long.valueOf(value.toString());}
    private static int intValue(Object value){return value instanceof Number n?n.intValue():value==null?0:Integer.parseInt(value.toString());}
    private static void requireId(Long id,String name){if(id==null||id<=0) throw new IllegalArgumentException(name+" phải là số nguyên dương");}
}
