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

import com.cogito.oms.data.model.Agent;
import com.cogito.oms.service.AgentService;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.ProfileService;
import com.cogito.oms.util.CustomBeanUtilsBean;


@Controller
@RequestMapping("/agents")
public class AgentController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	AgentService agentService;

	@GetMapping("{id}/view")
	String getAgent(@PathVariable Long id, Model model) {
		Agent agent = agentService.getAgent(id);
		model.addAttribute("agent", agent);
		return "admin/agents/detail";
	}

	@GetMapping("{id}/edit")
	String editAgent(@PathVariable Long id, Model model) {
		Agent agent = agentService.getAgent(id);
		model.addAttribute("agent", agent);
		return "admin/agents/edit";
	}

	@GetMapping("/new")
	String newAgent(Model model) {
		Agent agent = new Agent();
		model.addAttribute("agent", agent);
		return "admin/agents/edit";
	}

	@GetMapping
	String getAgents() {
		return "admin/agents/list";
	}

	@PostMapping
	String saveAgent(@ModelAttribute("agent") @Valid Agent agent, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating agent{}", result.toString());
			return "admin/agents/edit";

		}

		try {
			if (agent.getId() != null) {
				Agent agent2 = agentService.getAgent(agent.getId());
				CustomBeanUtilsBean.getInstance().copyProperties(agent2, agent);
				agent = agent2;
			}
			agentService.add(agent);
		} catch (ApplicationException | IllegalAccessException | InvocationTargetException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating agent{}", e);
			return "admin/agents/edit";
		}
		redirectAttributes.addFlashAttribute("message", "agent saved successfully");
		return "redirect:/agents";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<Agent> getAgents(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
		Page<Agent> agents = agentService.getAllAgents(pageable);
		if (StringUtils.isNoneBlank(search)) {
			agents = agentService.findAgent(search,pageable);
		}else{
			agents = agentService.getAllAgents(pageable);
		}
		DataTablesOutput<Agent> out = new DataTablesOutput<Agent>();
		out.setDraw(input.getDraw());
		out.setData(agents.getContent());
		out.setRecordsFiltered(agents.getTotalElements());
		out.setRecordsTotal(agents.getTotalElements());
		return out;
	}

	@GetMapping("/{id}/lock")
	public String changeLockStatus(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		Agent agent = agentService.getAgent(id);
		String response = "";
		try {
			response = agentService.toggleStatus(agent);
		} catch (ApplicationException e) {
			redirectAttributes.addAttribute("failure", e.getMessage());
			logger.error("Error occurred creating agent{}", e);
		}
		redirectAttributes.addFlashAttribute("message", response);
		return "redirect:/agents";
	}

	@GetMapping("/{id}/password")
	public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			Agent agent = agentService.getAgent(id);
			agentService.generateAgentPassword(agent);
			redirectAttributes.addFlashAttribute("message", "password set sucessfully");
		} catch (ApplicationException pe) {
			redirectAttributes.addAttribute("failure", pe.getMessage());
			logger.error("Error resetting password for admin agent", pe);
		}
		return "redirect:/agents";
	}


}
