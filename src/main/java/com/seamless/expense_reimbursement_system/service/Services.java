package com.seamless.expense_reimbursement_system.service;
import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.exception_helper.ResourceNotFoundException;
import com.seamless.expense_reimbursement_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @Autowired
    public CategoryPackageRepository categoryPackageRepository;
    @Autowired
    public RoleCategoryPackageRepository roleCategoryPackageRepository;

    // get all employees
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    // get all roles
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    // create employee
    public Employee createEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    // create role
    public Role createRole(Role role){
        role.setStatus((byte) 1);
        return roleRepository.save(role);
    }

    // create category
    public Categories createCategories(Categories categories){
        categories.setStatus((byte) 1);
        return categoriesRepository.save(categories);
    }

    // Expense Creation by Employee Method
    public ResponseEntity<Object> createExpense(int employeeId, Expense expense) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + employeeId));

        RoleCategoryPackage rcPkg = roleCategoryPackageRepository.findById(employee.getRole().getId())
                .orElseThrow(() -> new ResourceNotFoundException("No RoleCategoryPackage found for role ID: " + employee.getRole().getId()));

        // Get category package
        CategoryPackage cPkg = rcPkg.getCategoryPackage();
        int totalAvailedAmount = expenseRepository.findTotalExpensesByEmployeeId(employeeId);
        int remainingLimit = cPkg.getExpenseLimit() - totalAvailedAmount;

        // Set employee and default values
        expense.setApprovalDate(null);
        expense.setEmployee(employee);

        // Check if expense exceeds allowed limit for the role
        if (expense.getAmount() > cPkg.getExpenseLimit()) {
            ExpenseStatus rejectedStatus = expenseStatusRepository.findById(3)
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: 3"));
            expense.setStatus(rejectedStatus);
            expenseRepository.save(expense);
            return ResponseEntity.badRequest().body("Expense amount exceeds the allowed limit for the role: " + employee.getRole().getName());
        }

        // Check if expense exceeds the remaining limit
        if (expense.getAmount() > remainingLimit) {
            ExpenseStatus rejectedStatus = expenseStatusRepository.findById(3)
                    .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: 3"));
            expense.setStatus(rejectedStatus);
            expenseRepository.save(expense);
            return ResponseEntity.badRequest().body("Expense amount exceeds the remaining limit. You can only avail up to: " + remainingLimit);
        }

        // Assign pending status 1
        ExpenseStatus pendingStatus = expenseStatusRepository.findById(1)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: 1"));
        expense.setStatus(pendingStatus);
        expenseRepository.save(expense);
        return ResponseEntity.ok("Expense submitted successfully and is pending approval.");
    }

    // Getting list by Manager
    public List<Expense> getAllExpanses(){
        return expenseRepository.findAll();
    }

    // Get by status
    public List<Expense> getExpanseByStatus(int statusId) {
        return expenseRepository.findByStatusId(statusId);
    }

    // Get expanses status by employee id & date-range
    public List<Expense> getExpensesStatus(int employeeId, LocalDateTime date) {
        if (date == null) {
            throw new IllegalArgumentException("Date must not be null");
        }
        return expenseRepository.findByEmployeeIdAndSubmitDateAfter(employeeId, date);
    }

    // update expanse status
    public void updateExpenseStatus(int expenseId, int statusId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with ID: " + expenseId));

        ExpenseStatus expenseStatus = expenseStatusRepository.findById(statusId)
                .orElseThrow(() -> new ResourceNotFoundException("Status not found with ID: " + statusId));

        expense.setApprovalDate(LocalDateTime.now());
        expense.setStatus(expenseStatus);
        expenseRepository.save(expense);
    }

    // Create Category Package
    public CategoryPackage addCategoryPackage(CategoryPackage categoryPackage){
        return categoryPackageRepository.save(categoryPackage);
    }

    // Create Category Against Role
    public RoleCategoryPackage addRoleCategoryPackage(RoleCategoryPackage roleCategoryPackage){
        return roleCategoryPackageRepository.save(roleCategoryPackage);
    }

    // Validate Expanse amount
    public boolean validateExpense(int roleId, int expenseAmount) {
        RoleCategoryPackage roleCategoryPackage = roleCategoryPackageRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("No RoleCategoryPackage found for role ID: " + roleId));

        CategoryPackage categoryPackage = roleCategoryPackage.getCategoryPackage();

        if (expenseAmount > categoryPackage.getExpenseLimit()) {
            throw new IllegalArgumentException("Expense amount exceeds the allowed limit for the role.");
        }
        return true;
    }


}