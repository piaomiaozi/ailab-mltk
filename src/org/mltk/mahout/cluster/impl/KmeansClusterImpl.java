package org.mltk.mahout.cluster.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.mahout.clustering.dirichlet.UncommonDistributions;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansClusterer;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.common.distance.EuclideanDistanceMeasure;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;
import org.mltk.mahout.cluster.ClusterDataModel;
import org.mltk.mahout.cluster.ClusterPreUtil;
import org.mltk.mahout.cluster.ClusterDistanceFormat;
import org.mltk.mahout.cluster.DataDistanceFactory;
import org.mltk.mahout.util.RandomPointsUtil;

/**
 * ��ͨ�ڴ洦����ʽ��Kmeans�㷨ʵ��
 * 
 * @author superhy
 * 
 */
public class KmeansClusterImpl implements ClusterDataModel {

	// kmeans�㷨�صĸ���
	private Integer k;
	private Integer disTypeNum;
	private Integer vecTypeNum;

	public KmeansClusterImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public KmeansClusterImpl(Integer k, Integer disTypeNum, Integer vecTypeNum) {
		super();
		this.k = k;
		this.disTypeNum = disTypeNum;
		this.vecTypeNum = vecTypeNum;
	}

	@Override
	public Map<Integer, Integer> clusterDriver(Integer vecTypeNum,
			DistanceMeasure distanceMeasure, List<Vector> pointsVectors) {

		// ���෵�ؽ��
		Map<Integer, Integer> clusterResMap = new HashMap<Integer, Integer>();

		// �����Ȱ���Ŀ������������е����ѡ�����ĵ㣬���������������
		List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(
				pointsVectors, this.k);

		List<Cluster> clusters = new ArrayList<Cluster>();

		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new Cluster(v, clusterId++,
					new EuclideanDistanceMeasure()));
		}

		// ǰһ�����ִ�������㷨��������������һ�����ִ���������ֵ
		List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(
				pointsVectors, clusters, distanceMeasure, 10, 0.001);

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

		// ��þ����׼ʵ��
		DistanceMeasure distance = DataDistanceFactory
				.prodDistance(this.disTypeNum);

		Map<Integer, Integer> clusterResMap = this.clusterDriver(
				this.vecTypeNum, distance, pointsVectors);
		return clusterResMap;
	}

	@Override
	public Map<Integer, Integer> exec(List<Vector> pointsVectors) {

		// ��þ����׼ʵ��
		DistanceMeasure distance = DataDistanceFactory
				.prodDistance(this.disTypeNum);

		Map<Integer, Integer> clusterResMap = this.clusterDriver(
				this.vecTypeNum, distance, pointsVectors);
		return clusterResMap;
	}

	/*-----------------------------����Ϊ��������------------------------------*/

	/**
	 * ��������ʵ�������ڵ�
	 * 
	 * @param vectors
	 * @param num
	 * @param mx
	 * @param my
	 * @param sd
	 */
	public static void generateSamples(List<Vector> vectors, int num,
			double mx, double my, double mz, double sd) {
		for (int i = 0; i < num; i++) {
			vectors.add(new DenseVector(new double[] {
					UncommonDistributions.rNorm(mx, sd),
					UncommonDistributions.rNorm(my, sd),
					UncommonDistributions.rNorm(mz, sd) }));
		}
	}

	// TODO delete
	/*
	 * ����������
	 */
	public static void main(String[] args) {

		// generateSamples(sampleData, 400, 1, 1, -1, 3);
		// generateSamples(sampleData, 300, 1, 0, 2, 0.5);
		// generateSamples(sampleData, 300, 0, 2, 0, 0.1);

		double[][] points = { { 1, 1, 1 }, { 1, 2, 2 }, { 3, 3, 2 },
				{ 2, 1, 1 }, { 2, 2, 3 }, { 8, 8, 7 }, { 9, 8, 7 },
				{ 8, 9, 8 }, { 9, 9, 8 } };

		int k = 2;

		// List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(
		// sampleData, k);

		// ���������������б�
		List<Vector> randomPointsPre = ClusterPreUtil.getPoints(1, points);
		// �����Ȱ���Ŀ������������е����ѡ�����ĵ㣬��������������У��漴���ĵ������б�
		List<Vector> randomPoints = RandomPointsUtil.chooseRandomPoints(
				randomPointsPre, k);

		List<Cluster> clusters = new ArrayList<Cluster>();

		int clusterId = 0;
		for (Vector v : randomPoints) {
			clusters.add(new Cluster(v, clusterId++,
					new EuclideanDistanceMeasure()));
		}

		// ǰһ�����ִ�������㷨��������������һ�����ִ���������ֵ
		List<List<Cluster>> finalClusters = KMeansClusterer.clusterPoints(
				randomPointsPre, clusters, new EuclideanDistanceMeasure(), 1,
				0.001);
		for (Cluster cluster : finalClusters.get(finalClusters.size() - 1)) {

			System.out.println("Cluster id:" + cluster.getId() + " center: "
					+ cluster.getCenter().asFormatString());
		}

	}

	public Integer getK() {
		return k;
	}

	public void setK(Integer k) {
		this.k = k;
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

}
