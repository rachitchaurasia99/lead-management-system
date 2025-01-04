package com.example.lead.management.system.services;

import com.example.lead.management.system.dtos.LeadDto;
import com.example.lead.management.system.dtos.OrderDto;
import com.example.lead.management.system.mapper.LeadMapper;
import com.example.lead.management.system.mapper.OrderMapper;
import com.example.lead.management.system.models.Order;
import com.example.lead.management.system.repositories.LeadRepository;
import com.example.lead.management.system.repositories.OrderRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserService userService;
    private final LeadRepository leadRepository;
    private final LeadService leadService;

    public OrderService(OrderRepository orderRepository, UserService userService, LeadRepository leadRepository, LeadService leadService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.leadRepository = leadRepository;
        this.leadService = leadService;
    }
    public void save(OrderDto order) {
        order.setUserId(userService.currentUser().get().getId());
        Order orderEntity = OrderMapper.toEntity(order, userService, leadService);
        orderRepository.save(orderEntity);
    }

    public void update(Long id, OrderDto order) {
        Optional<Order> orderEntity = orderRepository.findById(id);
        if (orderEntity.isPresent()) {
            orderEntity.get().setLead(LeadMapper.toEntity(order.getLeadDto(leadService)));
            orderEntity.get().setDescription(order.getDescription());
            orderRepository.save(orderEntity.get());
        }
        else{
            throw new ObjectNotFoundException(orderEntity, "Order Not Found");
        }
    }
    public void delete(Long id) { orderRepository.deleteById(id); }
    public Order findById(Long id) { return orderRepository.findById(id).orElse(null); }
    public Page<OrderDto> findAll(PageRequest pageRequest) { return orderRepository.findAll(pageRequest).map(OrderMapper::toDto); }
    public Page<OrderDto> findAllByLeadId(Long leadId, PageRequest pageRequest) { return orderRepository.findAllByLeadId(leadId, pageRequest).map(OrderMapper::toDto); }
    public Page<OrderDto> findAllByUserId(Long leadId, Long userId, PageRequest pageRequest) {
        if (leadId == null) {
            return orderRepository.findByUserId(userId, pageRequest).map(OrderMapper::toDto);
        } else {
            return orderRepository.findAllByLeadIdAndUserId(leadId, userId, pageRequest).map(OrderMapper::toDto);
        }
    }
}
