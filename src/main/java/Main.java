import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Account account = null;
        boolean running = true;

        while (running) {
            System.out.println("\n===== BANKING MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. View Transaction History");
            System.out.println("6. Test Invalid Operations");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            if (!sc.hasNextInt()) {
                System.out.println("Please enter a number from 1 to 7.");
                sc.nextLine();
                continue;
            }

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        if (account != null) {
                            System.out.println("Account already exists.");
                            break;
                        }

                        System.out.print("Enter account number: ");
                        String number = sc.nextLine();

                        System.out.print("Enter account holder name: ");
                        String name = sc.nextLine();

                        System.out.print("Enter opening balance: ");

                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid balance input.");
                            sc.nextLine();
                            break;
                        }

                        double openingBalance = sc.nextDouble();
                        sc.nextLine();

                        account = new Account(number, name, openingBalance);
                        System.out.println("Account created successfully.");
                        break;

                    case 2:
                        if (account == null) {
                            System.out.println("Please create an account first.");
                            break;
                        }

                        System.out.print("Enter deposit amount: ");

                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid amount.");
                            sc.nextLine();
                            break;
                        }

                        double depositAmount = sc.nextDouble();
                        sc.nextLine();

                        account.deposit(depositAmount);
                        System.out.println("Deposit successful. Balance: "
                                + account.getBalance());
                        break;

                    case 3:
                        if (account == null) {
                            System.out.println("Please create an account first.");
                            break;
                        }

                        System.out.print("Enter withdrawal amount: ");

                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid amount.");
                            sc.nextLine();
                            break;
                        }

                        double withdrawalAmount = sc.nextDouble();
                        sc.nextLine();

                        account.withdraw(withdrawalAmount);
                        System.out.println("Withdrawal successful. Balance: "
                                + account.getBalance());
                        break;

                    case 4:
                        if (account == null) {
                            System.out.println("Please create an account first.");
                            break;
                        }

                        System.out.println("Current balance: "
                                + account.getBalance());
                        break;

                    case 5:
                        if (account == null) {
                            System.out.println("Please create an account first.");
                            break;
                        }

                        List<Transaction> history = account.getTransactions();

                        if (history.isEmpty()) {
                            System.out.println("No transactions yet.");
                        } else {
                            for (Transaction t : history) {
                                System.out.println(
                                    t.getType()
                                    + " | Amount: " + t.getAmount()
                                    + " | Balance after: " + t.getBalanceAfter()
                                    + " | Time: " + t.getTimestamp()
                                );
                            }
                        }
                        break;

                    case 6:
                        if (account == null) {
                            System.out.println("Please create an account first.");
                            break;
                        }

                        System.out.println("Testing invalid operations...");

                        try {
                            account.deposit(Double.NaN);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Deposit rejected: "
                                    + e.getMessage());
                        }

                        System.out.println("Balance after failed deposit: "
                                + account.getBalance());

                        try {
                            account.withdraw(account.getBalance() + 1000.0);
                        } catch (IllegalArgumentException e) {
                            System.out.println("Withdrawal rejected: "
                                    + e.getMessage());
                        }

                        System.out.println("Balance after failed withdrawal: "
                                + account.getBalance());
                        System.out.println("Transaction count: "
                                + account.getTransactions().size());
                        break;

                    case 7:
                        running = false;
                        System.out.println(
                                "Thank you for using our banking system.");
                        break;

                    default:
                        System.out.println("Invalid choice. Select 1-7.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Operation failed: " + e.getMessage());
            }
        }

        sc.close();
    }
}
