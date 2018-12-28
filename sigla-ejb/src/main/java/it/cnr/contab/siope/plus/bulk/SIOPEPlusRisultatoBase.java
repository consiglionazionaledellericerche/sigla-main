/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 20/12/2018
 */
package it.cnr.contab.siope.plus.bulk;
import it.cnr.jada.persistency.Keyed;

import java.sql.Timestamp;

public class SIOPEPlusRisultatoBase extends SIOPEPlusRisultatoKey implements Keyed {
//    ESITO VARCHAR(50) NOT NULL
	private String esito;
 
//    LOCATION VARCHAR(1000) NOT NULL
	private String location;

	private Integer prog_flusso;

	private Integer prog_esito_applicativo;

	private Timestamp data_produzione;

	private Timestamp data_upload;

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

	public Integer getProg_flusso() {
		return prog_flusso;
	}

	public void setProg_flusso(Integer prog_flusso) {
		this.prog_flusso = prog_flusso;
	}

	public Integer getProg_esito_applicativo() {
		return prog_esito_applicativo;
	}

	public void setProg_esito_applicativo(Integer prog_esito_applicativo) {
		this.prog_esito_applicativo = prog_esito_applicativo;
	}

	public Timestamp getData_produzione() {
		return data_produzione;
	}

	public void setData_produzione(Timestamp data_produzione) {
		this.data_produzione = data_produzione;
	}

	public Timestamp getData_upload() {
		return data_upload;
	}

	public void setData_upload(Timestamp data_upload) {
		this.data_upload = data_upload;
	}
}