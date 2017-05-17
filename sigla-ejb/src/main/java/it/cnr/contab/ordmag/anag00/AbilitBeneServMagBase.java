/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 26/04/2017
 */
package it.cnr.contab.ordmag.anag00;
import it.cnr.jada.persistency.Keyed;
public class AbilitBeneServMagBase extends AbilitBeneServMagKey implements Keyed {
//    DT_FIN_VALIDITA TIMESTAMP(7)
	private java.sql.Timestamp dtFinValidita;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: ABILIT_BENE_SERV_MAG
	 **/
	public AbilitBeneServMagBase() {
		super();
	}
	public AbilitBeneServMagBase(java.lang.String cdCds, java.lang.String cdMagazzino, java.lang.String cdCategoriaGruppo) {
		super(cdCds, cdMagazzino, cdCategoriaGruppo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dtFinValidita]
	 **/
	public java.sql.Timestamp getDtFinValidita() {
		return dtFinValidita;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dtFinValidita]
	 **/
	public void setDtFinValidita(java.sql.Timestamp dtFinValidita)  {
		this.dtFinValidita=dtFinValidita;
	}
}