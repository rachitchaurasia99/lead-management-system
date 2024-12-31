package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Lead;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    Page<Lead> findAllByNameContaining(String name, Pageable pageable);
}
