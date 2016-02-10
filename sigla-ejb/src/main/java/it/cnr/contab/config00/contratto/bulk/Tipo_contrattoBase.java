/*
* Creted by Generator 1.0
* Date 07/04/2005
*/
package it.cnr.contab.config00.contratto.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_contrattoBase extends Tipo_contrattoKey implements Keyed {
//    DS_TIPO_CONTRATTO VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_contratto;
 
//    TIPO_GESTIONE VARCHAR(1) NOT NULL
	private java.lang.String natura_contabile;
 
//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;

	//  FL_CIG CHAR(1) NOT NULL
	private java.lang.Boolean fl_cig;
 
	public Tipo_contrattoBase() {
		super();
	}
	public Tipo_contrattoBase(java.lang.String cd_tipo_contratto) {
		super(cd_tipo_contratto);
	}
	public java.lang.String getDs_tipo_contratto () {
		return ds_tipo_contratto;
	}
	public void setDs_tipo_contratto(java.lang.String ds_tipo_contratto)  {
		this.ds_tipo_contratto=ds_tipo_contratto;
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
	public java.lang.String getNatura_contabile() {
		return natura_contabile;
	}
	
	/**
	 * @param string
	 */
	public void setNatura_contabile(java.lang.String string) {
		natura_contabile = string;
	}
	public java.lang.Boolean getFl_cig() {
		return fl_cig;
	}
	public void setFl_cig(java.lang.Boolean fl_cig) {
		this.fl_cig = fl_cig;
	}

}