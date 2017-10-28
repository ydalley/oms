package com.cogito.oms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;

import java.util.List;

public interface PermissionService {

    public String createPermission(Permission permission) throws ApplicationException;

    public String modifyPermission(Permission permission)throws ApplicationException;

    public String deletePermission(Permission permission)throws ApplicationException;

    public Permission getPermission(Long Id);

    public List<Permission> getAllPermissions();

    public Page<Permission> getPermissions(Pageable page);
    
    List<Permission> getAllPermissionsNotInProfile(Profile profile);

	Page<Permission> findPermission(String pattern, Pageable page);

	List<Permission> getAllPermissionsInProfile(Profile profile);



}
