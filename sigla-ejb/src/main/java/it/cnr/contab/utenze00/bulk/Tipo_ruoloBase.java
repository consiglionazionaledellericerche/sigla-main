/*
* Created by Generator 1.0
* Date 09/01/2006
*/
package it.cnr.contab.utenze00.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_ruoloBase extends Tipo_ruoloKey implements Keyed {
//    DS_TIPO VARCHAR(50)
	private java.lang.String ds_tipo;

	public Tipo_ruoloBase() {
		super();
	}
	public Tipo_ruoloBase(java.lang.String tipo) {
		super(tipo);
	}
	public java.lang.String getDs_tipo () {
		return ds_tipo;
	}
	public void setDs_tipo(java.lang.String ds_tipo)  {
		this.ds_tipo=ds_tipo;
	}
}
