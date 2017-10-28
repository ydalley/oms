package com.cogito.oms.data.repository;


import org.springframework.stereotype.Repository;

import com.cogito.oms.data.model.Goods;


@Repository
public interface GoodsRepository extends CommonRepository<Goods,Long> {
	
}
