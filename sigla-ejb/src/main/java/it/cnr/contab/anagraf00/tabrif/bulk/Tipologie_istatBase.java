/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 23/04/2007
 */
package it.cnr.contab.anagraf00.tabrif.bulk ;
import it.cnr.jada.persistency.Keyed;
public class Tipologie_istatBase extends Tipologie_istatKey implements Keyed {
//    DS_TIPOLOGIA VARCHAR(200) NOT NULL
	private java.lang.String ds_tipologia;
 

	public Tipologie_istatBase() {
		super();
	}
	public Tipologie_istatBase(java.lang.Integer pg_tipologia) {
		super(pg_tipologia);
	}
	public java.lang.String getDs_tipologia() {
		return ds_tipologia;
	}
	public void setDs_tipologia(java.lang.String ds_tipologia)  {
		this.ds_tipologia=ds_tipologia;
	}

}