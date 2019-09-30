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

package it.cnr.contab.docamm00.intrastat.bulk;


import java.math.BigDecimal;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Fattura_passiva_intraBulk extends Fattura_passiva_intraBase {

	private Natura_transazioneBulk natura_transazione = null;
	private Nomenclatura_combinataBulk nomenclatura_combinata = null;
	private Codici_cpaBulk codici_cpa=null;
	private Modalita_erogazioneBulk modalita_erogazione =null;
	private Modalita_incassoBulk modalita_incasso =null;
	private Modalita_trasportoBulk modalita_trasporto = null;
	private Condizione_consegnaBulk condizione_consegna = null;
	private Fattura_passivaBase fattura_passiva = null;
	public Fattura_passivaBase getFattura_passiva() {
		return fattura_passiva;
	}
	public void setFattura_passiva(Fattura_passivaBase fattura_passiva) {
		this.fattura_passiva = fattura_passiva;
	}
	private NazioneBulk nazione_origine = null;
	private NazioneBulk nazione_provenienza = null;
	private ProvinciaBulk provincia_destinazione = null;
	private java.util.Collection condizione_consegnaColl = null;
	private java.util.Collection modalita_trasportoColl = null;
	private java.util.Collection modalita_erogazioneColl = null;
	private java.util.Collection modalita_incassoColl = null;
public Fattura_passiva_intraBulk() {
	super();
	/*setFattura_passiva(new it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase());
	setNazione_origine(new NazioneBulk());
    setNazione_provenienza(new NazioneBulk());
    setProvincia_destinazione(new ProvinciaBulk());
	setNatura_transazione(new Natura_transazioneBulk());
	setNomenclatura_combinata(new Nomenclatura_combinataBulk());
	setCondizione_consegna(new Condizione_consegnaBulk());
	setModalita_trasporto(new Modalita_trasportoBulk());*/
}
public Fattura_passiva_intraBulk(java.lang.String cd_cds,java.lang.String cd_unita_organizzativa,java.lang.Integer esercizio,java.lang.Long pg_fattura_passiva,java.lang.Long pg_riga_intra) {
	super(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva,pg_riga_intra);
	setFattura_passiva(new it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBase(cd_cds,cd_unita_organizzativa,esercizio,pg_fattura_passiva));
}

public java.lang.String getCd_cds() {
	
	if (this.getFattura_passiva() == null)
		return null;
	return this.getFattura_passiva().getCd_cds();
}
public java.lang.Integer getEsercizio_cons_consegna() {
	it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaBulk condizione_consegna = this.getCondizione_consegna();
	if (condizione_consegna == null)
		return null;
	return condizione_consegna.getEsercizio();
}

public java.lang.String getCd_incoterm() {
	it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaBulk condizione_consegna = this.getCondizione_consegna();
	if (condizione_consegna == null)
		return null;
	return condizione_consegna.getCd_incoterm();
}
public java.lang.Integer getEsercizio_mod_trasporto() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoBulk modalita_trasporto = this.getModalita_trasporto();
	if (modalita_trasporto == null)
		return null;
	return modalita_trasporto.getEsercizio();
}
public java.lang.String getCd_modalita_trasporto() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoBulk modalita_trasporto = this.getModalita_trasporto();
	if (modalita_trasporto == null)
		return null;
	return modalita_trasporto.getCd_modalita_trasporto();
}
public java.lang.Long getId_natura_transazione() {
	it.cnr.contab.docamm00.intrastat.bulk.Natura_transazioneBulk natura_transazione = this.getNatura_transazione();
	if (natura_transazione == null)
		return null;
	return natura_transazione.getId_natura_transazione();
}
public java.lang.Long getId_nomenclatura_combinata() {
	it.cnr.contab.docamm00.intrastat.bulk.Nomenclatura_combinataBulk nomenclatura_combinata = this.getNomenclatura_combinata();
	if (nomenclatura_combinata == null)
		return null;
	return nomenclatura_combinata.getId_nomenclatura_combinata();
}
public java.lang.String getCd_provincia_destinazione() {
	it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk provincia_destinazione = this.getProvincia_destinazione();
	if (provincia_destinazione == null)
		return null;
	return provincia_destinazione.getCd_provincia();
}
public java.lang.String getCd_unita_organizzativa() {
	if (this.getFattura_passiva() == null)
		return null;
	return this.getFattura_passiva().getCd_unita_organizzativa();
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @return it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaBulk
 */
public Condizione_consegnaBulk getCondizione_consegna() {
	return condizione_consegna;
}
/**
 * Insert the method's description here.
 * Creation date: (4/5/2002 12:19:14 PM)
 * @return java.util.Collection
 */
public java.util.Collection getCondizione_consegnaColl() {
	return condizione_consegnaColl;
}
public java.lang.Integer getEsercizio() {
	
	if (this.getFattura_passiva() == null)
		return null;
	return this.getFattura_passiva().getEsercizio();
}

/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @return it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoBulk
 */
public Modalita_trasportoBulk getModalita_trasporto() {
	return modalita_trasporto;
}
/**
 * Insert the method's description here.
 * Creation date: (4/5/2002 12:19:14 PM)
 * @return java.util.Collection
 */
public java.util.Collection getModalita_trasportoColl() {
	return modalita_trasportoColl;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @return it.cnr.contab.docamm00.intrastat.bulk.Natura_transazioneBulk
 */
public Natura_transazioneBulk getNatura_transazione() {
	return natura_transazione;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazione_origine() {
	return nazione_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @return it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk getNazione_provenienza() {
	return nazione_provenienza;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @return it.cnr.contab.docamm00.intrastat.bulk.Nomenclatura_combinataBulk
 */
public Nomenclatura_combinataBulk getNomenclatura_combinata() {
	return nomenclatura_combinata;
}
public java.lang.Long getPg_fattura_passiva() {
	if (this.getFattura_passiva() == null)
		return null;
	return this.getFattura_passiva().getPg_fattura_passiva();
}
public java.lang.Long getPg_nazione_origine() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione_origine = this.getNazione_origine();
	if (nazione_origine == null)
		return null;
	return nazione_origine.getPg_nazione();
}
public java.lang.Long getPg_nazione_provenienza() {
	it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk nazione_provenienza = this.getNazione_provenienza();
	if (nazione_provenienza == null)
		return null;
	return nazione_provenienza.getPg_nazione();
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @return it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk
 */
public it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk getProvincia_destinazione() {
	return provincia_destinazione;
}
public void initialize() {

    setAmmontare_divisa(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
    setAmmontare_euro(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
    setValore_statistico(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));

    setMassa_netta(null);
    setUnita_supplementari(null);
    setNazione_origine(new NazioneBulk());
    setNazione_provenienza(new NazioneBulk());
    setProvincia_destinazione(new ProvinciaBulk());
	setNatura_transazione(new Natura_transazioneBulk());
	setNomenclatura_combinata(new Nomenclatura_combinataBulk());
	setCondizione_consegna(new Condizione_consegnaBulk());
	setModalita_trasporto(new Modalita_trasportoBulk());
	setFl_inviato(false);
}
public boolean isROcondizione_consegna() {
	
	return getCondizione_consegna() == null ||
			getCondizione_consegna().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROmodalita_trasporto() {
	
	return getModalita_trasporto() == null ||
			getModalita_trasporto().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROnatura_transazione() {
	
	return getNatura_transazione() == null ||
			getNatura_transazione().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROnazione_origine() {
	
	return getNazione_origine() == null ||
			getNazione_origine().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROnazione_provenienza() {
	
	return getNazione_provenienza() == null ||
			getNazione_provenienza().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROcpa() {
	
	return getCodici_cpa() == null ||
		getCodici_cpa().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROnomenclatura_combinata() {
	
	return getNomenclatura_combinata() == null ||
			getNomenclatura_combinata().getCrudStatus() == OggettoBulk.NORMAL;
}
public boolean isROprovincia_destinazione() {
	
	return getProvincia_destinazione() == null ||
			getProvincia_destinazione().getCrudStatus() == OggettoBulk.NORMAL;
}
public void setCd_cds(java.lang.String cd_cds) {
	this.getFattura_passiva().setCd_cds(cd_cds);
}

public void setEsercizio_cond_consegna(Integer esercizio_cond_consegna) {	
	this.getCondizione_consegna().setEsercizio(esercizio_cond_consegna);
}
public void setCd_incoterm(java.lang.String cd_incoterm) {
	this.getCondizione_consegna().setCd_incoterm(cd_incoterm);
}
public void setCd_modalita_trasporto(String cd_modalita_trasporto) {
	this.getModalita_trasporto().setCd_modalita_trasporto(cd_modalita_trasporto);
}
@Override
public void setEsercizio_mod_trasporto(Integer esercizio_mod_trasporto) {
	this.getModalita_trasporto().setEsercizio(esercizio_mod_trasporto);
}
@Override
public void setId_natura_transazione(Long id_natura_transazione) {
	this.getNatura_transazione().setId_natura_transazione(id_natura_transazione);
}
@Override
public void setId_nomenclatura_combinata(Long id_nomenclatura_combinata) {

	this.getNomenclatura_combinata().setId_nomenclatura_combinata(id_nomenclatura_combinata);
}
public void setCd_provincia_destinazione(java.lang.String cd_provincia_destinazione) {
	this.getProvincia_destinazione().setCd_provincia(cd_provincia_destinazione);
}
public void setCd_unita_organizzativa(java.lang.String cd_unita_organizzativa) {
	this.getFattura_passiva().setCd_unita_organizzativa(cd_unita_organizzativa);
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @param newCondizione_consegna it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaBulk
 */
public void setCondizione_consegna(Condizione_consegnaBulk newCondizione_consegna) {
	condizione_consegna = newCondizione_consegna;
}
/**
 * Insert the method's description here.
 * Creation date: (4/5/2002 12:19:14 PM)
 * @param newCondizione_consegnaColl java.util.Collection
 */
public void setCondizione_consegnaColl(java.util.Collection newCondizione_consegnaColl) {
	condizione_consegnaColl = newCondizione_consegnaColl;
}
public void setEsercizio(java.lang.Integer esercizio) {
	this.getFattura_passiva().setEsercizio(esercizio);
}

/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @param newModalita_trasporto it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoBulk
 */
public void setModalita_trasporto(Modalita_trasportoBulk newModalita_trasporto) {
	modalita_trasporto = newModalita_trasporto;
}
/**
 * Insert the method's description here.
 * Creation date: (4/5/2002 12:19:14 PM)
 * @param newModalita_trasportoColl java.util.Collection
 */
public void setModalita_trasportoColl(java.util.Collection newModalita_trasportoColl) {
	modalita_trasportoColl = newModalita_trasportoColl;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @param newNatura_transazione it.cnr.contab.docamm00.intrastat.bulk.Natura_transazioneBulk
 */
public void setNatura_transazione(Natura_transazioneBulk newNatura_transazione) {
	natura_transazione = newNatura_transazione;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @param newNazione_origine it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione_origine(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazione_origine) {
	nazione_origine = newNazione_origine;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @param newNazione_provenienza it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk
 */
public void setNazione_provenienza(it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk newNazione_provenienza) {
	nazione_provenienza = newNazione_provenienza;
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 11:52:16 AM)
 * @param newNomenclatura_combinata it.cnr.contab.docamm00.intrastat.bulk.Nomenclatura_combinataBulk
 */
public void setNomenclatura_combinata(Nomenclatura_combinataBulk newNomenclatura_combinata) {
	nomenclatura_combinata = newNomenclatura_combinata;
}
public void setPg_fattura_passiva(java.lang.Long pg_fattura_passiva) {
	this.getFattura_passiva().setPg_fattura_passiva(pg_fattura_passiva);
}
public void setPg_nazione_origine(java.lang.Long pg_nazione_origine) {
	this.getNazione_origine().setPg_nazione(pg_nazione_origine);
}
public void setPg_nazione_provenienza(java.lang.Long pg_nazione_provenienza) {
	this.getNazione_provenienza().setPg_nazione(pg_nazione_provenienza);
}
/**
 * Insert the method's description here.
 * Creation date: (4/3/2002 12:13:53 PM)
 * @param newProvincia_destinazione it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk
 */
public void setProvincia_destinazione(it.cnr.contab.anagraf00.tabter.bulk.ProvinciaBulk newProvincia_destinazione) {
	provincia_destinazione = newProvincia_destinazione;
}
public Codici_cpaBulk getCodici_cpa() {
	return codici_cpa;
}
public void setCodici_cpa(Codici_cpaBulk codici_cpa) {
	this.codici_cpa = codici_cpa;
}
public Modalita_erogazioneBulk getModalita_erogazione() {
	return modalita_erogazione;
}
public void setModalita_erogazione(Modalita_erogazioneBulk modalita_erogazione) {
	this.modalita_erogazione = modalita_erogazione;
}
public Modalita_incassoBulk getModalita_incasso() {
	return modalita_incasso;
}
public void setModalita_incasso(Modalita_incassoBulk modalita_incasso) {
	this.modalita_incasso = modalita_incasso;
}
public java.util.Collection getModalita_erogazioneColl() {
	return modalita_erogazioneColl;
}
public void setModalita_erogazioneColl(
		java.util.Collection modalita_erogazioneColl) {
	this.modalita_erogazioneColl = modalita_erogazioneColl;
}
public java.util.Collection getModalita_incassoColl() {
	return modalita_incassoColl;
}
public void setModalita_incassoColl(java.util.Collection modalita_incassoColl) {
	this.modalita_incassoColl = modalita_incassoColl;
}
public void setCd_modalita_incasso(String cd_modalita_incasso) {
	this.getModalita_incasso().setCd_modalita_incasso(cd_modalita_incasso);
}
@Override
public void setEsercizio_mod_incasso(Integer esercizio_mod_incasso) {
	this.getModalita_incasso().setEsercizio(esercizio_mod_incasso);
}
public void setCd_modalita_erogazione(String cd_modalita_erogazione) {
	this.getModalita_erogazione().setCd_modalita_erogazione(cd_modalita_erogazione);
}
@Override
public void setEsercizio_mod_erogazione(Integer esercizio_mod_erogazione) {
	this.getModalita_erogazione().setEsercizio(esercizio_mod_erogazione);
}
public java.lang.Integer getEsercizio_mod_incasso() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_incassoBulk modalita_incasso = this.getModalita_incasso();
	if (modalita_incasso == null)
		return null;
	return modalita_incasso.getEsercizio();
}
public java.lang.String getCd_modalita_incasso() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_incassoBulk modalita_incasso = this.getModalita_incasso();
	if (modalita_incasso == null)
		return null;
	return modalita_incasso.getCd_modalita_incasso();
}
public java.lang.Integer getEsercizio_mod_erogazione() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_erogazioneBulk modalita_erogazione = this.getModalita_erogazione();
	if (modalita_erogazione == null)
		return null;
	return modalita_erogazione.getEsercizio();
}
public java.lang.String getCd_modalita_erogazione() {
	it.cnr.contab.docamm00.intrastat.bulk.Modalita_erogazioneBulk modalita_erogazione = this.getModalita_erogazione();
	if (modalita_erogazione == null)
		return null;
	return modalita_erogazione.getCd_modalita_erogazione();
}
public java.lang.Long getId_cpa() {
	it.cnr.contab.docamm00.intrastat.bulk.Codici_cpaBulk codici_cpa = this.getCodici_cpa();
	if (codici_cpa == null)
		return null;
	return codici_cpa.getId_cpa();
}
@Override
public void setId_cpa(Long id_cpa) {
	this.getCodici_cpa().setId_cpa(id_cpa);
}
public boolean isInviato() {
	if( getFl_inviato()==null)
		return false;
	return getFl_inviato().booleanValue();
}
}
