package de.bredex.lending.application.api;

import java.time.LocalDate;
import java.util.List;

public class LendingsResponse {

    private String accountNumber;
    private List<Item> items;
    
    public LendingsResponse(String accountNumber, List<Item> items) {
	this.accountNumber = accountNumber;
	this.items = items;
    }
    
    public String getAccountNumber() {
	return accountNumber;
    }
    
    public List<Item> getItems() {
	return items;
    }
    
    static class Item {
	
	private String isbn;
	private LocalDate returnDate;
	
	public Item(String isbn, LocalDate returnDate) {
	    this.isbn = isbn;
	    this.returnDate = returnDate;
	}
	
	public String getIsbn() {
	    return isbn;
	}
	
	public LocalDate getReturnDate() {
	    return returnDate;
	}
    }
}
