/*
* Created by Generator 1.0
* Date 17/11/2005
*/
package it.cnr.contab.config00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Parametri_uoBase extends Parametri_uoKey implements Keyed {
//    FL_GESTIONE_MODULI CHAR(1) NOT NULL
	private java.lang.Boolean fl_gestione_moduli;
 
//    PG_MODULO_DEFAULT DECIMAL(10,0)
	private java.lang.Integer pg_modulo_default;
 
	public Parametri_uoBase() {
		super();
	}
	public Parametri_uoBase(java.lang.String cd_unita_organizzativa, java.lang.Integer esercizio) {
		super(cd_unita_organizzativa, esercizio);
	}
	public java.lang.Boolean getFl_gestione_moduli () {
		return fl_gestione_moduli;
	}
	public void setFl_gestione_moduli(java.lang.Boolean fl_gestione_moduli)  {
		this.fl_gestione_moduli=fl_gestione_moduli;
	}
	public java.lang.Integer getPg_modulo_default () {
		return pg_modulo_default;
	}
	public void setPg_modulo_default(java.lang.Integer pg_modulo_default)  {
		this.pg_modulo_default=pg_modulo_default;
	}
}