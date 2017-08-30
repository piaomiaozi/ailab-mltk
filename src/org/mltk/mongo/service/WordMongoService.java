package org.mltk.mongo.service;

import org.mltk.lucene.model.Word;
import org.mltk.mongo.MongoDbBean;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class WordMongoService {

	// TODO

	private String dbName;

	public WordMongoService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WordMongoService(String dbName) {
		super();
		this.dbName = dbName;
	}

	/**
	 * ���뵥������
	 * 
	 * @param word
	 * @param collectionName
	 */
	public void insertSingleWord(Word word, String collectionName) {

		// ת����mongo���ݶ���
		DBObject wordDbs = new BasicDBObject();

		// �����ݶ������������
		wordDbs.put("wordText", word.getWordText());

		// �洢
		MongoDbBean mongoDbBean = MongoDbBean.getMongoDbBean(this.dbName);
		mongoDbBean.insert(wordDbs, collectionName);
	}

}
