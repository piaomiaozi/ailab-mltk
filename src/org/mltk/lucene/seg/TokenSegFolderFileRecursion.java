package org.mltk.lucene.seg;

import java.io.File;

import org.apache.lucene.analysis.Analyzer;

/**
 * �ݹ�ʵ���Զ�ɨ���ļ�����ȫ�����ļ��м��ļ��ķִʷ���
 * 
 * @author superhy
 *
 */
public class TokenSegFolderFileRecursion {

	private static boolean flagFirst = true;

	private Analyzer a;

	public TokenSegFolderFileRecursion(Analyzer a) {
		super();
		this.a = a;
	}

	/**
	 * ��Ŀ���ļ����н���һ����Դ�ļ�������ͬ�����ļ���
	 * 
	 * @param oriFolderPath
	 * @param tagFolderPath
	 */
	public void execTokenSegRecursion(String oriFolderPath, String tagFolderPath) {

		File oriFolder = new File(oriFolderPath);
		if (!oriFolder.exists()) {
			System.err.println("Դ�ļ��в����ڣ�");
			return;
		}
		File[] oriFolderFiles = oriFolder.listFiles();
		File tagFilesFolder;
		if (flagFirst) {
			tagFilesFolder = new File(tagFolderPath + File.separator
					+ oriFolder.getName());
			if (oriFolder.equals(tagFilesFolder)) {
				System.err.println("Դ�ļ��к�Ŀ���ļ��в����ظ���");
				return;
			}
			if (!tagFilesFolder.exists()) {
				tagFilesFolder.mkdirs();
			}
			flagFirst = false;
		} else {
			tagFilesFolder = new File(tagFolderPath);
		}

		for (File oriF : oriFolderFiles) {
			if (oriF.isDirectory()) {
				File newFolder = new File(tagFilesFolder.getAbsolutePath()
						+ File.separator + oriF.getName());
				if (!newFolder.exists()) {
					newFolder.mkdirs();
				}
				execTokenSegRecursion(oriF.getAbsolutePath(),
						newFolder.getAbsolutePath());
			} else {
				String tagFilePath = tagFilesFolder.getAbsolutePath()
						+ File.separator + oriF.getName();
				Boolean runFlag = TokenSegUtil.tokenFileSeg(oriF, a,
						tagFilePath);

				System.out.println("�����ִ��ļ���" + tagFilePath + runFlag);
			}
		}
	}

	public Analyzer getA() {
		return a;
	}

	public void setA(Analyzer a) {
		this.a = a;
	}

}
