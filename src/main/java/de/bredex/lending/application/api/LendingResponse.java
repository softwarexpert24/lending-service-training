package de.bredex.lending.application.api;

import java.time.LocalDate;

public class LendingResponse {

    private String accountNumber;
    private String isbn;
    private LocalDate returnDate;
    
    public LendingResponse(String accountNumber, String isbn, LocalDate returnDate) {
	this.accountNumber = accountNumber;
	this.isbn = isbn;
	this.returnDate = returnDate;
    }
    
    public String getAccountNumber() {
	return accountNumber;
    }
    
    public String getIsbn() {
	return isbn;
    }
    
    public LocalDate getReturnDate() {
	return returnDate;
    }
}
