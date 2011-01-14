/*
* Created by Generator 1.0
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Buono_carico_scarico_dettBase extends Buono_carico_scarico_dettKey implements Keyed {
//    INTERVALLO VARCHAR(20) NOT NULL
	private java.lang.String intervallo;
 
//    QUANTITA DECIMAL(22,0) NOT NULL
	private java.lang.Long quantita;
 
//    VALORE_UNITARIO DECIMAL(20,6)
	private java.math.BigDecimal valore_unitario;
 
//    STATO_COGE CHAR(1) NOT NULL
	private java.lang.String stato_coge;
//  STATO_COGE_quote CHAR(1) NOT NULL
	private java.lang.String stato_coge_quote;
 
	public Buono_carico_scarico_dettBase() {
		super();
	}
	public Buono_carico_scarico_dettBase(java.lang.Long pg_inventario, java.lang.String ti_documento, java.lang.Integer esercizio, java.lang.Long pg_buono_c_s, java.lang.Long nr_inventario, java.lang.Integer progressivo) {
		super(pg_inventario, ti_documento, esercizio, pg_buono_c_s, nr_inventario, progressivo);
	}
	public java.lang.String getIntervallo () {
		return intervallo;
	}
	public void setIntervallo(java.lang.String intervallo)  {
		this.intervallo=intervallo;
	}
	public java.lang.Long getQuantita () {
		return quantita;
	}
	public void setQuantita(java.lang.Long quantita)  {
		this.quantita=quantita;
	}
	public java.math.BigDecimal getValore_unitario () {
		return valore_unitario;
	}
	public void setValore_unitario(java.math.BigDecimal valore_unitario)  {
		this.valore_unitario=valore_unitario;
	}
	public java.lang.String getStato_coge () {
		return stato_coge;
	}
	public void setStato_coge(java.lang.String stato_coge)  {
		this.stato_coge=stato_coge;
	}
	public java.lang.String getStato_coge_quote() {
		return stato_coge_quote;
	}
	public void setStato_coge_quote(java.lang.String stato_coge_quote) {
		this.stato_coge_quote = stato_coge_quote;
	}
}