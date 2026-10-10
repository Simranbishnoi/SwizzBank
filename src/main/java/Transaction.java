
import java.time.LocalDateTime;

public final class Transaction {
    private final String type;
    private final double amount;
    private final LocalDateTime timestamp;
    private final double balanceAfter;

    
public Transaction(String t, double amt, LocalDateTime time, double b) {
    if (!"DEPOSIT".equals(t) && !"WITHDRAWAL".equals(t)) {
        throw new IllegalArgumentException("Invalid transaction type");
    }

    if (!Double.isFinite(amt) || amt <= 0) {
        throw new IllegalArgumentException("Amount must be finite and greater than zero");
    }

    if (time == null) {
        throw new IllegalArgumentException("Timestamp cannot be null");
    }

    if (!Double.isFinite(b) || b < 0) {
        throw new IllegalArgumentException("Balance must be finite and nonnegative");
    }

    this.type = t;
    this.amount = amt;
    this.timestamp = time;
    this.balanceAfter = b;
}


    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }
}
