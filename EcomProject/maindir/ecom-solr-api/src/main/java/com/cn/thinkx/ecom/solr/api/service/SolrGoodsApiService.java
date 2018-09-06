package com.cn.thinkx.ecom.solr.api.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;

public interface SolrGoodsApiService {

	/**
	 * 批量添加商品的
	 * @throws IOException
	 * @throws SolrServerException
	 */
    public void addGoodsBatch(List<Goods> list) throws IOException, SolrServerException;
    
    public void addGoodsIndex(Goods goods) throws IOException, SolrServerException;
    
    public void deleteGoodsIndex(String id) throws IOException, SolrServerException;
    
    public List<SolrGoods> query(GoodsQueryParam params)  throws Exception;
}
