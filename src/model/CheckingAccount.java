package model;

import exceptions.DailyLimitExceededException;
import exceptions.InsufficientFundsException;

public class CheckingAccount extends BankAccount {

    private double dailyWithdrawalLimit = 300;

    public CheckingAccount(String customerName, Long userId, String userPassword) {
        super(customerName, userId, userPassword);
    }

    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    @Override
    public void deposit(double amount) {
        checkAmount(amount);
        setAccountBalance(getAccountBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        checkAmount(amount);
        if (amount > dailyWithdrawalLimit) {
            throw new DailyLimitExceededException(
                    "Сума " + amount + " більша ніж ліміт у " + dailyWithdrawalLimit);
        }
        if (amount > getAccountBalance()) {
            throw new InsufficientFundsException(
                    "Недостатньо коштів, рахунок: : " + getAccountBalance());
        }
        setAccountBalance(getAccountBalance() - amount);
    }
}
