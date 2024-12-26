package com.seamless.expense_reimbursement_system.repository;

import com.seamless.expense_reimbursement_system.entity.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ExpenseStatusRepository extends JpaRepository<ExpenseStatus, Integer> {
    @Query("SELECT e FROM ExpenseStatus e WHERE e.name = :name")
    Optional<ExpenseStatus> findByName(@Param("name") String name);
}
