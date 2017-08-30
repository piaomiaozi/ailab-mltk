package org.mltk.mahout.cluster.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.mahout.clustering.WeightedVectorWritable;
import org.apache.mahout.clustering.kmeans.Cluster;
import org.apache.mahout.clustering.kmeans.KMeansDriver;
import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;
import org.mltk.mahout.cluster.ClusterDataModel;
import org.mltk.mahout.cluster.ClusterPreUtil;
import org.mltk.mahout.cluster.DataDistanceFactory;
import org.mltk.task.util.FileDocumentIOManagement;

/**
 * HDFS�ļ�����ʽ����Kmeans�㷨ʵ��
 * 
 * @author superhy
 * 
 */
public class HDFSKmeansClusterImpl implements ClusterDataModel {

	public static String CLUSTER_CENTER_FILE_DIR = ".\\file\\cluster\\testdata\\points\\file1";
	public static String CLUSTER_CENTER_PART_PATH = ".\\file\\cluster\\testdata\\clusters\\part-0000";
	public static String CLUSTER_DIRVER_OUTPUT_PATH = ".\\file\\cluster\\output\\"
			+ Cluster.CLUSTERED_POINTS_DIR + "\\part-m-00000";

	// kmeans�㷨�صĸ���
	private Integer k;
	private Integer disTypeNum;
	private Integer vecTypeNum;

	public HDFSKmeansClusterImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	public HDFSKmeansClusterImpl(Integer k, Integer disTypeNum,
			Integer vecTypeNum) {
		super();
		this.k = k;
		this.disTypeNum = disTypeNum;
		this.vecTypeNum = vecTypeNum;
	}

	@Override
	public Map<Integer, Integer> clusterDriver(Integer vecTypeNum,
			DistanceMeasure distanceMeasure, List<Vector> pointsVectors) {

		// ���cluster�ļ����е���������
		FileDocumentIOManagement.delAllFileInFolder(CLUSTER_ALL_FOLDER);

		// ׼������ķ��ؽ����ʵ����-������ż�ֵ�ԣ�
		Map<Integer, Integer> clusterResMap = new HashMap<Integer, Integer>();

		try {

			// Ϊ���ݴ�������Ŀ¼
			File testData = new File(CLUSTER_TESTDATA_DIR);
			if (!testData.exists()) {
				testData.mkdir();
			}
			testData = new File(CLUSTER_POINTS_DIR);
			if (!testData.exists()) {
				testData.mkdir();
			}
			// д���ʼ���ĵ�
			Configuration conf = new Configuration();
			FileSystem fs = FileSystem.get(conf);
			ClusterPreUtil.writePointsToFile(pointsVectors,
					CLUSTER_CENTER_FILE_DIR, fs, conf);

			Path path = new Path(CLUSTER_CENTER_PART_PATH);
			SequenceFile.Writer writer = new SequenceFile.Writer(fs, conf,
					path, Text.class, Cluster.class);

			for (int i = 0; i < this.k; i++) {
				Vector vec = pointsVectors.get(i);
				Cluster kluster = new Cluster(vec, i, distanceMeasure);
				writer.append(new Text(kluster.getIdentifier()), kluster);
			}
			writer.close();

			// ����kmeans�㷨
			KMeansDriver.run(conf, new Path(CLUSTER_POINTS_DIR), new Path(
					CLUSTER_CLUSTERS_DIR), new Path(CLUSTER_OUTPUT_DIR),
					distanceMeasure, 0.001, 10, true, false);

			SequenceFile.Reader reader = new SequenceFile.Reader(fs, new Path(
					CLUSTER_DIRVER_OUTPUT_PATH), conf);

			IntWritable key = new IntWritable();
			WeightedVectorWritable value = new WeightedVectorWritable();
			Integer valueNum = 0;
			// ��ȡ�������ӡ�����ʹ�ID
			while (reader.next(key, value)) {

				System.err.println("Analyzing " + valueNum + "->"
						+ value.toString() + " belongs to cluster "
						+ key.toString());

				clusterResMap.put(valueNum++, Integer.valueOf(key.toString()));
			}
			reader.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
