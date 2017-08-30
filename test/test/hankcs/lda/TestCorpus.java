/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2015/1/29 17:22</create-date>
 *
 * <copyright file="TestCorpus.java" company="�Ϻ���ԭ��Ϣ�Ƽ����޹�˾">
 * Copyright (c) 2003-2014, �Ϻ���ԭ��Ϣ�Ƽ����޹�˾. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact �Ϻ���ԭ��Ϣ�Ƽ����޹�˾ to get more information.
 * </copyright>
 */
package test.hankcs.lda;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.hankcs.lda.Corpus;
import com.hankcs.lda.LdaGibbsSampler;
import com.hankcs.lda.LdaUtil;

/**
 * @author hankcs
 */
public class TestCorpus {

	@Test
	public void testAddDocument() throws Exception {
		List<String> doc1 = new ArrayList<String>();
		doc1.add("hello");
		doc1.add("word");
		List<String> doc2 = new ArrayList<String>();
		doc2.add("hankcs");
		Corpus corpus = new Corpus();
		corpus.addDocument(doc1);
		corpus.addDocument(doc2);
		System.out.println(corpus);
	}

	@Test
	public void testAll() throws Exception {
		// 1. Load corpus from disk
		Corpus corpus = Corpus.load(".\\file\\sentiment\\seg\\tushuall");
		// 2. Create a LDA sampler
		LdaGibbsSampler ldaGibbsSampler = new LdaGibbsSampler(
				corpus.getDocument(), corpus.getVocabularySize());
		// 3. Train it
		ldaGibbsSampler.gibbs(10);
		// 4. The phi matrix is a LDA model, you can use LdaUtil to explain it.
		double[][] phi = ldaGibbsSampler.getPhi();
		Map<String, Double>[] topicMap = LdaUtil.translate(phi,
				corpus.getVocabulary(), 200);
		LdaUtil.explain(topicMap);

		// 5. TODO:Predict. I'm not sure whether it works, it is not stable.
		/*int[] document = Corpus.loadDocument("data/mini/����_500.txt",
				corpus.getVocabulary());
		double[] tp = LdaGibbsSampler.inference(phi, document);
		Map<String, Double> topic = LdaUtil.translate(tp, phi,
				corpus.getVocabulary(), 25);
		LdaUtil.explain(topic);*/
	}
}
