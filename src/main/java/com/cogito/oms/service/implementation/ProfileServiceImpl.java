package com.cogito.oms.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cogito.oms.data.model.Profile;
import com.cogito.oms.data.repository.ProfileRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.ProfileService;

import java.util.List;
import java.util.Locale;

@Service
public class ProfileServiceImpl implements ProfileService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProfileRepository profileRepo;

    @Autowired
    private MessageSource messageSource;

    private Locale locale = LocaleContextHolder.getLocale();

	public String createProfile(Profile profile) throws ApplicationException{
		String result = "" ;
		try{
			profileRepo.save(profile);
			result= messageSource.getMessage("profile.update.success", null, locale);
		}catch(Exception ex){
			result= messageSource.getMessage("profile.update.error", null, locale);
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	public String modifyProfile(Profile profile) throws ApplicationException{
		String result = "" ;
		try{
			profileRepo.save(profile);
			result= messageSource.getMessage("profile.update.success", null, locale);
		}catch(Exception ex){
			result= messageSource.getMessage("profile.update.error", null, locale);
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	@Override
	public String deleteProfile(Profile profile) throws ApplicationException{
		throw new ApplicationException("Unimplemented");
	}

	@Override
	public Profile getProfile(Long Id) {
		return profileRepo.findOne(Id);
		
	}

	@Override
	public List<Profile> fetchAllProfiles() {
		return profileRepo.findAll();
	}

	@Override
	public Page<Profile> fetchProfiles(Pageable page) {
		return profileRepo.findAll(page);
	}

	@Override
	public Page<Profile> findProfiles(String pattern, Pageable page) {
		return profileRepo.findByNameContaining(pattern, page);
	}


}
