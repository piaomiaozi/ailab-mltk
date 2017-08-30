package org.mltk.crawler.jsoup;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class JsoupGetDocument {

	public static Document getDocumentByJsoupBasic(String url) {
		try {

			// �������ӳ�ʱ�Ͷ�����ʱ
			// ���ú��Թ���ҳ��
			return Jsoup.connect(url).timeout(120000).ignoreHttpErrors(true)
					.ignoreContentType(true).get();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return null;
		}
	}
}
