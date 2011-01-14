package it.cnr.contab.client.docamm;
@javax.xml.ws.WebFault(name="FatturaAttivaException",targetNamespace="http://contab.cnr.it/sigla")
public class FatturaAttivaException_Exception  extends Exception {   
	private FatturaAttivaException faultInfo;
		
	public FatturaAttivaException_Exception(String message,FatturaAttivaException faultInfo) {  
		super(message);       
		this.faultInfo = faultInfo; 
	}   
	public FatturaAttivaException_Exception(String message, FatturaAttivaException faultInfo,Throwable cause)
	{       
		super(message, cause);       
		this.faultInfo = faultInfo;   
	}   
	public FatturaAttivaException getFaultInfo() { 
		return faultInfo;    
	}
}
