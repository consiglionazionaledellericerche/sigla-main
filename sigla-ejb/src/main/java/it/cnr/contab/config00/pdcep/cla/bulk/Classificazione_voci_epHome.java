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
* Created by Generator 1.0
* Date 29/08/2005
*/
package it.cnr.contab.config00.pdcep.cla.bulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.PersistentHome;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Classificazione_voci_epHome extends BulkHome {
	public final static int LIVELLO_PRIMO = 1 ;
	public final static int LIVELLO_SECONDO = 2 ;
	public final static int LIVELLO_TERZO = 3 ;
	public final static int LIVELLO_QUARTO = 4 ;
	public final static int LIVELLO_QUINTO = 5 ;
	public final static int LIVELLO_SESTO = 6 ;
	public final static int LIVELLO_SETTIMO = 7;
	public final static int LIVELLO_OTTAVO = 8;

	public final static int LIVELLO_MAX = LIVELLO_OTTAVO ;
	public final static int LIVELLO_MIN = LIVELLO_PRIMO ;

	protected Classificazione_voci_epHome(Class clazz,java.sql.Connection connection) {
		super(clazz,connection);
	}
	protected Classificazione_voci_epHome(Class clazz,java.sql.Connection connection,PersistentCache persistentCache) {
		super(clazz,connection,persistentCache);
	}

	public Classificazione_voci_epHome(java.sql.Connection conn) {
		super(Classificazione_voci_epBulk.class, conn);
	}
	public Classificazione_voci_epHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Classificazione_voci_epBulk.class, conn, persistentCache);
	}
	public SQLBuilder selectByClause(UserContext usercontext, CompoundFindClause compoundfindclause)
		throws PersistencyException
	{ 
		SQLBuilder sql = super.selectByClause(usercontext, compoundfindclause);
		sql.addSQLClause("AND","esercizio",SQLBuilder.EQUALS,CNRUserContext.getEsercizio(usercontext));
		return sql;
	}	

	/**
	 * Inizializza il modello per l'inserimento impostando il progressivo ottenuto come max progressivo + 1 o 1
	 * nel caso non ci siano classificazioni sul DB
	 *
	 * @param cla classificazione da inserire
	 */
	public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk cla) throws PersistencyException {
		try {
			((Classificazione_voci_epBulk)cla).setId_classificazione(
				new Integer(
					((Integer)findAndLockMax( cla, "id_classificazione", new Integer(0) )).intValue()+1
				)
			);
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
			throw new PersistencyException(e);
		}
	}
	/**
	 * Recupera solo le classificazioni associate direttamente a quella in uso.
	 *
	 * @param testata La classificazione in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>OggettoBulk</code>
	 */
	public java.util.Collection findClassVociAssociate(OggettoBulk testata) throws IntrospectionException, PersistencyException {
		return findClassVociAssociate(testata, new CompoundFindClause());
	}	
	/**
	 * Recupera solo le classificazioni associate direttamente a quella in uso.
	 *
	 * @param testata La classificazione in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>OggettoBulk</code>
	 */
	public java.util.Collection findClassVociAssociate(OggettoBulk testata, CompoundFindClause compoundfindclause) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome;
		if (testata instanceof Classificazione_voci_ep_eco_liv8Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv8Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv7Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv8Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv6Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv7Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv5Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv6Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv4Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv5Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv3Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv4Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv2Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv3Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_eco_liv1Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_eco_liv2Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv8Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv8Bulk.class);		
		else if (testata instanceof Classificazione_voci_ep_pat_liv7Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv8Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv6Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv7Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv5Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv6Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv4Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv5Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv3Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv4Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv2Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv3Bulk.class);
		else if (testata instanceof Classificazione_voci_ep_pat_liv1Bulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_pat_liv2Bulk.class);
		else
			dettHome = getHomeCache().getHome(Classificazione_voci_epBulk.class);

		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addClause(compoundfindclause);
		sql.addSQLClause("AND","ID_CLASS_PADRE",sql.EQUALS,((Classificazione_voci_epBulk)testata).getId_classificazione());
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera tutte le classificazioni associate, direttamente o indirettamente tramite le classificazioni figlie, 
	 * a quella in uso.
	 *
	 * @param testata La classificazione in uso.
	 *
	 * @return java.util.Collection Collezione di oggetti <code>OggettoBulk</code>
	 */
	public java.util.Collection findAllClassVociAssociate(OggettoBulk testata) throws IntrospectionException, PersistencyException {
		PersistentHome dettHome = null; 
		if (testata instanceof Classificazione_voci_ep_ecoBulk)
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_ecoBulk.class);
		else
			dettHome = getHomeCache().getHome(Classificazione_voci_ep_patBulk.class);

		SQLBuilder sql = dettHome.createSQLBuilder();
		sql.addSQLClause("AND","CD_LIVELLO"+((Classificazione_voci_epBulk)testata).getLivelloMax().intValue(),sql.EQUALS,((Classificazione_voci_epBulk)testata).getCd_livello(((Classificazione_voci_epBulk)testata).getLivelloMax().intValue()));
		if ((((Classificazione_voci_epBulk)testata).getLivelloMax().intValue()+1)!=Classificazione_voci_epHome.LIVELLO_MAX)
			sql.addSQLClause("AND","CD_LIVELLO"+(((Classificazione_voci_epBulk)testata).getLivelloMax().intValue()+1),sql.ISNOTNULL, null);
		return dettHome.fetchAll(sql);
	}	
	/**
	 * Recupera i figli dell'oggetto bulk
	 * Creation date: (28/11/2001 10.57.42)
	 * @return it.cnr.jada.persistency.sql.SQLBuilder
	 * @param bulk it.cnr.contab.inventario00.tabrif.bulk.Classificazione_vociBulk
	 */
	public SQLBuilder selectChildrenFor(it.cnr.jada.UserContext aUC, Classificazione_voci_epBulk cla) throws it.cnr.jada.comp.ComponentException{

		SQLBuilder sql= createSQLBuilder();

		sql.addSQLClause("AND","ID_CLASS_PADRE",sql.EQUALS,cla.getId_classificazione());
		return sql;
	}
	public Classificazione_voci_epBulk getParent(Classificazione_voci_epBulk bulk) throws PersistencyException, IntrospectionException{
		if (bulk == null)
			return null;
    	
		PersistentHome dettHome = getHomeCache().getHome(bulk.getClass().getSuperclass());
		SQLBuilder sql = dettHome.createSQLBuilder();
		if (bulk.getId_class_padre()==null)
			sql.addSQLClause("AND","ID_CLASSIFICAZIONE",sql.ISNULL,null);
		else
			sql.addSQLClause("AND","ID_CLASSIFICAZIONE",sql.EQUALS,bulk.getId_class_padre());

		java.util.Collection coll = this.fetchAll(sql);
		if (coll.size() != 1)
			return null;
    
		return (Classificazione_voci_epBulk)coll.iterator().next();
	}
}