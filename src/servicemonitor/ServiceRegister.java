/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

import java.util.HashMap;
import java.util.Map;

/**
 * The singleton service register will be used to hold the callers interest in a
 * services and the polling frequency. The singleton design pattern is used to
 * ensure to have only a single instance of the ServiceRegister.
 *
 * @author Lahiru
 */
public class ServiceRegister {

    //Map for hold registered services (key - name of the registry , value - Registry object)
    private Map<String, Registry> serviceRegistry = new HashMap<>();

    //singalton object
    private static final ServiceRegister serviceRegisterObj = new ServiceRegister();

    private ServiceRegister() {
    }

    public static ServiceRegister getInstance() {
        return serviceRegisterObj;
    }

    public Iterable<String> getServiceNames() {
        return serviceRegistry.keySet();
    }

    public Registry getServiceRegistry(String name) {
        return serviceRegistry.get(name);
    }

    public void registerService(Registry registry) {
        serviceRegistry.put(registry.getName(), registry);
    }

    public void removeServiceRegistry(String name) {
        serviceRegistry.remove(name);
    }
    
    public void reset() {
        serviceRegistry.clear();
    }
}
