package com.cn.thinkx.pms.service.product;

import java.util.List;
import java.util.Map;

import com.cn.thinkx.pms.model.product.TbProduct;
import com.cn.thinkx.pms.pageModel.base.PageFilter;
import com.cn.thinkx.pms.pageModel.product.Product;

public interface ProductService {

	List<Product> dataGrid(Product product, PageFilter ph);

	Long count(Product kv, PageFilter ph);

	void add(Product member);

	void delete(String id);

	void edit(Product p);

	Product get(String id);

	Product getProductByName(String productName);

	Product getProductByCardBin(String cardBin);
	
	List<Product> findProductList(Map<String, Object> params);

	TbProduct get(String hql, Map<String, Object> params);

}
