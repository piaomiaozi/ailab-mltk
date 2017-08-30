package org.mltk.task.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author superhy
 * 
 */
public class FileDocumentIOManagement {

	/**
	 * ͨ���ļ���ַ��ȡ�ļ���
	 * 
	 * @param textFilePath
	 * @return
	 */
	public static String getTextFileName(String textFilePath) {
		File file = new File(textFilePath);

		return file.getName();
	}

	/**
	 * ����һ���ļ����������ļ����ļ���
	 * 
	 * @param folderPath
	 * @return
	 */
	public static List<String> getTextFileNameInFolder(String folderPath) {
		// ׼�����ص�����
		List<String> fileNameList = new ArrayList<String>();

		File folder = new File(folderPath);

		// ���·�����ļ������ؿ�ֵ����������
		if (folder.isFile()) {
			return null;
		}

		for (File file : folder.listFiles()) {
			fileNameList.add(file.getName());
		}

		return fileNameList;
	}

	/**
	 * ���Ƶ����ļ�����һ���ļ���
	 * 
	 * @param sourceFilePath
	 * @param targetFolderPath
	 * @return
	 */
	public static boolean transFileToOtherFolder(String sourceFilePath,
			String targetFolderPath) {

		FileInputStream input;
		FileOutputStream output;

		try {
			input = new FileInputStream(sourceFilePath);

			String pathSeparator = File.separator;
			if (sourceFilePath.contains("/")) {
				pathSeparator = "/";
			} else if (sourceFilePath.contains("\\")) {
				pathSeparator = "\\";
			} else if (sourceFilePath.contains(File.separator)) {
				pathSeparator = File.separator;
			}

			String newFilePath = targetFolderPath
					+ "\\"
					+ sourceFilePath.substring(sourceFilePath
							.lastIndexOf(pathSeparator)
							+ pathSeparator.length());
			File file = new File(newFilePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			output = new FileOutputStream(newFilePath);
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = input.read(b)) != -1) {
				output.write(b, 0, len);
			}
			output.flush();
			output.close();
			input.close();

			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * ɾ��ĳ��·���µĵ�����һ���ļ�
	 * 
	 * @param filePath
	 */
	public static boolean delFile(String filePath) {
		try {
			File file = new File(filePath);
			// ·��Ϊ�ļ��Ҳ�Ϊ�������ɾ��
			if (file.isFile() && file.exists()) {
				file.delete();
			} else {
				System.err.println("·��ָ����ļ�����Ϊ�ջ���·������ָ��һ���ļ������飡");

				return false;
			}

			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * ɾ���ļ��� param�����ã�; folderPath �ļ�����������·��
	 * 
	 * @param folderPath
	 */
	public static boolean delFolder(String folderPath) {
		try {
			delAllFileInFolder(folderPath); // ɾ����������������
			String filePath = folderPath;
			filePath = filePath.toString();

			File myFilePath = new File(filePath);

			myFilePath.delete(); // ɾ�����ļ���

			return true;
		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * ɾ��ָ���ļ����������ļ� param; path �ļ�����������·��
	 * 
	 * @param folderPath
	 * @return
	 */
	public static boolean delAllFileInFolder(String folderPath) {

		// ������ļ��Ƿ����
		boolean flag = false;

		File file = new File(folderPath);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}

		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (folderPath.endsWith(File.separator)) {
				temp = new File(folderPath + tempList[i]);
			} else {
				temp = new File(folderPath + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFileInFolder(folderPath + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
				delFolder(folderPath + "/" + tempList[i]);// ��ɾ�����ļ���

				// ɾ���ɹ�
				flag = true;
			}
		}
		return flag;
	}
}
