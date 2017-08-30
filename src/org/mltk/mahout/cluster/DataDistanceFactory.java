package org.mltk.mahout.cluster;

import org.apache.mahout.common.distance.CosineDistanceMeasure;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.common.distance.ManhattanDistanceMeasure;
import org.apache.mahout.common.distance.SquaredEuclideanDistanceMeasure;
import org.apache.mahout.common.distance.TanimotoDistanceMeasure;

public class DataDistanceFactory {

	/**
	 * ����������������ʵ�壬Ĭ������
	 * 
	 * @return
	 */
	public synchronized static DistanceMeasure prodDistance() {

		DistanceMeasure distance = null;

		try {
			distance = new EuclideanDistanceMeasure();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return distance;
	}

	/**
	 * ����������������ʵ�壬�����Ӧ��ʵ���ţ�ŷ�Ͼ��룺1��ƽ��ŷʽ���룺2�������پ��룺3�����Ҿ��룺4���ȱ����룺5
	 * 
	 * @param disTypeNum
	 */
	public synchronized static DistanceMeasure prodDistance(Integer disTypeNum) {

		DistanceMeasure distance = null;

		if (disTypeNum == null || (disTypeNum < 1 || disTypeNum > 5)) {
			disTypeNum = 1;
		}
		
		try {
			switch (disTypeNum) {
			case 1:
				distance = new EuclideanDistanceMeasure();
				break;
			case 2:
				distance = new SquaredEuclideanDistanceMeasure();
				break;
			case 3:
				distance = new ManhattanDistanceMeasure();
				break;
			case 4:
				distance = new CosineDistanceMeasure();
				break;
			case 5:
				distance = new TanimotoDistanceMeasure();
			default:
				// Ĭ��ʹ��ŷ�Ͼ����׼
				distance = new EuclideanDistanceMeasure();
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return distance;
	}
}
