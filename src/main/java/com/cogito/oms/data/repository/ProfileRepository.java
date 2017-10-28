package com.cogito.oms.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Profile;


public interface ProfileRepository extends CommonRepository<Profile, Long> {

	Page<Profile> findByNameContaining(String name ,Pageable details);
	
	
}
