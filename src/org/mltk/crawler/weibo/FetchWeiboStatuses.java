package org.mltk.crawler.weibo;

import java.util.ArrayList;
import java.util.List;

import org.mltk.crawler.cache.WeiboKeyParam;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

public class FetchWeiboStatuses {

	/**
	 * ��ȡ���µĹ���΢��
	 * 
	 * @param count
	 *            ���󷵻ص�΢������
	 * @return
	 */
	public List<Status> getPublicTimeline(int count) {

		List<Status> publicStatusList = new ArrayList<Status>();

		try {

			Timeline tm = new Timeline(WeiboKeyParam.ACCESS_TOKEN);
			StatusWapper status = tm.getPublicTimeline(count, 0);

			System.out.println(status.getTotalNumber());

			List<Status> statusList = status.getStatuses();
			for (Status eachStatus : statusList) {
				publicStatusList.add(eachStatus);
			}

			// TODO delete print
			// System.out.println(publicJsonList);
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

		return publicStatusList;
	}

	/**
	 * �����û��ǳƷ����û���΢����ֻ�ܻ�ȡ��Ȩ�û���΢����Ŀǰֻ���Լ���΢����
	 * 
	 * @param screen_name
	 *            �û��ǳ�
	 * @param since_id
	 *            ��ʼ΢��id
	 * @param max_id
	 *            ����΢��id
	 * @param count
	 *            ��ҳ���󷵻ص�΢������
	 * @param page
	 *            ���ؽ����ҳ��
	 * @return
	 */
	@Deprecated
	public List<Status> getUserTimelineByName(String screen_name, int since_id,
			int max_id, int count, int page) {

		List<Status> userStatusList = new ArrayList<Status>();

		try {

			// ���ò��ֲ�����Ĭ��ֵ
			count = (count == 0 ? 20 : count);
			page = (page == 0 ? 1 : page);

			// ����Ĭ�ϲ���
			Timeline tm = new Timeline(WeiboKeyParam.ACCESS_TOKEN);
			Paging paging = new Paging();
			if (since_id != 0) {
				paging.setSinceId(since_id);
			}
			if (max_id != 0) {
				paging.setMaxId(max_id);
			}
			if (count != 0) {
				paging.setCount(count);
			}
			if (page != 0) {
				paging.setPage(page);
			}

			// ��ȡ��ѯ���
			StatusWapper status = tm.getUserTimelineByName(screen_name, paging,
					0, 0);

			System.out.println("���������" + status.getTotalNumber());

			List<Status> statusList = status.getStatuses();
			for (Status eachStatus : statusList) {
				userStatusList.add(eachStatus);
			}

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userStatusList;
	}

	/**
	 * ��ȡ�ʺŹ�ע�û�������΢����ȫ����ã������ٰ��û�Ψһ��ʶ���ˣ�
	 * 
	 * @param since_id
	 *            ��ʼ΢��id
	 * @param max_id
	 *            ����΢��id
	 * @param count
	 *            ��ҳ���󷵻ص�΢������
	 * @param page
	 *            ���ؽ����ҳ��
	 * @return
	 */
	public List<Status> getHomeTimeLine(int since_id, int max_id, int count,
			int page) {

		List<Status> userStatusList = new ArrayList<Status>();

		try {
			// ���ò��ֲ�����Ĭ��ֵ
			count = (count == 0 ? 20 : count);
			page = (page == 0 ? 1 : page);

			// ����Ĭ�ϲ���
			Timeline tm = new Timeline(WeiboKeyParam.ACCESS_TOKEN);
			Paging paging = new Paging();
			if (since_id != 0) {
				paging.setSinceId(since_id);
			}
			if (max_id != 0) {
				paging.setMaxId(max_id);
			}
			if (count != 0) {
				paging.setCount(count);
			}
			if (page != 0) {
				paging.setPage(page);
			}

			// ��ȡ��ѯ���
			StatusWapper status = tm.getHomeTimeline(0, 0, paging);

			System.out.println("���������" + status.getTotalNumber());

			List<Status> statusList = status.getStatuses();
			for (Status eachStatus : statusList) {
				userStatusList.add(eachStatus);
			}

		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userStatusList;
	}
}
