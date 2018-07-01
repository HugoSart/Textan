package textan.model.article.processing;

import textan.model.article.Article;
import textan.model.article.Section;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * User: hugo_<br/>
 * Date: 01/07/2018<br/>
 * Time: 00:46<br/>
 */
public class Exporter {

    private Article article;

    public Exporter(Article article) {
        this.article = article;
    }

    public boolean export(String path) throws FileNotFoundException {

        final String TAG_SEPARATOR = "::";
        final String REG_SEPARATOR = "&&";
        final String ITEM_SEPARATOR = ";;";


        final Statistics statistics = new Statistics(article);
        final Analyser analyser = new Analyser(article);

        StringBuilder builder = new StringBuilder();
        builder.append("title").append(TAG_SEPARATOR).append(article.title);

        // Append authors
        {
            builder.append(REG_SEPARATOR).append("authors").append(TAG_SEPARATOR);
            int i = 0;
            for (String author : article.authors) {
                builder.append(author);
                if (i < article.authors.size() - 1) builder.append(ITEM_SEPARATOR);
                i++;
            }
        }

        // Append index keys
        {
            builder.append(REG_SEPARATOR).append("index_keys").append(TAG_SEPARATOR);
            int i = 0;
            for (String keyword : article.keywords) {
                builder.append(keyword);
                if (i < article.keywords.size() - 1) builder.append(ITEM_SEPARATOR);
                i++;
            }
        }

        // Append sections
        {
            builder.append(REG_SEPARATOR).append("sections").append(TAG_SEPARATOR);
            int i = 0;
            for (Section section : article.sections) {
                builder.append(section.title);
                if (i < article.sections.size() - 1) builder.append(ITEM_SEPARATOR);
                i++;
            }
        }

        // Append references
        {
            builder.append(REG_SEPARATOR).append("references").append(TAG_SEPARATOR);
            int i = 0;
            for (String ref : article.references) {
                builder.append(ref);
                if (i < article.references.size() - 1) builder.append(ITEM_SEPARATOR);
                i++;
            }
        }

        // Append terms
        {
            builder.append(REG_SEPARATOR).append("terms").append(TAG_SEPARATOR);
            int i = 0;
            String[] terms = statistics.top10Terms();
            for (String term : terms) {
                builder.append(term);
                if (i < terms.length - 1) builder.append(ITEM_SEPARATOR);
                i++;
            }
        }

        // Append analysis
        builder.append(REG_SEPARATOR).append("objective").append(TAG_SEPARATOR).append(analyser.findObjective());
        builder.append(REG_SEPARATOR).append("problem").append(TAG_SEPARATOR).append(analyser.findProblem());
        builder.append(REG_SEPARATOR).append("methodology").append(TAG_SEPARATOR).append(analyser.findMethodology());
        builder.append(REG_SEPARATOR).append("contribution").append(TAG_SEPARATOR).append(analyser.findContribution());

        try (PrintStream out = new PrintStream(new FileOutputStream(path))) {
            out.print(builder.toString());
        } catch (Exception e) {
            return false;
        }

        return true;

    }

}
