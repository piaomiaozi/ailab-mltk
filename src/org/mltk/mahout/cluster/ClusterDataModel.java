package org.mltk.mahout.cluster;

import java.util.List;
import java.util.Map;

import org.apache.mahout.common.distance.DistanceMeasure;
import org.apache.mahout.math.Vector;

/**
 * 
 * @author superhy
 * 
 */
public interface ClusterDataModel {

	// ����HDFSģʽ�ľ����ļ�·��
	public static String CLUSTER_ALL_FOLDER = ".\\file\\cluster";
	public static String CLUSTER_TESTDATA_DIR = ".\\file\\cluster\\testdata";
	public static String CLUSTER_OUTPUT_DIR = ".\\file\\cluster\\output";
	public static String CLUSTER_CLUSTERS_DIR = ".\\file\\cluster\\testdata\\clusters";
	public static String CLUSTER_POINTS_DIR = ".\\file\\cluster\\testdata\\points";

	/**
	 * ʵ�־�������
	 * 
	 * @param vecTypeNum
	 * @param distance
	 * @param points
	 * @return
	 */
	public Map<Integer, Integer> clusterDriver(Integer vecTypeNum,
			DistanceMeasure distance, List<Vector> pointsVectors);

	/**
	 * ִ�о�������
	 * 
	 * @param points
	 * @return
	 */
	public Map<Integer, Integer> exec(double[][] points);

	/**
	 * ִ�о������棬ֱ�Ӵ���vector
	 * 
	 * @param pointsVectors
	 * @return
	 */
	@Deprecated
	public Map<Integer, Integer> exec(List<Vector> pointsVectors);

}
