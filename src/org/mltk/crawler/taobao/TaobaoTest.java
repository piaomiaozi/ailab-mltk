package org.mltk.crawler.taobao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.User;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemsOnsaleGetRequest;
import com.taobao.api.request.LogisticsAddressSearchRequest;
import com.taobao.api.request.LogisticsCompaniesGetRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.ShopUpdateRequest;
import com.taobao.api.request.TraderatesGetRequest;
import com.taobao.api.request.UserSellerGetRequest;
import com.taobao.api.request.WangwangEserviceChatpeersGetRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemsOnsaleGetResponse;
import com.taobao.api.response.LogisticsAddressSearchResponse;
import com.taobao.api.response.LogisticsCompaniesGetResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.ShopUpdateResponse;
import com.taobao.api.response.TraderatesGetResponse;
import com.taobao.api.response.UserSellerGetResponse;
import com.taobao.api.response.WangwangEserviceChatpeersGetResponse;

/**
 * 
 * @author superhy
 *
 */
@Deprecated
public class TaobaoTest {

	public static final String url = "http://gw.api.taobao.com/router/rest";

	// ����Ӧ��ʱ��TOP�䷢��Ψһ��ʶ��TOPͨ��App Key������Ӧ�õ���ݡ����ýӿ�ʱ���봫��Ĳ�����
	public static final String appkey = "23039919";

	/**
	 * SessionKey�򵥵�˵���Ǵ������ҵĵ�¼session
	 * SessionKey���û���ݵı�ʶ��Ӧ�û�ȡ����SessionKey����ζ��Ӧ��ȡ�����û�����Ȩ���������û���TOP�����û���
	 */
	public static final String sessionKey = "61010178e22b3e132cace531d1f4b378be60dfa93c64cb8402843776";

	// App Secret��TOP��Ӧ�÷������Կ����������Ҫ���Ʊ��������Կ�������Կ������֤Ӧ����Դ�Ŀɿ��ԣ���ֹ��α�졣
	public static final String secret = "20c26ac68af1a7dfe0f8dea79ed448e0";

	/**
	 * ��ѯ�����û���Ϣ
	 * 
	 * @throws ApiException
	 */

	public void test1() throws ApiException {

		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		UserSellerGetRequest req = new UserSellerGetRequest();
		req.setFields("location,user_id,nick,sex");
		UserSellerGetResponse response = client.execute(req, sessionKey);

		System.out.println(response.getBody());
		System.out.println(response.getMsg());
		User user = response.getUser();
		System.out.println(user);
	}

	/**
	 * ��ȡ��Ʒ�б�
	 * 
	 * @throws ApiException
	 */

	/*
	 * public void test2() throws ApiException { TaobaoClient client = new
	 * DefaultTaobaoClient(url, appkey, secret); ProductsGetRequest req = new
	 * ProductsGetRequest(); req.setFields("product_id,tsc,cat_name,name");
	 * req.setNick("dyjhxzzq"); req.setPageNo(1L); req.setPageSize(5L);
	 * ProductsGetResponse response = client.execute(req);
	 * System.out.println(response.getBody()); }
	 */

	/**
	 * ��ȡ���ҵ��̵Ļ�����Ϣ cid:14
	 * 
	 * @throws ApiException
	 */

	public void test3() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		ShopGetRequest req = new ShopGetRequest();
		req.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		req.setNick("dyjhxzzq");
		ShopGetResponse response = client.execute(req);
		System.out.println(response.getBody());
	}

	/**
	 * ���µ��̻�����Ϣ
	 * 
	 * @throws ApiException
	 */

	public void test4() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		ShopUpdateRequest req = new ShopUpdateRequest();
		req.setTitle("Ůװ");
		req.setBulletin("��żۿ�");
		req.setDesc("<p>��ӭ���˿�ǰ������ѡ�����ҵ꽫�߳�Ϊ������!</p>");
		ShopUpdateResponse response = client.execute(req, sessionKey);
		System.out.println(response.getBody());
	}

	/**
	 * ����������Ϣ
	 * 
	 * @throws ParseException
	 * @throws ApiException
	 */

	public void test5() throws ParseException, ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		TraderatesGetRequest req = new TraderatesGetRequest();
		req.setFields("tid,oid,role,nick,result,created,rated_nick,item_title,item_price,content,reply,num_iid");
		// req.setRateType("get");
		// req.setRole("buyer");
		req.setRateType("give");
		req.setRole("seller");
		req.setResult("good");
		req.setPageNo(1L);
		req.setPageSize(10L);
		Date dateTime = SimpleDateFormat.getDateTimeInstance().parse(
				"2014-01-01 00:00:00");
		req.setStartDate(dateTime);
		dateTime = SimpleDateFormat.getDateTimeInstance().parse(
				"2014-03-30 00:00:00");
		req.setEndDate(dateTime);
		// req.setTid(123456L);
		req.setUseHasNext(true);
		// req.setNumIid(1234L);
		TraderatesGetResponse response = client.execute(req, sessionKey);
		System.out.println(response.getBody());
	}

	/**
	 * �첽��ȡ���������������Ľ�������(֧�ֳ�������)
	 * 
	 * @throws ApiException
	 */

	/*
	 * public void test6() throws ApiException { TaobaoClient client = new
	 * DefaultTaobaoClient(url, appkey, secret); TopatsTradesSoldGetRequest req
	 * = new TopatsTradesSoldGetRequest();
	 * req.setFields("tid,seller_nick,buyer_nick,payment");
	 * req.setStartTime("20140101"); req.setEndTime("20140330");
	 * req.setIsAcookie(true); TopatsTradesSoldGetResponse response =
	 * client.execute(req, sessionKey); System.out.println(response.getBody());
	 * }
	 */

	/**
	 * ��ȡ��ǰ�Ự�û������е���Ʒ�б�
	 * 
	 * @throws Exception
	 */

	public void test7() throws Exception {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		ItemsOnsaleGetRequest req = new ItemsOnsaleGetRequest();
		req.setFields("num_iid,title,price");
		// req.setQ("N97");
		// req.setCid(14L);
		// req.setSellerCids("14");
		// req.setHasDiscount(true);
		// req.setHasShowcase(true);
		// req.setOrderBy("list_time:desc");
		// req.setIsTaobao(true);
		// req.setIsEx(true);
		req.setPageNo(1L);
		req.setPageSize(100L);
		// Date dateTime =
		// SimpleDateFormat.getDateTimeInstance().parse("2000-01-01 00:00:00");
		// req.setStartModified(dateTime);
		// dateTime =
		// SimpleDateFormat.getDateTimeInstance().parse("2000-01-01 00:00:00");
		// req.setEndModified(dateTime);
		ItemsOnsaleGetResponse response = client.execute(req, sessionKey);
		System.out.println(response.getBody());
	}

	/**
	 * �õ�������Ʒ��Ϣ
	 * 
	 * @throws ApiException
	 */

	public void test8() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		ItemGetRequest req = new ItemGetRequest();
		req.setFields("num_iid,title,price,desc_modules,sell_point,desc,product_prop_imgs,pic_url");
		req.setNumIid(37018609764L);
		// req.setTrackIid("123_track_456");
		ItemGetResponse response = client.execute(req, sessionKey);
		System.out.println(response.getBody());
		Item item = response.getItem();
		System.out.println(item.getTitle());
		System.out.println(item.getDesc());
		System.out.println(item.getPicUrl());
		System.out.println(item.getPropImgs());
	}

	public void test9() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		WangwangEserviceChatpeersGetRequest req = new WangwangEserviceChatpeersGetRequest();
		req.setChatId("abcd");
		req.setStartDate("2014-01-01");
		req.setEndDate("2014-03-24");
		req.setCharset("utf-8");
		WangwangEserviceChatpeersGetResponse response = client.execute(req,
				sessionKey);
		System.out.println(response.getBody());
	}

	/**
	 * ��ѯ���ҵ�ַ��
	 * 
	 * @throws Exception
	 */

	public void test10() throws Exception {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		LogisticsAddressSearchRequest req = new LogisticsAddressSearchRequest();
		// req.setRdef("no_def");
		LogisticsAddressSearchResponse response = client.execute(req,
				sessionKey);
		System.out.println(response.getBody());
	}

	/**
	 * ��ѯ������˾��Ϣ
	 * 
	 * @throws ApiException
	 */

	public void test11() throws ApiException {
		TaobaoClient client = new DefaultTaobaoClient(url, appkey, secret);
		LogisticsCompaniesGetRequest req = new LogisticsCompaniesGetRequest();
		req.setFields("id,code,name,reg_mail_no");
		req.setIsRecommended(true);
		req.setOrderMode("offline");
		LogisticsCompaniesGetResponse response = client.execute(req);
		System.out.println(response.getBody());
	}

	/**
	 * ���ýӿ���Ϣ
	 * 
	 * @param args
	 * @author: Jerri Liu
	 * @date: 2014��3��17������8:44:32
	 */
	public static void main(String[] args) {
		TaobaoTest app3 = new TaobaoTest();
		try {
			app3.test1();
			// app3.test2();
			// app3.test3();
			// app3.test4();
			// app3.test5();
			// app3.test6();
			// app3.test7();
			// app3.test8();
			// app3.test9();
			// app3.test10();
			// app3.test11();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}