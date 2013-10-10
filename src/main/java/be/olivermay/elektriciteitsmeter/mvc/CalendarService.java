package be.olivermay.elektriciteitsmeter.mvc;

import be.olivermay.elektriciteitsmeter.domain.Meting;
import be.olivermay.elektriciteitsmeter.mvc.statusService.Day;
import be.olivermay.elektriciteitsmeter.service.DatabaseService;
import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterService;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Controller("/calendar/**")
@Transactional
public class CalendarService {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    SessionFactory sessionFactory;

    private final Logger log = LoggerFactory.getLogger(CalendarService.class);

    @RequestMapping(method = RequestMethod.GET, value = "/calendar/month")
    public void getStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {

        List results = databaseService.getMetingenByHour("9 months", "0 months");

        int dagStart = 366;
        int dagEnd = 0;

        for (Object res : results) {
            Object[] result = (Object[]) res;
            Integer dag = ((Double) result[2]).intValue();
            dagStart = Math.min(dagStart, dag);
            dagEnd = Math.max(dagEnd, dag);
        }

        TreeMap<Integer, Day> days = new TreeMap<Integer, Day>(new ReverseComparator());
        for (int i = dagStart; i < dagEnd; i++) {
            days.put(i, new Day());
        }

        for (Object res : results) {
            Object[] result = (Object[]) res;
            int value = ((BigInteger) result[0]).intValue();
            int uur = ((Double) result[1]).intValue();
            int dag = ((Double) result[2]).intValue();

            if (!days.containsKey(dag)) {
                days.put(dag, new Day());
            }
            days.get(dag).getHours().get(uur).setValue(value);
        }

        PrintStream os = new PrintStream(response.getOutputStream());
        response.setHeader("Content-type", "text/html");

        os.println("<style>table, th, td {" +
                "border: 1px solid black;" +
                "" +
                "}" +
                "td {" +
                "text-align: right;" +
                "}" +
                "th {" +
                "background-color: lightgray;" +
                "}" +
                "</style>");

        os.println("<html><table>");

        os.println("<tr>");
        os.println("<th>dag</th>");
        for (int i = 0; i < 24; i++) {
            os.println("<th>" + i + "</th>");
        }
        os.println("<th>total</th>");
        os.println("</tr>");

        Calendar calendar = Calendar.getInstance();

        for (Map.Entry<Integer, Day> day : days.entrySet()) {
            os.println("<tr>");
            calendar.set(Calendar.DAY_OF_YEAR, day.getKey());
            os.println("<th>" + calendar.getTime() + "</th>");
            int total = 0;
            for (int hour = 0; hour < 24; hour++) {
                Integer value = day.getValue().getHours().get(hour).getValue();
                if (value != null) {
                    os.println("<td bgcolor='" + getColor(value) + "'>" + value + "</td>");
                    total += value;
                } else {
                    os.println("<td/>");
                }
            }
            os.println("<td bgcolor='" + getColor(total / 24) + "'>" + total + "</td>");
            os.println("</tr>");
        }

        os.println("<table></html>");
    }

    private String getColor(int value) {
        if (value < 200) {
            return "greenyellow";
        }
        if (value < 400) {
            return "lawngreen";
        }
        if (value < 800) {
            return "gold";
        }
        if (value < 1600) {
            return "orange";
        }
        if (value < 3200) {
            return "orangered";
        }
        return "red";

    }

}
