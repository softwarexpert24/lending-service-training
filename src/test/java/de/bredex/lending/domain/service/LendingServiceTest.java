package de.bredex.lending.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
	when(repository.save(any())).thenReturn(new LendingEntity("10001", "1-86092-038-1", LocalDate.now().plus(4, ChronoUnit.WEEKS)));
	
	Lending lending = service.borrow("10001", "1-86092-038-1");
	
	assertThat(lending.getAccountNumber()).isEqualTo("10001");
	assertThat(lending.getIsbn()).isEqualTo("1-86092-038-1");
	assertThat(lending.getReturnDate()).isEqualTo(LocalDate.now().plus(4, ChronoUnit.WEEKS));
    }
}
