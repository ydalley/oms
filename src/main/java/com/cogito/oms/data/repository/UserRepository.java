package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.User;


@Repository
public interface UserRepository extends CommonRepository<User,Long> {
	
	User findByLoginIdAndStatus(String name, String status);
	User findFirstByLoginId(String name);

}
