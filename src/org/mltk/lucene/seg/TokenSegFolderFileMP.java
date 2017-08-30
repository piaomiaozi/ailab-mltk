package org.mltk.lucene.seg;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.lucene.analysis.Analyzer;

/**
 * ���̷ִ߳ʣ������޶�ֻ���ǵ����ļ����е������ļ��������ļ��д��ģ�ļ����������
 * 
 * @author superhy
 *
 */
public class TokenSegFolderFileMP {

	private Analyzer a;

	public TokenSegFolderFileMP(Analyzer a) {
		super();
		this.a = a;
	}

	public void execTokenSegThread(String oriFolderPath, String tagFolderPath) {

		// Ԥ����
		File oriFolder = new File(oriFolderPath);
		if (!oriFolder.exists()) {
			System.err.println("Դ�ļ��в����ڣ�");
			return;
		}
		File[] oriFolderFiles = oriFolder.listFiles();
		File tagFolder = new File(tagFolderPath);
		if (!tagFolder.exists()) {
			tagFolder.mkdirs();
		}

		// �����̳߳�
		ExecutorService exes = Executors.newCachedThreadPool();
		Set<Future<Boolean>> setThreads = new HashSet<Future<Boolean>>();

		for (File oriFile : oriFolderFiles) {
			// �����ֳ�����
			TokenSegFolderFileThread tokenSegFolderFileThread = new TokenSegFolderFileThread(
					oriFile, tagFolderPath, this.a);

			// �ύ�ֳ�����
			setThreads.add(exes.submit(tokenSegFolderFileThread));
		}

		// ִ�ж��߳�����
		for (Future<Boolean> future : setThreads) {

			try {
				Boolean runFlag = future.get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
