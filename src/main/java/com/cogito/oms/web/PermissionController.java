package com.cogito.oms.web;

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
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.PermissionService;


@Controller
@RequestMapping("/permissions")
public class PermissionController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	PermissionService permissionservice;

	@GetMapping("{id}/view")
	String getPermissions(@PathVariable Long id, Model model) {
		Permission option = permissionservice.getPermission(id);
		model.addAttribute("permission", option);
		return "admin/permissions/detail";
	}

	@GetMapping("{id}/edit")
	String editPermissions(@PathVariable Long id, Model model) {
		Permission option = permissionservice.getPermission(id);
		model.addAttribute("permission", option);
		return "admin/permissions/edit";
	}

	@GetMapping("/new")
	String newPermission(Model model) {
		Permission option = new Permission();
		model.addAttribute("permission", option);
		return "admin/permissions/edit";
	}

	@GetMapping
	String getPermissions() {
		return "admin/permissions/list";
	}

	@PostMapping
	String savePermission(@ModelAttribute("permission") @Valid Permission Permission, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating permission{}", result.toString());
			return "admin/permissions/edit";
		}

		try {
			permissionservice.createPermission(Permission);
		} catch (ApplicationException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating permission{}", e);
			return "admin/permissions/edit";
		}
		redirectAttributes.addFlashAttribute("message", "permission save successfully");
		return "redirect:/permissions";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<Permission> getPermissions(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
        Page<Permission> permissions = null;
        if (StringUtils.isNoneBlank(search)) {
        	permissions = permissionservice.findPermission(search, pageable);
		}else{
			permissions = permissionservice.getPermissions(pageable);
		}
       
		DataTablesOutput<Permission> out = new DataTablesOutput<Permission>();
		out.setDraw(input.getDraw());
		out.setData(permissions.getContent());
		out.setRecordsFiltered(permissions.getTotalElements());
		out.setRecordsTotal(permissions.getTotalElements());
		return out;
	}

	

	

}
