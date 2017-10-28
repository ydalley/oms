package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Location;



@Repository
public interface LocationRepository extends CommonRepository<Location,Long> {
	
}
