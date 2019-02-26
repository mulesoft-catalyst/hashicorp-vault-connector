 

# The Hashicorp Vault Mule 4 Module

The Hashicorp Vault connector allows you to retrieve value at runtime from an Hashicorp vault instance via its only operation.

A hashicorp xml file is configured with a `<hashicorp-vault:config>` tag, for example:

```
	<hashicorp-vault:config name="Hashicorp_Vault_Config"
		doc:name="Hashicorp Vault Config" doc:id="2be419d8-996a-4332-bd51-0fe5c4829e90">
		<hashicorp-vault:connection 
			vaultToken="${vaultTokenEnvironmentVariable}" 
			vaultHost="${vault.host}" 
			vaultProtocol="${vault.protocol}" 
			vaultPort="${vault.port}" 
			vaultStoragePath="${vault.storagePath}" 
			truststorePath="${truststore.path}" 
			truststorePassword="${truststorePasswordEnvironmentVariable}" tls="true"/>
	</hashicorp-vault:config>
```

In this example, three values are passed into the Mule runtime at deployment time as a system environment variables `vaultTokenEnvironmentVariable`, `truststorePasswordEnvironmentVariable` and `env`. These properties must be the exact ones used to configure the vault and the truststore (if any for truststore as both the truststorePath and truststorePassword are optional, JVM's is used by default).

**Note**: When using sensitive information as for the token and password above, it is especially important to secure access to the operating system. Anyone who can run a `ps` command or view a Java console will be able to see the decrypted values that are stored in the Mule application's memory.

As this is built as a Mule module, the way to use it within an application is the same as for any other Mule module. Simply search for the Hashicorp Vault module in Exchange from the Mule Palette within Anypoint Studio and then import it from there.

Below a mule xml configuration for a test application that uses this connector.

**Note**: The environment variables truststorePasswordEnvironmentVariable and vaultTokenEnvironmentVariable must be set as such either in Anypoint Studio for testing purposes or on the OS.

```
<?xml version="1.0" encoding="UTF-8"?>

<mule xmlns:os="http://www.mulesoft.org/schema/mule/os"
	xmlns:hashicorp-vault="http://www.mulesoft.org/schema/mule/hashicorp-vault" xmlns:min-log="http://www.mulesoft.org/schema/mule/min-log"
	xmlns:ee="http://www.mulesoft.org/schema/mule/ee/core"
	xmlns:file="http://www.mulesoft.org/schema/mule/file"
	xmlns:collection-core-api="http://www.mulesoft.org/schema/mule/collection-core-api" xmlns:http="http://www.mulesoft.org/schema/mule/http"
	xmlns="http://www.mulesoft.org/schema/mule/core"
	xmlns:doc="http://www.mulesoft.org/schema/mule/documentation"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.mulesoft.org/schema/mule/core http://www.mulesoft.org/schema/mule/core/current/mule.xsd
http://www.mulesoft.org/schema/mule/http http://www.mulesoft.org/schema/mule/http/current/mule-http.xsd
http://www.mulesoft.org/schema/mule/ee/core http://www.mulesoft.org/schema/mule/ee/core/current/mule-ee.xsd
http://www.mulesoft.org/schema/mule/file http://www.mulesoft.org/schema/mule/file/current/mule-file.xsd
http://www.mulesoft.org/schema/mule/min-log http://www.mulesoft.org/schema/mule/min-log/current/mule-min-log.xsd
http://www.mulesoft.org/schema/mule/hashicorp-vault http://www.mulesoft.org/schema/mule/hashicorp-vault/current/mule-hashicorp-vault.xsd
http://www.mulesoft.org/schema/mule/os http://www.mulesoft.org/schema/mule/os/current/mule-os.xsd">
	<http:listener-config name="HTTP_Listener_config"
		doc:name="HTTP Listener config" doc:id="1af9a4c8-7091-40f0-8e78-c458879282b2">
		<http:listener-connection host="0.0.0.0"
			port="8083" />
	</http:listener-config>
	<hashicorp-vault:config name="Hashicorp_Vault_Config"
		doc:name="Hashicorp Vault Config" doc:id="cc23a8cd-d656-465a-9584-e7792b9f5a15">
		<hashicorp-vault:connection vaultToken="${vaultTokenEnvironmentVariable}"
			vaultURL="${vault.URL}" vaultStoragePath="${vault.storagePath}"
			truststorePath="${truststore.path}" truststorePassword="${truststorePasswordEnvironmentVariable}" />
	</hashicorp-vault:config>
	<ee:object-store-caching-strategy name="Caching_Strategy" doc:name="Caching Strategy" doc:id="bf2e1a60-9388-4509-b110-e4c6d0553500" >
		<os:private-object-store alias="vault" maxEntries="${max.entries}" entryTtl="${entry.ttl}" expirationInterval="${exp.interval}" entryTtlUnit="MILLISECONDS" expirationIntervalUnit="MILLISECONDS"/>
	</ee:object-store-caching-strategy>
	<configuration-properties doc:name="Configuration properties" doc:id="44935958-d46b-4de8-af0b-3b0e8161863f" file="${env}.yaml" />
	<flow name="testFlow" doc:id="2d3c3b07-f7f6-433f-8cf3-f713e705a819">
		<http:listener doc:name="Listener"
			doc:id="cf2af142-fe7d-4ba1-8438-69a0ca23c814" config-ref="HTTP_Listener_config"
			path="/api/tests/{testId}" />
		<ee:cache doc:name="Cache" doc:id="701ecad8-3bc1-427d-b7d3-2a0475a92c71" >
			<hashicorp-vault:get-key-value doc:name="Vault: Get Value" doc:id="34c7202d-c528-40e1-ac2b-f7ae6e074671" config-ref="Hashicorp_Vault_Config" key="example.password" />
		</ee:cache>
		<logger level="INFO" doc:name="Logger"
			doc:id="c674c1f6-11eb-48ac-a11d-80df28dad5ca" message="#[payload]" />
	</flow>
</mule>

```
