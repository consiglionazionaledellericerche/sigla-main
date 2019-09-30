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
 * Created by Aurelio's BulkGenerator 1.0
 * Date 26/07/2007
 */
package it.cnr.contab.incarichi00.xmlfp.bulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elenco_fpBulk;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.SQLBuilder;

import java.sql.Connection;
import java.util.List;
public class Incarichi_comunicati_fpHome extends BulkHome {
	public Incarichi_comunicati_fpHome(Connection conn) {
		super(Incarichi_comunicati_fpBulk.class, conn);
	}
	public Incarichi_comunicati_fpHome(Connection conn, PersistentCache persistentCache) {
		super(Incarichi_comunicati_fpBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext, OggettoBulk bulk) throws PersistencyException {
		try {
			if (((Incarichi_comunicati_fpBulk)bulk).getPg_record()==null) {
				((Incarichi_comunicati_fpBulk)bulk).setPg_record(
						new Long(
						((Long)findAndLockMax( bulk, "pg_record", new Long(0) )).longValue()+1
					)
				);
			}
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Metodo per cercare il record aggiornato inviato alla Funzione Pubblica
	 *
	 * @param Incarico comunicato <code>Incarichi_comunicati_fpBulk</code>
	 *
	 * @return L'Incarico comunicato principale
	 */
	public Incarichi_comunicati_fpBulk findIncaricoComunicatoAggFP( Incarichi_comunicati_fpBulk incComunicato ) throws IntrospectionException,PersistencyException 
	{
		try{
			SQLBuilder sql = super.createSQLBuilder();
	
			if (incComunicato.getEsercizio_repertorio()!=null && incComunicato.getPg_repertorio()!=null){
				sql.addClause(FindClause.AND, "esercizio_repertorio", SQLBuilder.EQUALS, incComunicato.getEsercizio_repertorio());
				sql.addClause(FindClause.AND, "pg_repertorio", SQLBuilder.EQUALS, incComunicato.getPg_repertorio());
			}
	
			sql.addClause(FindClause.AND, "tipo_record", SQLBuilder.EQUALS, Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
			sql.addClause(FindClause.AND, "pg_record", SQLBuilder.EQUALS, Incarichi_comunicati_fpBulk.PG_RECORD_PRINCIPALE);
			
			if (incComunicato.getId_incarico()!=null)
				sql.addClause(FindClause.AND, "id_incarico", SQLBuilder.EQUALS, incComunicato.getId_incarico());
	
			List list = fetchAll(sql);	
			
			if (!list.isEmpty() && list.size()==1)
				return (Incarichi_comunicati_fpBulk)list.get(0);
			return null;
		}
		catch( Exception e )
		{
			throw new PersistencyException( e );
		}
	}	
	
	/**
	 * Metodo per cercare i record comunicati alla FP ma successivamente eliminati
	 *
	 * @param esercizio <code>Integer</code>
	 * @param semestre <code>Integer</code>
	 *
	 * @return La lista degli Incarichi comunicati nell'esercizio e nel semestre richiesti ma successivamente annullati
	 */
	public List<Incarichi_comunicati_fpBulk> findIncarichiComunicatiEliminatiFP(Integer esercizio, Integer semestre) throws IntrospectionException,PersistencyException 
	{
		try{
			SQLBuilder sql = super.createSQLBuilder();
	
			sql.addClause(FindClause.AND, "anno_riferimento", SQLBuilder.EQUALS, esercizio);
			sql.addClause(FindClause.AND, "semestre_riferimento", SQLBuilder.EQUALS, semestre);
			sql.addClause(FindClause.AND, "tipo_record", SQLBuilder.EQUALS, Incarichi_comunicati_fpBulk.TIPO_RECORD_AGGIORNATO);
			sql.addClause(FindClause.AND, "pg_record", SQLBuilder.EQUALS, Incarichi_comunicati_fpBulk.PG_RECORD_PRINCIPALE);
			sql.addClause(FindClause.AND, "id_incarico", SQLBuilder.ISNOTNULL, null);
			
			
			SQLBuilder sqlNotExists = getHomeCache().getHome(V_incarichi_elenco_fpBulk.class).createSQLBuilder();
			sqlNotExists.addSQLJoin("V_INCARICHI_ELENCO_FP.ESERCIZIO", "INCARICHI_COMUNICATI_FP.ESERCIZIO_REPERTORIO");
			sqlNotExists.addSQLJoin("V_INCARICHI_ELENCO_FP.PG_REPERTORIO", "INCARICHI_COMUNICATI_FP.PG_REPERTORIO");

			sql.addSQLNotExistsClause(FindClause.AND, sqlNotExists);
			
			return fetchAll(sql);	
		}
		catch( Exception e )
		{
			throw new PersistencyException( e );
		}
	}	
	/**
	 * Metodo per cercare i record di dettaglio collegati al record indicato
	 *
	 * @param esercizio <code>Integer</code>
	 * @param semestre <code>Integer</code>
	 *
	 * @return La lista dei pagamenti associati all'Incarichi comunicati 
	 */
	public List<Incarichi_comunicati_fp_detBulk> findIncarichiComunicatiFpDet(Incarichi_comunicati_fpBulk incaricoComunicato) throws IntrospectionException,PersistencyException 
	{
		try{
			BulkHome home = (BulkHome)getHomeCache().getHome(Incarichi_comunicati_fp_detBulk.class);
			SQLBuilder sql = home.createSQLBuilder();
	
			sql.addClause(FindClause.AND, "esercizio_repertorio", SQLBuilder.EQUALS, incaricoComunicato.getEsercizio_repertorio());
			sql.addClause(FindClause.AND, "pg_repertorio", SQLBuilder.EQUALS, incaricoComunicato.getPg_repertorio());
			sql.addClause(FindClause.AND, "tipo_record", SQLBuilder.EQUALS, incaricoComunicato.getTipo_record());
			sql.addClause(FindClause.AND, "pg_record", SQLBuilder.EQUALS, incaricoComunicato.getPg_record());

			return home.fetchAll(sql);	
		}
		catch( Exception e )
		{
			throw new PersistencyException( e );
		}
	}	
}
