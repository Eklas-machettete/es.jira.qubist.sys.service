package com.jira.controllers;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jira.model.*;
import com.jira.model.issueResponse.IssueCommentResponse;
import com.jira.model.issueResponse.UserComments;
import com.jira.model.subtask.SubTask;
import com.jira.model.updateComment.UpdateComment;
import com.jira.services.AtlassianService;


@RestController
@RequestMapping("/jira/v1.0")
public class AtlassianController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AtlassianController.class);
	@Value("${jira.uri}")
	String jiraUri;
	@Value("${jira.host}")
	String jiraHost;
	private RestTemplate restTemplate;
	private AtlassianService atlassianService;
	@Autowired
	public AtlassianController(AtlassianService atlassianService,RestTemplate restTemplate)  {
		this.atlassianService=atlassianService;
		this.restTemplate=restTemplate;
		
	}
	
	HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }
		   };
		}   
     

	 @PostMapping(
    	        value = "/issue",
    	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> createIssue(@RequestBody Issue issue) 
           {
		      LOGGER.trace(jiraHost+"issue access");
    	      return  atlassianService.createIssue(issue);
				
    	     }
	 
	  @PutMapping(
	  	        value = "/issue/issueKey={issueKey}",
	  	        consumes = MediaType.APPLICATION_JSON_VALUE)
	     public  ResponseEntity<Reponse> updateIssue(@RequestBody Issue issue, @PathVariable String issueKey) 
	         {
	           LOGGER.trace(jiraHost+"/issue/issueKey="+issueKey+" access");
	   	       return atlassianService.updateIssue(issue, issueKey);
	   	   
	  	     }
     
     @PutMapping(
 	        value = "/assignee/issueKey={issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
 	 public ResponseEntity<Reponse> assignee(@RequestBody Assign assign, @PathVariable String issueKey) {
    	 LOGGER.trace(jiraHost+" assignee/issueKey="+issueKey+" access");
    	 return atlassianService.assignee(assign,issueKey);  
		
 	    }

     @DeleteMapping(
 	        value = "/issue/{issueKey}",
 	       produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> deleteIssue(@PathVariable String issueKey ) 
        {
    	 LOGGER.trace(jiraHost+"issue/issueKey="+issueKey+" access");
    	 return atlassianService.deleteIssue(issueKey ) ;
 	    }
     

    @GetMapping(
  	        value = "/issue",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getIssue(@RequestParam String issueKey ) 
         {
    	    LOGGER.trace(jiraHost+"issue/"+issueKey+" access");
   	        Reponse resp = new Reponse();
   	        resp.setNombre("Get Issue: "+issueKey);
   	        resp.setRegistros_status("SUCCESS");
   	        LOGGER.info("Trying to get Issue: "+issueKey);
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<String> entity =new HttpEntity<String>(headers);
  	        try {
  	        	LOGGER.info("retrieving data from Jira");
  	        return restTemplate.exchange(
  	        		jiraUri+"/issue/"+issueKey, HttpMethod.GET, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("DATA NOT FOUND");
  	            	resp.setRegistros_status(e.getMessage());
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "[ISSUE: "+issueKey+" NOT FOUND.] "+e.getMessage();
  	         }
  	     }
      
      
    @PutMapping(
 	        value = "/issue/{issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reponse> editIssue(@RequestBody Issue issue, @PathVariable String issueKey) 
        {
    	   LOGGER.trace(jiraHost+"issue/"+issueKey+" access");
  	       return atlassianService.editIssue(issue, issueKey);
 	    }
     
     
    @PostMapping(
  	        value = "/commentByAdministrators/{issueKey}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reponse> addComment(@RequestBody Comment comment, @PathVariable String issueKey) 
         {
    	   LOGGER.trace(jiraHost+"CommentByAdministrators/"+issueKey+" access");
   	       return atlassianService.addComment(comment,issueKey) ;
  	    	 
  	     }
     
     
     @GetMapping(
   	        value = "/comment",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String getComment(@RequestParam String issueKey, @RequestParam  String commentId ) 
          {
    	 
    	        LOGGER.trace(jiraHost+"/comment/issueKey="+issueKey+"/commentId="+commentId+" access");
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get Comment: "+commentId);
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get Comment: "+commentId);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        try {
   	        	LOGGER.info("retrieving data from Jira");
   	        return restTemplate.exchange(//https://eklas.atlassian.net/rest/api/3/issue/MFP-39/comment/10038
   	        		jiraUri+"/issue/"+issueKey+"/comment/"+commentId, HttpMethod.GET, entity, String.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("COMMENT NOT FOUND");
   	            	resp.setRegistros_status(e.getMessage());
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "[COMMENT: "+commentId+" NOT FOUND.] "+e.getMessage();
   	        	}
   	     }
          
     
     @DeleteMapping(
    	        value = "/comment/issueKey={issueKey}/commentId={commentId}",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> comment(@PathVariable String issueKey, @PathVariable String commentId ) 
           {
	           LOGGER.trace(jiraHost+"/comment/issueKey="+issueKey+"/commentId="+commentId+" access");
     	       return atlassianService.deleteComment(issueKey, commentId);
    	        	
    	   }
      
     
     @GetMapping(
    	        value = "/issueMeta",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String GetEditIssueMeta(@RequestParam String issueKey ) 
           {
    	           LOGGER.trace(jiraHost+"/issueMeta/issueKey="+issueKey+" access");
     	           Reponse resp = new Reponse();
     	           resp.setNombre("Get issue meta: "+issueKey);
     	           resp.setRegistros_status("SUCCESS");
     	           LOGGER.info("Trying to get issue Meta: "+issueKey);
    	    	HttpHeaders headers = new HttpHeaders();
    	        headers.setContentType(MediaType.APPLICATION_JSON);
    	        HttpEntity<String> entity =new HttpEntity<String>(headers);
    	        try {
    	        	LOGGER.info("retrieving data from Jira");
    	        return restTemplate.exchange(//https://eklas.atlassian.net/rest/api/3/issue/MFP-39/comment/10038
    	        		jiraUri+"/issue/"+issueKey+"/editmeta", HttpMethod.GET, entity, String.class).getBody();
    	            }
    	        
    	        catch(Exception e) {
    	        		LOGGER.error("ISSUE META NOT FOUND");
    	            	resp.setRegistros_status(e.getMessage());
    	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
    	            	return "[ISSUE: "+issueKey+" NOT FOUND.] "+e.getMessage();
    	        	}
    	     }
      
        
    @GetMapping(
 	        value = "/remoteIssueLink",
 	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRemoteIssueLink(@RequestParam String issueKey) 
        {
    	      LOGGER.trace(jiraHost+"/remoteIssueLink/issueKey="+issueKey+" access");
  	          Reponse resp = new Reponse();
  	          resp.setNombre("Get remote issue link: "+issueKey);
  	          resp.setRegistros_status("SUCCESS");
  	          LOGGER.info("Trying to get remote issue link: "+issueKey);
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<String> entity =new HttpEntity<String>(headers);
 	        try {
 	        	LOGGER.info("retrieving data from Jira");
 	        return restTemplate.exchange(//rest/api/2/issue/{issueIdOrKey}/remotelink
 	        		jiraUri+"/issue/"+issueKey+"/remotelink", HttpMethod.GET, entity, String.class).getBody();
 	            }
 	        
 	        catch(Exception e) {
 	        		LOGGER.error("ISSUE NOT FOUND");
 	            	resp.setRegistros_status(e.getMessage());
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return "[ISSUE: "+issueKey+" NOT FOUND.] "+e.getMessage();
 	        	}
 	     }
        
     @GetMapping(
   	        value = "/allUsers",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String getAllUsers( ) 
          {
	            LOGGER.trace(jiraHost+"allUsers access");
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get all users");
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get all users:");
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        try {
   	        	LOGGER.info("retrieving data from Jira");
   	        return restTemplate.exchange(
   	        		 jiraUri+"/users/search", HttpMethod.GET, entity, String.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("DATA NOT FOUND");
   	            	resp.setRegistros_status(e.getMessage());
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "[USERS NOT FOUND.] "+e.getMessage();
   	        	}
   	     }
     

     @GetMapping(
  	        value = "/issueComments",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCommentOfIssue(@RequestParam String issueKey ) 
         {
    	      LOGGER.trace(jiraHost+"/issueComments/issueKey="+issueKey+" access");
   	          Reponse resp = new Reponse();
   	          resp.setNombre("Get comment(s) of Issue: "+issueKey);
   	          resp.setRegistros_status("SUCCESS");
   	          LOGGER.info("Trying to get Comment of Issue: "+issueKey);
  	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<String> entity =new HttpEntity<String>(headers);
  	        try {
  	        	LOGGER.info("retrieving data from Jira");
  	        return restTemplate.exchange(
  	        		jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.GET, entity, String.class).getBody();
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("COMMENT(S) NOT FOUND");
  	            	resp.setRegistros_status(e.getMessage());
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "[COMMENT IF ISSUE: "+issueKey+" NOT FOUND.] "+e.getMessage();
  	        	 
  	        	}
  	    }
      
      
     @PostMapping(
  	        value = "/commentList",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getCommentList(@RequestBody CommenIdtList comment) 
         {
    	    LOGGER.trace(jiraHost+"/commentList access");
   	        return atlassianService.getCommentList(comment)	;	
  	        	  
  	      }
  	        	
  	     
  	    
     @PostMapping(
  	        value = "/customField",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> addCustomField(@RequestBody CustomField customField) 
         {
    	    LOGGER.trace(jiraHost+"/customField access");
   	        return atlassianService.addCustomField(customField);
  	        
  	     }
       
     
     @PutMapping(
  	        value = "/comment/issueKey={issueKey}/commentId={commentId}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public  ResponseEntity<Reponse> updateComment(@RequestBody UpdateComment updateComment, @PathVariable String issueKey, @PathVariable String commentId) 
         {
           LOGGER.trace(jiraHost+"/comment/issueKey="+issueKey+"/commentId="+commentId+" access");
   	       return atlassianService.updateComment(updateComment, issueKey, commentId);
  	    	 
  	     }
     
     
    @GetMapping(
   	        value = "/commentsByUserName",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
    public UserComments getUserComment(@RequestParam String issueKey, @RequestParam String userName) 
          {
              LOGGER.trace(jiraHost+"/commentsByUserName/issueKey="+issueKey+"/userName="+userName+" access");
    	      Reponse resp = new Reponse();
    	      resp.setNombre("Get comment(s) of Issue: "+issueKey);
    	      resp.setRegistros_status("SUCCESS");
    	    LOGGER.info("Trying to get Comment of Issue: "+issueKey);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        UserComments userComments=new UserComments();
   	        try {
   	        	LOGGER.info("retrieving data from Jira");
   	        	IssueCommentResponse issueCommentResponse =restTemplate.exchange(
   	        			jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.GET, entity,IssueCommentResponse.class).getBody();
   	        	
   	        	for(com.jira.model.issueResponse.Comment comment:issueCommentResponse.getComments())
   	        	{
   	        		if(comment.getAuthor().getDisplayName().equals(userName))
   	        		{
   	        			userComments.getList().add(comment);
   	        		}
   	        	}
   	        	LOGGER.info("Total comments: "+issueCommentResponse.getTotal());
   	        	return userComments;
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("COMMENT(S) NOT FOUND");
   	            	resp.setRegistros_status(e.getMessage());
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	//return "COMMENT IF ISSUE: "+issueKey+" NOT FOUND";
   	            	return userComments;
   	        	  
   	        }
   	     }
    
    
    
    @GetMapping(
  	        value = "/allProject",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getProject()
         {
    	      ObjectMapper mapper = new ObjectMapper();
    	      LOGGER.trace(jiraHost+"/allProject access");
   	          Reponse resp = new Reponse();
   	          resp.setNombre("Get all projects ");
   	          resp.setRegistros_status("SUCCESS");
   	          LOGGER.info("Trying to get all projects: ");
   	        List<ProjectIndentifier> projectIndentifiers=new ArrayList<ProjectIndentifier>();
	    	HttpHeaders headers = new HttpHeaders();
  	        headers.setContentType(MediaType.APPLICATION_JSON);
  	        HttpEntity<String> entity =new HttpEntity<String>(headers);
  	        try {
  	        	LOGGER.info("retrieving data from Jira");
  	            String str= restTemplate.exchange(
  	        		jiraUri+"/project/", HttpMethod.GET, entity, String.class, "id").getBody();
  	            ProjectResponse[] pr = mapper.readValue(str, ProjectResponse[].class);
  	            List< ProjectResponse> pr1 = Arrays.asList(mapper.readValue(str,  ProjectResponse[].class));
  	     for(ProjectResponse p:pr1)
  	    {
  	    	ProjectIndentifier pi=new ProjectIndentifier();
  	    	pi.setKey(p.getKey());
  	    	pi.setName(p.getName());
 	    	projectIndentifiers.add(pi);
  	    }
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(projectIndentifiers);
        LOGGER.info("retrieving project indentifier and name from Jira successfully");
  	   return json;
  	            }
  	        
  	        catch(Exception e) {
  	        		LOGGER.error("PROJECT(S) NOT FOUND");
  	        		LOGGER.info("retrieving project indentifier and name from Jira unsuccessfully");
  	            	resp.setRegistros_status(e.getMessage());
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return e.getMessage();
  	        	  
  	        	}
  	    }
    
    
    @PostMapping(
	        value = "/project",
	        consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Reponse> createProject(@RequestBody ProjectModel projectModel) 
       {
	      LOGGER.trace(jiraHost+"project access");
	      return  atlassianService.createProject(projectModel);
			
	     }
 
    
    @PostMapping(
	        value = "/subTask",
	        consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Reponse> createSubTask(@RequestBody SubTask subTask) 
       {
	      LOGGER.trace(jiraHost+"subTask access");
	      return  atlassianService.createSubTask(subTask);
			
	     }
 
    
    
    @PostMapping(
	        value = "/story",
	        consumes = MediaType.APPLICATION_JSON_VALUE)
 public ResponseEntity<Reponse> createStory(@RequestBody Issue issue) 
       {
	      LOGGER.trace(jiraHost+"issue access");
	      return  atlassianService.createIssue(issue);
			
	     }
    
    
    @PostMapping(
	        value = "/task",
	        consumes = MediaType.APPLICATION_JSON_VALUE)
 public ResponseEntity<Reponse> createTask(@RequestBody Issue issue) 
       {
	      LOGGER.trace(jiraHost+"issue access");
	      return  atlassianService.createIssue(issue);
			
	     }

    @GetMapping(
   	        value = "/task",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String getAllTask(@RequestParam String projectKey ) 
          {
	            LOGGER.trace(jiraHost+"/task access");
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Get all tasks");
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Trying to get all tasks of project:"+projectKey);
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<String> entity =new HttpEntity<String>(headers);
   	        try {
   	        	LOGGER.info("retrieving data from Jira");
   	        	System.out.println(jiraUri+"search/?jql=project="+projectKey +"AND issuetype = task");
   	        return restTemplate.exchange(
   	        		 jiraUri+"/search/?jql=project="+projectKey +" AND issuetype = task&maxResults=100", HttpMethod.GET, entity, String.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("DATA NOT FOUND");
   	            	resp.setRegistros_status(e.getMessage());
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "TASKS ARE NOT FOUND";
   	        	}
   	     }
    
    
    
    
    
       }