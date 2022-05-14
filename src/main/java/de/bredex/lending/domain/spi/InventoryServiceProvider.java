package de.bredex.lending.domain.spi;

public interface InventoryServiceProvider {

    public boolean bookExists(final String isbn);
}
