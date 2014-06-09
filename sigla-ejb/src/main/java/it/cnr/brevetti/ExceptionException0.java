
/**
 * ExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */

package it.cnr.brevetti;

public class ExceptionException0 extends java.lang.Exception{
    
    private it.cnr.brevetti.TrovatiWebServiceBeanServiceStub.ExceptionE faultMessage;
    
    public ExceptionException0() {
        super("ExceptionException0");
    }
           
    public ExceptionException0(java.lang.String s) {
       super(s);
    }
    
    public ExceptionException0(java.lang.String s, java.lang.Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(it.cnr.brevetti.TrovatiWebServiceBeanServiceStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public it.cnr.brevetti.TrovatiWebServiceBeanServiceStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    