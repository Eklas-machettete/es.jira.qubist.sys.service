package com.jira;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import com.jira.controllers.AtlassianController;
import com.jira.services.AtlassianService;

import junit.framework.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class AtlassianControllerTest 
{
    @LocalServerPort
    int randomServerPort;
	@Value("${jira.host}")
	String jiraUri;
	@Autowired
    private MockMvc mockMvc;
    @Mock
    private AtlassianService atlassianService;
    @InjectMocks
    AtlassianController atlassianController;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(atlassianController)
                .build();
    }

    
	    @Test
    	public void testGetUserComments() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getCommentsByUserName/issueKey=MFP-27/userName=Md.Eklasur.Rahman";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Hi!! This is Jose Lois Israel Servin"));
    	}
    	
    	@Test
    	public void testgetIssueComments() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getIssueComments/issueKey=MFP-27";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Hi!! This is Jose Lois Israel Servin"));
    	}
    	
    	@Test
    	public void testgetAllUsers() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getAllUsers";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Peter Peter"));
    	}
    	
    	@Test
    	public void testGetRemoteIssueLink() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getRemoteIssueLink/issueKey=MFP-39";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    //Assert.assertEquals(true, result.getBody().contains(""));
    	}

    	@Test
    	public void testGetEditIssueMeta() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getEditIssueMeta/issueKey=MFP-39";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("A small, distinct piece of work."));
    	}
    	
    	
    	@Test
    	public void testgetComment() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getComment/issueKey=MFP-49/commentId=10051";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Hola"));
    	    
    	}
    	@Test
    	public void testgetIssue() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"getIssue/MFP-27";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("My First Project"));
    	    
    	}
    	
        @Test
        public void testCreateIssue() throws Exception {

        	String j =  "{\"fields\":{\"project\":{\"key\":\"MFP\"},\"summary\":\"hero number 1.\","
        			+ "\"customfield_10011\":\"Eklas\",\"priority\":{\"name\":\"Medium\"},"
        			+ "\"labels\":[\"Ford\"],\"reporter\":{\"id\":\"6051fc7c45a3bb006801ee86\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\","
        			+ "\"content\":[{\"type\":\"text\",\"text\":\"bhnhnhhgghgghhhhhnhnhnhnhnhnhnhnhnhnh\"}]}]},\"issuetype\":{\"name\":\"Epic\"}}}";
            mockMvc.perform(post( "/response/createIssue")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(j))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
//            
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.registros_status", Matchers.is("SUCCESS")));
//                    .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
//                    .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
       }
        
        @Test
        public void testUpdateIssue() throws Exception {

        	String j =  "{\"fields\":{\"project\":{\"key\":\"MFP\"},\"summary\":\"hero number 1.\","
        			+ "\"customfield_10011\":\"Eklas\",\"priority\":{\"name\":\"Medium\"},"
        			+ "\"labels\":[\"Ford\"],\"reporter\":{\"id\":\"6051fc7c45a3bb006801ee86\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\","
        			+ "\"content\":[{\"type\":\"text\",\"text\":\"bhnhnhhgghgghhhhhnhnhnhnhnhnhnhnhnhnh\"}]}]},\"issuetype\":{\"name\":\"Epic\"}}}";
            mockMvc.perform(post( "/response/createIssue")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(j))
                    .andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
//            
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.registros_status", Matchers.is("SUCCESS")));
//                    .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
//                    .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
       }
        
        
        @Test
        public void testAssigne() throws Exception {

        	String j =  "{\"accountId\":"
        			+ "\"557058:3b8b0ef5-2dbe-462e-ba34-58714af666a8\"}";
            mockMvc.perform(put( "/response/assignee/issueKey=MFP-49")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(j))
                    .andExpect(status().isNoContent())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
          }
        
        
        @Test
        public void testDeleteComment() throws Exception 
        {
        	mockMvc.perform(delete("/response/deleteComment/issueKey=MFP-105/commentId=10073"))
                .andExpect(status().isNoContent());
        }
        
       
        @Test
        public void testGetCommentList() throws Exception {

        	String json =  "{\"ids\":[10054,10055,10056]}";
            mockMvc.perform(post( "/response/getCommentList")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.maxResults").exists());

        }
       
//        @Test
//        public void testAddCustomField() throws Exception {
//
//        	String json = "{\"searcherKey\":\"com.atlassian.jira.plugin.system.customfieldtypes:grouppickersearcher\",\"name\":\"New custom field\",\"description\":\"Custom field for picking groups\",\"type\":\"com.atlassian.jira.plugin.system.customfieldtypes:grouppicker\"}";
//            mockMvc.perform(post( "/response/addCustomField")
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .content(json))
//                    .andExpect(status().isCreated())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$registros_status").exists());
//
//        }
//       
       
        
        @Test
        public void testUpdateComment() throws Exception {

        	String json =  "{\"visibility\":{\"type\":\"role\",\"value\":\"Administrators\"},\"body\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"text\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\",\"type\":\"text\"}]}]}}";
            mockMvc.perform(put( "/response/updateComment/issueKey=MFP-68/commentId=10054")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(json))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());

        }
        
//        
//        @Test
//        public void testEditIssue() throws Exception {
//
//        	String json =  "{\"visibility\":{\"type\":\"role\",\"value\":\"Administrators\"},\"body\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"text\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\",\"type\":\"text\"}]}]}}";
//            mockMvc.perform(put( "/response/updateComment/issueKey=MFP-68/commentId=10054")
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .content(json))
//                    .andExpect(status().isOk())
//                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
//
//        }
//        
        
       
        
       @Test
        public void testDeleteIssue() throws Exception 
        {
     	mockMvc.perform(delete("/response/deleteIssue/MFP-122"))
             .andExpect(status().isNoContent());
       }

        
        
        

    	}
