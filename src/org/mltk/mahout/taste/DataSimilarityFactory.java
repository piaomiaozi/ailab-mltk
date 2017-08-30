package org.mltk.mahout.taste;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.common.Weighting;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.SpearmanCorrelationSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.TanimotoCoefficientSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class DataSimilarityFactory {

	/**
	 * ���������û������ƶ�ʵ�壬Ĭ������
	 * 
	 * @param model
	 * @return
	 */
	public synchronized static UserSimilarity prodUserSimilarity(DataModel model) {

		UserSimilarity userSim = null;

		try {
			userSim = new PearsonCorrelationSimilarity(model,
					Weighting.WEIGHTED);
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userSim;
	}

	/**
	 * ���������û������ƶ�ʵ�壬�����Ӧʵ��ı��
	 * ��Ȩֵ��Ƥ��ѷ���ƶȣ�1��ŷ�Ͼ��룺2���������ƶȣ�3��˹Ƥ�������ƶȣ�4��Jaccard���ƶȣ�5��������Ȼ�����ƶȣ�6
	 * 
	 * @param model
	 * @param simTypeNum
	 * @return
	 */
	public synchronized static UserSimilarity prodUserSimilarity(
			DataModel model, Integer simTypeNum) {

		UserSimilarity userSim = null;

		if (simTypeNum == null || (simTypeNum < 1 || simTypeNum > 6)) {
			simTypeNum = 1;
		}

		try {
			switch (simTypeNum) {
			case 1:
				userSim = new PearsonCorrelationSimilarity(model,
						Weighting.WEIGHTED);
				break;
			case 2:
				userSim = new EuclideanDistanceSimilarity(model);
				break;
			case 3:
				// �������ƶȾ��ǲ���Ȩ�ص�Ƥ��ѷ���ƶ�
				userSim = new PearsonCorrelationSimilarity(model);
				break;
			case 4:
				userSim = new SpearmanCorrelationSimilarity(model);
				break;
			case 5:
				userSim = new TanimotoCoefficientSimilarity(model);
				break;
			case 6:
				userSim = new LogLikelihoodSimilarity(model);
				break;
			default:
				// Ĭ��ʹ�ô�Ȩֵ��Ƥ��ѷ���ƶ�
				userSim = new PearsonCorrelationSimilarity(model,
						Weighting.WEIGHTED);
				break;
			}
		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userSim;
	}

	/**
	 * ����������Ʒ�����ƶ�ʵ�壬�����Ӧʵ��ı��
	 * 
	 * @param model
	 * @param simTypeNum
	 * @return
	 */
	public synchronized static ItemSimilarity prodItemSimilarity(
			DataModel model, Integer simTypeNum) {

		return null;
	}

}
