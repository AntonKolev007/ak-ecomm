package com.ecommerce.project.repositories;

import com.ecommerce.project.model.RoleRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRequestRepository extends JpaRepository<RoleRequest, Long> {
    List<RoleRequest> findByStatus(String status);
    List<RoleRequest> findByUserId(Long userId);
}
