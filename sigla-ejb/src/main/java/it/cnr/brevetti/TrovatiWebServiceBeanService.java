

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
                    * @param findTrovatoValido0
                
             * @throws it.cnr.brevetti.ExceptionException : 
         */

         
                     public it.cnr.brevetti.FindTrovatoValidoResponseE findTrovatoValido(

                        it.cnr.brevetti.FindTrovatoValidoE findTrovatoValido0)
                        throws java.rmi.RemoteException
             
          ,it.cnr.brevetti.ExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param findTrovatoValido0
            
          */
        public void startfindTrovatoValido(

            it.cnr.brevetti.FindTrovatoValidoE findTrovatoValido0,

            final it.cnr.brevetti.TrovatiWebServiceBeanServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        /**
          * Auto generated method signature
          * 
                    * @param findTrovato2
                
             * @throws it.cnr.brevetti.ExceptionException : 
         */

         
                     public it.cnr.brevetti.FindTrovatoResponseE findTrovato(

                        it.cnr.brevetti.FindTrovatoE findTrovato2)
                        throws java.rmi.RemoteException
             
          ,it.cnr.brevetti.ExceptionException;

        
         /**
            * Auto generated method signature for Asynchronous Invocations
            * 
                * @param findTrovato2
            
          */
        public void startfindTrovato(

            it.cnr.brevetti.FindTrovatoE findTrovato2,

            final it.cnr.brevetti.TrovatiWebServiceBeanServiceCallbackHandler callback)

            throws java.rmi.RemoteException;

     

        
       //
       }
    