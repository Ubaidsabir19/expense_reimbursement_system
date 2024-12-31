package com.seamless.expense_reimbursement_system.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "RoleCategoryPackage")
public class RoleCategoryPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "category_package_id", referencedColumnName = "id")
    private CategoryPackage categoryPackage;
}
