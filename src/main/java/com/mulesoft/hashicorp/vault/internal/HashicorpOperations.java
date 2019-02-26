package com.mulesoft.hashicorp.vault.internal;

import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

/**
 * This class is a container for operations, every public method in this class
 * will be taken as an extension operation.
 */
public class HashicorpOperations {

	/**
	 * Given the @param String key, the method returns its value
	 * from the hashicorp vault
	 * 
	 * @param key
	 *            Key for which to retrieve the value of from the Vault
	 * @return    
	 * 			  String value for the specified key            
	 */
	@DisplayName("Vault: Get Value")
	@Summary("Gets the value of the property specified by the key")
	@MediaType("application/json")
	public String getKeyValue(@DisplayName("Property Name") final String key,
			@Connection HashicorpConnection hashicorpConnection) {
		return hashicorpConnection.readSecrets(key);
	}
}