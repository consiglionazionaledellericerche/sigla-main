package it.cnr.contab.ordmag.richieste.comp;

import java.io.Serializable;
import java.rmi.RemoteException;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopHome;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class RichiestaUopComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,Cloneable,Serializable {

	public final static String TIPO_TOTALE_COMPLETO = "C";
	public final static String TIPO_TOTALE_PARZIALE = "P";
	
    public  RichiestaUopComponent()
    {

        /*Default constructor*/


    }
    
	private void assegnaProgressivo(UserContext userContext,RichiestaUopBulk richiesta) throws ComponentException {

	try {
		// Assegno un nuovo progressivo al documento
		NumeratoriOrdMagComponentSession progressiviSession = (NumeratoriOrdMagComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRORDMAG_EJB_NumeratoriOrdMagComponentSession", NumeratoriOrdMagComponentSession.class);
		NumerazioneOrdBulk numerazione = new NumerazioneOrdBulk(richiesta.getCdUnitaOperativa(), richiesta.getEsercizio(), richiesta.getCdNumeratore());
		richiesta.setNumero(progressiviSession.getNextPG(userContext, numerazione));
	} catch (Throwable t) {
		throw handleException(richiesta, t);
	}
}
//private java.math.BigDecimal calcolaTotale(it.cnr.jada.UserContext userContext, RichiestaUopBulk documento) throws it.cnr.jada.comp.ComponentException {
//
//	Documento_generico_rigaBulk riga=null;
//	java.math.BigDecimal importo=new java.math.BigDecimal(0);
//    for (java.util.Iterator i= documento.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
//        riga= (Documento_generico_rigaBulk) i.next();
//        //if (Documento_generico_rigaBulk.STATO_INIZIALE.equals(riga.getStato_cofi()))
//            //numeroDiRigheNonContabilizzate++;
//        if (riga.getIm_riga() != null) {
//            importo= importo.add(riga.getIm_riga()).setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//            riga.setIm_riga(riga.getIm_riga().setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
//        }
//    }
//    importo.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);
//
//    return importo;
//}
public OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException {

	return creaConBulk(userContext, bulk, null);
}
////^^@@
///** 
//  *  Creazione di un nuovo documento
//  *	 Validazioni superate
//  *    PreCondition:
//  *      Viene richiesto il salvataggio di un nuovo documento
//  *    PostCondition:
//  *      Salva.
//  *  Validazioni non superate
//  *    PreCondition:
//  *      Viene richiesto il salvataggio di un nuovo documento ma le validazioni
//  *      non vengono superate
//  *    PostCondition:
//  *      Informa l'utente della causa per la quale non è possibile salvare
// */
////^^@@
	public it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext userContext, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status)
			throws it.cnr.jada.comp.ComponentException {

		RichiestaUopBulk richiesta= (RichiestaUopBulk) bulk;
		try {

			//effettua il controllo di validazione    
			try {		
				if (existARowToBeInventoried(userContext,documento)) {
					verificaEsistenzaEdAperturaInventario(userContext, documento);
					if (documento.hasCompetenzaCOGEInAnnoPrecedente())
						throw new it.cnr.jada.comp.ApplicationException("Attenzione: per le date competenza indicate non è possibile inventariare i beni.");		

					if(hasDocumentoPassivoARowNotInventoried(userContext, documento))
						throw new it.cnr.jada.comp.ApplicationException("Attenzione: è necessario inventariare tutti i dettagli.");
				}
				validaDocumento(userContext, documento);
			} catch (it.cnr.jada.comp.ApplicationException e) {
				throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
			}
			Lettera_pagam_esteroBulk lettera = documento.getLettera_pagamento_estero();
			if (lettera != null) {

				Lettera_pagam_esteroBulk original1210 = (Lettera_pagam_esteroBulk)getHome(userContext, lettera).findByPrimaryKey(lettera);
				aggiornaLetteraPagamentoEstero(userContext, lettera);
				if (!documento.isFlagEnte() &&
						(original1210 == null ||
						lettera.getIm_pagamento().compareTo(original1210.getIm_pagamento()) != 0))
					validaDisponibilitaDiCassaCDS(userContext, documento);

			}

			//assegna un progressivo al documento all'atto della creazione.
			assegnaProgressivo(userContext, richiesta);

			// Salvo temporaneamente l'hash map dei saldi
			PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
			if (documento.getDefferredSaldi() != null)
				aTempDiffSaldi=(PrimaryKeyHashMap)documento.getDefferredSaldi().clone();    

			if (!documento.isGenericoAttivo()) {
				manageDocumentiContabiliCancellatiPerGenericoPassivo(userContext, documento,status);
				aggiornaObbligazioni(userContext, documento, status);
			}
			if (documento.isGenericoAttivo()) {
				manageDocumentiContabiliCancellatiPerGenericoAttivo(userContext, documento,status);
				aggiornaAccertamenti(userContext, documento, status);
			}
			documento = (RichiestaUopBulk)super.creaConBulk(userContext, documento);
			// Restore dell'hash map dei saldi
			if (documento.getDefferredSaldi() != null)
				documento.getDefferredSaldi().putAll(aTempDiffSaldi);

			aggiornaCogeCoanDocAmm(userContext, documento); 
			try {
				if (!verificaStatoEsercizio(
						userContext, 
						new EsercizioBulk( 
								documento.getCd_cds(), 
								((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio())))
					throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
			} catch (it.cnr.jada.comp.ApplicationException e) {
				throw handleException(bulk, e);
			}

			if (documento.getTi_entrate_spese()==RichiestaUopBulk.SPESE){
				prepareCarichiInventario(userContext, documento);
				aggiornaCarichiInventario(userContext, documento);
				// Le operazioni che rendono persistenti le modifiche fatte sull'Inventario,
				//	potrebbero rimandare un messaggio all'utente.
				String messaggio = aggiornaAssociazioniInventario(userContext, documento);

			}
			else{
				prepareScarichiInventario(userContext, documento);
				aggiornaScarichiInventario(userContext, documento);
				String messaggio = aggiornaAssociazioniInventario(userContext, documento);
			}


			controllaQuadraturaInventario(userContext,documento);

		} catch (it.cnr.jada.persistency.PersistencyException ex) {
			throw handleException( ex);
		}
		return richiesta;
	}
////^^@@
///** 
//  *  tutti i controlli superati.
//  *    PreCondition:
//  *      Nessun errore rilevato.
//  *    PostCondition:
//  *      Permette la cancellazione del documento.
//  *  validazione eliminazione documento.
//  *    PreCondition:
//  *      E' stata eliminata una documento in stato B or C
//  *    PostCondition:
//  *      Viene inviato un messaggio:"Attenzione non si può eliminare una documento in stato IVA B o C"
// */
////^^@@
//
//private void deleteLogically(UserContext userContext, RichiestaUopBulk documento) throws ComponentException {
//
//    if (documento instanceof Voidable && ((Voidable) documento).isVoidable()) {
//        try {
//			java.sql.Timestamp dataAnnullamento = getHome(userContext, documento).getServerTimestamp();
//			java.util.Calendar dtAnnullamentoCal = GregorianCalendar.getInstance();
//			dtAnnullamentoCal.setTime(dataAnnullamento);
////			java.util.Calendar dtAnnullamentoCal = documento.getDateCalendar(dataAnnullamento);
//			int annoSolare = dtAnnullamentoCal.get(java.util.Calendar.YEAR);
//			if (annoSolare != it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue()) {
//				//Sono costretto ad utilizzare il Calendar per mantenere ora, minuti e secondi.
//				dtAnnullamentoCal.set(java.util.Calendar.MONTH, Calendar.DECEMBER);
//				dtAnnullamentoCal.set(java.util.Calendar.DAY_OF_MONTH, 31);
//				dtAnnullamentoCal.set(java.util.Calendar.YEAR, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue());
//				dataAnnullamento = new java.sql.Timestamp(dtAnnullamentoCal.getTime().getTime());
//			}
//            for (int c = 0; c < documento.getBulkLists().length; c++) {
//                it.cnr.jada.bulk.BulkList bl = (it.cnr.jada.bulk.BulkList) documento.getBulkLists()[c];
//                for (Iterator i = bl.iterator(); i.hasNext();) {
//                    OggettoBulk obj = (OggettoBulk) i.next();
//                    if (obj instanceof Documento_generico_rigaBulk) {
//                        Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk) obj;
//                        if (riga instanceof Voidable && ((Voidable) riga).isVoidable()) {
//                            ((Voidable) riga).setAnnullato(dataAnnullamento);
//                            //if (riga.getDocumento_generico().isGenericoAttivo()) {
//                            //riga.getAccertamento_scadenziario().setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
//                            //updateImportoAssociatoDocAmm(userContext, riga.getAccertamento_scadenziario());
//                            //} else {
//                            ////if (!riga.getDocumento_generico().isPassivo_ente())
//                            ////riga.getObbligazione_scadenziario().setIm_associato_doc_amm(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
//                            ////else
//                            //if (riga.getDocumento_generico().isPassivo_ente()) {
//                            //riga.getObbligazione_scadenziario().setIm_associato_doc_amm(
//                            //riga.getObbligazione_scadenziario().getIm_associato_doc_amm().subtract((riga.getIm_riga_iniziale() == null ? riga.getIm_imponibile() : riga.getIm_riga_iniziale())).setScale(
//                            //2,
//                            //java.math.BigDecimal.ROUND_HALF_UP));
//                            //updateImportoAssociatoDocAmm(userContext, riga.getObbligazione_scadenziario());
//                            //}
//                            //}
//                            riga.setToBeUpdated();
//                            if (riga.isInventariato()) {
//                            	Documento_generico_rigaBulk cloneDettaglio = (Documento_generico_rigaBulk)riga.clone();
//								cloneDettaglio.setToBeDeleted();
//								deleteAssociazioniInventarioWith(userContext, cloneDettaglio);
//							}
//                            update(userContext, riga);
//                        } else
//                            throw new it.cnr.jada.comp.ApplicationException(
//                                "Questo documento NON è annullabile perché almeno uno dei sui dettagli non è stato associato a mandato o reversale!");
//                    }
//                }
//            }
//
//            documento.setAnnullato(dataAnnullamento);
//            if (documento.REGISTRATO_IN_COGE.equalsIgnoreCase(documento.getStato_coge()))
//                documento.setStato_coge(documento.DA_RIREGISTRARE_IN_COGE);
//
//			// Gennaro Borriello - (11/10/2004 16.59.48)
//			//	Aggiunto controllo sullo STATO_COAN, così come avviene per gli altri documenti.
//			if (documento.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(documento.getStato_coan()))
//				documento.setStato_coan(documento.DA_RICONTABILIZZARE_IN_COAN);
//				
//            documento.setToBeUpdated();
//            updateBulk(userContext, documento);
//            if (documento instanceof RichiestaUopBulk
//                && (documento.getObbligazioniHash() != null || documento.getAccertamentiHash() != null)) {
//                if (documento.getTi_entrate_spese() == documento.SPESE) {
//                    aggiornaObbligazioniSuCancellazione(userContext, documento, documento.getObbligazioniHash().keys(), null, null);
//                    Lettera_pagam_esteroBulk lettera = documento.getLettera_pagamento_estero();
//                    if (lettera != null && lettera.getSospeso() != null && lettera.getSospeso().getCrudStatus() == OggettoBulk.NORMAL)
//                        liberaSospeso(userContext, lettera.getSospeso());
//                } else
//                    aggiornaAccertamentiSuCancellazione(userContext, documento, documento.getAccertamentiHash().keys(), null, null);
//            }
//            return;
//        } catch (it.cnr.jada.persistency.PersistencyException e) {
//            throw handleException(documento, e);
//        }
//    }
//    throw new it.cnr.jada.comp.ApplicationException("Questa fattura NON è annullabile!");
//
//}
////^^@@
///** 
//  *  tutti i controlli superati.
//  *    PreCondition:
//  *      Nessun errore rilevato.
//  *    PostCondition:
//  *      Permette la cancellazione del documento.
//  *  validazione eliminazione documento.
//  *    PreCondition:
//  *      E' stata eliminata una documento in stato B or C
//  *    PostCondition:
//  *      Viene inviato un messaggio:"Attenzione non si può eliminare una documento in stato IVA B o C"
// */
////^^@@
//
//private void deletePhisically(UserContext aUC, RichiestaUopBulk documento) throws ComponentException {
//
//    //if (!documento.isGenericoAttivo())
//    //aggiornaObbligazioni(aUC, documento,null);
//    //if (documento.isGenericoAttivo())
//    //aggiornaAccertamenti(aUC, documento,null);
//    for (int c= 0; c < documento.getBulkLists().length; c++) {
//        BulkList bl= (it.cnr.jada.bulk.BulkList) documento.getBulkLists()[c];
//        for (Iterator i= bl.iterator(); i.hasNext();) {
//            OggettoBulk obj= (OggettoBulk) i.next();
//            obj.setToBeDeleted();
//            if (obj instanceof Documento_generico_rigaBulk) {
//            	Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)obj;
//				if (riga.isInventariato())
//					deleteAssociazioniInventarioWith(aUC, riga);
//			}
//        }
//    }
//
//    //manageDeletedElements(aUC, documento);
//    super.eliminaConBulk(aUC, documento);
//
//    Lettera_pagam_esteroBulk lettera= documento.getLettera_pagamento_estero();
//    if (lettera != null)
//        try {
//            if (lettera.getSospeso() != null && lettera.getSospeso().getCrudStatus() == OggettoBulk.NORMAL)
//                liberaSospeso(aUC, lettera.getSospeso());
//            lettera.setToBeDeleted();
//            makeBulkPersistent(aUC, lettera);
//        } catch (it.cnr.jada.persistency.PersistencyException e) {
//            throw handleException(lettera, e);
//        }
//
//    if (documento.isGenericoAttivo())
//        try {
//            aggiornaAccertamentiSuCancellazione(aUC, documento, documento.getAccertamentiHash().keys(), null, null);
//        } catch (Throwable e) {
//            throw handleException(documento, e);
//        }
//
//    if (!documento.isGenericoAttivo())
//        try {
//            aggiornaObbligazioniSuCancellazione(aUC, documento, documento.getDocumento_generico_obbligazioniHash().keys(), null, null);
//        } catch (Throwable e) {
//            throw handleException(documento, e);
//        }
//}
////^^@@
///** 
//  *  Eliminazione di un documento
//  *	 Elimina
//  *    PreCondition:
//  *      Il documento è eliminabile
//  *    PostCondition:
//  *      richiama la funzione deletePhisically.
//  *  Annulla
//  *    PreCondition:
//  *      Il documento è annullabile
//  *    PostCondition:
//  *      richiama la funzione deleteLogically.
// */
////^^@@
//public void eliminaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
//
//	 RichiestaUopBulk documento= (RichiestaUopBulk) bulk;
//	    Documento_generico_rigaBulk documentoGenericoRiga;
//	    Integer contaRighe=0;
//		try {
//			eliminaDocumento(aUC, documento);
//			for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext();){
//				documentoGenericoRiga =(Documento_generico_rigaBulk)i.next();
//				eliminaRiga(aUC,documentoGenericoRiga);
//					if (documentoGenericoRiga.getDocumento_generico().isGenericoAttivo()) {
//						try {
//							List result = findReversaleRigaCollegate(aUC, documentoGenericoRiga);
//							if (result!=null && !result.isEmpty()){
//								documentoGenericoRiga.setToBeUpdated();
//								documentoGenericoRiga.setTi_associato_manrev(RichiestaUopBulk.ASSOCIATO_A_MANDATO);
//								contaRighe++;
//							}
//								
//						} catch (PersistencyException e) {
//							throw new ComponentException(e);
//						} catch (IntrospectionException e) {
//							throw new ComponentException(e);
//						}
//					}
//					else
//					{
//						try {
//						List result = findMandatoRigaCollegati(aUC, documentoGenericoRiga);
//							if (result!=null && !result.isEmpty()){
//									documentoGenericoRiga.setToBeUpdated();
//									documentoGenericoRiga.setTi_associato_manrev(RichiestaUopBulk.ASSOCIATO_A_MANDATO);
//									contaRighe++;
//							}
//						} catch (PersistencyException e) {
//							throw new ComponentException(e);
//						} catch (IntrospectionException e) {
//							throw new ComponentException(e);
//						}
//					}
//			}
//			if(contaRighe== documento.getDocumento_generico_dettColl().size()){
//				documento.setToBeUpdated();
//				documento.setTi_associato_manrev(RichiestaUopBulk.ASSOCIATO_A_MANDATO);
//			}
//		} catch (it.cnr.jada.comp.ApplicationException e) {
//			throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
//		}
//	
//    // Salvo temporaneamente l'hash map dei saldi
//    PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
//    if (documento.getDefferredSaldi() != null)
//	    aTempDiffSaldi=(PrimaryKeyHashMap)documento.getDefferredSaldi().clone();    
//
//	if (documento instanceof Voidable && documento.isVoidable()) {
//        //modificaConBulk(aUC, bulk);
//        deleteLogically(aUC, documento);
//    } else
//        deletePhisically(aUC, documento);
//
//    // Restore dell'hash map dei saldi
//    if (documento.getDefferredSaldi() != null)
//		documento.getDefferredSaldi().putAll(aTempDiffSaldi);
//	aggiornaCogeCoanDocAmm(aUC, documento);
//
//	try {
//        if (!verificaStatoEsercizio(
//	        					aUC, 
//	        					new EsercizioBulk( 
//		        							documento.getCd_cds(), 
//		        							((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio())))
//            throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!");
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw handleException(bulk, e);
//	}
//}
//private void eliminaDocumento(UserContext aUC,RichiestaUopBulk documento)
//	throws ComponentException {
//
//	if (documento.getEsercizio().intValue()==it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()){
//	 if (!verificaStatoEsercizio(aUC, new EsercizioBulk( documento.getCd_cds(), documento.getEsercizio())))
//		 throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare un documento per un esercizio non aperto!"); 	  
//	} else if(documento.getEsercizio().intValue() > it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()) {
//		 throw new it.cnr.jada.comp.ApplicationException("Operazione non permessa!");
//	} else {	
//		// Gennaro Borriello - (11/10/2004 17.29.46)
//		//	Aggiunto controllo per i documenti RIPORTATI, in base all'esercizio COEP precedente
//		//	all'es. di scrivania.
//		//
//		// Gennaro Borriello - (02/11/2004 16.48.21)
//		// Fix sul controllo dello "Stato Riportato": controlla che il documento sia stato riportato DA UN ES. PRECEDENTE a quello di scrivania.
//		if (documento.isRiportataInScrivania() ){
//			Integer es_prec = new Integer(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue()-1);
//			if (!isEsercizioCoepChiusoFor(aUC, documento, es_prec)){
//				throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile eliminare il documento, poichè l'esercizio economico precedente a quello in scrivania non è chiuso.");	
//			}
//		} 
//		else 
//		 throw new it.cnr.jada.comp.ApplicationException("Impossibile eliminare il documento perchè non risulta riportato nell'esercizio di scrivania!");
//	}
//
//	if (documento.STATO_PARZIALE.equalsIgnoreCase(documento.getStato_cofi()))
//		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare un documento in stato parziale.");
//
//	if (documento.isPagata())
//		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare un documento pagato o registrato in fondo economale!");
//
//	if (documento.getTipo_documento() != null &&
//		"GEN_RC_DAT".equalsIgnoreCase(documento.getTipo_documento().getDs_tipo_documento_amm()))
//		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non è possibile cancellare un documento generico \"per recupero crediti da terzi\"!");
//
//	//Controllo nel caso di documento annullabile che tutti i dettagli siano
//	//annullabili.
//	if (documento.isVoidable()) {
//		boolean deletable = true;
//		for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
//			boolean voidable = ((Voidable)i.next()).isVoidable();
//			if (!voidable && deletable)
//				deletable = false;
//		}
//		if (!deletable)
//			throw new it.cnr.jada.comp.ApplicationException("Attenzione: questo documento non è annullabile perchè ha dettagli non associati a mandati/reversali o\nla testata è registrata in COAN o COGE!");
//	}
//}
////^^@@
///** 
//  *  tutti i controlli superati.
//  *    PreCondition:
//  *      Nessun errore rilevato.
//  *    PostCondition:
//  *      Permette la cancellazione della lettera di pagamento estero (Doc 1210).
//  *  validazione esistenza lettera 1210.
//  *    PreCondition:
//  *      Non è rilevata la presenza di doc 1210
//  *    PostCondition:
//  *      Esce dal metodo senza apportare modifiche
// */
////^^@@
//
//public RichiestaUopBulk eliminaLetteraPagamentoEstero(
//	UserContext context,
//	RichiestaUopBulk docGen,boolean cancella)
//	throws ComponentException {
//	
//	if (docGen != null &&
//		docGen.getLettera_pagamento_estero() != null) {
//
//		Lettera_pagam_esteroBulk lettera = docGen.getLettera_pagamento_estero();
//		try {
//			
//			if(cancella){
//				basicAggiornaLetteraPagamentoEstero(context, lettera, null);
//				lettera.setToBeDeleted();
//				makeBulkPersistent(context, lettera);
//			}else{
//				java.sql.Timestamp dataAnnullamento = getHome(context, docGen).getServerDate();
//				lettera.setToBeUpdated();
//				lettera.setDt_cancellazione(dataAnnullamento);
//				if(lettera.getSospeso()!=null){
//					liberaSospeso(context, lettera.getSospeso());
//					lettera.setSospeso(null);
//				}
//				makeBulkPersistent(context, lettera);
//			}
//					
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(lettera, e);
//		}
//		docGen.setLettera_pagamento_estero(null);
//	}
//	return docGen;
//}
////^^@@
///** 
//  *  Controlla se una riga del documento è eliminabile
//  *  lo stato del documento è PAGATO
//  *    PreCondition:
//  *      Richiesta di eliminare una riga
//  *    PostCondition:
//  *      Avverte l'utente che non è possibile eliminare dei dettagli in un documento già pagato
// */
////^^@@
//public void eliminaRiga(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws ComponentException {
//
//
//	//if (RichiestaUopBulk.STATO_PAGATO.equalsIgnoreCase(documentoGenericoRiga.getDocumento_generico().getStato_cofi()))
//		//throw new it.cnr.jada.comp.ApplicationException("Attenzione non si può eliminare un dettaglio in un documento pagato");
//
//	//if (documentoGenericoRiga.getStato_cofi()!=null && (documentoGenericoRiga.getStato_cofi().equals(documentoGenericoRiga.STATO_CONTABILIZZATO) || documentoGenericoRiga.getStato_cofi().equals(documentoGenericoRiga.STATO_PAGATO)))
//		//throw new it.cnr.jada.comp.ApplicationException("Attenzione non si può eliminare un dettaglio in stato non iniziale");
//		
//	if (documentoGenericoRiga.getDocumento_generico().isPagata())
//		throw new it.cnr.jada.comp.ApplicationException("Attenzione non si può eliminare un dettaglio in un documento pagato");
//
//	if (documentoGenericoRiga.isPagata())
//		throw new it.cnr.jada.comp.ApplicationException("Attenzione: non si può eliminare il dettaglio " + ((documentoGenericoRiga.getDs_riga() == null)? "" : documentoGenericoRiga.getDs_riga()) + " già pagato.");
//}
////^^@@
///** 
//  *  cerca i dati relativi alla banca
//  *    PreCondition:
//  *      Viene impostata o cambiata la banca
//  *    PostCondition:
//  *      restituisce i dati relativi alla banca selezionata.
// */
////^^@@
//public BancaBulk findBancaSelezionata(UserContext aUC, Documento_generico_rigaBulk documentoGenericoRiga)
//    throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
//
//    try {
//        Long banca;
//        Integer terzo;
//
//        it.cnr.jada.bulk.BulkHome home= getHome(aUC, BancaBulk.class);
//        it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//
//        if (documentoGenericoRiga.getDocumento_generico().isGenericoAttivo() && 
//	        documentoGenericoRiga.getDocumento_generico().getTerzo_uo_cds()==null)
//        return null;
//        
//        if (!documentoGenericoRiga.getDocumento_generico().isGenericoAttivo()) {
//            banca= documentoGenericoRiga.getPg_banca();
//            terzo= documentoGenericoRiga.getTerzo().getCd_terzo();
//        } else {
//            banca= documentoGenericoRiga.getPg_banca_uo_cds();
//            if (documentoGenericoRiga.getTerzo_uo_cds()!=null)
//	            terzo= documentoGenericoRiga.getTerzo_uo_cds().getCd_terzo();
//	        else
//	        	terzo= documentoGenericoRiga.getDocumento_generico().getTerzo_uo_cds().getCd_terzo();
//        }
//
//        if (banca == null)
//            return null;
//        sql.addClause("AND", "pg_banca", sql.EQUALS, banca);
//        sql.addClause("AND", "cd_terzo", sql.EQUALS, terzo);
//
//        it.cnr.jada.persistency.Broker broker= home.createBroker(sql);
//        if (!broker.next())
//            return null;
//
//        BancaBulk bancaBulk= (BancaBulk) broker.fetch(BancaBulk.class);
//        broker.close();
//        return bancaBulk;
//
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(e);
//    }
//}
////^^@@
///** 
//  *	  gestione dei dati relativi al terzo cessionario
//  *		PreCondition:
//  * 		Richiesta dell'anagrafico del cessionario
//  *   	PostCondition:
//  *  		Restituisce l'anagrafico
// */
////^^@@
//
//public TerzoBulk findCessionario(UserContext userContext, Documento_generico_rigaBulk riga) throws ComponentException {
//
//	try	{
//		if (riga == null || riga.getModalita_pagamento() == null)
//			return null;
//		Modalita_pagamentoHome mph = (Modalita_pagamentoHome)getHome(userContext, Modalita_pagamentoBulk.class);
//        TerzoBulk terzo = (riga.getDocumento_generico().getTi_entrate_spese() == RichiestaUopBulk.SPESE) ?
//        								riga.getTerzo() :
//										findTerzoUO(
//												userContext,
//												riga.getDocumento_generico());
//	    if (terzo==null) return null;
//	    
//		if (riga.getBanca()==null || riga.getBanca().getCd_terzo_delegato() == null) return null;
//		TerzoHome th = (TerzoHome)getHome(userContext, TerzoBulk.class);
//		return (TerzoBulk)th.findByPrimaryKey(new TerzoBulk(riga.getBanca().getCd_terzo_delegato() ));
//		
//	} catch( Exception e ) {
//		throw handleException(e);
//	}		
//}
////^^@@
///** 
//  *  Richiesta di un elenco di banche
//  *    PreCondition:
//  *      Richiesta di un elenco di banche per la riga
//  *    PostCondition:
//  *      Viene restituita la lista delle banche del fornitore/cliente.
// */
////^^@@
//public java.util.Collection findListabanche(
//    UserContext aUC,
//    Documento_generico_rigaBulk riga)
//    throws
//        ComponentException,
//        it.cnr.jada.persistency.PersistencyException,
//        it.cnr.jada.persistency.IntrospectionException {
//
//    if (riga.getTerzo() == null)
//        return null;
//
//    return getHome(aUC, BancaBulk.class).fetchAll(
//        selectBancaByClause(aUC, riga, null, null));
//}
////^^@@
///** 
//  *	Tutti i controlli  superati.
//  *		PreCondition:
//  * 		Richiesta ricerca delle modalità di pagamento del cliente/fornitore
//  *   	PostCondition:
//  *  		Restituisce la collezione di modalità di pagamento del cliente/fornitore
//  *	Validazione del fornitore
//  *		PreCondition:
//  *			Si è verificato un errore nel caricamento delle modalità di pagamento del cliente/fornitore.
//  * 	PostCondition:
//  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
// */
////^^@@
//public java.util.Collection findModalita(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws
//        ComponentException,
//        it.cnr.jada.persistency.PersistencyException,
//        it.cnr.jada.persistency.IntrospectionException {
//
//    try {
//	    TerzoHome home = (TerzoHome) getHome(aUC, TerzoBulk.class);
//	    Collection modalita = null;
//	    if (documentoGenericoRiga.getDocumento_generico().getTi_entrate_spese()==documentoGenericoRiga.getDocumento_generico().SPESE) {
//	        TerzoBulk terzo = documentoGenericoRiga.getTerzo();
//	        if (terzo == null || terzo.getCd_terzo() == null) return null;
//	        modalita = home.findRif_modalita_pagamento(terzo, null);
//	    } else {
//		    TerzoBulk terzo = findTerzoUO(aUC,documentoGenericoRiga.getDocumento_generico());
//	        if (terzo == null || terzo.getCd_terzo() == null) return null;
//	        modalita = home.findRif_modalita_pagamento(terzo);
//	    }
//        if (modalita == null || modalita.isEmpty())
//        		return null;        
//        return modalita;
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(e);
//    } catch (it.cnr.jada.persistency.IntrospectionException e) {
//        throw handleException(e);
//    }
//}
////^^@@
///** 
//  *  cerca i dati relativi alla modalità selezionata
//  *    PreCondition:
//  *      Viene cambiata la modalità di pagamento
//  *    PostCondition:
//  *      restituisce i dati relativi alla modalità selezionata
// */
////^^@@
//public Rif_modalita_pagamentoBulk findModalitaSelezionate(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws
//        ComponentException,
//        it.cnr.jada.persistency.PersistencyException,
//        it.cnr.jada.persistency.IntrospectionException {
//
//    try {
//	    String modalita;
//
//	    it.cnr.jada.bulk.BulkHome home = getHome(aUC, Rif_modalita_pagamentoBulk.class);
//		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
//	
//	    if (documentoGenericoRiga.getDocumento_generico().getTi_entrate_spese()==documentoGenericoRiga.getDocumento_generico().SPESE)
//	    	modalita=documentoGenericoRiga.getCd_modalita_pag();
//	    else
//	    	modalita=documentoGenericoRiga.getCd_modalita_pag_uo_cds()!=null?documentoGenericoRiga.getCd_modalita_pag_uo_cds():
//	    	(documentoGenericoRiga.getModalita_pagamento_uo_cds() == null ? null : documentoGenericoRiga.getModalita_pagamento_uo_cds().getCd_modalita_pag());
//
//	    sql.addClause("AND", "cd_modalita_pag", sql.EQUALS, modalita);
//
//		it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
//		if (!broker.next())
//    	        return null;
// 
//		Rif_modalita_pagamentoBulk modalitaBulk=(Rif_modalita_pagamentoBulk) broker.fetch(Rif_modalita_pagamentoBulk.class);
//		broker.close();
//		return modalitaBulk;
// 
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(e);
//    } 
//}
////^^@@
///** 
//  *	Tutti i controlli  superati.
//  *		PreCondition:
//  * 		Richiesta ricerca dei termini di pagamento del fornitore/cliente
//  *   	PostCondition:
//  *  		Restituisce la collezione dei termini di pagamento del fornitore/cliente
//  *	Validazione del fornitore
//  *		PreCondition:
//  *			Si è verificato un errore nel caricamento dei termini di pagamento del fornitore/cliente.
//  * 	PostCondition:
//  *  		Viene inviato il messaggio corrispondente all'errore segnalato.
// */
////^^@@
//public java.util.Collection findTermini(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws
//        ComponentException,
//        it.cnr.jada.persistency.PersistencyException,
//        it.cnr.jada.persistency.IntrospectionException {
//
//    try {
//	    it.cnr.contab.anagraf00.core.bulk.TerzoHome home;
//	    TerzoBulk terzo;
//	    if (documentoGenericoRiga.getDocumento_generico().getTi_entrate_spese()==documentoGenericoRiga.getDocumento_generico().SPESE) {
//	        terzo = documentoGenericoRiga.getTerzo();	        
//	    } else {
//		    terzo = findTerzoUO(aUC,documentoGenericoRiga.getDocumento_generico());
//	    }
//	    if (terzo==null)
//	    	return null;
//       	home = (it.cnr.contab.anagraf00.core.bulk.TerzoHome) getHome(aUC, terzo);
//        Collection termini = home.findRif_termini_pagamento(terzo);
//        if (termini.isEmpty())
//        		return null;        
//       return termini;        
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(e);
//    } catch (it.cnr.jada.persistency.IntrospectionException e) {
//        throw handleException(e);
//    }
//}
////^^@@
///** 
//  *  cerca i dati relativi ai termini selezionati
//  *    PreCondition:
//  *      Viengono cambiati i termini selezionati
//  *    PostCondition:
//  *      restituisce i dati relativi ai termini selezionati
// */
////^^@@
//public Rif_termini_pagamentoBulk findTerminiSelezionati(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws
//        ComponentException,
//        it.cnr.jada.persistency.PersistencyException,
//        it.cnr.jada.persistency.IntrospectionException {
//
//    try {
//	    String termini;
//
//	    it.cnr.jada.bulk.BulkHome home = getHome(aUC, Rif_termini_pagamentoBulk.class);
//		it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
//	
//	    if (documentoGenericoRiga.getDocumento_generico().getTi_entrate_spese()==documentoGenericoRiga.getDocumento_generico().SPESE)
//	    	termini=documentoGenericoRiga.getCd_termini_pag();
//	    else
//	    	termini=documentoGenericoRiga.getCd_termini_pag_uo_cds()!=null?documentoGenericoRiga.getCd_termini_pag_uo_cds():
//	    	    (documentoGenericoRiga.getTermini_pagamento_uo_cds()==null ? null: documentoGenericoRiga.getTermini_pagamento_uo_cds().getCd_termini_pag());
//
//	    if (termini==null) return null;
//	    sql.addClause("AND", "cd_termini_pag", sql.EQUALS, termini);
//
//		it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
//		if (!broker.next())
//    	        return null;
// 
//		Rif_termini_pagamentoBulk terminiBulk=(Rif_termini_pagamentoBulk) broker.fetch(Rif_termini_pagamentoBulk.class);
//		broker.close();
//		return terminiBulk;
// 
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(e);
//    } 
//}
////^^@@
///** 
//  *	 All'atto della creazione di un generico attivo viene impostato il terzo
//  *		PreCondition:
//  * 		ricerca del terzo di scrivania per i doc. attivi
//  *   	PostCondition:
//  *  		Restituisce il terzo
// */
////^^@@
//public TerzoBulk findTerzoUO(UserContext aUC,RichiestaUopBulk doc) throws ComponentException,it.cnr.jada.persistency.PersistencyException,it.cnr.jada.persistency.IntrospectionException {
//	
//
//	if (doc == null) return null;
//	
//	it.cnr.jada.bulk.BulkHome home = getHome(aUC, TerzoBulk.class);
//	it.cnr.jada.persistency.sql.SQLBuilder sql = home.createSQLBuilder();
//	
//	sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, doc.getCd_uo_origine());
//
//	it.cnr.jada.persistency.Broker broker = home.createBroker(sql);
//        if (!broker.next())
//            return null;
// 
//	TerzoBulk terzo=(TerzoBulk) broker.fetch(TerzoBulk.class);
//	broker.close();
//	return terzo;
//	
//}
////^^@@
///** 
//  *	Restituisce l'insieme dei tipi di documento per popolare liste e per controlli
//  *		PreCondition:
//  * 		Riechiesta dei tipi di documento compatibili
//  *   	PostCondition:
//  *  		Restituisce la collezione di tipo documento
// */
////^^@@
//public java.util.Collection findTipi_doc(UserContext aUC, RichiestaUopBulk doc)
//    throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
//
//    if (doc == null)
//        return null;
//    java.util.Collection tipi= null;
//    it.cnr.jada.bulk.BulkHome home= getHome(aUC, Tipo_documento_ammBulk.class);
//    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//
//    if (doc.getTipo_documento() != null && doc.getTipo_documento().getCd_tipo_documento_amm() != null) {
//        sql.addSQLClause("AND", "CD_TIPO_DOCUMENTO_AMM", sql.EQUALS, doc.getTipo_documento().getCd_tipo_documento_amm());
//    } else {
//        sql.addSQLClause("AND", "TI_ENTRATA_SPESA", sql.EQUALS, String.valueOf(doc.getTi_entrate_spese()));
//        sql.addSQLClause("AND", "FL_DOC_GENERICO", sql.EQUALS, "Y");
//        sql.addSQLClause("AND", "FL_UTILIZZO_DOC_GENERICO", sql.EQUALS, "Y");
//        if (doc.getCd_cds_origine().equals(doc.getCds_CNR()))
//        	sql.addSQLClause("AND", "FL_SOLO_PARTITA_GIRO", sql.NOT_EQUALS, "Y");        
//    }
//
//    tipi= (java.util.Collection) home.fetchAll(sql);
//    return tipi;
//
//}
////^^@@
///** 
//  *	Restituisce l'insieme dei tipi di documento per la stampa
//  *		PreCondition:
//  * 		Riechiesta dei tipi di documento compatibili
//  *   	PostCondition:
//  *  		Restituisce la collezione di tipo documento
// */
////^^@@
//private java.util.Collection findTipi_doc_for_print(UserContext aUC, Stampa_vpg_doc_genericoBulk stampa)
//    throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
//
//    if (stampa == null)
//        return null;
//
//    it.cnr.jada.bulk.BulkHome home= getHome(aUC, Tipo_documento_ammBulk.class);
//    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//
//    sql.addSQLClause("AND", "FL_DOC_GENERICO", sql.EQUALS, "Y");
//
//    //if (doc.getTipo_documento() != null && doc.getTipo_documento().getCd_tipo_documento_amm() != null) {
//        //sql.addSQLClause("AND", "CD_TIPO_DOCUMENTO_AMM", sql.EQUALS, doc.getTipo_documento().getCd_tipo_documento_amm());
//    //} else {
//        //sql.addSQLClause("AND", "TI_ENTRATA_SPESA", sql.EQUALS, String.valueOf(doc.getTi_entrate_spese()));
//        //sql.addSQLClause("AND", "FL_DOC_GENERICO", sql.EQUALS, "Y");
//        //if (doc.getCd_cds_origine().equals(doc.getCds_CNR()))
//        	//sql.addSQLClause("AND", "FL_SOLO_PARTITA_GIRO", sql.NOT_EQUALS, "Y");    
//    //}
//
//    return home.fetchAll(sql);
//
//}
////^^@@
///** 
//  *	Restituisce l'insieme dei tipi di documento per la ricerca guidata
//  *		PreCondition:
//  * 		Riechiesta dei tipi di documento compatibili
//  *   	PostCondition:
//  *  		Restituisce la collezione di tipo documento
// */
////^^@@
//public java.util.Collection findTipi_doc_for_search(UserContext aUC, RichiestaUopBulk doc)
//    throws ComponentException, it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException {
//
//    if (doc == null)
//        return null;
//
//       it.cnr.jada.bulk.BulkHome home= getHome(aUC, Tipo_documento_ammBulk.class);
//    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//
//    if (doc.getTipo_documento() != null && doc.getTipo_documento().getCd_tipo_documento_amm() != null) {
//        sql.addSQLClause("AND", "CD_TIPO_DOCUMENTO_AMM", sql.EQUALS, doc.getTipo_documento().getCd_tipo_documento_amm());
//    } else {
//        sql.addSQLClause("AND", "TI_ENTRATA_SPESA", sql.EQUALS, String.valueOf(doc.getTi_entrate_spese()));
//        sql.addSQLClause("AND", "FL_DOC_GENERICO", sql.EQUALS, "Y");
//        if (doc.getCd_cds_origine().equals(doc.getCds_CNR()))
//        	sql.addSQLClause("AND", "FL_SOLO_PARTITA_GIRO", sql.NOT_EQUALS, "Y");    
//    }
//
//    return home.fetchAll(sql);
//
//}
///**
// * Validazione dell'oggetto in fase di stampa
// *
//*/
//private java.sql.Timestamp getDataOdierna(it.cnr.jada.UserContext userContext) throws ComponentException {
//
//	try{
//		Documento_genericoHome home = (Documento_genericoHome)getHome(userContext, RichiestaUopBulk.class);
//		return home.getServerDate();
//	}catch(it.cnr.jada.persistency.PersistencyException ex){
//		throw handleException(ex);
//	}
//}
//private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliNonTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {
//
//	it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliNonTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//	if (scadenze != null)
//		while (scadenze.hasMoreElements()) {
//			IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
//			if (!scadenza.getFather().isTemporaneo()) {
//				if (!documentiContabiliNonTemporanei.containsKey(scadenza.getFather())) {
//					Vector allInstances = new java.util.Vector();
//					allInstances.addElement(scadenza.getFather());
//					documentiContabiliNonTemporanei.put(scadenza.getFather(), allInstances);
//				} else {
//					((Vector)documentiContabiliNonTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
//				}
//			}
//		}
//	return documentiContabiliNonTemporanei;
//}
//private it.cnr.jada.bulk.PrimaryKeyHashtable getDocumentiContabiliTemporanei(UserContext userContext, java.util.Enumeration scadenze) throws ComponentException {
//
//	it.cnr.jada.bulk.PrimaryKeyHashtable documentiContabiliTemporanei = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//	if (scadenze != null)
//		while (scadenze.hasMoreElements()) {
//			IScadenzaDocumentoContabileBulk scadenza = (IScadenzaDocumentoContabileBulk)scadenze.nextElement();
//			if (scadenza.getFather().isTemporaneo()) {
//				if (!documentiContabiliTemporanei.containsKey(scadenza.getFather())) {
//					Vector allInstances = new java.util.Vector();
//					allInstances.addElement(scadenza.getFather());
//					documentiContabiliTemporanei.put(scadenza.getFather(), allInstances);
//				} else {
//					((Vector)documentiContabiliTemporanei.get(scadenza.getFather())).add(scadenza.getFather());
//				}
//			}
//		}
//	return documentiContabiliTemporanei;
//}
///**
// * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
// */
//private it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk getEuro(UserContext userContext) throws ComponentException {
//
//	String cd_euro = null;
//	try {
//		cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
//	} catch (javax.ejb.EJBException e) {
//		handleException(e);
//	} catch (java.rmi.RemoteException e) {
//		handleException(e);
//	}
//
//	it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk valuta = null;
//	
//	try {
//		if (cd_euro != null)
//			valuta = (it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk)getHome(userContext, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk.class).find(new it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk(cd_euro)).get(0);
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		handleException(e);
//	}
//	return valuta;
//}
//private java.sql.Timestamp getFirstDayOfYear(int year){
//
//	java.util.Calendar calendar = java.util.GregorianCalendar.getInstance();
//	calendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
//	calendar.set(java.util.Calendar.MONTH, 0);
//	calendar.set(java.util.Calendar.YEAR, year);
//	calendar.set(java.util.Calendar.HOUR, 0);
//	calendar.set(java.util.Calendar.MINUTE, 0);
//	calendar.set(java.util.Calendar.SECOND, 0);
//	calendar.set(java.util.Calendar.MILLISECOND, 0);
//	calendar.set(java.util.Calendar.AM_PM, java.util.Calendar.AM);
//	return new java.sql.Timestamp(calendar.getTime().getTime());
//}
//private RiportoDocAmmComponentSession getRiportoComponentSession(
//	UserContext context)
//	throws ComponentException {
//
//	try {
//		return (RiportoDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//				"CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
//				RiportoDocAmmComponentSession.class);
//	} catch (Throwable t) {
//		throw handleException(t);
//	}
//}
//private String getStatoRiportoInScrivania(
//	UserContext context,
//	RichiestaUopBulk documento)
//	throws ComponentException {
//
//	try {
//		RiportoDocAmmComponentSession session = 
//			(RiportoDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//				"CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
//				RiportoDocAmmComponentSession.class);
//		return session.getStatoRiportoInScrivania(context, documento);
//	} catch (Throwable t) {
//		throw handleException(documento, t);
//	}
//}
//private String getStatoRiporto(
//	UserContext context,
//	RichiestaUopBulk documento)
//	throws ComponentException {
//
//	try {
//		RiportoDocAmmComponentSession session = 
//			(RiportoDocAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//				"CNRDOCAMM00_EJB_RiportoDocAmmComponentSession",
//				RiportoDocAmmComponentSession.class);
//		return session.getStatoRiporto(context, documento);
//	} catch (Throwable t) {
//		throw handleException(documento, t);
//	}
//}
/////^^@@
///** 
//  *	Restituisce il terzo per la gestione della spesa
//  *		PreCondition:
//  * 		Richiesta del terzo per l'utilizzo dei documenti per la spesa
//  *   	PostCondition:
//  *  		Restituisce il terzo di default dalla configurazione CNR
// */
////^^@@
//public TerzoBulk getTerzoDefault(UserContext userContext) throws ComponentException {
//
//    java.math.BigDecimal cd_terzo_default= null;
//    try {
//        cd_terzo_default=
//            (
//                (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//                    "CNRCONFIG00_EJB_Configurazione_cnrComponentSession",
//                    it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getIm01(
//                userContext,
//                new Integer(0),
//                "*",
//                "TERZO_SPECIALE",
//                "CODICE_DIVERSI_DEFAULT");
//    } catch (javax.ejb.EJBException e) {
//        handleException(e);
//    } catch (java.rmi.RemoteException e) {
//        handleException(e);
//    }
//
//    TerzoBulk terzo= null;
//
//    try {
//        if (cd_terzo_default != null) {
//            it.cnr.jada.bulk.BulkHome home= getHome(userContext, TerzoBulk.class);
//            it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//
//            sql.addClause("AND", "cd_terzo", sql.EQUALS, new Long(cd_terzo_default.longValue()).toString());
//
//            it.cnr.jada.persistency.Broker broker= home.createBroker(sql);
//            if (!broker.next())
//                return null;
//
//            terzo= (TerzoBulk) broker.fetch(TerzoBulk.class);
//            broker.close();
//
//        }
//
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        handleException(e);
//    }
//    return terzo;
//}
////^^@@
///** 
//  * Vengono richiesti i dati relativi all'ente
//  *    PreCondition:
//  *      vengono richiesti i dati relativi all'ente
//  *    PostCondition:
//  *		 vengono impostati i dati relativi alla UO e CDS dell'ente
//*/
////^^@@
//private Unita_organizzativa_enteBulk getUOEnte(
//    UserContext aUC)
//    throws ComponentException {
//
//	try {
//        Unita_organizzativa_enteHome home = (Unita_organizzativa_enteHome)getHome(aUC, Unita_organizzativa_enteBulk.class);
//        return (Unita_organizzativa_enteBulk)home.fetchAll(home.createSQLBuilder()).get(0);
//
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(e);
//	} 
//}
//private void impostaDatiEnteNelDocumento(
//    UserContext aUC,
//    RichiestaUopBulk documento, 
//    Unita_organizzativa_enteBulk uo_ente)
//    throws ComponentException {
//
//    if (documento != null) {
//        //imposto CDS e UO per l'ENTE
//        if (documento.getUo_CNR() == null || documento.getCds_CNR() == null) {
//	        if (uo_ente == null)
//	        	uo_ente = getUOEnte(aUC);
//		 	documento.setUo_CNR(uo_ente.getCd_unita_organizzativa());
//			documento.setCds_CNR(uo_ente.getCd_unita_padre());
//        }
//
//		if (!documento.isGenericoAttivo()){
//			if (documento.getCd_uo_origine().equals(documento.getUo_CNR())){
//				documento.setFlagEnte(true);
//			}
//			documento.setPassivo_ente(true);
//			documento.setStato_pagamento_fondo_eco(documento.NO_FONDO_ECO);
//		}
//    }
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di creazione.
//  *    PostCondition:
//  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
// */
////^^@@
//
//public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
//
//    RichiestaUopBulk documento= (RichiestaUopBulk) bulk;
//    try {
//        Documento_genericoHome dHome= (Documento_genericoHome) getHome(userContext, documento);
//        if (!verificaStatoEsercizio(userContext, new EsercizioBulk( documento.getCd_cds(), documento.getEsercizio())))
//            throw new it.cnr.jada.comp.ApplicationException("Impossibile inserire un documento generico per un esercizio non aperto!");
//		java.sql.Timestamp date = EJBCommonServices.getServerDate();
//		int annoSolare = documento.getDateCalendar(date).get(java.util.Calendar.YEAR);
//		int esercizioInScrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue();
//		if (annoSolare != esercizioInScrivania)
//			date = new java.sql.Timestamp(new java.text.SimpleDateFormat("dd/MM/yyyy").parse("31/12/" + esercizioInScrivania).getTime());
//		documento.setData_registrazione(date);
//		 try{ 
//			 documento.setDataInizioObbligoRegistroUnico(Utility.createConfigurazioneCnrComponentSession().
//					getDt01(userContext, new Integer(0), null,"REGISTRO_UNICO_FATPAS", "DATA_INIZIO"));
//		    	
//		    } catch ( Exception e )	{
//				throw handleException(documento, e);
//			}
//		setDt_termine_creazione_docamm(userContext, documento);
//    } catch (java.text.ParseException e) {
//        throw handleException(bulk, e);
//    }
//    
//	documento.setTi_istituz_commerc(documento.ISTITUZIONALE);
//	
//    documento.setIm_totale(new BigDecimal(0));
//    if (documento.isGenericoAttivo()) {
//        documento.setFlagEnte(true);        
//	    documento= setEnte(userContext, documento);
//    } 
//
//    try {
//        try {
//            //if (documento.getTi_entrate_spese()==documento.ENTRATE)
//            documento.setTerzo_uo_cds(findTerzoUO(userContext, documento));
//            //documento.setModalita_uo(findModalita(userContext,documento));
//        } catch (it.cnr.jada.persistency.PersistencyException e) {
//            throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
//        }
//    } catch (it.cnr.jada.persistency.IntrospectionException e) {
//        throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
//    }
//
//    documento.setStato_pagamento_fondo_eco(documento.NO_FONDO_ECO);
//
//    if (documento.isGenericoAttivo())
//        documento.setPassivo_ente(true);
//    else
//    	documento.setPassivo_ente(false);
//    
//    if (!documento.isGenericoAttivo())
//	    documento.setCd_unita_organizzativa(documento.getCd_uo_origine());
//	//else
//		//documento.setCd_unita_organizzativa(documento.getCd_uo_origine());
//    try {
//        if (!documento.isGenericoAttivo()) {
//            it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome uoHome=
//                (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome) getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//            if (documento
//                .getCd_uo_origine()
//                .equals(
//                    ((it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk) (getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).fetchAll(uoHome.createSQLBuilderEsteso())).get(0))
//                        .getCd_unita_organizzativa()))
//                setEnte(userContext,documento);
//        }
//
//    
//    documento= setChangeDataToEur(userContext, documento);
//
//    documento= (RichiestaUopBulk) super.inizializzaBulkPerInserimento(userContext, documento);
//
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(documento, e);
//    }
//
//    documento.setDt_da_competenza_coge(documento.getData_registrazione());
//	documento.setDt_a_competenza_coge(documento.getData_registrazione());
//
//	/**
//	  * Gennaro Borriello - (08/11/2004 13.35.27)
//	  *	Aggiunta proprietà <code>esercizioInScrivania</code>, che verrà utilizzata
//	  *	per la gestione di isRiportataInScrivania(), in alcuni casi.
//	 */
//	 documento.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
//
//
//	
//	return documento;
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di modifica.
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di modifica.
// */
////^^@@
//
//public OggettoBulk inizializzaBulkPerModifica(UserContext aUC, OggettoBulk bulk) throws ComponentException {
//
//    if (bulk == null)
//        throw new ComponentException("Attenzione: non esiste alcun documento corrispondente ai criteri di ricerca!");
//
//    RichiestaUopBulk generico = (RichiestaUopBulk) bulk;
//	if (generico.getEsercizio() == null)
//		throw new it.cnr.jada.comp.ApplicationException("L'esercizio del documento non è valorizzato! Impossibile proseguire.");
//			
//	if (generico.getEsercizio().intValue() >
//		it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue())
//		throw new it.cnr.jada.comp.ApplicationException("Il documento deve appartenere o all'esercizio di scrivania o ad esercizi precedenti per essere aperto in modifica!");
//	
//	generico = (RichiestaUopBulk)super.inizializzaBulkPerModifica(aUC, generico);
//
//    try {
//        lockBulk(aUC, generico);
//		//setDt_termine_creazione_docamm(aUC, generico);
//    } catch (Throwable t) {
//        throw handleException(t);
//    }
//
//    try {
//        // imposta il tipo di documento
//        if (generico.getCd_tipo_documento_amm()==null && generico.getTipo_documento().getCd_tipo_documento_amm() != null)
//	        generico.setCd_tipo_documento_amm(generico.getTipo_documento().getCd_tipo_documento_amm());
//            //generico.setCd_tipo_documento_amm(generico.getTipo_documento().getCd_tipo_documento_amm());
//            //it.cnr.jada.bulk.BulkHome homeTD= getHome(aUC, Tipo_documento_ammBulk.class);
//            //it.cnr.jada.persistency.sql.SQLBuilder sqlTD= homeTD.createSQLBuilder();
//            //sqlTD.addClause("AND", "cd_tipo_documento_amm", sqlTD.EQUALS, generico.getCd_tipo_documento_amm());
//            //it.cnr.jada.persistency.Broker brokerTD= homeTD.createBroker(sqlTD);
//
//            //generico.setTipo_documento((Tipo_documento_ammBulk) brokerTD.fetch(Tipo_documento_ammBulk.class));
//
//            //brokerTD.close();
//        //}
//        //generico =(RichiestaUopBulk) super.inizializzaBulkPerModifica(aUC, generico);
//        it.cnr.jada.bulk.BulkHome home= getHome(aUC, Documento_generico_rigaBulk.class);
//        it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
//        sql.addClause("AND", "pg_documento_generico", sql.EQUALS, generico.getPg_documento_generico());
//        sql.addClause("AND", "cd_cds", sql.EQUALS, generico.getCd_cds());
//        sql.addClause("AND", "esercizio", sql.EQUALS, generico.getEsercizio());
//        sql.addClause("AND", "cd_unita_organizzativa", sql.EQUALS, generico.getCd_unita_organizzativa());
//        sql.addClause("AND", "cd_tipo_documento_amm", sql.EQUALS, generico.getCd_tipo_documento_amm());
//
//        getHomeCache(aUC).fetchAll(aUC);
//
//        generico.setDocumento_generico_dettColl(new it.cnr.jada.bulk.BulkList(home.fetchAll(sql)));
//
//        //to be chkd
//        generico.setTi_entrate_spese(generico.getTipo_documento().getTi_entrata_spesa().charAt(0));
//
//        // 03/03/04 - BORRIELLO
//        //	Corretto Errore interno: questa operazione deve essere fatta DOPO aver impostato il
//        //	<code>Ti_entrate_spese</code>, altrimenti la dt_termine_creazione_docamm NON viene impostata e,
//        //	in fase di salvataggio, genera un errore di NullPointerException.
//        setDt_termine_creazione_docamm(aUC, generico);
//        try{
//   		 generico.setDataInizioObbligoRegistroUnico(Utility.createConfigurazioneCnrComponentSession().
//   				getDt01(aUC, new Integer(0), null,"REGISTRO_UNICO_FATPAS", "DATA_INIZIO"));
//   	    	
//   	    } catch ( Exception e )	{
//   			throw handleException(generico, e);
//   		}
//		if (!generico.getCd_uo_origine().equals(generico.getCd_unita_organizzativa()))
//            generico.setFlagEnte(true);
//        
//        if (!generico.isGenericoAttivo())
//        	generico.setPassivo_ente(generico.isFlagEnte());
//        else
//        	generico.setPassivo_ente(true);
//        //try {
//            //if (!generico.isGenericoAttivo()) {
//                //it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome uoHome= (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome) getHome(aUC, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//                //if (generico
//                    //.getCd_unita_organizzativa()
//                    //.equals(
//                        //((it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk) (getHome(aUC, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class).fetchAll(uoHome.createSQLBuilderEsteso())).get(0))
//                            //.getCd_unita_organizzativa()))
//                    //generico.setPassivo_ente(true);
//            //}
//
//        //} catch (it.cnr.jada.persistency.PersistencyException e) {
//            //throw handleException(generico, e);
//        //}
//
//       
//        if (!generico.getCd_uo_origine().equals(generico.getCd_unita_organizzativa()))
//	        impostaDatiEnteNelDocumento(aUC,generico, null);
//		else {
//			Unita_organizzativa_enteBulk uoEnte = getUOEnte(aUC);
//			if (generico.getCd_unita_organizzativa().equalsIgnoreCase(uoEnte.getCd_unita_organizzativa()))
//				impostaDatiEnteNelDocumento(aUC, generico, uoEnte);
//		}
//		
//        if (generico.getValuta().getCd_divisa().equals(getEuro(aUC).getCd_divisa()))
//		generico.setDefaultValuta(true);
//
//        if (!generico.isGenericoAttivo())
//            rebuildObbligazioni(aUC, generico);
//        if (generico.isGenericoAttivo()) {
//            rebuildAccertamenti(aUC, generico);
//            generico.setTerzo_uo_cds(((Documento_generico_rigaBulk) generico.getDocumento_generico_dettColl().get(0)).getTerzo_uo_cds());
//        }
//        for (java.util.Iterator i= generico.getDocumento_generico_dettColl().iterator(); i.hasNext();)
//            initializeKeysAndOptionsInto(aUC, (OggettoBulk) i.next());
//		
//        int dettagliRiportati = 0;
//        generico.setHa_beniColl(ha_beniColl(aUC,generico));
//        
//        for (java.util.Iterator i= generico.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
//
//            OggettoBulk rigabulk= (OggettoBulk) i.next();
//            Documento_generico_rigaBulk riga= (Documento_generico_rigaBulk) rigabulk;
//            
//            if (generico.getTi_entrate_spese() == generico.SPESE && riga.getObbligazione_scadenziario() !=null && riga.getObbligazione_scadenziario().getObbligazione()!= null && 
//					riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null &&
//					riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getFl_inv_beni_patr().booleanValue()){
//            	riga.setInventariato(true); 
//			 	if (ha_beniColl(aUC,riga).booleanValue())			   
//					riga.setInventariato(true);
//				 else
//					riga.setInventariato(false);
//			}else{
//					if (riga.getAccertamento_scadenziario() !=null && riga.getAccertamento_scadenziario().getAccertamento()!= null &&
//						riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce()!=null){ 
//						
//						Elemento_voceHome Home = (Elemento_voceHome)getHome(aUC, Elemento_voceBulk.class);
//						Elemento_voceBulk elem_voce = (Elemento_voceBulk)Home.findByPrimaryKey(new Elemento_voceBulk(riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce(),
//								riga.getAccertamento_scadenziario().getAccertamento().getEsercizio(),
//								riga.getAccertamento_scadenziario().getAccertamento().getTi_appartenenza(),
//								riga.getAccertamento_scadenziario().getAccertamento().getTi_gestione()));
//						if (elem_voce.getFl_inv_beni_patr().booleanValue())
//							riga.setInventariato(true); 
//					 	if (ha_beniColl(aUC,riga).booleanValue())			   
//							riga.setInventariato(true);
//						 else
//							riga.setInventariato(false);
//			    }
//			}
//            
//            if (generico.isPassivo_ente())
//                riga.setIm_riga_iniziale(riga.getIm_imponibile());
//            if (generico.getTi_entrate_spese() == generico.ENTRATE) {
//                riga.setModalita_pagamento_uo_cds(findModalitaSelezionate(aUC, riga));
//                riga.setTermini_pagamento_uo_cds(findTerminiSelezionati(aUC, riga));
//                riga.setModalita_uo_cds(findModalita(aUC, riga));
//                riga.setBanca_uo_cds(findBancaSelezionata(aUC, riga));
//                initializeKeysAndOptionsInto(aUC, (OggettoBulk) riga);
//            } else {
//                riga.setModalita_pagamento(findModalitaSelezionate(aUC, riga));
//                riga.setTermini_pagamento(findTerminiSelezionati(aUC, riga));
//                riga.setModalita(findModalita(aUC, riga));
//                riga.setBanca(findBancaSelezionata(aUC, riga));
//                initializeKeysAndOptionsInto(aUC, (OggettoBulk) riga);
//            }
//			if (riga.checkIfRiportata()) {
//				riga.setRiportata(riga.RIPORTATO);
//				generico.setRiportata(generico.PARZIALMENTE_RIPORTATO);
//				dettagliRiportati++;
//			}
//        }
//		generico.setRiportata(getStatoRiporto(aUC, generico));
//
//		/**
//		  * Gennaro Borriello - (02/11/2004 15.04.39)
//		  *	Aggiunta gestione dell Stato Riportato all'esercizio di scrivania.
//		 */
//		generico.setRiportataInScrivania(getStatoRiportoInScrivania(aUC, generico));
//
//		/**
//		  * Gennaro Borriello - (08/11/2004 13.35.27)
//		  *	Aggiunta proprietà <code>esercizioInScrivania</code>, che verrà utilizzata
//		  *	per la gestione di isRiportataInScrivania(), in alcuni casi.
//		 */
//		 generico.setEsercizioInScrivania(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
//
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(generico, e);
//    } catch (it.cnr.jada.persistency.IntrospectionException e) {
//        throw handleException(generico, e);
//    }
//    return generico;
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa Documenti Generici
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di stampa.
// */
////^^@@
//private void inizializzaBulkPerStampa(UserContext userContext, Stampa_docamm_per_voce_del_pianoVBulk stampa) throws it.cnr.jada.comp.ComponentException {
//
//
//	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
//	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
//
//	try{
//	
//		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
//		Unita_organizzativaHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)uoHome.findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_uo_scrivania));
//		
//		if (!uo.isUoCds()){
//			stampa.setUoForPrint(uo);
//			stampa.setUOForPrintEnabled(false);
//		} else {
//			stampa.setUoForPrint(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
//			stampa.setUOForPrintEnabled(true);
//		}
//			
//	} catch (it.cnr.jada.persistency.PersistencyException pe){
//		throw new ComponentException(pe);
//	}
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa di Fatture Passive/Attive
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di stampa.
// */
////^^@@
//private void inizializzaBulkPerStampa(UserContext userContext, Stampa_elenco_fattureVBulk stampa) throws it.cnr.jada.comp.ComponentException {
//
//	try{
//	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
//	BulkHome home = getHome(userContext, EnteBulk.class);
//	SQLBuilder sql = home.createSQLBuilder();
//	EnteBulk ente =(EnteBulk) home.fetchAll(sql).get(0);
//	 
//	if(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext).compareTo(ente.getCd_unita_organizzativa())!=0){
//		stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
//	}
//	stampa.setTerzo(new TerzoBulk());
//	
//		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
//		Unita_organizzativaHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)uoHome.findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_uo_scrivania));
//		
//		if (!uo.isUoCds()){
//			stampa.setUoForPrint(uo);
//			stampa.setUoForPrintEnabled(false);
//		} else {
//			stampa.setUoForPrint(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
//			stampa.setUoForPrintEnabled(true);
//		}
//			
//	} catch (it.cnr.jada.persistency.PersistencyException pe){
//		throw new ComponentException(pe);
//	}
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa Situzione Pagamenti Estero.
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di stampa.
// */
////^^@@
//private void inizializzaBulkPerStampa(UserContext userContext, Stampa_situazioni_pag_esteroVBulk stampa) throws it.cnr.jada.comp.ComponentException {
//
//
//	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
//	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
//
//	try{
//	
//		String cd_uo_scrivania = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
//		Unita_organizzativaHome uoHome = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//		it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo = (it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk)uoHome.findByPrimaryKey(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk(cd_uo_scrivania));
//		
//		if (!uo.isUoCds()){
//			stampa.setUoForPrint(uo);
//			stampa.setIsUOForPrintEnabled(false);
//		} else {
//			stampa.setUoForPrint(new it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk());
//			stampa.setIsUOForPrintEnabled(true);
//		}
//			
//	} catch (it.cnr.jada.persistency.PersistencyException pe){
//		throw new ComponentException(pe);
//	}
//}
////^^@@
///** 
//  *  normale
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa Documenti Generici
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una operazione di stampa.
// */
////^^@@
//private void inizializzaBulkPerStampa(UserContext userContext, Stampa_vpg_doc_genericoBulk stampa) throws it.cnr.jada.comp.ComponentException {
//
//
//	stampa.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
//	stampa.setCd_cds(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
//	stampa.setCd_unita_organizzativa(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
//
//	stampa.setCd_cds_origine(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cds(userContext));
//	stampa.setCd_uo_origine(it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext));
//	
//	stampa.setDataInizio(getFirstDayOfYear(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext).intValue()));
//	stampa.setDataFine(getDataOdierna(userContext));
//	stampa.setPgInizio(new Integer(1));
//	stampa.setPgFine(new Integer(999999999));
//
//	stampa.setTerzoForPrint(new TerzoBulk());
//
//	try{
//
//		stampa.setTipi_doc_for_search(findTipi_doc_for_print(userContext, stampa));
//	} catch (it.cnr.jada.persistency.PersistencyException e){
//		throw handleException(e);
//	} catch (it.cnr.jada.persistency.IntrospectionException ie){
//		throw handleException(ie);
//	}
//}
////^^@@
///** 
//  *  Inizializzazione per stampa Documenti Generici
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa di Documenti Generici
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una 
//  *		operazione di stampa, ( inizializzaBulkPerStampa(UserContext, (Stampa_vpg_doc_genericoBulk)) )
//  *
//  *  Inizializzazione per stampa Situazione Pagamenti Estero
//  *    PreCondition:
//  *      Viene richiesta una possibile operazione di stampa di Situazione Pagamenti Estero
//  *    PostCondition:
//  *      L'OggettoBulk viene aggiornato con tutti gli oggetti collegati e preparato per una 
//  *		operazione di stampa, ( inizializzaBulkPerStampa(UserContext, (Stampa_situazioni_pag_esteroVBulk)) )
// */
////^^@@
public OggettoBulk inizializzaBulkPerStampa(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {


//	if (bulk instanceof Stampa_vpg_doc_genericoBulk)
//		inizializzaBulkPerStampa(userContext, (Stampa_vpg_doc_genericoBulk)bulk);
//	else if (bulk instanceof Stampa_situazioni_pag_esteroVBulk)
//		inizializzaBulkPerStampa(userContext, (Stampa_situazioni_pag_esteroVBulk)bulk);
//	else if (bulk instanceof Stampa_docamm_per_voce_del_pianoVBulk)
//		inizializzaBulkPerStampa(userContext, (Stampa_docamm_per_voce_del_pianoVBulk)bulk);
//	else if (bulk instanceof Stampa_elenco_fattureVBulk)
//		inizializzaBulkPerStampa(userContext, (Stampa_elenco_fattureVBulk)bulk);
//	

		
	return bulk;	
}
////^^@@
///** 
//  *  non utilizzato
// */
////^^@@
//
//public void inserisciRiga (UserContext aUC,Documento_generico_rigaBulk documentoGenericoRiga) throws ComponentException
//        {
//            return ;
//        }
//		/**
//		  *	Controllo l'esercizio coep
//		  *
//		  * Nome: Controllo chiusura esercizio
//		  * Pre:  E' stata richiesta la creazione o modifica di una scrittura
//		  * Post: Viene chiamata una stored procedure che restituisce 
//		  *		  -		'Y' se il campo stato della tabella CHIUSURA_COEP vale C
//		  *		  -		'N' altrimenti
//		  *		  Se l'esercizio e' chiuso e' impossibile proseguire
//		  *
//		  * @param  userContext <code>UserContext</code>
//  
//		  * @return boolean : TRUE se stato = C
//		  *					  FALSE altrimenti
//		  */
//		private boolean isEsercizioCoepChiusoFor(UserContext userContext, RichiestaUopBulk documento, Integer esercizio) throws ComponentException
//		{
//			LoggableStatement cs = null;	
//			String status = null;
//
//			try
//			{
//				cs = new LoggableStatement(getConnection(userContext), 
//						"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() 
//						+	"CNRCTB200.isChiusuraCoepDef(?,?)}",false,this.getClass());	
//
//				cs.registerOutParameter( 1, java.sql.Types.VARCHAR);
//				cs.setObject( 2, esercizio);		
//				cs.setObject( 3, documento.getCd_cds());		
//		
//				cs.executeQuery();
//
//				status = new String(cs.getString(1));
//
//				if(status.compareTo("Y")==0)
//					return true;
//	    	
//			} catch (java.sql.SQLException ex) {
//				throw handleException(ex);
//			}
//	
//			return false;		    	
//		}
//private void liberaSospeso(UserContext userContext,SospesoBulk sospeso) throws ComponentException {
//
//	try {
//		if (sospeso != null) {
//			SospesoHome sospesoHome = (SospesoHome)getHome(userContext, SospesoBulk.class);
//			sospeso.setIm_ass_mod_1210(new java.math.BigDecimal(0).setScale(2, java.math.BigDecimal.ROUND_HALF_UP));
//			sospeso.setToBeUpdated();
//			sospesoHome.aggiornaImportoAssociatoMod1210(sospeso);
//		}
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(sospeso,e);
//	} catch (it.cnr.jada.bulk.OutdatedResourceException e) {
//		throw handleException(sospeso,e);
//	} catch (it.cnr.jada.bulk.BusyResourceException e) {
//		throw handleException(sospeso,e);
//	}
//
//}
//private RichiestaUopBulk manageDeletedElements(
//	UserContext userContext, 
//	RichiestaUopBulk documento,
//	OptionRequestParameter status)
//	throws ComponentException {
//
//	if (documento.isGenericoAttivo())
//		manageDocumentiContabiliCancellatiPerGenericoAttivo(userContext, documento,status);
//	else if (!documento.isGenericoAttivo())
//		manageDocumentiContabiliCancellatiPerGenericoPassivo(userContext, documento,status);
//	if (documento.getDettagliCancellati() != null && !documento.getDettagliCancellati().isEmpty())
//		for (Iterator i = documento.getDettagliCancellati().iterator(); i.hasNext();) {
//			Documento_generico_rigaBulk dettaglio = (Documento_generico_rigaBulk)i.next();
//			if (dettaglio.isInventariato())
//					deleteAssociazioniInventarioWith(userContext, dettaglio);
//		}
//	return documento;
//}
//private void manageDocumentiContabiliCancellatiPerGenericoAttivo(
//	UserContext userContext, 
//	RichiestaUopBulk documentoAttivo,
//	OptionRequestParameter status)
//	throws ComponentException {
//
//	if (documentoAttivo != null) {
//		if (documentoAttivo.getDocumentiContabiliCancellati() != null &&
//			!documentoAttivo.getDocumentiContabiliCancellati().isEmpty()) {
//
//			PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
//																	userContext,
//																	documentoAttivo.getDocumento_generico_accertamentiHash().keys());
//			Vector scadenzeConfermate = new Vector();
//			java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
//			while (e.hasMoreElements()) {
//				OggettoBulk obj = (OggettoBulk)e.nextElement();
//				if (obj instanceof AccertamentoBulk)
//					scadenzeConfermate.add(obj);
//			}
//			aggiornaAccertamentiSuCancellazione(
//				userContext,
//				documentoAttivo,
//				documentoAttivo.getDocumentiContabiliCancellati().elements(),
//				scadenzeConfermate,status);
//		}
//	}
//}
//private void manageDocumentiContabiliCancellatiPerGenericoPassivo(
//	UserContext userContext, 
//	RichiestaUopBulk documentoPassivo,
//	OptionRequestParameter status)
//	throws ComponentException {
//
//	if (documentoPassivo != null) {
//		if (documentoPassivo.getDocumentiContabiliCancellati() != null &&
//			!documentoPassivo.getDocumentiContabiliCancellati().isEmpty()) {
//
//			PrimaryKeyHashtable scadenzeConfermateTemporanee = getDocumentiContabiliTemporanei(
//																	userContext,
//																	documentoPassivo.getDocumento_generico_obbligazioniHash().keys());
//			Vector scadenzeConfermate = new Vector();
//			java.util.Enumeration e = scadenzeConfermateTemporanee.keys();
//			while (e.hasMoreElements()) {
//				OggettoBulk obj = (OggettoBulk)e.nextElement();
//				if (obj instanceof ObbligazioneBulk)
//					scadenzeConfermate.add(obj);
//			}
//			aggiornaObbligazioniSuCancellazione(
//				userContext,
//				documentoPassivo,
//				documentoPassivo.getDocumentiContabiliCancellati().elements(),
//				scadenzeConfermate,status);
//		}
//	}
//}
////^^@@
///** 
//  *  Richiesta di salvare le midifiche apportate sul documento
//  *    PreCondition:
//  *      Richiesta di salvare le midifiche apportate sul documento
//  *    PostCondition:
//  *      Viene richiamato il metodo modificaConBulk(aUC, bulk, null)
// */
////^^@@
//public OggettoBulk modificaConBulk(UserContext aUC, OggettoBulk bulk) throws ComponentException {
//
//    return modificaConBulk(aUC, bulk, null);
//}
////^^@@
///** 
//  *  Richiesta di salvare le modifiche sul documento generico
//  *	 validazione superata
//  *    PreCondition:
//  *      effettua il controllo di validazione del metodo validaDocumento
//  *    PostCondition:
//  *		 Vengono aggiornate le obbligazioni/accertamenti, controllati gli elementi disassociati,
//  *      la lettera di pagamento e infine salvato il documento
//  *	 validazione non superata
//  *    PreCondition:
//  *      effettua il controllo di validazione del metodo validaDocumento
//  *    PostCondition:
//  *      Viene restituito un messaggio di errore
//  *  Documento contabilizzato in COAN
//  *    PreCondition:
//  *      validazione superata
//  *    PostCondition:
//  *      documento viene impostato come da ricontabilizzare
//  *  Documento contabilizzato in COGE
//  *    PreCondition:
//  *      validazione superata
//  *    PostCondition:
//  *      documento viene impostato come da ricontabilizzare  
// */
////^^@@
//public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
//    RichiestaUopBulk documento= (RichiestaUopBulk) bulk;
//    documento.setAndVerifyStatus();
//    //  effettua il controllo di validazione    
//    try {		
//			if (existARowToBeInventoried(aUC,documento)) {
//				verificaEsistenzaEdAperturaInventario(aUC, documento);
//				if (documento.hasCompetenzaCOGEInAnnoPrecedente())
//					  throw new it.cnr.jada.comp.ApplicationException("Attenzione: per le date competenza indicate non è possibile inventariare i beni.");		
//					
//				if (hasDocumentoPassivoARowNotInventoried(aUC, documento))
//					throw new it.cnr.jada.comp.ApplicationException("Attenzione: è necessario inventariare tutti i dettagli.");
//			}
//			validaDocumento(aUC, documento);
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
//	}
//
//    // Salvo temporaneamente l'hash map dei saldi
//    PrimaryKeyHashMap aTempDiffSaldi=new PrimaryKeyHashMap();
//    if (documento.getDefferredSaldi() != null)
//	    aTempDiffSaldi=(PrimaryKeyHashMap)documento.getDefferredSaldi().clone();    
//    
//    manageDeletedElements(aUC, documento,status);
//
//    if (!documento.isGenericoAttivo()) {
//        aggiornaObbligazioni(aUC, documento,status);
//    }
//    if (documento.isGenericoAttivo()) {
//        aggiornaAccertamenti(aUC, documento,status);
//    }
//
//    Lettera_pagam_esteroBulk lettera = documento.getLettera_pagamento_estero();
//	if (lettera != null) {
//		try {
//			Lettera_pagam_esteroBulk original1210 = (Lettera_pagam_esteroBulk)getHome(aUC, lettera).findByPrimaryKey(lettera);
//			aggiornaLetteraPagamentoEstero(aUC, lettera);
//			if (!documento.isFlagEnte() &&
//				(original1210 == null ||
//				lettera.getIm_pagamento().compareTo(original1210.getIm_pagamento()) != 0) &&
//				lettera.getCd_sospeso() != null)
//				validaDisponibilitaDiCassaCDS(aUC, documento);
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(lettera,e);
//		}
//	}
//
//	//Nel caso in cui TUTTI i dettagli sono stati pagati imposto lo stato pagato
//	//in testata fattura. (Caso in cui era parzialmente pagata è ho eliminato tutti
//	//i dettagli non pagati)
//	if (documento.isPagataParzialmente() &&
//		documento.getDettagliPagati().size() == documento.getDocumento_generico_dettColl().size()) {
//		
//		documento.setStato_cofi(documento.STATO_PAGATO);
//		documento.setTi_associato_manrev(documento.ASSOCIATO_A_MANDATO);
//	}
//		
//	/*
//	 * Se il documento non era modificabile nei suoi elementi principali, ma si è solo proceduto a 
//	 * sdoppiare una riga di dettaglio allora la ricontabilizzazione in COAN e COGE non è necessaria
//	 */
//	boolean aggiornaStatoCoge = false;
//	if (documento.isGenericoAttivo()) {
//		try {
//			RichiestaUopBulk documentoDB = (RichiestaUopBulk)getTempHome(aUC, RichiestaUopBulk.class).findByPrimaryKey(documento);
//			if (!Utility.equalsNull(documento.getTi_istituz_commerc(),documentoDB.getTi_istituz_commerc()))
//				aggiornaStatoCoge = true;
//			if (!aggiornaStatoCoge && documento.getDocumento_generico_dettColl().size() > 0){
//				Documento_generico_rigaHome documentoHome =  (Documento_generico_rigaHome)getTempHome(aUC, Documento_generico_rigaBulk.class);
//				for (Iterator iter = documento.getDocumento_generico_dettColl().iterator(); iter.hasNext() && !aggiornaStatoCoge;) {
//					Documento_generico_rigaBulk documento_riga = (Documento_generico_rigaBulk) iter.next();
//					Documento_generico_rigaBulk documentoDB_riga = (Documento_generico_rigaBulk) documentoHome.findByPrimaryKey(documento_riga);
//					if (documentoDB_riga != null){
//						if (!Utility.equalsNull(documento_riga.getDt_da_competenza_coge(),documentoDB_riga.getDt_da_competenza_coge())||
//							!Utility.equalsNull(documento_riga.getDt_a_competenza_coge(),documentoDB_riga.getDt_a_competenza_coge())||
//							!Utility.equalsNull(documento_riga.getIm_riga_divisa(),documentoDB_riga.getIm_riga_divisa())||
//							!Utility.equalsNull(documento_riga.getIm_riga(),documentoDB_riga.getIm_riga())||
//							!Utility.equalsNull(documento_riga.getCd_terzo(),documentoDB_riga.getCd_terzo())||
//							!Utility.equalsBulkNull(documento_riga.getAccertamento_scadenziario(),documentoDB_riga.getAccertamento_scadenziario())
//						)
//							aggiornaStatoCoge = true;
//					}	
//				}
//			}
//		} catch (PersistencyException e) {
//			throw new ComponentException(e);
//		} 
//	}else{
//		try {
//			RichiestaUopBulk documentoDB = (RichiestaUopBulk)getTempHome(aUC, RichiestaUopBulk.class).findByPrimaryKey(documento);
//			if (!Utility.equalsNull(documento.getTi_istituz_commerc(),documentoDB.getTi_istituz_commerc())||
//				!Utility.equalsNull(documento.getStato_pagamento_fondo_eco(),documentoDB.getStato_pagamento_fondo_eco())||
//				!Utility.equalsNull(documento.getEsercizio_lettera(),documentoDB.getEsercizio_lettera())||
//				!Utility.equalsNull(documento.getPg_lettera(),documentoDB.getPg_lettera())
//			)
//				aggiornaStatoCoge = true;
//			if (!aggiornaStatoCoge && documento.getDocumento_generico_dettColl().size() > 0){
//				Documento_generico_rigaHome documentoHome =  (Documento_generico_rigaHome)getTempHome(aUC, Documento_generico_rigaBulk.class);
//				for (Iterator iter = documento.getDocumento_generico_dettColl().iterator(); iter.hasNext() && !aggiornaStatoCoge;) {
//					Documento_generico_rigaBulk documento_riga = (Documento_generico_rigaBulk) iter.next();
//					Documento_generico_rigaBulk documentoDB_riga = (Documento_generico_rigaBulk) documentoHome.findByPrimaryKey(documento_riga);
//					if (documentoDB_riga != null){
//						if (!Utility.equalsNull(documento_riga.getDt_da_competenza_coge(),documentoDB_riga.getDt_da_competenza_coge())||
//							!Utility.equalsNull(documento_riga.getDt_a_competenza_coge(),documentoDB_riga.getDt_a_competenza_coge())||
//							!Utility.equalsNull(documento_riga.getIm_riga_divisa(),documentoDB_riga.getIm_riga_divisa().setScale(2))||
//							!Utility.equalsNull(documento_riga.getIm_riga(),documentoDB_riga.getIm_riga())||
//							!Utility.equalsNull(documento_riga.getCd_terzo(),documentoDB_riga.getCd_terzo())||
//							!Utility.equalsBulkNull(documento_riga.getObbligazione_scadenziario(),documentoDB_riga.getObbligazione_scadenziario())
//						)
//							aggiornaStatoCoge = true;
//					}						
//				}
//			}
//		} catch (PersistencyException e) {
//			throw new ComponentException(e);
//		} 
//	}
//	if (aggiornaStatoCoge){
//		if (!(!documento.isDocumentoModificabile() && documento.isDetailDoubled())) {
//			if (documento.CONTABILIZZATO_IN_COAN.equalsIgnoreCase(documento.getStato_coan())) {
//				documento.setStato_coan(documento.DA_RICONTABILIZZARE_IN_COAN);
//				documento.setToBeUpdated();
//			}
//			if (documento.REGISTRATO_IN_COGE.equalsIgnoreCase(documento.getStato_coge())) {
//				documento.setStato_coge(documento.DA_RIREGISTRARE_IN_COGE);
//				documento.setToBeUpdated();
//			}
//		}
//	}
//	documento = (RichiestaUopBulk)super.modificaConBulk(aUC, documento);
//
//    // Restore dell'hash map dei saldi
//    if (documento.getDefferredSaldi() != null)
//		documento.getDefferredSaldi().putAll(aTempDiffSaldi);
//	
//	/*
//	 * Se il documento non era modificabile nei suoi elementi principali, ma si è solo proceduto a 
//	 * sdoppiare una riga di dettaglio allora il controllo sulla chiusura dell'esercizio del documento
//	 * e sulla ricontabilizzazione in COGE COAN non è necessario
//	 */
//	if (!documento.isDocumentoModificabile() && documento.isDetailDoubled())
//		return documento;
//
//	aggiornaCogeCoanDocAmm(aUC, documento);
//
//	try {
//        if (!verificaStatoEsercizio(
//	        					aUC, 
//	        					new EsercizioBulk( 
//		        							documento.getCd_cds(), 
//		        							((it.cnr.contab.utenze00.bp.CNRUserContext)aUC).getEsercizio())))
//            throw new it.cnr.jada.comp.ApplicationException("Impossibile salvare un documento per un esercizio non aperto!");
//	} catch (it.cnr.jada.comp.ApplicationException e) {
//		throw handleException(bulk, e);
//	}
//	
//	if (documento.getTi_entrate_spese()==RichiestaUopBulk.SPESE){
//		 prepareCarichiInventario(aUC, documento);
// 		 aggiornaCarichiInventario(aUC, documento);
//		// Le operazioni che rendono persistenti le modifiche fatte sull'Inventario,
//		//	potrebbero rimandare un messaggio all'utente.
//		String messaggio = aggiornaAssociazioniInventario(aUC, documento);
//	}
//	else{
//		prepareScarichiInventario(aUC, documento);
//		aggiornaScarichiInventario(aUC, documento);
//		// Le operazioni che rendono persistenti le modifiche fatte sull'Inventario,
//		//	potrebbero rimandare un messaggio all'utente.
//		String messaggio = aggiornaAssociazioniInventario(aUC, documento);
//	}
//	controllaQuadraturaInventario(aUC,documento);
//	
//	return documento;
//}
//
///** 
//  *  Normale.
//  *    PreCondition:
//  *      Viene richiesta la visualizzazione del consuntivo fattura.
//  *    PostCondition:
//  *      Vegono restituiti i dettagli fattura raggruppati per codice IVA.
// */
//public void protocolla(it.cnr.jada.UserContext param0, java.sql.Timestamp param1, java.lang.Long param2) throws it.cnr.jada.comp.ComponentException {}
//private void rebuildAccertamenti(UserContext aUC, RichiestaUopBulk doc)
//    throws ComponentException {
//
//    if (doc == null)
//        return;
//
//    it.cnr.jada.bulk.BulkList righe = doc.getDocumento_generico_dettColl();
//    if (righe != null) {
//
//        java.util.HashMap hashMap = null;
//        for (Iterator i = righe.iterator(); i.hasNext();) {
//            Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk) i.next();
//            Accertamento_scadenzarioBulk scadenza = riga.getAccertamento_scadenziario();
//            if (scadenza != null) {
//                if (doc.getDocumento_generico_accertamentiHash() == null
//                    || doc.getDocumento_generico_accertamentiHash().getKey(scadenza) == null) {
//                    scadenza = caricaAccertamentoPer(aUC, scadenza);
//                }
//                doc.addToDocumento_generico_accertamentiHash(scadenza, riga);
//            }
//        }
//    }
//    try {
//        getHomeCache(aUC).fetchAll(aUC);
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(doc, e);
//    }
//}
//private void rebuildObbligazioni(UserContext aUC, RichiestaUopBulk doc)
//    throws ComponentException {
//
//    if (doc == null)
//        return;
//
//    it.cnr.jada.bulk.BulkList righe = doc.getDocumento_generico_dettColl();
//    if (righe != null) {
//
//        java.util.HashMap hashMap = null;
//        for (Iterator i = righe.iterator(); i.hasNext();) {
//            Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk) i.next();
//            Obbligazione_scadenzarioBulk scadenza = riga.getObbligazione_scadenziario();
//            if (riga.getObbligazione_scadenziario() != null) {
//                if (doc.getDocumento_generico_obbligazioniHash() == null
//                    || doc.getDocumento_generico_obbligazioniHash().getKey(scadenza) == null) {
//                    scadenza = caricaObbligazionePer(aUC, scadenza);
//                }
//                doc.addToDocumento_generico_obbligazioniHash(scadenza, riga);
//            }
//        }
//    }
//    try {
//        getHomeCache(aUC).fetchAll(aUC);
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        throw handleException(doc, e);
//    }
//}
//private RichiestaUopBulk resetChangeData(
//	UserContext userContext, 
//	RichiestaUopBulk documentoBulk)
//	throws ComponentException {
//
//	// Viene chiamato solo quando la valuta del documento è null
//	documentoBulk.setInizio_validita_valuta(null);
//	documentoBulk.setFine_validita_valuta(null);
//	documentoBulk.setCambio(new java.math.BigDecimal(0));
//	
//	
//	return documentoBulk;
//}
////^^@@
///** 
//  *  Normale.
//  *    PreCondition:
//  *      Viene richiesto il riporto del documento amministrativo all'esercizio successivo.
//  *    PostCondition:
//  *      Viene richiamata la procedura DB corretta
// */
////^^@@
//
//public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaAvanti(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm, it.cnr.contab.doccont00.core.bulk.OptionRequestParameter status) throws it.cnr.jada.comp.ComponentException {
//
//	try {
//		return getRiportoComponentSession(userContext).riportaAvanti(userContext, docAmm, status);
//	} catch (java.rmi.RemoteException e) {
//		throw handleException(e);
//	}
//}
////^^@@
///** 
//  *  Normale.
//  *    PreCondition:
//  *      Viene richiesto il riporto del documento amministrativo all'esercizio precedente.
//  *    PostCondition:
//  *      Viene richiamata la procedura DB corretta
// */
////^^@@
//
//public it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk riportaIndietro(it.cnr.jada.UserContext userContext, it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoBulk docAmm) throws it.cnr.jada.comp.ComponentException {
//
//	try {
//		return getRiportoComponentSession(userContext).riportaIndietro(userContext, docAmm);
//	} catch (java.rmi.RemoteException e) {
//		throw handleException(e);
//	}
//}
///**
// * Annulla le modifiche apportate al compenso e ritorna al savepoint impostato in precedenza
// *
// * Pre-post-conditions:
// *
// * Nome: Rollback to savePoint
// * Pre:  Una richiesta di annullare tutte le modifiche apportate e di ritornare al savepoint e' stata generata 
// * Post: Tutte le modifiche effettuate sul compenso vengono annullate, mentre rimangono valide le
// *       modifiche apportate al doc. amministrativo che ha aperto il compenso
// * @param	uc	lo UserContext che ha generato la richiesta
// */	
//public void rollbackToSavePoint(UserContext userContext, String savePointName) throws ComponentException {
//
//	try {
//		rollbackToSavepoint(userContext, savePointName);
//	} catch (java.sql.SQLException e) {
//		if (e.getErrorCode() != 1086)
//			throw handleException(e);
//	}
//}
//private AccertamentoBulk salvaAccertamento(
//	UserContext context,
//	AccertamentoBulk accertamento)
//	throws ComponentException {
//
//	if (accertamento!= null) {
//		try {
//			it.cnr.contab.doccont00.ejb.AccertamentoComponentSession h = (it.cnr.contab.doccont00.ejb.AccertamentoComponentSession)
//																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//																					"CNRDOCCONT00_EJB_AccertamentoComponentSession",
//																					it.cnr.contab.doccont00.ejb.AccertamentoComponentSession.class);
//			try {
//				return (AccertamentoBulk)h.modificaConBulk(context, accertamento);
//			} catch (it.cnr.jada.comp.ApplicationException e) {
//				throw new it.cnr.jada.comp.ApplicationException("Accertamento \"" + accertamento.getDs_accertamento() + "\": " + e.getMessage(),e);
//			}
//		} catch (java.rmi.RemoteException e) {
//			throw handleException(accertamento, e);
//		} catch (javax.ejb.EJBException e) {
//			throw handleException(accertamento, e);
//		}
//	}
//	return null;
//}
//private ObbligazioneBulk salvaObbligazione(
//	UserContext context,
//	ObbligazioneBulk obbligazione)
//	throws ComponentException {
//
//	if (obbligazione != null) {
//		try {
//			it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession h = (it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession)
//																		it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//																					"CNRDOCCONT00_EJB_ObbligazioneComponentSession",
//																					it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession.class);
//			try {
//				return (ObbligazioneBulk)h.modificaConBulk(context, obbligazione);
//			} catch (it.cnr.jada.comp.ApplicationException e) {
//				throw new it.cnr.jada.comp.ApplicationException("Impegno \"" + obbligazione.getDs_obbligazione() + "\": " + e.getMessage(),e);
//			}
//		} catch (java.rmi.RemoteException e) {
//			throw handleException(obbligazione, e);
//		} catch (javax.ejb.EJBException e) {
//			throw handleException(obbligazione, e);
//		}
//	}
//	return null;
//}
////^^@@
///** 
//  *  gestisce una selezione di documenti (ricerca libera e guidata)
//  *    PreCondition:
//  *      l'utente effettua una ricerca libera o guidata
//  *    PostCondition:
//  *      Imposta condizioni supplementari per la gestione dei differenti tipi di documenti generici.
// */
////^^@@
//public it.cnr.jada.persistency.sql.Query select(UserContext aUC, CompoundFindClause clauses, OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {
//
//    boolean trovato= false;
//    String logicOperator= null;
//    int operator= 0;
//    String value= null;
//
//    if (clauses == null) {
//        if (bulk != null)
//            clauses= bulk.buildFindClauses(null);
//    } else {
//        CompoundFindClause newClauses= null;
//
//        for (Iterator i= clauses.iterator(); i.hasNext();) {
//            SimpleFindClause clause= (SimpleFindClause) i.next();
//            if (!trovato & (clause.getPropertyName().equals("tipo_documento"))) {
//                trovato= true;
//                logicOperator= clause.getLogicalOperator();
//                operator= clause.getOperator();
//                value= ((Tipo_documento_ammBulk) clause.getValue())!=null?((Tipo_documento_ammBulk) clause.getValue()).getCd_tipo_documento_amm():value;
//            } else
//                newClauses= CompoundFindClause.and(newClauses, clause);
//        }
//
//        clauses= newClauses;
//    }
//
//    it.cnr.jada.persistency.sql.SQLBuilder sql= null;
//
//    //sql= getHome(aUC, bulk).createSQLBuilder();
//    sql= (SQLBuilder)super.select(aUC, clauses, bulk);
//    sql.setDistinctClause(true);
//    sql.openParenthesis("AND");
//    sql.addClause(clauses);
//    if (trovato)
//        sql.addClause(logicOperator, "cd_tipo_documento_amm", operator, value);
//    sql.closeParenthesis();
//
//    if (((RichiestaUopBulk) bulk).getTi_entrate_spese()==RichiestaUopBulk.ENTRATE || ((RichiestaUopBulk) bulk).getTi_entrate_spese()==RichiestaUopBulk.SPESE) {
//        sql.openParenthesis("AND");
//        sql.addTableToHeader("TIPO_DOCUMENTO_AMM");
//        sql.addSQLJoin("DOCUMENTO_GENERICO.CD_TIPO_DOCUMENTO_AMM", "TIPO_DOCUMENTO_AMM.CD_TIPO_DOCUMENTO_AMM");
//        sql.addSQLClause("AND", "TIPO_DOCUMENTO_AMM.FL_DOC_GENERICO = 'Y'");
//        sql.addSQLClause("AND", "TIPO_DOCUMENTO_AMM.TI_ENTRATA_SPESA='" + (((RichiestaUopBulk) bulk).isGenericoAttivo() ? "E" : "S") + "')");
//        //if (true)
//        //sql.addSQLClause("AND", "TIPO_DOCUMENTO_AMM.TI_ENTRATA_SPESA='" + (((RichiestaUopBulk) bulk).isGenericoAttivo() ? "E" : "S") + "')");
//        //sql.closeParenthesis();
//    }
//    return sql;
//}
////^^@@
///** 
//  * Vengono richiesti i dati relativi ad una banca
//  *    PreCondition:
//  *      Vengono richiesti i dati relativi ad una banca
//  *    PostCondition:
//  *		 Aggiunge delle condizioni supplementari del terzo e del tipo di pagamento e esclude le banche cancellate
//*/
////^^@@
//public it.cnr.jada.persistency.sql.SQLBuilder selectBancaByClause(
//    UserContext aUC,
//    Documento_generico_rigaBulk doc_gen_riga,
//    it.cnr.contab.anagraf00.core.bulk.BancaBulk banca,
//    CompoundFindClause clauses)
//    throws ComponentException {
//
//    it.cnr.contab.anagraf00.core.bulk.BancaHome bancaHome= (it.cnr.contab.anagraf00.core.bulk.BancaHome) getHome(aUC, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
//
//    if (doc_gen_riga.getTerzo_uo_cds() != null && doc_gen_riga.getModalita_pagamento_uo_cds() != null) {
//		return bancaHome.selectBancaFor(
//									doc_gen_riga.getModalita_pagamento_uo_cds(),
//									doc_gen_riga.getTerzo_uo_cds().getCd_terzo());
//    } else if (doc_gen_riga.getTerzo() != null && doc_gen_riga.getModalita_pagamento() != null) {
//		return bancaHome.selectBancaFor(
//									doc_gen_riga.getModalita_pagamento(),
//									doc_gen_riga.getTerzo().getCd_terzo());
//    }
//
//    SQLBuilder sql = bancaHome.createSQLBuilder();
//    sql.addClause("AND", "fl_cancellato", sql.EQUALS, Boolean.FALSE);
//	return sql;
//}
////^^@@
///** 
//  * Vengono richiesti i dati relativi ad una lettera di pagamento
//  *    PreCondition:
//  *      Vengono richiesti i dati relativi ad una lettera di pagamento
//  *    PostCondition:
//  *		 Aggiunge delle condizioni supplementari dell'importo associatom, dell'importo del sospeso 
//  *      e che la lettera non sia di storno
//*/
////^^@@
//public it.cnr.jada.persistency.sql.SQLBuilder selectLettera_pagamento_estero_sospesoByClause(
//	UserContext aUC,
//	RichiestaUopBulk documento, 
//	SospesoBulk sospeso,
//	CompoundFindClause clauses)  
//	throws ComponentException {
//	try {
//		it.cnr.jada.persistency.sql.SQLBuilder sql = getHome(aUC,sospeso).createSQLBuilder();
// 
//		sql.openParenthesis("AND");
//		sql.addSQLClause("OR", "IM_ASSOCIATO", sql.EQUALS, new java.math.BigDecimal(0));
//		sql.addSQLClause("OR", "IM_ASSOCIATO", sql.ISNULL, null);
//		sql.closeParenthesis();
//		sql.openParenthesis("AND");
//		sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.EQUALS, new java.math.BigDecimal(0));
//		sql.addSQLClause("OR", "IM_ASS_MOD_1210", sql.ISNULL, null);
//		sql.closeParenthesis();
//		sql.openParenthesis("AND");
//		sql.addSQLClause("OR", "IM_SOSPESO", sql.NOT_EQUALS, new java.math.BigDecimal(0));
//		sql.addSQLClause("AND", "IM_SOSPESO", sql.ISNOTNULL, null);
//		sql.closeParenthesis();
//		sql.addClause("AND", "fl_stornato", sql.EQUALS, Boolean.FALSE);
//		sql.addClause("AND", "esercizio", sql.EQUALS, documento.getLettera_pagamento_estero().getEsercizio());
//		sql.addClause("AND", "ti_entrata_spesa", sql.EQUALS, sospeso.TIPO_SPESA);
//		sql.addClause("AND", "ti_sospeso_riscontro", sql.EQUALS, sospeso.TI_SOSPESO);
//	
//		//Questa operazione è possibile perché viene effettuato precedentemente
//		//il controllo di compatibilità sulle modalità di pagam
//		Rif_modalita_pagamentoBulk aMod = null;
//		if (documento.getDocumento_generico_dettColl() != null &&
//			!documento.getDocumento_generico_dettColl().isEmpty())
//			aMod = ((Documento_generico_rigaBulk)documento.getDocumento_generico_dettColl().get(0)).getModalita_pagamento();
//		if (aMod == null)
//			sql.addClause("AND", "ti_cc_bi", sql.NOT_EQUALS, sospeso.TIPO_BANCA_ITALIA);
//		else
//			sql.addClause(
//				"AND", 
//				"ti_cc_bi", 
//				(Rif_modalita_pagamentoBulk.BANCA_ITALIA.equalsIgnoreCase(aMod.getTi_pagamento())?
//					sql.EQUALS : sql.NOT_EQUALS),
//				sospeso.TIPO_BANCA_ITALIA);
//		EnteBulk ente = (EnteBulk) getHome(aUC, EnteBulk.class)
//				.findAll().get(0);
//		
//		if (!Utility.createParametriCnrComponentSession().getParametriCnr(aUC,documento.getEsercizio()).getFl_tesoreria_unica().booleanValue()){
//			sql.addClause("AND", "stato_sospeso", sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
//			sql.openParenthesis("AND");
//			sql.addClause("OR", "cd_cds_origine", sql.ISNULL, null);
//			sql.addClause("OR", "cd_cds_origine", sql.EQUALS, documento.getCd_cds());
//			sql.openParenthesis("OR");
//			sql.addClause("AND", "cd_cds", sql.EQUALS, documento.getCd_cds());
//			sql.addClause("AND", "cd_cds_origine", sql.EQUALS, documento.getCd_cds_origine());
//			sql.closeParenthesis();
//			sql.closeParenthesis();
//		}else{
//			sql.addClause("AND", "cd_cds", sql.EQUALS, ente.getCd_unita_organizzativa());
//			sql.openParenthesis("AND");
//			sql.addClause("OR", "cd_cds_origine", sql.ISNULL, null);
//			sql.addClause("OR", "cd_cds_origine", sql.EQUALS, documento.getCd_cds());
//			sql.closeParenthesis();
//			sql.openParenthesis("AND");
//			sql.addClause("OR", "stato_sospeso",sql.EQUALS, SospesoBulk.STATO_SOSP_ASS_A_CDS);
//			sql.addClause("OR", "stato_sospeso",sql.EQUALS, SospesoBulk.STATO_SOSP_IN_SOSPESO);
//			sql.closeParenthesis();
//		}	
//	sql.addClause(clauses);
//
//	return sql;
//		} catch (RemoteException e) {
//			throw handleException(documento, e);
//		} catch (EJBException e) {
//			throw handleException(documento, e);
//		} catch (PersistencyException e) {
//			throw handleException(documento, e);
//		}	
//}
////^^@@
///** 
//  * Vengono richiesti i dati relativi ad un terzo
//  *    PreCondition:
//  *      Vengono richiesti i dati relativi ad un terzo
//  *    PostCondition:
//  *		 Aggiunge delle condizioni supplementari del tipo di terzo da cercare per documenti attivi o passivi,   
//  *		 che il terzo non abbia una data di fine rapporto minore della data di registrazione del documento 
//*/
////^^@@
//public it.cnr.jada.persistency.sql.SQLBuilder selectTerzoByClause(
//    UserContext aUC,
//    Documento_generico_rigaBulk documento,
//    TerzoBulk fornitore,
//    CompoundFindClause clauses)
//    throws ComponentException {
//    it.cnr.jada.persistency.sql.SQLBuilder sql =
//        getHome(aUC, fornitore, "V_TERZO_CF_PI").createSQLBuilder();
//    sql.addTableToHeader("ANAGRAFICO");
//    sql.addSQLJoin("ANAGRAFICO.CD_ANAG", "V_TERZO_CF_PI.CD_ANAG");
//    sql.addSQLClause("AND", "V_TERZO_CF_PI.CD_TERZO", sql.EQUALS, fornitore.getCd_terzo());
//	sql.addSQLClause("AND","V_TERZO_CF_PI.CD_PRECEDENTE",sql.EQUALS,fornitore.getCd_precedente());
//	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.STARTSWITH,documento.getCodice_fiscale());
//	sql.addSQLClause("AND","ANAGRAFICO.PARTITA_IVA",sql.STARTSWITH,documento.getPartita_iva());
//	sql.addSQLClause("AND","ANAGRAFICO.NOME",sql.STARTSWITH,documento.getNome());
//	sql.addSQLClause("AND","ANAGRAFICO.COGNOME",sql.STARTSWITH,documento.getCognome());
//	sql.addSQLClause("AND","ANAGRAFICO.RAGIONE_SOCIALE",sql.STARTSWITH,documento.getRagione_sociale());
//
//    sql.addSQLClause("AND","V_TERZO_CF_PI.TI_TERZO",
//        sql.NOT_EQUALS, documento.getDocumento_generico().isGenericoAttivo() ?
//        fornitore.CREDITORE:
//        fornitore.DEBITORE);
//   	sql.addSQLClause("AND","((V_TERZO_CF_PI.DT_FINE_RAPPORTO IS NULL) OR (V_TERZO_CF_PI.DT_FINE_RAPPORTO >= ?))");
//	sql.addParameter(documento.getDocumento_generico().getData_registrazione(),java.sql.Types.TIMESTAMP,0);
//
//   	sql.addClause(clauses);
//    return sql;
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa degli elenchi 
//  *		   delle fatture passive per voce del piano.
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectTerzoByClause(
//	UserContext userContext, 
//	Stampa_elenco_fattureVBulk stampa, 
//	it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo, 
//	CompoundFindClause clauses) throws ComponentException {
//
//	TerzoHome home = (TerzoHome)getHome(userContext, TerzoBulk.class, "V_TERZO_CF_PI");
//	SQLBuilder sql = home.createSQLBuilder();
//	sql.addClause(clauses);
//	return sql;
//}
//
//public SQLBuilder selectTerzoByClause(UserContext userContext, Stampa_fat_pas_per_vpVBulk stampa,it.cnr.contab.anagraf00.core.bulk.TerzoBulk terzo, CompoundFindClause clauses) throws ComponentException {
//		
//		TerzoHome home = (TerzoHome)getHome(userContext, TerzoBulk.class, "V_TERZO_CF_PI");
//		SQLBuilder sql = home.createSQLBuilder();
//		//sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
//		sql.addClause(clauses);
//		return sql;
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa degli elenchi dei compensi per voce del piano.
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_compensi_per_vpVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws PersistencyException, ComponentException {
//
//	Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
//		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
//		
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			//sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//		else
//		{
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilder();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa degli elenchi 
//  *		   dei documenti generici per voce del piano.
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_doc_gen_per_vpVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws PersistencyException, ComponentException {
//	
//	   Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
//		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
//		
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			//sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//		else
//		{
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilder();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa degli elenchi 
//  *		   delle fatture passive/attive per fornitore/cliente.
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_elenco_fattureVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {
//
//	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//	SQLBuilder sql = home.createSQLBuilder();
//	sql.addClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//	sql.addClause(clauses);
//	return sql;
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa degli elenchi 
//  *		   delle fatture passive per voce del piano.
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_fat_pas_per_vpVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws PersistencyException, ComponentException {
//
//	Unita_organizzativa_enteBulk uoEnte = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class ).findAll().get(0);
//		if ( ((CNRUserContext)userContext).getCd_unita_organizzativa().equals ( uoEnte.getCd_unita_organizzativa() )){
//		
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilderEsteso();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			//sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//		else
//		{
//			SQLBuilder sql = ((Unita_organizzativaHome)getHome(userContext, uo, "V_UNITA_ORGANIZZATIVA_VALIDA")).createSQLBuilder();
//			sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
//			sql.addSQLClause("AND", "CD_UNITA_PADRE", sql.EQUALS, stampa.getCd_cds());
//			sql.addClause(clauses);
//			return sql;
//		}
//}
///**
//  * Costruisce l'struzione SQL corrispondente ad una ricerca con le clausole specificate.
//  * Aggiunge una clausola a tutte le operazioni di ricerca eseguite sulla Unita Organizzativa
//  *
//  * Nome: Richiesta di ricerca di una Unita Organizzativa per la Stampa dei Pagamenti Esteri (mod. 1210)
//  * Pre: E' stata generata la richiesta di ricerca delle UO associate al Cds di scrivania
//  * Post: Viene restituito l'SQLBuilder per filtrare le UO
//  *		  in base al cds di scrivania
//  *
//  * @param userContext	lo userContext che ha generato la richiesta
//  * @param stampa		l'OggettoBulk che rappresenta il contesto della ricerca.
//  * @param uo			l'OggettoBulk da usare come prototipo della ricerca; sul prototipo vengono
//  *						costruite delle clausole aggiuntive che vengono aggiunte in AND alle clausole specificate.
//  * @param				clauses L'albero logico delle clausole da applicare alla ricerca
//  * @return Un'istanza di SQLBuilder contenente l'istruzione SQL da eseguire e tutti i parametri
//  *			della query.
//  *
//**/
//public SQLBuilder selectUoForPrintByClause(UserContext userContext, Stampa_situazioni_pag_esteroVBulk stampa, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk uo, CompoundFindClause clauses) throws ComponentException {
//	try {
//	SQLBuilder sql = null;	
//	Unita_organizzativaHome home = (Unita_organizzativaHome)getHome(userContext, it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk.class);
//	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
//	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
//	   sql = home.createSQLBuilder();
//	   sql.addClause("AND", "cd_unita_padre", sql.EQUALS, stampa.getCd_cds());	   	   
//	}else{
//	   sql = home.createSQLBuilderEsteso();   
//	}
//	sql.addClause(clauses);
//	return sql;
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw new ComponentException(e);
//	}	   
//	
//}
//private RichiestaUopBulk setChangeDataToEur(
//	UserContext userContext, 
//	RichiestaUopBulk genericoBulk)
//	throws ComponentException {
//
//	genericoBulk.setValuta(getEuro(userContext));
//	cercaCambio(userContext, genericoBulk);
//	genericoBulk.setDefaultValuta(true);
//	return genericoBulk;
//}
////^^@@
///** 
//  *  tutti i controlli superati.
//  *    PreCondition:
//  *      Nessun errore rilevato.
//  *    PostCondition:
//  *      Viene impostato nel dettaglio del documento il conto dell'Ente.
//  *  Rifermento di tipo Bancario.
//  *    PreCondition:
//  *      Il riferimento non è di tipo bancario
//  *    PostCondition:
//  *      Esce dal metodo senza apportare modifiche
//  *  Generico attivo.
//  *    PreCondition:
//  *      Il documento non è di tipo "attivo"
//  *    PostCondition:
//  *      Esce dal metodo senza apportare modifiche
//  *  Generico attivo di tipo Ente.
//  *    PreCondition:
//  *      Il documento attivo non è di tipo "Ente"
//  *    PostCondition:
//  *      Esce dal metodo senza apportare modifiche
// */
////^^@@
//
//public BancaBulk setContoEnteIn(
//	it.cnr.jada.UserContext userContext, 
//	Documento_generico_rigaBulk dettaglio, 
//	java.util.List banche)
// 	throws ComponentException {
//	try {
//		if(Utility.createParametriCnrComponentSession().getParametriCnr(userContext,dettaglio.getEsercizio()).getFl_tesoreria_unica().booleanValue()){	
//			if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(dettaglio.getModalita_pagamento_uo_cds().getTi_pagamento()) ||
//				!dettaglio.getDocumento_generico().isGenericoAttivo())
//				return null;
//		}else{
//			if (!Rif_modalita_pagamentoBulk.BANCARIO.equals(dettaglio.getModalita_pagamento_uo_cds().getTi_pagamento()) ||
//				!dettaglio.getDocumento_generico().isGenericoAttivo() ||
//				!dettaglio.getDocumento_generico().isFlagEnte())
//				return null;
//		}
//		Configurazione_cnrBulk config = new Configurazione_cnrBulk(
//															"CONTO_CORRENTE_SPECIALE",
//															"ENTE", 
//															"*", 
//															new Integer(0));
//		it.cnr.contab.config00.bulk.Configurazione_cnrHome home = (it.cnr.contab.config00.bulk.Configurazione_cnrHome)getHome(userContext, config);
//		java.util.List configurazioni = home.find(config);
//		if (configurazioni != null) {
//			if (configurazioni.isEmpty())
//				return null;
//			else if (configurazioni.size() == 1) {
//				Configurazione_cnrBulk configBanca = (Configurazione_cnrBulk)configurazioni.get(0);
//				BancaBulk bancaEnte = null;
//				for (Iterator i = banche.iterator(); i.hasNext();) {
//					BancaBulk banca = (BancaBulk)i.next();
//					if (bancaEnte == null) {
//						if (banca.getAbi().equalsIgnoreCase(configBanca.getVal01()) &&
//							banca.getCab().equalsIgnoreCase(configBanca.getVal02()) &&
//							banca.getNumero_conto().contains(configBanca.getVal03()))
//							bancaEnte = banca;
//					}
//				}
//				//if (bancaEnte == null)
//					//bancaEnte = (BancaBulk)banche.get(0);
//				return bancaEnte;
//			} else
//				return null;
//		}
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(e);
//	} catch (RemoteException e) {
//		throw handleException(e);
//	} catch (EJBException e) {
//		throw handleException(e);
//	}
//
//	return (BancaBulk)banche.get(0);
//}
//private void setDt_termine_creazione_docamm(
//	it.cnr.jada.UserContext userContext,
//	RichiestaUopBulk generico) 
//	throws ComponentException {
//
//	if (generico == null || !generico.isGenericoAttivo()) {
//		generico.setDt_termine_creazione_docamm(null);
//		return;
//	}
//	try {
//		it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession session = (it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)
//			it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//							"CNRCONFIG00_EJB_Configurazione_cnrComponentSession", 
//							it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class);
//		java.sql.Timestamp t = session.getDt01(
//											userContext,
//											generico.getEsercizio(), 
//											"*", 
//											"CHIUSURA_COSTANTI", 
//											"TERMINE_CREAZIONE_DOCAMM_ES_PREC");
//		if (t == null)
//			throw new it.cnr.jada.comp.ApplicationException("La costante di chiusura \"termine creazione docamm es prec\" NON è stata definita! Impossibile proseguire.");
//
//		generico.setDt_termine_creazione_docamm(t);
//	} catch (Throwable e) {
//		throw handleException(e);
//	}
//}
////^^@@
///** 
//  * Vengono richiesti i dati relativi all'ente
//  *    PreCondition:
//  *      vengono richiesti i dati relativi all'ente
//  *    PostCondition:
//  *		 vengono impostati i dati relativi alla UO e CDS dell'ente
//*/
////^^@@
//public RichiestaUopBulk setEnte(
//    UserContext aUC,
//    RichiestaUopBulk documento)
//    throws ComponentException {
//
//    if (documento != null) {
//        
//	    //imposto CDS e UO per l'ENTE se necessario
//       	impostaDatiEnteNelDocumento(aUC, documento, null);
//        	
//		documento.setCd_unita_organizzativa(documento.getUo_CNR());
//		documento.setCd_cds(documento.getCds_CNR());
//    }
//    return documento;
//}
///**
// * Imposta un savepoint che consente di salvare le modifiche apportate al doc. amministrativo fino a quel momento
// * in modo che se gli aggiornamenti apportati al compenso non venissero confermati (rollback), comunque non verrebbero persi
// * anche quelli del documento amministrativo.
// *
// * Pre-post-conditions:
// *
// * Nome: Imposta savePoint
// * Pre:  Una richiesta di impostare un savepoint e' stata generata 
// * Post: Un savepoint e' stato impostato in modo che le modifiche apportate al doc. amministrativo vengono consolidate
// *
// * @param	uc	lo UserContext che ha generato la richiesta
// */	
//public void setSavePoint(UserContext userContext, String savePointName) throws ComponentException {
//
//	try {
//		setSavepoint(userContext, savePointName);
//	} catch (java.sql.SQLException e) {
//		throw handleException(e);
//	}
//}
////^^@@
///**  Stampa dei Documenti Generici/Situazione Pagamenti Estero
//  *  validazione Documenti Generici tutti i controlli superati (validateBulkForPrint(UserContext, Stampa_vpg_doc_genericoBulk).
//  *    PreCondition:
//  *      Nessuna situazione di errore di validazione è stata rilevata.
//  *    PostCondition:
//  *      Consentita la stampa.
//  */
////^^@@
public it.cnr.jada.bulk.OggettoBulk stampaConBulk(it.cnr.jada.UserContext aUC, it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {

//	if (bulk instanceof Stampa_vpg_doc_genericoBulk)
//		validateBulkForPrint(aUC, (Stampa_vpg_doc_genericoBulk)bulk);
//	if (bulk instanceof Stampa_elenco_fattureVBulk)
//		validateBulkForPrint(aUC, (Stampa_elenco_fattureVBulk)bulk);
//	
//	/*if (bulk instanceof Stampa_docamm_per_voce_del_pianoVBulk) 
//		return  stampaConBulk(aUC, (Stampa_docamm_per_voce_del_pianoVBulk) bulk);*/
//	if (bulk instanceof Stampa_fat_pas_per_vpVBulk) 
//		return  stampaConBulk(aUC, (Stampa_fat_pas_per_vpVBulk) bulk);
	return bulk;

}
//
///*public OggettoBulk stampaConBulk(UserContext userContext, Stampa_docamm_per_voce_del_pianoVBulk stampa) throws ComponentException {
//	return stampa;
//}*/
//
//public OggettoBulk stampaConBulk(UserContext userContext, Stampa_fat_pas_per_vpVBulk stampa) throws ComponentException {
//	if ( stampa.getstato()==null )
//			throw new ApplicationException( "E' necessario valorizzare il campo 'Stato'");
//	return stampa;
//}	
//
//
////^^@@
///** 
//  * gestisce la scrittura dei dati delle rige del docuemnto all'atto di eliminazione logica
//  *    PreCondition:
//  *      viene eliminato logicamente un documento
//  *    PostCondition:
//  *		 vengono resi persistenti le modifiche effettuate dal metodo deleteLogically
//*/
////^^@@
//public IDocumentoAmministrativoRigaBulk update(
//	it.cnr.jada.UserContext userContext, 
//	IDocumentoAmministrativoRigaBulk rigaDocAmm)
//	throws it.cnr.jada.comp.ComponentException {
//
//	try {
//		updateBulk(userContext, (OggettoBulk)rigaDocAmm);
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException((OggettoBulk)rigaDocAmm, e);
//	}
//	return rigaDocAmm;
//}
////^^@@
///** 
//  *  Aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
//  *    PreCondition:
//  *      Richiesto l'aggiornamento dell'importo associato a documenti amministrativi di una scadenza di documento contabile
//  *    PostCondition:
//  *      Il dettaglio viene aggiornato
// */
////^^@@
//
//public IScadenzaDocumentoContabileBulk updateImportoAssociatoDocAmm(
//	it.cnr.jada.UserContext userContext, 
//	IScadenzaDocumentoContabileBulk scadenza)
//	throws it.cnr.jada.comp.ComponentException {
//
//	try {
//		((IScadenzaDocumentoContabileHome)getHome(userContext, scadenza.getClass())).aggiornaImportoAssociatoADocAmm(userContext,scadenza);
//	} catch (it.cnr.jada.persistency.PersistencyException exc) {
//		throw handleException((OggettoBulk)scadenza, exc);
//	} catch (it.cnr.jada.bulk.BusyResourceException exc) {
//		throw handleException((OggettoBulk)scadenza, exc);
//	} catch (it.cnr.jada.bulk.OutdatedResourceException exc) {
//		throw handleException((OggettoBulk)scadenza, exc);
//	}
//
//	return scadenza;
//}
//private void validaDisponibilitaDiCassaCDS(UserContext userContext, RichiestaUopBulk documento) throws ComponentException {
//
//	if (documento.isGenericoAttivo()) return;
//	
//	try	{
//		if (documento.getLettera_pagamento_estero()!=null && documento.getLettera_pagamento_estero().getEsercizio()!=null &&
//				!Utility.createParametriCnrComponentSession().getParametriCnr(userContext,documento.getLettera_pagamento_estero().getEsercizio()).getFl_tesoreria_unica().booleanValue()){	
//			it.cnr.jada.bulk.BulkHome home = getHome( userContext, V_disp_cassa_cdsBulk.class);
//			SQLBuilder sql = home.createSQLBuilder();
//			sql.addClause( "AND", "esercizio", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
//			sql.addClause( "AND", "cd_cds", sql.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds());
//			List result = null;
//			result = home.fetchAll( sql );
//			if ( result.size() == 0 )
//				throw new ApplicationException("Non esiste il record per la disponibilità di cassa del CDS: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds() + " - esercizio: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio());
//			V_disp_cassa_cdsBulk cassa = (V_disp_cassa_cdsBulk) result.get(0);
//			if (cassa.getIm_disponibilita_cassa().compareTo(new java.math.BigDecimal(0)) < 0||
//			  ((documento.getLettera_pagamento_estero().getSospeso()==null||documento.getLettera_pagamento_estero().getSospeso().getCd_sospeso()==null) && cassa.getIm_disponibilita_cassa().compareTo(documento.getIm_totale())<0))			
//				throw new it.cnr.jada.comp.ApplicationException("La disponibilità di cassa del CDS: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds() + " - esercizio: " + ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getEsercizio() + " è stata superata! Salvataggio interrotto.");
//		}
//	} catch ( Exception e )	{
//		throw handleException(documento, e);
//	}
//
//}
////^^@@
///**  Validazione dell'intero documento amministrativo ativo/passivo
//  *  tutti i controlli superati
//  *    PreCondition:
//  *      Nessuna situazione di errore di validazione è stata rilevata.
//  *    PostCondition:
//  *      Consentita la registrazione.
//  *  validazione numero di dettagli maggiore di zero.
//  *    PreCondition:
//  *      Il numero di dettagli nel documento è zero
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Attenzione non possono esistere documenti senza almeno un dettaglio".
//  *  validazione associazione scadenze
//  *    PreCondition:
//  *      Esistono dettagli non collegati ad obbligazione.
//  *    PostCondition:
//  *      Viene inviato un messaggio:"Attenzione esistono dettagli non collegati ad obbligazione."
//  *  validazione modifica documento pagato.
//  *    PreCondition:
//  *      E' satata eseguita una modifica in documento con testata in stato P.
//  *    PostCondition:
//  *      Viene inviato un messaggio:"Attenzione non si modificare nulla in un documento pagato".
// */
////^^@@
public void validaDocumento(UserContext aUC, RichiestaUopBulk richiesta) throws ComponentException {

    //controllo stato diverso da Inserito
     if (RichiestaUopBulk.STATO_INSERITO.equalsIgnoreCase(richiesta.getStato()))
    	 throw new it.cnr.jada.comp.ApplicationException("Attenzione non si può modificare nulla in un documento pagato");

    //controllo dettagli
    if (documentoGenerico.getDocumento_generico_dettColl().isEmpty())
        throw new it.cnr.jada.comp.ApplicationException(
            "Attenzione non possono esistere documenti senza almeno un dettaglio");
	try {
    RichiestaUopBulk documentoDB = (RichiestaUopBulk)getTempHome(aUC, RichiestaUopBulk.class).findByPrimaryKey(documentoGenerico);
	if (documentoDB==null || (documentoGenerico.getDt_da_competenza_coge().compareTo(documentoDB.getDt_da_competenza_coge())!=0 ||
		documentoGenerico.getDt_a_competenza_coge().compareTo(documentoDB.getDt_a_competenza_coge())!=0)){
		    //controlla le date di competenza COGE
		    try {
			    documentoGenerico.validaDateCompetenza();
		    } catch (ValidationException e) {
			    throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
		    }
		}
	} catch (PersistencyException e) {
		throw handleException(e);
	}
    controllaCompetenzaCOGEDettagli(aUC, documentoGenerico);

    //controlla il tipo ti documento
    if (documentoGenerico.getCd_tipo_documento_amm() == null)
        documentoGenerico.setCd_tipo_documento_amm(documentoGenerico.getTipo_documento().getCd_tipo_documento_amm());

	//controlla compatibilità dei clienti/fornitori x accertamenti/obbligazioni
    for (java.util.Iterator i = documentoGenerico.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
        Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk) i.next();
        if (riga.getStato_cofi().equals(riga.STATO_INIZIALE))
            throw new it.cnr.jada.comp.ApplicationException("Attenzione la riga " + riga.getDs_riga() + " è in stato iniziale");
        if (documentoGenerico.getTi_entrate_spese() == documentoGenerico.ENTRATE) {
            if (!riga.getTerzo().getCd_terzo().equals(riga.getAccertamento_scadenziario().getAccertamento().getCd_terzo())
                && (!riga
                    .getAccertamento_scadenziario()
                    .getAccertamento()
                    .getDebitore()
                    .getAnagrafico()
                    .getTi_entita()
                    .equals(AnagraficoBulk.DIVERSI)
                    && !(riga.getTerzo().getAnagrafico().getTi_entita().equals(AnagraficoBulk.DIVERSI))))
                throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione la riga " + riga.getDs_riga() + " ha un terzo incompatibile con il documento contabile associato.");
        } else {
            if (!riga.getTerzo().getCd_terzo().equals(riga.getObbligazione_scadenziario().getObbligazione().getCd_terzo())
                && (!riga
                    .getObbligazione_scadenziario()
                    .getObbligazione()
                    .getCreditore()
                    .getAnagrafico()
                    .getTi_entita()
                    .equals(AnagraficoBulk.DIVERSI)
                    && !(riga.getTerzo().getAnagrafico().getTi_entita().equals(AnagraficoBulk.DIVERSI))))
                throw new it.cnr.jada.comp.ApplicationException(
                    "Attenzione la riga " + riga.getDs_riga() + " ha un terzo incompatibile con il documento contabile associato.");
        }
    }
    
    //controllo obbligazione/accertamento
    if (!documentoGenerico.isPassivo_ente()) {
        if (documentoGenerico.getTi_entrate_spese() == documentoGenerico.SPESE) {
            controllaQuadraturaObbligazioni(aUC, documentoGenerico);
        } else {
            controllaQuadraturaAccertamenti(aUC, documentoGenerico);
        }
    } else {
        if (documentoGenerico.getTi_entrate_spese() == documentoGenerico.SPESE) {
	        ObbligazioniTable obbligazioniHash = documentoGenerico.getObbligazioniHash();
	        if (obbligazioniHash != null)
	            for (java.util.Enumeration e = obbligazioniHash.keys(); e.hasMoreElements();) {
	                Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) e.nextElement();
	                controllaOmogeneitaTraTerzi(aUC, scadenza, (Vector)obbligazioniHash.get(scadenza));
	            	controlloTrovato(aUC, scadenza);		
	            }
        } else {
	        AccertamentiTable accertamentiHash = documentoGenerico.getAccertamentiHash();
	        if (accertamentiHash != null)
	            for (java.util.Enumeration e = accertamentiHash.keys(); e.hasMoreElements();) {
	                Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk) e.nextElement();
	                controllaOmogeneitaTraTerzi(aUC, scadenza, (Vector)accertamentiHash.get(scadenza));
	            	controlloTrovato(aUC, scadenza);		
	            }
        }
    }
    
    controllaContabilizzazioneDiTutteLeRighe(aUC, documentoGenerico);

    //controlli per la lettera di pagamento
    if (documentoGenerico.getLettera_pagamento_estero() != null) {
	    if (!documentoGenerico.controllaCompatibilitaPer1210() || documentoGenerico.isByFondoEconomale())
	    	throw new it.cnr.jada.comp.ApplicationException("E' stata selezionata una lettera di pagamento per un documento in cui o i terzi e le modalità di pagamento sono differenti o il pagamento del fondo economale è stato selezionato");
    }

    return;
}
////^^@@
///**  Validazione di una sigola riga del documento
//  *  tutti i controli superati
//  *    PreCondition:
//  *      Viene richiesta la validazione per salvataggio
//  *    PostCondition:
//  *      Viene consentita la registrazione riga.
//  *  validazione modifica dettaglio pagato.
//  *    PreCondition:
//  *      Le date di competenza non sono esatte
//  *    PostCondition:
//  *      Viene inviato un messaggio:"La data di inizio competenza non può essere successiva alla fine competenza.
//  *  validazione modifica  campi di dettaglio di un documento pagato.
//  *    PreCondition:
//  *      Non sono state inseririte le modalità di pagamento per la riga
//  *    PostCondition:
//  *      Viene inviato un messaggio "Inserire le modalità di pagamento per la riga xxx"
// */
////^^@@
//public void validaRiga(
//    UserContext aUC,
//    Documento_generico_rigaBulk documentoGenericoRiga)
//    throws ComponentException {
//        //if (RichiestaUopBulk
//            //.STATO_PAGATO
//            //.equalsIgnoreCase(
//                //documentoGenericoRiga.getDocumento_generico().getStato_cofi()))
//            //throw new it.cnr.jada.comp.ApplicationException(
//                //"Attenzione:  questa modifica non è permessa");
//        //if (documentoGenericoRiga.getDt_da_competenza_coge() != null
//            //&& documentoGenericoRiga.getDt_a_competenza_coge() != null
//            //&& documentoGenericoRiga.getDt_da_competenza_coge().after(
//                //documentoGenericoRiga.getDt_a_competenza_coge()))
//            //throw new it.cnr.jada.comp.ApplicationException(
//                //"La data di inizio competenza non può essere successiva alla fine competenza");
//
//		try { 
//			documentoGenericoRiga.validaDateCompetenza();
//		} catch (ValidationException e) {
//			throw new it.cnr.jada.comp.ApplicationException(e.getMessage());
//		}
//		
//        RichiestaUopBulk documentoGenerico =
//            (RichiestaUopBulk) documentoGenericoRiga.getDocumento_generico();
//        if (documentoGenerico.getTi_entrate_spese() == documentoGenerico.SPESE) {
//            if (documentoGenericoRiga.getModalita_pagamento() == null)
//                throw new it.cnr.jada.comp.ApplicationException(
//                    "Inserire le modalità di pagamento per la riga "
//                        + (documentoGenericoRiga.getDs_riga() != null
//                            ? documentoGenericoRiga.getDs_riga()
//                            : ""));
//        } else {
//            if (documentoGenericoRiga.getModalita_pagamento_uo_cds() == null)
//                throw new it.cnr.jada.comp.ApplicationException(
//                    "Inserire le modalità di pagamento per la riga "
//                        + (documentoGenericoRiga.getDs_riga() != null
//                            ? documentoGenericoRiga.getDs_riga()
//                            : ""));
//            documentoGenericoRiga.setPg_banca(null);
//            documentoGenericoRiga.setCd_modalita_pag(null);
//            documentoGenericoRiga.setCd_termini_pag(null);
//        }
//       
//}
////^^@@
///**  Validazione per la stampa dei Documenti Generici
//  *  validazione Progressivo Inizio nullo
//  *    PreCondition:
//  *      Non è stato indicato il progressivo di Documento Iniziale della stampa
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Il campo NUMERO INIZIO è obbligatorio".
//  *
//  *  validazione Progressivo Fine nullo
//  *    PreCondition:
//  *      Non è stato indicato il progressivo di Documento Finale della stampa
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Il campo NUMERO FINE è obbligatorio".
//  *
//  *  validazione Progressivo Inizio maggiore del Progressivo Fine
//  *    PreCondition:
//  *      Il progressivo Iniziale è minore del Documento Finale
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Il NUMERO INIZIO non può essere superiore al NUMERO FINE".
//  *
//  *  validazione Data Inizio nulla
//  *    PreCondition:
//  *      Non è stata indicata alcuna Data di Inizio
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Il campo DATA INIZIO PERIODO è obbligatorio".
//  *
//  *  validazione Data Fine nulla
//  *    PreCondition:
//  *      Non è stata indicata alcuna Data di Fine
//  *    PostCondition:
//  *      Viene inviato un messaggio: "Il campo DATA FINE PERIODO è obbligatorio".
//  *
//  *  validazione Data Inizio superiore alla Data Fine
//  *    PreCondition:
//  *      E' stata indicata una Data di Inizio periodo posteriore alla Data di Fine periodo.
//  *    PostCondition:
//  *      Viene inviato un messaggio: "La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO".
//  *
//  *  validazione Data Inizio antecedente al 01/01 dell'Esercizio di scrivania
//  *    PreCondition:
//  *      E' stata indicata una Data di Inizio periodo antecedente al 01/01 dell'Esercizio di scrivania.
//  *    PostCondition:
//  *      Viene inviato un messaggio: "La DATA di INIZIO PERIODO non può essere inferiore a....".
//  *
//  *  validazione Data Fine maggiore alla data di odierna
//  *    PreCondition:
//  *      E' stata indicata una Data Fine maggiore alla data di odierna
//  *    PostCondition:
//  *      Viene inviato un messaggio: "La DATA di FINE PERIODO non può essere superiore a ....".
//  *
//  *  tutti i controlli superati
//  *    PreCondition:
//  *      Nessuna situazione di errore di validazione è stata rilevata.
//  *    PostCondition:
//  *      Consentita la stampa.
//  *
//  */
////^^@@
//private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_vpg_doc_genericoBulk stampa) throws ComponentException{
//
//	try{
//		
//		java.sql.Timestamp firstDayOfYear = getFirstDayOfYear(stampa.getEsercizio().intValue());
//		java.sql.Timestamp lastDayOfYear = DateServices.getLastDayOfYear(stampa.getEsercizio().intValue());
//	
//		/**** Controlli sui PG_INIZIO/PG_FINE *****/
//		if (stampa.getPgInizio()==null)
//			throw new ValidationException("Il campo NUMERO INIZIO è obbligatorio");
//		if (stampa.getPgFine()==null)
//			throw new ValidationException("Il campo NUMERO FINE è obbligatorio");
//		if (stampa.getPgInizio().compareTo(stampa.getPgFine())>0)
//			throw new ValidationException("Il NUMERO INIZIO non può essere superiore al NUMERO FINE");
//
//		/**** Controlli sulle Date DA/A	*****/
//		if (stampa.getDataInizio()==null)
//			throw new ValidationException("Il campo DATA INIZIO PERIODO è obbligatorio");
//		if (stampa.getDataFine()==null)
//			throw new ValidationException("Il campo DATA FINE PERIODO è obbligatorio");
//
//  			
//		// La Data di Inizio Periodo è superiore alla data di Fine Periodo
//		if (stampa.getDataInizio().compareTo(stampa.getDataFine())>0)
//			throw new ValidationException("La DATA di INIZIO PERIODO non può essere superiore alla DATA di FINE PERIODO");
//			
//		// La Data di Inizio Periodo è ANTECEDENTE al 1 Gennaio dell'Esercizio di scrivania
//		if (stampa.getDataInizio().compareTo(firstDayOfYear)<0){
//			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
//			throw new ValidationException("La DATA di INIZIO PERIODO non può essere inferiore a " + formatter.format(firstDayOfYear));
//		}
//		// La Data di Fine periodo è SUPERIORE alla data lastDayOfYear
//		if (stampa.getDataFine().compareTo(lastDayOfYear)>0){
//			java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("dd/MM/yyyy");
//			throw new ValidationException("La DATA di FINE PERIODO non può essere superiore a " + formatter.format(lastDayOfYear));
//		}
//
//		
//	}catch(ValidationException ex){
//		throw new ApplicationException(ex);
//	}
//}
////^^@@
///** 
//  *  Verifica l'esistenza e apertura dell'esercizio
//  *    PreCondition:
//  *      Nessuna condizione di errore rilevata.
//  *    PostCondition:
//  *      Viene consentita l'attività richiesta
//  *  L'esercizio non è aperto
//  *    PreCondition:
//  *      L'esercizio su cui insiste il controllo non è aperto
//  *    PostCondition:
//  *      Viene notificato l'errore
// */
////^^@@
//
//public boolean verificaStatoEsercizio(
//	it.cnr.jada.UserContext userContext, 
//	it.cnr.contab.config00.esercizio.bulk.EsercizioBulk anEsercizio) 
//	throws it.cnr.jada.comp.ComponentException {
//
//	try {
//		it.cnr.contab.config00.esercizio.bulk.EsercizioHome eHome = (it.cnr.contab.config00.esercizio.bulk.EsercizioHome)getHome(userContext, EsercizioBulk.class);
//		return !eHome.isEsercizioChiuso(
//									userContext,
//									anEsercizio.getEsercizio(),
//									anEsercizio.getCd_cds());
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(e);
//	}
//}
//public SQLBuilder selectVocedpForPrintByClause(UserContext userContext, Stampa_fat_pas_per_vpVBulk stampa, Elemento_voceBulk e_v, CompoundFindClause clauses) throws ComponentException {
//		Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
//		SQLBuilder sql = home.createSQLBuilder();
//		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
//		sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, home.GESTIONE_SPESE); 
//		sql.addClause( clauses );
//		return sql;	
//}
//public SQLBuilder selectVocedpForPrintByClause(UserContext userContext, Stampa_compensi_per_vpVBulk stampa, Elemento_voceBulk e_v, CompoundFindClause clauses) throws ComponentException {
//		Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
//		SQLBuilder sql = home.createSQLBuilder();
//		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
//		sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, home.GESTIONE_SPESE);
//	    sql.addClause( clauses );
//		return sql;	
//}
//public SQLBuilder selectVocedpForPrintByClause(UserContext userContext, Stampa_doc_gen_per_vpVBulk stampa, Elemento_voceBulk e_v, CompoundFindClause clauses) throws ComponentException {
//		Elemento_voceHome home = (Elemento_voceHome)getHome(userContext, Elemento_voceBulk.class);
//		SQLBuilder sql = home.createSQLBuilder();
//		sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, ((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio());
//		sql.addSQLClause("AND", "TI_GESTIONE", SQLBuilder.EQUALS, home.GESTIONE_SPESE);
//	    sql.addClause(clauses);
//	    return sql;
//}
///**
// * Metodo che serve per ricostruire sul documento alcune HashTable necessarie per il funzionamento
// * del Business Process
// *
// * Pre:  E' stata modificato il documento o alcuni suoi dettagli  
// * Post: Viene ricaricata l'HashTable delle Obbligazioni o Accertamenti associati al documento generico       
// *
// * @param userContext
// * @param documento generico modificato
// * @return documento aggiornato con le modifiche
// * @throws it.cnr.jada.comp.ComponentException
// */
//public RichiestaUopBulk rebuildDocumento(it.cnr.jada.UserContext userContext, RichiestaUopBulk documento) throws it.cnr.jada.comp.ComponentException {
//    if (!documento.isGenericoAttivo()) {
//		documento.setDocumento_generico_obbligazioniHash(null);
//		rebuildObbligazioni(userContext, documento);
//    } else {
//		documento.setDocumento_generico_accertamentiHash(null);
//		rebuildAccertamenti(userContext, documento);
//    }
//	return documento;
//}
////aggiunto per testare l'esercizio della data competenza da/a
//public boolean isEsercizioChiusoPerDataCompetenza(UserContext userContext,Integer esercizio,String cd_cds) throws ComponentException, PersistencyException {
//	try {
//		LoggableStatement cs = new LoggableStatement(getConnection(userContext),
//				"{ ? = call " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema()
//				+	"CNRCTB008.isEsercizioChiusoYesNo(?,?)}",false,this.getClass());		
//
//		try {
//			cs.registerOutParameter( 1, java.sql.Types.CHAR);
//			cs.setObject(2,esercizio);		
//			cs.setObject(3,cd_cds);		
//			
//			cs.execute();
//
//			return "Y".equals(cs.getString(1));
//		} finally {
//			cs.close();
//		}
//	} catch (java.sql.SQLException e) {
//		throw handleSQLException(e);
//	}
//}
//public RemoteIterator selectBeniFor(
//		UserContext userContext,
//		RichiestaUopBulk documento) throws ComponentException 
//	{
//		SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
//		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,documento.getCd_cds());
//		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, documento.getCd_unita_organizzativa());			
//		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,documento.getEsercizio());
//		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
//		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,documento.getPg_documento_generico());
//		return  iterator(userContext,sql,V_ass_inv_bene_fatturaBulk.class,null);	
//	}
//public Boolean ha_beniColl(UserContext userContext,OggettoBulk doc) throws ComponentException
//{	
//	SQLBuilder sql = getHome(userContext, V_ass_inv_bene_fatturaBulk.class).createSQLBuilder();
//	if (doc instanceof RichiestaUopBulk){
//		RichiestaUopBulk documento = (RichiestaUopBulk)doc;
//		// nella view vengono valorizzati gli stessi campi
//		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,documento.getCd_cds());
//		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, documento.getCd_unita_organizzativa());			
//		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,documento.getEsercizio());
//		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
//		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,documento.getPg_documento_generico());
//	}else{
//		Documento_generico_rigaBulk documento = (Documento_generico_rigaBulk)doc;
//		sql.addSQLClause("AND","cd_cds_fatt_pass",sql.EQUALS,documento.getCd_cds());
//		sql.addSQLClause("AND","cd_uo_fatt_pass",sql.EQUALS, documento.getCd_unita_organizzativa());			
//		sql.addSQLClause("AND","esercizio_fatt_pass",sql.EQUALS,documento.getEsercizio());
//		sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
//		sql.addSQLClause("AND","pg_fattura_passiva",sql.EQUALS,documento.getPg_documento_generico());
//		sql.addSQLClause("AND","PROGRESSIVO_RIGA_FATT_PASS", sql.EQUALS,documento.getProgressivo_riga());
//		}
//		try 
//		{
//		  if (sql.executeCountQuery(getConnection(userContext))>0)							
//			return Boolean.TRUE;						 
//		  else
//			return Boolean.FALSE;	
//			} 
//		catch (java.sql.SQLException e) 
//		{
//			   throw handleSQLException(e);				
//		}
//	}	
////^^@@
//private void controllaQuadraturaInventario(UserContext auc, RichiestaUopBulk documento) throws ComponentException {
//	BigDecimal totale_inv = Utility.ZERO;
//	BigDecimal totale_doc = Utility.ZERO;
//	BigDecimal im_riga =Utility.ZERO;
//	
//	for(Iterator i =documento.getDocumento_generico_dettColl().iterator();i.hasNext();){
//		Documento_generico_rigaBulk riga =(Documento_generico_rigaBulk)i.next();
//		if (riga.isInventariato()){
//			im_riga = riga.getIm_riga();	
//			totale_doc=totale_doc.add(im_riga);
//		}
//	}
//	Buono_carico_scarico_dettHome assHome=(Buono_carico_scarico_dettHome)getHome(auc,Buono_carico_scarico_dettBulk.class);
//	SQLBuilder sql= assHome.createSQLBuilder();
//	
//	sql.setDistinctClause(true);
//	sql.addTableToHeader("ASS_INV_BENE_FATTURA");
//	sql.addSQLClause("AND","CD_CDS_DOC_GEN",sql.EQUALS,documento.getCd_cds());
//	sql.addSQLClause("AND","CD_UO_DOC_GEN",sql.EQUALS, documento.getCd_unita_organizzativa());			
//	sql.addSQLClause("AND","ESERCIZIO_DOC_GEN",sql.EQUALS,documento.getEsercizio());
//	sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,documento.getCd_tipo_documento_amm());
//	sql.addSQLClause("AND","PG_DOCUMENTO_GENERICO",sql.EQUALS,documento.getPg_documento_generico());
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.PG_INVENTARIO");
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.ESERCIZIO");
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",sql.EQUALS,"ASS_INV_BENE_FATTURA.TI_DOCUMENTO");
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO",sql.EQUALS,"ASS_INV_BENE_FATTURA.NR_INVENTARIO");
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",sql.EQUALS,"ASS_INV_BENE_FATTURA.PG_BUONO_C_S");
//	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO",sql.EQUALS,"ASS_INV_BENE_FATTURA.PROGRESSIVO");		
//	try {
//		List associazioni =assHome.fetchAll(sql);
//		for (Iterator i=associazioni.iterator();i.hasNext();){
//			Buono_carico_scarico_dettBulk ass=(Buono_carico_scarico_dettBulk)i.next();
//			ass.setBene((Inventario_beniBulk)getHome(auc, Inventario_beniBulk.class).findByPrimaryKey(ass.getBene()));
//			// per gestire l'inventario anche per i generici di reintegro del fondo 
//			if (documento.getTipo_documento().getCd_tipo_documento_amm().compareTo(documento.GENERICO_E)==0)
//				totale_inv=totale_inv.add(ass.getBene().getValore_alienazione());
//			else
//				totale_inv=totale_inv.add(ass.getValore_unitario());
//		}
//	} catch (PersistencyException e) {
//		throw handleException(e);
//	}		
//	if (totale_doc.compareTo(totale_inv)!=0)
//		throw new ApplicationException("Attenzione il totale delle righe collegate ad Inventario non corrisponde all'importo da Inventariare.");
//		
//}
///** 
// *	Dettagli di Documento passivo non inventariati
// *		PreCondition:
// * 		Richiesta dell'esistenza di dettagli non inventariati
// *   	PostCondition:
// *  		Restituisce la conferma
//*/
////^^@@
//
//
//public boolean hasDocumentoPassivoARowNotInventoried(
//	UserContext userContext,
//	RichiestaUopBulk documento)
//	throws ComponentException {
//
//	java.util.Vector coll = new java.util.Vector();
//	Iterator dettagli = documento.getDocumento_generico_dettColl().iterator();
//	
//		while (dettagli.hasNext()) {
//			Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)dettagli.next();
//			if (dettagli != null && documento.getTi_entrate_spese()==RichiestaUopBulk.SPESE) {
//				if (riga.getObbligazione_scadenziario() !=null && riga.getObbligazione_scadenziario().getObbligazione()!= null && 
//					 riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null &&
//					riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getFl_inv_beni_patr().booleanValue()&&
//				!riga.isInventariato())
//				return true;
//			}
//			else{
//				if (riga.getAccertamento_scadenziario() !=null && riga.getAccertamento_scadenziario().getAccertamento()!= null &&
//						riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce()!=null){ 
//						Elemento_voceBulk elem_voce = new Elemento_voceBulk(riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getEsercizio(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getTi_appartenenza(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getTi_gestione());
//						try {
//							elem_voce = (Elemento_voceBulk)getHome(userContext, elem_voce).findByPrimaryKey(elem_voce);
//						} catch (PersistencyException e) {
//							throw handleException(elem_voce,e);
//						} 
//						if (elem_voce.getFl_inv_beni_patr().booleanValue() &&!riga.isInventariato())
//							return true;
//				}
//			}
//	}
//	return false;
//}
//public void verificaEsistenzaEdAperturaInventario(
//		UserContext userContext, 
//		RichiestaUopBulk documento)
//		throws ComponentException {
//
//		try {
//			it.cnr.contab.inventario00.ejb.IdInventarioComponentSession h = (it.cnr.contab.inventario00.ejb.IdInventarioComponentSession)
//															it.cnr.jada.util.ejb.EJBCommonServices.createEJB(
//																		"CNRINVENTARIO00_EJB_IdInventarioComponentSession",
//																		it.cnr.contab.inventario00.ejb.IdInventarioComponentSession.class);
//			it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario = h.findInventarioFor(
//																						userContext,
//																						documento.getCd_cds_origine(),
//																						documento.getCd_uo_origine(),
//																						false);
//			if (inventario == null)
//				throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che non esiste un inventario per questo CDS.\nIn caso di inserimento di dettagli con beni soggetti ad inventario, non sarà permesso il salvataggio della fattura,\nfino alla creazione ed apertura di un nuovo inventario!");
//			else if (!h.isAperto(userContext, inventario, documento.getEsercizio())) {
//				throw new it.cnr.jada.comp.ApplicationException("Attenzione: si informa che l'inventario per questo CDS non è aperto.\nNel caso di inserimento di dettagli con beni soggetti ad inventario, non sarà permesso il salvataggio della fattura\nfino ad apertura di quest'ultimo!");
//			}
//		} catch (Exception e) {
//			throw handleException(documento, e);
//		}
//	}
//private void prepareCarichiInventario(UserContext userContext,RichiestaUopBulk documento) throws ComponentException {
//
//	if (documento != null) {
//		CarichiInventarioTable carichiInventarioHash = documento.getCarichiInventarioHash();
//		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
//			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
//				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
//				if (buonoCS instanceof Buono_carico_scaricoBulk){// && buonoCS.isByFattura()) {
//					it.cnr.jada.bulk.PrimaryKeyHashtable ht = buonoCS.getDettagliRigheDocHash();
//					if (ht != null && !ht.isEmpty()) {
//						it.cnr.jada.bulk.PrimaryKeyHashtable newHt = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//						for (java.util.Enumeration k = ht.keys(); k.hasMoreElements();)
//							((Documento_generico_rigaBulk)k.nextElement()).setDocumento_generico(documento);
//						ht = new it.cnr.jada.bulk.PrimaryKeyHashtable(ht);
//						for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
//							Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
//							BulkList bl = (BulkList)ht.get(riga);
//							if (bl != null)
//								newHt.put(riga, bl);
//						}
//						buonoCS.setDettagliRigheDocHash(newHt);
//					}
//				}
//			}
//		}
//	}
//}
//private String aggiornaAssociazioniInventario(UserContext userContext,RichiestaUopBulk doc) throws ComponentException {
//
//	StringBuffer msgBuf = new StringBuffer();
//
//	if (doc != null) {
//		AssociazioniInventarioTable associazioniInventarioHash = doc.getAssociazioniInventarioHash();
//		if (associazioniInventarioHash != null && !associazioniInventarioHash.isEmpty()) {
//			Vector associazioniTemporanee = new Vector();
//			for (java.util.Enumeration e = ((AssociazioniInventarioTable)associazioniInventarioHash.clone()).keys(); e.hasMoreElements();) {
//				Ass_inv_bene_fatturaBulk ass = (Ass_inv_bene_fatturaBulk)e.nextElement();
//				if (ass.isTemporaneo() &&
//					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(associazioniTemporanee, ass))
//					associazioniTemporanee.add(ass);
//			}
//			for (Iterator i = associazioniTemporanee.iterator(); i.hasNext();){
//				String msg = aggiornaAssociazioniInventarioTemporanee(userContext, doc, (Ass_inv_bene_fatturaBulk)i.next());
//				if (msg != null)
//					msgBuf.append(msg);
//			}
//		}
//	}
//
//	if (msgBuf.toString().equals(""))
//		return null;
//		
//	return msgBuf.toString();
//}
//private String aggiornaAssociazioniInventarioTemporanee(
//	UserContext userContext,
//	RichiestaUopBulk doc,
//	Ass_inv_bene_fatturaBulk assTemporanea) throws ComponentException {
//	String msg = null;
//	try {
//		Ass_inv_bene_fatturaHome home = (Ass_inv_bene_fatturaHome)getHome(userContext, assTemporanea);
//
//		if (assTemporanea.isPerAumentoValoreDoc())
//			msg = home.makePersistentAssociaPerAumento(userContext,assTemporanea, doc);
//		else 
//			home.makePersistentAssocia(userContext,assTemporanea, doc);
//			
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw new it.cnr.jada.comp.ApplicationException("Operazione effettuata al momento da un'altro utente, riprovare successivamente.");
//	} catch (it.cnr.jada.persistency.IntrospectionException e) {
//		throw handleException(assTemporanea, e);
//	}
//
//	return msg;
//}
//private void aggiornaCarichiInventario(UserContext userContext,RichiestaUopBulk documento) throws ComponentException {
//
//	 if (documento != null){
//		CarichiInventarioTable carichiInventarioHash = documento.getCarichiInventarioHash();
//		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
//			Vector carichiTemporanei = new Vector();
//			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
//				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
//				Buono_carico_scaricoHome home = (Buono_carico_scaricoHome)getHome(userContext, buonoCS);
//				if (buonoCS.isByDocumento() &&
//					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(carichiTemporanei, buonoCS))
//					carichiTemporanei.add(buonoCS);
//				// Rp test su formazione ok
//					 
//                 for(Iterator g= buonoCS.getBuono_carico_scarico_dettColl().iterator();g.hasNext();){
//					Buono_carico_scarico_dettBulk buono=((Buono_carico_scarico_dettBulk)g.next());
//					boolean exit=false;
//                  	BigDecimal tot_perc_cdr=new BigDecimal(0);
//					for(Iterator f =documento.getDocumento_generico_dettColl().iterator(); (f.hasNext()&&!exit);){
//						Documento_generico_rigaBulk doc=((Documento_generico_rigaBulk)f.next());
//						for(Iterator coll =((Buono_carico_scaricoBulk)buonoCS).getDettagliDocumentoColl().iterator(); (coll.hasNext()&&!exit);){
//							Documento_generico_rigaBulk doc_coll=((Documento_generico_rigaBulk)coll.next());
//						if (doc_coll.equalsByPrimaryKey(doc)) {
//                        try {                    	
//							Obbligazione_scadenzarioHome obblHome = ((Obbligazione_scadenzarioHome)getHome(userContext, Obbligazione_scadenzarioBulk.class, null, getFetchPolicyName("findForInventario")));
//							ObbligazioneBulk obblig =doc.getObbligazione_scadenziario().getObbligazione();
//							ObbligazioneHome obbligHome = (ObbligazioneHome) getHome(userContext, obblig.getClass());
//							obblig.setObbligazione_scadenzarioColl(new BulkList(obbligHome.findObbligazione_scadenzarioList(obblig)));
//							for ( Iterator i = obblig.getObbligazione_scadenzarioColl().iterator(); i.hasNext(); )
//							{
//								Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk) i.next();
//								if (os.getIm_associato_doc_amm().doubleValue()!=0 && os.equalsByPrimaryKey(doc.getObbligazione_scadenziario())){
//									os.setObbligazione( obblig );
//									os.setObbligazione_scad_voceColl( new BulkList( obblHome.findObbligazione_scad_voceList(userContext, os )));
//									doc.setObbligazione_scadenziario(os);
//									obblig.refreshDettagliScadenzarioPerCdrECapitoli();
//							for (Iterator lista_cdraggr =obblig.getCdrAggregatoColl().iterator();lista_cdraggr.hasNext();){	
//							  Obbligazione_scad_voce_aggregatoBulk cdraggr=(Obbligazione_scad_voce_aggregatoBulk) lista_cdraggr.next();
//	  					      if (cdraggr!=null && cdraggr.getImporto()!=null && cdraggr.getImporto().doubleValue() != 0){
//								BigDecimal tot_perc_la=new BigDecimal(0);
//								Iterator listaScad_voce = obblHome.findObbligazione_scad_voceList(userContext,doc.getObbligazione_scadenziario()).iterator();
//								for (Iterator x =listaScad_voce;x.hasNext();){
//									Obbligazione_scad_voceBulk dett =(Obbligazione_scad_voceBulk)x.next();
//									getHomeCache(userContext).fetchAll(userContext);
//									WorkpackageBulk linea_att =dett.getLinea_attivita();
//								if (linea_att.getCd_centro_responsabilita()==cdraggr.getCodice()){
//								if ( dett.getObbligazione_scadenzario().getIm_scadenza().doubleValue() != 0)
//								     dett.setPrc((dett.getIm_voce().multiply( new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(), 2, BigDecimal.ROUND_HALF_UP)));
//								if ( dett.getPrc()!=null && dett.getPrc().compareTo(new BigDecimal(0))!=0 && dett.getObbligazione_scadenzario().getIm_associato_doc_amm().doubleValue()!=0) {						    	
//									it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk new_utilizzatore_la
//									= new it.cnr.contab.inventario00.docs.bulk.Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(),linea_att.getCd_centro_responsabilita(),
//									buono.getNr_inventario(),
//									buono.getPg_inventario(),new Long(buono.getProgressivo().longValue()));
//									BigDecimal perc_cdr=(cdraggr.getImporto().multiply(new BigDecimal(100)).divide(dett.getObbligazione_scadenzario().getIm_scadenza(),2,6));
//									tot_perc_cdr=tot_perc_cdr.add(perc_cdr);
//									tot_perc_la=tot_perc_la.add(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6));
//									if ((tot_perc_cdr.doubleValue()>=100&&tot_perc_la.doubleValue()>=100)){
//										exit=true; 
//									}
//									new_utilizzatore_la.setPercentuale_utilizzo_cdr(perc_cdr);
//									new_utilizzatore_la.setPercentuale_utilizzo_la(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6));
//									new_utilizzatore_la.setToBeCreated();
//																if ((perc_cdr.compareTo(new BigDecimal(100))<=0) ||(((dett.getPrc()).multiply(new BigDecimal(100))).divide(perc_cdr,2,6).compareTo(new BigDecimal(100))<=0)){
//																	Inventario_utilizzatori_laHome Inventario_utilizzatore_laHome = (Inventario_utilizzatori_laHome)getHome(userContext, Inventario_utilizzatori_laBulk.class);
//																	Inventario_utilizzatori_laBulk utilizzatore = (Inventario_utilizzatori_laBulk)Inventario_utilizzatore_laHome.findByPrimaryKey(new Inventario_utilizzatori_laBulk(linea_att.getCd_linea_attivita(),linea_att.getCd_centro_responsabilita(),
//																			buono.getNr_inventario(),
//																			buono.getPg_inventario(),new Long(buono.getProgressivo().longValue())));
//																	if (!new_utilizzatore_la.equalsByPrimaryKey(utilizzatore))	
//										super.insertBulk(userContext,new_utilizzatore_la,true);
//								}	
//							}
//						}	
//					}
//				 }	
//							}	
//							}					
//							}					
//			} catch (IntrospectionException e1) {
//				throw new ComponentException(e1);
//			} catch (PersistencyException e1) {
//				throw new ComponentException(e1);
//			      }
//              	 }	
//			    }	
//		       }
//              }
//              //fine commento 
//			}
//			for (Iterator i = carichiTemporanei.iterator(); i.hasNext();){
//				Buono_carico_scaricoBulk buono_temporaneo=(Buono_carico_scaricoBulk)i.next();
//				aggiornaBuoniCaricoTemporanei(userContext, documento, buono_temporaneo);
//				cancellaBuoniCaricoTemporanei(userContext, documento,buono_temporaneo);
//				
//			}		
//		}
//	}
//}
//private void aggiornaBuoniCaricoTemporanei(
//		UserContext userContext,
//		RichiestaUopBulk doc,
//		Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {
//
//		try {
//			Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
//			Long pg = null;
//			
//				pg = numHome.getNextPg(userContext,
//							buonoTemporaneo.getEsercizio(),
//							buonoTemporaneo.getPg_inventario(),
//							buonoTemporaneo.getTi_documento(),
//							userContext.getUser());
//			
//			Buono_carico_scaricoBulk definitivo= (Buono_carico_scaricoBulk)buonoTemporaneo.clone();
//		
//			Buono_carico_scarico_dettHome home = (Buono_carico_scarico_dettHome)getTempHome(userContext, Buono_carico_scarico_dettBulk.class);
//			
//			SQLBuilder sql=home.createSQLBuilder();
//			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoTemporaneo.getEsercizio().toString());
//			sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoTemporaneo.getPg_inventario().toString());
//			sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoTemporaneo.getTi_documento());
//			sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoTemporaneo.getPg_buono_c_s().toString());
//			List dettagli = home.fetchAll(sql);
//
//			definitivo.setPg_buono_c_s(pg);
//			definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
//			super.creaConBulk(userContext,definitivo);		
//			for (Iterator i=dettagli.iterator();i.hasNext();){
//				Buono_carico_scarico_dettBulk dettaglio=(Buono_carico_scarico_dettBulk)i.next();
//				Buono_carico_scarico_dettBulk new_dettaglio=(Buono_carico_scarico_dettBulk)dettaglio.clone();
//				new_dettaglio.setPg_buono_c_s(pg);
//				new_dettaglio.setCrudStatus(OggettoBulk.TO_BE_CREATED);
//				super.creaConBulk(userContext,new_dettaglio);
//			}			
//			if (definitivo !=null) {
//				if (definitivo.getDettagliRigheDocHash() != null && !definitivo.getDettagliRigheDocHash().isEmpty()){
//					Documento_generico_rigaBulk riga = null;
//					Buono_carico_scarico_dettBulk inventario_dett = null;
//					Inventario_beniBulk bene = null;	
//					Ass_inv_bene_fatturaBulk nuova_associazione=null;	
//						for (Enumeration e = definitivo.getDettagliRigheDocHash().keys(); e.hasMoreElements();){
//							riga = (Documento_generico_rigaBulk)e.nextElement();
//							BulkList beni_associati = (BulkList) definitivo.getDettagliRigheDocHash().get(riga);
//							for (Iterator i = beni_associati.iterator(); i.hasNext();){
//								inventario_dett = (Buono_carico_scarico_dettBulk)i.next();
//								bene  = inventario_dett.getBene();
//								nuova_associazione = new Ass_inv_bene_fatturaBulk();
//								nuova_associazione.setRiga_doc_gen((Documento_generico_rigaBulk)riga);
//								nuova_associazione.setTest_buono(definitivo);
//								nuova_associazione.setNr_inventario(bene.getNr_inventario());
//								nuova_associazione.setProgressivo(bene.getProgressivo());
//								nuova_associazione.setToBeCreated();
//								super.creaConBulk(userContext,nuova_associazione);
//							}			
//						}
//				}
//				
//			}
//		
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(buonoTemporaneo, e);
//		} catch (it.cnr.jada.persistency.IntrospectionException e) {
//			throw handleException(buonoTemporaneo, e);
//		}	
//	}
//private void aggiornaEstremiDocumento(UserContext userContext, RichiestaUopBulk doc, Buono_carico_scaricoBulk buono_temporaneo) throws ComponentException {
//	try{	
//		Inventario_beni_apgHome apgHome =(Inventario_beni_apgHome)getHome(userContext,Inventario_beni_apgBulk.class);
//		SQLBuilder sql=apgHome.createSQLBuilder();
//		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,buono_temporaneo.getLocal_transactionID());
//		List beniApg=apgHome.fetchAll(sql);
//		for (Iterator i=beniApg.iterator();i.hasNext();){
//			Inventario_beni_apgBulk beneApg =(Inventario_beni_apgBulk)i.next();
//			beneApg.setCd_cds(doc.getCd_cds());
//			beneApg.setCd_unita_organizzativa(doc.getCd_unita_organizzativa());
//			beneApg.setPg_fattura(doc.getPg_documento_generico());
//			updateBulk(userContext,beneApg);
//		}
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(e);	
//	 }
//}
//private void cancellaBuoniCaricoTemporanei(UserContext userContext, RichiestaUopBulk doc, Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException 
//{
//	try{
//		Buono_carico_scarico_dettHome home = (Buono_carico_scarico_dettHome)getHome(userContext, Buono_carico_scarico_dettBulk.class);
//		SQLBuilder sql=home.createSQLBuilder();
//		sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,buonoTemporaneo.getEsercizio().toString());
//		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoTemporaneo.getPg_inventario().toString());
//		sql.addSQLClause("AND","TI_DOCUMENTO",sql.EQUALS,buonoTemporaneo.getTi_documento());
//		sql.addSQLClause("AND","PG_BUONO_C_S",sql.EQUALS,buonoTemporaneo.getPg_buono_c_s().toString());
//		List dettagli = home.fetchAll(sql);
//		
//		for (Iterator i=dettagli.iterator();i.hasNext();){			
//			Buono_carico_scarico_dettBulk dettaglio=(Buono_carico_scarico_dettBulk)i.next();
//			dettaglio.setToBeDeleted();
//			super.eliminaConBulk(userContext,dettaglio);
//		}
//		buonoTemporaneo.setToBeDeleted();
//		super.eliminaConBulk(userContext,buonoTemporaneo);
//	} catch (it.cnr.jada.persistency.PersistencyException e) {
//		throw handleException(buonoTemporaneo, e);
//	}
//}
//public void rimuoviDaAssociazioniInventario(
//		UserContext userContext,
//		Documento_generico_rigaBulk dettaglio,
//		Ass_inv_bene_fatturaBulk associazione) throws ComponentException {
//
//	if (dettaglio != null && associazione != null) {
//		Inventario_beni_apgHome home = (Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
//		try {
//			home.deleteTempFor(userContext, associazione);
//		} catch (it.cnr.jada.persistency.IntrospectionException e) {
//			throw handleException(dettaglio, e);
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(dettaglio, e);
//		}
//	}
//}
//public void rimuoviDaAssociazioniInventario(
//		UserContext userContext,
//		Ass_inv_bene_fatturaBulk associazione) throws ComponentException {
//
//	if (associazione != null) {
//		Inventario_beni_apgHome home = (Inventario_beni_apgHome)getHome(userContext, Inventario_beni_apgBulk.class);
//		try {
//			home.deleteTempFor(userContext, associazione);
//		} catch (it.cnr.jada.persistency.IntrospectionException e) {
//			throw handleException(e);
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(e);
//		}
//	}
//}
//private void deleteAssociazioniInventarioWith(UserContext userContext,Documento_generico_rigaBulk dettaglio)
//throws ComponentException {
//	try {
//		if (dettaglio != null && dettaglio.isToBeDeleted()) {
//			Ass_inv_bene_fatturaHome asshome=(Ass_inv_bene_fatturaHome)getHome(userContext,Ass_inv_bene_fatturaBulk.class);
//			SQLBuilder sql = asshome.createSQLBuilder();
//		    sql.addSQLClause("AND","CD_CDS_DOC_GEN",sql.EQUALS,dettaglio.getCd_cds());
//		    sql.addSQLClause("AND","ESERCIZIO_DOC_GEN",sql.EQUALS,dettaglio.getEsercizio().intValue());
//		    sql.addSQLClause("AND","CD_UO_DOC_GEN",sql.EQUALS,dettaglio.getCd_unita_organizzativa());
//		    sql.addSQLClause("AND","CD_TIPO_DOCUMENTO_AMM",sql.EQUALS,dettaglio.getCd_tipo_documento_amm());
//		    sql.addSQLClause("AND","PG_DOCUMENTO_GENERICO",sql.EQUALS,dettaglio.getPg_documento_generico().longValue());
//		    sql.addSQLClause("AND","PROGRESSIVO_RIGA_DOC_GEN",sql.EQUALS,dettaglio.getProgressivo_riga().longValue());
//		    List associazioni = asshome.fetchAll(sql);
//		    for (Iterator i = associazioni.iterator();i.hasNext();){
//		    	Ass_inv_bene_fatturaBulk associazione = (Ass_inv_bene_fatturaBulk)i.next();
//		    	associazione.setCrudStatus(OggettoBulk.TO_BE_DELETED);
//		    	super.eliminaConBulk(userContext,associazione);
//		    }
//		}
//	} catch (PersistencyException e) {
//		throw handleException(dettaglio, e);
//	}
// }
//private void prepareScarichiInventario(UserContext userContext,RichiestaUopBulk documento) throws ComponentException {
//
//	if (documento != null) {
//		CarichiInventarioTable carichiInventarioHash = documento.getCarichiInventarioHash();
//		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
//			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
//				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
//				if (buonoCS instanceof Buono_carico_scaricoBulk){// && buonoCS.isByFattura()) {
//					it.cnr.jada.bulk.PrimaryKeyHashtable ht = buonoCS.getDettagliRigheDocHash();
//					if (ht != null && !ht.isEmpty()) {
//						it.cnr.jada.bulk.PrimaryKeyHashtable newHt = new it.cnr.jada.bulk.PrimaryKeyHashtable();
//						for (java.util.Enumeration k = ht.keys(); k.hasMoreElements();)
//							((Documento_generico_rigaBulk)k.nextElement()).setDocumento_generico(documento);
//						ht = new it.cnr.jada.bulk.PrimaryKeyHashtable(ht);
//						for (Iterator i = documento.getDocumento_generico_dettColl().iterator(); i.hasNext();) {
//							Documento_generico_rigaBulk riga = (Documento_generico_rigaBulk)i.next();
//							BulkList bl = (BulkList)ht.get(riga);
//							if (bl != null)
//								newHt.put(riga, bl);
//						}
//						buonoCS.setDettagliRigheDocHash(newHt);
//					}
//				}
//			}
//		}
//	}
//}
//private void aggiornaScarichiInventario(UserContext userContext,RichiestaUopBulk documento) throws ComponentException {
//
//	if (documento != null) {
//		CarichiInventarioTable carichiInventarioHash = documento.getCarichiInventarioHash();
//		if (carichiInventarioHash != null && !carichiInventarioHash.isEmpty()) {
//			Vector carichiTemporanei = new Vector();
//			for (java.util.Enumeration e = ((CarichiInventarioTable)carichiInventarioHash.clone()).keys(); e.hasMoreElements();) {
//				Buono_carico_scaricoBulk buonoCS = (Buono_carico_scaricoBulk)e.nextElement();
//				Buono_carico_scaricoHome home = (Buono_carico_scaricoHome)getHome(userContext, buonoCS);
//				if (buonoCS.isByDocumento() &&
//					!it.cnr.jada.bulk.BulkCollections.containsByPrimaryKey(carichiTemporanei, buonoCS))
//					carichiTemporanei.add(buonoCS);
//			}
//			
//			for (Iterator i = carichiTemporanei.iterator(); i.hasNext();){
//				Buono_carico_scaricoBulk buono_temporaneo=(Buono_carico_scaricoBulk)i.next();
//				aggiornaEstremiDocumento(userContext,documento,buono_temporaneo);
//				aggiornaBuoniScaricoTemporanei(userContext, documento, buono_temporaneo);
//				
//			}
//		}
//	}
//}
//private void aggiornaBuoniScaricoTemporanei(
//		UserContext userContext,
//		RichiestaUopBulk documento,
//		Buono_carico_scaricoBulk buonoTemporaneo) throws ComponentException {
//
//		try {
//			Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome(Numeratore_buono_c_sBulk.class);
//			Long pg = numHome.getNextPg(userContext,
//							buonoTemporaneo.getEsercizio(),
//							buonoTemporaneo.getPg_inventario(),
//							buonoTemporaneo.getTi_documento(),
//							userContext.getUser());
//			
//			Buono_carico_scaricoBulk definitivo= (Buono_carico_scaricoBulk)buonoTemporaneo.clone();
//			definitivo.setPg_buono_c_s(pg);
//			definitivo.setCrudStatus(OggettoBulk.TO_BE_CREATED);
//			Buono_carico_scaricoHome home = (Buono_carico_scaricoHome)getHome(userContext, definitivo);
//			home.makePersistentScaricoDaFattura(definitivo,documento);
//		} catch (it.cnr.jada.persistency.PersistencyException e) {
//			throw handleException(buonoTemporaneo, e);
//		} catch (it.cnr.jada.persistency.IntrospectionException e) {
//			throw handleException(buonoTemporaneo, e);
//		}	
//	}
//public boolean existARowToBeInventoried(UserContext context,RichiestaUopBulk documento) throws ComponentException {
//	if (documento.getDocumento_generico_dettColl() != null) {
//		Iterator dettagli = documento.getDocumento_generico_dettColl().iterator();
//		while (dettagli.hasNext()) {
//			Documento_generico_rigaBulk riga=(Documento_generico_rigaBulk)dettagli.next();
//		    if (documento.getTi_entrate_spese()==RichiestaUopBulk.SPESE){
//			if (riga.getObbligazione_scadenziario() !=null && riga.getObbligazione_scadenziario().getObbligazione()!= null && 
//				riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce()!=null &&
//				riga.getObbligazione_scadenziario().getObbligazione().getElemento_voce().getFl_inv_beni_patr().booleanValue())
//				return true;
//		    }else{
//		    	if (riga.getAccertamento_scadenziario() !=null && riga.getAccertamento_scadenziario().getAccertamento()!= null &&
//						riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce()!=null){ 
//						Elemento_voceBulk elem_voce = new Elemento_voceBulk(riga.getAccertamento_scadenziario().getAccertamento().getCd_elemento_voce(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getEsercizio(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getTi_appartenenza(),
//																		riga.getAccertamento_scadenziario().getAccertamento().getTi_gestione());
//						try {
//							elem_voce = (Elemento_voceBulk)getHome(context,Elemento_voceBulk.class).findByPrimaryKey(elem_voce);
//						} catch (PersistencyException e) {
//							throw handleException(elem_voce,e);
//						} 
//						if (elem_voce.getFl_inv_beni_patr().booleanValue())
//							return true;
//				}		
//		    }
//		}
//	}
//	return false;
//}
//public List findMandatoRigaCollegati (UserContext userContext, Documento_generico_rigaBulk documentoGenericoRiga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
//{
//	List result=null;
//	if (documentoGenericoRiga.getObbligazione_scadenziario() != null &&
//		documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione() != null &&
//		documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getCd_cds()!=null && 
//		documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio()!=null &&
//		documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio_originale()!=null &&
//		documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getPg_obbligazione()!=null &&
//		documentoGenericoRiga.getObbligazione_scadenziario().getPg_obbligazione_scadenzario()!=null) { 
//
//        SQLBuilder sql = getHome( userContext, Mandato_rigaIBulk.class ).createSQLBuilder();
//        sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_cds() );
//        sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_unita_organizzativa() );
//        sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getEsercizio() );
//        sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_tipo_documento_amm() );
//        sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getPg_documento_generico() );
//        sql.addClause(FindClause.AND, "cd_cds_obbligazione", SQLBuilder.EQUALS, documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getCd_cds() );
//        sql.addClause(FindClause.AND, "esercizio_obbligazione", SQLBuilder.EQUALS, documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio() );
//        sql.addClause(FindClause.AND, "esercizio_ori_obbligazione", SQLBuilder.EQUALS, documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getEsercizio_originale() );
//        sql.addClause(FindClause.AND, "pg_obbligazione", SQLBuilder.EQUALS, documentoGenericoRiga.getObbligazione_scadenziario().getObbligazione().getPg_obbligazione() );
//        sql.addClause(FindClause.AND, "pg_obbligazione_scadenzario", SQLBuilder.EQUALS, documentoGenericoRiga.getObbligazione_scadenziario().getPg_obbligazione_scadenzario() );
//
//        result = getHome( userContext, Mandato_rigaIBulk.class ).fetchAll( sql );
//	}
//
//	return result;
//}
//public List findReversaleRigaCollegate (UserContext userContext, Documento_generico_rigaBulk documentoGenericoRiga) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
//{
//	List result=null;
//	if (documentoGenericoRiga.getAccertamento_scadenziario() != null &&
//		documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento() != null &&
//		documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getCd_cds()!=null && 
//		documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getEsercizio()!=null &&
//		documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getEsercizio_originale()!=null &&
//		documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getPg_accertamento()!=null &&
//		documentoGenericoRiga.getAccertamento_scadenziario().getPg_accertamento_scadenzario()!=null) { 
//		SQLBuilder sql = getHome( userContext, Reversale_rigaIBulk.class ).createSQLBuilder();
//		sql.addClause(FindClause.AND, "cd_cds_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_cds() );
//		sql.addClause(FindClause.AND, "cd_uo_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_unita_organizzativa() );
//		sql.addClause(FindClause.AND, "esercizio_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getEsercizio() );
//		sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getCd_tipo_documento_amm() );
//		sql.addClause(FindClause.AND, "pg_doc_amm", SQLBuilder.EQUALS, documentoGenericoRiga.getPg_documento_generico() );
//		sql.addClause(FindClause.AND, "cd_cds_accertamento", SQLBuilder.EQUALS, documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getCd_cds() );
//		sql.addClause(FindClause.AND, "esercizio_accertamento", SQLBuilder.EQUALS, documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getEsercizio() );
//		sql.addClause(FindClause.AND, "esercizio_ori_accertamento", SQLBuilder.EQUALS, documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getEsercizio_originale() );
//		sql.addClause(FindClause.AND, "pg_accertamento", SQLBuilder.EQUALS, documentoGenericoRiga.getAccertamento_scadenziario().getAccertamento().getPg_accertamento() );
//		sql.addClause(FindClause.AND, "pg_accertamento_scadenzario", SQLBuilder.EQUALS, documentoGenericoRiga.getAccertamento_scadenziario().getPg_accertamento_scadenzario() );
//	
//		result = getHome( userContext, Reversale_rigaIBulk.class ).fetchAll( sql );
//	}
//
//	return result;
//}
//private void validateBulkForPrint(it.cnr.jada.UserContext userContext, Stampa_elenco_fattureVBulk stampa) throws ComponentException{
//
//	try{
//		if (stampa.getTerzo()==null||stampa.getTerzo().getCd_terzo()==null)
//			throw new ValidationException("Il campo Terzo è obbligatorio");
//	
//	}catch(ValidationException ex){
//		throw new ApplicationException(ex);
//	}
//}
//public TerzoBulk getTerzoUnivoco(UserContext userContext,RichiestaUopBulk documento) throws ComponentException {
//
//        TerzoBulk terzo= null;
//        Integer cd_terzo =null;
//    	List terziCol;
//    try {
//    	Documento_generico_rigaHome home=(Documento_generico_rigaHome)getHome( userContext, Documento_generico_rigaBulk.class );
//    	SQLBuilder sql = home.createSQLBuilder();
//    	sql.addClause(FindClause.AND, "cd_cds", SQLBuilder.EQUALS, documento.getCd_cds() );
//		sql.addClause(FindClause.AND, "cd_unita_organizzativa", SQLBuilder.EQUALS, documento.getCd_unita_organizzativa() );
//		sql.addClause(FindClause.AND, "esercizio", SQLBuilder.EQUALS, documento.getEsercizio() );
//		sql.addClause(FindClause.AND, "cd_tipo_documento_amm", SQLBuilder.EQUALS, documento.getCd_tipo_documento_amm() );
//		sql.addClause(FindClause.AND, "pg_documento_generico", SQLBuilder.EQUALS, documento.getPg_documento_generico() );
//		if(sql.executeCountQuery(connection)==0)
//			return null;
//		else
//			terziCol=home.fetchAll(sql);
//			cd_terzo=((Documento_generico_rigaBulk)terziCol.get(0)).getCd_terzo();
//		for(Iterator i=terziCol.iterator();i.hasNext();){
//			Documento_generico_rigaBulk riga=(Documento_generico_rigaBulk)i.next();
//			if (riga.getCd_terzo().compareTo(cd_terzo)!=0)
//				cd_terzo=null;
//		}
//			 if(cd_terzo!=null){
//				 it.cnr.jada.bulk.BulkHome homet= getHome(userContext, TerzoBulk.class);
//				 it.cnr.jada.persistency.sql.SQLBuilder sql_terzo= homet.createSQLBuilder();
//				 sql_terzo.addClause("AND", "cd_terzo", sql.EQUALS, cd_terzo);
//	
//				 it.cnr.jada.persistency.Broker broker= homet.createBroker(sql_terzo);
//				 if (!broker.next())
//					 return null;
//	
//				 terzo= (TerzoBulk) broker.fetch(TerzoBulk.class);
//				 broker.close();
//			 }
//    } catch (it.cnr.jada.persistency.PersistencyException e) {
//        handleException(e);
//    } catch (java.sql.SQLException e) {
//        handleException(e);
//	}
//    return terzo;
//}
//	public RichiestaUopBulk eliminaLetteraPagamentoEstero(
//			UserContext context,
//			RichiestaUopBulk docGen) throws ComponentException{
//		return eliminaLetteraPagamentoEstero(context,docGen,true);
//	}
//	

@Override
public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
	return inizializzaBulk(usercontext, oggetto);
}

private OggettoBulk inizializzaBulk(UserContext usercontext, OggettoBulk oggetto) throws ComponentException {
	RichiestaUopHome home = (RichiestaUopHome) getHomeCache(usercontext).getHome(RichiestaUopBulk.class);
	try {
		home.inizializzaBulk(usercontext, oggetto);
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	return oggetto;
}

@Override
public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
	return inizializzaBulk(usercontext, oggetto);
}

@Override
public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerRicerca(usercontext, oggettobulk);
	return inizializzaBulk(usercontext, oggetto);
}

@Override
public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerRicercaLibera(usercontext, oggettobulk);
	return inizializzaBulk(usercontext, oggetto);
}
public SQLBuilder selectCentroResponsabilitaByClause(
		UserContext userContext, RichiestaUopRigaBulk pdg, CdrBulk cdr,
		CompoundFindClause clause) throws PersistencyException, ComponentException {
	SQLBuilder sql = getHome(userContext, CdrBulk.class, "V_CDR_VALIDO")
			.createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS,
			it.cnr.contab.utenze00.bp.CNRUserContext
					.getEsercizio(userContext));
	if (clause != null)
		sql.addClause(clause);
	sql.addOrderBy("CD_CENTRO_RESPONSABILITA");
	return sql;
}

public SQLBuilder selectLinea_attivitaByClause (UserContext userContext, 
		RichiestaUopRigaBulk dett,
		WorkpackageBulk latt, 
		CompoundFindClause clause) throws ComponentException, PersistencyException, RemoteException {	
	SQLBuilder sql = getHome(userContext, latt, "V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();

	sql.addSQLClause(FindClause.AND,"V_LINEA_ATTIVITA_VALIDA.ESERCIZIO",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(userContext));
	if (dett.getCdCentroResponsabilita() != null){
		sql.addClause(FindClause.AND,"cd_centro_responsabilita",SQLBuilder.EQUALS,dett.getCdCentroResponsabilita());
	} else {
		throw new ApplicationException ("GAE non selezionabile senza aver prima indicato il centro di responsabilità!");
	}

	sql.openParenthesis(FindClause.AND);
	sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_SPESE);
	sql.addClause(FindClause.OR,"ti_gestione",SQLBuilder.EQUALS,WorkpackageBulk.TI_GESTIONE_ENTRAMBE);
	sql.closeParenthesis();

	if (dett.getProgetto()!=null && dett.getProgetto().getPg_progetto()!=null)
		sql.addClause(FindClause.AND,"pg_progetto",SQLBuilder.EQUALS,dett.getProgetto().getPg_progetto());

	// Obbligatorio cofog sulle GAE
	if(((Parametri_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Parametri_cnrComponentSession",Parametri_cnrComponentSession.class)).isCofogObbligatorio(userContext))
		sql.addSQLClause(FindClause.AND,"CD_COFOG",SQLBuilder.ISNOTNULL,null);
	sql.addTableToHeader("FUNZIONE");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_FUNZIONE","FUNZIONE.CD_FUNZIONE");
	sql.addSQLClause(FindClause.AND, "FUNZIONE.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

	sql.addTableToHeader("NATURA");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.CD_NATURA","NATURA.CD_NATURA");
	sql.addSQLClause(FindClause.AND, "NATURA.FL_SPESA",SQLBuilder.EQUALS,"Y");

	sql.addTableToHeader("PROGETTO_GEST");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.ESERCIZIO","PROGETTO_GEST.ESERCIZIO");
	sql.addSQLJoin("V_LINEA_ATTIVITA_VALIDA.PG_PROGETTO","PROGETTO_GEST.PG_PROGETTO");
	sql.addSQLClause(FindClause.AND,"PROGETTO_GEST.FL_UTILIZZABILE",SQLBuilder.EQUALS,"Y");

	/**
	 * Escludo la linea di attività dell'IVA C20
	 */
	it.cnr.contab.config00.bulk.Configurazione_cnrBulk config = null;
	try {
		config = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, null, null, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.PK_LINEA_ATTIVITA_SPECIALE, it.cnr.contab.config00.bulk.Configurazione_cnrBulk.SK_LINEA_COMUNE_VERSAMENTO_IVA);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} catch (EJBException e) {
		throw new ComponentException(e);
	}
	if (config != null){
		sql.addSQLClause( FindClause.AND, "V_LINEA_ATTIVITA_VALIDA.CD_LINEA_ATTIVITA",  SQLBuilder.NOT_EQUALS, config.getVal01());
	}

	if (clause != null) sql.addClause(clause);

	return sql; 
}	

public SQLBuilder selectElemento_voceByClause (UserContext userContext, 
		RichiestaUopRigaBulk dett,
		Elemento_voceBulk elementoVoce, 
		CompoundFindClause clause) throws ComponentException, PersistencyException {
	if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

	SQLBuilder sql = getHome(userContext, elementoVoce,"V_ELEMENTO_VOCE_PDG_SPE").createSQLBuilder();
	
	if(clause != null) sql.addClause(clause);

	sql.addSQLClause("AND", "V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

	sql.addTableToHeader("PARAMETRI_LIVELLI");
	sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_SPE.ESERCIZIO", "PARAMETRI_LIVELLI.ESERCIZIO");

	sql.addTableToHeader("V_CLASSIFICAZIONE_VOCI_ALL");
	sql.addSQLJoin("V_ELEMENTO_VOCE_PDG_SPE.ID_CLASSIFICAZIONE", "V_CLASSIFICAZIONE_VOCI_ALL.ID_CLASSIFICAZIONE");
	sql.addSQLJoin("V_CLASSIFICAZIONE_VOCI_ALL.NR_LIVELLO", "PARAMETRI_LIVELLI.LIVELLI_SPESA");

	sql.openParenthesis("AND");
	sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", sql.ISNULL, null);	
	sql.addSQLClause("OR", "V_ELEMENTO_VOCE_PDG_SPE.FL_PARTITA_GIRO", sql.EQUALS, "N");	
	sql.closeParenthesis();
	sql.addSQLClause( "AND", "V_ELEMENTO_VOCE_PDG_SPE.FL_SOLO_RESIDUO", sql.EQUALS, "N"); 
	if (dett.getLineaAttivita() != null)
		sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_SPE.CD_FUNZIONE",sql.EQUALS,dett.getLineaAttivita().getCd_funzione());
	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
	if(!parCnrBulk.getFl_nuovo_pdg())
		if (dett.getCdCentroResponsabilita()!=null && dett.getCentroResponsabilita().getUnita_padre().getCd_tipo_unita() != null)
			sql.addSQLClause("AND","V_ELEMENTO_VOCE_PDG_SPE.CD_TIPO_UNITA",sql.EQUALS,dett.getCentroResponsabilita().getUnita_padre().getCd_tipo_unita());

	if (clause != null) sql.addClause(clause);

	return sql;
}
public SQLBuilder selectProgettoByClause (UserContext userContext, 
		RichiestaUopRigaBulk dett,
		ProgettoBulk prg, 
		CompoundFindClause clause) throws ComponentException, PersistencyException {
	ProgettoHome progettoHome = (ProgettoHome)getHome(userContext, prg,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettoHome.createSQLBuilder();
	sql.addSQLClause("AND","esercizio",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND","tipo_fase",sql.EQUALS,ProgettoBulk.TIPO_FASE_GESTIONE);

	Parametri_cnrHome parCnrhome = (Parametri_cnrHome)getHome(userContext, Parametri_cnrBulk.class);
	Parametri_cnrBulk parCnrBulk = (Parametri_cnrBulk)parCnrhome.findByPrimaryKey(new Parametri_cnrBulk(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext )));
	if (parCnrBulk.getFl_nuovo_pdg())
		sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	else
		sql.addClause("AND", "livello", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_TERZO);

	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){
		if (parCnrBulk.getFl_nuovo_pdg())
			sql.addSQLExistsClause("AND",progettoHome.abilitazioniCommesse(userContext));
		else
			sql.addSQLExistsClause("AND",progettoHome.abilitazioniModuli(userContext));
	}	  
	if (clause != null) sql.addClause(clause);
	return sql; 
}	
public SQLBuilder selectBeneServizioByClause(UserContext userContext, RichiestaUopRigaBulk riga, 
		Bene_servizioHome beneHome, Bene_servizioBulk bene, 
		CompoundFindClause compoundfindclause) throws PersistencyException{
	SQLBuilder sql = beneHome.selectByClause(userContext, compoundfindclause);
	sql.addSQLClause("AND", "FL_VALIDO", SQLBuilder.EQUALS, Bene_servizioBulk.STATO_VALIDO);
	
	return sql;
}
}
