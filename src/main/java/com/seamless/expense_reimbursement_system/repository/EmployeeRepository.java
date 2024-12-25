package com.seamless.expense_reimbursement_system.repository;
import com.seamless.expense_reimbursement_system.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> { }
