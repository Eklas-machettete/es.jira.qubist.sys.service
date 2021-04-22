package com.jira.controllers;
import java.nio.charset.Charset;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.jira.model.*;
import com.jira.model.issueResponse.IssueCommentResponse;
import com.jira.model.issueResponse.UserComments;
import com.jira.model.updateComment.UpdateComment;
import com.jira.services.AtlassianService;

@RestController
@RequestMapping("/response")
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
    	        value = "/createIssue",
    	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> createIssue(@RequestBody Issue issue) 
           {
		      LOGGER.trace(jiraHost+"createIssue access");
    	      return  atlassianService.createIssue(issue);
				
    	     }
	 
	  @PutMapping(
	  	        value = "/updateIssue/issueKey={issueKey}",
	  	        consumes = MediaType.APPLICATION_JSON_VALUE)
	     public  ResponseEntity<Reponse> updateIssue(@RequestBody Issue issue, @PathVariable String issueKey) 
	         {
	           LOGGER.trace(jiraHost+"/updateIssue/issueKey="+issueKey+" access");
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
 	        value = "/deleteIssue/{issueKey}",
 	       produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> deleteIssue(@PathVariable String issueKey ) 
        {
    	 LOGGER.trace(jiraHost+"deleteIssue/issueKey="+issueKey+" access");
    	 return atlassianService.deleteIssue(issueKey ) ;
 	    }
     

    @GetMapping(
  	        value = "/getIssue/{issueKey}",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getIssue(@PathVariable String issueKey ) 
         {
    	    LOGGER.trace(jiraHost+"getIssue/"+issueKey+" access");
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
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "ISSUE: "+issueKey+" NOT FOUND";
  	         }
  	     }
      
      
    @PutMapping(
 	        value = "/editIssue/{issueKey}",
 	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reponse> editIssue(@RequestBody Issue issue, @PathVariable String issueKey) 
        {
    	   LOGGER.trace(jiraHost+"editIssue/"+issueKey+" access");
  	       return atlassianService.editIssue(issue, issueKey);
 	    }
     
     
    @PostMapping(
  	        value = "/addCommentByAdministrators/{issueKey}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Reponse> addComment(@RequestBody Comment comment, @PathVariable String issueKey) 
         {
    	   LOGGER.trace(jiraHost+"addCommentByAdministrators/"+issueKey+" access");
   	       return atlassianService.addComment(comment,issueKey) ;
  	    	 
  	     }
     
     
     @GetMapping(
   	        value = "/getComment/issueKey={issueKey}/commentId={commentId}",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String getComment(@PathVariable String issueKey, @PathVariable String commentId ) 
          {
    	        LOGGER.trace(jiraHost+"/getComment/issueKey="+issueKey+"/commentId="+commentId+" access");
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
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "COMMENT: "+commentId+" NOT FOUND";
   	        	}
   	     }
          
     
     @DeleteMapping(
    	        value = "/deleteComment/issueKey={issueKey}/commentId={commentId}",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> deleteComment(@PathVariable String issueKey, @PathVariable String commentId ) 
           {
	           LOGGER.trace(jiraHost+"/deleteComment/issueKey="+issueKey+"/commentId="+commentId+" access");
     	       return atlassianService.deleteComment(issueKey, commentId);
    	        	
    	   }
      
     
     @GetMapping(
    	        value = "/getEditIssueMeta/issueKey={issueKey}",
    	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String GetEditIssueMeta(@PathVariable String issueKey ) 
           {
    	           LOGGER.trace(jiraHost+"/getEditIssueMeta/issueKey="+issueKey+" access");
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
    	            	resp.setRegistros_status("FAILED");
    	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
    	            	return "ISSUE: "+issueKey+" NOT FOUND";
    	        	}
    	     }
      
        
    @GetMapping(
 	        value = "/getRemoteIssueLink/issueKey={issueKey}",
 	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getRemoteIssueLink(@PathVariable String issueKey ) 
        {
    	      LOGGER.trace(jiraHost+"/getRemoteIssueLink/issueKey="+issueKey+" access");
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
 	            	resp.setRegistros_status("FAILED");
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return "ISSUE: "+issueKey+" NOT FOUND";
 	        	}
 	     }
        
     @GetMapping(
   	        value = "/getAllUsers",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
     public String getAllUsers( ) 
          {
	            LOGGER.trace(jiraHost+"/getAllUsers access");
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
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return "USERS NOT FOUND";
   	        	}
   	     }
     

     @GetMapping(
  	        value = "/getIssueComments/issueKey={issueKey}",
  	        produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCommentOfIssue(@PathVariable String issueKey ) 
         {
    	      LOGGER.trace(jiraHost+"/getIssueComments/issueKey="+issueKey+" access");
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
  	            	resp.setRegistros_status("FAILED");
  	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
  	            	return "COMMENT IF ISSUE: "+issueKey+" NOT FOUND";
  	        	  
  	        	}
  	    }
      
      
     @PostMapping(
  	        value = "/getCommentList",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
    public String getCommentList(@RequestBody CommenIdtList comment) 
         {
    	    LOGGER.trace(jiraHost+"/getCommentList access");
   	        return atlassianService.getCommentList(comment)	;	
  	        	  
  	      }
  	        	
  	     
  	    
     @PostMapping(
  	        value = "/addCustomField",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public ResponseEntity<Reponse> addCustomField(@RequestBody CustomField customField) 
         {
    	    LOGGER.trace(jiraHost+"/addCustomField access");
   	        return atlassianService.addCustomField(customField);
  	        
  	     }
       
     
     @PutMapping(
  	        value = "/updateComment/issueKey={issueKey}/commentId={commentId}",
  	        consumes = MediaType.APPLICATION_JSON_VALUE)
     public  ResponseEntity<Reponse> updateComment(@RequestBody UpdateComment updateComment, @PathVariable String issueKey, @PathVariable String commentId) 
         {
           LOGGER.trace(jiraHost+"/updateComment/issueKey="+issueKey+"/commentId="+commentId+" access");
   	       return atlassianService.updateComment(updateComment, issueKey, commentId);
  	    	 
  	     }
     
     
    @GetMapping(
   	        value = "/getCommentsByUserName/issueKey={issueKey}/userName={userName}",
   	        produces = MediaType.APPLICATION_JSON_VALUE)
    public UserComments getUserComment(@PathVariable String issueKey, @PathVariable String userName) 
          {
              LOGGER.trace(jiraHost+"/getCommentsByUserName/issueKey="+issueKey+"/userName="+userName+" access");
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
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	//return "COMMENT IF ISSUE: "+issueKey+" NOT FOUND";
   	            	return userComments;
   	        	  
   	        }
   	     }
       }