import os
from decimal import Decimal, InvalidOperation

from dotenv import load_dotenv
from sqlalchemy import create_engine, select, text
from sqlalchemy.exc import IntegrityError
from sqlalchemy.orm import sessionmaker

from app.models import Account, Transaction

load_dotenv()

DATABASE_URL = os.getenv("DATABASE_URL")

if not DATABASE_URL:
    raise RuntimeError("DATABASE_URL is missing from .env")

engine = create_engine(DATABASE_URL)

SessionLocal = sessionmaker(
    bind=engine,
    autoflush=False,
    autocommit=False
)

def get_db():
    db = SessionLocal()

    try:
        yield db
    finally:
        db.close()

def test_connection():
    with engine.connect() as connection:
        result = connection.execute(text("SELECT 1"))
        print("Database connection successful:", result.scalar())

def test_session():
    db = SessionLocal()

    try:
        print("Session created successfully")
    finally:
        db.close()
        print("Session closed")

def get_accounts():
    with engine.connect() as connection:
        result = connection.execute(
            text("""
                SELECT account_number, holder_name, balance
                FROM accounts
                ORDER BY account_number
            """)
        )

        for row in result:
            print(row)

def get_accounts_with_orm():
    with SessionLocal() as db:
        statement = select(Account).order_by(
            Account.account_number
        )

        accounts = db.scalars(statement).all()

        for account in accounts:
            print(
                account.account_number,
                account.holder_name,
                account.balance
            )

def create_account(account_number, holder_name, opening_balance):
    db = SessionLocal()

    try:
        if not isinstance(account_number, str) or not account_number.strip():
            raise ValueError("Account number cannot be empty.")

        if not isinstance(holder_name, str) or not holder_name.strip():
            raise ValueError("Holder name cannot be empty.")

        try:
            opening_balance = Decimal(str(opening_balance))
        except (InvalidOperation, ValueError):
            raise ValueError("Enter a valid opening balance.")

        if not opening_balance.is_finite() or opening_balance < 0:
            raise ValueError("Opening balance cannot be negative or non-finite.")

        if opening_balance.as_tuple().exponent < -2:
            raise ValueError("Opening balance can have at most two decimal places.")

        account = Account(
            account_number=account_number.strip(),
            holder_name=holder_name.strip(),
            balance=opening_balance
        )

        db.add(account)
        db.commit()
        db.refresh(account)

        print("Account created successfully!")
        print("Account number:", account.account_number)
        print("Holder name:", account.holder_name)
        print("Balance:", account.balance)

    except IntegrityError:
        db.rollback()
        print(
            "Account could not be created. "
            "The account number may already exist, "
            "or a database constraint was violated."
        )

    except Exception:
        db.rollback()
        raise

    finally:
        db.close()

def deposit(account_number, amount):
    db = SessionLocal()

    try:
        try:
            amount = Decimal(str(amount))
        except (InvalidOperation, ValueError):
            raise ValueError("Enter a valid deposit amount.")

        if not amount.is_finite() or amount <= 0:
            raise ValueError("Deposit amount must be positive.")

        if amount.as_tuple().exponent < -2:
            raise ValueError("Deposit amount can have at most two decimal places.")

        statement = (
            select(Account)
            .where(Account.account_number == account_number)
            .with_for_update()
        )

        account = db.scalars(statement).one_or_none()

        if account is None:
            raise ValueError("Account not found.")

        account.balance += amount

        transaction = Transaction(
            account_number=account.account_number,
            type="DEPOSIT",
            amount=amount,
            balance_after=account.balance
        )

        db.add(transaction)
        db.commit()

        print("Deposit successful!")
        print("New balance:", account.balance)

    except Exception:
        db.rollback()
        raise

    finally:
        db.close()

def withdraw(account_number, amount):
    db = SessionLocal()

    try:
        try:
            amount = Decimal(str(amount))
        except (InvalidOperation, ValueError):
            raise ValueError("Enter a valid withdrawal amount.")

        if not amount.is_finite() or amount <= 0:
            raise ValueError("Withdrawal amount must be positive.")

        if amount.as_tuple().exponent < -2:
            raise ValueError("Withdrawal amount can have at most two decimal places.")

        statement = (
            select(Account)
            .where(Account.account_number == account_number)
            .with_for_update()
        )

        account = db.scalars(statement).one_or_none()

        if account is None:
            raise ValueError("Account not found.")

        if account.balance < amount:
            raise ValueError("Insufficient balance.")

        account.balance -= amount

        transaction = Transaction(
            account_number=account.account_number,
            type="WITHDRAWAL",
            amount=amount,
            balance_after=account.balance
        )

        db.add(transaction)
        db.commit()

        print("Withdrawal successful!")
        print("New balance:", account.balance)

    except Exception:
        db.rollback()
        raise

    finally:
        db.close()

if __name__ == "__main__":
    test_connection()
    test_session()
    get_accounts()
    get_accounts_with_orm()