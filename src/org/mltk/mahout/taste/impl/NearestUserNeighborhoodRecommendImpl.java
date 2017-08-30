package org.mltk.mahout.taste.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CachingUserSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.mltk.mahout.taste.DataSimilarityFactory;
import org.mltk.mahout.taste.RecommendDataModel;

/**
 * 
 * @author superhy
 * 
 *         ���л����û����Ƽ����棬��Ҫ�������ƶȼ��㷽����Ӧ��ţ� ���贫���Ƿ�Ϊ���ƶȼ������뻺�����
 * 
 */
public class NearestUserNeighborhoodRecommendImpl extends RecommendDataModel {

	private Integer simTypeNum;
	private Boolean flagCachingSim;

	public NearestUserNeighborhoodRecommendImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NearestUserNeighborhoodRecommendImpl(DataModel model,
			Recommender recommender) {
		super(model, recommender);
		// TODO Auto-generated constructor stub
	}

	public NearestUserNeighborhoodRecommendImpl(Integer simTypeNum,
			Boolean flagCachingSim) {
		super();
		this.simTypeNum = simTypeNum;
		this.flagCachingSim = flagCachingSim;
	}

	public NearestUserNeighborhoodRecommendImpl(DataModel model,
			Recommender recommender, Integer simTypeNum, Boolean flagCachingSim) {
		super(model, recommender);
		this.simTypeNum = simTypeNum;
		this.flagCachingSim = flagCachingSim;
	}

	/**
	 * �����Ƽ�����
	 * 
	 * @param dataModel
	 */
	public Recommender recommenderProd(DataModel dataModel) {

		try {

			/*
			 * ������ƶȼ�����ʵ��
			 */

			if (this.flagCachingSim == null) {
				this.flagCachingSim = false;
			}
			UserSimilarity userSim = null;

			if (this.flagCachingSim) {
				// Ϊ���ƶȼ������뻺�����
				userSim = new CachingUserSimilarity(
						DataSimilarityFactory.prodUserSimilarity(dataModel,
								this.simTypeNum), dataModel);
			} else {
				// ֱ�ӽ������ƶȼ���
				userSim = DataSimilarityFactory.prodUserSimilarity(dataModel,
						this.simTypeNum);
			}

			// ǰ����������ھӼ��ϴ�С
			UserNeighborhood userNbh = new NearestNUserNeighborhood(20,
					userSim, dataModel);

			// �����Ƽ�����
			Recommender recommender = new GenericUserBasedRecommender(dataModel,
					userNbh, userSim);

			return recommender;
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}

	/**
	 * ִ���Ƽ����棬������Ƽ����
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

	public Integer getSimTypeNum() {
		return simTypeNum;
	}

	public void setSimTypeNum(Integer simTypeNum) {
		this.simTypeNum = simTypeNum;
	}

	public Boolean getFlagCachingSim() {
		return flagCachingSim;
	}

	public void setFlagCachingSim(Boolean flagCachingSim) {
		this.flagCachingSim = flagCachingSim;
	}

}
