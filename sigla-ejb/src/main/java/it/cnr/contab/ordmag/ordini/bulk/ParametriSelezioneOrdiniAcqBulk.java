/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
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
 * Date 21/09/2017
 */
package it.cnr.contab.ordmag.ordini.bulk;

import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_persona_fisicaBulk;
import it.cnr.contab.config00.bulk.CigBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.Procedure_amministrativeBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.tabrif.bulk.CupBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.ordini.bp.ParametriSelezioneOrdiniAcqBP;
import it.cnr.jada.persistency.KeyedPersistent;
import it.cnr.jada.util.OrderedHashtable;

import java.sql.Timestamp;
import java.util.Dictionary;
import java.util.Enumeration;

public class ParametriSelezioneOrdiniAcqBulk extends AbilitazioneOrdiniAcqBulk implements KeyedPersistent{
	private static final long serialVersionUID = 1L;

	private NumerazioneOrdBulk numerazioneOrd = new NumerazioneOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaOrdine = new UnitaOperativaOrdBulk();
	private UnitaOperativaOrdBulk unitaOperativaRicevente = new UnitaOperativaOrdBulk();

	private TerzoBulk terzo = new TerzoBulk();

	private Bene_servizioBulk daBeneServizio = new Bene_servizioBulk();
	private Bene_servizioBulk aBeneServizio = new Bene_servizioBulk();

	private java.sql.Timestamp daDataOrdine;
	private java.sql.Timestamp aDataOrdine;
	private Integer daNumeroOrdine;
	private Integer aNumeroOrdine;
	private java.sql.Timestamp daDataOrdineDef;
	private java.sql.Timestamp aDataOrdineDef;
	private java.sql.Timestamp daDataPrevConsegna;
	private java.sql.Timestamp aDataPrevConsegna;
	private String tipoConsegna;
	private String statoOrdine;
	private String statoConsegna;

	private Obbligazione_scadenzarioBulk impegno =  new Obbligazione_scadenzarioBulk();
	private ContrattoBulk contratto= new ContrattoBulk();
	private CigBulk cig= new CigBulk();
	private CupBulk cup= new CupBulk();
	private V_persona_fisicaBulk rup = new V_persona_fisicaBulk();
	private Procedure_amministrativeBulk proceduraAmministrativa= new Procedure_amministrativeBulk();
	private TipoOrdineBulk tipoOrdine= new TipoOrdineBulk();

	public final static Dictionary STATO_ORDINE;

	static{
		STATO_ORDINE =( OrderedHashtable) ((OrderedHashtable )OrdineAcqBulk.STATO).clone();
		STATO_ORDINE.remove(OrdineAcqBulk.STATO_ANNULLATO);
	}


	public final static Dictionary STATO_CONSEGNA;
	static{
		STATO_CONSEGNA =( OrderedHashtable) ((OrderedHashtable )OrdineAcqConsegnaBulk.STATO).clone();
		STATO_CONSEGNA.remove(OrdineAcqConsegnaBulk.STATO_ANNULLATA);
	}

	public ParametriSelezioneOrdiniAcqBulk() {
		super();
	}

	public UnitaOperativaOrdBulk getUnitaOperativaRicevente() {
		return unitaOperativaRicevente;
	}

	public void setUnitaOperativaRicevente(UnitaOperativaOrdBulk unitaOperativaRicevente) {
		this.unitaOperativaRicevente = unitaOperativaRicevente;
	}

	public TerzoBulk getTerzo() {
		return terzo;
	}

	public void setTerzo(TerzoBulk terzo) {
		this.terzo = terzo;
	}

	public Bene_servizioBulk getDaBeneServizio() {
		return daBeneServizio;
	}

	public void setDaBeneServizio(Bene_servizioBulk daBeneServizio) {
		this.daBeneServizio = daBeneServizio;
	}

	public Bene_servizioBulk getaBeneServizio() {
		return aBeneServizio;
	}

	public void setaBeneServizio(Bene_servizioBulk aBeneServizio) {
		this.aBeneServizio = aBeneServizio;
	}

	public java.sql.Timestamp getDaDataOrdine() {
		return daDataOrdine;
	}

	public void setDaDataOrdine(java.sql.Timestamp daDataOrdine) {
		this.daDataOrdine = daDataOrdine;
	}

	public java.sql.Timestamp getaDataOrdine() {
		return aDataOrdine;
	}

	public void setaDataOrdine(java.sql.Timestamp aDataOrdine) {
		this.aDataOrdine = aDataOrdine;
	}

	public Integer getDaNumeroOrdine() {
		return daNumeroOrdine;
	}

	public void setDaNumeroOrdine(Integer daNumeroOrdine) {
		this.daNumeroOrdine = daNumeroOrdine;
	}

	public Integer getaNumeroOrdine() {
		return aNumeroOrdine;
	}

	public void setaNumeroOrdine(Integer aNumeroOrdine) {
		this.aNumeroOrdine = aNumeroOrdine;
	}

	public java.sql.Timestamp getDaDataOrdineDef() {
		return daDataOrdineDef;
	}

	public void setDaDataOrdineDef(java.sql.Timestamp daDataOrdineDef) {
		this.daDataOrdineDef = daDataOrdineDef;
	}

	public java.sql.Timestamp getaDataOrdineDef() {
		return aDataOrdineDef;
	}

	public void setaDataOrdineDef(java.sql.Timestamp aDataOrdineDef) {
		this.aDataOrdineDef = aDataOrdineDef;
	}
	public ParametriSelezioneOrdiniAcqBulk initializeForSearch(
			ParametriSelezioneOrdiniAcqBP bp,
			it.cnr.jada.action.ActionContext context) {

		return this;
	}

	public NumerazioneOrdBulk getNumerazioneOrd() {
		return numerazioneOrd;
	}

	public void setNumerazioneOrd(NumerazioneOrdBulk numerazioneOrd) {
		this.numerazioneOrd = numerazioneOrd;
	}

	public UnitaOperativaOrdBulk getUnitaOperativaOrdine() {
		return unitaOperativaOrdine;
	}

	public void setUnitaOperativaOrdine(UnitaOperativaOrdBulk unitaOperativaOrdine) {
		this.unitaOperativaOrdine = unitaOperativaOrdine;
	}

	public Timestamp getDaDataPrevConsegna() {
		return daDataPrevConsegna;
	}

	public void setDaDataPrevConsegna(Timestamp daDataPrevConsegna) {
		this.daDataPrevConsegna = daDataPrevConsegna;
	}

	public Timestamp getaDataPrevConsegna() {
		return aDataPrevConsegna;
	}

	public void setaDataPrevConsegna(Timestamp aDataPrevConsegna) {
		this.aDataPrevConsegna = aDataPrevConsegna;
	}

	public String getStatoOrdine() {
		return statoOrdine;
	}

	public void setStatoOrdine(String statoOrdine) {
		this.statoOrdine = statoOrdine;
	}

	public Dictionary getStatoOrdineKeys() {
		return STATO_ORDINE;
	}

	public String getTipoConsegna() {
		return tipoConsegna;
	}

	public void setTipoConsegna(String tipoConsegna) {
		this.tipoConsegna = tipoConsegna;
	}

	public Dictionary getTipoConsegnaKeys(){ return OrdineAcqConsegnaBulk.TIPO_CONSEGNA;}

	public String getStatoConsegna() {
		return statoConsegna;
	}

	public void setStatoConsegna(String statoConsegna) {
		this.statoConsegna = statoConsegna;
	}

	public Dictionary getStatoConsegnaKeys() {
		return STATO_CONSEGNA;
	}

	public Obbligazione_scadenzarioBulk getImpegno() {
		return impegno;
	}

	public void setImpegno(Obbligazione_scadenzarioBulk impegno) {
		this.impegno = impegno;
	}

	public ContrattoBulk getContratto() {
		return contratto;
	}

	public void setContratto(ContrattoBulk contratto) {
		this.contratto = contratto;
	}

	public CigBulk getCig() {
		return cig;
	}

	public void setCig(CigBulk cig) {
		this.cig = cig;
	}

	public CupBulk getCup() {
		return cup;
	}

	public void setCup(CupBulk cup) {
		this.cup = cup;
	}

	public V_persona_fisicaBulk getRup() {
		return rup;
	}

	public void setRup(V_persona_fisicaBulk rup) {
		this.rup = rup;
	}

	public Procedure_amministrativeBulk getProceduraAmministrativa() {
		return proceduraAmministrativa;
	}

	public void setProceduraAmministrativa(Procedure_amministrativeBulk proceduraAmministrativa) {
		this.proceduraAmministrativa = proceduraAmministrativa;
	}

	public TipoOrdineBulk getTipoOrdine() {
		return tipoOrdine;
	}

	public void setTipoOrdine(TipoOrdineBulk tipoOrdine) {
		this.tipoOrdine = tipoOrdine;
	}


}