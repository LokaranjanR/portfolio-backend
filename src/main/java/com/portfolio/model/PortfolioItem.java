package com.portfolio.model;

import java.io.IOException;

import org.springframework.data.annotation.Id;
import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.data.mongodb.core.mapping.Document;

public class PortfolioItem {
    @Id
    private String id;
    private String project_name;
    private String description;
    private String skills;
    private String repo_link;
    private String s3Key;
    
    


//Default constructor with no parameters
public PortfolioItem() {
	
}

//a constructor with required parameters

public PortfolioItem(String project_name,String description,String skills,String repo_link,String s3Key) {
	this.project_name = project_name;
	this.description = description;
	this.skills = skills;
	this.repo_link = repo_link;
	this.s3Key = s3Key;
	
}
public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}

public String getprojectname() {
    return project_name;
}

public void setprojectname(String project_name) {
    this.project_name = project_name;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public String getSkills() {
	return skills;
}

public void setSkills(String skills) {
	this.skills = skills;
}

public String getrepolink() {
	return repo_link;
}

public void setrepolink(String repo_link) {
	this.repo_link = repo_link;
}

public String getS3Key() {
	return s3Key;
}

public void setS3Key(String s3Key) {
	this.s3Key = s3Key;
}

// Override the toString method to return a JSON representation of the object
public String toString() {
	return  "{\"id\":\"" + id + "\",\"project_name\":\"" + project_name + "\",\"description\":\"" + description +
            "\",\"skills\":\"" + skills + "\",\"repo_link\":\"" + repo_link + "\"}";
}

public static PortfolioItem fromJsonString(String jsonString) {
	try {
		ObjectMapper objectMapper = new ObjectMapper();
		
		return objectMapper.readValue(jsonString, PortfolioItem.class);
	}catch(IOException e) {
		e.printStackTrace();
		return null;
	}
}
}


