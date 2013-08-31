package be.olivermay.elektriciteitsmeter.mvc;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import be.olivermay.elektriciteitsmeter.domain.Meting;
import be.olivermay.elektriciteitsmeter.service.DatabaseService;
import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterEvent;
import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterListener;
import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterService;

@Controller("/status/**")
public class StatusService {

	@Autowired
	private DatabaseService dbService;
	
	@Autowired
	private ElektriciteitsmeterService meterService;

	private final Logger log = LoggerFactory.getLogger(StatusService.class);
	
	@RequestMapping(method = RequestMethod.GET, value = "/status/live")
	public void getLive(HttpServletRequest request, HttpServletResponse response) throws Exception {

		final PrintStream os = new PrintStream(response.getOutputStream());
		response.setHeader("Content-type", "text/html");
		
		os.println("<html>");
		
		ElektriciteitsmeterListener listener = new ElektriciteitsmeterListener() {
			
			private boolean active = true;
			
			@Override
			public void pulse(ElektriciteitsmeterEvent e) {
				try {
					os.println( (3600000.0 / e.getTime()) + " watt, time: " + e.getTime() + " fps: " + (e.getFrames()* 1000 / e.getFrames()) + "<br/>");
					os.flush();
				} catch (Exception ex) {
					log.warn("Exception while getting live data", e);
					meterService.removeListener(this);
					log.info("Removing listerner for live reporting");
					active = false;
				}
			}

			@Override
			public boolean isActive() {
				return active;
			}
		};
		log.info("Adding listener for live reporting");
		meterService.addListener(listener);
		while(listener.isActive()) {
			Thread.sleep(100);
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/status/")
	public void getStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintStream os = new PrintStream(response.getOutputStream());
		response.setHeader("Content-type", "text/html");
		
		os.println("<html><table>");
		int count = 0;
		for (Meting meting : dbService.getMetingen(100)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			os.print("<tr>");
			os.print("<td>" + sdf.format(meting.getDatum()) + " </td><td align='right'><b>" + meting.getPulses() + "</b></td><td align='right'>" + (60 * meting.getPulses())
					+ " watt</td>");
			os.print("</tr>");
			if (count++ >= 100) {
				break;
			}
		}
		os.println("<table></html>");
	}

	@RequestMapping(method = RequestMethod.GET, value = "/status/hourly")
	public void getStatusHourly(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintStream os = new PrintStream(response.getOutputStream());
		response.setHeader("Content-type", "text/html");
		
		os.println("<html><table>");
		
		Date date = null;
		int pulses = 0;
		int count = 0;
		
		for (Meting meting : dbService.getMetingen(100 * 60)) {
			if (date == null) {
				date = meting.getDatum();
			}
			if (meting.getDatum().getHours() != date.getHours()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				os.print("<tr>");
				os.print("<td>" + sdf.format(meting.getDatum()) + " </td><td align='right'><b>" + pulses + "</b></td><td align='right'>" + (pulses)
						+ " wattuur</td>");
				os.print("</tr>");
				date = null;
				pulses = 0;
				if (count++ >= 100) {
					break;
				}
			} else {
				pulses += meting.getPulses();
			}
		}
		os.println("<table></html>");
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/status/daily")
	public void getStatusDaily(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintStream os = new PrintStream(response.getOutputStream());
		response.setHeader("Content-type", "text/html");
		
		os.println("<html><table>");
		
		Date date = null;
		int pulses = 0;
		int count = 0;
		
		for (Meting meting : dbService.getMetingen(100 * 60 * 24)) {
			if (date == null) {
				date = meting.getDatum();
			}
			if (meting.getDatum().getDay() != date.getDay()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				os.print("<tr>");
				os.print("<td>" + sdf.format(meting.getDatum()) + " </td><td align='right'><b>" + pulses + "</b></td><td align='right'>" + (pulses)
						+ " wattuur</td>");
				os.print("</tr>");
				date = null;
				pulses = 0;
				if (count++ >= 100) {
					break;
				}
			} else {
				pulses += meting.getPulses();
			}
		}
		os.println("<table></html>");
	}
}
