package com.mulesoft.hashicorp.vault.internal;

import static org.springframework.vault.support.SslConfiguration.forTrustStore;

import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.SimpleSessionManager;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.config.ClientHttpRequestFactoryFactory;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.ClientOptions;
import org.springframework.vault.support.SslConfiguration;

//Needed to better test Vault with JUnit
public class HashicorpVaultConfigurationImpl {//extends AbstractVaultConfiguration{
	
//	protected static final Logger LOGGER = LoggerFactory.getLogger(HashicorpVaultConfigurationImpl.class);
//	private ConnectionConfigurations connectionConfigurations = null;
//	
//	public HashicorpVaultConfigurationImpl(ConnectionConfigurations connectionConfigurations) {
//		this.connectionConfigurations = connectionConfigurations;
//	}
//	
//	@Override
//	public ClientAuthentication clientAuthentication() {
//		return new TokenAuthentication(connectionConfigurations.getVaultToken());
//	}
//	@Override
//	public VaultEndpoint vaultEndpoint() {
//		URI uri = null;
//		try {
//			uri = new URI(connectionConfigurations.getVaultURL());
//			return  VaultEndpoint.from(uri);
//		} catch (URISyntaxException e) {
//			throw new RuntimeException(e.getCause());
//		}
//	}
//	@Override
//	public SslConfiguration sslConfiguration() {
//		DefaultResourceLoader defaultResourcLoader = new DefaultResourceLoader();
//		Resource resource = defaultResourcLoader.getResource(connectionConfigurations.getTruststorePath());
//		return forTrustStore(resource, connectionConfigurations.getTruststorePassword().toCharArray());
//	}
//	
//	public VaultTemplate createVaultTample() {
//		VaultTemplate vaultTemplate = null;
//		// In case the user wants to use a specific truststore as opposed to the default JVM's
//		if (connectionConfigurations.getTruststorePath() != null && !connectionConfigurations.getTruststorePath().equalsIgnoreCase("")) {
//			ClientHttpRequestFactory factory = ClientHttpRequestFactoryFactory.create(new ClientOptions(),
//					this.sslConfiguration());
//			SimpleSessionManager simpleSessionManager = new SimpleSessionManager(this.clientAuthentication());
//			vaultTemplate = new VaultTemplate(this.vaultEndpoint(), factory, simpleSessionManager);
//		} else {
//			vaultTemplate = new VaultTemplate(this.vaultEndpoint(), this.clientAuthentication());
//		}
//		return vaultTemplate;
//	}
}
