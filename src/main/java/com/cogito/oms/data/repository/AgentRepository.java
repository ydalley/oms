package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Agent;


@Repository
public interface AgentRepository extends CommonRepository<Agent,Long> {
	
}
