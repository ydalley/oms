package com.cogito.oms.data.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;


@Entity
public class Profile extends AbstractEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5906818978570786756L;
	@NotEmpty
	private String name;
	@Email
	private String email;
	
	@ManyToMany(cascade={CascadeType.MERGE},fetch=FetchType.EAGER)
	private Set<Permission> permissions;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void add(Permission p){
		if(permissions == null)
			permissions = new HashSet();
		//p.setId(id);
		permissions.add(p);
	}
	
	@Override
	public String toString() {
		return "Profile [name=" + name + ", email=" + email + "]";
	}
	
	//@JsonIgnore
	//@JsonView(JsonViews.Public.class)
	public Set<Permission> getPermissions() {
		return permissions;
	}
	
	
	
	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}
	@Override
	public List<String> getDefaultSearchFields() {
		return Arrays.asList("name");
	}
}
