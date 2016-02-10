
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.4-SNAPSHOT  Built on : Dec 11, 2010 (03:10:43 GMT-08:00)
 */

        
            package it.cnr.brevetti;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "Exception".equals(typeName)){
                   
                            return  it.cnr.brevetti.Exception.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "findTrovatoValido".equals(typeName)){
                   
                            return  it.cnr.brevetti.FindTrovatoValido.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "findTrovato".equals(typeName)){
                   
                            return  it.cnr.brevetti.FindTrovato.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "findTrovatoValidoResponse".equals(typeName)){
                   
                            return  it.cnr.brevetti.FindTrovatoValidoResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "findTrovatoResponse".equals(typeName)){
                   
                            return  it.cnr.brevetti.FindTrovatoResponse.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://brevetti.cnr.it".equals(namespaceURI) &&
                  "trovatoBean".equals(typeName)){
                   
                            return  it.cnr.brevetti.TrovatoBean.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    