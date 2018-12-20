/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.jada.persistency.Keyed;
public class SIOPEPlusRisultatoBase extends SIOPEPlusRisultatoKey implements Keyed {
//    ESITO VARCHAR(50) NOT NULL
	private String esito;
 
//    LOCATION VARCHAR(1000) NOT NULL
	private String location;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: SIOPE_PLUS_RISULTATO
	 **/
	public SIOPEPlusRisultatoBase() {
		super();
	}
	public SIOPEPlusRisultatoBase(Long id) {
		super(id);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [Codice identificativo dell''esito.]
	 **/
	public String getEsito() {
		return esito;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [Codice identificativo dell''esito.]
	 **/
	public void setEsito(String esito)  {
		this.esito=esito;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [URL del messaggio]
	 **/
	public String getLocation() {
		return location;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [URL del messaggio]
	 **/
	public void setLocation(String location)  {
		this.location=location;
	}

}