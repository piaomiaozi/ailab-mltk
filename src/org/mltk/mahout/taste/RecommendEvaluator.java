package org.mltk.mahout.taste;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.IRStatistics;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.common.RandomUtils;
import org.mltk.mahout.model.TasteEvalScore;

/**
 * 
 * @author superhy
 * 
 *         �Ƽ��������⹤��
 * 
 */
public class RecommendEvaluator {

	public static TasteEvalScore recommenderEvalProd(DataModel testModel,
			final RecommendDataModel testRecommendDataModel, final Integer N,
			double b, Boolean flagF1ValueBySelf) {

		// ���ɿ��ظ��Ľ��
		RandomUtils.useTestSeed();

		// ����������
		RecommenderEvaluator evaluator = new AverageAbsoluteDifferenceRecommenderEvaluator();
		GenericRecommenderIRStatsEvaluator irstatsEvaluator = new GenericRecommenderIRStatsEvaluator();

		RecommenderBuilder recommenderBuilder = new RecommenderBuilder() {

			@Override
			public Recommender buildRecommender(DataModel model)
					throws TasteException {
				return testRecommendDataModel.recommenderProd(model);
			}
		};

		// ����ָ��
		TasteEvalScore tasteScore = null;

		try {
			double difference = evaluator.evaluate(recommenderBuilder, null,
					testModel, 0.90, 1.0);

			// �����Ƽ��������ʱ��P��R
			IRStatistics stats = irstatsEvaluator.evaluate(recommenderBuilder,
					null, testModel, null, 2,
					GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0);
			double precision = stats.getPrecision();
			double recall = stats.getRecall();
			double f1 = stats.getF1Measure(); // Ĭ�ϵ�f1ֵ���������ѡ��ʹ���ֶ����ڲ����������f1ֵ

			tasteScore = new TasteEvalScore(difference, precision, recall);
			// �ж��Ƿ��ֶ�����f1ֵ
			if (flagF1ValueBySelf == true) {
				tasteScore.calculateF1Value(b);
			} else {
				tasteScore.setF1(f1);
			}

		} catch (TasteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return tasteScore;
		}

		return tasteScore;
	}

}
