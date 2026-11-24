package model;

import exceptions.InvalidAmountException;
import exceptions.*;

public abstract class BankAccount {

    private String customerName;
    private Long userId;
    private String userPassword;
    private Double accountBalance;

    public BankAccount(String customerName, Long userId, String userPassword) {
        if (userId == null || userId <= 0) {
            throw new InvalidUserIdException("Юзер айді повинен бути позитивним числом");
        }
        checkPassword(userPassword);
        this.customerName = customerName;
        this.userId = userId;
        this.userPassword = userPassword;
        this.accountBalance = 0.0;
    }

    public static void checkPassword(String password) {
        if (password == null || password.length() < 8) {
            throw new InvalidPasswordException("Пароль має містити мінімум 8 символів");
        }
        boolean hasDigit = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                hasDigit = true;
                break;
            }
        }
        if (!hasDigit) {
            throw new InvalidPasswordException("Пароль має містити як мінімум 1 цифру");
        }
    }

    protected static void checkAmount(double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount) || amount <= 0) {
            throw new InvalidAmountException("Кількість має бути більше 0: " + amount);
        }
    }

    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (userId == null || userId <= 0) {
            throw new InvalidUserIdException("Юзер айді повинен бути позитивним числом");
        }
        this.userId = userId;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        checkPassword(userPassword);
        this.userPassword = userPassword;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        if (accountBalance < 0) {
            throw new NegativeBalanceException("Баланс не може бути від'ємним: " + accountBalance);
        }
        this.accountBalance = accountBalance;
    }
}
