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

package it.cnr.contab.doccont00.comp;



import java.rmi.RemoteException;

import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.doccont00.bp.ConsConfrontoEntSpeBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_confronto_ent_speBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsConfrontoEntSpeComponent extends CRUDComponent {
	private String rem_col;
			public RemoteIterator findConsultazioneModulo(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException, RemoteException, IntrospectionException {
				return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause);
			}
			
			
			private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException, RemoteException, IntrospectionException {
					CdrBulk cdrUtente = cdrFromUserContext(userContext);
					BulkHome home = getHome(userContext, V_cons_confronto_ent_speBulk.class, pathDestinazione);
					SQLBuilder sql = home.createSQLBuilder();
//					SQLBuilder sql = new SQLBuilder();
					addBaseColumns(userContext, sql,  pathDestinazione, livelloDestinazione,true);
				return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_confronto_ent_speBulk.class,null);
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
			 * @return
			 * @throws IntrospectionException 
			 * @throws IntrospectionException
			 */
			
		private void addBaseColumns(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException, IntrospectionException {		
			if (livelloDestinazione.equals(ConsConfrontoEntSpeBP.LIV_BASE)||
					livelloDestinazione.equals(ConsConfrontoEntSpeBP.LIV_BASEMOD)) {
			sql.resetColumns();
			sql.addColumn("ESERCIZIO");
			sql.addColumn("CDS");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'E'", "NVL(ASSESTATO_COMP,0)", null, null, "0)")),"ASSESTATO_COMP_ETR");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'E'", "NVL(IM_OBBL_ACC_COMP,0)", null, null, "0)")),"IM_OBBL_ACC_COMP_ETR");
			sql.addColumn("NVL(sum(IM_ASS_DOC_AMM_ETR),0)","IM_ASS_DOC_AMM_ETR");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'E'", "NVL(IM_MANDATI_REVERSALI_PRO,0)", null, null, "0)")),"IM_MANDATI_REVERSALI_PRO_ETR");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'S'", "NVL(ASSESTATO_COMP,0)", null, null, "0)")),"ASSESTATO_COMP_SPE");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'S'", "NVL(IM_OBBL_ACC_COMP,0)", null, null, "0)")),"IM_OBBL_ACC_COMP_SPE");
			sql.addColumn("NVL(SUM(IM_ASS_DOC_AMM_SPE),0)","IM_ASS_DOC_AMM_SPE");
			sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'S'", "NVL(IM_MANDATI_REVERSALI_PRO,0)", null, null, "0)")),"IM_MANDATI_REVERSALI_PRO_SPE");
			sql.addColumn("NVL(sum(tratt_econ_int),0)","TRATT_ECON_INT");
			sql.addColumn("NVL(sum(tratt_econ_est),0)","TRATT_ECON_EST");
			addSQLGroupBy(sql,"ESERCIZIO",true);
			addSQLGroupBy(sql,"CDS",true);
			}
			
			if (livelloDestinazione.equals(ConsConfrontoEntSpeBP.LIV_BASEMODGAE)||
					livelloDestinazione.equals(ConsConfrontoEntSpeBP.LIV_BASEMODGAEVOCE)) {
				sql.resetColumns();
				sql.addColumn("ESERCIZIO");
				sql.addColumn("CDS");
				sql.addColumn("NVL(sum(ASSESTATO_COMP),0)","ASSESTATO_COMP");
				sql.addColumn("NVL(sum(IM_OBBL_ACC_COMP),0)", "IM_OBBL_ACC_COMP");
				sql.addColumn("SUM(".concat(sql.addDecode("TI_GESTIONE", "'E'", "NVL(IM_ASS_DOC_AMM_ETR,0)", "'S'", "NVL(IM_ASS_DOC_AMM_SPE,0)", "0)")),"IM_ASS_DOC_AMM");
				sql.addColumn("NVL(sum(IM_MANDATI_REVERSALI_PRO),0)","IM_MANDATI_REVERSALI_PRO");
				sql.addColumn("NVL(sum(tratt_econ_int),0)","TRATT_ECON_INT");
				sql.addColumn("NVL(sum(tratt_econ_est),0)","TRATT_ECON_EST");
				addSQLGroupBy(sql,"ESERCIZIO",true);
				addSQLGroupBy(sql,"CDS",true);
			}
				
				
				
			if (pathDestinazione.indexOf("BASEMOD")>=0){ 
			
				if (pathDestinazione.indexOf(ConsConfrontoEntSpeBP.LIV_BASEMOD)>=0) {
					addColumnMOD(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsConfrontoEntSpeBP.LIV_BASEMODGAE)>=0){
					addColumnGAE(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsConfrontoEntSpeBP.LIV_BASEMODGAEVOCE)>=0){
					addColumnVOCE(sql,true,pathDestinazione);
				}
				
			}
		
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
			private void addColumnMOD(SQLBuilder sql, boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("BASEMOD")>=0 ){
					addColumn(sql,"CD_MODULO",true);
//					addColumn(sql,"DS_MODULO",true);
					addSQLGroupBy(sql,"CD_MODULO",isBaseSQL);
//					addSQLGroupBy(sql,"DS_MODULO",isBaseSQL&&true);
				}
			}
			
			
			/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne GAE
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
			private void addColumnGAE(SQLBuilder sql,  boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("BASEMODGAE")>=0){
					addColumn(sql,"TI_GESTIONE",true);
//					addColumn(sql.addDecode("TI_GESTIONE", "'E'", "'Entrata'", "'S'", "'Spesa'", "TI_GESTIONE"),"TI_GESTIONE",true);
					addColumn(sql,"CD_CENTRO_RESPONSABILITA",true);
					addColumn(sql,"CD_LINEA_ATTIVITA",true);
					addColumn(sql,"CD_NATURA",true);
//					addColumn(sql,"DS_LDA",true);
					addSQLGroupBy(sql,"TI_GESTIONE",isBaseSQL);
					addSQLGroupBy(sql,"CD_CENTRO_RESPONSABILITA",isBaseSQL);
					addSQLGroupBy(sql,"CD_LINEA_ATTIVITA",isBaseSQL);
					addSQLGroupBy(sql,"CD_NATURA",isBaseSQL);
//					addSQLGroupBy(sql,"DS_LDA",isBaseSQL&&true);
				}
			}
			
			/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne GAE
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
			private void addColumnVOCE(SQLBuilder sql,boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("BASEMODGAEVOCE")>=0){
//					addColumn(sql,"CD_VOCE",true);
					addColumn(sql,"CD_ELEMENTO_VOCE",true);
//					addColumn(sql,"DS_VOCE",true);
//					addSQLGroupBy(sql,"CD_VOCE",isBaseSQL);
					addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",isBaseSQL);
//					addSQLGroupBy(sql,"DS_VOCE",isBaseSQL&&true);
				}
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
			private SQLBuilder completaSQL(SQLBuilder sql, CompoundFindClause baseClause, CompoundFindClause findClause){
				sql.addClause(baseClause);
				if (findClause==null)
					return sql;
				else {
					sql.addClause(findClause);
					return sql;
				}
				}
		
}
