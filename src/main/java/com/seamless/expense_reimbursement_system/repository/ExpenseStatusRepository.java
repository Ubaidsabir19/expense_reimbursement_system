package com.seamless.expense_reimbursement_system.repository;

import com.seamless.expense_reimbursement_system.entity.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseStatusRepository extends JpaRepository<ExpenseStatus, Integer> { }
