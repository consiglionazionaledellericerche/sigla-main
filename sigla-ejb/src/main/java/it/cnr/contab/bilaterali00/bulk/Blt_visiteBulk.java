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
ù * Created by BulkGenerator 2.0 [07/12/2009]
 * Date 02/02/2012
 */
package it.cnr.contab.bilaterali00.bulk;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.doccont00.core.bulk.IDefferUpdateSaldi;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.action.ActionContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.action.CRUDBP;

import java.math.BigDecimal;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Blt_visiteBulk extends Blt_visiteBase implements IDefferUpdateSaldi{
	
	private Blt_autorizzati_dettBulk bltAutorizzatiDett =  new Blt_autorizzati_dettBulk();
	
	public TerzoBulk terzoEnte;
	
	public TerzoBulk getTerzoEnte() {
		return terzoEnte;
	}
	public void setTerzoEnte(TerzoBulk terzoEnte) {
		this.terzoEnte = terzoEnte;
	}
	
	public Integer getCdTerzoEnte() {
		if (this.getTerzoEnte()!=null)
			return this.getTerzoEnte().getCd_terzo();
		return null;
	}

	public void setCdTerzoEnte(Integer cd_terzo_ente) {
		this.getTerzoEnte().setCd_terzo(cd_terzo_ente);
	}
	
	
	private Blt_programma_visiteBulk bltProgrammaVisite =  new Blt_programma_visiteBulk();

	private Incarichi_repertorioBulk incaricoRepertorio =  new Incarichi_repertorioBulk();

	private Obbligazione_scadenzarioBulk obbligazioneScadenzario = new Obbligazione_scadenzarioBulk();
	
	public final static Dictionary competenzaResiduoKeys;
	
	static 
	{
		competenzaResiduoKeys = new it.cnr.jada.util.OrderedHashtable();
		competenzaResiduoKeys.put(ObbligazioneBulk.TIPO_COMPETENZA, "Competenza");
		competenzaResiduoKeys.put(ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO, "Residuo Improprio");
	};	

	private java.lang.String tipo_obbligazione;

	protected BancaBulk banca = new BancaBulk();
	
	protected Rif_modalita_pagamentoBulk modalitaPagamento = new Rif_modalita_pagamentoBulk();
	
    protected TerzoBulk cessionario;

	protected BancaBulk bancaAnticipo = new BancaBulk();
	
	protected Rif_modalita_pagamentoBulk modalitaPagamentoAnticipo = new Rif_modalita_pagamentoBulk();
	
    protected TerzoBulk cessionarioAnticipo;

    protected java.util.Collection modalitaPagamentoAnticipoOptions;
	
    protected java.util.Collection modalitaPagamentoSaldoOptions;

    protected List bancaOptions;

	public final static int FASE_UNDEFINED  = 0;
	public final static int FASE_PRIMA 	    = 1;
	public final static int FASE_SECONDA    = 2;
	public final static int FASE_TERZA 	    = 3;
	public final static int FASE_QUARTA	    = 4;
	public final static int FASE_QUINTA	    = 5;
	public final static int FASE_SESTA	    = 6;
	public final static int FASE_SETTIMA    = 7;
	public final static int FASE_OTTAVA     = 8;
	public final static int FASE_NONA       = 9;
	public final static int FASE_DECIMA     = 10;
	public final static int FASE_UNDICESIMA = 11;
	public final static int FASE_DODICESIMA = 12;
	public final static int FASE_TREDICESIMA = 13;
	public final static int FASE_QUATTORDICESIMA = 14;
	public final static int FASE_QUINDICESIMA = 15;
	public final static int FASE_SEDICESIMA = 16;
	public final static int FASE_DICIASSETTESIMA = 17;
	public final static int FASE_DICIOTTESIMA = 18;
	public final static int FASE_DICIANNOVESIMA = 19;
	public final static int FASE_VENTESIMA = 20;

	private int fase = FASE_UNDEFINED;

	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_VISITE
	 **/
	public Blt_visiteBulk() {
		super();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Table name: BLT_VISITE
	 **/
	public Blt_visiteBulk(java.lang.String cdAccordo, java.lang.String cdProgetto, java.lang.Integer cdTerzo, java.lang.Long pgAutorizzazione, java.lang.Long pgVisita) {
		super(cdAccordo, cdProgetto, cdTerzo, pgAutorizzazione, pgVisita);
		setBltAutorizzatiDett( new Blt_autorizzati_dettBulk(cdAccordo, cdProgetto, cdTerzo, pgAutorizzazione) );
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [null]
	 **/
	public Blt_autorizzati_dettBulk getBltAutorizzatiDett() {
		return bltAutorizzatiDett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [null]
	 **/
	public void setBltAutorizzatiDett(Blt_autorizzati_dettBulk bltAutorizzatiDett)  {
		this.bltAutorizzatiDett=bltAutorizzatiDett;
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdAccordo]
	 **/
	public java.lang.String getCdAccordo() {
		Blt_autorizzati_dettBulk bltAutorizzatiDett = this.getBltAutorizzatiDett();
		if (bltAutorizzatiDett == null)
			return null;
		return getBltAutorizzatiDett().getCdAccordo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdAccordo]
	 **/
	public void setCdAccordo(java.lang.String cdAccordo)  {
		this.getBltAutorizzatiDett().setCdAccordo(cdAccordo);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdProgetto]
	 **/
	public java.lang.String getCdProgetto() {
		Blt_autorizzati_dettBulk bltAutorizzatiDett = this.getBltAutorizzatiDett();
		if (bltAutorizzatiDett == null)
			return null;
		return getBltAutorizzatiDett().getCdProgetto();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdProgetto]
	 **/
	public void setCdProgetto(java.lang.String cdProgetto)  {
		this.getBltAutorizzatiDett().setCdProgetto(cdProgetto);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [annoVisita]
	 **/
	public java.lang.Integer getAnnoVisita() {
		Blt_autorizzati_dettBulk bltAutorizzatiDett = this.getBltAutorizzatiDett();
		if (bltAutorizzatiDett == null)
			return null;
		return getBltAutorizzatiDett().getAnnoVisita();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [annoVisita]
	 **/
	public void setAnnoVisita(java.lang.Integer annoVisita)  {
		this.getBltAutorizzatiDett().setAnnoVisita(annoVisita);
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Restituisce il valore di: [cdTerzo]
	 **/
	public java.lang.Integer getCdTerzo() {
		Blt_autorizzati_dettBulk bltAutorizzatiDett = this.getBltAutorizzatiDett();
		if (bltAutorizzatiDett == null)
			return null;
		return getBltAutorizzatiDett().getCdTerzo();
	}
	/**
	 * Created by BulkGenerator 2.0 [07/12/2009]
	 * Setta il valore di: [cdTerzo]
	 **/
	public void setCdTerzo(java.lang.Integer cdTerzo)  {
		this.getBltAutorizzatiDett().setCdTerzo(cdTerzo);
	}

	public Long getPgAutorizzazione() {
		Blt_autorizzati_dettBulk bltAutorizzatiDett = this.getBltAutorizzatiDett();
		if (bltAutorizzatiDett == null)
			return null;
		return getBltAutorizzatiDett().getPgAutorizzazione();
	}

	public void setPgAutorizzazione(Long pgAutorizzazione) {
		this.getBltAutorizzatiDett().setPgAutorizzazione(pgAutorizzazione);
	}

	public boolean isROAnnoVisita() {
		return this.getCdAccordo()!=null;
	}

	public boolean isROAccordo() {
		return this.getCdProgetto()!=null;
	}

	public boolean isROProgetto() {
		return this.getCdAccordo()==null||this.getCdTerzo()!=null;
	}

	public boolean isROTerzo() {
		return this.getCdProgetto()==null || isROPrimaFase();
	}
	
	public boolean isROProtocolliDocumentiCandidatura() {
		return !this.isSecondaFase() && !this.isTerzaFase();
	}

	public boolean isROProtocolliDocumentiContratto() {
		return !this.isQuartaFase() && !this.isQuintaFase();
	}

	public boolean isROProtocolloNotaAddebito() {
		return !this.isQuartaFase() && !this.isQuintaFase() &&
			   !this.isSedicesimaFase() && !isDiciassettesimaFase();
	}

	public boolean isROProtocolloProvvedimentoImpegno() {
		return (this.isVisitaDipendente() && !this.isSettimaFase() && !this.isOttavaFase()) ||
		       (this.isVisitaUniversitario() && !this.isUndicesimaFase() && !this.isDodicesimaFase()) ||
		       (this.isVisitaStraniero() && !this.isUndicesimaFase() && !this.isSettimaFase() && !this.isDodicesimaFase());
	}

	public boolean isROProtocolloRimborsoSpese() {
		return (this.isVisitaDipendente() && !this.isOttavaFase()) ||
		       (this.isVisitaUniversitario() && !this.isDodicesimaFase());
	}

	public boolean isROProtocolloAutorizzazionePartenza() {
		return (this.isVisitaDipendente() && !this.isQuartaFase() && !this.isQuintaFase()) ||
			   (this.isVisitaUniversitario() && !this.isSestaFase() && !this.isSettimaFase()) || 
			   (this.isVisitaStraniero() && !this.isVisitaPagataAdEnteStraniero() && !this.isSestaFase() && !this.isSettimaFase()) ||
			   (this.isVisitaStraniero() && this.isVisitaPagataAdEnteStraniero() && !this.isQuartaFase() && !this.isQuintaFase()) ;
	}

	public boolean isROModalitaPagamento() {
		return (this.isVisitaDipendente() && !this.isOttavaFase()) ||
			   (this.isVisitaUniversitario() && !this.isDodicesimaFase()) ||
			   (this.isVisitaStraniero() && !this.isAccordoPagataAdEnteStraniero() && !this.isDiciassettesimaFase()) &&
			   (this.isVisitaStraniero() && this.isVisitaPagataAdEnteStraniero() && !this.isNonaFase());
		
	}	
	public boolean isROProtocolloAttestatoSoggiorno() {
		return (this.isAccordoPagataAdEnteStraniero() && !this.isQuattordicesimaFase() && !this.isQuindicesimaFase() && !this.isDiciannovesimaFase()) &&
			    (this.isVisitaPagataAdEnteStraniero() && !this.isOttavaFase());
	}
	public boolean isROProtocolloAnticipoPagamento() {
		return !this.isUndicesimaFase() && !isTredicesimaFase() && !isQuindicesimaFase();
	}

	public boolean isROProtocolloPagamento() {
		return this.isVentesimaFase();
	}	

	public boolean isROProtocolloRinunciaVisita() {
		return this.getFlStampatoAnnProvvImpegno()!=null && this.getFlStampatoAnnProvvImpegno();
	}

	public Blt_programma_visiteBulk getBltProgrammaVisite() {
		return bltProgrammaVisite;
	}
	public void setBltProgrammaVisite(Blt_programma_visiteBulk bltProgrammaVisite) {
		this.bltProgrammaVisite = bltProgrammaVisite;
	}
	@Override
	public String getCdAccordoPrg() {
		Blt_programma_visiteBulk bltProgrammaVisite = this.getBltProgrammaVisite();
		if (bltProgrammaVisite == null)
			return null;
		return getBltProgrammaVisite().getCdAccordo();
	}
	@Override
	public void setCdAccordoPrg(String cdAccordoPrg) {
		this.getBltProgrammaVisite().setCdAccordo(cdAccordoPrg);
	}
	@Override
	public String getCdProgettoPrg() {
		Blt_programma_visiteBulk bltProgrammaVisite = this.getBltProgrammaVisite();
		if (bltProgrammaVisite == null)
			return null;
		return getBltProgrammaVisite().getCdProgetto();
	}
	@Override
	public void setCdProgettoPrg(String cdProgettoPrg) {
		this.getBltProgrammaVisite().setCdProgetto(cdProgettoPrg);
	}
	@Override
	public Long getPgRecordPrg() {
		Blt_programma_visiteBulk bltProgrammaVisite = this.getBltProgrammaVisite();
		if (bltProgrammaVisite == null)
			return null;
		return getBltProgrammaVisite().getPgRecord();
	}
	@Override
	public void setPgRecordPrg(Long pgRecordPrg) {
		this.getBltProgrammaVisite().setPgRecord(pgRecordPrg);
	}
	/**
	 * Fase di inserimento Dati Candidatura e viaggio
	 */
	public boolean isPrimaFase() {
		return getCurrentFase()==FASE_PRIMA;
	}
	/**
	 * Fase di inserimento protocollo Documenti Candidatura
	 */
	public boolean isSecondaFase() {
		return getCurrentFase()==FASE_SECONDA;
	}
	/**
	 * Fase di stampa lettera partenza e creazione Obbligazione
	 */
	public boolean isTerzaFase() {
		return getCurrentFase()==FASE_TERZA;
	}
	/**
	 * Fase di stampa provvedimento di impegno
	 */
	public boolean isQuartaFase() {
		return getCurrentFase()==FASE_QUARTA;
	}
	/**
	 * Fase di inserimento protocollo rimborso spese e stampa provvedimento pagamento
	 */
	public boolean isQuintaFase() {
		return getCurrentFase()==FASE_QUINTA;
	}
	/**
	 * Fase di inserimento protocollo rimborso spese e stampa provvedimento pagamento
	 */
	public boolean isSestaFase() {
		return getCurrentFase()==FASE_SESTA;
	}

	public boolean isSettimaFase() {
		return getCurrentFase()==FASE_SETTIMA;
	}
	
	
	public boolean isOttavaFase() {
		return getCurrentFase()==FASE_OTTAVA;
	}
	
	public boolean isNonaFase() {
		return getCurrentFase()==FASE_NONA;
	}
	
	public boolean isDecimaFase() {
		return getCurrentFase()==FASE_DECIMA;
	}

	public boolean isUndicesimaFase() {
		return getCurrentFase()==FASE_UNDICESIMA;
	}

	public boolean isDodicesimaFase() {
		return getCurrentFase()==FASE_DODICESIMA;
	}

	public boolean isTredicesimaFase() {
		return getCurrentFase()==FASE_TREDICESIMA;
	}

	public boolean isQuattordicesimaFase() {
		return getCurrentFase()==FASE_QUATTORDICESIMA;
	}

	public boolean isQuindicesimaFase() {
		return getCurrentFase()==FASE_QUINDICESIMA;
	}

	public boolean isSedicesimaFase() {
		return getCurrentFase()==FASE_SEDICESIMA;
	}

	public boolean isDiciassettesimaFase() {
		return getCurrentFase()==FASE_DICIASSETTESIMA;
	}

	public boolean isDiciottesimaFase() {
		return getCurrentFase()==FASE_DICIOTTESIMA;
	}

	public boolean isDiciannovesimaFase() {
		return getCurrentFase()==FASE_DICIANNOVESIMA;
	}

	public boolean isVentesimaFase() {
		return getCurrentFase()==FASE_VENTESIMA;
	}

	public boolean isVisitaAnnullata() {
		return getFlVisitaAnnullata()!=null && getFlVisitaAnnullata();
	}
	public boolean isROPrimaFase() {
		return isVisitaAnnullata() || (!isPrimaFase() && !isSecondaFase() && !isTerzaFase());
	}	
	
	public boolean isROSecondaFase() {
		return isVisitaAnnullata() || !isSecondaFase();
	}
	public boolean isROTerzaFase() {
		return isVisitaAnnullata() || !isTerzaFase();
	}
	public boolean isROQuartaFase() {
		return isVisitaAnnullata() || !isQuartaFase();
	}
	public boolean isROQuintaFase() {
		return isVisitaAnnullata() || !isQuintaFase();
	}
	public boolean isROSestaFase() {
		return isVisitaAnnullata() || !isSestaFase();
	}
	public boolean isROSettimaFase() {
		return isVisitaAnnullata() || !isSettimaFase();
	}
	public boolean isROOttavaFase() {
		return isVisitaAnnullata() || !isOttavaFase();
	}
	public boolean isRONonaFase() {
		return isVisitaAnnullata() || !isNonaFase();
	}
	public boolean isRODecimaFase() {
		return isVisitaAnnullata() || !isDecimaFase();
	}
	public boolean isROUndicesimaFase() {
		return isVisitaAnnullata() || !isUndicesimaFase();
	}
	public boolean isRODodicesimaFase() {
		return isVisitaAnnullata() || (!isDodicesimaFase() && !isTredicesimaFase());
	}
	public boolean isROTredicesimaFase() {
		return isVisitaAnnullata() || !isTredicesimaFase();
	}
	public boolean isROQuattordicesimaFase() {
		return isVisitaAnnullata() || !isQuattordicesimaFase();
	}
	public boolean isROQuindicesimaFase() {
		return isVisitaAnnullata() || !isQuindicesimaFase();
	}
	public Incarichi_repertorioBulk getIncaricoRepertorio() {
		return incaricoRepertorio;
	}
	public void setIncaricoRepertorio(Incarichi_repertorioBulk incaricoRepertorio) {
		this.incaricoRepertorio = incaricoRepertorio;
	}
	@Override
	public Integer getEsercizioRepertorio() {
		Incarichi_repertorioBulk incaricoRepertorio = this.getIncaricoRepertorio();
		if (incaricoRepertorio == null)
			return null;
		return getIncaricoRepertorio().getEsercizio();
	}
	@Override
	public void setEsercizioRepertorio(Integer esercizioRepertorio) {
		this.getIncaricoRepertorio().setEsercizio(esercizioRepertorio);
	}
	@Override
	public Long getPgRepertorio() {
		Incarichi_repertorioBulk incaricoRepertorio = this.getIncaricoRepertorio();
		if (incaricoRepertorio == null)
			return null;
		return getIncaricoRepertorio().getPg_repertorio();
	}
	@Override
	public void setPgRepertorio(Long pgRepertorio) {
		this.getIncaricoRepertorio().setPg_repertorio(pgRepertorio);
	}
    
	public Obbligazione_scadenzarioBulk getObbligazioneScadenzario() {
		return obbligazioneScadenzario;
	}
	public void setObbligazioneScadenzario(Obbligazione_scadenzarioBulk obbligazioneScadenzario) {
		this.obbligazioneScadenzario = obbligazioneScadenzario;
	}
	@Override
	public String getCdCdsObblig() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getCd_cds();
	}
	@Override
	public void setCdCdsObblig(String cdCdsObblig) {
		this.getObbligazioneScadenzario().setCd_cds(cdCdsObblig);
	}
	@Override
	public Integer getEsercizioObblig() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio();
	}
	@Override
	public void setEsercizioObblig(Integer esercizioObblig) {
		this.getObbligazioneScadenzario().setEsercizio(esercizioObblig);
	}
	@Override
	public Integer getEsercizioOriObblig() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getEsercizio_originale();
	}
	@Override
	public void setEsercizioOriObblig(Integer esercizioOriObblig) {
		this.getObbligazioneScadenzario().setEsercizio_originale(esercizioOriObblig);
	}
	@Override
	public Long getPgObbligazione() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione();
	}
	@Override
	public void setPgObbligazione(Long pgObbligazione) {
		this.getObbligazioneScadenzario().setPg_obbligazione(pgObbligazione);
	}
	@Override
	public Long getPgObbligazioneScadenzario() {
		Obbligazione_scadenzarioBulk obbligazioneScadenzario = this.getObbligazioneScadenzario();
		if (obbligazioneScadenzario == null)
			return null;
		return getObbligazioneScadenzario().getPg_obbligazione_scadenzario();
	}
	@Override
	public void setPgObbligazioneScadenzario(Long pgObbligazioneScadenzario) {
		this.getObbligazioneScadenzario().setPg_obbligazione_scadenzario(pgObbligazioneScadenzario);
	}

	private PrimaryKeyHashMap deferredSaldi = new PrimaryKeyHashMap();

	public void addToDefferredSaldi(IDocumentoContabileBulk docCont, Map values) {
		if (docCont != null) {
			if (deferredSaldi == null)
				deferredSaldi = new PrimaryKeyHashMap();
			if (!deferredSaldi.containsKey(docCont))
				deferredSaldi.put(docCont, values);
			else {
				Map firstValues = (Map)deferredSaldi.get(docCont);
				deferredSaldi.remove(docCont);
				deferredSaldi.put(docCont, firstValues);
			}
		}
	}
	public PrimaryKeyHashMap getDefferredSaldi() {
		return deferredSaldi;
	}
	public IDocumentoContabileBulk getDefferredSaldoFor(IDocumentoContabileBulk docCont) {
		if (docCont != null && deferredSaldi != null)
			for (Iterator i = deferredSaldi.keySet().iterator(); i.hasNext();) {
				IDocumentoContabileBulk key = (IDocumentoContabileBulk)i.next();
				if (((OggettoBulk)docCont).equalsByPrimaryKey((OggettoBulk)key))
					return key;
			}
		return null;	
	}
	public void removeFromDefferredSaldi(IDocumentoContabileBulk docCont) {
		if (docCont != null && deferredSaldi != null &&
				deferredSaldi.containsKey(docCont))
					deferredSaldi.remove(docCont);
	}
	public void resetDefferredSaldi() {
		deferredSaldi = null;	
	}

	public java.lang.String getTipo_obbligazione() {
		return tipo_obbligazione;
	}
	public void setTipo_obbligazione(java.lang.String tipo_obbligazione) {
		this.tipo_obbligazione = tipo_obbligazione;
	}
	
	public BancaBulk getBancaAnticipo() {
		return bancaAnticipo;
	}
	public void setBancaAnticipo(BancaBulk bancaAnticipo) {
		this.bancaAnticipo = bancaAnticipo;
	}
	@Override
	public Integer getCdTerzoBancaAnt() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk bancaAnticipo = this.getBancaAnticipo();
		if (bancaAnticipo == null)
			return null;
		return this.getBancaAnticipo().getCd_terzo();
	}
	@Override
	public void setCdTerzoBancaAnt(Integer cdTerzoBancaAnt) {
		this.getBancaAnticipo().setCd_terzo(cdTerzoBancaAnt);
	}
	@Override
	public Long getPgBancaAnt() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk bancaAnticipo = this.getBancaAnticipo();
		if (bancaAnticipo == null)
			return null;
		return this.getBancaAnticipo().getPg_banca();
	}
	@Override
	public void setPgBancaAnt(Long pgBancaAnt) {
		this.getBancaAnticipo().setPg_banca(pgBancaAnt);
	}
	public Rif_modalita_pagamentoBulk getModalitaPagamentoAnticipo() {
		return modalitaPagamentoAnticipo;
	}
	public void setModalitaPagamentoAnticipo(Rif_modalita_pagamentoBulk modalitaPagamentoAnticipo) {
		this.modalitaPagamentoAnticipo = modalitaPagamentoAnticipo;
	}
	@Override
	public String getCdModalitaPagAnt() {
		Rif_modalita_pagamentoBulk bancaAnticipo = this.getModalitaPagamentoAnticipo();
		if (bancaAnticipo == null)
			return null;
		return this.getModalitaPagamentoAnticipo().getCd_modalita_pag();
	}
	@Override
	public void setCdModalitaPagAnt(String cdModalitaPagAnt) {
		this.getModalitaPagamentoAnticipo().setCd_modalita_pag(cdModalitaPagAnt);
	}
	public TerzoBulk getCessionarioAnticipo() {
		return cessionarioAnticipo;
	}
	public void setCessionarioAnticipo(TerzoBulk cessionarioAnticipo) {
		this.cessionarioAnticipo = cessionarioAnticipo;
	}

	public BancaBulk getBanca() {
		return banca;
	}
	public void setBanca(BancaBulk banca) {
		this.banca = banca;
	}
	@Override
	public Integer getCdTerzoBanca() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return this.getBanca().getCd_terzo();
	}
	@Override
	public void setCdTerzoBanca(Integer cdTerzoBanca) {
		this.getBanca().setCd_terzo(cdTerzoBanca);
	}
	@Override
	public Long getPgBanca() {
		it.cnr.contab.anagraf00.core.bulk.BancaBulk banca = this.getBanca();
		if (banca == null)
			return null;
		return this.getBanca().getPg_banca();
	}
	@Override
	public void setPgBanca(Long pgBanca) {
		this.getBanca().setPg_banca(pgBanca);
	}
	public Rif_modalita_pagamentoBulk getModalitaPagamento() {
		return modalitaPagamento;
	}
	public void setModalitaPagamento(Rif_modalita_pagamentoBulk modalitaPagamento) {
		this.modalitaPagamento = modalitaPagamento;
	}
	@Override
	public String getCdModalitaPag() {
		Rif_modalita_pagamentoBulk banca = this.getModalitaPagamento();
		if (banca == null)
			return null;
		return this.getModalitaPagamento().getCd_modalita_pag();
	}
	@Override
	public void setCdModalitaPag(String cdModalitaPag) {
		this.getModalitaPagamento().setCd_modalita_pag(cdModalitaPag);
	}

	public java.util.Collection getModalitaPagamentoAnticipoOptions() {
		return modalitaPagamentoAnticipoOptions;
	}
	public void setModalitaPagamentoAnticipoOptions(java.util.Collection modalitaPagamentoAnticipoOptions) {
		this.modalitaPagamentoAnticipoOptions = modalitaPagamentoAnticipoOptions;
	}
	public java.util.Collection getModalitaPagamentoSaldoOptions() {
		return modalitaPagamentoSaldoOptions;
	}
	public void setModalitaPagamentoSaldoOptions(java.util.Collection modalitaPagamentoSaldoOptions) {
		this.modalitaPagamentoSaldoOptions = modalitaPagamentoSaldoOptions;
	}
	public List getBancaOptions() {
		return bancaOptions;
	}
	public void setBancaOptions(List bancaOptions) {
		this.bancaOptions = bancaOptions;
	}
	public TerzoBulk getCessionario() {
		return cessionario;
	}
	public void setCessionario(TerzoBulk cessionario) {
		this.cessionario = cessionario;
	}

	public String getStatoText(){
		if (this.isInFaseAnnullamento())
			return "IN FASE DI ANNULLAMENTO";
		if (this.isVisitaAnnullata())
			return "ANNULLATA";
		if (this.isToBeCreated()||this.getCrudStatus()==OggettoBulk.UNDEFINED)
			return "";
		if (this.isPrimaFase())
			return "IN FASE DI STAMPA DOCUMENTI CANDIDATURA";
		if (this.isSecondaFase())
			return "IN FASE DI PROTOCOLLO DOCUMENTI CANDIDATURA";
		if (this.isVisitaDipendente()){
			if (this.isTerzaFase())
				return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isQuartaFase())
				return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isQuintaFase())
				return "IN FASE DI CREAZIONE IMPEGNO";
			if (this.isSestaFase())
				return "IN FASE DI STAMPA PROVVEDIMENTO IMPEGNO";
			if (this.isSettimaFase())
				return "IN FASE DI PROTOCOLLO PROVVEDIMENTO IMPEGNO";
			if (this.isOttavaFase())
				return "IN FASE DI RIMBORSO SPESE";
			if (this.isNonaFase())
				return "IN FASE DI PROTOCOLLO PROVVEDIMENTO PAGAMENTO";
		} else if (this.isVisitaUniversitario()){
			if (this.isTerzaFase())
				return "IN FASE DI STAMPA DOCUMENTI CONTRATTO";
			if (this.isQuartaFase())
				return "IN FASE DI PROTOCOLLO DOCUMENTI CONTRATTO";
			if (this.isQuintaFase())
				return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isSestaFase())
				return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isSettimaFase())
				return "IN FASE DI CREAZIONE INCARICO";
			if (this.isOttavaFase())
				return "IN FASE DI CHIUSURA DEFINITIVA INCARICO";
			if (this.isNonaFase())
				return "IN FASE DI CREAZIONE IMPEGNO";
			if (this.isDecimaFase())
				return "IN FASE DI STAMPA PROVVEDIMENTO IMPEGNO";
			if (this.isUndicesimaFase())
				return "IN FASE DI PROTOCOLLO PROVVEDIMENTO IMPEGNO";
			if (this.isDodicesimaFase())
				return "IN FASE DI RIMBORSO SPESE";
			if (this.isTredicesimaFase())
				return "IN FASE DI PROTOCOLLO PROVVEDIMENTO PAGAMENTO";
		} else if (this.isVisitaStraniero()){
			if (this.isAccordoPagataAdEnteStraniero()) {
				if (this.isTerzaFase())
					return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isQuartaFase())
					return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isQuintaFase())
					return "IN FASE DI CREAZIONE IMPEGNO";
				if (this.isSestaFase())
					return "IN FASE DI PAGAMENTO SALDO";
				if (this.isSettimaFase())
					return "IN FASE DI PROTOCOLLO PAGAMENTO SALDO";
				if (this.isOttavaFase())
					return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
			} 
			
			if (this.isVisitaPagataAdEnteStraniero()) {
				if (this.isTerzaFase())
					return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isQuartaFase())
					return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isQuintaFase())
					return "IN FASE DI CREAZIONE IMPEGNO";
				if (this.isSestaFase())
					return "IN FASE DI STAMPA PROVVEDIMENTO IMPEGNO";
				if (this.isSettimaFase())
					return "IN FASE DI PROTOCOLLO PROVVEDIMENTO IMPEGNO";
				if (this.isOttavaFase())
					return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
				if (this.isNonaFase())
						return "IN FASE DI PAGAMENTO SALDO";
				if (this.isDecimaFase()) 
					return "IN FASE DI PROTOCOLLO PAGAMENTO SALDO";
					
				
				}
			
			
			if (this.getFlPagamentoFineVisita()) {
			if (this.isTerzaFase())
				return "IN FASE DI STAMPA DOCUMENTI CONTRATTO";
			if (this.isQuartaFase())
				return "IN FASE DI PROTOCOLLO DOCUMENTI CONTRATTO";
			if (this.isQuintaFase())
				return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isSestaFase())
				return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
			if (this.isSettimaFase())
				return "IN FASE DI CREAZIONE INCARICO";
			if (this.isOttavaFase())
				return "IN FASE DI CHIUSURA DEFINITIVA INCARICO";
			if (this.isNonaFase())
				return "IN FASE DI CREAZIONE IMPEGNO";
			if (this.isDecimaFase())
				return "IN FASE DI STAMPA PROVVEDIMENTO IMPEGNO";
			if (this.isUndicesimaFase())
				return "IN FASE DI PROTOCOLLO PROVVEDIMENTO IMPEGNO";
			if (this.isDodicesimaFase())
				return "IN FASE DI PAGAMENTO ANTICIPO";
			if (this.isTredicesimaFase())
				return "IN FASE DI PROTOCOLLO PAGAMENTO ANTICIPO";
			if (this.isQuattordicesimaFase())
				return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
			if (this.isQuindicesimaFase())
				return "IN FASE DI STAMPA NOTA ADDEBITO SALDO";
			if (this.isSedicesimaFase())
				return "IN FASE DI PROTOCOLLO NOTA ADDEBITO SALDO";
			if (this.isDiciassettesimaFase()){
				if (getImRimbSpese().compareTo(BigDecimal.ZERO)==1) 
					return "IN FASE DI PAGAMENTO SALDO";
				else 
					return "IN FASE DI STAMPA RIMBORSO";
			}
			if (this.isDiciottesimaFase()) {
				if (getImRimbSpese().compareTo(BigDecimal.ZERO)==1) 
					return "IN FASE DI PROTOCOLLO PAGAMENTO SALDO";
				else 
					return "IN FASE DI PROTOCOLLO RIMBORSO";
			}
			if (this.isDiciannovesimaFase())
				return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
			}
				if (this.isTerzaFase()) 
					return "IN FASE DI STAMPA DOCUMENTI CONTRATTO";
				if (this.isQuartaFase())
					return "IN FASE DI PROTOCOLLO DOCUMENTI CONTRATTO";
				if (this.isQuintaFase())
					return "IN FASE DI STAMPA AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isSestaFase())
					return "IN FASE DI PROTOCOLLO AUTORIZZAZIONE ALLA PARTENZA";
				if (this.isSettimaFase())
					return "IN FASE DI CREAZIONE INCARICO";
				if (this.isOttavaFase())
					return "IN FASE DI CHIUSURA DEFINITIVA INCARICO";
				if (this.isNonaFase())
					return "IN FASE DI CREAZIONE IMPEGNO";
				if (this.isDodicesimaFase())
					return "IN FASE DI PAGAMENTO ANTICIPO";
				if (this.isTredicesimaFase())
					return "IN FASE DI PROTOCOLLO PAGAMENTO ANTICIPO";
				if (this.isQuattordicesimaFase())
					return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
				if (this.isQuindicesimaFase())
					return "IN FASE DI STAMPA NOTA ADDEBITO SALDO";
				if (this.isSedicesimaFase())
					return "IN FASE DI PROTOCOLLO NOTA ADDEBITO SALDO";
				if (this.isDiciassettesimaFase()){
					if (getImRimbSpese().compareTo(BigDecimal.ZERO)==1) 
						return "IN FASE DI PAGAMENTO SALDO";
					else 
						return "IN FASE DI STAMPA RIMBORSO";
				}
				if (this.isDiciottesimaFase()) {
					if (getImRimbSpese().compareTo(BigDecimal.ZERO)==1) 
						return "IN FASE DI PROTOCOLLO PAGAMENTO SALDO";
					else 
						return "IN FASE DI PROTOCOLLO RIMBORSO";
				}
				if (this.isDiciannovesimaFase())
					return "IN FASE DI PROTOCOLLO ATTESTATO DI SOGGIORNO";
				}
		
		if (this.isVentesimaFase())
			return "VISITA CHIUSA";
		return "";
	} 
	private boolean inFaseAnnullamento=Boolean.FALSE;
	public void setInFaseAnnullamento(boolean inFaseAnnullamento) {
		this.inFaseAnnullamento = inFaseAnnullamento;
	}
	public boolean isInFaseAnnullamento() {
		return inFaseAnnullamento;
	}
	public boolean isVisitaItaliano() {
		return this.getBltAutorizzatiDett()!=null && this.getBltAutorizzatiDett().getBltAutorizzati()!=null &&
			   this.getBltAutorizzatiDett().getBltAutorizzati().getTiItalianoEstero()!=null &&
  			   this.getBltAutorizzatiDett().getBltAutorizzati().getTiItalianoEstero().equals(NazioneBulk.ITALIA);
	}
	public boolean isVisitaStraniero() {
		return this.getBltAutorizzatiDett()!=null && this.getBltAutorizzatiDett().getBltAutorizzati()!=null &&
			   this.getBltAutorizzatiDett().getBltAutorizzati().getTiItalianoEstero()!=null &&
  			  !this.getBltAutorizzatiDett().getBltAutorizzati().getTiItalianoEstero().equals(NazioneBulk.ITALIA);
	}
	public boolean isVisitaDipendente() {
		return isVisitaItaliano() && this.getBltAutorizzatiDett().getBltAutorizzati().getFlAssimilatoDip();
	}
	public boolean isVisitaUniversitario() {
		return isVisitaItaliano() && !this.getBltAutorizzatiDett().getBltAutorizzati().getFlAssimilatoDip();
	}
	public boolean isVisitaPagataAdEnteStraniero() {
		return (isVisitaStraniero() && this.getFlPagamentoEnte()) || isAccordoPagataAdEnteStraniero();
	}
	
	public boolean isAccordoPagataAdEnteStraniero() {
		return isVisitaStraniero() && this.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getFl_pagamento_ente();
	}

	public Long getNumGiorniVisita() {
		return DateUtils.daysBetweenDates(this.getDtIniVisita(), this.getDtFinVisita())+1;
	}
	public boolean isAnticipoPrevisto() {
		return !isVisitaDipendente() && 
			   !getFlPagamentoFineVisita() &&
			   !isVisitaPagataAdEnteStraniero() &&
			   (getFlStampatoProvvPagamAnt() ||
			    (getBltAutorizzatiDett()!=null &&
 	    		 getBltAutorizzatiDett().getBltAutorizzati()!=null &&
 	    		 getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti()!=null &&
 	    		 getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo()!=null && 
 	    		 getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo()!=null &&
 	    		 getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo().compareTo(BigDecimal.ZERO)==1 &&
			     getImRimbPrevisto()!=null && 
			     getImRimbPrevisto().compareTo(getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getImporto_max_anticipo())==1));
	}

	public boolean isConvenzioneAccettata() {
		return getFlAccettazioneConvenzione()!=null && getFlAccettazioneConvenzione();
	}

	public BigDecimal getImRitenutaFiscalePrevisto() {
		return getImRitenutaFiscale(getImRimbPrevisto());
	}

	public BigDecimal getImContributiPrevisto() {
		return getImContributi(getImRimbPrevisto());
	}

	public BigDecimal getImRimborsoLordoPrevisto() {
		return getImRimborsoLordo(getImRimbPrevisto());
	}

	public BigDecimal getImRitenutaFiscaleAnticipo() {
		return getImRitenutaFiscale(getImRimbSpeseAnt());
	}

	public BigDecimal getImContributiAnticipo() {
		return getImContributi(getImRimbSpeseAnt());
	}

	public BigDecimal getImRimborsoLordoAnticipo() {
		return getImRimborsoLordo(getImRimbSpeseAnt());
	}

	public BigDecimal getImRitenutaFiscaleSaldo() {
		return getImRitenutaFiscale(getImRimbSpese());
	}

	public BigDecimal getImContributiSaldo() {
		return getImContributi(getImRimbSpese());
	}

	public BigDecimal getImRimborsoLordoSaldo() {
		return getImRimborsoLordo(getImRimbSpese());
	}
	
	public BigDecimal getImRimborsoLordoTotale() {
		return getImRimborsoLordoAnticipo().add(getImRimborsoLordoSaldo());
	}

	public BigDecimal getImRimborsoTotaleNetto() {
		return Utility.nvl(getImRimbSpeseAnt()).add(Utility.nvl(getImRimbSpese()));
	}

	public BigDecimal getImRitenutaFiscaleTotale() {
		return getImRitenutaFiscaleAnticipo().add(getImRitenutaFiscaleSaldo());
	}

	public BigDecimal getImContributiTotale() {
		return getImContributiAnticipo().add(getImContributiSaldo());
	}

	public BigDecimal getImRimborsoTotaleAnticipo() {
		return getImRimborsoLordoAnticipo()
				.add(getImContributiAnticipo());
	}

	public BigDecimal getImRimborsoTotaleSaldo() {
		return getImRimborsoLordoSaldo()
				.add(getImContributiSaldo());
	}

	public BigDecimal getImRimborsoTotale() {
		return getImRimborsoTotaleAnticipo()
				.add(getImRimborsoTotaleSaldo());
	}

	private BigDecimal getImRitenutaFiscale(BigDecimal importo) {
		BigDecimal importoTemp = BigDecimal.ZERO;
		if (importo!=null && importo.compareTo(BigDecimal.ZERO)==1 && 
			this.getPrc_oneri_fiscali()!=null && this.getPrc_oneri_fiscali().compareTo(BigDecimal.ZERO)==1 &&
			(this.getFlAccettazioneConvenzione()==null || !this.isConvenzioneAccettata()))
//			importoTemp = importo.divide(BigDecimal.ONE.subtract(this.getPrc_oneri_fiscali().divide(BigDecimal.TEN.multiply(BigDecimal.TEN))),2, BigDecimal.ROUND_HALF_EVEN).subtract(importo);
			importoTemp = importo.multiply(this.getPrc_oneri_fiscali()).divide(BigDecimal.TEN.multiply(BigDecimal.TEN),2, BigDecimal.ROUND_HALF_EVEN);
		return importoTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	private BigDecimal getImContributi(BigDecimal importo) {
		BigDecimal importoTemp = BigDecimal.ZERO;
		if (importo!=null && importo.compareTo(BigDecimal.ZERO)==1 && 
			!this.isVisitaDipendente() && this.getPrc_oneri_contributivi().compareTo(BigDecimal.ZERO)==1)
			importoTemp = getImRimborsoLordo(importo).multiply(Utility.nvl(this.getPrc_oneri_contributivi()).divide(BigDecimal.TEN.multiply(BigDecimal.TEN)));
		return importoTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	private BigDecimal getImRimborsoLordo(BigDecimal importo) {
		BigDecimal importoTemp = BigDecimal.ZERO;
		if (importo!=null && importo.compareTo(BigDecimal.ZERO)==1)
			//importoTemp = importo.add(getImRitenutaFiscale(importo));
			importoTemp = importo;
		return importoTemp.setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

	public int getFase() {
		return fase;
	}

	public void setFase(int fase) {
		this.fase = fase;
	}
	
	@Override
	public OggettoBulk initializeForInsert(CRUDBP crudbp,
			ActionContext actioncontext) {
		this.setFlAutorizzDirettore(Boolean.FALSE); 
		this.setFlPagamentoEnte(Boolean.FALSE);
		return super.initializeForInsert(crudbp, actioncontext);
	} 

	public void initializeFase() {
		setFase(FASE_UNDEFINED);
		setFase(getCurrentFase());
	}

	public int getCurrentFase() {
		if (this.getFase()!=FASE_UNDEFINED)
			return this.getFase();
		if (this.getCrudStatus()==OggettoBulk.UNDEFINED)
			return FASE_UNDEFINED;
		if (this.isToBeCreated())
			return FASE_PRIMA;
		if (this.isVisitaAnnullata()||this.isInFaseAnnullamento())
			return FASE_QUINDICESIMA;
		if (this.isVisitaDipendente()) {
			if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura())
				return FASE_PRIMA;
			else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
				if (this.getNumProtDispFin()==null || this.getDtProtDispFin()==null ||
					//this.getNumProtAccettDispFin()==null || this.getDtProtAccettDispFin()==null ||
					this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
					this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null)
					return FASE_SECONDA;
				else
					return FASE_TERZA;
			} else if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null) {
					return FASE_QUARTA;
			} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
					   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
				return FASE_QUINTA;
			} else if (this.getFlStampatoProvvImpegno()==null || !this.getFlStampatoProvvImpegno()) {
				return FASE_SESTA;
			} else if (this.getNumProtProvvImpegno()==null || this.getDtProtProvvImpegno()==null) {
				return FASE_SETTIMA;
			} else if (this.getFlStampatoProvvPagamento()==null || !this.getFlStampatoProvvPagamento()) {
				return FASE_OTTAVA;
			} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
				return FASE_NONA;
			}
		} else if (this.isVisitaUniversitario()){
			if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura())
				return FASE_PRIMA;
			else if (this.getFlStampatoModelloContratto()==null || !this.getFlStampatoModelloContratto()) {
				if (this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
					this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null ||
					this.getNumProtDispFin()==null || this.getDtProtDispFin()==null ||
					//this.getNumProtAccettDispFin()==null || this.getDtProtAccettDispFin()==null ||
					this.getNumProtAttribIncarico()==null || this.getDtProtAttribIncarico()==null)
					return FASE_SECONDA;
				else
					return FASE_TERZA;
			} else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
				if (this.getNumProtContratto()==null || this.getDataProtContratto()==null)
					return FASE_QUARTA;
				else
					return FASE_QUINTA;
			} else if (this.getIncaricoRepertorio()==null || this.getIncaricoRepertorio().getPg_repertorio()==null) {
				if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null)
					return FASE_SESTA;
				else
					return FASE_SETTIMA;
			} else if (this.getIncaricoRepertorio().isIncaricoProvvisorio()) {
				return FASE_OTTAVA;
			} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
					   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
				return FASE_NONA;
			} else if (this.getFlStampatoProvvImpegno()==null || !this.getFlStampatoProvvImpegno()) {
				return FASE_DECIMA;
			} else if (this.getNumProtProvvImpegno()==null || this.getDtProtProvvImpegno()==null) {
				return FASE_UNDICESIMA;
			} else if (this.getFlStampatoProvvPagamento()==null || !this.getFlStampatoProvvPagamento()) {
				return FASE_DODICESIMA;
			} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
				return FASE_TREDICESIMA;
			}
		} else if (isVisitaStraniero()) {
			if (isAccordoPagataAdEnteStraniero()) {
				if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura())
					return FASE_PRIMA;
				else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
					if (this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
						this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null)
						return FASE_SECONDA;
					else
						return FASE_TERZA;
				} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
						   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
					if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null)
						return FASE_QUARTA;
					else
						return FASE_QUINTA;
				} else if (getFlStampatoProvvPagamento()==null || !getFlStampatoProvvPagamento()) {
					return FASE_SESTA;
				} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
					return FASE_SETTIMA;
				} else if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
					return FASE_OTTAVA;
				}
			} else if (this.isVisitaPagataAdEnteStraniero()) {
				if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura())
					return FASE_PRIMA;
				else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
					if (this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
						this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null)
						return FASE_SECONDA;
					else
						return FASE_TERZA;
				} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
						   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
					if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null)
						return FASE_QUARTA;
					else
						return FASE_QUINTA;
				} else if (this.getFlStampatoProvvImpegno()==null || !this.getFlStampatoProvvImpegno()) {
					return FASE_SESTA;
				} if (this.getNumProtProvvImpegno()==null || this.getDtProtProvvImpegno()==null) {
					return FASE_SETTIMA;
				} if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
					return FASE_OTTAVA;		
				} else if (getFlStampatoProvvPagamento()==null || !getFlStampatoProvvPagamento()) {
					return FASE_NONA;
				} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
					return FASE_DECIMA;		
				}
			} else if (this.getFlPagamentoFineVisita()) {
				if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura())
					return FASE_PRIMA;
				else if (this.getFlStampatoModelloContratto()==null || !this.getFlStampatoModelloContratto()) {
					if (this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
						this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null ||
						this.getNumProtAttribIncarico()==null || this.getDtProtAttribIncarico()==null)
						return FASE_SECONDA;
					else
						return FASE_TERZA;
				} else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
					if (this.getNumProtContratto()==null || this.getDataProtContratto()==null || 
						(this.isNotaAddebitoAnticipoRequired() && (this.getNumProtNotaAddebitoAnt()==null || this.getDtProtNotaAddebitoAnt()==null)) ||
						(this.isNotaAddebitoSaldoConAnticipoRequired() && (this.getNumProtNotaAddebito()==null || this.getDtProtNotaAddebito()==null)))
						return FASE_QUARTA;
					else
						return FASE_QUINTA;
				} else if (this.getIncaricoRepertorio()==null || this.getIncaricoRepertorio().getPg_repertorio()==null) {
					if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null)
						return FASE_SESTA;
					else
						return FASE_SETTIMA;
				} else if (this.getIncaricoRepertorio().isIncaricoProvvisorio()) {
					return FASE_OTTAVA;
				} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
						   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
					return FASE_NONA;
				} else if (this.getFlStampatoProvvImpegno()==null || !this.getFlStampatoProvvImpegno()) {
					return FASE_DECIMA;
				} if (this.getNumProtProvvImpegno()==null || this.getDtProtProvvImpegno()==null) {
					return FASE_UNDICESIMA;
				} if (this.isAnticipoPrevisto() && 
						   (this.getNumProtProvvPagamAnt()==null || this.getDtProtProvvPagamAnt()==null)) {
					return FASE_DODICESIMA;
				} else if (isNotaAddebitoSaldoRequired() && (getFlStampatoNotaAddebito()==null || !getFlStampatoNotaAddebito())) {
					if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null)
						return FASE_QUATTORDICESIMA;
					else
						return FASE_QUINDICESIMA;
				} else if (getImRimbSpese().compareTo(BigDecimal.ZERO)!=0) {
					if (getFlStampatoProvvPagamento()==null || !getFlStampatoProvvPagamento()) {
						if (isNotaAddebitoSaldoRequired() || 
							(this.getNumProtNotaAddebito()==null || this.getDtProtNotaAddebito()==null))
							return FASE_SEDICESIMA;
						else
							return FASE_DICIASSETTESIMA;
					} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
						return FASE_DICIOTTESIMA;
					} else if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
							   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
						return FASE_DICIANNOVESIMA;
					}
				} else if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
					return FASE_DICIANNOVESIMA;
				}
			} else  if (this.getFlStampatoDocCandidatura()==null || !this.getFlStampatoDocCandidatura()){
					return FASE_PRIMA;
			} else if (this.getFlStampatoModelloContratto()==null || !this.getFlStampatoModelloContratto()) {
					if (this.getNumProtTrasmissCandidatura()==null || this.getDtProtTrasmissCandidatura()==null || 
						this.getNumProtAccettEnteStr()==null || this.getDtProtAccettEnteStr()==null ||
						this.getNumProtAttribIncarico()==null || this.getDtProtAttribIncarico()==null)
						return FASE_SECONDA;
					else
						return FASE_TERZA;
				} else if (this.getFlStampatoAutorizPartenza()==null || !this.getFlStampatoAutorizPartenza()) {
					if (this.getNumProtContratto()==null || this.getDataProtContratto()==null || 
						(this.isNotaAddebitoAnticipoRequired() && (this.getNumProtNotaAddebitoAnt()==null || this.getDtProtNotaAddebitoAnt()==null) 
					|| (this.isNotaAddebitoSaldoConAnticipoRequired() && (this.getNumProtNotaAddebito()==null || this.getDtProtNotaAddebito()==null))))
						return FASE_QUARTA;
					else
						return FASE_QUINTA;
				} else if (this.getIncaricoRepertorio()==null || this.getIncaricoRepertorio().getPg_repertorio()==null) {
					if (this.getNumProtAutorizPartenza()==null || this.getDtProtAutorizPartenza()==null)
						return FASE_SESTA;
					else
						return FASE_SETTIMA;
				} else if (this.getIncaricoRepertorio().isIncaricoProvvisorio()) {
					return FASE_OTTAVA;
				} else if (this.getObbligazioneScadenzario()==null || this.getObbligazioneScadenzario().getPg_obbligazione()==null ||
						   this.getObbligazioneScadenzario().getObbligazione().isTemporaneo()) {
					return FASE_NONA;
				} else if (this.isAnticipoPrevisto() && 
						   (getFlStampatoProvvPagamAnt()==null || !this.getFlStampatoProvvPagamAnt())) {
					return FASE_DODICESIMA;
				} else if (this.isAnticipoPrevisto() && (this.getNumProtProvvPagamAnt()==null || this.getDtProtProvvPagamAnt()==null)) {
					return FASE_TREDICESIMA;
				} else if (this.isAnticipoPrevisto() && isNotaAddebitoSaldoRequired() && (getFlStampatoNotaAddebito()==null || !getFlStampatoNotaAddebito())) {
					if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null)
						return FASE_QUATTORDICESIMA;
					else
						return FASE_QUINDICESIMA;
				} else if (getImRimbSpese().compareTo(BigDecimal.ZERO)!=0) {
					if (getFlStampatoProvvPagamento()==null || !getFlStampatoProvvPagamento()) {
						if (this.isAnticipoPrevisto() && isNotaAddebitoSaldoRequired() || 
							(this.getNumProtNotaAddebito()==null || this.getDtProtNotaAddebito()==null))
							return FASE_SEDICESIMA;
						else
							return FASE_DICIASSETTESIMA;
					} else if (this.getNumProtProvvPagam()==null || this.getDtProtProvvPagam()==null) {
						return FASE_DICIOTTESIMA;
					} else if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
							   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
						return FASE_DICIANNOVESIMA;
					}
				} else if (this.getNumProtAttestatoSogg()==null || this.getDtProtAttestatoSogg()==null ||
						   this.getDtIniVisitaEffettiva()==null || this.getDtFinVisitaEffettiva()==null) {
					return FASE_DICIANNOVESIMA;
				}
			}
		return FASE_VENTESIMA;
	}
	public TerzoBulk getTerzoPagamento() {
		if (this.isVisitaStraniero() && this.isAccordoPagataAdEnteStraniero()) {
			return this.getBltAutorizzatiDett().getBltAutorizzati().getBltProgetti().getBltAccordo().getTerzoEnte();
		} else if (this.isVisitaStraniero() && this.isVisitaPagataAdEnteStraniero()) {
			return this.getTerzoEnte();
		} else
			return this.getBltAutorizzatiDett().getBltAutorizzati().getTerzo();
	}
	
	public boolean isNotaAddebitoAnticipoRequired() {
		return !getFlPagamentoFineVisita() && this.getImRimbSpeseAnt().compareTo(BigDecimal.ZERO)==1;
	}
	public boolean isNotaAddebitoSaldoConAnticipoRequired() {
		return !getFlPagamentoConBonifico() && !getFlPagamentoFineVisita() && this.getImRimbSpese().compareTo(BigDecimal.ZERO)==1;
	}
	public boolean isNotaAddebitoSaldoRequired() {
		return !getFlStampatoNotaAddebito() && this.getImRimbSpese().compareTo(BigDecimal.ZERO)==1;
	}

	public Blt_visiteBulk returnToFasePrecedente() {
		if (this.getCrudStatus()!=OggettoBulk.UNDEFINED && !this.isToBeCreated()) {
			if (this.isVisitaDipendente()) {
				if (this.isVentesimaFase()) {
					this.setNumProtProvvPagam(null);
					this.setDtProtProvvPagam(null);
				} else if (this.isNonaFase()) {
					this.setFlStampatoProvvPagamento(Boolean.FALSE);
				} else if (this.isOttavaFase()) {
					this.setNumProtProvvImpegno(null);
					this.setDtProtProvvImpegno(null);
				} else if (this.isSettimaFase()) {
					this.setFlStampatoProvvImpegno(Boolean.FALSE);
				} else if (this.isSestaFase()) {
					//non fa nulla in quanyto non può scollegare l'impegno
				} else if (this.isQuintaFase()) {
					this.setNumProtAutorizPartenza(null);
					this.setDtProtAutorizPartenza(null);
				} else if (this.isQuartaFase()) {
					this.setFlStampatoAutorizPartenza(Boolean.FALSE);
				} else if (this.isTerzaFase()) {
					this.setNumProtDispFin(null);
					this.setDtProtDispFin(null);
					this.setNumProtTrasmissCandidatura(null);
					this.setDtProtTrasmissCandidatura(null); 
					this.setNumProtAccettEnteStr(null);
					this.setDtProtAccettEnteStr(null);
				} else if (this.isSecondaFase()) {
					this.setFlStampatoDocCandidatura(Boolean.FALSE);
				} 
			} else if (this.isVisitaUniversitario()){
				if (this.isVentesimaFase()) {
					this.setNumProtProvvPagam(null);
					this.setDtProtProvvPagam(null);
				} else if (this.isTredicesimaFase()) {
					this.setFlStampatoProvvPagamento(Boolean.FALSE);
				} else if (this.isDodicesimaFase()) {
					this.setNumProtProvvImpegno(null);
					this.setDtProtProvvImpegno(null);
				} else if (this.isUndicesimaFase()) {
					this.setFlStampatoProvvImpegno(Boolean.FALSE);
				} else if (this.isDecimaFase()) {
					//non fa nulla in quanto non può scollegare l'impegno
				} else if (this.isNonaFase()) {
					//non fa nulla in quanto non può scollegare l'incarico
				} else if (this.isOttavaFase()) {
					//non fa nulla in quanto non può scollegare l'incarico
				} else if (this.isSettimaFase()) {
					this.setNumProtAutorizPartenza(null);
					this.setDtProtAutorizPartenza(null);
				} else if (this.isSestaFase()) {
					this.setFlStampatoAutorizPartenza(Boolean.FALSE);
				} else if (this.isQuintaFase()) {
					this.setNumProtContratto(null);
					this.setDataProtContratto(null);
				} else if (this.isQuartaFase()) {
					this.setFlStampatoModelloContratto(Boolean.FALSE);
				} else if (this.isTerzaFase()) {
					this.setNumProtDispFin(null);
					this.setDtProtDispFin(null);
					this.setNumProtTrasmissCandidatura(null);
					this.setDtProtTrasmissCandidatura(null); 
					this.setNumProtAccettEnteStr(null);
					this.setDtProtAccettEnteStr(null);
					this.setNumProtAttribIncarico(null);
					this.setDtProtAttribIncarico(null);
				} else if (this.isSecondaFase()) {
					this.setFlStampatoDocCandidatura(Boolean.FALSE);
				} 
			} else if (isVisitaStraniero()) {
				if (isAccordoPagataAdEnteStraniero()) {
					if (this.isVentesimaFase()) {
						this.setNumProtAttestatoSogg(null);
						this.setDtProtAttestatoSogg(null);
						this.setDtIniVisitaEffettiva(null);
						this.setDtFinVisitaEffettiva(null);
					} else if (this.isOttavaFase()) {
						this.setNumProtProvvPagam(null);
						this.setDtProtProvvPagam(null);
					} else if (this.isSettimaFase()) {
						this.setFlStampatoProvvPagamento(Boolean.FALSE);
					} else if (this.isSestaFase()) {
						//non fa nulla in quanto non può scollegare l'impegno
					} else if (this.isQuintaFase()) {
						this.setNumProtAutorizPartenza(null);
						this.setDtProtAutorizPartenza(null);
					} else if (this.isQuartaFase()) {
						this.setFlStampatoAutorizPartenza(Boolean.FALSE);
					} else if (this.isTerzaFase()) {
						this.setNumProtTrasmissCandidatura(null);
						this.setDtProtTrasmissCandidatura(null); 
						this.setNumProtAccettEnteStr(null);
						this.setDtProtAccettEnteStr(null);
					} else if (this.isSecondaFase()) {
						this.setFlStampatoDocCandidatura(Boolean.FALSE);
					} 
				} else if (this.isVisitaPagataAdEnteStraniero()) {
					if (this.isVentesimaFase()) {
						this.setNumProtProvvPagam(null);
						this.setDtProtProvvPagam(null);
					} else if (this.isDecimaFase()) {
						this.setFlStampatoProvvPagamento(Boolean.FALSE);
					} else if (this.isNonaFase()) {
						this.setNumProtAttestatoSogg(null);
						this.setDtProtAttestatoSogg(null);
						this.setDtIniVisitaEffettiva(null);
						this.setDtFinVisitaEffettiva(null);
					} else if (this.isOttavaFase()) {
						this.setNumProtProvvImpegno(null);
						this.setDtProtProvvImpegno(null);
					} else if (this.isSettimaFase()) {
						this.setFlStampatoProvvImpegno(Boolean.FALSE);
					} else if (this.isSestaFase()) {
						//non fa nulla in quanto non può scollegare l'impegno
					} else if (this.isQuintaFase()) {
						this.setNumProtAutorizPartenza(null);
						this.setDtProtAutorizPartenza(null);
					} else if (this.isQuartaFase()) {
						this.setFlStampatoAutorizPartenza(Boolean.FALSE);
					} else if (this.isTerzaFase()) {
						this.setNumProtTrasmissCandidatura(null);
						this.setDtProtTrasmissCandidatura(null); 
						this.setNumProtAccettEnteStr(null);
						this.setDtProtAccettEnteStr(null);
					} else if (this.isSecondaFase()) {
						this.setFlStampatoDocCandidatura(Boolean.FALSE);
					} 
				} else if (this.getFlPagamentoFineVisita()) {
					if (this.isVentesimaFase()) {
						this.setNumProtAttestatoSogg(null);
						this.setDtProtAttestatoSogg(null);
						this.setDtIniVisitaEffettiva(null);
						this.setDtFinVisitaEffettiva(null);
					} else if (this.isDiciannovesimaFase()) {
						if (getImRimbSpese().compareTo(BigDecimal.ZERO)!=0) {
							this.setNumProtProvvPagam(null);
							this.setDtProtProvvPagam(null);
						} else if (isNotaAddebitoSaldoRequired()) {
							this.setFlStampatoNotaAddebito(Boolean.FALSE);
						} else if (isAnticipoPrevisto()) {
							this.setNumProtProvvPagamAnt(null);
							this.setDtProtProvvPagamAnt(null);
						} else {
							this.setNumProtProvvImpegno(null);
							this.setDtProtProvvImpegno(null);
						}
					} else if (this.isDiciottesimaFase()) {
						this.setFlStampatoProvvPagamento(Boolean.FALSE);
					} else if (this.isDiciassettesimaFase()) {
						if (isNotaAddebitoSaldoRequired()) {
							this.setFlStampatoNotaAddebito(Boolean.FALSE);
						} else if (isAnticipoPrevisto()) {
							this.setNumProtProvvPagamAnt(null);
							this.setDtProtProvvPagamAnt(null);
						} else {
							this.setNumProtProvvImpegno(null);
							this.setDtProtProvvImpegno(null);
						}
					} else if (this.isSedicesimaFase()) {
						this.setNumProtNotaAddebito(null);
						this.setDtProtNotaAddebito(null);
						if (isNotaAddebitoSaldoRequired()) {
							this.setFlStampatoNotaAddebito(Boolean.FALSE);
						} else if (isAnticipoPrevisto()) {
							this.setNumProtProvvPagamAnt(null);
							this.setDtProtProvvPagamAnt(null);
						} else {
							this.setNumProtProvvImpegno(null);
							this.setDtProtProvvImpegno(null);
						}
					} else if (this.isQuindicesimaFase()) {
						this.setNumProtAttestatoSogg(null);
						this.setDtProtAttestatoSogg(null);
						this.setDtIniVisitaEffettiva(null);
						this.setDtFinVisitaEffettiva(null);
					} else if (this.isQuattordicesimaFase()) {
						if (isAnticipoPrevisto()) {
							this.setNumProtProvvPagamAnt(null);
							this.setDtProtProvvPagamAnt(null);
						} else {
							this.setNumProtProvvImpegno(null);
							this.setDtProtProvvImpegno(null);
						}
					} else if (this.isDodicesimaFase()) {
						this.setNumProtProvvImpegno(null);
						this.setDtProtProvvImpegno(null);
					} else if (this.isUndicesimaFase()) {
						this.setFlStampatoProvvImpegno(Boolean.FALSE);
					} else if (this.isDecimaFase()) {
						//non fa nulla in quanto non può scollegare l'impegno
					} else if (this.isNonaFase()) {
						//non fa nulla in quanto non può scollegare l'incarico
					} else if (this.isOttavaFase()) {
						//non fa nulla in quanto non può scollegare l'incarico
					} else if (this.isSettimaFase()) {
						this.setNumProtAutorizPartenza(null);
						this.setDtProtAutorizPartenza(null);
					} else if (this.isSestaFase()) {
						this.setFlStampatoAutorizPartenza(Boolean.FALSE);
					} else if (this.isQuintaFase()) {
						this.setNumProtContratto(null);
						this.setDataProtContratto(null);
						this.setNumProtNotaAddebito(null);
						this.setDtProtNotaAddebito(null);
						this.setNumProtNotaAddebitoAnt(null);
						this.setDtProtNotaAddebitoAnt(null);
					} else if (this.isQuartaFase()) {
						this.setFlStampatoModelloContratto(Boolean.FALSE);
					} else if (this.isTerzaFase()) {
						this.setNumProtTrasmissCandidatura(null);
						this.setDtProtTrasmissCandidatura(null); 
						this.setNumProtAccettEnteStr(null);
						this.setDtProtAccettEnteStr(null);
						this.setNumProtAttribIncarico(null);
						this.setDtProtAttribIncarico(null);
					} else if (this.isSecondaFase()) {
						this.setFlStampatoDocCandidatura(Boolean.FALSE);
					} 
				} else if (this.isVentesimaFase()) {
					this.setNumProtAttestatoSogg(null);
					this.setDtProtAttestatoSogg(null);
					this.setDtIniVisitaEffettiva(null);
					this.setDtFinVisitaEffettiva(null);
				} else if (this.isDiciannovesimaFase()) {
					if (getImRimbSpese().compareTo(BigDecimal.ZERO)!=0) {
						this.setNumProtProvvPagam(null);
						this.setDtProtProvvPagam(null);
					} else if (this.isAnticipoPrevisto() && this.isNotaAddebitoSaldoRequired()) {
						this.setFlStampatoNotaAddebito(Boolean.FALSE);
					} else if (this.isAnticipoPrevisto()) {
						this.setNumProtProvvPagamAnt(null);
						this.setDtProtProvvPagamAnt(null);
					} else {
						//non fa nulla in quanto non può scollegare l'impegno
					}
				} else if (this.isDiciottesimaFase()) {
					this.setFlStampatoProvvPagamento(Boolean.FALSE);
				} else if (this.isDiciassettesimaFase()) {
					if (this.isAnticipoPrevisto() && this.isNotaAddebitoSaldoRequired()) {
						this.setNumProtNotaAddebito(null);
						this.setDtProtNotaAddebito(null);
						this.setFlStampatoNotaAddebito(Boolean.FALSE);
					} else if (this.isAnticipoPrevisto()) {
						this.setNumProtProvvPagamAnt(null);
						this.setDtProtProvvPagamAnt(null);
					} else {
						//non fa nulla in quanto non può scollegare l'impegno
					}
				} else if (this.isSedicesimaFase()) {
					this.setNumProtNotaAddebito(null);
					this.setDtProtNotaAddebito(null);
					if (this.isAnticipoPrevisto() && this.isNotaAddebitoSaldoRequired()) {
						this.setFlStampatoNotaAddebito(Boolean.FALSE);
					} else if (this.isAnticipoPrevisto()) {
						this.setNumProtProvvPagamAnt(null);
						this.setDtProtProvvPagamAnt(null);
					} else {
						//non fa nulla in quanto non può scollegare l'impegno
					}
				} else if (this.isQuindicesimaFase()) {
					this.setNumProtAttestatoSogg(null);
					this.setDtProtAttestatoSogg(null);
					this.setDtIniVisitaEffettiva(null);
					this.setDtFinVisitaEffettiva(null);
				} else if (this.isQuattordicesimaFase()) {
					if (this.isAnticipoPrevisto()) {
						this.setNumProtProvvPagamAnt(null);
						this.setDtProtProvvPagamAnt(null);
					} else {
						//non fa nulla in quanto non può scollegare l'impegno
					}
				} else if (this.isTredicesimaFase()) {
					this.setFlStampatoProvvPagamento(Boolean.FALSE);
				} else if (this.isDodicesimaFase()) {
					//non fa nulla in quanto non può scollegare l'impegno
				} else if (this.isNonaFase()) {
					//non fa nulla in quanto non può scollegare l'incarico
				} else if (this.isOttavaFase()) {
					//non fa nulla in quanto non può scollegare l'incarico
				} else if (this.isSettimaFase()) {
					this.setNumProtAutorizPartenza(null);
					this.setDtProtAutorizPartenza(null);
				} else if (this.isSestaFase()) {
					this.setFlStampatoAutorizPartenza(Boolean.FALSE);
				} else if (this.isQuintaFase()) {
					this.setNumProtContratto(null);
					this.setDataProtContratto(null);
					this.setNumProtNotaAddebito(null);
					this.setDtProtNotaAddebito(null);
					this.setNumProtNotaAddebitoAnt(null);
					this.setDtProtNotaAddebitoAnt(null);
				} else if (this.isQuartaFase()) {
					this.setFlStampatoModelloContratto(Boolean.FALSE);
				} else if (this.isTerzaFase()) {
					this.setNumProtTrasmissCandidatura(null);
					this.setDtProtTrasmissCandidatura(null); 
					this.setNumProtAccettEnteStr(null);
					this.setDtProtAccettEnteStr(null);
					this.setNumProtAttribIncarico(null);
					this.setDtProtAttribIncarico(null);
				} else if (this.isSecondaFase()) {
					this.setFlStampatoDocCandidatura(Boolean.FALSE);
				} 
			}
		}
		initializeFase();
		return this;
	}
}