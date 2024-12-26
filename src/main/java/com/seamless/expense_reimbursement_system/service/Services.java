package com.seamless.expense_reimbursement_system.service;
import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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


    // Expense Creation by Employee Method
    public Expense createExpense(Expense expense){
        ExpenseStatus newStatus = new ExpenseStatus();
        newStatus.setName("Pending");
        newStatus.setStatus((byte) 0);
        ExpenseStatus savedStatus = expenseStatusRepository.save(newStatus);
        expense.setStatus(savedStatus);
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
    public void updateExpenseStatus(int expenseId, int statusId, String name, byte status) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));

        ExpenseStatus expenseStatus = expenseStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status not found with ID: " + statusId));

        if (name != null && !name.isEmpty()) {
            expenseStatus.setName(name);
            expenseStatus.setStatus(status);
            expenseStatusRepository.save(expenseStatus);
        }

        expense.setStatus(expenseStatus);
        expenseRepository.save(expense);
    }





}
