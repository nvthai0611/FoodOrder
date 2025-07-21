package com.example.foodorder.response;

public class PaymentCheckResponse {
    private boolean success;
    private String message;
    private Transaction transaction;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public static class Transaction {
        private String amount_in;
        private String transaction_content;

        public String getAmount_in() {
            return amount_in;
        }

        public void setAmount_in(String amount_in) {
            this.amount_in = amount_in;
        }

        public String getTransaction_content() {
            return transaction_content;
        }

        public void setTransaction_content(String transaction_content) {
            this.transaction_content = transaction_content;
        }
    }
}
