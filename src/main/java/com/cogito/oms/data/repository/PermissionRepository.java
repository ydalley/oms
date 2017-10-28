package com.cogito.oms.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;



public interface PermissionRepository extends CommonRepository<Permission, Long> {
	List<Permission> findByIdNotIn(Long[] permissions);
	
	@Query("SELECT b FROM Permission b INNER JOIN b.profiles pr WHERE pr = :profile")
	List<Permission> findByProfile(@Param("profile") Profile profile);
	
	
}
