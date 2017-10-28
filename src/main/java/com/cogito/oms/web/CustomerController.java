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

import com.cogito.oms.data.model.Customer;
import com.cogito.oms.service.CustomerService;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.ProfileService;
import com.cogito.oms.util.CustomBeanUtilsBean;


@Controller
@RequestMapping("/customers")
public class CustomerController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	CustomerService customerService;

	@GetMapping("{id}/view")
	String getCustomer(@PathVariable Long id, Model model) {
		Customer customer = customerService.getCustomer(id);
		model.addAttribute("customer", customer);
		return "admin/customers/detail";
	}

	@GetMapping("{id}/edit")
	String editCustomer(@PathVariable Long id, Model model) {
		Customer customer = customerService.getCustomer(id);
		model.addAttribute("customer", customer);
		return "admin/customers/edit";
	}

	@GetMapping("/new")
	String newCustomer(Model model) {
		Customer customer = new Customer();
		model.addAttribute("customer", customer);
		return "admin/customers/edit";
	}

	@GetMapping
	String getCustomers() {
		return "admin/customers/list";
	}

	@PostMapping
	String saveCustomer(@ModelAttribute("customer") @Valid Customer customer, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating customer{}", result.toString());
			return "admin/customers/edit";

		}

		try {
			if (customer.getId() != null) {
				Customer customer2 = customerService.getCustomer(customer.getId());
				CustomBeanUtilsBean.getInstance().copyProperties(customer2, customer);
				customer = customer2;
			}
			customerService.add(customer);
		} catch (ApplicationException | IllegalAccessException | InvocationTargetException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating customer{}", e);
			return "admin/customers/edit";
		}
		redirectAttributes.addFlashAttribute("message", "customer saved successfully");
		return "redirect:/customers";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<Customer> getCustomers(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
		Page<Customer> customers = customerService.getAllCustomers(pageable);
		if (StringUtils.isNoneBlank(search)) {
			customers = customerService.findCustomer(search,pageable);
		}else{
			customers = customerService.getAllCustomers(pageable);
		}
		DataTablesOutput<Customer> out = new DataTablesOutput<Customer>();
		out.setDraw(input.getDraw());
		out.setData(customers.getContent());
		out.setRecordsFiltered(customers.getTotalElements());
		out.setRecordsTotal(customers.getTotalElements());
		return out;
	}

}
