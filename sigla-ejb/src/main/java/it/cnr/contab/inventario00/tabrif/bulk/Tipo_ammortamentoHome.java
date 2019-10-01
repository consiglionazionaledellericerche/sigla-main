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

package it.cnr.contab.inventario00.tabrif.bulk;


import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class Tipo_ammortamentoHome extends BulkHome {
public Tipo_ammortamentoHome(java.sql.Connection conn) {
	super(Tipo_ammortamentoBulk.class,conn);
}
public Tipo_ammortamentoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_ammortamentoBulk.class,conn,persistentCache);
}
/*
 * Trova i Tipi Ammortamento legati ad una Categoria Gruppo
*/
public java.util.Collection findTipiAmmortamentoFor(UserContext aUC,Categoria_gruppo_inventBulk cat_gruppo)
	throws PersistencyException, IntrospectionException {
	
	if (cat_gruppo == null) 
		return null; 

	it.cnr.jada.persistency.sql.SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("ASS_TIPO_AMM_CAT_GRUP_INV");
	sql.addSQLJoin("TIPO_AMMORTAMENTO.CD_TIPO_AMMORTAMENTO","ASS_TIPO_AMM_CAT_GRUP_INV.CD_TIPO_AMMORTAMENTO");	
	sql.addSQLJoin("TIPO_AMMORTAMENTO.TI_AMMORTAMENTO","ASS_TIPO_AMM_CAT_GRUP_INV.TI_AMMORTAMENTO");
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV.CD_CATEGORIA_GRUPPO", sql.EQUALS, cat_gruppo.getCd_categoria_gruppo());
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV.ESERCIZIO_COMPETENZA", sql.EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC));
	sql.addSQLClause("AND", "ASS_TIPO_AMM_CAT_GRUP_INV.DT_CANCELLAZIONE", sql.ISNULL, null);
	
	return fetchAll(sql);

}
/*
 * Permette di rendere persistenti le associazioni tra il Tipo Ammortamento e le 
 *	Categorie di beni ad esso associato.
*/
public void makePersistentAssTiAmm_CatBeni(it.cnr.jada.UserContext aUC, Tipo_ammortamentoBulk ti_ammort) throws PersistencyException, IntrospectionException {

	LoggableStatement ps = null;
	java.io.StringWriter sql = new java.io.StringWriter();
	java.io.PrintWriter pw = new java.io.PrintWriter(sql);

	SimpleBulkList cat_beni_associati =(SimpleBulkList)ti_ammort.getCatBeni();
	
	String colonne_associa = "CD_TIPO_AMMORTAMENTO,CD_CATEGORIA_GRUPPO,ESERCIZIO_INIZIO,ESERCIZIO_FINE";
	String colonne_utente = "DACR,DUVA,UTCR,UTUV,PG_VER_REC";
	String valori_associa = "?,?,?,?";
	String valori_utente = "sysdate,sysdate,?,?,1";
	try {
		for (java.util.Iterator i = cat_beni_associati.iterator(); i.hasNext();){
			
			Categoria_gruppo_inventBulk	cat_bene = (Categoria_gruppo_inventBulk)i.next();
			pw.write("INSERT INTO " + it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() +  "ASS_TIPO_AMM_CAT_GRUP_INV (");
			pw.write(colonne_associa);
			pw.write(",");
			pw.write(colonne_utente);
			pw.write(") VALUES (");
			pw.write(valori_associa);
			pw.write(",");
			pw.write(valori_utente);
			pw.write(")");
			pw.flush();
			
			ps = new LoggableStatement(getConnection(),sql.toString(),true,this.getClass());
			pw.close();

			ps.setString(1,ti_ammort.getCd_tipo_ammortamento());
			ps.setString(2,cat_bene.getCd_proprio());
			ps.setInt(3,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue());
			ps.setInt(4,it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(aUC).intValue());
			ps.setString(5,ti_ammort.getUtcr());
			ps.setString(6,ti_ammort.getUtuv());

			ps.execute();			
		}
	} catch (java.sql.SQLException e) {
		throw new PersistencyException(e);
	} finally {
		if (ps != null)
			try{ps.close();}catch( java.sql.SQLException e ){};
	}
	
}
}
