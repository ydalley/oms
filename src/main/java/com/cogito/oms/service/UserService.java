package com.cogito.oms.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.User;



public interface UserService {
	User getUser(long lcID);
	
	Page<User> getAllUsers(Pageable pageDetails);
	Page<User> findUser(String pattern,Pageable pageDetails);
//	User  getCurrentUser();
	User getUserForLoginByName(String userId);
	String modify(User user) throws ApplicationException;
	String add(User user) throws ApplicationException;
	String enable(User user) throws ApplicationException;
	String toggleStatus(User user) throws ApplicationException;
	String disable(User user) throws ApplicationException;
	void setUserPassword(User user, String oldPassword, String newPassword) throws ApplicationException;
	void setUserPassword(User user, String newPassword) throws ApplicationException;
	void genearteUserPassword(User user)
			throws ApplicationException;
}
