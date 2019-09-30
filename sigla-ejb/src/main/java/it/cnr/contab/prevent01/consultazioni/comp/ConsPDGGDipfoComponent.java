
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

package it.cnr.contab.prevent01.consultazioni.comp;

import java.rmi.RemoteException;
import java.util.Enumeration;
import javax.ejb.EJBException;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.prevent01.bulk.Pdg_modulo_costiBulk;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGGDipFoBP;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGPDipFoBP;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipfoBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipfo_etrBulk;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipfo_speBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.ColumnMap;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author rp
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGGDipfoComponent extends CRUDComponent {
		
	private static String TIPO_ETR = "E"; 
	private static String TIPO_SPE = "S"; 

	public RemoteIterator findConsultazioneEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		BulkHome home = getHome(userContext, V_cons_pdgg_dipfo_etrBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		String tabAlias = sql.getColumnMap().getTableName();
		sql.addSQLClause("AND","CD_DIPARTIMENTO",SQLBuilder.EQUALS,getDipartimentoScrivania(userContext));
		addBaseColumns(userContext, sql, tabAlias, pathDestinazione, livelloDestinazione, TIPO_ETR);
		return iterator(userContext,completaSQL(sql,tabAlias,baseClause,findClause,TIPO_ETR),V_cons_pdgg_dipfo_etrBulk.class,null);		
	}

	public RemoteIterator findConsultazioneSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		BulkHome home = getHome(userContext, V_cons_pdgg_dipfo_speBulk.class);
		SQLBuilder sql = home.createSQLBuilder();
		String tabAlias = sql.getColumnMap().getTableName();
		sql.addSQLClause("AND","CD_DIPARTIMENTO",SQLBuilder.EQUALS,getDipartimentoScrivania(userContext));
		addBaseColumns(userContext, sql, tabAlias, pathDestinazione, livelloDestinazione, TIPO_SPE);
		return iterator(userContext,completaSQL(sql,tabAlias,baseClause,findClause,TIPO_SPE),V_cons_pdgg_dipfo_speBulk.class,null);		
	}

	/**
	 * Costruisce l'SQLBuilder individuando i campi da ricercare sulla base del path della  consultazione 
	 * <pathDestinazione> indicata. 
	 *
	 * @param sql la select principale contenente le Sum e i GroupBy
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 * @param livelloDestinazione il livello della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, String tabAlias, String pathDestinazione, String livelloDestinazione, String tipo) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		addColumnDIP(sql,tabAlias,pathDestinazione);
		addColumnTIP(sql,tabAlias,pathDestinazione);
		addColumnCDR(sql,tabAlias,pathDestinazione);
		addColumnPRO(sql,tabAlias,pathDestinazione);
		addColumnCOM(sql,tabAlias,pathDestinazione);
		addColumnMOD(sql,tabAlias,pathDestinazione);
		addColumnLIV1(sql,tabAlias,livelloDestinazione,userContext,pathDestinazione,tipo);
		addColumnLIV2(sql,tabAlias,pathDestinazione,tipo);
		addColumnLIV3(sql,tabAlias,pathDestinazione,tipo);
		addColumnLIN(sql,tabAlias,pathDestinazione,tipo);
		addColumnVOC(sql,tabAlias,pathDestinazione,tipo);
		addColumnDET(sql,tabAlias,pathDestinazione,tipo);
		addColumnRestanti(sql,tabAlias,pathDestinazione,tipo);
		if(!isUtenteEnte(userContext) && (tipo.equals(TIPO_ETR))){ 
			   CdrBulk cdrUtente = cdrFromUserContext(userContext);
			   if (cdrUtente.getLivello().compareTo(CdrHome.CDR_PRIMO_LIVELLO)==0){
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO.ESERCIZIO");
					sql.addSQLJoin(tabAlias.concat(".CD_CENTRO_RESPONSABILITA"),"V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
					sql.openParenthesis("AND");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("OR", "V_CDR_VALIDO.CD_CDR_AFFERENZA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.closeParenthesis();
				}else{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO.ESERCIZIO");
					sql.addSQLJoin(tabAlias.concat(".CD_CENTRO_RESPONSABILITA"),"V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				}
		}else if(!isUtenteEnte(userContext) && (tipo.equals(TIPO_SPE))){ 
			   CdrBulk cdrUtente = cdrFromUserContext(userContext);
			   if (cdrUtente.getLivello().compareTo(CdrHome.CDR_PRIMO_LIVELLO)==0){
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO.ESERCIZIO");
					sql.addSQLJoin(tabAlias.concat(".CD_CDR_ASSEGNATARIO"),"V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
					sql.openParenthesis("AND");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("OR", "V_CDR_VALIDO.CD_CDR_AFFERENZA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.closeParenthesis();
				}else{
					sql.addTableToHeader("V_CDR_VALIDO");
					sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO.ESERCIZIO");
					sql.addSQLJoin(tabAlias.concat(".CD_CDR_ASSEGNATARIO"),"V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA");
					sql.addSQLClause("AND", "V_CDR_VALIDO.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
					sql.addSQLClause("AND", "V_CDR_VALIDO.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
				}
		}
	}			

	/**
	 * Individua e completa l'SQLBuilder da utilizzare:
	 * 1) è stata effettuata una ricerca mirata (<findClause> != null)
	 * 	  la select finale è costruita come interrogazione di una view costruita sulla select principale <baseClause>
	 * 2) non è stata fatta una ricerca mirata
	 * 	  la select finale è uguale alla select principale
	 *
	 * @param sql la select principale contenente le Sum e i GroupBy
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param baseClause la condizione principale non proveniente dall'utente
	 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
	 * @return SQLBuilder la query da effettuare
	 */
	private SQLBuilder completaSQL(SQLBuilder sql, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause, String tipo){ 
		sql.addClause(baseClause);		
		addColumnIMPORTI(sql,tabAlias,tipo);
		//sql.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
		sql.addClause(findClause);
		return sql;
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne importo.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 */
	private void addColumnIMPORTI(SQLBuilder sql, String tabAlias, String tipo) { 
		tabAlias = getAlias(tabAlias);
		if (tipo.equals(TIPO_ETR)) {
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_ENT_IST_A1),0)")), "TOT_ENT_IST_A1");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_ENT_AREE_A1),0)")), "TOT_ENT_AREE_A1");
			sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("TOT_ENT_IST_A1,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("TOT_ENT_AREE_A1,0)),0)")))), "TOT_ENT_A1");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_INC_IST_A1),0)")), "TOT_INC_IST_A1");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_INC_AREE_A1),0)")), "TOT_INC_AREE_A1");
			sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("TOT_INC_IST_A1,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("TOT_INC_AREE_A1,0)),0)")))), "TOT_INC_A1");
		}
		else {
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_INT),0)")), "IM_DEC_IST_INT");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_EST),0)")), "IM_DEC_IST_EST");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_INT),0)")), "IM_DEC_AREA_INT");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_EST),0)")), "IM_DEC_AREA_EST");
			sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_IST_INT,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_AREA_INT,0)),0)")))), "IMP_TOT_DEC_INT");
			sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_IST_EST,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("IM_DEC_AREA_EST,0)),0)")))), "IMP_TOT_DEC_EST");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_EST),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_INT),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_INT),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_EST),0)")))))))), "IMP_TOT_DECENTRATO");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TRATT_ECON_INT),0)")), "TRATT_ECON_INT");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TRATT_ECON_EST),0)")), "TRATT_ECON_EST");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_ACC_ALTRE_SP_INT),0)")), "IM_ACC_ALTRE_SP_INT");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TRATT_ECON_EST),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_EST),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_EST),0)")))))), "IMP_TOT_COMP_EST");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TRATT_ECON_INT),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_IST_INT),0)+NVL(SUM(".concat(tabAlias.concat("IM_DEC_AREA_INT),0)+NVL(SUM(".concat(tabAlias.concat("IM_ACC_ALTRE_SP_INT),0)")))))))), "IMP_TOT_COMP_INT");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_PAGAMENTI),0)")), "IM_PAGAMENTI");
		}
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del Cdr.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnCDR(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_CDR)>=0) {
			addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
			addColumn(sql,tabAlias.concat("DS_CDR"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),true);					
		}
		else {
			addColumn(sql,"NULL CD_CENTRO_RESPONSABILITA",true);
			addColumn(sql,"NULL DS_CDR",true);
		}
	}
	private void addColumnTIP(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_TIP)>=0) {
			addColumn(sql,tabAlias.concat("CD_TIPO_MODULO"),true);
			addColumn(sql,tabAlias.concat("DS_TIPO_MODULO"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_tipo_modulo"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_tipo_modulo"),true);
		}
		else {
			addColumn(sql,"NULL CD_TIPO_MODULO",true);
			addColumn(sql,"NULL DS_TIPO_MODULO",true);
		}
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del dipartimento
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnDIP(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
	   tabAlias = getAlias(tabAlias);
	   if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_DIP)>=0) {
		   addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
		   addColumn(sql,tabAlias.concat("CD_DIPARTIMENTO"),true);
		   addColumn(sql,tabAlias.concat("DS_DIPARTIMENTO"),true);
		   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),true);
		   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_dipartimento"),true);
		   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dipartimento"),true);
	   }
	   else {
		   addColumn(sql,"NULL ESERCIZIO",true);
		   addColumn(sql,"NULL CD_DIPARTIMENTO",true);
		   addColumn(sql,"NULL DS_DIPARTIMENTO",true);
	   }
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del progetto
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnPRO(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_PRO)>=0) {
			addColumn(sql,tabAlias.concat("PG_PROGETTO"),true);
			addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
			addColumn(sql,tabAlias.concat("DS_PROGETTO"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_progetto"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_progetto"),true);
		}
		else  if (!(pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_DET)>=0 && pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_TIP)>=0)){
			addColumn(sql,"NULL PG_PROGETTO",true);
			addColumn(sql,"NULL CD_PROGETTO",true);
			addColumn(sql,"NULL DS_PROGETTO",true);
		}
	} 
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del commessa
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnCOM(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
	   tabAlias = getAlias(tabAlias);
	   if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_COM)>=0) {
		   addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
		   addColumn(sql,tabAlias.concat("DS_COMMESSA"),true);
		   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),true);
		   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_commessa"),true);
	   }
	   else  if (!(pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_DET)>=0 && pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_TIP)>=0)){
		   addColumn(sql,"NULL CD_COMMESSA",true);
		   addColumn(sql,"NULL DS_COMMESSA",true);
	   }
	}	   
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne Modulo
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnMOD(SQLBuilder sql, String tabAlias, String pathDestinazione){ 
		tabAlias = getAlias(tabAlias);
	    if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_MOD)>=0) {
			addColumn(sql,tabAlias.concat("CD_MODULO"),true);
			addColumn(sql,tabAlias.concat("DS_MODULO"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),true);
	    }
	    else {
			addColumn(sql,"NULL CD_MODULO",true);
			addColumn(sql,"NULL DS_MODULO",true);
	    }
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del primo livello della classificazione.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnLIV1(SQLBuilder sql, String tabAlias, String livello_arrivo,UserContext context, String pathDestinazione, String tipo) throws ComponentException{ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV1)>=0) {
			addColumn(sql,tabAlias.concat("CD_LIVELLO1"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello1"),true);
		}
		else
			addColumn(sql,"NULL CD_LIVELLO1",true);

		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV1)>0&&
			(pathDestinazione.length()-(pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV1)+ConsPDGGDipFoBP.LIVELLO_LIV1.length())==0)) {
			sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
			addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
			addSQLGroupBy(sql,"classificazione_voci.ds_classificazione",true);

			sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
			sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,tipo);
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO2",sql.ISNULL,null);
		}
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del secondo livello della classificazione.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnLIV2(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV2)>=0) {
			addColumn(sql,tabAlias.concat("CD_LIVELLO2"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello2"),true);
		}
		else
			addColumn(sql,"NULL CD_LIVELLO2",true);

		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV2)>0&&
				(pathDestinazione.length()-(pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV2)+ConsPDGGDipFoBP.LIVELLO_LIV2.length())==0)) {
			sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
			addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
			sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
			sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
			sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,tipo);
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO3",sql.ISNULL,null);
		}
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del secondo livello della classificazione.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnLIV3(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV3)>=0) {
			addColumn(sql,tabAlias.concat("CD_LIVELLO3"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello3"),true);
		}
		else
			addColumn(sql,"NULL CD_LIVELLO3",true);

		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV3)>0&&
		   (pathDestinazione.length()-(pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIV3)+ConsPDGGDipFoBP.LIVELLO_LIV3.length())==0)) {
			sql.addTableToHeader("CLASSIFICAZIONE_VOCI CLASSIFICAZIONE_VOCI");
			addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
			sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
			sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
			sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,tipo);
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO3"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO3");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO4",sql.ISNULL,null);
		}
	}
	private void addColumnLIN(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo){ 
		tabAlias = getAlias(tabAlias);
	    if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_LIN)>=0) {
			addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
			addColumn(sql,tabAlias.concat("DS_LINEA"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_linea"),true);
	    }
	    else {
			addColumn(sql,"NULL CD_LINEA_ATTIVITA",true);
			addColumn(sql,"NULL DS_LINEA",true);
	    }
	}
	private void addColumnVOC(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo){ 
		tabAlias = getAlias(tabAlias);
	    if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_VOC)>=0) {
			addColumn(sql,tabAlias.concat("CD_ELEMENTO_VOCE"),true);
			addColumn(sql,tabAlias.concat("DS_ELEMENTO_VOCE"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_elemento_voce"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_elemento_voce"),true);
	    }
	    else {
			addColumn(sql,"NULL CD_ELEMENTO_VOCE",true);
			addColumn(sql,"NULL DS_ELEMENTO_VOCE",true);
	    }
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del dettaglio ultimo della Consultazione.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
	 */
	private void addColumnDET(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo){ 
		tabAlias = getAlias(tabAlias);
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_DET)>=0) {
			addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
			addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),true);

			if (tipo.equals(TIPO_ETR)) {
				addColumn(sql,tabAlias.concat("DS_DETTAGLIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dettaglio"),true);
			}
		}
		else {
			addColumn(sql,"NULL CD_CLASSIFICAZIONE",true);
			addColumn(sql,"NULL DS_CLASSIFICAZIONE",true);
			if (tipo.equals(TIPO_ETR)) {
				addColumn(sql,"NULL DS_DETTAGLIO",true);
			}
		}
		if (pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_DET)>=0 && pathDestinazione.indexOf(ConsPDGGDipFoBP.LIVELLO_TIP)>=0)
		{
			addColumn(sql,tabAlias.concat("PG_PROGETTO"),true);
			addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
			addColumn(sql,tabAlias.concat("DS_PROGETTO"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_progetto"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_progetto"),true);
			addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
			addColumn(sql,tabAlias.concat("DS_COMMESSA"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_commessa"),true);
		}	
	}
	private void addColumnRestanti(SQLBuilder sql, String tabAlias, String pathDestinazione, String tipo) {
		if (tipo.equals(TIPO_ETR)) {
		}
		else {
			addColumn(sql,"NULL CD_CDR_ASSEGNATARIO",true);
			addColumn(sql,"NULL DS_CDR_ASSEGNATARIO",true);
		}
		
	}
	/**
	 * Ritorna l'alias della tabella da aggiungere alle colonne di una select.
	 * Viene verificato il parametro <alias> indicato
	 * 1) se null ritorna null;
	 * 2) se pieno concatena "."
	 *
	 * @param alias l'alias da aggiornare per aggiungerlo alle colonne di una select.
	 * @return l'alias da utilizzare nella select.
	 */
	private String getAlias(String alias){ 
		if (alias == null) return new String("");
		if (alias.equals(new String(""))) return new String("");
		return new String(alias.concat("."));
	}
	/**
	 * Aggiunge nel GroupBy dell'SQLBuilder <sql> indicato il valore <valore>
	 *
	 * @param sql l'SQLBuilder da aggiornare.
	 * @param valore il valore da aggiungere al GroupBy.
	 * @param addGroupBy se TRUE aggiunge il valore. Se FALSE non effettua alcuna istruzione.
	 */
	private void addSQLGroupBy(SQLBuilder sql, String valore, boolean addGroupBy){
		if (addGroupBy)
			sql.addSQLGroupBy(valore);
	}
	/**
	 * Aggiunge nella lista delle colonne da interrogare dell'SQLBuilder <sql> indicato il valore <valore>
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param valore il valore da aggiungere alla lista delle colonne da interrogare.
	 * @param addColumn se TRUE aggiunge il valore. Se FALSE non effettua alcuna istruzione.
	 */
	private void addColumn(SQLBuilder sql, String valore, boolean addColumn){
		if (addColumn)
			sql.addColumn(valore);
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

	private String getDipartimentoScrivania(UserContext userContext)throws ComponentException{
		try {
			UtenteBulk utente = (UtenteBulk)getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext)));
			return utente.getCd_dipartimento();
		} catch (PersistencyException e) {
			throw handleException(e);
		}
	}
}
