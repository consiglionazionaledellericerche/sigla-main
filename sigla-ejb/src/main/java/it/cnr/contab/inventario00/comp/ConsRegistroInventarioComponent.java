
package it.cnr.contab.inventario00.comp;

import java.rmi.RemoteException;
import java.util.Enumeration;

import javax.ejb.EJBException;

import it.cnr.contab.inventario00.bp.ConsRegistroInventarioBP;
import it.cnr.contab.inventario00.consultazioni.bulk.V_cons_registro_inventarioBulk;
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

/**
 * @author rpucciarelli
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsRegistroInventarioComponent extends CRUDComponent {	
		public RemoteIterator findConsultazione(UserContext userContext, String pathDestinazione, CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
			return findConsultazioneDettaglio(userContext, pathDestinazione, baseClause, findClause);
		}

		private RemoteIterator findConsultazioneDettaglio(UserContext userContext, String pathDestinazione,CompoundFindClause baseClause, CompoundFindClause findClause) throws it.cnr.jada.comp.ComponentException {
				BulkHome home = getHome(userContext,V_cons_registro_inventarioBulk.class, pathDestinazione);
				SQLBuilder sql = home.createSQLBuilder();
				SQLBuilder sqlEsterna = home.createSQLBuilder();
				String tabAlias = sql.getColumnMap().getTableName();
				addBaseColumns(userContext,sql, sqlEsterna, tabAlias, pathDestinazione);
				return iterator(userContext,completaSQL(sql,sqlEsterna,tabAlias,baseClause,findClause),V_cons_registro_inventarioBulk.class,null);
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
		private void addBaseColumns(UserContext userContext,SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, String pathDestinazione) throws it.cnr.jada.comp.ComponentException {
			sql.resetColumns();
			sqlEsterna.resetColumns();
			if (pathDestinazione.indexOf(ConsRegistroInventarioBP.VALORI)>=0) {
				addColumnValori(sql,tabAlias,true);
				addColumnValori(sqlEsterna,tabAlias,true);
			}
			if (pathDestinazione.indexOf(ConsRegistroInventarioBP.QUOTE)>=0) {
				addColumnQuote(sql,tabAlias,true);
				addColumnQuote(sqlEsterna,tabAlias,true);
			}
		}			
		/**
		 * Individua e completa l'SQLBuilder da utilizzare:
		 * 1) è stata effettuata una ricerca mirata (<findClause> != null)
		 * 	  la select finale è costruita come interrogazione di una view costruita sulla select principale <baseClause>
		 * 2) non è stata fatta una ricerca mirata
		 * 	  la select finale è uguale alla select principale
		 *
		 * @param sql la select principale contenente le GroupBy
		 * @param sqlEsterna la select esterna necessaria per interrogare la select principale come view
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param baseClause la condizione principale non proveniente dall'utente
		 * @param findClause la condizione secondaria proveniente dall'utente tramite la mappa di ricerca guidata
		 * @return SQLBuilder la query da effettuare
		 */
		private SQLBuilder completaSQL(SQLBuilder sql, SQLBuilder sqlEsterna, String tabAlias, CompoundFindClause baseClause, CompoundFindClause findClause){ 
			sql.addClause(baseClause);	
			if (findClause == null) 
				return sql;
			else
			{
				sqlEsterna.setFromClause(new StringBuffer("(".concat(sql.toString().concat(") ".concat(tabAlias)))));
				sqlEsterna.addClause(findClause);
				return sqlEsterna;
			}
		}
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne delle quote.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnQuote(SQLBuilder sql, String tabAlias, boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			addColumn(sql,tabAlias.concat("CD_CDS"),true);
			addColumn(sql,tabAlias.concat("CD_UNITA_ORGANIZZATIVA"),true);
			addColumn(sql,tabAlias.concat("CATEGORIA"),true);
			addColumn(sql,tabAlias.concat("CD_CATEGORIA_GRUPPO"),true);
			addColumn(sql,tabAlias.concat("DS_CATEGORIA_GRUPPO"),true);
			addColumn(sql,tabAlias.concat("ESERCIZIO_CARICO_BENE"),true);
			addColumn(sql,tabAlias.concat("ETICHETTA"),true);
			addColumn(sql,tabAlias.concat("PG_INVENTARIO"),true);
			addColumn(sql,tabAlias.concat("NR_INVENTARIO"),true);
			addColumn(sql,tabAlias.concat("PROGRESSIVO"),true);
			addColumn(sql,tabAlias.concat("DS_BENE"),true);
			addColumn(sql,tabAlias.concat("ESERCIZIO_AMM"),true);
			addColumn(sql,tabAlias.concat("IM_MOVIMENTO_AMMORT"),true);
			addColumn(sql,tabAlias.concat("PERC_AMMORTAMENTO"),true);
			addColumn(sql,tabAlias.concat("PERC_PRIMO_ANNO"),true);
			addColumn(sql,tabAlias.concat("PERC_SUCCESSIVI"),true);
			addColumn(sql,tabAlias.concat("CD_UTILIZZATORE_CDR"),true);
			addColumn(sql,tabAlias.concat("PERCENTUALE_UTILIZZO_CDR"),true);
			addColumn(sql,tabAlias.concat("CD_LINEA_ATTIVITA"),true);
			addColumn(sql,tabAlias.concat("PERCENTUALE_UTILIZZO_LA"),true);
			
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_unita_organizzativa"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("categoria"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_categoria_gruppo"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_categoria_gruppo"),isBaseSQL);		
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio_carico_bene"),isBaseSQL);	
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("etichetta"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_inventario"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("nr_inventario"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("progressivo"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_bene"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio_amm"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("im_movimento_ammort"),isBaseSQL);			
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("perc_ammortamento"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("perc_primo_anno"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("perc_successivi"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_utilizzatore_cdr"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("percentuale_utilizzo_cdr"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_linea_attivita"),isBaseSQL);
			addSQLGroupBy(sql,tabAlias.toLowerCase().concat("percentuale_utilizzo_la"),isBaseSQL);
		}
			
		/**
		 * Aggiunge nell'SQLBuilder <sql> le colonne dei VALORI.
		 *
		 * @param sql l'SQLBuilder da aggiornare
		 * @param tabAlias l'alias della tabella da aggiungere alle colonne interrogate
		 * @param addDescrizione se TRUE aggiunge anche la colonna della Descrizione
		 * @param isBaseSQL indica se il parametro sql indicato è l'SQLBuilder principale
		 * 		  (necessario perchè solo per l'SQLBuilder principale occorre aggiungere i GroupBy) 
		 */
		private void addColumnValori(SQLBuilder sql, String tabAlias,boolean isBaseSQL){ 
			tabAlias = getAlias(tabAlias);
			
				addColumn(sql,tabAlias.concat("CD_CDS"),true);
				addColumn(sql,tabAlias.concat("CD_UNITA_ORGANIZZATIVA"),true);
				addColumn(sql,tabAlias.concat("CATEGORIA"),true);
				addColumn(sql,tabAlias.concat("CD_CATEGORIA_GRUPPO"),true);
				addColumn(sql,tabAlias.concat("DS_CATEGORIA_GRUPPO"),true);
				addColumn(sql,tabAlias.concat("ESERCIZIO_CARICO_BENE"),true);
				addColumn(sql,tabAlias.concat("ETICHETTA"),true);
			    addColumn(sql,tabAlias.concat("PG_INVENTARIO"),true);
				addColumn(sql,tabAlias.concat("NR_INVENTARIO"),true);
				addColumn(sql,tabAlias.concat("PROGRESSIVO"),true);
				addColumn(sql,tabAlias.concat("DS_BENE"),true);
				addColumn(sql,tabAlias.concat("DS_UBICAZIONE_BENE"),true);
				addColumn(sql,tabAlias.concat("CD_ASSEGNATARIO"),true);
				addColumn(sql,tabAlias.concat("DENOMINAZIONE_SEDE"),true);
				addColumn(sql,tabAlias.concat("DATA_REGISTRAZIONE"),true);
				addColumn(sql,tabAlias.concat("TI_DOCUMENTO"),true);
				addColumn(sql,tabAlias.concat("ESERCIZIO"),true);
				addColumn(sql,tabAlias.concat("PG_BUONO_C_S"),true);
				addColumn(sql,tabAlias.concat("DS_TIPO_CARICO_SCARICO"),true);
				addColumn(sql,tabAlias.concat("VALORE_INIZIALE"),true);
				addColumn(sql,tabAlias.concat("VARIAZIONE_PIU"),true);
				addColumn(sql,tabAlias.concat("VARIAZIONE_MENO"),true);
				addColumn(sql,tabAlias.concat("VALORE_AMMORTIZZATO"),true);
				addColumn(sql,tabAlias.concat("IMPONIBILE_AMMORTAMENTO"),true);
				
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_cds"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_unita_organizzativa"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("categoria"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_categoria_gruppo"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_categoria_gruppo"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio_carico_bene"),isBaseSQL);	
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("etichetta"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_inventario"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("nr_inventario"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("progressivo"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_bene"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_ubicazione_bene"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("cd_assegnatario"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("denominazione_sede"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("data_registrazione"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ti_documento"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("esercizio"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("pg_buono_c_s"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("ds_tipo_carico_scarico"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("valore_iniziale"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("variazione_piu"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("variazione_meno"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("valore_ammortizzato"),isBaseSQL);
				addSQLGroupBy(sql,tabAlias.toLowerCase().concat("imponibile_ammortamento"),isBaseSQL);
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