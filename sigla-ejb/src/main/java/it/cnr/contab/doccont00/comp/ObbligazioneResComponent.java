/*
 * Created on Feb 6, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_cdsBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneRes_impropriaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceKey;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaKey;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.V_doc_passivo_obbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.V_obbligazione_im_mandatoBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.DateUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

	private ObbligazioneBulk calcolaPercentualeImputazioneObbligazione (UserContext aUC,ObbligazioneBulk obbligazione, String tipoErrore, StringBuffer errControllo) throws ComponentException
	{
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
				WorkpackageBulk latt = ((WorkpackageHome)getHome(aUC, WorkpackageBulk.class)).searchGAECompleta(aUC,CNRUserContext.getEsercizio(aUC),
						key.getCd_centro_responsabilita(), key.getCd_linea_attivita());
				ProgettoBulk progetto = latt.getProgetto();
				Optional.ofNullable(progetto.getOtherField())
						.filter(el->el.isStatoApprovato()||el.isStatoChiuso())
						.orElseThrow(()->new ApplicationException("Attenzione! Modifica importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
								+ "Il progetto associato "+progetto.getCd_progetto()+" non risulta in stato Approvato o Chiuso."));
				if (progetto.isDatePianoEconomicoRequired()) {
					Optional.ofNullable(progetto.getOtherField().getDtInizio())
						.filter(dt->!dt.after(obbligazione.getDt_registrazione()))
						.orElseThrow(()->new ApplicationException("Attenzione! Modifica importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
								+ "La data inizio ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(progetto.getOtherField().getDtInizio())
								+ ") del progetto "+progetto.getCd_progetto()+" associato è successiva "
								+ "rispetto alla data odierna."));
				}
				Optional.ofNullable(
					Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(Optional.ofNullable(progetto.getOtherField().getDtFine()).orElse(DateUtils.firstDateOfTheYear(3000))))
						.filter(dt->!dt.before(DateUtils.truncate(it.cnr.jada.util.ejb.EJBCommonServices.getServerTimestamp())))
						.orElseThrow(()->new ApplicationException("Attenzione! Modifica importo GAE "+latt.getCd_linea_attivita()+" non consentito. "
								+ "La data fine/proroga ("+new java.text.SimpleDateFormat("dd/MM/yyyy").format(Optional.ofNullable(progetto.getOtherField().getDtProroga()).orElse(progetto.getOtherField().getDtFine()))
								+ ") del progetto "+progetto.getCd_progetto()+" associato è precedente "
								+ "rispetto alla data odierna."));

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
