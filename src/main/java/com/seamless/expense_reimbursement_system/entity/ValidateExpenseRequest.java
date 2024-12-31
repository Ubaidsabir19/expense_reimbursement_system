package com.seamless.expense_reimbursement_system.entity;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateExpenseRequest {
    private int roleId;
    private int expenseAmount;
}
