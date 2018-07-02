package textan.controller;

import com.victorlaerte.asynctask.AsyncTask;
import fxscenario.Scenario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import textan.model.I3EArticleParser;
import textan.model.article.Article;
import textan.model.article.processing.Analyser;
import textan.model.article.processing.Exporter;
import textan.model.article.processing.Statistics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.*;

/**
 * User: hugo_<br/>
 * Date: 01/07/2018<br/>
 * Time: 18:50<br/>
 */
public class MainScenario extends Scenario {

    @FXML
    private Button btOpenFile;
    @FXML
    private Button btExport;
    @FXML
    private AnchorPane paneArticle;
    @FXML
    private AnchorPane paneLoading;
    @FXML
    private Label lbTerms;
    @FXML
    private Label lbTitle;
    @FXML
    private Label lbAuthors;
    @FXML
    private Label lbObjective;
    @FXML
    private Label lbProblem;
    @FXML
    private Label lbMethodology;
    @FXML
    private Label lbContributions;
    @FXML
    private ListView<String> lvReferences;

    public Article article = null;

    public MainScenario() {
        super("view/fxml/main_scenario.fxml");
    }

    @Override
    protected void onConfigScene(Scene scene) {
        super.onConfigScene(scene);
        scene.getStylesheets().add("view/css/Style.css");
    }

    @Override
    protected void onConfigStage(Stage stage) {
        super.onConfigStage(stage);
        stage.setTitle("IEEE - Analisador de artigos científicos");
        stage.getIcons().add(new Image("view/icons/pdf-icon.png"));
    }

    @FXML
    public void btOpenFile_onAction(ActionEvent event) throws IOException, ExecutionException, InterruptedException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Abrir arquivo");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Arquivo PDF (*.pdf)", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(getStage());

        if (file != null) {
            ArticleLoader loader = new ArticleLoader();
            loader.execute(PDDocument.load(file));
        }

    }

    @FXML
    public void btExport_onAction(ActionEvent event) throws FileNotFoundException {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Selecionar caminho");
        File selectedDirectory = directoryChooser.showDialog(getStage());

        if (selectedDirectory != null) {
            Exporter exporter = new Exporter(article);
            exporter.export(selectedDirectory.getPath() + "/" + article.title + "_POSTPROCESS.txt");
        } else {
            System.out.println("NAO FOI");
        }

    }

    public void onFileOpened() {

        Statistics statistics = new Statistics(article);
        Analyser analyser = new Analyser(article);

        // Set title
        lbTitle.setText(article.title);
        System.out.println(article.title);

        // Set authors
        StringBuilder authorsString = new StringBuilder();
        for (int i = 0; i < article.authors.size(); i++) {
            authorsString.append(article.authors.get(i));
            if (i < article.authors.size() - 1) authorsString.append(", ");
        }
        lbAuthors.setText(authorsString.toString());

        // Fill terms label
        StringBuilder builder = new StringBuilder();
        String[] terms = statistics.top10Terms();
        int i = 0;
        for (String s : terms) {
            builder.append(s);
            if (i < terms.length - 1) builder.append(", ");
            i++;
        }
        lbTerms.setWrapText(true);
        lbTerms.setText(builder.toString());

        // Fill analysis
        lbObjective.setText(analyser.findObjective());
        lbProblem.setText(analyser.findProblem());
        lbMethodology.setText(analyser.findMethodology());
        lbContributions.setText(analyser.findContribution());

        if (lbObjective.getText() == null || lbObjective.getText().isEmpty()) lbObjective.setText("Não encontrado.");
        if (lbProblem.getText() == null || lbProblem.getText().isEmpty()) lbProblem.setText("Não encontrado.");
        if (lbMethodology.getText() == null || lbMethodology.getText().isEmpty()) lbMethodology.setText("Não encontrado.");
        if (lbContributions.getText() == null || lbContributions.getText().isEmpty()) lbContributions.setText("Não encontrado.");

        // Fill references
        ObservableList<String> refList = FXCollections.observableArrayList(article.references);
        lvReferences.setItems(refList);

    }

    public class ArticleLoader extends AsyncTask<PDDocument, Article, Article> {

        @Override
        public void onPreExecute() {
            paneArticle.setVisible(false);
            paneLoading.setVisible(true);
            lbTitle.setVisible(false);
            lbAuthors.setVisible(false);
        }

        @Override
        public Article doInBackground(PDDocument... pdDocuments) {
            try {
                return new I3EArticleParser(pdDocuments[0]).toArticle();
            } catch (ParseException | IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Article art) {
            if (art != null) {
                paneArticle.setVisible(true);
                paneLoading.setVisible(false);
                btExport.setDisable(false);
                lbTitle.setVisible(true);
                lbAuthors.setVisible(true);
                article = art;
                onFileOpened();
            } else {
                paneArticle.setVisible(false);
                paneLoading.setVisible(false);
                btExport.setDisable(true);
                lbTitle.setVisible(false);
                lbAuthors.setVisible(false);
            }
        }

        @Override
        public void progressCallback(Article... articles) { }

    }

}
