package de.bredex.lending.infrastructure.account;

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

import de.bredex.lending.domain.spi.AccountServiceProvider;

@Component
public class AccountServiceProviderImpl implements AccountServiceProvider {

    @Value("${service.external.uri:}")
    private String externalServiceBaseUri;

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate;

    private final Set<String> accountNumbers = new HashSet<>();

    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean accountExists(String accountNumber) {
        Optional<URI> uri = resolveAccountServiceBaseUri();
        if (uri.isPresent()) {
            try {
                final ResponseEntity<String> response = restTemplate.getForEntity(
                    uri.get() + "/api/v1/account/" + accountNumber, String.class);

                return response.getStatusCode() == HttpStatus.OK;
            } catch (RestClientException e) {
                // Do nothing.
            }
        }

        return accountNumbers.contains(accountNumber);
    }

    @Scheduled(fixedRate = 5000)
    public void updateAccounts() {
        Optional<URI> uri = resolveAccountServiceBaseUri();
        if (uri.isPresent()) {
            try {
                final ResponseEntity<AccountDto[]> response = restTemplate.getForEntity(
                    uri.get() + "/api/v1/account",
                    AccountDto[].class);

                if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                    accountNumbers.clear();
                    for (AccountDto account : response.getBody()) {
                        accountNumbers.add(account.getNumber());
                    }
                }
            } catch (ResourceAccessException e) {
                // Do nothing.
            }
        }
    }

    private Optional<URI> resolveAccountServiceBaseUri() {
        if (externalServiceBaseUri != null && !externalServiceBaseUri.isEmpty()) {
            return Optional.of(URI.create(externalServiceBaseUri));
        } else {
            List<ServiceInstance> instances = discoveryClient.getInstances("account-service");
            if (!instances.isEmpty()) {
                return Optional.of(instances.get(0).getUri());
            } else {
                return Optional.empty();
            }
        }
    }
}
