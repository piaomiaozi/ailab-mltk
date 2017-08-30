package org.mltk.mahout.cluster.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.Vector;
import org.mltk.mahout.cluster.ClusterDataModel;
import org.mltk.mahout.cluster.ClusterPreUtil;
import org.mltk.mahout.cluster.ClusterDistanceFormat;
import org.mltk.mahout.cluster.DataDistanceFactory;

public class CanopyKmeansClusterImpl implements ClusterDataModel {

	// ����ʡ����kmeans�㷨kֵ���趨

	private Integer disTypeNum;
	private Integer vecTypeNum;
	// �����޶�canopy�����㷨�Ĳ���T1��T2
	private Double T1;
	private Double T2;

	public CanopyKmeansClusterImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CanopyKmeansClusterImpl(Integer disTypeNum, Integer vecTypeNum) {
		super();
		this.disTypeNum = disTypeNum;
		this.vecTypeNum = vecTypeNum;
	}

	public CanopyKmeansClusterImpl(Integer disTypeNum, Integer vecTypeNum,
			Double t1, Double t2) {
		super();
		this.disTypeNum = disTypeNum;
		this.vecTypeNum = vecTypeNum;
		T1 = t1;
		T2 = t2;
	}

	@Override
	public Map<Integer, Integer> clusterDriver(Integer vecTypeNum,
			DistanceMeasure distanceMeasure, List<Vector> pointsVectors) {

		// �ж�canopyר�����ݼ��Ƿ��Ѿ������趨�ã���Ҫ�����趨canopy���ݼ����Ҳ���ʹ�ýӿڶ��壬�������ʵ�����һ��ȱ��
		if (CanopyClusterAssist.getCanopyVectors() == null) {
			System.err.println("��δ�趨canopy���ݼ�����Ҫ�趨��error��setCanopyVectors()");
			return null;
		}

		// ���෵�ؽ��
		Map<Integer, Integer> clusterResMap = new HashMap<Integer, Integer>();

		// ͨ��canopy�㷨����kֵ�����ĵ�
		Map<String, Object> canopyResMap = CanopyClusterAssist
				.generateClusterCenter(CanopyClusterAssist.getCanopyVectors(),
						distanceMeasure, this.T1, this.T2);

		List<Vector> canopyPoints = (List<Vector>) canopyResMap.get("centers");

		List<Cluster> clusters = new ArrayList<Cluster>();

		int clusterId = 0;
		for (Vector v : canopyPoints) {
			clusters.add(new Cluster(v, clusterId++,
					new EuclideanDistanceMeasure()));
		}

		// ǰһ�����ִ�������㷨��������������һ�����ִ���������ֵ
		List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(
				pointsVectors, clusters, distanceMeasure, 10, 0.01);

		// ��ô���ź����ĵ�ӳ�伯��
		Map<Integer, Vector> clusterIdCenterMap = new HashMap<Integer, Vector>();
		for (Cluster cluster : finalClusters.get(finalClusters.size() - 1)) {
			// TODO delete print
			System.out.println("Cluster id:" + cluster.getId() + " center: "
					+ cluster.getCenter().asFormatString());

			clusterIdCenterMap.put(cluster.getId(), cluster.getCenter());
		}

		// �ֶ�������������������ص�ӳ���ϵ
		clusterResMap = ClusterDistanceFormat.checkPointBelongCenter(
				pointsVectors, clusterIdCenterMap, distanceMeasure);

		return clusterResMap;
	}

	@Override
	public Map<Integer, Integer> exec(double[][] points) {

		// ����㼯��
		List<Vector> pointsVectors = ClusterPreUtil.getPoints(vecTypeNum,
				points);
		// ��Ҫ��������canopy����㼯�ϣ�����canopy����Ὣԭ���ĵ㼯�ƻ�
		CanopyClusterAssist.setCanopyVectors(ClusterPreUtil.getPoints(
				vecTypeNum, points));

		// ��þ����׼ʵ��
		DistanceMeasure distance = DataDistanceFactory
				.prodDistance(this.disTypeNum);

		Map<Integer, Integer> clusterResMap = this.clusterDriver(
				this.vecTypeNum, distance, pointsVectors);
		return clusterResMap;
	}

	@Override
	public Map<Integer, Integer> exec(List<Vector> pointsVectors) {

		// �ж�canopyר�����ݼ��Ƿ��Ѿ������趨�ã���Ҫ�����趨canopy���ݼ����Ҳ���ʹ�ýӿڶ��壬�������ʵ�����һ��ȱ��
		if (CanopyClusterAssist.getCanopyVectors() == null) {
			System.err.println("��δ�趨canopy���ݼ�����Ҫ�趨��error��setCanopyVectors()");
			return null;
		}

		// ��þ����׼ʵ��
		DistanceMeasure distance = DataDistanceFactory
				.prodDistance(this.disTypeNum);

		Map<Integer, Integer> clusterResMap = this.clusterDriver(
				this.vecTypeNum, distance, pointsVectors);
		return clusterResMap;
	}

	public Integer getDisTypeNum() {
		return disTypeNum;
	}

	public void setDisTypeNum(Integer disTypeNum) {
		this.disTypeNum = disTypeNum;
	}

	public Integer getVecTypeNum() {
		return vecTypeNum;
	}

	public void setVecTypeNum(Integer vecTypeNum) {
		this.vecTypeNum = vecTypeNum;
	}

	public Double getT1() {
		return T1;
	}

	public void setT1(Double t1) {
		T1 = t1;
	}

	public Double getT2() {
		return T2;
	}

	public void setT2(Double t2) {
		T2 = t2;
	}

}
