package org.mltk.openNLP;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.featuregen.AggregatedFeatureGenerator;
import opennlp.tools.util.featuregen.PreviousMapFeatureGenerator;
import opennlp.tools.util.featuregen.TokenClassFeatureGenerator;
import opennlp.tools.util.featuregen.TokenFeatureGenerator;
import opennlp.tools.util.featuregen.WindowFeatureGenerator;

/**
 * ��������ʵ��ʶ��ģ��ѵ�����
 * 
 * @author ddlovehy
 *
 */
public class NamedEntityMultiFindTrainer {

	// Ĭ�ϲ���
	private int iterations = 80;
	private int cutoff = 5;
	private String langCode = "general";
	private String type = "default";

	// ���趨�Ĳ���
	private String nameWordsPath; // ����ʵ��ʿ�·��
	private String dataPath; // ѵ�����ѷִ�����·��
	private String modelPath; // ģ�ʹ洢·��

	public NamedEntityMultiFindTrainer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NamedEntityMultiFindTrainer(String nameWordsPath, String dataPath,
			String modelPath) {
		super();
		this.nameWordsPath = nameWordsPath;
		this.dataPath = dataPath;
		this.modelPath = modelPath;
	}

	public NamedEntityMultiFindTrainer(int iterations, int cutoff,
			String langCode, String type, String nameWordsPath,
			String dataPath, String modelPath) {
		super();
		this.iterations = iterations;
		this.cutoff = cutoff;
		this.langCode = langCode;
		this.type = type;
		this.nameWordsPath = nameWordsPath;
		this.dataPath = dataPath;
		this.modelPath = modelPath;
	}

	/**
	 * ���ɶ�������
	 * 
	 * @return
	 */
	public AggregatedFeatureGenerator prodFeatureGenerators() {
		AggregatedFeatureGenerator featureGenerators = new AggregatedFeatureGenerator(
				new WindowFeatureGenerator(new TokenFeatureGenerator(), 2, 2),
				new WindowFeatureGenerator(new TokenClassFeatureGenerator(), 2,
						2), new PreviousMapFeatureGenerator());

		return featureGenerators;
	}

	/**
	 * ��ģ��д�����
	 * 
	 * @param model
	 * @throws Exception
	 */
	public void writeModelIntoDisk(TokenNameFinderModel model) throws Exception {
		File outModelFile = new File(this.getModelPath());
		FileOutputStream outModelStream = new FileOutputStream(outModelFile);
		model.serialize(outModelStream);
	}

	/**
	 * ������ע��ѵ������
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getTrainCorpusDataStr() throws Exception {

		// TODO ������־û��ж�ֱ�������ע���ݵ���� �Լ�����ʽѵ��

		String trainDataStr = null;
		trainDataStr = NameEntityTextFactory.prodNameFindTrainText(
				this.getNameWordsPath(), this.getDataPath(), null);

		return trainDataStr;
	}

	/**
	 * ѵ��ģ��
	 * 
	 * @param trainDataStr
	 *            �ѱ�ע��ѵ�����������ַ���
	 * @return
	 * @throws Exception
	 */
	public TokenNameFinderModel trainNameEntitySamples(String trainDataStr)
			throws Exception {
		ObjectStream<NameSample> nameEntitySample = new NameSampleDataStream(
				new PlainTextByLineStream(new StringReader(trainDataStr)));
		
		System.out.println("**************************************");
		System.out.println(trainDataStr);

		TokenNameFinderModel nameFinderModel = NameFinderME.train(
				this.getLangCode(), this.getType(), nameEntitySample,
				this.prodFeatureGenerators(),
				Collections.<String, Object> emptyMap(), this.getIterations(),
				this.getCutoff());

		return nameFinderModel;
	}

	/**
	 * ѵ������ܵ��÷���
	 * 
	 * @return
	 */
	public boolean execNameFindTrainer() {

		try {
			String trainDataStr = this.getTrainCorpusDataStr();
			TokenNameFinderModel nameFinderModel = this
					.trainNameEntitySamples(trainDataStr);
			// System.out.println(nameFinderModel);
			this.writeModelIntoDisk(nameFinderModel);

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}
	}

	/* -------------------------getter & setter------------------------- */

	public int getIterations() {
		return iterations;
	}

	public void setIterations(int iterations) {
		this.iterations = iterations;
	}

	public int getCutoff() {
		return cutoff;
	}

	public void setCutoff(int cutoff) {
		this.cutoff = cutoff;
	}

	public String getLangCode() {
		return langCode;
	}

	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public String getNameWordsPath() {
		return nameWordsPath;
	}

	public void setNameWordsPath(String nameWordsPath) {
		this.nameWordsPath = nameWordsPath;
	}

}
