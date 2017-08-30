package org.mltk.mahout.cluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

public class ClusterDistanceFormat {

	/**
	 * ������������֮��ľ���
	 * 
	 * @param vectorA
	 * @param vectorB
	 * @param distanceMeasure
	 */
	public static double cmptVectorDistance(Vector vectorA, Vector vectorB,
			DistanceMeasure distanceMeasure) {

		return distanceMeasure.distance(vectorA, vectorB);
	}

	/**
	 * ����ÿ����������ĵľ�����ȷ��������
	 * 
	 * @param pointsVectors
	 * @param clusterIdCenterMap
	 * @return
	 */
	public static Map<Integer, Integer> checkPointBelongCenter(
			List<Vector> pointsVectors,
			Map<Integer, Vector> clusterIdCenterMap,
			DistanceMeasure distanceMeasure) {

		Map<Integer, Integer> clusterResMap = new HashMap<Integer, Integer>();

		// ����ÿ��������ѡ����������Ĳ������ڸô�
		int pointId = 0;
		
		for (Vector point : pointsVectors) {

			int centerId = 0;
			double minDistance = cmptVectorDistance(point,
					clusterIdCenterMap.get(centerId), distanceMeasure);
			for (Map.Entry<Integer, Vector> entry : clusterIdCenterMap
					.entrySet()) {

				double pointsDistance = cmptVectorDistance(point,
						entry.getValue(), distanceMeasure);
				if (minDistance > pointsDistance) {
					centerId = entry.getKey();
					minDistance = pointsDistance;
				}
			}

			clusterResMap.put(pointId++, centerId);
		}

		return clusterResMap;
	}
}
