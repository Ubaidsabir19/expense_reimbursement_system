package com.seamless.expense_reimbursement_system.repository;
import com.seamless.expense_reimbursement_system.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> { }
