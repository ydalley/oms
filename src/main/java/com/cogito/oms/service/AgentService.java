package com.cogito.oms.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Agent;



public interface AgentService {
	Agent getAgent(long id);
	
	Page<Agent> getAllAgents(Pageable pageDetails);
	Page<Agent> findAgent(String pattern,Pageable pageDetails);
	Agent getAgentForLoginByName(String userId);
	String modify(Agent agent) throws ApplicationException;
	String add(Agent agent) throws ApplicationException;
	String enable(Agent agent) throws ApplicationException;
	String toggleStatus(Agent agent) throws ApplicationException;
	String disable(Agent agent) throws ApplicationException;
	void setAgentPassword(Agent agent, String oldPassword, String newPassword) throws ApplicationException;
	void setAgentPassword(Agent agent, String newPassword) throws ApplicationException;
	void generateAgentPassword(Agent agent)
			throws ApplicationException;
}
