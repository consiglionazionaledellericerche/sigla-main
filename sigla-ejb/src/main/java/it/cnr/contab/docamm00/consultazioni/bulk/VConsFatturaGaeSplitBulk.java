/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

/*
 * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 22/04/2010
 */
package it.cnr.contab.docamm00.consultazioni.bulk;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.Persistent;
import it.cnr.jada.util.action.CRUDBP;
public class VConsFatturaGaeSplitBulk extends OggettoBulk implements Persistent {
	private java.lang.String cdCds;
	  
	  private java.lang.String cdUnitaOrganizzativa;
	  
	  private java.lang.Integer esercizio;
	  
	  private java.lang.Integer mese;
	  
	  private java.lang.Long pgFatturaPassiva;
	  
	  private java.lang.Long progressivoRiga;
	  
	  private java.lang.Long pgObbligazione;
	  
	  private java.lang.Long pgObbligazioneScadenzario;
	  
	  private java.lang.String cdBeneServizio;
	  
	  private java.lang.String dsRigaFattura;
	  
	  private java.lang.String cdVoceIva;
	  
	  private java.lang.String cdElementoVoce;
	  
	  private java.lang.String cdCentroResponsabilita;
	  
	  private java.lang.String cdLineaAttivita;

	  private java.lang.String tiFattura;
	  
	  private java.lang.Long cdTerzo;
	  
	  private java.math.BigDecimal imImponibile;
	  
	  private java.math.BigDecimal imIva;

	  private java.math.BigDecimal imVoce;
	  
	  private java.math.BigDecimal imQuota;

	  private java.lang.Integer esercizioObbligazione;
	  
	  private java.lang.Integer esercizioOriObbligazione;
	  
private static java.util.Dictionary meseKeys = new it.cnr.jada.util.OrderedHashtable();
public static final int GENNAIO = 1;
public static final int FEBBRAIO = 2;
public static final int MARZO = 3;
public static final int APRILE = 4;
public static final int MAGGIO = 5;
public static final int GIUGNO = 6;
public static final int LUGLIO = 7;
public static final int AGOSTO = 8;
public static final int SETTEMBRE = 9;
public static final int OTTOBRE = 10;
public static final int NOVEMBRE = 11;
public static final int DICEMBRE = 12;

static {
	meseKeys.put(new Integer(GENNAIO),"Gennaio");
	meseKeys.put(new Integer(FEBBRAIO),"Febbraio");
	meseKeys.put(new Integer(MARZO),"Marzo");
	meseKeys.put(new Integer(APRILE),"Aprile");
	meseKeys.put(new Integer(MAGGIO),"Maggio");
	meseKeys.put(new Integer(GIUGNO),"Giugno");
	meseKeys.put(new Integer(LUGLIO),"Luglio");
	meseKeys.put(new Integer(AGOSTO),"Agosto");
	meseKeys.put(new Integer(SETTEMBRE),"Settembre");
	meseKeys.put(new Integer(OTTOBRE),"Ottobre");
	meseKeys.put(new Integer(NOVEMBRE),"Novembre");
	meseKeys.put(new Integer(DICEMBRE),"Dicembre");
}
/**
 * @param dictionary
 */
public final java.util.Dictionary getMeseKeys() {
	return meseKeys;
}
/**
 * @param dictionary
 */
public static void setMeseKeys(java.util.Dictionary dictionary) {
	meseKeys = dictionary;
}
public java.lang.String getCdCds() {
	return cdCds;
}
public void setCdCds(java.lang.String cdCds) {
	this.cdCds = cdCds;
}
public java.lang.String getCdUnitaOrganizzativa() {
	return cdUnitaOrganizzativa;
}
public void setCdUnitaOrganizzativa(java.lang.String cdUnitaOrganizzativa) {
	this.cdUnitaOrganizzativa = cdUnitaOrganizzativa;
}
public java.lang.Integer getEsercizio() {
	return esercizio;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.esercizio = esercizio;
}
public java.lang.Integer getMese() {
	return mese;
}
public void setMese(java.lang.Integer mese) {
	this.mese = mese;
}
public java.lang.Long getPgFatturaPassiva() {
	return pgFatturaPassiva;
}
public void setPgFatturaPassiva(java.lang.Long pgFatturaPassiva) {
	this.pgFatturaPassiva = pgFatturaPassiva;
}
public java.lang.Long getProgressivoRiga() {
	return progressivoRiga;
}
public void setProgressivoRiga(java.lang.Long progressivoRiga) {
	this.progressivoRiga = progressivoRiga;
}
public java.lang.Long getPgObbligazione() {
	return pgObbligazione;
}
public void setPgObbligazione(java.lang.Long pgObbligazione) {
	this.pgObbligazione = pgObbligazione;
}
public java.lang.Long getPgObbligazioneScadenzario() {
	return pgObbligazioneScadenzario;
}
public void setPgObbligazioneScadenzario(
		java.lang.Long pgObbligazioneScadenzario) {
	this.pgObbligazioneScadenzario = pgObbligazioneScadenzario;
}
public java.lang.String getCdBeneServizio() {
	return cdBeneServizio;
}
public void setCdBeneServizio(java.lang.String cdBeneServizio) {
	this.cdBeneServizio = cdBeneServizio;
}
public java.lang.String getDsRigaFattura() {
	return dsRigaFattura;
}
public void setDsRigaFattura(java.lang.String dsRigaFattura) {
	this.dsRigaFattura = dsRigaFattura;
}
public java.lang.String getCdVoceIva() {
	return cdVoceIva;
}
public void setCdVoceIva(java.lang.String cdVoceIva) {
	this.cdVoceIva = cdVoceIva;
}
public java.lang.String getCdElementoVoce() {
	return cdElementoVoce;
}
public void setCdElementoVoce(java.lang.String cdElementoVoce) {
	this.cdElementoVoce = cdElementoVoce;
}
public java.lang.String getCdCentroResponsabilita() {
	return cdCentroResponsabilita;
}
public void setCdCentroResponsabilita(java.lang.String cdCentroResponsabilita) {
	this.cdCentroResponsabilita = cdCentroResponsabilita;
}
public java.lang.String getCdLineaAttivita() {
	return cdLineaAttivita;
}
public void setCdLineaAttivita(java.lang.String cdLineaAttivita) {
	this.cdLineaAttivita = cdLineaAttivita;
}
public java.lang.String getTiFattura() {
	return tiFattura;
}
public void setTiFattura(java.lang.String tiFattura) {
	this.tiFattura = tiFattura;
}
public java.lang.Long getCdTerzo() {
	return cdTerzo;
}
public void setCdTerzo(java.lang.Long cdTerzo) {
	this.cdTerzo = cdTerzo;
}
public java.math.BigDecimal getImImponibile() {
	return imImponibile;
}
public void setImImponibile(java.math.BigDecimal imImponibile) {
	this.imImponibile = imImponibile;
}
public java.math.BigDecimal getImIva() {
	return imIva;
}
public void setImIva(java.math.BigDecimal imIva) {
	this.imIva = imIva;
}
public java.math.BigDecimal getImVoce() {
	return imVoce;
}
public void setImVoce(java.math.BigDecimal imVoce) {
	this.imVoce = imVoce;
}
public java.lang.Integer getEsercizioObbligazione() {
	return esercizioObbligazione;
}
public void setEsercizioObbligazione(java.lang.Integer esercizioObbligazione) {
	this.esercizioObbligazione = esercizioObbligazione;
}
public java.lang.Integer getEsercizioOriObbligazione() {
	return esercizioOriObbligazione;
}
public void setEsercizioOriObbligazione(java.lang.Integer esercizioOriObbligazione) {
	this.esercizioOriObbligazione = esercizioOriObbligazione;
}
public java.math.BigDecimal getImQuota() {
	return imQuota;
}
public void setImQuota(java.math.BigDecimal imQuota) {
	this.imQuota = imQuota;
}
public VConsFatturaGaeSplitBulk() {
	super();
}
public OggettoBulk initialize(CRUDBP bp, ActionContext context) {
	super.initialize(bp, context);
	setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context.getUserContext()));
	return this;
}
}