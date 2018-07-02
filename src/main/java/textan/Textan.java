package textan;

import fxscenario.Spawner;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import textan.controller.MainScenario;
import textan.model.I3EArticleParser;
import textan.model.article.Article;
import textan.model.article.processing.Analyser;
import textan.model.article.processing.Exporter;
import textan.model.article.processing.Statistics;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * User: hugo_<br/>
 * Date: 17/06/2018<br/>
 * Time: 17:47<br/>
 */
public class Textan extends Application {

    public static void main(String[] args) throws IOException, ParseException {

        BasicConfigurator.configure();

        // Disable loggers
        List<Logger> loggers = Collections.<Logger>list(LogManager.getCurrentLoggers());
        loggers.add(LogManager.getRootLogger());
        for (Logger logger : loggers) {
            logger.setLevel(Level.OFF);
        }

        launch(args);

        /*PDDocument doc = PDDocument.load(new File("pdf/17.pdf"));

        Article article = new I3EArticleParser(doc).toArticle();
        Statistics statistics = new Statistics(article);
        Analyser analyser = new Analyser(article);
        Exporter exporter = new Exporter(article);

        doc.close();*/


    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainScenario mainScenario = new MainScenario();
        Spawner.startScenario(mainScenario, null, primaryStage);
    }

}
