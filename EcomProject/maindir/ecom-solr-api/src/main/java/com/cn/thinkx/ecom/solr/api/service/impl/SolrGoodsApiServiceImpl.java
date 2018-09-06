package com.cn.thinkx.ecom.solr.api.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.basics.goods.domain.Goods;
import com.cn.thinkx.ecom.common.util.NumberUtils;
import com.cn.thinkx.ecom.common.util.StringUtil;
import com.cn.thinkx.ecom.redis.core.constants.RedisConstants;
import com.cn.thinkx.ecom.redis.core.utils.JedisClusterUtils;
import com.cn.thinkx.ecom.solr.api.model.GoodsQueryParam;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;
import com.cn.thinkx.ecom.solr.api.service.SolrGoodsApiService;
import com.cn.thinkx.ecom.solr.api.utils.SolrConstants;

@Service("solrGoodsApiService")
public class SolrGoodsApiServiceImpl implements SolrGoodsApiService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JedisClusterUtils jedisClusterUtils;

	@Override
	public void addGoodsBatch(List<Goods> list) throws IOException, SolrServerException {
		if (list != null && list.size() > 0) {
			List<SolrGoods> batchList = new ArrayList<SolrGoods>(list.size());
			SolrGoods sgs = null;
			for (Goods goods : list) {
				sgs = new SolrGoods();
				sgs.setId(goods.getGoodsId());
				sgs.setGoodsName(goods.getGoodsName());
				sgs.setSpuCode(goods.getSpuCode());
				sgs.setEcomCode(goods.getEcomCode());
				sgs.setEcomName(goods.getEcomName());
				sgs.setMarketEnable(goods.getMarketEnable());
				sgs.setPageTitle(goods.getPageTitle());
				sgs.setPicUrl(goods.getPicUrl());
				sgs.setGoodsPrice(Double.valueOf(NumberUtils.RMBCentToYuan(goods.getGoodsPrice())));
				sgs.setCatId1(goods.getParentCatId());
				sgs.setCatId2(goods.getCatId());
				sgs.setClickRate(0);
				sgs.setPayRate(goods.getPayRate());
				sgs.setKeyword(goods.getEcomName() + " " + goods.getGoodsName() + " " + goods.getPageTitle() + " "
						+ goods.getCatName());
				batchList.add(sgs);
			}
			HttpSolrClient solr = getSolrUrlAndCore();
			solr.addBeans(batchList);
			solr.commit();
			solr.close();
		}

	}

	@Override
	public void addGoodsIndex(Goods goods) throws IOException, SolrServerException {
		SolrGoods sgs = new SolrGoods();
		sgs.setId(goods.getGoodsId());
		sgs.setGoodsName(goods.getGoodsName());
		sgs.setSpuCode(goods.getSpuCode());
		sgs.setEcomCode(goods.getEcomCode());
		sgs.setEcomName(goods.getEcomName());
		sgs.setMarketEnable(goods.getMarketEnable());
		sgs.setPageTitle(goods.getPageTitle());
		sgs.setPicUrl(goods.getPicUrl());
		sgs.setGoodsPrice(Double.valueOf(NumberUtils.RMBCentToYuan(goods.getGoodsPrice())));
		sgs.setCatId1(goods.getParentCatId());
		sgs.setCatId2(goods.getCatId());
		sgs.setClickRate(0);
		sgs.setPayRate(0);
		sgs.setKeyword(
				sgs.getEcomName() + " " + sgs.getGoodsName() + " " + sgs.getPageTitle() + " " + sgs.getCatName());
		HttpSolrClient solr = getSolrUrlAndCore();
		solr.addBean(sgs);
		solr.commit();
		solr.close();

	}

	@Override
	public void deleteGoodsIndex(String id) throws IOException, SolrServerException {
		HttpSolrClient server = getSolrUrlAndCore();
		// 删除文档
		server.deleteById(id);
		// 提交修改
		server.commit();
		server.close();

	}

	@Override
	public List<SolrGoods> query(GoodsQueryParam params){
		try {
			HttpSolrClient solrServer = getSolrUrlAndCore();
			SolrQuery query = new SolrQuery();
			// 下面设置solr查询参数
			query.set("q", "*:*");// 参数q 查询所有
			// 参数df,给query设置默认搜索域
			query.set("df", "keyword");
			if (!StringUtil.isNullOrEmpty(params.getKeyword())) {
				// 给query增加布尔过滤条件
				query.addFilterQuery("keyword:" + params.getKeyword());
				query.setQuery("keyword:" + params.getKeyword());
			}
			// 如果商城代码有值，查询要关联查询（查询本渠道商城下的商品信息）
			if (!StringUtil.isNullOrEmpty(params.getEcomCode())) {
				query.setQuery("keyword:" + params.getKeyword() + "AND ecomCode:" + params.getEcomCode());
			}
			// solrQuery.setFacetSort("count asc"); //根据 count 数量 升序和降序 也可以根据索引
			// 如果是2安装价格排序（升序）
			if ("2".equals(params.getPsort())) {
				query.setSort("goodsPrice", SolrQuery.ORDER.asc);
			}else if("3".equals(params.getPsort())){	//购买量排序（降序）
				query.setSort("payRate", SolrQuery.ORDER.desc);
			}
			// /*query.setFacetLimit(101); */ // 设置返回结果条数 ,-1表示返回所有,默认值为100
			/* query.setParam(FacetParams.FACET_OFFSET, "100"); */ // 开始条数,偏移量,它与facet.limit配合使用可以达到分页的效果
			query.setFacetMinCount(1); // 设置返回的数据中每个分组的数据最小值，比如设置为1，则统计数量最小为1，不然不显示
										// //设置 限制 count的最小返回值，默认为0
			query.setFacetMissing(false);// 不统计null的值
			// 设置分页参数
			query.setStart((params.getStarts()-1)*params.getRows());
			query.setRows(params.getRows());// 每一页多少值
			// 获取查询结果
			QueryResponse response = solrServer.query(query);
			// 得到实体对象
			List<SolrGoods> tmpLists = response.getBeans(SolrGoods.class);
			logger.info("## 搜索得到的数据集tmpLists:[{}]", JSONObject.toJSON(tmpLists).toString());
			return tmpLists;

		} catch (Exception e) {
			logger.error("## 出错了", e);
		}
		return null;
	}

	public HttpSolrClient getSolrUrlAndCore() {
		String solrUrl = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,SolrConstants.ECOM_SOLR_URL);
		String solrCore = jedisClusterUtils.hget(RedisConstants.REDIS_HASH_TABLE_TB_BASE_DICT_KV,SolrConstants.ECOM_SOLR_CORE);
		HttpSolrClient solr = new HttpSolrClient(solrUrl + solrCore);
		return solr;
	}
	
//	public  HttpSolrClient getSolrClient(String url){
//		return new HttpSolrClient.Builder(url).build();
//	}
}
