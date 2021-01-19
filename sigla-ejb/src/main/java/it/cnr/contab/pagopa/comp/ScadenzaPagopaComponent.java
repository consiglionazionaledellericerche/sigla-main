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

import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloBulk;
import it.cnr.contab.bollo00.tabrif.bulk.Tipo_atto_bolloHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.doccont00.comp.DateServices;
import it.cnr.contab.pagopa.bulk.GestionePagopaBulk;
import it.cnr.contab.pagopa.bulk.GestionePagopaHome;
import it.cnr.contab.pagopa.bulk.ScadenzaPagopaBulk;
import it.cnr.contab.pagopa.bulk.TipoScadenzaPagopaBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Optional;

public class ScadenzaPagopaComponent extends CRUDComponent {
	private static final long serialVersionUID = 1L;

	public ScadenzaPagopaBulk generaPosizioneDebitoria(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataScadenza, BigDecimal importoScadenza) throws ComponentException {

		try {
			GestionePagopaHome home = (GestionePagopaHome) getHome(userContext, GestionePagopaBulk.class);
			Timestamp dataOdierna = DateServices.getDataOdierna();
			GestionePagopaBulk gestionePagopaBulk = home.findGestionePagopa(dataOdierna);
			if (gestionePagopaBulk != null){
				ScadenzaPagopaBulk scadenzaPagopaBulk = new ScadenzaPagopaBulk();
				Numerazione_doc_ammBulk numerazioneProgressivoUnivoco = new Numerazione_doc_ammBulk();

				TipoScadenzaPagopaBulk tipoScadenzaPagopaBulk = getTipoScadenzaPagopaBulk(userContext, gestionePagopaBulk);


				String iuv = generaIuv(userContext, documentoAmministrativoBulk, dataOdierna, numerazioneProgressivoUnivoco, tipoScadenzaPagopaBulk);

				String codiceAvviso = generaCodiceAvviso(tipoScadenzaPagopaBulk, iuv);

				scadenzaPagopaBulk.setCdAvviso(codiceAvviso);
				scadenzaPagopaBulk.setCdIuv(iuv);
				scadenzaPagopaBulk.setTipoScadenzaPagopa(tipoScadenzaPagopaBulk);
				scadenzaPagopaBulk.setEsercizio(numerazioneProgressivoUnivoco.getEsercizio());
				scadenzaPagopaBulk.setDtScadenza(dataScadenza);
				scadenzaPagopaBulk.setTipoPosizione(ScadenzaPagopaBulk.TIPO_POSIZIONE_CREDITORIA);
				scadenzaPagopaBulk.setStato(ScadenzaPagopaBulk.STATO_VALIDO);
				scadenzaPagopaBulk.setImportoScadenza(importoScadenza);
				if (documentoAmministrativoBulk != null){
					scadenzaPagopaBulk.setUnitaOrganizzativa(new Unita_organizzativaBulk());
					scadenzaPagopaBulk.setCdUnitaOrganizzativa(documentoAmministrativoBulk.getCd_uo());
					scadenzaPagopaBulk.setCdCdsDocAmm(documentoAmministrativoBulk.getCd_cds());
					scadenzaPagopaBulk.setCdUoDocAmm(documentoAmministrativoBulk.getCd_uo());
					scadenzaPagopaBulk.setCdTipoDocAmm(documentoAmministrativoBulk.getCd_tipo_doc_amm());
					scadenzaPagopaBulk.setEsercizioDocAmm(documentoAmministrativoBulk.getEsercizio());
					scadenzaPagopaBulk.setPgDocAmm(documentoAmministrativoBulk.getPg_doc_amm());
				}
				scadenzaPagopaBulk.setToBeCreated();
				return scadenzaPagopaBulk;
			} else {
				throw new it.cnr.jada.comp.ApplicationException("La gestione PagoPA non è indicata per la data odierna");
			}

		} catch (Throwable t) {
			throw handleException(t);
		}
	}

	private String generaCodiceAvviso(TipoScadenzaPagopaBulk tipoScadenzaPagopaBulk, String iuv) {
		String codiceAvviso = tipoScadenzaPagopaBulk.getAuxDigit().toString();
		if (tipoScadenzaPagopaBulk.getApplicationCode()){
			codiceAvviso += tipoScadenzaPagopaBulk.getApplicationCodeDefault();
		}
		codiceAvviso += iuv;
		return codiceAvviso;
	}

	private TipoScadenzaPagopaBulk getTipoScadenzaPagopaBulk(UserContext userContext, GestionePagopaBulk gestionePagopaBulk) throws ComponentException {
		TipoScadenzaPagopaBulk tipoScadenzaPagopaBulk = gestionePagopaBulk.getTipoScadenzaPagopa();
		try {
			tipoScadenzaPagopaBulk = (TipoScadenzaPagopaBulk) getHome(userContext, tipoScadenzaPagopaBulk).findByPrimaryKey(tipoScadenzaPagopaBulk);
		} catch (PersistencyException e) {
			throw handleException(tipoScadenzaPagopaBulk, e);
		}
		return tipoScadenzaPagopaBulk;
	}

	private String generaIuv(UserContext userContext, IDocumentoAmministrativoBulk documentoAmministrativoBulk, Timestamp dataOdierna, Numerazione_doc_ammBulk numerazioneProgressivoUnivoco, TipoScadenzaPagopaBulk tipoScadenzaPagopaBulk) throws PersistencyException, ComponentException {
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
			String iuv = numerazioneProgressivoUnivoco.getEsercizio().toString()+ Utility.lpad(iuvSenzaAnno.toString(),tipoScadenzaPagopaBulk.getLunghezzaIuvBase() - 4,'0');
			if (tipoScadenzaPagopaBulk.getIuvCheckDigit()){
				BigDecimal iuvBase = new BigDecimal(iuv);
				BigDecimal divisorStandard = new BigDecimal(tipoScadenzaPagopaBulk.getDivisoreCheckDigit());
				BigDecimal mod = iuvBase.remainder(divisorStandard);
				iuv += mod.toString();
			}
			return iuv;

		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	}

}
