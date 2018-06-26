package textan;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLPClient;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import textan.model.Article;
import textan.model.PDFProcessor;

import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 * User: hugo_<br/>
 * Date: 17/06/2018<br/>
 * Time: 17:47<br/>
 */
public class Textan {

    public static void main(String[] args) throws IOException, PrinterException, ParseException {

        BasicConfigurator.configure();

        // Disable loggers
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }


        PDDocument doc = PDDocument.load(new File("pdf/29.pdf"));
        PDFProcessor processor = new PDFProcessor(doc);
        Article article = processor.toArticle();
        System.out.println(article);

        doc.close();


    }

}
