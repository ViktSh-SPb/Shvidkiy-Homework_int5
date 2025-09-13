package org.example.employee_service.repository;

import org.example.employee_service.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Integer> {
}
