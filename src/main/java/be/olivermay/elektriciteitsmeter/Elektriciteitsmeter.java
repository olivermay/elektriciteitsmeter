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
package be.olivermay.elektriciteitsmeter;

import be.olivermay.elektriciteitsmeter.service.ElektriciteitsmeterServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author Oliver May
 * 
 */
public class Elektriciteitsmeter {

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"/be/olivermay/elektriciteitsmeter/config/spring/applicationContext.xml");

		ElektriciteitsmeterServiceImpl service = (ElektriciteitsmeterServiceImpl) context
				.getBean(ElektriciteitsmeterServiceImpl.NAME);
		
	}

}
