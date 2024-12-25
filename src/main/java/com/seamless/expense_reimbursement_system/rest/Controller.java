package com.seamless.expense_reimbursement_system.rest;

import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.service.Services;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
public class Controller {

    @Autowired
    public Services service;

    @GetMapping("/allEmployee")
    public List<Employee> getAllEmployee(){
        return service.getAllEmployees();
    }

    @GetMapping("/allRoles")
    public List<Role> getAllRoles(){
        return service.getAllRoles();
    }

    // Post End Points
    @PostMapping("/employee")
    public Employee createEmployee(@RequestBody Employee employee){
        return service.createEmployee(employee);
    }

    @PostMapping("/role")
    public Role createRole(@RequestBody Role role){
        return service.createRole(role);
    }

    @PostMapping("/category")
    public Categories createCategories(@RequestBody Categories categories){
        return service.createCategories(categories);
    }

    @PostMapping("/expense-status")
    public ExpenseStatus createExpenseStatus(@RequestBody ExpenseStatus expenseStatus){
        return service.createExpenseStatus(expenseStatus);
    }


    // Expense Creation
    @PostMapping("/expense")
    public ResponseEntity<Expense> createExpenseStatus(@RequestBody Expense expense){
        if (expense.getSubmitDate() == null) {
            expense.setSubmitDate(LocalDateTime.now());
        }
        if (expense.getApprovalDate() == null) {
            expense.setApprovalDate(LocalDateTime.now().minusDays(5));
        }
        Expense savedExpense = service.createExpense(expense);
        return ResponseEntity.ok(savedExpense);
    }

    // Get only pending status expanses
    @GetMapping("/allExpanses")
    public ResponseEntity<List<Expense>> getAllExpanses() {
        List<Expense> expenses = service.getAllExpanses().stream()
                .filter(n -> "Pending".equals(n.getStatus().getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(expenses);
    }

    // Get Expanses by Status
    @GetMapping("/ExpanseByStatus")
    public ResponseEntity<List<Expense>> getExpanseByStatus(@RequestParam String name) {
        List<Expense> filteredExpenses = service.getExpanseByStatus(name);
        return ResponseEntity.ok(filteredExpenses);
    }

    // Get Expanse Status with employee id and date range
    @GetMapping("/ExpanseStatusByDate")
    public ResponseEntity<List<Expense>> getExpensesStatus(
            @RequestParam(required = false) Integer employeeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date) {
//        if (employeeId == 0 || date == null) {
//            return ResponseEntity.badRequest().body(Collections.emptyList());
//        }
        List<Expense> expenses = service.getExpensesStatus(employeeId, date);
        return ResponseEntity.ok(expenses);
    }

    @PatchMapping("/updateExpenseStatus")
    public ResponseEntity<Expense> updateExpenseStatus(
            @RequestParam int expenseId,
            @RequestParam ExpenseStatus status) {

        try {
            Expense updatedExpense = service.updateExpenseStatus(expenseId, status);
            return ResponseEntity.ok(updatedExpense);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }





}
