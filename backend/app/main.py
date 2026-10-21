from fastapi import FastAPI,Depends
from pydantic import BaseModel,Field
from decimal import Decimal
from sqlalchemy import select
from sqlalchemy.orm import Session

from app.database import get_db,create_account
from app.models import Account
from fastapi import HTTPException,status
from sqlalchemy.exc import IntegrityError
app=FastAPI(
    title ="SwizzBank API",
    description="Banking Management System API",
    version="1.0.0"
)
class AccountResponse(BaseModel):
    account_number:str
    holder_name:str
    balance:Decimal
class AccountCreate(BaseModel):
    account_number:str=Field(min_length=1,max_length=20)
    holder_name:str=Field(min_length=1,max_length=100)
    opening_balance:Decimal=Field(
        ge=0,
        max_digits=12,
        decimal_places=2
    )
@app.get("/")
def home():
    return {"message":"Welcome to SwizzBank API"}
@app.get("/health")
def health_check():
    return {"status":"healthy"}
@app.get("/accounts",response_model=list[AccountResponse])
def get_all_accounts(db:Session=Depends(get_db)):
    statement=select(Account).order_by(Account.account_number)
    accounts=db.scalars(statement).all()
    return accounts
@app.get("/accounts/{account_number}",response_model=AccountResponse)
def get_account(account_number:str,db:Session=Depends(get_db)):
    statement =select(Account).where(
        Account.account_number==account_number
    )
    account=db.scalars(statement).one_or_none()
    if account is None:
        raise HTTPException(
            status_code=404,
            detail="Account not found"
        )
    return account
@app.post("/accounts",response_model=AccountResponse,
          status_code=status.HTTP_201_CREATED)
def create_new_account(data:AccountCreate):
    account_number=data.account_number.strip()
    holder_name=data.holder_name.strip()
    if not account_number or not holder_name:
        raise HTTPException(
            status_code=422,
            detail="Account number and holder_name cannot be blank."
        )
    try:
        create_account(
            account_number,
            holder_name,
            data.opening_balance
        )
    except IntegrityError:
        raise HTTPException(
            status_code=409,
            detail="Account number already exists or DB constraint was violated."
        )
    return AccountResponse(
        account_number=account_number,
        holder_name=holder_name,
        balance=data.opening_balance
    )