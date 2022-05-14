package de.bredex.lending.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.bredex.lending.domain.model.Lending;
import de.bredex.lending.domain.spi.LendingEntity;
import de.bredex.lending.domain.spi.LendingRepository;

public class LendingServiceTest {

    private LendingRepository repository = mock(LendingRepository.class);

    private LendingService service;

    @BeforeEach
    public void setUp() {
	service = new LendingService(repository);
    }

    @Test
    public void borrow_creates_new_lending() {
	when(repository.save(any()))
		.thenReturn(new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plus(4, ChronoUnit.WEEKS)));

	Lending lending = service.borrow("10001", "1-86092-038-1");

	assertThat(lending.getAccountNumber()).isEqualTo("10001");
	assertThat(lending.getIsbn()).isEqualTo("1-86092-038-1");
	assertThat(lending.getReturnDate()).isEqualTo(LocalDate.now().plus(4, ChronoUnit.WEEKS));
    }

    @Test
    public void getLendings_returns_lendings() {
	final List<LendingEntity> storedLendings = new LinkedList<>();
	storedLendings.add(new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plus(4, ChronoUnit.WEEKS)));
	storedLendings.add(new LendingEntity("10001", "1-86092-025-9", LocalDate.now().plus(4, ChronoUnit.WEEKS)));
	when(repository.findAllByAccountNumber(any())).thenReturn(storedLendings);

	final List<Lending> lendings = service.getLendings("10001");

	assertThat(lendings.size()).isEqualTo(2);
    }

    @Test
    public void deleteLending_throws_exception_for_non_existing_lending() {
	when(repository.findByAccountNumberAndIsbn(any(), any())).thenReturn(Optional.empty());

	Assertions.assertThrows(IllegalArgumentException.class, () -> service.deleteLending("10001", "1-86092-038-1"));
    }
    
    @Test
    public void deleteLending_deletes_existing_lending() {
	final LendingEntity lendingEntity = new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plus(4, ChronoUnit.WEEKS));
	when(repository.findByAccountNumberAndIsbn(any(), any())).thenReturn(Optional.of(lendingEntity));

	service.deleteLending("10001", "1-86092-038-1");
	
	verify(repository, times(1)).delete(lendingEntity);
    }
}
