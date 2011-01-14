/*
* Created by Generator 1.0
* Date 30/08/2005
*/
package it.cnr.contab.inventario00.docs.bulk;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
public class V_associazioni_disponibiliBase extends OggettoBulk implements Persistent {
 
 
//    PG_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long pg_inventario;
 
//    NR_INVENTARIO DECIMAL(10,0) NOT NULL
	private java.lang.Long nr_inventario;
 
//    PROGRESSIVO DECIMAL(10,0) NOT NULL
	private java.lang.Long progressivo;
 
//    DS_BENE VARCHAR(100)
	private java.lang.String ds_bene;
 
//	ESERCIZIO DECIMAL(4,0) 
	private java.lang.Integer esercizio;

// TI_DOCUMENTO CHAR(1) 
 	private java.lang.String ti_documento;
 
// PG_BUONO_C_S DECIMAL(10,0)
	private java.lang.Long pg_buono_c_s;
	
	private java.math.BigDecimal associato;
	
	private java.math.BigDecimal inventariato;
	
//	 CD_UNITA_ORGANIZZATIVA VARCHAR(30) NOT NULL
	private java.lang.String cd_unita_organizzativa;
		
	public V_associazioni_disponibiliBase() {
		super();
	}
	
	public java.lang.Long getPg_inventario () {
		return pg_inventario;
	}
	public void setPg_inventario(java.lang.Long pg_inventario)  {
		this.pg_inventario=pg_inventario;
	}
	public java.lang.Long getNr_inventario () {
		return nr_inventario;
	}
	public void setNr_inventario(java.lang.Long nr_inventario)  {
		this.nr_inventario=nr_inventario;
	}
	public java.lang.Long getProgressivo () {
		return progressivo;
	}
	public void setProgressivo(java.lang.Long progressivo)  {
		this.progressivo=progressivo;
	}
	public java.lang.String getDs_bene () {
		return ds_bene;
	}
	public void setDs_bene(java.lang.String ds_bene)  {
		this.ds_bene=ds_bene;
	}
	
	public java.lang.Integer getEsercizio() {
		return esercizio;
	}
	public java.lang.Long getPg_buono_c_s() {
		return pg_buono_c_s;
	}
	public java.lang.String getTi_documento() {
		return ti_documento;
	}
	public void setEsercizio(java.lang.Integer integer) {
		esercizio = integer;
	}
	public void setPg_buono_c_s(java.lang.Long long1) {
		pg_buono_c_s = long1;
	}
	public void setTi_documento(java.lang.String string) {
		ti_documento = string;
	}

	public java.math.BigDecimal getAssociato() {
		return associato;
	}

	public void setAssociato(java.math.BigDecimal associato) {
		this.associato = associato;
	}

	public java.math.BigDecimal getInventariato() {
		return inventariato;
	}

	public void setInventariato(java.math.BigDecimal inventariato) {
		this.inventariato = inventariato;
	}

	public java.lang.String getCd_unita_organizzativa() {
		return cd_unita_organizzativa;
	}

	public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
		this.cd_unita_organizzativa = cd_unita_organizzativa;
	}
	
}