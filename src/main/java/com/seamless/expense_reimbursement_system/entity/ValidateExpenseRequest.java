package com.seamless.expense_reimbursement_system.entity;

//import lombok.Getter;
//import lombok.Setter;
//
//@Setter
//@Getter
public class ValidateExpenseRequest {
    private int roleId;
    private int expenseAmount;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(int expenseAmount) {
        this.expenseAmount = expenseAmount;
    }
}
