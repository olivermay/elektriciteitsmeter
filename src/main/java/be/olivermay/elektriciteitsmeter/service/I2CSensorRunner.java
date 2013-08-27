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
package be.olivermay.elektriciteitsmeter.service;

import java.io.IOException;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;


/**
 * @author Oliver May
 *
 */
public class I2CSensorRunner implements Runnable {

	private boolean stopRequested = false;
	
	private boolean pulse = false;
	
	@Override
	public void run() {
		I2CDevice device;
		try {
			device = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(Integer.parseInt("48", 16));

			while (!stopRequested) {
				int value = device.read(0);
				
				System.out.println(value);
				Thread.sleep(100);
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void requestStop() {
		stopRequested = true;
	}
	
}
