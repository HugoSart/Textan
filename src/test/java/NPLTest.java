import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import textan.model.I3EArticleParser;
import textan.model.article.Article;
import textan.model.article.processing.Analyser;
import textan.model.article.processing.Statistics;
import textan.model.util.NLPUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;

/**
 * User: hugo_<br/>
 * Date: 18/06/2018<br/>
 * Time: 18:59<br/>
 */
public class NPLTest {

    public static final String[] testDocNames = new String[]{"13.pdf","17.pdf","19.pdf","20.pdf","21.pdf","22.pdf","23.pdf","29.pdf","31.pdf","49.pdf","48.pdf","49.pdf","50.pdf","98.pdf","118.pdf","120.pdf"};
    public static final String[] testDocNamesHalf = new String[]{"13.pdf","17.pdf","19.pdf","20.pdf","21.pdf","22.pdf","23.pdf"};

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {

        BasicConfigurator.configure();

        // Disable loggers
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }

        for (String testDocName : testDocNamesHalf) {

            // Load file
            PDDocument doc = PDDocument.load(new File("pdf/" + testDocName));
            I3EArticleParser parser = new I3EArticleParser(doc);
            Article article = parser.toArticle();
            Analyser analyser = new Analyser(article);

            System.out.println("File path: " + testDocName);
            System.out.println("Objective: " + analyser.findObjective());
            System.out.println("Problem: " + analyser.findProblem());
            System.out.println("Methodology: " + analyser.findMethodology());
            System.out.println();
        }

    }

}
