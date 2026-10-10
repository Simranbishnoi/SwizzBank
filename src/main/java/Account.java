import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
public class Account {
    private String accountNumber;
    private String holderName;
    private double balance;
    private List<Transaction> transactions = new ArrayList<>();
    Account(String accNum,String name,double b) {
        this.accountNumber=accNum;
        this.holderName=name;
       
        if(b<0)
            throw new IllegalArgumentException("Balance negative mat dalo bhai");
         this.balance=b;

         if (accNum==null || accNum.isBlank()){
            throw new IllegalArgumentException("accnum shi dalo");
         }
    }
    public String getAccountNumber() {
    return accountNumber;
}

public String getHolderName() {
    return holderName;
}
public double getBalance() {
    return balance;
}
public List<Transaction> getTransactions() {
    return new ArrayList<>(transactions);
}
public double deposit(double amount){
    if(!Double.isFinite(amount) || amount<=0)
        throw new IllegalArgumentException("Enter >0 and finite amount");
     balance+=amount;
     Transaction t=new Transaction("DEPOSIT",amount,LocalDateTime.now(),balance);
     transactions.add(t);
    return balance;
}
public double  withdraw(double amount){
    if(amount<=0 || !Double.isFinite(amount))//if amount is NaN then Double.NaN always gives False with every comparison so it would have bypassed this without isFinit
        throw new IllegalArgumentException("Enter >0 amount");
    else if(amount>balance){
        throw new IllegalArgumentException("Itne paise to hone chiye");
    }
    else{
       balance-=amount;
        Transaction t=new Transaction("WITHDRAWAL",amount,LocalDateTime.now(),balance);
     transactions.add(t);

    }
    return balance;
}


}
