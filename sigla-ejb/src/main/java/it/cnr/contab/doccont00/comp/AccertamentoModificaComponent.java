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
 * Created on Jun 23, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.doccont00.comp;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.IVoceBilancioBulk;
import it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk;
import it.cnr.contab.doccont00.core.bulk.AccertamentoBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaHome;
import it.cnr.contab.doccont00.core.bulk.Accertamento_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Accertamento_scadenzarioBulk;
import it.cnr.contab.doccont00.ejb.AccertamentoComponentSession;
import it.cnr.contab.doccont00.ejb.AccertamentoResiduoComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_distinta_cass_im_man_revBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.contab.varstanz00.bulk.Ass_var_stanz_res_cdrBulk;
import it.cnr.contab.varstanz00.bulk.Var_stanz_resBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.PrimaryKeyHashtable;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class AccertamentoModificaComponent extends it.cnr.jada.comp.CRUDComponent  {
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Accertamento_modificaBulk) {
				Accertamento_modificaBulk accMod = (Accertamento_modificaBulk)oggettobulk;
				// carica i dettagli della modifica all'accertamento
				Accertamento_modificaHome omHome = (Accertamento_modificaHome) getHome( usercontext, Accertamento_modificaBulk.class);
				accMod.setAccertamento_mod_voceColl( new BulkList( omHome.findAccertamento_mod_voceList( accMod ) ));
				
				// riempiamo le descrizioni
				for (Iterator it = accMod.getAccertamento_mod_voceColl().iterator();it.hasNext();) {
					Accertamento_mod_voceBulk accModVoce = (Accertamento_mod_voceBulk) it.next();

					IVoceBilancioBulk voce = null; 
					if (((Parametri_cnrHome)getHome(usercontext, Parametri_cnrBulk.class)).isNuovoPdg(usercontext)) {
						voce = new Elemento_voceBulk(
								accModVoce.getCd_voce(),
								accModVoce.getEsercizio(),
								accModVoce.getTi_appartenenza(),
								accModVoce.getTi_gestione());
						voce = (Elemento_voceBulk) getHome(usercontext, Elemento_voceBulk.class).findByPrimaryKey(voce);
					} else {
						voce = new Voce_fBulk(
								accModVoce.getCd_voce(),
								accModVoce.getEsercizio(),
								accModVoce.getTi_appartenenza(),
								accModVoce.getTi_gestione());
						voce = (Voce_fBulk) getHome(usercontext, Voce_fBulk.class).findByPrimaryKey(voce);
					}
					accModVoce.setVoce(voce);
					WorkpackageBulk linea =
						new WorkpackageBulk(
								accModVoce.getCd_centro_responsabilita(),
								accModVoce.getCd_linea_attivita());
					linea = (WorkpackageBulk) getHome(usercontext, linea).findByPrimaryKey(linea);
					accModVoce.setLinea_attivita(linea);
				}
					
			}
			return oggettobulk;
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Accertamento_modificaBulk accMod = (Accertamento_modificaBulk)bulk;
			
			java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
			GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
	       	tsOdiernoGregorian.setTime(tsOdierno);
	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
	        
	        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(userContext).intValue())
	        	accMod.setDt_modifica(new java.sql.Timestamp(sdf.parse("31/12/"+CNRUserContext.getEsercizio(userContext).intValue()).getTime()));
	        else {
	        	accMod.setDt_modifica(EJBCommonServices.getServerDate());
	        }
	       
			//Accertamento_modificaHome testataHome = (Accertamento_modificaHome)getHome(userContext, Accertamento_modificaBulk.class);
			accMod = inserisciModifiche(userContext, accMod);
			return super.inizializzaBulkPerInserimento(userContext,accMod);
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	private Accertamento_modificaBulk inserisciModifiche(UserContext aUC, Accertamento_modificaBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Accertamento_modificaBulk accMod = (Accertamento_modificaBulk)bulk;
			AccertamentoBulk accertamento = accMod.getAccertamento();
			BigDecimal percentuale = new BigDecimal( 100);
			BigDecimal totaleScad = new BigDecimal(0);
			BigDecimal diffScad = new BigDecimal(0);
			Accertamento_scad_voceBulk osv;
			Accertamento_scadenzarioBulk os;
			Accertamento_scad_voceBulk key = new Accertamento_scad_voceBulk();

			PrimaryKeyHashtable prcImputazioneFinanziariaTable = getOldRipartizioneCdrVoceLinea(aUC, accertamento); 

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
							key.getCd_linea_attivita().equals(osv.getCd_linea_attivita())) {
							totaleScad = totaleScad.add(Utility.nvl(osv.getIm_voce())); 						
						}
					}
				}

				if (totaleScad.compareTo((BigDecimal) prcImputazioneFinanziariaTable.get( key ))!=0) {
					Accertamento_mod_voceBulk accModVoce =
						new Accertamento_mod_voceBulk(
								accMod.getCd_cds(),
								accMod.getEsercizio(),
								accMod.getPg_modifica(),
								accertamento.getCapitolo().getTi_appartenenza(),
								accertamento.getCapitolo().getTi_gestione(),
								accertamento.getCapitolo().getCd_voce(),
								key.getCd_centro_responsabilita(),
								key.getCd_linea_attivita());

					IVoceBilancioBulk voce = null;
					if (((Parametri_cnrHome)getHome(aUC, Parametri_cnrBulk.class)).isNuovoPdg(aUC)) {
						voce = new Elemento_voceBulk(
								accertamento.getCapitolo().getCd_voce(),
								accMod.getEsercizio(),
								accertamento.getCapitolo().getTi_appartenenza(),
								accertamento.getCapitolo().getTi_gestione());
						voce = (Elemento_voceBulk) getHome(aUC, Elemento_voceBulk.class).findByPrimaryKey(voce);
					} else {
						new Voce_fBulk(
								accertamento.getCapitolo().getCd_voce(),
								accMod.getEsercizio(),
								accertamento.getCapitolo().getTi_appartenenza(),
								accertamento.getCapitolo().getTi_gestione());
						voce = (Voce_fBulk) getHome(aUC, Voce_fBulk.class).findByPrimaryKey(voce);
					}
					accModVoce.setVoce(voce);

					WorkpackageBulk linea =
						new WorkpackageBulk(
								key.getCd_centro_responsabilita(),
								key.getCd_linea_attivita());
					linea = (WorkpackageBulk) getHome(aUC, linea).findByPrimaryKey(linea);
					accModVoce.setLinea_attivita(linea);
					accModVoce.setIm_modifica(totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )));
					accModVoce.setToBeCreated();
					accMod.addToAccertamento_mod_voceColl(accModVoce);
				}
			}
			return accMod;
			
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	private PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(UserContext userContext, AccertamentoBulk accertamento) throws it.cnr.jada.comp.ComponentException {
		try {
			AccertamentoComponentSession obbSess = (AccertamentoComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_AccertamentoComponentSession",AccertamentoComponentSession.class);
			
			return obbSess.getOldRipartizioneCdrVoceLinea(userContext,accertamento);

		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		if (!isCdsEnte(userContext)) {
			sql.addTableToHeader("ACCERTAMENTO");
			sql.addSQLJoin("ACCERTAMENTO_MODIFICA.CD_CDS", "ACCERTAMENTO.CD_CDS");
			sql.addSQLJoin("ACCERTAMENTO_MODIFICA.ESERCIZIO", "ACCERTAMENTO.ESERCIZIO");
			sql.addSQLJoin("ACCERTAMENTO_MODIFICA.ESERCIZIO_ORIGINALE", "ACCERTAMENTO.ESERCIZIO_ORIGINALE");
			sql.addSQLJoin("ACCERTAMENTO_MODIFICA.PG_ACCERTAMENTO", "ACCERTAMENTO.PG_ACCERTAMENTO");
			//sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			sql.addSQLClause("AND","ACCERTAMENTO.CD_CDS_ORIGINE", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		}
		sql.addOrderBy("cd_cds, esercizio, pg_modifica");
		return sql;
	}
	public void cancellaVariazioneTemporanea( 
			UserContext userContext,
			Var_stanz_resBulk variazioneTemporanea)
			throws IntrospectionException,PersistencyException, ComponentException {
			
		BulkList dett1 = variazioneTemporanea.getRigaVariazione();
		for(Iterator it=dett1.iterator();it.hasNext();) {
			Ass_var_stanz_res_cdrBulk varRiga = (Ass_var_stanz_res_cdrBulk) it.next();

			try {
				deleteBulk(userContext, varRiga);
			}
			catch (Exception e) {}
		}
		BulkList dett2 = variazioneTemporanea.getAssociazioneCDR();
		for(Iterator it=dett2.iterator();it.hasNext();) {
			Ass_var_stanz_res_cdrBulk varCDR = (Ass_var_stanz_res_cdrBulk) it.next();

			try {
				deleteBulk(userContext, varCDR);
			}
			catch (Exception e) {}
		}
		try {
			deleteBulk(userContext,variazioneTemporanea);
		}
		catch (Exception e) {}
	}
	private boolean isCdsEnte(UserContext userContext) throws ComponentException {
		try {
			Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
			if (((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
				return true;
			else
				return false;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

}
