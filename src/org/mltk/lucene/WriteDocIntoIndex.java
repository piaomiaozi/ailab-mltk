package org.mltk.lucene;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.mltk.lucene.model.IndexDoc;

public class WriteDocIntoIndex {

	/**
	 * ��ʼ������Ŀ¼
	 * 
	 * @param indexPath
	 */
	public synchronized static Directory loadDirectory(String indexPath) {

		Directory directory = null;

		try {

			// ����������Ӳ�̵���
			directory = FSDirectory.open(new File(indexPath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return directory;
	}

	/**
	 * ֱ�Ӵ����ַ�������
	 * 
	 * @param textName
	 * @param textContent
	 * @param indexPath
	 * @param analyzer
	 * @return
	 */
	public synchronized static IndexDoc writeTextIntoIndex(String textName,
			String textContent, String indexPath, Analyzer analyzer) {

		Directory directory = loadDirectory(indexPath);

		IndexDoc indexDoc = new IndexDoc();

		IndexWriter writer = null;

		try {

			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,
					analyzer);
			writer = new IndexWriter(directory, iwc);

			Document doc = new Document();

			// TODO delete print
			System.out.println("���ڴ���������" + textName);

			doc.add(new Field("textName", textName, Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("textContent", textContent, Field.Store.NO,
					Field.Index.ANALYZED));

			indexDoc.setTextName(textName);
			indexDoc.setTextContent(textContent);

			writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// ע��ر�����д��������������д��
				if (writer != null) {
					writer.close();
				}
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return indexDoc;
	}

	/**
	 * �����ļ��Ķ���
	 * 
	 * @param textName
	 * @param fileReader
	 * @param indexPath
	 * @param analyzer
	 * @return
	 */
	public synchronized static IndexDoc writeTextIntoIndex(String textName,
			Reader fileReader, String indexPath, Analyzer analyzer) {

		Directory directory = loadDirectory(indexPath);

		IndexDoc indexDoc = new IndexDoc();

		IndexWriter writer = null;

		try {

			IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_35,
					analyzer);
			writer = new IndexWriter(directory, iwc);

			Document doc = new Document();

			// TODO delete print
			System.out.println("���ڴ���������" + textName);

			doc.add(new Field("textName", textName, Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field("textContent", fileReader));

			indexDoc.setTextName(textName);
			indexDoc.setTextContent("�ļ���ȡ");

			writer.addDocument(doc);
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LockObtainFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				// ע��ر�����д��������������д��
				if (writer != null) {
					writer.close();
				}
			} catch (CorruptIndexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return indexDoc;
	}
}
