package org.mltk.task.t_mcmf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.mltk.task.t_mcmf.model.LdaGraph;

/**
 * 
 * @author superhy
 *
 */
public class T_MCMFTrSentimentModel {

	private String trainFolderPath;
	private String testFolderPath;

	public T_MCMFTrSentimentModel() {
		super();
		// TODO Auto-generated constructor stub
	}

	public T_MCMFTrSentimentModel(String trainFolderPath, String testFolderPath) {
		super();
		this.trainFolderPath = trainFolderPath;
		this.testFolderPath = testFolderPath;
	}

	/**
	 * @param networkFlowGraph
	 * @param gama
	 * @throws Exception
	 */
	public void execMinCostMaxFlow(NetworkFlowGraph networkFlowGraph,
			double gama) throws Exception {

		System.out.println("���ڽ�����������ͼ...");

		// ͳ�Ƶ����ͱ���
		int n = networkFlowGraph.getsDocPoints().size()
				+ networkFlowGraph.getsTopicPoints().size()
				+ networkFlowGraph.gettDocPoints().size()
				+ networkFlowGraph.gettTopicPoints().size();
		int m = networkFlowGraph.getCrossDomainDiffSpace().getKlArcs().size()
				+ networkFlowGraph.getCrossDomainInfSpace().getMiArcs().size()
				+ networkFlowGraph.getTopicHierarchicalSpace().getDtArcs()
						.size()
				+ networkFlowGraph.getTopicHierarchicalSpace().getTdArcs()
						.size();

		CrossDomainDiffSpace crossDomainDiffSpace = networkFlowGraph
				.getCrossDomainDiffSpace();
		CrossDomainInfSpace crossDomainInfSpace = networkFlowGraph
				.getCrossDomainInfSpace();
		TopicHierarchicalSpace topicHierarchicalSpace = networkFlowGraph
				.getTopicHierarchicalSpace();
		List<Integer> sDocPoints = networkFlowGraph.getsDocPoints();
		List<Integer> sTopicPoints = networkFlowGraph.getsTopicPoints();
		List<Integer> tDocPoints = networkFlowGraph.gettDocPoints();
		List<Integer> tTopicPoints = networkFlowGraph.gettTopicPoints();

		// ��¼������ģ����Ϣ
		File NetWorkModelFile = new File(
				".\\file\\sentiment\\model\\network");
		if (!NetWorkModelFile.exists()) {
			NetWorkModelFile.createNewFile();
		}
		BufferedWriter bwTest = new BufferedWriter(new FileWriter(
				NetWorkModelFile));
		String NetWorkModelText = "";

		NetWorkModelText += "sDocPoints: ";
		for (Integer sDocPoint : sDocPoints) {
			NetWorkModelText += (sDocPoint + " ");
		}
		NetWorkModelText += "\r\n";
		NetWorkModelText += "sTopicPoints: ";
		for (Integer sTopicPoint : sTopicPoints) {
			NetWorkModelText += (sTopicPoint + " ");
		}
		NetWorkModelText += "\r\n";
		NetWorkModelText += "tTopicPoints: ";
		for (Integer tTopicPoint : tTopicPoints) {
			NetWorkModelText += (tTopicPoint + " ");
		}
		NetWorkModelText += "\r\n";
		NetWorkModelText += "tDocPoints: ";
		for (Integer tDocPoint : tDocPoints) {
			NetWorkModelText += (tDocPoint + " ");
		}
		NetWorkModelText += "\r\n";

		MinCostMaxFlow minCostMaxFlow = new MinCostMaxFlow(n, m);
		int s = 0, t = n + 1;

		System.out.println("st-tt:" + crossDomainDiffSpace.getKlArcs().size());

		for (int i = 0; i < crossDomainDiffSpace.getKlArcs().size(); i++) {

			int u = crossDomainDiffSpace.getKlArcs().get(i).sTopic.topicLdaId, v = crossDomainDiffSpace
					.getKlArcs().get(i).tTopic.topicLdaId; // ��ʼ����յ�
			double volume = crossDomainInfSpace.getMiArcs().get(i).arcWeight; // ����
			double cost = crossDomainDiffSpace.getKlArcs().get(i).arcCost; // ����

			minCostMaxFlow.addEdge(u, v, volume, cost);

			NetWorkModelText += (minCostMaxFlow.edges[minCostMaxFlow.eCnt - 2] + "\r\n");
		}

		System.out
				.println("sd-st:" + topicHierarchicalSpace.getDtArcs().size());

		for (int i = 0; i < topicHierarchicalSpace.getDtArcs().size(); i++) {

			int u = topicHierarchicalSpace.getDtArcs().get(i).ldaDoc.docLdaId, v = topicHierarchicalSpace
					.getDtArcs().get(i).ldaTopic.topicLdaId; // ��ʼ����յ�
			double volume = topicHierarchicalSpace.getDtArcs().get(i).arcWeight; // ����
			double cost = 0; // ����

			minCostMaxFlow.addEdge(u, v, volume, cost);

			NetWorkModelText += (minCostMaxFlow.edges[minCostMaxFlow.eCnt - 2] + "\r\n");
		}

		System.out
				.println("tt-td:" + topicHierarchicalSpace.getTdArcs().size());

		for (int i = 0; i < topicHierarchicalSpace.getTdArcs().size(); i++) {

			int u = topicHierarchicalSpace.getTdArcs().get(i).ldaTopic.topicLdaId, v = topicHierarchicalSpace
					.getTdArcs().get(i).ldaDoc.docLdaId; // ��ʼ����յ�
			double volume = topicHierarchicalSpace.getTdArcs().get(i).arcWeight; // ����
			double cost = 0; // ����

			minCostMaxFlow.addEdge(u, v, volume, cost);

			NetWorkModelText += (minCostMaxFlow.edges[minCostMaxFlow.eCnt - 2] + "\r\n");
		}
		for (Integer sDocPoint : sDocPoints) {

			minCostMaxFlow.addEdge(s, sDocPoint, 1, 0);

			NetWorkModelText += (minCostMaxFlow.edges[minCostMaxFlow.eCnt - 2] + "\r\n");
		}
		for (Integer tDocPoint : tDocPoints) {

			minCostMaxFlow.addEdge(tDocPoint, t, 1.5, 0);

			NetWorkModelText += (minCostMaxFlow.edges[minCostMaxFlow.eCnt - 2] + "\r\n");
		}

		bwTest.write(NetWorkModelText);
		bwTest.close();

		System.out.println("����������С����������㷨...");

		// ִ����С���������
		minCostMaxFlow.exec();

		/*
		 * for (int i = 0; i < minCostMaxFlow.eCnt; i += 2) {
		 * System.out.println(minCostMaxFlow.edges[i]); }
		 */

		/*
		 * ��������������VSM��д���ļ�
		 */

		System.out.println("�ڵ�����" + sDocPoints.size() + " "
				+ sTopicPoints.size() + " " + tDocPoints.size() + " "
				+ tTopicPoints.size());
		System.out.println("��������model��Ϣ...");

		// ��¼ִ�к�������Ϣ
		File ArcFile = new File(".\\file\\sentiment\\model\\arc");
		if (!ArcFile.exists()) {
			ArcFile.createNewFile();
		}
		BufferedWriter bwArc = new BufferedWriter(new FileWriter(ArcFile));
		String arcText = "";

		for (int i = 0; i < minCostMaxFlow.eCnt; i += 2) {
			arcText += (minCostMaxFlow.edges[i].toString() + "\r\n");
		}

		bwArc.write(arcText);
		bwArc.close();

		// ����Դ����ѵ������
		List<String> trainFileLines = new ArrayList<String>();
		int trainNum = 1;
		for (Integer sDocPoint : sDocPoints) {
			String trainFileLine = "";
			for (Integer sTopicPoint : sTopicPoints) {

				double resFlow = 0;
				for (int i = 0; i < minCostMaxFlow.eCnt; i += 2) {
					if (sDocPoint.equals((Integer) minCostMaxFlow.edges[i].u)
							&& sTopicPoint
									.equals((Integer) minCostMaxFlow.edges[i].v)) {
						double flowRatio = minCostMaxFlow.edges[i].flow
								* 1.0
								/ (minCostMaxFlow.edges[i].volume + minCostMaxFlow.edges[i].flow);

						if (gama <= flowRatio) {
							resFlow = minCostMaxFlow.edges[i].flow;

							// System.out.println(minCostMaxFlow.edges[i]);

							break;
						}
					}
				}
				trainFileLine += (resFlow + " ");
			}
			// change 50
			if (trainNum <= 100) {
				trainFileLine += "label:1";
			} else {
				trainFileLine += "label:0";
			}
			trainFileLines.add(trainFileLine);
			trainNum++;
		}
		List<String> testFileLines = new ArrayList<String>();
		int testNum = 1;
		for (Integer tDocPoint : tDocPoints) {
			String testFileLine = "";
			for (Integer tTopicPoint : tTopicPoints) {

				double resFlow = 0;
				for (int i = 0; i < minCostMaxFlow.eCnt; i += 2) {
					if (tTopicPoint.equals((Integer) minCostMaxFlow.edges[i].u)
							&& tDocPoint
									.equals((Integer) minCostMaxFlow.edges[i].v)) {
						double flowRatio = minCostMaxFlow.edges[i].flow
								* 1.0
								/ (minCostMaxFlow.edges[i].volume + minCostMaxFlow.edges[i].flow);

						if (gama <= flowRatio) {
							resFlow = minCostMaxFlow.edges[i].flow;

							// System.out.println(resFlow);

							break;
						}
					}
				}
				testFileLine += (resFlow + " ");
			}
			// change 50
			/*
			 * if (testNum <= 50) { testFileLine += "label:1"; } else {
			 * testFileLine += "label:0"; }
			 */
			testFileLines.add(testFileLine);
			testNum++;
		}

		System.out.println("�����������д��model...");

		// ��ģ��д���ļ�
		String trainFileText = "";
		for (String line : trainFileLines) {
			trainFileText += (line + "\r\n");
		}
		File trainModelFile = new File(".\\file\\sentiment\\model\\train");
		if (!trainModelFile.exists()) {
			trainModelFile.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(trainModelFile));
		bw.write(trainFileText);
		bw.close();

		String testFileText = "";
		for (String line : testFileLines) {
			testFileText += (line + "\r\n");
		}
		File testModelFile = new File(".\\file\\sentiment\\model\\test");
		if (!testModelFile.exists()) {
			testModelFile.createNewFile();
		}
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(testModelFile));
		bw2.write(testFileText);
		bw2.close();
	}

	/**
	 * ִ�п�������з���ģ�ͽ���
	 * 
	 * @param topicNum
	 * @param wordNum
	 * @param gama
	 */
	public void trSentimentAnalysis(int topicNum, int genWordNum, double gama) {

		try {

			LDALevelModel sLdaLevelModel = new LDALevelModel(
					this.trainFolderPath, topicNum, genWordNum);
			LDALevelModel tLdaLevelModel = new LDALevelModel(
					this.testFolderPath, topicNum, genWordNum);
			LdaGraph sGraph = sLdaLevelModel.getLdaLevelGraph();
			LdaGraph tGraph = tLdaLevelModel.getLdaLevelGraph();
			NetworkFlowGraph networkFlowGraph = new NetworkFlowGraph(sGraph,
					tGraph);
			networkFlowGraph.initNetworkFlowGraph();
			this.execMinCostMaxFlow(networkFlowGraph, gama);

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public String getTrainFolderPath() {
		return trainFolderPath;
	}

	public void setTrainFolderPath(String trainFolderPath) {
		this.trainFolderPath = trainFolderPath;
	}

	public String getTestFolderPath() {
		return testFolderPath;
	}

	public void setTestFolderPath(String testFolderPath) {
		this.testFolderPath = testFolderPath;
	}

}
