/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 14/09/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tematica_attivitaBase extends Tematica_attivitaKey implements Keyed {
//    DS_TEMATICA_ATTIVITA VARCHAR(200) NOT NULL
	private java.lang.String ds_tematica_attivita;
 
	public Tematica_attivitaBase() {
		super();
	}
	public Tematica_attivitaBase(java.lang.String cd_tematica_attivita) {
		super(cd_tematica_attivita);
	}
	public java.lang.String getDs_tematica_attivita() {
		return ds_tematica_attivita;
	}
	public void setDs_tematica_attivita(java.lang.String ds_tematica_attivita)  {
		this.ds_tematica_attivita=ds_tematica_attivita;
	}
}