package it.cnr.contab.compensi00.tabrif.bulk;

import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_ti_rapp_ti_trattBulk extends Ass_ti_rapp_ti_trattBase {

	private it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk();
	private Tipo_trattamentoBulk tipoTrattamento = new Tipo_trattamentoBulk();
public Ass_ti_rapp_ti_trattBulk() {
	super();
}
public Ass_ti_rapp_ti_trattBulk(java.lang.String cd_tipo_rapporto,java.lang.String cd_trattamento) {
	super(cd_tipo_rapporto,cd_trattamento);
	setTipoRapporto(new it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk(cd_tipo_rapporto));
}
public java.lang.String getCd_tipo_rapporto() {
	it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk tipoRapporto = this.getTipoRapporto();
	if (tipoRapporto == null)
		return null;
	return tipoRapporto.getCd_tipo_rapporto();
}
public java.lang.String getCd_trattamento() {
	it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk tipoTrattamento = this.getTipoTrattamento();
	if (tipoTrattamento == null)
		return null;
	return tipoTrattamento.getCd_trattamento();
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
 * @return it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public Tipo_trattamentoBulk getTipoTrattamento() {
	return tipoTrattamento;
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
public boolean isROTipoTrattamento(){
	return (getTipoTrattamento() == null || getTipoTrattamento().getCrudStatus() == OggettoBulk.NORMAL);
}
public void setCd_tipo_rapporto(java.lang.String cd_tipo_rapporto) {
	this.getTipoRapporto().setCd_tipo_rapporto(cd_tipo_rapporto);
}
public void setCd_trattamento(java.lang.String cd_trattamento) {
	this.getTipoTrattamento().setCd_trattamento(cd_trattamento);
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
 * @param newTipoTrattamento it.cnr.contab.compensi00.tabrif.bulk.Tipo_trattamentoBulk
 */
public void setTipoTrattamento(Tipo_trattamentoBulk newTipoTrattamento) {
	tipoTrattamento = newTipoTrattamento;
}
}
