package textan.model;

import java.util.ArrayList;
import java.util.List;

/**
 * User: hugo_<br/>
 * Date: 25/06/2018<br/>
 * Time: 18:27<br/>
 */
public class Chapter {

    public Chapter(int number, String title, String content) {
        this.number = number;
        this.title = title;
        this.content = content;
    }

    public int number;
    public String title;
    public String content;
    public List<Chapter> subChapters = new ArrayList<Chapter>();

}
