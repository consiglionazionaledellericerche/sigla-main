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
 * Created on Sep 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.prevent01.comp;

import java.rmi.RemoteException;
import java.util.Iterator;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Parametri_cnrBulk;
import it.cnr.contab.config00.bulk.Parametri_cnrHome;
import it.cnr.contab.config00.bulk.Parametri_enteBulk;
import it.cnr.contab.config00.bulk.Parametri_enteHome;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.pdcfin.bulk.NaturaBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Classificazione_vociBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliBulk;
import it.cnr.contab.config00.pdcfin.cla.bulk.Parametri_livelliHome;
import it.cnr.contab.config00.pdcfin.cla.bulk.V_classificazione_vociBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.CdsHome;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.V_cons_pdgp_pdgg_etrBulk;
import it.cnr.contab.prevent01.bp.CRUDPdg_Modulo_EntrateBP;
import it.cnr.contab.prevent01.bulk.Pdg_moduloBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateBulk;
import it.cnr.contab.prevent01.bulk.Pdg_Modulo_EntrateHome;
import it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_other_fieldHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoBulk;
import it.cnr.contab.progettiric00.core.bulk.Progetto_piano_economicoHome;
import it.cnr.contab.progettiric00.core.bulk.Progetto_sipBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgBulk;
import it.cnr.contab.progettiric00.tabrif.bulk.Voce_piano_economico_prgHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.UtenteComuneBulk;
import it.cnr.contab.util.Utility;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.bulk.SimpleBulkList;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CRUDPdg_Modulo_EntrateComponent extends it.cnr.jada.comp.CRUDComponent {

public CRUDPdg_Modulo_EntrateComponent() {
	super();
}

public OggettoBulk modificaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk.setCrudStatus(OggettoBulk.NORMAL);
		return super.modificaConBulk(usercontext, oggettobulk);
}
public OggettoBulk creaConBulk(UserContext usercontext,	OggettoBulk oggettobulk) throws ComponentException {
		oggettobulk.setCrudStatus(OggettoBulk.NORMAL);
		return super.creaConBulk(usercontext, oggettobulk);
}
/**
* 	Ritorna il bulk dei parametri livelli <ParametriLivelliBulk> dell'Esercizio indicato <esercizio>.
*/
public Parametri_livelliBulk findParametriLivelli(UserContext userContext, Integer esercizio) throws it.cnr.jada.comp.ComponentException {
		try
		{
			Parametri_livelliHome parametri_livelliHome = (Parametri_livelliHome) getHome(userContext, Parametri_livelliBulk.class );
			Parametri_livelliBulk parametri_livelliBulk = (Parametri_livelliBulk)parametri_livelliHome.findByPrimaryKey(new Parametri_livelliBulk(esercizio));
			if (parametri_livelliBulk==null)
				throw new ApplicationException("Parametri Livelli non definiti per l'esercizio " + esercizio + ".");
			return parametri_livelliBulk;
		}
		catch (Exception e )
		{
			throw handleException( e );
		}	
	}

public SQLBuilder selectLinea_attivitaByClause(UserContext userContext, Pdg_Modulo_EntrateBulk dettaglio, it.cnr.contab.config00.latt.bulk.WorkpackageBulk l_att, CompoundFindClause clauses) 
		throws ComponentException
{		
		SQLBuilder sql = getHome(userContext, it.cnr.contab.config00.latt.bulk.WorkpackageBulk.class,"V_LINEA_ATTIVITA_VALIDA").createSQLBuilder();
		sql.addClause( clauses );	
	    
		if (dettaglio.getCdr_linea()!=null)
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			sql.addSQLClause("AND","CD_CENTRO_RESPONSABILITA",sql.EQUALS, dettaglio.getCdr_linea());		
			sql.addSQLClause("AND","TI_GESTIONE",sql.EQUALS, "E");
		return sql;		
}

public SQLBuilder selectAreaByClause (UserContext userContext,Pdg_Modulo_EntrateBulk dett,CdsBulk ass_uo,CompoundFindClause clause) throws ComponentException, PersistencyException
{
	SQLBuilder sql = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
	if (clause != null) 
	  sql.addClause(clause);			
	sql.addTableToHeader("ASS_UO_AREA");
	sql.addTableToHeader("UNITA_ORGANIZZATIVA UO");
	sql.addTableToHeader("UNITA_ORGANIZZATIVA CDS");
	sql.addSQLClause("AND", "ASS_UO_AREA.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	sql.addSQLJoin("CDS.CD_UNITA_PADRE","UO.CD_UNITA_PADRE");
	sql.addSQLJoin("CDS.CD_UNITA_ORGANIZZATIVA","ASS_UO_AREA.CD_UNITA_ORGANIZZATIVA");
	sql.addSQLJoin("ASS_UO_AREA.CD_AREA_RICERCA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
		    
	SQLBuilder sql2 = ((CdsHome)getHome(userContext, CdsBulk.class)).createSQLBuilder();
	sql2.addTableToHeader("UNITA_ORGANIZZATIVA UO");
	sql2.addSQLClause("AND", "UO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	sql2.addSQLJoin("UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA","UO.CD_UNITA_PADRE");
	return sql.union(sql2,true);
}
public SQLBuilder selectClassificazione_vociByClause(CNRUserContext userContext, Pdg_Modulo_EntrateBulk dettaglio,V_classificazione_vociBulk clas, CompoundFindClause clauses) 
		throws ComponentException
{
	Parametri_cnrBulk param= parametriCnr(userContext);
	SQLBuilder sql =  getHome(userContext, V_classificazione_vociBulk.class).createSQLBuilder();
	sql.addTableToHeader("UNITA_ORGANIZZATIVA");
	sql.addTableToHeader("CDR");
	sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
	sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.NR_LIVELLO", sql.EQUALS,param.getLivello_pdg_decis_etr());
	sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.ESERCIZIO", sql.EQUALS, param.getEsercizio());
	sql.addSQLClause("AND", "CDR.CD_CENTRO_RESPONSABILITA", sql.EQUALS, dettaglio.getCd_centro_responsabilita());
	sql.addSQLJoin("CDR.CD_UNITA_ORGANIZZATIVA","UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA");
	sql.openParenthesis("AND");
	sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
	sql.openParenthesis("OR");		        
	sql.addSQLClause("AND", "UNITA_ORGANIZZATIVA.CD_TIPO_UNITA", sql.NOT_EQUALS, Tipo_unita_organizzativaHome.TIPO_UO_SAC);
	sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_CLASS_SAC", sql.EQUALS, "N");
	sql.closeParenthesis();  		      
	sql.closeParenthesis();
	sql.addSQLClause("AND", "V_CLASSIFICAZIONE_VOCI.FL_SOLO_GESTIONE", sql.EQUALS, "N");
	if (clauses != null) 
		 sql.addClause(clauses);
	return sql;
}/**
 * Ricerca i parametri ente per l'anno di esercizio in scrivania
 * @param aUC UserContext
 * @return Parametri_cnrBulk contenente i parametri ente
 * @throws it.cnr.jada.comp.ComponentException
 */
public Parametri_cnrBulk parametriCnr(UserContext aUC) throws it.cnr.jada.comp.ComponentException {
	Parametri_cnrBulk param;
	try {
		 param = (Parametri_cnrBulk) getHome( aUC, Parametri_cnrBulk.class ).findByPrimaryKey( 
			new Parametri_cnrBulk(
				((CNRUserContext) aUC).getEsercizio()));
	} catch (PersistencyException ex) {
		throw handleException(ex);
	} catch (ComponentException ex) {
		throw handleException(ex);
	}
	if (param == null) {
		throw new ApplicationException("Parametri CNR non trovati.");
	}
	return param;
}
public SQLBuilder selectNaturaByClause(UserContext aUC, Pdg_Modulo_EntrateBulk dettaglio,NaturaBulk natura,CompoundFindClause clauses)
	throws ComponentException
{
	SQLBuilder sql =  getHome(aUC, NaturaBulk.class).createSQLBuilder();
	Parametri_cnrBulk param= parametriCnr(aUC);
	sql.addClause( clauses );	
	sql.setDistinctClause(true);
	sql.addTableToHeader("ASS_EV_EV ASS,ELEMENTO_VOCE E,V_CLASSIFICAZIONE_VOCI_ALL V");
	sql.addSQLJoin("natura.cd_natura", "ass.cd_natura");
	sql.addSQLJoin("e.id_classificazione", "v.id_classificazione");
	sql.addSQLJoin("e.cd_elemento_voce", "ass.cd_elemento_voce");
	sql.addSQLJoin("v.esercizio", "ass.esercizio");
	sql.addSQLJoin("v.ti_gestione", "ass.ti_gestione");
	
	sql.addSQLClause("AND", "v.TI_GESTIONE", sql.EQUALS, Elemento_voceHome.GESTIONE_ENTRATE);
	sql.addSQLClause("AND", "v.CD_LIV"+param.getLivello_pdg_decis_etr(), sql.EQUALS,dettaglio.getClassificazione_voci().getCd_classificazione());
	sql.addSQLClause("AND", "V.FL_MASTRINO", sql.EQUALS,"Y");
	sql.addSQLClause("AND", "V.ESERCIZIO", sql.EQUALS, dettaglio.getEsercizio());
	
	return sql;
} 
public java.util.Collection findNatura(UserContext aUC,Pdg_Modulo_EntrateBulk dett) throws ComponentException,it.cnr.jada.persistency.PersistencyException {

	if (dett.getClassificazione_voci() == null || dett ==null) return null;

	return getHome(aUC, NaturaBulk.class).fetchAll(selectNaturaByClause(aUC,dett, null, null));
}
	
public OggettoBulk inizializzaBulkPerModifica (UserContext userContext,OggettoBulk bulk) throws ComponentException
{	
	Pdg_moduloBulk testata = (Pdg_moduloBulk)bulk;
	try {
		testata = (Pdg_moduloBulk)getHome(userContext,Pdg_moduloBulk.class).findByPrimaryKey(testata);		
		Pdg_Modulo_EntrateHome dettHome = (Pdg_Modulo_EntrateHome)getHome(userContext,Pdg_Modulo_EntrateBulk.class);
		testata.setDettagli_entrata(new BulkList(dettHome.findDetailsFor(testata)));
		aggiornaImportiTotali(userContext,testata);
		
		testata.getProgetto().setOtherField(
				(Progetto_other_fieldBulk)getHome(userContext, Progetto_other_fieldBulk.class)
				.findByPrimaryKey(new Progetto_other_fieldBulk(testata.getProgetto().getPg_progetto())));

	    for (Iterator dett = testata.getDettagli_entrata().iterator();dett.hasNext();){
			Pdg_Modulo_EntrateBulk pdg_modulo_entrate = (Pdg_Modulo_EntrateBulk)dett.next();
			pdg_modulo_entrate.setNature(((PdgModuloEntrateComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRPREVENT01_EJB_Pdg_Modulo_EntrateComponentSession",it.cnr.contab.prevent01.ejb.PdgModuloEntrateComponentSession.class)).findNatura(userContext, pdg_modulo_entrate));
	    }	    
		getHomeCache(userContext).fetchAll(userContext,dettHome);
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	} catch (DetailedRuntimeException e) {
		throw new ComponentException(e);
	} catch (RemoteException e) {
		throw new ComponentException(e);
	} 	
	return testata;
}

	public boolean isUtenteEnte(UserContext userContext) throws ComponentException {
		try {
			CdsHome cds_home = (CdsHome)getHome(userContext, CdsBulk.class);
			CdsBulk	cds_scrivania = (CdsBulk)cds_home.findByPrimaryKey(new CdsBulk(CNRUserContext.getCd_cds(userContext)));

			if (cds_scrivania.getCd_tipo_unita().equals(Tipo_unita_organizzativaHome.TIPO_UO_ENTE))
				return true;
			return false;
			} catch(Throwable e) {
				throw handleException(e);
		}	
	}
	public Pdg_moduloBulk aggiornaImportiTotali(it.cnr.jada.UserContext uc, Pdg_moduloBulk testata) throws ComponentException {

		SimpleBulkList dettaglio= testata.getDettagli_entrata();
		if (dettaglio == null)
			return testata;

		java.math.BigDecimal totale= new java.math.BigDecimal(0);
		totale= totale.setScale(2, java.math.BigDecimal.ROUND_HALF_UP);

		try {
			for (Iterator i= dettaglio.iterator(); i.hasNext();) {
				Pdg_Modulo_EntrateBulk riga= (Pdg_Modulo_EntrateBulk) i.next();
					totale= totale.add(riga.getIm_entrata());
				}
		} catch (Throwable t) {
			throw handleException(testata, t);
		}
		testata.setImporto_progetto(totale);
		return testata;
	}
	public Pdg_Modulo_EntrateBulk getPdgModuloEntrateBulk(it.cnr.jada.UserContext userContext, V_cons_pdgp_pdgg_etrBulk consPdg) throws ComponentException {
		try {
			Pdg_Modulo_EntrateHome pdgEtrhome = (Pdg_Modulo_EntrateHome)getHome(userContext, Pdg_Modulo_EntrateBulk.class);
			Pdg_Modulo_EntrateBulk	pdgEtrBulk = (Pdg_Modulo_EntrateBulk)pdgEtrhome.findByPrimaryKey(new Pdg_Modulo_EntrateBulk(consPdg.getEsercizio(),consPdg.getCd_centro_responsabilita(),consPdg.getPg_progetto(),consPdg.getCd_natura(),consPdg.getId_classificazione(),consPdg.getPg_dettaglio(),consPdg.getCd_cds_area()));
			return pdgEtrBulk;
		} catch(Throwable e) {
			throw handleException(e);
		}
	}

	public SQLBuilder selectVoce_piano_economicoByClause(UserContext userContext, Pdg_Modulo_EntrateBulk dettaglio, Voce_piano_economico_prgBulk vocePiano, CompoundFindClause clause) throws ComponentException, PersistencyException {
		Voce_piano_economico_prgHome vocePianoHome = (Voce_piano_economico_prgHome)getHome(userContext, Voce_piano_economico_prgBulk.class);
		Integer pgProgetto=null;
 		if (dettaglio!=null && dettaglio.getPg_progetto()!=null)
 			pgProgetto = dettaglio.getPg_progetto();

		SQLBuilder sql = vocePianoHome.findVocePianoEconomicoPrgList(pgProgetto);

		if (clause != null) 
			sql.addClause(clause);
		return sql;
	}
}
