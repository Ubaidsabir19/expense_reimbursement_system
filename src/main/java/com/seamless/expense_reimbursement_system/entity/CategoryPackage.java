package com.seamless.expense_reimbursement_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
}
