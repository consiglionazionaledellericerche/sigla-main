package it.cnr.contab.fondecon00.core.bulk;

import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Ass_fondo_eco_mandatoBulk extends Ass_fondo_eco_mandatoBase {

	private MandatoIBulk mandato = null;
public Ass_fondo_eco_mandatoBulk() {
	super();
}
public Ass_fondo_eco_mandatoBulk(java.lang.String cd_cds,java.lang.String cd_codice_fondo,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio) {
	super(cd_cds,cd_codice_fondo,cd_unita_organizzativa,esercizio);
}
public java.lang.String getCd_cds_mandato() {
	it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	it.cnr.contab.config00.sto.bulk.CdsBulk cds = mandato.getCds();
	if (cds == null)
		return null;
	return cds.getCd_unita_organizzativa();
}
public java.lang.Integer getEsercizio_mandato() {
	it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	return mandato.getEsercizio();
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 4:30:30 PM)
 * @return it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public it.cnr.contab.doccont00.core.bulk.MandatoIBulk getMandato() {
	return mandato;
}
public java.lang.Long getPg_mandato() {
	it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = this.getMandato();
	if (mandato == null)
		return null;
	return mandato.getPg_mandato();
}
public void setCd_cds_mandato(java.lang.String cd_cds_mandato) {
	this.getMandato().getCds().setCd_unita_organizzativa(cd_cds_mandato);
}
public void setEsercizio_mandato(java.lang.Integer esercizio_mandato) {
	this.getMandato().setEsercizio(esercizio_mandato);
}
/**
 * Insert the method's description here.
 * Creation date: (5/15/2002 4:30:30 PM)
 * @param newMandato it.cnr.contab.doccont00.core.bulk.MandatoIBulk
 */
public void setMandato(it.cnr.contab.doccont00.core.bulk.MandatoIBulk newMandato) {
	mandato = newMandato;
}
public void setPg_mandato(java.lang.Long pg_mandato) {
	this.getMandato().setPg_mandato(pg_mandato);
}
}
