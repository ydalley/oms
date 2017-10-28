package com.cogito.oms.service.implementation;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.shiro.authc.credential.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.cogito.oms.data.model.Agent;
import com.cogito.oms.data.model.User;
import com.cogito.oms.data.repository.AgentRepository;
import com.cogito.oms.service.AgentService;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.util.Messages;

@Service
public class AgentServiceImpl implements AgentService {

	private static final Logger log = LogManager.getLogger(AgentServiceImpl.class);

	@Autowired
	private AgentRepository agentRepo;
	@Autowired
	Messages messages;
	@Autowired
	JavaMailSender emailSender;
	@Autowired
	private PasswordService passwordService;

	@Override

	public Agent getAgent(long id) {
		return agentRepo.getOne(id);
	}

	@Override
	public Page<Agent> getAllAgents(Pageable pageDetails) {
		return agentRepo.findAll(pageDetails);
	}

	@Override
	public Page<Agent> findAgent(String pattern, Pageable pageDetails) {
		return agentRepo.findUsingPattern(pattern, pageDetails);
	}

	@Override
	public Agent getAgentForLoginByName(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(Agent agent) throws ApplicationException {
		String result = "";
		try {
			agentRepo.save(agent);
			result = messages.get("agent.create.success");
		} catch (Exception ex) {
			result = messages.get("agent.create.error");
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	@Override
	public String add(Agent agent) throws ApplicationException {
		String result = "";
		try {
			agentRepo.save(agent);
			result = messages.get("agent.create.success");
		} catch (Exception ex) {
			result = messages.get("agent.create.error");
			log.error(result, ex);
			throw new ApplicationException(result);
		}
		return result;
	}

	@Override
	public String disable(Agent agent) throws ApplicationException {
		agent.setStatus("D");
		agentRepo.save(agent);
		return "";
	}

	@Override
	public String enable(Agent agent) throws ApplicationException {
		agent.setStatus("E");
		agentRepo.save(agent);
		return "";
	}

	@Override
	public String toggleStatus(Agent agent) throws ApplicationException {
		String status = agent.getStatus();
		String response = "";
		if (StringUtils.isEmpty(status)) {
			status = "D";
			response = "Agent is now disabled";
		} else if (status.equals("E")) {
			status = "D";
			response = "Agent is now disabled";
		} else if (status.equals("D")) {
			status = "E";
			response = "Agent is now enabled";
		}
		agent.setStatus(status);
		Object o = null;
		try {
			o = agentRepo.save(agent);
		} catch (Exception e) {
			o = e.getMessage();
		}
		if (o instanceof String)
			return (String) o;

		return response;
	}

	@Override
	public void setAgentPassword(Agent agent, String oldPassword, String newPassword) throws ApplicationException {

		if (validatePassword(agent, oldPassword, newPassword)) {
			String passwd = passwordService.encryptPassword(newPassword);
			Agent temp = agentRepo.findOne(agent.getId());
			temp.setPassword(passwd);
			agentRepo.save(temp);
		} else {
			throw new ApplicationException("Old password is not correct");
		}
	}

	@Override
	public void setAgentPassword(Agent agent, String newPassword) throws ApplicationException {
		agent.setPassword(passwordService.encryptPassword(newPassword));
		agentRepo.save(agent);

	}

	@Override
	public void generateAgentPassword(Agent agent) throws ApplicationException {
		String randomPassword = generateRandomPassword();
		log.debug("setting agent password for " + agent + " to [" + randomPassword + "]");
		setAgentPassword(agent, randomPassword);
		sendPasswordEmail(agent, randomPassword);
	}

	@Async
	private void sendPasswordEmail(Agent agent, String password) throws ApplicationException {
		MimeMessage message = emailSender.createMimeMessage();

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(agent.getEmail());
			helper.setText(messages.get("email.message", agent.getLoginId(), password), true);
			helper.setSubject(messages.get("email.subject"));
			emailSender.send(message);
		} catch (NoSuchMessageException | MessagingException e1) {
			new ApplicationException("Error sending email, try again later");
		}
		
	}
	
	private String generateRandomPassword() {
		String alphanumeric = RandomStringUtils.randomAlphanumeric(8);
		return alphanumeric;
	}

	private boolean validatePassword(Agent agent, String oldPassword, String newPassword) {
		Agent tempagent = agentRepo.findOne(agent.getId());
		if (passwordService.passwordsMatch(oldPassword, tempagent.getPassword())) {
			return true;
		}
		return false;
	}

}
