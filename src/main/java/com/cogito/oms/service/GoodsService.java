package com.cogito.oms.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cogito.oms.data.model.Goods;

public interface GoodsService {

	public String addGoods(Goods Goods) throws ApplicationException;

	public String updateGoods(Goods Goods) throws ApplicationException;

	public String deleteGoods(Goods Goods) throws ApplicationException;

	public Goods getGoods(Long Id);

	public List<Goods> getAllGoods();

	public Page<Goods> getGoods(Pageable page);
	
	Page<Goods> findGoods(String pattern,Pageable pageDetails);

}
