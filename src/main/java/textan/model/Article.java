package textan.model;

import java.util.*;

/**
 * User: hugo_<br/>
 * Date: 18/06/2018<br/>
 * Time: 15:24<br/>
 */
public class Article {

    public String publisher;
    public int volume;
    public int number;
    public int month;
    public int year;

    public List<String> authors = new ArrayList<String>();
    public String title;
    public String subtitle;
    public String abst;
    public List<String> keywords = new ArrayList<String>();

    public List<Chapter> chapters = new ArrayList<Chapter>();
    public List<String> references = new ArrayList<String>();

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("-------------------------------------------- Article --------------------------------------------").append("\n");
        str.append("Publisher : ").append(publisher).append("\n");
        str.append("Volume    : ").append(volume).append("\n");
        str.append("Number    : ").append(number).append("\n");
        str.append("Month     : ").append(month).append("\n");
        str.append("Year      : ").append(year).append("\n");
        str.append("Title     : ").append(title).append("\n\n");
        str.append("** Keywords **\n");
        for (String s : keywords)
            str.append(s).append("\n");
        str.append("\n");
        str.append("** Abstract **\n").append(abst).append("\n\n");

        str.append("** Chapters **\n\n");
        for (Chapter chapter : chapters) {
            str.append(chapter.number).append(" ").append(chapter.title).append("\n")
            .append(chapter.content).append("\n\n");

            for (Chapter subChapter : chapter.subChapters) {
                str.append(chapter.number).append(".").append(subChapter.number).append(" ").append(subChapter.title).append("\n");
                str.append(subChapter.content).append("\n\n");
            }

        }

        return str.toString();

    }
}
