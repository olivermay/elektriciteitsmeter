package be.olivermay.elektriciteitsmeter.mvc.statusService;

import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: oliverm
 * Date: 10/10/13
 * Time: 22:02
 * To change this template use File | Settings | File Templates.
 */
public class Day {

    private HashMap<Integer, Hour> hours = new HashMap<Integer, Hour>();

    public Day() {
        for (int i = 0; i < 24; i++) {
            hours.put(i, new Hour());
        }
    }

    public HashMap<Integer, Hour> getHours() {
        return hours;
    }

    public void setHours(HashMap<Integer, Hour> hours) {
        this.hours = hours;
    }
}
