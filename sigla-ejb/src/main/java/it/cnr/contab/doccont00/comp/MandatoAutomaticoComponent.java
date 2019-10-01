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
 * Created on Mar 30, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.V_anagrafico_terzoBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaBulk;
import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.contab.docamm00.docs.bulk.Tipo_documento_ammBulk;
import it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardHome;
import it.cnr.contab.doccont00.core.bulk.MandatoAutomaticoWizardBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoBulk;
import it.cnr.contab.doccont00.core.bulk.MandatoIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaIBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_terzoBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_terzoIBulk;
import it.cnr.contab.doccont00.core.bulk.Numerazione_doc_contBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazione_wizardBulk;
import it.cnr.contab.doccont00.core.bulk.V_obbligazioneBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk;
import it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MandatoAutomaticoComponent extends MandatoComponent {
	/**
	 * Crea la ComponentSession da usare per effettuare le operazioni sulle Obbligazioni
	 *
	 * @return ObbligazioneComponentSession l'istanza di <code>ObbligazioneComponentSession</code> che serve per 
	 *         effettuare le operazioni sulle Obbligazioni
	 */
	private ObbligazioneComponentSession createObbligazioneComponentSession() throws ComponentException 
	{
		try
		{
			return (ObbligazioneComponentSession)EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession");
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	/** 
	  *  creazione mandato
	  *    PreCondition:
	  *      E' stata generata la richiesta di creazione un Mandato Automatico da impegni e il mandato supera la validazione
	  *      (metodo verificaMandato)
	  *    PostCondition:
	  *      Vengono aggiornati gli importi dei sospesi eventualmente associati al mandato (metodo aggiornaImportoSospesi), 
	  *      vengono aggiornati gli importi associati a documenti contabili di tutte le scadenze di obbligazioni specificate 
	  *      nelle righe del mandato (metodo aggiornaImportoObbligazione), vengo aggiornati i saldi relativi ai capitoli di spesa
	  *      (metodo aggiornaStatoFattura), vengono aggiornati gli stati delle fatture specificate nelle righe dei mandati
	  *      (metodo aggiornaCapitoloSaldoRiga)
	  *  creazione mandato automatico da impegno di competenza
	  *    PreCondition:
	  *      E' stata generata la richiesta di creazione un Mandato automatico e l'utente ha selezionato solo
	  *      impegni di competenza
	  *    PostCondition:
	  *      Viene richiesto alla Component che gestisce i documenti amministrativi generici di creare un documento
	  *      generico di spesa (di tipo GENERICO_S) con tante righe quanti sono gli impegni selezionati dall'utente,
	  *      viene creato un mandato di tipo competenza con tante righe (metodo creaMandatoRiga)
	  *      quanti sono gli impegni selezionati dall'utente. Con il metodo 'aggiornaImportoObbligazione'
	  *		 vengono incrementati gli importi (im_associato_doc_contabili)
	  *      degli impegni selezionati con l'importo trasferito nel mandato. Con il metodo 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
	  *      capitoli di competenza degli impegni selezionati.
	  *  creazione mandato automatico da impegno residuo
	  *    PreCondition:
	  *      E' stata generata la richiesta di creazione un Mandato automatico e l'utente ha selezionato solo
	  *      impegni residui
	  *    PostCondition:
	  *      Viene richiesto alla Component che gestisce i documenti amministrativi generici di creare un documento
	  *      generico di spesa (di tipo GENERICO_S) con tante righe quanti sono gli impegni selezionati dall'utente,
	  *      viene creato un mandato di tipo residuo con tante righe (metodo creaMandatoRiga)
	  *      quanti sono gli impegni selezionati dall'utente. Con il metodo 'aggiornaImportoObbligazione'
	  *		 vengono incrementati gli importi (im_associato_doc_contabili)
	  *      degli impegni selezionati con l'importo trasferito nel mandato. Con il metodo 'aggiornaCapitoloSaldoRiga' vengono aggiornati i saldi relativi ai
	  *      capitoli residui degli impegni selezionati. 
	  *  creazione di 2 mandati di trasferimento residuo+competenza
	  *    PreCondition:
	  *      E' stata generata la richiesta di creazione un Mandato di trasferimento e l'utente ha selezionato sia 
	  *      impegni residui che di competenza
	  *    PostCondition:
	  *      Vengono creati 2 mandati uno di competenza e uno residuo e sono da considerarsi valide entrambe le
	  *      postconditions: 'creazione mandato di trasferimento residuo' e 'creazione mandato di trasferimento competenza'
	  *
	  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il mandato da creare
	  *
	  * @return wizard il Mandato Automatico creato
	*/
	public OggettoBulk creaMandatoAutomatico (UserContext userContext,OggettoBulk bulk) throws ComponentException
	{
		MandatoAutomaticoWizardBulk wizard = (MandatoAutomaticoWizardBulk) bulk;
		if (wizard.isAutomatismoDaImpegni()) 
			return creaMandatoAutomaticoDaImpegni(userContext, wizard, wizard.getImpegniSelezionatiColl());
		else if (wizard.isAutomatismoDaDocumentiPassivi())
			return creaMandatoAutomaticoDaDocPassivi(userContext, wizard, wizard.getDocPassiviSelezionatiColl());
		return bulk;
	}

	private OggettoBulk creaMandatoAutomaticoDaImpegni (UserContext userContext, MandatoAutomaticoWizardBulk wizard, java.util.Collection impegniColl) throws ComponentException
	{
		try
		{
			MandatoBulk mandatoCompetenza = null, mandatoResiduo = null;
			Documento_genericoBulk docCompetenza = null, docResiduo = null;
			Mandato_rigaBulk mRiga;
			V_obbligazioneBulk impegno;
			
			SaldoComponentSession session = createSaldoComponentSession();

			for ( Iterator i = impegniColl.iterator(); i.hasNext(); )
			{
				impegno = (V_obbligazioneBulk) i.next();
					
				Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk)
		             ((Obbligazione_scadenzarioHome)getHome(userContext,Obbligazione_scadenzarioBulk.class)).findAndLock(new Obbligazione_scadenzarioBulk(impegno.getCd_cds(), impegno.getEsercizio(), impegno.getEsercizio_originale(), impegno.getPg_obbligazione(), impegno.getPg_obbligazione_scadenzario()));
	
				if (os.getIm_scadenza().compareTo(impegno.getIm_scadenza())!=0 ||
					os.getIm_associato_doc_amm().compareTo(impegno.getIm_associato_doc_amm())!= 0 ||
					os.getIm_associato_doc_contabile().compareTo(impegno.getIm_associato_doc_contabile()) !=0 )
					throw new ApplicationException("Operazione non possibile! E' stata utilizzata da un altro utente la scadenza nr." + impegno.getPg_obbligazione_scadenzario() + " dell'impegno " + impegno.getEsercizio_originale() + "/" + impegno.getPg_obbligazione());
					
				if (impegno.getIm_disponibile().compareTo(impegno.getIm_da_trasferire())>0) {
					createObbligazioneComponentSession().sdoppiaScadenzaInAutomatico( userContext, os, impegno.getIm_da_trasferire() );
				}
					
				if ( impegno.isCompetenza())
				{
					if (mandatoCompetenza == null )
					{
						mandatoCompetenza = creaMandatoAutomatico( userContext, wizard, mandatoCompetenza.TIPO_COMPETENZA );
						mandatoCompetenza.setMandato_terzo( creaMandatoTerzo( userContext, mandatoCompetenza, wizard.getMandato_terzo().getTerzo(), wizard.getMandato_terzo().getTipoBollo() ) );
						docCompetenza = docGenerico_creaDocumentoGenerico( userContext, wizard, mandatoCompetenza );
					}	
					mRiga = creaMandatoRiga( userContext, wizard, mandatoCompetenza, impegno, docCompetenza);
				}
				else //residuo
				{
					if (mandatoResiduo == null )
					{
						mandatoResiduo = creaMandatoAutomatico( userContext, wizard, mandatoResiduo.TIPO_RESIDUO);
						mandatoResiduo.setMandato_terzo( creaMandatoTerzo( userContext, mandatoResiduo, wizard.getMandato_terzo().getTerzo(), wizard.getMandato_terzo().getTipoBollo() ) );
						docResiduo = docGenerico_creaDocumentoGenerico( userContext, wizard, mandatoResiduo );
					}	
					mRiga = creaMandatoRiga( userContext, wizard, mandatoResiduo , impegno, docResiduo);
				}
				aggiornaCapitoloSaldoRiga( userContext, mRiga, session );		
			}
			if ( mandatoCompetenza != null )
			{
				mandatoCompetenza.refreshImporto();
				verificaMandato( userContext, mandatoCompetenza );
				aggiornaImportoObbligazioni(userContext, mandatoCompetenza );								
				super.creaConBulk( userContext, mandatoCompetenza);
				aggiornaStatoFattura( userContext, mandatoCompetenza, INSERIMENTO_MANDATO_ACTION );
				wizard.getMandatiColl().add( mandatoCompetenza );
			}	
			if ( mandatoResiduo != null )
			{
				mandatoResiduo.refreshImporto();
				verificaMandato( userContext, mandatoResiduo );
				aggiornaImportoObbligazioni(userContext, mandatoResiduo );												
				super.creaConBulk( userContext, mandatoResiduo);
				aggiornaStatoFattura( userContext, mandatoResiduo, INSERIMENTO_MANDATO_ACTION );				
				wizard.getMandatiColl().add( mandatoResiduo );
			}		
			return wizard;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}
	}

	private OggettoBulk creaMandatoAutomaticoDaDocPassivi(UserContext userContext, MandatoAutomaticoWizardBulk wizard, java.util.Collection docPassiviColl) throws ComponentException
	{
		try
		{
			MandatoBulk mandatoCompetenza = null, mandatoResiduo = null;
			Mandato_rigaBulk mRiga;
			V_doc_passivo_obbligazione_wizardBulk docPassivo = null, docTerzo = null;
			List docPassiviCompetenzaColl = new ArrayList();
			List docPassiviResiduiColl = new ArrayList();
			List docTerziSelezionatiColl = new ArrayList();
			
			SaldoComponentSession session = createSaldoComponentSession();

			boolean trovato = false;
			for ( Iterator i = docPassiviColl.iterator(); i.hasNext(); )
			{
				docPassivo = (V_doc_passivo_obbligazione_wizardBulk)i.next();
				trovato = false;

				for ( Iterator j = docTerziSelezionatiColl.iterator(); j.hasNext(); ) 
				{
					docTerzo = (V_doc_passivo_obbligazione_wizardBulk)j.next();
					if (docTerzo.getCd_terzo().equals(docPassivo.getCd_terzo()) &&
						docTerzo.getCd_modalita_pag().equals(docPassivo.getCd_modalita_pag()) &&
						docTerzo.getPg_banca().equals(docPassivo.getPg_banca()))
					{
						trovato = true;
						break;
					}
				}
				if (!trovato) 
				{
					docTerziSelezionatiColl.add(docPassivo);
				}
			}

			for ( Iterator i = docTerziSelezionatiColl.iterator(); i.hasNext(); ) {
				docTerzo = (V_doc_passivo_obbligazione_wizardBulk)i.next();
				
				docPassiviCompetenzaColl.clear();
				docPassiviResiduiColl.clear();
				mandatoCompetenza = null;
				mandatoResiduo = null;

				for ( Iterator j = docPassiviColl.iterator(); j.hasNext(); )
				{
					docPassivo = (V_doc_passivo_obbligazione_wizardBulk) j.next();

					if (docPassivo.getCd_terzo().equals(docTerzo.getCd_terzo()) &&
						docPassivo.getCd_modalita_pag().equals(docTerzo.getCd_modalita_pag()) &&
						docPassivo.getPg_banca().equals(docTerzo.getPg_banca()))
					{
						Obbligazione_scadenzarioBulk os = (Obbligazione_scadenzarioBulk)
			              ((Obbligazione_scadenzarioHome)getHome(userContext,Obbligazione_scadenzarioBulk.class)).findAndLock(new Obbligazione_scadenzarioBulk(docPassivo.getCd_cds(), docPassivo.getEsercizio(), docPassivo.getEsercizio_ori_obbligazione(), docPassivo.getPg_obbligazione(), docPassivo.getPg_obbligazione_scadenzario()));
		
						if (os.getIm_scadenza().compareTo(docPassivo.getIm_scadenza())!=0 ||
							os.getIm_associato_doc_contabile().compareTo(docPassivo.getIm_associato_doc_contabile()) !=0 )
							throw new ApplicationException("Operazione non possibile! E' stata utilizzata da un altro utente la scadenza nr." + docPassivo.getPg_obbligazione_scadenzario() + " dell'impegno " + docPassivo.getEsercizio_ori_obbligazione() + "/" + docPassivo.getPg_obbligazione());
							
						if ( docPassivo.isCompetenza())
							docPassiviCompetenzaColl.add(docPassivo);
						else
							docPassiviResiduiColl.add(docPassivo);
					}
				}
				if ( !docPassiviCompetenzaColl.isEmpty() )
				{
					mandatoCompetenza = creaMandatoAutomatico( userContext, wizard, mandatoCompetenza.TIPO_COMPETENZA );
					mandatoCompetenza.setMandato_terzo( creaMandatoTerzo( userContext, mandatoCompetenza, cercaTerzo(userContext, docPassivo.getCd_terzo()), wizard.getMandato_terzo().getTipoBollo() ) );
					mandatoCompetenza = aggiungiDocPassivi(userContext, mandatoCompetenza, docPassiviCompetenzaColl );
				}	
				if ( !docPassiviResiduiColl.isEmpty() )
				{
					mandatoResiduo = creaMandatoAutomatico( userContext, wizard, mandatoResiduo.TIPO_RESIDUO );
					mandatoResiduo.setMandato_terzo( creaMandatoTerzo( userContext, mandatoResiduo, cercaTerzo(userContext, docPassivo.getCd_terzo()), wizard.getMandato_terzo().getTipoBollo() ) );
					mandatoResiduo = aggiungiDocPassivi(userContext, mandatoResiduo, docPassiviResiduiColl );
				}	
				if ( mandatoCompetenza != null )
				{
					mandatoCompetenza.refreshImporto();
					verificaMandato( userContext, mandatoCompetenza );
					aggiornaImportoObbligazioni(userContext, mandatoCompetenza );								
					super.creaConBulk( userContext, mandatoCompetenza);
					aggiornaStatoFattura( userContext, mandatoCompetenza, INSERIMENTO_MANDATO_ACTION );
					wizard.getMandatiColl().add( mandatoCompetenza );
				}	
				if ( mandatoResiduo != null )
				{
					mandatoResiduo.refreshImporto();
					verificaMandato( userContext, mandatoResiduo );
					aggiornaImportoObbligazioni(userContext, mandatoResiduo );												
					super.creaConBulk( userContext, mandatoResiduo);
					aggiornaStatoFattura( userContext, mandatoResiduo, INSERIMENTO_MANDATO_ACTION );				
					wizard.getMandatiColl().add( mandatoResiduo );
				}		
			}
			return wizard;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	
	/**
	 *  creazione mandato automatico da impegno
	 *    PreCondition:
	 *      E' stata generata la richiesta di creazione un Mandato di accreditamento
	 *    PostCondition:
	 *      Viene creato un mandato di tipo Accreditamento con CDS/UO = Ente e CDS/UO origine = CDS/UO di scrivania.
	 *      Viene creato un mandatoTerzo coi dati relativi al terzo CDS verso cui il mandato e' emesso
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param wizard <code>MandatoAccreditamentoWizardBulk</code> il mandato di accreditamento
	 * @param ti_competenza_residuo tipologia (competenza/residuo) del mandato da creare
	 *
	 * @return mandato <code>MandatoAccreditamentoBulk</code> il mandato di regolarizzazione creato
	 */
	private MandatoBulk creaMandatoAutomatico (UserContext userContext, MandatoAutomaticoWizardBulk wizard, String ti_competenza_residuo ) throws ComponentException
	{
		try
		{
			MandatoIBulk mandato = new MandatoIBulk();
			mandato.setToBeCreated();
			mandato.setUser( wizard.getUser() );
			mandato.setEsercizio( wizard.getEsercizio());
			mandato.setCds( wizard.getCds());
			mandato.setUnita_organizzativa( wizard.getUnita_organizzativa());
			mandato.setCd_cds_origine( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_cds());
			mandato.setCd_uo_origine( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getCd_unita_organizzativa());
			mandato.setStato( wizard.STATO_MANDATO_EMESSO);
			mandato.setDt_emissione( wizard.getDt_emissione());
			mandato.setIm_mandato(new BigDecimal(0));
			mandato.setIm_pagato(new BigDecimal(0));

			if (wizard.isAutomatismoDaImpegni())
				mandato.setDs_mandato("Mandato automatico da impegno");
			else
				mandato.setDs_mandato("Mandato automatico da documenti passivi");

			mandato.setTi_mandato(wizard.TIPO_PAGAMENTO);
			mandato.setCd_tipo_documento_cont( Numerazione_doc_contBulk.TIPO_MAN);
			mandato.setTi_competenza_residuo( ti_competenza_residuo );
			mandato.setStato_trasmissione( wizard.STATO_TRASMISSIONE_NON_INSERITO);
			mandato.setStato_coge( wizard.STATO_COGE_N);
			mandato.setIm_ritenute( new java.math.BigDecimal(0));

			return mandato;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	
	/**
	 *  creazione riga di mandato da Mandato automatico da impegni
	 *    PreCondition:
	 *      E' stata generata la richiesta di creazione di una riga di Mandato
	 *    PostCondition:
	 *      Viene creata una riga di mandato coi dati relativi all'impegno selezionato dall'utente e
	 *      al documento generico di spesa (GENERICO_S) creato in automatico alla crezione del mandato
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param wizard <code>MandatoAutomaticoBulk</code> il mandato di accreditamento
	 * @param mandato <code>MandatoBulk</code> il mandato
	 * @param impegno <code>V_obbligazioneBulk</code> l'impegno selezionato dall'utente
	 * @param documento <code>Documento_genericoBulk</code> il documento generico di spesa
	 *
	 * @return riga <code>Mandato_rigaBulk</code> la riga di mandato creata
	 */
	private Mandato_rigaBulk creaMandatoRiga (UserContext userContext, MandatoAutomaticoWizardBulk wizard, MandatoBulk mandato, V_obbligazioneBulk impegno, Documento_genericoBulk documento ) throws ComponentException
	{
		try
		{
			Mandato_rigaBulk riga = new Mandato_rigaIBulk();
			riga.setToBeCreated();
			riga.setUser( mandato.getUser() );
			riga.setStato( riga.STATO_INIZIALE);
			riga.setIm_mandato_riga( impegno.getIm_da_trasferire());
			riga.setIm_ritenute_riga( new java.math.BigDecimal(0));
			riga.setMandato( mandato );
//			riga.setImpegno( impegno );
			
			riga.setFl_pgiro( new Boolean( false));
			riga.setEsercizio_obbligazione( impegno.getEsercizio());
			riga.setEsercizio_ori_obbligazione( impegno.getEsercizio_originale());
			riga.setPg_obbligazione( impegno.getPg_obbligazione());
			riga.setPg_obbligazione_scadenzario( impegno.getPg_obbligazione_scadenzario());

			riga.setEsercizio_doc_amm( documento.getEsercizio());
			riga.setCd_cds_doc_amm( documento.getCd_cds());
			riga.setCd_uo_doc_amm( documento.getCd_unita_organizzativa());
			riga.setPg_doc_amm( documento.getPg_documento_generico());
			riga.setCd_tipo_documento_amm( documento.getCd_tipo_documento_amm());
			riga.setPg_ver_rec_doc_amm( documento.getPg_ver_rec());
			
			riga.setBancaOptions( wizard.getBancaOptions());
			riga.setBanca( wizard.getBanca());
		
			riga.setModalita_pagamentoOptions( wizard.getModalita_pagamentoOptions());
			riga.setModalita_pagamento( wizard.getModalita_pagamento());

			mandato.addToMandato_rigaColl( riga );		
			return riga;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	
	/**
	 *  creazione mandato terzo
	 *    PreCondition:
	 *      E' stata generata la richiesta di creazione di un Mandato_terzoBulk
	 *    PostCondition:
	 *      Viene creata una istanza di Mandato_terzoBulk coi dati del beneficiario del mandato e viene impostato
	 *      il tipo bollo di default.
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param mandato <code>MandatoAutomaticoBulk</code> il mandato
	 * @param cd_terzo Il codice del beneficiario del mandato
	 *
	 * @return mTerzo l'istanza di <code>Mandato_terzoBulk</code> creata
	 */
	private Mandato_terzoBulk creaMandatoTerzo (UserContext userContext, MandatoBulk mandato, TerzoBulk terzo, Tipo_bolloBulk bollo ) throws ComponentException
	{
		Mandato_terzoBulk mTerzo = new Mandato_terzoIBulk();

		if (!(mandato instanceof MandatoAutomaticoWizardBulk))
			mTerzo.setToBeCreated();
		
		mTerzo.setUser( mandato.getUser() );	
		mTerzo.setMandato( mandato );
				
		//imposto il terzo
		mTerzo.setTerzo( terzo );

		//imposto il tipo bollo di default
		mTerzo.setTipoBollo( bollo );
		return mTerzo;
	}

	private Mandato_terzoBulk creaMandatoTerzo (UserContext userContext, MandatoBulk mandato, TerzoBulk terzo ) throws ComponentException
	{
		try
		{
			return creaMandatoTerzo(userContext, mandato, terzo, ((Tipo_bolloHome)getHome( userContext, Tipo_bolloBulk.class )).findTipoBolloDefault(Tipo_bolloBulk.TIPO_SPESA));
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	
	/** 
	 *  creazione documento amm.generico per mandato automatico
	 *    PreCondition:
	 *      E' stata generata la richiesta di creazione di un documento generico di spesa di tipo TRASF_S a partire
	 *      da un mandato automatico
	 *    PostCondition:
	 *      Un documento viene creato con un numero di righe pari al numero di impegni selezionati dall'utente di tipo
	 *      uguale al tipo del mandato(competenza o residuo)
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param mandato <code>MandatoAutomaticoWizardBulk</code> il mandato automatico
	 * @param impegni La collezione di impegni selezionati dall'utente
	 *
	 * @return documento <code>Documento_genericoBulk</code> il documento generico di spesa creato
	*/
	public Documento_genericoBulk docGenerico_creaDocumentoGenerico (UserContext userContext, MandatoAutomaticoWizardBulk wizard, MandatoBulk mandato ) throws ComponentException
	{
		try
		{
			V_obbligazioneBulk impegno;
			Documento_generico_rigaBulk dRiga;
			Documento_genericoBulk documento = new Documento_genericoBulk();
			documento.setToBeCreated();
			documento.setUser( mandato.getUser() );
			documento.setTi_entrate_spese( documento.SPESE );
			documento.setEsercizio( mandato.getEsercizio());
			documento.setCd_cds( mandato.getCd_cds());
			documento.setCd_unita_organizzativa( mandato.getCd_unita_organizzativa());
			documento.setCd_cds_origine( mandato.getCd_cds_origine());
			documento.setCd_uo_origine( mandato.getCd_uo_origine());
			documento.setTipo_documento( new Tipo_documento_ammBulk( Numerazione_doc_ammBulk.TIPO_DOC_GENERICO_S));
			documento.setTi_istituz_commerc( wizard.getModelloDocumento().getTi_istituz_commerc());
			documento.setStato_cofi( documento.STATO_CONTABILIZZATO );
			documento.setStato_coge( documento.NON_REGISTRATO_IN_COGE);
			documento.setStato_coan(documento.NON_CONTABILIZZATO_IN_COAN);
			documento.setStato_pagamento_fondo_eco( "N");
			documento.setTi_associato_manrev("T");
			documento.setData_registrazione( mandato.getDt_emissione());
			documento.setDt_a_competenza_coge( wizard.getModelloDocumento().getDt_a_competenza_coge() );
			documento.setDt_da_competenza_coge( wizard.getModelloDocumento().getDt_da_competenza_coge() );
			documento.setDs_documento_generico( "DOCUMENTO ASSOCIATO A MANDATO AUTOMATICO DA IMPEGNO" );
			documento.setIm_totale( mandato.getIm_mandato());
			DivisaBulk divisa = new DivisaBulk( docGenerico_createConfigurazioneCnrComponentSession().getVal01(userContext, new Integer(0), "*", Configurazione_cnrBulk.PK_CD_DIVISA,Configurazione_cnrBulk.SK_EURO ));
			documento.setValuta( divisa );
			documento.setCambio( new BigDecimal(1));
			for ( Iterator i = wizard.getImpegniSelezionatiColl().iterator(); i.hasNext(); )
			{
				impegno = (V_obbligazioneBulk) i.next();
				if ( impegno.isCompetenza() && mandato.getTi_competenza_residuo().equals( mandato.TIPO_COMPETENZA))
					dRiga = docGenerico_creaDocumentoGenericoRiga( userContext, wizard, mandato, documento, impegno );
				else if ( !impegno.isCompetenza() && mandato.getTi_competenza_residuo().equals( mandato.TIPO_RESIDUO ))
					dRiga = docGenerico_creaDocumentoGenericoRiga( userContext, wizard, mandato, documento, impegno );				
			}

			documento = (Documento_genericoBulk) createDocumentoGenericoComponentSession().creaConBulk( userContext, documento );
//			documento.validate();
			return documento;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}

	/** 
	 *  creazione riga di documento amm.generico di spesa per mandato automatico da impegno
	 *    PreCondition:
	 *      E' stata generata la richiesta di creazione di una riga di documento generico di spesa di tipo TRASF_S 
	 *      a partire da un impegno selezionato dall'utente nel mandato di accreditamento
	 *    PostCondition:
	 *      Un riga di documento viene creata con i dati relativi al terzo (codice terzo, coordinate bancarie, modalità di 
	 *      pagamento) derivati da quelli che l'utente ha specificato nel mandato e i dati relativi all'importo derivati dall'impegno selezionato
	 *      dall'utente nel mandato; viene inoltre aggiornato l'importo associato ai documenti amministrativi della scadenza di obbligazione
	 *      che rappresenta l'impegno( scadenza.im_associato_doc_amm = scadenza.im_associato_doc_amm + documento_riga.im_riga)
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param documento <code>Documento_genericoBulk</code> il documento generico di spesa
	 * @param impegno <code>V_impegnoBulk</code> l'impegno selezionato dall'utente nel mandato di accreditamento
	 * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di accreditamento
	 *
	 * @return riga <code>Documento_generico_rigaBulk</code> la riga del documento generico di spesa creata
	*/

	public Documento_generico_rigaBulk docGenerico_creaDocumentoGenericoRiga (UserContext userContext, MandatoAutomaticoWizardBulk wizard, MandatoBulk mandato, Documento_genericoBulk documento, V_obbligazioneBulk impegno ) throws ComponentException
	{
		try
		{
			Documento_generico_rigaBulk riga = new Documento_generico_rigaBulk();
			riga.setToBeCreated();
			riga.setUser( documento.getUser() );
			riga.setCd_cds( documento.getCd_cds());
			riga.setCd_unita_organizzativa( documento.getCd_unita_organizzativa());
			riga.setCd_tipo_documento_amm( documento.getCd_tipo_documento_amm() );
			riga.setStato_cofi( riga.STATO_CONTABILIZZATO );
			riga.setDt_a_competenza_coge( documento.getData_registrazione());
			riga.setDt_da_competenza_coge( documento.getData_registrazione());
			riga.setTerzo( wizard.getMandato_terzo().getTerzo());      //CNR
			AnagraficoBulk anagrafico = (AnagraficoBulk) getHome( userContext, AnagraficoBulk.class ).findByPrimaryKey( riga.getTerzo().getAnagrafico());
			riga.getTerzo().setAnagrafico( anagrafico );
			riga.setRagione_sociale( anagrafico.getRagione_sociale());
			riga.setNome( anagrafico.getNome());
			riga.setCognome( anagrafico.getCognome());
			riga.setCodice_fiscale( anagrafico.getCodice_fiscale());
			riga.setPartita_iva( anagrafico.getPartita_iva());
			riga.setModalita_pagamento( wizard.getModalita_pagamento().getRif_modalita_pagamento());
			riga.setBanca( wizard.getBanca());
			riga.setIm_riga( impegno.getIm_da_trasferire());
			riga.setIm_riga_divisa( impegno.getIm_da_trasferire());
			riga.setTi_associato_manrev("T");		
			Obbligazione_scadenzarioBulk scadenza = (Obbligazione_scadenzarioBulk) getHome( userContext, Obbligazione_scadenzarioBulk.class ).findByPrimaryKey( new Obbligazione_scadenzarioBulk( impegno.getCd_cds(), impegno.getEsercizio(), impegno.getEsercizio_originale(), impegno.getPg_obbligazione(), impegno.getPg_obbligazione_scadenzario()));
			getHomeCache(userContext).fetchAll(userContext);
			riga.setObbligazione_scadenziario( scadenza );
			riga.setDocumento_generico( documento );
			documento.getDocumento_generico_dettColl().add(  riga );

			//aggiorno im_assciato_doc_amm della scadenza
			lockBulk( userContext, scadenza.getObbligazione());

			scadenza.setIm_associato_doc_amm( scadenza.getIm_associato_doc_amm().add(riga.getIm_riga()));
			updateBulk( userContext, scadenza );

			return riga;
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
	/** 
	 *  lista le coordinate bancarie 
	 *    PreCondition:
	 *      E' stato creata una riga di mandato di trasferimento
	 *    PostCondition:
	 *     La lista delle coordinate bancarie del terzo beneficiario del mandato viene estratta
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param mandato <code>MandatoAutomaticoWizardBulk</code> il mandato da creare
	 *
	 * @return result la lista delle banche definite per il terzo beneficiario del mandato
	 *			null non è stata definita nessuna banca per il terzo beneficiario del mandato
	*/

	public List findBancaOptions (UserContext userContext,MandatoAutomaticoWizardBulk mandato) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
	{
		if ( mandato.getMandato_terzo() != null )
		{
			if ( mandato.getModalita_pagamentoOptions() != null && mandato.getModalita_pagamento().getCd_modalita_pag() == null)
				mandato.setModalita_pagamento( (Modalita_pagamentoBulk) mandato.getModalita_pagamentoOptions().get(0));

			SQLBuilder sql = getHome( userContext, BancaBulk.class ).createSQLBuilder();
			sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getMandato_terzo().getCd_terzo() );
			sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", sql.ISNULL, null);
			sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", sql.EQUALS, "N");
			sql.addOrderBy("FL_CC_CDS DESC");		
			if (mandato.getModalita_pagamento() != null && mandato.getModalita_pagamento().getCd_modalita_pag() != null )
			{
				SQLBuilder sql2 = getHome( userContext, Modalita_pagamentoBulk.class ).createSQLBuilder();
				sql2.setHeader( "SELECT DISTINCT TI_PAGAMENTO " );
				sql2.addTableToHeader( "rif_modalita_pagamento" );
				sql2.addSQLClause( "AND" , "modalita_pagamento.cd_terzo", sql.EQUALS, mandato.getMandato_terzo().getCd_terzo() );
				sql2.addSQLClause( "AND" , "modalita_pagamento.cd_modalita_pag", sql.EQUALS, mandato.getModalita_pagamento().getCd_modalita_pag() );
				sql2.addSQLJoin( "modalita_pagamento.cd_modalita_pag", "rif_modalita_pagamento.cd_modalita_pag" );
				sql2.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", sql.ISNULL, null);
				
				sql.addSQLClause( "AND", "TI_PAGAMENTO" , sql.EQUALS, sql2 );
			}	
			List result = getHome( userContext, BancaBulk.class ).fetchAll( sql );
			if ( result.size() == 0 )
				throw new ApplicationException("Non esistono coordinate bancarie per il terzo " + mandato.getMandato_terzo().getCd_terzo());
			return result;	
		}
		else
			return null;	
	}
	/** 
	 *  lista le modalità di pagamento
	 *    PreCondition:
	 *      E' stato creata una riga di mandato  di trasferimento
	 *    PostCondition:
	 *     La lista delle modalità di pagamento del terzo beneficiario, tutte appartenenti alla stessa classe (Bancario/Postale/..) per cui si sta emettendo il mandato,
	 *     viene estratta.Vengono escluse le modalità di pagamento riferite a terzi cessionari
	 *
	 * @param userContext lo <code>UserContext</code> che ha generato la richiesta
	 * @param mandato <code>MandatoAccreditamentoBulk</code> il mandato di trasferimento
	 *
	 * @return result la lista delle modalità di pagamento definite per il terzo beneficiario del mandato
	 *			null non è stata definita nessuna modalità di pagamento per il terzo beneficiario del mandato
	*/

	public List findModalita_pagamentoOptions (UserContext userContext, MandatoAutomaticoWizardBulk mandato) throws it.cnr.jada.persistency.PersistencyException, it.cnr.jada.persistency.IntrospectionException, ComponentException
	{
		if ( mandato.getMandato_terzo() != null )
		{
			SQLBuilder sql = getHome( userContext, Modalita_pagamentoBulk.class ).createSQLBuilder();
			sql.addTableToHeader( "RIF_MODALITA_PAGAMENTO");
			sql.addSQLJoin("MODALITA_PAGAMENTO.CD_MODALITA_PAG","RIF_MODALITA_PAGAMENTO.CD_MODALITA_PAG" );
			sql.addClause( "AND", "cd_terzo", sql.EQUALS, mandato.getMandato_terzo().getCd_terzo() );
			sql.addClause( "AND", "cd_terzo_delegato", sql.ISNULL, null );			
			List result =  getHome( userContext, Modalita_pagamentoBulk.class ).fetchAll( sql );
			if ( result.size() == 0 )
				throw new ApplicationException("Non esistono modalità di pagamento per il terzo " + mandato.getMandato_terzo().getCd_terzo());
			return result;	

		}
		else
			return null;	
	}
	/** 
	  *  inizializzazione di una istanza di MandatoBulk
	  *    PreCondition:
	  *     E' stata richiesta l'inizializzazione di una istanza di MandatoBulk
	  *    PostCondition:
	  *     Viene impostata la data di emissione del mandato con la data del Server
	  *  inizializzazione di una istanza di MandatoAutomaticoWizardBulk
	  *    PreCondition:
	  *     E' stata richiesta l'inizializzazione di una istanza di MandatoAutomaticoWizardBulk, l'oggetto bulk
	  *     utilizzato come wizard per la generazione dei mandati automatici da impegno
	  *    PostCondition:
	  *     Viene impostata la data di emissione del wizard con la data del Server, il Cds e l'UO di appartenenza con 
	  *     il Cds e l'UO collegato, il mandato terzo con il codice terzo che corrisponde al codice terzo 
	  *     del mandato automatico (metodo creaMandatoTerzo), viene impostata la lista degli impegni
	  *     (metodo listaImpegniCNR) del CNR
	  *
	  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
	  * @param bulk <code>OggettoBulk</code> il mandato da inizializzare per l'inserimento
	  *
	  * @return bulk <code>OggettoBulk</code> il Mandato inizializzato per l'inserimento
	  *     
	*/
	public OggettoBulk inizializzaBulkPerInserimento (UserContext aUC,OggettoBulk bulk) throws ComponentException
	{
		try
		{
			MandatoAutomaticoWizardBulk mandato = (MandatoAutomaticoWizardBulk) bulk;
			mandato.setCds( (CdsBulk)getHome( aUC, CdsBulk.class).findByPrimaryKey( new CdsBulk(CNRUserContext.getCd_cds(aUC))));
			mandato.setUnita_organizzativa( (Unita_organizzativaBulk)getHome( aUC, Unita_organizzativaBulk.class).findByPrimaryKey( new Unita_organizzativaBulk(CNRUserContext.getCd_unita_organizzativa(aUC))));

			bulk = super.inizializzaBulkPerInserimento( aUC, bulk );			

			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}	
	}

	public OggettoBulk inizializzaMappaAutomatismo (UserContext aUC, OggettoBulk bulk) throws ComponentException
	{
		try
		{
			MandatoAutomaticoWizardBulk mandato = (MandatoAutomaticoWizardBulk)bulk;
			mandato.getMandatiColl().clear();
			if (mandato.isAutomatismoDaImpegni()) { 
				if (mandato.getFind_doc_passivi().getCd_terzo() == null &&
				    mandato.getFind_doc_passivi().getCd_precedente() == null && 			
					mandato.getFind_doc_passivi().getCognome() == null &&
					mandato.getFind_doc_passivi().getRagione_sociale() == null &&
					mandato.getFind_doc_passivi().getNome()  == null &&
					mandato.getFind_doc_passivi().getPartita_iva()  == null &&
					mandato.getFind_doc_passivi().getCodice_fiscale()  == null)
					throw new ApplicationException( "Attenzione! Deve essere specificato almeno un campo dell'anagrafica." );
				else 
				{		
					mandato.getImpegniSelezionatiColl().clear();
					mandato.getImpegniColl().clear();
					mandato.setMandato_terzo( creaMandatoTerzo( aUC, mandato, cercaTerzo( aUC, mandato ) ) );
					mandato = listaImpegniTerzo( aUC, mandato );
					mandato.setModalita_pagamento((Modalita_pagamentoBulk)findModalita_pagamentoOptions( aUC, mandato).get(0));
					mandato.setBanca((BancaBulk)findBancaOptions(aUC, mandato).get(0));
					mandato.getModelloDocumento().setDt_da_competenza_coge(mandato.getDt_emissione());
					mandato.getModelloDocumento().setDt_a_competenza_coge(mandato.getDt_emissione());
					initializeKeysAndOptionsInto(aUC, bulk);
				}
			}
			else if (mandato.isAutomatismoDaDocumentiPassivi()) 
			{
				mandato.getDocPassiviColl().clear();

				/*
				 * Necessario per caricare il bollo di default 
				 */
				mandato.setMandato_terzo( creaMandatoTerzo( aUC, mandato, null ) );
				bulk = listaDocPassivi( aUC, mandato );
			}

			return bulk;
		}
		catch ( Exception e )
		{
			throw handleException( bulk, e );
		}	
	}

	/** 
	 *  ricerca impegni Terzo
	 *    PreCondition:
	 *     E' stata richiesta la ricerca degli impegni del Terzo per emettere un mandato di pagamento
	 *    PostCondition:
	 *     Vengono ricercati tutti gli impegni che hanno un importo disponibile ( importo disponibile = importo iniziale
	 *     dell'impegno - importo già associato ai documenti contabili) e il cui Terzo sia quello indicato
	 *
	 * @param aUC lo <code>UserContext</code> che ha generato la richiesta
	 * @param mandato <code>MandatoBulk</code> il mandato di pagamento
	 *
	 * @return mandato il Mandato automatico emesso dopo la ricerca degli impegni del CNR
	 * 
	*/
	public MandatoAutomaticoWizardBulk listaImpegniTerzo (UserContext aUC, MandatoAutomaticoWizardBulk mandato) throws ComponentException
	{
		try
		{
			Collection result = ((MandatoAutomaticoWizardHome)getHome( aUC, mandato.getClass())).findImpegni( mandato );
			mandato.setImpegniColl(result);
			int size = mandato.getImpegniColl().size();
			if ( size == 0 )
				throw new ApplicationException( "La ricerca degli Impegni non ha fornito alcun risultato.");
			for ( Iterator i = mandato.getImpegniColl().iterator(); i.hasNext(); )
				((V_obbligazioneBulk) i.next()).setNrImpegni(size );
			return mandato;
		}
		catch ( it.cnr.jada.persistency.PersistencyException e )
		{
			throw handleException( mandato, e );
		}
		catch ( it.cnr.jada.persistency.IntrospectionException e )
		{
			throw handleException( mandato, e );
		}

	}

	private TerzoBulk cercaTerzo (UserContext aUC, MandatoAutomaticoWizardBulk wizard) throws ComponentException
	{
		try
		{
			Collection result = ((MandatoAutomaticoWizardHome)getHome( aUC, wizard.getClass())).findTerzi( wizard );
			if ( result.size() == 0 )
				throw new ApplicationException( "La ricerca dei Terzi non ha fornito alcun risultato.");
			if ( result.size() > 1 )
				throw new ApplicationException( "Esiste piu' di un terzo che soddisfa i criteri di ricerca.");

			return (TerzoBulk)getHome( aUC, TerzoBulk.class).findByPrimaryKey( new TerzoBulk(((V_anagrafico_terzoBulk)result.iterator().next()).getCd_terzo()));				
		}
		catch ( it.cnr.jada.persistency.PersistencyException e )
		{
			throw handleException( wizard, e );
		}
		catch ( it.cnr.jada.persistency.IntrospectionException e )
		{
			throw handleException( wizard, e );
		}
	}
	private TerzoBulk cercaTerzo (UserContext aUC, Integer cd_terzo) throws ComponentException
	{
		try
		{
			return (TerzoBulk)getHome( aUC, TerzoBulk.class).findByPrimaryKey( new TerzoBulk(cd_terzo));				
		}
		catch ( it.cnr.jada.persistency.PersistencyException e )
		{
			throw handleException( e );
		}
	}
	protected Mandato_rigaBulk creaMandatoRiga (UserContext userContext, MandatoBulk mandato, V_doc_passivo_obbligazioneBulk docPassivo ) throws ComponentException
	{
		Mandato_rigaBulk riga = super.creaMandatoRiga(userContext, mandato, docPassivo);
		if (docPassivo instanceof V_doc_passivo_obbligazione_wizardBulk)
			((V_doc_passivo_obbligazione_wizardBulk)docPassivo).setMandatoRiga(riga);
		return riga;	
	}
}
