package de.bredex.lending.domain.model;

import java.time.LocalDate;

public final class Lending {

    private final String accountNumber;
    private final String isbn;
    private final LocalDate returnDate;
    
    public Lending(final String accountNumber, final String isbn, final LocalDate returnDate) {
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
