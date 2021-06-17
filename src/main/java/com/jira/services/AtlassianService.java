package com.jira.services;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;
import com.jira.controllers.AtlassianController;
import com.jira.model.Assign;
import com.jira.model.CommenIdtList;
import com.jira.model.Comment;
import com.jira.model.CustomField;
import com.jira.model.Issue;
import com.jira.model.ProjectModel;
import com.jira.model.updateComment.UpdateComment;
import com.jira.model.Reponse;
import com.jira.model.subtask.SubTask;

@Service
public class AtlassianService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AtlassianController.class);
	@Value("${jira.uri}")
	String jiraUri;
	@Autowired
	private RestTemplate restTemplate;
	
	HttpHeaders createHeaders(String username, String password){
		   return new HttpHeaders() {{
		         String auth = username + ":" + password;
		         byte[] encodedAuth = Base64.encodeBase64( 
		            auth.getBytes(Charset.forName("US-ASCII")) );
		         String authHeader = "Basic " + new String( encodedAuth );
		         set( "Authorization", authHeader );
		      }};
		}  
	
	

   
    public ResponseEntity<Reponse> createIssue(Issue issue) {
    	        Reponse resp = new Reponse();
    	        resp.setNombre("Created Issue");
    	        resp.setRegistros_status("SUCCESS");
    	        LOGGER.info("Creating issue ");
    	        
   	    	HttpHeaders headers = new HttpHeaders();
   	        headers.setContentType(MediaType.APPLICATION_JSON);
   	        HttpEntity<Issue> entity =new HttpEntity<Issue>(issue,headers);
   	        try {
   	        	LOGGER.info("pusing data into Jira");
   	        restTemplate.exchange(
   	        		jiraUri+"/issue", HttpMethod.POST, entity, Issue.class).getBody();
   	            }
   	        
   	        catch(Exception e) {
   	        		LOGGER.error("DATA NOT FOUND");
   	            	resp.setRegistros_status("FAILED");
   	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
   	            	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
   	        	}
   	        LOGGER.info("created isuue successfully");
   	        return new ResponseEntity<Reponse>(resp, HttpStatus.CREATED);
   	    }
	
    
    public ResponseEntity<Reponse> assignee(Assign assign,String issueKey) {
    
    	      Reponse resp = new Reponse();
	          resp.setNombre("Trying to assign: "+assign.getName());
	          resp.setRegistros_status("SUCCESS");
	          LOGGER.info("Assign started in issue: "+issueKey);
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<Assign> entity =new HttpEntity<Assign>(assign,headers);
 	        try {
 	        	LOGGER.info("pusing data into Jira");
 	        restTemplate.exchange(
 	        		jiraUri+"/issue/"+issueKey+"/assignee", HttpMethod.PUT, entity, Issue.class).getBody();
 	           }
 	        catch(Exception e) {
       		LOGGER.error("FAIL TO ASSIGN: "+assign.getName());
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
       	  }
 	        return new ResponseEntity<Reponse>(resp, HttpStatus.NO_CONTENT);
 	        
 	    }
	
	
	
    public ResponseEntity<Reponse> deleteIssue(String issueKey ) {
	           Reponse resp = new Reponse();
	           resp.setNombre("Trying to delete Issue: "+issueKey);
	           resp.setRegistros_status("SUCCESS");
	           LOGGER.info("Creating issue ");
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        HttpEntity<String> entity =new HttpEntity<String>(headers);
	        try {
	        	LOGGER.info("deleting data from Jira");
	        restTemplate.exchange(
	        		jiraUri+"/issue/"+issueKey, HttpMethod.DELETE, entity, String.class).getBody();
	            }
	        
	        catch(Exception e) {
	        		LOGGER.error("DATA NOT FOUND");
	            	resp.setRegistros_status("FAILED");
	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
	            	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
	        	}
	        return new ResponseEntity<Reponse>(resp, HttpStatus.NO_CONTENT);
	    }
 

 	   
  public ResponseEntity<Reponse> editIssue(Issue issue,String issueKey) {
  	          Reponse resp = new Reponse();
  	          resp.setNombre("Created Issue");
  	          resp.setRegistros_status("SUCCESS");
  	          LOGGER.info("Creating issue ");
 	    	HttpHeaders headers = new HttpHeaders();
 	        headers.setContentType(MediaType.APPLICATION_JSON);
 	        HttpEntity<Issue> entity =new HttpEntity<Issue>(issue,headers);
 	        try {
 	        	LOGGER.info("Editing data into Jira");
 	        restTemplate.exchange(
 	        		jiraUri+"/issue/"+issueKey, HttpMethod.PUT, entity, Issue.class).getBody();
 	            }
 	        
 	        catch(Exception e) {
 	        		LOGGER.error("DATA NOT FOUND");
 	            	resp.setRegistros_status("FAILED");
 	            	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
 	            	return new ResponseEntity<Reponse>(resp, HttpStatus.FORBIDDEN);
 	        	  
 	        	}
 	        	
 	        return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
 	    	 
 	     }
  
	
  public ResponseEntity<Reponse> addComment(Comment comment, String issueKey)  {
        Reponse resp = new Reponse();
        resp.setNombre("Add Comment");
        resp.setRegistros_status("SUCCESS");
        LOGGER.info("Adding comment ");
   	HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<Comment> entity =new HttpEntity<Comment>(comment,headers);
       try {
    	   LOGGER.info("adding comment into Jira");
       restTemplate.exchange(
       		jiraUri+"/issue/"+issueKey+"/comment", HttpMethod.POST, entity, Comment.class).getBody();
           }
       
       catch(Exception e) {
       		LOGGER.error("Fail to add comment");
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
       	  
       	}
       	
       return new ResponseEntity<Reponse>(resp, HttpStatus.CREATED);
   	 
    }
	
  public ResponseEntity<Reponse> deleteComment(String issueKey,String commentId )  {
          Reponse resp = new Reponse();
          resp.setNombre("Get Issue: "+issueKey);
          resp.setRegistros_status("SUCCESS");
          LOGGER.info("Trying to delete comment: "+commentId);
     	HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<String> entity =new HttpEntity<String>(headers);
       try {
    	   LOGGER.info("deleting comment from Jira");
     restTemplate.exchange(
       		jiraUri+"/issue/"+issueKey+"/comment/"+commentId, HttpMethod.DELETE, entity, String.class).getBody();
           }
       
       catch(Exception e) {
       		LOGGER.error("COMMENT NOT FOUND");
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	//return "COMMENT: "+commentId+" NOT FOUND";
        	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
       	  
       	}
       return new ResponseEntity<Reponse>(resp, HttpStatus.NO_CONTENT);
   }
  
  
  public String getCommentList(CommenIdtList comment)  {
          Reponse resp = new Reponse();
          resp.setNombre("Get Comment List");
          resp.setRegistros_status("SUCCESS");
        LOGGER.info("Getting comment(s) respect to your ids ");
     	HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<CommenIdtList> entity =new HttpEntity<CommenIdtList>(comment,headers);
       try {
    	   LOGGER.info("retrieving data from Jira");
       return restTemplate.exchange(
       		jiraUri+"/comment/list", HttpMethod.POST, entity, String.class).getBody();
           }
       
       catch(Exception e) {
       		LOGGER.error("Fail to add comment");
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND).toString();
       
         }
   }

  
  
  public  ResponseEntity<Reponse> addCustomField(CustomField customField) {
        Reponse resp = new Reponse();
        resp.setNombre("Add Custom Field");
        resp.setRegistros_status("SUCCESS");
        LOGGER.info("Adding custom fields ");
   	HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<CustomField> entity =new HttpEntity<CustomField>(customField,headers);
       try {
    	   LOGGER.info("pusing data into Jira");
        restTemplate.exchange(
       		jiraUri+"/field", HttpMethod.POST, entity, String.class).getBody();
           }
       
       catch(Exception e) {
       		LOGGER.error("Fail to add custom fileds");
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
         }
       	
       return new ResponseEntity<Reponse>(resp, HttpStatus.CREATED);
   	 }
 
  
  public  ResponseEntity<Reponse> updateComment(UpdateComment updateComment,String issueKey, @PathVariable String commentId)  {
        Reponse resp = new Reponse();
        resp.setNombre("Update Comment: "+commentId);
        resp.setRegistros_status("SUCCESS");
        LOGGER.info("Updating Comment: "+commentId);
   	HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_JSON);
       HttpEntity<UpdateComment> entity =new HttpEntity<UpdateComment>(updateComment,headers);
       try {
    	   LOGGER.info("updating data into Jira");
       restTemplate.exchange(// https://eklas.atlassian.net/rest/api/3/issue/MFP-27/comment/10047
       		jiraUri+"/issue/"+issueKey+ "/comment/"+commentId, HttpMethod.PUT, entity, UpdateComment.class).getBody();
           }
       
       catch(Exception e) {
       		LOGGER.error("Fail to add custom fileds");
           	resp.setRegistros_status("FAILED");
           	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
           	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
        }
       return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
   	 }




public ResponseEntity<Reponse> updateIssue(Issue issue, String issueKey) {
	 Reponse resp = new Reponse();
     resp.setNombre("Updated Issue");
     resp.setRegistros_status("SUCCESS");
     LOGGER.info("Updating issue ");
     
	HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<Issue> entity =new HttpEntity<Issue>(issue,headers);
    try {
    	LOGGER.info("pusing data into Jira");
    restTemplate.exchange(
    		jiraUri+"/issue/"+issueKey, HttpMethod.PUT, entity, Issue.class).getBody();
        }
    
    catch(Exception e) {
    		LOGGER.error("DATA NOT FOUND");
        	resp.setRegistros_status("FAILED");
        	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
        	return new ResponseEntity<Reponse>(resp, HttpStatus.NOT_FOUND);
    	}
    LOGGER.info("Updated isuue successfully");
    return new ResponseEntity<Reponse>(resp, HttpStatus.OK);
   
     }




public ResponseEntity<Reponse> createProject(ProjectModel projectModel)  {
    Reponse resp = new Reponse();
    resp.setNombre("Created Project");
    resp.setRegistros_status("SUCCESS");
    LOGGER.info("Creating project ");
    
	HttpHeaders headers = new HttpHeaders();
   headers.setContentType(MediaType.APPLICATION_JSON);
   HttpEntity<ProjectModel> entity =new HttpEntity<ProjectModel>(projectModel,headers);
   try {
   	LOGGER.info("pusing data into Jira");
    restTemplate.exchange(
   		jiraUri+"/project", HttpMethod.POST, entity, ProjectModel.class).getBody();
       
       }
   
   catch(Exception e) {
   		LOGGER.error("DATA PUSHED IN JIRA FAILED");
   		LOGGER.error("Please check if insert project key or name duplicate ");
   		
       	resp.setRegistros_status("FAILED");
       	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
       	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
   	}
   LOGGER.info("created Project successfully");
   return new ResponseEntity<Reponse>(resp, HttpStatus.CREATED);
}




public ResponseEntity<Reponse> createSubTask(SubTask subTask) {
	  Reponse resp = new Reponse();
	    resp.setNombre("Created Subtask");
	    resp.setRegistros_status("SUCCESS");
	    LOGGER.info("Creating subtask for "+subTask.getFields().getParent().getKey());
	    
	HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   HttpEntity<SubTask> entity =new HttpEntity<SubTask>(subTask,headers);
	   try {
	   	LOGGER.info("pusing data into Jira");
	    restTemplate.exchange(
	   		jiraUri+"/issue", HttpMethod.POST, entity, ProjectModel.class).getBody();
	       
	       }
	   
	   catch(Exception e) {
	   		LOGGER.error("DATA PUSHED IN JIRA FAILED");
	   		LOGGER.error("Please check if inserted project key or parent key is invalid");
	   		
	       	resp.setRegistros_status("FAILED");
	       	resp.setRegistros_fallidos(resp.getRegistros_fallidos()+1);
	       	return new ResponseEntity<Reponse>(resp, HttpStatus.BAD_REQUEST);
	   	}
	   LOGGER.info("created subtask successfully for "+subTask.getFields().getParent().getKey());
	   return new ResponseEntity<Reponse>(resp, HttpStatus.CREATED);
}

 
}
