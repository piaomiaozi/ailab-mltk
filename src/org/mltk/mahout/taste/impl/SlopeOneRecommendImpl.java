package org.mltk.mahout.taste.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.mltk.mahout.taste.RecommendDataModel;

/**
 * 
 * @author superhy
 * 
 *         һ��ʽ�Ƽ�����ʵ����
 * 
 */
public class SlopeOneRecommendImpl extends RecommendDataModel {

	public SlopeOneRecommendImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SlopeOneRecommendImpl(DataModel model, Recommender recommender) {
		super(model, recommender);
		// TODO Auto-generated constructor stub
	}

	/**
	 * �����Ƽ�����
	 * 
	 * @param dataModel
	 */
	public Recommender recommenderProd(DataModel dataModel) {

		try {

			// �����Ƽ�����
			Recommender recommender = new SlopeOneRecommender(dataModel);

			return recommender;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ִ���Ƽ����棬������Ƽ����
	 * 
	 * @param userNo
	 * @param itemNum
	 * @return
	 */
	public List<RecommendedItem> exec(int userNo, int itemNum) {

		List<RecommendedItem> recItemList = new ArrayList<RecommendedItem>();

		try {

			// ��Ҫ��ǰ����dataModel
			// ���dataModel�Ƿ�ɹ����룬���û�гɹ����룬��ǰ�˳�����
			if (super.model == null) {
				System.err.println("dataModel��ʧ��������ʧ�ܣ����飡");
				return null;
			}
			// �����Ƽ�����
			Recommender recommender = this.recommenderProd(super.model);
			super.setRecommender(recommender);

			// ˢ���Ƽ���
			this.recommender.refresh(null);

			// Ϊָ���û����Ƽ��ƶ���Ŀ
			recItemList = this.recommender.recommend(userNo, itemNum);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		return recItemList;
	}

}
