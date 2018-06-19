package textan.model;

import java.util.*;

/**
 * User: hugo_<br/>
 * Date: 18/06/2018<br/>
 * Time: 15:24<br/>
 */
public class PDFArticle {

    private String publisher;
    private int volume;
    private int number;
    private int month;
    private int year;

    private List<String> authors = new ArrayList<String>();
    private String title;
    private String subtitle;
    private String resume;
    private List<String> keywords = new ArrayList<String>();

    private Map<String, String> sections = new HashMap<String, String>();
    private List<String> references = new ArrayList<String>();

    public void addAuthor(String name) {
        authors.add(name);
    }

    public void addKeyword(String keyword) {
        keywords.add(keyword);
    }

    public String[] getKeywords() {
        return keywords.toArray(new String[0]);
    }

    public void addSection(String sectionName, String content) {
        sections.put(sectionName, content);
    }

    public void addReference(String ref){
        references.add(ref);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getAbsctract() {
        return resume;
    }

    public void setAbstract(String resume) {
        this.resume = resume;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
