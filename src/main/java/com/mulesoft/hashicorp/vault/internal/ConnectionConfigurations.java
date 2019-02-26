package com.mulesoft.hashicorp.vault.internal;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
public class ConnectionConfigurations  {

	private String vaultToken;
	private String vaultHost;
	private String vaultPort;
	private String vaultProtocol;
	private String vaultStoragePath;
	private String truststorePath;
	private String truststorePassword;
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
