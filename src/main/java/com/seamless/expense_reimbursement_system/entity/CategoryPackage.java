package com.seamless.expense_reimbursement_system.entity;

import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
@Entity
@Table(name = "category_package")
public class CategoryPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "package_name", nullable = false)
    private String packageName;

    @Column(name = "expense_limit", nullable = false)
    private int expenseLimit;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Categories category;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getExpenseLimit() {
        return expenseLimit;
    }

    public void setExpenseLimit(int expenseLimit) {
        this.expenseLimit = expenseLimit;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
