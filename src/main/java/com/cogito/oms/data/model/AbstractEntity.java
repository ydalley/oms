package com.cogito.oms.data.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;



@MappedSuperclass
public abstract class AbstractEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7219828492728424063L;

	@Id 
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected Long id;
	/**
	 * @return
	 */
	public Long getId() {
		return id;
	}
	

	/**
	 * @param id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	@Version
    protected int version;
	
    protected String delFlag;
	/**
	 * @return
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return
	 */
	public String getDelFlag() {
		return delFlag;
	}

	/**
	 * @param delFlag
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	
//	@Column(name = "created_by")
//	@CreatedBy
//	private String createdBy;
//
//	@Column(name = "modified_by")
//	@LastModifiedBy
//	private String modifiedBy;
//	
//	@Column(name = "created_date")
//	@CreatedDate
//	private DateTime createdDate;
//	
//	@Column(name = "modified_date")
//	@LastModifiedDate
//	private DateTime modifiedDate;
//	    

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
//			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@JsonIgnore
	 public List<String> getDefaultSearchFields(){
		return new ArrayList<String>();
	};
}
