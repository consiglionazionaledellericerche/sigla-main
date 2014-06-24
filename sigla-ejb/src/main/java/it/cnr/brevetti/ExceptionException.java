
/**
 * ExceptionException.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4-SNAPSHOT  Built on : Dec 11, 2010 (03:10:24 GMT-08:00)
 */

package it.cnr.brevetti;

public class ExceptionException extends java.lang.Exception{
    
    private it.cnr.brevetti.ExceptionE faultMessage;

    
        public ExceptionException() {
            super("ExceptionException");
        }

        public ExceptionException(java.lang.String s) {
           super(s);
        }

        public ExceptionException(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public ExceptionException(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(it.cnr.brevetti.ExceptionE msg){
       faultMessage = msg;
    }
    
    public it.cnr.brevetti.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    