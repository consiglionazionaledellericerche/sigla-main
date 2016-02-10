/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 12/09/2011
 */
package it.cnr.contab.anagraf00.tabter.bulk;
import it.cnr.jada.persistency.Keyed;
public class RifAreePaesiEsteriBase extends RifAreePaesiEsteriKey implements Keyed {
//    DS_AREA_ESTERA VARCHAR(100) NOT NULL
	private java.lang.String ds_area_estera;
 
//    TI_ITALIA_ESTERO VARCHAR(1) NOT NULL
	private java.lang.String ti_italia_estero;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: RIF_AREE_PAESI_ESTERI
	 **/
	public RifAreePaesiEsteriBase() {
		super();
	}
	public RifAreePaesiEsteriBase(java.lang.String cd_area_estera) {
		super(cd_area_estera);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [dsAreaEstera]
	 **/
	public java.lang.String getDs_area_estera() {
		return ds_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [dsAreaEstera]
	 **/
	public void setDs_area_estera(java.lang.String ds_area_estera)  {
		this.ds_area_estera=ds_area_estera;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [tiItaliaEstero]
	 **/
	public java.lang.String getTi_italia_estero() {
		return ti_italia_estero;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [tiItaliaEstero]
	 **/
	public void setTi_italia_estero(java.lang.String ti_italia_estero)  {
		this.ti_italia_estero=ti_italia_estero;
	}
}