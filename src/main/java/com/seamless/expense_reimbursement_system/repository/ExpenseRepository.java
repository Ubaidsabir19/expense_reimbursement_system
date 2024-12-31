package com.seamless.expense_reimbursement_system.repository;
import com.seamless.expense_reimbursement_system.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Integer> {
    @Query("SELECT e FROM Expense e WHERE e.status.id = :statusId ORDER BY e.submitDate DESC")
    List<Expense> findByStatusId(@Param("statusId") int statusId);

    @Query("SELECT e FROM Expense e WHERE e.employee.id = :employeeId AND e.submitDate >= :date ORDER BY e.submitDate DESC")
    List<Expense> findByEmployeeIdAndSubmitDateAfter(@Param("employeeId") int employeeId,
                                                     @Param("date") LocalDateTime date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.employee.id = :employeeId AND e.status.name IN ('Pending', 'Approved')")
    int findTotalExpensesByEmployeeId(@Param("employeeId") int employeeId);
}
