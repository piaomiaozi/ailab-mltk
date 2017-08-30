package org.mltk.crawler.jsoup;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.mltk.crawler.cache.CrawlSeedParam;
import org.mltk.crawler.model.ExactLinks;

public class ExecFetchSinaNewsContent {

	private Set<ExactLinks> links;
	private String folderPath;

	public ExecFetchSinaNewsContent() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ExecFetchSinaNewsContent(Set<ExactLinks> links, String folderPath) {
		super();
		this.links = links;
		this.folderPath = folderPath;
	}

	public void initSinaLinks(String seedUrl) {
		FetchLinksFromPage fetchObj = new FetchLinksFromPage();
		setLinks(fetchObj.filterUrl(seedUrl));
	}

	public void initFileFolderPath(String fileFolderPath) {
		setFolderPath(fileFolderPath);
	}

	public void execFetchSinaContentThread(String seedUrl, String fileFolderPath, int threadNum) {

		// TODO �ĳ��ֶ��������
		this.initSinaLinks(seedUrl);
		this.initFileFolderPath(fileFolderPath);

		// �����̳߳�
		ExecutorService exes = Executors.newFixedThreadPool(threadNum);
		Set<Future<Boolean>> setThreads = new java.util.HashSet<Future<Boolean>>();
		for (ExactLinks links : getLinks()) {
			// �����߳�����
			FetchPageContentThread fetchThread = new FetchPageContentThread(
					links, getFolderPath());

			// �ύ�߳�����
			setThreads.add(exes.submit(fetchThread));
		}

		// ִ�ж��߳�����
		for (Future<Boolean> future : setThreads) {

			try {
				Boolean flagSucc = future.get();

				// TODO delete print
				System.out.println(flagSucc);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Set<ExactLinks> getLinks() {
		return links;
	}

	public void setLinks(Set<ExactLinks> links) {
		this.links = links;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

}
