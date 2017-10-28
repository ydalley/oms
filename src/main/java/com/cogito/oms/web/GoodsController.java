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

import com.cogito.oms.data.model.Goods;
import com.cogito.oms.service.ApplicationException;
import com.cogito.oms.service.GoodsService;
import com.cogito.oms.util.CustomBeanUtilsBean;


@Controller
@RequestMapping("/goods")
public class GoodsController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	GoodsService goodsService;

	@GetMapping("{id}/view")
	String getGoods(@PathVariable Long id, Model model) {
		Goods goods = goodsService.getGoods(id);
		model.addAttribute("goods", goods);
		return "admin/goods/detail";
	}

	@GetMapping("{id}/edit")
	String editGoods(@PathVariable Long id, Model model) {
		Goods goods = goodsService.getGoods(id);
		model.addAttribute("goods", goods);
		return "admin/goods/edit";
	}

	@GetMapping("/new")
	String newGoods(Model model) {
		Goods goods = new Goods();
		model.addAttribute("goods", goods);
		return "admin/goods/edit";
	}

	@GetMapping
	String getGoods() {
		return "admin/goods/list";
	}

	@PostMapping
	String saveGoods(@ModelAttribute("goods") @Valid Goods goods, BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			logger.warn("Error occurred creating goods{}", result.toString());
			return "admin/goods/edit";

		}
		try {
			if (goods.getId() != null) {
				Goods goods2 = goodsService.getGoods(goods.getId());
				CustomBeanUtilsBean.getInstance().copyProperties(goods2, goods);
				goods = goods2;
			}
			goodsService.addGoods(goods);
		} catch (ApplicationException | IllegalAccessException | InvocationTargetException e) {
			result.reject(e.getMessage());
			logger.error("Error occurred creating goods{}", e);
			return "admin/users/edit";
		}
		redirectAttributes.addFlashAttribute("message", "goods save successfully");
		return "redirect:/goods";
	}

	@GetMapping("/all")
	public @ResponseBody DataTablesOutput<Goods> getUsers(DataTablesInput input, @RequestParam("csearch") String search) {
		Pageable pageable = DataTablesUtils.getPageable(input);
		Page<Goods> goods = null;
		if (StringUtils.isNoneBlank(search)) {
			goods = goodsService.findGoods(search,pageable);
		}else{
			goods = goodsService.getGoods(pageable);
		}
		DataTablesOutput<Goods> out = new DataTablesOutput<>();
		out.setDraw(input.getDraw());
		out.setData(goods.getContent());
		out.setRecordsFiltered(goods.getTotalElements());
		out.setRecordsTotal(goods.getTotalElements());
		return out;
	}

}
