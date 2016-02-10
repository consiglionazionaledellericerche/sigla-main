/*
 * Created by Aurelio's BulkGenerator 1.0
 * Date 18/07/2007
 */
package it.cnr.contab.incarichi00.tabrif.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_prestazioneBase extends Tipo_prestazioneKey implements Keyed {
	//    DS_TIPO_PRESTAZIONE VARCHAR(200) NOT NULL
	private java.lang.String ds_tipo_prestazione;
 
	//    TIPO_CLASSIFICAZIONE CHAR(5) NOT NULL
	private java.lang.String tipo_classificazione;

	//    FL_CANCELLATO CHAR(1) NOT NULL
	private java.lang.Boolean fl_cancellato;
 
	public Tipo_prestazioneBase() {
		super();
	}
	public Tipo_prestazioneBase(java.lang.String cd_tipo_attivita) {
		super(cd_tipo_attivita);
	}
	public java.lang.String getDs_tipo_prestazione() {
		return ds_tipo_prestazione;
	}
	public void setDs_tipo_prestazione(java.lang.String ds_tipo_prestazione)  {
		this.ds_tipo_prestazione=ds_tipo_prestazione;
	}
	public java.lang.Boolean getFl_cancellato() {
		return fl_cancellato;
	}
	public void setFl_cancellato(java.lang.Boolean fl_cancellato)  {
		this.fl_cancellato=fl_cancellato;
	}
    public java.lang.String getTipo_classificazione() {
		return tipo_classificazione;
	}
    public void setTipo_classificazione(java.lang.String tipo_classificazione) {
		this.tipo_classificazione = tipo_classificazione;
	}
}