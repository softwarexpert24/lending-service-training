package de.bredex.lending.infrastructure.inventory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.InventoryServiceProvider;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {

    @Value("${service.inventory.uri}")
    private String inventoryServiceUri;

    private final RestTemplate restTemplate;
    
    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
    }
    
    @Override
    public boolean bookExists(String isbn) {
	final ResponseEntity<String> response = restTemplate.getForEntity(inventoryServiceUri + "/" + isbn, String.class);
	return response.getStatusCode() == HttpStatus.OK;
    }
}
