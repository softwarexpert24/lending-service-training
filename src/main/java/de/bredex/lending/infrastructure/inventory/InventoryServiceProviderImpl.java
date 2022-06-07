package de.bredex.lending.infrastructure.inventory;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.InventoryServiceProvider;

@Component
public class InventoryServiceProviderImpl implements InventoryServiceProvider {

    @Value("${service.external.uri:}")
    private String externalServiceBaseUri;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean bookExists(String isbn) {
        final URI inventoryServiceUri = resolveInventoryServiceBaseUri();

        try {
            final ResponseEntity<String> response = restTemplate
                .getForEntity(inventoryServiceUri + "/api/v1/inventory/" + isbn, String.class);
            return response.getStatusCode() == HttpStatus.OK;
        } catch (final RestClientException exception) {
            return false;
        }
    }

    private URI resolveInventoryServiceBaseUri() {
        if (externalServiceBaseUri != null && !externalServiceBaseUri.isEmpty()) {
            return URI.create(externalServiceBaseUri);
        } else {
            return discoveryClient.getInstances("inventory-service").get(0).getUri();
        }
    }
}
