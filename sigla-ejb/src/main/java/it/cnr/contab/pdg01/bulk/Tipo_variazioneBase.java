/*
* Created by Generator 1.0
* Date 14/06/2006
*/
package it.cnr.contab.pdg01.bulk;
import it.cnr.jada.persistency.Keyed;
public class Tipo_variazioneBase extends Tipo_variazioneKey implements Keyed {
//    DS_TIPO_VARIAZIONE VARCHAR(100) NOT NULL
	private java.lang.String ds_tipo_variazione;
 
//    TI_TIPO_VARIAZIONE VARCHAR(10) NOT NULL
	private java.lang.String ti_tipo_variazione;
 
//    FL_UTILIZZABILE_ENTE CHAR(1) NOT NULL
	private java.lang.Boolean fl_utilizzabile_ente;
 
//    FL_UTILIZZABILE_AREA CHAR(1) NOT NULL
	private java.lang.Boolean fl_utilizzabile_area;
 
//    FL_UTILIZZABILE_CDS CHAR(1) NOT NULL
	private java.lang.Boolean fl_utilizzabile_cds;
 
//    TI_APPROVAZIONE CHAR(1) NOT NULL
	private java.lang.String ti_approvazione;
 
//    FL_VARIAZIONE_TRASFERIMENTO CHAR(1) NOT NULL
	private java.lang.Boolean fl_variazione_trasferimento;

	public Tipo_variazioneBase() {
		super();
	}
	public Tipo_variazioneBase(java.lang.Integer esercizio, java.lang.String cd_tipo_variazione) {
		super(esercizio, cd_tipo_variazione);
	}
	public java.lang.String getDs_tipo_variazione () {
		return ds_tipo_variazione;
	}
	public void setDs_tipo_variazione(java.lang.String ds_tipo_variazione)  {
		this.ds_tipo_variazione=ds_tipo_variazione;
	}
	public java.lang.String getTi_tipo_variazione () {
		return ti_tipo_variazione;
	}
	public void setTi_tipo_variazione(java.lang.String ti_tipo_variazione)  {
		this.ti_tipo_variazione=ti_tipo_variazione;
	}
	public java.lang.Boolean getFl_utilizzabile_ente () {
		return fl_utilizzabile_ente;
	}
	public void setFl_utilizzabile_ente(java.lang.Boolean fl_utilizzabile_ente)  {
		this.fl_utilizzabile_ente=fl_utilizzabile_ente;
	}
	public java.lang.Boolean getFl_utilizzabile_area () {
		return fl_utilizzabile_area;
	}
	public void setFl_utilizzabile_area(java.lang.Boolean fl_utilizzabile_area)  {
		this.fl_utilizzabile_area=fl_utilizzabile_area;
	}
	public java.lang.Boolean getFl_utilizzabile_cds () {
		return fl_utilizzabile_cds;
	}
	public void setFl_utilizzabile_cds(java.lang.Boolean fl_utilizzabile_cds)  {
		this.fl_utilizzabile_cds=fl_utilizzabile_cds;
	}
	public java.lang.String getTi_approvazione () {
		return ti_approvazione;
	}
	public void setTi_approvazione(java.lang.String ti_approvazione)  {
		this.ti_approvazione=ti_approvazione;
	}
	public java.lang.Boolean getFl_variazione_trasferimento() {
		return fl_variazione_trasferimento;
	}
	public void setFl_variazione_trasferimento(java.lang.Boolean fl_variazione_trasferimento) {
		this.fl_variazione_trasferimento = fl_variazione_trasferimento;
	}
}