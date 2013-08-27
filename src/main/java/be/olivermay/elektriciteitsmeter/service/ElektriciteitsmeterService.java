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

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.impl.I2CBusImpl;

import be.olivermay.elektriciteitsmeter.domain.Meting;

/**
 * @author Oliver May
 * 
 */
@Component(ElektriciteitsmeterService.NAME)
@Transactional(rollbackFor = { Exception.class })
public class ElektriciteitsmeterService {

	public static final String NAME = "ElektriciteitsmeterService";

	private CopyOnWriteArrayList<ElektriciteitsmeterListener> listeners = 
		new CopyOnWriteArrayList<ElektriciteitsmeterListener>();
	
	private boolean isPulsing;

	private int pulses;
	
	private long lastPulse = System.currentTimeMillis();
	
	private boolean running = false;
	
	private Thread thread;

	//Elke minuut
	private static final String CRON = "0 * * * * *";
	
	private final Logger log = LoggerFactory.getLogger(ElektriciteitsmeterService.class);
	
	@Autowired
	private DatabaseService dbService;
	
	public ElektriciteitsmeterService() {
	}
	
	public void addListener(ElektriciteitsmeterListener l) {
		listeners.add(l);
	}
	
	public void removeListener(ElektriciteitsmeterListener l) {
		listeners.remove(l);
	}

	@Scheduled(cron = CRON)
//	@Scheduled(fixedRate=60000)
	@Transactional(rollbackFor = { Exception.class })
	protected void savePulses() {
		log.info("saving pulses");
		Meting meting = new Meting();
		meting.setDatum(new Date());
		synchronized (this) {
			meting.setPulses(pulses);
			pulses = 0;
		}
		dbService.saveOrUpdate(meting);
		log.info("saved record: " + meting.toString());
	}

	public void start() {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					I2CDevice device = I2CFactory.getInstance(I2CBus.BUS_1).getDevice(Integer.parseInt("48", 16));
					device.read(0);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}; 
	}

	public void stop() {
	}
}
