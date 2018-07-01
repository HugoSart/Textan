package textan.model;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import textan.model.article.Article;
import textan.model.article.Section;
import textan.model.util.NLPUtils;
import textan.model.util.Util;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: hugo_<br/>
 * Date: 17/06/2018<br/>
 * Time: 18:01<br/>
 */
public class I3EArticleParser {

    // Main members
    private String docText;

    public I3EArticleParser(PDDocument doc) throws IOException {
        docText = removeTrash(new PDFTextStripper().getText(doc));
    }

    public Article toArticle() throws ParseException {

        Article article = new Article();

        String fullText = docText;
        String header = fullText.split("Abstract—")[0];
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

        // Parse title
        List<String> titleLines = new ArrayList<>();
        for (int i = 1; NLPUtils.findPersonalNames(lines[i]).size() == 0; i++)
            titleLines.add(lines[i]);
        article.title = "";
        for (String titleLine : titleLines)
            article.title += titleLine + " ";
        article.title = article.title.trim();

        // Parse authors
        for (int i = titleLines.size() + 1; i < header.split("\n").length; i++)
            article.authors.addAll(NLPUtils.findPersonalNames(lines[i]));

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

        // Parse sections
        int index = 1;
        while (true) {

            try {
                article.sections.add(findSection(index));
            } catch (NullPointerException e) {
                break;
            }

            index++;

        }

        int i = 1;
        while (true) {
            try {
                article.references.add(findReference(i));
            } catch (NullPointerException e) {
                break;
            }
            i++;
        }


        return article;

    }

    public Section findSection(int index) {

        String[] sectionStrings = splitSection(index);
        Section section = new Section(index, sectionStrings[0], "");

        int subIndex = 0;
        while (true) {

            try {

                String[] subSectionStrings = splitSubSection(sectionStrings[1], subIndex);
                if (subIndex != 0)
                    section.subSections.add(new Section(subIndex, subSectionStrings[0], subSectionStrings[1]));
                else section.content = subSectionStrings[1];

            } catch (NullPointerException e) {
                break;
            }

            subIndex++;

        }

        return section;

    }

    private String[] splitSection(int index) {

        String fullText = docText;

        // Extract section text
        final Pattern ssPattern = Pattern.compile("^" + Util.arabicToRoman(index) + "[.]\\s+(.+?)" + "^" + Util.arabicToRoman(index + 1) + "[.][ ]", Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern spPattern = Pattern.compile("^" + Util.arabicToRoman(index) + "[.]\\s+(.+?)" + "^APPENDIX", Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern saPattern = Pattern.compile("^" + Util.arabicToRoman(index) + "[.]\\s+(.+?)" + "^ACKNOWLEDGMENTS", Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern srPattern = Pattern.compile("^" + Util.arabicToRoman(index) + "[.]\\s+(.+?)" + "^REFERENCES", Pattern.DOTALL | Pattern.MULTILINE);

        final Matcher ssMatcher = ssPattern.matcher(fullText);
        final Matcher spMatcher = spPattern.matcher(fullText);
        final Matcher saMatcher = saPattern.matcher(fullText);
        final Matcher srMatcher = srPattern.matcher(fullText);

        String sectionContent = null;
        if (ssMatcher.find()) sectionContent = ssMatcher.group();
        else if (spMatcher.find()) sectionContent = spMatcher.group();
        else if (saMatcher.find()) sectionContent = saMatcher.group();
        else if (srMatcher.find()) sectionContent = srMatcher.group();

        // Remove last line
        sectionContent = Util.removeLinesFromBottom(sectionContent, 1);

        // Extract and remove title
        String[] sectionLines = sectionContent.split("\\r?\\n");
        String title = "";
        int numberOfLinesInTitle = 0;
        for (String sectionLine : sectionLines) {
            if (sectionLine.equals(sectionLine.toUpperCase())) {
                title += " " + sectionLine;
                numberOfLinesInTitle++;
            } else break;
        }

        sectionContent = Util.removeLinesFromTop(sectionContent, numberOfLinesInTitle);

        String[] ret = new String[2];
        ret[0] = title.replaceAll("^(\\s|[A-Z])+\\.\\s", "").replaceAll("\n", "");
        ret[1] = sectionContent;

        return ret;

    }

    private String[] splitSubSection(String str, int index) {

        // Extract section text
        final Pattern stsPattern = Pattern.compile("^" + Util.arabicToUpperCase(index) + "[.][ ](.+)" + "^" + Util.arabicToUpperCase(index + 1) + "[.][ ]", Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern stfPattern = Pattern.compile("^" + Util.arabicToUpperCase(index) + "[.][ ](.+)", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher stsMatcher = stsPattern.matcher(str);
        final Matcher stfMatcher = stfPattern.matcher(str);

        // Additional zero patterns
        final Pattern ztsPattern = Pattern.compile("^(.+)" + Util.arabicToUpperCase(index + 1) + "[.][ ]", Pattern.DOTALL | Pattern.MULTILINE);
        final Pattern ztfPattern = Pattern.compile("^(.+)", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher ztsMatcher = ztsPattern.matcher(str);
        final Matcher ztfMatcher = ztfPattern.matcher(str);

        // Empty pattern
        final Pattern zesPattern = Pattern.compile("\\A" + Util.arabicToUpperCase(index + 1) + "[.][ ]", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher zesMatcher = zesPattern.matcher(str);

        String subSectionContent = null;
        if (index != 0) {
            if (stsMatcher.find()) {
                subSectionContent = stsMatcher.group();
                subSectionContent = Util.removeLinesFromBottom(subSectionContent, 1);
            } else if (stfMatcher.find()) {
                subSectionContent = stfMatcher.group();
            }
        } else {
            if (zesMatcher.find()) {
                subSectionContent = "";
            } else if (ztsMatcher.find()) {
                subSectionContent = ztsMatcher.group();
                subSectionContent = Util.removeLinesFromBottom(subSectionContent, 1);
            } else if (ztfMatcher.find()) {
                subSectionContent = ztfMatcher.group();
            }
        }

        // Extract and remove title
        String title = "";
        if (index != 0) {
            title = Util.removeWordsFromTop(subSectionContent.split("\\n+")[0], 1);
            subSectionContent = Util.removeLinesFromTop(subSectionContent, 1);
        }

        String[] ret = new String[2];
        ret[0] = title;
        ret[1] = subSectionContent;

        return ret;

    }

    private String removeTrash(String str) {

        str = Util.removeAll(str, "^Manuscript\\sreceived.+Digital\\sObject\\sIdentifier\\s[0-9./A-Z]+$");
        str = Util.removeAll(str, "^[0-9]+-[0-9]+\\s©\\s[0-9]+\\sIEEE.+permission[.]$");
        str = Util.removeAll(str, "^See\\s+.+\\s+for\\s+more\\s+information[.]$");

        // TODO: remove article name trash
        // TODO: remove trash pdf 29

        str = str.replaceAll("(?m)^[ \t]*\r?\n", "");
        return str;

    }

    private String findReference(int index) {

        final Pattern refPattern = Pattern.compile("REFERENCES(.+)", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher refMatcher = refPattern.matcher(docText);

        String text = null;
        if (refMatcher.find())
            text = refMatcher.group();

        text = Util.removeLinesFromTop(text, 1);

        final Pattern patternYear = Pattern.compile("^\\[" + index + "\\](.*?)[0-9]+[.]$", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher matcherYear = patternYear.matcher(text);

        final Pattern patternOnline = Pattern.compile("^\\[" + index + "\\](.*?)Available(.+?)$", Pattern.DOTALL | Pattern.MULTILINE);
        final Matcher matcherOnline = patternOnline.matcher(text);

        String refYear = null, refOnline = null, ref = null;
        if (matcherYear.find()) refYear = matcherYear.group();
        if (matcherOnline.find()) refOnline = matcherOnline.group();

        if (refYear != null && refOnline != null) {
            ref = refYear.length() <= refOnline.length() ? refYear : refOnline;
        } else if (refYear != null) {
            ref = refYear;
        } else if (refOnline != null) {
            ref = refOnline;
        }

        ref = ref.replaceAll("\\[" + index + "\\] ", "");

        return ref;

    }

}
