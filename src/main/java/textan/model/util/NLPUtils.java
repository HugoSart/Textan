package textan.model.util;

import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: hugo_<br/>
 * Date: 30/06/2018<br/>
 * Time: 17:46<br/>
 */
public class NLPUtils {

    public static List<String> findPersonalNames(String str) {

        CRFClassifier<CoreMap> crf = CRFClassifier.getDefaultClassifier();
        String classifiedString = crf.classifyToString(str);
        Matcher nameClassificationMatcher = Pattern.compile("((([a-zA-Z\\-'.ł]+?)/PERSON\\s)+([a-zA-Z'.ł]+?)/PERSON)").matcher(classifiedString);
        List<String> names = new ArrayList<>();
        while (nameClassificationMatcher.find())
            names.add(nameClassificationMatcher.group().replaceAll("/PERSON", ""));

        return names;

    }

    private static final Pattern END_OF_SENTENCE = Pattern.compile("\\.\\s+");
    public static String findSentence(String text, String word) {
        final String lcword = word.toLowerCase();
        return END_OF_SENTENCE.splitAsStream(text)
                .filter(s -> s.toLowerCase().contains(lcword))
                .findAny()
                .orElse(null);
    }

}
