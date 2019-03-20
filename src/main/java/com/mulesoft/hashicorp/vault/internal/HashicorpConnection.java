package com.mulesoft.hashicorp.vault.internal;

import static org.springframework.vault.support.SslConfiguration.forTrustStore;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

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
import org.springframework.vault.support.VaultResponseSupport;


/**
 * This class is a container for operations, every public method in this class will be taken as an extension operation.
 */
public class HashicorpConnection extends AbstractVaultConfiguration{

	private VaultTemplate vaultTemplate = null;
	protected static final Logger LOGGER = LoggerFactory.getLogger(HashicorpConnection.class);
	private ConnectionConfigurations connectionConfigurations = null;
	HashicorpVaultConfigurationImpl configuraionImpl;

//	public HashicorpConnection(String vaultToken, 
//			String vaultHost, String vaultStoragePath, String truststorePath, String truststorePassword, VaultTemplate vaultTemplate){
//		connectionConfigurations = new ConnectionConfigurations();
//		connectionConfigurations.setVaultToken(vaultToken);
//		connectionConfigurations.setVaultURL(vaultHost);
//		connectionConfigurations.setTruststorePath(truststorePath);
//		connectionConfigurations.setVaultStoragePath(vaultStoragePath);
//		connectionConfigurations.setTruststorePassword(truststorePassword);
//		this.vaultTemplate = vaultTemplate;  
//	}
	
	public HashicorpConnection(String vaultToken, String vaultProtocol, String vaultHost, String vaultPort,
			String vaultStoragePath, String truststorePath, String truststorePassword,
			boolean isTls) {
		
		try {
			new URI(vaultProtocol+"://"+vaultHost+":"+vaultPort);
		}
		catch (URISyntaxException e) {
			throw new RuntimeException(e.getCause());
		}
		connectionConfigurations = new ConnectionConfigurations();
		connectionConfigurations.setVaultToken(vaultToken);
		connectionConfigurations.setVaultProtocol(vaultProtocol);
		connectionConfigurations.setVaultHost(vaultHost);
		connectionConfigurations.setVaultPort(vaultPort);
		connectionConfigurations.setTruststorePath(truststorePath);
		connectionConfigurations.setVaultStoragePath(vaultStoragePath);
		connectionConfigurations.setTruststorePassword(truststorePassword);
		connectionConfigurations.setTls(isTls);
		//configuraionImpl = new HashicorpVaultConfigurationImpl(connectionConfigurations);
		
		LOGGER.debug("Instantiating vault template with the following parameters: \n "
				+ "Vault URL: " +  vaultHost + "\n" 
				+ "Vault storage path " + vaultStoragePath );
		
		// In case the user wants to use a specific truststore as opposed to the default JVM's
		if(connectionConfigurations.isTls()) {
			if (truststorePath != null && !truststorePath.equalsIgnoreCase("")) {
				ClientHttpRequestFactory factory = ClientHttpRequestFactoryFactory.create(new ClientOptions(),
						this.sslConfiguration());
				SimpleSessionManager simpleSessionManager = new SimpleSessionManager(this.clientAuthentication());
				vaultTemplate = new VaultTemplate(this.vaultEndpoint(), factory, simpleSessionManager);
			} else {
				vaultTemplate = establishConnection();
			}
		} else {
			vaultTemplate = establishConnection();
		}
	}

	private VaultTemplate establishConnection() {
		return new VaultTemplate(this.vaultEndpoint(), this.clientAuthentication());
	}

	public String readSecrets(String key) {
		@SuppressWarnings("rawtypes")
		VaultResponseSupport<HashMap> response = vaultTemplate.read(
				connectionConfigurations.getVaultStoragePath(), HashMap.class);
		if(response == null ) {
			throw new IllegalArgumentException("The vault storage path " + connectionConfigurations.getVaultStoragePath() + " could not be found in the Vault");
		} else if (response.getData().get(key) == null){
			throw new IllegalArgumentException("The vault key " + key + " could not be found in the Vault");
		} else {
			return response.getData().get(key).toString();
		}
	}
	public void disconnect() throws Exception {
		vaultTemplate.destroy();
	}
	public boolean validate() {
		return vaultTemplate != null ? true : false;
	}
	@Override
	public ClientAuthentication clientAuthentication() {
		return new TokenAuthentication(connectionConfigurations.getVaultToken());
	}
	@Override
	public VaultEndpoint vaultEndpoint() {
		VaultEndpoint endpoint = new VaultEndpoint();
		endpoint.setHost(connectionConfigurations.getVaultHost());
		endpoint.setPort(Integer.parseInt(connectionConfigurations.getVaultPort()));
		endpoint.setScheme(connectionConfigurations.getVaultProtocol());
		return  endpoint;
	}
	@Override
	public SslConfiguration sslConfiguration() {
		DefaultResourceLoader defaultResourcLoader = new DefaultResourceLoader();
		Resource resource = defaultResourcLoader.getResource(connectionConfigurations.getTruststorePath());
		return forTrustStore(resource, connectionConfigurations.getTruststorePassword().toCharArray());
	}
}
