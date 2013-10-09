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

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import be.olivermay.elektriciteitsmeter.domain.Meting;

/**
 * @author Oliver May
 */
@Component(ElektriciteitsmeterServiceImpl.NAME)
@Transactional(rollbackFor = {Exception.class})
public class ElektriciteitsmeterServiceImpl implements PulseHandler, ElektriciteitsmeterService {

    public static final String NAME = "ElektriciteitsmeterServiceImpl";

    private CopyOnWriteArrayList<ElektriciteitsmeterListener> listeners =
            new CopyOnWriteArrayList<ElektriciteitsmeterListener>();

    private boolean isPulsing;

    private int pulses;

    private long lastPulse = System.currentTimeMillis();

    private boolean running = false;

    private Thread thread;

    //Elke minuut
    private static final String CRON = "0 * * * * *";

    private final Logger log = LoggerFactory.getLogger(ElektriciteitsmeterServiceImpl.class);

    @Autowired
    private DatabaseService dbService;
    private I2CSensorRunner runner;

    public ElektriciteitsmeterServiceImpl() {
        runner = new I2CSensorRunner(this);
    }

    public void addListener(ElektriciteitsmeterListener l) {
        listeners.add(l);
    }

    public void removeListener(ElektriciteitsmeterListener l) {
        listeners.remove(l);
    }

    @Scheduled(cron = CRON)
//	@Scheduled(fixedRate=60000)
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void savePulses() {
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

    @Override
    public void start() {
        Thread thread = new Thread(runner);
        thread.start();
    }

    @Override
    public void stop() {
        runner.requestStop();
    }

    @Override
    public void handlePulse() {
        pulses++;
    }
}
