package textan.model.article;

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

    public List<Section> sections = new ArrayList<Section>();
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

        str.append("** Sections **\n\n");
        for (Section section : sections) {
            str.append(section.number).append(" ").append(section.title).append("\n")
            .append(section.content).append("\n\n");

            for (Section subSection : section.subSections) {
                str.append(section.number).append(".").append(subSection.number).append(" ").append(subSection.title).append("\n");
                str.append(subSection.content).append("\n\n");
            }

        }

        return str.toString();

    }
}
