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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.anagraf00.core.bulk.*;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoHome;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsHome;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaHome;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_IBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAcquistoBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleAllegatiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleDdtBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleIvaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleLineaBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleScontoMaggBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTestataHome;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTrasmissioneHome;
import it.cnr.contab.docamm00.fatturapa.bulk.DocumentoEleTributiBulk;
import it.cnr.contab.docamm00.fatturapa.bulk.StatoDocumentoEleEnum;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoAcquistoEnum;
import it.cnr.contab.docamm00.fatturapa.bulk.TipoIntegrazioneSDI;
import it.cnr.contab.docamm00.service.FatturaPassivaElettronicaService;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailBulk;
import it.cnr.contab.utenze00.bulk.Utente_indirizzi_mailHome;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.BusyResourceException;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.SendMail;
import it.cnr.jada.util.ejb.EJBCommonServices;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.RegimeFiscaleType;
import it.gov.agenziaentrate.ivaservizi.docs.xsd.fatture.v1.SoggettoEmittenteType;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.internet.InternetAddress;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FatturaElettronicaPassivaComponent extends it.cnr.jada.comp.CRUDComponent 
	implements Cloneable,Serializable {
	private static final long serialVersionUID = 1L;
	private transient final static Logger logger = LoggerFactory.getLogger(FatturaPassivaElettronicaService.class);

	public  FatturaElettronicaPassivaComponent(){
    }
	
	@Override
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext,
			OggettoBulk oggettobulk) throws ComponentException {
		DocumentoEleTestataBulk documentoEleTestata = (DocumentoEleTestataBulk) super.inizializzaBulkPerModifica(usercontext, oggettobulk);
		try {
			boolean hasAccesso = ((it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession")).controllaAccesso(usercontext, "AMMFATTURDOCSFATPASA");
			documentoEleTestata.setAbilitato(hasAccesso);
			documentoEleTestata.setFromInizializzaBulkPerModifica(true);
			documentoEleTestata.setAttivoSplitPayment(Utility.createFatturaPassivaComponentSession().isAttivoSplitPayment(usercontext, documentoEleTestata.getDataDocumento()));
			
			documentoEleTestata.setDocEleLineaColl(new BulkList<DocumentoEleLineaBulk>(
					getHome(usercontext, DocumentoEleLineaBulk.class).find(new DocumentoEleLineaBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleIVAColl(new BulkList<DocumentoEleIvaBulk>(
					getHome(usercontext, DocumentoEleIvaBulk.class).find(new DocumentoEleIvaBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleAllegatiColl(new BulkList<DocumentoEleAllegatiBulk>(
					getHome(usercontext, DocumentoEleAllegatiBulk.class).find(new DocumentoEleAllegatiBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleTributiColl(new BulkList<DocumentoEleTributiBulk>(
					getHome(usercontext, DocumentoEleTributiBulk.class).find(new DocumentoEleTributiBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleScontoMaggColl(new BulkList<DocumentoEleScontoMaggBulk>(
					getHome(usercontext, DocumentoEleScontoMaggBulk.class).find(new DocumentoEleScontoMaggBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleAcquistoColl(new BulkList<DocumentoEleAcquistoBulk>(
					getHome(usercontext, DocumentoEleAcquistoBulk.class).find(new DocumentoEleAcquistoBulk(documentoEleTestata))));
			documentoEleTestata.setDocEleDdtColl(new BulkList<DocumentoEleDdtBulk>(
					getHome(usercontext, DocumentoEleDdtBulk.class).find(new DocumentoEleDdtBulk(documentoEleTestata))));
			if ((documentoEleTestata.getDocEleTributiColl()!=null && !documentoEleTestata.getDocEleTributiColl().isEmpty()) 
					||(documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale()!= null && 
					(documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_02.name()) ||
							documentoEleTestata.getDocumentoEleTrasmissione().getRegimefiscale().equals(RegimeFiscaleType.RF_19.name())))){
				documentoEleTestata.setAttivoSplitPaymentProf(Utility.createFatturaPassivaComponentSession().isAttivoSplitPaymentProf(usercontext, documentoEleTestata.getDataDocumento()));
			}
			DocumentoEleTestataBulk nota = new DocumentoEleTestataBulk();
			nota.setFatturaCollegata(documentoEleTestata);
			documentoEleTestata.setNotaCollegata(
					(DocumentoEleTestataBulk) getHome(usercontext, DocumentoEleTestataBulk.class)
					.find(nota)
					.stream()
					.findAny().orElse(null)
			);
			getHomeCache(usercontext).fetchAll(usercontext);
		} catch (RemoteException | PersistencyException e) {
			throw handleException(e);
		}
		return documentoEleTestata;
	}
	
	@Override
	protected Query select(UserContext userContext,
			CompoundFindClause clauses, OggettoBulk bulk)
			throws ComponentException, PersistencyException {
        if(clauses == null){
            if(bulk != null)
            	clauses = bulk.buildFindClauses(null);
        }else{
        	clauses = CompoundFindClause.and(clauses, bulk.buildFindClauses(Boolean.FALSE));
        }
        SQLBuilder sql = getHome(userContext, bulk, "V_DOCUMENTO_ELE").selectByClause(userContext, clauses);
        if (CNRUserContext.getCd_unita_organizzativa(userContext) != null) {
			Unita_organizzativaBulk uoScrivania = ((Unita_organizzativaBulk)getHome(userContext, Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(userContext))));
			boolean isUoEnte = uoScrivania.getCd_tipo_unita().compareTo(Tipo_unita_organizzativaHome.TIPO_UO_ENTE)==0;
			if (!isUoEnte) {
				sql.openParenthesis(FindClause.AND);
				sql.addSQLClause(FindClause.AND, "V_DOCUMENTO_ELE.CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
				sql.addSQLClause(FindClause.OR, "V_DOCUMENTO_ELE.CD_UNITA_COMPETENZA", SQLBuilder.EQUALS, uoScrivania.getCd_unita_organizzativa());
				sql.closeParenthesis();			
			}			
		}
		return sql;
	}

	public Boolean isPartitaIvaGruppoIva(UserContext usercontext, AnagraficoBulk anagrafico, String partitaIva, Timestamp dataDocumento) throws ComponentException{
		AnagraficoHome anagraficoHome = (AnagraficoHome)getHome(usercontext,AnagraficoBulk.class);
		try {
			Collection coll = anagraficoHome.findGruppiIvaAssociati(anagrafico);
			if (!coll.isEmpty()){
				for (Iterator d = coll.iterator(); d.hasNext(); ) {
					AssGruppoIvaAnagBulk assColl = (AssGruppoIvaAnagBulk) d.next();
					AnagraficoBulk anagraficoGruppoIva = assColl.getAnagraficoGruppoIva();
					anagraficoGruppoIva = (AnagraficoBulk)anagraficoHome.findByPrimaryKey(usercontext, anagraficoGruppoIva);
					if (anagraficoGruppoIva.getPartita_iva().compareTo(partitaIva) == 0 && dataDocumento.compareTo(anagraficoGruppoIva.getDt_canc()) <= 0  && dataDocumento.compareTo(anagraficoGruppoIva.getDtIniValGruppoIva()) >= 0 ){
						return true;
					}
				}
			}
		} catch (IntrospectionException e) {
			throw handleException(e);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public void completaDocumento(UserContext usercontext, DocumentoEleTrasmissioneBulk documentoEleTrasmissioneBulk) throws ComponentException{
        try{
        	AnagraficoHome anagraficoHome = (AnagraficoHome)getHome(usercontext,AnagraficoBulk.class);
        	TerzoHome terzoHome = (TerzoHome)getHome(usercontext,TerzoBulk.class);
        	Rif_modalita_pagamentoHome rifModPagHome = (Rif_modalita_pagamentoHome) getHome(usercontext,Rif_modalita_pagamentoBulk.class);
        	Modalita_pagamentoHome modPagHome = (Modalita_pagamentoHome) getHome(usercontext,Modalita_pagamentoBulk.class);
        	Voce_ivaHome voceIvaHome = (Voce_ivaHome) getHome(usercontext, Voce_ivaBulk.class);

        	List<String> anomalieTrasmissione = new ArrayList<String>();
        	SQLBuilder sql = terzoHome.createSQLBuilder();
        	sql.addClause(FindClause.AND, "codiceUnivocoUfficioIpa", SQLBuilder.EQUALS, documentoEleTrasmissioneBulk.getCodiceDestinatario());
        	sql.addSQLClause("AND", "DT_FINE_RAPPORTO", sql.ISNULL, null);
        	List<TerzoBulk> terzoUOS = terzoHome.fetchAll(sql);
        	if (terzoUOS != null && !terzoUOS.isEmpty()) {
        		documentoEleTrasmissioneBulk.setUnitaOrganizzativa(terzoUOS.get(0).getUnita_organizzativa());
        		documentoEleTrasmissioneBulk.setCommittente(terzoUOS.get(0));       		
        	} else {
        		TerzoBulk terzoEnte = terzoHome.findTerzoEnte();
        		documentoEleTrasmissioneBulk.setUnitaOrganizzativa(terzoEnte.getUnita_organizzativa());
        		documentoEleTrasmissioneBulk.setCommittente(terzoEnte);
        	}
    		/**
    		 * Invio mail di notifica Ricezione
    		 */
        	try {
        		String subject= "[SIGLA] Notifica ricezione fattura passiva con Identificativo SdI:" + documentoEleTrasmissioneBulk.getIdentificativoSdi();
        		subject += " UO: " + documentoEleTrasmissioneBulk.getUnitaOrganizzativa().getCd_unita_organizzativa();
        		String text = "E' pervenuta la fattura dal trasmittente: <b>" +documentoEleTrasmissioneBulk.getIdCodice() + "</b><br>"+
        				"Prestatore: " + documentoEleTrasmissioneBulk.getDenominzionePrestatore() +"<br>" +
        				"Il documento è presente nell'area temporanea di SIGLA.";
        		String addressTO = null;
        		Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome)getHome(usercontext,Utente_indirizzi_mailBulk.class);
    			for (java.util.Iterator<Utente_indirizzi_mailBulk> i = utente_indirizzi_mailHome.findUtenteNotificaRicezioneFatturaElettronica(
    					documentoEleTrasmissioneBulk.getUnitaOrganizzativa()).iterator();i.hasNext();){
    				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
    				if (addressTO == null)
    				  addressTO = new String();
    				else
    				  addressTO = addressTO + ",";    
    				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
    			}
    			if (addressTO != null){
					SendMail.sendMail(subject, text, InternetAddress.parse(addressTO));
    			}        	        		
        	}catch (Exception _ex) {
        	}

			if (documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale() != null ||
        			documentoEleTrasmissioneBulk.getPrestatoreCodice() != null) {
				final List<String> cigs = documentoEleTrasmissioneBulk
						.getDocEleTestataColl()
						.stream()
						.map(DocumentoEleTestataBulk::getDocEleAcquistoColl)
						.flatMap(documentoEleAcquistoBulks -> documentoEleAcquistoBulks.stream())
						.map(DocumentoEleAcquistoBulk::getAcquistoCig)
						.filter(s -> Optional.ofNullable(s).isPresent())
						.collect(Collectors.toList());
				if (!cigs.isEmpty()) {
					ContrattoHome contrattoHome = (ContrattoHome) getHome(usercontext, ContrattoBulk.class);
					for(String cig : cigs) {
						final Optional<ContrattoBulk> optionalContrattoBulk = contrattoHome.findByCIG(usercontext, cig).stream().findAny();
						if (optionalContrattoBulk.isPresent()) {
							final TerzoBulk figura_giuridica_esterna = optionalContrattoBulk.get().getFigura_giuridica_esterna();
							if(Optional.ofNullable(documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale())
											.filter(s -> s.equalsIgnoreCase(Optional.ofNullable(figura_giuridica_esterna.getCodice_fiscale_anagrafico()).orElse(""))).isPresent() ||
											(Optional.ofNullable(documentoEleTrasmissioneBulk.getPrestatoreCodice())
													.filter(s -> s.equalsIgnoreCase(Optional.ofNullable(figura_giuridica_esterna.getPartita_iva_anagrafico()).orElse(""))).isPresent() &&
													!Optional.ofNullable(figura_giuridica_esterna)
															.flatMap(terzoBulk -> Optional.ofNullable(terzoBulk.getAnagrafico()))
															.flatMap(anagraficoBulk -> Optional.ofNullable(anagraficoBulk.getTi_entita_giuridica()))
															.map(s -> s.equalsIgnoreCase(AnagraficoBulk.GIURIDICA))
															.orElse(Boolean.TRUE)
											)
							) {
								documentoEleTrasmissioneBulk.setPrestatore(figura_giuridica_esterna);
								documentoEleTrasmissioneBulk.setPrestatoreAnag(figura_giuridica_esterna.getAnagrafico());
							}
						}
					}
				}
				if (!Optional.ofNullable(documentoEleTrasmissioneBulk)
						.flatMap(documentoEleTrasmissioneBulk1 -> Optional.ofNullable(documentoEleTrasmissioneBulk.getPrestatore()))
						.flatMap(terzoBulk -> Optional.ofNullable(terzoBulk.getCd_terzo()))
						.isPresent()) {
					List<AnagraficoBulk> anagraficoBulks = null;
					anagraficoBulks = anagraficoHome.findByCodiceFiscaleOrPartitaIVA(
							documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale(),
							documentoEleTrasmissioneBulk.getPrestatoreCodice());
					if (anagraficoBulks != null && !anagraficoBulks.isEmpty()) {
						if (anagraficoBulks.size() == 1) {
							AnagraficoBulk anag = anagraficoBulks.get(0);
							documentoEleTrasmissioneBulk.setPrestatoreAnag(anag);
							List<TerzoBulk> terzi = terzoHome.findTerzi(anagraficoBulks.get(0));
							if (terzi != null && !terzi.isEmpty() && terzi.size() == 1) {
								documentoEleTrasmissioneBulk.setPrestatore(terzi.get(0));
							}
						} else {
							anomalieTrasmissione.add("Esistono più di una riga in anagrafica per il CF:" +
									documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale() +" o la partita IVA: " +
									documentoEleTrasmissioneBulk.getPrestatoreCodice());
						}
					}
				}
				if (documentoEleTrasmissioneBulk.getPrestatoreCodice() != null){
					List<AnagraficoBulk> list = anagraficoHome.findByCodiceFiscaleOrPartitaIVA(null,documentoEleTrasmissioneBulk.getPrestatoreCodice());
					if (list != null && list.size() == 1){
						AnagraficoBulk anagraficoBulk = list.get(0);
						if (anagraficoBulk.isGruppoIVA()){
							documentoEleTrasmissioneBulk.setPrestatoreAnag(anagraficoBulk);
							documentoEleTrasmissioneBulk.setPrestatore(null);
							if (documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale() != null){
								List<AnagraficoBulk> lista = anagraficoHome.findByCodiceFiscaleOrPartitaIVA(documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale(),null);
								if (lista != null && lista.size() == 1){
									AnagraficoBulk anagraficoPerTerzo = lista.get(0);
									if (documentoEleTrasmissioneBulk.getDataRicezione().compareTo(anagraficoBulk.getDt_canc()) <= 0  && documentoEleTrasmissioneBulk.getDataRicezione().compareTo(anagraficoBulk.getDtIniValGruppoIva()) > 0){
										List<TerzoBulk> listaTerzi = terzoHome.findTerzi(anagraficoPerTerzo);
										if (listaTerzi.size() == 0){
											documentoEleTrasmissioneBulk.setPrestatore(listaTerzi.get(0));
										}
									}
								}
							}
						}
					}
				}
        	}

        	if (documentoEleTrasmissioneBulk.getRappresentanteCodicefiscale() != null || 
        			documentoEleTrasmissioneBulk.getRappresentanteCodice() != null) {
        		List<AnagraficoBulk> anagraficoBulks = anagraficoHome.findByCodiceFiscaleOrPartitaIVA(
        				documentoEleTrasmissioneBulk.getRappresentanteCodicefiscale(),
        				documentoEleTrasmissioneBulk.getRappresentanteCodice());
        		if (anagraficoBulks != null && !anagraficoBulks.isEmpty()) {
        			if (anagraficoBulks.size() == 1) {
        				documentoEleTrasmissioneBulk.setRappresentanteAnag(anagraficoBulks.get(0));
        				List<TerzoBulk> terzi = terzoHome.findTerzi(anagraficoBulks.get(0));
        				if (terzi != null && !terzi.isEmpty() && terzi.size() == 1) {
        					documentoEleTrasmissioneBulk.setRappresentante(terzi.get(0));
        				}
        			} else {
        				anomalieTrasmissione.add("Esistono più di una riga in anagrafica per il CF:" + 
        						documentoEleTrasmissioneBulk.getRappresentanteCodicefiscale() +" o la partita IVA: " + 
        						documentoEleTrasmissioneBulk.getRappresentanteCodice());
        			}
        		}
        	}
        	
           	if (documentoEleTrasmissioneBulk.getIntermediarioCodicefiscale() != null || 
        			documentoEleTrasmissioneBulk.getIntermediarioCodice() != null) {
        		List<AnagraficoBulk> anagraficoBulks = anagraficoHome.findByCodiceFiscaleOrPartitaIVA(
        				documentoEleTrasmissioneBulk.getIntermediarioCodicefiscale(),
        				documentoEleTrasmissioneBulk.getIntermediarioCodice());
        		if (anagraficoBulks != null && !anagraficoBulks.isEmpty()) {
        			if (anagraficoBulks.size() == 1) {
        				documentoEleTrasmissioneBulk.setIntermediarioAnag(anagraficoBulks.get(0));
        				List<TerzoBulk> terzi = terzoHome.findTerzi(anagraficoBulks.get(0));
        				if (terzi != null && !terzi.isEmpty() && terzi.size() == 1) {
        					documentoEleTrasmissioneBulk.setIntermediario(terzi.get(0));
        				}
        			} else {
        				anomalieTrasmissione.add("Esistono più di una riga in anagrafica per il CF:" + 
        						documentoEleTrasmissioneBulk.getIntermediarioCodicefiscale() +" o la partita IVA: " + 
        						documentoEleTrasmissioneBulk.getIntermediarioCodice());
        			}
        		}
        	}
			if (!anomalieTrasmissione.isEmpty())
				documentoEleTrasmissioneBulk.setAnomalieRicezione(StringUtils.join(anomalieTrasmissione.toArray()," - "));
        	
        	for (DocumentoEleTestataBulk docTestata : documentoEleTrasmissioneBulk.getDocEleTestataColl()) {
            	List<String> anomalieTestata = new ArrayList<String>();
        		if (docTestata.getBeneficiarioModPag() != null) {
                	SQLBuilder sqlRifModPag = rifModPagHome.createSQLBuilder();
                	sqlRifModPag.addClause(FindClause.AND, "tipoPagamentoSdi", SQLBuilder.EQUALS, docTestata.getBeneficiarioModPag());
                	List<Rif_modalita_pagamentoBulk> rifModPags = rifModPagHome.fetchAll(sqlRifModPag);
                	boolean trovataModPag = Boolean.FALSE;
                	for (Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk : rifModPags) {
                		Modalita_pagamentoBulk modalitaPagamento = 
                				(Modalita_pagamentoBulk) modPagHome.findByPrimaryKey(new Modalita_pagamentoBulk(
								rif_modalita_pagamentoBulk.getCd_modalita_pag(), docTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo()));
                		if (modalitaPagamento != null) {
                			trovataModPag = Boolean.TRUE;
                			docTestata.setModalitaPagamento(modalitaPagamento);
                			break;
                		}
					}
                	if (!trovataModPag) {                		
                		anomalieTestata.add("Modalità di pagamento non trovata");
                	}
        		}
    			if (!anomalieTestata.isEmpty())
    				docTestata.setAnomalie(StringUtils.join(anomalieTestata.toArray()," - "));
        		docTestata.setStatoDocumento(StatoDocumentoEleEnum.AGGIORNATO.name());
        		docTestata.setToBeUpdated();
        		for (DocumentoEleIvaBulk documentoEleIvaBulk : docTestata.getDocEleIVAColl()) {
            		List<String> anomalieIVA = new ArrayList<String>();
        			SQLBuilder sqlVoceIva = voceIvaHome.createSQLBuilder();
        			sqlVoceIva.addClause(FindClause.AND, "percentuale", SQLBuilder.EQUALS, documentoEleIvaBulk.getAliquotaIva());
        			sqlVoceIva.addSQLClause(FindClause.AND, "NATURA_OPER_NON_IMP_SDI", SQLBuilder.EQUALS, documentoEleIvaBulk.getNatura());
        			List<Voce_ivaBulk> results = voceIvaHome.fetchAll(sqlVoceIva);
        			if (results.isEmpty())
        				anomalieIVA.add("Codice Iva non trovato!");
        			if (results.size() > 1)
        				anomalieIVA.add("Esistonio piu codici Iva associabili alla riga!");
        			if (results.size() == 1)
        				documentoEleIvaBulk.setVoceIva(results.get(0));     
        			if (!anomalieIVA.isEmpty())
        				documentoEleIvaBulk.setAnomalie(StringUtils.join(anomalieIVA.toArray()," - "));
            		documentoEleIvaBulk.setToBeUpdated();        			
				}
			}
        	documentoEleTrasmissioneBulk.setToBeUpdated();
        	modificaConBulk(usercontext, documentoEleTrasmissioneBulk);
        }catch(Throwable throwable){
            throw handleException(throwable);
        }
    }
        
    private boolean isDocumentoCompletato(UserContext usercontext, DocumentoEleTestataBulk documentoEleTestata) {
    	DocumentoEleTrasmissioneBulk documentoEleTrasmissioneBulk= documentoEleTestata.getDocumentoEleTrasmissione();
    	boolean isCompletato = Boolean.TRUE;
    	if ((documentoEleTrasmissioneBulk.getPrestatore() == null || documentoEleTrasmissioneBulk.getPrestatore().getCrudStatus() == OggettoBulk.UNDEFINED ||
    			documentoEleTrasmissioneBulk.getPrestatoreAnag() == null || documentoEleTrasmissioneBulk.getPrestatoreAnag().getCrudStatus() == OggettoBulk.UNDEFINED) && 
    			(documentoEleTrasmissioneBulk.getPrestatoreCodicefiscale() != null || 
        			documentoEleTrasmissioneBulk.getPrestatoreCodice() != null))
    		return false;
    	if (documentoEleTestata.getCdUnitaCompetenza() != null && ((documentoEleTrasmissioneBulk.getFlCompletato() != null && !documentoEleTrasmissioneBulk.getFlCompletato()) || 
    			documentoEleTrasmissioneBulk.getFlCompletato() == null))
    		return false;
    	return isCompletato;
    }
    private void cambiaStatoCompletato(UserContext usercontext, DocumentoEleTestataBulk documentoEleTestata) {
    	if (isDocumentoCompletato(usercontext, documentoEleTestata)) {
    		if (documentoEleTestata.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.AGGIORNATO)) {	
    			documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.COMPLETO.name());
    		}
    	} else {
    		if (documentoEleTestata.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.COMPLETO)) {	
    			documentoEleTestata.setStatoDocumento(StatoDocumentoEleEnum.AGGIORNATO.name());
    		}    		
    	}
    }

    public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
    	if (oggettobulk instanceof DocumentoEleTestataBulk){
    		((DocumentoEleTestataBulk)oggettobulk).getDocumentoEleTrasmissione().setToBeUpdated();
			notificaUOCompetenza(usercontext, ((DocumentoEleTestataBulk)oggettobulk));
    		super.modificaConBulk(usercontext, ((DocumentoEleTestataBulk)oggettobulk).getDocumentoEleTrasmissione());
    		cambiaStatoCompletato(usercontext, ((DocumentoEleTestataBulk)oggettobulk));
    	}
    	return super.modificaConBulk(usercontext, oggettobulk);    	
    }      
    
    @SuppressWarnings("unchecked")
	private void notificaUOCompetenza(UserContext usercontext, DocumentoEleTestataBulk testata) throws ComponentException {
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
    	try {
    		DocumentoEleTestataBulk testataDB = (DocumentoEleTestataBulk) home.findByPrimaryKey(testata);
			if ((testataDB.getUnitaCompetenza() == null && testata.getUnitaCompetenza() != null && 
					testata.getUnitaCompetenza().getCd_unita_organizzativa() != null) ||
					(testataDB.getUnitaCompetenza() != null && testata.getUnitaCompetenza() != null && 
					testata.getUnitaCompetenza().getCd_unita_organizzativa() != null && 
					!testataDB.getUnitaCompetenza().equalsByPrimaryKey(testata.getUnitaCompetenza()))) {
				try {
	        		String subject= "[SIGLA] Notifica assegnazione fattura passiva con Identificativo SdI:" + testata.getIdentificativoSdi();
	        		subject += " UO: " + testata.getUnitaCompetenza().getCd_unita_organizzativa();
	        		String text = "E' pervenuta la fattura dal trasmittente: <b>" +testata.getIdCodice() + "</b><br>"+
	        				"Prestatore: " + testata.getDocumentoEleTrasmissione().getDenominzionePrestatore() +"<br>" +
	        				"Il documento è presente nell'area temporanea di SIGLA.";
	        		String addressTO = null;
	        		Utente_indirizzi_mailHome utente_indirizzi_mailHome = (Utente_indirizzi_mailHome)getHome(usercontext,Utente_indirizzi_mailBulk.class);
	    			for (java.util.Iterator<Utente_indirizzi_mailBulk> i = utente_indirizzi_mailHome.findUtenteNotificaRicezioneFatturaElettronica(
	    					testata.getUnitaCompetenza()).iterator();i.hasNext();){
	    				Utente_indirizzi_mailBulk utente_indirizzi = (Utente_indirizzi_mailBulk)i.next();
	    				if (addressTO == null)
	    				  addressTO = new String();
	    				else
	    				  addressTO = addressTO + ",";    
	    				addressTO = addressTO+utente_indirizzi.getIndirizzo_mail();			
	    			}
	    			if (addressTO != null){
						SendMail.sendMail(subject, text, InternetAddress.parse(addressTO));
	    			}        	        		
	        	}catch (Exception _ex) {
	        		logger.error("NotificaUOCompetenza: " + _ex.getMessage(), _ex);
	        	}				
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public SQLBuilder selectVoceIvaByClause(UserContext usercontext, DocumentoEleIvaBulk documentoEleIvaBulk, 
			Voce_ivaBulk voce_ivaBulk, CompoundFindClause compoundfindclause) throws ComponentException, PersistencyException{
		Voce_ivaHome voceIvaHome = (Voce_ivaHome) getHome(usercontext, Voce_ivaBulk.class);
		SQLBuilder sqlVoceIva = voceIvaHome.selectByClause(compoundfindclause,
						Optional.ofNullable(documentoEleIvaBulk.getDocumentoEleTestata())
									.flatMap(documentoEleTestataBulk -> Optional.ofNullable(documentoEleTestataBulk.getDataDocumento()))
									.orElse(EJBCommonServices.getServerDate()));

		sqlVoceIva.addClause(FindClause.AND, "percentuale", SQLBuilder.EQUALS, documentoEleIvaBulk.getAliquotaIva());
		sqlVoceIva.addSQLClause(FindClause.AND, "NATURA_OPER_NON_IMP_SDI", SQLBuilder.EQUALS, documentoEleIvaBulk.getNatura());
		sqlVoceIva.addSQLClause("AND", "ti_applicazione", SQLBuilder.NOT_EQUALS, Voce_ivaBulk.VENDITE);
		return sqlVoceIva;
	}

    @SuppressWarnings("unchecked")
	public SQLBuilder selectModalitaPagamentoByClause(UserContext usercontext, DocumentoEleTestataBulk docTestata, 
    		Modalita_pagamentoBulk modalita_pagamentoBulk, CompoundFindClause compoundfindclause) throws ComponentException, PersistencyException{
    	Rif_modalita_pagamentoHome rifModPagHome = (Rif_modalita_pagamentoHome) getHome(usercontext,Rif_modalita_pagamentoBulk.class);
    	SQLBuilder sqlRifModPag = rifModPagHome.selectByClause(compoundfindclause);
    	sqlRifModPag.addClause(FindClause.AND, "tipoPagamentoSdi", SQLBuilder.EQUALS, docTestata.getBeneficiarioModPag());
    	List<Rif_modalita_pagamentoBulk> rifModPags = rifModPagHome.fetchAll(sqlRifModPag);
    	SQLBuilder sql = (SQLBuilder) super.select( usercontext, compoundfindclause, modalita_pagamentoBulk );
    	if (docTestata.getDocumentoEleTrasmissione().getSoggettoEmittente() != null && 
    			docTestata.getDocumentoEleTrasmissione().getSoggettoEmittente().equals(SoggettoEmittenteType.TZ.value())) {
    		if (docTestata.getDocumentoEleTrasmissione().getIntermediarioCdTerzo() != null)
    			sql.addSQLClause(FindClause.AND, "CD_TERZO", SQLBuilder.EQUALS, docTestata.getDocumentoEleTrasmissione().getIntermediarioCdTerzo());
    		else if (docTestata.getDocumentoEleTrasmissione().getRappresentanteCdTerzo() != null)
    			sql.addSQLClause(FindClause.AND, "CD_TERZO", SQLBuilder.EQUALS, docTestata.getDocumentoEleTrasmissione().getRappresentanteCdTerzo());
    		else
    			sql.addSQLClause(FindClause.AND, "CD_TERZO", SQLBuilder.EQUALS, docTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo());
    	} else {
        	sql.addSQLClause(FindClause.AND, "CD_TERZO", SQLBuilder.EQUALS, docTestata.getDocumentoEleTrasmissione().getPrestatoreCdTerzo());    		
    	}
    	sql.openParenthesis(FindClause.AND);
    	for (Rif_modalita_pagamentoBulk rif_modalita_pagamentoBulk : rifModPags) {
        	sql.addSQLClause(FindClause.OR, "CD_MODALITA_PAG", SQLBuilder.EQUALS, rif_modalita_pagamentoBulk.getCd_modalita_pag());			
		}
    	sql.closeParenthesis();
    	return sql;
    	
    }

	public Fattura_passivaBulk cercaFatturaPassiva(UserContext usercontext, DocumentoEleTestataBulk oggettobulk) throws ComponentException {
    	Fattura_passivaHome fatturaPassivaHome = (Fattura_passivaHome) getHome(usercontext, oggettobulk.getInstanceFattura());
    	SQLBuilder sqlFatturaPassiva = fatturaPassivaHome.createSQLBuilder();
    	sqlFatturaPassiva.addClause(FindClause.AND, "documentoEleTestata", SQLBuilder.EQUALS, oggettobulk);
    	try {    		
			return (Fattura_passivaBulk) fatturaPassivaHome.fetchAll(fatturaPassivaHome.createBroker(sqlFatturaPassiva)).get(0);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public Fattura_passivaBulk cercaFatturaPassivaForNota(UserContext usercontext, DocumentoEleTestataBulk oggettobulk) throws ComponentException {
    	Fattura_passivaHome fatturaPassivaHome = (Fattura_passivaHome) getHome(usercontext, Fattura_passiva_IBulk.class);
    	String numeroFatturaFornitore = null;
    	Timestamp dataFatturaFornitore = null;
    	for (DocumentoEleAcquistoBulk documentoEleAcquistoBulk : oggettobulk.getDocEleAcquistoColl()) {
			if (documentoEleAcquistoBulk.getTipoRifacquisto().equalsIgnoreCase(TipoAcquistoEnum.Fatture_Collegate.name())){
				numeroFatturaFornitore = documentoEleAcquistoBulk.getAcquistoDocumento();
				dataFatturaFornitore = documentoEleAcquistoBulk.getAcquistoData();
				break;
			}
		}
    	// Rospuc 22/09/2016 consentito registrazione nota credito anche senza riferimenti alla fattura collegata 
    	/* if (numeroFatturaFornitore == null || dataFatturaFornitore == null) {
				throw new ApplicationException("Fattura collegata alla nota non trovata! Informazioni mancanti!");    		
    	} */
    	SQLBuilder sqlFatturaPassiva = fatturaPassivaHome.createSQLBuilder();
    	sqlFatturaPassiva.addClause(FindClause.AND, "fornitore", SQLBuilder.EQUALS, oggettobulk.getDocumentoEleTrasmissione().getPrestatore());
    	// Rospuc 22/09/2016 consentito registrazione nota credito anche con riferimenti simili alla fattura collegata indicata
    	sqlFatturaPassiva.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, oggettobulk.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa());
    	sqlFatturaPassiva.addClause(FindClause.AND, "nr_fattura_fornitore", SQLBuilder.CONTAINS, numeroFatturaFornitore);
    	sqlFatturaPassiva.addClause(FindClause.AND, "dt_fattura_fornitore", SQLBuilder.EQUALS, dataFatturaFornitore);
    	try {    		
    		List<?> fattureCol = fatturaPassivaHome.fetchAll(fatturaPassivaHome.createBroker(sqlFatturaPassiva));
    		if (fattureCol.isEmpty()||fattureCol.size()>1){
    	    	// Rospuc 22/09/2016 consentito registrazione nota credito su ultima fattura registrata 
    			sqlFatturaPassiva = fatturaPassivaHome.createSQLBuilder();
    	    	sqlFatturaPassiva.addClause(FindClause.AND, "fornitore", SQLBuilder.EQUALS, oggettobulk.getDocumentoEleTrasmissione().getPrestatore());
    	    	sqlFatturaPassiva.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, oggettobulk.getDocumentoEleTrasmissione().getUnitaOrganizzativa().getCd_unita_organizzativa());
    	    	sqlFatturaPassiva.setOrderBy("dt_registrazione",  it.cnr.jada.util.OrderConstants.ORDER_ASC);
    	    	List<?> fatture = fatturaPassivaHome.fetchAll(fatturaPassivaHome.createBroker(sqlFatturaPassiva));
    	    		if (fatture.isEmpty()){
    	    			throw new ApplicationException("Fattura collegata alla nota non trovata!");
    	    		}
    	    		return (Fattura_passivaBulk) fatture.get(fatture.size()-1);
    		}
			return (Fattura_passivaBulk) fattureCol.get(0);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
	
	private boolean isRibaltato(it.cnr.jada.UserContext userContext, String cd_cds, Integer esercizio) throws ComponentException
	{
		try	{
			return ((Parametri_cdsHome)getHome(userContext,Parametri_cdsBulk.class)).isRibaltato(userContext, cd_cds, esercizio);
		} 
		catch(Throwable e) {
			throw handleException(e);
		}
	}	

	public Configurazione_cnrBulk getEmailPecSdi(UserContext userContext, boolean lock) throws it.cnr.jada.comp.ComponentException {
		try {
			Configurazione_cnrBulk configurazione_cnrBulk  = new Configurazione_cnrBulk(Configurazione_cnrBulk.PK_EMAIL_PEC,Configurazione_cnrBulk.SK_SDI, "*",  new Integer(0));
			Configurazione_cnrHome configurazione_cnrHome = (Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class);
			configurazione_cnrBulk = (Configurazione_cnrBulk) configurazione_cnrHome.findAndLock(configurazione_cnrBulk);
			if (lock) {
				if ("Y".equalsIgnoreCase(configurazione_cnrBulk.getVal04()))
					return null;
				configurazione_cnrBulk.setVal04("Y");
				configurazione_cnrBulk.setToBeUpdated();
				configurazione_cnrHome.update(configurazione_cnrBulk, userContext);
			}
			return configurazione_cnrBulk;
		} catch (BusyResourceException _ex) {
			return null;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	
	public void unlockEmailPEC(UserContext userContext) throws it.cnr.jada.comp.ComponentException {
		try {
			Configurazione_cnrBulk configurazione_cnrBulk  = new Configurazione_cnrBulk(Configurazione_cnrBulk.PK_EMAIL_PEC,Configurazione_cnrBulk.SK_SDI, "*",  new Integer(0));
			Configurazione_cnrHome configurazione_cnrHome = (Configurazione_cnrHome) getHome(userContext, Configurazione_cnrBulk.class);
			configurazione_cnrBulk = (Configurazione_cnrBulk) configurazione_cnrHome.findAndLock(configurazione_cnrBulk);
			configurazione_cnrBulk.setVal04("N");
			configurazione_cnrBulk.setToBeUpdated();
			configurazione_cnrHome.update(configurazione_cnrBulk, userContext);
		} catch(Throwable e) {
			throw handleException(e);
		}		
	}
	
	
	public void notificaEsito(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI, DocumentoEleTestataBulk documentoEleTestataBulk) throws ComponentException{
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
		try {
			List<DocumentoEleTrasmissioneBulk> lista = recuperoTrasmissione(usercontext, documentoEleTestataBulk.getIdentificativoSdi());
			documentoEleTestataBulk.setDocumentoEleTrasmissione(lista.get(0));
			home.notificaEsito(usercontext, tipoIntegrazioneSDI, documentoEleTestataBulk);
		} catch (IOException e) {
			handleException(e);
		}		
	}

	public boolean existsIdentificativo(UserContext usercontext, Long identificativoSdI) throws ComponentException {
		return !recuperoTrasmissione(usercontext, identificativoSdI).isEmpty();
	}

	public List<DocumentoEleTrasmissioneBulk> recuperoTrasmissione(UserContext usercontext, Long identificativoSdI) throws ComponentException {
		DocumentoEleTrasmissioneHome home = (DocumentoEleTrasmissioneHome) getHome(usercontext, DocumentoEleTrasmissioneBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "identificativoSdi", SQLBuilder.EQUALS, identificativoSdI);
		sql.addClause(FindClause.AND, "cmisNodeRef", SQLBuilder.ISNOTNULL, null);
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public List<DocumentoEleTestataBulk> recuperoDocumento(UserContext usercontext, Long identificativoSdI) throws ComponentException {
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "identificativoSdi", SQLBuilder.EQUALS, identificativoSdI);
		try {
			return home.fetchAll(sql);
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}

	public void aggiornaDecorrenzaTerminiSDI(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		for (DocumentoEleTestataBulk doc : listaDoc) {
			doc.setFlDecorrenzaTermini("S");
			doc.setToBeUpdated();
			updateBulk(userContext, doc);
		}
	}

	public void aggiornaConsegnaEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		for (DocumentoEleTestataBulk doc : listaDoc) {
			doc.setStatoNotificaEsito(DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_CONSEGNATO_SDI);
			doc.setDataRicevimentoMailRifiuto(dataRicevimentoMail!=null?new Timestamp(dataRicevimentoMail.getTime().getTime()):doc.getDataRicevimentoMailRifiuto());
			doc.setToBeUpdated();
			updateBulk(userContext, doc);
		}
	}

	public void aggiornaScartoEsitoPec(UserContext userContext, List<DocumentoEleTestataBulk> listaDoc, Calendar dataRicevimentoMail) throws PersistencyException, ComponentException,java.rmi.RemoteException {
		for (DocumentoEleTestataBulk doc : listaDoc) {
				doc.setStatoNotificaEsito(DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI);
			  doc.setDataRicevimentoMailRifiuto(dataRicevimentoMail!=null?new Timestamp(dataRicevimentoMail.getTime().getTime()):doc.getDataRicevimentoMailRifiuto());
				doc.setToBeUpdated();
				updateBulk(userContext, doc);
		}
	}

	@SuppressWarnings("unchecked")
	public void allineaEsitoCommitente(UserContext usercontext, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException {
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "statoNotificaEsito", SQLBuilder.EQUALS, DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI);		
		sql.addClause(FindClause.AND, "flDecorrenzaTermini", SQLBuilder.EQUALS, "N");		
		try {
			List<DocumentoEleTestataBulk> results = home.fetchAll(sql);
			getHomeCache(usercontext).fetchAll(usercontext);
			for (DocumentoEleTestataBulk documentoEleTestata : results) {
				notificaEsito(usercontext, tipoIntegrazioneSDI, documentoEleTestata);
	        	documentoEleTestata.setStatoNotificaEsito(null);
	        	documentoEleTestata.setToBeUpdated();
	        	modificaConBulk(usercontext, documentoEleTestata);
				logger.info("Inviata notifica per identificativo:" + documentoEleTestata.getIdentificativoSdi());				
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}	
	
	@SuppressWarnings("unchecked")
	public void allineaEsitoCommitente(UserContext usercontext, Long identificativoSdI, String statoSDI, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException {
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addClause(FindClause.AND, "identificativoSdi", SQLBuilder.EQUALS, identificativoSdI);
		sql.addClause(FindClause.AND, "statoNotificaEsito", SQLBuilder.EQUALS, DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI);		
		try {
			List<DocumentoEleTestataBulk> results = home.fetchAll(sql);
			getHomeCache(usercontext).fetchAll(usercontext);
			if (!results.isEmpty() && results.size() == 1) {
				DocumentoEleTestataBulk documentoEleTestata = results.get(0);
				if (!documentoEleTestata.getStatoDocumentoEle().equals(StatoDocumentoEleEnum.fromStatoSDI(statoSDI)) &&
						(documentoEleTestata.isRegistrata() || documentoEleTestata.isRifiutata())){	
					notificaEsito(usercontext, tipoIntegrazioneSDI, documentoEleTestata);
		        	documentoEleTestata.setStatoNotificaEsito(null);
		        	documentoEleTestata.setToBeUpdated();
		        	modificaConBulk(usercontext, documentoEleTestata);
					logger.info("Inviata notifica per identificativo:" + identificativoSdI + 
							" STATO SIGLA:"+ documentoEleTestata.getStatoDocumentoEle() + " - STATO SDI:" + StatoDocumentoEleEnum.fromStatoSDI(statoSDI));
				}
			}			
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}	

	@SuppressWarnings("unchecked")
	public void allineaEsitoCommitente(UserContext usercontext, List<Long> identificativi, TipoIntegrazioneSDI tipoIntegrazioneSDI) throws ComponentException {
		DocumentoEleTestataHome home = (DocumentoEleTestataHome) getHome(usercontext, DocumentoEleTestataBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		for (Long identificativoSdI : identificativi) {
			sql.addClause(FindClause.AND, "identificativoSdi", SQLBuilder.NOT_EQUALS, identificativoSdI);			
		}
		sql.openParenthesis(FindClause.AND);
		sql.addClause(FindClause.OR, "statoDocumento", SQLBuilder.EQUALS, StatoDocumentoEleEnum.RIFIUTATO.name());					
		sql.addClause(FindClause.OR, "statoDocumento", SQLBuilder.EQUALS, StatoDocumentoEleEnum.REGISTRATO.name());							
		sql.closeParenthesis();		
		sql.addClause(FindClause.AND, "statoNotificaEsito", SQLBuilder.EQUALS, DocumentoEleTestataBulk.STATO_CONSEGNA_ESITO_SCARTATO_SDI);
		try {
			List<DocumentoEleTestataBulk> results = home.fetchAll(sql);
			getHomeCache(usercontext).fetchAll(usercontext);
			for (DocumentoEleTestataBulk documentoEleTestata : results) {
				notificaEsito(usercontext, tipoIntegrazioneSDI, documentoEleTestata);
	        	documentoEleTestata.setStatoNotificaEsito(null);
	        	documentoEleTestata.setToBeUpdated();
	        	modificaConBulk(usercontext, documentoEleTestata);
				logger.info("Inviata notifica per identificativo:" + documentoEleTestata.getIdentificativoSdi() + 
						" STATO SIGLA:"+ documentoEleTestata.getStatoDocumentoEle() + " - STATO SDI NON PRESENTE");				
			}
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}	

}