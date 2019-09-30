/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
