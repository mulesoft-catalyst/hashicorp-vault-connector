package com.mulesoft.hashicorp.vault.internal;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;
import org.mule.runtime.core.api.event.CoreEvent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.vault.VaultContainer;

public class HashicorpConnectionTestCase extends MuleArtifactFunctionalTestCase {

	private static final int VAULT_PORT = 8200; //using non-default port to show other ports can be passed besides 8200
    private static final String VAULT_TOKEN = "49VLojFvnnv5wnIY3GGW9i6x";
	CoreEvent response;

	//These have to match these value in src/test/resources/local.yaml file
	static String expectedValue = "cloudpassword";
	static String vaultKey = "example.password";
	static String vaultKeyAndValue = "example.password=cloudpassword";
	static String vaultStoragePath = "secret/gs-vault-config";
	static String expectedWrongValue = "password1Wrong";
	public GenericContainer.ExecResult result;

    @ClassRule
    public static VaultContainer vaultContainer = new VaultContainer<>()
            .withVaultToken(VAULT_TOKEN)
            .withVaultPort(VAULT_PORT)
            .withSecretInVault(vaultStoragePath, vaultKeyAndValue)
            .waitingFor(Wait.forHttp("/v1/secret/testing1").forStatusCode(400));

	/**
	 * Specifies the mule config xml with the flows that are going to be executed in the tests,
	 *  this file lives in the test resources.
	 */
	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}
	@Before
	public void init() throws Exception {
		result = vaultContainer.execInContainer("vault",
                "read", "-field="+vaultKey, vaultStoragePath);
		response = flowRunner("testFlow").run();
	}
	@Test
	public void executeGetKeyOperation() throws Exception {
		assertThat(result.getStdout(), containsString(expectedValue));
	}
	@Test
	public void executeGetKeyOperationKeyNotFound() throws Exception {
		assertThat(result.getStdout(), not(expectedWrongValue));
	}
}