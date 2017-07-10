package it.cnr.contab.docamm00.comp;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.ejb.EJBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaHome;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoHome;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.cmis.service.SiglaCMISService;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteHome;
import it.cnr.contab.docamm00.client.RicercaTrovato;
import it.cnr.contab.docamm00.cmis.CMISFolderFatturaPassiva;
import it.cnr.contab.docamm00.docs.bulk.AccertamentiTable;
import it.cnr.contab.docamm00.docs.bulk.AssociazioniInventarioTable;
import it.cnr.contab.docamm00.docs.bulk.AutofatturaBulk;
import it.cnr.contab.docamm00.docs.bulk.AutofatturaHome;
import it.cnr.contab.docamm00.docs.bulk.CarichiInventarioTable;
import it.cnr.contab.docamm00.docs.bulk.Consuntivo_rigaVBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_amministrativo_passivoBulk;
import it.cnr.contab.docamm00.docs.bulk.ElaboraNumUnicoFatturaPBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIHome;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_doc_ammVBulk;
import it.cnr.contab.docamm00.docs.bulk.Filtro_ricerca_obbligazioniVBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk;
import it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoRigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Lettera_pagam_esteroBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_creditoHome;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_credito_rigaHome;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debitoHome;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Nota_di_debito_rigaHome;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.ObbligazioniTable;
import it.cnr.contab.docamm00.docs.bulk.TrovatoBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPBulk;
import it.cnr.contab.docamm00.docs.bulk.VFatturaPassivaSIPHome;
import it.cnr.contab.docamm00.docs.bulk.Voidable;
import it.cnr.contab.docamm00.ejb.AutoFatturaComponentSession;
import it.cnr.contab.docamm00.ejb.ProgressiviAmmComponentSession;
import it.cnr.contab.docamm00.ejb.RiportoDocAmmComponentSession;
import it.cnr.contab.docamm00.ejb.VoceIvaComponentSession;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAcquistoBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleDdtBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleIvaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleLineaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleScontoMaggBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataHome;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTributiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.StatoDocumentoEleEnum;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Condizione_consegnaHome;
import it.cnr.contab.docamm00.intrastat.bulk.Fattura_passiva_intraBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_erogazioneBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_erogazioneHome;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_incassoBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_incassoHome;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoBulk;
import it.cnr.contab.docamm00.intrastat.bulk.Modalita_trasportoHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.CambioHome;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.contab.doccont00.comp.DocumentoContabileComponentSession;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoHome;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.IDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileBulk;
import it.cnr.contab.doccont00.core.bulk.IScadenzaDocumentoContabileHome;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voce_aggregatoBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.OptionRequestParameter;
import it.cnr.contab.doccont00.core.bulk.ReversaleBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIBulk;
import it.cnr.contab.doccont00.core.bulk.ReversaleIHome;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Reversale_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoBulk;
import it.cnr.contab.doccont00.core.bulk.SospesoHome;
import it.cnr.contab.doccont00.core.bulk.V_disp_cassa_cdsBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaHome;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laHome;
import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sBulk;
import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sHome;
import it.cnr.contab.inventario00.docs.bulk.V_ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoHome;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettHome;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgHome;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.RemoveAccent;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashMap;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.FatturaNonTrovataException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBroker;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class FatturaPassivaComponent extends it.cnr.jada.comp.CRUDComponent 
	implements IFatturaPassivaMgr, Cloneable,Serializable {
	private transient final static Logger logger = LoggerFactory.getLogger(DocumentoEleTestataHome.class);

    public  FatturaPassivaComponent()
    {
     }
//^^@@
/** 
  *  Addebita i dettagli selezionati.
  *    PreCondition:
  *      In nota di debito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vegono addebitati nella nota di debito passata i "dettagliDaInventariare" sull'obbligazione/accertamento selezionato/creato.
 */
//^^@@

public Nota_di_debitoBulk addebitaDettagli(
	UserContext context,
	Nota_di_debitoBulk ndD,
	java.util.List dettagliDaAddebitare,
	java.util.Hashtable relationsHash)
	throws ComponentException {

	if (ndD == null) return ndD;

	if (dettagliDaAddebitare == null || dettagliDaAddebitare.isEmpty()) {
		if (relationsHash != null) {
			Obbligazione_scadenzarioBulk obbligazioneSelezionata = (Obbligazione_scadenzarioBulk)relationsHash.get(ndD);
			if (obbligazioneSelezionata != null)
				ndD.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, null);
		}
	} else {
		for (Iterator i = dettagliDaAddebitare.iterator(); i.hasNext();) {
			Nota_di_debito_rigaBulk rigaNdD = (Nota_di_debito_rigaBulk)i.next();
			Fattura_passiva_rigaIBulk rigaAssociata = null;
			if (relationsHash != null) {
				Object obj = relationsHash.get(rigaNdD);
				if (obj instanceof Fattura_passiva_rigaIBulk) {
					Fattura_passiva_rigaIBulk rigaInRelazione = (Fattura_passiva_rigaIBulk)obj;
					rigaAssociata = (rigaInRelazione == null) ? 
											rigaNdD.getRiga_fattura_origine() : 
											rigaInRelazione;
				}
			} else
				rigaAssociata = rigaNdD.getRiga_fattura_origine();

			if (rigaAssociata != null) {
				ndD = basicAddebitaDettaglio(
										context,
										ndD,
										rigaNdD,
										rigaAssociata);
			}
		}
	}
	return ndD;
}
private void aggiornaAccertamenti(
	UserContext userContext,
	Nota_di_creditoBulk notaDiCredito,
	OptionRequestParameter status) throws ComponentException {

	if (notaDiCredito != null) {
		AccertamentiTable accertamentiHash = notaDiCredito.getAccertamenti_scadenzarioHash();
		if (accertamentiHash != null && !accertamentiHash.isEmpty()) {
			//Aggiorna i saldi per gli accertamenti NON temporanei
			for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((AccertamentiTable)accertamentiHash.clone()).keys()).keys(); e.hasMoreElements();)
				aggiornaSaldi(userContext, 
					notaDiCredito, 
					(IDocumentoContabileBulk)e.nextElement(),
					status);

			PrimaryKeyHashtable accTemporanei = getDocumentiContabiliTemporanei(userContext, ((AccertamentiTable)accertamentiHash.clone()).keys());
			for (java.util.Enumeration e = accTemporanei.keys(); e.hasMoreElements();) {
				AccertamentoBulk accT = (AccertamentoBulk)e.nextElement();

				//Aggiorna i saldi per gli accertamenti temporanei
				//DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
				aggiornaSaldi(userContext, notaDiCredito, accT, status);
				
				aggiornaAccertamentiTemporanei(userContext, accT);
				accTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable(accTemporanei);
				for (Iterator i = ((Vector)accTemporanei.get(accT)).iterator(); i.hasNext();)
					((AccertamentoBulk)i.next()).setPg_accertamento(accT.getPg_accertamento());
			}
			AccertamentiTable newAccertamentiHash = new AccertamentiTable(accertamentiHash);
			notaDiCredito.setAccertamenti_scadenzarioHash(newAccertamentiHash);
			for (java.util.Enumeration e = ((AccertamentiTable)newAccertamentiHash.clone()).keys(); e.hasMoreElements();) {
				Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)e.nextElement();
				scadenza.setIm_associato_doc_amm(
						calcolaTotalePer(
							(java.util.List)newAccertamentiHash.get(scadenza),
							notaDiCredito.quadraturaInDeroga()));
				updateImportoAssociatoDocAmm(userContext, scadenza);
			}
		}
	}
}

private void aggiornaAccertamentiSuCancellazione(
	UserContext userContext,
	Nota_di_creditoBulk notaDiCredito,
	java.util.Enumeration scadenzeDaCancellare,
	java.util.Collection scadenzeConfermate,
	OptionRequestParameter status)
	throws ComponentException {

	if (scadenzeDaCancellare != null) {
		it.cnr.jada.bulk.PrimaryKeyHashtable accTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
		for (java.util.Enumeration e = scadenzeDaCancellare; e.hasMoreElements();) {
			OggettoBulk oggettoBulk = (OggettoBulk)e.nextElement();
			if (oggettoBulk instanceof Accertamento_scadenzarioBulk) {
				Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)oggettoBulk;
				if (scadenza.getAccertamento().isTemporaneo()) {
					if (!accTemporanei.containsKey(scadenza.getAccertamento())) {
						Vector allInstances = new java.util.Vector();
						allInstances.addElement(scadenza);
						accTemporanei.put(scadenza.getAccertamento(), allInstances);
					} else {
						((Vector)accTemporanei.get(scadenza.getAccertamento())).add(scadenza);
					}
				} else if (!notaDiCredito.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
					PrimaryKeyHashtable accerts = getDocumentiContabiliNonTemporanei(userContext, notaDiCredito.getAccertamentiHash().keys());
					if (!accerts.containsKey(scadenza.getAccertamento()))
						aggiornaSaldi(
							userContext, 
							notaDiCredito, 
							scadenza.getAccertamento(),
							status);
					scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
					updateImportoAssociatoDocAmm(userContext, scadenza);
				}
			}
		}
		for (java.util.Enumeration e = accTemporanei.keys(); e.hasMoreElements();) {
			AccertamentoBulk accT = (AccertamentoBulk)e.nextElement();
			if (scadenzeConfermate == null || !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(scadenzeConfermate, accT)) {

				//Aggiorna i saldi per le obbligazioni temporanee
				//DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
				PrimaryKeyHashtable accerts = getDocumentiContabiliTemporanei(userContext, notaDiCredito.getAccertamentiHash().keys());
				if (!accerts.containsKey(accT))
					aggiornaSaldi(userContext, notaDiCredito, accT, status);

				for (Iterator i = ((Vector)accTemporanei.get(accT)).iterator(); i.hasNext();) {
					Accertamento_scadenzarioBulk scad = (Accertamento_scadenzarioBulk)i.next();
					scad.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
					updateImportoAssociatoDocAmm(userContext, scad);
				}
				aggiornaAccertamentiTemporanei(userContext, accT);
			}
		}
	}
}
private void aggiornaAccertamentiTemporanei(UserContext userContext,AccertamentoBulk accertamentoTemporaneo) throws ComponentException {

	try {
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
		Long pg = null;
		pg = numHome.getNextPg(userContext,
						accertamentoTemporaneo.getEsercizio(), 
						accertamentoTemporaneo.getCd_cds(), 
						accertamentoTemporaneo.getCd_tipo_documento_cont(), 
						accertamentoTemporaneo.getUser());
		AccertamentoHome home = (AccertamentoHome)getHome(userContext, accertamentoTemporaneo);
		home.confirmAccertamentoTemporaneo(userContext, accertamentoTemporaneo, pg);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(accertamentoTemporaneo, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(accertamentoTemporaneo, e);
	}	
}
private String aggiornaAssociazioniInventario(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	StringBuffer msgBuf = new StringBuffer();

	if (fattura_passiva != null) {
		AssociazioniInventarioTable associazioniInventarioHash = fattura_passiva.getAssociazioniInventarioHash();
		if (associazioniInventarioHash != null && !associazioniInventarioHash.isEmpty()) {
			Vector associazioniTemporanee = new Vector();
			for (java.util.Enumeration e = ((AssociazioniInventarioTable)associazioniInventarioHash.clone()).keys(); e.hasMoreElements();) {
				Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
				if (ass.isTemporaneo() &&
					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(associazioniTemporanee, ass))
					associazioniTemporanee.add(ass);
			}
			for (Iterator i = associazioniTemporanee.iterator(); i.hasNext();){
				String msg = aggiornaAssociazioniInventarioTemporanee(userContext, fattura_passiva, (Ass_inv_bene_fatturaBulk)i.next());
				if (msg != null)
					msgBuf.append(msg);
			}
		}
	}

	if (msgBuf.toString().equals(""))
		return null;
		
	return msgBuf.toString();
}
private String aggiornaAssociazioniInventarioTemporanee(
	UserContext userContext,
	Fattura_passivaBulk fp,
	Ass_inv_bene_fatturaBulk assTemporanea) throws ComponentException {
	String msg=null;
	try {
		Ass_inv_bene_fatturaHome home = (Ass_inv_bene_fatturaHome)getHome(userContext, assTemporanea);

		if (assTemporanea.isPerAumentoValore())
			msg = home.makePersistentAssociaPerAumento(userContext,assTemporanea, fp);
		else 
			home.makePersistentAssocia(userContext, assTemporanea, fp);
			
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(assTemporanea, e);
	}

	return msg;
}
private void aggiornaAutofattura(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	if (fattura_passiva != null && !fattura_passiva.isStampataSuRegistroIVA() && fattura_passiva.getProgr_univoco()==null) {
		if (fattura_passiva.getFl_autofattura() == null)
			fattura_passiva.setFl_autofattura(Boolean.FALSE);
		try {
			AutoFatturaComponentSession h = getAutofatturaComponentSession(userContext);
			if (fattura_passiva.getAutofattura() != null) {
				fattura_passiva.getAutofattura().setToBeDeleted();
				h.eliminaConBulk(userContext, fattura_passiva.getAutofattura());
				fattura_passiva.setAutofattura(null);
			}
		} catch (java.rmi.RemoteException e) {
			throw handleException(fattura_passiva, e);
		}
		if (fattura_passiva.getFl_autofattura().booleanValue())
			creaAutofattura(userContext, fattura_passiva);
	}
}
private void aggiornaBuoniCaricoTemporanei(
	UserContext userContext,
	Fattura_passivaBulk fp,
	Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {

	try {
		Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
		Long pg = null;
		
			pg = numHome.getNextPg(userContext,
						buonoTemporaneo.getEsercizio(),
						buonoTemporaneo.getPg_inventario(),
						buonoTemporaneo.getTi_documento(),
						userContext.getUser());
		
		Buono_carico_scaricoBulk definitivo= (Buono_carico_scaricoBulk)buonoTemporaneo.clone();
	
		Buono_carico_scarico_dettHome home = (Buono_carico_scarico_dettHome)getTempHome(userContext, Buono_carico_scarico_dettBulk.class);
		
		SQLBuilder sql=home.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoTemporaneo.getEsercizio().toString());
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoTemporaneo.getPg_inventario().toString());
		sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoTemporaneo.getTi_documento());
		sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoTemporaneo.getPg_buono_c_s().toString());
		List dettagli = home.fetchAll(sql);

		definitivo.setPg_buono_c_s(pg);
		definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
		super.creaConBulk(userContext,definitivo);		
		for (Iterator i=dettagli.iterator();i.hasNext();){
			Buono_carico_scarico_dettBulk dettaglio=(Buono_carico_scarico_dettBulk)i.next();
			Buono_carico_scarico_dettBulk new_dettaglio=(Buono_carico_scarico_dettBulk)dettaglio.clone();
			new_dettaglio.setPg_buono_c_s(pg);
			new_dettaglio.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			super.creaConBulk(userContext,new_dettaglio);
		}			
		if (definitivo !=null) {
			if (definitivo.getDettagliRigheHash() != null && !definitivo.getDettagliRigheHash().isEmpty()){
				Fattura_passiva_rigaBulk riga_fattura = null;
				Buono_carico_scarico_dettBulk inventario_dett = null;
				Inventario_beniBulk bene = null;	
				Ass_inv_bene_fatturaBulk nuova_associazione=null;	
					for (Enumeration e = definitivo.getDettagliRigheHash().keys(); e.hasMoreElements();){
						riga_fattura = (Fattura_passiva_rigaBulk)e.nextElement();
						BulkList beni_associati = (BulkList) definitivo.getDettagliRigheHash().get(riga_fattura);
						for (Iterator i = beni_associati.iterator(); i.hasNext();){
							inventario_dett = (Buono_carico_scarico_dettBulk)i.next();
							bene  = inventario_dett.getBene();
							nuova_associazione = new Ass_inv_bene_fatturaBulk();
							nuova_associazione.setRiga_fatt_pass((Fattura_passiva_rigaIBulk)riga_fattura);
							nuova_associazione.setTest_buono(definitivo);
							nuova_associazione.setNr_inventario(bene.getNr_inventario());
							nuova_associazione.setProgressivo(bene.getProgressivo());
							nuova_associazione.setToBeCreated();
							super.creaConBulk(userContext,nuova_associazione);
						}			
					}
			}
			
		}
	
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(buonoTemporaneo, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(buonoTemporaneo, e);
	}	
}
private void aggiornaEstremiFattura(UserContext userContext, Fattura_passivaBulk fattura, Buono_carico_scaricoBulk buono_temporaneo) throws ComponentException {
	try{	
		Inventario_beni_apgHome apgHome =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
		SQLBuilder sql=apgHome.createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buono_temporaneo.getLocal_transactionID());
		List beniApg=apgHome.fetchAll(sql);
		for (Iterator i=beniApg.iterator();i.hasNext();){
			Inventario_beni_apgBulk beneApg =(Inventario_beni_apgBulk)i.next();
			beneApg.setCd_cds(fattura.getCd_cds());
			beneApg.setCd_unita_organizzativa(fattura.getCd_unita_organizzativa());
			beneApg.setPg_fattura(fattura.getPg_fattura_passiva());
			updateBulk(userContext,beneApg);
		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);	
	 }
}
private void aggiornaBuoniScaricoTemporanei(
		UserContext userContext,
		Fattura_passivaBulk fa,
		Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {

		try {
			Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
			Long pg = numHome.getNextPg(userContext,
							buonoTemporaneo.getEsercizio(),
							buonoTemporaneo.getPg_inventario(),
							buonoTemporaneo.getTi_documento(),
							userContext.getUser());
			
			Buono_carico_scaricoBulk definitivo= (Buono_carico_scaricoBulk)buonoTemporaneo.clone();
			definitivo.setPg_buono_c_s(pg);
			definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
			Buono_carico_scaricoHome home = (Buono_carico_scaricoHome)getHome(userContext, definitivo);
			home.makePersistentScaricoDaFattura(definitivo,fa);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(buonoTemporaneo, e);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(buonoTemporaneo, e);
		}	
	}

private void aggiornaCarichiInventario(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	if (fattura_passiva != null && fattura_passiva instanceof Nota_di_creditoBulk) {
		CarichiInventarioTable carichiInventarioHash = fattura_passiva.getCarichiInventarioHash();
		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
			Vector carichiTemporanei = new Vector();
			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
				if (buonoCS.isByFattura() &&
					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(carichiTemporanei, buonoCS))
					carichiTemporanei.add(buonoCS);
			}			
			for (Iterator i = carichiTemporanei.iterator(); i.hasNext();){
				Buono_carico_scaricoBulk buono_temporaneo=(Buono_carico_scaricoBulk)i.next();
				aggiornaEstremiFattura(userContext,fattura_passiva,buono_temporaneo);
				aggiornaBuoniScaricoTemporanei(userContext, fattura_passiva, buono_temporaneo);				
			}
		}
	}
	else if (fattura_passiva != null){
		CarichiInventarioTable carichiInventarioHash = fattura_passiva.getCarichiInventarioHash();
		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
			Vector carichiTemporanei = new Vector();
			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
				Buono_carico_scaricoHome home = (Buono_carico_scaricoHome)getHome(userContext, buonoCS);
				if (buonoCS.isByFattura() &&
					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(carichiTemporanei, buonoCS))
					carichiTemporanei.add(buonoCS);
				// Rp test su formazione ok
					 
                 for(Iterator g= buonoCS.getBuono_carico_scarico_dettColl().iterator();g.hasNext();){
					Buono_carico_scarico_dettBulk buono=((Buono_carico_scarico_dettBulk)g.next());
					boolean exit=false;
                  	BigDecimal tot_perc_cdr=new BigDecimal(0);
					for(Iterator f =fattura_passiva.getFattura_passiva_dettColl().iterator(); (f.hasNext()&&!exit);){
						Fattura_passiva_rigaIBulk fattura=((Fattura_passiva_rigaIBulk)f.next());
						for(Iterator coll =((Buono_carico_scaricoBulk)buonoCS).getDettagliFatturaColl().iterator(); (coll.hasNext()&&!exit);){
							Fattura_passiva_rigaIBulk fattura_coll=((Fattura_passiva_rigaIBulk)coll.next());
						if (fattura_coll.equalsByPrimaryKey(fattura)) {
                        try {                    	
							Obbligazione_scadenzarioHome obblHome = ((Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class, null, getFetchPolicyName("findForInventario")));
							ObbligazioneBulk obblig =fattura.getObbligazione_scadenziario().getObbligazione();
							ObbligazioneHome obbligHome = (ObbligazioneHome) getHome(userContext, obblig.getClass());
							obblig.setObbligazione_scadenzarioColl(new BulkList(obbligHome.findObbligazione_scadenzarioList(obblig)));
							for ( Iterator i = obblig.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
							{
								Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) i.next();
								if (os.getIm_associato_doc_amm().doubleValue()!=0 && os.equalsByPrimaryKey(fattura.getObbligazione_scadenziario())){
									os.setObbligazione( obblig );
									os.setObbligazione_scad_voceColl( new BulkList( obblHome.findObbligazione_scad_voceList( userContext,os )));
									fattura.setObbligazione_scadenziario(os);							
									obblig.refreshDettagliScadenzarioPerCdrECapitoli();
										for (Iterator lista_cdraggr =obblig.getCdrAggregatoColl().iterator();lista_cdraggr.hasNext();){	
											Obbligazione_scad_voce_aggregatoBulk cdraggr=(Obbligazione_scad_voce_aggregatoBulk) lista_cdraggr.next();
											if (cdraggr!=null && cdraggr.getImporto()!=null && cdraggr.getImporto().doubleValue() != 0){
												BigDecimal tot_perc_la=new BigDecimal(0);
												Iterator listaScad_voce = obblHome.findObbligazione_scad_voceList(userContext,fattura.getObbligazione_scadenziario()).iterator();
												for (Iterator x =listaScad_voce;x.hasNext();){
													Obbligazione_scad_voceBulk dett =(Obbligazione_scad_voceBulk)x.next();
													getHomeCache(userContext).fetchAll(userContext);
													WorkpackageBulk linea_att =dett.getLinea_attivita();
													if (linea_att.getCd_centro_responsabilita()==cdraggr.getCodice()){
														if ( dett.getObbligazione_scadenzario().getIm_scadenza().doubleValue() != 0)
															dett.setPrc((dett.getIm_voce().multiply( new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
															if ( dett.getPrc()!=null && dett.getPrc().compareTo(new BigDecimal(0))!=0 && dett.getObbligazione_scadenzario().getIm_associato_doc_amm().doubleValue()!=0) {						    	
																it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk new_utilizzatore_la
																= new it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(),linea_att.getCd_centro_responsabilita(),
																		buono.getNr_inventario(),
																		buono.getPg_inventario(),new Long(buono.getProgressivo().longValue()));
																BigDecimal perc_cdr=(cdraggr.getImporto().multiply(new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(),2,6));
																tot_perc_cdr=tot_perc_cdr.add(perc_cdr);
																tot_perc_la=tot_perc_la.add(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6));
																if ((tot_perc_cdr.doubleValue()>=100&&tot_perc_la.doubleValue()>=100)){
																	exit=true; 
																}
																new_utilizzatore_la.setPercentuale_utilizzo_cdr(perc_cdr);
																new_utilizzatore_la.setPercentuale_utilizzo_la(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6));
																new_utilizzatore_la.setToBeCreated();
																if ((perc_cdr.compareTo(new BigDecimal(100))<=0) ||(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6).compareTo(new BigDecimal(100))<=0)){
																	Inventario_utilizzatori_laHome Inventario_utilizzatore_laHome = (Inventario_utilizzatori_laHome)getHome(userContext, Inventario_utilizzatori_laBulk.class);
																	Inventario_utilizzatori_laBulk utilizzatore = (Inventario_utilizzatori_laBulk)Inventario_utilizzatore_laHome.findByPrimaryKey(new Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(),linea_att.getCd_centro_responsabilita(),
																			buono.getNr_inventario(),
																			buono.getPg_inventario(),new Long(buono.getProgressivo().longValue())));
																	if (!new_utilizzatore_la.equalsByPrimaryKey(utilizzatore))	
																		super.insertBulk(userContext,new_utilizzatore_la,true);
																}
															}	
														}
													}	
											}
										}	
								Iterator listaScad_voce = obblHome.findObbligazione_scad_voceList(userContext,fattura.getObbligazione_scadenziario()).iterator();
									getHomeCache(userContext).fetchAll(userContext);
								}	
					}					
			} catch (IntrospectionException e1) {
				throw new ComponentException(e1);
			} catch (PersistencyException e1) {
				throw new ComponentException(e1);
			}
       }	
	}	
  }
}
              //fine commento 
			}
			for (Iterator i = carichiTemporanei.iterator(); i.hasNext();){
				Buono_carico_scaricoBulk buono_temporaneo=(Buono_carico_scaricoBulk)i.next();
				aggiornaBuoniCaricoTemporanei(userContext, fattura_passiva, buono_temporaneo);
				cancellaBuoniCaricoTemporanei(userContext, fattura_passiva,buono_temporaneo);
				
			}		
		}
	}
}
private void cancellaBuoniCaricoTemporanei(UserContext userContext, Fattura_passivaBulk fattura_passiva, Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException 
{
	try{
		Buono_carico_scarico_dettHome home = (Buono_carico_scarico_dettHome)getHome(userContext, Buono_carico_scarico_dettBulk.class);
		SQLBuilder sql=home.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoTemporaneo.getEsercizio().toString());
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoTemporaneo.getPg_inventario().toString());
		sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoTemporaneo.getTi_documento());
		sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoTemporaneo.getPg_buono_c_s().toString());
		List dettagli = home.fetchAll(sql);
		
		for (Iterator i=dettagli.iterator();i.hasNext();){			
			Buono_carico_scarico_dettBulk dettaglio=(Buono_carico_scarico_dettBulk)i.next();
			dettaglio.setToBeDeleted();
			super.eliminaConBulk(userContext,dettaglio);
		}
		buonoTemporaneo.setToBeDeleted();
		super.eliminaConBulk(userContext,buonoTemporaneo);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(buonoTemporaneo, e);
	}
}
private void aggiornaCogeCoan(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva,
	IDocumentoContabileBulk docCont)
	throws ComponentException {

	try {
		if (docCont != null && fatturaPassiva != null && fatturaPassiva.getDefferredSaldi() != null) {
			IDocumentoContabileBulk key = fatturaPassiva.getDefferredSaldoFor(docCont);
			if (key != null) {
				java.util.Map values = (java.util.Map)fatturaPassiva.getDefferredSaldi().get(key);

				//caso di creazione o di nessuna modifica sui doc cont
				if (values == null) return;
				
				//QUI chiamare component del documento contabile interessato
				String jndiName = null;
				Class clazz = null;
				DocumentoContabileComponentSession session = null;
				if (docCont instanceof ObbligazioneBulk) {
					jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
					clazz = ObbligazioneAbstractComponentSession.class;
					session = 
						(ObbligazioneAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						jndiName,clazz);
				} else if (docCont instanceof AccertamentoBulk) {
					jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
					clazz = AccertamentoAbstractComponentSession.class;
					session = 
						(AccertamentoAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						jndiName,clazz);
				}
				if (session != null) {
					session.aggiornaCogeCoanInDifferita(userContext, key, values);
					fatturaPassiva.getDefferredSaldi().remove(key);
				}
			}
		}
	} catch (javax.ejb.EJBException e) {
		throw handleException(fatturaPassiva, e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(fatturaPassiva, e);
	}
}
private void aggiornaCogeCoanAccertamenti(
	UserContext userContext,
	Nota_di_creditoBulk notaDiCredito)
	throws ComponentException {

	if (notaDiCredito != null) {
		AccertamentiTable accertamentiHash = notaDiCredito.getAccertamenti_scadenzarioHash();
		if (accertamentiHash != null && !accertamentiHash.isEmpty()) {
			//Aggiorna coge coan per gli accertamenti NON temporanei
			for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((AccertamentiTable)accertamentiHash.clone()).keys()).keys(); e.hasMoreElements();)
				aggiornaCogeCoan(userContext, 
					notaDiCredito, 
					(IDocumentoContabileBulk)e.nextElement());
		}
	}
}
private void aggiornaCogeCoanAccertamentiDaCancellare(
	UserContext userContext,
	Nota_di_creditoBulk notaDiCredito)
	throws ComponentException {

	if (notaDiCredito != null) {
		if (notaDiCredito.getDocumentiContabiliCancellati() != null &&
			!notaDiCredito.getDocumentiContabiliCancellati().isEmpty() &&
			notaDiCredito.getAccertamentiHash() != null) {

			for (java.util.Enumeration e = notaDiCredito.getDocumentiContabiliCancellati().elements(); e.hasMoreElements();) {
				OggettoBulk oggettoBulk = (OggettoBulk)e.nextElement();
				if (oggettoBulk instanceof Accertamento_scadenzarioBulk) {
					Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)oggettoBulk;
					if (!scadenza.getAccertamento().isTemporaneo()) {
						PrimaryKeyHashtable accerts = getDocumentiContabiliNonTemporanei(userContext, notaDiCredito.getAccertamentiHash().keys());
						if (!accerts.containsKey(scadenza.getAccertamento()))
							aggiornaCogeCoan(
								userContext, 
								notaDiCredito, 
								scadenza.getAccertamento());
					}
				}
			}
		}
	}
}
private void aggiornaCogeCoanDocAmm(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva == null) return;
	
	aggiornaCogeCoanObbligazioniDaCancellare(userContext, fatturaPassiva);
	aggiornaCogeCoanObbligazioni(userContext, fatturaPassiva);

	if (fatturaPassiva instanceof Nota_di_creditoBulk) {
		aggiornaCogeCoanAccertamentiDaCancellare(userContext, (Nota_di_creditoBulk)fatturaPassiva);
		aggiornaCogeCoanAccertamenti(userContext, (Nota_di_creditoBulk)fatturaPassiva);
	}
}
private void aggiornaCogeCoanObbligazioni(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva != null) {
		ObbligazioniTable obbligazioniHash = fatturaPassiva.getFattura_passiva_obbligazioniHash();
		if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {

			//Aggiorna coge coan per le obbligazioni NON temporanee
			for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable)obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements();)
				aggiornaCogeCoan(
					userContext, 
					fatturaPassiva, 
					(IDocumentoContabileBulk)e.nextElement());
				
		}
	}
}
private void aggiornaCogeCoanObbligazioniDaCancellare(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva != null) {
		if (fatturaPassiva.getDocumentiContabiliCancellati() != null &&
			!fatturaPassiva.getDocumentiContabiliCancellati().isEmpty() &&
			fatturaPassiva.getObbligazioniHash() != null) {

			for (java.util.Enumeration e = fatturaPassiva.getDocumentiContabiliCancellati().elements(); e.hasMoreElements();) {
				OggettoBulk oggettoBulk = (OggettoBulk)e.nextElement();
				if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
					Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)oggettoBulk;
					if (!scadenza.getObbligazione().isTemporaneo()) {
						PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, fatturaPassiva.getObbligazioniHash().keys());
						if (!obbligs.containsKey(scadenza.getObbligazione()))
							aggiornaCogeCoan(
								userContext, 
								fatturaPassiva, 
								scadenza.getObbligazione());
					}
				}
			}
		}
	}
}
//^^@@
/** 
  *  Calcolo totali di fattura.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Vegono calcolati i totali per la fattura passata in argomento.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public Fattura_passivaBulk aggiornaImportiTotali(it.cnr.jada.UserContext uc, Fattura_passivaBulk fattura) 
 	throws ComponentException{

	fattura.aggiornaImportiTotali();
	return fattura;
}
private void aggiornaLetteraPagamentoEstero(UserContext userContext,Lettera_pagam_esteroBulk lettera) throws ComponentException {

	try {
		checkSQLConstraints(userContext,lettera,false,true);
		basicAggiornaLetteraPagamentoEstero(userContext, lettera, lettera.getIm_pagamento());
		lettera.setToBeUpdated();
		makeBulkPersistent(userContext, lettera);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(lettera,e);
	}

}
private void aggiornaObbligazioni(
	UserContext userContext,
	Fattura_passivaBulk fattura_passiva,
	OptionRequestParameter status)
	throws ComponentException {

	if (fattura_passiva != null) {
		ObbligazioniTable obbligazioniHash = fattura_passiva.getFattura_passiva_obbligazioniHash();
		if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {
			Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class);

			//Aggiorna i saldi per le obbligazioni NON temporanee
			for (java.util.Enumeration e = getDocumentiContabiliNonTemporanei(userContext, ((ObbligazioniTable)obbligazioniHash.clone()).keys()).keys(); e.hasMoreElements();)
				aggiornaSaldi(
					userContext, 
					fattura_passiva, 
					(IDocumentoContabileBulk)e.nextElement(),
					status);
				
			PrimaryKeyHashtable obblTemporanee = getDocumentiContabiliTemporanei(userContext, ((ObbligazioniTable)obbligazioniHash.clone()).keys());
			for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements();) {
				ObbligazioneBulk obblT = (ObbligazioneBulk)e.nextElement();

				//Aggiorna i saldi per le obbligazioni temporanee
				//DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
				aggiornaSaldi(userContext, fattura_passiva, obblT, status);
				
				aggiornaObbligazioniTemporanee(userContext, obblT);
				obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable(obblTemporanee);
				for (Iterator i = ((Vector)obblTemporanee.get(obblT)).iterator(); i.hasNext();) 
					((ObbligazioneBulk)i.next()).setPg_obbligazione(obblT.getPg_obbligazione());
			}
			ObbligazioniTable newObbligazioniHash = new ObbligazioniTable(obbligazioniHash);

			//* Questo codice NON � una ripetizione. E' STRETTAMENTE NECESSARIO
			//perch� talvolta a seconda della modalit� operativa di selezione della scadenza
			//nella rigaAssociata non ho la stessa istanza (riselezione di una scadenza temporanea
			//gi� contabilizzata dopo la ricerca obbligazione). --> devo aggionrare il PG_OBBL
			//dopo averlo reso definitivo e PRIMA di risettarlo nel doc amm
			//for (java.util.Enumeration en = newObbligazioniHash.keys(); en.hasMoreElements();) {
				//Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk)en.nextElement();
				//for(Iterator syncronizer = ((Vector)newObbligazioniHash.get(scad)).iterator(); syncronizer.hasNext();) {
					//Fattura_passiva_rigaBulk rigaAssociata = (Fattura_passiva_rigaBulk)syncronizer.next();
					//if (!rigaAssociata.getObbligazione_scadenziario().equalsByPrimaryKey(scad))
						//rigaAssociata.setObbligazione_scadenziario(scad);
				//}
			//}
			//*//
			
			fattura_passiva.setFattura_passiva_obbligazioniHash(newObbligazioniHash);
			for (java.util.Enumeration e = ((ObbligazioniTable)newObbligazioniHash.clone()).keys(); e.hasMoreElements();) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
				java.math.BigDecimal im_ass = null;
				if (fattura_passiva.isEstera() &&
					fattura_passiva.getLettera_pagamento_estero() != null &&
					fattura_passiva.getLettera_pagamento_estero().getIm_pagamento() != null &&
					fattura_passiva.getLettera_pagamento_estero().getIm_pagamento().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) != 0)
					//Caso di collegamento a documento 1210	con sospeso inserito
					im_ass = scadenza.getIm_scadenza();
				else
					//Tutti gli altri casi
					im_ass = calcolaTotaleObbligazionePer(userContext, scadenza, fattura_passiva);
					
				scadenza.setIm_associato_doc_amm(im_ass);
				updateImportoAssociatoDocAmm(userContext, scadenza);
			}
		}
	}
}

private void aggiornaObbligazioniSuCancellazione(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva,
	java.util.Enumeration scadenzeDaCancellare,
	java.util.Collection scadenzeConfermate,
	OptionRequestParameter status)
	throws ComponentException {

	if (scadenzeDaCancellare != null) {

		it.cnr.jada.bulk.PrimaryKeyHashtable obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable();
		for (java.util.Enumeration e = scadenzeDaCancellare; e.hasMoreElements();) {
			OggettoBulk oggettoBulk = (OggettoBulk)e.nextElement();
			if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)oggettoBulk;
				if (scadenza.getObbligazione().isTemporaneo()) {
					if (!obblTemporanee.containsKey(scadenza.getObbligazione())) {
						Vector allInstances = new java.util.Vector();
						allInstances.addElement(scadenza);
						obblTemporanee.put(scadenza.getObbligazione(), allInstances);
					} else {
						((Vector)obblTemporanee.get(scadenza.getObbligazione())).add(scadenza);
					}
				} else if (!fatturaPassiva.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
					PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, fatturaPassiva.getObbligazioniHash().keys());
					if (!obbligs.containsKey(scadenza.getObbligazione()))
						aggiornaSaldi(
							userContext, 
							fatturaPassiva, 
							scadenza.getObbligazione(),
							status);
					scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
					updateImportoAssociatoDocAmm(userContext, scadenza);
				}
            	/**
            	 * Devo aggiornare i Saldi per quelle scadenze modificate e riportate
            	 * ma poi scollegate dal documento 
            	 * Marco Spasiano 05/05/2006
            	 */
                aggiornaSaldi(userContext, fatturaPassiva, scadenza.getObbligazione(), status);
			}
		}
		for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements();) {
			ObbligazioneBulk obblT = (ObbligazioneBulk)e.nextElement();

			//Aggiorna i saldi per le obbligazioni temporanee
			//DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
			PrimaryKeyHashtable obbligs = getDocumentiContabiliTemporanei(userContext, fatturaPassiva.getObbligazioniHash().keys());
			if (!obbligs.containsKey(obblT))
				aggiornaSaldi(
						userContext, 
						fatturaPassiva, 
						obblT,
						status);

			if (scadenzeConfermate == null || !it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(scadenzeConfermate, obblT))
				aggiornaObbligazioniTemporanee(userContext, obblT);
		}
	}
}
private void aggiornaObbligazioniTemporanee(UserContext userContext,ObbligazioneBulk obbligazioneTemporanea) throws ComponentException {

	try {
		Numerazione_doc_contHome numHome = (Numerazione_doc_contHome) getHomeCache(userContext).getHome(Numerazione_doc_contBulk.class);
		Long pg = null;
		pg = numHome.getNextPg(userContext,
						obbligazioneTemporanea.getEsercizio(), 
						obbligazioneTemporanea.getCd_cds(), 
						obbligazioneTemporanea.getCd_tipo_documento_cont(), 
						obbligazioneTemporanea.getUser());
		ObbligazioneHome home = (ObbligazioneHome)getHome(userContext, obbligazioneTemporanea);
		home.confirmObbligazioneTemporanea(userContext, obbligazioneTemporanea, pg);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(obbligazioneTemporanea, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(obbligazioneTemporanea, e);
	}	
}

private void aggiornaRigheFatturaPassivaDiOrigine(UserContext aUC,Nota_di_creditoBulk ndc) throws ComponentException {

	for (Iterator i = ndc.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
		Fattura_passiva_rigaIBulk rigaFP = ((Nota_di_credito_rigaBulk)i.next()).getRiga_fattura_origine();
		basicAggiornaRigheFatturaPassivaDiOrigine(aUC, rigaFP);
	}
}

private void aggiornaRigheFatturaPassivaDiOrigine(UserContext aUC,Nota_di_debitoBulk ndd) throws ComponentException {

	for (Iterator i = ndd.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
		Fattura_passiva_rigaIBulk rigaFP = ((Nota_di_debito_rigaBulk)i.next()).getRiga_fattura_origine();
		basicAggiornaRigheFatturaPassivaDiOrigine(aUC, rigaFP);
	}
}
private void aggiornaSaldi(
	it.cnr.jada.UserContext uc,
	Fattura_passivaBulk fp,
	IDocumentoContabileBulk docCont,
	OptionRequestParameter status) 
 	throws ComponentException{

	try {
		if (docCont != null && fp != null && fp.getDefferredSaldi() != null) {
			IDocumentoContabileBulk key = fp.getDefferredSaldoFor(docCont);
			if (key != null) {
				java.util.Map values = (java.util.Map)fp.getDefferredSaldi().get(key);
				//QUI chiamare component del documento contabile interessato
				String jndiName = null;
				Class clazz = null;
				DocumentoContabileComponentSession session = null;
				if (docCont instanceof ObbligazioneBulk) {
					jndiName = "CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession";
					clazz = ObbligazioneAbstractComponentSession.class;
					session = 
						(ObbligazioneAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						jndiName,clazz);
				} else if (docCont instanceof AccertamentoBulk) {
					jndiName = "CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession";
					clazz = AccertamentoAbstractComponentSession.class;
					session = 
						(AccertamentoAbstractComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
						jndiName,clazz);
				}
				if (session != null) {
					session.aggiornaSaldiInDifferita(uc, key, values, status);
					//NON Differibile: si rischia di riprocessare i saldi impropriamente
					fp.getDefferredSaldi().remove(key);
				}
			}
		}
	} catch (javax.ejb.EJBException e) {
		throw handleException(fp, e);
	} catch (java.rmi.RemoteException e) {
		throw handleException(fp, e);
	}
}
//^^@@
/** 
  *  Inserimento del mandato.
  *    PreCondition:
  *      Si sta inserendo o modificando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato P o L.
  *  Annullamento del mandato.
  *    PreCondition:
  *      Si sta annullando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato A.
  *  Cancellazione del mandato.
  *    PreCondition:
  *      Si sta cancellando il mandato.
  *    PostCondition:
  *      Aggiorna lo stato dei documenti amministrativi associati al mandato con stato Q.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public void aggiornaStatoDocumentiAmministrativi(
								it.cnr.jada.UserContext userContext, 
								String cd_cds, 
								String cd_unita_organizzativa, 
								String tipo_documento, 
								Integer esercizio, 
								Long progressivo, 
								String action) 
								throws it.cnr.jada.comp.ComponentException {

	LoggableStatement cs = null;
	try {
		try	{
			cs = new LoggableStatement(getConnection(userContext),
				"{ call " +
	//			"{ ? = call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB300.leggiMandatoReversale(?, ?, ?, ?, ?, ?) }",false,this.getClass());
	//		cs.registerOutParameter( 1, java.sql.Types.CHAR );				
			cs.setString(1, cd_cds);
			cs.setInt(2, esercizio.intValue());
			cs.setLong(3, progressivo.longValue());
			cs.setString(4, tipo_documento);
			cs.setString(5, action);
			cs.setString(6, userContext.getUser());
			cs.executeQuery();
	//		char result = cs.getString( 1 ).charAt( 0 );		

			//if ( result == 'N' )
				//throw handleException( new CheckDisponibilitaCassaFailed( "L'importo dei dettagli inseriti supera la disponibilit� di cassa del capitolo" ));
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
		}
	} catch (SQLException ex) {
		throw handleException(ex);
	}
}

private void assegnaProgressivo(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	try {
		// Assegno un nuovo progressivo alla fattura
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(fattura_passiva);
		fattura_passiva.setPg_fattura_passiva(progressiviSession.getNextPG(userContext, numerazione));
	} catch (Throwable t) {
		throw handleException(fattura_passiva, t);
	}
}
private Nota_di_debitoBulk basicAddebitaDettaglio(
	UserContext context,
	Nota_di_debitoBulk ndD,
	Nota_di_debito_rigaBulk rigaNdD,
	Fattura_passiva_rigaIBulk rigaAssociata)
	throws ComponentException {

	Obbligazione_scadenzarioBulk obbligazioneSelezionata = rigaAssociata.getObbligazione_scadenziario();
	if (obbligazioneSelezionata != null) {

		if (ndD.getFattura_passiva_obbligazioniHash() != null) {
			Obbligazione_scadenzarioBulk key = ndD.getFattura_passiva_obbligazioniHash().getKey(obbligazioneSelezionata);
			if (key != null) obbligazioneSelezionata = key;
			else obbligazioneSelezionata = caricaScadenzaObbligazionePer(context, obbligazioneSelezionata);
		} else obbligazioneSelezionata = caricaScadenzaObbligazionePer(context, obbligazioneSelezionata);
		
			
		rigaNdD.setObbligazione_scadenziario(obbligazioneSelezionata);
		rigaNdD.setRiga_fattura_associata(rigaAssociata);
		rigaNdD.setStato_cofi(rigaNdD.STATO_CONTABILIZZATO);
		rigaNdD.setToBeUpdated();
		ndD.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, rigaNdD);
		//ndD.setStato_coan(ndD.NON_PROCESSARE_IN_COAN);
		ndD.setAndVerifyStatus();
		try {
			ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession)EJBCommonServices.createEJB(
															"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
															ObbligazioneAbstractComponentSession.class);
			session.lockScadenza(context, obbligazioneSelezionata);
		} catch (Throwable t) {
			throw handleException(ndD, t);
		}
	}
	return ndD;
}
private void basicAggiornaLetteraPagamentoEstero(
	UserContext userContext,
	Lettera_pagam_esteroBulk lettera,
	java.math.BigDecimal impAssDoc1210)
	throws ComponentException {

	if (lettera == null) return;
	if (impAssDoc1210 == null)
		impAssDoc1210 = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
		
	if (lettera.getSospesiCancellati() != null && !lettera.getSospesiCancellati().isEmpty())
		for (Iterator i = lettera.getSospesiCancellati().iterator(); i.hasNext();) {
			SospesoBulk sospeso = (SospesoBulk)i.next();
			if (sospeso.getCrudStatus() == OggettoBulk.NORMAL)
				liberaSospeso(userContext, sospeso);
		}
	
	if (lettera.getSospeso() != null) {
		if (lettera.getSospeso().getCrudStatus() != OggettoBulk.NORMAL)
			lettera.setSospeso(null);
		else {
			lettera.getSospeso().setIm_ass_mod_1210(impAssDoc1210);
			lettera.getSospeso().setToBeUpdated();			
		}
	}
}

private void basicAggiornaRigheFatturaPassivaDiOrigine(UserContext aUC,Fattura_passiva_rigaIBulk rigaFP) throws ComponentException {

	try {
		updateBulk(aUC, rigaFP);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(rigaFP, e);
	}
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rivelata.
  *    PostCondition:
  *      Permesso il ritorno dell cambio nella fattura.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */
//^^@@

private Fattura_passivaBulk basicCercaCambio(
	it.cnr.jada.UserContext uc,
	Fattura_passivaBulk fattura, 
	java.sql.Timestamp dataCambio) 
	throws ComponentException {
	
	if (dataCambio == null)
		throw new it.cnr.jada.comp.ApplicationException( "Impostare la data di emissione della " + fattura.getDescrizioneEntita() + " del fornitore!");

	DivisaBulk valuta = fattura.getValuta();
	CambioHome cambioHome = (CambioHome)getHomeCache(uc).getHome(CambioBulk.class);
	CambioBulk cambioValido = null;
	try{
		cambioValido = cambioHome.getCambio(valuta, dataCambio);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		fattura = setChangeDataToEur(uc, fattura);
		throw handleException(fattura, new it.cnr.jada.comp.ApplicationException( "Non esiste una valuta per il periodo specificato!"));
	}
	
	fattura.setInizio_validita_valuta(cambioValido.getDt_inizio_validita());
	fattura.setFine_validita_valuta(cambioValido.getDt_fine_validita());
	fattura.setCambio(cambioValido.getCambio());
	return aggiornaImportiTotali(uc, fattura);
}
private Nota_di_creditoBulk basicStornaDettaglio(
	UserContext context,
	Nota_di_creditoBulk ndC,
	Nota_di_credito_rigaBulk rigaNdC,
	Fattura_passiva_rigaIBulk rigaAssociata)
	throws ComponentException {

	Obbligazione_scadenzarioBulk obbligazioneSelezionata = rigaAssociata.getObbligazione_scadenziario();
	if (obbligazioneSelezionata != null) {

		if (ndC.getFattura_passiva_obbligazioniHash() != null) {
			Obbligazione_scadenzarioBulk key = ndC.getFattura_passiva_obbligazioniHash().getKey(obbligazioneSelezionata);
			if (key != null) 
				obbligazioneSelezionata = key;
			else 
				obbligazioneSelezionata = caricaScadenzaObbligazionePer(context, obbligazioneSelezionata);
			//Questo controllo NON � + necessario. Viene fatto dall'obbligazione
			//java.util.List dettagliGiaCollegati = (java.util.List)ndC.getFattura_passiva_obbligazioniHash().get(obbligazioneSelezionata);
			//if (dettagliGiaCollegati != null && !dettagliGiaCollegati.isEmpty()) {
				//java.math.BigDecimal importo = calcolaTotalePer(dettagliGiaCollegati, ndC.quadraturaInDeroga());
				//java.math.BigDecimal impDiRiga = (ndC.quadraturaInDeroga()) ?
													//rigaNdC.getIm_imponibile() :
													//rigaNdC.getIm_imponibile().add(rigaNdC.getIm_iva());
				//if (importo.subtract(impDiRiga).compareTo(obbligazioneSelezionata.getIm_scadenza()) > 0)
					//throw new it.cnr.jada.comp.ApplicationException("I dettagli che si sta cercando di collegare superano la disponibilt� della scadenza!");
			//}
		} else obbligazioneSelezionata = caricaScadenzaObbligazionePer(context, obbligazioneSelezionata);

		//Nel caso in cui ho dettagli pagati e seleziono una scadenza della stessa nota di credito
		//che sto gi� modificando.
		rigaNdC.setObbligazione_scadenziario(obbligazioneSelezionata);
		rigaNdC.setRiga_fattura_associata(rigaAssociata);
		rigaNdC.setStato_cofi(rigaNdC.STATO_CONTABILIZZATO);
		rigaNdC.setToBeUpdated();
		ndC.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, rigaNdC);
		//ndC.setStato_coan(ndC.NON_PROCESSARE_IN_COAN);
		ndC.setAndVerifyStatus();
		try {
			ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession)EJBCommonServices.createEJB(
															"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
															ObbligazioneAbstractComponentSession.class);
			session.lockScadenza(context, obbligazioneSelezionata);
		} catch (Throwable t) {
			throw handleException(ndC, t);
		}
	}
	return ndC;
}
private Nota_di_creditoBulk basicStornaDettaglio(
	UserContext context,
	Nota_di_creditoBulk ndC,
	Nota_di_credito_rigaBulk rigaNdC,
	Accertamento_scadenzarioBulk scadenza)
	throws ComponentException {

	if (scadenza != null) {
		
		if (ndC.getAccertamenti_scadenzarioHash() != null) {
			Accertamento_scadenzarioBulk key = ndC.getAccertamenti_scadenzarioHash().getKey(scadenza);
			if (key != null) scadenza = key;
			else scadenza = caricaScadenzaAccertamentoPer(context, scadenza);
		} else scadenza = caricaScadenzaAccertamentoPer(context, scadenza);

		rigaNdC.setAccertamento_scadenzario(scadenza);
		rigaNdC.setRiga_fattura_associata(rigaNdC.getRiga_fattura_origine());
		rigaNdC.setStato_cofi(rigaNdC.STATO_CONTABILIZZATO);
		impostaCollegamentoCapitoloPerTrovato(context, rigaNdC);
		rigaNdC.setToBeUpdated();
		ndC.addToAccertamenti_scadenzarioHash(scadenza, rigaNdC);
		ndC.setAndVerifyStatus();
		try {
			AccertamentoAbstractComponentSession session = (AccertamentoAbstractComponentSession)EJBCommonServices.createEJB(
															"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
															AccertamentoAbstractComponentSession.class);
			session.lockScadenza(context, scadenza);
		} catch (Throwable t) {
			throw handleException(ndC, t);
		}
	}
	return ndC;
}
private java.math.BigDecimal calcolaTotaleIVAPer(
	java.util.List selectedModels)
	throws it.cnr.jada.comp.ApplicationException {

	java.math.BigDecimal importo = new java.math.BigDecimal(0);
	java.math.BigDecimal impStorniTot=new java.math.BigDecimal(0);
	java.math.BigDecimal impStorniSenzaIVA=new java.math.BigDecimal(0);
	java.math.BigDecimal impAddebitiTot=new java.math.BigDecimal(0);
	java.math.BigDecimal impAddebitiSenzaIVA=new java.math.BigDecimal(0);
	if (selectedModels != null) {
		for (java.util.Iterator i = selectedModels.iterator(); i.hasNext();) {
			Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk)i.next();
			if(rigaSelected  instanceof Fattura_passiva_rigaIBulk) {
				Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk) rigaSelected;
				if (riga.getFattura_passivaI().getAddebitiHashMap()!=null  && riga.getFattura_passivaI().getAddebitiHashMap().size()!=0){
					impAddebitiTot = calcolaTotalePer((Vector)riga.getFattura_passivaI().getAddebitiHashMap().get(riga), false);
					impAddebitiSenzaIVA = calcolaTotalePer((Vector)riga.getFattura_passivaI().getAddebitiHashMap().get(riga), true);
					importo = importo.add(impAddebitiTot.subtract(impAddebitiSenzaIVA));
				}
				if (riga.getFattura_passivaI().getStorniHashMap()!=null  && riga.getFattura_passivaI().getStorniHashMap().size()!=0 ){
					impStorniTot = calcolaTotalePer((Vector)riga.getFattura_passivaI().getStorniHashMap().get(riga), false);
					impStorniSenzaIVA = calcolaTotalePer((Vector)riga.getFattura_passivaI().getStorniHashMap().get(riga), true);
					importo = importo.subtract(impStorniTot.subtract(impStorniSenzaIVA));
				}
				importo = importo.add(riga.getIm_iva());	
			}else
				importo = importo.add(rigaSelected.getIm_iva());
		}
	}
	importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	return importo;
}
private java.math.BigDecimal calcolaTotaleObbligazionePer(
	it.cnr.jada.UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza,
	Fattura_passivaBulk fatturaPassiva)
	throws it.cnr.jada.comp.ComponentException {

	java.math.BigDecimal imp = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	if (fatturaPassiva instanceof Nota_di_creditoBulk) {
		imp = calcolaTotaleObbligazionePerNdC(
									userContext,
									scadenza,
									(Nota_di_creditoBulk)fatturaPassiva);
	} else if (fatturaPassiva instanceof Nota_di_debitoBulk) {
		imp = calcolaTotaleObbligazionePerNdD(
									userContext,
									scadenza,
									(Nota_di_debitoBulk)fatturaPassiva);
	} else {
		imp = calcolaTotaleObbligazionePerFP(
									userContext,
									scadenza,
									(Fattura_passiva_IBulk)fatturaPassiva);
	}
	return imp;
}
private java.math.BigDecimal calcolaTotaleObbligazionePerFP(
	it.cnr.jada.UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza,
	Fattura_passiva_IBulk fatturaPassiva)
	throws it.cnr.jada.comp.ComponentException {

	ObbligazioniTable obbligazioniHash = fatturaPassiva.getFattura_passiva_obbligazioniHash();
	Vector dettagli = (Vector)obbligazioniHash.get(scadenza);
	java.math.BigDecimal impTotaleDettagli = calcolaTotalePer(dettagli, fatturaPassiva.quadraturaInDeroga());
	java.math.BigDecimal impTotaleStornati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal impTotaleAddebitati = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.util.HashMap dettagliDaStornare = caricaStorniPer(userContext, dettagli);
	java.util.HashMap dettagliDaAddebitare = caricaAddebitiPer(userContext, dettagli);
	if (dettagliDaStornare != null) {
		Vector dettagliStornati = null;
		boolean quadraturaInDeroga = false;
		for (Iterator dett = dettagliDaStornare.keySet().iterator(); dett.hasNext();) {
			Fattura_passiva_rigaIBulk key = (Fattura_passiva_rigaIBulk)dett.next();
			for (Iterator i = ((Vector)dettagliDaStornare.get(key)).iterator(); i.hasNext();) {
				Nota_di_credito_rigaBulk riga = (Nota_di_credito_rigaBulk)i.next();
				if (!quadraturaInDeroga && riga.getFattura_passiva().quadraturaInDeroga())
					quadraturaInDeroga = true;
					
				ObbligazioniTable hash = riga.getFattura_passiva().getFattura_passiva_obbligazioniHash();
				if (hash == null)
					rebuildObbligazioni(userContext, riga.getFattura_passiva());
				hash = riga.getFattura_passiva().getFattura_passiva_obbligazioniHash();
				if (hash != null) {
					Vector dettagliStornatiScadenza = (Vector)hash.get(scadenza);
					if (dettagliStornati == null) {
						dettagliStornati = new Vector();
						dettagliStornati.addAll(dettagliStornatiScadenza);
					} else {
						for (Iterator d = dettagliStornatiScadenza.iterator(); d.hasNext();) {
							Nota_di_credito_rigaBulk rigaNdC = (Nota_di_credito_rigaBulk)d.next();
							if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(dettagliStornati, rigaNdC))
								dettagliStornati.add(rigaNdC);
						}
					}
				}
			}
		}
		if (dettagliStornati != null)
			impTotaleStornati = impTotaleStornati.add(
									calcolaTotalePer(
												dettagliStornati,
												quadraturaInDeroga));
	}
	if (dettagliDaAddebitare != null) {
		Vector dettagliAddebitati = null;
		boolean quadraturaInDeroga = false;
		for (Iterator dett = dettagliDaAddebitare.keySet().iterator(); dett.hasNext();) {
			Fattura_passiva_rigaIBulk key = (Fattura_passiva_rigaIBulk)dett.next();
			for (Iterator i = ((Vector)dettagliDaAddebitare.get(key)).iterator(); i.hasNext();) {
				Nota_di_debito_rigaBulk riga = (Nota_di_debito_rigaBulk)i.next();
				ObbligazioniTable hash = riga.getFattura_passiva().getFattura_passiva_obbligazioniHash();
				if (hash == null)
					rebuildObbligazioni(userContext, riga.getFattura_passiva());
				hash = riga.getFattura_passiva().getFattura_passiva_obbligazioniHash();
				Vector dettagliAddebitatiScadenza = (Vector)hash.get(scadenza);
				if (hash != null) {
					if (dettagliAddebitati == null) {
						dettagliAddebitati = new Vector();
						dettagliAddebitati.addAll(dettagliAddebitatiScadenza);
					} else {
						for (Iterator d = dettagliAddebitatiScadenza.iterator(); d.hasNext();) {
							Nota_di_debito_rigaBulk rigaNdD = (Nota_di_debito_rigaBulk)d.next();
							if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(dettagliAddebitati, rigaNdD))
								dettagliAddebitati.add(rigaNdD);
						}
					}
				}
			}
		}
		if (dettagliAddebitati != null)
			impTotaleAddebitati = impTotaleAddebitati.add(
									calcolaTotalePer(
												dettagliAddebitati,
												quadraturaInDeroga));
	}
	return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
}
private java.math.BigDecimal calcolaTotaleObbligazionePerNdC(
	it.cnr.jada.UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza,
	Nota_di_creditoBulk notaDiCredito)
	throws it.cnr.jada.comp.ComponentException {

	ObbligazioniTable obbligazioniHash = notaDiCredito.getFattura_passiva_obbligazioniHash();
	Vector dettagli = (Vector)obbligazioniHash.get(scadenza);
	java.math.BigDecimal impTotaleDettagli = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal impTotaleStornati = calcolaTotalePer(
													dettagli, 
													notaDiCredito.quadraturaInDeroga()).add(
														calcolaTotalePer(
																	caricaStorniExceptFor(userContext, scadenza, notaDiCredito),
																	notaDiCredito.quadraturaInDeroga()));
	java.math.BigDecimal impTotaleAddebitati = calcolaTotalePer(caricaAddebitiExceptFor(userContext, scadenza, null), notaDiCredito.quadraturaInDeroga());
	Vector fattureContenute = new Vector();
	for (Iterator i = dettagli.iterator(); i.hasNext();) {
		Nota_di_credito_rigaBulk riga = (Nota_di_credito_rigaBulk)i.next();
		Fattura_passiva_IBulk fatturaOrigine = riga.getRiga_fattura_associata().getFattura_passivaI();
		if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(fattureContenute,fatturaOrigine))
			fattureContenute.add(fatturaOrigine);
	}
	for (Iterator i = fattureContenute.iterator(); i.hasNext();) {
		Fattura_passiva_IBulk fatturaPassivaI = (Fattura_passiva_IBulk)i.next();
		if (fatturaPassivaI.getFattura_passiva_dettColl() == null ||
			fatturaPassivaI.getFattura_passiva_dettColl().isEmpty()) {
			try {
				fatturaPassivaI.setFattura_passiva_dettColl(new BulkList(findDettagli(userContext, fatturaPassivaI)));
				rebuildObbligazioni(userContext, fatturaPassivaI);
			} catch (Throwable t) {
				throw handleException(t);
			}
		}

		Vector dettagliContabilizzati = (Vector)fatturaPassivaI.getFattura_passiva_obbligazioniHash().get(scadenza);
		if (dettagliContabilizzati != null)
			impTotaleDettagli = impTotaleDettagli.add(calcolaTotalePer(dettagliContabilizzati, notaDiCredito.quadraturaInDeroga()));
	}

	return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
}
private java.math.BigDecimal calcolaTotaleObbligazionePerNdD(
	it.cnr.jada.UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza,
	Nota_di_debitoBulk notaDiDebito)
	throws it.cnr.jada.comp.ComponentException {

	ObbligazioniTable obbligazioniHash = notaDiDebito.getFattura_passiva_obbligazioniHash();
	Vector dettagli = (Vector)obbligazioniHash.get(scadenza);
	java.math.BigDecimal impTotaleDettagli = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	java.math.BigDecimal impTotaleStornati = calcolaTotalePer(
														caricaStorniExceptFor(userContext, scadenza, null),
														notaDiDebito.quadraturaInDeroga());
	java.math.BigDecimal impTotaleAddebitati = calcolaTotalePer(
														dettagli,
														notaDiDebito.quadraturaInDeroga()).add(
															calcolaTotalePer(
																caricaAddebitiExceptFor(userContext, scadenza, notaDiDebito),
																notaDiDebito.quadraturaInDeroga()));
	Vector fattureContenute = new Vector();
	for (Iterator i = dettagli.iterator(); i.hasNext();) {
		Nota_di_debito_rigaBulk riga = (Nota_di_debito_rigaBulk)i.next();
		Fattura_passiva_IBulk fatturaOrigine = riga.getRiga_fattura_associata().getFattura_passivaI();
		if (!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(fattureContenute,fatturaOrigine))
			fattureContenute.add(fatturaOrigine);
	}
	for (Iterator i = fattureContenute.iterator(); i.hasNext();) {
		Fattura_passiva_IBulk fatturaPassivaI = (Fattura_passiva_IBulk)i.next();
		if (fatturaPassivaI.getFattura_passiva_dettColl() == null ||
			fatturaPassivaI.getFattura_passiva_dettColl().isEmpty()) {
			try {
				fatturaPassivaI.setFattura_passiva_dettColl(new BulkList(findDettagli(userContext, fatturaPassivaI)));
				rebuildObbligazioni(userContext, fatturaPassivaI);
			} catch (Throwable t) {
				throw handleException(t);
			}
		}

		Vector dettagliContabilizzati = (Vector)fatturaPassivaI.getFattura_passiva_obbligazioniHash().get(scadenza);
		if (dettagliContabilizzati != null)
			impTotaleDettagli = impTotaleDettagli.add(
													calcolaTotalePer(
														dettagliContabilizzati,
														notaDiDebito.quadraturaInDeroga()));
	}

	return impTotaleDettagli.add(impTotaleAddebitati).subtract(impTotaleStornati);
}
private java.math.BigDecimal calcolaTotalePer(
	java.util.List selectedModels,
	boolean escludiIVA)
	throws it.cnr.jada.comp.ApplicationException {

	java.math.BigDecimal importo = new java.math.BigDecimal(0);
	//RP 20/03/2015 
	boolean escludiIVAInt=false;
	boolean escludiIVAOld=escludiIVA;	
	if (selectedModels != null) {
		for (java.util.Iterator i = selectedModels.iterator(); i.hasNext();) {
			escludiIVA=escludiIVAOld;	
			Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk)i.next();
			if (!escludiIVA &&  rigaSelected.getVoce_iva()!=null && rigaSelected.getVoce_iva().getFl_autofattura()!=null && rigaSelected.getVoce_iva().getFl_autofattura())
				escludiIVAInt=true;
			else if (!escludiIVA && rigaSelected.getVoce_iva()!=null && rigaSelected.getVoce_iva().getFl_autofattura()!=null && !rigaSelected.getVoce_iva().getFl_autofattura())
				escludiIVAInt=false;
			if(escludiIVAInt)
				escludiIVA=escludiIVAInt;
			// fine RP 20/03/2015
			
			importo = importo.add(
				(escludiIVA	) ?
					rigaSelected.getIm_imponibile() :
					rigaSelected.getIm_imponibile().add(rigaSelected.getIm_iva()));
		}
	}

	importo = importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
	return importo;
}
private java.math.BigDecimal calcolaTotaleScadenzaAccertamentoPer(
	it.cnr.jada.UserContext userContext,
	Accertamento_scadenzarioBulk scadenza,
	Nota_di_creditoBulk notaDiCredito)
	throws it.cnr.jada.comp.ComponentException {

	Vector dettagli = (Vector)notaDiCredito.getAccertamentiHash().get(scadenza);
	return calcolaTotalePer(dettagli, notaDiCredito.quadraturaInDeroga());
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */
//^^@@

public IDocumentoAmministrativoBulk calcoloConsuntivi (UserContext aUC, IDocumentoAmministrativoBulk documentoAmministrativo) throws ComponentException {

	if (documentoAmministrativo == null) return documentoAmministrativo;

	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)documentoAmministrativo;
	fatturaPassiva.setFattura_passiva_consuntivoColl(new Vector());
	
	BulkList righeFattura = fatturaPassiva.getFattura_passiva_dettColl();
	if (righeFattura == null) return fatturaPassiva;
	
	for (Iterator i = righeFattura.iterator(); i.hasNext(); ) {
		Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();
		if (!riga.isAnnullato()) {
			Consuntivo_rigaVBulk rigaConsuntivo = getRigaConsuntivoFor(riga);
			if (rigaConsuntivo != null) {
				java.math.BigDecimal totaleImponibile = rigaConsuntivo.getTotale_imponibile().add((riga.getIm_imponibile() == null)?new java.math.BigDecimal(0):riga.getIm_imponibile());
				rigaConsuntivo.setTotale_imponibile(totaleImponibile);
				java.math.BigDecimal totaleIva = rigaConsuntivo.getTotale_iva().add((riga.getIm_iva() == null)?new java.math.BigDecimal(0):riga.getIm_iva());
				rigaConsuntivo.setTotale_iva(totaleIva);
				java.math.BigDecimal totalePrezzo = rigaConsuntivo.getTotale_prezzo().add(riga.getIm_imponibile().add(riga.getIm_iva()));
				rigaConsuntivo.setTotale_prezzo(totalePrezzo);
			}
		}
	}

	return aggiornaImportiTotali(aUC, fatturaPassiva);
}
private void callVerifyDataRegistrazione(
	UserContext userContext, 
	Fattura_passivaBulk fatturaPassiva)
	throws  ComponentException {
    	
	LoggableStatement cs = null;
	try	{
		Fattura_passivaHome fatturaHome = (Fattura_passivaHome)getHome(userContext,Fattura_passivaBulk.class);
		Fattura_passivaBulk fatturaDB = (Fattura_passivaBulk)fatturaHome.findByPrimaryKey(
		                        new Documento_amministrativo_passivoBulk(
		                        fatturaPassiva.getCd_cds(),
		                        fatturaPassiva.getCd_unita_organizzativa(),
		                        fatturaPassiva.getEsercizio(),
		                        fatturaPassiva.getPg_fattura_passiva()
		                        ));
		// Se la Fattura gi� esiste e la data di registrazione 
		// � uguale a quella precedente allora il controllo viene bypassato 
		if (fatturaDB != null && fatturaPassiva.getDt_registrazione().compareTo(fatturaDB.getDt_registrazione()) == 0)
		  return;
		cs = new LoggableStatement(getConnection(userContext), 
			"{ call " +
			it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
			"CNRCTB100.chkDtRegistrazPerIva(?, ?, ?, ?, ?) }",false,this.getClass());
		cs.setString(1, fatturaPassiva.getCd_cds_origine());
		cs.setString(2, fatturaPassiva.getCd_uo_origine());
		cs.setInt(3, fatturaPassiva.getEsercizio().intValue());
		cs.setString(4, fatturaPassiva.getCd_tipo_sezionale());
		cs.setTimestamp(5, fatturaPassiva.getDt_registrazione());
		
		cs.executeQuery();
		
	} catch (Throwable e) {
		throw handleException(fatturaPassiva, e);
	} finally {
		try {
			if (cs != null) cs.close();
		} catch (java.sql.SQLException e) {
			throw handleException(fatturaPassiva, e);
		}
 	}
}
private java.util.List caricaAddebitiExceptFor(
	UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza, 
	Nota_di_debitoBulk notaDiDebito)
	throws it.cnr.jada.comp.ComponentException {

	Nota_di_debito_rigaHome home = (Nota_di_debito_rigaHome)getHomeCache(userContext).getHome(Nota_di_debito_rigaBulk.class ,"default", "testata");
	try {
		return home.findAddebitiForObbligazioneExceptFor(scadenza, notaDiDebito);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(notaDiDebito, e);
	}
}
private java.util.HashMap caricaAddebitiPer(
	UserContext userContext,
	java.util.List dettagliFattura)
	throws it.cnr.jada.comp.ComponentException {

	java.util.HashMap dettagliCaricati = new java.util.HashMap();
	if (dettagliFattura != null) {
		Nota_di_debito_rigaHome home = (Nota_di_debito_rigaHome)getHomeCache(userContext).getHome(Nota_di_debito_rigaBulk.class, "default", "testata");
		for (Iterator i = dettagliFattura.iterator(); i.hasNext();) {
			Fattura_passiva_rigaIBulk rigaFattura = (Fattura_passiva_rigaIBulk)i.next();
			java.util.List righeNdD = home.findRigaFor(rigaFattura);
			if (righeNdD != null && !righeNdD.isEmpty()) {
				for (Iterator it = righeNdD.iterator(); it.hasNext();) {
					Nota_di_debito_rigaBulk rigaNdD = (Nota_di_debito_rigaBulk)it.next();
					if (rigaNdD.getFattura_passiva().getFattura_passiva_dettColl() == null ||
						rigaNdD.getFattura_passiva().getFattura_passiva_dettColl().isEmpty()) {
						try {
							rigaNdD.getFattura_passiva().setFattura_passiva_dettColl(new BulkList(findDettagli(userContext, rigaNdD.getFattura_passiva())));
						} catch (Throwable t) {
							throw handleException(t);
						}
					}
				}
				dettagliCaricati.put(rigaFattura, new Vector(righeNdD));
			}
		}
	}
	return dettagliCaricati;
}
private void caricaAutofattura(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	if (fattura_passiva != null) {
		if (fattura_passiva.getFl_autofattura() == null)
			fattura_passiva.setFl_autofattura(Boolean.FALSE);
		if (fattura_passiva.getFl_autofattura().booleanValue()) {
				AutofatturaHome autofatturaHome = (AutofatturaHome)getHome(userContext, AutofatturaBulk.class,null,"default");
			try {
				AutofatturaBulk autof = autofatturaHome.findFor(fattura_passiva);

				try {
					if (autof != null)
						lockBulk(userContext, autof);
				} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
					throw handleException(fattura_passiva, e);
				} catch (it.cnr.jada.bulk.BusyResourceException e) {
					throw handleException(fattura_passiva, e);
				}
				
				fattura_passiva.setAutofattura(autof);
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(fattura_passiva, e);
			}
		}
	}
}
private Accertamento_scadenzarioBulk caricaScadenzaAccertamentoPer(
	UserContext context,
	Accertamento_scadenzarioBulk scadenza)
	throws ComponentException {

	if (scadenza != null) {
		try {
			it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession)
																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																					"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
																					it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession.class);
			AccertamentoBulk accertamento = (AccertamentoBulk)h.inizializzaBulkPerModifica(context, scadenza.getAccertamento());
			BulkList scadenze = accertamento.getAccertamento_scadenzarioColl();
			scadenza = (Accertamento_scadenzarioBulk)scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
		} catch (java.rmi.RemoteException e) {
			throw handleException(scadenza, e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(scadenza, e);
		}
		return scadenza;
	}
	return null;
}
private Obbligazione_scadenzarioBulk caricaScadenzaObbligazionePer(
	UserContext context,
	Obbligazione_scadenzarioBulk scadenza)
	throws ComponentException {

	if (scadenza != null) {
		try {
			it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)
																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																					"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
																					it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class);
			ObbligazioneBulk obbligazione = (ObbligazioneBulk)h.inizializzaBulkPerModifica(context, scadenza.getObbligazione());
			BulkList scadenze = obbligazione.getObbligazione_scadenzarioColl();
			scadenza = (Obbligazione_scadenzarioBulk)scadenze.get(scadenze.indexOfByPrimaryKey(scadenza));
		} catch (java.rmi.RemoteException e) {
			throw handleException(scadenza, e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(scadenza, e);
		}
		return scadenza;
	}
	return null;
}
private java.util.List caricaStorniExceptFor(
	UserContext userContext,
	Obbligazione_scadenzarioBulk scadenza, 
	Nota_di_creditoBulk notaDiCredito)
	throws it.cnr.jada.comp.ComponentException {

	Nota_di_credito_rigaHome home = (Nota_di_credito_rigaHome)getHomeCache(userContext).getHome(Nota_di_credito_rigaBulk.class, "default", "testata");
	try {
		return home.findStorniForObbligazioneExceptFor(scadenza, notaDiCredito);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(notaDiCredito, e);
	}
}
private java.util.HashMap caricaStorniPer(
	UserContext userContext,
	java.util.List dettagliFattura)
	throws it.cnr.jada.comp.ComponentException {

	java.util.HashMap dettagliCaricati = new java.util.HashMap();
	if (dettagliFattura != null) {
		Nota_di_credito_rigaHome home = (Nota_di_credito_rigaHome)getHomeCache(userContext).getHome(Nota_di_credito_rigaBulk.class, "default", "testata");
		for (Iterator i = dettagliFattura.iterator(); i.hasNext();) {
			Fattura_passiva_rigaIBulk rigaFattura = (Fattura_passiva_rigaIBulk)i.next();
			java.util.List righeNdC = home.findRigaFor(rigaFattura);
			if (righeNdC != null && !righeNdC.isEmpty()) {
				for (Iterator it = righeNdC.iterator(); it.hasNext();) {
					Nota_di_credito_rigaBulk rigaNdC = (Nota_di_credito_rigaBulk)it.next();
					if (rigaNdC.getFattura_passiva().getFattura_passiva_dettColl() == null ||
						rigaNdC.getFattura_passiva().getFattura_passiva_dettColl().isEmpty()) {
						try {
							rigaNdC.getFattura_passiva().setFattura_passiva_dettColl(new BulkList(findDettagli(userContext, rigaNdC.getFattura_passiva())));
						} catch (Throwable t) {
							throw handleException(t);
						}
					}
				}
				dettagliCaricati.put(rigaFattura, new Vector(righeNdC));
			}
		}
	}
	return dettagliCaricati;
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rivelata.
  *    PostCondition:
  *      Permesso il ritorno dell cambio nella fattura.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */
//^^@@

public Fattura_passivaBulk cercaCambio(it.cnr.jada.UserContext uc, Fattura_passivaBulk fattura) 
 	throws ComponentException{
	
	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = fattura.getValuta();
	if (valuta == null)
		return resetChangeData(uc, fattura);
		
	java.sql.Timestamp dataCambio = fattura.getDt_fattura_fornitore();
	if (valuta.getCd_divisa().equals(getEuro(uc).getCd_divisa())) {
		fattura.setDefaultValuta(true);
		if (dataCambio == null)
			try {
				dataCambio = getHome(uc, fattura).getServerTimestamp();
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(fattura, e);
			}
	}
	else fattura.setDefaultValuta(false);

	return basicCercaCambio(uc, fattura, dataCambio);
}
//^^@@
/** 
  *  Tutti i controlli  superati.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura passiva selezionata per l'inserimento di dettagli nelle note di debito.
 */
//^^@@

public RemoteIterator cercaDettagliFatturaPerNdC(UserContext context, Fattura_passiva_IBulk fatturaPassiva)
	throws ComponentException {

	String statoR = getStatoRiporto(context, fatturaPassiva);
	String statoRipInScrivania = getStatoRiportoInScrivania(context, fatturaPassiva);
	
	if (it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).equals(fatturaPassiva.getEsercizio())) {
		if (!fatturaPassiva.NON_RIPORTATO.equals(statoR))
			throw new it.cnr.jada.comp.ApplicationException("La fattura selezionata � stata riportata in altro esercizio! Operazione annullata.");
	}
	
	// RP 16/03/2010 Da commentare per generare NC di anni precedenti
//	else {
//		if (!fatturaPassiva.COMPLETAMENTE_RIPORTATO.equals(statoRipInScrivania))
//			throw new it.cnr.jada.comp.ApplicationException("La fattura selezionata o � stata riportata parzialmente o non � stata riportata nell'esercizio corrente! Operazione annullata.");
//	}
		
	Fattura_passiva_rigaIHome home = (Fattura_passiva_rigaIHome)getHome(context, Fattura_passiva_rigaIBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "pg_fattura_passiva", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
	sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaPassiva.getCd_cds_origine());
	sql.addClause("AND", "esercizio", sql.EQUALS, fatturaPassiva.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fatturaPassiva.getCd_uo_origine());
	sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_passiva_IBulk.STATO_ANNULLATO);
	//Escludo le riportate
	//sql.addSQLClause("AND", "ESERCIZIO_OBBLIGAZIONE", sql.EQUALS, fatturaPassiva.getEsercizio());
	
	try {
		return iterator(
			context,
			sql,
			Fattura_passiva_rigaIBulk.class,
			"default");
	} catch (Throwable e) {
		throw handleException(e);
	}
} 
//^^@@
/** 
  *  Tutti i controlli  superati.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista dei dettagli della fattura passiva selezionata per l'inserimento di dettagli
  *		 nelle note di debito.
 */
//^^@@

public RemoteIterator cercaDettagliFatturaPerNdD(UserContext context, Fattura_passiva_IBulk fatturaPassiva)
	throws ComponentException {

	return cercaDettagliFatturaPerNdC(context, fatturaPassiva);
} 
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture passive congruenti con la nota di credito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture passive per le note di credito
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle fatture passive.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di credito = fornitore fattura passiva
  *		PreCondition:
  *			Il fornitore della fattura passiva non � lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura passiva non � lo stesso di quello della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unit�� organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura passiva non � la stessa di quella della nota di credito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */
//^^@@

public RemoteIterator cercaFatturaPerNdC(UserContext context, Nota_di_creditoBulk notaDiCredito)
	throws ComponentException {

	Fattura_passiva_IHome home = (Fattura_passiva_IHome)getHome(context, Fattura_passiva_IBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND","cd_terzo", sql.EQUALS, notaDiCredito.getFornitore().getCd_terzo());
	sql.addClause("AND", "cd_cds", sql.EQUALS, notaDiCredito.getCd_cds_origine());
	sql.addClause("AND", "esercizio", sql.LESS_EQUALS, notaDiCredito.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, notaDiCredito.getCd_uo_origine());
	sql.addClause("AND", "fl_fattura_compenso", sql.EQUALS, Boolean.FALSE);
	sql.addClause("AND", "fl_congelata", sql.EQUALS, Boolean.FALSE);
	sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_passiva_IBulk.STATO_ANNULLATO);
	// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
	//sql.addClause("AND", "pg_lettera", sql.ISNULL, null);
	sql.addClause("AND", "ti_istituz_commerc", sql.EQUALS, notaDiCredito.getTi_istituz_commerc());
	sql.addOrderBy("ESERCIZIO DESC");
	
	try {
		return iterator(
			context,
			sql,
			Fattura_passiva_IBulk.class,
			"default");
	} catch (Throwable e) {
		throw handleException(e);
	}
} 
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle fatture passive congruenti con la nota di debito che si sta creando/modificando.
  *   	PostCondition:
  *  		La fattura viene aggiunta alla lista delle fatture congruenti.
  *	Validazione lista delle fatture passive per le note di debito
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle fatture passive.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Fornitore nota di debito = fornitore fattura passiva
  *		PreCondition:
  *			Il fornitore della fattura passiva non � lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	CDS di appartenenza
  *		PreCondition:
  *			La fattura non appartiene al CDS di creazione della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Esercizio di appartenenza
  *		PreCondition:
  *			L'esercizio della fattura passiva non � lo stesso di quello della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
  *	Unit� organizzativa di appartenenza
  *		PreCondition:
  *			La UO della fattura passiva non � la stessa di quella della nota di debito
  * 	PostCondition:
  *  		La fattura non viene aggiunta alla lista delle fatture congruenti.
 */
//^^@@

public RemoteIterator cercaFatturaPerNdD(UserContext context, Nota_di_debitoBulk notaDiDebito)
	throws ComponentException {

	Fattura_passiva_IHome home = (Fattura_passiva_IHome)getHome(context, Fattura_passiva_IBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND","cd_terzo", sql.EQUALS, notaDiDebito.getFornitore().getCd_terzo());
	sql.addClause("AND", "cd_cds", sql.EQUALS, notaDiDebito.getCd_cds_origine());
	sql.addClause("AND", "esercizio", sql.LESS_EQUALS, notaDiDebito.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, notaDiDebito.getCd_uo_origine());
	sql.addClause("AND", "fl_fattura_compenso", sql.EQUALS, Boolean.FALSE);
	sql.addClause("AND", "fl_congelata", sql.EQUALS, Boolean.FALSE);
	sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_passiva_IBulk.STATO_ANNULLATO);
	// RP 23/03/2010  commentato per permettere la generazione delle nc/nd di fatture con lettera di pagamento
	//sql.addClause("AND", "pg_lettera", sql.ISNULL, null);
	sql.addOrderBy("ESERCIZIO DESC");
	
	try {
		return iterator(
			context,
			sql,
			Fattura_passiva_IBulk.class,
			"default");
	} catch (Throwable e) {
		throw handleException(e);
	}
} 
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Ricerca la lista delle scadenze di obbligazioni congruenti con la fattura passiva che si sta creando/modificando.
  *   	PostCondition:
  *  		Le scadenze vengono aggiunte alla lista delle scadenze congruenti.
  *	Validazione lista delle obbligazioni per le fatture passive
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle scadenze delle obbligazioni.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
  *	Obbligazione definitiva
  *		PreCondition:
  *			La scadenza non appartiene ad un'obbligazione definitiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni non cancellate
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione cancellata
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni associate ad altri documenti amministrativi
  *		PreCondition:
  *			La scadenza appartiene ad un'obbligazione associata ad altri documenti amministrativi
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Obbligazioni della stessa UO
  *		PreCondition:
  *			La scadenza dell'obbligazione non appartiene alla stessa UO di generazione fattura passiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitatazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Disabilitazione filtro di selezione sul debitore dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un debitore diverso da quello della fattura passiva e non � di tipo "diversi"
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro di selezione sulla data di scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha una data scadenza precedente alla data di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro importo scadenza
  *		PreCondition:
  *			La scadenza dell'obbligazione ha un importo di scadenza inferiore a quella di filtro
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Abilitazione filtro sul progressivo dell'obbligazione
  *		PreCondition:
  *			La scadenza dell'obbligazione non ha progressivo specificato
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
  *	Associazione di una scadenza a titolo capitolo dei beni servizio inventariabili da contabilizzare
  *		PreCondition:
  *			L'obbligazione non ha titolo capitolo dei beni servizio inventariabili da contabilizzare
  * 	PostCondition:
  *  		La scadenza non viene aggiunta alla lista delle scadenze congruenti.
 */
//^^@@

public RemoteIterator cercaObbligazioni(UserContext context, Filtro_ricerca_obbligazioniVBulk filtro)
	throws ComponentException {

	Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome)getHome(context, Obbligazione_scadenzarioBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.setDistinctClause(true);
	sql.addTableToHeader("OBBLIGAZIONE");
	sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.CD_CDS","OBBLIGAZIONE.CD_CDS");
	sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO","OBBLIGAZIONE.ESERCIZIO");
	sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.ESERCIZIO_ORIGINALE","OBBLIGAZIONE.ESERCIZIO_ORIGINALE");
	sql.addSQLJoin("OBBLIGAZIONE_SCADENZARIO.PG_OBBLIGAZIONE","OBBLIGAZIONE.PG_OBBLIGAZIONE");

	sql.addTableToHeader("ELEMENTO_VOCE");
	sql.addSQLJoin("OBBLIGAZIONE.CD_ELEMENTO_VOCE","ELEMENTO_VOCE.CD_ELEMENTO_VOCE");
	sql.addSQLJoin("OBBLIGAZIONE.TI_APPARTENENZA","ELEMENTO_VOCE.TI_APPARTENENZA");
	sql.addSQLJoin("OBBLIGAZIONE.TI_GESTIONE","ELEMENTO_VOCE.TI_GESTIONE");
	sql.addSQLJoin("OBBLIGAZIONE.ESERCIZIO","ELEMENTO_VOCE.ESERCIZIO");

	sql.addSQLClause("AND", "OBBLIGAZIONE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context));
	sql.addSQLClause("AND","OBBLIGAZIONE.STATO_OBBLIGAZIONE", sql.EQUALS, "D");
	sql.addSQLClause("AND","OBBLIGAZIONE.RIPORTATO", sql.EQUALS, "N");
	sql.addSQLClause("AND","OBBLIGAZIONE.DT_CANCELLAZIONE", sql.ISNULL, null);
	sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA", sql.NOT_EQUALS, new java.math.BigDecimal(0));
	sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_AMM IS NULL");
	sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP),java.sql.Types.DECIMAL,2);
	sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE = ? OR OBBLIGAZIONE_SCADENZARIO.IM_ASSOCIATO_DOC_CONTABILE IS NULL");
	sql.addParameter(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP),java.sql.Types.DECIMAL,2);
	sql.addSQLClause("AND","OBBLIGAZIONE.CD_UNITA_ORGANIZZATIVA",sql.EQUALS, filtro.getCd_unita_organizzativa());

	if (filtro.getElemento_voce() != null) {
		sql.addSQLClause("AND","OBBLIGAZIONE.CD_ELEMENTO_VOCE",sql.STARTSWITH, filtro.getElemento_voce().getCd_elemento_voce());
		sql.addSQLClause("AND","OBBLIGAZIONE.TI_APPARTENENZA",sql.EQUALS, filtro.getElemento_voce().getTi_appartenenza());
		sql.addSQLClause("AND","OBBLIGAZIONE.TI_GESTIONE",sql.EQUALS, filtro.getElemento_voce().getTi_gestione());
		sql.addSQLClause("AND","OBBLIGAZIONE.ESERCIZIO",sql.EQUALS, filtro.getElemento_voce().getEsercizio());
	}
	
	if (filtro.hasDocumentoCompetenzaCOGEInAnnoPrecedente() ||
		!filtro.hasDocumentoCompetenzaCOGESoloInAnnoCorrente())
		sql.addSQLClause("AND","OBBLIGAZIONE.FL_PGIRO",sql.EQUALS, "N");

	if (!filtro.getFl_fornitore().booleanValue()) {
		sql.addTableToHeader("TERZO");
		sql.addTableToHeader("ANAGRAFICO");
		sql.addSQLJoin("OBBLIGAZIONE.CD_TERZO", "TERZO.CD_TERZO");
		sql.addSQLJoin("TERZO.CD_ANAG", "ANAGRAFICO.CD_ANAG");
		sql.addSQLClause("AND","(OBBLIGAZIONE.CD_TERZO = ? OR ANAGRAFICO.TI_ENTITA = ?)");
		sql.addParameter(filtro.getFornitore().getCd_terzo(),java.sql.Types.INTEGER,0);
		sql.addParameter(AnagraficoBulk.DIVERSI,java.sql.Types.VARCHAR,0);
	} else {
		sql.addSQLClause("AND","OBBLIGAZIONE.CD_TERZO",sql.EQUALS, filtro.getFornitore().getCd_terzo());
	}
	
	if (filtro.getFl_data_scadenziario().booleanValue() && filtro.getData_scadenziario() != null)
		sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.DT_SCADENZA",sql.EQUALS, filtro.getData_scadenziario());
	if (filtro.getFl_importo().booleanValue() && filtro.getIm_importo() != null)
		sql.addSQLClause("AND","OBBLIGAZIONE_SCADENZARIO.IM_SCADENZA",sql.GREATER_EQUALS, filtro.getIm_importo());

	//filtro su Tipo obbligazione
	if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getTipo_obbligazione() != null) {
		if (ObbligazioneBulk.TIPO_COMPETENZA.equals(filtro.getTipo_obbligazione()))
			sql.addSQLClause("AND","OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",sql.EQUALS,Numerazione_doc_contBulk.TIPO_OBB);
		else if (ObbligazioneBulk.TIPO_RESIDUO_PROPRIO.equals(filtro.getTipo_obbligazione()))
			sql.addSQLClause("AND","OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",sql.EQUALS,Numerazione_doc_contBulk.TIPO_OBB_RES);
		else if (ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO.equals(filtro.getTipo_obbligazione()))
			sql.addSQLClause("AND","OBBLIGAZIONE.CD_TIPO_DOCUMENTO_CONT",sql.EQUALS,Numerazione_doc_contBulk.TIPO_OBB_RES_IMPROPRIA);
	}

	//filtro su Anno Residuo obbligazione
	if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getEsercizio_ori_obbligazione() != null)
		sql.addSQLClause("AND","OBBLIGAZIONE.ESERCIZIO_ORIGINALE",sql.EQUALS, filtro.getEsercizio_ori_obbligazione());

	//filtro su Numero obbligazione
	if (filtro.getFl_nr_obbligazione().booleanValue() && filtro.getNr_obbligazione() != null)
		sql.addSQLClause("AND","OBBLIGAZIONE.PG_OBBLIGAZIONE",sql.EQUALS, filtro.getNr_obbligazione());

	return iterator(
		context,
		sql,
		Obbligazione_scadenzarioBulk.class,
		"default");
} 
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Sono state inserite nella nota di credito degli accertamenti su cui stornare i dettagli
  *   	PostCondition:
  *  		Ricerca la lista delle tipologie e modalit� di pagamento per l'ente.
  *	Validazione dell'ente
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle tipologie e modalit� di pagamento per l'ente.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */
//^^@@

public Nota_di_creditoBulk completaEnte(
	it.cnr.jada.UserContext userContext,
	Nota_di_creditoBulk notaDiCredito) 
	throws it.cnr.jada.comp.ComponentException {

	it.cnr.contab.anagraf00.core.bulk.TerzoBulk ente = findTerzoUO(userContext, notaDiCredito.getEsercizio());
	notaDiCredito.setEnte(ente);
	it.cnr.contab.anagraf00.core.bulk.TerzoHome home = (it.cnr.contab.anagraf00.core.bulk.TerzoHome)getHome(userContext, ente);
	try {
		notaDiCredito.setTermini_uo(findTermini_uo(userContext, notaDiCredito));
		notaDiCredito.setModalita_uo(findModalita_uo(userContext, notaDiCredito));
		notaDiCredito.setTermini_pagamento_uo(null);
		notaDiCredito.setModalita_pagamento_uo(null);
		notaDiCredito.setBanca_uo(null);
		notaDiCredito.setBanche_uo(null);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(e);
	}

	return notaDiCredito;
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Vengono richiesti i dati relativi al fornitore della fattura passiva
  *    PostCondition:
  *      vengono trasmessi i dati relativi al fornitore.
 */
//^^@@

public Fattura_passivaBulk completaFornitore(it.cnr.jada.UserContext uc, Fattura_passivaBulk fattura_passiva, it.cnr.contab.anagraf00.core.bulk.TerzoBulk fornitoreTrovato) 
	throws ComponentException {
	try {		
		if (fattura_passiva != null) {
			if (fattura_passiva.isEstera()) {
				if(fattura_passiva.getFl_extra_ue() != null && fattura_passiva.getFl_extra_ue().booleanValue() &&
					!it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.EXTRA_CEE.equalsIgnoreCase(fornitoreTrovato.getAnagrafico().getTi_italiano_estero()))
					throw new it.cnr.jada.comp.ApplicationException("La fattura � estera. La nazionalit� del fornitore deve appartenere ad uno Stato extra UE.");
				if(fattura_passiva.getFl_intra_ue() != null && fattura_passiva.getFl_intra_ue().booleanValue() &&
					!it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk.CEE.equalsIgnoreCase(fornitoreTrovato.getAnagrafico().getTi_italiano_estero()))
					throw new it.cnr.jada.comp.ApplicationException("La fattura � estera. La nazionalit� del fornitore deve appartenere ad uno Stato intra UE.");
				AnagraficoComponentSession sess = (AnagraficoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRANAGRAF00_EJB_AnagraficoComponentSession", AnagraficoComponentSession.class);
				if(fattura_passiva.getFl_intra_ue() != null && fattura_passiva.getFl_intra_ue().booleanValue()&&
					!sess.verificaStrutturaPiva(uc,fornitoreTrovato.getAnagrafico()))
					throw new it.cnr.jada.comp.ApplicationException("Verificare la partita Iva del fornitore non corrisponde al modello della sua nazionalit�.");
			}
			fattura_passiva.setFornitore(fornitoreTrovato);
			fattura_passiva.setNome(fornitoreTrovato.getAnagrafico().getNome());
			fattura_passiva.setCognome(fornitoreTrovato.getAnagrafico().getCognome());
			fattura_passiva.setRagione_sociale(fornitoreTrovato.getAnagrafico().getRagione_sociale());
			fattura_passiva.setCodice_fiscale(fornitoreTrovato.getAnagrafico().getCodice_fiscale());
			fattura_passiva.setPartita_iva(fornitoreTrovato.getAnagrafico().getPartita_iva());

			it.cnr.contab.anagraf00.core.bulk.TerzoHome home = (it.cnr.contab.anagraf00.core.bulk.TerzoHome)getHome(uc, fornitoreTrovato);
			try {
				fattura_passiva.setTermini(findTermini(uc, fattura_passiva));
				fattura_passiva.setModalita(findModalita(uc, fattura_passiva));
				fattura_passiva.setTermini_pagamento(null);
				fattura_passiva.setModalita_pagamento(null);
				fattura_passiva.setBanca(null);
				fattura_passiva.setBanche(null);
				fattura_passiva.setCessionario(null);
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(e);
			} catch (it.cnr.jada.persistency.IntrospectionException e) {
				throw handleException(e);
			}

			//Aggiorno le righe
			if (fattura_passiva.getFattura_passiva_dettColl() != null) {
				Iterator dettagli = fattura_passiva.getFattura_passiva_dettColl().iterator();
				while (dettagli.hasNext()) {
					Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)dettagli.next();
					if (!riga.getFornitore().equalsByPrimaryKey(fattura_passiva.getFornitore())) {
						riga.setFornitore(fattura_passiva.getFornitore());
						riga.setTermini(fattura_passiva.getTermini());
						riga.setModalita(fattura_passiva.getModalita());
						riga.setTermini_pagamento(null);
						riga.setModalita_pagamento(null);
						riga.setBanca(null);
						riga.setBanche(null);
						riga.setCessionario(null);
						riga.setToBeUpdated();
					}
				}
			}
		}
	}catch (ValidationException e) {
          throw new ApplicationException(e.getMessage());
	} 
	catch (Throwable t) {
		throw handleException(fattura_passiva, t);
	}
	return fattura_passiva;
}

//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */
//^^@@

private Fattura_passivaBulk completeWithCondizioneConsegna(UserContext userContext, Fattura_passivaBulk fatturaPassiva) throws ComponentException {
		Condizione_consegnaHome home = (Condizione_consegnaHome)getHome(userContext, Condizione_consegnaBulk.class);
		try {
			fatturaPassiva.setCondizione_consegnaColl(home.fetchAll(home.selectByClause(userContext, null)));
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(fatturaPassiva, e);
		}
	return fatturaPassiva;
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */
//^^@@

private Fattura_passivaBulk completeWithModalitaTrasporto(UserContext userContext, Fattura_passivaBulk fatturaPassiva) throws ComponentException {
		Modalita_trasportoHome home = (Modalita_trasportoHome)getHome(userContext, Modalita_trasportoBulk.class);
		try {
			fatturaPassiva.setModalita_trasportoColl(home.fetchAll(home.selectByClause(userContext, null)));
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(fatturaPassiva, e);
		}
	return fatturaPassiva;
}
//^^@@
/** 
  *  Validazione riga.
  *    PreCondition:
  *      E' stata richiesta la contabilizzazione dei dettagli di fattura passiva selezionati ma almeno un dettaglio
  *      non supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *    PreCondition:
  *	E' stata richiesta la contabilizzazione dei dettagli di fattura passiva selezionati. Ogni dettaglio
  *	supera i controlli del metodo 'validaRiga'.
  *    PostCondition:
  *      Consente il passaggio alla riga seguente.
 */
//^^@@
public Fattura_passivaBulk contabilizzaDettagliSelezionati(
	UserContext context,
	Fattura_passivaBulk fatturaPassiva,
	java.util.Collection dettagliSelezionati,
	it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk obbligazioneSelezionata)
	throws ComponentException {

	if (obbligazioneSelezionata != null && dettagliSelezionati != null) {
		if (!dettagliSelezionati.isEmpty()) {
			for (java.util.Iterator i = dettagliSelezionati.iterator(); i.hasNext();) {
				Fattura_passiva_rigaBulk rigaSelected = (Fattura_passiva_rigaBulk)i.next();

                validaScadenze(fatturaPassiva, obbligazioneSelezionata);
//                rigaSelected.setCollegatoCapitoloPerTrovato(obbligazioneSelezionata.getObbligazione().getElemento_voce().isVocePerTrovati());
                
                rigaSelected.setObbligazione_scadenziario(obbligazioneSelezionata);
    			impostaCollegamentoCapitoloPerTrovato(context, rigaSelected);
				rigaSelected.setStato_cofi(rigaSelected.STATO_CONTABILIZZATO);
				rigaSelected.setToBeUpdated();
				fatturaPassiva.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, rigaSelected);
			}
			fatturaPassiva.addToFattura_passiva_ass_totaliMap(obbligazioneSelezionata, calcolaTotalePer(
										(Vector)fatturaPassiva.getFattura_passiva_obbligazioniHash().get(obbligazioneSelezionata),
										false));
			fatturaPassiva.setAndVerifyStatus();
		} else {
			fatturaPassiva.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, null);
		}
		try {
			ObbligazioneAbstractComponentSession session = (ObbligazioneAbstractComponentSession)EJBCommonServices.createEJB(
															"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
															ObbligazioneAbstractComponentSession.class);
			session.lockScadenza(context, obbligazioneSelezionata);
		} catch (Throwable t) {
			throw handleException(fatturaPassiva, t);
		}
	}
	return fatturaPassiva;
}
private void validaScadenze(Fattura_passivaBulk doc, Obbligazione_scadenzarioBulk newScad) throws ComponentException{
	Iterator it;
	
	Vector scadCanc = doc.getDocumentiContabiliCancellati();
	if (scadCanc != null) {
		it = scadCanc.iterator();
	 
		while(it.hasNext()) {
			Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) it.next();
			if(scad.getObbligazione() instanceof ObbligazioneResBulk){
				if (scad.getObbligazione().equalsByPrimaryKey(newScad.getObbligazione()) && ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica()!=null
					&& ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica().getPg_modifica()!=null) {
					throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'impegno residuo "+scad.getPg_obbligazione()+" poich� � stata effettuata una modifica in questo documento amministrativo!");									
				}
			}
		}
	}
		
    ObbligazioniTable obbligazioniHash= doc.getObbligazioniHash();
    if (obbligazioniHash != null && !obbligazioniHash.isEmpty()) {

    	for (java.util.Enumeration e= obbligazioniHash.keys(); e.hasMoreElements();) {
    		Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk) e.nextElement();
    		if(scad.getObbligazione() instanceof ObbligazioneResBulk){
    			if (scad.getObbligazione().equalsByPrimaryKey(newScad.getObbligazione()) && ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica()!=null
					&& ((ObbligazioneResBulk)scad.getObbligazione()).getObbligazione_modifica().getPg_modifica()!=null) {
    				throw new it.cnr.jada.comp.ApplicationException("Impossibile collegare una scadenza dell'impegno residuo "+scad.getPg_obbligazione()+" poich� � stata effettuata una modifica in questo documento amministrativo!");									
    			}
    		}
    	}
    }
}
//^^@@
/** 
  *	Quadratura delle scadenze obbligazioni di fattura passiva non estera o estera senza lettera di pagamento.
  *		PreCondition:
  * 		La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleObbligazionePer') insistenti sull'elenco di dettagli associati
  *			alla scadenza obbligazione � uguale all'importo della scadenza obbligazione stessa
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Quadratura delle scadenze obbligazioni di fattura passiva estera con lettera di pagamento.
  *		PreCondition:
  * 		L'importo della lettera di pagamento � uguale all'importo della scadenza obbligazione
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Controlli non superati.
  *		PreCondition:
  * 		Non vengono superate tutte le validazioni
  *   	PostCondition:
  *  		Emette errore con messaggio:"Quadratura non superata".
 */
//^^@@



public void controllaCompetenzaCOGEDettagli(
	UserContext aUC,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva != null && !fatturaPassiva.isAnnullato()) {
		ObbligazioniTable obbligazioniHash = fatturaPassiva.getFattura_passiva_obbligazioniHash();
		if (obbligazioniHash != null) {
			for (java.util.Enumeration e = obbligazioniHash.keys(); e.hasMoreElements();) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
				Iterator righeCollegate = ((List)obbligazioniHash.get(scadenza)).iterator();
				if (righeCollegate != null && righeCollegate.hasNext()) {
					Fattura_passiva_rigaBulk primaRiga = (Fattura_passiva_rigaBulk)righeCollegate.next();
					java.util.Calendar dtCompetenzaDa = fatturaPassiva.getDateCalendar(primaRiga.getDt_da_competenza_coge());
					java.util.Calendar dtCompetenzaA = fatturaPassiva.getDateCalendar(primaRiga.getDt_a_competenza_coge());
					// il controllo se reputato necessario potrebbe essere cambiato con le righe commentate
					//int annoDa =dtCompetenzaDa.get(java.util.Calendar.YEAR);
					//int annoA =dtCompetenzaA.get(java.util.Calendar.YEAR);
					while (righeCollegate.hasNext()) {
						Fattura_passiva_rigaBulk rigaSuccessiva = (Fattura_passiva_rigaBulk)righeCollegate.next();
						java.util.Calendar dtCompetenzaDaSuccessiva = fatturaPassiva.getDateCalendar(rigaSuccessiva.getDt_da_competenza_coge());
						java.util.Calendar dtCompetenzaASuccessiva = fatturaPassiva.getDateCalendar(rigaSuccessiva.getDt_a_competenza_coge());
//						if(annoDa!=(dtCompetenzaDaSuccessiva.get(java.util.Calendar.YEAR)) ||
//							annoA!=(dtCompetenzaASuccessiva.get(java.util.Calendar.YEAR)))
//								throw new ApplicationException("I dettagli del documento collegati alla scadenza \"" + scadenza.getDs_scadenza() + "\"\nnon hanno lo stesso anno di competenza! Impossibile salvare.");					
						if (!dtCompetenzaDa.equals(dtCompetenzaDaSuccessiva) ||
							!dtCompetenzaA.equals(dtCompetenzaASuccessiva))
							throw new ApplicationException("I dettagli del documento collegati alla scadenza \"" + scadenza.getDs_scadenza() + "\"\nnon hanno lo stesso periodo di competenza! Impossibile salvare.");
					}
				}
			}
		}
	}
}

private void controllaContabilizzazioneDiTutteLeRighe(
	UserContext userContext, 
	Fattura_passivaBulk fattura_passiva)
	throws ComponentException {
		
	for (java.util.Iterator i = fattura_passiva.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
		Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();
		if (Fattura_passiva_rigaBulk.STATO_INIZIALE.equals(riga.getStato_cofi()))
			throw new it.cnr.jada.comp.ApplicationException("Il dettaglio \"" + riga.getDs_riga_fattura() + "\" NON � stato contabilizzato!");
	}

	if (fattura_passiva instanceof Fattura_passiva_IBulk && ((Fattura_passiva_IBulk)fattura_passiva).isDoc1210Associato()) {
		ObbligazioniTable obbs = fattura_passiva.getObbligazioniHash();
		if (obbs != null)
			for (java.util.Enumeration e = obbs.keys(); e.hasMoreElements();) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
				Vector dettagli = (Vector)obbs.get(scadenza);
				if (dettagli == null || dettagli.isEmpty())
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: la scadenza \"" + scadenza.getDs_scadenza() + "\" non ha associato alcun dettaglio! Operazione annullata.");
			}
	}
}
//^^@@
/** 
  *  Validazione riga.
  *    PreCondition:
  *      validaRiga non superata.
  *    PostCondition:
  *      Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *    PreCondition:
  *      validaRiga superato
  *    PostCondition:
  *      Consente il passaggio alla riga seguente.
 */
//^^@@

public void controllaQuadraturaAccertamenti(UserContext aUC,Nota_di_creditoBulk notaDiCredito)
	throws ComponentException {

	if (notaDiCredito != null  && !notaDiCredito.isAnnullato() && !notaDiCredito.isCongelata()) {
		AccertamentiTable accertamentiHash = notaDiCredito.getAccertamentiHash();
		if (accertamentiHash != null) {
			for (java.util.Enumeration e = accertamentiHash.keys(); e.hasMoreElements();) {
				Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)e.nextElement();
				java.math.BigDecimal totale = calcolaTotaleScadenzaAccertamentoPer(aUC, scadenza, notaDiCredito).abs();
				java.math.BigDecimal delta = scadenza.getIm_scadenza().subtract(totale);
				if (delta.compareTo(new java.math.BigDecimal(0)) > 0) {
					StringBuffer sb = new StringBuffer();
					sb.append("Attenzione: La scadenza ");
					sb.append(scadenza.getDs_scadenza());
					sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
					sb.append(" � stata coperta solo per ");
					sb.append(totale.doubleValue() + " EUR!");
					throw new it.cnr.jada.comp.ApplicationException(sb.toString());
				} else if (delta.compareTo(new java.math.BigDecimal(0)) < 0) {
					StringBuffer sb = new StringBuffer();
					sb.append("Attenzione: La scadenza ");
					sb.append(scadenza.getDs_scadenza());
					sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
					sb.append(" � scoperta per ");
					sb.append(delta.abs().doubleValue() + " EUR!");
					throw new it.cnr.jada.comp.ApplicationException(sb.toString());
				}
				controllaOmogeneitaTraTerzi(aUC, scadenza, (Vector)accertamentiHash.get(scadenza));
			}
		}
	}
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Non � stato rilevato nessun errore.
  *    PostCondition:
  *      Nessun messaggio.
  *  Valida quadratura IVA
  *    PreCondition:
  *      l totale imponibile +IVA  di esteso a tutte le righe non quadra con il totale imponibile + IVA della fattura.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione, la somma degli imponibili delle righe + la somma della relativa iva non quadra con il totale fattura".
  *      
 */
//^^@@

public void controllaQuadraturaConti(UserContext aUC,Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	try {
		if (!fatturaPassiva.isAnnullato() && !fatturaPassiva.isCongelata()) {
			aggiornaImportiTotali(aUC, fatturaPassiva);

			if (fatturaPassiva.getIm_importo_totale_fattura_fornitore_euro() == null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: il totale dei dettagli di " + fatturaPassiva.getIm_totale_fattura_calcolato() + " (Imponibile + IVA) non corrisponde al totale di " + fatturaPassiva.getIm_totale_fattura() + " della testata fattura!");
				
//			if (new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP).compareTo(fatturaPassiva.getIm_importo_totale_fattura_fornitore_euro()) == 0)
//				throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'importo di testata non pu� essere 0!");

			if (fatturaPassiva.getIm_importo_totale_fattura_fornitore_euro().compareTo(fatturaPassiva.getIm_totale_fattura_calcolato()) != 0) {
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: il totale dei dettagli di " + fatturaPassiva.getIm_totale_fattura_calcolato() + " (Imponibile + IVA) non corrisponde al totale di " + fatturaPassiva.getIm_importo_totale_fattura_fornitore_euro() + " EUR della testata fattura!");
			}
		}
	} catch (ComponentException t) {
		throw handleException(fatturaPassiva, t);
	}
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Non � stato rilevato nessun errore.
  *    PostCondition:
  *      Nessun messaggio.
  *  Valida quadratura IVA
  *    PreCondition:
  *      l totale imponibile +IVA  di esteso a tutte le righe non quadra con il totale imponibile + IVA della fattura.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione, la somma degli imponibili delle righe + la somma della relativa iva non quadra con il totale fattura".
  *      
 */
//^^@@

public void controllaQuadraturaIntrastat(UserContext aUC,Fattura_passivaBulk fattura)
	throws ComponentException {

	try {
		if (fattura.getFl_intra_ue() != null && fattura.getFl_intra_ue().booleanValue() && !fattura.getFl_merce_extra_ue().booleanValue() &&
				fattura.getFornitore()!=null &&fattura.getFornitore().getAnagrafico()!=null && fattura.getFornitore().getAnagrafico().getPartita_iva()!=null){
		     	Boolean trovato=false;
		     	Boolean obbligatorio=false;
		     	Parametri_cnrBulk par = ((Parametri_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).getParametriCnr(aUC,fattura.getEsercizio());
				if (par!=null && par.getFl_obb_intrastat()!=null && par.getFl_obb_intrastat().booleanValue()){	
				for (Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
					Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
					if(dettaglio.getBene_servizio()!=null && dettaglio.getBene_servizio().getFl_obb_intrastat_acq().booleanValue() && (dettaglio.getIm_diponibile_nc()!=null && dettaglio.getIm_diponibile_nc().compareTo(BigDecimal.ZERO)!=0))
						obbligatorio=true;
					
				}
				if(obbligatorio && fattura.getFattura_passiva_intrastatColl()!=null && fattura.getFattura_passiva_intrastatColl().isEmpty())
					trovato=true;
				else if (!obbligatorio && fattura.getFattura_passiva_intrastatColl()!=null && !fattura.getFattura_passiva_intrastatColl().isEmpty())
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non indicare i dati Intrastat");
				
				if(trovato && (fattura instanceof Fattura_passiva_IBulk ))	
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: indicare i dati Intrastat");
				}
			}
		/* RP 11/02/2010 controllo quadratura importi attualmente sospeso
		if (fatturaPassiva.getFl_intra_ue() != null && 
			fatturaPassiva.getFl_intra_ue().booleanValue() &&
			fatturaPassiva.getFattura_passiva_intrastatColl() != null &&
			!fatturaPassiva.getFattura_passiva_intrastatColl().isEmpty() &&
			!fatturaPassiva.isAnnullato() &&
			!fatturaPassiva.isCongelata()) {
			aggiornaImportiTotali(aUC, fatturaPassiva);

			java.math.BigDecimal impIntrastat = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
			BulkList intrastatColl = fatturaPassiva.getFattura_passiva_intrastatColl();
			for (java.util.Iterator i = intrastatColl.iterator(); i.hasNext();) {
				Fattura_passiva_intraBulk rigaSelected = (Fattura_passiva_intraBulk)i.next();
				impIntrastat = impIntrastat.add(rigaSelected.getAmmontare_euro());
			}
			impIntrastat = impIntrastat.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

			if (fatturaPassiva.getIm_totale_imponibile() == null)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: il totale dei dettagli intrastat di " + impIntrastat + " non corrisponde al totale di " + fatturaPassiva.getIm_totale_imponibile() + " (solo imponibile)!");
				
			if (fatturaPassiva.getIm_totale_imponibile().compareTo(impIntrastat) != 0) {
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: il totale dei dettagli intrastat di " + impIntrastat + " non corrisponde al totale di " + fatturaPassiva.getIm_totale_fattura_calcolato() + " (solo imponibile)!");
			}
		}
		*/			
	} catch (RemoteException t) {
		throw handleException(fattura, t);	
	} catch (ComponentException t) {
		throw handleException(fattura, t);
	}
}
//^^@@
/** 
  *	Quadratura delle scadenze obbligazioni di fattura passiva non estera o estera senza lettera di pagamento.
  *		PreCondition:
  * 		La somma algebrica dei dettagli, storni e addebiti (metodo 'calcolaTotaleObbligazionePer') insistenti sull'elenco di dettagli associati
  *			alla scadenza obbligazione � uguale all'importo della scadenza obbligazione stessa
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Quadratura delle scadenze obbligazioni di fattura passiva estera con lettera di pagamento.
  *		PreCondition:
  * 		L'importo della lettera di pagamento � uguale all'importo della scadenza obbligazione
  *   	PostCondition:
  *  		Permette la continuazione.
  *	Controlli non superati.
  *		PreCondition:
  * 		Non vengono superate tutte le validazioni
  *   	PostCondition:
  *  		Emette errore con messaggio:"Quadratura non superata".
 */
//^^@@



public void controllaQuadraturaObbligazioni(UserContext aUC,Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva != null && !fatturaPassiva.isAnnullato() && !fatturaPassiva.isCongelata()) {
		ObbligazioniTable obbligazioniHash = fatturaPassiva.getFattura_passiva_obbligazioniHash();
		if (obbligazioniHash != null) {
			for (java.util.Enumeration e = obbligazioniHash.keys(); e.hasMoreElements();) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
				java.math.BigDecimal totale = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
				java.math.BigDecimal delta = null;
				if (fatturaPassiva.isEstera() &&
					fatturaPassiva.getLettera_pagamento_estero() != null &&
					fatturaPassiva.getLettera_pagamento_estero().getIm_pagamento() != null &&
					fatturaPassiva.getLettera_pagamento_estero().getCd_sospeso() != null) {
					if (fatturaPassiva.getLettera_pagamento_estero().getIm_pagamento().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) == 0) {
						totale = calcolaTotaleObbligazionePer(aUC, scadenza, fatturaPassiva).abs();
						delta = scadenza.getIm_scadenza().subtract(totale);
					} else {
						for (java.util.Enumeration scadenze = fatturaPassiva.getFattura_passiva_obbligazioniHash().keys(); scadenze.hasMoreElements();) {
							Obbligazione_scadenzarioBulk scad = (Obbligazione_scadenzarioBulk)scadenze.nextElement();
							totale = totale.add(scad.getIm_scadenza());
						}
						//Errore 588: se fattura estera e lettera 1210 associata nel
						//caso di QUADRATURA IN DEROGA DEVO sommare anche tutta l'iva
						java.math.BigDecimal importoLettera = fatturaPassiva.getLettera_pagamento_estero().getIm_pagamento();
						boolean deroga1210 = fatturaPassiva.quadraturaInDeroga1210();
						if (deroga1210){
								if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
									Fattura_passiva_IBulk fatturaPassivaI = (Fattura_passiva_IBulk)fatturaPassiva;
									if (fatturaPassivaI.hasStorni() || fatturaPassivaI.hasAddebiti())
										importoLettera = importoLettera.add(calcolaTotaleIVAPer((List)fatturaPassiva.getObbligazioniHash().get(scadenza)));
									else
										importoLettera = importoLettera.add(fatturaPassiva.getIm_totale_iva());
								}
						}
						delta = importoLettera.subtract(totale);
						if (delta.compareTo(new java.math.BigDecimal(0)) != 0) {
							if (deroga1210)
								throw new it.cnr.jada.comp.ApplicationException("La somma delle scadenze delle obbligazioni deve corrispondere all'importo della lettera di pagamento estero addizionato del totale IVA!");
							else
								throw new it.cnr.jada.comp.ApplicationException("La somma delle scadenze delle obbligazioni deve corrispondere all'importo della lettera di pagamento estero!");
						}
						if (fatturaPassiva instanceof Fattura_passiva_IBulk &&
							deroga1210) {
							totale = calcolaTotaleIVAPer((List)fatturaPassiva.getObbligazioniHash().get(scadenza));
							if (scadenza.getIm_scadenza().compareTo(totale) <= 0)
								throw new it.cnr.jada.comp.ApplicationException("Attenzione: l'importo della scadenza \"" + scadenza.getDs_scadenza() + "\" deve essere strettamente maggiore dell'importo totale IVA dei dettagli ad essa associati!");
						}
					}
				} else {
					totale = calcolaTotaleObbligazionePer(aUC, scadenza, fatturaPassiva);//.abs();
					delta = scadenza.getIm_scadenza().subtract(totale);
				}
				if (delta.compareTo(new java.math.BigDecimal(0)) > 0) {
					StringBuffer sb = new StringBuffer();
					sb.append("Attenzione: La scadenza ");
					sb.append(scadenza.getDs_scadenza());
					sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
					sb.append(" � stata coperta solo per ");
					sb.append(totale.doubleValue() + " EUR!");
					throw new it.cnr.jada.comp.ApplicationException(sb.toString());
				} else if (delta.compareTo(new java.math.BigDecimal(0)) < 0) {
					StringBuffer sb = new StringBuffer();
					sb.append("Attenzione: La scadenza ");
					sb.append(scadenza.getDs_scadenza());
					sb.append(" di " + scadenza.getIm_scadenza().doubleValue() + " EUR");
					sb.append(" � scoperta per ");
					sb.append(delta.abs().doubleValue() + " EUR!");
					throw new it.cnr.jada.comp.ApplicationException(sb.toString());
				}					
				controllaOmogeneitaTraTerzi(aUC, scadenza, (Vector)obbligazioniHash.get(scadenza));
			}
		}
	}
}
private void creaAutofattura(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {
	if (fattura_passiva != null) {
		boolean autoObb=false, 
				fatturaSplit = fattura_passiva.isCommerciale() && 
 				  			   fattura_passiva.getFl_split_payment()!=null && fattura_passiva.getFl_split_payment();

		autoObb=verificaGenerazioneAutofattura(userContext, fattura_passiva);
		if(autoObb)
			fattura_passiva.setFl_autofattura(Boolean.TRUE);

		if (fattura_passiva.getFl_autofattura() == null)
			fattura_passiva.setFl_autofattura(Boolean.FALSE);

		if (fattura_passiva.getFl_autofattura().booleanValue() || fatturaSplit) {
			it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk uoEnte = findUOEnte(userContext, fattura_passiva.getEsercizio());
			AutofatturaBulk autofattura = new AutofatturaBulk();
			autofattura.setCd_cds(uoEnte.getCd_unita_padre());
			autofattura.setCd_unita_organizzativa(uoEnte.getCd_unita_organizzativa());
			autofattura.setEsercizio(fattura_passiva.getEsercizio());
			autofattura.completeFrom(fattura_passiva);
			AutoFatturaComponentSession h = getAutofatturaComponentSession(userContext);
			try {
				Vector sez = h.estraeSezionali(userContext, autofattura,autoObb||fatturaSplit);
				if (sez == null || sez.isEmpty())
					throw new it.cnr.jada.comp.ApplicationException("Non � stato inserito alcun sezionale valido per l'autofattura collegata al documento amministrativo " + fattura_passiva.getPg_fattura_passiva().longValue() + "!");
				if (sez.size() != 1)
					throw new it.cnr.jada.comp.ApplicationException("Sono stati trovati pi� sezionali validi per l'autofattura collegata alla fattura passiva " + fattura_passiva.getPg_fattura_passiva().longValue() + "!");
				autofattura.setTipo_sezionale((Tipo_sezionaleBulk)sez.firstElement());
				AutofatturaBulk autof = (AutofatturaBulk)h.creaConBulk(userContext, autofattura);
				fattura_passiva.setAutofattura(autof);
				updateBulk(userContext,fattura_passiva);
			} catch (java.rmi.RemoteException e) {
				throw handleException(autofattura, e);
			} catch (PersistencyException e) {
				throw handleException(autofattura, e);
			}
		}
	}
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */
//^^@@

public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	return creaConBulk(userContext, bulk, null);
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */
//^^@@

public OggettoBulk creaConBulk(
	UserContext userContext,
	OggettoBulk bulk,
	OptionRequestParameter status)
	throws ComponentException {

	Fattura_passivaBulk fattura_passiva = (Fattura_passivaBulk)bulk;
	
	assegnaProgressivo(userContext, fattura_passiva);
	
	if(fattura_passiva.isElettronica())
		validaFatturaElettronica(userContext, fattura_passiva);
	
	try {
		if (fattura_passiva instanceof Fattura_passiva_IBulk ||fattura_passiva instanceof Nota_di_creditoBulk ) {
			if (fattura_passiva.existARowToBeInventoried()) { 
				verificaEsistenzaEdAperturaInventario(userContext, fattura_passiva);
				if (fattura_passiva instanceof Fattura_passiva_IBulk  && hasFatturaPassivaARowNotInventoried(userContext, fattura_passiva) &&
					(fattura_passiva.getStato_liquidazione()==null || fattura_passiva.getStato_liquidazione().compareTo(Fattura_passiva_IBulk.LIQ)==0))
						throw new it.cnr.jada.comp.ApplicationException("Attenzione: � necessario inventariare tutti i dettagli.");
				if (fattura_passiva instanceof Nota_di_creditoBulk  && hasFatturaPassivaARowNotInventoried(userContext, fattura_passiva))
							throw new it.cnr.jada.comp.ApplicationException("Attenzione: � necessario inventariare tutti i dettagli.");	
			}
		}
		validaFattura(userContext, fattura_passiva);
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	}

    // Salvo temporaneamente l'hash map dei saldi
    PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
    if (fattura_passiva.getDefferredSaldi() != null)
	    aTempDiffSaldi=(PrimaryKeyHashMap)fattura_passiva.getDefferredSaldi().clone();    

    manageDocumentiContabiliCancellati(userContext, fattura_passiva, status);

	aggiornaObbligazioni(	
					userContext, 
					fattura_passiva,
					status);
	if (fattura_passiva instanceof Nota_di_creditoBulk) {
		Nota_di_creditoBulk ndc = (Nota_di_creditoBulk)fattura_passiva;
		aggiornaRigheFatturaPassivaDiOrigine(userContext, ndc);
		aggiornaAccertamenti(userContext, ndc, status);
	}
	if (fattura_passiva instanceof Nota_di_debitoBulk) {
		Nota_di_debitoBulk ndd = (Nota_di_debitoBulk)fattura_passiva;
		aggiornaRigheFatturaPassivaDiOrigine(userContext, ndd);
	}

	prepareCarichiInventario(userContext, fattura_passiva);

	Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
	if (lettera != null) {
		try {
			Lettera_pagam_esteroBulk original1210 = (Lettera_pagam_esteroBulk)getHome(userContext, lettera).findByPrimaryKey(lettera);
			aggiornaLetteraPagamentoEstero(userContext, lettera);
			if (original1210 == null ||
				lettera.getIm_pagamento().compareTo(original1210.getIm_pagamento()) != 0)
				validaDisponibilitaDiCassaCDS(userContext, fattura_passiva);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(lettera,e);
		}
	}
	
	fattura_passiva = (Fattura_passivaBulk)super.creaConBulk(userContext, fattura_passiva);
	
	aggiornaCarichiInventario(userContext, fattura_passiva);

	// Le operazioni che rendono persistenti le modifiche fatte sull'Inventario,
	//	potrebbero rimandare un messaggio all'utente.
	String messaggio = aggiornaAssociazioniInventario(userContext, fattura_passiva);
	
	creaAutofattura(userContext, fattura_passiva);
	
	// Restore dell'hash map dei saldi
	if (fattura_passiva.getDefferredSaldi() != null)
		fattura_passiva.getDefferredSaldi().putAll(aTempDiffSaldi);
	aggiornaCogeCoanDocAmm(userContext, fattura_passiva);
	
	aggiornaDataEsigibilitaIVA(userContext, fattura_passiva, "C");
	
	try {
        if (!verificaStatoEsercizio(
	        					userContext, 
	        					new EsercizioBulk( 
		        							fattura_passiva.getCd_cds(), 
		        							((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw handleException(bulk, e);
	}
	controllaQuadraturaInventario(userContext,fattura_passiva);
	logger.info("Creazione fattura passiva legata al documento elettronico:" + fattura_passiva.getDocumentoEleTestata());
	if (fattura_passiva.getDocumentoEleTestata() != null) {
		fattura_passiva.getDocumentoEleTestata().setStatoDocumento(StatoDocumentoEleEnum.REGISTRATO.name());
		fattura_passiva.getDocumentoEleTestata().setToBeUpdated();

		try {
			if (Utility.createParametriEnteComponentSession().getParametriEnte(userContext).getTipo_db().equals(Parametri_enteBulk.DB_PRODUZIONE)) {
				TipoIntegrazioneSDI tipoIntegrazioneSDI = TipoIntegrazioneSDI.PEC;
				Configurazione_cnrBulk configurazione_cnrBulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).
						findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(
								Configurazione_cnrBulk.PK_INTEGRAZIONE_SDI, Configurazione_cnrBulk.SK_INTEGRAZIONE_SDI,
								"*", CNRUserContext.getEsercizio(userContext)));
				if (configurazione_cnrBulk != null)
					tipoIntegrazioneSDI = TipoIntegrazioneSDI.valueOf(configurazione_cnrBulk.getVal01());
	
				logger.info("Notifica di accettazione relativa al documento:" + fattura_passiva.getDocumentoEleTestata());			
				((DocumentoEleTestataHome)getHome(userContext, DocumentoEleTestataBulk.class)).
					notificaEsito(userContext, tipoIntegrazioneSDI, fattura_passiva.getDocumentoEleTestata());
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		} catch (IOException e) {
			throw handleException(e);
		}
		logger.info("Aggiornamento dello stato REGISTRATO sul documento:" + fattura_passiva.getDocumentoEleTestata());			
		super.modificaConBulk(userContext, fattura_passiva.getDocumentoEleTestata());
	}
	try {
		assegnaProgressivoUnivoco(userContext, fattura_passiva);
		super.updateBulk(userContext, fattura_passiva, false);		
	} catch(PersistencyException _ex) {
		throw handleException(_ex);
	}
	if (fattura_passiva.getDocumentoEleTestata() != null && fattura_passiva.getDocumentoEleTestata().getIdentificativoSdi() != null) {
		try {
			if (Utility.createParametriEnteComponentSession().getParametriEnte(userContext).getTipo_db().equals(Parametri_enteBulk.DB_PRODUZIONE))
				aggiornaMetadatiDocumentale(fattura_passiva);
		} catch (RemoteException | EJBException e) {
			throw handleException(e);
		}
	}
	if (messaggio != null)
		return asMTU(fattura_passiva, messaggio);
	return fattura_passiva;
}
private void aggiornaMetadatiDocumentale(Fattura_passivaBulk fattura_passiva) throws ComponentException {
	CMISFolderFatturaPassiva folder = new CMISFolderFatturaPassiva(fattura_passiva, fattura_passiva.getDocumentoEleTestata());	
	SiglaCMISService cmisService = SpringUtil.getBean("cmisService", SiglaCMISService.class);
	folder.updateMetadataPropertiesCMIS(cmisService);
}
private void deleteAssociazioniInventarioWith(UserContext userContext,Fattura_passiva_rigaBulk dettaglio)
	throws ComponentException {
	try {
	if (dettaglio != null && dettaglio.isToBeDeleted()) {
		Ass_inv_bene_fatturaHome asshome=(Ass_inv_bene_fatturaHome)getHome(userContext,Ass_inv_bene_fatturaBulk.class);
		SQLBuilder sql = asshome.createSQLBuilder();
	    sql.addSQLClause("AND","CD_CDS_FATT_PASS",sql.EQUALS,dettaglio.getCd_cds());
	    sql.addSQLClause("AND","ESERCIZIO_FATT_PASS",sql.EQUALS,dettaglio.getEsercizio().intValue());
	    sql.addSQLClause("AND","CD_UO_FATT_PASS",sql.EQUALS,dettaglio.getCd_unita_organizzativa());
	    sql.addSQLClause("AND","PG_FATTURA_PASSIVA",sql.EQUALS,dettaglio.getPg_fattura_passiva().longValue());
	    sql.addSQLClause("AND","PROGRESSIVO_RIGA_FATT_PASS",sql.EQUALS,dettaglio.getProgressivo_riga().longValue());
	    List associazioni = asshome.fetchAll(sql);
	    for (Iterator i = associazioni.iterator();i.hasNext();){
	    	Ass_inv_bene_fatturaBulk associazione = (Ass_inv_bene_fatturaBulk)i.next();
	    	associazione.setCrudStatus(OggettoBulk.TO_BE_DELETED);
	    	super.eliminaConBulk(userContext,associazione);
	    }
	}
	} catch (PersistencyException e) {
		throw handleException(dettaglio, e);
	}
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della fattura.
  *  validazione eliminazione fattura.
  *    PreCondition:
  *      E' stata eliminata una fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare una fattura in stato IVA B o C"
 */
//^^@@

private void deleteLogically(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	if (fattura_passiva instanceof Voidable && ((Voidable)fattura_passiva).isVoidable()) {
		try {
			java.sql.Timestamp dataAnnullamento = getHome(userContext, fattura_passiva).getServerTimestamp();
			java.util.Calendar dtAnnullamentoCal = java.util.GregorianCalendar.getInstance();
			dtAnnullamentoCal.setTime(dataAnnullamento);
			int annoSolare = dtAnnullamentoCal.get(java.util.Calendar.YEAR);
			if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue()) {
				//Sono costretto ad utilizzare il Calendar per mantenere ora, minuti e secondi.
				dtAnnullamentoCal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
				dtAnnullamentoCal.set(java.util.Calendar.DAY_OF_MONTH, 31);
				dtAnnullamentoCal.set(java.util.Calendar.YEAR, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
				dataAnnullamento = new java.sql.Timestamp(dtAnnullamentoCal.getTime().getTime());
			}
			
			for (int c = 0; c < fattura_passiva.getBulkLists().length; c++) {
				BulkList bl = (it.cnr.jada.bulk.BulkList)fattura_passiva.getBulkLists()[c];
				for (Iterator i = bl.iterator(); i.hasNext();) {
					OggettoBulk obj = (OggettoBulk)i.next();
					if (obj instanceof Fattura_passiva_rigaBulk) {
						Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk)obj;
						if (fpr instanceof Voidable && ((Voidable)fpr).isVoidable()) {
							((Voidable)fpr).setAnnullato(dataAnnullamento);
							fpr.setToBeUpdated();
							if (fpr.isInventariato()) {
								Fattura_passiva_rigaBulk cloneDettaglio = (Fattura_passiva_rigaBulk)fpr.clone();
								cloneDettaglio.setToBeDeleted();
								deleteAssociazioniInventarioWith(userContext, cloneDettaglio);
							}
							update(userContext, fpr);
						} else
							throw new ApplicationException("Questa fattura NON � annullabile perch� almeno uno dei sui dettagli non � stato associato a mandato o reversale!");
					}
				}
			}

			fattura_passiva.setAnnullato(dataAnnullamento);
			if (fattura_passiva.REGISTRATO_IN_COGE.equalsIgnoreCase(fattura_passiva.getStato_coge()))
				fattura_passiva.setStato_coge(fattura_passiva.DA_RICONTABILIZZARE_IN_COGE);
			if (fattura_passiva.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(fattura_passiva.getStato_coan()))
				fattura_passiva.setStato_coan(fattura_passiva.DA_RICONTABILIZZARE_IN_COAN);

			try {
				if (fattura_passiva.getAutofattura() != null) {
					AutoFatturaComponentSession h = getAutofatturaComponentSession(userContext);
					fattura_passiva.getAutofattura().setToBeDeleted();
					h.eliminaConBulk(userContext, fattura_passiva.getAutofattura());
					fattura_passiva.setAutofattura(null);
				}
			} catch (java.rmi.RemoteException e) {
				throw handleException(fattura_passiva, e);
			}
	
			fattura_passiva.setToBeUpdated();
			updateBulk(userContext, fattura_passiva);

			if (fattura_passiva instanceof Fattura_passiva_IBulk &&
				fattura_passiva.getObbligazioniHash() != null) {
					aggiornaObbligazioniSuCancellazione(
						userContext,
						fattura_passiva,
						fattura_passiva.getObbligazioniHash().keys(), 
						null,
						null);
					Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
					if (lettera != null &&
						lettera.getSospeso() != null &&
						lettera.getSospeso().getCrudStatus() == OggettoBulk.NORMAL)
						liberaSospeso(userContext, lettera.getSospeso());
				}
			if (fattura_passiva instanceof Nota_di_creditoBulk &&
				((Nota_di_creditoBulk)fattura_passiva).getAccertamentiHash() != null)
				aggiornaAccertamentiSuCancellazione(
					userContext,
					(Nota_di_creditoBulk)fattura_passiva,
					((Nota_di_creditoBulk)fattura_passiva).getAccertamentiHash().keys(),
					null,
					null);
			
			return;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(fattura_passiva, e);
		}
	}
	throw new ApplicationException("Questa fattura NON � annullabile!");
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della fattura.
  *  validazione eliminazione fattura.
  *    PreCondition:
  *      E' stata eliminata una fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare una fattura in stato IVA B o C"
 */
//^^@@

private void deletePhisically(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	for (int c = 0; c < fattura_passiva.getBulkLists().length; c++) {
		BulkList bl = (it.cnr.jada.bulk.BulkList)fattura_passiva.getBulkLists()[c];
		for (Iterator i = bl.iterator(); i.hasNext();) {
			OggettoBulk obj = (OggettoBulk)i.next();
			obj.setToBeDeleted();
			if (obj instanceof Fattura_passiva_rigaBulk) {
				Fattura_passiva_rigaBulk fpr = (Fattura_passiva_rigaBulk)obj;
				if (fpr.isInventariato())
					deleteAssociazioniInventarioWith(userContext, fpr);
			}
		}
	}

	try {
		if (fattura_passiva.getAutofattura() != null) {
			AutoFatturaComponentSession h = getAutofatturaComponentSession(userContext);
			fattura_passiva.getAutofattura().setToBeDeleted();
			h.eliminaConBulk(userContext, fattura_passiva.getAutofattura());
			fattura_passiva.setAutofattura(null);
		}
	} catch (java.rmi.RemoteException e) {
		throw handleException(fattura_passiva, e);
	}

	super.eliminaConBulk(userContext, fattura_passiva);

	Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
	if (lettera != null)
		try {
			if (lettera.getSospeso() != null && lettera.getSospeso().getCrudStatus() == OggettoBulk.NORMAL)
				liberaSospeso(userContext, lettera.getSospeso());
			lettera.setToBeDeleted();
			makeBulkPersistent(userContext, lettera);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(lettera,e);
		}

	try {
		if (fattura_passiva instanceof Fattura_passiva_IBulk)
			aggiornaObbligazioniSuCancellazione(
				userContext, 
				fattura_passiva,
				fattura_passiva.getObbligazioniHash().keys(), 
				null,
				null);
		if (fattura_passiva instanceof Nota_di_creditoBulk &&
			((Nota_di_creditoBulk)fattura_passiva).getAccertamentiHash() != null)
			aggiornaAccertamentiSuCancellazione(
				userContext,
				(Nota_di_creditoBulk)fattura_passiva,
				((Nota_di_creditoBulk)fattura_passiva).getAccertamentiHash().keys(),
				null,
				null);
	} catch (Throwable e) {
		throw handleException(fattura_passiva, e);
	}
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della fattura.
  *  validazione eliminazione fattura.
  *    PreCondition:
  *      E' stata eliminata una fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare una fattura in stato IVA B o C"
 */
//^^@@

public void eliminaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException {

Fattura_passivaBulk fattura_passiva = (Fattura_passivaBulk)bulk;
	
	// Gennaro Borriello - (08/11/2004 19.06.55)
	//	Controllo gli stati degli esercizi Finanziari/Economici.
	verificaStatoEsercizi(aUC, fattura_passiva);
	
	eliminaFattura(aUC, fattura_passiva);
	Integer contaRighe=0;
	for (Iterator i = fattura_passiva.getFattura_passiva_dettColl().iterator(); i.hasNext();){
		Fattura_passiva_rigaBulk riga=(Fattura_passiva_rigaBulk)i.next();
		eliminaRiga(aUC, riga);
			try {
				List result = findManRevRigaCollegati(aUC, riga);
				if (result!=null && !result.isEmpty()){
					riga.setToBeUpdated();
					riga.setTi_associato_manrev(Fattura_passivaBulk.ASSOCIATO_A_MANDATO);
					contaRighe++;
				}		
			} catch (PersistencyException e) {
				throw new ComponentException(e);
			} catch (IntrospectionException e) {
				throw new ComponentException(e);
			}
	}
	if(contaRighe== fattura_passiva.getFattura_passiva_dettColl().size()){
		fattura_passiva.setToBeUpdated();
		fattura_passiva.setTi_associato_manrev(Fattura_passivaBulk.ASSOCIATO_A_MANDATO);
	}
	
    // Salvo temporaneamente l'hash map dei saldi
    PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
    if (fattura_passiva.getDefferredSaldi() != null)
	    aTempDiffSaldi=(PrimaryKeyHashMap)fattura_passiva.getDefferredSaldi().clone();    

	if (fattura_passiva instanceof Voidable && fattura_passiva.isVoidable())
		deleteLogically(aUC, fattura_passiva);
	else
		deletePhisically(aUC, fattura_passiva);

    // Restore dell'hash map dei saldi
    if (fattura_passiva.getDefferredSaldi() != null)
		fattura_passiva.getDefferredSaldi().putAll(aTempDiffSaldi);
	aggiornaCogeCoanDocAmm(aUC, fattura_passiva);

}
private void eliminaFattura(UserContext aUC,Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva.STATO_PARZIALE.equalsIgnoreCase(fatturaPassiva.getStato_cofi()))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare una " + fatturaPassiva.getDescrizioneEntita() + " in stato parziale.");

	if (fatturaPassiva.isPagata())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare una " + fatturaPassiva.getDescrizioneEntita() + " pagata o registrata in fondo economale!");
	
	if (fatturaPassiva.hasIntrastatInviati())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare una " + fatturaPassiva.getDescrizioneEntita() + " per cui esistono dettagli intrastat inviati!");
	//ATTENZIONE: a seguito dell'errore segnalato 569 (dovuto alla richiesta 423) il controllo viene
	//ora eseguito anche se la sola autofattura � stampata sui registri IVA
	if (fatturaPassiva.isStampataSuRegistroIVA() || fatturaPassiva.getProgr_univoco()!=null)
	throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare una " + fatturaPassiva.getDescrizioneEntita() + " o la sua autofattura (se esiste) quando una di esse � gi� stampata sui registri IVA oppure � valorizzato il progressivo univoco!");

	if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
		Fattura_passiva_IBulk fatturaPassivaI = (Fattura_passiva_IBulk)fatturaPassiva;
		if (fatturaPassivaI.hasStorni() || fatturaPassivaI.hasAddebiti())
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: per cancellare una fattura � necessario eliminare tutte le note di credito/debito collegate!");
		//Controllo la presenza di eventuali ndc o ndd annullate per non permettere la
		//cancellazione della fattura. Infatti hasStorni e hasAddebiti non caricano
		//i documenti annullati (Necessario solo se la fattura � cancellabile fisicamente e non logicamente)
		if (!fatturaPassivaI.isVoidable()) {
			try {
				Nota_di_creditoHome homeNdC = (Nota_di_creditoHome)getHome(aUC,Nota_di_creditoBulk.class);
				if (homeNdC.selectFor(fatturaPassivaI).executeExistsQuery(getConnection(aUC)))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare fisicamente questa fattura perch� ad essa sono associate note di credito annullate!");
				Nota_di_debitoHome homeNdD = (Nota_di_debitoHome)getHome(aUC,Nota_di_debitoBulk.class);
				if (homeNdD.selectFor(fatturaPassivaI).executeExistsQuery(getConnection(aUC)))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile cancellare fisicamente questa fattura perch� ad essa sono associate note di debito annullate!");
			} catch (SQLException e) {
				throw handleException(fatturaPassivaI, e);
			}
				
		}
		if (hasBolleDoganali(aUC, fatturaPassivaI))
			throw new it.cnr.jada.comp.ApplicationException("La fattura � collegata a una o pi� bolle doganali! Impossibile cancellare.");
		if (hasSpedizionieri(aUC, fatturaPassivaI))
			throw new it.cnr.jada.comp.ApplicationException("La fattura � collegata a una o pi� fatture di spedizionieri! Impossibile cancellare.");
	}

	//Controllo nel caso di fattura annullabile che tutti i dettagli siano
	//annullabili.
	if (fatturaPassiva.isVoidable()) {
		boolean deletable = true;
		for (Iterator i = fatturaPassiva.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
			boolean voidable = ((Voidable)i.next()).isVoidable();
			if (!voidable && deletable)
				deletable = false;
		}
		if (!deletable)
			if (fatturaPassiva.isRiportata())
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: questa fattura non � annullabile perch� ha dettagli non riportati!");
			else
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: questa fattura non � annullabile perch� ha dettagli non associati a mandati/reversali o\nla testata � registrata in COAN o COGE!");
	}
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Permette la cancellazione della lettera di pagamento estero (Doc 1210).
  *  validazione esistenza lettera 1210.
  *    PreCondition:
  *      Non � rilevata la presenza di doc 1210
  *    PostCondition:
  *      Esce dal metodo senza apportare modifiche
 */
//^^@@

public Fattura_passivaBulk eliminaLetteraPagamentoEstero(
	UserContext context,
	Fattura_passivaBulk fatturaPassiva,boolean cancella)
	throws ComponentException {

	if (fatturaPassiva != null &&
		fatturaPassiva.getLettera_pagamento_estero() != null) {

		Lettera_pagam_esteroBulk lettera = fatturaPassiva.getLettera_pagamento_estero();
		try {
			if(cancella){
				basicAggiornaLetteraPagamentoEstero(context, lettera, null);
				lettera.setToBeDeleted();
				makeBulkPersistent(context, lettera);
			}else{
				java.sql.Timestamp dataAnnullamento = getHome(context, fatturaPassiva).getServerDate();
				lettera.setToBeUpdated();
				lettera.setDt_cancellazione(dataAnnullamento);
				if(lettera.getSospeso()!=null){
					liberaSospeso(context, lettera.getSospeso());
					lettera.setSospeso(null);
				}
				makeBulkPersistent(context, lettera);
			}
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(lettera, e);
		}
		fatturaPassiva.setLettera_pagamento_estero(null);
	}
	return fatturaPassiva;
}
//^^@@
/** 
  *  valida eliminazione dettaglio
  *    PreCondition:
  *      E' stato eliminato un dettaglio in  in una fattura in stato IVA B o C.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare un dettaglio in una fattura in stato IVA B o C".
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessun errore � stato rilevato.
  *    PostCondition:
  *      Viene dato il consenso per l'eliminazione della riga.
  *      
  *  valida eliminazione dettaglio pagato.
  *    PreCondition:
  *      E' stato eliminato un dettaglio in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare un dettaglio pagato".
  *  eliminazione dettaglio in fattura pagata.
  *    PreCondition:
  *      E' stato eliminato un dettaglio in una fattura con testata in stato P
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� eliminare un dettaglio in una fattura pagata".
 */
//^^@@

public void eliminaRiga (UserContext aUC,Fattura_passiva_rigaBulk riga) throws ComponentException {

	if (riga.getFattura_passiva().isPagata())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� eliminare un dettaglio di una " + riga.getFattura_passiva().getDescrizioneEntita() + " gi� pagata o registrata in fondo economale.");

	if (riga instanceof Fattura_passiva_rigaIBulk) {
		Fattura_passiva_rigaIBulk fpRiga = (Fattura_passiva_rigaIBulk)riga;
		if (fpRiga.hasAddebiti() || fpRiga.hasStorni())
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� eliminare il dettaglio " + ((fpRiga.getDs_riga_fattura() == null)? "" : fpRiga.getDs_riga_fattura()) + " perch� ad esso sono associati addebiti o storni!");
	}
	
	//Tolto come da richiesta 423.
	//if (riga.getFattura_passiva().isStampataSuRegistroIVA())
			//throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � permessa la cancellazione di un dettaglio quando lo stato IVA � B o C.");

	if (riga.isPagata())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� eliminare il dettaglio " + ((riga.getDs_riga_fattura() == null)? "" : riga.getDs_riga_fattura()) + " gi� pagato.");

}
//^^@@
/** 
  *  fattura istituzionale
  *    PreCondition:
  *      La fattura � di tipo istituzionale.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondente al tipo  sezionale istituzionale.
  *  fattura di tipo commerciale o promiscua 
  *    PreCondition:
  *      E' stato selezionata una fattura di tipo commerciale o promiscua.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali corrispondenti al tipo commerciale.
  *  fattura di tipo intra UE
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo intra UE.
  *    PostCondition:
  *      E' stato estratto il vettore dei sezionali intra UE.
  *  fattura relativa alla repubblica di S.Marino
  *    PreCondition:
  *      E' stata selezionata una fattura relativa alla Repubblica di S.Marino.
  *    PostCondition:
  *      Sono stati estratti i vettori dei sezionali corrispondenti alla repubblica di s.Marino.
  *  fattura di tipo non valido
  *    PreCondition:
  *      E' stata selezionata una fattura di tipo non valido.
  *    PostCondition:
  *      Viene inviato il messaggio: "Il tipo di fattura selezionato non � valido".
 */
//^^@@

public Vector estraeSezionali (UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException {

	try {
		if (fatturaPassiva == null || fatturaPassiva.getTi_istituz_commerc() == null)
			return null;
		return new Vector(findSezionali(aUC, fatturaPassiva));
	} catch (Throwable t) {
		throw handleException(fatturaPassiva, t);
	}
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta dell'anagrafico del cessionario
  *   	PostCondition:
  *  		Restituisce l'anagrafico
 */
//^^@@

public TerzoBulk findCessionario(UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException {

	try	{
		if (fattura == null || fattura.getModalita_pagamento() == null)
			return null;
		
		Modalita_pagamentoHome mph = (Modalita_pagamentoHome)getHome(userContext, Modalita_pagamentoBulk.class);
		List<Modalita_pagamentoBulk> mps = mph.find(new Modalita_pagamentoBulk(fattura.getModalita_pagamento().getCd_modalita_pag(), fattura.getCd_terzo()));
		if (mps.isEmpty())
			throw new ApplicationException("Modalit� di pagamento non trovata per il Terzo:" +fattura.getCd_terzo());
		Modalita_pagamentoBulk mp = mps.get(0);
		if (mp == null || fattura.getBanca()==null || fattura.getBanca().getCd_terzo_delegato() == null) return null;
		TerzoHome th = (TerzoHome)getHome(userContext, TerzoBulk.class);
		return (TerzoBulk)th.findByPrimaryKey(new TerzoBulk(fattura.getBanca().getCd_terzo_delegato() ));
	} catch( Exception e ) {
		throw handleException(e);
	}		
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */
//^^@@

public java.util.List findDettagli(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	
	if (fatturaPassiva == null) return null;
	
	it.cnr.jada.bulk.BulkHome home = null;
	if (fatturaPassiva instanceof Nota_di_creditoBulk)
		home = getHome(aUC, Nota_di_credito_rigaBulk.class);
	else if (fatturaPassiva instanceof Nota_di_debitoBulk)
		home = getHome(aUC, Nota_di_debito_rigaBulk.class);
	else home = getHome(aUC, Fattura_passiva_rigaIBulk.class);
	
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "pg_fattura_passiva", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
	sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaPassiva.getCd_cds());
	sql.addClause("AND", "esercizio", sql.EQUALS, fatturaPassiva.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
	return home.fetchAll(sql);
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 		Richiesta di caricamento dettagli di una fattura passiva, nota di credito, nota di debito
  *   	PostCondition:
  *  		Restituisce la lista dei dettagli
 */
//^^@@

private java.util.List findDettagliIntrastat(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	
	if (fatturaPassiva == null && 
		!fatturaPassiva.getFl_intra_ue().booleanValue()) return null;
	
	it.cnr.jada.bulk.BulkHome home = getHome(aUC, Fattura_passiva_intraBulk.class, null, "NoFatturaPassivaBulk");
	
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "pg_fattura_passiva", sql.EQUALS, fatturaPassiva.getPg_fattura_passiva());
	sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaPassiva.getCd_cds());
	sql.addClause("AND", "esercizio", sql.EQUALS, fatturaPassiva.getEsercizio());
	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, fatturaPassiva.getCd_unita_organizzativa());
	return home.fetchAll(sql);
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche del fornitore.
 */
//^^@@

public java.util.Collection findListabanche(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException,it.cnr.jada.persistency.PersistencyException {

	if (fatturaPassiva.getFornitore() == null ||
		fatturaPassiva.getFornitore().getCd_terzo() == null) return null;

	return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, fatturaPassiva, null, null));
}
public java.util.Collection findListabanchedett(UserContext aUC,Fattura_passiva_rigaBulk fatturaPassivaRiga) throws ComponentException,it.cnr.jada.persistency.PersistencyException {

	if (fatturaPassivaRiga.getFornitore() == null ||
			fatturaPassivaRiga.getFornitore().getCd_terzo() == null) return null;

	return getHome(aUC, BancaBulk.class).fetchAll(selectBancaByClause(aUC, fatturaPassivaRiga, null, null));
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene restituita la lista delle banche dell'ente.
 */
//^^@@

public java.util.Collection findListabancheuo(UserContext aUC,Nota_di_creditoBulk notaDiCredito) throws ComponentException,it.cnr.jada.persistency.PersistencyException {

	if (notaDiCredito.getEnte() == null ||
		notaDiCredito.getEnte().getCd_terzo() == null)
			return null;

	return getHome(aUC, BancaBulk.class).fetchAll(selectBanca_uoByClause(aUC, notaDiCredito, null, null));
}
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta ricerca delle modalit� di pagamento del fornitore
  *   	PostCondition:
  *  		Restituisce la collezione di modalit� di pagamento del fornitore
  *	Validazione del fornitore
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle modalit� di pagamento del fornitore.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */
//^^@@

public java.util.Collection findModalita(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (fatturaPassiva.getFornitore() == null || fatturaPassiva.getFornitore().getCd_terzo() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_modalita_pagamento(fatturaPassiva.getFornitore(), null);
}
/*
public java.util.Collection findModalita(UserContext aUC,Fattura_passiva_rigaBulk fatturaPassivaRiga) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	return findModalita(aUC, fatturaPassivaRiga.getFattura_passiva());
}
*/
public java.util.Collection findModalita(UserContext aUC,Fattura_passiva_rigaBulk fatturaPassivaRiga) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (fatturaPassivaRiga.getFornitore() == null || fatturaPassivaRiga.getFornitore().getCd_terzo() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_modalita_pagamento(fatturaPassivaRiga.getFornitore(), null);
}

//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta ricerca delle modalit� di pagamento del fornitore
  *   	PostCondition:
  *  		Restituisce la collezione di modalit� di pagamento del fornitore
  *	Validazione del fornitore
  *		PreCondition:
  *			Si � verificato un errore nel caricamento delle modalit� di pagamento del fornitore.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */
//^^@@

public java.util.Collection findModalita_uo(UserContext aUC,Nota_di_creditoBulk ndc) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (ndc.getEnte() == null || ndc.getEnte().getCd_terzo() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_modalita_pagamento(ndc.getEnte());
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 			Richiesta ricerca delle note di credito generate dalla fattura passiva in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */
//^^@@

public RemoteIterator findNotaDiCreditoFor(UserContext aUC, Fattura_passiva_IBulk fatturaPassiva) throws ComponentException {

	if (fatturaPassiva == null) return null;
	Nota_di_creditoHome home = (Nota_di_creditoHome)getHome(aUC,Nota_di_creditoBulk.class);
	return iterator(aUC,
		home.selectFor(fatturaPassiva),
		Nota_di_creditoBulk.class,
		"dafault");
}
//^^@@
/** 
  *	Normale.
  *		PreCondition:
  * 			Richiesta ricerca delle note di debito generate dalla fattura passiva in argomento
  *   		PostCondition:
  *  			Restituisce la lista delle ricorrenze
 */
//^^@@

public RemoteIterator findNotaDiDebitoFor(UserContext aUC, Fattura_passiva_IBulk fatturaPassiva) throws ComponentException {

	if (fatturaPassiva == null) return null;
	Nota_di_debitoHome home = (Nota_di_debitoHome)getHome(aUC,Nota_di_debitoBulk.class);
	return iterator(aUC,
		home.selectFor(fatturaPassiva),
		Nota_di_debitoBulk.class,
		"dafault");
}
//^^@@
/** 
  *  Ricerca obbligazioni disponibili per storno o addebito di dettagli di fattura passiva pagata.
  *    	PreCondition:
  *     	Nessuna condizione di errore rilevata.
  *    	PostCondition:
  *    		Restituisce l'elenco dei dettagli di fatture passive da cui ricavare le scadenze di obbligazione
  *			da utilizzare.
  *  Dettagli non pagati.
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca � gi� stato pagato
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Dettagli di fatture dello stesso fornitore.
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non � di fattura passiva dello stesso fornitore
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Tipo di fattura
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non � di tipo fattura passiva
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  CDS
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non � del CDS di appartenenza
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  UO
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non � della UO di appartenenza
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
  *  Importo scadenza
  *  	PreCondition:
  *   		Il dettaglio ottenuto dalla ricerca non � collegato ad una scadenza con importo
  *			maggiore o uguale all'importo passato in argomento
  *    PostCondition:
  *    		Il dettaglio non viene aggiunto all'elenco
 */
//^^@@

public RemoteIterator findObbligazioniFor(
	UserContext userContext,
	Fattura_passivaBulk fatturaPassiva, 
	java.math.BigDecimal minIm_Scadenza)
	throws ComponentException {

	Fattura_passiva_rigaIHome home = (Fattura_passiva_rigaIHome)getHome(userContext, Fattura_passiva_rigaIBulk.class);
	try {
		it.cnr.jada.persistency.sql.SQLBuilder sql = home.selectObbligazioniPer(
																userContext,
																fatturaPassiva, 
																minIm_Scadenza);
		return iterator(
			userContext,
			sql,
			Fattura_passiva_rigaIBulk.class,
			"default");
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
}
//^^@@
/** 
  *  Ricerca dei sezionali
  *    	PreCondition:
  *     	Nessuna condizione di errore rilevata.
  *    	PostCondition:
  *    		Restituisce la collezione dei sezionali validi per la fattura passiva in argomento
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata un messaggio dettagliato.
 */
//^^@@
public java.util.Collection findSezionali(UserContext aUC,Fattura_passivaBulk fatturaPassiva) 
	throws ComponentException,it.cnr.jada.persistency.PersistencyException {
	
	Vector options = new Vector();
	Vector options_all = new Vector();
	Vector options_bs = new Vector();

	if (fatturaPassiva.getFl_intra_ue() != null && fatturaPassiva.getFl_intra_ue().booleanValue()) {
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_INTRA_UE","Y", "AND" } });
	} else if (fatturaPassiva.getFl_extra_ue() != null && fatturaPassiva.getFl_extra_ue().booleanValue()) {
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_EXTRA_UE","Y", "AND" } });
		///??? Rospuc da chiedere
		if (fatturaPassiva.isIstituzionale() && fatturaPassiva.getFl_merce_intra_ue() != null && fatturaPassiva.getFl_merce_intra_ue().booleanValue()) {
			options.add(new String[][] { { "TIPO_SEZIONALE.TI_BENE_SERVIZIO","B", "AND" } });
		}else if( fatturaPassiva.getTi_bene_servizio()!=null && fatturaPassiva.getTi_bene_servizio().compareTo( Bene_servizioBulk.BENE)==0){
			options.add(new String[][] { { "TIPO_SEZIONALE.TI_BENE_SERVIZIO","*", "AND" } });
		}
		///??? Rospuc da chiedere
	} else if (fatturaPassiva.getFl_san_marino_con_iva() != null && fatturaPassiva.getFl_san_marino_con_iva().booleanValue()) {
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SAN_MARINO_CON_IVA","Y", "AND" } });
	} else if (fatturaPassiva.getFl_san_marino_senza_iva() != null &&  fatturaPassiva.getFl_san_marino_senza_iva().booleanValue()) {
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SAN_MARINO_SENZA_IVA","Y", "AND" } });
	} else {
		if ((fatturaPassiva.getFl_intra_ue()==null &&  
			 fatturaPassiva.getFl_extra_ue()==null && 
			 fatturaPassiva.getFl_san_marino_con_iva()==null && 
			 fatturaPassiva.getFl_san_marino_senza_iva()==null) ||
			(fatturaPassiva.isIstituzionale() && 
			 fatturaPassiva.getFl_split_payment()!=null && fatturaPassiva.getFl_split_payment().booleanValue()))
			options = new Vector();
		else
			options.add(new String[][] { { "TIPO_SEZIONALE.FL_ORDINARIO","Y", "AND" } });
	}
	//Richiesta Mingarelli e Paolo del 25/02/2002.
	//Aggiunta per sicurezza in modo tale che se su tipo sezionale viene aggirato
	//il controllo applicativo (Acq+Fl_autofatt) non vengano caricati sez autofatt
	options.add(new String[][] { { "TIPO_SEZIONALE.FL_AUTOFATTURA", "N", "AND" } });
	
	if (!fatturaPassiva.isIstituzionale())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SPLIT_PAYMENT", "N", "AND" } });
	else if (fatturaPassiva.getFl_split_payment()!=null && fatturaPassiva.getFl_split_payment().booleanValue())
		options.add(new String[][] { { "TIPO_SEZIONALE.FL_SPLIT_PAYMENT","Y", "AND" } });


	options.add(new String[][] {
		{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", "*", "AND" },
		{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", fatturaPassiva.getTi_bene_servizio(), "OR" }
	});
//	if(fatturaPassiva.getTi_bene_servizio()==null || fatturaPassiva.getTi_bene_servizio().compareTo(Bene_servizioBulk.BENE_SERVIZIO)==0){
//			options.add(new String[][] {
//						{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", "*", "AND" },
//						{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", fatturaPassiva.getTi_bene_servizio(), "OR" }
//					});
//	}else{ 
//		for (java.util.Iterator i = options.iterator(); i.hasNext();) {
//			String[][] option = (String[][])i.next();
//			options_bs.add(option);
//			options_all.add(option);
//		}
//		options_bs.add(new String[][] {
//				{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", fatturaPassiva.getTi_bene_servizio(), "AND" }});
//		options_all.add(new String[][] {
//						{ "TIPO_SEZIONALE.TI_BENE_SERVIZIO", "*", "AND" }});
//		try {
//			 
//			List l= new ArrayList( ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class)).findTipiSezionali(
//						fatturaPassiva.getEsercizio(),
//						fatturaPassiva.getCd_uo_origine(),
//						(fatturaPassiva.isPromiscua()) ?
//							fatturaPassiva.COMMERCIALE :
//							fatturaPassiva.getTi_istituz_commerc(),
//						it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.ACQUISTI,
//						fatturaPassiva.getTi_fattura(),
//						options_bs));
//			 List l_all= new ArrayList(((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class)).findTipiSezionali(
//						fatturaPassiva.getEsercizio(),
//						fatturaPassiva.getCd_uo_origine(),
//						(fatturaPassiva.isPromiscua()) ?
//							fatturaPassiva.COMMERCIALE :
//							fatturaPassiva.getTi_istituz_commerc(),
//						it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.ACQUISTI,
//						fatturaPassiva.getTi_fattura(),
//						options_all));
//			 if(fatturaPassiva.getFl_extra_ue() &&!fatturaPassiva.getFl_merce_intra_ue() && fatturaPassiva.getTi_bene_servizio().compareTo( Bene_servizioBulk.BENE)==0){
//				 l_all.addAll(l);
//				  return l_all;
//			 }
//			 else{
//				 l.addAll(l_all);
//				  return l;
//			 }
//		} catch (it.cnr.jada.persistency.IntrospectionException e) {
//			throw handleException(fatturaPassiva, e);
//		}
//	}

	try {
		return ((it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleHome)getHome(aUC,it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.class)).findTipiSezionali(
					fatturaPassiva.getEsercizio(),
					fatturaPassiva.getCd_uo_origine(),
					(fatturaPassiva.isPromiscua()) ?
						fatturaPassiva.COMMERCIALE :
						fatturaPassiva.getTi_istituz_commerc(),
					it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk.ACQUISTI,
					fatturaPassiva.getTi_fattura(),
					options);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(fatturaPassiva, e);
	}
}
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta ricerca dei termini di pagamento del fornitore
  *   	PostCondition:
  *  		Restituisce la collezione di termini di pagamento del fornitore
  *	Validazione del fornitore
  *		PreCondition:
  *			Si � verificato un errore nel caricamento dei termini di pagamento del fornitore.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */
//^^@@

public java.util.Collection findTermini(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (fatturaPassiva.getFornitore() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_termini_pagamento(fatturaPassiva.getFornitore());
}
public java.util.Collection findTermini(UserContext aUC,Fattura_passiva_rigaBulk fatturaPassivaRiga) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (fatturaPassivaRiga.getFornitore() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_termini_pagamento(fatturaPassivaRiga.getFornitore());
}
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta ricerca dei termini di pagamento del fornitore
  *   	PostCondition:
  *  		Restituisce la collezione di termini di pagamento del fornitore
  *	Validazione del fornitore
  *		PreCondition:
  *			Si � verificato un errore nel caricamento dei termini di pagamento del fornitore.
  * 	PostCondition:
  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
 */
//^^@@

public java.util.Collection findTermini_uo(UserContext aUC,Nota_di_creditoBulk ndc) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
	if (ndc.getEnte() == null) return null;
	return ((TerzoHome)getHome(aUC,TerzoBulk.class)).findRif_termini_pagamento(ndc.getEnte());
}
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta dell'anagrafico ente per Esercizio e Unit� organizzativa di scrivania
  *   	PostCondition:
  *  		Restituisce l'anagrafico
  *	Esercizio dell'anagrafico Ente
  *		PreCondition:
  *			L'anagrafico trovato non appartiene all'esercizio corrente
  * 	PostCondition:
  *  		Viene inviato il messaggio "Non e' stato definito in anagrafico il terzo per l'ente".
  *	Unit� Organizzativa dell'anagrafico Ente
  *		PreCondition:
  *			L'anagrafico trovato non appartiene alla UO dell'esercizio corrente
  * 	PostCondition:
  *  		Viene inviato il messaggio "Non e' stato definito una unit� organizzativa per l'Ente.".
 */
//^^@@

public TerzoBulk findTerzoUO (UserContext userContext, Integer esercizio) throws ComponentException
{
	try	{
		String cd_uo = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
		SQLBuilder sql = getHomeCache(userContext).getHome( TerzoBulk.class ).createSQLBuilder();
		sql.addClause("AND","cd_unita_organizzativa", sql.EQUALS, cd_uo);
		java.util.List result = getHomeCache(userContext).getHome( TerzoBulk.class ).fetchAll(sql);
		if ( result.size() == 0)
			throw new ApplicationException("Non e' stato definito in anagrafico il terzo per l'unit� organizzativa " + cd_uo);
		return (TerzoBulk) result.get(0);
	}	
	catch( Exception e )
	{
		throw handleException( e );
	}		

}
//^^@@
/** 
  *	Tutti i controlli  superati.
  *		PreCondition:
  * 		Richiesta della UO ente per l'esercizio corrente
  *   	PostCondition:
  *  		Restituisce la UO
  *	Unit� Organizzativa
  *		PreCondition:
  *			La UO trovata non appartiene all'esercizio corrente
  * 		PostCondition:
  *  			Viene inviato il messaggio "Non e' stato definito una unit� organizzativa per l'Ente.".
 */
//^^@@

public it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk findUOEnte(UserContext userContext, Integer esercizio) throws ComponentException {

	try {
		Unita_organizzativa_enteHome uo_home = (Unita_organizzativa_enteHome)getHomeCache(userContext).getHome(it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk.class);
		it.cnr.jada.persistency.sql.SQLBuilder sql = uo_home.createSQLBuilder();
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.FL_UO_CDS",sql.EQUALS, "Y");
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_INIZIO",sql.LESS_EQUALS, esercizio);
		sql.addSQLClause("AND","UNITA_ORGANIZZATIVA.ESERCIZIO_FINE",sql.GREATER_EQUALS, esercizio);				
		java.util.List uo = uo_home.fetchAll(sql);
		if (uo.isEmpty()) return null;
		return (it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk)uo.get(0);
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
private AutoFatturaComponentSession getAutofatturaComponentSession(UserContext userContext) throws ComponentException {

	try {
		return (AutoFatturaComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
							"CNRDOCAMM00_EJB_AutoFatturaComponentSession", 
							AutoFatturaComponentSession.class);
	} catch (javax.ejb.EJBException e) {
		throw handleException(e);
	}
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
private java.util.List getBeniServizioPerSconto(UserContext userContext) throws ComponentException {

	java.util.List beniServizioPerSconto = new Vector();
	try {
		Configurazione_cnrBulk config = new Configurazione_cnrBulk(
															"BENE_SERVIZIO_SPECIALE",
															"SCONTO_ABBUONO", 
															"*", 
															new Integer(0));
		it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome)getHome(userContext, config);
		List configurazioni = home.find(config);
		if (configurazioni != null)
			for (Iterator i = configurazioni.iterator(); i.hasNext();) {
				Configurazione_cnrBulk configurazione = (Configurazione_cnrBulk)i.next();
				if (configurazione.getVal01() != null && !configurazione.getVal01().trim().equals(""))
					beniServizioPerSconto.add(new Bene_servizioBulk(configurazione.getVal01()));
			}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		handleException(e);
	}

	return beniServizioPerSconto;
}
private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(
	UserContext userContext, 
	java.util.Enumeration scadenze) throws ComponentException {

	it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
	if (scadenze != null)
		while (scadenze.hasMoreElements()) {
			IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
			if (!scadenza.getFather().isTemporaneo()) {
				if (!documentiContabiliNonTemporanei.containsKey(scadenza.getFather())) {
					Vector allInstances = new java.util.Vector();
					allInstances.addElement(scadenza.getFather());
					documentiContabiliNonTemporanei.put(scadenza.getFather(), allInstances);
				} else {
					((Vector)documentiContabiliNonTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
				}
			}
		}
	return documentiContabiliNonTemporanei;
}
private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {

	it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
	if (scadenze != null)
		while (scadenze.hasMoreElements()) {
			IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
			if (scadenza.getFather().isTemporaneo()) {
				if (!documentiContabiliTemporanei.containsKey(scadenza.getFather())) {
					Vector allInstances = new java.util.Vector();
					allInstances.addElement(scadenza.getFather());
					documentiContabiliTemporanei.put(scadenza.getFather(), allInstances);
				} else {
					((Vector)documentiContabiliTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
				}
			}
		}
	return documentiContabiliTemporanei;
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
private DivisaBulk getEuro(UserContext userContext) throws ComponentException {

	String cd_euro = null;
	try {
		cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
		if (cd_euro == null)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire una fattura, immettere tale valore.");
	} catch (javax.ejb.EJBException e) {
		handleException(e);
	} catch (java.rmi.RemoteException e) {
		handleException(e);
	}

	DivisaBulk valuta = null;
	
	try {
		java.util.List divise = getHome(userContext, DivisaBulk.class).find(new it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk(cd_euro));
		if (divise == null || divise.isEmpty())
			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire una fattura, immettere tale valore.");
		valuta = (DivisaBulk)divise.get(0);
		if (valuta == null)
			throw new it.cnr.jada.comp.ApplicationException("Impossibile caricare la valuta di default! Prima di poter inserire una fattura, immettere tale valore.");
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		handleException(e);
	}
	return valuta;
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
private Consuntivo_rigaVBulk getRigaConsuntivoFor(Fattura_passiva_rigaBulk rigaFatturaPassiva) {
	
	if (rigaFatturaPassiva == null || rigaFatturaPassiva.getVoce_iva() == null)
		return null;

	java.util.Collection consuntivo = rigaFatturaPassiva.getFattura_passiva().getFattura_passiva_consuntivoColl();

	String codiceIva = rigaFatturaPassiva.getVoce_iva().getCd_voce_iva();
	Consuntivo_rigaVBulk rigaConsuntivo = null;
	// Cerco nel consuntivo corrente una riga per il codice IVA richiesto
	for (Iterator i = consuntivo.iterator(); i.hasNext();) {
		Consuntivo_rigaVBulk rigaC = (Consuntivo_rigaVBulk)i.next();
		if (codiceIva.equalsIgnoreCase(rigaC.getVoce_iva().getCd_voce_iva())) {
			rigaConsuntivo = rigaC;
			break;
		}
	}
	
	if (rigaConsuntivo == null) {
		// Non ho trovato una riga per il codice IVA richiesto
		rigaConsuntivo = new Consuntivo_rigaVBulk(rigaFatturaPassiva);
		rigaFatturaPassiva.getFattura_passiva().addToFattura_passiva_consuntivoColl(rigaConsuntivo);
	}

	return rigaConsuntivo;
}
private RiportoDocAmmComponentSession getRiportoComponentSession(
	UserContext context)
	throws ComponentException {

	try {
		return (RiportoDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
				"CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
				RiportoDocAmmComponentSession.class);
	} catch (Throwable t) {
		throw handleException(t);
	}
}
private String getStatoRiporto(
	UserContext context,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	try {
		RiportoDocAmmComponentSession session = getRiportoComponentSession(context);
		return session.getStatoRiporto(context, fatturaPassiva);
	} catch (Throwable t) {
		throw handleException(fatturaPassiva, t);
	}
}
private String getStatoRiportoInScrivania(
	UserContext context,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	try {
		RiportoDocAmmComponentSession session = getRiportoComponentSession(context);
		return session.getStatoRiportoInScrivania(context, fatturaPassiva);
	} catch (Throwable t) {
		throw handleException(fatturaPassiva, t);
	}
}

private boolean hasBolleDoganali(
	UserContext userContext,
	Fattura_passiva_IBulk fattura_passiva) 
	throws ComponentException {

	if (fattura_passiva.getFattura_estera() != null)
		return false;
		
	Fattura_passiva_IHome home = (Fattura_passiva_IHome)getHome(userContext,fattura_passiva);

	try {
		return home.selectBolleDoganaliPer(fattura_passiva).executeExistsQuery(getConnection(userContext));
	} catch (SQLException e) {
		throw handleException(fattura_passiva, e);
	}
}
//^^@@
/** 
  *	Dettagli di fattura passiva non inventariati
  *		PreCondition:
  * 		Richiesta dell'esistenza di dettagli FP non inventariati
  *   	PostCondition:
  *  		Restituisce la conferma
 */
//^^@@


public boolean hasFatturaPassivaARowNotInventoried(
	UserContext userContext,
	Fattura_passivaBulk fattura_passiva)
	throws ComponentException {

	java.util.Vector coll = new java.util.Vector();
	Iterator dettagli = fattura_passiva.getFattura_passiva_dettColl().iterator();
	if (dettagli != null) {
		while (dettagli.hasNext()) {
			Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)dettagli.next();
			if (riga.getBene_servizio() != null && riga.getBene_servizio().getFl_gestione_inventario().booleanValue() &&
				!riga.isInventariato())
				return true;
		}
	}
	return false;
}

private boolean hasFatturaPassivaARowNotStateI(
	Fattura_passivaBulk fattura_passiva)
	throws ComponentException {

	if (fattura_passiva == null) return false;

	for (Iterator i = fattura_passiva.getFattura_passiva_dettColl().iterator(); i.hasNext();)
		if (!Fattura_passiva_rigaBulk.STATO_INIZIALE.equalsIgnoreCase(((Fattura_passiva_rigaBulk)i.next()).getStato_cofi()))
			return true;
	return false;
}

private boolean hasSpedizionieri(
	UserContext userContext,
	Fattura_passiva_IBulk fattura_passiva)
	throws ComponentException {

	if (fattura_passiva.getFattura_estera() != null)
		return false;
		
	Fattura_passiva_IHome home = (Fattura_passiva_IHome)getHome(userContext,fattura_passiva);

	try {
		return home.selectSpedizionieriPer(fattura_passiva).executeExistsQuery(getConnection(userContext));
	} catch (SQLException e) {
		throw handleException(fattura_passiva, e);
	}
}
public Boolean ha_beniColl(UserContext userContext,OggettoBulk fattura) throws ComponentException
{	
	SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
	if (fattura instanceof Nota_di_creditoBulk){
		Nota_di_creditoBulk fattura_passiva = (Nota_di_creditoBulk)fattura;
		// nella view vengono valorizzati gli stessi campi
		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,fattura_passiva.getCd_cds());
		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());			
		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);
	}
	else if (fattura instanceof Nota_di_credito_rigaBulk){
		Nota_di_credito_rigaBulk fattura_passiva = (Nota_di_credito_rigaBulk)fattura;
		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,fattura_passiva.getCd_cds());
		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());			
		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		sql.addSQLClause("AND","progressivo_riga_fatt_pass",sql.EQUALS,fattura_passiva.getProgressivo_riga());
		sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);
	}else if(fattura instanceof Nota_di_debitoBulk){
		Nota_di_debitoBulk fattura_passiva = (Nota_di_debitoBulk)fattura;
		// nella view vengono valorizzati gli stessi campi
		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,fattura_passiva.getCd_cds());
		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());			
		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);
	}
	else if (fattura instanceof Nota_di_debito_rigaBulk){
		Nota_di_debito_rigaBulk fattura_passiva = (Nota_di_debito_rigaBulk)fattura;
		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,fattura_passiva.getCd_cds());
		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, fattura_passiva.getCd_unita_organizzativa());			
		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		sql.addSQLClause("AND","progressivo_riga_fatt_pass",sql.EQUALS,fattura_passiva.getProgressivo_riga());
		sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);
	}else
	{
	sql.addClause( "AND", "cd_cds_fatt_pass", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds());
	sql.addClause( "AND", "cd_uo_fatt_pass", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_unita_organizzativa());			
	sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);	
	if (fattura instanceof Fattura_passivaBulk){
		Fattura_passivaBulk fattura_passiva = (Fattura_passivaBulk)fattura;
		sql.addSQLClause( "AND", "esercizio_fatt_pass", sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva", sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		}
	else{
		Fattura_passiva_rigaBulk fattura_passiva = (Fattura_passiva_rigaBulk)fattura;
		sql.addSQLClause( "AND", "esercizio_fatt_pass", sql.EQUALS,fattura_passiva.getEsercizio());
		sql.addSQLClause("AND","pg_fattura_passiva", sql.EQUALS,fattura_passiva.getPg_fattura_passiva());
		sql.addSQLClause("AND","progressivo_riga_fatt_pass", sql.EQUALS,fattura_passiva.getProgressivo_riga());
		}
	}
		try 
		{
		  if (sql.executeCountQuery(getConnection(userContext))>0)							
			return Boolean.TRUE;						 
		  else
			return Boolean.FALSE;	
			} 
		catch (java.sql.SQLException e) 
		{
			   throw handleSQLException(e);				
		}
	}	
//^^@@
/** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */
//^^@@

public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

	Fattura_passivaBulk fattura = (Fattura_passivaBulk)bulk;

	try {
		Unita_organizzativa_enteBulk ente = findUOEnte(userContext, fattura.getEsercizio());
		if (ente != null && ente.getCd_unita_organizzativa().equalsIgnoreCase(fattura.getCd_unita_organizzativa()))
			throw new it.cnr.jada.comp.ApplicationException("Non � possibile emettere fatture passive per l'Ente!");
		
		Fattura_passivaHome fHome = (Fattura_passivaHome)getHome(userContext, fattura);
		if (!verificaStatoEsercizio(
							userContext, 
							new EsercizioBulk( 
											fattura.getCd_cds(), 
											fattura.getEsercizio()))) //equivale a quello di scrivania!!
			throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire una fattura passiva per un esercizio non aperto!");
		java.sql.Timestamp date = fHome.getServerDate();
		int annoSolare = fattura.getDateCalendar(date).get(java.util.Calendar.YEAR);
		if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue())
			throw new it.cnr.jada.comp.ApplicationException("Non � possibile inserire " + fattura.getDescrizioneEntitaPlurale() + " in esercizi non corrispondenti all'anno solare!");
		fattura.setDt_registrazione(date);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fattura, e);
	}
	
	fattura.resetDefferredSaldi();
	
	fattura.setStato_cofi(fattura.STATO_INIZIALE);
	fattura.setStato_coge(fattura.NON_REGISTRATO_IN_COGE);
	fattura.setStato_coan(fattura.NON_CONTABILIZZATO_IN_COAN);
	fattura.setStato_pagamento_fondo_eco(fattura.NO_FONDO_ECO);
	fattura.setTi_associato_manrev(fattura.NON_ASSOCIATO_A_MANDATO);
	fattura.setStato_liquidazione(fattura.SOSP);
	fattura.setCausale(fattura.ATTLIQ);
	fattura.setIm_totale_fattura(new java.math.BigDecimal(0));
	fattura.setIm_importo_totale_fattura_fornitore_euro(new java.math.BigDecimal(0));
	fattura.setFl_fattura_compenso(Boolean.FALSE);
	
	fattura.setDt_da_competenza_coge(fattura.getDt_registrazione());
	fattura.setDt_a_competenza_coge(fattura.getDt_registrazione());
	setDt_termine_creazione_docamm(userContext, fattura);
	/**
	  * Gennaro Borriello - (08/11/2004 13.35.27)
	  *	Aggiunta propriet� <code>esercizioInScrivania</code>, che verr� utilizzata
	  *	per la gestione di isRiportataInScrivania(), in alcuni casi.
	 */
	fattura.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	 
	
	//Come da richiesta 101 gestione errori CNR imposto come default 
	//istituzionale (09/09/2002 RP)
	//Originale: fattura.setTi_istituz_commerc(fattura.COMMERCIALE);
	fattura.setTi_istituz_commerc(fattura.ISTITUZIONALE);

	fattura = setChangeDataToEur(userContext, fattura);
	
	fattura = (Fattura_passivaBulk)super.inizializzaBulkPerInserimento(userContext, fattura);
	completeWithCondizioneConsegna(userContext,fattura);
	completeWithModalitaTrasporto(userContext,fattura);
	completeWithModalitaIncasso(userContext,fattura);
	completeWithModalitaErogazione(userContext,fattura);
	TerzoBulk tb = new TerzoBulk();
	tb.setAnagrafico(new AnagraficoBulk());
	fattura.setFornitore(tb);
	java.util.Collection coll = fattura.getSezionali();
	fattura.setTipo_sezionale(
		(coll != null && !coll.isEmpty()) ?
			(it.cnr.contab.docamm00.tabrif.bulk.Tipo_sezionaleBulk)coll.iterator().next():
			null);
	fattura = caricaAllegatiBulk(userContext, fattura);

	
	fattura = valorizzaInfoDocEle(userContext,fattura);
	fattura.setDataInizioSplitPayment(getDataInizioSplitPayment(userContext));  
	
	return fattura;
}
public Fattura_passivaBulk caricaAllegatiBulk (UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException {
	if (fattura.getDocumentoEleTestata() != null) {
		try {
			fattura.setDocEleAllegatiColl(new BulkList<DocumentoEleAllegatiBulk>(
					getHome(userContext, DocumentoEleAllegatiBulk.class).find(
							new DocumentoEleAllegatiBulk(fattura.getDocumentoEleTestata()))));
		} catch (PersistencyException e) {
			throw handleException(e);
		}	
	}
	return fattura;	
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@

public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException {

	if (bulk == null)
		throw new ComponentException("Attenzione: non esiste alcuna fattura corrispondente ai criteri di ricerca!");

	Fattura_passivaBulk fattura_passiva = (Fattura_passivaBulk)bulk;

	if (fattura_passiva.getEsercizio() == null)
		throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non � valorizzato! Impossibile proseguire.");

	int esScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue();
	if (fattura_passiva.getEsercizio().intValue() > esScrivania)
		throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");

	fattura_passiva = (Fattura_passivaBulk)super.inizializzaBulkPerModifica(aUC, fattura_passiva);
	try {
		lockBulk(aUC, fattura_passiva);
	} catch (Throwable t) {
		throw handleException(t);
	}
	fattura_passiva.setHa_beniColl(ha_beniColl(aUC,fattura_passiva));
	fattura_passiva.setChangeOperationOn(fattura_passiva.getValuta());
	fattura_passiva.resetDefferredSaldi();
	
	try {
		BulkList dettagli = new BulkList(findDettagli(aUC, fattura_passiva));
		fattura_passiva.setFattura_passiva_dettColl(dettagli);

		completeWithCondizioneConsegna(aUC, fattura_passiva);
		completeWithModalitaTrasporto(aUC, fattura_passiva);
		completeWithModalitaIncasso(aUC, fattura_passiva);
		completeWithModalitaErogazione(aUC, fattura_passiva);
		BulkList dettagliIntrastat = new BulkList(findDettagliIntrastat(aUC, fattura_passiva));
		if (dettagliIntrastat != null && !dettagliIntrastat.isEmpty())
			for (Iterator i = dettagliIntrastat.iterator(); i.hasNext();) {
				Fattura_passiva_intraBulk dettaglio = (Fattura_passiva_intraBulk)i.next();
				dettaglio.setFattura_passiva(fattura_passiva);
				dettaglio.setCondizione_consegnaColl(fattura_passiva.getCondizione_consegnaColl());
				dettaglio.setModalita_trasportoColl(fattura_passiva.getModalita_trasportoColl());
				dettaglio.setModalita_incassoColl(fattura_passiva.getModalita_incassoColl());
				dettaglio.setModalita_erogazioneColl(fattura_passiva.getModalita_erogazioneColl());
			}
		fattura_passiva.setFattura_passiva_intrastatColl(dettagliIntrastat);


		getHomeCache(aUC).fetchAll(aUC);
		int dettagliRiportati = 0;
		for (Iterator i = dettagli.iterator(); i.hasNext();) {
			Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
			if (dettaglio.getBene_servizio().getFl_gestione_inventario().booleanValue()){
				dettaglio.setInventariato(true); 
			 	 if (ha_beniColl(aUC,dettaglio).booleanValue())			   
					dettaglio.setInventariato(true);
				else
					dettaglio.setInventariato(false);
			}else	 
				dettaglio.setInventariato(false);
			if (dettaglio.checkIfRiportata()) {
				dettaglio.setRiportata(dettaglio.RIPORTATO);
				fattura_passiva.setRiportata(fattura_passiva.PARZIALMENTE_RIPORTATO);
				dettagliRiportati++;
			}
			dettaglio.setTermini(findTermini(aUC, dettaglio));
			dettaglio.setModalita(findModalita(aUC, dettaglio));
			dettaglio.setTrovato(ricercaDatiTrovato(aUC, dettaglio.getPg_trovato()));
		}
		fattura_passiva.setRiportata(getStatoRiporto(aUC, fattura_passiva));
		
		/**
		  * Gennaro Borriello - (02/11/2004 15.04.39)
		  *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
		 */
		fattura_passiva.setRiportataInScrivania(getStatoRiportoInScrivania(aUC, fattura_passiva));

		/**
		  * Gennaro Borriello - (08/11/2004 13.35.27)
		  *	Aggiunta propriet� <code>esercizioInScrivania</code>, che verr� utilizzata
		  *	per la gestione di isRiportataInScrivania(), in alcuni casi.
		 */
		fattura_passiva.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
		 
		calcoloConsuntivi(aUC, fattura_passiva);
		rebuildObbligazioni(aUC, fattura_passiva);
		if (fattura_passiva instanceof Nota_di_creditoBulk)
			rebuildAccertamenti(aUC, (Nota_di_creditoBulk)fattura_passiva);
	
		Fattura_passiva_rigaBulk riga=null;
		for (java.util.Iterator i = fattura_passiva.getFattura_passiva_dettColl().iterator();i.hasNext();){	
			riga=(Fattura_passiva_rigaBulk)i.next();
			impostaCollegamentoCapitoloPerTrovato(aUC, riga);
			riga.setTrovato(ricercaDatiTrovato(aUC, riga.getPg_trovato()));
		}
		fattura_passiva = caricaAllegatiBulk(aUC, fattura_passiva);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fattura_passiva, e);
	} catch (RemoteException e) {
		throw handleException(fattura_passiva, e);
	} catch (it.cnr.jada.persistency.IntrospectionException e) {
		throw handleException(fattura_passiva, e);
	}

	caricaAutofattura(aUC, fattura_passiva);
	if (fattura_passiva.getValuta().getCd_divisa().equals(getEuro(aUC).getCd_divisa()))
		fattura_passiva.setDefaultValuta(true);
	if (fattura_passiva.getLettera_pagamento_estero() != null) {
		Lettera_pagam_esteroBulk lettera = fattura_passiva.getLettera_pagamento_estero();
		lettera.setAnnoDiCompetenza(esScrivania == lettera.getEsercizio().intValue());
	}
	
	fattura_passiva = valorizzaInfoDocEle(aUC,fattura_passiva);
	fattura_passiva.setDataInizioSplitPayment(getDataInizioSplitPayment(aUC));
	
	if(fattura_passiva.isElettronica()){
		try{
			DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) fattura_passiva.getDocumentoEleTestata();
	
			documentoEleTestata.setDocEleLineaColl(new BulkList<DocumentoEleLineaBulk>(
					getHome(aUC, DocumentoEleLineaBulk.class).find(new DocumentoEleLineaBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleIVAColl(new BulkList<DocumentoEleIvaBulk>(
					getHome(aUC, DocumentoEleIvaBulk.class).find(new DocumentoEleIvaBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleAllegatiColl(new BulkList<DocumentoEleAllegatiBulk>(
					getHome(aUC, DocumentoEleAllegatiBulk.class).find(new DocumentoEleAllegatiBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleTributiColl(new BulkList<DocumentoEleTributiBulk>(
					getHome(aUC, DocumentoEleTributiBulk.class).find(new DocumentoEleTributiBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleScontoMaggColl(new BulkList<DocumentoEleScontoMaggBulk>(
					getHome(aUC, DocumentoEleScontoMaggBulk.class).find(new DocumentoEleScontoMaggBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleAcquistoColl(new BulkList<DocumentoEleAcquistoBulk>(
					getHome(aUC, DocumentoEleAcquistoBulk.class).find(new DocumentoEleAcquistoBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleDdtColl(new BulkList<DocumentoEleDdtBulk>(
					getHome(aUC, DocumentoEleDdtBulk.class).find(new DocumentoEleDdtBulk(documentoEleTestata))));
			getHomeCache(aUC).fetchAll(aUC);	 
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}	
	return fattura_passiva;
}
private void impostaCollegamentoCapitoloPerTrovato(UserContext aUC,
		Fattura_passiva_rigaBulk riga) throws ComponentException {
	if (riga.getObbligazione_scadenziario() != null && riga.getObbligazione_scadenziario().getPg_obbligazione() != null){
		riga.setCollegatoCapitoloPerTrovato(riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().isVocePerTrovati());
	} else {
		if (riga instanceof Nota_di_credito_rigaBulk){
			Nota_di_credito_rigaBulk rigaNc = (Nota_di_credito_rigaBulk)riga;
			if (rigaNc.getAccertamento_scadenzario() != null && rigaNc.getAccertamento_scadenzario().getPg_accertamento() != null){
				riga.setCollegatoCapitoloPerTrovato(isVocePerTrovati(aUC, rigaNc.getAccertamento_scadenzario()));
			}
		}
	}
}

private boolean isVocePerTrovati(UserContext aUC, Accertamento_scadenzarioBulk scadenza) throws ComponentException {

	if (scadenza.getAccertamento()==null)
		return false;
	
	Elemento_voceHome evHome=(Elemento_voceHome)getHome(aUC,Elemento_voceBulk.class);
	SQLBuilder sql= evHome.createSQLBuilder();
	
	sql.addSQLClause("AND","esercizio",SQLBuilder.EQUALS,scadenza.getAccertamento().getEsercizio());
	sql.addSQLClause("AND","ti_appartenenza",SQLBuilder.EQUALS,scadenza.getAccertamento().getTi_appartenenza());
	sql.addSQLClause("AND","ti_gestione",SQLBuilder.EQUALS,scadenza.getAccertamento().getTi_gestione());
	sql.addSQLClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,scadenza.getAccertamento().getCd_elemento_voce());
	sql.addSQLClause("AND","fl_trovato",SQLBuilder.NOT_EQUALS,"N");

	try {
		List voce=evHome.fetchAll(sql);
		if (voce.isEmpty())
			return false;
	} catch (PersistencyException e) {
		throw handleException(e);
	}		
	return true;
}

//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@
public OggettoBulk inizializzaBulkPerRicercaLibera (UserContext userContext, OggettoBulk bulk) throws ComponentException {

	if (bulk == null)
		throw new ComponentException("Attenzione: non esiste alcuna fattura corrispondente ai criteri di ricerca!");

	Fattura_passivaBulk fp = (Fattura_passivaBulk)super.inizializzaBulkPerRicercaLibera(userContext, bulk);

	TerzoBulk fornitore = new TerzoBulk();
	fornitore.setAnagrafico(new AnagraficoBulk());
	fp.setFornitore(fornitore);
	fp.setFl_autofattura(null);
	fp.setFl_merce_extra_ue(null);
	fp.setFl_merce_intra_ue(null);
	fp.setFl_fattura_compenso(null);
	fp.setFl_liquidazione_differita(null);
	try {
		fp.setSezionali(findSezionali(userContext, fp));
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fp, e);
	}
	fp.setSezionaliFlag(null);

	if (fp instanceof Fattura_passiva_IBulk) {
		((Fattura_passiva_IBulk)fp).setFattura_estera(new Fattura_passiva_IBulk(fp.getCd_cds(), fp.getCd_unita_organizzativa(), fp.getEsercizio(), null));
		fp.setFl_bolla_doganale(null);
		fp.setFl_spedizioniere(null);
	}
	
	return fp;
}
//^^@@
/** 
  *  Validazione riga.
  *		PreCondition:
  *    		Viene richiesto l'inserimento di un dettaglio in documento passivo e il dettaglio non ha superato il metodo 'validaRiga'
  *		PostCondition:
  *			Obbligo di modifica o annullamento riga.
  *  Tutti i controlli superati.
  *		PreCondition:
  *			Viene richiesto l'inserimento di un dettaglio in documento passivo e il dettaglio ha superato il metodo 'validaRiga'
  *		PostCondition:
  *			Inserisce il dettaglio e consente il passaggio alla riga seguente.
 */
//^^@@

public void inserisciRiga (UserContext aUC,Fattura_passiva_rigaBulk rigaFattura) throws ComponentException {
	
	validaRiga(aUC, rigaFattura);
}
//^^@@
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto se il dettaglio in argomento � un bene di tipo sconto/abbuono.
  *    PostCondition:
  *      Vengono caricati tutti i beni sconto e confrontati con "dettaglio". Risponde
  *		 true se il dettaglio � contenuto nella lista
 */
//^^@@

public boolean isBeneServizioPerSconto(
	UserContext userContext,
	Fattura_passiva_rigaBulk dettaglio)
	throws ComponentException {

	if (dettaglio == null ||
		dettaglio.getBene_servizio() == null ||
		!(dettaglio instanceof Fattura_passiva_rigaIBulk))
		return false;
		
	List beniServizioPerSconto = getBeniServizioPerSconto(userContext);
	return (beniServizioPerSconto != null &&
			it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(
															beniServizioPerSconto, 
															dettaglio.getBene_servizio()));
}
/**
  *	Controllo l'esercizio coep
  *
  * Nome: Controllo chiusura esercizio
  * Pre:  E' stata richiesta la creazione o modifica di una scrittura
  * Post: Viene chiamata una stored procedure che restituisce 
  *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
  *		  -		'N' altrimenti
  *		  Se l'esercizio e' chiuso e' impossibile proseguire
  *
  * @param  userContext <code>UserContext</code>
  
  * @return boolean : TRUE se stato = C
  *					  FALSE altrimenti
  */
private boolean isEsercizioCoepChiusoFor(UserContext userContext, Fattura_passivaBulk documento, Integer esercizio) throws ComponentException
{
	LoggableStatement cs = null;	
	String status = null;

	try
	{
		cs = new LoggableStatement(getConnection(userContext),
				"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
				+	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());	

		cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
		cs.setObject( 2, esercizio);		
		cs.setObject( 3, documento.getCd_cds());		
		
		cs.executeQuery();

		status = new String(cs.getString(1));

		if(status.compareTo("Y")==0)
			return true;
	    	
	} catch (java.sql.SQLException ex) {
		throw handleException(ex);
	}
	
	return false;		    	
}
private void liberaSospeso(UserContext userContext,SospesoBulk sospeso) throws ComponentException {

	try {
		if (sospeso != null) {
			SospesoHome sospesoHome = (SospesoHome)getHome(userContext, SospesoBulk.class);
			sospeso.setIm_ass_mod_1210(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
			sospeso.setToBeUpdated();
			sospesoHome.aggiornaImportoAssociatoMod1210(sospeso);
		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(sospeso,e);
	} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
		throw handleException(sospeso,e);
	} catch (it.cnr.jada.bulk.BusyResourceException e) {
		throw handleException(sospeso,e);
	}

}
private Fattura_passivaBulk manageDeletedElements(
	UserContext userContext, 
	Fattura_passivaBulk fatturaPassiva,
	OptionRequestParameter status)
	throws ComponentException {

	if (fatturaPassiva != null) {
		manageDocumentiContabiliCancellati(userContext, fatturaPassiva, status);
		if (fatturaPassiva.getDettagliCancellati() != null &&
			!fatturaPassiva.getDettagliCancellati().isEmpty())
			for (Iterator i = fatturaPassiva.getDettagliCancellati().iterator(); i.hasNext();) {
				Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
				if (dettaglio.isInventariato())
					deleteAssociazioniInventarioWith(userContext, dettaglio);
			}
	}
	return fatturaPassiva;
}
private void manageDocumentiContabiliCancellati(
	UserContext userContext, 
	Fattura_passivaBulk fatturaPassiva,
	OptionRequestParameter status)
	throws ComponentException {

	if (fatturaPassiva != null) {
		if (fatturaPassiva.getDocumentiContabiliCancellati() != null &&
			!fatturaPassiva.getDocumentiContabiliCancellati().isEmpty()) {

			if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
				PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
																		userContext,
																		fatturaPassiva.getFattura_passiva_obbligazioniHash().keys());
				Vector scadenzeConfermate = new Vector();
				java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
				while (e.hasMoreElements()) {
					OggettoBulk obj = (OggettoBulk)e.nextElement();
					if (obj instanceof ObbligazioneBulk)
						scadenzeConfermate.add(obj);
				}
				aggiornaObbligazioniSuCancellazione(
					userContext,
					fatturaPassiva,
					fatturaPassiva.getDocumentiContabiliCancellati().elements(),
					scadenzeConfermate,
					status);
			} else if (fatturaPassiva instanceof Nota_di_creditoBulk && ((Nota_di_creditoBulk)fatturaPassiva).getAccertamentiHash() != null) {
				PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
														userContext,
														((Nota_di_creditoBulk)fatturaPassiva).getAccertamentiHash().keys());
				Vector scadenzeConfermate = new Vector();
				java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
				while (e.hasMoreElements()) {
					OggettoBulk obj = (OggettoBulk)e.nextElement();
					if (obj instanceof AccertamentoBulk)
						scadenzeConfermate.add(obj);
				}

				aggiornaAccertamentiSuCancellazione(
					userContext,
					(Nota_di_creditoBulk) fatturaPassiva,
					fatturaPassiva.getDocumentiContabiliCancellati().elements(),
					scadenzeConfermate,
					status);
			}
		}
	}
}
//^^@@
/** 
  *  Non passa validazione di business
  *    PreCondition:
  *      L'OggettoBulk non passa i criteri di validit� di business per l'operazione di modifica
  *    PostCondition:
  *      Viene generata una ComponentException con detail la ValidationException che descrive l'errore di validazione.
  *  Oggetto non trovato
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
  *  Oggetto scaduto
  *    PreCondition:
  *      L'OggettoBulk specificato � stato modificato da altri utenti dopo la lettura
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
  *  Oggetto occupato
  *    PreCondition:
  *      L'OggettoBulk specificato � bloccato da qualche altro utente.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore
 */
//^^@@

public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk bulk) throws ComponentException {

	return modificaConBulk(aUC, bulk, null);
}
//^^@@
/** 
  *   Validazione documento.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso non ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Non  viene consentita la registrazione della fattura.
  *   Tutti i controlli superati.
  *	PreCondition:
  *		Viene richiesta la creazione di un documento passivo e lo stesso ha superato il metodo 'validaFattura'.
  *	PostCondition:
  *		Viene consentita la registrazione del documento.
 */
//^^@@

public OggettoBulk modificaConBulk(
	UserContext aUC,
	OggettoBulk bulk,
	OptionRequestParameter status)
	throws ComponentException {

			
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bulk;
	if(fatturaPassiva.isElettronica()) 
		validaFatturaElettronica(aUC, fatturaPassiva);
	try {
		if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
			//if (fatturaPassiva.existARowToBeInventoried()) {
			if (fatturaPassiva.existARowToBeInventoried() && (fatturaPassiva.getStato_liquidazione()==null || fatturaPassiva.getStato_liquidazione().compareTo(Fattura_passiva_IBulk.LIQ)==0)) {
				if (hasFatturaPassivaARowNotInventoried(aUC, fatturaPassiva))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: � necessario inventariare tutti i dettagli.");
				else{
					if ((fatturaPassiva.getCarichiInventarioHash()!=null) ||((fatturaPassiva.getAssociazioniInventarioHash()!=null)&& verificaEsistenzaAumentiValori(fatturaPassiva)))
						verificaEsistenzaEdAperturaInventario(aUC, fatturaPassiva);
				}
			}
		}	
		validaFattura(aUC, fatturaPassiva);
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw handleException(bulk, e);
	}

    // Salvo temporaneamente l'hash map dei saldi
    PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
    if (fatturaPassiva.getDefferredSaldi() != null)
	    aTempDiffSaldi=(PrimaryKeyHashMap)fatturaPassiva.getDefferredSaldi().clone();    

    manageDeletedElements(aUC, fatturaPassiva, status);
	
	aggiornaObbligazioni(aUC, fatturaPassiva, status);
	
	if (bulk instanceof Nota_di_creditoBulk) {
		Nota_di_creditoBulk ndc = (Nota_di_creditoBulk)fatturaPassiva;
		aggiornaRigheFatturaPassivaDiOrigine(aUC, ndc);
		aggiornaAccertamenti(aUC, ndc, status);
	}
	if (bulk instanceof Nota_di_debitoBulk) {
		Nota_di_debitoBulk ndd = (Nota_di_debitoBulk)fatturaPassiva;
		aggiornaRigheFatturaPassivaDiOrigine(aUC, ndd);
	}

	prepareCarichiInventario(aUC, fatturaPassiva);
	
	Lettera_pagam_esteroBulk lettera = fatturaPassiva.getLettera_pagamento_estero();
	if (lettera != null) {
		try {
			Lettera_pagam_esteroBulk original1210 = (Lettera_pagam_esteroBulk)getHome(aUC, lettera).findByPrimaryKey(lettera);
			aggiornaLetteraPagamentoEstero(aUC, lettera);
			if ((original1210 == null ||
				lettera.getIm_pagamento().compareTo(original1210.getIm_pagamento()) != 0) && lettera.getCd_sospeso() != null)
				validaDisponibilitaDiCassaCDS(aUC, fatturaPassiva);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(lettera,e);
		}
	}

	//Nel caso in cui TUTTI i dettagli sono stati pagati imposto lo stato pagato
	//in testata fattura. (Caso in cui era parzialmente pagata � ho eliminato tutti
	//i dettagli non pagati)
	if (fatturaPassiva.isPagataParzialmente() &&
		fatturaPassiva.getDettagliPagati().size() == fatturaPassiva.getFattura_passiva_dettColl().size()) {
		
		fatturaPassiva.setStato_cofi(fatturaPassiva.STATO_PAGATO);
		fatturaPassiva.setTi_associato_manrev(fatturaPassiva.ASSOCIATO_A_MANDATO);
	}

	//Aggiornamenti degli stati COGE e COAN
	boolean aggiornaStatoCoge = false;
	try {
		Fattura_passivaBulk fatturaPassivaDB = (Fattura_passivaBulk)getTempHome(aUC, Fattura_passivaBulk.class).findByPrimaryKey(		                        
				new Documento_amministrativo_passivoBulk(
                fatturaPassiva.getCd_cds(),
                fatturaPassiva.getCd_unita_organizzativa(),
                fatturaPassiva.getEsercizio(),
                fatturaPassiva.getPg_fattura_passiva()
                ));
		if(fatturaPassiva.getDt_scadenza() == null && fatturaPassivaDB.getDt_scadenza()!=null)
			 throw new it.cnr.jada.comp.ApplicationException("La data di scadenza non pu� essere nulla!");
				
		if (!Utility.equalsNull(fatturaPassiva.getTi_fattura(),fatturaPassivaDB.getTi_fattura())||
			!Utility.equalsNull(fatturaPassiva.getFl_congelata(),fatturaPassivaDB.getFl_congelata())||
			!Utility.equalsNull(fatturaPassiva.getFl_intra_ue(),fatturaPassivaDB.getFl_intra_ue())||
			!Utility.equalsNull(fatturaPassiva.getFl_extra_ue(),fatturaPassivaDB.getFl_extra_ue())||
			!Utility.equalsNull(fatturaPassiva.getFl_san_marino_con_iva(),fatturaPassivaDB.getFl_san_marino_con_iva())||
			!Utility.equalsNull(fatturaPassiva.getFl_san_marino_senza_iva(),fatturaPassivaDB.getFl_san_marino_senza_iva())||
			!Utility.equalsNull(fatturaPassiva.getStato_pagamento_fondo_eco(),fatturaPassivaDB.getStato_pagamento_fondo_eco())||
			!Utility.equalsNull(fatturaPassiva.getTi_bene_servizio(),fatturaPassivaDB.getTi_bene_servizio())||
			!Utility.equalsNull(fatturaPassiva.getEsercizio_lettera(),fatturaPassivaDB.getEsercizio_lettera())||
			!Utility.equalsNull(fatturaPassiva.getPg_lettera(),fatturaPassivaDB.getPg_lettera())||
			!Utility.equalsNull(fatturaPassiva.getCd_terzo(),fatturaPassivaDB.getCd_terzo())
		)
			aggiornaStatoCoge = true;
		if (!aggiornaStatoCoge && fatturaPassiva.getFattura_passiva_dettColl().size() > 0){
			for (Iterator iter = fatturaPassiva.getFattura_passiva_dettColl().iterator(); iter.hasNext() && !aggiornaStatoCoge;) {
				Fattura_passiva_rigaBulk fattura_riga = (Fattura_passiva_rigaBulk) iter.next();
				Fattura_passiva_rigaBulk fatturaDB_riga = caricaRigaDB(aUC,fattura_riga);
				if (fatturaDB_riga != null){
					if (!Utility.equalsNull(fattura_riga.getIm_totale_divisa(),fatturaDB_riga.getIm_totale_divisa().setScale(2))||
						!Utility.equalsNull(fattura_riga.getIm_imponibile(),fatturaDB_riga.getIm_imponibile().setScale(2))||
						!Utility.equalsNull(fattura_riga.getIm_iva(),fatturaDB_riga.getIm_iva().setScale(2))||
						(fatturaDB_riga.getIm_diponibile_nc()!=null  &&
						!Utility.equalsNull(fattura_riga.getIm_diponibile_nc(),fatturaDB_riga.getIm_diponibile_nc().setScale(2)))||
						!Utility.equalsNull(fattura_riga.getTi_istituz_commerc(),fatturaDB_riga.getTi_istituz_commerc())||
						!Utility.equalsNull(fattura_riga.getCd_bene_servizio(),fatturaDB_riga.getCd_bene_servizio())||
						!Utility.equalsBulkNull(fattura_riga.getObbligazione_scadenziario(),fatturaDB_riga.getObbligazione_scadenziario())||
						!Utility.equalsNull(fattura_riga.getDt_da_competenza_coge(),fatturaDB_riga.getDt_da_competenza_coge())||
						!Utility.equalsNull(fattura_riga.getDt_a_competenza_coge(),fatturaDB_riga.getDt_a_competenza_coge())
					)
						aggiornaStatoCoge = true;
				}					
			}
		}
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	if (aggiornaStatoCoge){
		if (!(!fatturaPassiva.isDocumentoModificabile() && fatturaPassiva.isDetailDoubled())) {
			//Aggiornamenti degli stati COGE e COAN
			if (fatturaPassiva.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(fatturaPassiva.getStato_coan())) {
				fatturaPassiva.setStato_coan(fatturaPassiva.DA_RICONTABILIZZARE_IN_COAN);
				fatturaPassiva.setToBeUpdated();
			}
			if (fatturaPassiva.REGISTRATO_IN_COGE.equalsIgnoreCase(fatturaPassiva.getStato_coge())) {
				fatturaPassiva.setStato_coge(fatturaPassiva.DA_RICONTABILIZZARE_IN_COGE);
				fatturaPassiva.setToBeUpdated();
			}
		}
	}
	
	fatturaPassiva = (Fattura_passivaBulk)super.modificaConBulk(aUC, fatturaPassiva);

	if (fatturaPassiva.getDocumentoEleTestata() != null && fatturaPassiva.getDocumentoEleTestata().getIdentificativoSdi() != null) {
		try {
			if (Utility.createParametriEnteComponentSession().getParametriEnte(aUC).getTipo_db().equals(Parametri_enteBulk.DB_PRODUZIONE))
				aggiornaMetadatiDocumentale(fatturaPassiva);
		} catch (RemoteException | EJBException e) {
			throw handleException(e);
		}
	}
	
	aggiornaCarichiInventario(aUC, fatturaPassiva);
	
	// Le operazioni che rendono persistenti le modifiche fatte sull'Inventario,
	//	potrebbero rimandare un messaggio all'utente.
	String messaggio = null;
	messaggio = aggiornaAssociazioniInventario(aUC, fatturaPassiva);
	
	aggiornaAutofattura(aUC, fatturaPassiva);
	
	
	// Restore dell'hash map dei saldi
    if (fatturaPassiva.getDefferredSaldi() != null)
		fatturaPassiva.getDefferredSaldi().putAll(aTempDiffSaldi);
	aggiornaCogeCoanDocAmm(aUC, fatturaPassiva);

	/*
	 * Se il documento non era modificabile nei suoi elementi principali, ma si � solo proceduto a 
	 * sdoppiare una riga di dettaglio allora il controllo sulla chiusura dell'esercizio del documento
	 * non � necessario
	 */
	if (!fatturaPassiva.isDocumentoModificabile() && fatturaPassiva.isDetailDoubled())
		return fatturaPassiva;
	
	aggiornaDataEsigibilitaIVA(aUC, fatturaPassiva, "M");
	
	try {
        if (!verificaStatoEsercizio(
	        					aUC, 
	        					new EsercizioBulk( 
		        							fatturaPassiva.getCd_cds(), 
		        							((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio())))
            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
	} catch (it.cnr.jada.comp.ApplicationException e) {
		throw handleException(bulk, e);
	}
	controllaQuadraturaInventario(aUC,fatturaPassiva);
	if (messaggio != null)
		return asMTU(fatturaPassiva, messaggio);
		
	return fatturaPassiva;
}
private Fattura_passiva_rigaBulk caricaRigaDB(UserContext userContext,Fattura_passiva_rigaBulk fatturaPassivaRiga) throws ComponentException, PersistencyException{
	Fattura_passiva_rigaIHome home = (Fattura_passiva_rigaIHome)getTempHome(userContext, Fattura_passiva_rigaIBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
	sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_cds());
	sql.addClause("AND", "cd_unita_organizzativa", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_unita_organizzativa());
	sql.addClause("AND", "esercizio", SQLBuilder.EQUALS, fatturaPassivaRiga.getEsercizio());
	sql.addClause("AND", "pg_fattura_passiva", SQLBuilder.EQUALS, fatturaPassivaRiga.getPg_fattura_passiva());
	sql.addClause("AND", "progressivo_riga", SQLBuilder.EQUALS, fatturaPassivaRiga.getProgressivo_riga());
    SQLBroker broker = home.createBroker(sql);
    if (broker.next())
    	return (Fattura_passiva_rigaBulk)broker.fetch(Fattura_passiva_rigaIBulk.class);
	return null;
}
private void prepareCarichiInventario(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {

	if (fattura_passiva != null) {
		CarichiInventarioTable carichiInventarioHash = fattura_passiva.getCarichiInventarioHash();
		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
				if (buonoCS instanceof Buono_carico_scaricoBulk){// && buonoCS.isByFattura()) {
					it.cnr.jada.bulk.PrimaryKeyHashtable ht = buonoCS.getDettagliRigheHash();
					if (ht != null && !ht.isEmpty()) {
						it.cnr.jada.bulk.PrimaryKeyHashtable newHt = new it.cnr.jada.bulk.PrimaryKeyHashtable();
						for (java.util.Enumeration k = ht.keys(); k.hasMoreElements();)
							((Fattura_passiva_rigaBulk)k.nextElement()).setFattura_passiva(fattura_passiva);
						ht = new it.cnr.jada.bulk.PrimaryKeyHashtable(ht);
						for (Iterator i = fattura_passiva.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
							Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();
							BulkList bl = (BulkList)ht.get(riga);
							if (bl != null)
								newHt.put(riga, bl);
						}
						buonoCS.setDettagliRigheHash(newHt);
					}
				}
			}
		}
	}
}

/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesta la visualizzazione del consuntivo fattura.
  *    PostCondition:
  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
 */
public void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException {}
private void rebuildAccertamenti(
	UserContext userContext, 
	Nota_di_creditoBulk notaDiCredito)
	throws ComponentException {

	if (notaDiCredito == null) return;
	
	BulkList righeNdC = notaDiCredito.getFattura_passiva_dettColl();
	if (righeNdC != null) {
		for (Iterator i = righeNdC.iterator(); i.hasNext(); ) {
			Nota_di_credito_rigaBulk riga = (Nota_di_credito_rigaBulk)i.next();
			Accertamento_scadenzarioBulk scadenza = riga.getAccertamento_scadenzario();
			if (scadenza != null) {
				if (notaDiCredito.getAccertamentiHash() == null ||
					notaDiCredito.getAccertamentiHash().getKey(scadenza) == null) {
					scadenza = caricaScadenzaAccertamentoPer(userContext, scadenza);
				}
				notaDiCredito.addToAccertamenti_scadenzarioHash(scadenza, riga);
			}
		}
	}
	if (notaDiCredito.getAccertamenti_scadenzarioHash() != null) {
		for (java.util.Enumeration e = notaDiCredito.getAccertamenti_scadenzarioHash().keys(); e.hasMoreElements();) {
			Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)e.nextElement();
			notaDiCredito.addToFattura_passiva_ass_totaliMap(
									scadenza,
									calcolaTotalePer(
												(Vector)notaDiCredito.getAccertamenti_scadenzarioHash().get(scadenza),
												notaDiCredito.quadraturaInDeroga()));
		}
	}

	try {
		getHomeCache(userContext).fetchAll(userContext);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(notaDiCredito, e);
	}
}
private void rebuildObbligazioni(UserContext aUC, Fattura_passivaBulk fatturaPassiva) throws ComponentException {

	if (fatturaPassiva == null) return;
	
	BulkList righeFattura = fatturaPassiva.getFattura_passiva_dettColl();
	if (righeFattura != null) {

		java.util.HashMap storniHashMap = null;
		java.util.HashMap addebitiHashMap = null;
		if (fatturaPassiva instanceof Fattura_passiva_IBulk) {
			storniHashMap = caricaStorniPer(aUC, righeFattura);
			((Fattura_passiva_IBulk)fatturaPassiva).setStorniHashMap(storniHashMap);
			addebitiHashMap = caricaAddebitiPer(aUC, righeFattura);
			((Fattura_passiva_IBulk)fatturaPassiva).setAddebitiHashMap(addebitiHashMap);
			try {
				getHomeCache(aUC).fetchAll(aUC);
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(fatturaPassiva, e);
			}
		}
		for (Iterator i = righeFattura.iterator(); i.hasNext(); ) {
			Fattura_passiva_rigaBulk riga = (Fattura_passiva_rigaBulk)i.next();
			Obbligazione_scadenzarioBulk scadenza = riga.getObbligazione_scadenziario();
			
			if (riga.getObbligazione_scadenziario() != null) {
				if (fatturaPassiva.getFattura_passiva_obbligazioniHash() == null ||
					fatturaPassiva.getFattura_passiva_obbligazioniHash().getKey(scadenza) == null) {
					scadenza = caricaScadenzaObbligazionePer(aUC, scadenza);
				}
				fatturaPassiva.addToFattura_passiva_obbligazioniHash(scadenza, riga);
				if (riga instanceof Fattura_passiva_rigaIBulk) {
					Fattura_passiva_rigaIBulk rigaFP = (Fattura_passiva_rigaIBulk)riga;
					java.math.BigDecimal impStorni = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
					java.math.BigDecimal impAddebiti = new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
					if (storniHashMap != null) {
						impStorni = calcolaTotalePer((Vector)storniHashMap.get(riga), false);
						rigaFP.setIm_totale_storni(impStorni);
					}
					if (addebitiHashMap != null) {
						impAddebiti = calcolaTotalePer((Vector)addebitiHashMap.get(riga), false);
						rigaFP.setIm_totale_addebiti(impAddebiti);
					}
					java.math.BigDecimal totaleRiga = riga.getIm_imponibile().add(riga.getIm_iva());
					rigaFP.setSaldo(totaleRiga.subtract(impStorni).add(impAddebiti));
				}
			}
		}
		if (!(fatturaPassiva instanceof Fattura_passiva_IBulk) && fatturaPassiva.getFattura_passiva_obbligazioniHash() != null) {
			for (java.util.Enumeration e = fatturaPassiva.getFattura_passiva_obbligazioniHash().keys(); e.hasMoreElements();) {
				Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)e.nextElement();
				fatturaPassiva.addToFattura_passiva_ass_totaliMap(
											scadenza,
											calcolaTotalePer(
												(Vector)fatturaPassiva.getFattura_passiva_obbligazioniHash().get(scadenza),
												fatturaPassiva.quadraturaInDeroga()));
			}
		}
	}
	try {
		getHomeCache(aUC).fetchAll(aUC);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
}

private Fattura_passivaBulk resetChangeData(
	UserContext userContext, 
	Fattura_passivaBulk fattura_passiva)
	throws ComponentException {

	// Viene chiamato solo quando la valuta della fattura � null
	fattura_passiva.setInizio_validita_valuta(null);
	fattura_passiva.setFine_validita_valuta(null);
	fattura_passiva.setCambio(new java.math.BigDecimal(0));
	return fattura_passiva;
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Nessun errore segnalato.
  *    PostCondition:
  *      Viene rimosso il dettaglio dalla lista delle associazioni con i beni dell'inventario.
 */
//^^@@

public void rimuoviDaAssociazioniInventario(
		UserContext userContext,
		Fattura_passiva_rigaIBulk dettaglio,
		Ass_inv_bene_fatturaBulk associazione) throws ComponentException {

	if (dettaglio != null && associazione != null) {
		Inventario_beni_apgHome home = (Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
		try {
			home.deleteTempFor(userContext, associazione);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(dettaglio, e);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(dettaglio, e);
		}
	}
}
public void rimuoviDaAssociazioniInventario(
		UserContext userContext,
		Ass_inv_bene_fatturaBulk associazione) throws ComponentException {

	if (associazione != null) {
		Inventario_beni_apgHome home = (Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
		try {
			home.deleteTempFor(userContext, associazione);
		} catch (it.cnr.jada.persistency.IntrospectionException e) {
			throw handleException(e);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(e);
		}
	}
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesto il riporto del documento amministrativo all'esercizio successivo.
  *    PostCondition:
  *      Viene richiamata la procedura DB corretta
 */
//^^@@

public IDocumentoAmministrativoBulk riportaAvanti(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm,
	OptionRequestParameter status)
	throws ComponentException {

//	fattura = (Fattura_passivaBulk)modificaConBulk(userContext, fattura, status);
	try {
		return getRiportoComponentSession(userContext).riportaAvanti(userContext, docAmm, status);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  Normale.
  *    PreCondition:
  *      Viene richiesto il riporto del documento amministrativo all'esercizio precedente.
  *    PostCondition:
  *      Viene richiamata la procedura DB corretta
 */
//^^@@

public IDocumentoAmministrativoBulk riportaIndietro(
	UserContext userContext,
	IDocumentoAmministrativoBulk docAmm) 
	throws ComponentException {

	try {
		return getRiportoComponentSession(userContext).riportaIndietro(userContext, docAmm);
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}
}
/**
 * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
 *
 * Pre-post-conditions:
 *
 * Nome: Rollback to savePoint
 * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
 * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
 *       modifiche apportate al doc. amministrativo che ha aperto il compenso
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException {

	try {
		rollbackToSavepoint(userContext, savePointName);
	} catch (java.sql.SQLException e) {
		if (e.getErrorCode() != 1086)
			throw handleException(e);
	}
}
private AccertamentoBulk salvaAccertamento(
	UserContext context,
	AccertamentoBulk accertamento)
	throws ComponentException {

	if (accertamento != null) {
		try {
			it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession)
																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																					"CNRDOCCONT00_EJB_AccertamentoAbstractComponentSession",
																					it.cnr.contab.doccont00.ejb.AccertamentoAbstractComponentSession.class);
			try {
				return (AccertamentoBulk)h.modificaConBulk(context, accertamento);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				throw new it.cnr.jada.comp.ApplicationException("Accertamento \"" + accertamento.getDs_accertamento() + "\": " + e.getMessage(),e);
			}
		} catch (java.rmi.RemoteException e) {
			throw handleException(accertamento, e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(accertamento, e);
		}
	}
	return null;
}
private Buono_carico_scaricoBulk salvaBuonoCarico(
	UserContext context,
	Buono_carico_scaricoBulk buonoCS)
	throws ComponentException {

	if (buonoCS != null) {
		try {
			it.cnr.jada.ejb.CRUDComponentSession h = null;
				h = (it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession)
										it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
										"CNRINVENTARIO01_EJB_BuonoCaricoScaricoComponentSession",
										it.cnr.contab.inventario01.ejb.BuonoCaricoScaricoComponentSession.class);
			try {
				return (Buono_carico_scaricoBulk)h.creaConBulk(context, buonoCS);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				throw new it.cnr.jada.comp.ApplicationException("Buono carico/scarico \"" + buonoCS.getDs_buono_carico_scarico() + "\": " + e.getMessage(),e);
			}
		} catch (java.rmi.RemoteException e) {
			throw handleException(buonoCS, e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(buonoCS, e);
		}
	}
	return null;
}
private ObbligazioneBulk salvaObbligazione(
	UserContext context,
	ObbligazioneBulk obbligazione)
	throws ComponentException {

	if (obbligazione != null) {
		try {
			it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession)
																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																					"CNRDOCCONT00_EJB_ObbligazioneAbstractComponentSession",
																					it.cnr.contab.doccont00.ejb.ObbligazioneAbstractComponentSession.class);
			try {
				return (ObbligazioneBulk)h.modificaConBulk(context, obbligazione);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				throw new it.cnr.jada.comp.ApplicationException("Impegno \"" + obbligazione.getDs_obbligazione() + "\": " + e.getMessage(),e);
			}
		} catch (java.rmi.RemoteException e) {
			throw handleException(obbligazione, e);
		} catch (javax.ejb.EJBException e) {
			throw handleException(obbligazione, e);
		}
	}
	return null;
}

private void searchDuplicateInDB (UserContext aUC, Fattura_passivaBulk fatturaPassiva) 
	throws ComponentException {

	if (fatturaPassiva.getNr_fattura_fornitore() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: inserire il numero del documento del fornitore.");
	
	try {
		Fattura_passivaBulk clause = null;
		if (fatturaPassiva instanceof Fattura_passiva_IBulk)
			clause = new Fattura_passiva_IBulk();
		else if (fatturaPassiva instanceof Nota_di_creditoBulk)
			clause = new Nota_di_creditoBulk();
		else 
			clause = new Nota_di_debitoBulk();

		//clause.setCd_cds(fatturaPassiva.getCd_cds());
		//clause.setEsercizio(fatturaPassiva.getEsercizio());
		clause.setEsercizio_fattura_fornitore(fatturaPassiva.getEsercizio_fattura_fornitore());
		clause.setNr_fattura_fornitore(fatturaPassiva.getNr_fattura_fornitore());
		clause.setDt_fattura_fornitore(fatturaPassiva.getDt_fattura_fornitore());
		if (fatturaPassiva.getPartita_iva()!=null)
			clause.setPartita_iva(fatturaPassiva.getPartita_iva());
		else
			clause.setFornitore(fatturaPassiva.getFornitore());
		clause.setTi_fattura(fatturaPassiva.getTi_fattura());
		java.util.List occurences = ((Fattura_passivaHome)getHome(aUC, fatturaPassiva)).findDuplicateFatturaFornitore(clause);
		if (occurences != null && !occurences.isEmpty()) {
			for (Iterator i = occurences.iterator(); i.hasNext();) {
				Fattura_passivaBulk occurence = (Fattura_passivaBulk)i.next();
				if ((!fatturaPassiva.equalsByPrimaryKey(occurence)) && (!occurence.isAnnullato()))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione duplicazione documento fornitore: il numero di documento " + fatturaPassiva.getNr_fattura_fornitore() + " risulta gi� registrato");
			}
		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
	
}
protected it.cnr.jada.persistency.sql.Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
	it.cnr.jada.persistency.sql.SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses,bulk);
	TerzoBulk fornitore = ((Fattura_passivaBulk)bulk).getFornitore();
	if (fornitore != null) {
		sql.addTableToHeader("TERZO");
		sql.addSQLJoin("TERZO.CD_TERZO","FATTURA_PASSIVA.CD_TERZO");
		sql.addSQLClause("AND","FATTURA_PASSIVA.CD_TERZO",sql.EQUALS, fornitore.getCd_terzo());
		sql.addSQLClause("AND","TERZO.CD_PRECEDENTE",sql.EQUALS, fornitore.getCd_precedente());
	}
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectBanca_uoByClause(UserContext aUC,Nota_di_creditoBulk notaDiCredito, it.cnr.contab.anagraf00.core.bulk.BancaBulk banca, CompoundFindClause clauses) 
	throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
	return bancaHome.selectBancaFor(
								notaDiCredito.getModalita_pagamento_uo(),
								notaDiCredito.getCd_terzo_uo_cds());
}
public it.cnr.jada.persistency.sql.SQLBuilder selectBancaByClause(UserContext aUC,Fattura_passivaBulk fatturaPassiva, it.cnr.contab.anagraf00.core.bulk.BancaBulk banca, CompoundFindClause clauses) 
	throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
	return bancaHome.selectBancaFor(
								fatturaPassiva.getModalita_pagamento(),
								fatturaPassiva.getCd_terzo());
}
public it.cnr.jada.persistency.sql.SQLBuilder selectBancaByClause(UserContext aUC,Fattura_passiva_rigaBulk fatturaPassivaRiga, it.cnr.contab.anagraf00.core.bulk.BancaBulk banca, CompoundFindClause clauses) 
throws ComponentException {

BancaHome bancaHome = (BancaHome)getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
return bancaHome.selectBancaFor(
		fatturaPassivaRiga.getModalita_pagamento(),
		fatturaPassivaRiga.getCd_terzo());
}
public it.cnr.jada.persistency.sql.SQLBuilder selectBene_servizioByClause(
	UserContext userContext,
	Fattura_passiva_rigaBulk dettaglio, 
	Bene_servizioBulk beneServizio, 
	CompoundFindClause clauses) 
	throws ComponentException {
	GregorianCalendar data_da =new GregorianCalendar();
	GregorianCalendar data_a =new GregorianCalendar();
	Bene_servizioHome beneServizioHome = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = beneServizioHome.createSQLBuilder();
	//Viene aggiunta clausola su beni o servizi
	// Se la data competenza da o Se la data competenza a
	// � minore dell'esercizio della fattura non si possono inserire  
	// bene_servizio soggetti all'inventario
	if (dettaglio.getFattura_passiva().getDt_da_competenza_coge()!=null)
		data_da.setTime(new Date(dettaglio.getFattura_passiva().getDt_da_competenza_coge().getTime()));
	
	if (dettaglio.getFattura_passiva().getDt_a_competenza_coge()!=null)
		data_a.setTime(new Date(dettaglio.getFattura_passiva().getDt_a_competenza_coge().getTime()));
	
	if ((data_da!=null && data_da.get(GregorianCalendar.YEAR)< dettaglio.getEsercizio())||(data_a!=null && data_a.get(GregorianCalendar.YEAR)< dettaglio.getEsercizio()))
	    sql.addSQLClause("AND","FL_GESTIONE_INVENTARIO",sql.EQUALS,"N");
	sql.addClause("AND", "ti_bene_servizio", sql.EQUALS, dettaglio.getFattura_passiva().getTi_bene_servizio());
	if((dettaglio.getFattura_passiva().getFl_intra_ue().booleanValue()||dettaglio.getFattura_passiva().getFl_extra_ue().booleanValue()|| dettaglio.getFattura_passiva().getFl_san_marino_senza_iva().booleanValue()) 
			&& dettaglio.getFattura_passiva().isCommerciale()&& dettaglio.getFattura_passiva().getTi_bene_servizio().compareTo(Bene_servizioBulk.SERVIZIO)==0)
			sql.addClause("AND", "fl_autofattura", sql.EQUALS, dettaglio.getFattura_passiva().getFl_autofattura());
	else
		if( dettaglio.getFattura_passiva().getTipo_sezionale()!=null )
			sql.addClause("AND", "fl_autofattura", sql.EQUALS, dettaglio.getFattura_passiva().getTipo_sezionale().getFl_servizi_non_residenti());
	sql.addClause(clauses);
	return sql;
}

public RemoteIterator selectBeniFor(
	UserContext userContext,
	Fattura_passivaBulk fattura) throws ComponentException 
{
	SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
	sql.addSQLClause( "AND", "esercizio_fatt_pass", sql.EQUALS, fattura.getEsercizio());
	sql.addSQLClause( "AND", "cd_cds_fatt_pass", sql.EQUALS, fattura.getCd_cds());
	sql.addSQLClause( "AND", "cd_uo_fatt_pass", sql.EQUALS, fattura.getCd_unita_organizzativa());			
	sql.addSQLClause("AND","pg_fattura_passiva", sql.EQUALS, fattura.getPg_fattura_passiva());
	sql.addSQLClause("AND","cd_tipo_documento_amm", sql.ISNULL, null);
	it.cnr.jada.util.RemoteIterator ri = iterator(userContext,sql,V_ass_inv_bene_fatturaBulk.class,null);
	
	return ri;
	
}
public it.cnr.jada.persistency.sql.SQLBuilder selectFattura_esteraByClause(UserContext aUC,Fattura_passivaBulk fatturaPassiva, Fattura_passiva_IBulk fatturaEstera, CompoundFindClause clauses) 
	throws ComponentException {

	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,fatturaEstera).createSQLBuilder();
	sql.addClause(clauses);
	sql.openParenthesis("AND");
    sql.openParenthesis("AND");
	sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_passivaBulk.STATO_ANNULLATO);
	sql.addClause("AND", "fl_extra_ue", sql.EQUALS, Boolean.TRUE); 
	sql.addSQLClause("AND", "FATTURA_PASSIVA.TI_BENE_SERVIZIO", sql.EQUALS,  Bene_servizioBulk.BENE);
	sql.addClause("AND", "fl_merce_intra_ue", sql.NOT_EQUALS, Boolean.TRUE); 
	sql.closeParenthesis();
    sql.openParenthesis("OR");
	sql.addClause("AND","fl_merce_extra_ue",sql.EQUALS, Boolean.TRUE);
	sql.addClause("AND", "stato_cofi", sql.NOT_EQUALS, Fattura_passivaBulk.STATO_ANNULLATO);
    sql.closeParenthesis();
    sql.closeParenthesis();
	sql.addSQLClause("AND", " NOT EXISTS (SELECT 1 FROM FATTURA_PASSIVA B WHERE B.CD_CDS_FAT_CLGS = FATTURA_PASSIVA.CD_CDS AND B.CD_UO_FAT_CLGS = FATTURA_PASSIVA.CD_UNITA_ORGANIZZATIVA AND B.ESERCIZIO_FAT_CLGS = FATTURA_PASSIVA.ESERCIZIO AND B.PG_FATTURA_PASSIVA_FAT_CLGS = FATTURA_PASSIVA.PG_FATTURA_PASSIVA AND (B.FL_BOLLA_DOGANALE = 'Y' OR B.FL_SPEDIZIONIERE = 'Y') AND B.STATO_COFI <> 'A')");
	
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectFornitoreByClause(UserContext aUC,Fattura_passivaBulk fatturaPassiva, TerzoBulk fornitore, CompoundFindClause clauses) 
	throws ComponentException {

	//if (fatturaPassiva.getDt_fattura_fornitore() == null)
		//throw new it.cnr.jada.comp.ApplicationException("Impostare una data fattura fornitore");
		
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,fornitore,"V_TERZO_CF_PI").createSQLBuilder();
	sql.addTableToHeader("ANAGRAFICO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","V_TERZO_CF_PI.CD_ANAG");
	sql.addSQLClause("AND","V_TERZO_CF_PI.CD_TERZO",sql.EQUALS,fornitore.getCd_terzo());
	sql.addSQLClause("AND","V_TERZO_CF_PI.DENOMINAZIONE_SEDE",sql.STARTSWITH, fornitore.getDenominazione_sede());

	sql.addSQLClause("AND","((V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL) OR (V_TERZO_CF_PI.DT_FINE_RAPPORTO >= ?))");
	sql.addParameter(fatturaPassiva.getDt_fattura_fornitore(),java.sql.Types.TIMESTAMP,0);

	sql.addSQLClause("AND","V_TERZO_CF_PI.TI_TERZO",sql.NOT_EQUALS,TerzoBulk.DEBITORE);
	sql.addSQLClause("AND","V_TERZO_CF_PI.CD_PRECEDENTE",sql.EQUALS,fornitore.getCd_precedente());

	//	modifica aggiunta per Art.35 DL n.223/2006
	sql.openParenthesis( "AND");
	sql.addSQLClause("AND","(ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS,"I");
	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA", sql.ISNOTNULL, true);
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE", sql.ISNOTNULL, true);
	sql.closeParenthesis();
	sql.openParenthesis("OR");
	sql.addSQLClause("OR","ANAGRAFICO.FL_NON_OBBLIG_P_IVA", sql.EQUALS,"Y");
	sql.addSQLClause("AND","ANAGRAFICO.TI_ITALIANO_ESTERO", sql.EQUALS,"I");
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE", sql.ISNOTNULL, true);
	sql.closeParenthesis();
	sql.addSQLClause("OR","ANAGRAFICO.TI_ITALIANO_ESTERO", sql.NOT_EQUALS,"I");
	sql.closeParenthesis();
	
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.STARTSWITH,fatturaPassiva.getCodice_fiscale());
	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.STARTSWITH,fatturaPassiva.getPartita_iva());
	sql.addSQLClause("AND","ANAGRAFICO.NOME",sql.STARTSWITH,fatturaPassiva.getNome());
	sql.addSQLClause("AND","ANAGRAFICO.COGNOME",sql.STARTSWITH,fatturaPassiva.getCognome());
	sql.addSQLClause("AND","ANAGRAFICO.RAGIONE_SOCIALE",sql.STARTSWITH,fatturaPassiva.getRagione_sociale());
	sql.addSQLClause("AND","ANAGRAFICO.TI_ENTITA",sql.NOT_EQUALS,"D");

	sql.addSQLClause("AND","ANAGRAFICO.TI_ITALIANO_ESTERO",sql.EQUALS,fatturaPassiva.getSupplierNationType());
	
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectFornitoreByClause(
	UserContext aUC,
	Filtro_ricerca_doc_ammVBulk filtro, 
	TerzoBulk soggetto,
	CompoundFindClause clauses) 
	throws ComponentException {
	
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,soggetto,"V_TERZO_CF_PI").createSQLBuilder();
	sql.addTableToHeader("ANAGRAFICO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","V_TERZO_CF_PI.CD_ANAG");
	sql.addSQLClause("AND","V_TERZO_CF_PI.CD_TERZO",sql.EQUALS,soggetto.getCd_terzo());

	sql.addSQLClause("AND","V_TERZO_CF_PI.TI_TERZO",sql.NOT_EQUALS,TerzoBulk.DEBITORE);
	sql.addSQLClause("AND","V_TERZO_CF_PI.CD_PRECEDENTE",sql.EQUALS,soggetto.getCd_precedente());

	sql.addSQLClause("AND","ANAGRAFICO.TI_ENTITA",sql.NOT_EQUALS,AnagraficoBulk.DIVERSI);

	if (soggetto.getAnagrafico() != null) {
		sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.STARTSWITH,soggetto.getAnagrafico().getCodice_fiscale());
		sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.STARTSWITH,soggetto.getAnagrafico().getPartita_iva());
	}
	
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectFornitoreByClause(UserContext aUC,Filtro_ricerca_obbligazioniVBulk filtro, TerzoBulk fornitore, CompoundFindClause clauses) 
	throws ComponentException {

	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,fornitore).createSQLBuilder();
	sql.addTableToHeader("ANAGRAFICO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","TERZO.CD_ANAG");
	sql.addSQLClause("AND","TERZO.DENOMINAZIONE_SEDE",sql.STARTSWITH, fornitore.getDenominazione_sede());
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.STARTSWITH,fornitore.getAnagrafico().getCodice_fiscale());
	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.STARTSWITH,fornitore.getAnagrafico().getPartita_iva());
	sql.addSQLClause("AND","ANAGRAFICO.NOME",sql.STARTSWITH,fornitore.getAnagrafico().getNome());
	sql.addSQLClause("AND","ANAGRAFICO.COGNOME",sql.STARTSWITH,fornitore.getAnagrafico().getCognome());
	sql.addSQLClause("AND","ANAGRAFICO.RAGIONE_SOCIALE",sql.STARTSWITH,fornitore.getAnagrafico().getRagione_sociale());
	sql.addClause(clauses);
	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectLettera_pagamento_estero_sospesoByClause(
	UserContext aUC,
	Fattura_passivaBulk fatturaPassiva, 
	SospesoBulk sospeso,
	CompoundFindClause clauses) 
	throws ComponentException {

	if (fatturaPassiva.getModalita_pagamento() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione selezionare la Modalit� di pagamento!");
	if (fatturaPassiva.getModalita_pagamento().getTi_pagamento() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione la Tipologia del pagamento � vuota!");
	
	it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,sospeso).createSQLBuilder();
	
	sql.openParenthesis("AND");
	sql.addSQLClause("OR", "IM_ASSOCIATO", sql.EQUALS, new java.math.BigDecimal(0));
	sql.addSQLClause("OR", "IM_ASSOCIATO", sql.ISNULL, null);
	sql.closeParenthesis();
	sql.openParenthesis("AND");
	sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.EQUALS, new java.math.BigDecimal(0));
	sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.ISNULL, null);
	sql.closeParenthesis();
	sql.openParenthesis("AND");
	sql.addSQLClause("OR", "IM_SOSPESO", sql.NOT_EQUALS, new java.math.BigDecimal(0));
	sql.addSQLClause("AND", "IM_SOSPESO", sql.ISNOTNULL, null);
	sql.closeParenthesis();
	sql.addClause("AND", "fl_stornato", sql.EQUALS, Boolean.FALSE);
	try {
	EnteBulk ente = (EnteBulk) getHome(aUC, EnteBulk.class)
			.findAll().get(0);

		if (!Utility.createParametriCnrComponentSession().getParametriCnr(aUC,fatturaPassiva.getLettera_pagamento_estero().getEsercizio()).getFl_tesoreria_unica().booleanValue()){
			sql.addClause("AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
			sql.addClause("AND", "cd_cds", sql.EQUALS, fatturaPassiva.getCd_cds());
			sql.addClause("AND", "cd_cds_origine", sql.EQUALS, fatturaPassiva.getCd_cds_origine());
		}
		else
		{
			sql.addClause("AND", "cd_cds", sql.EQUALS, ente.getCd_unita_organizzativa());
			sql.openParenthesis("AND"); 
			sql.openParenthesis("AND"); 
			sql.addClause("AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
			sql.addClause("AND", "cd_cds_origine", sql.EQUALS, fatturaPassiva.getCd_cds_origine());
			sql.closeParenthesis();
			sql.openParenthesis("OR");
			sql.addClause("OR", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
			sql.addClause("AND", "cd_cds_origine", sql.ISNULL,null);
			sql.closeParenthesis();
			sql.closeParenthesis();
		}
	} catch (RemoteException e) {
		throw handleException(fatturaPassiva, e);
	} catch (EJBException e) {
		throw handleException(fatturaPassiva, e);
	} catch (PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}	
	
	sql.addClause("AND", "esercizio", sql.EQUALS, fatturaPassiva.getLettera_pagamento_estero().getEsercizio());
	sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS, sospeso.TIPO_SPESA);
	sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso.TI_SOSPESO);
	sql.addClause(
		"AND", 
		"ti_cc_bi", 
		(Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(fatturaPassiva.getModalita_pagamento().getTi_pagamento())?
			sql.EQUALS : sql.NOT_EQUALS),
		sospeso.TIPO_BANCA_ITALIA);
	
	sql.addClause(clauses);

	return sql;
}
public it.cnr.jada.persistency.sql.SQLBuilder selectVoce_ivaByClause(
	UserContext userContext,
	Fattura_passiva_rigaBulk dettaglio, 
	Voce_ivaBulk voceIva, 
	CompoundFindClause clauses) 
	throws ComponentException {

	Voce_ivaHome voceIvaHome = (Voce_ivaHome)getHome(userContext, Voce_ivaBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = voceIvaHome.createSQLBuilder();
	sql.addSQLClause("AND", "ti_applicazione", sql.NOT_EQUALS, Voce_ivaBulk.VENDITE);
	//Richista 658 del 29/01/2004
	
	// rospuc 29/11/2013 da verificare !!!
	if((dettaglio.getFattura_passiva().isCommerciale() && dettaglio.getFattura_passiva().getFl_intra_ue() != null && dettaglio.getFattura_passiva().getFl_intra_ue().booleanValue()) ||
		(dettaglio.getFattura_passiva().isCommerciale() && dettaglio.getFattura_passiva().getFl_merce_intra_ue() != null && dettaglio.getFattura_passiva().getFl_merce_intra_ue().booleanValue()) ||
		(dettaglio.getFattura_passiva().isCommerciale() && dettaglio.getFattura_passiva().getFl_san_marino_senza_iva() != null && dettaglio.getFattura_passiva().getFl_san_marino_senza_iva().booleanValue())||
   	    (dettaglio.isVoceIVAOnlyIntraUE()))
	//if (dettaglio.isVoceIVAOnlyIntraUE())
		sql.addSQLClause("AND", "fl_intra", sql.EQUALS, "Y");
	//***************************
	if(dettaglio instanceof Nota_di_credito_rigaBulk && !dettaglio.getFattura_passiva().isIvaRecuperabile()){
		 
		if(((Nota_di_credito_rigaBulk)dettaglio).getRiga_fattura_origine()!=null &&((Nota_di_credito_rigaBulk)dettaglio).getRiga_fattura_origine().getVoce_iva()!=null){
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","CD_VOCE_IVA",sql.EQUALS,((Nota_di_credito_rigaBulk)dettaglio).getRiga_fattura_origine().getVoce_iva().getCd_voce_iva());
			sql.addSQLClause("OR","FL_IVA_NON_RECUPERABILE",sql.EQUALS,"Y");
			sql.closeParenthesis();
		}
	}
	// Se Commerciale ed il fornitore � un soggetto iva ed � Italiano
		if (dettaglio.isCommerciale() &&
				dettaglio.getFattura_passiva()!=null && dettaglio.getFattura_passiva().getFornitore()!=null && 
				dettaglio.getFattura_passiva().getFornitore().getAnagrafico()!=null &&
				dettaglio.getFattura_passiva().getFornitore().getAnagrafico().getTi_italiano_estero()!=null &&
				dettaglio.getFattura_passiva().getFornitore().getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.ITALIA)==0 &&
				dettaglio.getFattura_passiva().getFornitore().getAnagrafico().getPartita_iva()!=null){
			// tutti
				sql.openParenthesis("AND");
				sql.addSQLClause("AND","FL_SOLO_ITALIA",sql.EQUALS,"N");
				sql.addSQLClause("OR","FL_SOLO_ITALIA",sql.EQUALS,"Y");
				sql.closeParenthesis();

				sql.openParenthesis("AND");
				sql.addSQLClause("AND","FL_AUTOFATTURA",sql.EQUALS,"N");
				sql.addSQLClause("OR","FL_AUTOFATTURA",sql.EQUALS,"Y");
				sql.closeParenthesis();
		} 
		else{
			sql.addSQLClause("AND","FL_SOLO_ITALIA",sql.EQUALS,"N");
			sql.addSQLClause("AND","FL_AUTOFATTURA",sql.EQUALS,"N");
		}
		
		if(dettaglio.getBene_servizio()!=null){
			sql.openParenthesis("AND");
			sql.addSQLClause("AND","TI_BENE_SERVIZIO",sql.EQUALS,voceIva.BENE_SERVIZIO);
			sql.addSQLClause("OR","TI_BENE_SERVIZIO",sql.EQUALS,dettaglio.getBene_servizio().getTi_bene_servizio());
			sql.closeParenthesis();
		}
		
	sql.addClause(clauses);
	return sql;
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna condizione di errore rivelata.
  *    PostCondition:
  *      Imposta nella fattura la valuta di default del sistema.
  *  Non esiste la valuta o il periodo di cambio di riferimento.
  *    PreCondition:
  *      La valuta di riferimento o il relativo cambio non sono presenti.
  *    PostCondition:
  *      Annullata la scelta della valuta.
 */
//^^@@

public Fattura_passivaBulk selezionaValutaDiDefault(it.cnr.jada.UserContext uc, Fattura_passivaBulk fattura) 
 	throws ComponentException{
	
	return setChangeDataToEur(uc, fattura);
}

private Fattura_passivaBulk setChangeDataToEur(
	UserContext userContext, 
	Fattura_passivaBulk fattura_passiva)
	throws ComponentException {

	fattura_passiva.setValuta(getEuro(userContext));
	try {
		fattura_passiva = basicCercaCambio(
						userContext, 
						fattura_passiva, 
						getHome(userContext, fattura_passiva).getServerTimestamp());
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fattura_passiva, e);
	}
	fattura_passiva.setDefaultValuta(true);
	return fattura_passiva;
}
//^^@@
/** 
  *  Calcolo totali di fattura.
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Vegono calcolati i totali per la fattura passata in argomento.
  *  Si verifica errore.
  *    PreCondition:
  *      Condizione di errore.
  *    PostCondition:
  *      Viene rilanciata una ComponentExcpetion con messaggio dettagliato.
 */
//^^@@

public Nota_di_creditoBulk setContoEnteIn(
	it.cnr.jada.UserContext userContext, 
	Nota_di_creditoBulk ndc, 
	java.util.List banche)
 	throws ComponentException {

	if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(ndc.getModalita_pagamento_uo().getTi_pagamento())) {
		ndc.setBanca_uo((BancaBulk)banche.get(0));
		return ndc;
	}
		
	try {
		Configurazione_cnrBulk config = new Configurazione_cnrBulk(
															"CONTO_CORRENTE_SPECIALE",
															"ENTE", 
															"*", 
															new Integer(0));
		it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome)getHome(userContext, config);
		List configurazioni = home.find(config);
		if (configurazioni != null) {
			if (configurazioni.isEmpty())
				ndc.setBanca_uo((BancaBulk)banche.get(0));
			else if (configurazioni.size() == 1) {
				Configurazione_cnrBulk configBanca = (Configurazione_cnrBulk)configurazioni.get(0);
				BancaBulk bancaEnte = null;
				for (Iterator i = banche.iterator(); i.hasNext();) {
					BancaBulk banca = (BancaBulk)i.next();
					if (bancaEnte == null) {
						if (banca.getAbi().equalsIgnoreCase(configBanca.getVal01()) &&
							banca.getCab().equalsIgnoreCase(configBanca.getVal02()) &&
							banca.getNumero_conto().contains(configBanca.getVal03()))
							bancaEnte = banca;
					}
				}
				if (bancaEnte == null)
					bancaEnte = (BancaBulk)banche.get(0);
				ndc.setBanca_uo(bancaEnte);
			} else
				ndc.setBanca_uo((BancaBulk)banche.get(0));
		}
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}

	return ndc;
}
/**
 * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
 * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
 * anche quelli del documento amministrativo.
 *
 * Pre-post-conditions:
 *
 * Nome: Imposta savePoint
 * Pre:  Una richiesta di impostare un savepoint e' stata generata 
 * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
 *
 * @param	uc	lo UserContext che ha generato la richiesta
 */	
public void setSavePoint(UserContext userContext, String savePointName) throws ComponentException {

	try {
		setSavepoint(userContext, savePointName);
	} catch (java.sql.SQLException e) {
		throw handleException(e);
	}
}
//^^@@
/** 
  *  Storna i dettagli selezionati.
  *    PreCondition:
  *      In nota di credito viene richiesta la contabilizzazione dei dettagli selezionati.
  *    PostCondition:
  *      Vegono stornati nella nota di credito passata i "dettagliDaStornare" sull'obbligazione/accertamento selezionato/creato.
  */
//^^@@

public Nota_di_creditoBulk stornaDettagli(
	UserContext context,
	Nota_di_creditoBulk ndC,
	java.util.List dettagliDaStornare,
	java.util.Hashtable relationsHash)
	throws ComponentException {

	if (ndC == null) return ndC;

	if (dettagliDaStornare == null || dettagliDaStornare.isEmpty()) {
		if (relationsHash != null) {
			Obbligazione_scadenzarioBulk obbligazioneSelezionata = (Obbligazione_scadenzarioBulk)relationsHash.get(ndC);
			if (obbligazioneSelezionata != null)
				ndC.addToFattura_passiva_obbligazioniHash(obbligazioneSelezionata, null);
		}
	} else {
		for (Iterator i = dettagliDaStornare.iterator(); i.hasNext();) {
			Nota_di_credito_rigaBulk rigaNdC = (Nota_di_credito_rigaBulk)i.next();
			Fattura_passiva_rigaIBulk rigaAssociata = null;
			Accertamento_scadenzarioBulk accertamento_scadenzarioAssociato = null;
			if (relationsHash != null) {
				Object obj = relationsHash.get(rigaNdC);
				if (obj instanceof Fattura_passiva_rigaIBulk) {
					Fattura_passiva_rigaIBulk rigaInRelazione = (Fattura_passiva_rigaIBulk)obj;
					rigaAssociata = (rigaInRelazione == null) ? 
											rigaNdC.getRiga_fattura_origine() : 
											rigaInRelazione;
				} else {
					accertamento_scadenzarioAssociato = (Accertamento_scadenzarioBulk)obj;
				}
			} else
				rigaAssociata = rigaNdC.getRiga_fattura_origine();

			if (rigaAssociata != null) {
				ndC = basicStornaDettaglio(
										context,
										ndC,
										rigaNdC,
										rigaAssociata);
			} else {
				ndC = basicStornaDettaglio(
										context,
										ndC,
										rigaNdC,
										accertamento_scadenzarioAssociato);
			}
		}
	}
	return ndC;
}
//^^@@
/** 
  *  Aggiornamento di un dettaglio di documento amministrativo
  *    PreCondition:
  *      Richiesto l'aggiornamento di un dettaglio di documento amministrativo
  *    PostCondition:
  *      Il dettaglio viene aggiornato
 */
//^^@@

public IDocumentoAmministrativoRigaBulk update(
	it.cnr.jada.UserContext userContext, 
	IDocumentoAmministrativoRigaBulk rigaDocAmm)
	throws it.cnr.jada.comp.ComponentException {

	try {
		updateBulk(userContext, (OggettoBulk)rigaDocAmm);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException((OggettoBulk)rigaDocAmm, e);
	}
	return rigaDocAmm;
}
//^^@@
/** 
  *  Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PreCondition:
  *      Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
  *    PostCondition:
  *      Il dettaglio viene aggiornato
 */
//^^@@

public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
	it.cnr.jada.UserContext userContext, 
	IScadenzaDocumentoContabileBulk scadenza)
	throws it.cnr.jada.comp.ComponentException {

	try {
		((IScadenzaDocumentoContabileHome)getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext,scadenza);
	} catch (it.cnr.jada.persistency.PersistencyException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	} catch (it.cnr.jada.bulk.BusyResourceException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	} catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
		throw handleException((OggettoBulk)scadenza, exc);
	}

	return scadenza;
}
private void validaConConsuntivi(
	UserContext userContext,
	Fattura_passivaBulk original,
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (original == null || fatturaPassiva == null)
		return;

	if (fatturaPassiva.isStampataSuRegistroIVA() || fatturaPassiva.isElettronica()|| fatturaPassiva.isGenerataDaCompenso()) {
		//ATTENZIONE: a seguito dell'errore segnalato 569 (dovuto alla richiesta 423) il controllo viene
		//ora eseguito anche se la sola autofattura � stampata sui registri IVA

		Fattura_passivaBulk fpConsuntivata = (Fattura_passivaBulk)calcoloConsuntivi(userContext, fatturaPassiva);
		Fattura_passivaBulk originaleConsuntivato = (Fattura_passivaBulk)calcoloConsuntivi(userContext, original);
		Vector consOriginale = (Vector)originaleConsuntivato.getFattura_passiva_consuntivoColl();
		Vector consFP = (Vector)fpConsuntivata.getFattura_passiva_consuntivoColl();
		if (consFP.size() != consOriginale.size())
			throw new ApplicationException("Attenzione: non � possibile aggiungere, togliere o cambiare codici IVA su " + fatturaPassiva.getDescrizioneEntitaPlurale() + " gi� stampate o collegate ad autofattura gi� stampata!");
		for (Iterator i = consFP.iterator(); i.hasNext();) {
			Consuntivo_rigaVBulk rigaConsuntivoFP = (Consuntivo_rigaVBulk)i.next();
			try {
				int idx = it.cnr.jada.bulk.BulkCollections.indexOfByPrimaryKey(consOriginale, rigaConsuntivoFP);
				Consuntivo_rigaVBulk rigaConsuntivoOriginale = (Consuntivo_rigaVBulk)consOriginale.get(idx);
				if (!rigaConsuntivoOriginale.equalsByPrimaryKey(rigaConsuntivoFP))
					throw new IndexOutOfBoundsException();					
				if ((rigaConsuntivoOriginale.getTotale_iva().compareTo(rigaConsuntivoFP.getTotale_iva()) != 0) ||
					(rigaConsuntivoOriginale.getTotale_imponibile().compareTo(rigaConsuntivoFP.getTotale_imponibile()) != 0))
					throw new ApplicationException("Attenzione: i totali IVA o imponibile per il codice IVA \""+ 
								rigaConsuntivoFP.getVoce_iva().getCd_voce_iva() +
								"\" non sono modificabili perch� la " + fatturaPassiva.getDescrizioneEntita() + " o la sua autofattura (se esiste) risulta gi� stampata su registro definitivo!");
			} catch (IndexOutOfBoundsException e) {
					throw new ApplicationException("Attenzione: non � possibile aggiungere il codice IVA \""+ 
								rigaConsuntivoFP.getVoce_iva().getCd_voce_iva() +
								"\" perch� la " + fatturaPassiva.getDescrizioneEntita() + " o la sua autofattura (se esiste) risulta gi� stampata su registro definitivo!");
			}
		}
	}
}
private void validaDisponibilitaDiCassaCDS(UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException {

	try	{
		if (fattura.getLettera_pagamento_estero()!=null && fattura.getLettera_pagamento_estero().getEsercizio()!=null && !Utility.createParametriCnrComponentSession().getParametriCnr(userContext,fattura.getLettera_pagamento_estero().getEsercizio()).getFl_tesoreria_unica().booleanValue()){	
			it.cnr.jada.bulk.BulkHome home = getHome( userContext, V_disp_cassa_cdsBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addClause( "AND", "esercizio", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
			sql.addClause( "AND", "cd_cds", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds());
			List result = null;
			result = home.fetchAll( sql );
			if ( result.size() == 0 )
				throw new ApplicationException("Non esiste il record per la disponibilit� di cassa del CDS: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds() + " - esercizio: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
			V_disp_cassa_cdsBulk cassa = (V_disp_cassa_cdsBulk) result.get(0);
			if (cassa.getIm_disponibilita_cassa().compareTo(new java.math.BigDecimal(0)) < 0||
				((fattura.getLettera_pagamento_estero().getSospeso()==null||fattura.getLettera_pagamento_estero().getSospeso().getCd_sospeso()==null) && cassa.getIm_disponibilita_cassa().compareTo(fattura.getIm_totale_quadratura())<0))
				throw new it.cnr.jada.comp.ApplicationException("La disponibilit� di cassa del CDS: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds() + " - esercizio: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() + " � stata superata! Salvataggio interrotto.");
		}
	} catch ( Exception e )	{
		throw handleException(fattura, e);
	}

}
//^^@@
/** 
  *  validazione numero fattura
  *    PreCondition:
  *      Il numero della fattura fornitore � gia presente nell'archivio fatture.
  *    PostCondition:
  *      Viene visualizzato il messaggio "Attenzione duplicazione fattura: Il numero di fattura risulta gi� registrato".
  *  tutti i controlli superati
  *    PreCondition:
  *      Nessuna situazione di errore di validazione � stata rilevata.
  *    PostCondition:
  *      Consentita la registrazione.
  *  validazione numero di dettagli maggiore di zero.
  *    PreCondition:
  *      Il numero di dettagli nella fattura � zero
  *    PostCondition:
  *      Viene inviato un messaggio: "Attenzione non possono esistere fatture senza almeno un dettaglio".
  *  validazione aggiunta dettagli n fatture con stato iva B o C.
  *    PreCondition:
  *      E' stato aggiunto un dettaglio in  fatture con stato iva B o C .
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono aggiungere dettagli in fatture con stato iva B o C ."
  *  validazione modifica sezionale
  *    PreCondition:
  *      E' stato modificato un sezionale in fatture con dettagli in stato not I e stato iva B o C.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono eliminare  dettagli in  fatture parzialmente contabilizzate e stato iva B o C"
  *  validazione modifica testata (G3)
  *    PreCondition:
  *      Sono stati modificati i campi  numero fattura di  emissione, data fattura di emissione , importo, flag IntraUE, flag San Marino, sezionale,   
  *      valuta in fatture con stato iva B o C .
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
  *  validazione modifica testata campo fornitore.(G5)
  *    PreCondition:
  *      E stato modificato il campo fornitore nella testata in stato (B or C) or (A and testata=P).
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione lo stato della fattura non consente di modificare il fornitore".
  *  validazione modifica fattura pagata.
  *    PreCondition:
  *      E' satata eseguita una modifica in fattura con testata in stato P.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� modificare nulla in una fattura pagata".
  *  validazione quadratura IVA.
  *    PreCondition:
  *      Il totale imponibile +IVA  di tutte le righe non quadra con il totale fattura riporato in testata.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione, la somma degli imponibili delle righe + la somma della relativa iva non quadra con il totale  
  *	fattura".
  *  validazione associazione scadenze
  *    PreCondition:
  *      Esistono dettagli non collegati ad obbligazione.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si possono modificare questi campi in fatture pagate o in stato IVA B o C"
 */
//^^@@

public void validaFattura(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException {

	if (fatturaPassiva.getFattura_passiva_dettColl().isEmpty())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: per salvare una " + fatturaPassiva.getDescrizioneEntita() + " � necessario inserire almeno un dettaglio");
    for(Iterator i=fatturaPassiva.getFattura_passiva_dettColl().iterator();i.hasNext();)
    {
      Fattura_passiva_rigaBulk riga=(Fattura_passiva_rigaBulk)i.next();
      validaRiga(aUC, riga);
    }
	searchDuplicateInDB(aUC, fatturaPassiva);

	validazioneComune(aUC, fatturaPassiva);

	if (fatturaPassiva instanceof Fattura_passiva_IBulk &&
		(fatturaPassiva.isBollaDoganale() || fatturaPassiva.isSpedizioniere()) &&
		((Fattura_passiva_IBulk)fatturaPassiva).getFattura_estera() == null)
		throw new it.cnr.jada.comp.ApplicationException("La fattura � definita come bolla doganale o spedizioniere. Specificare la fattura estera collegata!");
	
	if (fatturaPassiva instanceof Nota_di_creditoBulk) {
		Nota_di_creditoBulk ndc = (Nota_di_creditoBulk)fatturaPassiva;
		if (ndc.getAccertamentiHash() != null && !ndc.getAccertamentiHash().isEmpty()) {
			if (ndc.getModalita_pagamento_uo() == null)
				throw new it.cnr.jada.comp.ApplicationException("Specificare le modalit� di pagamento per gli accertamenti inseriti.");
			if (ndc.getBanca_uo() == null)
				throw new it.cnr.jada.comp.ApplicationException("Specificare il conto d'appoggio per gli accertamenti inseriti.");
		}
		controllaQuadraturaAccertamenti(aUC, ndc);
	}

	controllaQuadraturaConti(aUC, fatturaPassiva);
	
	//if ((fatturaPassiva.getStato_liquidazione()==null || fatturaPassiva.getStato_liquidazione().compareTo(Fattura_passiva_IBulk.LIQ)==0))
	if (!fatturaPassiva.isGenerataDaCompenso())
		controllaContabilizzazioneDiTutteLeRighe(aUC, fatturaPassiva);
	else
	{
		if(fatturaPassiva.getCompenso()==null)
			throw new it.cnr.jada.comp.ApplicationException("Prima di salvare la fattura occorre generare il compenso!");
	}
		
	controllaQuadraturaIntrastat(aUC, fatturaPassiva);
	controllaQuadraturaObbligazioni(aUC, fatturaPassiva);
	
}
private void controllaQuadraturaInventario(UserContext auc, Fattura_passivaBulk fatturaPassiva) throws ComponentException {
	BigDecimal totale_inv = Utility.ZERO;
	BigDecimal totale_fat = Utility.ZERO;
	BigDecimal im_riga_fattura =Utility.ZERO;
	
	for(Iterator i =fatturaPassiva.getFattura_passiva_dettColl().iterator();i.hasNext();){
		Fattura_passiva_rigaBulk riga_fattura =(Fattura_passiva_rigaBulk)i.next();
		if (riga_fattura.isInventariato()){
			if (riga_fattura.getTi_istituz_commerc().equals(riga_fattura.ISTITUZIONALE))
				im_riga_fattura = riga_fattura.getIm_imponibile().add(riga_fattura.getIm_iva());
			else
				im_riga_fattura = riga_fattura.getIm_imponibile();	
		totale_fat=totale_fat.add(im_riga_fattura);
		}
	}
	Buono_carico_scarico_dettHome assHome=(Buono_carico_scarico_dettHome)getHome(auc,Buono_carico_scarico_dettBulk.class);
	SQLBuilder sql= assHome.createSQLBuilder();
	
		sql.setDistinctClause(true);
		sql.addTableToHeader("ASS_INV_BENE_FATTURA");
		sql.addSQLClause("AND","CD_CDS_FATT_PASS",SQLBuilder.EQUALS,fatturaPassiva.getCd_cds());
		sql.addSQLClause("AND","CD_UO_FATT_PASS",SQLBuilder.EQUALS,fatturaPassiva.getCd_unita_organizzativa());
		sql.addSQLClause("AND","ESERCIZIO_FATT_PASS",SQLBuilder.EQUALS,fatturaPassiva.getEsercizio());
		sql.addSQLClause("AND","PG_FATTURA_PASSIVA",SQLBuilder.EQUALS,fatturaPassiva.getPg_fattura_passiva());
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.ESERCIZIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",sql.EQUALS,"ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.NR_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",sql.EQUALS,"ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO",sql.EQUALS,"ASS_INV_BENE_FATTURA.PROGRESSIVO");
		
	try {
		List associazioni =assHome.fetchAll(sql);
		for (Iterator i=associazioni.iterator();i.hasNext();){
			Buono_carico_scarico_dettBulk ass=(Buono_carico_scarico_dettBulk)i.next();
			totale_inv=totale_inv.add(ass.getValore_unitario());
		}
	} catch (PersistencyException e) {
		throw handleException(e);
	}		
    /* r.p. verificare la condizione fatturaPassiva.getCrudStatus()==1
    *  Escludo le fattura gi� inserite in quanto la quadratura potrebbe non essere verificata
    * a causa della vecchia gestione dell'associazione con il valore del bene completo,
    * che non � stato possibile ricostruire in automatico
    */
	if (totale_fat.compareTo(totale_inv)!=0)//  && fatturaPassiva.getCrudStatus()==1)
		throw new ApplicationException("Attenzione il totale delle righe collegate ad Inventario non corrisponde all'importo da Inventariare.");
		
}
//^^@@
/** 
  *  validazione bene.
  *    PreCondition:
  *      Il bene  relativo alla riga fattura in via di variazione risulta di tipo soggetto ad inventario.
  *    PostCondition:
  *      Viene inviato un messaggio all'utente "Questo bene � soggetto ad inventario".
  *  tutti i controli superati
  *    PreCondition:
  *      Nessun errore rilevato.
  *    PostCondition:
  *      Viene consentita la registrazione riga.
  *  validazione modifica imponibile, iva, totale, aliquiota, tipologia (istituzionale/commerciale) (G2)
  *    PreCondition:
  *      Sono stati modificati i campi  imponibile, iva, totale, aliquiota, tipologia (istituzionale/commerciale) (G2) in fattura in stato B or C
  *    PostCondition:
  *      Viene inviato un messaggio "Attenzione:  questa modifica non � permessa"
  *  validazione modifica/eliminazione dettaglio pagato.
  *    PreCondition:
  *      E' stata richiesta la modifica o l'eliminazione di un dettaglio pagato.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� modificare o eliminare un dettaglio gi� pagato".
  *  validazione modifica/eliminazione dettaglio di fattura interamente pagata.
  *    PreCondition:
  *      E' stato modificato un dettaglio di fattura con testata in in stato pagato.
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione non si pu� modificare un dettaglio in una fattura gi� pagata".
  *  Tipologia di riga fattura
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare il tipo di riga (Commerciale/Istituzionale/Promiscuo)
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare una tipologia per la riga inserita".
  *  Bene/servizio di riga fattura
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare il bene/servizio
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare un bene/servizio per la riga inserita".
  *  Validazione Voce IVA
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza specificare la Voce IVA
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare una Voce IVA per la riga inserita".
  *  Validazione degli importi e quantit�
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura con prezzo unitario, importo iva e quantit� non validi
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare un prezzo unitario, importo iva e quantit� validi".
  *  Data competenza COGE
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura senza data "competenza da" e "competenza a"
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare la data "competenza da" e "competenza a"
  *  Validazione date competenza COGE
  *    PreCondition:
  *      E' stato inserito un dettaglio in fattura con intervallo temporale identificato dalle date "competenza da" e     	   
  *	"competenza a" non valido ("competenza da" maggiore di "competenza a")
  *    PostCondition:
  *      Viene inviato un messaggio:"Attenzione specificare date "competenza da" e "competenza a" valide."
 */
//^^@@

public void validaRiga (UserContext aUC,Fattura_passiva_rigaBulk riga) throws ComponentException {

	if (riga.getFattura_passiva().isPromiscua() && riga.getTi_istituz_commerc() == null)
		throw new it.cnr.jada.comp.ApplicationException("Inserire un tipo per la riga.");
	if (riga.getBene_servizio() == null || riga.getBene_servizio().getCrudStatus() == OggettoBulk.UNDEFINED)
		throw new it.cnr.jada.comp.ApplicationException("Inserire un bene per la riga.");
	if (riga.getVoce_iva() == null || riga.getVoce_iva().getCrudStatus() == OggettoBulk.UNDEFINED)
		throw new it.cnr.jada.comp.ApplicationException("Inserire una voce IVA per la riga.");
	if (riga.getQuantita() == null || riga.getQuantita().compareTo(BigDecimal.ZERO) != 1)
		throw new it.cnr.jada.comp.ApplicationException("La quantit� specificata NON � valida.");
	if (riga.getPrezzo_unitario() == null)
		throw new it.cnr.jada.comp.ApplicationException("Il prezzo unitario specificato NON � valido.");
	  //20/04/2016 Rospuc - Gestione importo 0
//	if (riga.getPrezzo_unitario().doubleValue() == 0 && !riga.getFl_iva_forzata().booleanValue())
//		throw new it.cnr.jada.comp.ApplicationException("Il prezzo unitario o l'importo IVA specificati NON sono validi.");
	if (riga.getFl_iva_forzata().booleanValue() &&
		riga.getPrezzo_unitario().doubleValue() == 0 && 
		riga.getIm_iva().doubleValue() == 0)
		throw new it.cnr.jada.comp.ApplicationException("Il prezzo unitario o l'importo iva non devono essere 0.");
	Elemento_voceBulk voce = recuperoVoce(aUC, riga.getObbligazione_scadenziario());
    if (riga.getPg_trovato()!=null && ((riga.getObbligazione_scadenziario() == null) || (riga.getObbligazione_scadenziario()!=null && isInibitaIndicazioneTrovato(voce))))
    	riga.setPg_trovato(null);
    if (riga.getObbligazione_scadenziario()!=null && isObbligatoriaIndicazioneTrovato(voce) && riga.getPg_trovato()==null )
        throw new it.cnr.jada.comp.ApplicationException(
            "Attenzione! Non � stato inserito il Brevetto/Trovato mentre la voce di bilancio utilizzata per la contabilizzazione del dettaglio collegato ne prevede l'indicazione obbligatoria");
    boolean isBeneSconto = isBeneServizioPerSconto(aUC, riga);
    if(isBeneSconto && (riga.getIm_imponibile().add(riga.getIm_iva())).abs().compareTo(BigDecimal.ONE)>0) //??
    	throw new it.cnr.jada.comp.ApplicationException(
                "Attenzione! Non � possibile inserire per questo bene/servizio questo importo.");
    	
    //28/08/2014 Rospuc - Gestione importo righe negativo 
//	if (riga.getPrezzo_unitario().compareTo(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP)) < 0 && !isBeneSconto)
//		throw new it.cnr.jada.comp.ApplicationException("L'importo del prezzo unitario specificato NON � valido.");
//	if (riga.getIm_iva() == null || riga.getIm_iva().doubleValue() < 0 && !isBeneSconto)
//		throw new it.cnr.jada.comp.ApplicationException("L'importo IVA specificato NON � valido.");
	try {
	VoceIvaComponentSession h=null;
	Voce_ivaBulk def=null; 
		if (riga instanceof Fattura_passiva_rigaIBulk && riga.getFattura_passiva().isFatturaDiBeni() && riga.getFattura_passiva().getFl_intra_ue()){
			 h = (VoceIvaComponentSession)EJBCommonServices.createEJB("CNRDOCAMM00_EJB_VoceIvaComponentSession",VoceIvaComponentSession.class);
			def = h.caricaVoceIvaDefault(aUC);
			if(def!=null && def.getCd_voce_iva()!=null)
			if(riga.getVoce_iva().getCd_voce_iva().compareTo(def.getCd_voce_iva())==0)
				throw new it.cnr.jada.comp.ApplicationException("Codice iva non valido");
		}
	} catch (java.rmi.RemoteException e) {
		throw handleException(e);
	}	
	
	try {
		riga.validaDateCompetenza();
	} catch (it.cnr.jada.bulk.ValidationException exc) {
		throw new ApplicationException(exc.getMessage());
	}
	//java.sql.Timestamp competenzaDa = riga.getDt_da_competenza_coge();
	//java.sql.Timestamp competenzaA = riga.getDt_a_competenza_coge();
	//if (competenzaA != null && competenzaDa != null)
		//if (!competenzaDa.equals(competenzaA) && !competenzaDa.before(competenzaA))
			//throw new it.cnr.jada.comp.ApplicationException("La data \"competenza da\" deve essere precedente o uguale a \"competenza a\"!");
	
	if (riga.getFattura_passiva().isPagata()
		 && riga.isToBeUpdated())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� modificare un dettaglio di una " + riga.getFattura_passiva().getDescrizioneEntita() + " gi� pagata.");
	if (riga.isPagata() && riga.isToBeUpdated())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� modificare un dettaglio gi� pagato.");

	//Il controllo � stato eliminato a seguito della richiesta 423
	//if (riga.getFattura_passiva().isStampataSuRegistroIVA() && 
		//riga.getCrudStatus() != OggettoBulk.NORMAL &&
		//!(riga instanceof Fattura_passiva_rigaIBulk)) { /* aggiunto per richiesta 423 */

		//Fattura_passiva_rigaBulk original = null;
		//try {
			//original = (Fattura_passiva_rigaBulk)getHome(aUC, riga).findByPrimaryKey(riga);
		//} catch (it.cnr.jada.persistency.PersistencyException e) {
			//throw handleException(riga, e);
		//}
		//if (original != null) {
			//if (original.getIm_imponibile().compareTo(riga.getIm_imponibile()) != 0 ||
				//original.getIm_iva().compareTo(riga.getIm_iva()) != 0 ||
				//(original.getIm_diponibile_nc() != null && original.getIm_diponibile_nc().compareTo(riga.getIm_totale_divisa()) != 0) ||
				//original.getCd_voce_iva().compareTo(riga.getCd_voce_iva()) != 0)

				//throw new it.cnr.jada.comp.ApplicationException("Attenzione: questa modifica non � permessa quando lo stato IVA � B o C.");
		//}
	//}

	if (riga.getVoce_iva() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare una voce IVA per il dettaglio.");
	if (riga.isVoceIVAOnlyIntraUE() && 
		(riga.getVoce_iva().getFl_intra() != null &&
		!riga.getVoce_iva().getFl_intra().booleanValue()))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare una voce IVA per il dettaglio - "+riga.getDs_riga_fattura());
	
	if (riga.getFattura_passiva()!= null && riga.getFattura_passiva().isCommerciale() &&
			riga.getFattura_passiva().getFornitore()!= null && riga.getFattura_passiva().getFornitore().getAnagrafico()!=null &&
			riga.getFattura_passiva().getFornitore().getAnagrafico().getPartita_iva()!=null &&
			riga.getFattura_passiva().getFornitore().getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.ITALIA)==0  && 
			riga.getVoce_iva()!= null  && riga.getVoce_iva().getNaturaOperNonImpSdi()!=null &&
			riga.getVoce_iva().getNaturaOperNonImpSdi().compareTo(Voce_ivaBulk.REVERSE_CHARGE)==0)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: specificare una voce IVA che genera autofattura per il dettaglio - "+riga.getDs_riga_fattura());
	if (riga.getFattura_passiva()!= null && riga.getFattura_passiva().isIstituzionale() &&
			riga.getVoce_iva()!= null  && riga.getVoce_iva().getNaturaOperNonImpSdi()!=null &&
			riga.getVoce_iva().getNaturaOperNonImpSdi().compareTo(Voce_ivaBulk.REVERSE_CHARGE)==0)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: voce IVA non valida per il dettaglio - "+riga.getDs_riga_fattura());
	
	
}
private void validateFornitore(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws it.cnr.jada.bulk.ValidationException {

	if (fatturaPassiva.getDt_fattura_fornitore() == null)
		throw new it.cnr.jada.bulk.ValidationException("La data di emissione della " + fatturaPassiva.getDescrizioneEntita() + " del fornitore non pu� essere vuota.");
	if (fatturaPassiva.getFornitore() == null || fatturaPassiva.getFornitore().getCrudStatus() != OggettoBulk.NORMAL)
		throw new it.cnr.jada.bulk.ValidationException("Selezionare un fornitore!");
	if (fatturaPassiva.getIm_totale_quadratura() == null)
		throw new it.cnr.jada.bulk.ValidationException("Impostare il totale fattura in euro.");

	if (fatturaPassiva.getFornitore().getDt_fine_rapporto() != null) {
		java.util.Calendar dataFineRapporto = java.util.Calendar.getInstance();
		dataFineRapporto.setTime(new java.util.Date(fatturaPassiva.getFornitore().getDt_fine_rapporto().getTime()));
		dataFineRapporto.set(java.util.Calendar.HOUR, 0);
		dataFineRapporto.set(java.util.Calendar.MINUTE, 0);
		dataFineRapporto.set(java.util.Calendar.SECOND, 0);
		dataFineRapporto.set(java.util.Calendar.MILLISECOND, 0);
		dataFineRapporto.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		java.util.Calendar dataEmissioneFattura = java.util.Calendar.getInstance();
		dataEmissioneFattura.setTime(new java.util.Date(fatturaPassiva.getDt_fattura_fornitore().getTime()));
		dataEmissioneFattura.set(java.util.Calendar.HOUR, 0);
		dataEmissioneFattura.set(java.util.Calendar.MINUTE, 0);
		dataEmissioneFattura.set(java.util.Calendar.SECOND, 0);
		dataEmissioneFattura.set(java.util.Calendar.MILLISECOND, 0);
		dataEmissioneFattura.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);

		if(dataFineRapporto.before(dataEmissioneFattura) &&
			!dataFineRapporto.equals(dataEmissioneFattura))
			throw new it.cnr.jada.bulk.ValidationException("Il rapporto con il fornitore � terminato! Non � possibile salvare il documento.");
	}

	
}
private void validaSequenceDateNumber (UserContext aUC, Fattura_passivaBulk fatturaPassiva) 
	throws ComponentException {
	if (fatturaPassiva.getPg_fattura_passiva() == null || 
		fatturaPassiva.getDt_registrazione() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: inserire il progressivo e la data di registrazione del documento.");
	
	try	{
		Fattura_passivaHome fattpasHome = (Fattura_passivaHome)getHome(aUC,Fattura_passivaBulk.class);
		Timestamp dtMin = fattpasHome.findDataRegFatturaPrecedente(fatturaPassiva);
		
		if (!(dtMin == null) && fatturaPassiva.getDt_registrazione().before(dtMin)){    
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new it.cnr.jada.comp.ApplicationException("Data registrazione inferiore a quella del documento precedentemente registrato " + sdf.format(dtMin) + "!");
		}

		Timestamp dtMax = fattpasHome.findDataRegFatturaSuccessiva(fatturaPassiva);
        
		if (!(dtMax == null) && fatturaPassiva.getDt_registrazione().after(dtMax)) {    
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
			throw new it.cnr.jada.comp.ApplicationException("Data registrazione successiva a quella del documento successivamente registrato " + sdf.format(dtMax) + "!");
		}
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}catch(it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(ex);
	}
}
private void validazioneComune(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException {

	if (!verificaEsistenzaSezionalePer(aUC, fatturaPassiva))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � stato definito un sezionale per le " + fatturaPassiva.getDescrizioneEntitaPlurale() + " e il tipo sezionale \"" + fatturaPassiva.getTipo_sezionale().getDs_tipo_sezionale() + "\"!");
	
	try {
		fatturaPassiva.validateDate();
		
		//Se � una fattura differita, la data di emissione non deve superare
		//quella inserita in configurazione
		if(fatturaPassiva.getFl_liquidazione_differita() && fatturaPassiva.getDt_fattura_fornitore()!=null){
			java.sql.Timestamp data_limite;
			java.sql.Timestamp data_limite_sup;
			try {
				data_limite = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt01(aUC, new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
				data_limite_sup = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt02(aUC, new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
			} catch (RemoteException e) {		
				throw handleException(e); 
			}
			if(fatturaPassiva.getDt_fattura_fornitore().compareTo(data_limite)<0||fatturaPassiva.getDt_fattura_fornitore().compareTo(data_limite_sup)>0){
				fatturaPassiva.setFl_liquidazione_differita(false);
				throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare la liquidazione differita con la data emissione inserita.");
			}
		}
		
		//Verifica la validit� della data di registrazione rispetto all'ultima
		//data di stampa registri IVA
		callVerifyDataRegistrazione(aUC, fatturaPassiva);

        //Verifica che il documento rispetti la sequenza data/numero 
        //di registrazione
	    validaSequenceDateNumber(aUC, fatturaPassiva);

		//Controllo date competenza COGE
		fatturaPassiva.validaDateCompetenza();
		// r.p. 17/09/2015 controllo non ritenuto utile
		// competenze omogenee per dettagli collegate alla stessa scadenza
		//controllaCompetenzaCOGEDettagli(aUC, fatturaPassiva);
		
		validateFornitore(aUC, fatturaPassiva);
	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	}

	Fattura_passivaBulk original = null;
	try {
		original = (Fattura_passivaBulk)getHome(aUC, fatturaPassiva, null, "none").findByPrimaryKey(fatturaPassiva);
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
	if (original != null) {
		if (fatturaPassiva.isStampataSuRegistroIVA() || fatturaPassiva.getProgr_univoco()!=null) {
		//ATTENZIONE: a seguito dell'errore segnalato 569 (dovuto alla richiesta 423) il controllo viene
		//ora eseguito anche se la sola autofattura � stampata sui registri IVA

			if (!original.getDt_registrazione().equals(fatturaPassiva.getDt_registrazione()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile modificare la data registrazione della " + fatturaPassiva.getDescrizioneEntita() + " o della sua autofattura (se esiste) quando � presente sui registri.");
			if (fatturaPassiva.isStampataSuRegistroIVA()){
				if (!original.getCd_tipo_sezionale().equalsIgnoreCase(fatturaPassiva.getCd_tipo_sezionale()) &&
					 hasFatturaPassivaARowNotStateI(fatturaPassiva))
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile modificare il sezionale di " + fatturaPassiva.getDescrizioneEntitaPlurale() + " o della relativa autofattura (se esiste) parzialmente contabilizzate e stato IVA B o C.");
			}
			if (original.getFl_autofattura() != null &&
				!original.getFl_autofattura().equals(fatturaPassiva.getFl_autofattura()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile modificare il tipo di sezionale o la tipologia dei dettagli (bene/servizio) per " + fatturaPassiva.getDescrizioneEntitaPlurale() + " o per la relativa autofattura (se esiste) in stato IVA B o C.");
			
			if (fatturaPassiva.isStampataSuRegistroIVA() || fatturaPassiva.isElettronica()||fatturaPassiva.isGenerataDaCompenso()) 
				if (original.getIm_totale_fattura().compareTo(fatturaPassiva.getIm_totale_fattura()) != 0 )
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si pu� modificare il totale fattura quando la fattura � elettronica e/o presente sui sezionali iva");
			
			if (!original.getNr_fattura_fornitore().equalsIgnoreCase(fatturaPassiva.getNr_fattura_fornitore()) ||
				!original.getDt_fattura_fornitore().equals(fatturaPassiva.getDt_fattura_fornitore()) ||
				//original.getIm_totale_fattura().compareTo(fatturaPassiva.getIm_totale_fattura()) != 0 ||
				!original.getFl_intra_ue().equals(fatturaPassiva.getFl_intra_ue()) ||
				!original.getFl_extra_ue().equals(fatturaPassiva.getFl_extra_ue()) ||
				!original.getFl_san_marino_con_iva().equals(fatturaPassiva.getFl_san_marino_con_iva()) ||
				!original.getFl_san_marino_senza_iva().equals(fatturaPassiva.getFl_san_marino_senza_iva()))// ||
				//!original.getCd_tipo_sezionale().equalsIgnoreCase(fatturaPassiva.getCd_tipo_sezionale()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare campi relativi alla " + fatturaPassiva.getDescrizioneEntita() + " del fornitore quando la fattura o la relativa autofattura (se esiste).");

			if (!original.getCd_divisa().equals(fatturaPassiva.getCd_divisa()) ||
				original.getCambio().compareTo(fatturaPassiva.getCambio()) != 0)
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare i campi della valuta o del cambio della " + fatturaPassiva.getDescrizioneEntita());
				
			if (!original.getCd_terzo().equals(fatturaPassiva.getCd_terzo()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare campi relativi al fornitore della " + fatturaPassiva.getDescrizioneEntita());
		}

		if (!fatturaPassiva.isStampataSuRegistroIVA() &&
			fatturaPassiva.getProgr_univoco()==null &&	
			fatturaPassiva.isPagata() &&
			!original.getCd_terzo().equals(fatturaPassiva.getCd_terzo()))
				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si possono modificare campi relativi al fornitore della " + fatturaPassiva.getDescrizioneEntita());

		//Controllo se la fattura salvata era una fattura estera collegabile
		//a fatture spedizioniere o bolle e controllo che siano ancora valide
		//se gi� collegate
		if (fatturaPassiva instanceof Fattura_passiva_IBulk &&
			(original.getFl_extra_ue() != null && original.getFl_extra_ue().booleanValue() &&
			 original.FATTURA_DI_BENI.equalsIgnoreCase(original.getTi_bene_servizio()))) {
			if ((fatturaPassiva.getFl_extra_ue() == null || !fatturaPassiva.getFl_extra_ue().booleanValue()) ||
			 	!fatturaPassiva.FATTURA_DI_BENI.equalsIgnoreCase(fatturaPassiva.getTi_bene_servizio())) {

				Fattura_passiva_IBulk fp = (Fattura_passiva_IBulk)fatturaPassiva;
				Fattura_passiva_IHome fpHome = (Fattura_passiva_IHome)getHome(aUC, fatturaPassiva);
				try {
					if (fpHome.selectBolleDoganaliPer(fp).executeExistsQuery(getConnection(aUC)) ||
						fpHome.selectSpedizionieriPer(fp).executeExistsQuery(getConnection(aUC)))
					throw new ApplicationException("La fattura estera � collegata a fatture di tipo spedizioniere o bolle doganali. I cambiamenti apportati non sono validi. Operazione annullata!");
				} catch (SQLException e) {
					throw handleException(fp, e);
				}
			 }
		}
		
		//Controllo se la fattura salvata era una fattura estera con 1210 -->
		//non posso cambiare tipo
		if (original.isEstera() && !fatturaPassiva.isEstera() && fatturaPassiva.getPg_lettera() != null)
			throw new ApplicationException("La fattura era estera: i cambiamenti apportati non sono validi perch� � gi� stato emesso un documento 1210. Operazione annullata!");
		
		java.util.List originalRows = null;
		try {
			Fattura_passiva_rigaBulk clause = null;
			if (fatturaPassiva instanceof Fattura_passiva_IBulk)
				clause = new Fattura_passiva_rigaIBulk();
			else if (fatturaPassiva instanceof Nota_di_creditoBulk)
				clause = new Nota_di_credito_rigaBulk();
			else clause = new Nota_di_debito_rigaBulk();
				
			clause.setFattura_passiva(original);
			originalRows = getHome(aUC, clause, null, "solo_voce_iva").find(clause);
			getHomeCache(aUC).fetchAll(aUC);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(fatturaPassiva, e);
		}
		
		//Modificato a seguito richiesta 423
		if (originalRows != null && (fatturaPassiva.isStampataSuRegistroIVA() || fatturaPassiva.getProgr_univoco()!=null)) {
			//if (!(fatturaPassiva instanceof Fattura_passiva_IBulk) &&
				//originalRows.size() != fatturaPassiva.getFattura_passiva_dettColl().size())
				//throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile aggiungere, eliminare o modificare i dettagli quando lo stato IVA della " + fatturaPassiva.getDescrizioneEntita() + " � B o C.");
			//else {
				original.setFattura_passiva_dettColl(new BulkList(originalRows));
				for (Iterator i = ((Fattura_passivaBulk)original).getFattura_passiva_dettColl().iterator(); i.hasNext();)
					((Fattura_passiva_rigaBulk)i.next()).calcolaCampiDiRiga();
				original.setChangeOperationOn(original.getValuta());
				validaConConsuntivi(
								aUC, 
								original, 
								fatturaPassiva);
			//}
		}
	}
}
//^^@@
/** 
  *  Verifica l'esistenza e apertura dell'inventario
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentita l'attivit� richiesta
  *  L'inventario non esiste
  *    PreCondition:
  *      L'inventario per CDS e UO correnti non esiste
  *    PostCondition:
  *      Viene visualizzato messaggio "non esiste un inventario per questo CDS"
  *  L'inventario non � aperto
  *    PreCondition:
  *      L'inventario per CDS e UO correnti esiste ma non � aperto
  *    PostCondition:
  *      Viene visualizzato messaggio "l'inventario per questo CDS non � aperto"
 */
//^^@@

public void verificaEsistenzaEdAperturaInventario(
	UserContext userContext, 
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	try {
		it.cnr.contab.inventario00.ejb.IdInventarioComponentSession h = (it.cnr.contab.inventario00.ejb.IdInventarioComponentSession)
														it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
																	"CNRINVENTARIO00_EJB_IdInventarioComponentSession",
																	it.cnr.contab.inventario00.ejb.IdInventarioComponentSession.class);
		it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = h.findInventarioFor(
																					userContext,
																					fatturaPassiva.getCd_cds_origine(),
																					fatturaPassiva.getCd_uo_origine(),
																					false);
		if (inventario == null)
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che non esiste un inventario per questo CDS.\nIn caso di inserimento di dettagli con beni soggetti ad inventario, non sar� permesso il salvataggio della fattura,\nfino alla creazione ed apertura di un nuovo inventario!");
		else if (!h.isAperto(userContext, inventario, fatturaPassiva.getEsercizio())) {
			throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che l'inventario per questo CDS non � aperto.\nNel caso di inserimento di dettagli con beni soggetti ad inventario, non sar� permesso il salvataggio della fattura\nfino ad apertura di quest'ultimo!");
		}
	} catch (Exception e) {
		throw handleException(fatturaPassiva, e);
	}
}

private boolean verificaEsistenzaSezionalePer(
	UserContext userContext, 
	Fattura_passivaBulk fatturaPassiva)
	throws ComponentException {

	if (fatturaPassiva != null && fatturaPassiva.getTipo_sezionale() != null) {

		SezionaleBulk key = new SezionaleBulk(
									fatturaPassiva.getCd_cds_origine(), 
									fatturaPassiva.getTipo_sezionale().getCd_tipo_sezionale(), 
									fatturaPassiva.getCd_uo_origine(), 
									fatturaPassiva.getEsercizio(), 
									fatturaPassiva.getTi_fattura());
		try {
			return getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.SezionaleBulk.class).findByPrimaryKey(key) != null;
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw handleException(fatturaPassiva, e);
		}
	}
	return false;
	}
//	  ^^@@
	/** 
	  *  
	 */
//	  ^^@@

	private void verificaStatoEsercizi(UserContext aUC, Fattura_passivaBulk documento) throws ComponentException {

		if (!verificaStatoEsercizio(aUC, new EsercizioBulk( documento.getCd_cds(), it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC))))
			throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!"); 	  
		
		if(documento.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()) {
			 throw new it.cnr.jada.comp.ApplicationException("Operazione non permessa!");
		}
    
		if (documento.getEsercizio().intValue() < it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()){	
			// Gennaro Borriello - (11/10/2004 17.29.46)
			//	Aggiunto controllo per i documenti RIPORTATI, in base all'esercizio COEP precedente
			//	all'es. di scrivania.
			//
			// Gennaro Borriello - (02/11/2004 16.48.21)
			// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
			if (documento.isRiportataInScrivania() ){
				Integer es_prec = new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()-1);
				if (!isEsercizioCoepChiusoFor(aUC, documento, es_prec)){
					throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile eliminare il documento, poich� l'esercizio economico precedente a quello in scrivania non � chiuso.");	
				}
			} 
			else 
			 throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare il documento perch� non risulta riportato nell'esercizio di scrivania!");
		}
}
//^^@@
/** 
  *  Verifica l'esistenza e apertura dell'esercizio
  *    PreCondition:
  *      Nessuna condizione di errore rilevata.
  *    PostCondition:
  *      Viene consentita l'attivit� richiesta
  *  L'esercizio non � aperto
  *    PreCondition:
  *      L'esercizio su cui insiste il controllo non � aperto
  *    PostCondition:
  *      Viene notificato l'error
 */
//^^@@

public boolean verificaStatoEsercizio(
	UserContext userContext,
	EsercizioBulk anEsercizio) 
	throws ComponentException {

	try {
		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, EsercizioBulk.class);
		return !eHome.isEsercizioChiuso(
									userContext,
									anEsercizio.getEsercizio(),
									anEsercizio.getCd_cds());
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
private boolean verificaEsistenzaAumentiValori(Fattura_passivaBulk fatturaPassiva){
	boolean aumento=false;
	if ((fatturaPassiva.getAssociazioniInventarioHash()!=null) &&(!fatturaPassiva.getAssociazioniInventarioHash().isEmpty())){	
		for (java.util.Enumeration e = ((AssociazioniInventarioTable)fatturaPassiva.getAssociazioniInventarioHash()).keys(); e.hasMoreElements();) {
			Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
			if (ass.isPerAumentoValore())
				aumento=true;
		}
	}
	return aumento;
	}

// aggiunto per testare l'esercizio della data competenza da/a
public boolean isEsercizioChiusoPerDataCompetenza(UserContext userContext,Integer esercizio,String cd_cds) throws ComponentException, PersistencyException {
	try {
			
		LoggableStatement cs =new LoggableStatement(getConnection(userContext),"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +	"CNRCTB008.isEsercizioChiusoYesNo(?,?)}",false,this.getClass());		

		try {
			cs.registerOutParameter( 1, java.sql.Types.CHAR);
			cs.setObject(2,esercizio);		
			cs.setObject(3,cd_cds);		
			
			cs.execute();

			return "Y".equals(cs.getString(1));
		} finally {
			cs.close();
		}
	} catch (java.sql.SQLException e) {
		throw handleSQLException(e);
	}
}
public TerzoBulk findCessionario(UserContext userContext, Fattura_passiva_rigaBulk fattura_riga) throws ComponentException {

	try	{
		if (fattura_riga == null || fattura_riga.getModalita_pagamento() == null)
			return null;
		Modalita_pagamentoHome mph = (Modalita_pagamentoHome)getHome(userContext, Modalita_pagamentoBulk.class);
		Modalita_pagamentoBulk mp = (Modalita_pagamentoBulk)mph.find(new Modalita_pagamentoBulk(fattura_riga.getModalita_pagamento().getCd_modalita_pag(), fattura_riga.getCd_terzo())).get(0);
		if (mp == null || fattura_riga.getBanca()==null || fattura_riga.getBanca().getCd_terzo_delegato() == null) return null;
		TerzoHome th = (TerzoHome)getHome(userContext, TerzoBulk.class);
		return (TerzoBulk)th.findByPrimaryKey(new TerzoBulk(fattura_riga.getBanca().getCd_terzo_delegato() ));
	} catch( Exception e ) {
		throw handleException(e);
	}		
}
private void controllaOmogeneitaTraTerzi(
		it.cnr.jada.UserContext userContext,
		IScadenzaDocumentoContabileBulk scadenza,
		Vector dettagli)
		throws ApplicationException {

		//SEGNALAZIONE ERRORI n� 631 del 22/08/2003
		
		if (dettagli != null && !dettagli.isEmpty() && dettagli.size() != 1) {
			Fattura_passiva_rigaBulk primoDettaglio = null;
			for (Enumeration e = dettagli.elements(); e.hasMoreElements();) {
				Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)e.nextElement();
				if (primoDettaglio == null)
					primoDettaglio = dettaglio;
				else {
					TerzoBulk unTerzo = dettaglio.getFornitore();
					if (!primoDettaglio.getFornitore().equalsByPrimaryKey(unTerzo))
		               	throw new ApplicationException("Attenzione: i terzi della scadenza " + scadenza.getDs_scadenza() + " non sono compatibili! Operazione interrotta.");
		           // if (dettaglio.getFattura_passiva() instanceof Nota_di_creditoBulk) {
		            	/*
						if (!dettaglio.getModalita_pagamento_uo_cds().equalsByPrimaryKey(primoDettaglio.getModalita_pagamento_uo_cds()))
			               	throw new ApplicationException("Attenzione: le modalit� di pagamento del dettaglio \"" + dettaglio.getDs_riga() + "\" non sono compatibili con le altre modalit� di pagamento insistenti sulla scadenza \"" + scadenza.getDs_scadenza() + "\"!");
			            //Errore 704: controllo aggiunto per correggere comportamento anomalo di
			            //mandati e reversali su richiesta di Paolo. 01/12/2003
						if (!dettaglio.getBanca_uo_cds().equalsByPrimaryKey(primoDettaglio.getBanca_uo_cds()))
			               	throw new ApplicationException("Attenzione: la banca d'appoggio del dettaglio \"" + dettaglio.getDs_riga() + "\" non � compatibile con la banca insistente sulla scadenza \"" + scadenza.getDs_scadenza() + "\"!");
			            */
		            //} else {
		            	// r.p. commentato il codice seguente non coerente con equitalia
			            //if (!dettaglio.getModalita_pagamento().equalsByPrimaryKey(primoDettaglio.getModalita_pagamento()))
			              // 	throw new ApplicationException("Attenzione: le modalit� di pagamento del dettaglio \"" + dettaglio.getDs_riga_fattura() + "\" non sono compatibili con le altre modalit� di pagamento insistenti sulla scadenza \"" + scadenza.getDs_scadenza() + "\"!");
			            //Errore 704: controllo aggiunto per correggere comportamento anomalo di
			            //mandati e reversali su richiesta di Paolo. 01/12/2003
						//if (!dettaglio.getBanca().equalsByPrimaryKey(primoDettaglio.getBanca()))
			              // 	throw new ApplicationException("Attenzione: la banca d'appoggio del dettaglio \"" + dettaglio.getDs_riga_fattura() + "\" non � compatibile con la banca insistente sulla scadenza \"" + scadenza.getDs_scadenza() + "\"!");
					//}
				}
			}
		}
	}
public java.util.List findListaFattureSIP(UserContext userContext,String query,String dominio,String uo,
		String terzo,String voce,String cdr,String gae,String tipoRicerca,Timestamp
		 data_inizio ,Timestamp data_fine)throws ComponentException{
	try {	
		
		VFatturaPassivaSIPHome home = (VFatturaPassivaSIPHome)getHome(userContext, VFatturaPassivaSIPBulk.class,"VFATTURAPASSIVASIP_RID");
		SQLBuilder sql = home.createSQLBuilder();
		sql.setDistinctClause( true );
		if(data_inizio!=null && data_fine !=null){
			sql.addSQLClause("AND","DT_PAGAMENTO",SQLBuilder.GREATER_EQUALS,data_inizio);
			sql.addSQLClause("AND","DT_PAGAMENTO",SQLBuilder.LESS_EQUALS,data_fine);

		}
		if(uo!=null)
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS,uo);
		if(terzo!=null)
			sql.addSQLClause("AND","CD_TERZO",SQLBuilder.EQUALS,terzo);
		if(voce!=null)
			sql.addSQLClause("AND","CD_ELEMENTO_VOCE",SQLBuilder.EQUALS,voce);
		if(cdr!=null)
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS,cdr);
		if(gae!=null)
			sql.addSQLClause("AND","GAE",SQLBuilder.EQUALS,gae);
		if (dominio.equalsIgnoreCase("pg_fattura"))
			sql.addSQLClause("AND","PG_FATTURA",SQLBuilder.EQUALS,query);
		else if (dominio.equalsIgnoreCase("descrizione")){
				for(StringTokenizer stringtokenizer = new StringTokenizer(query, " "); stringtokenizer.hasMoreElements();){
					String queryDetail = stringtokenizer.nextToken();
					if ((tipoRicerca != null && tipoRicerca.equalsIgnoreCase("selettiva"))|| tipoRicerca == null){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail)))
							sql.addSQLClause("AND","DS_FATTURA_PASSIVA",SQLBuilder.CONTAINS,queryDetail);
						else{
							sql.openParenthesis("AND");
							sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.CONTAINS,queryDetail);
							sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.CONTAINS,RemoveAccent.convert(queryDetail));
							sql.closeParenthesis();
						}	
					}else if (tipoRicerca.equalsIgnoreCase("puntuale")){
						if (queryDetail.equalsIgnoreCase(RemoveAccent.convert(queryDetail))){
							sql.openParenthesis("AND");
							  sql.addSQLClause("AND","UPPER(DS_FATTURA_PASSIVA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							  sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.STARTSWITH,queryDetail+" ");
							  sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.ENDSWITH," "+queryDetail);
							sql.closeParenthesis();  
						}else{
							sql.openParenthesis("AND");
							  sql.openParenthesis("AND");
							    sql.addSQLClause("OR","UPPER(DS_FATTURA_PASSIVA)",SQLBuilder.EQUALS,queryDetail.toUpperCase());
							    sql.addSQLClause("OR","UPPER(DS_FATTURA_PASSIVA)",SQLBuilder.EQUALS,RemoveAccent.convert(queryDetail).toUpperCase());
							  sql.closeParenthesis();
							  sql.openParenthesis("OR");							  
							    sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.STARTSWITH,queryDetail+" ");
							    sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.STARTSWITH,RemoveAccent.convert(queryDetail)+" ");
							  sql.closeParenthesis();  
							  sql.openParenthesis("OR");
							    sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.ENDSWITH," "+queryDetail);
							    sql.addSQLClause("OR","DS_FATTURA_PASSIVA",SQLBuilder.ENDSWITH," "+RemoveAccent.convert(queryDetail));
							  sql.closeParenthesis();  
							sql.closeParenthesis();  
						}
					}
				}
				sql.addOrderBy("DS_FATTURA_PASSIVA");
			}
		return home.fetchAll(sql);
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
public boolean isDettaglioInviato(
		UserContext userContext,
		Fattura_passiva_rigaBulk fattura_passiva_riga)
		throws ComponentException {
	try {
		Obbligazione_scadenzarioBulk obb_scad = fattura_passiva_riga.getObbligazione_scadenziario();
		if(obb_scad!=null){
			Obbligazione_scadenzarioHome home = (Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class);		
			Mandato_rigaBulk mandato_riga = home.findMandato(obb_scad);
			if (mandato_riga!=null){
				MandatoIHome mHome = (MandatoIHome)getHome(userContext, MandatoIBulk.class);
				MandatoBulk mKey = new MandatoBulk(mandato_riga.getCd_cds(),
	                			          		mandato_riga.getEsercizio(),
	                			          		mandato_riga.getPg_mandato());
				MandatoIBulk man = (MandatoIBulk)mHome.findByPrimaryKey(mKey);
	
				if (man.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO))
					return true;
				else
					return false;
			}
			return false;
		}else
		{
			if (fattura_passiva_riga instanceof Nota_di_credito_rigaBulk) {
   			 Accertamento_scadenzarioBulk acc_scad = ((Nota_di_credito_rigaBulk)fattura_passiva_riga).getAccertamento_scadenzario();
   			 if(acc_scad!=null){
	   			Accertamento_scadenzarioHome home = (Accertamento_scadenzarioHome)getHome(userContext, Accertamento_scadenzarioBulk.class);		
				Reversale_rigaBulk reversale_riga = home.findReversale(acc_scad);
				if (reversale_riga!=null){
					ReversaleIHome rHome = (ReversaleIHome)getHome(userContext, ReversaleIBulk.class);
					ReversaleBulk rKey = new ReversaleBulk(reversale_riga.getCd_cds(),
							reversale_riga.getEsercizio(),
							reversale_riga.getPg_reversale());
					ReversaleIBulk rev = (ReversaleIBulk)rHome.findByPrimaryKey(rKey);
		
					if (rev.getStato_trasmissione().equals(MandatoBulk.STATO_TRASMISSIONE_TRASMESSO))
						return true;
					else
						return false;
				}
   			 }
				
		  }
			return false;
	}
	} catch (IntrospectionException e) {
		throw handleException(e);
	} catch (PersistencyException e) {
		throw handleException(e);
	}
}

private void aggiornaDataEsigibilitaIVA(
		UserContext userContext,
		Fattura_passivaBulk fatturaPassiva,
		String creaModifica)
		throws ComponentException {
	
		if (creaModifica.compareTo("C")!=0 && (fatturaPassiva == null || fatturaPassiva.isStampataSuRegistroIVA()|| fatturaPassiva.getProgr_univoco()!=null))
		    return;
		try {
			for (Iterator i = fatturaPassiva.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
				Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
				if (!isDettaglioInviato(userContext,dettaglio))
				{
					if (fatturaPassiva.getFl_liquidazione_differita().booleanValue()) {
						dettaglio.setData_esigibilita_iva(null);
					} else
					dettaglio.setData_esigibilita_iva(fatturaPassiva.getDt_registrazione());
				
					updateBulk(userContext, dettaglio);
				}	
			}		
		} catch (PersistencyException e) {
				throw handleException(e);
		}
	}
public List findManRevRigaCollegati (UserContext userContext, Fattura_passiva_rigaBulk fatturaPassivaRiga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
{
	List result=null;
	if (fatturaPassivaRiga.getObbligazione_scadenziario() != null &&
		fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione() !=null &&
		fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getCd_cds () !=null && 
		fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio()!=null &&
		fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio_originale()!=null &&
		fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getPg_obbligazione()!=null &&
		fatturaPassivaRiga.getObbligazione_scadenziario().getPg_obbligazione_scadenzario()!=null) { 
		SQLBuilder sql = getHome( userContext, Mandato_rigaIBulk.class ).createSQLBuilder();
		sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_cds() );
		sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_unita_organizzativa() );
		sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getEsercizio() );
		sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA );
		sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getPg_fattura_passiva() );
		sql.addClause(FindClause.AND, "cd_cds_obbligazione", SQLBuilder.EQUALS, fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getCd_cds() );
		sql.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio() );
		sql.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio_originale() );
		sql.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, fatturaPassivaRiga.getObbligazione_scadenziario().getObbligazione().getPg_obbligazione() );
		sql.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, fatturaPassivaRiga.getObbligazione_scadenziario().getPg_obbligazione_scadenzario() );
	
        result = getHome( userContext, Mandato_rigaIBulk.class ).fetchAll( sql );
	} else if (fatturaPassivaRiga.getCd_cds_accertamento()!=null && 
 		   	   fatturaPassivaRiga.getEsercizio_accertamento()!=null && 
    		   fatturaPassivaRiga.getEsercizio_ori_accertamento()!=null &&
    		   fatturaPassivaRiga.getPg_accertamento()!=null &&
    		   fatturaPassivaRiga.getPg_accertamento_scadenzario()!=null) { 
		SQLBuilder sql = getHome( userContext, Reversale_rigaIBulk.class ).createSQLBuilder();
    	sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_cds() );
    	sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_unita_organizzativa() );
        sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getEsercizio() );
    	sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA );
    	sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, fatturaPassivaRiga.getPg_fattura_passiva() );
    	sql.addClause(FindClause.AND, "cd_cds_accertamento", SQLBuilder.EQUALS, fatturaPassivaRiga.getCd_cds_accertamento() );
    	sql.addClause(FindClause.AND, "esercizio_accertamento", SQLBuilder.EQUALS, fatturaPassivaRiga.getEsercizio_accertamento() );
    	sql.addClause(FindClause.AND, "esercizio_ori_accertamento", SQLBuilder.EQUALS, fatturaPassivaRiga.getEsercizio_ori_accertamento() );
    	sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, fatturaPassivaRiga.getPg_accertamento() );
    	sql.addClause(FindClause.AND, "pg_accertamento_scadenzario", SQLBuilder.EQUALS, fatturaPassivaRiga.getPg_accertamento_scadenzario() );
    		
    	result = getHome( userContext, Reversale_rigaIBulk.class ).fetchAll( sql );
	}
	return result;
}
private Fattura_passivaBulk completeWithModalitaIncasso(UserContext userContext, Fattura_passivaBulk fatturaPassiva) throws ComponentException {
	Modalita_incassoHome home = (Modalita_incassoHome)getHome(userContext, Modalita_incassoBulk.class);
	try {
		fatturaPassiva.setModalita_incassoColl(home.fetchAll(home.selectByClause(userContext, null)));
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
return fatturaPassiva;
}
private Fattura_passivaBulk completeWithModalitaErogazione(UserContext userContext, Fattura_passivaBulk fatturaPassiva) throws ComponentException {
	Modalita_erogazioneHome home = (Modalita_erogazioneHome)getHome(userContext, Modalita_erogazioneBulk.class);
	try {
		fatturaPassiva.setModalita_erogazioneColl(home.fetchAll(home.selectByClause(userContext, null)));
	} catch (it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(fatturaPassiva, e);
	}
return fatturaPassiva;
}
/**
 * Metodo che serve per ricostruire sul documento alcune HashTable necessarie per il funzionamento
 * del Business Process
 *
 * Pre:  E' stata modificato il documento o alcuni suoi dettagli  
 * Post: Viene ricaricata l'HashTable delle Obbligazioni associate alle Nota Credito  
 * o degli Accertamenti associati alla fattura attiva      
 *
 * @param userContext
 * @param bulk fattura o nota credito da aggiornare
 * @return fattura o nota credito aggiornato con le modifiche
 * @throws it.cnr.jada.comp.ComponentException
 */
public OggettoBulk rebuildDocumento(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
	 if (bulk instanceof Fattura_passivaBulk){
		((Fattura_passivaBulk)bulk).setFattura_passiva_obbligazioniHash(null);
		rebuildObbligazioni(userContext, (Fattura_passivaBulk)bulk);
	    }else
	if (bulk instanceof Nota_di_creditoBulk) {
		rebuildAccertamenti(userContext, (Nota_di_creditoBulk)bulk);
    } 
	return bulk;
}
private Fattura_passivaBulk ricercaFatturaTrovato(
		UserContext userContext,
		Long esercizio,
		String cd_cds,
		String cd_unita_organizzativa,
		Long pg_fattura,
		boolean byKey) throws PersistencyException, ComponentException {
try{
	Fattura_passiva_IBulk fatturaPassiva = new Fattura_passiva_IBulk();
	fatturaPassiva.setEsercizio(esercizio.intValue());
	if (byKey) {
		fatturaPassiva.setCd_cds(cd_cds);
		fatturaPassiva.setCd_unita_organizzativa(cd_unita_organizzativa);
	} else {
		fatturaPassiva.setCd_cds_origine(cd_cds);
		fatturaPassiva.setCd_uo_origine(cd_unita_organizzativa);
	}
	fatturaPassiva.setPg_fattura_passiva(pg_fattura);
	
	List fatture = 	(getHome(userContext, Fattura_passiva_IBulk.class).find(fatturaPassiva));
	if (fatture.size()==0)
		fatturaPassiva=null;
	else if (fatture.size()==1){
		fatturaPassiva=(Fattura_passiva_IBulk)fatture.get(0);
		BulkList dettagli = new BulkList(findDettagli(userContext, fatturaPassiva));
		BulkList dettagli_intra = new BulkList(findDettagliIntrastat(userContext, fatturaPassiva));
		fatturaPassiva.setFattura_passiva_dettColl(dettagli);
		fatturaPassiva.setFattura_passiva_intrastatColl(dettagli_intra);
	}
	else  //non dovrebbe capitare mai!
		throw new FatturaNonTrovataException("Fattura non trovata!");
	
	Nota_di_creditoBulk ncPassiva=null;
	if(fatturaPassiva==null){
		ncPassiva = new Nota_di_creditoBulk();
		ncPassiva.setEsercizio(esercizio.intValue());
		if (byKey) {
			ncPassiva.setCd_cds(cd_cds);
			ncPassiva.setCd_unita_organizzativa(cd_unita_organizzativa);
		} else {
			ncPassiva.setCd_cds_origine(cd_cds);
			ncPassiva.setCd_uo_origine(cd_unita_organizzativa);
		}
		ncPassiva.setPg_fattura_passiva(pg_fattura);
		List notec = 	(getHome(userContext, Nota_di_creditoBulk.class).find(ncPassiva));
		if (notec.size()==0)
			ncPassiva=null;
		else if (notec.size()==1){
			ncPassiva=(Nota_di_creditoBulk)notec.get(0);
			BulkList dettagli= new BulkList(findDettagli(userContext, ncPassiva));
			ncPassiva.setFattura_passiva_dettColl(dettagli);
		}
		else  //non dovrebbe capitare mai!
			throw new FatturaNonTrovataException("Fattura non trovata!");
	}
	
	caricaDettagliFatturaTrovato(userContext, fatturaPassiva);	

	if (fatturaPassiva!=null)
		return fatturaPassiva;
	else if (ncPassiva!=null)	
			return ncPassiva;
	else
		throw new FatturaNonTrovataException("Fattura non trovata!");
	} catch (IntrospectionException e) {
		throw handleException(e);
	}
}
private void caricaDettagliFatturaTrovato(UserContext userContext, BulkList<Fattura_passivaBulk> fatture) throws ComponentException, PersistencyException {
	for (Iterator<Fattura_passivaBulk> i = fatture.iterator(); i.hasNext(); ) {
		Fattura_passivaBulk fattura=(Fattura_passivaBulk)i.next();
		caricaDettagliFatturaTrovato(userContext, fattura);
	}
}
private void caricaDettagliFatturaTrovato(UserContext userContext, Fattura_passivaBulk fattura) throws ComponentException, PersistencyException {
	if (fattura!=null) {
		BulkList<Fattura_passiva_rigaIBulk> dett = fattura.getFattura_passiva_dettColl();
		for (Iterator<Fattura_passiva_rigaIBulk> j = dett.iterator(); j.hasNext(); ) {
			Fattura_passiva_rigaIBulk det = (Fattura_passiva_rigaIBulk) j.next();
			det.setVoce_iva((Voce_ivaBulk)getHome(userContext, Voce_ivaBulk.class).findByPrimaryKey(det.getVoce_iva()));

			if (det.getTi_associato_manrev() != null && det.ASSOCIATO_A_MANDATO.equalsIgnoreCase(det.getTi_associato_manrev())) {
				SQLBuilder sql = getHome( userContext, Mandato_rigaIBulk.class ).createSQLBuilder();
				sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, det.getCd_cds() );
				sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, det.getCd_unita_organizzativa() );
				sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, det.getEsercizio() );
				sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, Numerazione_doc_ammBulk.TIPO_FATTURA_PASSIVA );
				sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, det.getPg_fattura_passiva() );
				sql.addClause(FindClause.AND, "stato", SQLBuilder.NOT_EQUALS, Mandato_rigaBulk.STATO_ANNULLATO);
				List result = getHome( userContext, Mandato_rigaIBulk.class ).fetchAll( sql );
				BulkList bl = det.getMandatiRighe();
				for (Iterator k = result.iterator(); k.hasNext(); ) {
					Mandato_rigaIBulk manr = (Mandato_rigaIBulk)k.next();
					manr.setMandato((MandatoIBulk)getHome(userContext, MandatoIBulk.class).findByPrimaryKey(manr.getMandato()));
					bl.add(manr);
				}
	        }
		}
	}
}
public Fattura_passivaBulk ricercaFatturaTrovato(
		UserContext userContext,
		Long esercizio,
		String cd_cds,
		String cd_unita_organizzativa,
		Long pg_fattura) throws PersistencyException, ComponentException {
	return ricercaFatturaTrovato(
			userContext,
			esercizio,
			cd_cds,
			cd_unita_organizzativa,
			pg_fattura,
			false);
}
public Fattura_passivaBulk ricercaFatturaByKey(
		UserContext userContext,
		Long esercizio,
		String cd_cds,
		String cd_unita_organizzativa,
		Long pg_fattura) throws PersistencyException, ComponentException {
	return ricercaFatturaTrovato(
			userContext,
			esercizio,
			cd_cds,
			cd_unita_organizzativa,
			pg_fattura,
			true);
}
public List<Fattura_passivaBulk> ricercaFattureTrovato(
		UserContext userContext,
		Long trovato) throws PersistencyException, ComponentException {
	try{
		Fattura_passiva_rigaIBulk fatturaPassivaRiga = new Fattura_passiva_rigaIBulk();
		fatturaPassivaRiga.setPg_trovato(trovato);
		List fattureRighe =(getHome(userContext, Fattura_passiva_rigaIBulk.class).find(fatturaPassivaRiga));
		BulkList fatture = new BulkList();
		for (Iterator<Fattura_passiva_rigaIBulk> i = fattureRighe.iterator(); i.hasNext(); ) {
			Fattura_passiva_rigaBulk fatr = (Fattura_passiva_rigaBulk) i.next();
			
			Fattura_passiva_IBulk fatturaPassiva = new Fattura_passiva_IBulk();
			fatturaPassiva.setEsercizio(fatr.getEsercizio().intValue());
			fatturaPassiva.setCd_cds(fatr.getCd_cds());
			fatturaPassiva.setCd_unita_organizzativa(fatr.getCd_unita_organizzativa());
			fatturaPassiva.setPg_fattura_passiva(fatr.getPg_fattura_passiva());
			
			fatturaPassiva = (Fattura_passiva_IBulk) (getHome(userContext, Fattura_passiva_IBulk.class).findByPrimaryKey(fatturaPassiva));
			if (fatturaPassiva==null)
				throw new FatturaNonTrovataException("Fattura non trovata!");
			else {
				BulkList dettagli = new BulkList(findDettagli(userContext, fatturaPassiva));
				BulkList dettagli_intra = new BulkList(findDettagliIntrastat(userContext, fatturaPassiva));
				fatturaPassiva.setFattura_passiva_dettColl(dettagli);
				fatturaPassiva.setFattura_passiva_intrastatColl(dettagli_intra);
			}
			if (!fatture.containsByPrimaryKey(fatturaPassiva))
				fatture.add(fatturaPassiva);
		}
		
		caricaDettagliFatturaTrovato(userContext, fatture);	

		return fatture;
	} catch (IntrospectionException e) {
		throw handleException(e);
	}
}

public TrovatoBulk ricercaDatiTrovato(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException {
	if (trovato != null){
		RicercaTrovato ricercaTrovato;
		try {
			ricercaTrovato = new RicercaTrovato();
			return ricercaTrovato.ricercaDatiTrovato(userContext, trovato);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File in configurazione non trovato "+e.getMessage());
		} catch (IOException e) {
			throw new ApplicationException("Eccezione di IO "+ e.getMessage());
		} catch (ComponentException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("Eccezione generica "+e.getMessage());
		}
	}
	return new TrovatoBulk();
}


public TrovatoBulk ricercaDatiTrovatoValido(it.cnr.jada.UserContext userContext,Long trovato)throws ComponentException,java.rmi.RemoteException,PersistencyException {
	if (trovato != null){
		RicercaTrovato ricercaTrovato;
		try {
			ricercaTrovato = new RicercaTrovato();
			return ricercaTrovato.ricercaDatiTrovato(userContext, trovato, true);
		} catch (FileNotFoundException e) {
			throw new ApplicationException("File in configurazione non trovato "+e.getMessage());
		} catch (IOException e) {
			throw new ApplicationException("Eccezione di IO "+ e.getMessage());
		} catch (ComponentException e) {
			throw e;
		} catch (Exception e) {
			throw new ApplicationException("Eccezione generica "+e.getMessage());
		}
	}
	return new TrovatoBulk();
}

private boolean isObbligatoriaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
	if (voce == null)
		return false;
	return voce.isObbligatoriaIndicazioneTrovato();
}

private boolean isFacoltativaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
	if (voce == null)
		return false;
	return voce.isFacoltativaIndicazioneTrovato();
}

private boolean isInibitaIndicazioneTrovato(Elemento_voceBulk voce) throws ComponentException {
	if (voce == null)
		return false;
	return voce.isInibitaIndicazioneTrovato();
}

private Elemento_voceBulk recuperoVoce(UserContext aUC, Obbligazione_scadenzarioBulk scadenza) throws ComponentException {

	if (scadenza == null || scadenza.getObbligazione()==null)
		return null;
	
	Elemento_voceHome evHome=(Elemento_voceHome)getHome(aUC,Elemento_voceBulk.class);
	SQLBuilder sql= evHome.createSQLBuilder();
	
	sql.addSQLClause("AND","esercizio",SQLBuilder.EQUALS,scadenza.getObbligazione().getEsercizio());
	sql.addSQLClause("AND","ti_appartenenza",SQLBuilder.EQUALS,scadenza.getObbligazione().getTi_appartenenza());
	sql.addSQLClause("AND","ti_gestione",SQLBuilder.EQUALS,scadenza.getObbligazione().getTi_gestione());
	sql.addSQLClause("AND","cd_elemento_voce",SQLBuilder.EQUALS,scadenza.getObbligazione().getCd_elemento_voce());

	try {
		List voce=evHome.fetchAll(sql);
		if (voce.isEmpty())
			return null;
		return (Elemento_voceBulk)voce.get(0);
		
	} catch (PersistencyException e) {
		throw handleException(e);
	}		
}
public void inserisciProgUnivoco(UserContext context,ElaboraNumUnicoFatturaPBulk lancio) throws ComponentException, RemoteException {
	Timestamp dataFine=lancio.getDataRegistrazioneA();
	LoggableStatement cs = null;
	try {
		try	{
			cs = new LoggableStatement(getConnection(context),
				"{ call " +
				it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +
				"CNRCTB100.insProgrUnivocoFatturaPassiva(?, ?)}",false,this.getClass());
			cs.setInt(1, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(context).intValue());
			cs.setDate(2, new java.sql.Date(lancio.getDataRegistrazioneA().getTime()));
			cs.executeQuery();
		} catch (Throwable e) {
			throw handleException(e);
		} finally {
			if (cs != null) cs.close();
}
	} catch (SQLException ex) {
		throw handleException(ex);
	}	  	
}
public void validaFatturaPerCompenso(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException {
	//ripete i controlli fatti nel validaFattura (richiamato al salvataggio) tranne quelli sulla contabilizzazione delle righe e sul pg_fattura
	if (fatturaPassiva.getFattura_passiva_dettColl().isEmpty())
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: per salvare una " + fatturaPassiva.getDescrizioneEntita() + " � necessario inserire almeno un dettaglio");
    for(Iterator i=fatturaPassiva.getFattura_passiva_dettColl().iterator();i.hasNext();)
    {
      Fattura_passiva_rigaBulk riga=(Fattura_passiva_rigaBulk)i.next();
      validaRiga(aUC, riga);


    }
	searchDuplicateInDB(aUC, fatturaPassiva);

	//validazioneComune(aUC, fatturaPassiva);
	if (!verificaEsistenzaSezionalePer(aUC, fatturaPassiva))
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � stato definito un sezionale per le " + fatturaPassiva.getDescrizioneEntitaPlurale() + " e il tipo sezionale \"" + fatturaPassiva.getTipo_sezionale().getDs_tipo_sezionale() + "\"!");
	
	try {
		fatturaPassiva.validateDate();
		
		//Se � una fattura differita, la data di emissione non deve superare
		//quella inserita in configurazione
		if(fatturaPassiva.getFl_liquidazione_differita() && fatturaPassiva.getDt_fattura_fornitore()!=null){
			java.sql.Timestamp data_limite;
			java.sql.Timestamp data_limite_sup;
			try {
				data_limite = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt01(aUC, new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
				data_limite_sup = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getDt02(aUC, new Integer(0), "*", "COSTANTI", "LIMITE_CREAZIONE_FATT_PASS_ES_DIF");
			} catch (RemoteException e) {		
				throw handleException(e); 
			}
			if(fatturaPassiva.getDt_fattura_fornitore().compareTo(data_limite)<0||fatturaPassiva.getDt_fattura_fornitore().compareTo(data_limite_sup)>0){
				fatturaPassiva.setFl_liquidazione_differita(false);
				throw new it.cnr.jada.comp.ApplicationException("Non � possibile indicare la liquidazione differita con la data emissione inserita.");
			}
		}
		
		//Verifica la validit� della data di registrazione rispetto all'ultima
		//data di stampa registri IVA
		callVerifyDataRegistrazione(aUC, fatturaPassiva);

        //Verifica che il documento rispetti la sequenza data/numero 
        //di registrazione
	    //validaSequenceDateNumber(aUC, fatturaPassiva);

		//Controllo date competenza COGE
		fatturaPassiva.validaDateCompetenza();
		
		//controllaCompetenzaCOGEDettagli(aUC, fatturaPassiva);
		
		validateFornitore(aUC, fatturaPassiva);
	} catch (it.cnr.jada.bulk.ValidationException e) {
		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
	}

	if (fatturaPassiva instanceof Fattura_passiva_IBulk &&
		(fatturaPassiva.isBollaDoganale() || fatturaPassiva.isSpedizioniere()))
		throw new it.cnr.jada.comp.ApplicationException("La fattura � definita come bolla doganale o spedizioniere. Non � possibile generare un compenso!");
	
	controllaQuadraturaConti(aUC, fatturaPassiva);
	controllaQuadraturaIntrastat(aUC, fatturaPassiva);
	
	if(fatturaPassiva.isElettronica())
		validaFatturaElettronica(aUC, fatturaPassiva);
	
}
private void assegnaProgressivoUnivoco(UserContext userContext,Fattura_passivaBulk fattura_passiva) throws ComponentException {
	try {
	it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession numerazione =
			(it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession)
				it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_TABNUM_EJB_Numerazione_baseComponentSession",
																it.cnr.contab.config00.tabnum.ejb.Numerazione_baseComponentSession.class);
		((Fattura_passivaBulk)fattura_passiva).setProgr_univoco(
			new Long(
				( numerazione.creaNuovoProgressivo(userContext,new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext)), "FATTURA_PASSIVA", "PG_REGISTRO_UNICO_FATPAS", ((Fattura_passivaBulk)fattura_passiva).getUser()) ).toString()
			)
		);
	} catch (Throwable t) {
		throw handleException(fattura_passiva, t);
	}
}

private Timestamp getDataInizioFatturazioneElettronica(UserContext userContext) throws ComponentException {

	try {

		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
				.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		return sess.getDt01(userContext, it.cnr.contab.utenze00.bp.CNRUserContext
				.getEsercizio(userContext), null,
				Configurazione_cnrBulk.PK_FATTURAZIONE_ELETTRONICA, Configurazione_cnrBulk.SK_PASSIVA);

	} catch (javax.ejb.EJBException ex) {
		throw handleException(ex);
	} catch (RemoteException ex) {
		throw handleException(ex);
	}

}

private Timestamp getDataInizioSplitPayment(UserContext userContext) throws ComponentException {

	try {

		Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices
				.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
		return sess.getDt01(userContext, 0, null,
				Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA);

	} catch (javax.ejb.EJBException ex) {
		throw handleException(ex);
	} catch (RemoteException ex) {
		throw handleException(ex);
	}

}

public Fattura_passivaBulk valorizzaInfoDocEle(UserContext userContext, Fattura_passivaBulk fp)
		throws ComponentException {
	try {
		fp.setDataInizioFatturaElettronica(getDataInizioFatturazioneElettronica(userContext));  
//		if(fp.getDt_registrazione() != null && fp.getDataInizioFatturaElettronica() != null)
//		{
//			if ((fp.getDt_registrazione().compareTo(fp.getDataInizioFatturaElettronica())<0))
//				fp.setGestione_doc_ele(false);
//			else
//				fp.setGestione_doc_ele(true);
//		}
//		else fp.setGestione_doc_ele(true);  //non dovrebbe mai verificarsi
		return fp;
	} catch (Throwable e) {
		throw handleException(e);
	}
}

public void validaFatturaElettronica(UserContext aUC,Fattura_passivaBulk fatturaPassiva) throws ComponentException {

	if (fatturaPassiva.getDocumentoEleTestata() == null)
		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non � possibile recuperare il documento elettronico!");
	boolean noSegno=false;
	/*
	if (fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getSoggettoEmittente() == null || 
			  !fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getSoggettoEmittente().equals(SoggettoEmittenteType.TZ.value())) 
		// il fornitore � il prestatore
	{
		if (((fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale() ==null
			)
		   &&
		   ((fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva() == null
			)
		  )
			throw new it.cnr.jada.comp.ApplicationException("Almeno uno tra Codice Fiscale e Partita IVA del fornitore deve coincidere con quello inserito per il prestatore nel documento elettronico. CF: " + fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale() + " PI: " + fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva() + "!");		
	}
	*/
	//if (fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getSoggettoEmittente() != null && 
		//	fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getSoggettoEmittente().equals(SoggettoEmittenteType.TZ.value())) 
		// il fornitore � l'intermediario o il rappresentante
	//{
		if (
			((fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getCodice_fiscale() ==null
			)
			&&
			((fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getPrestatoreAnag().getPartita_iva() == null
			)
				
			&&	
				
			((fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodicefiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodicefiscale().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodicefiscale() ==null
			)
		   &&
		   ((fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodice()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodice().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getIntermediarioCodice() == null
			)
			
			&&
			
			((fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodicefiscale()!= null &&
			  fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodicefiscale().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getCodice_fiscale()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodicefiscale() ==null
			)
		   &&
		   ((fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodice()!= null &&
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodice().compareTo(fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva())!=0
			 )
			 || 
			 fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()== null
			 ||
			 fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getRappresentanteCodice() == null
			)
			
		  )
			throw new it.cnr.jada.comp.ApplicationException("Almeno uno tra Codice Fiscale e Partita IVA del fornitore deve coincidere con quelli inseriti per il Prestatore/Rappresentante fiscale/Intermediario nel documento elettronico.");		

	//}
     java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
    if (fatturaPassiva.getDocumentoEleTestata().getNumeroDocumento().compareTo(fatturaPassiva.getNr_fattura_fornitore())!=0)
    	throw new it.cnr.jada.comp.ApplicationException("Numero Fattura fornitore diverso da quello inserito nel documento elettronico: " + fatturaPassiva.getDocumentoEleTestata().getNumeroDocumento() + "!");

    if (DateUtils.truncate(fatturaPassiva.getDocumentoEleTestata().getDataDocumento()).compareTo(DateUtils.truncate(fatturaPassiva.getDt_fattura_fornitore()))!=0)
    	throw new it.cnr.jada.comp.ApplicationException("Data Fattura fornitore diversa da quella presente nel documento elettronico: " +  sdf.format(fatturaPassiva.getDocumentoEleTestata().getDataDocumento()) + "!");
    
    if (DateUtils.truncate(fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getDataRicezione()).compareTo(DateUtils.truncate(fatturaPassiva.getData_protocollo()))!=0)
    	throw new it.cnr.jada.comp.ApplicationException("Data Ricezione diversa da quella presente nel documento elettronico: " + sdf.format(fatturaPassiva.getDocumentoEleTestata().getDocumentoEleTrasmissione().getDataRicezione()) + "!");
	
	if (fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()== null)
		   throw new it.cnr.jada.comp.ApplicationException("Totale Documento elettronico non valorizzato!");
	
	if (fatturaPassiva.getIm_totale_fattura()== null)
		   throw new it.cnr.jada.comp.ApplicationException("Totale Fattura non valorizzato!");

	BigDecimal totaleFat=fatturaPassiva.getIm_totale_fattura();
	if (fatturaPassiva.isCommerciale() && fatturaPassiva.getFornitore()!=null && fatturaPassiva.getFornitore().getAnagrafico()!=null &&
			fatturaPassiva.getFornitore().getAnagrafico().getPartita_iva()!=null &&
			fatturaPassiva.getFornitore().getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.ITALIA)==0 ){
		    	for (Iterator i = fatturaPassiva.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
					Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
					if(dettaglio.getVoce_iva()!=null && dettaglio.getVoce_iva().getFl_autofattura().booleanValue()){
						totaleFat=totaleFat.subtract(dettaglio.getIm_iva());
					}
		    	}
	}
	if(fatturaPassiva.getTi_fattura().compareTo(Fattura_passivaBulk.TIPO_NOTA_DI_CREDITO)==0)
			noSegno=true;
	else
		    noSegno=false;
	if (fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()!= null &&
			fatturaPassiva.getIm_totale_fattura() != null &&
		 (noSegno?fatturaPassiva.getDocumentoEleTestata().getImportoDocumento().abs():fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()).compareTo(totaleFat)!= 0)
	{   //se non � previsto arrotondamento restituisco l'errore	
		if (fatturaPassiva.getDocumentoEleTestata().getArrotondamento()== null)
				throw new it.cnr.jada.comp.ApplicationException("Totale Fattura: "+  totaleFat + " diverso da quello inserito nel documento elettronico: " + (noSegno?fatturaPassiva.getDocumentoEleTestata().getImportoDocumento().abs():fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()) + "!");
		//controllo se c'� quadratura a meno di arrotondamento
		else
			if (fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()!= null &&
			totaleFat != null &&
			((((noSegno?fatturaPassiva.getDocumentoEleTestata().getImportoDocumento().abs():fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()).subtract(totaleFat)).abs()).compareTo((fatturaPassiva.getDocumentoEleTestata().getArrotondamento()).abs())) != 0)
				throw new it.cnr.jada.comp.ApplicationException("Totale Fattura: "+  totaleFat + " non coerente con quello inserito nel documento elettronico: " +(noSegno?fatturaPassiva.getDocumentoEleTestata().getImportoDocumento().abs():fatturaPassiva.getDocumentoEleTestata().getImportoDocumento()) + " anche considerando l'arrotondamento: " + fatturaPassiva.getDocumentoEleTestata().getArrotondamento() + "!");	
	}
	try{
		boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(aUC, "AMMFATTURDOCSFATPASA");
		if(!hasAccesso){	
			Hashtable<String, BigDecimal> mapNatura = new Hashtable<String, BigDecimal>(), mapIva = new Hashtable<String, BigDecimal>();
			for(Iterator i=fatturaPassiva.getFattura_passiva_dettColl().iterator();i.hasNext();)
		    {
			      Fattura_passiva_rigaBulk riga=(Fattura_passiva_rigaBulk)i.next();
			      String key = null;
			      Hashtable<String, BigDecimal> currentMap = null;
			      if (riga.getVoce_iva()!=null && !riga.getVoce_iva().getFl_autofattura() ){
				      if (riga.getVoce_iva().getNaturaOperNonImpSdi()!=null) {
				    	  key = riga.getVoce_iva().getNaturaOperNonImpSdi();
				    	  currentMap = mapNatura;
				      } else {
				    	  key = riga.getVoce_iva().getPercentuale().toString();
				    	  currentMap = mapIva;
				      }
			      }
			      if (key != null) {
				      if (currentMap.get(key)!=null)
				    	  currentMap.put(key, currentMap.get(key).add(riga.getIm_iva()));
				      else
				    	  currentMap.put(key, riga.getIm_iva());	    	  
			      }
			}
		
			Hashtable<String, BigDecimal> mapNaturaEle = new Hashtable<String, BigDecimal>(), mapIvaEle = new Hashtable<String, BigDecimal>();
			for(Iterator i=fatturaPassiva.getDocumentoEleTestata().getDocEleIVAColl().iterator();i.hasNext();)
		    {
			      DocumentoEleIvaBulk rigaEle=(DocumentoEleIvaBulk)i.next();
			      String key = null;
			      Hashtable<String, BigDecimal> currentMap = null;
			  	  if(rigaEle.getImponibileImporto()!=null && rigaEle.getImponibileImporto().compareTo(BigDecimal.ZERO)!=0){
				      if (rigaEle.getNatura()!=null) {
				    	  key = rigaEle.getNatura();
				    	  currentMap = mapNaturaEle;
				      } else {
				    	  key = rigaEle.getAliquotaIva().toString();
				    	  currentMap = mapIvaEle;
				      }
			      
				      if (currentMap.get(key)!=null)
				    	  currentMap.put(key, currentMap.get(key).add(rigaEle.getImposta()));
				      else
				    	  currentMap.put(key, rigaEle.getImposta());
			      }
			}
		
			Hashtable<String, BigDecimal> mapNaturaEleArr = new Hashtable<String, BigDecimal>(), mapIvaEleArr = new Hashtable<String, BigDecimal>();
			for(Iterator i=fatturaPassiva.getDocumentoEleTestata().getDocEleIVAColl().iterator();i.hasNext();)
		    {
			      DocumentoEleIvaBulk rigaEle=(DocumentoEleIvaBulk)i.next();
			      String key = null;
			      Hashtable<String, BigDecimal> currentMap = null;
			      if (rigaEle.getNatura()!=null) {
			    	  key = rigaEle.getNatura();
			    	  currentMap = mapNaturaEleArr;
			      } else {
			    	  key = rigaEle.getAliquotaIva().toString();
			    	  currentMap = mapIvaEleArr;
			      }
		
			      if (currentMap.get(key)!=null)
			    	  currentMap.put(key, currentMap.get(key).add(Utility.nvl(rigaEle.getArrotondamento())));
			      else
			    	  currentMap.put(key, Utility.nvl(rigaEle.getArrotondamento()));
			}
		
			StringBuffer codiciNaturaSqu = new StringBuffer();
			for(Iterator i=mapNatura.keySet().iterator();i.hasNext();) {
				String key = (String)i.next();
				BigDecimal value = mapNatura.get(key);
				BigDecimal valueEle = mapNaturaEle.get(key);
				if (noSegno) 
					valueEle = valueEle.abs();
				BigDecimal valueEleArr = Utility.nvl(mapNaturaEleArr.get(key));
				if (!(value!=null && valueEle!=null && value.compareTo(valueEle)==0))
					if ((valueEleArr.compareTo(new BigDecimal(0))==0 && value!=null && valueEle!=null && value.compareTo(valueEle)!=0) ||
						(valueEleArr.compareTo(new BigDecimal(0))!=0 && value!=null && valueEle!=null && ((value.subtract(valueEle)).abs()).compareTo(valueEleArr.abs())!=0) ||	
						(value==null && valueEle!=null) || (value!=null && valueEle==null))
					codiciNaturaSqu.append((codiciNaturaSqu.length()>0?",":"")+key);
				mapNaturaEle.remove(key);
			}
			
			for(Iterator i=mapNaturaEle.keySet().iterator();i.hasNext();)
				codiciNaturaSqu.append((codiciNaturaSqu.length()>0?",":"")+(String)i.next());
		
			StringBuffer codiciIvaSqu = new StringBuffer();
			for(Iterator i=mapIva.keySet().iterator();i.hasNext();) {
				String key = (String)i.next();
				BigDecimal value = mapIva.get(key);
				BigDecimal valueEle = mapIvaEle.get(key);
				if (noSegno) 
					valueEle = valueEle.abs();
				BigDecimal valueEleArr = Utility.nvl(mapIvaEleArr.get(key));
				if(!(value!=null && valueEle!=null && value.compareTo(valueEle)==0))
					if ((valueEleArr.compareTo(new BigDecimal(0))==0 && value!=null && valueEle!=null && value.compareTo(valueEle)!=0) ||
						(valueEleArr.compareTo(new BigDecimal(0))!=0 && value!=null && valueEle!=null && ((value.subtract(valueEle)).abs()).compareTo(valueEleArr.abs())!=0) ||		
						(value==null && valueEle!=null) || (value!=null && valueEle==null))
					codiciIvaSqu.append((codiciIvaSqu.length()>0?",":"")+key);
				mapIvaEle.remove(key);
			}
		
			for(Iterator i=mapIvaEle.keySet().iterator();i.hasNext();)
				codiciIvaSqu.append((codiciIvaSqu.length()>0?",":"")+(String)i.next());
		
			if (verificaGenerazioneAutofattura(aUC,fatturaPassiva) && 
				(codiciNaturaSqu.toString().compareTo(Voce_ivaBulk.REVERSE_CHARGE)==0))
					codiciNaturaSqu=new StringBuffer();
			
			if (codiciNaturaSqu.length()>0 || codiciIvaSqu.length()>0)
				throw new it.cnr.jada.comp.ApplicationException("Squadratura dettagli IVA con la fattura elettronica per "+ 
								(codiciIvaSqu.length()>0?"le aliquote IVA: "+ codiciIvaSqu.toString():"") +
								(codiciIvaSqu.length()>0 && codiciNaturaSqu.length()>0?" e ":"") +
								(codiciNaturaSqu.length()>0?"i codici natura : "+ codiciNaturaSqu.toString():"")+"!");
		}
	}catch(RemoteException e){
		throw handleException(e);
	}
}
public void aggiornaObblSuCancPerCompenso(
		UserContext userContext,
		Fattura_passivaBulk fatturaPassiva,
		java.util.Vector scadenzeDaCancellare,
		OptionRequestParameter status)
		throws ComponentException {
		if (scadenzeDaCancellare != null) {

			it.cnr.jada.bulk.PrimaryKeyHashtable obblTemporanee = new it.cnr.jada.bulk.PrimaryKeyHashtable();
			for (Object oggettoBulk : scadenzeDaCancellare) {				
				if (oggettoBulk instanceof Obbligazione_scadenzarioBulk) {
					Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk)oggettoBulk;
					if (scadenza.getObbligazione().isTemporaneo()) {
						if (!obblTemporanee.containsKey(scadenza.getObbligazione())) {
							Vector allInstances = new java.util.Vector();
							allInstances.addElement(scadenza);
							obblTemporanee.put(scadenza.getObbligazione(), allInstances);
						} else {
							((Vector)obblTemporanee.get(scadenza.getObbligazione())).add(scadenza);
						}
					} else if (!fatturaPassiva.isToBeCreated() && OggettoBulk.NORMAL == scadenza.getCrudStatus()) {
						PrimaryKeyHashtable obbligs = getDocumentiContabiliNonTemporanei(userContext, fatturaPassiva.getObbligazioniHash().keys());
						if (!obbligs.containsKey(scadenza.getObbligazione()))
							aggiornaSaldi(
								userContext, 
								fatturaPassiva, 
								scadenza.getObbligazione(),
								status);
						scadenza.setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
						updateImportoAssociatoDocAmm(userContext, scadenza);
					}
	            	/**
	            	 * Devo aggiornare i Saldi per quelle scadenze modificate e riportate
	            	 * ma poi scollegate dal documento 
	            	 * Marco Spasiano 05/05/2006
	            	 */
	                aggiornaSaldi(userContext, fatturaPassiva, scadenza.getObbligazione(), status);
				}
			}
			for (java.util.Enumeration e = obblTemporanee.keys(); e.hasMoreElements();) {
				ObbligazioneBulk obblT = (ObbligazioneBulk)e.nextElement();

				//Aggiorna i saldi per le obbligazioni temporanee
				//DEVE ESSERE FATTO PRIMA DELL'AGGIORNAMENTO A DEFINITIVA
				PrimaryKeyHashtable obbligs = getDocumentiContabiliTemporanei(userContext, fatturaPassiva.getObbligazioniHash().keys());
				if (!obbligs.containsKey(obblT))
					aggiornaSaldi(
							userContext, 
							fatturaPassiva, 
							obblT,
							status);
			}
		}

}
public boolean verificaGenerazioneAutofattura(UserContext aUC,Fattura_passivaBulk fattura)
		throws ComponentException {
 			Boolean obbligatorio=false;
 			if (fattura.isCommerciale() && fattura.getFornitore()!=null && fattura.getFornitore().getAnagrafico()!=null && 
				fattura.getFornitore().getAnagrafico().getPartita_iva()!=null &&
				fattura.getFornitore().getAnagrafico().getTi_italiano_estero().compareTo(NazioneBulk.ITALIA)==0 ){
			   	for (Iterator i = fattura.getFattura_passiva_dettColl().iterator(); i.hasNext();) {
					Fattura_passiva_rigaBulk dettaglio = (Fattura_passiva_rigaBulk)i.next();
					if(dettaglio.getVoce_iva()!=null && dettaglio.getVoce_iva().getFl_autofattura().booleanValue())
						obbligatorio=true;
				}
			}
			
			return obbligatorio;
}
private void setDt_termine_creazione_docamm(
		it.cnr.jada.UserContext userContext,
		Fattura_passivaBulk fattura) 
		throws ComponentException {

		try {
			it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession session = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)
				it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
								"CNRCONFIG00_EJB_Configurazione_cnrComponentSession", 
								it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
			java.sql.Timestamp t = session.getDt01(
												userContext,
												fattura.getEsercizio(), 
												"*", 
												"CHIUSURA_COSTANTI", 
												"TERMINE_CREAZIONE_DOCAMM_ES_PREC");
			if (t == null)
				throw new it.cnr.jada.comp.ApplicationException("La costante di chiusura \"termine creazione docamm es prec\" NON � stata definita! Impossibile proseguire.");

			fattura.setDt_termine_creazione_docamm(t);
		} catch (Throwable e) {
			throw handleException(e);
		}
	}

	public Fattura_passivaBulk eliminaLetteraPagamentoEstero(
		UserContext context,
		Fattura_passivaBulk fatturaPassiva)
		throws ComponentException {
		return eliminaLetteraPagamentoEstero(context, fatturaPassiva,true);
	}
	
	public boolean isAttivoSplitPayment(UserContext aUC, Timestamp dataFattura) throws ComponentException {					   
		Date dataInizio;
		try {
			dataInizio = Utility.createConfigurazioneCnrComponentSession().getDt01(aUC, new Integer(0), null, Configurazione_cnrBulk.PK_SPLIT_PAYMENT, Configurazione_cnrBulk.SK_PASSIVA);
		} catch (ComponentException e) {
	    	throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		} catch (RemoteException e) {
	    	throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		} catch (EJBException e) {
	    	throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		}
		if (dataFattura == null || dataInizio == null || dataFattura.before(dataInizio)){
			return false;
		}
		return true;
	}
}