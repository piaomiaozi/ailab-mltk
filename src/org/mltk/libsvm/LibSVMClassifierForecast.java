package org.mltk.libsvm;

import java.util.ArrayList;
import java.util.List;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;

import org.mltk.libsvm.model.ClassifyRes;
import org.mltk.libsvm.model.TestDataItem;

/**
 * 
 * @author superhy
 *
 */
public class LibSVMClassifierForecast {

	public LibSVMClassifierForecast() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * LibSVM�����������ܷ���
	 * 
	 * @param model
	 * @param testDataItem
	 * @return
	 */
	public ClassifyRes classifyForecastDriver(svm_model model, TestDataItem testDataItem) {

		ClassifyRes classifyRes = new ClassifyRes();

		String testVecId = testDataItem.getVecId();
		Double[] testNodeVector = testDataItem.getTestData();

		svm_node[] testNode = new svm_node[testNodeVector.length];
		for (int i = 0; i < testNodeVector.length; i++) {
			// ���ȶ�svm_node���г�ʼ��
			testNode[i] = new svm_node();

			testNode[i].index = i + 1;
			testNode[i].value = testNodeVector[i];
		}
		double[] probEstimates = new double[model.label.length];

		// ִ�з���Ԥ��
		double probilityRes = svm.svm_predict_probability(model, testNode,
				probEstimates);
		double normalRes = svm.svm_predict(model, testNode);

		// ���������
		classifyRes.setVecId(testVecId);
		classifyRes.setProbilityRes(probilityRes);
		classifyRes.setNormalRes(normalRes);
		classifyRes.prodResDistribution(model, probEstimates);

		return classifyRes;
	}

	/**
	 * LibSVM������ִ�нӿ�
	 * 
	 * @param svmModelDiskPath
	 * @param testDataItem
	 * @return
	 */
	public ClassifyRes exec(String svmModelDiskPath, TestDataItem testDataItem) {

		ClassifyRes classifyRes = null;

		try {

			svm_model model = svm.svm_load_model(svmModelDiskPath);

			classifyRes = this.classifyForecastDriver(model, testDataItem);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

		return classifyRes;
	}

	/**
	 * LibSVM����������ִ��ִ�нӿ�
	 * 
	 * @param svmModelDiskPath
	 * @param testDataItems
	 * @return
	 */
	public List<ClassifyRes> execBatch(String svmModelDiskPath,
			List<TestDataItem> testDataItems) {

		List<ClassifyRes> classifyResList = new ArrayList<ClassifyRes>();

		try {
			svm_model model = svm.svm_load_model(svmModelDiskPath);

			for (TestDataItem testDataItem : testDataItems) {
				classifyResList.add(this.classifyForecastDriver(model, testDataItem));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classifyResList;
	}

}
