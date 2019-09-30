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
import java.sql.Timestamp;
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
import it.cnr.contab.docamm00.docs.bulk.Documento_generico_rigaHome;
import it.cnr.contab.doccont00.core.bulk.Linea_attivitaBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_mod_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaHome;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_modificaBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scad_voceBulk;
import it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk;
import it.cnr.contab.doccont00.ejb.ObbligazioneComponentSession;
import it.cnr.contab.doccont00.ejb.ObbligazioneResComponentSession;
import it.cnr.contab.doccont00.ejb.SaldoComponentSession;
import it.cnr.contab.doccont00.intcass.bulk.V_distinta_cass_im_man_revBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazioneHome;
import it.cnr.contab.pdg00.ejb.CostiDipendenteComponentSession;
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
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

public class ObbligazioneModificaComponent extends it.cnr.jada.comp.CRUDComponent  {
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk = super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof Obbligazione_modificaBulk) {
				Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)oggettobulk;
				// carica i dettagli della modifica all'obbligazione
				Obbligazione_modificaHome omHome = (Obbligazione_modificaHome) getHome( usercontext, Obbligazione_modificaBulk.class);
				obbMod.setObbligazione_mod_voceColl( new BulkList( omHome.findObbligazione_mod_voceList( obbMod ) ));
				
				// riempiamo le descrizioni
				for (Iterator it = obbMod.getObbligazione_mod_voceColl().iterator();it.hasNext();) {
					Obbligazione_mod_voceBulk obbModVoce = (Obbligazione_mod_voceBulk) it.next();
					IVoceBilancioBulk voce = null; 
					if (((Parametri_cnrHome)getHome(usercontext, Parametri_cnrBulk.class)).isNuovoPdg(usercontext)) {
						voce = new Elemento_voceBulk(
										obbModVoce.getCd_voce(),
										obbModVoce.getEsercizio(),
										obbModVoce.getTi_appartenenza(),
										obbModVoce.getTi_gestione());
						voce = (Elemento_voceBulk) getHome(usercontext, Elemento_voceBulk.class).findByPrimaryKey(voce);
					} else {
						voce = new Voce_fBulk(
									obbModVoce.getCd_voce(),
									obbModVoce.getEsercizio(),
									obbModVoce.getTi_appartenenza(),
									obbModVoce.getTi_gestione());
						voce = (Voce_fBulk) getHome(usercontext, Voce_fBulk.class).findByPrimaryKey(voce);
					}
					obbModVoce.setVoce(voce);

					WorkpackageBulk linea =
						new WorkpackageBulk(
								obbModVoce.getCd_centro_responsabilita(),
								obbModVoce.getCd_linea_attivita());
					linea = (WorkpackageBulk) getHome(usercontext, linea).findByPrimaryKey(linea);
					obbModVoce.setLinea_attivita(linea);
				}
			}
			return oggettobulk;
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	public OggettoBulk inizializzaBulkPerInserimento(UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)bulk;
			
			java.sql.Timestamp tsOdierno = EJBCommonServices.getServerDate();
			GregorianCalendar tsOdiernoGregorian = (GregorianCalendar) GregorianCalendar.getInstance();
	       	tsOdiernoGregorian.setTime(tsOdierno);
	        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");		
	        	        
	        if(tsOdiernoGregorian.get(GregorianCalendar.YEAR) > CNRUserContext.getEsercizio(userContext).intValue())
	        	obbMod.setDt_modifica(new java.sql.Timestamp(sdf.parse("31/12/"+obbMod.getEsercizio().intValue()).getTime()));
	        else {
	        //Obbligazione_modificaHome testataHome = (Obbligazione_modificaHome)getHome(userContext, Obbligazione_modificaBulk.class);
	        	obbMod.setDt_modifica(EJBCommonServices.getServerDate());
	        }
			obbMod = inserisciModifiche(userContext, obbMod);
			return super.inizializzaBulkPerInserimento(userContext,obbMod);
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	private Obbligazione_modificaBulk inserisciModifiche(UserContext aUC, Obbligazione_modificaBulk bulk) throws it.cnr.jada.comp.ComponentException {
		try {
			Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)bulk;
			ObbligazioneBulk obbligazione = obbMod.getObbligazione();
			BigDecimal percentuale = new BigDecimal( 100);
			BigDecimal totaleScad = new BigDecimal(0);
			BigDecimal diffScad = new BigDecimal(0);
			Obbligazione_scad_voceBulk osv;
			Obbligazione_scadenzarioBulk os;
			Obbligazione_scad_voceBulk key = new Obbligazione_scad_voceBulk();

			PrimaryKeyHashtable prcImputazioneFinanziariaTable = getOldRipartizioneCdrVoceLinea(aUC, obbligazione); 

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
					Obbligazione_mod_voceBulk obbModVoce =
						new Obbligazione_mod_voceBulk(
								obbMod.getCd_cds(),
								obbMod.getEsercizio(),
								obbMod.getPg_modifica(),
								key.getTi_appartenenza(),
								key.getTi_gestione(),
								key.getCd_voce(),
								key.getCd_centro_responsabilita(),
								key.getCd_linea_attivita());

					IVoceBilancioBulk voce = null;
					if (((Parametri_cnrHome)getHome(aUC, Parametri_cnrBulk.class)).isNuovoPdg(aUC)) {
						voce = new Elemento_voceBulk(
								key.getCd_voce(),
								obbMod.getEsercizio(),
								key.getTi_appartenenza(),
								key.getTi_gestione());
						voce = (Elemento_voceBulk) getHome(aUC, Elemento_voceBulk.class).findByPrimaryKey(voce);
					} else {
						voce = new Voce_fBulk(
								key.getCd_voce(),
								obbMod.getEsercizio(),
								key.getTi_appartenenza(),
								key.getTi_gestione());
						voce = (Voce_fBulk) getHome(aUC, Voce_fBulk.class).findByPrimaryKey(voce);
					}
					obbModVoce.setVoce(voce);
					WorkpackageBulk linea =
						new WorkpackageBulk(
								key.getCd_centro_responsabilita(),
								key.getCd_linea_attivita());
					linea = (WorkpackageBulk) getHome(aUC, linea).findByPrimaryKey(linea);
					obbModVoce.setLinea_attivita(linea);
					obbModVoce.setIm_modifica(totaleScad.subtract((BigDecimal) prcImputazioneFinanziariaTable.get( key )));
					obbModVoce.setToBeCreated();
					obbMod.addToObbligazione_mod_voceColl(obbModVoce);
				}
			}
			return obbMod;
			
		} catch( Exception e ) {
			throw handleException( e );
		}		
	}
	private PrimaryKeyHashtable getOldRipartizioneCdrVoceLinea(UserContext userContext, ObbligazioneBulk obbligazione) throws it.cnr.jada.comp.ComponentException {
		try {
			ObbligazioneComponentSession obbSess = (ObbligazioneComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCCONT00_EJB_ObbligazioneComponentSession",ObbligazioneComponentSession.class);
			
			return obbSess.getOldRipartizioneCdrVoceLinea(userContext,obbligazione);

		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
	{
		SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
		sql.addClause("AND", "cd_cds", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		sql.addClause("AND", "esercizio", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
		sql.addOrderBy("cd_cds, esercizio, pg_modifica");
		return sql;
	}
	public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
	{
		Obbligazione_modificaBulk obbMod = (Obbligazione_modificaBulk)bulk;
		
		validaCampi(uc, obbMod);
		
		return super.creaConBulk( uc, bulk );
	}
		
	private void validaCampi(UserContext uc, Obbligazione_modificaBulk obbMod) throws ComponentException {
		try {
			ObbligazioneBulk obbligazione = obbMod.getObbligazione();

			// controlli di validazione del campo MOTIVAZIONE
			Parametri_cnrBulk bulkCNR = (Parametri_cnrBulk)getHome(uc, Parametri_cnrBulk.class).findByPrimaryKey(new Parametri_cnrBulk(CNRUserContext.getEsercizio(uc)));		
			if (bulkCNR == null)
			  throw new ApplicationException("Parametri CNR non presenti per l'anno "+CNRUserContext.getEsercizio(uc));
			
			if (bulkCNR.getFl_motivazione_su_imp() &&
				obbMod.getMotivazione()==null) {

				BigDecimal impObbl=obbligazione.getIm_obbligazione();
				ObbligazioneBulk oldObbl = (ObbligazioneBulk)getHome(uc, ObbligazioneBulk.class).findByPrimaryKey(obbligazione);
				BigDecimal oldImpObbl = oldObbl.getIm_obbligazione();
				if (Utility.nvl(impObbl.subtract(oldImpObbl)).compareTo(Utility.nvl(bulkCNR.getImporto_max_imp()))>=0)
					throw new ApplicationException("Attenzione: il campo MOTIVAZIONE è obbligatorio.");
			}
		}
		catch ( Exception e )
		{
			throw handleException( e )	;
		}	
	}
}
