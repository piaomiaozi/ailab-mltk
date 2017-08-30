package test.mltk.task.t_mcmf;

import java.util.List;

import org.junit.Test;
import org.mltk.task.t_mcmf.LDALevelModel;
import org.mltk.task.t_mcmf.model.AllLdaItemSet;
import org.mltk.task.t_mcmf.model.LdaDoc;
import org.mltk.task.t_mcmf.model.LdaGraph;
import org.mltk.task.t_mcmf.model.LdaTopic;

public class LDALevelModelTest {

	@Test
	public void testGetLevelGraph() {

		String CORPUS_PATH = ".\\file\\sentiment\\seg\\dianyingall";
		int topicNum = 200;
		int genWordNum = 100;

		LDALevelModel ldaLevelModel = new LDALevelModel(CORPUS_PATH, topicNum,
				genWordNum);

		LdaGraph ldaGraph = ldaLevelModel.getLdaLevelGraph();
		List<LdaDoc> ldaDocs = ldaGraph.allDocs;
		List<LdaTopic> ldaTopics = ldaGraph.allTopics;

		System.out.println("\n�ĵ���----------------------------");
		for (LdaDoc doc : ldaDocs) {
			System.out.println("����������Ŀ��" + doc.generateTopics.size() + " ");
		}
		System.out.println("\n�����----------------------------");
		for (LdaTopic topic : ldaTopics) {
			System.out.println("���ɴ�����Ŀ��" + topic.generateWords.size());
		}

		CORPUS_PATH = ".\\file\\sentiment\\seg\\dianziall";
		topicNum = 200;
		genWordNum = 100;

		LDALevelModel ldaLevelModel2 = new LDALevelModel(CORPUS_PATH, topicNum,
				genWordNum);

		LdaGraph ldaGraph2 = ldaLevelModel2.getLdaLevelGraph();
		List<LdaDoc> ldaDocs2 = ldaGraph.allDocs;
		List<LdaTopic> ldaTopics2 = ldaGraph.allTopics;

		System.out.println("\n�ĵ���----------------------------");
		for (LdaDoc doc : ldaDocs2) {
			System.out.println("����������Ŀ��" + doc.generateTopics.size() + " ");
		}
		System.out.println("\n�����----------------------------");
		for (LdaTopic topic : ldaTopics2) {
			System.out.println("���ɴ�����Ŀ��" + topic.generateWords.size());
		}

		System.out.println(AllLdaItemSet.wordsNum + " "
				+ AllLdaItemSet.docsTopicsNum);
	}
}
