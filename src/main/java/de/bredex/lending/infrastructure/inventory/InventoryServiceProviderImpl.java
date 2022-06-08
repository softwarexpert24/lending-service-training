package de.bredex.lending.infrastructure.inventory;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
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

    private final Set<String> isbns = new HashSet<>();

    public InventoryServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean bookExists(String isbn) {
        final Optional<URI> uri = resolveInventoryServiceBaseUri();
        if (uri.isPresent()) {
            try {
                final ResponseEntity<String> response = restTemplate
                    .getForEntity(uri.get() + "/api/v1/inventory/" + isbn, String.class);
                return response.getStatusCode() == HttpStatus.OK;
            } catch (final RestClientException exception) {
                // Do nothing.
            }
        }

        return isbns.contains(isbn);
    }

    @Scheduled(fixedRate = 5000)
    public void updateInventory() {
        Optional<URI> uri = resolveInventoryServiceBaseUri();
        if (uri.isPresent()) {
            try {
                final ResponseEntity<BookDto[]> response = restTemplate.getForEntity(
                    uri.get() + "/api/v1/inventory",
                    BookDto[].class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    isbns.clear();
                    for (BookDto book : response.getBody()) {
                        isbns.add(book.getIsbn());
                    }
                }
            } catch (ResourceAccessException e) {
                // Do nothing.
            }
        }
    }

    private Optional<URI> resolveInventoryServiceBaseUri() {
        if (externalServiceBaseUri != null && !externalServiceBaseUri.isEmpty()) {
            return Optional.of(URI.create(externalServiceBaseUri));
        } else {
            List<ServiceInstance> instances = discoveryClient.getInstances("inventory-service");
            if (!instances.isEmpty()) {
                return Optional.of(instances.get(0).getUri());
            } else {
                return Optional.empty();
            }
        }
    }
}
