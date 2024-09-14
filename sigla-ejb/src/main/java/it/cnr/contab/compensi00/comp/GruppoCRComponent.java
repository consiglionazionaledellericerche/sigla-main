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

package it.cnr.contab.compensi00.comp;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.anagraf00.core.bulk.BancaBulk;
import it.cnr.contab.anagraf00.core.bulk.BancaHome;
import it.cnr.contab.anagraf00.core.bulk.Modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoHome;
import it.cnr.contab.anagraf00.ejb.AnagraficoComponentSession;
import it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneHome;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_crHome;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_detBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_detHome;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_uoBulk;
import it.cnr.contab.compensi00.tabrif.bulk.Gruppo_cr_uoHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.contab.config00.sto.bulk.Tipo_unita_organizzativaHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaHome;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_cori_detBulk;
import it.cnr.contab.cori00.docs.bulk.Liquid_gruppo_cori_detHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class GruppoCRComponent extends it.cnr.jada.comp.CRUDComponent{
public GruppoCRComponent() {
	super();
}


/**
  * Viene richiesta la lista delle Modalita di Pagamento associate ad un Terzo
  *
  * Pre-post-conditions:
  *
  * Nome: Terzo NON selezionato
  * Pre: Non è stato selezionato un Terzo per il gruppo_det
  * Post: Non vengono caricate le modalita di pagamento
  *
  * Nome: Terzo selezionato
  * Pre: E' stato selezionato un Terzo valido per il gruppo_det
  * Post: Viene restituita la lista delle Modalita di pagamento associate al Terzo
  *
  * @param	userContext	lo UserContext che ha generato la richiesta
  * @param	bulk l'OggettoBulk da completare
  * @return	La lista delle modalita di pagamento associate al terzo
  *
**/
public java.util.Collection findModalitaOptions(UserContext userContext, OggettoBulk bulk) throws ComponentException {

	try {

		Gruppo_cr_detBulk gruppo_det = (Gruppo_cr_detBulk)bulk;
		if (gruppo_det.getTerzo() == null)
			return null;

		TerzoHome terzoHome = (TerzoHome)getHome(userContext,TerzoBulk.class);
		return terzoHome.findRif_modalita_pagamento(gruppo_det.getTerzo(),null);

	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(bulk, ex);
	}catch (it.cnr.jada.persistency.IntrospectionException ex){
		throw handleException(bulk, ex);
	}
}


public SQLBuilder selectComuneByClause(UserContext userContext, Gruppo_cr_detBulk gruppo_det,ComuneBulk comune, CompoundFindClause clauses) throws ComponentException, PersistencyException {

	ComuneHome comuneHome = (ComuneHome)getHome(userContext, ComuneBulk.class);
	clauses.addClause("AND", "TI_ITALIANO_ESTERO", SQLBuilder.EQUALS, NazioneBulk.ITALIA);
	return comuneHome.selectByClause(clauses);
}
public SQLBuilder selectBancaByClause(UserContext userContext, Gruppo_cr_detBulk gruppo_det, BancaBulk banca, CompoundFindClause clauses) throws ComponentException {

	BancaHome bancaHome = (BancaHome)getHome(userContext, it.cnr.contab.anagraf00.core.bulk.BancaBulk.class);
	return bancaHome.selectBancaFor(gruppo_det.getModalitaPagamento(), gruppo_det.getCd_terzo_versamento());
}
public java.util.List findListaBanche(UserContext userContext, Gruppo_cr_detBulk det) throws ComponentException{

	try {
		if(det.getTerzo() == null || det.getTerzo().getCd_terzo()==null) 
			return null;

		return getHome(userContext, BancaBulk.class).fetchAll(selectBancaByClause(userContext, det, null, null));
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}

public void CreaperTutteUOSAC(UserContext userContext, Gruppo_crBulk ass)  throws ComponentException{
	Unita_organizzativaHome home=(Unita_organizzativaHome)getHome(userContext, Unita_organizzativaBulk.class,"V_UNITA_ORGANIZZATIVA_VALIDA");
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql.addSQLClause("AND", "FL_CDS", SQLBuilder.EQUALS,"N");
	sql.addSQLClause("AND", "CD_TIPO_UNITA", SQLBuilder.EQUALS,Tipo_unita_organizzativaHome.TIPO_UO_SAC);
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession) it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", AnagraficoComponentSession.class);
	try {
		String cdsSAC = sess.getCdsSAC(userContext, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		if (cdsSAC != null){
			sql.addSQLClause("AND", "CD_UNITA_PADRE", SQLBuilder.EQUALS,cdsSAC);
		} else {
			throw new it.cnr.jada.comp.ApplicationException("CDS SAC non presente nei parametri.");
		}
	} catch (RemoteException e) {
		throw  new ComponentException(e);
	}
	 try {
			List canc = home.fetchAll(sql);
			for (Iterator i=canc.iterator();i.hasNext();){
				Unita_organizzativaBulk bulk=(Unita_organizzativaBulk)i.next();
				if (!associazione_esistente(userContext,ass, bulk)){
					Gruppo_cr_uoBulk nuovo=new Gruppo_cr_uoBulk();
					nuovo.setUnita_organizzativa(bulk);
					nuovo.setFl_accentrato(new Boolean(false));
					nuovo.setGruppo(new Gruppo_crBulk(ass.getEsercizio(),ass.getCd_gruppo_cr()));
					nuovo.setToBeCreated();
					super.creaConBulk(userContext, nuovo);
				}
			}
	 } catch (PersistencyException e) {
			handleException(e);
	} catch (SQLException e) {
		handleException(e);
	} 
}
private boolean associazione_esistente(UserContext userContext,Gruppo_crBulk ass, Unita_organizzativaBulk bulk) throws ComponentException, SQLException{
	Gruppo_cr_uoHome home_cr=(Gruppo_cr_uoHome)getHome(userContext, Gruppo_cr_uoBulk.class);
	SQLBuilder sql_esistenti = home_cr.createSQLBuilder();
	sql_esistenti.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
	sql_esistenti.addSQLClause("AND", "CD_GRUPPO_CR", SQLBuilder.EQUALS,ass.getCd_gruppo_cr());
	sql_esistenti.addSQLClause("AND", "CD_UNITA_ORGANIZZATIVA", SQLBuilder.EQUALS,bulk.getCd_unita_organizzativa());
	
	return sql_esistenti.executeExistsQuery(getConnection(userContext));
}
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
{	
	if (bulk == null)
		throw new ComponentException("Attenzione: non esiste alcun dettaglio corrispondente ai criteri di ricerca!");   
	Gruppo_crBulk gruppo = (Gruppo_crBulk)bulk;
	gruppo =(Gruppo_crBulk)super.inizializzaBulkPerModifica(aUC, gruppo);
	Gruppo_cr_detHome dettHome = (Gruppo_cr_detHome)getHome(aUC,Gruppo_cr_detBulk.class);
	try {	 
		gruppo.setDettagli_col(new BulkList(dettHome.getDetailsFor(gruppo)));
		getHomeCache(aUC).fetchAll(aUC,dettHome);
		for (java.util.Iterator i= gruppo.getDettagli_col().iterator();i.hasNext();){
			 Gruppo_cr_detBulk riga= (Gruppo_cr_detBulk) i.next();
			 riga =(Gruppo_cr_detBulk)super.inizializzaBulkPerModifica(aUC, riga);			 
			 BancaHome  home= (BancaHome)getHome(aUC, BancaBulk.class);
	         riga.setBanca((BancaBulk)home.findByPrimaryKey(aUC,riga.getBanca()));
		}
	 } catch (PersistencyException e) {
		 throw new ComponentException (e);				
	 }
	return gruppo;
}
public void initializeKeysAndOptionsInto(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException{
	if (oggettobulk instanceof Gruppo_cr_detBulk){	
		java.util.Collection tipi = findModalitaOptions(usercontext,oggettobulk);
		Gruppo_cr_detBulk gruppo_det = (Gruppo_cr_detBulk)oggettobulk;
		gruppo_det.setModalitaOptions(new BulkList(tipi));
		gruppo_det.setBancaOptions(findListaBanche(usercontext, gruppo_det));			
		super.initializeKeysAndOptionsInto(usercontext,oggettobulk);
	}
}
	public Gruppo_cr_detBulk completaTerzo(UserContext userContext, Gruppo_cr_detBulk det,TerzoBulk terzo) 
	throws ComponentException {
		if (det != null) {
			det.setTerzo(terzo);
			det.setModalitaOptions(findModalitaOptions(userContext, det));
			det.setBancaOptions(findBancaOptions(userContext, det));
		}
	return det;
}


public List findBancaOptions (UserContext userContext,Gruppo_cr_detBulk det) throws ComponentException
{
	try {
		if ( det.getTerzo() != null && det.getTerzo().getCd_terzo()!=null){
			det.setTerzo((TerzoBulk)getHome(userContext, TerzoBulk.class).findByPrimaryKey(new TerzoBulk(det.getTerzo().getCd_terzo())));
			
			if ( det.getModalitaOptions()!= null && !det.getModalitaOptions().isEmpty() && det.getModalitaPagamento().getCd_modalita_pag() == null)
				det.setModalitaPagamento((Rif_modalita_pagamentoBulk)new BulkList(det.getModalitaOptions()).get(0));

				SQLBuilder sql = getHome( userContext, BancaBulk.class ).createSQLBuilder();
				sql.addClause( "AND", "cd_terzo", sql.EQUALS, det.getTerzo().getCd_terzo() );
				//sql.addSQLClause("AND", "BANCA.CD_TERZO_DELEGATO", sql.ISNULL, null);
				sql.addSQLClause("AND", "BANCA.FL_CANCELLATO", sql.EQUALS, "N");
				sql.addSQLClause("AND", "BANCA.PG_BANCA", sql.EQUALS, det.getPg_banca());
				sql.addOrderBy("FL_CC_CDS DESC");		
				if (det.getModalitaPagamento() != null && det.getModalitaPagamento().getCd_modalita_pag() != null ){	
					SQLBuilder sql2 = getHome( userContext, Modalita_pagamentoBulk.class ).createSQLBuilder();
					sql2.setHeader( "SELECT DISTINCT TI_PAGAMENTO " );
					sql2.addTableToHeader( "rif_modalita_pagamento" );
					sql2.addSQLClause( "AND" , "modalita_pagamento.cd_terzo", sql.EQUALS, det.getTerzo().getCd_terzo() );
					sql2.addSQLClause( "AND" , "modalita_pagamento.cd_modalita_pag", sql.EQUALS, det.getModalitaPagamento().getCd_modalita_pag() );
					sql2.addSQLJoin( "modalita_pagamento.cd_modalita_pag", "rif_modalita_pagamento.cd_modalita_pag" );
					//sql2.addSQLClause("AND", "MODALITA_PAGAMENTO.CD_TERZO_DELEGATO", sql.ISNULL, null);			
					sql.addSQLClause( "AND", "TI_PAGAMENTO" , sql.EQUALS, sql2 );
				}	 
		List result = getHome( userContext, BancaBulk.class ).fetchAll(sql);
		if ( result.size() == 0 )
			throw new ApplicationException("Non esistono o non sono valide le coordinate bancarie associate per il terzo " + det.getTerzo().getCd_terzo());
		return result;	
	}
		else
			return null;
	}catch (it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
public void validaCancellazioneDettaglio(UserContext userContext, Gruppo_cr_detBulk det) throws it.cnr.jada.comp.ComponentException {
	if (isDettaglioInLiquidazioneGruppoCORI(userContext, det))
		throw new ComponentException("La riga non può essere eliminata perchè utilizzata nella liquidazione CORI.");
			
}
private boolean isDettaglioInLiquidazioneGruppoCORI(UserContext userContext, Gruppo_cr_detBulk det)  throws it.cnr.jada.comp.ComponentException {
	if (det.isToBeCreated())
		return false;

	try {
			Liquid_gruppo_cori_detHome home = (Liquid_gruppo_cori_detHome)getHome(userContext,Liquid_gruppo_cori_detBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,det.getEsercizio());
			sql.addSQLClause("AND","CD_GRUPPO_CR",SQLBuilder.EQUALS,det.getCd_gruppo_cr());
			sql.addSQLClause("AND","CD_REGIONE",SQLBuilder.EQUALS,det.getCd_regione());
			sql.addSQLClause("AND","PG_COMUNE",SQLBuilder.EQUALS,det.getPg_comune());
			
			return sql.executeExistsQuery(home.getConnection());
	} catch (ComponentException e) {
		throw handleException(e);
	} catch (SQLException e) {
		throw handleException(e);
	}
}

public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
	
	Gruppo_crBulk gruppo=new Gruppo_crBulk();
	Gruppo_cr_detBulk gruppo_det=new Gruppo_cr_detBulk();
	if (oggettobulk instanceof Gruppo_crBulk){
		 gruppo=(Gruppo_crBulk)oggettobulk;
	}
	 for(Iterator i=gruppo.getDettagli_col().iterator();i.hasNext();){
		 gruppo_det=(Gruppo_cr_detBulk)i.next();
		 creaConBulk(usercontext, gruppo_det);
	 }
	 return super.creaConBulk(usercontext, gruppo);
}

public OggettoBulk modificaConBulk(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
	Gruppo_crBulk gruppo=(Gruppo_crBulk)oggettobulk;
	Gruppo_cr_detBulk gruppo_det=new Gruppo_cr_detBulk();
	if (oggettobulk instanceof Gruppo_crBulk){
		 gruppo=(Gruppo_crBulk)oggettobulk;
	}
//	 for(Iterator i=gruppo.getDettagli_col().iterator();i.hasNext();){
//		 gruppo_det=(Gruppo_cr_detBulk)i.next();
//		 super.modificaConBulk(usercontext, gruppo_det);
//	 }
	 return super.modificaConBulk(usercontext, gruppo);
}
public boolean controllaEsistenzaGruppo(UserContext usercontext,Gruppo_crBulk gruppo) throws ComponentException{

	try {
			Gruppo_crHome home = (Gruppo_crHome)getHome(usercontext,Gruppo_crBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
			sql.addSQLClause("AND","ESERCIZIO",SQLBuilder.EQUALS,gruppo.getEsercizio());
			sql.addSQLClause("AND","CD_GRUPPO_CR",SQLBuilder.EQUALS,gruppo.getCd_gruppo_cr());
			java.util.List list = getHome(usercontext,Gruppo_crBulk.class).fetchAll(sql);
			if (!list.isEmpty())
				return true;
			
	} catch (PersistencyException e) {
		throw handleException(e);
	} catch (ComponentException e) {
		throw handleException(e);
	}

	return false;
}
}
