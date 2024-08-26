/*
*    This file is part of TeamCity Stash.
*
*    TeamCity Stash is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    TeamCity Stash is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with TeamCity Stash.  If not, see <http://www.gnu.org/licenses/>.
*/

package tests;

import junit.framework.TestCase;
import mendhak.teamcity.stash.api.StashClient;

import java.util.HashMap;
import java.util.Map;

public class StashClientTest extends TestCase
{
    public void testStashUrlConstruction_ReturnsStashUrl()
    {
        StashClient client = new StashClient();
        String stashBase = "http://example.com";
        String revision = "12349782349";
        String stashBuildStatusUrl = client.GetBuildStatusUrl(stashBase, revision);
        assertEquals("http://example.com/rest/build-status/1.0/commits/12349782349", stashBuildStatusUrl);

    }

    public void testStashUrlConstructionWithTrailingSlash_RemovesTrailingSlash()
    {
        StashClient client = new StashClient();
        String stashBase = "http://example.com/";
        String revision = "12349782349";
        String stashBuildStatusUrl = client.GetBuildStatusUrl(stashBase, revision);
        assertEquals("http://example.com/rest/build-status/1.0/commits/12349782349", stashBuildStatusUrl);

    }

    public void testStashUrlConstructionWithInvalidBaseUrl_ReturnsNull()
    {
        StashClient client = new StashClient();
        String stashBase = "://example.com/";
        String revision = "12349782349";
        String stashBuildStatusUrl = client.GetBuildStatusUrl(stashBase, revision);
        assertNull(stashBuildStatusUrl);

    }

    public void testStashUrlConstructionWithHTTPSSchema_ReturnsUrl()
    {
        StashClient client = new StashClient();
        String stashBase = "https://example.com/";
        String revision = "12349782349";
        String stashBuildStatusUrl = client.GetBuildStatusUrl(stashBase, revision);
        assertEquals("https://example.com/rest/build-status/1.0/commits/12349782349", stashBuildStatusUrl);

    }

    public void testStashBuildStateFromTeamCityBuildState()
    {
        StashClient client = new StashClient();
        String stashBuildState = client.GetBuildState(StashClient.BuildState.IN_PROGRESS);
        assertEquals("INPROGRESS", stashBuildState);

        stashBuildState = client.GetBuildState(StashClient.BuildState.FAILED);
        assertEquals("FAILED", stashBuildState);

        stashBuildState = client.GetBuildState(StashClient.BuildState.SUCCESSFUL);
        assertEquals("SUCCESSFUL", stashBuildState);
    }

    public void testAuthorizationHeader()
    {
        StashClient client = new StashClient();
        String authHeaderValue = client.GetAuthorizationHeaderValue("testuser","2jfksfjadf");
        assertEquals("dGVzdHVzZXI6Mmpma3NmamFkZg==", authHeaderValue);
    }

    public void testAdditionalHeaders()
    {
        StashClient client = new StashClient(
                "http://example.com",
                "testuser",
                "hunter2",
                "X-Header-1:value-1,X-Header-2: value2, X-Header-3: value_3"
        );

        Map<String, String> expectedHeaders = new HashMap<String, String>() {{
            put("X-Header-1", "value-1");
            put("X-Header-2", "value2");
            put("X-Header-3", "value_3");
        }};

        assertEquals(expectedHeaders, client.GetAdditionalHeaders());
    }

    public void testBuildStatusJsonBody()
    {

        StashClient client = new StashClient();
        String jsonBody = client.GetJsonBody("SUCCESSFUL", "REPO-MASTER", "REPO-MASTER-42",
                "http://example.com/browse/REPO-MASTER-42", "A description...");

        String expected = "{\n" +
                "    \"state\": \"SUCCESSFUL\",\n" +
                "    \"key\": \"REPO-MASTER\",\n" +
                "    \"name\": \"REPO-MASTER-42\",\n" +
                "    \"url\": \"http://example.com/browse/REPO-MASTER-42\",\n" +
                "    \"description\": \"A description...\"\n" +
                "}";

        assertEquals(expected, jsonBody);
    }

    public void testJsonBodyEscapesBackslash()
    {
        StashClient client = new StashClient();
        String jsonBody = client.GetJsonBody("SUCCESSFUL", "REPO-MASTER", "Look at this \\ backaslash!",
                "http://example.com/browse/REPO-MASTER-42", "Look at this \\ backaslash!");

        String expected = "{\n" +
                "    \"state\": \"SUCCESSFUL\",\n" +
                "    \"key\": \"REPO-MASTER\",\n" +
                "    \"name\": \"Look at this \\\\ backaslash!\",\n" +
                "    \"url\": \"http://example.com/browse/REPO-MASTER-42\",\n" +
                "    \"description\": \"Look at this \\\\ backaslash!\"\n" +
                "}";

        assertEquals(expected, jsonBody);
    }

    public void testJsonBodyEscapesDoubleQuote()
    {
        StashClient client = new StashClient();
        String jsonBody = client.GetJsonBody("SUCCESSFUL", "REPO-MASTER", "Look at this \" doublequote!",
                "http://example.com/browse/REPO-MASTER-42", "Look at this \" doublequote!");

        String expected = "{\n" +
                "    \"state\": \"SUCCESSFUL\",\n" +
                "    \"key\": \"REPO-MASTER\",\n" +
                "    \"name\": \"Look at this \\\" doublequote!\",\n" +
                "    \"url\": \"http://example.com/browse/REPO-MASTER-42\",\n" +
                "    \"description\": \"Look at this \\\" doublequote!\"\n" +
                "}";

        System.out.println(jsonBody);
        assertEquals(expected, jsonBody);
    }

    public void testJsonBodyNameTruncatedAt255Characters()
    {
        StashClient client = new StashClient();
        String jsonBody = client.GetJsonBody("SUCCESSFUL", "REPO-MASTER",
                "The name field in a Stash API body should be no longer than 255 characters, for if it exceeds this limit, " +
                        "Stash will become rather unhappy and inform us of its feelings on the matter.  We must endeavor " +
                        "not to upset Stash, as we depend on it for many things.  That is all.  Goodbye.",
                "http://example.com/browse/REPO-MASTER-42", "Look at this \" doublequote!");

        String expected = "{\n" +
                "    \"state\": \"SUCCESSFUL\",\n" +
                "    \"key\": \"REPO-MASTER\",\n" +
                "    \"name\": \"The name field in a Stash API body should be no longer than 255 characters, for if it " +
                "exceeds this limit, Stash will become rather unhappy and inform us of its feelings on the matter.  We " +
                "must endeavor not to upset Stash, as we depend on it for many thing\",\n" +
                "    \"url\": \"http://example.com/browse/REPO-MASTER-42\",\n" +
                "    \"description\": \"Look at this \\\" doublequote!\"\n" +
                "}";

        System.out.println(jsonBody);
        assertEquals(expected, jsonBody);
    }
}
