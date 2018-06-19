import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;
import org.apache.log4j.BasicConfigurator;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * User: hugo_<br/>
 * Date: 18/06/2018<br/>
 * Time: 18:59<br/>
 */
public class NPLTest {
    public static String text = "Much research has been conducted arguing that tipping points at which complex systems " +
            "experience phase transitions are difficult to identify. To test the existence of tipping points in financial " +
            "markets, based on the alternating offer strategic model we propose a network of bargaining agents who " +
            "mutually either cooperate or compete, where the feedback mechanism between trading and price dynamics is " +
            "driven by an external “hidden” variable R that quantifies the degree of market overpricing.Due to the feedback " +
            "mechanism, " +
            "R fluctuates and oscillates over time, and thus periods when the market is underpriced and overpriced occur " +
            "repeatedly. As the market becomes overpriced, bubbles are created that ultimately burst as the market reaches " +
            "a crash tipping point Rc. The market starts recovering from the crash as a recovery tipping point Rr is reached. " +
            "The probability that the index will drop in the next year exhibits a strong hysteresis behavior very much " +
            "alike critical transitions in other complex systems. The probability distribution function of R has a bimodal shape " +
            "characteristic of small systems near the tipping point. By examining the S&P500 index we illustrate the " +
            "applicability of the model and demonstrate that the financial data exhibit tipping points that agree with " +
            "the model. We report a cointegration between the returns of the S&P 500 index and its intrinsic value.";

    public static void main(String[] args) {

        // Just ignore
        BasicConfigurator.configure();

        // set up pipeline properties
        Properties props = new Properties();
        // set the list of annotators to run
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner,parse,depparse,coref,kbp,quote");
        // set a property for an annotator, in this case the coref annotator is being set to use the neural algorithm
        props.setProperty("coref.algorithm", "neural");
        // build pipeline
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        // create a document object
        CoreDocument document = new CoreDocument(text);
        // annnotate the document
        pipeline.annotate(document);
        // examples

        // 10th token of the document
        CoreLabel token = document.tokens().get(10);
        System.out.println("Example: token");
        System.out.println(token);
        System.out.println();

        // text of the first sentence
        String sentenceText = document.sentences().get(0).text();
        System.out.println("Example: sentence");
        System.out.println(sentenceText);
        System.out.println();

        // second sentence
        CoreSentence sentence = document.sentences().get(1);
    }
}
