package com.cogito.oms.service.implementation;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cogito.oms.data.model.Goods;
import com.cogito.oms.data.repository.GoodsRepository;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.GoodsService;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	private GoodsRepository goodsRepo;

	@Autowired
	public GoodsServiceImpl(GoodsRepository goodsRepo) {
		this.goodsRepo = goodsRepo;
	}

	
	@Override
	public String addGoods(Goods goods) throws ApplicationException {
		goodsRepo.save(goods);
		return "goods.added";
	}

	@Override
	public String updateGoods(Goods goods) throws ApplicationException {
		goodsRepo.save(goods);
		return "goods.updates";
	}

	@Override
	public String deleteGoods(Goods goods) throws ApplicationException {
		goodsRepo.delete(goods);
		return "goods.deleted";
	}

	@Override
	public Goods getGoods(Long Id) {
		return goodsRepo.getOne(Id);
	}

	@Override
	public List<Goods> getAllGoods() {
		return goodsRepo.findAll();
	}

	@Override
	public Page<Goods> getGoods(Pageable page) {
		return goodsRepo.findAll(page);
	}


	@Override
	public Page<Goods> findGoods(String pattern, Pageable pageDetails) {
		return goodsRepo.findUsingPattern(pattern,pageDetails);
	}

}
