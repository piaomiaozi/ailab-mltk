package test.mltk.lucene.seg;

import java.util.List;

import org.ansj.domain.Term;
import org.ansj.lucene3.AnsjAnalysis;
import org.ansj.recognition.NatureRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;

public class SegContentTest {

	@Test
	public void testSplitWord() {
		Analyzer a1 = new AnsjAnalysis();

		String txt = "�����к�����ɣ�Ҵ�Ժǰ��ǧ����ޱ����֦��Ҷï�������ã�ȴ��2013���10��10������(����������)����������������������29��Ԫ�ı��ꡣ�ڶ��죬��ޱ������͵�˵��ɶ������ƳǸ��ٵ�һ�����ڴ���������ĳ��58��Ԫ�ļ۸���۸���ĳ��׬��29��Ԫ����ĳ��û������ޱ�����Լ�����ͣ����������Ͱ�����������ʥ�����һ��԰�չ�˾��������88��Ԫ�ļ۸�";
		AnalyzerUtils.displayToken(txt, a1);
	}

	@Test
	public void testSplitWordRef() {

		// �ִ�
		List<Term> parse = ToAnalysis
				.parse("��ֵ����죬���Ѿ��ǿ��еĳɹ����ˣ��ܶ���֮�󣬵����ǻع���ηܶ���ʱ�⣬���еľ�����ʹ������������ഺ��ɫ�ʣ����ǳɹ����븶���Ĵ��ۣ��������ĵ��Ͽ����ɣ������Լ���ףͬѧ�ǳɹ���");
		// ���Ա�ע
		new NatureRecognition(parse).recognition();

		String splitStr = "";
		for (Term term : parse) {
			splitStr += (term + " ");
		}
		System.out.println(splitStr);
	}
}
