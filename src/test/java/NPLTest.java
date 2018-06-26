import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.log4j.BasicConfigurator;

import java.util.ArrayList;
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

        // creates a StanfordCoreNLP object, with POS tagging, lemmatization,

        // NER, parsing, and coreference resolution

        Properties props = new Properties();

        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable

        String text = "Karma of humans is AI";

        // create an empty Annotation just with the given text

        Annotation document = new Annotation(text);

        // run all Annotators on this text

        pipeline.annotate(document);



        // these are all the sentences in this document

        List <CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        List<String> words = new ArrayList<>();

        List<String> posTags = new ArrayList<>();

        List<String> nerTags = new ArrayList<>();

        for (CoreMap sentence : sentences) {

            // traversing the words in the current sentence

            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                // this is the text of the token

                String word = token.get(CoreAnnotations.TextAnnotation.class);

                words.add(word);

                // this is the POS tag of the token

                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);

                posTags.add(pos);

                // this is the NER label of the token

                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);

                nerTags.add(ne);

            }

            // This is the syntactic parse tree of sentence

            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            System.out.println("Tree:\n"+ tree);

            // This is the dependency graph of the sentence

            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedDependenciesAnnotation.class);

            System.out.println("Dependencies\n:"+ dependencies);

        }

        System.out.println("Words: " + words.toString());

        System.out.println("posTags: " + posTags.toString());

        System.out.println("nerTags: " + nerTags.toString());

        // This is a map of the chain

        Map<Integer, CorefChain> graph = document.get(CorefCoreAnnotations.CorefChainAnnotation.class);

        System.out.println("Map of the chain:\n" + graph);

        System.out.println( "End of Processing" );
    }
}
