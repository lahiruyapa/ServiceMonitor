/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.servicemonitor;

import org.junit.Assert;
import org.junit.Test;
import servicemonitor.Caller;
import servicemonitor.Monitor;
import servicemonitor.Registry;
import servicemonitor.Service;
import servicemonitor.ServiceRegister;
import servicemonitor.ServiceStateChecker;

/**
 * The unit test class for cover basic logic of the Service monitoring class.
 * 
 * @author Lahiru
 */
public class MonitorTest {

    /**
     * The test method to check the behavior of ServiceRegister singleton class
     * and the service registry map.
     */
    @Test
    public void serviceRegisterTest() {
        ServiceRegister instance = ServiceRegister.getInstance();
        Assert.assertNotNull(instance);

        Service testServiceOne = new Service("Test-service-one", "127.0.0.1", 8080, 0, 0, true);
        Caller testCallerOne = new Caller("Caller-one", 0);
        Registry testRegistryOne = new Registry("Test-registry-one", testCallerOne, testServiceOne, 2000);

        Service testServiceTwo = new Service("Test-service-two", "127.0.0.1", 9090, 0, 0, true);
        Caller testCallerTwo = new Caller("Caller-two", 0);
        Registry testRegistryTwo = new Registry("Test-registry-two", testCallerTwo, testServiceTwo, 4000);

        ServiceRegister.getInstance().registerService(testRegistryOne);
        ServiceRegister.getInstance().registerService(testRegistryTwo);

        Iterable<String> afterServiceNames = instance.getServiceNames();
        for (String serviceName : afterServiceNames) {
            Registry registry = ServiceRegister.getInstance().getServiceRegistry(serviceName);
            Assert.assertNotNull(registry);
            Assert.assertNotNull(registry.getCaller());
            Assert.assertNotNull(registry.getService());
        }

        Registry registry = ServiceRegister.getInstance().getServiceRegistry("Test-registry-one");
        Assert.assertEquals(2000, registry.getPollingFrequency());
        Assert.assertEquals("Caller-one", registry.getCaller().getName());
        Assert.assertTrue(registry.getService().isRunning());
        
        //After test complete, clear the service register.
        ServiceRegister.getInstance().reset();
    }

    /**
     * The test method to check the service up/down using TCP connection.
     *
     */
    @Test
    public void tcpConectionTest() {
        Service testServiceOne = new Service("Test-service-one", "127.0.0.1", 8080, 0, 0, true);
        Caller testCallerOne = new Caller("Caller-one", 0);
        Registry testRegistryOne = new Registry("Test-registry-one", testCallerOne, testServiceOne, 2000);
        ServiceRegister.getInstance().registerService(testRegistryOne);
        Assert.assertEquals(0, testRegistryOne.getLastPollingTime());

        testRegistryOne.setLastPollingTime(System.currentTimeMillis());
        testRegistryOne.getService().setLastPollingTime(System.currentTimeMillis());
        Runnable r = new ServiceStateChecker(testRegistryOne.getName());
        new Thread(r).start();
        Assert.assertNotEquals(0, ServiceRegister.getInstance().getServiceRegistry("Test-registry-one").getLastPollingTime());
        Assert.assertNotEquals(0, ServiceRegister.getInstance().getServiceRegistry("Test-registry-one").getService().getLastPollingTime());
        
        //After test complete, clear the service register.
        ServiceRegister.getInstance().reset();
    }

    /**
     * The test method to validate the polling frequency.
     */
    @Test
    public void pollingFrequencyTest() {
        Service testServiceOne = new Service("Test-service-one", "127.0.0.1", 8080, 0, 0, true);
        Caller testCallerOne = new Caller("Caller-one", 0);
        Registry testRegistryOne = new Registry("Test-registry-one", testCallerOne, testServiceOne, 9000);
        ServiceRegister.getInstance().registerService(testRegistryOne);

        for (int i = 0; i < 10; i++) {
            if (Monitor.checkPollingTime(testRegistryOne.getName())) {                
                testRegistryOne.setLastPollingTime(System.currentTimeMillis());
                testRegistryOne.getService().setLastPollingTime(System.currentTimeMillis());
                Runnable r = new ServiceStateChecker(testRegistryOne.getName());
                new Thread(r).start();
                //Since the polling frequency is 10 seconds, during 10 iteration it should be only 
                //first time allows to poll the service.
                Assert.assertTrue(i==0);
            }
        }
        //After test complete, clear the service register.
        ServiceRegister.getInstance().reset();
    }
    
    @Test
    public void serviceOutageTest(){
        Service testServiceOne = new Service("Test-service-one", "127.0.0.1", 8080, System.currentTimeMillis()-5000, System.currentTimeMillis()+5000, true);
        Caller testCallerOne = new Caller("Caller-one", 0);
        Registry testRegistryOne = new Registry("Test-registry-one", testCallerOne, testServiceOne, 9000);
        ServiceRegister.getInstance().registerService(testRegistryOne);
        
        if (!Monitor.isServiceOutage(testRegistryOne.getName())) {                
                testRegistryOne.setLastPollingTime(System.currentTimeMillis());
                testRegistryOne.getService().setLastPollingTime(System.currentTimeMillis());
                Runnable r = new ServiceStateChecker(testRegistryOne.getName());
                new Thread(r).start();
        }
        
        //Since the service is in outage, no notification will be recevied.
        Assert.assertEquals(0, testRegistryOne.getLastPollingTime());
        
        //After test complete, clear the service register.
        ServiceRegister.getInstance().reset();
    }   

}
