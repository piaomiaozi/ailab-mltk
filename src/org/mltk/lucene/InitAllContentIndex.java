package org.mltk.lucene;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.Analyzer;

public class InitAllContentIndex {

	private List<File> fileList;
	private String allIndexPath;

	public void InitIndexProdData(String fileDirPath, String indexPath) {
		setAllIndexPath(indexPath);

		File fileFolder = new File(fileDirPath);
		List<File> fileList = new ArrayList<File>();
		for (File f : fileFolder.listFiles()) {
			fileList.add(f);
		}
		setFileList(fileList);
	}

	/**
	 * ִ�ж��̴߳�������
	 * 
	 * @param analyzer
	 */
	public void execInitContentIndexThread(Analyzer analyzer,
			String fileDirPath, String indexPath) {

		// ��Ҫ�Ľ����ֶ�����·����Ϣ
		this.InitIndexProdData(fileDirPath, indexPath);

		// �����̳߳�
		ExecutorService exes = Executors.newFixedThreadPool(5);
		Set<Future<Boolean>> setThreads = new HashSet<Future<Boolean>>();
		for (File file : getFileList()) {
			// �����߳�����
			InitContentIndexThread initContentIndexThread = new InitContentIndexThread(
					file, getAllIndexPath(), analyzer);

			// �ύ�߳�����
			setThreads.add(exes.submit(initContentIndexThread));
		}
		// ִ�ж��߳�����
		for (Future<Boolean> future : setThreads) {

			try {
				Boolean flagSucc = future.get();

				// TODO delete print
				// System.out.println(flagSucc);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public List<File> getFileList() {
		return fileList;
	}

	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}

	public String getAllIndexPath() {
		return allIndexPath;
	}

	public void setAllIndexPath(String allIndexPath) {
		this.allIndexPath = allIndexPath;
	}

}
