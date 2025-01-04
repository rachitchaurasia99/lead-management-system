package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Long> {
    Page<Lead> findAllByName(String name, Pageable pageable);

    Page<Lead> findByUserAndName(User user, String name, Pageable pageable);

    Page<Lead> findAllByUser(User user, Pageable pageable);
    List<Lead> findAllByUser(User user);

    @Query("select l from Lead l where l.id IN :leadIds")
    List<Lead> findAllByIds(List<Long> leadIds);
}
