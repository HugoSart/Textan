package textan.model;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
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

    public Article toArticle() throws IOException, ParseException {

        Article article = new Article();

        String fullText =  docStripper.getText(doc);
        String[] lines = fullText.split(System.getProperty("line.separator"));

        // Parse first line
        String[] pLine = lines[0].split(",");
        article.publisher = pLine[0];
        article.volume = Integer.valueOf(pLine[1].replace(" VOL. ", ""));
        article.number = Integer.valueOf(pLine[2].replace(" NO. ", ""));
        String[] dateLine = pLine[3].replace(" ", "-").substring(1).split("-");
        Date date = new SimpleDateFormat("MMMM", Locale.US).parse(dateLine[0]);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        article.month = cal.get(Calendar.MONTH);
        article.year = Integer.valueOf(dateLine[1]);

        // Parse others
        article.title = lines[1].trim();

        // Parse abstract
        String abs = fullText;
        abs = abs.split("[A][b][s][t][r][a][c][t][—]")[1].split("[I][n][d][e][x][ ][T][e][r][m][s][—]")[0].trim();
        article.abst = abs;

        // Parse keywords
        String keyWords = fullText;
        keyWords = keyWords.split("[I][n][d][e][x][ ][T][e][r][m][s][—]")[1].split("[I][.][ ][I][N][T][R][O][D][U][C][T][I][O][N]")[0];
        String[] keyWordsArray = keyWords.split("[,]");
        for (String s : keyWordsArray)
            article.keywords.add(s.replace("\r\n", " ").replace("\n", " ").replace(".", "").trim());

        // Extract chapters
        StringBuilder chapterContent = new StringBuilder();
        String chapterTitle = "NONE";
        int chapterNumber = 0;
        int aux = chapterNumber;
        Chapter chapter = null;
        for (String line : lines) {

            if ((line.matches("(I|II|III|IV|V|VI|VII|IX|X)[.]\\s.*"))
                    && line.equals(line.toUpperCase())) {

                chapterNumber++;
                chapterTitle = line;

                chapter = new Chapter(chapterNumber, chapterTitle.split("[.]")[1].trim(), null);
                article.chapters.add(chapter);

            }

            if (chapterNumber != 0 && aux == chapterNumber && !line.equals(line.toUpperCase())) {
                chapterContent.append(line).append("\n");
                chapter.content = chapterContent.toString().trim();
            } else if (aux != chapterNumber) {
                chapterContent = new StringBuilder();
            }

            aux = chapterNumber;

        }

        // Extract sub-chapters
        for (Chapter chap : article.chapters) {

            StringBuilder subChapterContent = new StringBuilder();
            String subChapterTitle = "None";
            int subChapterNumber = 0;
            int subAux = subChapterNumber;
            Chapter subChapter = null;
            for (String s : chap.content.split("\n")) {

                if ((s.matches("[A-Z][.]\\s.*"))) {

                    subChapterNumber++;
                    subChapterTitle = s;

                    subChapter = new Chapter(subChapterNumber, subChapterTitle.split("[.]")[1].trim(), null);
                    chap.subChapters.add(subChapter);

                }

                if (subChapterNumber != 0 && subAux == subChapterNumber && !s.equals(s.toUpperCase())) {
                    subChapterContent.append(s).append("\n");
                    subChapter.content = subChapterContent.toString().trim();
                } else if (subAux != subChapterNumber) {
                    subChapterContent = new StringBuilder();
                }

                subAux = subChapterNumber;

            }
        }

        for (Chapter chap : article.chapters) {
            for (Chapter subChap : chap.subChapters) {
                chap.content = chap.content.trim().replace(subChap.content, "");
                chap.content = chap.content.replaceAll("[A-Z][.]\\s.*", "");
            }
        }

        return article;

    }

    private String removeEndLine(String str) {
        return str.replace("-\n", "").replace("\n", " ");
    }

    private String removeEmptyLines(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : str.split("\n")) {
            if (!s.trim().equals(" "))
                stringBuilder.append(s).append("\n");
        }
        return stringBuilder.toString();
    }

}
