
from sqlalchemy import String, Numeric, DateTime, func,ForeignKey
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column
from decimal import Decimal
class Base(DeclarativeBase):
    pass

class Account(Base):
    __tablename__ = "accounts"

    account_number: Mapped[str] = mapped_column(
        String(20),
        primary_key=True
    )

    holder_name: Mapped[str] = mapped_column(
        String(100),
        nullable=False
    )

    balance: Mapped[float] = mapped_column(
        Numeric(12, 2),
        nullable=False
    )

    created_at: Mapped[object] = mapped_column(
        DateTime(timezone=True),
        nullable=False,
        server_default=func.now()
    )
class Transaction(Base):
    __tablename__="transactions"
    transaction_id:Mapped[int]=mapped_column(
        primary_key=True
    )
    account_number:Mapped[str]=mapped_column(
        String(20),
        ForeignKey("accounts.account_number"),
        nullable=False
    )
    type:Mapped[str]=mapped_column(
        String(10),
        nullable=False
    )
    amount:Mapped[Decimal]=mapped_column(
        Numeric(12,2),
        nullable=False
    )
    timestamp:Mapped[object]=mapped_column(
        DateTime(timezone=True),
        nullable=False,
        server_default=func.now()
    )
    balance_after:Mapped[Decimal]=mapped_column(
        Numeric(12,2),
        nullable=False
    )