
package com.jira.model.issueResponse;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "self",
    "id",
    "author",
    "body",
    "updateAuthor",
    "created",
    "updated",
    "visibility",
    "jsdPublic"
})
public class Comment {

    @JsonProperty("self")
    private String self;
    @JsonProperty("id")
    private String id;
    @JsonProperty("author")
    private Author author;
    @JsonProperty("body")
    private Body body;
    @JsonProperty("updateAuthor")
    private UpdateAuthor updateAuthor;
    @JsonProperty("created")
    private String created;
    @JsonProperty("updated")
    private String updated;
    @JsonProperty("visibility")
    private Visibility visibility;
    @JsonProperty("jsdPublic")
    private Boolean jsdPublic;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("self")
    public String getSelf() {
        return self;
    }

    @JsonProperty("self")
    public void setSelf(String self) {
        this.self = self;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("author")
    public Author getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(Author author) {
        this.author = author;
    }

    @JsonProperty("body")
    public Body getBody() {
        return body;
    }

    @JsonProperty("body")
    public void setBody(Body body) {
        this.body = body;
    }

    @JsonProperty("updateAuthor")
    public UpdateAuthor getUpdateAuthor() {
        return updateAuthor;
    }

    @JsonProperty("updateAuthor")
    public void setUpdateAuthor(UpdateAuthor updateAuthor) {
        this.updateAuthor = updateAuthor;
    }

    @JsonProperty("created")
    public String getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(String created) {
        this.created = created;
    }

    @JsonProperty("updated")
    public String getUpdated() {
        return updated;
    }

    @JsonProperty("updated")
    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @JsonProperty("visibility")
    public Visibility getVisibility() {
        return visibility;
    }

    @JsonProperty("visibility")
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    @JsonProperty("jsdPublic")
    public Boolean getJsdPublic() {
        return jsdPublic;
    }

    @JsonProperty("jsdPublic")
    public void setJsdPublic(Boolean jsdPublic) {
        this.jsdPublic = jsdPublic;
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
