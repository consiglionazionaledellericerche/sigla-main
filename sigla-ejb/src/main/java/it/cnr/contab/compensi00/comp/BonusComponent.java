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

import it.cnr.contab.anagraf00.core.bulk.AnagraficoBulk;
import it.cnr.contab.anagraf00.core.bulk.TerzoBulk;
import it.cnr.contab.anagraf00.tabrif.bulk.Tipo_rapportoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.ComuneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.compensi00.docs.bulk.BonusBulk;
import it.cnr.contab.compensi00.docs.bulk.BonusHome;
import it.cnr.contab.compensi00.docs.bulk.Bonus_condizioniBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_condizioniHome;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famBulk;
import it.cnr.contab.compensi00.docs.bulk.Bonus_nucleo_famHome;
import it.cnr.contab.compensi00.docs.bulk.CompensoBulk;
import it.cnr.contab.compensi00.docs.bulk.CompensoHome;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk;
import it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoHome;
import it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

public class BonusComponent extends it.cnr.jada.comp.CRUDComponent{
public BonusComponent() {
	super();
}

public void checkCodiceFiscale(UserContext context, BonusBulk bonus) throws ComponentException,  SQLException
{
	/* verifica esattezza carattere di controllo del codice fiscale */
	if ((bonus.getCodice_fiscale() == null)||
		(bonus.getCodice_fiscale().length() != 16) ||
		(!it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(bonus.getCodice_fiscale())))
		throw new ApplicationException("Codice fiscale inserito errato.");
	if(bonus.getCodice_fiscale()!=null && bonus.getDt_registrazione()!=null){
		controlloTerzoRapportovalido(context,bonus);
		controlloBonusUnico(context,bonus);	
		controlloBonusUnicoComp(context, bonus);	
	}  
}
public BonusBulk recuperoDati(UserContext context, BonusBulk bonus) throws ComponentException, RemoteException {
	TerzoBulk terzo=null;
	SQLBuilder sql = getHome(context, TerzoBulk.class).createSQLBuilder();
	sql.addTableToHeader("ANAGRAFICO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","TERZO.CD_ANAG");
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",SQLBuilder.EQUALS,bonus.getCodice_fiscale());
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
	sql.addSQLClause("OR","TERZO.DT_FINE_RAPPORTO",SQLBuilder.GREATER_EQUALS,bonus.getDt_registrazione());
	sql.closeParenthesis();
	try {
		terzo=(TerzoBulk)getHome(context, TerzoBulk.class).fetchAll(sql).get(0);	
		if(terzo!=null && terzo.getCd_terzo()!=null){
			bonus.setTerzo((TerzoBulk)getHome(context,TerzoBulk.class).findByPrimaryKey(terzo));
			bonus.getTerzo().setAnagrafico((AnagraficoBulk)getHome(context,AnagraficoBulk.class).findByPrimaryKey(terzo.getAnagrafico()));
			bonus.getTerzo().getAnagrafico().setComune_nascita((ComuneBulk) getHome(context,ComuneBulk.class).findByPrimaryKey(terzo.getAnagrafico().getComune_nascita()));
		}
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	SQLBuilder sql_rap = getHome(context, Tipo_rapportoBulk.class).createSQLBuilder();
	sql_rap.addTableToHeader("ANAGRAFICO,RAPPORTO");
	sql_rap.addSQLJoin("ANAGRAFICO.CD_ANAG","RAPPORTO.CD_ANAG");
	sql_rap.addSQLClause("AND","RAPPORTO.CD_ANAG",sql.EQUALS,bonus.getTerzo().getAnagrafico().getCd_anag());
	sql_rap.addSQLClause("AND","RAPPORTO.DT_INI_VALIDITA",SQLBuilder.LESS_EQUALS,bonus.getDt_registrazione());
	sql_rap.addSQLClause("AND","RAPPORTO.DT_FIN_VALIDITA",SQLBuilder.GREATER_EQUALS,bonus.getDt_registrazione());
	sql_rap.addSQLJoin("RAPPORTO.CD_TIPO_RAPPORTO","TIPO_RAPPORTO.CD_TIPO_RAPPORTO");
	sql_rap.addSQLClause("AND","TIPO_RAPPORTO.FL_BONUS",SQLBuilder.EQUALS,"Y");
	Tipo_rapportoBulk tipo_rap;
	try {
		tipo_rap = (Tipo_rapportoBulk)getHome(context, Tipo_rapportoBulk.class).fetchAll(sql_rap).get(0);
	} catch (PersistencyException e) {
		throw new ComponentException(e);
	}
	bonus.setCd_trattamento(tipo_rap.getCd_trattamento());
	bonus.setTipo_rapporto(tipo_rap);
	return bonus;
}

public void checkCodiceFiscaleComponente(UserContext context, Bonus_nucleo_famBulk bonus_nucleo_fam) throws ComponentException, SQLException
{
	/* verifica esattezza carattere di controllo del codice fiscale */
	if ((bonus_nucleo_fam.getCf_componente_nucleo() ==null) ||
		(bonus_nucleo_fam.getCf_componente_nucleo().length() != 16) ||
		 !it.cnr.contab.anagraf00.util.CodiceFiscaleControllo.checkCC(bonus_nucleo_fam.getCf_componente_nucleo()))
		throw new ApplicationException("Codice fiscale inserito errato.");
	if(bonus_nucleo_fam.getCf_componente_nucleo()!=null && bonus_nucleo_fam.getBonus()!=null && bonus_nucleo_fam.getBonus().getDt_registrazione()!=null){
		controlloBonusComponenteUnico(context,bonus_nucleo_fam);
	}	  
}
private void controlloBonusUnico(UserContext context, BonusBulk bonus) throws ComponentException, SQLException {
	SQLBuilder sql = getHome(context, BonusBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","CODICE_FISCALE",sql.EQUALS,bonus.getCodice_fiscale());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bonus.getEsercizio());
	if(bonus.getPg_bonus()!=null)
		sql.addSQLClause("AND","PG_BONUS",sql.NOT_EQUALS,bonus.getPg_bonus());
	if(sql.executeCountQuery(getConnection(context))>0 )
		throw new ApplicationException("Codice Fiscale già utilizzato per richiedere il bonus.");
}
private void controlloBonusUnicoComp(UserContext context, BonusBulk bonus) throws ComponentException, SQLException {
Bonus_nucleo_famHome home = (Bonus_nucleo_famHome)getHome(context,Bonus_nucleo_famBulk.class);
SQLBuilder sql_det = home.createSQLBuilder();
sql_det.addSQLClause("AND","CF_COMPONENTE_NUCLEO",sql_det.EQUALS,bonus.getCodice_fiscale());
sql_det.addSQLClause("AND","ESERCIZIO",sql_det.EQUALS,bonus.getEsercizio());
sql_det.addSQLClause("AND","TIPO_COMPONENTE_NUCLEO",sql_det.EQUALS,Bonus_nucleo_famBulk.CONIUGE);
if(bonus.getPg_bonus()!=null)
	sql_det.addSQLClause("AND","PG_BONUS",sql_det.NOT_EQUALS,bonus.getPg_bonus());

if(sql_det.executeCountQuery(getConnection(context))>0 )
		throw new ApplicationException("Codice Fiscale già utilizzato come conuige per richiedere altro bonus.");
}
private void controlloBonusComponenteUnico(UserContext context, Bonus_nucleo_famBulk bonus) throws ComponentException, SQLException {
	SQLBuilder sql = getHome(context, BonusBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","CODICE_FISCALE",sql.EQUALS,bonus.getCf_componente_nucleo());
	sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bonus.getEsercizio());
	if(sql.executeCountQuery(getConnection(context))>0 )
		throw new ApplicationException("Codice Fiscale già utilizzato per richiedere il bonus.");
	 sql = getHome(context, Bonus_nucleo_famBulk.class).createSQLBuilder();
	 sql.addSQLClause("AND","CF_COMPONENTE_NUCLEO",sql.EQUALS,bonus.getCf_componente_nucleo());
	 sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bonus.getEsercizio());
	 if(sql.executeCountQuery(getConnection(context))>0 )
			throw new ApplicationException("Codice Fiscale già utilizzato richiedere bonus.");
	 if(bonus.getCf_componente_nucleo().compareToIgnoreCase(bonus.getBonus().getCodice_fiscale())==0)
		 throw new ApplicationException("Codice Fiscale già utilizzato come richiedente bonus.");
}

private void controlloTerzoRapportovalido(UserContext context, BonusBulk bonus) throws ComponentException, SQLException {
	
	SQLBuilder sql = getHome(context, AnagraficoBulk.class).createSQLBuilder();
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.EQUALS,bonus.getCodice_fiscale());
	sql.addSQLClause("AND","TI_ITALIANO_ESTERO",sql.EQUALS,NazioneBulk.ITALIA);
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","ANAGRAFICO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
	sql.addSQLClause("OR","ANAGRAFICO.DT_FINE_RAPPORTO",SQLBuilder.GREATER_EQUALS,bonus.getDt_registrazione());
	sql.closeParenthesis();
	if(sql.executeCountQuery(getConnection(context))==0 )
		throw new ApplicationException("Anagrafica non esistente o non valida per richiedere il bonus.");
	sql.addTableToHeader("TERZO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","TERZO.CD_ANAG");
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","TERZO.DT_FINE_RAPPORTO",SQLBuilder.ISNULL,null);
	sql.addSQLClause("OR","TERZO.DT_FINE_RAPPORTO",SQLBuilder.GREATER_EQUALS,bonus.getDt_registrazione());
	sql.closeParenthesis();
	if(sql.executeCountQuery(getConnection(context))>1 )
		throw new ApplicationException("Esiste più di un terzo valido.");
	sql = getHome(context, AnagraficoBulk.class).createSQLBuilder();
	sql.addTableToHeader("RAPPORTO,TIPO_RAPPORTO");
	sql.addSQLJoin("ANAGRAFICO.CD_ANAG","RAPPORTO.CD_ANAG");
	sql.addSQLClause("AND","ANAGRAFICO.CODICE_FISCALE",sql.EQUALS,bonus.getCodice_fiscale());
	sql.addSQLClause("AND","RAPPORTO.DT_INI_VALIDITA",SQLBuilder.LESS_EQUALS,bonus.getDt_registrazione());
	sql.addSQLClause("AND","RAPPORTO.DT_FIN_VALIDITA",SQLBuilder.GREATER_EQUALS,bonus.getDt_registrazione());
	sql.addSQLJoin("RAPPORTO.CD_TIPO_RAPPORTO","TIPO_RAPPORTO.CD_TIPO_RAPPORTO");
	sql.addSQLClause("AND","TIPO_RAPPORTO.FL_BONUS",SQLBuilder.EQUALS,"Y");
	if(sql.executeCountQuery(getConnection(context))>1 )
		throw new ApplicationException("Esiste più di un rapporto valido che prevede il bonus.");
	if(sql.executeCountQuery(getConnection(context))==0 )
		throw new ApplicationException("Non esiste un rapporto valido che prevede il bonus.");
	
}
@Override
public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext,
		OggettoBulk bulk) throws ComponentException {
	 BonusBulk bonus=(BonusBulk)bulk;
	 bonus.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(usercontext));
 	 bonus.setDt_registrazione(it.cnr.jada.util.ejb.EJBCommonServices.getServerDate());
 	 try {
		bonus.setLimite(recuperaLimiteFamiliareCarico(usercontext));
	} catch (RemoteException e) {
		handleException(e);
	}
 	 bonus.setFl_trasmesso(false);
 	 bonus.setModificabile(true);
 	 return super.inizializzaBulkPerInserimento(usercontext, bonus);

}
@Override
public OggettoBulk inizializzaBulkPerRicerca(UserContext usercontext,
		OggettoBulk bulk) throws ComponentException {
	 BonusBulk bonus=(BonusBulk)bulk;
	 bonus.setEsercizio(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(usercontext));
	 bonus.setBonusNucleoFamColl(new BulkList());
	 return super.inizializzaBulkPerRicerca(usercontext, bonus);
}
public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext,
		OggettoBulk bulk) throws ComponentException {
	BonusBulk bonus = (BonusBulk)super.inizializzaBulkPerModifica(usercontext, bulk);
	 try {
			 bonus=(BonusBulk)getHome(usercontext,BonusBulk.class).findByPrimaryKey(bonus);
			 recuperoDati(usercontext, bonus);
			 Bonus_nucleo_famHome dettHome = (Bonus_nucleo_famHome)getHome(usercontext,Bonus_nucleo_famBulk.class);		
			 bonus.setBonusNucleoFamColl(new BulkList(dettHome.findDetailsFor(bonus)));
			 bonus.setLimite(recuperaLimiteFamiliareCarico(usercontext));
			 bonus.setVTerzo(loadTerzo(usercontext, bonus));
			 if(esisteCompenso(usercontext, bonus))
				 bonus.setModificabile(false);
			 else
				 bonus.setModificabile(true);
		} catch (PersistencyException e) {
			throw new ComponentException(e);
		} catch (RemoteException e) {
			throw new ComponentException(e);
		}
	 return bonus;
 }

public Boolean verificaLimiteFamiliareCarico(UserContext context,
		Bonus_nucleo_famBulk bonus_nucleo_fam) throws ComponentException, RemoteException {
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	java.math.BigDecimal limite=sess.getIm01(context, new Integer(0), null,"COSTANTI", "IMPORTO_LIMITE_FAMILIARE_CARICO");
	if (bonus_nucleo_fam.getIm_reddito_componente()!=null && bonus_nucleo_fam.getIm_reddito_componente().compareTo(limite)>0)
		return false;
	else
		return true;
}
private java.math.BigDecimal recuperaLimiteFamiliareCarico(UserContext context) throws ComponentException, RemoteException {
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	return sess.getIm01(context, new Integer(0), null,"COSTANTI", "IMPORTO_LIMITE_FAMILIARE_CARICO");
	
}

public BonusBulk completaBonus(UserContext usercontext, BonusBulk bonus) throws ComponentException {
	try {
	Boolean handicap=false;
		java.math.BigDecimal num_componenti=new java.math.BigDecimal(bonus.getBonusNucleoFamColl().size()+1);
		for(Iterator i=bonus.getBonusNucleoFamColl().iterator();i.hasNext();){
			Bonus_nucleo_famBulk det=(Bonus_nucleo_famBulk)i.next();
			if(det.getFl_handicap()){
				handicap=true;
			}
		}
		
		Bonus_condizioniHome home=(Bonus_condizioniHome)getHome(usercontext, Bonus_condizioniBulk.class);
		Bonus_condizioniBulk bulk=new Bonus_condizioniBulk();
		bulk.setEsercizio(bonus.getEsercizio_imposta());
		Integer max_componenti =(Integer)home.findMax(bulk,"numero_componenti");
		SQLBuilder sql = getHome(usercontext, Bonus_condizioniBulk.class).createSQLBuilder();
		if(handicap){
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bonus.getEsercizio_imposta());
			sql.addSQLClause("AND","NUMERO_COMPONENTI",sql.ISNULL,null);
			sql.addSQLClause("AND","IM_REDDITO_LIMITE",sql.GREATER,bonus.getIm_reddito_nucleo_f());
		}else{
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,bonus.getEsercizio_imposta());
			sql.addSQLClause("AND","IM_REDDITO_LIMITE",sql.GREATER,bonus.getIm_reddito_nucleo_f());
			if(max_componenti!=null && num_componenti.intValue()>max_componenti)
				sql.addSQLClause("AND","NUMERO_COMPONENTI",sql.EQUALS,max_componenti);
			else
				sql.addSQLClause("AND","NUMERO_COMPONENTI",sql.EQUALS,num_componenti);
		}
			if(sql.executeCountQuery(getConnection(usercontext))>0){
				bonus.setBonus_condizioni((Bonus_condizioniBulk)home.fetchAll(sql).get(0));
				bonus.setIm_bonus(bonus.getBonus_condizioni().getIm_bonus());
			}else{
				bonus.setBonus_condizioni(null);
				bonus.setIm_bonus(null);
				throw new ApplicationException("Non ci sono i requisiti per la richiesta del Bonus");
			}
		} catch (PersistencyException e) {
			handleException(e);
		} catch (SQLException e) {
			handleException(e);
		}
	 	return bonus;
 
}
private V_terzo_per_compensoBulk loadTerzo(UserContext userContext, BonusBulk bonus) throws ComponentException {
	try {
		V_terzo_per_compensoHome home = (V_terzo_per_compensoHome)getHome(userContext, V_terzo_per_compensoBulk.class, "DISTINCT_TERZO");
		return home.loadVTerzo(userContext,
						bonus.getTipo_rapporto().getTi_dipendente_altro(), 
						bonus.getCd_terzo(),
						bonus.getDt_registrazione(),
						bonus.getDt_registrazione());
		
	}catch(it.cnr.jada.persistency.PersistencyException ex){
		throw handleException(ex);
	}
}
private boolean esisteCompenso(UserContext userContext, BonusBulk bonus)throws ComponentException {
	CompensoHome home=(CompensoHome)getHome(userContext, CompensoBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addSQLClause("AND","ESERCIZIO_BONUS",SQLBuilder.EQUALS,bonus.getEsercizio());
	sql.addSQLClause("AND","PG_BONUS",SQLBuilder.EQUALS,bonus.getPg_bonus());
	try {
		if (sql.executeCountQuery(connection)>0)
			return true;
		else return false;
	} catch (SQLException e) {
		handleException(e);
	}
	return false;
}

public CompensoBulk cercaCompensoPerBonus(UserContext userContext, BonusBulk bonus)throws ComponentException 
{
	if (!bonus.isModificabile()){
		CompensoHome home=(CompensoHome)getHome(userContext, CompensoBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","ESERCIZIO_BONUS",SQLBuilder.EQUALS,bonus.getEsercizio());
		sql.addSQLClause("AND","PG_BONUS",SQLBuilder.EQUALS,bonus.getPg_bonus());

			try {
				return (CompensoBulk) home.fetchAll(sql).get(0);
			} catch (PersistencyException e) {
				handleException(e);
			}
	}
	return null;
}
@Override
protected Query select(UserContext usercontext,
		CompoundFindClause clauses, OggettoBulk oggettobulk)
		throws ComponentException, PersistencyException {
	BonusBulk bonus= (BonusBulk)oggettobulk;
	SQLBuilder sql = (SQLBuilder)super.select(usercontext, clauses, oggettobulk);
	if(bonus.getTerzo()!=null && bonus.getTerzo().getCd_terzo()!=null)
		sql.addSQLClause("AND", "CD_TERZO", sql.EQUALS, bonus.getTerzo().getCd_terzo());
	sql.addClause(clauses);
	return sql;
}
public java.util.List estraiLista(UserContext context)	throws ComponentException, PersistencyException {
	BonusHome home=(BonusHome)getHome(context, BonusBulk.class);
	SQLBuilder sql = home.createSQLBuilder();
	sql.addTableToHeader("COMPENSO,V_DT_MANREV_COMP");
	sql.addSQLClause("AND","COMPENSO.ESERCIZIO_BONUS",SQLBuilder.ISNOTNULL,null);
	sql.addSQLClause("AND","COMPENSO.PG_BONUS",SQLBuilder.ISNOTNULL,null);
	sql.addSQLJoin("BONUS.ESERCIZIO", sql.EQUALS,"COMPENSO.ESERCIZIO_BONUS");
	sql.addSQLJoin("BONUS.PG_BONUS", sql.EQUALS,"COMPENSO.PG_BONUS");
	sql.addSQLClause("AND","FL_TRASMESSO",sql.EQUALS,"N");
	sql.addSQLClause("AND","STATO_COFI", sql.EQUALS,CompensoBulk.STATO_PAGATO);
	sql.addSQLJoin("COMPENSO.ESERCIZIO", sql.EQUALS,"V_DT_MANREV_COMP.ESERCIZIO_COMPENSO");
	sql.addSQLJoin("COMPENSO.CD_CDS", sql.EQUALS,"V_DT_MANREV_COMP.CD_CDS_COMPENSO");
	sql.addSQLJoin("COMPENSO.CD_UNITA_ORGANIZZATIVA", sql.EQUALS,"V_DT_MANREV_COMP.CD_UO_COMPENSO");
	sql.addSQLJoin("COMPENSO.PG_COMPENSO", sql.EQUALS,"V_DT_MANREV_COMP.PG_COMPENSO");
	
	return home.fetchAll(sql);
}
public java.util.List estraiDettagli(UserContext context,BonusBulk bonus)throws ComponentException, PersistencyException {
	Bonus_nucleo_famHome home=(Bonus_nucleo_famHome)getHome(context, Bonus_nucleo_famBulk.class);
	return home.findDetailsFor(bonus);
}
public String recuperaCodiceFiscaleInvio(UserContext context) throws ComponentException, RemoteException {
	Configurazione_cnrComponentSession sess = (Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession");
	return sess.getVal01(context, new Integer(0), null,"COSTANTI", "CODICE_FISCALE_INVIO");
	
}

public void aggiornaInvio(UserContext context) throws ComponentException, PersistencyException, RemoteException {
	java.util.List da_aggiornare= estraiLista(context);
	for(Iterator i=da_aggiornare.iterator();i.hasNext();){
		BonusBulk bonus=(BonusBulk)i.next();
		bonus=recuperoDati(context, bonus);
		bonus.setFl_trasmesso(Boolean.TRUE);
		bonus.setToBeUpdated();
		updateBulk(context, bonus);
	}
	
}
}
