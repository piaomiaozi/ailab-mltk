package org.mltk.mahout.cluster;

import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.RandomAccessSparseVector;
import org.apache.mahout.math.SequentialAccessSparseVector;
import org.apache.mahout.math.Vector;

public class DataVectorFactory {

	/**
	 * ��������ת�����ߣ�Ĭ������
	 * 
	 * @param arrayLength
	 * @return
	 */
	public synchronized static Vector prodVector(Integer arrayLength) {

		Vector vec = null;

		try {
			vec = new SequentialAccessSparseVector(arrayLength);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vec;
	}

	/**
	 * ��������ת�����ߣ������Ӧ��ʵ���ţ��ܼ������ռ䣺1��ϡ��������2���Ż���������������ռ䣺3
	 * 
	 * @param arrayLength
	 * @param vecTypeNum
	 * @return
	 */
	public synchronized static Vector prodVector(Integer arrayLength,
			Integer vecTypeNum) {

		Vector vec = null;

		if (vecTypeNum == null || (vecTypeNum < 1 || vecTypeNum > 3)) {
			vecTypeNum = 3;
		}

		try {
			switch (vecTypeNum) {
			case 1:
				vec = new DenseVector(arrayLength);
				break;
			case 2:
				vec = new RandomAccessSparseVector(arrayLength);
				break;
			case 3:
				vec = new SequentialAccessSparseVector(arrayLength);
				break;
			default:
				// Ĭ��ʹ���Ż���������������ռ�
				vec = new SequentialAccessSparseVector(arrayLength);
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return vec;
	}
}
