package org.mltk.libsvm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.mltk.libsvm.model.ClassifyRes;
import org.mltk.libsvm.model.TrainDataSet;

/**
 * 
 * @author superhy
 *
 */
public class LibSVMClassifierDemo {

	// �������ݼ�
	private svm_problem classifyTrainSet;
	// �������ò���
	private svm_parameter classifyParame;

	private LibSVMParameFactory parameFactory;

	public LibSVMClassifierDemo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LibSVMClassifierDemo(LibSVMParameFactory parameFactory) {
		super();
		this.parameFactory = parameFactory;
	}

	// ���÷���ѵ�����ݼ�
	public void initClassifyTrainData(Map<Double, List<String[]>> trainData,
			Integer trainSetSize, Integer trainSetScale) throws Exception {

		/*
		 * ����ѵ�������ݣ���ѵ����������ΪLibSVM�ܹ�ʶ��ĸ�ʽ
		 */
		svm_problem trainSet = new svm_problem();
		svm_node[][] trainSpace = new svm_node[trainSetSize][trainSetScale];
		double[] trainLabels = new double[trainSetSize];

		int trainEntryNum = 0;
		for (Entry<Double, List<String[]>> trainEntry : trainData.entrySet()) { // ������ʱ������ʹ��entry����map����
			// ��������������ѵ�����
			trainLabels[trainEntryNum] = trainEntry.getKey();

			// ������������������ֵ
			int vectorEleNum = 0;
			for (String[] vectorNode : trainEntry.getValue()) {
				svm_node trainNode = new svm_node();
				trainNode.index = Integer.parseInt(vectorNode[0]);
				trainNode.value = Double.parseDouble(vectorNode[1]);

				trainSpace[trainEntryNum][vectorEleNum] = trainNode;
				vectorEleNum++;
			}
			trainEntryNum++;
		}

		// ��������飬�����ռ��ѵ����������������setģ��
		trainSet.x = trainSpace;
		trainSet.y = trainLabels;
		trainSet.l = trainSetSize;

		for (int i = 0; i < trainSpace.length; i++) {
			System.out.print(trainLabels[i] + ": ");
			for (int j = 0; j < trainSpace[i].length; j++) {
				System.out.print(trainSpace[i][j].value + " ");
			}
			System.out.println();
		}

		setClassifyTrainSet(trainSet);
	}

	// ���÷������ò���
	public void initClassifyParame(LibSVMParameFactory parameFactory)
			throws Exception {

		svm_parameter svmParame = parameFactory.prodLibSVMParam();
		// �����������Ƿ�Ϸ�
		String parameError = svm.svm_check_parameter(getClassifyTrainSet(),
				svmParame);
		if (parameError != null) {
			System.err.println("�������ô���! " + parameError);
			return;
		}

		setClassifyParame(parameFactory.prodLibSVMParam());
	}

	// LibSVM�����������ܷ���
	public Map<Integer, ClassifyRes> classifyDriver(
			Map<Integer, Double[]> testData) {

		// ׼��Ҫ���ص�����
		Map<Integer, ClassifyRes> classifyResMap = new HashMap<Integer, ClassifyRes>();
		// ѵ������ģ��
		svm_model model = svm.svm_train(this.classifyTrainSet,
				this.classifyParame);

		try {
			// TODO ѵ���洢modelFile��ִ��Ԥ�����
			svm.svm_save_model("./file/libsvm/svm_model_file", model);
			svm.svm_load_model("./file/libsvm/svm_model_file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ���з���Ԥ��
		for (Entry<Integer, Double[]> testEntry : testData.entrySet()) {

			ClassifyRes classifyRes = new ClassifyRes();

			int testNodeIndex = testEntry.getKey();
			Double[] testNodeVector = testEntry.getValue();

			svm_node[] testNode = new svm_node[testNodeVector.length];
			for (int i = 0; i < testNodeVector.length; i++) {
				// ���ȶ�svm_node���г�ʼ��
				testNode[i] = new svm_node();

				testNode[i].index = i + 1;
				testNode[i].value = testNodeVector[i];
			}
			double[] probEstimates = new double[model.label.length];

			// ���з���Ԥ��
			double probilityRes = svm.svm_predict_probability(model, testNode,
					probEstimates);
			double normalRes = svm.svm_predict(model, testNode);

			classifyRes.setProbilityRes(probilityRes);
			classifyRes.setNormalRes(normalRes);
			classifyRes.prodResDistribution(model, probEstimates);

			// ����һ���Ԥ����������ӳ�伯��
			classifyResMap.put(testNodeIndex, classifyRes);
		}

		return classifyResMap;
	}

	// LibSVM������ִ�нӿ�
	public Map<Integer, ClassifyRes> exec(TrainDataSet trainDataSet,
			Map<Integer, Double[]> testDataSet) {

		Map<Integer, ClassifyRes> classifyResMap = new HashMap<Integer, ClassifyRes>();

		try {

			this.initClassifyTrainData(trainDataSet.getTrainData(),
					trainDataSet.getTrainSetSize(),
					trainDataSet.getTrainSetScale());

			// ���û������parameFactory���½�Ĭ�ϲ�����parameFactory
			if (this.parameFactory == null) {
				setParameFactory(new LibSVMParameFactory());
			}
			this.initClassifyParame(this.parameFactory);

			classifyResMap = this.classifyDriver(testDataSet);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}

		return classifyResMap;
	}

	public svm_problem getClassifyTrainSet() {
		return classifyTrainSet;
	}

	public void setClassifyTrainSet(svm_problem classifyTrainData) {
		this.classifyTrainSet = classifyTrainData;
	}

	public svm_parameter getClassifyParame() {
		return classifyParame;
	}

	public void setClassifyParame(svm_parameter classifyParame) {
		this.classifyParame = classifyParame;
	}

	public LibSVMParameFactory getParameFactory() {
		return parameFactory;
	}

	public void setParameFactory(LibSVMParameFactory parameFactory) {
		this.parameFactory = parameFactory;
	}

}
