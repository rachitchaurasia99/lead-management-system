package com.example.lead.management.system.repositories;

import com.example.lead.management.system.models.Call;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallRepository extends JpaRepository<Call,Long> {
}
