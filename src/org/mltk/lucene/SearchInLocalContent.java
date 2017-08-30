package org.mltk.lucene;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchInLocalContent {

	private Directory directory;
	private IndexReader reader;

	// ѡ���Ƿ����ѯ
	private Boolean flagAndSearch = false;

	/**
	 * �����ڴ��е�����
	 * 
	 * @param directory
	 */
	public SearchInLocalContent(Directory directory) {
		super();
		this.directory = directory;
	}

	public SearchInLocalContent(Directory directory, Boolean flagAndSearch) {
		super();
		this.directory = directory;
		this.flagAndSearch = flagAndSearch;
	}

	/**
	 * ��������ϵ�����
	 * 
	 * @param indexPath
	 */
	public SearchInLocalContent(String indexPath) {
		super();
		try {

			// ����������Ӳ�̵���
			directory = FSDirectory.open(new File(indexPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public SearchInLocalContent(String indexPath, Boolean flagAndSearch) {
		super();
		try {

			// ����������Ӳ�̵���
			directory = FSDirectory.open(new File(indexPath));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.flagAndSearch = flagAndSearch;
	}

	// ��ȡ��ѯ��
	public IndexSearcher getSearcher() {
		try {

			if (this.reader == null) {
				this.reader = IndexReader.open(this.directory);
			} else {
				IndexReader tr = IndexReader.openIfChanged(this.reader);
				if (tr != null) {
					this.reader.close();
					this.reader = tr;
				}
			}

			return new IndexSearcher(this.reader);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ģ��������ѯ,fieldΪ��ѯ����ֵ,valueΪ��ѯ�Ĺؼ���,numΪ��ѯ������
	 * 
	 * @param field
	 * @param value
	 * @param num
	 * @return
	 */
	public List<Map<String, Object>> phraseQuerySearcher(String value,
			Integer num, Analyzer analyzer) {

		// ׼����ѯ���ؽ��
		List<Map<String, Object>> searchResults = new ArrayList<Map<String, Object>>();

		try {

			// ����IndexReader����IndexSeacher
			IndexSearcher searcher = this.getSearcher();

			// ����������Query
			// ����parser��ȷ���������ݣ��ڶ���������ʾ�����������һ��������ʾ��ʹ�õķִ���
			QueryParser parser = new QueryParser(Version.LUCENE_35,
					"textContent", analyzer);
			// �趨��������Ϊ"��"����
			if (this.flagAndSearch == true) {
				parser.setDefaultOperator(QueryParser.AND_OPERATOR);
			} else {
				parser.setDefaultOperator(QueryParser.OR_OPERATOR);
			}
			// ����query��ʾ������Ϊcontent�����ƶ����ĵ���ʹ�ö����ѯ
			Query query = parser.parse(value);

			// TODO delete print
			System.out.println(query.toString());

			// ����seacher�������ҷ���TopDocs
			TopDocs tds = searcher.search(query, num);

			// ����TopDocs��ȡScoreDoc����
			ScoreDoc[] sds = tds.scoreDocs;
			for (ScoreDoc sd : sds) {

				// ����seacher��ScoreDoc�����ȡ�����Documnet����
				Document d = searcher.doc(sd.doc);

				// TODO delete print
				// ����Documnet�����ȡ��Ҫ��ֵ
				// System.out.println(d.get("collectionName") + "["
				// + d.get("postUrlMD5") + "]");

				// װ�ز�ѯ���ؽ��
				Map<String, Object> searchResultMap = new HashMap<String, Object>();
				searchResultMap.put("textName", d.get("textName"));
				searchResults.add(searchResultMap);
			}

			// �ر�searcher
			searcher.close();
			// �ر�reader
			this.reader.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return searchResults;
	}

	public Directory getDirectory() {
		return directory;
	}

	public void setDirectory(Directory directory) {
		this.directory = directory;
	}

	public IndexReader getReader() {
		return reader;
	}

	public void setReader(IndexReader reader) {
		this.reader = reader;
	}

	public Boolean getFlagAndSearch() {
		return flagAndSearch;
	}

	public void setFlagAndSearch(Boolean flagAndSearch) {
		this.flagAndSearch = flagAndSearch;
	}

}
