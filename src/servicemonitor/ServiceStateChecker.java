/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ServiceStateChecker is used to create the TCP connection with given
 * host/port combination. This will notify the caller whether the service is up
 * or not according to the logic. Service state checker makes sure to create
 * separate thread for each connection and update relevant registry record.
 *
 * @author Lahiru
 */
public class ServiceStateChecker implements Runnable {

    private Registry registry;

    public ServiceStateChecker() {
    }

    public ServiceStateChecker(String name) {
        registry = ServiceRegister.getInstance().getServiceRegistry(name);
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(registry.getService().getHost(), registry.getService().getPort());
            if (socket.isConnected()) {
                serviceUp();
            } else {
                serviceDown();
            }
        } catch (IOException ex) {
            serviceDown();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                Logger.getLogger(ServiceStateChecker.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public void serviceUp() {
        for (String serviceName : ServiceRegister.getInstance().getServiceNames()) {
            Registry registryObj = ServiceRegister.getInstance().getServiceRegistry(serviceName);
            if (registryObj.getService().getName().equals(registry.getService().getName())) {
                registryObj.getService().setRunning(true);
            }
        }

        System.out.println(registry.getService().getName() + " is connected via " + registry.getCaller().getName() + " with " + Thread.currentThread().getName());
        ServiceRegister.getInstance().getServiceRegistry(registry.getName()).setLastPollingTime(System.currentTimeMillis());
        ServiceRegister.getInstance().getServiceRegistry(registry.getName()).getService().setLastPollingTime(System.currentTimeMillis());
    }

    public void serviceDown() {
        for (String serviceName : ServiceRegister.getInstance().getServiceNames()) {
            Registry registryObj = ServiceRegister.getInstance().getServiceRegistry(serviceName);
            if (registryObj.getService().getName().equals(registry.getService().getName())) {
                registryObj.getService().setRunning(false);
            }
        }

        if (registry.getCaller().getGraceTime() > 0) {
            System.out.println("Waiting for the grace time to expire before notifying any clients with " + Thread.currentThread().getName());
            try {
                Thread.sleep(registry.getCaller().getGraceTime());
            } catch (InterruptedException ex) {
                Logger.getLogger(ServiceStateChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //If the service has gone online during grace period, no notification will be sent.
        if (!registry.getService().isRunning()) {
            System.out.println(registry.getService().getName() + " is not connected via " + registry.getCaller().getName() + " with " + Thread.currentThread().getName());
        }
        ServiceRegister.getInstance().getServiceRegistry(registry.getName()).setLastPollingTime(System.currentTimeMillis());
        ServiceRegister.getInstance().getServiceRegistry(registry.getName()).getService().setLastPollingTime(System.currentTimeMillis());
    }

}
