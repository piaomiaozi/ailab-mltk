package org.mltk.mongo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mltk.mongo.MongoDbBean;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class ClassifierModelMongoService {

	private String dbName;

	public ClassifierModelMongoService() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ClassifierModelMongoService(String dbName) {
		super();
		this.dbName = dbName;
	}

	/**
	 * �����µļ���
	 * 
	 * @param collectionName
	 */
	public void createNewColl(String collectionName) {

		MongoDbBean mongoDbBean = MongoDbBean.getMongoDbBean(this.dbName);
		boolean createCollRes = mongoDbBean.createNewCollection(collectionName);

		if (createCollRes == true) {
			System.out
					.println("ClassifierModelMongoServiceImpl.createNewColl()"
							+ ":�����¼��ϳɹ���");
		} else {
			System.err.println("�����¼���ʧ�ܣ�");
		}
	}

	/**
	 * ���뵥������ʵ��
	 * 
	 * @param vecId
	 * @param label
	 * @param featrues
	 * @param collectionName
	 */
	public void insertSingleVector(String vecId, Integer label,
			List<String> featrues, String collectionName) {

		// ת����mongo���ݶ���
		DBObject vecDbs = new BasicDBObject();

		// �������ݶ����б��洢����
		List<DBObject> featruesDbsList = new ArrayList<DBObject>();
		for (String featrue : featrues) {
			DBObject featrueDbs = new BasicDBObject();

			featrueDbs.put("feature", featrue);
			featruesDbsList.add(featrueDbs);
		}

		// �����ݶ������������
		vecDbs.put("vecId", vecId);
		vecDbs.put("label", label);
		vecDbs.put("featrues", featruesDbsList);

		// �洢
		MongoDbBean mongoDbBean = MongoDbBean.getMongoDbBean(this.dbName);
		mongoDbBean.insert(vecDbs, collectionName);
	}

	/**
	 * ��ѯ��ģ�ͼ�����ȫ��������
	 * 
	 * @param collectionName
	 * @return
	 */
	public List<Map<String, Object>> findAllVectorsInColl(String collectionName) {

		// ָ�����ص���������
		List<Map<String, Object>> vectorsInColl = new ArrayList<Map<String, Object>>();

		MongoDbBean mongoDbBean = MongoDbBean.getMongoDbBean(this.dbName);
		for (DBObject vecDbs : mongoDbBean.findByKeyAndRef(null, null,
				collectionName)) {
			vectorsInColl.add(vecDbs.toMap());
		}

		return vectorsInColl;
	}

	/**
	 * ��ѯ��ģ�ͼ����еĵ�������
	 * 
	 * @param vecId
	 * @param collectionName
	 * @return
	 */
	public Map<String, Object> findSigVectorInCollByVecId(String vecId,
			String collectionName) {

		// ׼�����ص���������
		Map<String, Object> vectorInColl = new HashMap<String, Object>();

		MongoDbBean mongoDbBean = MongoDbBean.getMongoDbBean(this.dbName);
		// ��Ӳ�ѯ����
		DBObject ref = new BasicDBObject();
		ref.put("vecId", vecId);
		List<DBObject> findResInColl = mongoDbBean.findByKeyAndRef(ref, null,
				collectionName);

		vectorInColl = findResInColl.get(0).toMap();

		return vectorInColl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
