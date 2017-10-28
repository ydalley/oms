package com.cogito.oms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Profile;

import java.util.List;

public interface ProfileService {

    public String createProfile(Profile profile) throws  ApplicationException;

    public String modifyProfile(Profile profile)throws  ApplicationException;


    public String deleteProfile(Profile profile)throws  ApplicationException;

    public Page<Profile> findProfiles(String pattern,Pageable page);

    public Profile getProfile(Long Id);

    public List<Profile> fetchAllProfiles();

    public Page<Profile> fetchProfiles(Pageable page);

}
