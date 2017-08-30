package test.mltk.openNPL;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.ansj.lucene3.AnsjAnalysis;
import org.apache.lucene.analysis.Analyzer;
import org.junit.Test;
import org.mltk.lucene.seg.TokenSegFolderFileRecursion;
import org.mltk.openNLP.NameEntityFindTester;
import org.mltk.openNLP.NamedEntityMultiFindTrainer;

public class NamedEntityMultiFindTest {

	@Test
	public void testSegWord() {
		Analyzer ansjAnalyzer = new AnsjAnalysis();

		String oriFolderPath = ".\\file\\name_find\\train";
		String tagFolderPath = ".\\file\\name_find\\seg";

		TokenSegFolderFileRecursion tokenSegFolderFileRecursion = new TokenSegFolderFileRecursion(
				ansjAnalyzer);
		tokenSegFolderFileRecursion.execTokenSegRecursion(oriFolderPath,
				tagFolderPath);
	}

	@Test
	public void testExecFindTrainer() {
		String nameWordsPath = "." + File.separator + "file" + File.separator
				+ "name_find" + File.separator + "name_words";
		String dataPath = "." + File.separator + "file" + File.separator
				+ "name_find" + File.separator + "seg" + File.separator
				+ "train";
		String modelPath = "." + File.separator + "file" + File.separator
				+ "name_find" + File.separator + "model" + File.separator
				+ "multi_name_model.bin";

		NamedEntityMultiFindTrainer trainer = new NamedEntityMultiFindTrainer(
				nameWordsPath, dataPath, modelPath);
		boolean succFlag = trainer.execNameFindTrainer();

		System.out.println(succFlag);
	}

	@Test
	public void testExecNameFindTester() {
		String modelPath = "." + File.separator + "file" + File.separator
				+ "name_find" + File.separator + "model" + File.separator
				+ "multi_name_model.bin";
		String testFileDirPath = "." + File.separator + "file" + File.separator
				+ "name_find" + File.separator + "seg" + File.separator
				+ "test";

		NameEntityFindTester tester = new NameEntityFindTester(modelPath,
				testFileDirPath);
		Map<String, String> nameProbResMap = tester.execNameFindTester();

		for (Entry<String, String> nameProbRes : nameProbResMap.entrySet()) {
			System.out.println(nameProbRes.getKey() + " -> "
					+ nameProbRes.getValue());
		}
	}
}
