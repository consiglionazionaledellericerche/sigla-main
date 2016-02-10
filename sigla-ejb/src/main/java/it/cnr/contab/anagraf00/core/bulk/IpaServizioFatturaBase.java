/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 06/06/2014
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class IpaServizioFatturaBase extends IpaServizioFatturaKey implements Keyed {
//    COD_UNI_OU VARCHAR(6)
	private java.lang.String codUniOu;
 
//    CF VARCHAR(11)
	private java.lang.String cf;
 
//    DATA_AVVIO_SFE TIMESTAMP(7)
	private java.sql.Timestamp dataAvvioSfe;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: IPA_SERVIZIO_FATTURA
	 **/
	public IpaServizioFatturaBase() {
		super();
	}
	public IpaServizioFatturaBase(java.lang.String codAmm, java.lang.String codOu) {
		super(codAmm, codOu);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [codUniOu]
	 **/
	public java.lang.String getCodUniOu() {
		return codUniOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [codUniOu]
	 **/
	public void setCodUniOu(java.lang.String codUniOu)  {
		this.codUniOu=codUniOu;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cf]
	 **/
	public java.lang.String getCf() {
		return cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cf]
	 **/
	public void setCf(java.lang.String cf)  {
		this.cf=cf;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dataAvvioSfe]
	 **/
	public java.sql.Timestamp getDataAvvioSfe() {
		return dataAvvioSfe;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dataAvvioSfe]
	 **/
	public void setDataAvvioSfe(java.sql.Timestamp dataAvvioSfe)  {
		this.dataAvvioSfe=dataAvvioSfe;
	}
}