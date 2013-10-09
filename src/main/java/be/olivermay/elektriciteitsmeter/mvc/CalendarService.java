package be.olivermay.elektriciteitsmeter.mvc;

import be.olivermay.elektriciteitsmeter.domain.Meting;
import be.olivermay.elektriciteitsmeter.service.DatabaseService;
import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		PrintStream os = new PrintStream(response.getOutputStream());
		response.setHeader("Content-type", "text/html");
		
		os.println("<html><table>");

	 	List results = databaseService.getMetingenByHour("12 months", "11 months");
		HashMap<Integer, HashMap<Integer, Integer>> map = new HashMap<Integer, HashMap<Integer,
				Integer>>();

		int dagStart = 366;

		for (Object res : results) {
			Object[] result = (Object[]) res;
			Integer count = (BigInteger) result[0];
			Integer uur = (BigInteger) result[1];
			Integer dag = (BigInteger) result[2];
			if (!map.containsKey(uur)) {
				map.put(uur, new HashMap<Integer, Integer>());
			}
			if (!map.get(uur).containsKey(dag)) {
				map.get(uur).put(dag, count);
			}
			dagStart = Math.min(dagStart, dag);
		}

		for (int hour = 0; hour < 24; hour++) {
			os.println("<tr>");
			for (int dag = dagStart; dag < dagStart + 32; dag++) {
				os.print("<td>");
				if (map.containsKey(hour) && map.get(hour).containsKey(dag)) {
					os.print(map.get(hour).get(dag));
				}
				os.print("</td>");
			}
			os.println("</tr>");
		}

		os.println("<table></html>");
	}

}
