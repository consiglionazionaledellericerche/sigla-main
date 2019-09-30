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

package it.cnr.contab.preventvar00.comp;



import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_disp_comp_resBulk;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataBP;
import it.cnr.contab.preventvar00.consultazioni.bp.ConsAssCompPerDataDettagliBP;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataBulk;
import it.cnr.contab.preventvar00.consultazioni.bulk.V_cons_ass_comp_per_dataHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

/**
 * Insert the type's description here.
 * Creation date: (13/11/2006)
 * @author: Flavia Giardina
 */
public class ConsAssCompPerDataComponent extends CRUDComponent{
/**
 * ConsAssCompPerDataComponent constructor comment.
 */
	
public ConsAssCompPerDataComponent() {
	super();
}


public RemoteIterator findVariazioniDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, OggettoBulk bulk) 
	throws ComponentException, RemoteException {
	
	    BulkHome home = getHome(userContext, V_cons_ass_comp_per_dataBulk.class, pathDestinazione);
	    V_cons_ass_comp_per_dataBulk assestato= (V_cons_ass_comp_per_dataBulk)bulk;
		SQLBuilder sql = home.createSQLBuilder();
		addBaseColumns(userContext, sql,  pathDestinazione, livelloDestinazione,true);
		if (pathDestinazione.equals("BASESTANZ"))
			sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.PG_VARIAZIONE_PDG",sql.ISNULL,null);
		else 
			sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.PG_VARIAZIONE_PDG",sql.ISNOTNULL,null);
		
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.DATA_APPROVAZIONE_VAR",sql.LESS_EQUALS,assestato.getData_approvazione_var());
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.TI_GESTIONE",sql.EQUALS,assestato.getTi_gestione());
		
		
	return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_ass_comp_per_dataBulk.class,null);
}




public it.cnr.jada.util.RemoteIterator findVariazioni(UserContext userContext, OggettoBulk bulk) 
	throws  ComponentException, RemoteException{
		V_cons_ass_comp_per_dataBulk assestato = (V_cons_ass_comp_per_dataBulk)bulk;
		V_cons_ass_comp_per_dataHome assestatoHome = (V_cons_ass_comp_per_dataHome)getHome(userContext, V_cons_ass_comp_per_dataBulk.class,"BASE");
		SQLBuilder sql = assestatoHome.createSQLBuilder();
		sql.resetColumns();
		sql.addColumn("CD_DIPARTIMENTO");
		sql.addColumn("CD_LIVELLO1");
		sql.addColumn("DS_LIVELLO1");
		sql.addColumn("CD_LIVELLO2");
		sql.addColumn("DS_LIVELLO2");
		sql.addColumn("NVL(SUM(IM_STANZ_INIZIALE_A1),0)","TOT_IM_STANZ_INIZIALE_A1");
		sql.addColumn("NVL(SUM(VARIAZIONI_PIU),0)","TOT_VARIAZIONI_PIU");
		sql.addColumn("NVL(SUM(VARIAZIONI_MENO),0)","TOT_VARIAZIONI_MENO");
		addSQLGroupBy(sql,"CD_DIPARTIMENTO",true);
		addSQLGroupBy(sql,"CD_LIVELLO1",true);
		addSQLGroupBy(sql,"DS_LIVELLO1",true);
		addSQLGroupBy(sql,"CD_LIVELLO2",true);
		addSQLGroupBy(sql,"DS_LIVELLO2",true);
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.ESERCIZIO",sql.EQUALS,CNRUserContext.getEsercizio(userContext));
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.DATA_APPROVAZIONE_VAR",sql.LESS_EQUALS,assestato.getData_approvazione_var());
		sql.addSQLClause("AND","V_CONS_ASSESTATO_COMP_PER_DATA.TI_GESTIONE",sql.EQUALS,assestato.getTi_gestione());
	return  iterator(userContext,sql,V_cons_ass_comp_per_dataBulk.class,null);
}

private SQLBuilder completaSQL(SQLBuilder sql, CompoundFindClause baseClause, CompoundFindClause findClause){
	sql.addClause(baseClause);
		if (findClause==null)
			return sql;
		else {
			sql.addClause(findClause);
			return sql;
		}
	}


private void addBaseColumns(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException {		
		sql.resetColumns();
		sql.addColumn("CD_DIPARTIMENTO");
		sql.addColumn("CD_LIVELLO1");
		sql.addColumn("DS_LIVELLO1");
		sql.addColumn("CD_LIVELLO2");
		sql.addColumn("DS_LIVELLO2");
		sql.addColumn("NVL(SUM(IM_STANZ_INIZIALE_A1),0)","TOT_IM_STANZ_INIZIALE_A1");
		sql.addColumn("NVL(SUM(VARIAZIONI_PIU),0)","TOT_VARIAZIONI_PIU");
		sql.addColumn("NVL(SUM(VARIAZIONI_MENO),0)","TOT_VARIAZIONI_MENO");
		addSQLGroupBy(sql,"CD_DIPARTIMENTO",true);
		addSQLGroupBy(sql,"CD_LIVELLO1",true);
		addSQLGroupBy(sql,"DS_LIVELLO1",true);
		addSQLGroupBy(sql,"CD_LIVELLO2",true);
		addSQLGroupBy(sql,"DS_LIVELLO2",true);
		
		if (livelloDestinazione.indexOf(ConsAssCompPerDataDettagliBP.LIV_BASEVARPIU)>=0) {
			addColumnDettPiu(sql,true,livelloDestinazione);
		}
		
		if (livelloDestinazione.indexOf(ConsAssCompPerDataDettagliBP.LIV_BASEVARMENO)>=0) {
			addColumnDettMeno(sql,true,livelloDestinazione);
		}
		
		if (livelloDestinazione.indexOf(ConsAssCompPerDataDettagliBP.LIV_BASESTANZ)>=0) {
			addColumnStanz(sql,true,livelloDestinazione);
		}
	}

	private void addColumnDettPiu(SQLBuilder sql, boolean isBaseSQL,String livelloDestinazione ){ 
			sql.resetColumns();
			sql.addColumn("PG_VARIAZIONE_PDG");
			sql.addColumn("DATA_APPROVAZIONE_VAR");
			sql.addColumn("CD_MODULO");
			sql.addColumn("DS_MODULO");
			sql.addColumn("CD_CENTRO_RESPONSABILITA");
			sql.addColumn("CD_LINEA_ATTIVITA");
			sql.addColumn("CD_ELEMENTO_VOCE");
			sql.addColumn("DS_ELEMENTO_VOCE");
			sql.addColumn("CD_DIPARTIMENTO");
			sql.addColumn("CD_LIVELLO1");
			sql.addColumn("DS_LIVELLO1");
			sql.addColumn("CD_LIVELLO2");
			sql.addColumn("DS_LIVELLO2");
			sql.addColumn("NVL(SUM(IM_STANZ_INIZIALE_A1),0)","TOT_IM_STANZ_INIZIALE_A1");
			sql.addColumn("NVL(SUM(VARIAZIONI_PIU),0)","TOT_VARIAZIONI_PIU");
			addSQLGroupBy(sql,"PG_VARIAZIONE_PDG",true);
			addSQLGroupBy(sql,"DATA_APPROVAZIONE_VAR",true);
			addSQLGroupBy(sql,"CD_MODULO",true);
			addSQLGroupBy(sql,"DS_MODULO",true);
			addSQLGroupBy(sql,"CD_CENTRO_RESPONSABILITA",true);
			addSQLGroupBy(sql,"CD_LINEA_ATTIVITA",true);
			addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",true);
			addSQLGroupBy(sql,"DS_ELEMENTO_VOCE",true);
			addSQLGroupBy(sql,"CD_DIPARTIMENTO",true);
			addSQLGroupBy(sql,"CD_LIVELLO1",true);
			addSQLGroupBy(sql,"DS_LIVELLO1",true);
			addSQLGroupBy(sql,"CD_LIVELLO2",true);
			addSQLGroupBy(sql,"DS_LIVELLO2",true);
			
		}

		
	private void addColumnDettMeno(SQLBuilder sql, boolean isBaseSQL,String livelloDestinazione ){ 
			sql.resetColumns();
			sql.addColumn("PG_VARIAZIONE_PDG");
			sql.addColumn("DATA_APPROVAZIONE_VAR");
			sql.addColumn("CD_MODULO");
			sql.addColumn("DS_MODULO");
			sql.addColumn("CD_CENTRO_RESPONSABILITA");
			sql.addColumn("CD_LINEA_ATTIVITA");
			sql.addColumn("CD_ELEMENTO_VOCE");
			sql.addColumn("DS_ELEMENTO_VOCE");
			sql.addColumn("CD_DIPARTIMENTO");
			sql.addColumn("CD_LIVELLO1");
			sql.addColumn("DS_LIVELLO1");
			sql.addColumn("CD_LIVELLO2");
			sql.addColumn("DS_LIVELLO2");
			sql.addColumn("NVL(SUM(IM_STANZ_INIZIALE_A1),0)","TOT_IM_STANZ_INIZIALE_A1");
			sql.addColumn("NVL(SUM(VARIAZIONI_MENO),0)","TOT_VARIAZIONI_MENO");
			addSQLGroupBy(sql,"PG_VARIAZIONE_PDG",true);
			addSQLGroupBy(sql,"DATA_APPROVAZIONE_VAR",true);
			addSQLGroupBy(sql,"CD_MODULO",true);
			addSQLGroupBy(sql,"DS_MODULO",true);
			addSQLGroupBy(sql,"CD_CENTRO_RESPONSABILITA",true);
			addSQLGroupBy(sql,"CD_LINEA_ATTIVITA",true);
			addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",true);
			addSQLGroupBy(sql,"DS_ELEMENTO_VOCE",true);
			addSQLGroupBy(sql,"CD_DIPARTIMENTO",true);
			addSQLGroupBy(sql,"CD_LIVELLO1",true);
			addSQLGroupBy(sql,"DS_LIVELLO1",true);
			addSQLGroupBy(sql,"CD_LIVELLO2",true);
			addSQLGroupBy(sql,"DS_LIVELLO2",true);
			
		}

	private void addColumnStanz(SQLBuilder sql, boolean isBaseSQL,String livelloDestinazione ){ 
		sql.resetColumns();
		sql.addColumn("PG_VARIAZIONE_PDG");
		sql.addColumn("DATA_APPROVAZIONE_VAR");
		sql.addColumn("CD_MODULO");
		sql.addColumn("DS_MODULO");
		sql.addColumn("CD_CENTRO_RESPONSABILITA");
		sql.addColumn("CD_LINEA_ATTIVITA");
		sql.addColumn("CD_ELEMENTO_VOCE");
		sql.addColumn("DS_ELEMENTO_VOCE");
		sql.addColumn("CD_DIPARTIMENTO");
		sql.addColumn("CD_LIVELLO1");
		sql.addColumn("DS_LIVELLO1");
		sql.addColumn("CD_LIVELLO2");
		sql.addColumn("DS_LIVELLO2");
		sql.addColumn("NVL(SUM(IM_STANZ_INIZIALE_A1),0)","TOT_IM_STANZ_INIZIALE_A1");
//		sql.addColumn("NVL(SUM(VARIAZIONI_MENO),0)","TOT_VARIAZIONI_MENO");
		addSQLGroupBy(sql,"PG_VARIAZIONE_PDG",true);
		addSQLGroupBy(sql,"DATA_APPROVAZIONE_VAR",true);
		addSQLGroupBy(sql,"CD_MODULO",true);
		addSQLGroupBy(sql,"DS_MODULO",true);
		addSQLGroupBy(sql,"CD_CENTRO_RESPONSABILITA",true);
		addSQLGroupBy(sql,"CD_LINEA_ATTIVITA",true);
		addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",true);
		addSQLGroupBy(sql,"DS_ELEMENTO_VOCE",true);
		addSQLGroupBy(sql,"CD_DIPARTIMENTO",true);
		addSQLGroupBy(sql,"CD_LIVELLO1",true);
		addSQLGroupBy(sql,"DS_LIVELLO1",true);
		addSQLGroupBy(sql,"CD_LIVELLO2",true);
		addSQLGroupBy(sql,"DS_LIVELLO2",true);
		
	}
	
	private void addSQLGroupBy(SQLBuilder sql, String valore, boolean addGroupBy){
		if (addGroupBy)
			sql.addSQLGroupBy(valore);
	}
	
	private boolean isCdrEnte(UserContext userContext,CdrBulk cdr) throws ComponentException {
		try {
			getHome(userContext,cdr.getUnita_padre()).findByPrimaryKey(cdr.getUnita_padre());
			return cdr.isCdrAC();
		} catch(Throwable e) {
			throw handleException(e);
		}
	}
	
	private boolean isUtenteEnte(UserContext userContext) throws ComponentException {
		return isCdrEnte(userContext,cdrFromUserContext(userContext));
	}
	
	public CdrBulk cdrFromUserContext(UserContext userContext) throws ComponentException {

		try {
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((it.cnr.contab.utenze00.bp.CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( it.cnr.contab.utenze00.bp.CNRUserContext.getCd_cdr(userContext) );

			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);
		} catch (it.cnr.jada.persistency.PersistencyException e) {
			throw new ComponentException(e);
		}
	}
}
