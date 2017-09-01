/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

/**
 * The caller class is used to hold the service related properties and
 * behaviors.
 *
 * @author Lahiru
 */
public class Caller {

    private String name;
    private long graceTime;

    public Caller(String name, long graceTime) {
        this.name = name;
        this.graceTime = graceTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGraceTime() {
        return graceTime;
    }

    public void setGraceTime(long graceTime) {
        this.graceTime = graceTime;
    }
}
