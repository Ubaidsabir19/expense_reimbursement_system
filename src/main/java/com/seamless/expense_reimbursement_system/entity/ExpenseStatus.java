package com.seamless.expense_reimbursement_system.entity;

import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
@Entity
@Table(name = "expense_status")
public class ExpenseStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "status")
    private byte status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }
}
