package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Call;
import com.example.lead.management.system.models.Lead;
import com.example.lead.management.system.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CallRepository extends JpaRepository<Call,Long> {
    Page<Call> findAllByUser(User user, Pageable pageable);
    @Query("SELECT c FROM Call c JOIN Contact con on c.id = con.id WHERE con.lead.id = :leadId")
    Page<Call> findAllByLead(Long leadId, Pageable pageable);
}
