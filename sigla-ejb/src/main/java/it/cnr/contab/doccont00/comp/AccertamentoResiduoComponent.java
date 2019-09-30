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

import it.cnr.contab.config00.pdcfin.bulk.V_voce_f_partita_giroBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoHome;
import it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_res_rigaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccertamentoResiduoComponent extends AccertamentoComponent {
	public static final String TIPO_ERRORE_ECCEZIONE = "E"; // solleva eccezione
	public static final String TIPO_ERRORE_CONTROLLO = "C"; // effettua solo controllo
	public  AccertamentoResiduoComponent()
	{
		/*Default constructor*/
	}

	public String controllaDettagliScadenzaAccertamento (UserContext aUC,AccertamentoBulk accertamento,Accertamento_scadenzarioBulk scadenzario) throws ComponentException
	{
		StringBuffer errControllo = new StringBuffer();
		//accertamento = generaDettagliScadenzaAccertamento (aUC, accertamento, scadenzario, false);
		accertamento = calcolaPercentualeImputazioneAccertamento( aUC, accertamento, TIPO_ERRORE_CONTROLLO, errControllo);

		// per evitare messaggi successivi alla modifica fa le seguenti verifiche
		//verifica la correttezza dell'accertamento
		verificaAccertamento( aUC, accertamento );

		return errControllo.toString();
	}

	public AccertamentoBulk generaDettagliScadenzaAccertamento (UserContext aUC,AccertamentoBulk accertamento,Accertamento_scadenzarioBulk scadenzario, boolean allineaImputazioneFinanziaria) throws ComponentException
	{
		Accertamento_scadenzarioBulk os;

		// non e' ancora stata selezionata l'imputazione finanziaria
		if (accertamento.getLineeAttivitaSelezionateColl().size() == 0 &&
			accertamento.getNuoveLineeAttivitaColl().size() == 0)
			return accertamento;

		// non sono ancora state inserite le scadenze
		if (accertamento.getAccertamento_scadenzarioColl().size() == 0 )
			return accertamento;
	
		if ( scadenzario != null ) // una sola scadenza e' stata modificata
		{
			//creo i dettagli della scadenza per le linee attivita da PDG
			creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, accertamento, scadenzario );

			//creo i dettagli della scadenza per le nuove linee attivita 		
			creaDettagliScadenzaPerNuoveLineeAttivita( aUC, accertamento, scadenzario );

			// aggiunta delle linee di attività comuni nelle scadenze con importo nullo
			creaDettagliScadenzaLAComuni(aUC, accertamento, scadenzario); 

		}	// imputazione finanziaria e' stata modificata, quindi rigenero i dettagli per tutte le scadenze
		else
		{
				
			// per ogni scadenza aggiorno i suoi dettagli in base alle linee di attività specificate dall'utente
			for ( Iterator scadIterator = accertamento.getAccertamento_scadenzarioColl().iterator(); scadIterator.hasNext(); )
			{
					os = (Accertamento_scadenzarioBulk) scadIterator.next();		
					//cancello i dettagli della scadenza per le linee attivita che non esistono piu'
					cancellaDettagliScadenze( aUC, accertamento, os );

					//creo i dettagli della scadenza per le linee attivita da PDG
					creaDettagliScadenzaPerLineeAttivitaDaPdG( aUC, accertamento, os );

					//creo i dettagli della scadenza per le nuove linee attivita 		
					creaDettagliScadenzaPerNuoveLineeAttivita( aUC, accertamento, os );

					// aggiunta delle linee di attività comuni nelle scadenze con importo nullo
					creaDettagliScadenzaLAComuni(aUC, accertamento, os); 
			}
		}
	
		if (accertamento.getFl_calcolo_automatico().booleanValue() && allineaImputazioneFinanziaria) accertamento = calcolaPercentualeImputazioneAccertamento( aUC, accertamento );

		return accertamento;
	}
	protected AccertamentoBulk calcolaPercentualeImputazioneAccertamento (UserContext aUC,AccertamentoBulk accertamento) throws ComponentException
	{
		StringBuffer errControllo = new StringBuffer();
		return calcolaPercentualeImputazioneAccertamento( aUC, accertamento, TIPO_ERRORE_ECCEZIONE, errControllo);
	}

	private AccertamentoBulk calcolaPercentualeImputazioneAccertamento (UserContext aUC,AccertamentoBulk accertamento, String tipoErrore, StringBuffer errControllo) throws ComponentException
	{
		BigDecimal percentuale = new BigDecimal( 100);
		BigDecimal totaleScad = new BigDecimal(0);
		BigDecimal diffScad = new BigDecimal(0);
		Accertamento_scad_voceBulk osv;
		Accertamento_scadenzarioBulk os;
		Accertamento_scad_voceBulk key = new Accertamento_scad_voceBulk();

		boolean cdsModAccResImporto = false; //isCdsModObblResImporto(aUC, obbligazione.getCd_cds());
		
		// calcolo le percentuali di imputazione finanziaria per le linee di attivita da pdg
		// 100 - percentuali specificate x linee att non da PDG
		try {
			AccertamentoHome accertHome = (AccertamentoHome)getHome(aUC, AccertamentoBulk.class);
			Accertamento_scadenzarioHome accertScadHome = (Accertamento_scadenzarioHome)getHome(aUC, Accertamento_scadenzarioBulk.class);

			AccertamentoBulk accertDB = (AccertamentoBulk)accertHome.findAccertamento(accertamento);
			accertDB.setAccertamento_scadenzarioColl(new BulkList(accertHome.findAccertamento_scadenzarioList(accertDB)));

			for ( Iterator i = accertDB.getAccertamento_scadenzarioColl().iterator(); i.hasNext(); )
			{
				Accertamento_scadenzarioBulk accertScadDB = (Accertamento_scadenzarioBulk) i.next();
				accertScadDB.setAccertamento_scad_voceColl( new BulkList( accertScadHome.findAccertamento_scad_voceList( aUC,accertScadDB )));
			}							    	
			getHomeCache(aUC).fetchAll(aUC);

			PrimaryKeyHashtable prcImputazioneFinanziariaTable = new PrimaryKeyHashtable();	
		
			for ( Iterator s = accertDB.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); )
			{
				os = (Accertamento_scadenzarioBulk) s.next();
				for ( Iterator d = os.getAccertamento_scad_voceColl().iterator(); d.hasNext(); )
				{
					osv = (Accertamento_scad_voceBulk) d.next();
					// totale per Cdr e per scadenza				
					key = new Accertamento_scad_voceBulk(osv.getCd_cds(),
														 osv.getCd_centro_responsabilita(),
														 osv.getCd_linea_attivita(),
														 osv.getEsercizio(),
														 osv.getEsercizio_originale(),
														 osv.getPg_accertamento(),
														 new Long(1));

					key.setAccertamento_scadenzario(new Accertamento_scadenzarioBulk(osv.getCd_cds(),
							  														 osv.getEsercizio(),
																					 osv.getEsercizio_originale(),
																					 osv.getPg_accertamento(),
																					 new Long(1)));

					key.getAccertamento_scadenzario().setAccertamento(new AccertamentoBulk(osv.getCd_cds(),
								 														   osv.getEsercizio(),
																						   osv.getEsercizio_originale(),
																						   osv.getPg_accertamento()));
					
					key.getAccertamento_scadenzario().getAccertamento().setCapitolo(new V_voce_f_partita_giroBulk(accertDB.getCd_voce(),
																					   							  accertDB.getEsercizio(),
																												  accertDB.getTi_appartenenza(),
																												  accertDB.getTi_gestione()));

					totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );			
					if ( totaleScad == null || totaleScad.compareTo(new BigDecimal(0)) == 0)
						prcImputazioneFinanziariaTable.put( key, osv.getIm_voce());				
					else
					{
						totaleScad = totaleScad.add( osv.getIm_voce());
						prcImputazioneFinanziariaTable.put( key, totaleScad );
					}			
				}
			}

			if ( accertamento.getFl_calcolo_automatico().booleanValue() ) {
				for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
				{
					key = (Accertamento_scad_voceBulk)e.nextElement();
					totaleScad = (BigDecimal) prcImputazioneFinanziariaTable.get( key );			
					percentuale = totaleScad.multiply(new BigDecimal(100)).divide(accertDB.getIm_accertamento(), 2, BigDecimal.ROUND_HALF_UP);
	
					for ( Iterator s = accertamento.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); )
					{
						os = (Accertamento_scadenzarioBulk) s.next();
						for ( Iterator d = os.getAccertamento_scad_voceColl().iterator(); d.hasNext(); )
						{
							osv = (Accertamento_scad_voceBulk) d.next();
							// totale per Cdr e per scadenza				
							if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
								key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
								key.getCd_voce().equals(osv.getCd_voce())) {
								osv.setToBeUpdated(); 
								osv.setIm_voce(osv.getAccertamento_scadenzario().getIm_scadenza().multiply(percentuale).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
								break;
							}
						}
					}
				}
			}

			boolean trovato = false;

			//devo controllare che tutto è quadrato vecchio e nuovo
			//verifico che nel nuovo Accertamento non ci siano linee o voci nuove rispetto alla precedente
			for ( Iterator s = accertamento.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); )
			{
				os = (Accertamento_scadenzarioBulk) s.next();
				for ( Iterator d = os.getAccertamento_scad_voceColl().iterator(); d.hasNext(); )
				{
					osv = (Accertamento_scad_voceBulk) d.next();
					trovato=false;
					for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
					{			
						key = (Accertamento_scad_voceBulk)e.nextElement();
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
			//verifico che nel nuovo Accertamento alle linee/voci siano stati assegnati gli stessi importi
			for ( Enumeration e = prcImputazioneFinanziariaTable.keys(); e.hasMoreElements(); ) 
			{
				key = (Accertamento_scad_voceBulk)e.nextElement();
				totaleScad = new BigDecimal(0);
	
				for ( Iterator s = accertamento.getAccertamento_scadenzarioColl().iterator(); s.hasNext(); )
				{
					os = (Accertamento_scadenzarioBulk) s.next();
					for ( Iterator d = os.getAccertamento_scad_voceColl().iterator(); d.hasNext(); )
					{
						osv = (Accertamento_scad_voceBulk) d.next();
						// totale per Cdr e per scadenza				
						if (key.getCd_centro_responsabilita().equals(osv.getCd_centro_responsabilita()) &&
							key.getCd_linea_attivita().equals(osv.getCd_linea_attivita()) &&
							key.getCd_voce().equals(osv.getCd_voce()) && 
							osv.getIm_voce()!=null) {
							totaleScad = totaleScad.add(osv.getIm_voce()); 						
						}
					}
				}

				if (totaleScad.compareTo((BigDecimal) prcImputazioneFinanziariaTable.get( key ))!=0) {
					if ( !accertamento.getFl_calcolo_automatico().booleanValue() ) { 
						String errore = "L'importo (" +
						new it.cnr.contab.util.EuroFormat().format(totaleScad) + 
						") assegnato alla GAE " + key.getCd_linea_attivita() + 
						" e alla voce " + accertamento.getCd_elemento_voce()  + " è " + 
						new String(totaleScad.compareTo((BigDecimal) prcImputazioneFinanziariaTable.get( key ))==1?"maggiore":"minore") + 
						" di " + 
						new it.cnr.contab.util.EuroFormat().format(totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).abs()) + 
						" rispetto al valore originario dell'accertamento residuo proprio (" +
						new it.cnr.contab.util.EuroFormat().format((BigDecimal) prcImputazioneFinanziariaTable.get( key )) + ")";

						// in questa condizione non è errore ma solo avvertimento con possibilità di creare
						// un movimento di modifica altrimenti è errore bloccante
						if (totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).compareTo(Utility.ZERO)<0) {
							// in questo condizione dobbiamo dare risposta di avvertimento
							// oppure nessun errore
							if (tipoErrore.equals(TIPO_ERRORE_CONTROLLO)) {

								errControllo.append(errore);
								return accertamento;
							}
						}
						else if (totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )).compareTo(Utility.ZERO)>0) {
							// in questo condizione dobbiamo dare risposta di avvertimento
							// oppure nessun errore
							if (cdsModAccResImporto) {
								if (tipoErrore.equals(TIPO_ERRORE_CONTROLLO)) {
									errControllo.append(errore);
									return accertamento;
								}
							}
							else
								throw new ApplicationException(errore);
						}
						else
							throw new ApplicationException(errore);
					}
					else
					{
						diffScad = totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key ));  
						for ( Iterator s = accertamento.getAccertamento_scadenzarioColl().iterator(); s.hasNext()&&diffScad.compareTo(Utility.ZERO)!=0; )
						{
							os = (Accertamento_scadenzarioBulk) s.next();
							if (os.getIm_associato_doc_amm().compareTo(Utility.ZERO)==0){
								for ( Iterator d = os.getAccertamento_scad_voceColl().iterator(); d.hasNext()&&diffScad.compareTo(Utility.ZERO)!=0; )
								{
									osv = (Accertamento_scad_voceBulk) d.next();
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
															" e alla voce " + accertamento.getCd_elemento_voce() + ". Procedere con l'imputazione manuale."); 

					}
				}
			}
		} catch (PersistencyException e) {
			throw handleException( e );
		} catch (IntrospectionException e) {
			throw handleException( e );
		}
		return accertamento;
	}

	public void cancellaAccertamentoModTemporanea( 
			UserContext userContext,
			Accertamento_modificaBulk accertamentoModTemporanea)
			throws IntrospectionException,PersistencyException, ComponentException {
			
		BulkList dett = accertamentoModTemporanea.getAccertamento_mod_voceColl();

		for(Iterator it=dett.iterator();it.hasNext();) {
			Accertamento_mod_voceBulk obbModVoceTemp = (Accertamento_mod_voceBulk) it.next();

			try {
				deleteBulk(userContext, obbModVoceTemp);
			}
			catch (Exception e) {}
		}
		try {
			deleteBulk(userContext,accertamentoModTemporanea);
		}
		catch (Exception e) {}

		Var_stanz_resBulk var = accertamentoModTemporanea.getVariazione();

		BulkList dett1 = var.getRigaVariazione();
		for(Iterator it=dett1.iterator();it.hasNext();) {
			Var_stanz_res_rigaBulk varRiga = (Var_stanz_res_rigaBulk) it.next();

			try {
				deleteBulk(userContext, varRiga);
			}
			catch (Exception e) {}
		}
		BulkList dett2 = var.getAssociazioneCDR();
		for(Iterator it=dett2.iterator();it.hasNext();) {
			Ass_var_stanz_res_cdrBulk varCDR = (Ass_var_stanz_res_cdrBulk) it.next();

			try {
				deleteBulk(userContext, varCDR);
			}
			catch (Exception e) {}
		}
		try {
			deleteBulk(userContext,var);
		}
		catch (Exception e) {}
	}
}
