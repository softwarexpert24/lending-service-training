package de.bredex.lending.infrastructure.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import de.bredex.lending.domain.spi.AccountServiceProvider;

@Component
public class AccountServiceProviderImpl implements AccountServiceProvider {

    @Value("${service.account.uri}")
    private String accountServiceUri;
    
    private final RestTemplate restTemplate;
    
    public AccountServiceProviderImpl(final RestTemplate restTemplate) {
	this.restTemplate = restTemplate;
    }
    
    @Override
    public boolean accountExists(String accountNumber) {
	final ResponseEntity<String> response = restTemplate.getForEntity(accountServiceUri + "/" + accountNumber, String.class);
	return response.getStatusCode() == HttpStatus.OK;
    }
}
