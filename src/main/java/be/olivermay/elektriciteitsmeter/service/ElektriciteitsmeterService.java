package be.olivermay.elektriciteitsmeter.service;

/**
 * Created with IntelliJ IDEA.
 * User: oliverm
 * Date: 08/10/13
 * Time: 20:56
 * To change this template use File | Settings | File Templates.
 */
public interface ElektriciteitsmeterService {
    void start();

    void stop();

    void savePulses();
}
