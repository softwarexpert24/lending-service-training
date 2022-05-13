package de.bredex.lending.application.api;

public class LendingRequest {

    private String accountNumber;
    private String isbn;
    
    public LendingRequest(String accountNumber, String isbn) {
	this.accountNumber = accountNumber;
	this.isbn = isbn;
    }
    
    public String getAccountNumber() {
	return accountNumber;
    }
    
    public String getIsbn() {
	return isbn;
    }
}
