package de.bredex.lending.domain.spi;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface LendingRepository extends CrudRepository<LendingEntity, Integer> {

    public List<LendingEntity> findAllByAccountNumber(String accountNumber);
    
    public Optional<LendingEntity> findByAccountNumberAndIsbn(String accountNumber, String isbn);
}
