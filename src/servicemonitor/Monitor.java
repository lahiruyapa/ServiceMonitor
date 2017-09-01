/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servicemonitor;

/**
 * The service monitoring class.
 *
 * @author Lahiru
 */
public class Monitor {

    //protected ServiceRegister serviceRegister = ServiceRegister.getInstance();

    /**
     * The method checks the following two conditions, 1. Should not poll any
     * service more frequently than once a second. This will be achieved by
     * checking the difference between currentTime and the lastPollingTime. This
     * difference must be greater than or equal to 1000ms(1 second).
     *
     * 2. The service will be polled as defined by the polling frequency.
     *
     * @param String name
     * @return true if polling is allowed
     */
    public static boolean checkPollingTime(String name) {
        Registry registry = ServiceRegister.getInstance().getServiceRegistry(name);
        long currentTime = System.currentTimeMillis();
        Service service = registry.getService();
        return (((currentTime - service.getLastPollingTime()) >= 1000)
                && currentTime >= (registry.getLastPollingTime() + registry.getPollingFrequency()));
    }

    /**
     * The method to check whether given service is in outage status or not.
     *
     * @param String name
     * @return true if the service in outage
     */
    public static boolean isServiceOutage(String name) {
        Registry registry = ServiceRegister.getInstance().getServiceRegistry(name);
        long currentTimeMillis = System.currentTimeMillis();
        long outageStart = registry.getService().getOutageStart();
        long outageEnd = registry.getService().getOutageEnd();
        if (outageStart <= currentTimeMillis && currentTimeMillis <= outageEnd) {
            System.out.println(registry.getService().getName() + " is in outage!");
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        boolean isThreadRunning = true;
        final String LOCALHOST = "127.0.0.1";
        //services
        final String SERVICE_ONE = "Service_One";
        final String SERVICE_TWO = "Service_Two";
        final String SERVICE_THREE = "Service_Three";
        //callers
        final String CALLER_ONE = "Caller_One";
        final String CALLER_TWO = "Caller_Two";
        final String CALLER_THREE = "Caller_Three";

        Service serviceOne = new Service(SERVICE_ONE, LOCALHOST, 8080, 0, 0, true);
        Caller callerOne = new Caller(CALLER_ONE, 15000);
        Registry registryOne = new Registry("Registry_One", callerOne, serviceOne, 4000);
        
        Service serviceTwo = new Service(SERVICE_TWO, LOCALHOST, 8080, 0, 0, true);
        Caller callerTwo = new Caller(CALLER_TWO, 0);
        Registry registryTwo = new Registry("Registry_Two", callerTwo, serviceTwo, 2000);
        
        Service serviceThree = new Service(SERVICE_THREE, LOCALHOST, 9090, 9000, 81000, true);
        Caller callerThree = new Caller(CALLER_THREE, 0);
        Registry registryThree = new Registry("Registry_Three", callerThree, serviceThree, 1000);
        
        
        ServiceRegister.getInstance().registerService(registryOne);
        ServiceRegister.getInstance().registerService(registryTwo);
        ServiceRegister.getInstance().registerService(registryThree);

        //while loop keeps services running continuously.
        while (isThreadRunning) {
            for (String serviceName : ServiceRegister.getInstance().getServiceNames()) {
                if (!isServiceOutage(serviceName) && checkPollingTime(serviceName)) {
                    ServiceRegister.getInstance().getServiceRegistry(serviceName).setLastPollingTime(System.currentTimeMillis());
                    ServiceRegister.getInstance().getServiceRegistry(serviceName).getService().setLastPollingTime(System.currentTimeMillis());
                    Runnable r = new ServiceStateChecker(serviceName);
                    new Thread(r).start();
                }
            }
        }
    }
}
