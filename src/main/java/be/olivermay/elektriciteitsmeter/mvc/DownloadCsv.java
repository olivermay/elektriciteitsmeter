/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2012 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */
package be.olivermay.elektriciteitsmeter.mvc;

import java.io.PrintStream;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import be.olivermay.elektriciteitsmeter.domain.Meting;
import be.olivermay.elektriciteitsmeter.service.DatabaseService;


/**
 * @author Oliver May
 *
 */
@Controller("/csv")
public class DownloadCsv {

	@Autowired
	private DatabaseService dbService;
	
	@RequestMapping(method = RequestMethod.GET)
	public void getStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {

		PrintStream os = new PrintStream(response.getOutputStream());

		response.setHeader("Content-type", "text/csv");
		response.setHeader("Content-disposition",  "attachment;filename=download.csv");
		
		os.println("Datum;Pulsen");

		for (Meting meting : dbService.getMetingen(100)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			os.println(sdf.format(meting.getDatum()) + ";" + meting.getPulses());
		}
	}
	
	
}
