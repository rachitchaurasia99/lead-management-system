package com.example.lead.management.system.repositories;

import com.example.lead.management.system.dtos.OrderDto;
import com.example.lead.management.system.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findByUserIdAndLeadId(Long userId, Long leadId, Pageable pageable);
    Page<Order> findAll(Pageable pageable);
    Page<Order> findByUserId(Long userId, Pageable pageable);
    Page<Order> findAllByLeadIdAndUserId(Long leadId, Long userId, Pageable pageable);
    Page<Order> findAllByLeadId(Long leadId, Pageable pageable);
}

