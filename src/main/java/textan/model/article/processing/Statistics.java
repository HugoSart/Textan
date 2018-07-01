package textan.model.article.processing;

import textan.model.article.Article;
import textan.model.article.Section;
import textan.model.util.Counter;
import textan.model.util.Util;

/**
 * User: hugo_<br/>
 * Date: 29/06/2018<br/>
 * Time: 01:38<br/>
 */
public class Statistics {

    private Article article;
    private String fullText;
    public Counter<String> wordCounter = new Counter<>();

    public Statistics(Article article) {
        this.article = article;
        fullText = joinAllText();

        // Init word wordCounter
        String[] words = fullText.split("\\W+");
        for (String word : words)
            if (word.length() > 3 && word.matches("[a-zA-Z]+")) wordCounter.add(word.toLowerCase());

        wordCounter.map = Util.sortByValue(wordCounter.map);

    }

    private String joinAllText() {

        StringBuilder text = new StringBuilder();
        for (Section section : article.sections) {
            text.append(section.content);
            for (Section subSection : section.subSections)
                text.append(subSection.content);
        }

        return text.toString();

    }

    public String[] top10Terms() {

        String[] terms = new String[10];

        int i = 0;
        for (String s : wordCounter.map.keySet()) {
            terms[i] = s;
            if (++i == 10) break;
        }

        return terms;

    }

}
