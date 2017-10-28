package com.cogito.oms.security;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;
import com.cogito.oms.data.model.User;
import com.cogito.oms.service.ProfileService;
import com.cogito.oms.service.UserService;



/**
 * @author Yemi Dalley
 *
 */
public class InlineRealm extends AuthorizingRealm {

	@Autowired
	private UserService userservice;
	@Autowired
	private ProfileService profileService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		User user = (User)arg0.getPrimaryPrincipal() ;
		Profile profile = profileService.getProfile(user.getProfile().getId());
		
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addRole(profile.getName());
		for (Permission perm : profile.getPermissions()) {
			info.addStringPermission(perm.getName());
		}
		return info;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken arg0) throws AuthenticationException {
		UsernamePasswordToken at = (UsernamePasswordToken) arg0;
		SimpleAuthenticationInfo info = null;
		User user;
		user = userservice.getUserForLoginByName(at.getUsername());
		if(user == null )
			throw new AuthenticationException("Unknown user");
		if(!"E".equals(user.getStatus())){
			throw new AuthenticationException("Inactive user account, contact your administrator");
		}
		info = new SimpleAuthenticationInfo(user,
				user.getPassword(), ByteSource.Util.bytes(""), getName());
		return info;
	}

	
	
}
