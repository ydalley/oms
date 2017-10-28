package com.cogito.oms.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.data.jpa.datatables.repository.DataTablesUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cogito.oms.data.model.Permission;
import com.cogito.oms.data.model.Profile;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.PermissionService;
import com.cogito.oms.service.ProfileService;


@Controller
@RequestMapping("/profiles")
public class ProfileController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	ProfileService profileservice;
	
	@Autowired
	PermissionService permissionservice;

	@GetMapping("{id}/view")
	String getProfile(@PathVariable Long id, Model model) {
		Profile profile = profileservice.getProfile(id);
		model.addAttribute("profile", profile);
		return "admin/profiles/detail";
	}

	@GetMapping("{id}/edit")
	String editProfile(@PathVariable Long id, Model model) {
		Profile profile = profileservice.getProfile(id);
		Iterable<Permission> allOtherOptions = permissionservice.getAllPermissionsNotInProfile(profile);
		model.addAttribute("profile", profile);
		model.addAttribute("permissions", allOtherOptions);
		model.addAttribute("categoryMap", prepareCatMap());
		return "admin/profiles/edit";
	}

	@GetMapping("/new")
	String newProfile(Model model) {
		Profile profile = new Profile();
		Iterable<Permission> allOptions = permissionservice.getAllPermissions();
		model.addAttribute("profile", profile);
		model.addAttribute("permissions", allOptions);
		model.addAttribute("categoryMap", prepareCatMap());
		return "admin/profiles/edit";
	}

	@GetMapping
	String getProfiles() {
		return "admin/profiles/list";
	}

	@PostMapping
	String saveProfile(@ModelAttribute("profile") @Valid Profile profile, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating profile{}", result.toString());
			return "admin/profiles/edit";
		}

		try {
			profileservice.createProfile(profile);
		} catch (ApplicationException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating profile{}", e);
			return "admin/profiles/edit";
		}
		redirectAttributes.addFlashAttribute("message", "profile saved successfully");
		return "redirect:/profiles";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<Profile> getProfiles(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
		Page<Profile> profiles = null;
		if (StringUtils.isNoneBlank(search)) {
			profiles = profileservice.findProfiles(search,pageable);
		}else{
			profiles = profileservice.fetchProfiles(pageable);
		}
       
		DataTablesOutput<Profile> out = new DataTablesOutput<Profile>();
		out.setDraw(input.getDraw());
		out.setData(profiles.getContent());
		out.setRecordsFiltered(profiles.getTotalElements());
		out.setRecordsTotal(profiles.getTotalElements());
		return out;
	}

	private Map<String, List<Permission>> prepareCatMap(){
		Iterable<Permission> options = permissionservice.getAllPermissions();
		Map<String, List<Permission>> map = new HashMap<String, List<Permission>>();
		for (Permission option : options) {
			if(map.containsKey(option.getCategory())){
				map.get(option.getCategory()).add(option);
			}else{
				List<Permission> l = new ArrayList<Permission>();
				l.add(option);
				map.put(option.getCategory(),l );
			}
		}
		return map;
	}
}
