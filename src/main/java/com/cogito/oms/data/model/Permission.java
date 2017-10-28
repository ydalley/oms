package com.cogito.oms.data.model;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
public class Permission extends AbstractEntity {
	private static final long serialVersionUID = 1501425030389582318L;
	public Permission(String name, String category, String description) {
		super();
		this.name = name;
		this.category = category;
		this.description = description;
	}

	public Permission() {
	}

	@NotEmpty
	private String name;
	
	private String category;
	@NotEmpty
	private String description;
	@ManyToMany(mappedBy="permissions")
	private Set<Profile>  profiles;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	
	@JsonIgnore
	public Set<Profile> getProfiles() {
		return profiles;
	}
	public void setProfiles(Set<Profile> profiles) {
		this.profiles = profiles;
	}
	@Override
	public String toString() {
		return "Permission [id=" + id + ", name=" + name + ", category=" + category + ", description=" + description + "]";
	}
	@Override
	public List<String> getDefaultSearchFields() {
			return Arrays.asList("description", "name","category");
	}
}
