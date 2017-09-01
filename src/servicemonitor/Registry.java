/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

/**
 * The Register class is used to hold the single caller interest in a service
 * and polling frequency. Apart form that necessary properties are maintained to
 * handle the logic.
 *
 * @author Lahiru
 */
public class Registry {

    private String name;
    private Caller caller;
    private Service service;
    //milli-second(s)
    private long pollingFrequency;
    private long lastPollingTime = 0;

    public Registry(String name, Caller caller, Service service, long pollingFrequency) {
        this.name = name;
        this.caller = caller;
        this.service = service;
        this.pollingFrequency = pollingFrequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Caller getCaller() {
        return caller;
    }

    public void setCaller(Caller caller) {
        this.caller = caller;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public long getPollingFrequency() {
        return pollingFrequency;
    }

    public void setPollingFrequency(long pollingFrequency) {
        this.pollingFrequency = pollingFrequency;
    }

    public long getLastPollingTime() {
        return lastPollingTime;
    }

    public void setLastPollingTime(long lastPollingTime) {
        this.lastPollingTime = lastPollingTime;
    }
}
