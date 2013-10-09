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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author Oliver May
 *
 */
public class I2CSensorRunner implements Runnable {

    private PulseHandler handler;

    private boolean stopRequested = false;
	
	private boolean state = false;
    private boolean previousState = false;

    private int lastValue;

    private final Logger log = LoggerFactory.getLogger(I2CSensorRunner.class);

    public I2CSensorRunner(PulseHandler handler) {
        this.handler = handler;
    }

	@Override
	public void run() {
		I2CDevice device;
		try {
			device = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(Integer.parseInt("48", 16));
			while (!stopRequested) {
				int value = device.read();
                if (value < 128) {
                    state = false;
                } else {
                    state = true;
                }

                if (state != previousState) {
                    if (state) {
                        handler.handlePulse();
                        log.debug("pulse");
                    }
                    previousState = state;
                }
				Thread.sleep(50);
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
