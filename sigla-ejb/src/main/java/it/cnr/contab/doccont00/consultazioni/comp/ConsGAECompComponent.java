
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

import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.ejb.EJBException;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.consultazioni.bp.ConsGAECompBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_competenza_entBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_gae_competenza_speBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
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
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;
import it.cnr.jada.util.RemoteIterator;


public class ConsGAECompComponent extends CRUDComponent {
		private static String TIPO_ETR = "ETR"; 
		private static String TIPO_SPE = "SPE"; 

		public RemoteIterator findConsultazioneEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, TIPO_ETR);
		}

		public RemoteIterator findConsultazioneSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, TIPO_SPE);
		}

		private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws it.cnr.jada.comp.ComponentException {
			CdrBulk cdrUtente = cdrFromUserContext(userContext);
			if (pathDestinazione.indexOf("ETRLIN")>=0){ 
				BulkHome home = getHome(userContext, V_cons_gae_competenza_entBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione, tipoCons);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause,pathDestinazione),V_cons_gae_competenza_entBulk.class,null);
			}
			else
			{
				BulkHome home = getHome(userContext, V_cons_gae_competenza_speBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione, tipoCons);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause,pathDestinazione),V_cons_gae_competenza_speBulk.class,null);
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
	private void addBaseColumns(UserContext userContext,SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione, String livelloDestinazione, String tipoCons) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		sqlEsterna.resetColumns();
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_ETRLIN)>=0||pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_SPELIN)>=0) {
				addColumnLIN(sql,tabAlias,(livelloDestinazione.equals(ConsGAECompBP.LIVELLO_ETRLIN)||livelloDestinazione.equals(ConsGAECompBP.LIVELLO_SPELIN)),true,pathDestinazione);
				addColumnLIN(sqlEsterna,tabAlias,(livelloDestinazione.equals(ConsGAECompBP.LIVELLO_ETRLIN)||livelloDestinazione.equals(ConsGAECompBP.LIVELLO_SPELIN)),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_VOC)>=0){
				addColumnVOC(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VOC),true);
				addColumnVOC(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VOC),false);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_VARP)>=0){
				addColumnVARP(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VARP),true);
				addColumnVARP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VARP),false);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_VARM)>=0){
				addColumnVARM(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VARM),true);
				addColumnVARM(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_VARM),false);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_IMP)>=0){
				addColumnIMP(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_IMP),true,pathDestinazione);
				addColumnIMP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_IMP),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_MAN)>=0){
				addColumnMAN(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_MAN),true);
				addColumnMAN(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_MAN),false);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_ACC)>=0){
				addColumnACC(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_ACC),true,pathDestinazione);
				addColumnACC(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_ACC),false,pathDestinazione);
			}
			if (pathDestinazione.indexOf(ConsGAECompBP.LIVELLO_REV)>=0){
				addColumnREV(sql,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_REV),true);
				addColumnREV(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsGAECompBP.LIVELLO_REV),false);
			}
		
			if(!isUtenteEnte(userContext)){ 
			CdrBulk cdrUtente = cdrFromUserContext(userContext);
				if ( !cdrUtente.isCdrILiv() ){
				
			/*	sql.addTableToHeader("V_CDR_VALIDO_LIV1");
				sql.addSQLJoin(tabAlias.concat(".ESERCIZIO"),"V_CDR_VALIDO_LIV1.ESERCIZIO");
				sql.addSQLJoin(tabAlias.concat(".CD_CENTRO_RESPONSABILITA"),"V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA");
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.CD_CENTRO_RESPONSABILITA",sql.EQUALS,cdrUtente.getCd_centro_responsabilita());
				sql.addSQLClause("AND", "V_CDR_VALIDO_LIV1.ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));*/
				
				sql.addSQLClause("AND", "CD_CENTRO_RESPONSABILITA",sql.EQUALS,CNRUserContext.getCd_cdr(userContext));
				sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			 }
				else {
					sql.addSQLClause("AND", "ESERCIZIO", SQLBuilder.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
			 		sql.addSQLClause("AND", "CD_CDS", SQLBuilder.EQUALS, CNRUserContext.getCd_cds(userContext));
					
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
			if (pathDestinazione.compareTo("ETRLIN")==0||pathDestinazione.compareTo("SPELIN")==0||pathDestinazione.endsWith("VOC") ){
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
		if (tabAlias.indexOf("V_CONS_GAE_COMPETENZA_ENT")>=0){
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)")), "IM_STANZ_INIZIALE_A1");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)")), "VARIAZIONI_PIU");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)")), "VARIAZIONI_MENO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)+NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)-NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)")))))), "ASSESTATO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("ACCERTAMENTI_COMP),0)")), "ACCERTAMENTI_COMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)+NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)-NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)-NVL(SUM(".concat(tabAlias.concat("ACCERTAMENTI_COMP),0)")))))))), "DISPONIBILE");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("REVERSALI_COMP),0)")), "REVERSALI_COMP");
				sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("ACCERTAMENTI_COMP,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("REVERSALI_COMP,0)),0)")))), "DA_INCASSARE");
			}
			else
			{
				sql.addColumn("NVL(IM_STANZ_INIZIALE_A1,0)", "IM_STANZ_INIZIALE_A1");
				sql.addColumn("NVL(VARIAZIONI_PIU,0)", "VARIAZIONI_PIU");
				sql.addColumn("NVL(VARIAZIONI_MENO,0)", "VARIAZIONI_MENO");
				sql.addColumn("NVL(ASSESTATO,0)", "ASSESTATO");
				sql.addColumn("NVL(ACCERTAMENTI_COMP,0)", "ACCERTAMENTI_COMP");
				sql.addColumn("NVL(DISPONIBILE,0)", "DISPONIBILE");
				sql.addColumn("NVL(REVERSALI_COMP,0)", "REVERSALI_COMP");
				sql.addColumn("NVL(DA_INCASSARE,0)", "DA_INCASSARE");	
			}
		}else
		{	
			if (isSommatoria) 
			{
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)")), "IM_STANZ_INIZIALE_A1");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)")), "VARIAZIONI_PIU");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)")), "VARIAZIONI_MENO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)+NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)-NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)")))))), "ASSESTATO");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IMPEGNI_COMP),0)")), "IMPEGNI_COMP");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IM_STANZ_INIZIALE_A1),0)+NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)-NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)-NVL(SUM(".concat(tabAlias.concat("IMPEGNI_COMP),0)")))))))), "DISPONIBILE");
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("MANDATI_COMP),0)")), "MANDATI_COMP");
				sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IMPEGNI_COMP,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("MANDATI_COMP,0)),0)")))), "DA_PAGARE");
			}
			else
			{
				sql.addColumn("NVL(IM_STANZ_INIZIALE_A1,0)", "IM_STANZ_INIZIALE_A1");
				sql.addColumn("NVL(VARIAZIONI_PIU,0)", "VARIAZIONI_PIU");
				sql.addColumn("NVL(VARIAZIONI_MENO,0)", "VARIAZIONI_MENO");
				sql.addColumn("NVL(ASSESTATO,0)", "ASSESTATO");
				sql.addColumn("NVL(IMPEGNI_COMP,0)", "IMPEGNI_COMP");
				sql.addColumn("NVL(DISPONIBILE,0)", "DISPONIBILE");
				sql.addColumn("NVL(MANDATI_COMP,0)", "MANDATI_COMP");
				sql.addColumn("NVL(DA_PAGARE,0)", "DA_PAGARE");
				
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
		private void addColumnLIN(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String pathDestinazione){
				tabAlias = getAlias(tabAlias);
				
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_CENTRO_RESPONSABILITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_centro_responsabilita"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_LINEA_ATTIVITA"),addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_linea_attivita"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC)));
				
				addColumn(sql,tabAlias.concat("DS_NATURA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_natura"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_DIPARTIMENTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_dipartimento"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("CD_PROGETTO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_progetto"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_PROGETTO"),addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_progetto"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC)));	
				
				addColumn(sql,tabAlias.concat("CD_COMMESSA"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_commessa"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_COMMESSA"),addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_commessa"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC)));
				
				addColumn(sql,tabAlias.concat("CD_MODULO"),true);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_modulo"),isBaseSQL);
				
				addColumn(sql,tabAlias.concat("DS_MODULO"),addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC));
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_modulo"),isBaseSQL&&(addDescrizione||pathDestinazione.endsWith(ConsGAECompBP.LIVELLO_VOC)));	
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
			addColumn(sql,tabAlias.concat("PG_VARIAZIONE_PDG"),true);
			addColumn(sql,tabAlias.concat("DS_VARIAZIONE"),addDescrizione);
			if(isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_PIU),0)")), "VARIAZIONI_PIU");
			else
				sql.addColumn("NVL(VARIAZIONI_PIU,0)", "VARIAZIONI_PIU");
			
			sql.addSQLClause("AND",tabAlias.concat("VARIAZIONI_PIU"),sql.NOT_EQUALS, 0);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_variazione_pdg"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_variazione"),isBaseSQL &&addDescrizione);
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
			
			addColumn(sql,tabAlias.concat("PG_VARIAZIONE_PDG"),true);
			addColumn(sql,tabAlias.concat("DS_VARIAZIONE"),addDescrizione);
			if(isBaseSQL)
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VARIAZIONI_MENO),0)")), "VARIAZIONI_MENO");
			else
				sql.addColumn("NVL(VARIAZIONI_MENO,0)", "VARIAZIONI_MENO");
			
			sql.addSQLClause("AND",tabAlias.concat("VARIAZIONI_MENO"),sql.NOT_EQUALS, 0);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_variazione_pdg"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_variazione"),isBaseSQL&&addDescrizione);
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
			if(PathDestinazione.indexOf(ConsGAECompBP.LIVELLO_REV)>=0)
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
			else
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),addDescrizione);
			
			if(PathDestinazione.endsWith(ConsGAECompBP.LIVELLO_ACC)){
				if (isBaseSQL){
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("ACCERTAMENTI_COMP),0)")), "ACCERTAMENTI_COMP");
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("REVERSALI_COMP),0)")), "REVERSALI_COMP");
					sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("ACCERTAMENTI_COMP,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("REVERSALI_COMP,0)),0)")))), "DA_INCASSARE");
				}
				else{
					sql.addColumn("NVL(ACCERTAMENTI_COMP,0)", "ACCERTAMENTI_COMP");
					sql.addColumn("NVL(REVERSALI_COMP,0)", "REVERSALI_COMP");
					sql.addColumn("NVL(DA_INCASSARE,0)", "DA_INCASSARE");		
				}
			}
			sql.addSQLClause("AND", tabAlias.concat("PG_ACCERTAMENTO"),sql.ISNOTNULL,null);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_acc"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_accertamento"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_accertamento_scadenzario"),isBaseSQL);
			if(PathDestinazione.indexOf(ConsGAECompBP.LIVELLO_REV)>=0)
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL);
			else
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_scadenza"),isBaseSQL&&addDescrizione);
		}
		private void addColumnIMP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL,String PathDestinazione){ 
			tabAlias = getAlias(tabAlias);
			
			addColumn(sql,tabAlias.concat("CD_CDS_OBB"),true);
			addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE"),true);
			addColumn(sql,tabAlias.concat("PG_OBBLIGAZIONE_SCADENZARIO"),true);
			if(PathDestinazione.indexOf(ConsGAECompBP.LIVELLO_MAN)>=0)
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),true);
			else
				addColumn(sql,tabAlias.concat("DS_SCADENZA"),addDescrizione);
			if(PathDestinazione.endsWith(ConsGAECompBP.LIVELLO_IMP)){
				if (isBaseSQL){
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("IMPEGNI_COMP),0)")), "IMPEGNI_COMP");
					sql.addColumn("NVL(SUM(".concat(tabAlias.concat("MANDATI_COMP),0)")), "MANDATI_COMP");
					sql.addColumn("NVL(SUM(nvl(".concat(tabAlias.concat("IMPEGNI_COMP,0)),0)-NVL(SUM(nvl(".concat(tabAlias.concat("MANDATI_COMP,0)),0)")))), "DA_PAGARE");
				}else{
					sql.addColumn("NVL(IMPEGNI_COMP,0)", "IMPEGNI_COMP");
					sql.addColumn("NVL(MANDATI_COMP,0)", "MANDATI_COMP");
					sql.addColumn("NVL(DA_PAGARE,0)", "DA_PAGARE");
				}
			}
			
			sql.addSQLClause("AND", tabAlias.concat("PG_OBBLIGAZIONE"),sql.ISNOTNULL,null);
			
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds_obb"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_obbligazione_scadenzario"),isBaseSQL);
			if(PathDestinazione.indexOf(ConsGAECompBP.LIVELLO_MAN)>=0)
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
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("MANDATI_COMP),0)")), "MANDATI_COMP");
			else
				sql.addColumn("NVL(MANDATI_COMP,0)", "MANDATI_COMP");
			
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
				sql.addColumn("NVL(SUM(".concat(tabAlias.concat("REVERSALI_COMP),0)")), "REVERSALI_COMP");
			else
				sql.addColumn("NVL(REVERSALI_COMP,0)", "REVERSALI_COMP");
			
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
}
