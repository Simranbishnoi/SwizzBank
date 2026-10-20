from fastapi import FastAPI,Depends
from pydantic import BaseModel
from decimal import Decimal
from sqlalchemy import select
from sqlalchemy.orm import Session

from app.database import get_db
from app.models import Account
app=FastAPI(
    title ="SwizzBank API",
    description="Banking Management System API",
    version="1.0.0"
)
class AccountResponse(BaseModel):
    account_number:str
    holder_name:str
    balance:Decimal
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