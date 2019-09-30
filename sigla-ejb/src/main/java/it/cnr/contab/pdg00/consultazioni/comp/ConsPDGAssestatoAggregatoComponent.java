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

/*
 * Created on Nov 10, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package it.cnr.contab.pdg00.consultazioni.comp;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.pdg00.consultazioni.bp.ConsPDGAssestatoAggregatoSpeBP;
import it.cnr.contab.pdg00.consultazioni.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.BusinessProcessException;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.RemoteIterator;

/**
 * @author rpagano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsPDGAssestatoAggregatoComponent extends CRUDComponent {
	public RemoteIterator findConsultazioneEtr(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, "ETR");
	}

	public RemoteIterator findConsultazioneSpe(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
		return findConsultazioneDettaglio(userContext, pathDestinazione, livelloDestinazione, baseClause, findClause, "SPE");
	}

	private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione, String livelloDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause, String tipoCons) throws it.cnr.jada.comp.ComponentException {
		BulkHome home = getHome(userContext, V_cons_pdg_assestato_aggregatoBulk.class, tipoCons.concat(pathDestinazione));
		SQLBuilder sql = home.createSQLBuilder();
		SQLBuilder sqlEsterna = home.createSQLBuilder();
		String tabAlias = sql.getColumnMap().getTableName();
		addBaseColumns(sql, sqlEsterna, tabAlias, pathDestinazione, livelloDestinazione);
		return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_pdg_assestato_aggregatoBulk.class,null);
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
	private void addBaseColumns(SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione, String livelloDestinazione) throws it.cnr.jada.comp.ComponentException {
		sql.resetColumns();
		sqlEsterna.resetColumns();

		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DIP)>=0) {
			addColumnDIP(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DIP),true);
			addColumnDIP(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DIP),false);
		}
		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV1)>=0){
			addColumnLIV1(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV1),true);
			addColumnLIV1(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV1),false);
		}
		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV2)>=0){
			addColumnLIV2(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV2),true);
			addColumnLIV2(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_LIV2),false);
		}
		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_CDS)>=0){
			addColumnCDS(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_CDS),true);
			addColumnCDS(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_CDS),false);
		}
		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_UO)>=0){
			addColumnUO(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_UO),true);
			addColumnUO(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_UO),false);
		}
		if (pathDestinazione.indexOf(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DET)>=0){
			addColumnDET(sql,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DET),true);
			addColumnDET(sqlEsterna,tabAlias,livelloDestinazione.equals(ConsPDGAssestatoAggregatoSpeBP.LIVELLO_DET),false);
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
			sql.addOrderBy(tabAlias.toLowerCase().concat(".peso_dip"));
			return sql;
		}
		else
		{
			addColumnIMPORTI(sql,tabAlias,true);		
			addColumnIMPORTI(sqlEsterna,tabAlias,false);		
			sqlEsterna.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
			sqlEsterna.addClause(findClause);
			sqlEsterna.addOrderBy(tabAlias.toLowerCase().concat(".peso_dip"));
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
		if (isSommatoria) 
		{
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("INI),0)")), "INI");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_PIU),0)")), "VAR_PIU");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("VAR_MENO),0)")), "VAR_MENO");
			sql.addColumn("NVL(SUM(".concat(tabAlias.concat("INI),0)+NVL(SUM(".concat(tabAlias.concat("VAR_PIU),0)+NVL(SUM(".concat(tabAlias.concat("VAR_MENO),0)")))))), "ASSESTATO");
		}
		else
		{
			sql.addColumn("NVL(INI,0)", "INI");
			sql.addColumn("NVL(VAR_PIU,0)", "VAR_PIU");
			sql.addColumn("NVL(VAR_MENO,0)", "VAR_MENO");
			sql.addColumn("NVL(ASSESTATO,0)", "ASSESTATO");
		}
	}

	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne del Dipartimento.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
	 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
	 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
	 */
	private void addColumnDIP(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
		addColumn(sql,tabAlias.concat("PESO_DIP"),true);
		addColumn(sql,tabAlias.concat("DIP"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("peso_dip"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("dip"),isBaseSQL);

		addColumn(sql,tabAlias.concat("DS_DIPARTIMENTO"),addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_dipartimento"),isBaseSQL&&addDescrizione);
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
	private void addColumnCDS(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("CDS"),true);
		addColumn(sql,tabAlias.concat("DES_CDS"),addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cds"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("des_cds"),isBaseSQL&&addDescrizione);
	}
	/**
	 * Aggiunge nell'SQLBuilder <sql> le colonne dell'Unità Organizzativa.
	 *
	 * @param sql l'SQLBuilder da aggiornare
	 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
	 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
	 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
	 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
	 */
	private void addColumnUO(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("UO"),true);
		addColumn(sql,tabAlias.concat("DES_UO"),addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("uo"),isBaseSQL);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("des_uo"),isBaseSQL&&addDescrizione);
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
	private void addColumnLIV1(SQLBuilder sql, String tabAlias, boolean addDescrizione, boolean isBaseSQL){ 
		tabAlias = getAlias(tabAlias);
		addColumn(sql,tabAlias.concat("CD_LIVELLO1"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_livello1"),isBaseSQL);
		if (isBaseSQL && addDescrizione) {
			sql.addTableToHeader("CLASSIFICAZIONE_VOCI");
			addColumn(sql,"CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE",true);
			addSQLGroupBy(sql,"classificazione_voci.ds_classificazione",true);

			sql.addSQLJoin(tabAlias.concat("ESERCIZIO"),"CLASSIFICAZIONE_VOCI.ESERCIZIO");
			if (sql.getColumnMap().getTableName().equals("V_CONS_PDG_ETR_ASSESTATO"))
				sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_ENTRATE);
			else
				sql.addSQLClause("AND","CLASSIFICAZIONE_VOCI.TI_GESTIONE",sql.EQUALS,Elemento_voceHome.GESTIONE_SPESE);
			
			sql.addSQLJoin(tabAlias.concat("CD_LIVELLO1"),"CLASSIFICAZIONE_VOCI.CD_LIVELLO1");
			sql.addSQLClause("AND", "CLASSIFICAZIONE_VOCI.CD_LIVELLO2",sql.ISNULL,null);
		}
		else
			addColumn(sql,"DS_CLASSIFICAZIONE",addDescrizione);
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
			if (sql.getColumnMap().getTableName().equals("V_CONS_PDG_ETR_ASSESTATO"))
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
		addColumn(sql,tabAlias.concat("CDR"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cdr"),isBaseSQL);
		addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),isBaseSQL);
		addColumn(sql,tabAlias.concat("CD_CLASSIFICAZIONE"),true);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_classificazione"),isBaseSQL);
		addColumn(sql,tabAlias.concat("DS_CLASSIFICAZIONE"),addDescrizione);
		addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_classificazione"),isBaseSQL&&addDescrizione);
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
