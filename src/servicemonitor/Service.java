/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

/**
 * The service class is used to hold the service related properties and
 * behaviors.
 *
 * @author Lahiru
 */
public class Service {

    private String name;
    private String host;
    private int port;
    private long outageStart;
    private long outageEnd;
    private boolean running;
    private long lastPollingTime = 0;
    private boolean stateChange;

    public Service(String name, String host, int port, long outageStart, long outageEnd, boolean running) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.outageStart = outageStart;
        this.outageEnd = outageEnd;
        this.running = running;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public long getOutageStart() {
        return outageStart;
    }

    public void setOutageStart(long outageStart) {
        this.outageStart = outageStart;
    }

    public long getOutageEnd() {
        return outageEnd;
    }

    public void setOutageEnd(long outageEnd) {
        this.outageEnd = outageEnd;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public long getLastPollingTime() {
        return lastPollingTime;
    }

    public void setLastPollingTime(long lastPollingTime) {
        this.lastPollingTime = lastPollingTime;
    }

    public boolean isStateChange() {
        return stateChange;
    }

    public void setStateChange(boolean stateChange) {
        this.stateChange = stateChange;
    }
}
