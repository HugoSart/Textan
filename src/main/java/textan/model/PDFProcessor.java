package textan.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * User: hugo_<br/>
 * Date: 17/06/2018<br/>
 * Time: 18:01<br/>
 */
public class PDFProcessor {

    // Main members
    private final PDDocument doc;
    private final PDDocumentInformation docInfo;
    private final PDFTextStripper docStripper;

    public PDFProcessor(PDDocument doc) throws IOException {
        this.doc = doc;
        this.docInfo = doc.getDocumentInformation();
        this.docStripper = new PDFTextStripper();
    }

    public PDFArticle toArticle() throws IOException, ParseException {

        PDFArticle article = new PDFArticle();

        String[] lines = docStripper.getText(doc).split(System.getProperty("line.separator"));

        // Parse first line
        String[] pLine = lines[0].split(",");
        article.setPublisher(pLine[0]);
        article.setVolume(Integer.valueOf(pLine[1].replace(" VOL. ", "")));
        article.setNumber(Integer.valueOf(pLine[2].replace(" NO. ", "")));
        String[] dateLine = pLine[3].replace(" ", "-").substring(1).split("-");
        Date date = new SimpleDateFormat("MMMM", Locale.US).parse(dateLine[0]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        article.setMonth(cal.get(Calendar.MONTH));
        article.setYear(Integer.valueOf(dateLine[1]));

        // Parse others
        article.setTitle(lines[1].trim());

        // Parse abstract
        String abs = docStripper.getText(doc);
        abs = abs.split("[A][b][s][t][r][a][c][t][—]")[1].split("[I][n][d][e][x][ ][T][e][r][m][s][—]")[0];
        article.setAbstract(abs);

        // Parse keywords
        String keyWords = docStripper.getText(doc);
        keyWords = keyWords.split("[I][n][d][e][x][ ][T][e][r][m][s][—]")[1].split("[I][.][ ][I][N][T][R][O][D][U][C][T][I][O][N]")[0];
        String[] keyWordsArray = keyWords.split("[,]");
        for (String s : keyWordsArray) {
            article.addKeyword(s.replace("\r\n", " ").replace("\n", " ").replace(".", "").trim());
        }

        return article;
    }


}
