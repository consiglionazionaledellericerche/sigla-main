/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class UnitaOperativaOrdBase extends UnitaOperativaOrdKey implements Keyed {
//    DS_UNITA_OPERATIVA VARCHAR(100) NOT NULL
	private java.lang.String dsUnitaOperativa;
 
//    CD_UNITA_ORGANIZZATIVA VARCHAR(30)
	private java.lang.String cdUnitaOrganizzativa;
 
//    DT_CANCELLAZIONE TIMESTAMP(7)
	private java.sql.Timestamp dtCancellazione;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: UNITA_OPERATIVA_ORD
	 **/
	public UnitaOperativaOrdBase() {
		super();
	}
	public UnitaOperativaOrdBase(java.lang.String cdUnitaOperativa) {
		super(cdUnitaOperativa);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsUnitaOperativa]
	 **/
	public java.lang.String getDsUnitaOperativa() {
		return dsUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsUnitaOperativa]
	 **/
	public void setDsUnitaOperativa(java.lang.String dsUnitaOperativa)  {
		this.dsUnitaOperativa=dsUnitaOperativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdUnitaOrganizzativa]
	 **/
	public java.lang.String getCdUnitaOrganizzativa() {
		return cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdUnitaOrganizzativa]
	 **/
	public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa)  {
		this.cdUnitaOrganizzativa=cdUnitaOrganizzativa;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtCancellazione]
	 **/
	public java.sql.Timestamp getDtCancellazione() {
		return dtCancellazione;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtCancellazione]
	 **/
	public void setDtCancellazione(java.sql.Timestamp dtCancellazione)  {
		this.dtCancellazione=dtCancellazione;
	}
}