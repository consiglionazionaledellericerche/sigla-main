/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 29/11/2006
 */
package it.cnr.contab.config00.geco.bulk;
import it.cnr.jada.persistency.Keyed;
public class Geco_dipartimento_rstlBase extends Geco_dipartimento_rstlKey implements Keyed {
//    id_dip DECIMAL(10,0) NOT NULL
	private java.lang.Long id_dip;
 
//    descrizione VARCHAR(255) NOT NULL
	private java.lang.String descrizione;
 
//    data_istituzione TIMESTAMP(7)
	private java.sql.Timestamp data_istituzione;
 
//    ordine DECIMAL(10,0) NOT NULL
	private java.lang.Long ordine;
 
	public Geco_dipartimento_rstlBase() {
		super();
	}
	public Geco_dipartimento_rstlBase(java.lang.String cod_dip) {
		super(cod_dip);
	}
	public java.lang.Long getId_dip() {
		return id_dip;
	}
	public void setId_dip(java.lang.Long id_dip)  {
		this.id_dip=id_dip;
	}
	public java.lang.String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(java.lang.String descrizione)  {
		this.descrizione=descrizione;
	}
	public java.sql.Timestamp getData_istituzione() {
		return data_istituzione;
	}
	public void setData_istituzione(java.sql.Timestamp data_istituzione)  {
		this.data_istituzione=data_istituzione;
	}
	public java.lang.Long getOrdine() {
		return ordine;
	}
	public void setOrdine(java.lang.Long ordine)  {
		this.ordine=ordine;
	}
}