/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_limiteBase extends Tipo_limiteKey implements Keyed {
//    DS_TIPO_LIMITE VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_limite;
 
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	public Tipo_limiteBase() {
		super();
	}
	public Tipo_limiteBase(java.lang.String cd_tipo_limite) {
		super(cd_tipo_limite);
	}
	public java.lang.String getDs_tipo_limite() {
		return ds_tipo_limite;
	}
	public void setDs_tipo_limite(java.lang.String ds_tipo_limite)  {
		this.ds_tipo_limite=ds_tipo_limite;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
}