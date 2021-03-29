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
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.pagopa.bulk.GestionePagopaBulk;
import it.cnr.contab.pagopa.bulk.GestionePagopaHome;
import it.cnr.contab.pagopa.bulk.PendenzaPagopaBulk;
import it.cnr.contab.pagopa.bulk.TipoPendenzaPagopaBulk;
import it.cnr.contab.pagopa.model.Entrata;
import it.cnr.contab.pagopa.model.Pendenza;
import it.cnr.contab.pagopa.model.SoggettoPagatore;
import it.cnr.contab.pagopa.model.Voci;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;

public class PendenzaPagopaComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	public PendenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, String descrizione, BigDecimal importoScadenza, TerzoBulk terzoBulk) throws ComponentException {

		try {
			GestionePagopaHome home = (GestionePagopaHome) getHome(userContext, GestionePagopaBulk.class);
			Timestamp dataOdierna = DateServices.getDataOdierna();
			GestionePagopaBulk gestionePagopaBulk = home.findGestionePagopa(dataOdierna);
			if (gestionePagopaBulk != null){
				PendenzaPagopaBulk scadenzaPagopaBulk = new PendenzaPagopaBulk();
				Numerazione_doc_ammBulk numerazioneProgressivoUnivoco = new Numerazione_doc_ammBulk();

				TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk = getTipoScadenzaPagopaBulk(userContext, gestionePagopaBulk);


				String iuv = generaIuv(userContext, documentoAmministrativoBulk, dataOdierna, numerazioneProgressivoUnivoco, tipoPendenzaPagopaBulk);

				String codiceAvviso = generaCodiceAvviso(tipoPendenzaPagopaBulk, iuv);

				scadenzaPagopaBulk.setCdAvviso(codiceAvviso);
				scadenzaPagopaBulk.setCdIuv(iuv);
				scadenzaPagopaBulk.setTipoPendenzaPagopa(tipoPendenzaPagopaBulk);
				scadenzaPagopaBulk.setEsercizio(numerazioneProgressivoUnivoco.getEsercizio());
				scadenzaPagopaBulk.setDtScadenza(dataScadenza);
				scadenzaPagopaBulk.setTipoPosizione(PendenzaPagopaBulk.TIPO_POSIZIONE_CREDITORIA);
				scadenzaPagopaBulk.setStato(PendenzaPagopaBulk.STATO_VALIDO);
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
				Pendenza pendenza = new Pendenza();
				pendenza.setIdTipoPendenza("PRESTAZIONE");
				pendenza.setIdDominio(1);
				pendenza.setIdUnitaOperativa(scadenzaPagopaBulk.getCdUnitaOrganizzativa());
				pendenza.setCausale(scadenzaPagopaBulk.getDescrizione());
				SoggettoPagatore soggettoPagatore = new SoggettoPagatore();
				AnagraficoBulk anagraficoBulk = scadenzaPagopaBulk.getTerzo().getAnagrafico();
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
				if (mails != null && mails.isEmpty()){
					soggettoPagatore.setEmail(mails.iterator().next().getRiferimento());
				}
				Collection<TelefonoBulk> telefoni = terzoHome.findTelefoni(terzoBulk, TelefonoBulk.TEL);
				if (telefoni != null && telefoni.isEmpty()){
					soggettoPagatore.setCellulare(telefoni.iterator().next().getRiferimento());
				}
				soggettoPagatore.setIndirizzo(terzoBulk.getVia_sede());
				pendenza.setImporto(importoScadenza);
				pendenza.setNumeroAvviso(new Long(codiceAvviso));
				pendenza.setAnnoRiferimento(CNRUserContext.getEsercizio(userContext));
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd");
				Date data = new Date(dataScadenza.getTime());
				pendenza.setDataScadenza(formatter.format(data));
				pendenza.setDataNotificaAvviso(formatter.format(new Date(dataOdierna.getTime())));
				pendenza.setDataPromemoriaScadenza(pendenza.getDataScadenza());
				Voci voci = new Voci();
				Entrata entrata = new Entrata();
				entrata.setIbanAccredito("AA");
				entrata.setIbanAppoggio("AA1");
				voci.setEntrata(entrata);
				pendenza.setVoci(Arrays.asList(voci));




				return scadenzaPagopaBulk;
			} else {
				throw new it.cnr.jada.comp.ApplicationException("La gestione PagoPA non è indicata per la data odierna");
			}

		} catch (Throwable t) {
			throw handleException(t);
		}
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


				String iuv = generaIuv(userContext, null, dataOdierna, numerazioneProgressivoUnivoco, tipoPendenzaPagopaBulk);

				String codiceAvviso = generaCodiceAvviso(tipoPendenzaPagopaBulk, iuv);

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

		return super.creaConBulk(userContext, oggettobulk);
	}

	private String generaCodiceAvviso(TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk, String iuv) {
		String codiceAvviso = tipoPendenzaPagopaBulk.getAuxDigit().toString();
		if (tipoPendenzaPagopaBulk.getApplicationCode()){
			codiceAvviso += tipoPendenzaPagopaBulk.getApplicationCodeDefault();
		}
		codiceAvviso += iuv;
		return codiceAvviso;
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

	private String generaIuv(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataOdierna, Numerazione_doc_ammBulk numerazioneProgressivoUnivoco, TipoPendenzaPagopaBulk tipoPendenzaPagopaBulk) throws PersistencyException, ComponentException {
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
				BigDecimal iuvBase = new BigDecimal(iuv);
				BigDecimal divisorStandard = new BigDecimal(tipoPendenzaPagopaBulk.getDivisoreCheckDigit());
				BigDecimal mod = iuvBase.remainder(divisorStandard);
				iuv += mod.toString();
			}
			return iuv;

		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	}

}
