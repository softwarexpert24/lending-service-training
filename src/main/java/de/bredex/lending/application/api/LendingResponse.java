package de.bredex.lending.application.api;

import java.time.LocalDate;

public final class LendingResponse {

    private final String accountNumber;
    private final String isbn;
    private final LocalDate returnDate;
    
    public LendingResponse(final String accountNumber, final String isbn, final LocalDate returnDate) {
	this.accountNumber = accountNumber;
	this.isbn = isbn;
	this.returnDate = returnDate;
    }
    
    public final String getAccountNumber() {
	return accountNumber;
    }
    
    public final String getIsbn() {
	return isbn;
    }
    
    public final LocalDate getReturnDate() {
	return returnDate;
    }
}
