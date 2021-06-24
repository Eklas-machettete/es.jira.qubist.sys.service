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

    
//	    @Test
//    	public void testGetUserComments() throws URISyntaxException 
//    	{
//    	    RestTemplate restTemplate = new RestTemplate();
//    	    final String baseUrl = jiraUri+"commentsByUserName?issueKey=SFK-3&userName=Eklas Rh";
//    	    URI uri = new URI(jiraUri+"commentsByUserName?issueKey=SFK-3&userName=Eklas Rh");
//    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
//    	    Assert.assertEquals(200, result.getStatusCodeValue());
//    	    Assert.assertEquals(true, result.getBody().contains("Hi!! This is Jose Lois Israel Servin"));
//    	}
    	
    	@Test
    	public void testgetIssueComments() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"issueComments?issueKey=SFK-3";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Hi!! This is Jose Lois Israel Servin"));
    	}
    	
    	@Test
    	public void testgetAllProject() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"allProject";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("SFK"));
    	}
    	
    	@Test
    	public void testgetAllTask() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"task?projectKey=SFK";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("A small, distinct piece of work"));
    	}
    	
    	
    	
    	
    	@Test
    	public void testgetAllUsers() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"allUsers";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("Trello"));
    	}
    	
    	@Test
    	public void testGetRemoteIssueLink() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"remoteIssueLink?issueKey=SFK-3";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    //Assert.assertEquals(true, result.getBody().contains(""));
    	}

    	@Test
    	public void testGetEditIssueMeta() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"issueMeta?issueKey=SFK-3";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("A small, distinct piece of work."));
    	}
    	
    	
    	@Test
    	public void testgetComment() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"comment?issueKey=SFK-3&commentId=10002";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("60ccc225c90cb20068162bca"));
    	    
    	}
    	@Test
    	public void testgetIssue() throws URISyntaxException 
    	{
    	    RestTemplate restTemplate = new RestTemplate();
    	    final String baseUrl = jiraUri+"issue?issueKey=SFK-3";
    	    URI uri = new URI(baseUrl);
    	    ResponseEntity<String> result = restTemplate.getForEntity(uri, String.class);
    	    Assert.assertEquals(200, result.getStatusCodeValue());
    	    Assert.assertEquals(true, result.getBody().contains("qq1oft511o21"));
    	    
    	}
    	
        @Test
        public void testCreateIssue() throws Exception {

        	String j =  "{\"fields\":{\"project\":{\"key\":\"SFK\"},\"summary\":\"hero number 1.\","
        			+ "\"customfield_10011\":\"Eklas\",\"priority\":{\"name\":\"Medium\"},"
        			+ "\"labels\":[\"Ford\"],\"reporter\":{\"id\":\"60ccc225c90cb20068162bca\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\","
        			+ "\"content\":[{\"type\":\"text\",\"text\":\"bhnhnhhgghgghhhhhnhnhnhnhnhnhnhnhnhnh\"}]}]},\"issuetype\":{\"name\":\"Epic\"}}}";
            mockMvc.perform(post( "/jira/v1.0/issue")
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
        public void testCreateStory() throws Exception {

        	String j = "{\"fields\":{\"project\":{\"key\":\"SFK\"},\"summary\":\"creating epic from postman\","
        			+ "\"priority\":{\"name\":\"Medium\"},\"labels\":[\"Ford\",\"BMWkuk\","
        			+ "\"Fiujkuat\"],\"reporter\":{\"id\":\"60ccc225c90cb20068162bca\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\","
        			+ "\"content\":[{\"type\":\"text\",\"text\":\"description\"}]}]},\"issuetype\":{\"name\":\"Story\"}}}";
            mockMvc.perform(post( "/jira/v1.0/story")
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
        public void testCreateTask() throws Exception {

        	String j =  "{\"fields\":{\"project\":{\"key\":\"SFK\"},\"summary\":"
        			+ "\"creating epic from postman\",\"priority\":{\"name\":\"Medium\"},"
        			+ "\"labels\":[\"Ford\",\"BMWkuk\",\"Fiujkuat\"],"
        			+ "\"reporter\":{\"id\":\"60ccc225c90cb20068162bca\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":"
        			+ "\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"description\"}]}]},"
        			+ "\"issuetype\":{\"name\":\"Task\"}}}";
            mockMvc.perform(post( "/jira/v1.0/task")
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
        public void testCreateSubTask() throws Exception {

        	String j = "{\"fields\":{\"project\":{\"key\":\"SFK\"},\"parent\":{\"key\":\"SFK-3\"},\"summary\":"
        			+ "\"Sub-task of TEST-101\",\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":"
        			+ "\"paragraph\",\"content\":[{\"type\":\"text\",\"text\":\"description\"}]}]},\"issuetype\":{\"name\":"
        			+ "\"Sub-task\"}}}";
            mockMvc.perform(post("/jira/v1.0/subTask")
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
        public void testCreateProject() throws Exception {

        	String j =  "{\"key\":\"NT\",\"name\":\"Bio Ches\",\"projectTypeKey\":\"software\","
        			+ "\"projectTemplateKey\":\"com.pyxis.greenhopper.jira:gh-scrum-template\","
        			+ "\"description\":\"Example Project description\",\"leadAccountId\":"
        			+ "\"5ee10e86e31f620aba74131a\",\"assigneeType\":\"PROJECT_LEAD\","
        			+ "\"avatarId\":10200}";
            mockMvc.perform(post( "/jira/v1.0/project")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(j))
                    //.andExpect(status().isCreated())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
//            
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.registros_status", Matchers.is("SUCCESS")));
//                    .andExpect(jsonPath("$.value", Matchers.is("Hello World")))
//                    .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
       }
        
        
        
        
        
        
        
        @Test
        public void testUpdateIssue() throws Exception {

        	String j =  "{\"fields\":{\"project\":{\"key\":\"SFK\"},\"summary\":\"hero number 1.\","
        			+ "\"customfield_10011\":\"Eklas\",\"priority\":{\"name\":\"Medium\"},"
        			+ "\"labels\":[\"Ford\"],\"reporter\":{\"id\":\"60ccc225c90cb20068162bca\"},"
        			+ "\"description\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\","
        			+ "\"content\":[{\"type\":\"text\",\"text\":\"bhnhnhhgghgghhhhhnhnhnhnhnhnhnhnhnhnh\"}]}]},\"issuetype\":{\"name\":\"Epic\"}}}";
            mockMvc.perform(post( "/jira/v1.0/issue")
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
        			+ "\"60ccc225c90cb20068162bca\"}";
            mockMvc.perform(put( "/jira/v1.0/assignee/issueKey=SFK-3")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(j))
                    .andExpect(status().isNoContent())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.registros_status").exists());
          }
        
        
        @Test
        public void testDeleteComment() throws Exception 
        {
        	mockMvc.perform(delete("/jira/v1.0/comment/issueKey=SFK-7/commentId=10019"))
                .andExpect(status().isNoContent());
        }
        
       
        @Test
        public void testGetCommentList() throws Exception {

        	String json =  "{\"ids\":[10002,10003]}";
            mockMvc.perform(post( "/jira/v1.0/commentList")
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
       
        
//        @Test
//        public void testUpdateComment() throws Exception {
//
//        	String json =  "{\"visibility\":{\"type\":\"role\",\"value\":\"Administrators\"},\"body\":{\"type\":\"doc\",\"version\":1,\"content\":[{\"type\":\"paragraph\",\"content\":[{\"text\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque eget venenatis elit. Duis eu justo eget augue iaculis fermentum. Sed semper quam laoreet nisi egestas at posuere augue semper.\",\"type\":\"text\"}]}]}}";
//            mockMvc.perform(put( "/jira/v1.0/comment/issueKey=SFK-3/commentId=10002")
//                    .contentType(MediaType.APPLICATION_JSON_VALUE)
//                    .content(json))
//                    .andExpect(MockMvcResultMatchers.jsonPath("Update Comment: 10002").exists());
//
//        }
        
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
     	mockMvc.perform(delete("/jira/v1.0/issue/SFK-42"))
             .andExpect(status().isNoContent());
       }

        
        
        

    	}
