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
* Date 01/03/2006
*/
package it.cnr.contab.inventario01.bulk;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import it.cnr.contab.docamm00.docs.bulk.Fattura_passiva_rigaIBulk;
import it.cnr.contab.inventario00.docs.bulk.Ass_inv_bene_fatturaBulk;
import it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.PersistentCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
public class Inventario_beni_apgHome extends BulkHome {
	public Inventario_beni_apgHome(java.sql.Connection conn) {
		super(Inventario_beni_apgBulk.class, conn);
	}
	public Inventario_beni_apgHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Inventario_beni_apgBulk.class, conn, persistentCache);
	}
	public void initializePrimaryKeyForInsert(UserContext usercontext,OggettoBulk oggettobulk)throws PersistencyException, it.cnr.jada.comp.ComponentException {
		try {
		((Inventario_beni_apgBulk)oggettobulk).setPg_riga(
		new Long(
		((Long)findAndLockMax( oggettobulk, "pg_riga", new Long(0) )).longValue()+1));
		} catch(it.cnr.jada.bulk.BusyResourceException e) {
		throw new PersistencyException(e);
		} 
		super.initializePrimaryKeyForInsert(usercontext, oggettobulk);
		}
	public java.sql.Timestamp getMaxDataFor(String transaction)throws PersistencyException{
		  
		Timestamp Max_data_val=null;
		Inventario_beni_apgBulk bulk =new Inventario_beni_apgBulk();
		bulk.setLocal_transaction_id(transaction);
		Max_data_val = (Timestamp) findMax(bulk,"dt_validita_variazione");
		return Max_data_val;
	}
	/*
	 * Esegue la cancellazione, dalla tabella temporanea INVENTARIO_BENI_APG, 
	 *	di tutte le associazione fatte durante la transazione.
	*/
	public void deleteTemp(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk) throws PersistencyException, IntrospectionException {
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
		 List oggetti=fetchAll(sql);
		 for (Iterator i=oggetti.iterator();i.hasNext();){
			 Inventario_beni_apgBulk inv_provvisorio=(Inventario_beni_apgBulk)i.next();
			 delete(inv_provvisorio, userContext);
		 }		
	}
	
	public void deleteTempFor(UserContext userContext, Ass_inv_bene_fatturaBulk associata) throws PersistencyException, IntrospectionException {

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associata.getLocal_transactionID());
		if (associata.getCd_cds_fatt_pass()!= null){
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,associata.getCd_cds_fatt_pass());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,associata.getCd_uo_fatt_pass());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,associata.getEsercizio_fatt_pass());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,associata.getPg_fattura_passiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,associata.getProgressivo_riga_fatt_pass());
		}else{		
			sql.addSQLClause("AND","CD_CDS",sql.EQUALS,associata.getCd_cds_fatt_att());
			sql.addSQLClause("AND","CD_UNITA_ORGANIZZATIVA",sql.EQUALS,associata.getCd_uo_fatt_att());
			sql.addSQLClause("AND","ESERCIZIO",sql.EQUALS,associata.getEsercizio_fatt_att());
			sql.addSQLClause("AND","PG_FATTURA",sql.EQUALS,associata.getPg_fattura_attiva());
			sql.addSQLClause("AND","PROGRESSIVO_RIGA",sql.EQUALS,associata.getProgressivo_riga_fatt_att());               
	  }
	 List oggetti=fetchAll(sql);
	 for (Iterator i=oggetti.iterator();i.hasNext();){
		 Inventario_beni_apgBulk inv_provvisorio=(Inventario_beni_apgBulk)i.next();
		 delete(inv_provvisorio, userContext);
	 }
    }
	/*
	 * Esegue la cancellazione, dalla tabella temporanea INVENTARIO_BENI_APG, 
	 *	di tutte le associazione fatte con le righe di Fattura passate come parametro List.
	*/
	public void deleteTempFor(UserContext userContext, Ass_inv_bene_fatturaBulk associaBulk, java.util.List righe_fattura) throws PersistencyException, IntrospectionException {
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","LOCAL_TRANSACTION_ID",sql.EQUALS,associaBulk.getLocal_transactionID());
		
		
		String progressivi = null;
		for (java.util.Iterator i = righe_fattura.iterator(); i.hasNext();){		
			Fattura_passiva_rigaIBulk riga = (Fattura_passiva_rigaIBulk)i.next();
			if (progressivi == null)
				progressivi = "(";
			else
				progressivi = progressivi + ",";
				
			progressivi = progressivi + riga.getProgressivo_riga();
		}

		progressivi = progressivi + ")";
		sql.addSQLClause("AND", "(PROGRESSIVO_RIGA) IN " + progressivi);
		List oggetti=fetchAll(sql);
		for (Iterator i=oggetti.iterator();i.hasNext();){
			 Inventario_beni_apgBulk inv_provvisorio=(Inventario_beni_apgBulk)i.next();
			 delete(inv_provvisorio, userContext);
		}
	}

}