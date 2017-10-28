package com.cogito.oms.service.implementation;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;
import com.cogito.oms.data.repository.PermissionRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.PermissionService;

@Service
//@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	private PermissionRepository repo;
	@Autowired
	MessageSource messageSource;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public Permission getPermission(Long id) {
		Permission permission = repo.findOne(id);
		return permission;
	}

	@Override
	public List<Permission> getAllPermissions() {
		log.debug("get All permissions");
		return repo.findAll();
	}


	@Override
	public String modifyPermission(Permission permission) throws ApplicationException {
		String result = "" ;
		try{
			repo.save(permission);
			result= messageSource.getMessage("permission.update.success", null, null);
		}catch(Exception ex){
			result= messageSource.getMessage("permission.update.error", null, null);
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	@Override
	public String createPermission(Permission permission) throws ApplicationException {
		String result = "" ;
		try{
			repo.save(permission);
			result= messageSource.getMessage("permission.create.success", null, null);
		}catch(Exception ex){
			result= messageSource.getMessage("permission.create.error", null, null);
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	@Override
	public String deletePermission(Permission permission) throws ApplicationException {
		throw new ApplicationException("Unimplimented");
		//return null;
	}


	@Override
	public Page<Permission> getPermissions(Pageable page) {
		return repo.findAll(page);
	}

	

	@Override
	public Page<Permission> findPermission(String pattern, Pageable page) {
		
//		ExampleMatcher matcher = ExampleMatcher.matchingAll().withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING).withIgnoreCase().withIgnoreNullValues();
//		return repo.findAll(Example.of(c,matcher),page);
		//return repo.findPermissions(pattern, page);
		return repo.findUsingPattern(pattern, page);
	}

	@Override
	public List<Permission> getAllPermissionsNotInProfile(Profile profile) {
		Long[] permissionArray = new Long[profile.getPermissions().size()];
		int idx = 0;
		for (Permission perm : profile.getPermissions()) {
			permissionArray[idx] = perm.getId();
			idx++;
		}
		// not in NULL check
		if (permissionArray.length == 0)
			permissionArray = new Long[] { -1L };
		List<Permission> optionsNotInProfile = repo.findByIdNotIn(permissionArray);
		return optionsNotInProfile;
	}

	@Override
	public List<Permission> getAllPermissionsInProfile(Profile profile) {
		List<Permission> optionsInProfile = repo.findByProfile(profile);
		return optionsInProfile;
	}

}
