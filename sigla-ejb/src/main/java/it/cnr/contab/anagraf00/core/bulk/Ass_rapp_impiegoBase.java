/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/01/2009
 */
package it.cnr.contab.anagraf00.core.bulk;
import it.cnr.jada.persistency.Keyed;
public class Ass_rapp_impiegoBase extends Ass_rapp_impiegoKey implements Keyed {
//    DS_RAPP_IMPIEGO_STI VARCHAR(50) NOT NULL
	private java.lang.String ds_rapp_impiego_sti;
 
//    TIPO_RAPP_IMPIEGO VARCHAR(3) NOT NULL
	private java.lang.String tipo_rapp_impiego;
 
	public Ass_rapp_impiegoBase() {
		super();
	}
	public Ass_rapp_impiegoBase(java.lang.String cd_rapp_impiego_sti) {
		super(cd_rapp_impiego_sti);
	}
	public java.lang.String getDs_rapp_impiego_sti() {
		return ds_rapp_impiego_sti;
	}
	public void setDs_rapp_impiego_sti(java.lang.String ds_rapp_impiego_sti)  {
		this.ds_rapp_impiego_sti=ds_rapp_impiego_sti;
	}
	public java.lang.String getTipo_rapp_impiego() {
		return tipo_rapp_impiego;
	}
	public void setTipo_rapp_impiego(java.lang.String tipo_rapp_impiego)  {
		this.tipo_rapp_impiego=tipo_rapp_impiego;
	}
}