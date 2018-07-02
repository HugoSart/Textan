package textan.model.article.processing;

import textan.model.article.Article;
import textan.model.article.Section;
import textan.model.util.NLPUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: hugo_<br/>
 * Date: 30/06/2018<br/>
 * Time: 21:38<br/>
 */
public class Analyser {

    private final Article article;

    public Analyser(Article article) {
        this.article = article;
    }

    public String findObjective() {
        return findSentence(new String[]{"goal", "target", "aim", "this paper", "the paper"});
    }

    public String findProblem() {
        return findSentence(new String[]{"our article overcomes", "overcomes", "our article"});
    }

    public String findMethodology() {
        return findSentence(new String[]{"methodology", "interviews", "survey", "content", "analysis", "method", "approach", "technique", "process", "mode", "procedure", "way", "design", "style"});
    }

    public String findContribution() {
        return findSentence(new String[]{"contributes to", "contribute to", "contributed", "contribute", "contributing", "contributes", "contribution", "contributions"});
    }

    private String findSentence(String[] keyWords) {
        // Search in abstract
        for (String keyWord : keyWords) {
            String sentence = NLPUtils.findSentence(article.abst, keyWord);
            if (sentence != null) {
                return sentence + ".";
            }
        }

        // Search in contents
        for (Section section : article.sections) {

            for (String keyWord : keyWords) {
                String sentence = NLPUtils.findSentence(section.content, keyWord);
                if (sentence != null) {
                    return sentence + ".";
                }
            }

            for (Section subSection : section.subSections) {
                for (String keyWord : keyWords) {
                    String sentence = NLPUtils.findSentence(subSection.content, keyWord);
                    if (sentence != null) {
                        return sentence + ".";
                    }
                }
            }
        }
        return null;
    }

}
