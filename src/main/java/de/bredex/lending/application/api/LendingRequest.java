package de.bredex.lending.application.api;

public final class LendingRequest {

    private final String accountNumber;
    private final String isbn;
    
    public LendingRequest(final String accountNumber, final String isbn) {
	this.accountNumber = accountNumber;
	this.isbn = isbn;
    }
    
    public final String getAccountNumber() {
	return accountNumber;
    }
    
    public final String getIsbn() {
	return isbn;
    }
}
