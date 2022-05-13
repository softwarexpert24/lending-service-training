package de.bredex.lending.domain.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import de.bredex.lending.domain.model.Lending;
import de.bredex.lending.domain.spi.LendingEntity;
import de.bredex.lending.domain.spi.LendingRepository;

@Service
public class LendingService {

    private LendingRepository repository;
    
    public LendingService(LendingRepository repository) {
	this.repository = repository;
    }
    
    public Lending borrow(String accountNumber, String isbn) {
	LendingEntity savedLending = repository.save(new LendingEntity(accountNumber, isbn, LocalDate.now().plus(4, ChronoUnit.WEEKS)));
	return new Lending(savedLending.getAccountNumber(), savedLending.getIsbn(), savedLending.getReturnDate());
    }
    
    public List<Lending> getLendings(String accountNumber) {
	List<LendingEntity> lendings = repository.findAllByAccountNumber(accountNumber);
	return lendings.stream().map(lending -> new Lending(lending.getAccountNumber(), lending.getIsbn(), lending.getReturnDate())).collect(Collectors.toList());
    }
    
    public void deleteLending(String accountNumber, String isbn) {
	Optional<LendingEntity> lending = repository.findByAccountNumberAndIsbn(accountNumber, isbn);
	
	if (lending.isPresent()) {
	    repository.delete(lending.get());
	} else {
	    throw new IllegalArgumentException(String.format("No lending of ISBN '%s' found for account '%s'.", isbn, accountNumber));
	}
    }
}