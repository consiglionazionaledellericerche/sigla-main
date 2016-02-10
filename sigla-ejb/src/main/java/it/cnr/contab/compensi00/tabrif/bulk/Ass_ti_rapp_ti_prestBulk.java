package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_prestBulk extends Ass_ti_rapp_ti_prestBase {

	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk();
	private Tipo_prestazione_compensoBulk tipoPrestazione = new Tipo_prestazione_compensoBulk();
public Ass_ti_rapp_ti_prestBulk() {
	super();
}
public Ass_ti_rapp_ti_prestBulk(java.lang.String cd_tipo_rapporto,java.lang.String cd_ti_prestazione) {
	super(cd_tipo_rapporto,cd_ti_prestazione);
	setTipoRapporto(new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk(cd_tipo_rapporto));
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
	if (tipoRapporto == null)
		return null;
	return tipoRapporto.getCd_tipo_rapporto();
}
public java.lang.String getCd_ti_prestazione() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk tipoPrestazione = this.getTipoPrestazione();
	if (tipoPrestazione == null)
		return null;
	return tipoPrestazione.getCd_ti_prestazione();
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk getTipoRapporto() {
	return tipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.58)
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk
 */
public Tipo_prestazione_compensoBulk getTipoPrestazione() {
	return tipoPrestazione;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public boolean isROTipoRapporto(){
	return (getTipoRapporto() == null || getTipoRapporto().getCrudStatus() == OggettoBulk.NORMAL);
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @return it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public boolean isROTipoPrestazione(){
	return (getTipoPrestazione() == null || getTipoPrestazione().getCrudStatus() == OggettoBulk.NORMAL);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
public void setCd_ti_prestazione(java.lang.String cd_ti_prestazione) {
	this.getTipoPrestazione().setCd_ti_prestazione(cd_ti_prestazione);
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.40)
 * @param newTipoRapporto it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk
 */
public void setTipoRapporto(it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk newTipoRapporto) {
	tipoRapporto = newTipoRapporto;
}
/**
 * Insert the method's description here.
 * Creation date: (05/03/2002 13.00.58)
 * @param newTipoPrestazione it.cnr.contab.compensi00.tabrif.bulk.Tipo_prestazione_compensoBulk
 */
public void setTipoPrestazione(Tipo_prestazione_compensoBulk newTipoPrestazione) {
	tipoPrestazione = newTipoPrestazione;
}
}
