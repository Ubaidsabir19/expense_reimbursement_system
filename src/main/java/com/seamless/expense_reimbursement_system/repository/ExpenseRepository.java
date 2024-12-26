package com.seamless.expense_reimbursement_system.repository;
import com.seamless.expense_reimbursement_system.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;


public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT e FROM Expense e WHERE LOWER(e.status.name) = LOWER(:name)")
    List<Expense> findByStatusName(@Param("name") String name);

    @Query("SELECT e FROM Expense e WHERE e.employee.id = :employeeId AND e.submitDate >= :date")
    List<Expense> findByEmployeeIdAndSubmitDateAfter(@Param("employeeId") int employeeId,
                                                     @Param("date") LocalDateTime date);
}
