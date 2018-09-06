package com.cn.thinkx.ecom.solr.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.params.FacetParams;

import com.alibaba.fastjson.JSONObject;
import com.cn.thinkx.ecom.solr.api.model.SolrGoods;

public class SolrApi {
	//指定solr服务器的地址    
	    private final static String SOLR_URL = "http://192.168.1.202:18087/solr/";    
	  
	    private String solrCore="core1";//指定的存储数据的collection
	    
	    /** 
	     * 创建SolrServer对象 
	     *  
	     * 该对象有两个可以使用，都是线程安全的   
	     * 1、CommonsHttpSolrServer：启动web服务器使用的，通过http请求的  
	     * 2、 EmbeddedSolrServer：内嵌式的，导入solr的jar包就可以使用了   
	     * 3、solr 4.0之后好像添加了不少东西，其中CommonsHttpSolrServer这个类改名为HttpSolrClient 
	     *  
	     * @return 
	     */  
	    public HttpSolrClient createSolrServer(){  
	        HttpSolrClient solr = null;  
//	        solr = new HttpSolrClient(SOLR_URL);  
	        solr.setConnectionTimeout(100);    
	        solr.setDefaultMaxConnectionsPerHost(100);    
	        solr.setMaxTotalConnections(100);   
	        return solr;  
	    }  
	  
	      
	      
	    public void addPersonBatch() throws IOException, SolrServerException{  
	         List<Person> list = new ArrayList<Person>();  
	         list.add(new Person("1","zhangsanfeng","我是张三丰"));  
	         list.add(new Person("2","zhangsanfeng123","我是张三丰123"));  
	         list.add(new Person("3","zhangsan123","我是张三"));  
	         list.add(new Person("4","zhangsan123456","我是张三"));  
	         list.add(new Person("5","lisi","李四"));  
	         list.add(new Person("6","lisi5","我是lisi"));  
	         list.add(new Person("7","wangwu","王五"));  
	         list.add(new Person("8","wangwu","王五002"));  
	         HttpSolrClient solr = new HttpSolrClient(SOLR_URL + solrCore);  
	         solr.addBeans(list);  
	         solr.commit();  
	         solr.close();  
	    }  
	      
	      
	      
	    public void addPersonIndex() throws IOException, SolrServerException{  
	        Person p = new Person();  
	        p.setId("9");  
	        p.setName("zhangsan");  
	        p.setDescription("张三");  
	         HttpSolrClient solr = new HttpSolrClient(SOLR_URL + solrCore);  
	         solr.addBean(p);     
	         solr.commit();  
	         solr.close();  
	    }  
	      
	  
	    /** 
	     * 往索引库添加文档 
	    * @throws IOException  
	    * @throws SolrServerException  
	     */  
	    public void addDoc() throws SolrServerException, IOException{  
	       //构造一篇文档    
	        SolrInputDocument document = new SolrInputDocument();    
	        //往doc中添加字段,在客户端这边添加的字段必须在服务端中有过定义    
	        document.addField("id", "13");    
	        document.addField("name", "华为移动手机");    
	        document.addField("description", "世界上最牛逼的华为移动手机");    
	        //获得一个solr服务端的请求，去提交  ,选择具体的某一个solr core  
	       HttpSolrClient solr = new HttpSolrClient(SOLR_URL + solrCore);  
	       solr.add(document);    
	       solr.commit();  
	       solr.close();  
	    }  
	  
	  
	    /**  
	     * 根据id从索引库删除文档  
	     */  
	    public void deleteDocumentById() throws Exception {    
	        //选择具体的某一个solr core  
	        HttpSolrClient server = new HttpSolrClient(SOLR_URL+solrCore);    
	        //删除文档    
//	        server.deleteById("100042");    
	        //删除所有的索引  
	        server.deleteByQuery("*:*");  
	        //提交修改    
	        server.commit();    
	        server.close();  
	    }    
	  
	    /** 
	     * 查询 
	    * @throws Exception  
	     */  
	    public void querySolr() {  
	        HttpSolrClient solrServer = new HttpSolrClient(SOLR_URL+solrCore);    
	        SolrQuery query = new SolrQuery();    
	        //下面设置solr查询参数  
	        query.set("q", "*:*");// 参数q  查询所有     
//	        query.set("q","*张三*");//相关查询，比如某条数据某个字段含有周、星、驰三个字  将会查询出来 ，这个作用适用于  
	        //参数fq, 给query增加过滤查询条件    
//	        query.addFilterQuery("name:华为移动手机");//id为0-9    
//	        query.addFilterQuery("id:13");//id为0-9   
//			query.setQuery(queryString);
	        //给query增加布尔过滤条件    
	        //query.addFilterQuery("description:演员");  //description字段中含有“演员”两字的数据  
	  
	        //参数df,给query设置默认搜索域    
	        query.set("df", "keyword");
	          
	        query.setQuery("keyword: 网易");  
	//        query.setQuery("name:*zhangsan* OR name:*123*" );//name 包含zhangsan或者123  
	//        query.setQuery("name:*zhangsan* AND description:*zhangsan*" );// name包含且  
	        /* 
	         * FacetComponet有两种排序选择，分别是count和index， 
	         * count是按每个词出现的次数，index是按词的字典顺序。如果查询参数不指定facet.sort，solr默认是按count排序。 
	         */// 参数q  查询所有
	       
	        query.setFacetSort(FacetParams.FACET_SORT_COUNT);  
	        query.setSort("goodsPrice",  SolrQuery.ORDER.asc);
	        query.setFacetLimit(100);  // 设置返回结果条数 ,-1表示返回所有,默认值为100  
	        /* query.setParam(FacetParams.FACET_OFFSET, "100");*/   //开始条数,偏移量,它与facet.limit配合使用可以达到分页的效果  
	        query.setFacetMissing(false);//不统计null的值  
	  
	          

	        //设置分页参数    
	        query.setStart(0);    
	        query.setRows(40);//每一页多少值    
	        QueryResponse response =null;
	        try{
	        //获取查询结果  
	         response = solrServer.query(query);    

			  }catch(Exception ex){
				  ex.printStackTrace();
			  }
	        //查询得到文档的集合    
	        SolrDocumentList solrDocumentList = response.getResults();    
	        System.out.println("通过文档集合获取查询文档数量："+solrDocumentList.getNumFound());   

	  
	        //得到实体对象  
	        List<SolrGoods> tmpLists = response.getBeans(SolrGoods.class);  
	        if(tmpLists!=null && tmpLists.size()>0){  
	            System.out.println("实体对象赋值内容：");   
	            for(SolrGoods per:tmpLists){  
	                System.out.println(JSONObject.toJSON(per).toString());  
	            }  
	        }  
	    }  
	  
	    public static void main(String[] args) throws Exception {  
	        SolrApi solr = new SolrApi();  
//	        solr.createSolrServer();  
//	        solr.addDoc();  
//	        solr.addPersonIndex();  
//	        solr.addPersonBatch();  
//	        solr.deleteDocumentById();  
	        solr.querySolr();
	   }  
	  

}
