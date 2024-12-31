package com.seamless.expense_reimbursement_system.rest;
import com.seamless.expense_reimbursement_system.entity.*;
import com.seamless.expense_reimbursement_system.service.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    public Services service;

    @GetMapping("/allEmployees")
    public ResponseEntity<Object> getAllEmployee(){
        return ResponseEntity.ok(service.getAllEmployees());
    }

    @GetMapping("/allRoles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return ResponseEntity.ok(service.getAllRoles());
    }

    // Post End Points
    @PostMapping("/createEmployee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee){
        return ResponseEntity.ok(service.createEmployee(employee));
    }

    @PostMapping("/createRole")
    public ResponseEntity<Role> createRole(@RequestBody Role role){
        return ResponseEntity.ok(service.createRole(role));
    }

    @PostMapping("/createCategory")
    public ResponseEntity<Categories> createCategories(@RequestBody Categories categories){
        return ResponseEntity.ok(service.createCategories(categories));
    }

    // Expense Creation
    @PostMapping("employee/{employeeId}/expense")
    public ResponseEntity<Object> createExpenseStatus(@PathVariable int employeeId, @RequestBody Expense expense){
        if (expense.getSubmitDate() == null) {
            expense.setSubmitDate(LocalDateTime.now());
        }
        ResponseEntity<Object> savedExpense = service.createExpense(employeeId,expense);
        return ResponseEntity.ok(savedExpense);
    }

    // Get only pending status expanses
    @GetMapping("/allExpanses")
    public ResponseEntity<List<Expense>> getAllExpanses() {
        List<Expense> expenses = service.getAllExpanses().stream()
                .filter(n -> n.getStatus() != null && "Pending".equals(n.getStatus().getName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(expenses);
    }

    // Get Expanses by Status
    @GetMapping("/expanseByStatus")
    public ResponseEntity<List<Expense>> getExpanseByStatus(@RequestParam int statusId) {
        return ResponseEntity.ok(service.getExpanseByStatus(statusId));
    }

    // Get Expanse Status with employee id and date range
    @GetMapping("/expanseStatusByDate")
    public ResponseEntity<List<Expense>> getExpensesStatus(
            @RequestParam Integer employeeId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime date) {
        if (employeeId == 0 || date == null) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
        return ResponseEntity.ok(service.getExpensesStatus(employeeId, date));
    }

    // Update expense status
    @PatchMapping("/updateExpenseStatus")
    public ResponseEntity<String> updateExpenseStatus(@RequestBody Map<String, String> request) {
        try {
            String expenseId = request.get("expenseId");
            String statusId = request.get("status_id");

            service.updateExpenseStatus(Integer.parseInt(expenseId), Integer.parseInt(statusId));
            return ResponseEntity.ok("Status Updated Successfully");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Create Category Pkg
    @PostMapping("/createCategoryPackage")
    public ResponseEntity<CategoryPackage> createCategoryPackage(@RequestBody CategoryPackage categoryPackage){
        try {
            return ResponseEntity.ok(service.addCategoryPackage(categoryPackage));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Create category Against Pkg
    @PostMapping("/createCategoryAgainstPackage")
    public ResponseEntity<RoleCategoryPackage> createRoleCategoryPackage(@RequestBody RoleCategoryPackage roleCategoryPackage){
        try {
            return ResponseEntity.ok(service.addRoleCategoryPackage(roleCategoryPackage));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Validate Expanse
    @PostMapping("/validateExpense")
    public boolean validateExpense(@RequestBody ValidateExpenseRequest request) {
        try {
            return service.validateExpense(request.getRoleId(), request.getExpenseAmount());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

}