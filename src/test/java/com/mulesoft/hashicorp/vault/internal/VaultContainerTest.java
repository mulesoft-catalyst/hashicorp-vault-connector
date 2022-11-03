package com.mulesoft.hashicorp.vault.internal;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.IOException;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.Wait;
import org.testcontainers.vault.VaultContainer;

/**
 * This test shows the pattern to use the VaultContainer @ClassRule for a junit test. It also has tests that ensure
 * the secrets were added correctly by reading from Vault with the CLI and over HTTP.
 * NOTE This is from the official GitHub page and is intended to show how the vault works. It does not test the connector as such
 * for further info check it out https://github.com/testcontainers/testcontainers-java/blob/master/modules/vault/src/main/java/org/testcontainers/vault/VaultContainer.java
 */

public class VaultContainerTest {

	private static final int VAULT_PORT = 8201; //using non-default port to show other ports can be passed besides 8200

    private static final String VAULT_TOKEN = "[VAULT TOKEN]";

    @ClassRule
    public static VaultContainer vaultContainer = new VaultContainer<>()
            .withVaultToken(VAULT_TOKEN)
            .withVaultPort(VAULT_PORT)
            .withSecretInVault("secret/testing1", "top_secret=[ENTER PASSWORD]")
            .withSecretInVault("secret/testing2", "secret_one=[ENTER PASSWORD]",
                    "secret_two=[ENTER PASSWORD]", "secret_three=password3", "secret_three=[ENTER PASSWORD]",
                    "secret_four=[ENTER PASSWORD]")
            .waitingFor(Wait.forHttp("/v1/secret/testing1").forStatusCode(400));

    @Test
    public void readFirstSecretPathWithCli() throws IOException, InterruptedException {
        GenericContainer.ExecResult result = vaultContainer.execInContainer("vault",
                "read", "-field=top_secret", "secret/[SECRET]");
        assertThat(result.getStdout(), containsString("[ENTER PASSWORD]"));
    }

    @Test
    public void readSecondSecretPathWithCli() throws IOException, InterruptedException {
        GenericContainer.ExecResult result = vaultContainer.execInContainer("vault",
                "read", "secret/testing2");
        String output = result.getStdout();
        assertThat(output, containsString("[ENTER PASSWORD]"));
        assertThat(output, containsString("[ENTER PASSWORD]"));
        assertThat(output, containsString("[ENTER PASSWORD]"));
        assertThat(output, containsString("[ENTER PASSWORD]"));
    }

    @Test
    public void readFirstSecretPathOverHttpApi() throws InterruptedException {
        given().
            header("X-Vault-Token", VAULT_TOKEN).
        when().
            get("http://"+getHostAndPort()+"/v1/secret/testing1").
        then().
            assertThat().body("data.top_secret", equalTo("[ENTER PASSWORD]"));
    }

    @Test
    public void readSecondecretPathOverHttpApi() throws InterruptedException {
        given().
            header("X-Vault-Token", VAULT_TOKEN).
        when().
            get("http://"+getHostAndPort()+"/v1/secret/testing2").
        then().
            assertThat().body("data.secret_one", containsString("[ENTER PASSWORD]")).
            assertThat().body("data.secret_two", containsString("[ENTER PASSWORD]")).
            assertThat().body("data.secret_three", containsString("[ENTER PASSWORD]")).
            assertThat().body("data.secret_four", containsString("[ENTER PASSWORD]"));
    }

    private String getHostAndPort(){
        return vaultContainer.getContainerIpAddress()+":"+vaultContainer.getMappedPort(8200);
    }
}
