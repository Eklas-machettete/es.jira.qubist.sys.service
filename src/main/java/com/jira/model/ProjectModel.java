
package com.jira.model;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "key",
    "name",
    "projectTypeKey",
    "projectTemplateKey",
    "description",
    "leadAccountId",
    "assigneeType",
    "avatarId"
})
@Generated("jsonschema2pojo")
public class ProjectModel {

    @JsonProperty("key")
    private String key;
    @JsonProperty("name")
    private String name;
    @JsonProperty("projectTypeKey")
    private String projectTypeKey;
    @JsonProperty("projectTemplateKey")
    private String projectTemplateKey;
    @JsonProperty("description")
    private String description;
    @JsonProperty("leadAccountId")
    private String leadAccountId;
    @JsonProperty("assigneeType")
    private String assigneeType;
    @JsonProperty("avatarId")
    private Integer avatarId;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("key")
    public String getKey() {
        return key;
    }

    @JsonProperty("key")
    public void setKey(String key) {
        this.key = key;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("projectTypeKey")
    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    @JsonProperty("projectTypeKey")
    public void setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
    }

    @JsonProperty("projectTemplateKey")
    public String getProjectTemplateKey() {
        return projectTemplateKey;
    }

    @JsonProperty("projectTemplateKey")
    public void setProjectTemplateKey(String projectTemplateKey) {
        this.projectTemplateKey = projectTemplateKey;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("leadAccountId")
    public String getLeadAccountId() {
        return leadAccountId;
    }

    @JsonProperty("leadAccountId")
    public void setLeadAccountId(String leadAccountId) {
        this.leadAccountId = leadAccountId;
    }

    @JsonProperty("assigneeType")
    public String getAssigneeType() {
        return assigneeType;
    }

    @JsonProperty("assigneeType")
    public void setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
    }

    @JsonProperty("avatarId")
    public Integer getAvatarId() {
        return avatarId;
    }

    @JsonProperty("avatarId")
    public void setAvatarId(Integer avatarId) {
        this.avatarId = avatarId;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
