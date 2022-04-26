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

package it.cnr.contab.pagopa.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TelefonoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.doccont00.intcass.giornaliera.MovimentoContoEvidenzaBulk;
import it.cnr.contab.pagopa.bulk.*;
import it.cnr.contab.pagopa.model.*;
import it.cnr.contab.pagopa.model.pagamento.DatiSingoloPagamento;
import it.cnr.contab.pagopa.model.pagamento.NotificaPagamento;
import it.cnr.contab.pagopa.model.pagamento.Riscossioni;
import it.cnr.contab.pagopa.model.pagamento.Transfer;
import it.cnr.contab.pagopa.rest.PagopaService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.SendMail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

public class PendenzaPagopaComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;
	private transient static final Logger logger = LoggerFactory.getLogger(PendenzaPagopaComponent.class);
	PagopaService pagopaService =
			SpringUtil.getBean("pagopaService", PagopaService.class);

	@Override
	public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		PendenzaPagopaBulk pendenzaPagopaBulk = (PendenzaPagopaBulk)super.modificaConBulk(usercontext, oggettobulk);
		try {
			generaPendenzaSuPagopa(usercontext, pendenzaPagopaBulk);
		} catch (Throwable t) {
			logger.info(t.getMessage());
			throw handleException(t);
		}
		return pendenzaPagopaBulk;
	}

	@Override
	public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		super.eliminaConBulk(usercontext, oggettobulk);
	}

	public void riconciliaIncassoPagopa(UserContext userContext, MovimentoContoEvidenzaBulk movimentoContoEvidenzaBulk) throws ComponentException {
		try {
			MovimentoCassaPagopa movimentoCassaPagopa = new MovimentoCassaPagopa();
			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
			movimentoCassaPagopa.setDataContabile(formatter.format(movimentoContoEvidenzaBulk.getDataMovimento()));
			movimentoCassaPagopa.setDataValuta(formatter.format(movimentoContoEvidenzaBulk.getDataValutaEnte()));
			movimentoCassaPagopa.setCausale(movimentoContoEvidenzaBulk.getCausale());
			movimentoCassaPagopa.setImporto(movimentoContoEvidenzaBulk.getImporto());
			movimentoCassaPagopa.setSct(movimentoContoEvidenzaBulk.getIdentificativoFlusso());
			try  {
				movimentoCassaPagopa = pagopaService.riconciliaIncasso(getIdDominio(userContext), movimentoCassaPagopa);
			} catch (HttpClientErrorException.BadRequest e ){
				movimentoCassaPagopa = null;
			}
			if (movimentoCassaPagopa != null && movimentoCassaPagopa.getRiscossioni() != null && !movimentoCassaPagopa.getRiscossioni().isEmpty()){
				PagamentoPagopaHome home = (PagamentoPagopaHome) getHome(userContext, PagamentoPagopaBulk.class);
				for (RiscossioneMovimentoCassaPagopa riscossione : movimentoCassaPagopa.getRiscossioni()){
					PagamentoPagopaBulk pagamentoPagopaBulk = home.findPagamentoPagopa(userContext, riscossione.getIur());
					pagamentoPagopaBulk.setStato(Riscossione.StatoEnum.INCASSATA.getValue());
					pagamentoPagopaBulk.setId_riconciliazione(movimentoCassaPagopa.getIdRiconciliazione());
					pagamentoPagopaBulk.setRiconciliazione(riscossione.getRiconciliazione());
				}
			}
		} catch (Throwable t) {
			String error = "pagoPA: Errore durante la riconciliazione dell'incasso: "  + movimentoContoEvidenzaBulk.getIdentificativoFlusso() +" "+t.getMessage();
			logger.error(error);
			SendMail.sendErrorMail("pagoPA: Errore durante riconciliazione dell'incasso: "  + movimentoContoEvidenzaBulk.getIdentificativoFlusso(), error);
			throw handleException(t);
		}
	}

	public PendenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, String descrizione, BigDecimal importoScadenza, TerzoBulk terzoBulk) throws ComponentException {

		try {
			GestionePagopaHome home = (GestionePagopaHome) getHome(userContext, GestionePagopaBulk.class);
			Timestamp dataOdierna = DateServices.getDataOdierna();
			GestionePagopaBulk gestionePagopaBulk = home.findGestionePagopa(dataOdierna);
			if (gestionePagopaBulk != null){
				PendenzaPagopaBulk scadenzaPagopaBulk = new PendenzaPagopaBulk();
				Numerazione_doc_ammBulk numerazioneProgressivoUnivoco = new Numerazione_doc_ammBulk();

				TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk = getTipoScadenzaPagopaBulk(userContext, gestionePagopaBulk);



				String inizialiCodiceAvviso = generaCodiceAvviso(tipoPendenzaPagopaBulk);
				String iuv = generaIuv(userContext, documentoAmministrativoBulk, dataOdierna, numerazioneProgressivoUnivoco, tipoPendenzaPagopaBulk, inizialiCodiceAvviso);
				String codiceAvviso = inizialiCodiceAvviso+iuv;

				scadenzaPagopaBulk.setCdAvviso(codiceAvviso);
				scadenzaPagopaBulk.setCdIuv(iuv);
				scadenzaPagopaBulk.setTipoPendenzaPagopa(tipoPendenzaPagopaBulk);
				scadenzaPagopaBulk.setEsercizio(numerazioneProgressivoUnivoco.getEsercizio());
				scadenzaPagopaBulk.setDtScadenza(dataScadenza);
				scadenzaPagopaBulk.setTipoPosizione(PendenzaPagopaBulk.TIPO_POSIZIONE_CREDITORIA);
				scadenzaPagopaBulk.setStato(PendenzaPagopaBulk.STATO_APERTA);
				scadenzaPagopaBulk.setUnitaOrganizzativa(new Unita_organizzativaBulk());
				scadenzaPagopaBulk.setCdUnitaOrganizzativa(CNRUserContext.getCd_unita_organizzativa(userContext));
				scadenzaPagopaBulk.setImportoPendenza(importoScadenza);
				scadenzaPagopaBulk.setDescrizione(descrizione);
				scadenzaPagopaBulk.setTerzo(terzoBulk);
				if (documentoAmministrativoBulk != null){
					scadenzaPagopaBulk.setCdCdsDocAmm(documentoAmministrativoBulk.getCd_cds());
					scadenzaPagopaBulk.setCdUoDocAmm(documentoAmministrativoBulk.getCd_uo());
					scadenzaPagopaBulk.setCdTipoDocAmm(documentoAmministrativoBulk.getCd_tipo_doc_amm());
					scadenzaPagopaBulk.setEsercizioDocAmm(documentoAmministrativoBulk.getEsercizio());
					scadenzaPagopaBulk.setPgDocAmm(documentoAmministrativoBulk.getPg_doc_amm());
				}
				scadenzaPagopaBulk.setToBeCreated();
				generaPendenzaSuPagopa(userContext, scadenzaPagopaBulk);


				return scadenzaPagopaBulk;
			} else {
				throw new it.cnr.jada.comp.ApplicationException("La gestione PagoPA non è indicata per la data odierna");
			}

		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	private void generaPendenzaSuPagopa(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws ComponentException, IntrospectionException, PersistencyException {
		Pendenza pendenza = getPendenza(userContext, pendenzaPagopaBulk);
		PendenzaResponse pendenzaCreata = null;
		try {
			pendenzaCreata = pagopaService.creaPendenza(pendenzaPagopaBulk.getId(), pendenza);
		} catch (Exception e){
			logger.error("Errore su creaPendenza",e);
			throw new ComponentException("Errore nella creazione della pendenza"+e.getMessage());
		}
	}

	public Pendenza getPendenza(UserContext userContext, String numeroAvviso) throws ComponentException, IntrospectionException, PersistencyException {

		SQLBuilder sql = (SQLBuilder) super.select( userContext, null, new PendenzaPagopaBulk() );
		sql.addSQLClause(FindClause.AND, "CD_AVVISO", SQLBuilder.EQUALS, numeroAvviso);

		PendenzaPagopaHome pendenzaPagopaHome = (PendenzaPagopaHome)getHome(userContext, PendenzaPagopaBulk.class);

		List<PendenzaPagopaBulk> listPendenze=pendenzaPagopaHome.fetchAll(sql);

		if (listPendenze.isEmpty() || listPendenze.size() != 1){
			return null;
		}

		PendenzaPagopaBulk pendenzaPagopaBulk = listPendenze.get(0);
		Pendenza pendenza = getPendenza(userContext, pendenzaPagopaBulk);
		if (pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_APERTA)){
			pendenza.setStato(StatoPendenzaVerificata.NON_ESEGUITA);
		} else if (pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_CHIUSO) || pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_IN_PAGAMENTO)){
			pendenza.setStato(StatoPendenzaVerificata.DUPLICATA);
		} else if (pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_ANNULLATO)){
			pendenza.setStato(StatoPendenzaVerificata.ANNULLATA);
		}
		return pendenza;
	}

	private Pendenza getPendenza(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws PersistencyException, ComponentException, IntrospectionException {
		Timestamp dataOdierna = DateServices.getDataOdierna();
		Pendenza pendenza = new Pendenza();
		pendenza.setIdTipoPendenza("LIBERO");
		pendenza.setIdDominio(getIdDominio(userContext));
		pendenza.setIdUnitaOperativa(pendenzaPagopaBulk.getCdUnitaOrganizzativa().replace(".",""));
		pendenza.setCausale(pendenzaPagopaBulk.getDescrizione());
		SoggettoPagatore soggettoPagatore = new SoggettoPagatore();
		TerzoBulk terzoBulk = (TerzoBulk) getHome(userContext, pendenzaPagopaBulk.getTerzo()).findByPrimaryKey(pendenzaPagopaBulk.getTerzo());
		AnagraficoBulk anagraficoBulk = terzoBulk.getAnagrafico();

		anagraficoBulk = (AnagraficoBulk) getHome(userContext, anagraficoBulk).findByPrimaryKey(anagraficoBulk);
		soggettoPagatore.setIdentificativo(anagraficoBulk.getCodice_fiscale() == null ? anagraficoBulk.getPartita_iva() : anagraficoBulk.getCodice_fiscale());
		soggettoPagatore.setTipo(anagraficoBulk.getTi_entita());
		soggettoPagatore.setAnagrafica(terzoBulk.getDenominazione_sede());
		soggettoPagatore.setIndirizzo(terzoBulk.getVia_sede());
		try{
			soggettoPagatore.setCap(new Integer(terzoBulk.getCap_comune_sede()));
		} catch (NumberFormatException e ){
		}
		try {
			ComuneBulk comuneBulk = (ComuneBulk) getHome(userContext, terzoBulk.getComune_sede()).findByPrimaryKey(terzoBulk.getComune_sede());
			soggettoPagatore.setLocalita(comuneBulk.getDs_comune());
			soggettoPagatore.setProvincia(comuneBulk.getCd_provincia());
			NazioneBulk nazioneBulk = (NazioneBulk) getHome(userContext, comuneBulk.getNazione()).findByPrimaryKey(comuneBulk.getNazione());
			soggettoPagatore.setNazione(nazioneBulk.getCd_iso());
		} catch (PersistencyException e) {
		}
		TerzoHome terzoHome = (TerzoHome) getHome(userContext, terzoBulk);
		Collection<TelefonoBulk> mails = terzoHome.findTelefoni(terzoBulk, TelefonoBulk.EMAIL);
		if (mails != null && !mails.isEmpty()){
			soggettoPagatore.setEmail(mails.iterator().next().getRiferimento());
		}
		Collection<TelefonoBulk> telefoni = terzoHome.findTelefoni(terzoBulk, TelefonoBulk.TEL);
		if (telefoni != null && !telefoni.isEmpty()){
			soggettoPagatore.setCellulare(telefoni.iterator().next().getRiferimento());
		}
		soggettoPagatore.setIndirizzo(terzoBulk.getVia_sede());
		pendenza.setSoggettoPagatore(soggettoPagatore);
		pendenza.setImporto(pendenzaPagopaBulk.getImportoPendenza());
		pendenza.setNumeroAvviso(pendenzaPagopaBulk.getCdAvviso());
		pendenza.setAnnoRiferimento(CNRUserContext.getEsercizio(userContext));
		java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
		Date data = new Date(pendenzaPagopaBulk.getDtScadenza().getTime());
		pendenza.setDataScadenza(formatter.format(data));
		pendenza.setDataNotificaAvviso(formatter.format(new Date(dataOdierna.getTime())));
		pendenza.setDataPromemoriaScadenza(pendenza.getDataScadenza());
		Voci voci = new Voci();
		try {
			String iban = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, 0, null, "PAGOPA", "IBAN");
			voci.setIbanAccredito(iban);
		} catch (Exception e) {
			logger.info(e.getMessage());
			throw new ComponentException("Errore nel recupero dell'IBAN su CONFIGURAZIONE_CNR - 0 - PAGOPA - IBAN");
		}
		voci.setDescrizione(pendenzaPagopaBulk.getDescrizione());
		voci.setIdVocePendenza(pendenzaPagopaBulk.getId().toString());
		voci.setCodiceContabilita(pendenzaPagopaBulk.getCd_elemento_voce());
		voci.setTipoContabilita("SIOPE");
		voci.setImporto(pendenzaPagopaBulk.getImportoPendenza());
		pendenza.setVoci(Arrays.asList(voci));
		return pendenza;
	}

	private String getIdDominio(UserContext userContext) throws PersistencyException, ComponentException {
		TerzoBulk terzoCnr = ((TerzoHome)getHome(userContext, TerzoBulk.class)).findTerzoEnte();

		return terzoCnr.getCodice_fiscale_anagrafico();
	}

	@Override
	public OggettoBulk creaConBulk(UserContext userContext, OggettoBulk oggettobulk) throws ComponentException {
		PendenzaPagopaBulk pendenzaPagopaBulk = (PendenzaPagopaBulk) oggettobulk;
		try {
			GestionePagopaHome home = (GestionePagopaHome) getHome(userContext, GestionePagopaBulk.class);
			Timestamp dataOdierna = DateServices.getDataOdierna();
			GestionePagopaBulk gestionePagopaBulk = home.findGestionePagopa(dataOdierna);
			if (gestionePagopaBulk != null){
				Numerazione_doc_ammBulk numerazioneProgressivoUnivoco = new Numerazione_doc_ammBulk();

				TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk = getTipoScadenzaPagopaBulk(userContext, gestionePagopaBulk);


				String inizialiCodiceAvviso = generaCodiceAvviso(tipoPendenzaPagopaBulk);

				String iuv = generaIuv(userContext, null, dataOdierna, numerazioneProgressivoUnivoco, tipoPendenzaPagopaBulk, inizialiCodiceAvviso);

				String codiceAvviso = inizialiCodiceAvviso+iuv;

				pendenzaPagopaBulk.setCdAvviso(codiceAvviso);
				pendenzaPagopaBulk.setCdIuv(iuv);
				pendenzaPagopaBulk.setTipoPendenzaPagopa(tipoPendenzaPagopaBulk);
				pendenzaPagopaBulk.setEsercizio(numerazioneProgressivoUnivoco.getEsercizio());
				pendenzaPagopaBulk.setTipoPosizione(PendenzaPagopaBulk.TIPO_POSIZIONE_CREDITORIA);
				pendenzaPagopaBulk.setToBeCreated();
			} else {
				throw new it.cnr.jada.comp.ApplicationException("La gestione PagoPA non è indicata per la data odierna");
			}

		} catch (Throwable t) {
			throw handleException(t);
		}

		pendenzaPagopaBulk = (PendenzaPagopaBulk) super.creaConBulk(userContext, oggettobulk);
		try {
			generaPendenzaSuPagopa(userContext, pendenzaPagopaBulk);
		} catch (Throwable t) {
			logger.info(t.getMessage());
			throw handleException(t);
		}
		return pendenzaPagopaBulk;
	}

	private String generaCodiceAvviso(TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk) {
		String codiceAvviso = tipoPendenzaPagopaBulk.getAuxDigit().toString();
		if (tipoPendenzaPagopaBulk.getApplicationCode()){
			codiceAvviso += tipoPendenzaPagopaBulk.getApplicationCodeDefault();
		}
		return codiceAvviso;
	}

	public it.cnr.jada.persistency.sql.SQLBuilder selectElemento_voceByClause(
			UserContext aUC,
			PendenzaPagopaBulk pendenzaPagopaBulk,
			Elemento_voceBulk elemento_voceBulk,
			CompoundFindClause clauses)
			throws ComponentException {
		it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC, elemento_voceBulk).createSQLBuilder();
		sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(aUC));
		sql.addSQLClause("AND", "TI_GESTIONE", sql.EQUALS, "E");
		sql.addClause(clauses);
		return sql;
	}
	private TipoPendenzaPagopaBulk getTipoScadenzaPagopaBulk(UserContext userContext, GestionePagopaBulk gestionePagopaBulk) throws ComponentException {
		TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk = gestionePagopaBulk.getTipoPendenzaPagopa();
		try {
			tipoPendenzaPagopaBulk = (TipoPendenzaPagopaBulk) getHome(userContext, tipoPendenzaPagopaBulk).findByPrimaryKey(tipoPendenzaPagopaBulk);
		} catch (PersistencyException e) {
			throw handleException(tipoPendenzaPagopaBulk, e);
		}
		return tipoPendenzaPagopaBulk;
	}

	public byte[] stampaRt(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws ComponentException{

		TerzoBulk terzoCnr = null;
		try {
			terzoCnr = ((TerzoHome)getHome( userContext, TerzoBulk.class)).findTerzoEnte();
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}

		PagamentoPagopaHome home = (PagamentoPagopaHome) getHome(userContext, PagamentoPagopaBulk.class);
		PagamentoPagopaBulk pagamentoPagopaBulk = null;
		try {
			pagamentoPagopaBulk = home.findPagamentoPagopa(userContext, pendenzaPagopaBulk.getId());
		} catch (PersistencyException e) {
			logger.info(e.getMessage());
			throw handleException(e);
		}


		try {
			return pagopaService.getRt(terzoCnr.getCodice_fiscale_anagrafico(), pendenzaPagopaBulk.getCdIuv(), pagamentoPagopaBulk.getCcp());
		} catch (Throwable t) {
			logger.info(t.getMessage());
			throw handleException(t);
		}
	}

	public byte[] stampaAvviso(UserContext userContext, PendenzaPagopaBulk pendenzaPagopaBulk) throws ComponentException{

		TerzoBulk terzoCnr = null;
		try {
			terzoCnr = ((TerzoHome)getHome( userContext, TerzoBulk.class)).findTerzoEnte();
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}

		try {
			return pagopaService.getAvviso(terzoCnr.getCodice_fiscale_anagrafico(), pendenzaPagopaBulk.getCdAvviso());
		} catch (Throwable t) {
			logger.info(t.getMessage());
			throw handleException(t);
		}
	}

	protected Query select(UserContext userContext, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		sql.addOrderBy("id");
		return sql;
	}
	private String generaIuv(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataOdierna, Numerazione_doc_ammBulk numerazioneProgressivoUnivoco,
							 TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk, String inizialiCodiceAvviso) throws PersistencyException, ComponentException {
		Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome(userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		if (documentoAmministrativoBulk != null){
			numerazioneProgressivoUnivoco.setEsercizio(documentoAmministrativoBulk.getEsercizio());
		} else {
			long timestamp = dataOdierna.getTime();
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(timestamp);
			int anno =  cal.get(Calendar.YEAR);
			numerazioneProgressivoUnivoco.setEsercizio(anno);
		}
		numerazioneProgressivoUnivoco.setCd_cds(uoEnte.getCd_cds());
		numerazioneProgressivoUnivoco.setCd_unita_organizzativa(uoEnte.getCd_unita_organizzativa());

		numerazioneProgressivoUnivoco.setCd_tipo_documento_amm(Numerazione_doc_ammBulk.TIPO_POSIZIONE_CREDITORIA_PAGOPA);
		try {
			Long iuvSenzaAnno =  progressiviSession.getNextPG(userContext, numerazioneProgressivoUnivoco);
			String iuv = numerazioneProgressivoUnivoco.getEsercizio().toString()+ Utility.lpad(iuvSenzaAnno.toString(), tipoPendenzaPagopaBulk.getLunghezzaIuvBase() - 4,'0');
			if (tipoPendenzaPagopaBulk.getIuvCheckDigit()){
				BigDecimal iuvBase = new BigDecimal(inizialiCodiceAvviso+iuv);
				BigDecimal divisorStandard = new BigDecimal(tipoPendenzaPagopaBulk.getDivisoreCheckDigit());
				BigDecimal mod = iuvBase.remainder(divisorStandard);
				iuv += mod.toString();
			}
			return iuv;

		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	}

	public RemoteIterator cercaPagamenti(UserContext aUC, PendenzaPagopaBulk pendenzaPagopaBulk) throws ComponentException {

		if (pendenzaPagopaBulk == null) return null;
		PagamentoPagopaHome home = (PagamentoPagopaHome) getHome(aUC, PagamentoPagopaBulk.class);
		return iterator(aUC,
				home.searchPagamenti(pendenzaPagopaBulk.getId()),
				PagamentoPagopaBulk.class,
				"dafault");
	}


	public NotificaPagamento notificaPagamento(UserContext userContext, NotificaPagamento notificaPagamento, String iuv) throws ComponentException, IntrospectionException, PersistencyException {
			SQLBuilder sql = (SQLBuilder) super.select( userContext, null, new PendenzaPagopaBulk() );
			sql.addSQLClause(FindClause.AND, "CD_IUV", SQLBuilder.EQUALS, iuv);

			PendenzaPagopaHome pendenzaPagopaHome = (PendenzaPagopaHome)getHome(userContext, PendenzaPagopaBulk.class);

			List<PendenzaPagopaBulk> listPendenze=pendenzaPagopaHome.fetchAll(sql);

			if (listPendenze.isEmpty() || listPendenze.size() != 1){
				return null;
			}

		try {
			PendenzaPagopaBulk pendenzaPagopaBulk = listPendenze.get(0);
			if (notificaPagamento.getRt() != null){
					PagamentoPagopaHome home = (PagamentoPagopaHome) getHome(userContext, PagamentoPagopaBulk.class);
					PagamentoPagopaBulk pagamentoPagopaBulk = home.findPagamentoPagopa(userContext, pendenzaPagopaBulk.getId());
					boolean pagamentoNonTrovato = false;
					if (pagamentoPagopaBulk == null){
						pagamentoNonTrovato = true;
						pagamentoPagopaBulk = new PagamentoPagopaBulk();
					}

					Riscossioni riscossioni = notificaPagamento.getRiscossioni().get(0);
					java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
					pagamentoPagopaBulk.setDtPagamento(new Timestamp(formatter.parse(riscossioni.getData()).getTime()));
					pagamentoPagopaBulk.setCcp(notificaPagamento.getRt().getReceiptId());
					pagamentoPagopaBulk.setIur(riscossioni.getIur());
					pagamentoPagopaBulk.setStato(riscossioni.getStato());
					pagamentoPagopaBulk.setImporto((new BigDecimal(riscossioni.getImporto())).setScale(2, RoundingMode.HALF_EVEN));
					pagamentoPagopaBulk.setRpp(riscossioni.getRpp());

					for (Transfer transfer : notificaPagamento.getRt().getTransferList().getTransfer()){
						pagamentoPagopaBulk.setCausale(transfer.getRemittanceInformation());
						break;
					}
					if (pagamentoNonTrovato){
						pagamentoPagopaBulk.setToBeCreated();
						pagamentoPagopaBulk = (PagamentoPagopaBulk) super.creaConBulk(userContext, pagamentoPagopaBulk);
					} else {
						pagamentoPagopaBulk.setToBeUpdated();
						pagamentoPagopaBulk = (PagamentoPagopaBulk) super.modificaConBulk(userContext, pagamentoPagopaBulk);
					}
					pendenzaPagopaBulk.setStato(PendenzaPagopaBulk.STATO_RISCOSSA);
			} else {
					PagamentoPagopaBulk pagamentoPagopaBulk = new PagamentoPagopaBulk();
					pagamentoPagopaBulk.setDtPagamento(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
					pagamentoPagopaBulk.setPendenzaPagopa(pendenzaPagopaBulk);
					pagamentoPagopaBulk.setStato(PagamentoPagopaBulk.STATO_PRENOTATO);
					for (Transfer transfer : notificaPagamento.getRpt().getTransferList().getTransfer()){
						pagamentoPagopaBulk.setImporto(transfer.getTransferAmount());
						pagamentoPagopaBulk.setCausale(transfer.getRemittanceInformation());
						break;
					}
					pagamentoPagopaBulk.setToBeCreated();
					pagamentoPagopaBulk = (PagamentoPagopaBulk) super.creaConBulk(userContext, pagamentoPagopaBulk);
					pendenzaPagopaBulk.setStato(PendenzaPagopaBulk.STATO_IN_PAGAMENTO);
			}
			pendenzaPagopaBulk.setToBeUpdated();
			super.modificaConBulk(userContext, pendenzaPagopaBulk);
		} catch (Exception ex ){
			logger.error("Errore durante l'elaborazione della notifica di pagamento Iuv: "+iuv);
			String msg = Arrays.stream(ex.getStackTrace())
					.map(Objects::toString)
					.collect(Collectors.joining("\n"));
			String subject = "PagoPA: Errore durante l'elaborazione della notifica di pagamento. Iuv: "+iuv;
			SendMail.sendErrorMail(subject, msg);
			throw handleException(ex);
		}
		return notificaPagamento;
	}

}
