package org.mltk.openNLP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class NameEntityTextFactory {

	/**
	 * ���뵥���ļ�
	 * 
	 * @param corpusFile
	 * @return
	 * @throws Exception
	 */
	public synchronized static String loadFileText(File corpusFile)
			throws Exception {
		String corpusStr = "";

		BufferedReader br = new BufferedReader(new FileReader(corpusFile));
		String line = null;
		while ((line = br.readLine()) != null) {
			corpusStr += line;
		}

		br.close();

		return corpusStr;
	}

	/**
	 * ����ȫ���ļ�
	 * 
	 * @param fileDirPath
	 *            �ļ����ļ���λ��
	 * @return
	 * @throws Exception
	 */
	public synchronized static String loadFileTextDir(String fileDirPath)
			throws Exception {
		String allCorpusStr = "";

		File corpusFileDir = new File(fileDirPath);

		if (!corpusFileDir.exists()) {
			System.err.println("�������Ǹ��ļ����ļ���");
			return null;
		}

		if (corpusFileDir.isFile()) {
			allCorpusStr += loadFileText(corpusFileDir);
		} else {
			for (File eachFile : corpusFileDir.listFiles()) {
				allCorpusStr += loadFileText(eachFile);
			}
		}

		return allCorpusStr;
	}

	/**
	 * ����ʿ��е�����ʵ��
	 * 
	 * @param nameListFile
	 * @return
	 * @throws Exception
	 */
	public static List<String> loadNameWords(File nameListFile)
			throws Exception {
		List<String> nameWords = new ArrayList<String>();

		if (!nameListFile.exists() || nameListFile.isDirectory()) {
			System.err.println("�������Ǹ��ļ�");
			return null;
		}

		BufferedReader br = new BufferedReader(new FileReader(nameListFile));
		String line = null;
		while ((line = br.readLine()) != null) {
			nameWords.add(line);
		}

		br.close();

		return nameWords;
	}

	/**
	 * ��ȡ����ʵ������
	 * 
	 * @param nameListFile
	 * @return
	 */
	public static String getNameType(File nameListFile) {
		String nameType = nameListFile.getName();

		return nameType.substring(0, nameType.lastIndexOf("."));
	}

	/**
	 * д�ļ�
	 * 
	 * @param filePath
	 * @param writeStr
	 * @throws Exception
	 */
	public static void writeIntoFile(String filePath, String writeStr)
			throws Exception {
		File file = new File(filePath);
		FileWriter fw = new FileWriter(file);

		fw.write(writeStr);

		fw.close();
	}

	/**
	 * ����ѵ������ע����
	 * 
	 * @param nameListFilePath
	 *            ����ʵ��ʿ��ļ�·��
	 * @param corpusfileDirPath
	 *            ԭʼ����·��
	 * @param trainDataPath
	 *            ����עѵ������·��
	 * @throws Exception
	 */
	public static String prodNameFindTrainText(String nameListFilePath,
			String corpusfileDirPath, String trainDataPath) throws Exception {
		// ����ʵ��ʿ��б�
		List<String> nameWordsList = new ArrayList<String>();
		// ����ע����
		String tagCorpusStr = null;

		tagCorpusStr = loadFileTextDir(corpusfileDirPath);

		File nameListFile = new File(nameListFilePath);
		if (!nameListFile.exists()) {
			System.err.println("����ʿ���Ϣʧ�ܣ�");
			return null;
		} else if (nameListFile.isFile()) {
			// ֻ��һ�����͵Ĵʿ����
			nameWordsList = loadNameWords(nameListFile);
			String nameType = getNameType(nameListFile);
			for (String nameWord : nameWordsList) {
				String replacement = "<START:" + nameType + "> " + nameWord
						+ " <END>";
				tagCorpusStr = tagCorpusStr.replaceAll(nameWord, replacement);
			}
		} else {
			// �ж������͵Ĵʿ����
			for (File eachNameFile : nameListFile.listFiles()) {

				// TODO �������������Ҫ���󻯴�������Ҫ��ֹ��ͬ�ʿ������ͬ�ʵ����

				nameWordsList = loadNameWords(eachNameFile);
				String nameType = getNameType(eachNameFile);
				for (String nameWord : nameWordsList) {
					// ps��[�ո�]�����ַ�������
					// TODO ��Ҫ�ӷִ�������ԭ��
					String replacement = " <START:" + nameType + "> "
							+ nameWord + " <END> ";
					tagCorpusStr = tagCorpusStr.replaceAll(
							" " + nameWord + " ", replacement);
				}
			}
		}

		// ����д���ļ�
		if (trainDataPath != null) {
			writeIntoFile(trainDataPath, tagCorpusStr);
		}

		return tagCorpusStr;
	}

}
