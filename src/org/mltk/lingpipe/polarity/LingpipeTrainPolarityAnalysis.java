package org.mltk.lingpipe.polarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.Files;

public class LingpipeTrainPolarityAnalysis {

	private String trainTextDir;
	private String modelDiskPath;
	private Integer nGram;

	private String[] categories;

	public LingpipeTrainPolarityAnalysis(String trainTextDir,
			String modelDiskPath, Integer nGram) {
		super();
		this.trainTextDir = trainTextDir;
		this.modelDiskPath = modelDiskPath;
		this.nGram = nGram;
	}

	/**
	 * �����ļ�ϵͳ�µ���Ŀ¼�ļ�����ʼ����������б�
	 * 
	 * @param file
	 */
	public void initCategories(File file) {
		setCategories(file.list());
	}

	/**
	 * ѵ���ı������������Է���ģ�͵�������
	 * 
	 * @return
	 */
	public Boolean trainPolarityAnalysisModel() {
		// �������ϼ��ļ���
		File pDir = new File(this.trainTextDir);
		// ��ʼ����������б�
		this.initCategories(pDir);

		try {
			// TODO delete print
			System.out.println("��ʼ���ɷ�����");

			// ������̬Logistic�ع��������ģ��
			DynamicLMClassifier<NGramProcessLM> polarityClassifier = DynamicLMClassifier
					.createNGramProcess(this.categories, this.nGram);

			// ɨ�輫�Է���ѵ����ѵ�����Է�����
			for (int i = 0; i < this.categories.length; ++i) {
				String category = this.categories[i];
				// �½����
				Classification classification = new Classification(category);
				File dir = new File(pDir, this.categories[i]);
				File[] trainFiles = dir.listFiles();
				for (int j = 0; j < trainFiles.length; ++j) {
					File trainFile = trainFiles[j];

					// TODO delete print
					System.out.println("����ѵ����" + trainFile.getName());

					String review = Files.readFromFile(trainFile, "UTF-8");
					// ָ�����ݺ����
					Classified classified = new Classified(review,
							classification);
					// ѵ�����Է�����
					polarityClassifier.handle(classified);
				}
			}

			// TODO delete print
			System.out.println("���ڽ�������д�������ϣ����Ե�...");

			// �ѷ�����ģ��д���ļ���
			File modelFile = new File(this.modelDiskPath);
			// ���Ŀ¼��û�ж�Ӧ�ļ�������֮
			if (!modelFile.exists()) {
				modelFile.createNewFile();
			}
			ObjectOutputStream os = new ObjectOutputStream(
					new FileOutputStream(modelFile));
			polarityClassifier.compileTo(os);

			os.close();

			// TODO delete print
			System.out.println("�������������");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}

		return true;
	}

	public String getTrainTextDir() {
		return trainTextDir;
	}

	public void setTrainTextDir(String trainTextDir) {
		this.trainTextDir = trainTextDir;
	}

	public String getModelDiskPath() {
		return modelDiskPath;
	}

	public void setModelDiskPath(String modelDiskPath) {
		this.modelDiskPath = modelDiskPath;
	}

	public Integer getnGram() {
		return nGram;
	}

	public void setnGram(Integer nGram) {
		this.nGram = nGram;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

}
