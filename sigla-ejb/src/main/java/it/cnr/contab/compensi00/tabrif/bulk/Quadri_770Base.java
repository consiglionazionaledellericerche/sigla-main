/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 28/07/2011
 */
package it.cnr.contab.compensi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Quadri_770Base extends Quadri_770Key implements Keyed {
//    DS_QUADRO VARCHAR(300) NOT NULL
	private java.lang.String ds_quadro;
 
//    TI_MODELLO VARCHAR(1) NOT NULL
	private java.lang.String ti_modello;
 
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: QUADRI_770
	 **/
	public Quadri_770Base() {
		super();
	}
	public Quadri_770Base(java.lang.Integer esercizio, java.lang.String cd_quadro) {
		super(esercizio, cd_quadro);
	}
	public java.lang.String getDs_quadro() {
		return ds_quadro;
	}
	public void setDs_quadro(java.lang.String ds_quadro) {
		this.ds_quadro = ds_quadro;
	}
	public java.lang.String getTi_modello() {
		return ti_modello;
	}
	public void setTi_modello(java.lang.String ti_modello) {
		this.ti_modello = ti_modello;
	}

}