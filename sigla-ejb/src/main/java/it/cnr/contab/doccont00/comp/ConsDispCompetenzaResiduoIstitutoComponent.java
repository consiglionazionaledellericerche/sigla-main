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



import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.doccont00.bp.ConsDispCompResEntIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResEntIstVoceBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstCdrGaeBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResIstVoceBP;
import it.cnr.contab.doccont00.bp.ConsDispCompResVoceNatBP;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_disp_comp_resBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.V_cons_disp_comp_res_entBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;


public class ConsDispCompetenzaResiduoIstitutoComponent extends CRUDComponent {
	private String rem_col;
			public RemoteIterator findConsultazioneCdrGae(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			public RemoteIterator findConsultazioneVoce(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			public RemoteIterator findConsultazioneDip(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneDettaglioDip(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			public RemoteIterator findConsultazioneVoceNat(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneDettaglioVoce(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			public RemoteIterator findConsultazioneEntrateCdrGae(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneEntrateDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			public RemoteIterator findConsultazioneEntrateVoce(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws ComponentException {
				return findConsultazioneEntrateDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, null);
			}
			
			private RemoteIterator findConsultazioneDettaglioDip(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws ComponentException {
				try{
					String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
					Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_unita_organizzativa));
					Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache(userContext).getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
					BulkHome home = getHome(userContext, V_cons_disp_comp_resBulk.class, pathDestinazione);
					SQLBuilder sql = home.createSQLBuilder();
					sql.addSQLClause("AND","DIPARTIMENTO",SQLBuilder.EQUALS,getDipartimentoScrivania(userContext));
				    if (!((CNRUserContext) userContext).getCd_unita_organizzativa().equals( ente.getCd_unita_organizzativa())){			
					   sql.addSQLClause("AND","CDS",SQLBuilder.EQUALS,CNRUserContext.getCd_cds(userContext));
				    }
				    if(!uoScrivania.isUoCds()){
					   sql.addSQLClause("AND","UO",SQLBuilder.EQUALS,uoScrivania.getCd_unita_organizzativa());  
				    }
				    addBaseColumnsDip(userContext, sql,  pathDestinazione, livelloDestinazione,true);
				    return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_disp_comp_resBulk.class,null);
				} catch (PersistencyException e) {
					throw new ComponentException(e);
				}	
			}
			
			private RemoteIterator findConsultazioneDettaglioVoce(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws ComponentException {
//				try{
//					String cd_unita_organizzativa = it.cnr.contab.utenze00.bp.CNRUserContext.getCd_unita_organizzativa(userContext);
//					Unita_organizzativaBulk uoScrivania = (Unita_organizzativaBulk) getHome(userContext,Unita_organizzativaBulk.class).findByPrimaryKey(new Unita_organizzativaBulk(cd_unita_organizzativa));
//					Unita_organizzativa_enteBulk ente = (Unita_organizzativa_enteBulk) getHomeCache(userContext).getHome(Unita_organizzativa_enteBulk.class).findAll().get(0);
					BulkHome home = getHome(userContext, V_cons_disp_comp_resBulk.class, pathDestinazione);
					SQLBuilder sql = home.createSQLBuilder();
				    addBaseColumnsVoce(userContext, sql,  pathDestinazione, livelloDestinazione,true);
				    return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_disp_comp_resBulk.class,null);
//				} catch (PersistencyException e) {
//					throw new ComponentException(e);
//				}	
			}
			
			private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws ComponentException {
					CdrBulk cdrUtente = cdrFromUserContext(userContext);
					BulkHome home = getHome(userContext, V_cons_disp_comp_resBulk.class, pathDestinazione);
					SQLBuilder sql = home.createSQLBuilder();
					addBaseColumns(userContext, sql,  pathDestinazione, livelloDestinazione,true);
				return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_disp_comp_resBulk.class,null);
			}
			
			private RemoteIterator findConsultazioneEntrateDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws ComponentException {
				CdrBulk cdrUtente = cdrFromUserContext(userContext);
				BulkHome home = getHome(userContext, V_cons_disp_comp_res_entBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				addBaseColumnsEntrate(userContext, sql,  pathDestinazione, livelloDestinazione,true);
			return iterator(userContext, completaSQL(sql,baseClause,findClause),V_cons_disp_comp_res_entBulk.class,null);
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
			 */
			
		private void addBaseColumns(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException {		
		sql.resetColumns();
			sql.addColumn("CDS");
			sql.addColumn("DS_CDS");
			sql.addColumn("UO");
			sql.addColumn("ESERCIZIO");
			sql.addColumn("ESERCIZIO_RES");
			sql.addColumn("NVL(SUM(STANZ_INI),0)","STANZ_INI");
			sql.addColumn("NVL(SUM(VAR_PIU),0)","VAR_PIU");
			sql.addColumn("NVL(SUM(VAR_MENO),0)","VAR_MENO");
			sql.addColumn("NVL(SUM(ASSESTATO_COMP),0)","ASSESTATO_COMP");
			sql.addColumn("NVL(SUM(OBB_COMP),0)","OBB_COMP");
			sql.addColumn("NVL(SUM(DISP_COMP),0)","DISP_COMP");
			sql.addColumn("NVL(SUM(PAGATO_COMP),0)","PAGATO_COMP");
			sql.addColumn("NVL(SUM(STANZ_RES_IMP),0)","STANZ_RES_IMP");
			sql.addColumn("NVL(SUM(VAR_PIU_STANZ_RES_IMP),0)","VAR_PIU_STANZ_RES_IMP");
			sql.addColumn("NVL(SUM(VAR_MENO_STANZ_RES_IMP),0)","VAR_MENO_STANZ_RES_IMP");
			sql.addColumn("NVL(SUM(ASSESTATO_RES_IMP),0)","ASSESTATO_RES_IMP");
			sql.addColumn("NVL(SUM(OBB_RES_IMP),0)","OBB_RES_IMP");
			sql.addColumn("NVL(SUM(OBB_RES_PRO),0)","OBB_RES_PRO");
			sql.addColumn("NVL(SUM(VAR_PIU_RES_PRO),0)","VAR_PIU_RES_PRO");
			sql.addColumn("NVL(SUM(VAR_MENO_RES_PRO),0)","VAR_MENO_RES_PRO");
			sql.addColumn("NVL(SUM(VINCOLI_RES),0)","VINCOLI_RES");
			sql.addColumn("NVL(SUM(DISP_RES),0)","DISP_RES");
			sql.addColumn("NVL(SUM(PAGATO_RES),0)","PAGATO_RES");
			sql.addColumn("NVL(SUM(OBB_COMP),0)-NVL(SUM(PAGATO_COMP),0)", "RIMASTI_DA_PAGARE_COMP");
			sql.addColumn("NVL(SUM(OBB_RES_IMP),0)+NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)-NVL(SUM(PAGATO_RES),0)", "RIMASTI_DA_PAGARE_RES");
			sql.addColumn("NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)", "ASS_RES");
			addSQLGroupBy(sql,"CDS",true);
			addSQLGroupBy(sql,"DS_CDS",true);
			addSQLGroupBy(sql,"UO",true);
			addSQLGroupBy(sql,"ESERCIZIO",true);
			addSQLGroupBy(sql,"ESERCIZIO_RES",true);

			
			if (pathDestinazione.indexOf("BASECDSPROG")>=0){ 
			
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROG)>=0) {
					addColumnPROG(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMM)>=0){
					addColumnCOMM(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMOD)>=0){
					addColumnMOD(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDR)>=0){
					addColumnCDR(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDRGAE)>=0){
					addColumnGAE(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSPROGCOMMMODCDRGAEDET)>=0){
					addColumnVOCE(sql,true,pathDestinazione);
				}
			}
			
			if (pathDestinazione.indexOf("BASECDSCDR")>=0){ 
				
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDR)>=0) {
					addColumnCDR(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDRGAE)>=0){
					addColumnGAE(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_BASECDSCDRGAEDET)>=0){
					addColumnVOCE(sql,true,pathDestinazione);
				}
			}
			
			if (pathDestinazione.indexOf("BASECDSVOCE")>=0){ 
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCE)>=0) {
					addColumnVOCE(sql,true,pathDestinazione);
				}
			}
			
			if (pathDestinazione.indexOf("BASECDSVOCEPROG")>=0){ 
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROG)>=0){
					addColumnPROG(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMM)>=0){
					addColumnCOMM(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMOD)>=0){
					addColumnMOD(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMODCDR)>=0){
					addColumnCDR(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCEPROGCOMMMODCDRGAE)>=0){
					addColumnGAE(sql,true,pathDestinazione);
				}
			}
			
			if (pathDestinazione.indexOf("BASECDSVOCECDR")>=0){ 
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCECDR)>=0) {
					addColumnCDR(sql,true,pathDestinazione);
				}
				if (pathDestinazione.indexOf(ConsDispCompResIstVoceBP.LIV_BASECDSVOCECDRGAE)>=0){
					addColumnGAE(sql,true,pathDestinazione);
				}
			}
		}
		
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne del Cds.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnCDS(SQLBuilder sql, boolean isBaseSQL,String pathDestinazione ){ 
			if (pathDestinazione.indexOf("DIPCDS")>=0){
				addColumn(sql,"CDS",true);
				addColumn(sql,"DS_CDS",true);
				addColumn(sql,"UO",true);
				addSQLGroupBy(sql,"CDS",isBaseSQL);
				addSQLGroupBy(sql,"DS_CDS",isBaseSQL&&true);
				addSQLGroupBy(sql,"UO",isBaseSQL);
			}
		}
		
		
			/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne del Progetto.
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
			private void addColumnPROG(SQLBuilder sql, boolean isBaseSQL,String pathDestinazione ){ 
				if (pathDestinazione.indexOf("BASECDSPROG")>=0 || pathDestinazione.indexOf("BASECDSVOCEPROG")>=0 
						|| pathDestinazione.indexOf("DIPCDSPROG")>=0
						|| pathDestinazione.indexOf("VOCEVOCEVOCENATVOCEMOD")>=0
						|| pathDestinazione.indexOf("ENTCDSPROG")>=0 || pathDestinazione.indexOf("ENTCDSVOCEPROG")>=0 ){
					addColumn(sql,"PROGETTO",true);
					addColumn(sql,"DS_PROGETTO",true);
					addSQLGroupBy(sql,"PROGETTO",isBaseSQL);
					addSQLGroupBy(sql,"DS_PROGETTO",isBaseSQL&&true);
				}
			}
			
			/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne Commessa
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
			private void addColumnCOMM(SQLBuilder sql, boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("BASECDSPROGCOMM")>=0 || pathDestinazione.indexOf("BASECDSVOCEPROGCOMM")>=0
						|| pathDestinazione.indexOf("DIPCDSPROGCOMM")>=0
						|| pathDestinazione.indexOf("VOCEVOCEVOCENATVOCEMOD")>=0
						|| pathDestinazione.indexOf("ENTCDSPROGCOMM")>=0 || pathDestinazione.indexOf("ENTCDSVOCEPROGCOMM")>=0){
					addColumn(sql,"COMMESSA",true);
					addColumn(sql,"DS_COMMESSA",true);
					addSQLGroupBy(sql,"COMMESSA",isBaseSQL);
					addSQLGroupBy(sql,"DS_COMMESSA",isBaseSQL&&true);
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
				if (pathDestinazione.indexOf("BASECDSPROGCOMMMOD")>=0 || pathDestinazione.indexOf("BASECDSVOCEPROGCOMMMOD")>=0
						|| pathDestinazione.indexOf("DIPCDSPROGCOMMMOD")>=0
						|| pathDestinazione.indexOf("VOCEVOCEVOCENATVOCEMOD")>=0
						|| pathDestinazione.indexOf("ENTCDSPROGCOMMMOD")>=0 || pathDestinazione.indexOf("ENTCDSVOCEPROGCOMMMOD")>=0){
					addColumn(sql,"MODULO",true);
					addColumn(sql,"DS_MODULO",true);
					addSQLGroupBy(sql,"MODULO",isBaseSQL);
					addSQLGroupBy(sql,"DS_MODULO",isBaseSQL&&true);
				}
			}
			
			/**
			 * Aggiunge nell'SQLBuilder <sql> le colonne CDR
			 *
			 * @param sql l'SQLBuilder da aggiornare
			 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
			 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
			 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
			 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
			 */
			private void addColumnCDR(SQLBuilder sql, boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("BASECDSPROGCOMMMODCDR")>=0||pathDestinazione.indexOf("BASECDSCDR")>=0 ||
					pathDestinazione.indexOf("BASECDSVOCEPROGCOMMMODCDR")>=0||pathDestinazione.indexOf("BASECDSVOCECDR")>=0||
					pathDestinazione.indexOf("DIPCDSPROGCOMMMODCDR")>=0||pathDestinazione.indexOf("DIPCDSCDR")>=0
					||pathDestinazione.indexOf("VOCEVOCEVOCENATVOCEMODVOCECDR")>=0
					||pathDestinazione.indexOf("ENTCDSPROGCOMMMODCDR")>=0||pathDestinazione.indexOf("ENTCDSCDR")>=0 ||
					pathDestinazione.indexOf("ENTCDSVOCEPROGCOMMMODCDR")>=0||pathDestinazione.indexOf("ENTCDSVOCECDR")>=0){
					addColumn(sql,"CDR",true);
					addColumn(sql,"DS_CDR",true);
					addSQLGroupBy(sql,"CDR",isBaseSQL);
					addSQLGroupBy(sql,"DS_CDR",isBaseSQL&&true);
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
				if (pathDestinazione.indexOf("BASECDSPROGCOMMMODCDRGAE")>=0||pathDestinazione.indexOf("BASECDSCDRGAE")>=0 ||
					pathDestinazione.indexOf("BASECDSVOCEPROGCOMMMODCDRGAE")>=0||pathDestinazione.indexOf("BASECDSVOCECDRGAE")>=0||
					pathDestinazione.indexOf("DIPCDSPROGCOMMMODCDRGAE")>=0||pathDestinazione.indexOf("DIPCDSCDRGAE")>=0
					||pathDestinazione.indexOf("VOCEVOCEVOCENATVOCEMODVOCECDRVOCEGAE")>=0){
					addColumn(sql,"LDA",true);
					addColumn(sql,"DS_LDA",true);
					addColumn(sql,"CD_RESPONSABILE_TERZO",true);
					addSQLGroupBy(sql,"LDA",isBaseSQL);
					addSQLGroupBy(sql,"DS_LDA",isBaseSQL&&true);
					addSQLGroupBy(sql,"CD_RESPONSABILE_TERZO",isBaseSQL&&true);
				}
			}
			
			private void addColumnENTGAE(SQLBuilder sql,  boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf("ENTCDSPROGCOMMMODCDRGAE")>=0||pathDestinazione.indexOf("ENTCDSCDRGAE")>=0 ||
					pathDestinazione.indexOf("ENTCDSVOCEPROGCOMMMODCDRGAE")>=0||pathDestinazione.indexOf("ENTCDSVOCECDRGAE")>=0	){
					addColumn(sql,"LDA",true);
					addColumn(sql,"DS_LDA",true);
					addColumn(sql,"CD_RESPONSABILE_TERZO",true);
					addColumn(sql,"CD_NATURA",true);
					addColumn(sql,"DS_NATURA",true);
					addColumn(sql,"TIPO",true);
					addSQLGroupBy(sql,"LDA",isBaseSQL);
					addSQLGroupBy(sql,"DS_LDA",isBaseSQL&&true);
					addSQLGroupBy(sql,"CD_RESPONSABILE_TERZO",isBaseSQL&&true);
					addSQLGroupBy(sql,"CD_NATURA",isBaseSQL);
					addSQLGroupBy(sql,"DS_NATURA",isBaseSQL&&true);
					addSQLGroupBy(sql,"TIPO",isBaseSQL&&true);
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
				if (pathDestinazione.indexOf("BASECDSPROGCOMMMODCDRGAEDET")>=0||pathDestinazione.indexOf("BASECDSCDRGAEDET")>=0||
					pathDestinazione.indexOf("BASECDSVOCE")>=0||
					pathDestinazione.indexOf("DIPCDSPROGCOMMMODCDRGAEDET")>=0||pathDestinazione.indexOf("DIPCDSCDRGAEDET")>=0
					||pathDestinazione.indexOf("ENTCDSPROGCOMMMODCDRGAEDET")>=0||pathDestinazione.indexOf("ENTCDSCDRGAEDET")>=0
					||pathDestinazione.indexOf("ENTCDSVOCE")>=0){
					addColumn(sql,"CD_VOCE",true);
					addColumn(sql,"CD_ELEMENTO_VOCE",true);
					addColumn(sql,"DS_VOCE",true);
					addSQLGroupBy(sql,"CD_VOCE",isBaseSQL);
					addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",isBaseSQL);
					addSQLGroupBy(sql,"DS_VOCE",isBaseSQL&&true);
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
			
			private String getDipartimentoScrivania(UserContext userContext)throws ComponentException{
				try {
					UtenteBulk utente = (UtenteBulk)getHome(userContext, UtenteBulk.class).findByPrimaryKey(new UtenteBulk(CNRUserContext.getUser(userContext)));
					return utente.getCd_dipartimento();
				} catch (PersistencyException e) {
					throw handleException(e);
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
			private SQLBuilder completaSQL(SQLBuilder sql, CompoundFindClause baseClause, CompoundFindClause findClause){
			sql.addClause(baseClause);
				if (findClause==null)
					return sql;
				else {
					sql.addClause(findClause);
					return sql;
				}
			}
			
			private void addBaseColumnsDip(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException {		
				sql.resetColumns();
					sql.addColumn("DIPARTIMENTO");
					sql.addColumn("DS_DIPARTIMENTO");
					sql.addColumn("ESERCIZIO");
					sql.addColumn("ESERCIZIO_RES");
					sql.addColumn("NVL(SUM(STANZ_INI),0)","STANZ_INI");
					sql.addColumn("NVL(SUM(VAR_PIU),0)","VAR_PIU");
					sql.addColumn("NVL(SUM(VAR_MENO),0)","VAR_MENO");
					sql.addColumn("NVL(SUM(ASSESTATO_COMP),0)","ASSESTATO_COMP");
					sql.addColumn("NVL(SUM(OBB_COMP),0)","OBB_COMP");
					sql.addColumn("NVL(SUM(DISP_COMP),0)","DISP_COMP");
					sql.addColumn("NVL(SUM(PAGATO_COMP),0)","PAGATO_COMP");
					sql.addColumn("NVL(SUM(STANZ_RES_IMP),0)","STANZ_RES_IMP");
					sql.addColumn("NVL(SUM(VAR_PIU_STANZ_RES_IMP),0)","VAR_PIU_STANZ_RES_IMP");
					sql.addColumn("NVL(SUM(VAR_MENO_STANZ_RES_IMP),0)","VAR_MENO_STANZ_RES_IMP");
					sql.addColumn("NVL(SUM(ASSESTATO_RES_IMP),0)","ASSESTATO_RES_IMP");
					sql.addColumn("NVL(SUM(OBB_RES_IMP),0)","OBB_RES_IMP");
					sql.addColumn("NVL(SUM(OBB_RES_PRO),0)","OBB_RES_PRO");
					sql.addColumn("NVL(SUM(VAR_PIU_RES_PRO),0)","VAR_PIU_RES_PRO");
					sql.addColumn("NVL(SUM(VAR_MENO_RES_PRO),0)","VAR_MENO_RES_PRO");
					sql.addColumn("NVL(SUM(VINCOLI_RES),0)","VINCOLI_RES");
					sql.addColumn("NVL(SUM(DISP_RES),0)","DISP_RES");
					sql.addColumn("NVL(SUM(PAGATO_RES),0)","PAGATO_RES");
					sql.addColumn("NVL(SUM(OBB_COMP),0)-NVL(SUM(PAGATO_COMP),0)", "RIMASTI_DA_PAGARE_COMP");
					sql.addColumn("NVL(SUM(OBB_RES_IMP),0)+NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)-NVL(SUM(PAGATO_RES),0)", "RIMASTI_DA_PAGARE_RES");
					sql.addColumn("NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)", "ASS_RES");
					addSQLGroupBy(sql,"DIPARTIMENTO",true);
					addSQLGroupBy(sql,"DS_DIPARTIMENTO",true);
					addSQLGroupBy(sql,"ESERCIZIO",true);
					addSQLGroupBy(sql,"ESERCIZIO_RES",true);
					
					
					if (pathDestinazione.indexOf("DIP")>=0){ 
						
						if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDS)>=0) {
							addColumnCDS(sql,true,pathDestinazione);
						}
						if(pathDestinazione.indexOf("DIPCDSPROG")>=0){
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROG)>=0) {
								addColumnPROG(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMM)>=0){
								addColumnCOMM(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMOD)>=0){
								addColumnMOD(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDR)>=0){
								addColumnCDR(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDRGAE)>=0){
								addColumnGAE(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSPROGCOMMMODCDRGAEDET)>=0){
								addColumnVOCE(sql,true,pathDestinazione);
							}
						}
					
						if (pathDestinazione.indexOf("DIPCDSCDR")>=0){ 
							
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDR)>=0) {
								addColumnCDR(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDRGAE)>=0){
								addColumnGAE(sql,true,pathDestinazione);
							}
							if (pathDestinazione.indexOf(ConsDispCompResIstCdrGaeBP.LIV_DIPCDSCDRGAEDET)>=0){
								addColumnVOCE(sql,true,pathDestinazione);
							}
						}
					}
			}
			
			private void addColumnNAT(SQLBuilder sql,  boolean isBaseSQL,String pathDestinazione){ 
				if (pathDestinazione.indexOf(ConsDispCompResVoceNatBP.LIV_VOCE)>=0){
					addColumn(sql,"CD_NATURA",true);
					addSQLGroupBy(sql,"CD_NATURA",isBaseSQL);
				}
			}
			private void addBaseColumnsVoce(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException {
				sql.resetColumns();
				sql.addColumn("CD_ELEMENTO_VOCE");
				sql.addColumn("DS_VOCE");
				sql.addColumn("ESERCIZIO");
				sql.addColumn("ESERCIZIO_RES");
				sql.addColumn("NVL(SUM(STANZ_INI),0)","STANZ_INI");
				sql.addColumn("NVL(SUM(VAR_PIU),0)","VAR_PIU");
				sql.addColumn("NVL(SUM(VAR_MENO),0)","VAR_MENO");
				sql.addColumn("NVL(SUM(ASSESTATO_COMP),0)","ASSESTATO_COMP");
				sql.addColumn("NVL(SUM(OBB_COMP),0)","OBB_COMP");
				sql.addColumn("NVL(SUM(DISP_COMP),0)","DISP_COMP");
				sql.addColumn("NVL(SUM(PAGATO_COMP),0)","PAGATO_COMP");
				sql.addColumn("NVL(SUM(STANZ_RES_IMP),0)","STANZ_RES_IMP");
				sql.addColumn("NVL(SUM(VAR_PIU_STANZ_RES_IMP),0)","VAR_PIU_STANZ_RES_IMP");
				sql.addColumn("NVL(SUM(VAR_MENO_STANZ_RES_IMP),0)","VAR_MENO_STANZ_RES_IMP");
				sql.addColumn("NVL(SUM(ASSESTATO_RES_IMP),0)","ASSESTATO_RES_IMP");
				sql.addColumn("NVL(SUM(OBB_RES_IMP),0)","OBB_RES_IMP");
				sql.addColumn("NVL(SUM(OBB_RES_PRO),0)","OBB_RES_PRO");
				sql.addColumn("NVL(SUM(VAR_PIU_RES_PRO),0)","VAR_PIU_RES_PRO");
				sql.addColumn("NVL(SUM(VAR_MENO_RES_PRO),0)","VAR_MENO_RES_PRO");
				sql.addColumn("NVL(SUM(VINCOLI_RES),0)","VINCOLI_RES");
				sql.addColumn("NVL(SUM(DISP_RES),0)","DISP_RES");
				sql.addColumn("NVL(SUM(PAGATO_RES),0)","PAGATO_RES");
				sql.addColumn("NVL(SUM(OBB_COMP),0)-NVL(SUM(PAGATO_COMP),0)", "RIMASTI_DA_PAGARE_COMP");
				sql.addColumn("NVL(SUM(OBB_RES_IMP),0)+NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)-NVL(SUM(PAGATO_RES),0)", "RIMASTI_DA_PAGARE_RES");
				sql.addColumn("NVL(SUM(OBB_RES_PRO),0)+NVL(SUM(VAR_PIU_RES_PRO),0)-NVL(SUM(VAR_MENO_RES_PRO),0)", "ASS_RES");
				addSQLGroupBy(sql,"CD_ELEMENTO_VOCE",true);
				addSQLGroupBy(sql,"DS_VOCE",true);
				addSQLGroupBy(sql,"ESERCIZIO",true);
				addSQLGroupBy(sql,"ESERCIZIO_RES",true);
				
				
					if (pathDestinazione.indexOf(ConsDispCompResVoceNatBP.LIV_VOCENAT)>=0){	
						addColumnNAT(sql,true,pathDestinazione);
					}
					if (pathDestinazione.indexOf(ConsDispCompResVoceNatBP.LIV_VOCENATMOD)>=0){
						addColumnMOD(sql,true,pathDestinazione);
						addColumnCOMM(sql,true,pathDestinazione);
						addColumnPROG(sql,true,pathDestinazione);
					}
					if (pathDestinazione.indexOf(ConsDispCompResVoceNatBP.LIV_VOCENATMODCDR)>=0){
						addColumnCDR(sql,true,pathDestinazione);
					}
					if (pathDestinazione.indexOf(ConsDispCompResVoceNatBP.LIV_VOCENATMODCDRGAE)>=0){
						addColumnGAE(sql,true,pathDestinazione);
					}
		}		
			
			
			private void addBaseColumnsEntrate(UserContext userContext, SQLBuilder sql,  String pathDestinazione, String livelloDestinazione,boolean isBaseSQL) throws it.cnr.jada.comp.ComponentException {		
				sql.resetColumns();
					sql.addColumn("CDS");
					sql.addColumn("DS_CDS");
					sql.addColumn("UO");
					sql.addColumn("ESERCIZIO");
					sql.addColumn("ESERCIZIO_RES");
					sql.addColumn("NVL(SUM(STANZ_INI),0)","STANZ_INI");
					sql.addColumn("NVL(SUM(VAR_PIU),0)","VAR_PIU");
					sql.addColumn("NVL(SUM(VAR_MENO),0)","VAR_MENO");
					sql.addColumn("NVL(SUM(ASSESTATO_COMP),0)","ASSESTATO_COMP");
					sql.addColumn("NVL(SUM(ACC_COMP),0)","ACC_COMP");
					sql.addColumn("NVL(SUM(DISP_COMP),0)","DISP_COMP");
					sql.addColumn("NVL(SUM(RISCOSSO_COMP),0)","RISCOSSO_COMP");
					sql.addColumn("NVL(SUM(ACC_RES_PRO),0)","ACC_RES_PRO");
					sql.addColumn("NVL(SUM(VAR_PIU_RES_PRO),0)","VAR_PIU_RES_PRO");
					sql.addColumn("NVL(SUM(VAR_MENO_RES_PRO),0)","VAR_MENO_RES_PRO");
					sql.addColumn("NVL(SUM(RISCOSSO_RES),0)","RISCOSSO_RES");
					sql.addColumn("NVL(SUM(ACC_RES_PRO),0)+ NVL(SUM(VAR_PIU_RES_PRO),0) - NVL(SUM(VAR_MENO_RES_PRO),0)", "ACC_RES_ASS");
					sql.addColumn("NVL(SUM(ACC_RES_PRO),0)+ NVL(SUM(VAR_PIU_RES_PRO),0) - NVL(SUM(VAR_MENO_RES_PRO),0) -NVL(SUM(RISCOSSO_RES),0)", "RIMASTI_DA_RISCUOTERE_RES");
					sql.addColumn("NVL(SUM(ACC_COMP),0)-NVL(SUM(RISCOSSO_COMP),0)", "RIMASTI_DA_RISCUOTERE_COMP");
					addSQLGroupBy(sql,"CDS",true);
					addSQLGroupBy(sql,"DS_CDS",true);
					addSQLGroupBy(sql,"UO",true);
					addSQLGroupBy(sql,"ESERCIZIO",true);
					addSQLGroupBy(sql,"ESERCIZIO_RES",true);

					
					if (pathDestinazione.indexOf("ENTCDSPROG")>=0){ 
					
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROG)>=0) {
							addColumnPROG(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMM)>=0){
							addColumnCOMM(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMOD)>=0){
							addColumnMOD(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDR)>=0){
							addColumnCDR(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDRGAE)>=0){
							addColumnENTGAE(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSPROGCOMMMODCDRGAEDET)>=0){
							addColumnVOCE(sql,true,pathDestinazione);
						}
					}
					
					if (pathDestinazione.indexOf("ENTCDSCDR")>=0){ 
						
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDR)>=0) {
							addColumnCDR(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDRGAE)>=0){
							addColumnENTGAE(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstCdrGaeBP.LIV_ENTCDSCDRGAEDET)>=0){
							addColumnVOCE(sql,true,pathDestinazione);
						}
					}
					
					if (pathDestinazione.indexOf("ENTCDSVOCE")>=0){ 
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCE)>=0) {
							addColumnVOCE(sql,true,pathDestinazione);
						}
					}
					
					if (pathDestinazione.indexOf("ENTCDSVOCEPROG")>=0){ 
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROG)>=0){
							addColumnPROG(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMM)>=0){
							addColumnCOMM(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMOD)>=0){
							addColumnMOD(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMODCDR)>=0){
							addColumnCDR(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCEPROGCOMMMODCDRGAE)>=0){
							addColumnENTGAE(sql,true,pathDestinazione);
						}
					}
					
					if (pathDestinazione.indexOf("ENTCDSVOCECDR")>=0){ 
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCECDR)>=0) {
							addColumnCDR(sql,true,pathDestinazione);
						}
						if (pathDestinazione.indexOf(ConsDispCompResEntIstVoceBP.LIV_ENTCDSVOCECDRGAE)>=0){
							addColumnENTGAE(sql,true,pathDestinazione);
						}
					}
				}

}