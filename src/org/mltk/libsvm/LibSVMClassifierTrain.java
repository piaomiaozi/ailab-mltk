package org.mltk.libsvm;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;

import org.mltk.libsvm.model.TrainDataSet;

/**
 * 
 * @author superhy
 *
 */
public class LibSVMClassifierTrain {

	// �������ݼ�
	private svm_problem classifyTrainEntity;
	// �������ò���
	private svm_parameter classifyParame;

	private LibSVMParameFactory parameFactory;

	public LibSVMClassifierTrain() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LibSVMClassifierTrain(LibSVMParameFactory parameFactory) {
		super();
		this.parameFactory = parameFactory;
	}

	/**
	 * ���÷���ѵ�����ݼ�
	 * 
	 * @param trainData
	 * @param trainSetSize
	 * @param trainSetScale
	 * @throws Exception
	 */
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

		setClassifyTrainEntity(trainSet);
	}

	/**
	 * ���÷������ò���
	 * 
	 * @param parameFactory
	 * @throws Exception
	 */
	public void initClassifyParame(LibSVMParameFactory parameFactory)
			throws Exception {

		svm_parameter svmParame = parameFactory.prodLibSVMParam();
		// �����������Ƿ�Ϸ�
		String parameError = svm.svm_check_parameter(getClassifyTrainEntity(),
				svmParame);
		if (parameError != null) {
			System.err.println("�������ô���! " + parameError);
			return;
		}

		setClassifyParame(parameFactory.prodLibSVMParam());
	}

	/**
	 * LibSVM������ѵ��������
	 * 
	 * @param svmModelDiskPath
	 * @return
	 */
	public svm_model classifierTrainDriver(String svmModelDiskPath) {

		// ѵ������ģ��
		svm_model model = svm.svm_train(this.classifyTrainEntity,
				this.classifyParame);

		try {

			svm.svm_save_model(svmModelDiskPath, model);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return model;
	}

	/**
	 * LibSVM������ִ�нӿ�
	 * 
	 * @param trainDataSet
	 * @return
	 */
	public boolean exec(String svmModelDiskPath, TrainDataSet trainDataSet) {

		try {

			this.initClassifyTrainData(trainDataSet.getTrainData(),
					trainDataSet.getTrainSetSize(),
					trainDataSet.getTrainSetScale());

			// ���û������parameFactory���½�Ĭ�ϲ�����parameFactory
			if (this.parameFactory == null) {
				setParameFactory(new LibSVMParameFactory());
			}
			this.initClassifyParame(this.parameFactory);

			this.classifierTrainDriver(svmModelDiskPath);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public svm_problem getClassifyTrainEntity() {
		return classifyTrainEntity;
	}

	public void setClassifyTrainEntity(svm_problem classifyTrainData) {
		this.classifyTrainEntity = classifyTrainData;
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
