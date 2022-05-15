package de.bredex.lending.infrastructure.account;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
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

    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
    }

    @Override
    public boolean accountExists(String accountNumber) {
	final URI accountServiceBaseUri = resolveAccountServiceBaseUri();

	try {
	    final ResponseEntity<String> response = restTemplate
		    .getForEntity(accountServiceBaseUri + "/api/v1/account/" + accountNumber, String.class);
	    return response.getStatusCode() == HttpStatus.OK;
	} catch (final RestClientException exception) {
	    return false;
	}
    }
    
    private URI resolveAccountServiceBaseUri( ) {
	if (externalServiceBaseUri != null && !externalServiceBaseUri.isEmpty()) {
	    return URI.create(externalServiceBaseUri);
	} else {
	    return discoveryClient.getInstances("account-service").get(0).getUri();
	}
    }
}
