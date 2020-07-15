
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

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdsBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.prevent01.consultazioni.bp.ConsPDGGDipBP;
import it.cnr.contab.prevent01.consultazioni.bulk.V_cons_pdgg_dipBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGGDipComponent extends CRUDComponent {
	
		public RemoteIterator findConsultazioneEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
		}

		public RemoteIterator findConsultazioneSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
		}

		private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws it.cnr.jada.comp.ComponentException {
			BulkHome home = getHome(userContext, V_cons_pdgg_dipBulk.class, pathDestinazione);
			SQLBuilder sql = home.createSQLBuilder();
			SQLBuilder sqlEsterna = home.createSQLBuilder();
			
			try {
			   String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
			   Unita_organizzativaBulk 	uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_unita_organizzativa));
			   Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache(userContext).getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
			   String tabAlias = sql.getColumnMap().getTableName();	
			   if (tabAlias.indexOf("V_CONS_PDGG_ETR_BIL_IST_DIP_FO")>=0){
				   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
					   sql.addSQLClause("AND","SUBSTR(CD_CENTRO_RESPONSABILITA,1,3)",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
					   sqlEsterna.addSQLClause("AND","SUBSTR(CD_CENTRO_RESPONSABILITA,1,3)",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				   }
				   if(!uoScrivania.isUoCds()){
					   sql.addSQLClause("AND","SUBSTR(CD_CENTRO_RESPONSABILITA,1,7)",SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());  
					   sqlEsterna.addSQLClause("AND","SUBSTR(CD_CENTRO_RESPONSABILITA,1,7)",SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());
				   }
			   }
			   else{
				   if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
					   sql.addSQLClause("AND","SUBSTR(CD_CDR_ASSEGNATARIO,1,3)",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
					   sqlEsterna.addSQLClause("AND","SUBSTR(CD_CDR_ASSEGNATARIO,1,3)",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				   }
				   if(!uoScrivania.isUoCds()){
					   sql.addSQLClause("AND","SUBSTR(CD_CDR_ASSEGNATARIO,1,7)",SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());  
					   sqlEsterna.addSQLClause("AND","SUBSTR(CD_CDR_ASSEGNATARIO,1,7)",SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());
				   }		   
			   }
			
			
			addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione);
			return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_pdgg_dipBulk.class,null);
			} catch (PersistencyException e) {
				throw new ComponentException(e);
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
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		sqlEsterna.resetColumns();
			
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_CDR)>=0) {
				addColumnCDR(sql,tabAlias,(livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_ETRCDR)||livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_SPECDR)),true,pathDestinazione);
				addColumnCDR(sqlEsterna,tabAlias,(livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_ETRCDR)||livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_SPECDR)),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_DIP)>=0){
				addColumnDIP(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_DIP),true);
				addColumnDIP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_DIP),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_PRO)>=0){
				addColumnPRO(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_PRO),true);
				addColumnPRO(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_PRO),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_COM)>=0){
				addColumnCOM(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_COM),true);
				addColumnCOM(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_COM),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_MOD)>=0){
				addColumnMOD(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_MOD),true);
				addColumnMOD(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_MOD),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_LIV1)>=0){
				addColumnLIV1(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV1),true,livelloDestinazione,userContext);
				addColumnLIV1(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV1),false,livelloDestinazione,userContext);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_LIV2)>=0){
				addColumnLIV2(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV2),true);
				addColumnLIV2(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV2),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_LIV3)>=0){
				addColumnLIV3(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV3),true);
				addColumnLIV3(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIV3),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_LIN)>=0){
				addColumnLIN(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIN),true);
				addColumnLIN(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_LIN),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_VOC)>=0){
				addColumnVOC(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_VOC),true);
				addColumnVOC(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_VOC),false);
			}
			if (pathDestinazione.indexOf(ConsPDGGDipBP.LIVELLO_DET)>=0){
				addColumnDET(sql,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_DET),true);
				addColumnDET(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGGDipBP.LIVELLO_DET),false);
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
		private SQLBuilder completaSQL(SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause){ 
			sql.addClause(baseClause);		
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
		if (tabAlias.indexOf("V_CONS_PDGG_ETR_BIL_IST_DIP_FO")>=0){
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_ENT_IST_A1),0)")), "TOT_ENT_IST_A1");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_ENT_AREE_A1),0)")), "TOT_ENT_AREE_A1");
				sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("TOT_ENT_IST_A1,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("TOT_ENT_AREE_A1,0)),0)")))), "TOT_ENT_A1");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_INC_IST_A1),0)")), "TOT_INC_IST_A1");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("TOT_INC_AREE_A1),0)")), "TOT_INC_AREE_A1");
				sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("TOT_INC_IST_A1,0)),0)+NVL(SUM(nvl(".concat(tabAlias.concat("TOT_INC_AREE_A1,0)),0)")))), "TOT_INC_A1");
			}
			else
			{
				sql.addColumn("NVL(TOT_ENT_IST_A1,0)", "TOT_ENT_IST_A1");
				sql.addColumn("NVL(TOT_ENT_AREE_A1,0)", "TOT_ENT_AREE_A1");
				sql.addColumn("NVL(TOT_ENT_A1,0)", "TOT_ENT_A1");
				sql.addColumn("NVL(TOT_INC_IST_A1,0)", "TOT_INC_IST_A1");
				sql.addColumn("NVL(TOT_INC_AREE_A1,0)", "TOT_INC_AREE_A1");
				sql.addColumn("NVL(TOT_INC_A1,0)", "TOT_INC_A1");
			}
		}
		else{		
				if (isSommatoria) 
				{
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
				else
				{
					sql.addColumn("NVL(IM_DEC_IST_INT,0)", "IM_DEC_IST_INT");
					sql.addColumn("NVL(IM_DEC_IST_EST,0)", "IM_DEC_IST_EST");
					sql.addColumn("NVL(IM_DEC_AREA_INT,0)", "IM_DEC_AREA_INT");
					sql.addColumn("NVL(IM_DEC_AREA_EST,0)", "IM_DEC_AREA_EST");
					sql.addColumn("NVL(IMP_TOT_DEC_INT,0)", "IMP_TOT_DEC_INT");
					sql.addColumn("NVL(IMP_TOT_DEC_EST,0)", "IMP_TOT_DEC_EST");
					sql.addColumn("NVL(IMP_TOT_DECENTRATO,0)", "IMP_TOT_DECENTRATO");
					sql.addColumn("NVL(TRATT_ECON_INT,0)", "TRATT_ECON_INT");
					sql.addColumn("NVL(TRATT_ECON_EST,0)", "TRATT_ECON_EST");
					sql.addColumn("NVL(IM_ACC_ALTRE_SP_INT,0)", "IM_ACC_ALTRE_SP_INT");
					sql.addColumn("NVL(IMP_TOT_COMP_INT,0)", "IMP_TOT_COMP_INT");
					sql.addColumn("NVL(IMP_TOT_COMP_EST,0)", "IMP_TOT_COMP_EST");					
					sql.addColumn("NVL(IM_PAGAMENTI,0)", "IM_PAGAMENTI");
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
		private void addColumnCDR(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String pathDestinazione ){ 
			tabAlias = getAlias(tabAlias);
			if (pathDestinazione.indexOf("ETRCDR")>=0 || pathDestinazione.indexOf("SPECDR")>=0){
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
			}	
			 if (pathDestinazione.indexOf("ETRCDR")>=0){
				addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),isBaseSQL);

				addColumn(sql,tabAlias.concat("DS_CDR"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr"),isBaseSQL&&addDescrizione);
			}else
			{
				addColumn(sql,tabAlias.concat("CD_CDR_ASSEGNATARIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cdr_assegnatario"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CDR_ASSEGNATARIO"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_cdr_assegnatario"),isBaseSQL&&addDescrizione);
			}
}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del dipartimento
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		
		   private void addColumnDIP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			   tabAlias = getAlias(tabAlias);
			   addColumn(sql,tabAlias.concat("CD_DIPARTIMENTO"),true);
			   addColumn(sql,tabAlias.concat("DS_DIPARTIMENTO"),addDescrizione);
			   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_dipartimento"),isBaseSQL);
			   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dipartimento"),isBaseSQL&&addDescrizione);
		   }
		/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne del progetto
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
		
			   private void addColumnPRO(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
				   tabAlias = getAlias(tabAlias);
				   addColumn(sql,tabAlias.concat("PG_PROGETTO"),true);
				   addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
				   addColumn(sql,tabAlias.concat("DS_PROGETTO"),addDescrizione);
				   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),isBaseSQL);
				   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_progetto"),isBaseSQL);
				   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_progetto"),isBaseSQL&&addDescrizione);
			   } 
				/**
				 * Aggiunge nell'SQLBuilder <sql> le colonne del commessa
				 *
				 * @param sql l'SQLBuilder da aggiornare
				 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
				 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
				 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
				 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
				 */
		
				   private void addColumnCOM(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
					   tabAlias = getAlias(tabAlias);
					   addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
					   addColumn(sql,tabAlias.concat("DS_COMMESSA"),addDescrizione);
					   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),isBaseSQL);
					   addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_commessa"),isBaseSQL&&addDescrizione);
				   }	   
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne Modulo
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnMOD(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_MODULO"),true);
			addColumn(sql,tabAlias.concat("DS_MODULO"),addDescrizione);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL&&addDescrizione);
		}
		private void addColumnVOC(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_ELEMENTO_VOCE"),true);
			addColumn(sql,tabAlias.concat("DS_ELEMENTO_VOCE"),addDescrizione);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_elemento_voce"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_elemento_voce"),isBaseSQL&&addDescrizione);
		}
		private void addColumnLIN(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
			addColumn(sql,tabAlias.concat("DS_LINEA"),addDescrizione);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_linea"),isBaseSQL&&addDescrizione);
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del primo livello della classificazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnLIV1(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String livello_arrivo,UserContext context) throws ComponentException{ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_LIVELLO1"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello1"),isBaseSQL);
			if (isBaseSQL && addDescrizione) {
				sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
				addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
				addSQLGroupBy(sql,"classificazione_voci.ds_classificazione",true);

				sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
				if (sql.getColumnMap().getTableName().equals("V_CONS_PDGG_ETR_BIL_IST_DIP_FO"))
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
				else
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
				sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO2",sql.ISNULL,null);
			}
			else
				addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del secondo livello della classificazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnLIV2(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_LIVELLO2"),true);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello2"),isBaseSQL);
			if (isBaseSQL && addDescrizione) {
				sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
				addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
				sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
				sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
				if (sql.getColumnMap().getTableName().equals("V_CONS_PDGG_ETR_BIL_IST_DIP_FO"))
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
				else
					sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
				sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
				sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO3",sql.ISNULL,null);
			}
			else
				addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
		}
		
		private void addColumnLIV3(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
				tabAlias = getAlias(tabAlias);
				addColumn(sql,tabAlias.concat("CD_LIVELLO3"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello3"),isBaseSQL);
				if (isBaseSQL && addDescrizione) {
					sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
					addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
					sql.addSQLGroupBy("classificazione_voci.ds_classificazione");
					sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
					if (sql.getColumnMap().getTableName().equals("V_CONS_PDGG_ETR_BIL_IST_DIP_FO"))
						sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
					else
						sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO2"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO2");
					sql.addSQLJoin(tabAlias.concat("CD_LIVELLO3"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO3");
					sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO4",sql.ISNULL,null);
				}
				else
					addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
			}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del dettaglio ultimo della Consultazione.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnDET(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			if (tabAlias.indexOf("V_CONS_PDGG_ETR_BIL_IST_DIP_FO")>=0){
				addColumn(sql,tabAlias.concat("DS_DETTAGLIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dettaglio"),isBaseSQL);
				addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),isBaseSQL&&addDescrizione);
			}	else
			{
				addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),isBaseSQL);
				addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),isBaseSQL&&addDescrizione);	
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
	}