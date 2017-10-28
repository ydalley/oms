package com.cogito.oms.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;
import com.cogito.oms.data.model.User;
import com.cogito.oms.data.repository.PermissionRepository;
import com.cogito.oms.data.repository.ProfileRepository;
import com.cogito.oms.data.repository.UserRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.UserService;

@Component
public class AppInit implements InitializingBean {
	private static final Logger log = LogManager.getLogger(AppInit.class);
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ResourceLoader resourceLoader;

	@Autowired
	private PermissionRepository permissionRepo;
	@Autowired
	private ProfileRepository profilerepository;
	@Autowired
	private UserService userService;

	private Profile defaultProfile;

	@Transactional
	@Override
	public void afterPropertiesSet() throws Exception {
		if (userRepo.count() > 0)
			return;

		createPermissions();
		createDefaultAdmin();
	}

	@Value("${file.permissions}")
	private String permission_csv;

	private void createPermissions() throws ApplicationException {
		try {
			BufferedReader br = null;
			Resource resource = resourceLoader.getResource(permission_csv);
			if(resource.getClass().isInstance(ClassPathResource.class)){
				br = new BufferedReader(
						new InputStreamReader(resource.getInputStream(), Charset.defaultCharset()));
			}else{
				br = new BufferedReader(new FileReader(resource.getFile()));
			}
			String header = br.readLine();
			Set<Permission> permissions = new HashSet<>();
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				
				String[] data = StringUtils.split(line, ",");
				Permission p = new Permission();
				try {
					String category = "";
					String name;
					String desc;
					if (data.length >= 3) {
						category = StringUtils.strip(data[0], "\"");
						name = StringUtils.strip(data[2], "\"");
						desc = StringUtils.strip(data[1], "\"");
					} else {
						name = StringUtils.strip(data[1], "\"");
						desc = StringUtils.strip(data[0], "\"");
					}
					p = new Permission(name, category, desc);
				} catch (Exception e) {
					log.warn("error reading line " + data.toString(), e);
					continue;
				}
				permissions.add(p);
			}
			log.info("Saving permissions ....");
			permissionRepo.save(permissions);

			Profile _profile = new Profile();
			_profile.setDelFlag("N");
			_profile.setName("Default Profile");
			_profile.setEmail("test@yahoo.com");
			_profile.setPermissions(permissions);
			log.info("Creating default Profile ....");
			defaultProfile = profilerepository.save(_profile);

		} catch (Exception e) {
			log.warn("Can't initialize permissions file", e);
			log.warn("proceeding ....");
		}
	}

	@Transactional
	private void createDefaultAdmin() throws ApplicationException {

		User user = new User();
		user.setDelFlag("N");
		user.setFirstName("Sample");
		// user.set
		user.setLastName("Admin");
		user.setLoginId("su");
		user.setStatus("E");
		user.setProfile(defaultProfile);
		try {
			userService.add(user);
			userService.setUserPassword(user, "su");
		} catch (Exception e) {
			log.error("Can't create user", e);
			e.printStackTrace();
		}
	}

}
