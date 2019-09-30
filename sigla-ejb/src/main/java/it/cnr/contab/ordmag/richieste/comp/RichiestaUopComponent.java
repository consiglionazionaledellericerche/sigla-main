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

package it.cnr.contab.ordmag.richieste.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJBException;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_voceHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraHome;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopHome;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
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
//			//assegna un progressivo al documento all'atto della creazione.
		validaRichiesta(userContext, richiesta);
		assegnaProgressivo(userContext, richiesta);
		richiesta = (RichiestaUopBulk)super.creaConBulk(userContext, richiesta);
		return richiesta;
	}

	private void validaRichiesta(it.cnr.jada.UserContext userContext, RichiestaUopBulk richiesta) throws it.cnr.jada.comp.ComponentException{
		if (richiesta.getRigheRichiestaColl() == null || richiesta.getRigheRichiestaColl().size() == 0){
			throw new ApplicationException ("Non è possibile salvare una richiesta senza dettagli.");
		}
    	for (java.util.Iterator i= richiesta.getRigheRichiestaColl().iterator(); i.hasNext();) {
    		RichiestaUopRigaBulk riga = (RichiestaUopRigaBulk) i.next();
    		if (riga != null){
    			if (riga.getCdElementoVoce() != null && riga.getCdCategoriaGruppo() != null){
    				Categoria_gruppo_voceHome home = (Categoria_gruppo_voceHome)getHome(userContext, Categoria_gruppo_voceBulk.class);
    				SQLBuilder sql = home.createSQLBuilder();
    				
    				sql.addSQLClause("AND", "ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );
    				sql.addSQLClause("AND", "CD_CATEGORIA_GRUPPO_INVENT", sql.EQUALS, riga.getCdCategoriaGruppo());
    				sql.addSQLClause("AND", "CD_ELEMENTO_VOCE", sql.EQUALS, riga.getCdElementoVoce());
    				
    				List list;
					try {
						list = home.fetchAll(sql);
					} catch (PersistencyException e) {
						throw new ComponentException(e);
					}
    				if (list == null || list.size() == 0){
        				throw new ApplicationException ("Sulla riga numero "+riga.getRiga()+" è stata indicata una voce di bilancio non utilizzabile per "+riga.getCdBeneServizio());
    				}
    			}
    			String value = null;
    			if (richiesta.isDefinitivaOInviata() && riga.getCdElementoVoce() == null){
        			try {
        				value = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, richiesta.getEsercizio(), null, Configurazione_cnrBulk.PK_OBBLIGATORIETA_ORDINI, Configurazione_cnrBulk.SK_VOCE_RICHIESTA).getVal01();
        			} catch (RemoteException e) {
        				throw new ComponentException(e);
        			} catch (EJBException e) {
        				throw new ComponentException(e);
        			}
        			if (value!= null && value.equals("Y")){
        				throw new ApplicationException ("Sulla riga numero "+riga.getRiga()+" è necessario indicare la voce di bilancio.");
        			}
    			} 
    			if (richiesta.isDefinitivaOInviata() && riga.getProgetto() == null || riga.getProgetto().getPg_progetto() == null){
    				value = null;
    				try {
        				value = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, richiesta.getEsercizio(), null, Configurazione_cnrBulk.PK_OBBLIGATORIETA_ORDINI, Configurazione_cnrBulk.SK_PROGETTO_RICHIESTA).getVal01();
        			} catch (RemoteException e) {
        				throw new ComponentException(e);
        			} catch (EJBException e) {
        				throw new ComponentException(e);
        			}
        			if (value!= null && value.equals("Y")){
        				throw new ApplicationException ("Sulla riga numero "+riga.getRiga()+" è necessario indicare il progetto.");
        			}
    			}
    			if (richiesta.isDefinitivaOInviata() && (riga.getCentroResponsabilita() == null || riga.getCentroResponsabilita().getCd_centro_responsabilita() == null || 
    					riga.getLineaAttivita() == null || riga.getLineaAttivita().getCd_linea_attivita() == null)){
    				value = null;
    				try {
        				value = Utility.createConfigurazioneCnrComponentSession().getConfigurazione( userContext, richiesta.getEsercizio(), null, Configurazione_cnrBulk.PK_OBBLIGATORIETA_ORDINI, Configurazione_cnrBulk.SK_GAE_RICHIESTA).getVal01();
        			} catch (RemoteException e) {
        				throw new ComponentException(e);
        			} catch (EJBException e) {
        				throw new ComponentException(e);
        			}
        			if (value!= null && value.equals("Y")){
            			if (riga.getCentroResponsabilita() == null || riga.getCentroResponsabilita().getCd_centro_responsabilita() == null){
            				throw new ApplicationException ("Sulla riga numero "+riga.getRiga()+" è necessario indicare il CDR.");
            			}
            			if (riga.getLineaAttivita() == null || riga.getLineaAttivita().getCd_linea_attivita() == null){
            				throw new ApplicationException ("Sulla riga numero "+riga.getRiga()+" è necessario indicare la GAE.");
            			}
        			}
    			}
    		}
    	}
	}
	
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

@Override
public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
	return inizializzaBulk(usercontext, oggetto);
}

private OggettoBulk inizializzaBulk(UserContext usercontext, OggettoBulk oggetto) throws ComponentException {
	oggetto = inizializzaRichiesta(usercontext, oggetto);
	return oggetto;
}

@Override
public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	RichiestaUopBulk richiesta = (RichiestaUopBulk)super.inizializzaBulkPerModifica(usercontext, oggettobulk);

	
	it.cnr.jada.bulk.BulkHome home= getHome(usercontext, RichiestaUopRigaBulk.class);
    it.cnr.jada.persistency.sql.SQLBuilder sql= home.createSQLBuilder();
    sql.addClause("AND", "numero", sql.EQUALS, richiesta.getNumero());
    sql.addClause("AND", "cdCds", sql.EQUALS, richiesta.getCdCds());
    sql.addClause("AND", "cdUnitaOperativa", sql.EQUALS, richiesta.getCdUnitaOperativa());
    sql.addClause("AND", "esercizio", sql.EQUALS, richiesta.getEsercizio());
    sql.addClause("AND", "cdNumeratore", sql.EQUALS, richiesta.getCdNumeratore());
    try {
    	richiesta.setRigheRichiestaColl(new it.cnr.jada.bulk.BulkList(home.fetchAll(sql)));

    	for (java.util.Iterator i= richiesta.getRigheRichiestaColl().iterator(); i.hasNext();) {
    		OggettoBulk rigabulk= (OggettoBulk) i.next();
    		RichiestaUopRigaBulk riga= (RichiestaUopRigaBulk) rigabulk;
    		if (riga.getBeneServizio() != null){
    			Bene_servizioHome Home = (Bene_servizioHome)getHome(usercontext, Bene_servizioBulk.class);
    			Bene_servizioBulk bene = (Bene_servizioBulk)Home.findByPrimaryKey(new Bene_servizioBulk(riga.getCdBeneServizio()));
    			riga.setBeneServizio(bene);
    		}
    		if (riga.getUnitaMisura() != null){
    			UnitaMisuraHome Home = (UnitaMisuraHome)getHome(usercontext, UnitaMisuraBulk.class);
    			UnitaMisuraBulk um = (UnitaMisuraBulk)Home.findByPrimaryKey(new UnitaMisuraBulk(riga.getCdUnitaMisura()));
    			riga.setUnitaMisura(um);
    		}
    		if (riga.getElementoVoce() != null){
    			Elemento_voceHome Home = (Elemento_voceHome)getHome(usercontext, Elemento_voceBulk.class);
    			Elemento_voceBulk elem_voce = (Elemento_voceBulk)Home.findByPrimaryKey(new Elemento_voceBulk(riga.getCdElementoVoce(), riga.getEsercizioVoce(), riga.getTiAppartenenza(), riga.getTiGestione()));
    			riga.setElementoVoce(elem_voce);
    		}
    		if (riga.getCentroResponsabilita() != null){
    			CdrHome Home = (CdrHome)getHome(usercontext, CdrBulk.class);
    			CdrBulk cdr = (CdrBulk)Home.findByPrimaryKey(new CdrBulk(riga.getCdCentroResponsabilita()));
    			riga.setCentroResponsabilita(cdr);
    		}
    		if (riga.getLineaAttivita() != null){
    			WorkpackageHome Home = (WorkpackageHome)getHome(usercontext, WorkpackageBulk.class);
    			WorkpackageBulk wp = (WorkpackageBulk)Home.findByPrimaryKey(new WorkpackageBulk(riga.getCdCentroResponsabilita(), riga.getCdLineaAttivita()));
    			riga.setLineaAttivita(wp);
    		}
//    		if (riga.getProgetto() != null){
//    			ProgettoHome Home = (ProgettoHome)getHome(usercontext, ProgettoBulk.class);
//    			ProgettoGestBulk prog = (ProgettoGestBulk)Home.findByPrimaryKey(new ProgettoGestBulk(riga.getPgProgetto()));
//    			riga.setpro(prog);
//    		}
    		if (riga.getObbligazione() != null){
    			ObbligazioneHome Home = (ObbligazioneHome)getHome(usercontext, ObbligazioneBulk.class);
    			ObbligazioneBulk obbl = (ObbligazioneBulk)Home.findByPrimaryKey(new ObbligazioneBulk(riga.getCdCdsObbl(), riga.getEsercizioObbl(), riga.getEsercizioOrigObbl(), riga.getPgObbligazione()));
    			riga.setObbligazione(obbl);
    		}
    		if (riga.getCategoriaGruppoInvent() != null){
    			Categoria_gruppo_inventHome Home = (Categoria_gruppo_inventHome)getHome(usercontext, Categoria_gruppo_inventBulk.class);
    			Categoria_gruppo_inventBulk cat = (Categoria_gruppo_inventBulk)Home.findByPrimaryKey(new Categoria_gruppo_inventBulk(riga.getCdCategoriaGruppo()));
    			riga.setCategoriaGruppoInvent(cat);
    		}
    	}

    } catch (PersistencyException e) {
    	throw handleException(e);
    }
        
	return inizializzaBulk(usercontext, (OggettoBulk)richiesta);
}

@Override
public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	oggettobulk = super.inizializzaBulkPerRicerca( usercontext, oggettobulk );
//	try
//		{
//			if ( oggettobulk instanceof ObbligazioneBulk)
//			{
//				ObbligazioneBulk obbligazione = (ObbligazioneBulk) bulk;
//				obbligazione.setCds( (CdsBulk) getHome( aUC, CdsBulk.class).findByPrimaryKey( new CdsBulk(((CNRUserContext) aUC).getCd_cds())));
//				obbligazione.setCd_cds_origine( ((CNRUserContext) aUC).getCd_cds() );
//			// if (!((ObbligazioneHome)getHome(aUC, obbligazione.getClass())).verificaStatoEsercizio(obbligazione))
//			//	throw handleException( new ApplicationException( "Non e' possibile creare obbligazioni: esercizio non ancora aperto!") );
//	   	
//				return super.inizializzaBulkPerRicerca( aUC, obbligazione );
//			}
//			else
//				return super.inizializzaBulkPerRicerca( aUC, bulk );		
//		}
//		catch ( it.cnr.jada.persistency.PersistencyException e )
//		{
//			throw handleException(bulk, e);
//		}
	return inizializzaBulk(usercontext, oggettobulk);
}

@Override
public OggettoBulk inizializzaBulkPerRicercaLibera(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	OggettoBulk oggetto = super.inizializzaBulkPerRicercaLibera(usercontext, oggettobulk);
	return inizializzaRichiesta(usercontext, oggetto);
}
public SQLBuilder selectCentroResponsabilitaByClause(
		UserContext userContext, RichiestaUopRigaBulk pdg, CdrBulk cdr,
		CompoundFindClause clause) throws PersistencyException, ComponentException {

	SQLBuilder sql = getHome(userContext, CdrBulk.class, "V_CDR_VALIDO").createSQLBuilder();
	sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));

	if (!isCdrUo(userContext)){
		sql.addSQLClause("AND","V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, CNRUserContext.getCd_cdr(userContext));
	} else {
		sql.addTableToHeader("V_STRUTTURA_ORGANIZZATIVA", "B");		
		sql.addSQLJoin("V_CDR_VALIDO.ESERCIZIO", "B.ESERCIZIO");
		sql.addSQLJoin("V_CDR_VALIDO.CD_UNITA_ORGANIZZATIVA", "B.CD_UNITA_ORGANIZZATIVA");
		sql.addSQLJoin("V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA", "B.CD_CENTRO_RESPONSABILITA");
		sql.addSQLClause("AND", "B.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
		sql.addSQLClause("AND","B.CD_CDS",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
		sql.addSQLClause("AND","B.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	}

	if (clause != null)
		sql.addClause(clause);
	sql.addOrderBy("V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
	
	return sql;
}

protected Boolean isCdrUo(UserContext userContext) throws ComponentException, PersistencyException {
	V_struttura_organizzativaHome homeStr =(V_struttura_organizzativaHome)getHome(userContext, V_struttura_organizzativaBulk.class );
	SQLBuilder sqlStr =homeStr.createSQLBuilder();
	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.ESERCIZIO",SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CDS",SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA",SQLBuilder.EQUALS, CNRUserContext.getCd_unita_organizzativa(userContext));
	sqlStr.addSQLClause("AND","V_STRUTTURA_ORGANIZZATIVA.CD_CENTRO_RESPONSABILITA",SQLBuilder.EQUALS, CNRUserContext.getCd_cdr(userContext));
	sqlStr.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.CD_TIPO_LIVELLO", SQLBuilder.EQUALS, V_struttura_organizzativaHome.LIVELLO_CDR);
	sqlStr.addSQLClause("AND", "V_STRUTTURA_ORGANIZZATIVA.FL_CDR_UO", SQLBuilder.EQUALS, "Y");

	List listStr=homeStr.fetchAll(sqlStr);
	if (listStr != null && listStr.size() == 1){
		return true;
	} else {
		return false;
	}
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

public SQLBuilder selectElementoVoceByClause (UserContext userContext, 
		RichiestaUopRigaBulk dett,
		Elemento_voceBulk elementoVoce, 
		CompoundFindClause clause) throws ComponentException, PersistencyException {
	if (clause == null) clause = ((OggettoBulk)elementoVoce).buildFindClauses(null);

	SQLBuilder sql = getHome(userContext, elementoVoce).createSQLBuilder();
	
	if(clause != null) sql.addClause(clause);

	SQLBuilder sqlExists = null;
	Categoria_gruppo_voceHome abilCatVoce = (Categoria_gruppo_voceHome) getHomeCache(userContext).getHome(Categoria_gruppo_voceBulk.class);
	sqlExists = abilCatVoce.createSQLBuilder();
	sqlExists.addSQLJoin("ELEMENTO_VOCE.ESERCIZIO", "CATEGORIA_GRUPPO_VOCE.ESERCIZIO");
	sqlExists.addSQLJoin("ELEMENTO_VOCE.TI_APPARTENENZA", "CATEGORIA_GRUPPO_VOCE.TI_APPARTENENZA");
	sqlExists.addSQLJoin("ELEMENTO_VOCE.TI_GESTIONE", "CATEGORIA_GRUPPO_VOCE.TI_GESTIONE");
	sqlExists.addSQLJoin("ELEMENTO_VOCE.CD_ELEMENTO_VOCE", "CATEGORIA_GRUPPO_VOCE.CD_ELEMENTO_VOCE");
	if (dett.getCdBeneServizio() != null){
		Bene_servizioHome Home = (Bene_servizioHome)getHome(userContext, Bene_servizioBulk.class);
		Bene_servizioBulk bene = (Bene_servizioBulk)Home.findByPrimaryKey(new Bene_servizioBulk(dett.getCdBeneServizio()));
		sqlExists.addSQLClause("AND", "CATEGORIA_GRUPPO_VOCE.CD_CATEGORIA_GRUPPO",SQLBuilder.EQUALS,bene.getCd_categoria_gruppo());
	}
	sql.addSQLClause("AND", "ELEMENTO_VOCE.ESERCIZIO", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio( userContext ) );

	if (dett.getLineaAttivita() != null)
		sql.addSQLClause("AND","ELEMENTO_VOCE.CD_FUNZIONE",sql.EQUALS,dett.getLineaAttivita().getCd_funzione());

	sql.addSQLExistsClause("AND", sqlExists);

	return sql;
}
public SQLBuilder selectProgettoByClause (UserContext userContext, 
		RichiestaUopRigaBulk dett,
		ProgettoBulk prg, 
		CompoundFindClause clause) throws ComponentException, PersistencyException {
	ProgettoHome progettohome = (ProgettoHome)getHome(userContext, ProgettoBulk.class,"V_PROGETTO_PADRE");
	SQLBuilder sql = progettohome.createSQLBuilder();
	sql.addClause( clause );

	sql.addSQLClause("AND", "V_PROGETTO_PADRE.ESERCIZIO", sql.EQUALS, CNRUserContext.getEsercizio(userContext));
	
    if (prg!=null)
    	sql.addSQLClause("AND", "V_PROGETTO_PADRE.PG_PROGETTO", sql.EQUALS, prg.getPg_progetto());
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.TIPO_FASE", sql.EQUALS, ProgettoBulk.TIPO_FASE_NON_DEFINITA);
	sql.addSQLClause("AND", "V_PROGETTO_PADRE.LIVELLO", sql.EQUALS, ProgettoBulk.LIVELLO_PROGETTO_SECONDO);
	// Se uo 999.000 in scrivania: visualizza tutti i progetti
	Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHome( userContext, Unita_organizzativa_enteBulk.class).findAll().get(0);
	if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa()))
		sql.addSQLExistsClause("AND",progettohome.abilitazioniCommesse(userContext));
	if (clause != null) 
		sql.addClause(clause);

	return sql; 
}	
public SQLBuilder selectBeneServizioByClause(UserContext userContext, RichiestaUopRigaBulk riga, 
		Bene_servizioHome beneHome, Bene_servizioBulk bene, 
		CompoundFindClause compoundfindclause) throws PersistencyException{
	SQLBuilder sql = beneHome.selectByClause(userContext, compoundfindclause);
	sql.addSQLClause("AND", "FL_VALIDO", SQLBuilder.EQUALS, Bene_servizioBulk.STATO_VALIDO);
	
	return sql;
}

private String preparaFileNamePerStampa(String reportName) {
	String fileName = reportName;
	fileName = fileName.replace('/', '_');
	fileName = fileName.replace('\\', '_');
	if(fileName.startsWith("_"))
		fileName = fileName.substring(1);
	if(fileName.endsWith(".jasper"))
		fileName = fileName.substring(0, fileName.length() - 7);
	fileName = fileName + ".pdf";
	return fileName;
}

protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException 
{
	SQLBuilder sql = (SQLBuilder) super.select( userContext, clauses, bulk );
	AbilUtenteUopOperHome abilHome = (AbilUtenteUopOperHome) getHomeCache(userContext).getHome(AbilUtenteUopOperBulk.class);
	RichiestaUopBulk richiestaUopBulk = (RichiestaUopBulk)bulk;
	SQLBuilder sqlExists = null;
	sqlExists = abilHome.createSQLBuilder();
	sqlExists.addSQLJoin("RICHIESTA_UOP.CD_UNITA_OPERATIVA", "ABIL_UTENTE_UOP_OPER.CD_UNITA_OPERATIVA");
	if (!richiestaUopBulk.getIsForApprovazione()){
		sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
	} else {
		sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_TIPO_OPERAZIONE", SQLBuilder.EQUALS, TipoOperazioneOrdBulk.OPERAZIONE_VALIDAZIONE_RICHIESTA);
		sqlExists.openParenthesis("AND");
		sqlExists.addSQLClause("AND", "RICHIESTA_UOP.STATO", SQLBuilder.EQUALS, RichiestaUopBulk.STATO_DEFINITIVA);
		sqlExists.addSQLClause("OR", "RICHIESTA_UOP.STATO", SQLBuilder.EQUALS, RichiestaUopBulk.STATO_INVIATA_ORDINE);
		sqlExists.closeParenthesis();
	}
	sqlExists.addSQLClause("AND", "ABIL_UTENTE_UOP_OPER.CD_UTENTE", SQLBuilder.EQUALS, userContext.getUser());

	sql.addSQLExistsClause("AND", sqlExists);
	return sql;
}

private OggettoBulk inizializzaRichiesta(UserContext usercontext, OggettoBulk oggettobulk)
		throws ComponentException {
	RichiestaUopBulk richiesta = (RichiestaUopBulk)oggettobulk;
	try {
		RichiestaUopHome home = (RichiestaUopHome) getHomeCache(usercontext).getHome(RichiestaUopBulk.class);
		richiesta.setCdCds( ((CNRUserContext) usercontext).getCd_cds());
		if (richiesta.getCdUnitaOperativa() == null){
			UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome)getHome(usercontext, UnitaOperativaOrdBulk.class);
			SQLBuilder sql = home.selectUnitaOperativaOrdByClause(usercontext, richiesta, uopHome, new UnitaOperativaOrdBulk(), new CompoundFindClause());
			List listUop=uopHome.fetchAll(sql);
			if (listUop != null && listUop.size() == 1){
				richiesta.setUnitaOperativaOrd((UnitaOperativaOrdBulk)listUop.get(0));
				assegnaUnitaOperativaDest(usercontext, richiesta, home, uopHome);
			}
		}
		assegnaNumeratoreOrd(usercontext, richiesta, home);
	} catch (PersistencyException e){
		throw new ComponentException(e);
	}
	return richiesta;
}

private void assegnaUnitaOperativaDest(UserContext usercontext, RichiestaUopBulk richiesta, RichiestaUopHome home,
		UnitaOperativaOrdHome uopHome) throws PersistencyException, ComponentException {
	if (richiesta.getCdUnitaOperativaDest() == null){
		SQLBuilder sqlAss = home.selectUnitaOperativaOrdDestByClause(usercontext, richiesta, uopHome, new UnitaOperativaOrdBulk(), new CompoundFindClause());
		List listAssUop=uopHome.fetchAll(sqlAss);
		if (listAssUop != null && listAssUop.size() == 1){
			richiesta.setUnitaOperativaOrdDest((UnitaOperativaOrdBulk)listAssUop.get(0));
		}
	}
}

private void assegnaNumeratoreOrd(UserContext usercontext, RichiestaUopBulk richiesta, RichiestaUopHome home)
		throws PersistencyException, ComponentException {
	if (richiesta.getCdNumeratore() == null && richiesta.getCdUnitaOperativa() != null){
//			AbilUtenteUopOperHome abilHome = (AbilUtenteUopOperHome)getHome(usercontext, AbilUtenteUopOperBulk.class);
//			AbilUtenteUopOperBulk abil = new AbilUtenteUopOperBulk(usercontext.getUser(), richiesta.getCdUnitaOperativa(), TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
//			abil = (AbilUtenteUopOperBulk)abilHome.findByPrimaryKey(usercontext, abil);
//			if (abil != null){
			NumerazioneOrdHome numerazioneHome = (NumerazioneOrdHome)getHome(usercontext, NumerazioneOrdBulk.class);
			SQLBuilder sql = home.selectNumerazioneOrdByClause(usercontext, richiesta, numerazioneHome, new NumerazioneOrdBulk(), new CompoundFindClause());
			List listNum=numerazioneHome.fetchAll(sql);
			if (listNum != null && listNum.size() == 1){
				richiesta.setNumerazioneOrd((NumerazioneOrdBulk)listNum.get(0));
			}
//			}
	}
}
public Boolean isUtenteAbilitatoRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException{
	return isUtenteAbilitato(usercontext, richiesta, TipoOperazioneOrdBulk.OPERAZIONE_RICHIESTA);
}

public Boolean isUtenteAbilitatoValidazioneRichiesta(UserContext usercontext, RichiestaUopBulk richiesta) throws ComponentException, PersistencyException{
	return isUtenteAbilitato(usercontext, richiesta, TipoOperazioneOrdBulk.OPERAZIONE_VALIDAZIONE_RICHIESTA);
}

private Boolean isUtenteAbilitato(UserContext usercontext, RichiestaUopBulk richiesta, String tipoOperazione) throws ComponentException {
	if (richiesta.getCdUnitaOperativa() != null){
		AbilUtenteUopOperHome abilHome = (AbilUtenteUopOperHome)getHome(usercontext, AbilUtenteUopOperBulk.class);
		AbilUtenteUopOperBulk abil = new AbilUtenteUopOperBulk(usercontext.getUser(), richiesta.getCdUnitaOperativa(), tipoOperazione);
		try {
			abil = (AbilUtenteUopOperBulk)abilHome.findByPrimaryKey(usercontext, abil);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		}
		if (abil != null){
			return true;
		}
		return false;
	}
	return true;
}

public RichiestaUopBulk completaRichiesta(UserContext userContext, RichiestaUopBulk richiesta) throws PersistencyException, ComponentException{
	RichiestaUopHome home = (RichiestaUopHome) getHomeCache(userContext).getHome(RichiestaUopBulk.class);
	assegnaNumeratoreOrd(userContext, richiesta, home);
	UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome)getHome(userContext, UnitaOperativaOrdBulk.class);
	assegnaUnitaOperativaDest(userContext, richiesta, home, uopHome);
	return richiesta;
}

@Override
public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
	// TODO Auto-generated method stub
	RichiestaUopBulk richiesta= (RichiestaUopBulk)super.modificaConBulk(usercontext, oggettobulk);
	validaRichiesta(usercontext, richiesta);
	return richiesta;
}

public RichiestaUopRigaBulk selezionaRichiestaPerOrdine (UserContext aUC,VRichiestaPerOrdiniBulk richiesta) throws ComponentException
{
	try
	{

		RichiestaUopRigaBulk richiestaRiga = (RichiestaUopRigaBulk) getHome( aUC, RichiestaUopRigaBulk.class).findByPrimaryKey( new RichiestaUopRigaBulk( richiesta.getCdCds(), richiesta.getCdUnitaOperativa(), richiesta.getEsercizio(), richiesta.getCdNumeratore(), richiesta.getNumero(), richiesta.getRiga() ));

		if ( richiestaRiga == null )
			throw new ApplicationException( "Richiesta non esistente" );

		lockBulk( aUC, richiestaRiga );
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_ANNULLATO))
			throw new ApplicationException("La richiesta è stata annullata.");
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE))
			throw new ApplicationException("La richiesta è già stata trasformata in ordine.");
		if ( !richiestaRiga.getRichiestaUop().getStato().equals(RichiestaUopBulk.STATO_INVIATA_ORDINE))
			throw new ApplicationException("La richiesta non è stata inviata in ordine");
		richiestaRiga.setStato(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE);
		richiestaRiga.setUser( aUC.getUser());
		updateBulk( aUC, richiestaRiga );
		return richiestaRiga;
	}
	catch ( Exception e )
	{
		throw handleException( richiesta, e )	;
	}
}
}
