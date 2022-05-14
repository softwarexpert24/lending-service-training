package de.bredex.lending.domain.spi;

public interface AccountServiceProvider {

    public boolean accountExists(final String accountNumber);
}
