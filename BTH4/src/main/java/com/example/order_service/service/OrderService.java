package com.example.order_service.service;

import com.example.order_service.dto.CreateOrderRequest;
import com.example.order_service.entity.Order;
import com.example.order_service.entity.OrderItem;
import com.example.order_service.repository.OrderRepository;
import com.example.order_service.repository.OrderItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orders;
    private final OrderItemRepository items;
    private final IntegrationService integration;

    public OrderService(OrderRepository orders, OrderItemRepository items, IntegrationService integration){
        this.orders=orders; this.items=items; this.integration=integration;
    }

    public List<Order> findAll(){return orders.findAll();}
    public Order findById(Long id){return orders.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy đơn hàng: "+id));}

    @Transactional
    public Order create(CreateOrderRequest request, String token) {
        if(request.getCustomerName()==null || request.getCustomerEmail()==null || request.getItems()==null || request.getItems().isEmpty())
            throw new IllegalArgumentException("Thiếu thông tin khách hàng hoặc danh sách sản phẩm");
        Order order=new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setStatus("pending");
        order.setTotalAmount(BigDecimal.ZERO);
        order=orders.save(order);
        BigDecimal total=BigDecimal.ZERO;
        for(CreateOrderRequest.ItemRequest input:request.getItems()){
            if(input.getQuantity()==null || input.getQuantity()<=0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            var product=integration.getProduct(input.getProductId(),token);
            Integer stock=((Number)product.get("quantity")).intValue();
            if(stock<input.getQuantity()) throw new IllegalArgumentException("Sản phẩm "+input.getProductId()+" không đủ tồn kho. Còn: "+stock);
            BigDecimal price=new BigDecimal(product.get("price").toString());
            OrderItem item=new OrderItem();
            item.setOrder(order); item.setProductId(input.getProductId());
            item.setProductName(product.get("name").toString()); item.setQuantity(input.getQuantity());
            item.setUnitPrice(price); item.setTotalPrice(price.multiply(BigDecimal.valueOf(input.getQuantity())));
            order.getItems().add(item); total=total.add(item.getTotalPrice());
        }
        order.setTotalAmount(total);
        return orders.save(order);
    }

    public Order updateStatus(Long id,String status){
        if(!List.of("pending","completed","cancelled").contains(status)) throw new IllegalArgumentException("Trạng thái chỉ gồm pending, completed, cancelled");
        Order order=findById(id); order.setStatus(status); return orders.save(order);
    }
    public void delete(Long id){orders.delete(findById(id));}
    public List<OrderItem> findAllItems(){return items.findAll();}
    public OrderItem findItem(Long id){return items.findById(id).orElseThrow(()->new RuntimeException("Không tìm thấy order item: "+id));}

    @Transactional
    public OrderItem addItem(Long orderId,Long productId,Integer quantity,String token){
        if(quantity==null || quantity<=0) throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
        Order order=findById(orderId);
        var product=integration.getProduct(productId,token);
        int stock=((Number)product.get("quantity")).intValue();
        if(stock<quantity) throw new IllegalArgumentException("Không đủ tồn kho. Còn: "+stock);
        BigDecimal price=new BigDecimal(product.get("price").toString());
        OrderItem item=new OrderItem(); item.setOrder(order); item.setProductId(productId);
        item.setProductName(product.get("name").toString()); item.setQuantity(quantity);
        item.setUnitPrice(price); item.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        order.getItems().add(item); order.setTotalAmount(order.getTotalAmount().add(item.getTotalPrice()));
        orders.save(order); return item;
    }
    @Transactional
    public OrderItem updateItem(Long id,Long productId,Integer quantity,String token){
        OrderItem item=findItem(id); Order order=item.getOrder();
        var product=integration.getProduct(productId,token);
        int stock=((Number)product.get("quantity")).intValue();
        if(quantity==null || quantity<=0 || stock<quantity) throw new IllegalArgumentException("Số lượng không hợp lệ hoặc không đủ tồn kho");
        BigDecimal old=item.getTotalPrice(), price=new BigDecimal(product.get("price").toString());
        item.setProductId(productId); item.setProductName(product.get("name").toString());
        item.setQuantity(quantity); item.setUnitPrice(price); item.setTotalPrice(price.multiply(BigDecimal.valueOf(quantity)));
        order.setTotalAmount(order.getTotalAmount().subtract(old).add(item.getTotalPrice()));
        orders.save(order); return items.save(item);
    }
    @Transactional
    public void deleteItem(Long id){
        OrderItem item=findItem(id); Order order=item.getOrder();
        order.setTotalAmount(order.getTotalAmount().subtract(item.getTotalPrice()));
        order.getItems().remove(item); items.delete(item); orders.save(order);
    }
}