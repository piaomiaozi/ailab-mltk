package org.mltk.mahout.model;

/**
 * 
 * @author superhy
 *
 */
public class TasteEvalScore {

	private Double difference; // Ԥ��ֵ����ʵֵ�Ĳ�ࣨԽСԽ�ã�
	private Double precision; // ��׼�ʣ�׼ȷ�ʣ�
	private Double recall; // ��ȫ�ʣ��ٻ��ʣ�

	private Double f1; // f1ֵ

	public TasteEvalScore() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TasteEvalScore(Double difference, Double precision, Double recall) {
		super();
		this.difference = difference;
		this.precision = precision;
		this.recall = recall;
	}

	public TasteEvalScore(Double difference, Double precision, Double recall,
			Double f1) {
		super();
		this.difference = difference;
		this.precision = precision;
		this.recall = recall;
		this.f1 = f1;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public Double getPrecision() {
		return precision;
	}

	public void setPrecision(Double precision) {
		this.precision = precision;
	}

	public Double getRecall() {
		return recall;
	}

	public void setRecall(Double recall) {
		this.recall = recall;
	}

	public Double getF1() {
		return f1;
	}

	public void setF1(Double f1) {
		this.f1 = f1;
	}

	@Override
	public String toString() {
		return "TasteEvalScore [difference=" + difference + ", precision="
				+ precision + ", recall=" + recall + ", f1=" + f1 + "]";
	}

	/**
	 * ���ݲ�ͬ��Ȩ���趨����f1ֵ
	 * 
	 * @param b
	 */
	public void calculateF1Value(double b) {
		double value = (1 + b * b)
				* (this.precision * this.recall / ((b * b) * this.precision + this.recall));
		setF1(value);
	}

}
