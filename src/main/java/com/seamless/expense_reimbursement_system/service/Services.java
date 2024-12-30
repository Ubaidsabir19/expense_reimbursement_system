package com.seamless.expense_reimbursement_system.service;
import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    public Expense createExpense(int employeeId, Expense expense) {

        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with ID: " + employeeId));

            // Get role id with the help of employee
            RoleCategoryPackage rcPkg = roleCategoryPackageRepository.findById(employee.getRole().getId())
                    .orElseThrow(() -> new EntityNotFoundException("No RoleCategoryPackage found for role ID: " + employee.getRole().getId()));

            // get Category Pkg
            CategoryPackage cPkg = rcPkg.getCategoryPackage();

            // If enter amount > Expense Limit against role
            if (expense.getAmount() > cPkg.getExpenseLimit()) {
                throw new IllegalArgumentException(
                        "Expense amount exceeds the allowed limit for the role: " + employee.getRole().getName());
            }

            ExpenseStatus newStatus = new ExpenseStatus();
            newStatus.setName("Pending");
            newStatus.setStatus((byte) 1);
            ExpenseStatus savedStatus = expenseStatusRepository.save(newStatus);

            expense.setStatus(savedStatus);
            expense.setEmployee(employee);
            expense.setApprovalDate(null);
            return expenseRepository.save(expense);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    // Getting list by Manager
    public List<Expense> getAllExpanses(){
        return expenseRepository.findAll();
    }

    // Get by status
    // Use employee id ------- modification
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
    public void updateExpenseStatus(int expenseId, int statusId, String name) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));

        ExpenseStatus expenseStatus = expenseStatusRepository.findById(statusId)
                .orElseThrow(() -> new IllegalArgumentException("Status not found with ID: " + statusId));

        if (name != null && !name.isEmpty()) {
            expenseStatus.setName(name);
            expenseStatusRepository.save(expenseStatus);
        }

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

    public boolean validateExpense(int roleId, int expenseAmount) {
        RoleCategoryPackage roleCategoryPackage = roleCategoryPackageRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("No RoleCategoryPackage found for role ID: " + roleId));

        CategoryPackage categoryPackage = roleCategoryPackage.getCategoryPackage();

        if (expenseAmount > categoryPackage.getExpenseLimit()) {
            throw new IllegalArgumentException("Expense amount exceeds the allowed limit for the role.");
        }
        return true;
    }

}