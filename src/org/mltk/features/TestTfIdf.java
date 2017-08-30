package TIDF;

import java.io.*;
import java.util.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * �ִ�-TFIDF-��Ϣ����
 * @author LJ
 * 
 * @datetime 2015-6-15 
 */
public class TestTfIdf {
	public static final String stopWordTable = "C:/Users/zzw/Desktop/sc_ot-tingyongzhongwen_hc/stopWordTable.txt"; // ����ͣ�ôʿ�

	private static ArrayList<String> FileList = new ArrayList<String>(); // �ļ��б�

	// �ݹ��ȡ��·�����ļ������ļ��б�
	public static List<String> readDirs(String filepath)
			throws FileNotFoundException, IOException {
		try {
			File file = new File(filepath);
			if (!file.isDirectory()) {
				System.out.println("�����[]");
				System.out.println("filepath:" + file.getAbsolutePath());
			} else {
				String[] flist = file.list();
				for (int i = 0; i < flist.length; i++) {
					File newfile = new File(filepath + "\\" + flist[i]);
					if (!newfile.isDirectory()) {
						FileList.add(newfile.getAbsolutePath());
					} else if (newfile.isDirectory()) {
						readDirs(filepath + "\\" + flist[i]);
					}
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return FileList;
	}

	// �����ļ�
	public static String readFile(String file) throws FileNotFoundException,
			IOException {
		StringBuffer strSb = new StringBuffer();
		InputStreamReader inStrR = new InputStreamReader(new FileInputStream(
				file), "gbk");
		BufferedReader br = new BufferedReader(inStrR);
		String line = br.readLine();
		while (line != null) {
			strSb.append(line).append("\r\n");
			line = br.readLine();
		}

		return strSb.toString();
	}

	// �ִʴ���
	public static ArrayList<String> cutWords(String file) throws IOException {

		ArrayList<String> fenci = new ArrayList<String>();
		ArrayList<String> words = new ArrayList<String>();
		String text = TestTfIdf.readFile(file);

		IKAnalyzer analyzer = new IKAnalyzer();
		fenci = analyzer.split(text); // �ִʴ���
		BufferedReader StopWordFileBr = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(
						stopWordTable))));
		// �������ͣ�ôʵļ���
		Set<String> stopWordSet = new HashSet<String>();
		// ���绯ͣ�ôʼ�
		String stopWord = null;
		for (; (stopWord = StopWordFileBr.readLine()) != null;) {
			stopWordSet.add(stopWord);
		}
		for (String word : fenci) {
			if (stopWordSet.contains(word)) {
				continue;
			}
			words.add(word);
		}
		System.out.println(words);
		return words;
	}

	// ͳ��һ���ļ���ÿ���ʳ��ֵĴ���
	public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords) {
		HashMap<String, Integer> resTF = new HashMap<String, Integer>();

		for (String word : cutwords) {
			if (resTF.get(word) == null) {
				resTF.put(word, 1);
				System.out.println(word);
			} else {
				resTF.put(word, resTF.get(word) + 1);
				System.out.println(word.toString());
			}
		}
		System.out.println(resTF);
		return resTF;
	}

	// ����һ���ļ�ÿ����tfֵ
	@SuppressWarnings("unchecked")
	public static HashMap<String, Float> tf(ArrayList<String> cutwords) {
		HashMap<String, Float> resTF = new HashMap<String, Float>();

		int wordLen = cutwords.size();
		HashMap<String, Integer> intTF = TestTfIdf.normalTF(cutwords);

		Iterator iter = intTF.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			resTF.put(entry.getKey().toString(), Float.parseFloat(entry
					.getValue().toString())
					/ wordLen);
			System.out.println(entry.getKey().toString() + " = "
					+ Float.parseFloat(entry.getValue().toString()) / wordLen);
		}
		return resTF;
	}

	// tf times for file ��������������
	public static HashMap<String, HashMap<String, Integer>> normalTFAllFiles(
			String dirc) throws IOException {
		FileList.clear();
		HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String, Integer>>();

		List<String> filelist = TestTfIdf.readDirs(dirc);
		for (String file : filelist) {
			HashMap<String, Integer> dict = new HashMap<String, Integer>();
			ArrayList<String> cutwords = TestTfIdf.cutWords(file);
			dict = TestTfIdf.normalTF(cutwords);
			allNormalTF.put(file, dict);
		}
		return allNormalTF;
	}

	// ���������ļ�tfֵ
	public static HashMap<String, HashMap<String, Float>> tfAllFiles(String dirc)
			throws IOException {
		FileList.clear();
		HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
		List<String> filelist = TestTfIdf.readDirs(dirc);

		for (String file : filelist) {
			HashMap<String, Float> dict = new HashMap<String, Float>();
			ArrayList<String> cutwords = TestTfIdf.cutWords(file);
			dict = TestTfIdf.tf(cutwords);
			allTF.put(file, dict);
		}
		return allTF;
	}

	// �����Ŀ¼�����дʵ�idf
	@SuppressWarnings("unchecked")
	public static HashMap<String, Float> idf(
			HashMap<String, HashMap<String, Float>> all_tf, String file)
			throws IOException {
		FileList.clear();
		HashMap<String, Float> resIdf = new HashMap<String, Float>();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		int docNum = readDirs(file).size();
		for (int i = 0; i < docNum; i++) {
			HashMap<String, Float> temp = all_tf.get(FileList.get(i));
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}
			}
		}
		// �����ļ���¼���дʺͰ����ôʵ��ļ���
		StringBuilder sb1 = new StringBuilder();
		Iterator iter1 = dict.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entry = (Map.Entry) iter1.next();
			if (entry.getKey().toString() != null) {
				sb1.append(entry.getKey().toString() + " "
						+ dict.get(entry.getKey()) + "\r\n");
			}
		}
		File filewriter = new File("E:/allCount.txt");
		FileWriter fw = new FileWriter(filewriter.getAbsoluteFile());
		BufferedWriter bb = new BufferedWriter(fw);
		bb.write(sb1.toString());
		bb.close();
		System.out.println(dict);
		// ����idf
		System.out.println("IDF for every word is:");
		Iterator iter_dict = dict.entrySet().iterator();
		while (iter_dict.hasNext()) {
			Map.Entry entry = (Map.Entry) iter_dict.next();
			float value = (float) Math.log(docNum
					/ Float.parseFloat(entry.getValue().toString()));
			resIdf.put(entry.getKey().toString(), value);
			System.out.println(entry.getKey().toString() + " = " + value);
		}
		return resIdf;
	}

	// ���ظ�Ŀ¼�����д��Լ������ʵ��ļ���
	@SuppressWarnings("unchecked")
	public static HashMap<String, Integer> idf_dict(
			HashMap<String, HashMap<String, Float>> all_tf, String file)
			throws IOException {
		FileList.clear();
		HashMap<String, Integer> dict = new HashMap<String, Integer>();
		List<String> filelist = readDirs(file);
		int docNum = filelist.size();

		for (int i = 0; i < docNum; i++) {
			HashMap<String, Float> temp = all_tf.get(filelist.get(i));
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				if (dict.get(word) == null) {
					dict.put(word, 1);
				} else {
					dict.put(word, dict.get(word) + 1);
				}
			}
		}
		System.out.println(dict);
		return dict;
	}

	// ����TFIDFֵ
	@SuppressWarnings("unchecked")
	public static void tf_idf(HashMap<String, HashMap<String, Float>> all_tf,
			HashMap<String, Float> idfs, String file) throws IOException {
		HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();
		FileList.clear();
		int docNum = readDirs(file).size();
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> tfidf = new HashMap<String, Float>();
			HashMap<String, Float> temp = all_tf.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				Float value = (float) Float.parseFloat(entry.getValue()
						.toString())
						* idfs.get(word);
				tfidf.put(word, value);
			}
			resTfIdf.put(filepath, tfidf);
		}
		System.out.println("TF-IDF for Every file is :");
		DisTfIdf(resTfIdf); // ��ʾTFIDF
	}

	// ���ؼ����TFIDFֵ
	@SuppressWarnings("unchecked")
	public static HashMap<String, HashMap<String, Float>> tf_idf_return(
			HashMap<String, HashMap<String, Float>> all_tf,
			HashMap<String, Float> idfs, String file) throws IOException {
		FileList.clear();
		HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();
		int docNum = readDirs(file).size();
		for (int i = 0; i < docNum; i++) {
			@SuppressWarnings("unused")
			HashMap<String, Float> tfidf_reduce = new HashMap<String, Float>();
			String filepath = FileList.get(i);
			HashMap<String, Float> tfidf = new HashMap<String, Float>();
			HashMap<String, Float> temp = all_tf.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String word = entry.getKey().toString();
				Float value = (float) Float.parseFloat(entry.getValue()
						.toString())
						* idfs.get(word);
				tfidf.put(word, value);

			}
			resTfIdf.put(filepath, tfidf);
		}
		return resTfIdf;
	}

	// TFIDF��ʾ��� �������ļ��洢����Ϣ
	@SuppressWarnings("unchecked")
	public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf)
			throws IOException {
		StringBuilder stall = new StringBuilder();
		Iterator iter1 = tfidf.entrySet().iterator();
		while (iter1.hasNext()) {
			Map.Entry entrys = (Map.Entry) iter1.next();
			System.out.println("FileName: " + entrys.getKey().toString());
			System.out.print("{");
			HashMap<String, Float> temp = (HashMap<String, Float>) entrys
					.getValue();
			Iterator iter2 = temp.entrySet().iterator();
			while (iter2.hasNext()) {
				Map.Entry entry = (Map.Entry) iter2.next();
				System.out.print(entry.getKey().toString() + " = "
						+ entry.getValue().toString() + ", ");
				stall.append(entrys.getKey().toString() + " "
						+ entry.getKey().toString() + " "
						+ entry.getValue().toString() + "\r\n");
			}
			System.out.println("}");
		}
		File filewriter = new File("E:/allTFIDF.txt");
		FileWriter fw = new FileWriter(filewriter.getAbsoluteFile());
		BufferedWriter bz = new BufferedWriter(fw);
		bz.write(stall.toString());
		bz.close();
	}

	// ��������
	public static double Entropy(double[] p, double tot) {
		double entropy = 0.0;
		for (int i = 0; i < p.length; i++) {
			if (p[i] > 0.0) {
				entropy += -p[i] / tot * Math.log(p[i] / tot) / Math.log(2.0);
			}
		}
		return entropy;
	}

	// ��Ϣ����������ά
	@SuppressWarnings("unchecked")
	private static void Total(int N,
			HashMap<String, HashMap<String, Float>> result,
			HashMap<String, Integer> idfs_dict_neg,
			HashMap<String, Integer> idfs_dict_pos, String file)
			throws IOException {
		FileList.clear();
		double[] classCnt = new double[N]; // �������
		double totalCnt = 0.0; // ���ļ���
		for (int c = 0; c < N; c++) {
			classCnt[c] = 125; // ÿ�������ļ���Ŀ
			totalCnt += classCnt[c];
		}
		int docNum = readDirs(file).size();
		int num = 0; // ��f�ı��
		int numb = 0; // ��f�ı��
		double totalEntroy = Entropy(classCnt, totalCnt); // �ܵ���
		HashMap<String, Integer> count = new HashMap<String, Integer>();// �洢�ʼ�����
		HashMap<String, Integer> countG = new HashMap<String, Integer>();// �洢������ά��word������
		HashMap<String, Double> countG1 = new HashMap<String, Double>();// �洢������ά��word������Ϣ����
		HashMap<String, Double> infogains = new HashMap<String, Double>();// �洢�ʺ͸ôʵ���Ϣ����
		StringBuilder st = new StringBuilder();// �����ļ���,��,��Ϣ����,TFIDF
		StringBuilder ss = new StringBuilder();// ����δ������������,���ʵı��,���ʵ�TFIDFֵ
		StringBuilder sr = new StringBuilder();// ���澭���������������,���ʵı��,���ʵ�TFIDFֵ
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> temp = result.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			if (filepath.contains("dubo")) {
				ss.append(1 + "  "); // ���Ĳ��ඨ��Ϊ���1
			} else if (filepath.contains("fangdong")) {
				ss.append(2 + "  "); // �������ඨ��Ϊ���2
			}
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				String f = entry.getKey().toString();
				double[] featureCntWithF = new double[N]; // ������F�ķֲ������1,2�ֱ�����ôʵ��ļ�����
				double[] featureCntWithoutF = new double[N]; // ��������F�ķֲ�
				double totalCntWithF = 0.0; // ��������а�����F���ļ���
				double totalCntWithoutF = 0.0; // ��������в�������F���ļ���
				for (int c = 0; c < N; c++) {
					Iterator iter_dict = null;
					switch (c) {
					case 0:
						iter_dict = idfs_dict_neg.entrySet().iterator();
						break;
					case 1:
						iter_dict = idfs_dict_pos.entrySet().iterator();
						break;
					}
					while (iter_dict.hasNext()) {
						Map.Entry entry_neg = (Map.Entry) iter_dict.next();
						if (f.equals(entry_neg.getKey().toString())) { // �ô��ڸ�����г���
							featureCntWithF[c] = Double.parseDouble(entry_neg
									.getValue().toString()); // ���ó��ָôʵ��ļ�����ֵ������
							break;
						} else {
							featureCntWithF[c] = 0.0;
						}
					}
					featureCntWithoutF[c] = classCnt[c] - featureCntWithF[c]; // ��������F���ļ������ڸ����������ȥ�����ôʵ��ļ���
					totalCntWithF += featureCntWithF[c];
					totalCntWithoutF += featureCntWithoutF[c];
				}
				double entropyWithF = Entropy(featureCntWithF, totalCntWithF);
				double entropyWithoutF = Entropy(featureCntWithoutF,
						totalCntWithoutF);
				double wf = totalCntWithF / totalCnt;
				double infoGain = totalEntroy - wf * entropyWithF - (1.0 - wf) // ��Ϣ����Ĺ�ʽ
						* entropyWithoutF;
				infogains.put(f, infoGain);
				st.append(filepath + " " + f + " " + "��Ϣ����" + "="
						+ infoGain // �����ʽ
						+ " " + "tfidf" + "=" + entry.getValue().toString()
						+ "\r\n");

				// }
				// ��ʽһ��ֱ������ֵѡȡ����ֵ����ʡȥ�����ٴα����Ĺ���
				// if(infogains.get(f)>0.004011587943125061){
				// ����f���
				if (count.get(f) == null) {
					num++;
					count.put(f, num);
				}
				ss.append(count.get(f) + ":" + entry.getValue() + " "); // �����ʽ
				// }
			}
			ss.append("\r\n");
		}
		File fileprepare = new File("E:/test.txt");
		FileWriter fz = new FileWriter(fileprepare.getAbsoluteFile());
		BufferedWriter bz = new BufferedWriter(fz);
		bz.write(ss.toString());
		bz.close();
		File filewriter = new File("E:/jieguo.txt");
		FileWriter fw = new FileWriter(filewriter.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(st.toString());
		bw.close();
		// ��ʽ��������Ϣ����Ӵ�С����,ѡȡǰ�ض����Ĵ�Ϊ������
		// ����Ϣ�������򣨴Ӵ�С��
		ArrayList<Map.Entry<String, Double>> infoIds = new ArrayList<Map.Entry<String, Double>>(
				infogains.entrySet());
		Collections.sort(infoIds, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1,
					Map.Entry<String, Double> o2) {
				if (o2.getValue() - o1.getValue() > 0) {
					return 1; // ��������
				} else {
					return -1;
				}
			}
		});
		// ѡȡ��Ϣ����Ϊǰ2000�Ĵ���������
		for (int c = 0; c < 2000; c++) {
			countG1.put(infoIds.get(c).getKey(), infoIds.get(c).getValue()); // �����������ݴ洢��countG1��
		}
		// �ٴα���
		for (int i = 0; i < docNum; i++) {
			String filepath = FileList.get(i);
			HashMap<String, Float> temp = result.get(filepath);
			Iterator iter = temp.entrySet().iterator();
			if (filepath.contains("dubo")) {
				sr.append(1 + "  ");
			} else if (filepath.contains("fangdong")) {
				sr.append(2 + "  ");
			}
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				// for(Iterator<Feature>
				// i=index.featureIterator();i.hasNext();){
				String f = entry.getKey().toString();
				// �жϸô���������ά�����Щ��
				if (countG1.get(f) != null) {
					// ���ôʱ��
					if (countG.get(f) == null) {
						numb++;
						countG.put(f, numb);
					}
					sr.append(countG.get(f) + ":" + entry.getValue() + " ");
				}
			}
			sr.append("\r\n");
		}
		File fileprepare1 = new File("E:/testt.txt");
		FileWriter fr = new FileWriter(fileprepare1.getAbsoluteFile());
		BufferedWriter br = new BufferedWriter(fr);
		br.write(sr.toString());
		br.close();
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String file = "C:/Users/zzw/Desktop/��Ŀ����/����/test"; // �ܵ�����·��
		String file1 = "C:/Users/zzw/Desktop/��Ŀ����/����/test/�Ĳ�"; // ��1����·��
		String file2 = "C:/Users/zzw/Desktop/��Ŀ����/����/test/����"; // ��2����·��
		HashMap<String, HashMap<String, Float>> all_tf = tfAllFiles(file);
		HashMap<String, HashMap<String, Float>> all_tf_neg = tfAllFiles(file1); // file1�ļ���tfֵ��·��
		HashMap<String, HashMap<String, Float>> all_tf_pos = tfAllFiles(file2); // file2�ļ���tfֵ��·��
		System.out.println();
		HashMap<String, Integer> idfs_dict_neg = idf_dict(all_tf_neg, file1); // ����file1�����д��Լ������ʵ��ļ���
		HashMap<String, Integer> idfs_dict_pos = idf_dict(all_tf_pos, file2); // ����file2�����д��Լ������ʵ��ļ���
		HashMap<String, Float> idfs = idf(all_tf, file);
		System.out.println();
		tf_idf(all_tf, idfs, file);
		HashMap<String, HashMap<String, Float>> result = tf_idf_return(all_tf,
				idfs, file);
		int N = 2; // ���������
		/*
		 * ��Ϣ���湫ʽ IG(T)=H(C)-H(C|T) H(C|T)=P(t)H(C|t)+P(t')H(C|t��)
		 */
		Total(N, result, idfs_dict_neg, idfs_dict_pos, file); // ����Ϣ�������������ά

	}

}