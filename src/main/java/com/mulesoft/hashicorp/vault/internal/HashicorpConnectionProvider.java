/*
 * (c) 2003-2018 MuleSoft, Inc. This software is protected under international copyright
 * law. All use of this software is subject to MuleSoft's Master Subscription Agreement
 * (or other master license agreement) separately entered into in writing between you and
 * MuleSoft. If such an agreement is not in place, you may not use the software.
 */
package com.mulesoft.hashicorp.vault.internal;

import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.api.connection.PoolingConnectionProvider;
import org.mule.runtime.api.lifecycle.Initialisable;
import org.mule.runtime.api.lifecycle.InitialisationException;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider to resolve properties from HashiCorp Vault.
 * <p>
 * This provider returns the value of the expression to the one corresponding to the configuration file. 
 * </p>
 *
 * @since 1.0
 */

public class HashicorpConnectionProvider
implements Initialisable, PoolingConnectionProvider<HashicorpConnection> {

	protected static final Logger logger = LoggerFactory.getLogger(HashicorpConnectionProvider.class);
	private HashicorpConnection hashiscorpsecurepropertiesConnection =null;

	public HashicorpConnectionProvider() {
	}
	public HashicorpConnectionProvider(
			String vaultToken, String vaultProtocol, String vaultHost, String vaultPort, 
			String vaultStoragePath, String truststorePath, String truststorePassword, boolean isTls) {
		hashiscorpsecurepropertiesConnection = new HashicorpConnection(
				vaultToken, vaultProtocol, vaultHost, vaultPort, vaultStoragePath, truststorePath, truststorePassword, isTls);
	}
	@Override
	public HashicorpConnection connect() throws ConnectionException {
		hashiscorpsecurepropertiesConnection = new HashicorpConnection(this.getVaultToken(), this.getVaultProtocol(), this.getVaultHost(),
				this.getVaultPort(), this.getVaultStoragePath(), 	this.getTruststorePath(), this.getTruststorePassword(), this.isTls());
		return hashiscorpsecurepropertiesConnection;
	}
	@Override
	public void disconnect(HashicorpConnection connection) {
		try {
			connection.disconnect();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	@Override
	public ConnectionValidationResult validate(HashicorpConnection connection) {

		if (connection.validate()) {
			return ConnectionValidationResult.success();
		} else {
			return ConnectionValidationResult.failure("Connection to the vault could not be validated",
					new RuntimeException("Connection to the vault could not be validated"));
		}
	}
	@Override
	public void initialise() throws InitialisationException {
	}
	@Parameter
	@DisplayName("Vault Token")
	@Summary("The root token to gain access to the vault, as token auth is the Auth method for this module")
	private String vaultToken;

	@Parameter
	@DisplayName("Vault Host")
	@Summary("The host to connect to the vault e.g. localhost")
	private String vaultHost;
	
	@Parameter
	@DisplayName("Vault Protocol")
	@Summary("The protocol to connect to the vault, either http or https")
	private String vaultProtocol;
	
	@Parameter
	@DisplayName("Vault Port")
	@Summary("The host to connect to the vault e.g. localhost")
	private String vaultPort;

	@Parameter
	@DisplayName("Vault Storage Path")
	@Summary("The storage secrets path e.g. secrets/project/environment")
	private String vaultStoragePath;

	@Parameter
	@DisplayName("Truststore Path")
	@Summary("Truststore Path defaulted to the JVM's if not set. Please note that if this field is set so the password must be")
	@Optional
	private String truststorePath;

	@Parameter
	@DisplayName("Truststore Password")
	@Summary("Truststore Password, only must be set if the truststore path is")
	@Optional
	private String truststorePassword;
	
	@Parameter
	@DisplayName("TLS enabled")
	@Summary("If checked the connector will establish a HTTPS connection with the Vault, HTTP otherwise")
	@Optional
	private boolean tls;

	public String getTruststorePath() {
		return truststorePath;
	}
	public void setTruststorePath(String truststorePath) {
		this.truststorePath = truststorePath;
	}
	public String getTruststorePassword() {
		return truststorePassword;
	}
	public void setTruststorePassword(String truststorePassword) {
		this.truststorePassword = truststorePassword;
	}
	public String getVaultHost() {
		return vaultHost;
	}
	public void setVaultHost(String vaultHost) {
		this.vaultHost = vaultHost;
	}
	public String getVaultToken() {
		return vaultToken;
	}
	public void setVaultToken(String vaultToken) {
		this.vaultToken = vaultToken;
	}
	public String getVaultStoragePath() {
		return vaultStoragePath;
	}
	public void setVaultStoragePath(String vaultStoragePath) {
		this.vaultStoragePath = vaultStoragePath;
	}
	public boolean isTls() {
		return tls;
	}
	public void setTls(boolean tls) {
		this.tls = tls;
	}
	public String getVaultProtocol() {
		return vaultProtocol;
	}
	public void setVaultProtocol(String vaultProtocol) {
		this.vaultProtocol = vaultProtocol;
	}
	public String getVaultPort() {
		return vaultPort;
	}
	public void setVaultPort(String vaultPort) {
		this.vaultPort = vaultPort;
	}
}
