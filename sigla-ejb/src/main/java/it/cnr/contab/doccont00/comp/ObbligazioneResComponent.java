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
 * Created on Feb 6, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Optional;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.contratto.bulk.ContrattoBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.util.ApplicationMessageFormatException;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.util.DateUtils;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ObbligazioneResComponent extends ObbligazioneComponent {
	public static final String TIPO_ERRORE_ECCEZIONE = "E"; // solleva eccezione
	public static final String TIPO_ERRORE_CONTROLLO = "C"; // effettua solo controllo
	
	public  ObbligazioneResComponent()
	{
		/*Default constructor*/
	}
	public String controllaDettagliScadenzaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione,Obbligazione_scadenzarioBulk scadenzario) throws ComponentException
	{
		StringBuffer errControllo = new StringBuffer();
		//obbligazione = generaDettagliScadenzaObbligazione (aUC, obbligazione, scadenzario, false);
		obbligazione = calcolaPercentualeImputazioneObbligazione( aUC, obbligazione, TIPO_ERRORE_CONTROLLO, errControllo);

		// per evitare messaggi successivi alla modifica fa le seguenti verifiche
		//verifica la correttezza dell'obbligazione
		verificaObbligazione( aUC, obbligazione );

		//verifica la correttezza dell'imputazione finanziaria
		validaImputazioneFinanziaria( aUC, obbligazione );

		return errControllo.toString();
	}
	
	protected ObbligazioneBulk generaDettagliScadenzaObbligazione (UserContext aUC,ObbligazioneBulk obbligazione,Obbligazione_scadenzarioBulk scadenzario, boolean allineaImputazioneFinanziaria) throws ComponentException
	{
		Obbligazione_scadenzarioBulk os;

		// non e' ancora stata selezionata l'imputazione finanziaria
		if (obbligazione.getLineeAttivitaSelezionateColl().size() == 0 &&
			obbligazione.getNuoveLineeAttivitaColl().size() == 0)
			return obbligazione;

		// non sono ancora state inserite le scadenze
		if (obbligazione.getObbligazione_scadenzarioColl().size() == 0 )
			return obbligazione;
	
		if ( scadenzario != null ) // una sola scadenza e' stata modificata
		{
			//creo i dettagli della scadenza per le linee attivita da PDG
			creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, obbligazione, scadenzario );

			//creo i dettagli della scadenza per le nuove linee attivita 		
			creaDettagliScadenzaPerNuoveLineeAttivita( aUC, obbligazione, scadenzario );
		}	// imputazione finanziaria e' stata modificata, quindi rigenero i dettagli per tutte le scadenze
		else
		{
			
			// per ogni scadenza aggiorno i suoi dettagli in base alle linee di attività specificate dall'utente
			for ( Iterator scadIterator = obbligazione.getObbligazione_scadenzarioColl().iterator(); scadIterator.hasNext(); )
			{
					os = (Obbligazione_scadenzarioBulk) scadIterator.next();		
					//cancello i dettagli della scadenza per le linee attivita che non esistono piu'
					cancellaDettaglioScadenze( aUC, obbligazione, os );

					//creo i dettagli della scadenza per le linee attivita da PDG
					creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, obbligazione, os );

					//creo i dettagli della scadenza per le nuove linee attivita 		
					creaDettagliScadenzaPerNuoveLineeAttivita( aUC, obbligazione, os );
			}
		}
	
		if (allineaImputazioneFinanziaria) obbligazione = calcolaPercentualeImputazioneObbligazione( aUC, obbligazione );

		return obbligazione;
	}

	protected ObbligazioneBulk calcolaPercentualeImputazioneObbligazione (UserContext aUC,ObbligazioneBulk obbligazione) throws ComponentException
	{
		StringBuffer errControllo = new StringBuffer();
		return calcolaPercentualeImputazioneObbligazione( aUC, obbligazione, TIPO_ERRORE_ECCEZIONE, errControllo);
	}

	private ObbligazioneBulk calcolaPercentualeImputazioneObbligazione (UserContext aUC,ObbligazioneBulk obbligazione, String tipoErrore, StringBuffer errControllo) throws ComponentException {
	try {
		BigDecimal percentuale = new BigDecimal( 100);
		BigDecimal totaleScad = new BigDecimal(0);
		BigDecimal diffScad = new BigDecimal(0);
		Obbligazione_scad_voceBulk osv;
		Obbligazione_scadenzarioBulk os;
		Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();

		boolean cdsModObblResImporto = isCdsModObblResImporto(aUC, obbligazione.getCd_cds());

		// recupero le percentuali di imputazione finanziaria per le linee di attivita da pdg
		// 100 - percentuali specificate x linee att non da PDG
		PrimaryKeyHashtable prcImputazioneFinanziariaTable = getOldRipartizioneCdrVoceLinea(aUC, obbligazione); 

		if ( obbligazione.getFl_calcolo_automatico().booleanValue() ) {
			if (obbligazione.getIm_obbligazione()==null||obbligazione.getIm_obbligazione().compareTo(Utility.ZERO)<=0)
				throw new ApplicationException( "Non è possibile effettuare il calcolo automatico su una obbligazione con importo nullo.");

			for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
			{
				key = (Obbligazione_scad_voceBulk)e.nextElement();
				totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );			
				percentuale = totaleScad.multiply(new BigDecimal(100)).divide(obbligazione.getIm_obbligazione(), 100, BigDecimal.ROUND_HALF_UP);
	
				for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
				{
					os = (Obbligazione_scadenzarioBulk) s.next();
					for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
					{
						osv = (Obbligazione_scad_voceBulk) d.next();
						// totale per Cdr e per scadenza				
						if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
							key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
							key.getCd_voce().equals(osv.getCd_voce())) {
							osv.setToBeUpdated(); 
							osv.setIm_voce(osv.getObbligazione_scadenzario().getIm_scadenza().multiply(percentuale).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
							break;
						}
					}
				}
			}
		}

		boolean trovato = false;

		//devo controllare che tutto è quadrato vecchio e nuovo
		//verifico che nella nuova Obbligazione non ci siano linee o voci nuove rispetto alla precedente
		for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
		{
			os = (Obbligazione_scadenzarioBulk) s.next();
			for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
			{
				osv = (Obbligazione_scad_voceBulk) d.next();
				trovato=false;
				for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
				{			
					key = (Obbligazione_scad_voceBulk)e.nextElement();
					if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
						key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
						key.getCd_voce().equals(osv.getCd_voce())) {
						trovato=true;						
						break;
					}
				}
				if (!trovato)
					throw new ApplicationException( "Non è possibile aggiungere nuove linee di attività/voci (" + osv.getCd_linea_attivita() + "/" + osv.getCd_voce() + ") ai residui propri.");
			}
		}
		//verifico che nella nuova Obbligazione alle linee/voci siano stati assegnati gli stessi importi
		for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
		{
			key = (Obbligazione_scad_voceBulk)e.nextElement();
			totaleScad = new BigDecimal(0);

			for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext(); )
			{
				os = (Obbligazione_scadenzarioBulk) s.next();
				for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext(); )
				{
					osv = (Obbligazione_scad_voceBulk) d.next();
					// totale per Cdr e per scadenza				
					if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
						key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
						key.getCd_voce().equals(osv.getCd_voce())) {
						totaleScad = totaleScad.add(Utility.nvl(osv.getIm_voce())); 						
					}
				}
			}

			if (totaleScad.compareTo((BigDecimal) prcImputazioneFinanziariaTable.get( key ))!=0) {
				//se modifico l'importo del residuo devo controllare che non sia bloccata la creazione/modifica del residuo se attiva la gestione del limite sui residui sia sul CDS che sulla voce e per la natura e tipo
				//finanziamento indicato in CONFIGURAZIONE_CNR
				WorkpackageBulk latt = ((WorkpackageHome)getHome(aUC, WorkpackageBulk.class)).searchGAECompleta(aUC,CNRUserContext.getEsercizio(aUC),
						key.getCd_centro_responsabilita(), key.getCd_linea_attivita());

				if (!UtenteBulk.isAbilitatoSbloccoImpegni(aUC))
					Utility.createSaldoComponentSession().checkBloccoImpegniNatfin(aUC, latt, obbligazione.getElemento_voce(), obbligazione.isObbligazioneResiduo()?ObbligazioneBulk.TIPO_RESIDUO_PROPRIO:ObbligazioneBulk.TIPO_RESIDUO_IMPROPRIO);

				//se aumento l'importo del residuo devo controllare che il progetto non sia scaduto
				if (totaleScad.compareTo((BigDecimal)prcImputazioneFinanziariaTable.get( key ))>0 &&
					Utility.createParametriEnteComponentSession().isProgettoPianoEconomicoEnabled(aUC, CNRUserContext.getEsercizio(aUC))) {
					ProgettoBulk progetto = latt.getProgetto();
					Optional.ofNullable(progetto.getOtherField())
							.filter(el->el.isStatoApprovato()||el.isStatoChiuso())
							.orElseThrow(()->new ApplicationException("Attenzione! Aumento importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
									+ "Il progetto associato "+progetto.getCd_progetto()+" non risulta in stato Approvato o Chiuso."));
					if (progetto.isDatePianoEconomicoRequired()) {
						//Negli impegni controllare la più piccola data tra data inizio progetto e data stipula contratto definitivo
						ProgettoHome progettoHome = (ProgettoHome)getHome(aUC, ProgettoBulk.class);
						java.util.Collection<ContrattoBulk> contratti = progettoHome.findContratti(progetto.getPg_progetto());

						Optional<ContrattoBulk> optContratto = 
								contratti.stream().filter(el->el.isAttivo()||el.isAttivo_e_Passivo())
								 .min((p1, p2) -> p1.getDt_stipula().compareTo(p2.getDt_stipula()))
				    			 .filter(el->el.getDt_stipula().before(progetto.getOtherField().getDtInizio()));
						
						if (optContratto.isPresent())
							optContratto
			 	    			.filter(ctr->ctr.getDt_stipula().after(obbligazione.getDt_registrazione()))
			 	    			.ifPresent(ctr->{
			 	    				throw new ApplicationRuntimeException(
			 	    						"Attenzione! Aumento importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
											  + "La data stipula ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(ctr.getDt_stipula())
											  + ") del primo contratto " + ctr.getEsercizio()+"/"+ctr.getStato()+"/"+ctr.getPg_contratto()
											  + " associato al progetto "+progetto.getCd_progetto()+" è successiva "
											  + "rispetto alla data di registrazione dell'obbligazione ("
											  + new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione())+").");
				    			  });
						else
							Optional.ofNullable(progetto.getOtherField().getDtInizio())
								.filter(dt->!dt.after(obbligazione.getDt_registrazione()))
								.orElseThrow(()->new ApplicationException("Attenzione! Aumento importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
										+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
										+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
										+ "rispetto alla data di registrazione dell'obbligazione ("
										+ new java.text.SimpleDateFormat("dd/MM/yyyy").format(obbligazione.getDt_registrazione())+")."));
					}

					LocalDate localDateFineProgetto = Optional.ofNullable(progetto.getOtherField().getDtProroga())
							.orElse(Optional.ofNullable(progetto.getOtherField().getDtFine())
							.orElse(DateUtils.firstDateOfTheYear(3000))).toLocalDateTime().toLocalDate();
					
					int ggProroga = Optional.ofNullable(obbligazione.getElemento_voce())
											.flatMap(el->{
												if (obbligazione.isCompetenza())
													return Optional.ofNullable(el.getGg_deroga_obbl_comp_prg_scad());
												else
													return Optional.ofNullable(el.getGg_deroga_obbl_res_prg_scad());
											})
											.filter(el->el.compareTo(0)>0)
											.orElse(0);

					localDateFineProgetto = localDateFineProgetto.plusDays(ggProroga);

					if (localDateFineProgetto.isBefore(EJBCommonServices.getServerDate().toLocalDateTime().toLocalDate()))
						throw new ApplicationMessageFormatException("Attenzione! Aumento importo GAE {0} non consentito. "
								+ "La data fine/proroga del progetto {1} {2} ({3}) è precedente rispetto alla data odierna.",
								latt.getCd_linea_attivita(),
								progetto.getCd_progetto(),
								(ggProroga>0?", aumentata di " + ggProroga +" giorni,":""),
								localDateFineProgetto.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
				}

				if ( !obbligazione.getFl_calcolo_automatico().booleanValue() ) {
					String errore = "L'importo (" +
					new it.cnr.contab.util.EuroFormat().format(totaleScad) + 
					") assegnato alla GAE " + key.getCd_linea_attivita() + 
					" e alla voce " + obbligazione.getElemento_voce().getCd_elemento_voce() + " è " + 
					new String(totaleScad.compareTo((BigDecimal) prcImputazioneFinanziariaTable.get( key ))==1?"maggiore":"minore") + 
					" di " + 
					new it.cnr.contab.util.EuroFormat().format(totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).abs()) + 
					" rispetto al valore originario dell'impegno residuo proprio (" +
					new it.cnr.contab.util.EuroFormat().format((BigDecimal) prcImputazioneFinanziariaTable.get( key )) + ")";

					// in questa condizione non è errore ma solo avvertimento con possibilità di creare
					// un movimento di modifica altrimenti è errore bloccante
					if (totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).compareTo(Utility.ZERO)<0) {
						// in questo condizione dobbiamo dare risposta di avvertimento
						// oppure nessun errore
						if (tipoErrore.equals(TIPO_ERRORE_CONTROLLO)) {

							errControllo.append(errore);
							return obbligazione;
						}
					}
					else if (totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).compareTo(Utility.ZERO)>0) {
						// in questo condizione dobbiamo dare risposta di avvertimento
						// oppure nessun errore
						if (cdsModObblResImporto) {
							if (tipoErrore.equals(TIPO_ERRORE_CONTROLLO)) {
								errControllo.append(errore);
								return obbligazione;
							}
						}
						else
							throw new ApplicationException(errore);
					}
					else
						throw new ApplicationException(errore);
				}
					//}
				else
				{
					diffScad = totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key ));  
					for ( Iterator s = obbligazione.getObbligazione_scadenzarioColl().iterator(); s.hasNext()&&diffScad.compareTo(Utility.ZERO)!=0; )
					{
						os = (Obbligazione_scadenzarioBulk) s.next();
						if (os.getIm_associato_doc_amm().compareTo(Utility.ZERO)==0){
							for ( Iterator d = os.getObbligazione_scad_voceColl().iterator(); d.hasNext()&&diffScad.compareTo(Utility.ZERO)!=0; )
							{
								osv = (Obbligazione_scad_voceBulk) d.next();
								// totale per Cdr e per scadenza				
								if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
									key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
									key.getCd_voce().equals(osv.getCd_voce())) {
									if (osv.getIm_voce().subtract(diffScad).compareTo(Utility.ZERO)==1) 
									{ 
										osv.setToBeUpdated();
										osv.setIm_voce(osv.getIm_voce().subtract(diffScad));
										diffScad = Utility.ZERO;
									}
									else  
									{ 
										osv.setToBeUpdated();
										osv.setIm_voce(Utility.ZERO);
										diffScad = diffScad.subtract(osv.getIm_voce());
									}
								}
							}
						}
					}
					if (diffScad.compareTo(Utility.ZERO)!=0)
						throw new ApplicationException( "Si è verificato un errore durante la ripartizione dell'importo assegnato alla GAE " + key.getCd_linea_attivita() + 
														" e alla voce " + obbligazione.getElemento_voce().getCd_elemento_voce()  + ". Procedere con l'imputazione manuale."); 
				}
			}
		}
		return obbligazione;
	} catch ( Exception ex )
	{
		throw handleException( ex );
	}					
	}
	/**
	 * E'' consentito l''aumento dell''importo degli impegni residui propri
	 * 
	 * @param userContext
	 * @param cd_cds
	 * @return
	 * @throws ComponentException
	 */
	private boolean isCdsModObblResImporto(UserContext userContext, String cd_cds) throws ComponentException{
    	try {
			Parametri_cdsBulk param_cds = (Parametri_cdsBulk)getHome(userContext, Parametri_cdsBulk.class).findByPrimaryKey(new Parametri_cdsBulk(cd_cds,((it.cnr.contab.utenze00.bp.CNRUserContext) userContext).getEsercizio()));
			if (param_cds.getFl_mod_obbl_res().booleanValue())
			   return true;
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
    	return false;
    }

	public void cancellaObbligazioneModTemporanea( 
			UserContext userContext,
			Obbligazione_modificaBulk obbligazioneModTemporanea)
			throws IntrospectionException,PersistencyException, ComponentException {
			
		BulkList dett = obbligazioneModTemporanea.getObbligazione_mod_voceColl();

		for(Iterator it=dett.iterator();it.hasNext();) {
			Obbligazione_mod_voceBulk obbModVoceTemp = (Obbligazione_mod_voceBulk) it.next();

			try {
				deleteBulk(userContext, obbModVoceTemp);
			}
			catch (Exception e) {}
		}
		try {
			deleteBulk(userContext,obbligazioneModTemporanea);
		}
		catch (Exception e) {}
	}
		
	public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk obbligazione) throws ComponentException
	 {
		try
		{
			ObbligazioneBulk obblig = (ObbligazioneBulk) super.inizializzaBulkPerModifica( aUC, obbligazione );
			try {
				lockBulk(aUC, obblig);
			} catch (Throwable t) {
				throw handleException(t);
			}
			return obblig;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		

	}
}
