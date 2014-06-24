

/**
 * TrovatiWebServiceBeanService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4-SNAPSHOT  Built on : Dec 11, 2010 (03:10:24 GMT-08:00)
 */

    package it.cnr.brevetti;

    /*
     *  TrovatiWebServiceBeanService java interface
     */

    public interface TrovatiWebServiceBeanService {
          

        /**
          * Auto generated method signature
          * 
                    * @param findTrovato0
                
             * @throws it.cnr.brevetti.ExceptionException : 
         */

         
                     public it.cnr.brevetti.FindTrovatoResponseE findTrovato(

                        it.cnr.brevetti.FindTrovatoE findTrovato0)
                        throws java.rmi.RemoteException
             
          ,it.cnr.brevetti.ExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param findTrovato0
            
          */
        public void startfindTrovato(

            it.cnr.brevetti.FindTrovatoE findTrovato0,

            final it.cnr.brevetti.TrovatiWebServiceBeanServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    