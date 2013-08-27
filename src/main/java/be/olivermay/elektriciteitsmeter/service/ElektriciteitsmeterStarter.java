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
package be.olivermay.elektriciteitsmeter.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author Oliver May
 *
 */
@Component
public class ElektriciteitsmeterStarter {

	@Autowired
	ElektriciteitsmeterService service;
	
	@PostConstruct
	public void start() {
		service.start();
	}
	
	@PreDestroy
	public void stop() {
		service.stop();
	}
}
