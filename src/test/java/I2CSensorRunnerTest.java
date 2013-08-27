import org.junit.Test;

import be.olivermay.elektriciteitsmeter.service.I2CSensorRunner;


/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

/**
 * @author Oliver May
 *
 */
public class I2CSensorRunnerTest {

	@Test
	public void testReadSensor() throws InterruptedException {
		I2CSensorRunner runner = new I2CSensorRunner();
		Thread thread = new Thread(runner);
		
		thread.start();
		
		Thread.sleep(30000);
		
		runner.requestStop();
		
		Thread.sleep(1000);
		
	}
}
