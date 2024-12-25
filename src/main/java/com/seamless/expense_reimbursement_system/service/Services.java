package com.seamless.expense_reimbursement_system.service;
import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.repository.*;
import jakarta.transaction.Status;
import jdk.jshell.Snippet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class Services {

    @Autowired
    public EmployeeRepository employeeRepository;
    @Autowired
    public RoleRepository roleRepository;
    @Autowired
    public ExpenseRepository expenseRepository;
    @Autowired
    public CategoriesRepository categoriesRepository;
    @Autowired
    public ExpenseStatusRepository expenseStatusRepository;

    // get Functions
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    // Create Functions
    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Role createRole(Role role){
        return roleRepository.save(role);
    }

    public Categories createCategories(Categories categories){
        return categoriesRepository.save(categories);
    }

    public ExpenseStatus createExpenseStatus(ExpenseStatus expenseStatus){
        return expenseStatusRepository.save(expenseStatus);
    }


    // Expense Creation by Employee Method
    public Expense createExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    // Getting list by Manager
    public List<Expense> getAllExpanses(){
        return expenseRepository.findAll();
    }

    // Get by status
    public List<Expense> getExpanseByStatus(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Status name must not be null or empty");
        }
        return expenseRepository.findByStatusName(name);
    }

    // Get expanses status by employee id & date-range
    public List<Expense> getExpensesStatus(int employeeId, LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        return expenseRepository.findByEmployeeIdAndSubmitDateAfter(employeeId, date);
    }

    // update expanse status
    public Expense updateExpenseStatus(int expenseId, ExpenseStatus status) {
        Optional<Expense> optionalExpense = expenseRepository.findById(expenseId);

        if (optionalExpense.isEmpty()) {
            throw new IllegalArgumentException("Expense not found");
        }

        Expense expense = optionalExpense.get();
        expense.setStatus(status);
        return expenseRepository.save(expense);
    }



}
