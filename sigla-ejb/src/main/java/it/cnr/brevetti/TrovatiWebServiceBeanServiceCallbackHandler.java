
/**
 * TrovatiWebServiceBeanServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4-SNAPSHOT  Built on : Dec 11, 2010 (03:10:24 GMT-08:00)
 */

    package it.cnr.brevetti;

    /**
     *  TrovatiWebServiceBeanServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class TrovatiWebServiceBeanServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public TrovatiWebServiceBeanServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public TrovatiWebServiceBeanServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for findTrovatoValido method
            * override this method for handling normal response from findTrovatoValido operation
            */
           public void receiveResultfindTrovatoValido(
                    it.cnr.brevetti.FindTrovatoValidoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findTrovatoValido operation
           */
            public void receiveErrorfindTrovatoValido(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for findTrovato method
            * override this method for handling normal response from findTrovato operation
            */
           public void receiveResultfindTrovato(
                    it.cnr.brevetti.FindTrovatoResponseE result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from findTrovato operation
           */
            public void receiveErrorfindTrovato(java.lang.Exception e) {
            }
                


    }
    