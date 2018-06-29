package textan.model.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * User: hugo_<br/>
 * Date: 29/06/2018<br/>
 * Time: 01:46<br/>
 */
public class Counter<T> {

    public Map<T, Integer> map = new TreeMap<>();

    public void add(T key) {
        if (map.containsKey(key)) map.replace(key, map.get(key) + 1);
        else map.put(key, 1);
    }

}
