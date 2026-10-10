import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;
import java.util.List;
class AccountTest{
    @Test 
    void depositIncreaseBalance(){
        Account a=new Account("A101","Simran",5000.0);
        a.deposit(1000.0);
        assertEquals(6000.0,a.getBalance(),0.001);
    }
    @Test 
    void negativeWithdrawalIsRejected(){
        Account a=new Account("A101","Simran",5000.0);
        assertThrows(
            IllegalArgumentException.class,
            ()->a.withdraw(-500.0));
        assertEquals(5000.0, a.getBalance(),0.001);
    }
    @Test 
    void zeroDepositIsRejected(){
        Account a=new Account("A101","Simran",5000.0);
        assertThrows(
            IllegalArgumentException.class,
            ()->a.deposit(0.0));
        assertEquals(5000.0,a.getBalance(),0.001);
    }
    @Test 
    void nanDepositIsRejected(){
       Account a=new Account("A101","Simran",5000.0);
        assertThrows(
            IllegalArgumentException.class,
            ()->a.deposit(Double.NaN));
        assertEquals(5000.0,a.getBalance(),0.001); 
    }
    @Test 
    void infiniteDepositIsRejected(){
        Account a=new Account("A101","Simran",5000.0);
        assertThrows(
            IllegalArgumentException.class,
            ()->a.deposit(Double.POSITIVE_INFINITY));
        assertEquals(5000.0,a.getBalance(),0.001); 
    }
    @Test 
    void insufficientFundDoesNotChangeBalance(){
        Account a=new Account("A101","Simran",5000.0);
        assertThrows(
            IllegalArgumentException.class,
            ()->a.withdraw(6000.0));
        assertEquals(5000.0,a.getBalance(),0.001); 
    }
    
@Test
void successfulDepositIsRecorded() {
    Account a = new Account("A101", "Simran", 5000.0);

    a.deposit(1000.0);

    assertEquals(1, a.getTransactions().size());
    assertEquals("DEPOSIT", a.getTransactions().get(0).getType());
    assertEquals(1000.0, a.getTransactions().get(0).getAmount(), 0.001);
    assertEquals(6000.0, a.getTransactions().get(0).getBalanceAfter(), 0.001);
}
@Test
void successfulWithdrawIsRecorded() {
    Account a = new Account("A101", "Simran", 5000.0);

    a.withdraw(1000.0);

    assertEquals(1, a.getTransactions().size());
    assertEquals("WITHDRAWAL", a.getTransactions().get(0).getType());
    assertEquals(1000.0, a.getTransactions().get(0).getAmount(), 0.001);
    assertEquals(4000.0, a.getTransactions().get(0).getBalanceAfter(), 0.001);
}
@Test
void failedDepositIsRecorded() {
    Account a = new Account("A101", "Simran", 5000.0);
    assertThrows(
        IllegalArgumentException.class,
        ()->a.withdraw(6000)
    );
    assertEquals(0, a.getTransactions().size());
    assertEquals(5000.0, a.getBalance(), 0.001);
}

@Test
void invalidTransactionTypeIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Transaction(
            "INVALID", 100.0, LocalDateTime.now(), 900.0
        )
    );
}

@Test
void invalidTransactionAmountIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Transaction(
            "DEPOSIT", Double.NaN, LocalDateTime.now(), 900.0
        )
    );
}

@Test
void nullTransactionTimestampIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Transaction("DEPOSIT", 100.0, null, 900.0)
    );
}

@Test
void negativeBalanceAfterTransactionIsRejected() {
    assertThrows(
        IllegalArgumentException.class,
        () -> new Transaction(
            "WITHDRAWAL", 100.0, LocalDateTime.now(), -1.0
        )
    );
}
@Test
void modifyingReturnedHistoryDoesNotChangeAccountHistory() {
    Account a = new Account("A101", "Simran", 5000.0);
    a.deposit(1000.0);

    List<Transaction> history = a.getTransactions();
    history.clear();

    assertEquals(1, a.getTransactions().size());
}

}