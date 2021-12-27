
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

package it.cnr.contab.doccont00.consultazioni.comp;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.doccont00.consultazioni.bp.ConsGAEResBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_residui_entBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_residui_speBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsGAEResComponent extends CRUDComponent {
		private static String TIPO_ETR = "ETR"; 
		private static String TIPO_SPE = "SPE"; 
		
		public RemoteIterator findConsultazioneResEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
            try {
                return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, TIPO_ETR);
            } catch (IntrospectionException|PersistencyException e) {
               throw handleException(e);
            }
        }

		public RemoteIterator findConsultazioneResSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
            try {
		        return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, TIPO_SPE);
            } catch (IntrospectionException|PersistencyException e) {
                throw handleException(e);
            }
		}

		private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws ComponentException, IntrospectionException, PersistencyException {
			CdrBulk cdrUtente = cdrFromUserContext(userContext);
			if (pathDestinazione.indexOf("ETRGAE")>=0){ 
				BulkHome home = getHome(userContext, V_cons_gae_residui_entBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione, tipoCons);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause,pathDestinazione),V_cons_gae_residui_entBulk.class,null);
			}
			else
			{
				BulkHome home = getHome(userContext, V_cons_gae_residui_speBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione, tipoCons);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause,pathDestinazione),V_cons_gae_residui_speBulk.class,null);
			}
		}
		/**
		 * Costruisce l'SQLBuilder individuando i campi da ricercare sulla base del path della  consultazione 
		 * <pathDestinazione> indicata. 
		 *
		 * @param sql la select principale contenente le Sum e i GroupBy
		 * @param sqlEsterna la select esterna necessaria per interrogare la select principale come view
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param pathDestinazione il path completo della mappa di consultazione che ha effettuato la richiesta 
		 * @param livelloDestinazione il livello della mappa di consultazione che ha effettuato la richiesta 
		 */
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione, String livelloDestinazione, String tipoCons) throws ComponentException, IntrospectionException, PersistencyException {
		sql.resetColumns();
		sqlEsterna.resetColumns();
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_ETRGAE)>=0||pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_SPEGAE)>=0) {
				addColumnGAE(sql,tabAlias,(livelloDestinazione.equals(ConsGAEResBP.LIVELLO_ETRGAE)||livelloDestinazione.equals(ConsGAEResBP.LIVELLO_SPEGAE)),true,pathDestinazione);
				addColumnGAE(sqlEsterna,tabAlias,(livelloDestinazione.equals(ConsGAEResBP.LIVELLO_ETRGAE)||livelloDestinazione.equals(ConsGAEResBP.LIVELLO_SPEGAE)),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_VOC)>=0){
				addColumnVOC(sql,tabAlias,livelloDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC),true);
				addColumnVOC(sqlEsterna,tabAlias,livelloDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_ESRES)>=0){
				String alias = getAlias(tabAlias);
				addColumn(sql,alias.concat("ESERCIZIO_RES"),true);
				addSQLGroupBy(sql,alias.toLowerCase().concat("esercizio_res"),true);
				addColumn(sqlEsterna,alias.concat("ESERCIZIO_RES"),true);
				addSQLGroupBy(sqlEsterna,alias.toLowerCase().concat("esercizio_res"),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_VARP)>=0){
				addColumnVARP(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_VARP),true);
				addColumnVARP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_VARP),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_VARM)>=0){
				addColumnVARM(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_VARM),true);
				addColumnVARM(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_VARM),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_PIUSTAN)>=0){
				addColumnPIUSTAN(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_PIUSTAN),true);
				addColumnPIUSTAN(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_PIUSTAN),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MENOSTAN)>=0){
				addColumnMENOSTAN(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MENOSTAN),true);
				addColumnMENOSTAN(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MENOSTAN),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_IMP)>=0){
				addColumnIMP(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_IMP),true,pathDestinazione);
				addColumnIMP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_IMP),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_OBB)>=0){
				addColumnOBB(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_OBB),true,pathDestinazione);
				addColumnOBB(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_OBB),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MAN)>=0){
				addColumnMAN(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MAN),true);
				addColumnMAN(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MAN),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MOB)>=0){
				addColumnMOB(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MOB),true);
				addColumnMOB(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_MOB),false);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_ACC)>=0){
				addColumnACC(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_ACC),true,pathDestinazione);
				addColumnACC(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_ACC),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAEResBP.LIVELLO_REV)>=0){
				addColumnREV(sql,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_REV),true);
				addColumnREV(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAEResBP.LIVELLO_REV),false);
			}
		if(!isUtenteEnte(userContext)){
				CdrBulk cdrUtente = cdrFromUserContext(userContext);
			 	if ( !cdrUtente.isCdrILiv() ){
					sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA",sql.EQUALS,CNRUserContext.getCd_cdr(userContext));
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			 	} else {
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, CNRUserContext.getEsercizio(userContext));
			 		sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
			 		sql.openParenthesis("AND");
                    sql.addSQLClause("OR", "CD_CENTRO_RESPONSABILITA",sql.EQUALS,CNRUserContext.getCd_cdr(userContext));
					CdrHome cdrHome = (CdrHome) getHome(userContext, CdrBulk.class);
					for (java.util.Iterator j = cdrHome.findCdrAfferenti(cdrUtente).iterator(); j.hasNext();) {
						CdrBulk cdrAfferenti = (CdrBulk) j.next();
						sql.addSQLClause("OR","CD_CENTRO_RESPONSABILITA", sql.EQUALS, cdrAfferenti.getCd_centro_responsabilita());
					}
					sql.closeParenthesis();
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
		 * @param sqlEsterna la select esterna necessaria per interrogare la select principale come view
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param baseClause la condizione principale non proveniente dall'utente
		 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
		 * @return SQLBuilder la query da effettuare
		 */
		private SQLBuilder completaSQL(SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause,String pathDestinazione){ 
			sql.addClause(baseClause);
			if (pathDestinazione.compareTo("ETRGAE")==0||pathDestinazione.compareTo("SPEGAE")==0||pathDestinazione.endsWith("VOC") ){
				if (findClause == null) 
				{
					addColumnIMPORTI(sql,tabAlias,true);
					return sql;
				}
				else
				{
					addColumnIMPORTI(sql,tabAlias,true);		
					addColumnIMPORTI(sqlEsterna,tabAlias,false);		
					sqlEsterna.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
					sqlEsterna.addClause(findClause);
					return sqlEsterna;
				}
			}else
				if (findClause == null) 
					return sql;
				else{
					sqlEsterna.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
					sqlEsterna.addClause(findClause);
					return sqlEsterna;
				}
		}

		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne importo.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param isSommatoria se TRUE aggiunge la SUM delle colonne
		 */
		private void addColumnIMPORTI(SQLBuilder sql, String tabAlias, boolean isSommatoria) { 
			tabAlias = getAlias(tabAlias);
		if (tabAlias.indexOf("V_CONS_GAE_RESIDUI_ENT")>=0){
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)")), "IM_OBBL_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_ACC_RES_PRO),0)")), "VAR_PIU_ACC_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_ACC_RES_PRO),0)")), "VAR_MENO_ACC_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
				sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IM_OBBL_RES_PRO,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO,0)),0)")))), "DA_RISCUOTERE");
			}
			else
			{
				sql.addColumn("NVL(IM_OBBL_RES_PRO,0)", "IM_OBBL_RES_PRO");
				sql.addColumn("NVL(VAR_PIU_ACC_RES_PRO,0)", "VAR_PIU_ACC_RES_PRO");
				sql.addColumn("NVL(VAR_MENO_ACC_RES_PRO,0)", "VAR_MENO_ACC_RES_PRO");
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");
				sql.addColumn("NVL(DA_RISCUOTERE,0)", "DA_RISCUOTERE");	
			}
		}else
		{	
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_RES_IMPROPRIO),0)")), "IM_STANZ_RES_IMPROPRIO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_STANZ_RES_IMP),0)")), "VAR_PIU_STANZ_RES_IMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_STANZ_RES_IMP),0)")), "VAR_MENO_STANZ_RES_IMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_IMP),0)")), "IM_OBBL_RES_IMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)")), "IM_OBBL_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_OBBL_RES_PRO),0)")), "VAR_PIU_OBBL_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_OBBL_RES_PRO),0)")), "VAR_MENO_OBBL_RES_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")), "IM_MANDATI_REVERSALI_IMP");
				
//			  IM_STANZ_RES_IMPROPRIO+VAR_PIU_STANZ_RES_IMP-VAR_MENO_STANZ_RES_IMP=ass_res_imp;
//			  IM_OBBL_RES_PRO - VAR_PIU_OBBL_RES_PRO + VAR_MENO_OBBL_RES_PRO = iniziale;
//			  ass_res_imp - IM_OBBL_RES_IMP + VAR_MENO_OBBL_RES_PRO - VAR_PIU_OBBL_RES_PRO = disp_res;
//			  IM_OBBL_RES_IMP + IM_OBBL_RES_PRO - (IM_MANDATI_REVERSALI_PRO + IM_MANDATI_REVERSALI_IMP) = rimasti_da_pagare;
//			  IM_OBBL_RES_IMP - IM_MANDATI_REVERSALI_IMP = rimasti_da_pagare_imp ;
//			  IM_OBBL_RES_PRO - IM_MANDATI_REVERSALI_PRO = rimasti_da_pagare_pro;
//			  IM_MANDATI_REVERSALI_PRO + IM_MANDATI_REVERSALI_IMP = pagato_totale
				
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_RES_IMPROPRIO),0)+NVL(SUM(".concat(tabAlias.concat("VAR_PIU_STANZ_RES_IMP),0)-NVL(SUM(".concat(tabAlias.concat("VAR_MENO_STANZ_RES_IMP),0)")))))), "ASS_RES_IMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)-NVL(SUM(".concat(tabAlias.concat("VAR_PIU_OBBL_RES_PRO),0)+NVL(SUM(".concat(tabAlias.concat("VAR_MENO_OBBL_RES_PRO),0)")))))), "INIZIALE");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_RES_IMPROPRIO),0)+NVL(SUM(".concat(tabAlias.concat("VAR_PIU_STANZ_RES_IMP),0)-NVL(SUM(".concat(tabAlias.concat("VAR_MENO_STANZ_RES_IMP),0)-NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_IMP),0)+NVL(SUM(".concat(tabAlias.concat("VAR_MENO_OBBL_RES_PRO),0)-NVL(SUM(".concat(tabAlias.concat("VAR_PIU_OBBL_RES_PRO),0)")))))))))))), "DISP_RES");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_IMP),0)+NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)-NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)-NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")))))))), "RIMASTI_DA_PAGARE");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)+NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")))), "PAGATO_TOTALE");
				
			}
			else
			{
				sql.addColumn("NVL(IM_STANZ_RES_IMPROPRIO,0)", "IM_STANZ_RES_IMPROPRIO");
				sql.addColumn("NVL(VAR_PIU_STANZ_RES_IMP,0)", "VAR_PIU_STANZ_RES_IMP");
				sql.addColumn("NVL(VAR_MENO_STANZ_RES_IMP,0)", "VAR_MENO_STANZ_RES_IMP");
				sql.addColumn("NVL(IM_OBBL_RES_IMP,0)", "IM_OBBL_RES_IMP");
				sql.addColumn("NVL(IM_OBBL_RES_PRO,0)", "IM_OBBL_RES_PRO");
				sql.addColumn("NVL(VAR_PIU_OBBL_RES_PRO,0)", "VAR_PIU_OBBL_RES_PRO");
				sql.addColumn("NVL(VAR_MENO_OBBL_RES_PRO,0)", "VAR_MENO_OBBL_RES_PRO");
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_IMP,0)", "IM_MANDATI_REVERSALI_IMP");
				
				sql.addColumn("NVL(ASS_RES_IMP,0)", "ASS_RES_IMP");
				sql.addColumn("NVL(INIZIALE,0)", "INIZIALE");
				sql.addColumn("NVL(DISP_RES,0)", "DISP_RES");
				sql.addColumn("NVL(RIMASTI_DA_PAGARE,0)", "RIMASTI_DA_PAGARE");
				sql.addColumn("NVL(PAGATO_TOTALE,0)", "PAGATO_TOTALE");
				
			}
		}
	}

		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del Cdr.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnGAE(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String pathDestinazione){
				tabAlias = getAlias(tabAlias);
				
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_LINEA_ATTIVITA"),addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_linea_attivita"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC)));
				
				addColumn(sql,tabAlias.concat("CD_DIPARTIMENTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_dipartimento"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_PROGETTO"),addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_progetto"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC)));	
				
				addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_COMMESSA"),addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_commessa"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC)));
				
				addColumn(sql,tabAlias.concat("CD_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_MODULO"),addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAEResBP.LIVELLO_VOC)));	
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne var +
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnVARP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			if (tabAlias.indexOf("V_CONS_GAE_RESIDUI_ENT")>=0){
			addColumn(sql,tabAlias.concat("PG_VAR_RES_PRO"),true);
			addColumn(sql,tabAlias.concat("DS_VAR_RES_PRO"),addDescrizione);
			if(isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_ACC_RES_PRO),0)")), "VAR_PIU_ACC_RES_PRO");
			else
				sql.addColumn("NVL(VAR_PIU_ACC_RES_PRO,0)", "VAR_PIU_ACC_RES_PRO");
			
			sql.addSQLClause("AND",tabAlias.concat("VAR_PIU_ACC_RES_PRO"),sql.NOT_EQUALS, 0);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_res_pro"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_res_pro"),isBaseSQL &&addDescrizione);
			}
			else
			{
				addColumn(sql,tabAlias.concat("PG_VAR_RES_PRO"),true);
				addColumn(sql,tabAlias.concat("DS_VAR_RES_PRO"),addDescrizione);
				if(isBaseSQL)
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_OBBL_RES_PRO),0)")), "VAR_PIU_OBBL_RES_PRO");
				else
					sql.addColumn("NVL(VAR_PIU_OBBL_RES_PRO,0)", "VAR_PIU_OBBL_RES_PRO");

				sql.addSQLClause("AND",tabAlias.concat("VAR_PIU_OBBL_RES_PRO"),sql.NOT_EQUALS, 0);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_res_pro"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_res_pro"),isBaseSQL &&addDescrizione);
			}
		}
		
		private void addColumnPIUSTAN(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("PG_VAR_ST_RES"),true);
				addColumn(sql,tabAlias.concat("DS_VAR_ST_RES"),addDescrizione);
				if(isBaseSQL)
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_STANZ_RES_IMP),0)")), "VAR_PIU_STANZ_RES_IMP");
				else
					sql.addColumn("NVL(VAR_PIU_STANZ_RES_IMP,0)", "VAR_PIU_STANZ_RES_IMP");
					
				sql.addSQLClause("AND",tabAlias.concat("VAR_PIU_STANZ_RES_IMP"),sql.NOT_EQUALS, 0);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_st_res"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_st_res"),isBaseSQL &&addDescrizione);
			}
	
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne var -
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnVARM(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			if (tabAlias.indexOf("V_CONS_GAE_RESIDUI_ENT")>=0){
			addColumn(sql,tabAlias.concat("PG_VAR_RES_PRO"),true);
			addColumn(sql,tabAlias.concat("DS_VAR_RES_PRO"),addDescrizione);
			if(isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_ACC_RES_PRO),0)")), "VAR_MENO_ACC_RES_PRO");
			else
				sql.addColumn("NVL(VAR_MENO_ACC_RES_PRO,0)", "VAR_MENO_ACC_RES_PRO");
			
			sql.addSQLClause("AND",tabAlias.concat("VAR_MENO_ACC_RES_PRO"),sql.NOT_EQUALS, 0);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_res_pro"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_res_pro"),isBaseSQL&&addDescrizione);
			}
			else
			{
				addColumn(sql,tabAlias.concat("PG_VAR_RES_PRO"),true);
				addColumn(sql,tabAlias.concat("DS_VAR_RES_PRO"),addDescrizione);
				if(isBaseSQL)
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_OBBL_RES_PRO),0)")), "VAR_MENO_OBBL_RES_PRO");
				else
					sql.addColumn("NVL(VAR_MENO_OBBL_RES_PRO,0)", "VAR_MENO_OBBL_RES_PRO");
	
				sql.addSQLClause("AND",tabAlias.concat("VAR_MENO_OBBL_RES_PRO"),sql.NOT_EQUALS, 0);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_res_pro"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_res_pro"),isBaseSQL &&addDescrizione);
			}
		}
		private void addColumnMENOSTAN(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("PG_VAR_ST_RES"),true);
				addColumn(sql,tabAlias.concat("DS_VAR_ST_RES"),addDescrizione);
				if(isBaseSQL)
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_STANZ_RES_IMP),0)")), "VAR_MENO_STANZ_RES_IMP");
				else
					sql.addColumn("NVL(VAR_MENO_STANZ_RES_IMP,0)", "VAR_MENO_STANZ_RES_IMP");
					
				sql.addSQLClause("AND",tabAlias.concat("VAR_MENO_STANZ_RES_IMP"),sql.NOT_EQUALS, 0);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_var_st_res"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_var_st_res"),isBaseSQL &&addDescrizione);
		}
		
		private void addColumnVOC(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_ELEMENTO_VOCE"),true);
			addColumn(sql,tabAlias.concat("DS_ELEMENTO_VOCE"),addDescrizione);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_elemento_voce"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_elemento_voce"),isBaseSQL&&addDescrizione);
		}
		private void addColumnACC(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String PathDestinazione){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_CDS_ACC"),true);
			addColumn(sql,tabAlias.concat("PG_ACCERTAMENTO"),true);
			addColumn(sql,tabAlias.concat("PG_ACCERTAMENTO_SCADENZARIO"),true);
			if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_REV)>=0)
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
			else
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),addDescrizione);
			
			if(PathDestinazione.endsWith(ConsGAEResBP.LIVELLO_ACC)){
				if (isBaseSQL){
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
					sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IM_OBBL_RES_PRO,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO,0)),0)")))), "DA_RISCUOTERE");
				}
				else{
					sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");
					sql.addColumn("NVL(DA_RISCUOTERE,0)", "DA_RISCUOTERE");
				}
			}
			sql.addSQLClause("AND", tabAlias.concat("PG_ACCERTAMENTO"),sql.ISNOTNULL,null);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_acc"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_accertamento"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_accertamento_scadenzario"),isBaseSQL);
			if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_REV)>=0)
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL);
			else
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL&&addDescrizione);
		}
		private void addColumnIMP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String PathDestinazione){ 
			tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("CD_CDS_OBB"),true);
				addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE"),true);
				addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE_SCADENZARIO"),true);
				if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MAN)>=0)
					addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
				else
					addColumn(sql,tabAlias.concat("DS_SCADENZA"),addDescrizione);
				if(PathDestinazione.endsWith(ConsGAEResBP.LIVELLO_IMP)){
					if (isBaseSQL){
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)")), "IM_OBBL_RES_PRO");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU_OBBL_RES_PRO),0)")), "VAR_PIU_OBBL_RES_PRO");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO_OBBL_RES_PRO),0)")), "VAR_MENO_OBBL_RES_PRO");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_PRO),0)-NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")))), "RIMASTI_DA_PAGARE_PRO");
					}else{
						sql.addColumn("NVL(IM_OBBL_RES_PRO,0)", "IM_OBBL_RES_PRO");
						sql.addColumn("NVL(VAR_PIU_OBBL_RES_PRO,0)", "VAR_PIU_OBBL_RES_PRO");
						sql.addColumn("NVL(VAR_MENO_OBBL_RES_PRO,0)", "VAR_MENO_OBBL_RES_PRO");		
						sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");		
						sql.addColumn("NVL(RIMASTI_DA_PAGARE_PRO,0)", "RIMASTI_DA_PAGARE_PRO");
					}
				}
				
				sql.addSQLClause("AND", tabAlias.concat("PG_OBBLIGAZIONE"),sql.ISNOTNULL,null);
				
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_obb"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione_scadenzario"),isBaseSQL);
				if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MAN)>=0)
					addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL);
				else
					addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL&&addDescrizione);
			}
		
		
		private void addColumnOBB(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String PathDestinazione){ 
			tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("CD_CDS_OBB"),true);
				addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE"),true);
				addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE_SCADENZARIO"),true);
				if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MOB)>=0)
					addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
				else
					addColumn(sql,tabAlias.concat("DS_SCADENZA"),addDescrizione);
				if(PathDestinazione.endsWith(ConsGAEResBP.LIVELLO_OBB)){
					if (isBaseSQL){
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_IMP),0)")), "IM_OBBL_RES_IMP");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")), "IM_MANDATI_REVERSALI_IMP");
						sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_OBBL_RES_IMP),0)-NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")))), "RIMASTI_DA_PAGARE_IMP");
					}
					else
					{
						sql.addColumn("NVL(IM_OBBL_RES_IMP,0)", "IM_OBBL_RES_IMP");
						sql.addColumn("NVL(IM_MANDATI_REVERSALI_IMP,0)", "IM_MANDATI_REVERSALI_IMP");
						sql.addColumn("NVL(RIMASTI_DA_PAGARE_IMP,0)", "RIMASTI_DA_PAGARE_IMP");
					}
				}
				
				sql.addSQLClause("AND", tabAlias.concat("PG_OBBLIGAZIONE"),sql.ISNOTNULL,null);
				
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_obb"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione_scadenzario"),isBaseSQL);
				if(PathDestinazione.indexOf(ConsGAEResBP.LIVELLO_MOB)>=0)
					addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL);
				else
					addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL&&addDescrizione);
			}
	
		private void addColumnMAN(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			
			addColumn(sql,tabAlias.concat("CD_CDS_MAN"),true);
			addColumn(sql,tabAlias.concat("PG_MANDATO"),true);
			addColumn(sql,tabAlias.concat("DS_MANDATO"),addDescrizione);
	
			if (isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
			else
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");
			
			sql.addSQLClause("AND", tabAlias.concat("PG_MANDATO"),sql.ISNOTNULL,null);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_man"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_mandato"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_mandato"),isBaseSQL&&addDescrizione);
			
		}
		private void addColumnMOB(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			
			addColumn(sql,tabAlias.concat("CD_CDS_MAN"),true);
			addColumn(sql,tabAlias.concat("PG_MANDATO"),true);
			addColumn(sql,tabAlias.concat("DS_MANDATO"),addDescrizione);
	
			if (isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_IMP),0)")), "IM_MANDATI_REVERSALI_IMP");
			else
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_IMP,0)", "IM_MANDATI_REVERSALI_IMP");
			
			sql.addSQLClause("AND", tabAlias.concat("PG_MANDATO"),sql.ISNOTNULL,null);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_man"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_mandato"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_mandato"),isBaseSQL&&addDescrizione);
			
		}
		private void addColumnREV(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			
			addColumn(sql,tabAlias.concat("CD_CDS_REV"),true);
			addColumn(sql,tabAlias.concat("PG_REVERSALE"),true);
			addColumn(sql,tabAlias.concat("DS_REVERSALE"),addDescrizione);
			if (isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_MANDATI_REVERSALI_PRO),0)")), "IM_MANDATI_REVERSALI_PRO");
			else
				sql.addColumn("NVL(IM_MANDATI_REVERSALI_PRO,0)", "IM_MANDATI_REVERSALI_PRO");
			
			sql.addSQLClause("AND", tabAlias.concat("PG_REVERSALE"),sql.ISNOTNULL,null);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_rev"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_reversale"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_reversale"),isBaseSQL&&addDescrizione);
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
			it.cnr.contab.utenze00.bulk.UtenteBulk user = new it.cnr.contab.utenze00.bulk.UtenteBulk( ((CNRUserContext)userContext).getUser() );
			user = (it.cnr.contab.utenze00.bulk.UtenteBulk)getHome(userContext, user).findByPrimaryKey(user);

			CdrBulk cdr = new CdrBulk( CNRUserContext.getCd_cdr(userContext) );

			return (CdrBulk)getHome(userContext, cdr).findByPrimaryKey(cdr);
		} catch (PersistencyException e) {
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
}
