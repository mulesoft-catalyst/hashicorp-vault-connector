package com.mulesoft.hashicorp.vault.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;


/**
 * This is the main class of an extension, is the entry point from which configurations, connection providers, operations
 * and sources are going to be declared.
 */
@Xml(prefix = "hashicorp-vault")
@Extension(name = "Hashicorp Vault")
@Operations(HashicorpOperations.class)
@ConnectionProviders(HashicorpConnectionProvider.class)
public class HashicorpVaultExtension {
}