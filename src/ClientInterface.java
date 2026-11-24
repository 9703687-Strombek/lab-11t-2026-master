

import exceptions.AccountException;
import exceptions.AccountNotFoundException;
import exceptions.DuplicateUserIdException;
import exceptions.InvalidAmountException;
import exceptions.InvalidUserIdException;
import exceptions.NonZeroBalanceException;
import exceptions.PasswordMismatchException;
import exceptions.WrongPasswordException;
import model.BankAccount;
import model.CheckingAccount;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientInterface {

    private final List<BankAccount> accounts = new ArrayList<>();

    private final Scanner scanner = new Scanner(System.in);

    private List<String> options = List.of(
            "1 - Create account",
            "2 - Delete account",
            "3 - Make account deposit",
            "4 - Make account withdrawal",
            "5 - Print account info",
            "6 - Exit"
    );

    public void start() throws IOException {

        String option;
        while (true) {
            printMainMenu();
            option = scanner.next();

            int choice;
            try {
                choice = Integer.parseInt(option);
            } catch (NumberFormatException e) {
                System.err.println("Помилка, виберіть від 1 до 6\n");
                continue;
            }

            if (choice == 6) {
                return;
            }

            // Усі виняткові ситуації обробляємо тут: виводимо повідомлення
            // у потік помилок та повертаємось у головне меню
            try {
                switch (choice) {
                    case 1:
                        addNewAccount();
                        break;
                    case 2:
                        deleteAccount();
                        break;
                    case 3:
                        deposit();
                        break;
                    case 4:
                        withdraw();
                        break;
                    case 5:
                        printInfo();
                        break;
                    default:
                        System.err.println("Error: please enter a valid choice (1 thru 6)\n");
                }
            } catch (AccountException e) {
                System.err.println("Error: " + e.getMessage() + "\n");
            }
        }

    }

    private void printMainMenu() throws IOException {
        System.out.println("\n****************");
        System.out.println("Banking system: ");
        System.out.println("****************");
        options.forEach(System.out::println);
        System.out.print("\n>>> Choose your option: ");
    }

    private void addNewAccount() {

        /*
         * В даному методі ви повинні передбачити перевірки для
         * коректності полів нового акаунту та створити новий акаунт, якщо всі дані
         * є коректними
         */

        System.out.println("\n****************");
        System.out.println("Creating new account: ");
        System.out.println("****************");

        System.out.print(">>> Enter customer name: ");
        var name = scanner.next();
        System.out.print(">>> Enter user id: ");
        var userId = parseUserId(scanner.next());
        System.out.print(">>> Enter user password: ");
        String password = scanner.next();
        System.out.print(">>> Confirm password: ");
        String confirmPassword = scanner.next();

        if (findAccount(userId) != null) {
            throw new DuplicateUserIdException("Account with userId " + userId + " already exists");
        }
        if (!password.equals(confirmPassword)) {
            throw new PasswordMismatchException("Password and confirmation do not match");
        }

        accounts.add(new CheckingAccount(name, userId, password));
        System.out.println("\nAccount created successfully.");
    }

    private void deleteAccount() {

        /*
         * В даному методі ви повинні передбачити перевірки для
         * коректності полів для видалення акаунту та видалити акаунт
         */

        System.out.println("\n****************");
        System.out.println("Deleting account: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = authorize(userId, password);
        if (account.getAccountBalance() != 0) {
            throw new NonZeroBalanceException(
                    "Cannot delete account with non-zero balance: " + account.getAccountBalance());
        }
        accounts.remove(account);
        System.out.println("\nAccount deleted successfully.");
    }

    private void deposit() {

        /*
         * В даному методі ви повинні передбачити перевірки для
         * коректності полів та зробити внесок
         */

        System.out.println("\n****************");
        System.out.println("Making a deposit: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amount = scanner.next();

        BankAccount account = authorize(userId, password);
        account.deposit(parseAmount(amount));
        System.out.println("\nDeposit completed successfully. Balance: " + account.getAccountBalance());
    }

    private void withdraw() {

        /*
         * В даному методі ви повинні передбачити перевірки для
         * коректності полів та зробити зняття коштів
         */

        System.out.println("\n****************");
        System.out.println("Making a withdrawal: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();
        System.out.print(">>> Enter amount: ");
        var amount = scanner.next();

        BankAccount account = authorize(userId, password);
        account.withdraw(parseAmount(amount));
        System.out.println("\nWithdrawal completed successfully. Balance: " + account.getAccountBalance());
    }

    private void printInfo() {

        /*
         * Даний метод виводить інформацію щодо рахунку. Ви
         * повинні перевірити наявність рахунку та вивести інформацію
         * стосовно нього
         */

        System.out.println("\n****************");
        System.out.println("Printing account info: ");
        System.out.println("****************");

        System.out.print(">>> Enter user id: ");
        var userId = scanner.next();
        System.out.print(">>> Enter user password: ");
        var password = scanner.next();

        BankAccount account = authorize(userId, password);

        System.out.println("\nAccount info: ");

        System.out.println(" - customer name: " + account.getCustomerName());
        System.out.println(" - account balance: " + account.getAccountBalance());
        System.out.println(" - account type: Checking");
        System.out.println(" - account daily withdrawal limit: "
                + ((CheckingAccount) account).getDailyWithdrawalLimit());

        System.out.print("\nPress Enter to return to main menu...");

        scanner.nextLine();
        scanner.nextLine();
    }


    private Long parseUserId(String text) throws InvalidUserIdException {
        try {
            long id = Long.parseLong(text);
            if (id <= 0) {
                throw new InvalidUserIdException("юзер айді повинен бути позитивним");
            }
            return id;
        } catch (NumberFormatException e) {
            throw new InvalidUserIdException("Юзер айді повинен бути цифрою: " + text);
        }
    }

    private double parseAmount(String text) throws InvalidAmountException {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Сума повинна бути цифрою: " + text);
        }
    }

    private BankAccount findAccount(Long userId) {
        for (BankAccount account : accounts) {
            if (account.getUserId().equals(userId)) {
                return account;
            }
        }
        return null;
    }

    // Знаходить рахунок та перевіряє пароль
    private BankAccount authorize(String userIdText, String password)
            throws AccountNotFoundException, WrongPasswordException {
        BankAccount account = findAccount(parseUserId(userIdText));
        if (account == null) {
            throw new AccountNotFoundException("Аккаунт з юзер id " + userIdText + " не знайдено");
        }
        if (!account.getUserPassword().equals(password)) {
            throw new WrongPasswordException("Неправильний пароль!");
        }
        return account;
    }

}
