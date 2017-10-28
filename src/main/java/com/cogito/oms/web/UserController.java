package com.cogito.oms.web;

import java.lang.reflect.InvocationTargetException;

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

import com.cogito.oms.data.model.Profile;
import com.cogito.oms.data.model.User;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.ProfileService;
import com.cogito.oms.service.UserService;
import com.cogito.oms.util.CustomBeanUtilsBean;


@Controller
@RequestMapping("/users")
public class UserController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	UserService userService;
	@Autowired
	ProfileService profileservice;

	@GetMapping("{id}/view")
	String getUser(@PathVariable Long id, Model model) {
		User user = userService.getUser(id);
		model.addAttribute("user", user);
		return "admin/users/detail";
	}

	@GetMapping("{id}/edit")
	String editUser(@PathVariable Long id, Model model) {
		User user = userService.getUser(id);
		model.addAttribute("user", user);
		return "admin/users/edit";
	}

	@GetMapping("/new")
	String newUser(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "admin/users/edit";
	}

	@GetMapping
	String getUsers() {
		return "admin/users/list";
	}

	@PostMapping
	String saveUser(@ModelAttribute("user") @Valid User user, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating user{}", result.toString());
			return "admin/users/edit";

		}

		try {
			if (user.getId() != null) {
				User user2 = userService.getUser(user.getId());
				CustomBeanUtilsBean.getInstance().copyProperties(user2, user);
				user = user2;
			}
			userService.add(user);
		} catch (ApplicationException | IllegalAccessException | InvocationTargetException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating user{}", e);
			return "admin/users/edit";
		}
		redirectAttributes.addFlashAttribute("message", "user save successfully");
		return "redirect:/users";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<User> getUsers(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
		Page<User> users = null;
		if (StringUtils.isNoneBlank(search)) {
			users = userService.findUser(search,pageable);
		}else{
			users = userService.getAllUsers(pageable);
		}
		DataTablesOutput<User> out = new DataTablesOutput<User>();
		out.setDraw(input.getDraw());
		out.setData(users.getContent());
		out.setRecordsFiltered(users.getTotalElements());
		out.setRecordsTotal(users.getTotalElements());
		return out;
	}

	@GetMapping("/{id}/lock")
	public String changeLockStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		User user = userService.getUser(id);
		String response = "";
		try {
			response = userService.toggleStatus(user);
		} catch (ApplicationException e) {
			redirectAttributes.addAttribute("failure", e.getMessage());
			logger.error("Error occurred creating user{}", e);
		}
		redirectAttributes.addFlashAttribute("message", response);
		return "redirect:/users";
	}

	@GetMapping("/{id}/password")
	public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			User user = userService.getUser(id);
			userService.genearteUserPassword(user);
			redirectAttributes.addFlashAttribute("message", "password set sucessfully");
		} catch (ApplicationException pe) {
			redirectAttributes.addAttribute("failure", pe.getMessage());
			logger.error("Error resetting password for admin user", pe);
		}
		return "redirect:/users";
	}

	@ModelAttribute(name = "profiles")
	public Iterable<Profile> init(Model model) {
		return profileservice.fetchAllProfiles();
	}

}
