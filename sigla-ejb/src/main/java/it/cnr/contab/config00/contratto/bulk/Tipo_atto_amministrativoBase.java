/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_atto_amministrativoBase extends Tipo_atto_amministrativoKey implements Keyed {
//    DS_TIPO_ATTO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_atto;
 
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

//	FL_NON_DEFINITO CHAR(1) NOT NULL
    private java.lang.Boolean fl_non_definito;
 
	public Tipo_atto_amministrativoBase() {
		super();
	}
	public Tipo_atto_amministrativoBase(java.lang.String cd_tipo_atto) {
		super(cd_tipo_atto);
	}
	public java.lang.String getDs_tipo_atto() {
		return ds_tipo_atto;
	}
	public void setDs_tipo_atto(java.lang.String ds_tipo_atto)  {
		this.ds_tipo_atto=ds_tipo_atto;
	}
	public java.lang.Boolean getFl_cancellato () {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
	/**
	 * @return
	 */
	public java.lang.Boolean getFl_non_definito() {
		return fl_non_definito;
	}
	
	/**
	 * @param boolean1
	 */
	public void setFl_non_definito(java.lang.Boolean boolean1) {
		fl_non_definito = boolean1;
	}

}