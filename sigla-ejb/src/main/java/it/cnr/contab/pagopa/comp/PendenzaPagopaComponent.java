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
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.pagopa.bulk.*;
import it.cnr.contab.pagopa.model.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

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

		PendenzaResponse pendenzaCreata = pagopaService.creaPendenza(pendenzaPagopaBulk.getId(), pendenza);
		byte [] stampaAvviso = Base64.getDecoder().decode(pendenzaCreata.getPdf());
		int i = 0;
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
		if (pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_ASSOCIATO) || pendenzaPagopaBulk.getStato().equals(PendenzaPagopaBulk.STATO_APERTA)){
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
		TerzoBulk terzoCnr = ((TerzoHome)getHome(userContext, TerzoBulk.class)).findTerzoEnte();

		pendenza.setIdDominio(terzoCnr.getCodice_fiscale_anagrafico());
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
			String iban = Utility.createConfigurazioneCnrComponentSession().getVal01(userContext, 0, null, "F24_EP", "CONTO_CORRENTE");
			voci.setIbanAccredito(iban);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		voci.setDescrizione(pendenzaPagopaBulk.getDescrizione());
		voci.setIdVocePendenza(pendenzaPagopaBulk.getId().toString());
		voci.setCodiceContabilita(pendenzaPagopaBulk.getCd_elemento_voce());
		voci.setTipoContabilita("SIOPE");
		voci.setImporto(pendenzaPagopaBulk.getImportoPendenza());
		pendenza.setVoci(Arrays.asList(voci));
		return pendenza;
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
	public NotificaPagamento notificaPagamento(UserContext userContext, NotificaPagamento notificaPagamento, String iuv) throws ComponentException, IntrospectionException, PersistencyException {

		SQLBuilder sql = (SQLBuilder) super.select( userContext, null, new PendenzaPagopaBulk() );
		sql.addSQLClause(FindClause.AND, "CD_IUV", SQLBuilder.EQUALS, iuv);

		PendenzaPagopaHome pendenzaPagopaHome = (PendenzaPagopaHome)getHome(userContext, PendenzaPagopaBulk.class);

		List<PendenzaPagopaBulk> listPendenze=pendenzaPagopaHome.fetchAll(sql);

		if (listPendenze.isEmpty() || listPendenze.size() != 1){
			return null;
		}

		PendenzaPagopaBulk pendenzaPagopaBulk = listPendenze.get(0);
		PagamentoPagopaBulk pagamentoPagopaBulk = new PagamentoPagopaBulk();
		Riscossione riscossione = notificaPagamento.getRiscossioni().get(0);
		pagamentoPagopaBulk.setDtPagamento(new Timestamp(riscossione.getData().getTime()));
		pagamentoPagopaBulk.setCcp(notificaPagamento.getRpt().getCcp());
		pagamentoPagopaBulk.setIur(riscossione.getIur());
		pagamentoPagopaBulk.setPendenzaPagopa(pendenzaPagopaBulk);
		pagamentoPagopaBulk.setStato(riscossione.getStato());
		pagamentoPagopaBulk.setImporto(riscossione.getImporto());
		pagamentoPagopaBulk.setRpp(riscossione.getRpp());
		pagamentoPagopaBulk.setCausale(notificaPagamento.getRt().getIdentificativoMessaggioRicevuta());
		pagamentoPagopaBulk.setToBeCreated();
		pagamentoPagopaBulk = (PagamentoPagopaBulk) super.creaConBulk(userContext, pagamentoPagopaBulk);
		return notificaPagamento;
	}

}
