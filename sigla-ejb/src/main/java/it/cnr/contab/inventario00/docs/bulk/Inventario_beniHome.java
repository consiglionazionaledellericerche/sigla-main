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

package it.cnr.contab.inventario00.docs.bulk;
import java.sql.Timestamp;
import java.util.List;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgBulk;
import it.cnr.contab.inventario01.bulk.Inventario_beni_apgHome;
import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineRigaBulk;
import it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;

public class Inventario_beniHome extends BulkHome {
	
public Inventario_beniHome(java.sql.Connection conn) {
	super(Inventario_beniBulk.class,conn);
}
public Inventario_beniHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Inventario_beniBulk.class,conn,persistentCache);
}
public java.util.Collection findCondizioni(Buono_carico_scaricoBulk buonoCarico, 
		it.cnr.contab.inventario01.bulk.Buono_carico_scarico_dettHome h,
	Buono_carico_scarico_dettBulk clause) throws PersistencyException, IntrospectionException {
	PersistentHome evHome = getHomeCache().getHome(Buono_carico_scaricoBulk.class);
	SQLBuilder sql = evHome.createSQLBuilder();
	sql.addClause("AND","ti_documento",sql.EQUALS, "C");
	return evHome.fetchAll(sql);
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 16.20.47)
 * @return java.util.List
 * @param bene it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public Inventario_beniBulk getBenePrincipaleFor(it.cnr.jada.UserContext userContext,Inventario_beniBulk accessorio) throws IntrospectionException, PersistencyException {
	if (accessorio.getProgressivo().intValue()==0)
		return null;
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,accessorio.getPg_inventario());
	sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,accessorio.getNr_inventario());
	sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS,new Integer(0));
	Inventario_beniBulk principale = null;
	SQLBroker broker = createBroker(sql);
	if (broker.next()){
		principale = (Inventario_beniBulk)fetch(broker);
	}
	getHomeCache().fetchAll(userContext);
	broker.close();
	return principale;
}
/**
 * Insert the method's description here.
 * Creation date: (10/01/2002 16.20.47)
 * @return java.util.List
 * @param bene it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk
 */
public java.util.List getBeniAccessoriFor(Inventario_beniBulk principale) throws IntrospectionException, PersistencyException {
	if (principale.getProgressivo().intValue()==0){
		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,principale.getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS,principale.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO",sql.NOT_EQUALS,new Integer(0));
		return fetchAll(sql);
	}
	return principale.getAccessori();
}
/**
 * Insert the method's description here.
 * Creation date: (04/01/2002 12.50.29)
 * @return java.util.List
 * @param userContext it.cnr.jada.UserContext
 */
public SQLBuilder getListaBeni(it.cnr.jada.UserContext userContext, Buono_carico_scaricoBulk buono, boolean no_accessori, SimpleBulkList beni_da_escludere) throws IntrospectionException, PersistencyException{
	String nr_da_escludere = "";
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buono.getPg_inventario());
	sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO",sql.EQUALS,Boolean.FALSE,java.sql.Types.VARCHAR,0,new CHARToBooleanConverter(),true,false);
	if (no_accessori){
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS, "0");
	}
	if (beni_da_escludere != null && beni_da_escludere.size()>0){
		for (java.util.Iterator i = beni_da_escludere.iterator(); i.hasNext();){
			Buono_carico_scarico_dettBulk bene = (Buono_carico_scarico_dettBulk)i.next();
			if (!nr_da_escludere.equals("")){
				nr_da_escludere = nr_da_escludere + ",";
			}			
			nr_da_escludere = nr_da_escludere + "('" + bene.getBene().getNr_inventario() + "','" + bene.getBene().getProgressivo() + "')";
		}
		sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
	}	
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
    sql.addSQLClause("AND","DT_VALIDITA_VARIAZIONE",sql.LESS_EQUALS,new java.sql.Timestamp(buono.getData_registrazione().getTime()));	
	return sql;
}
/**
 * Insert the method's description here.
 * Creation date: (04/01/2002 12.50.29)
 * @return java.util.List
 * @param userContext it.cnr.jada.UserContext
 */
public SQLBuilder getListaBeni(it.cnr.jada.UserContext userContext, it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario, boolean no_accessori, SimpleBulkList beni_da_escludere) throws IntrospectionException, PersistencyException{
	String nr_prog_da_escludere = "";
	SQLBuilder sql = createSQLBuilder();
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inventario.getPg_inventario());
	sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO",sql.EQUALS,Boolean.FALSE,java.sql.Types.VARCHAR,0,new CHARToBooleanConverter(),true, false);
	if (no_accessori){
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS, "0");
	}
	if (beni_da_escludere != null && beni_da_escludere.size()>0){
		for (java.util.Iterator i = beni_da_escludere.iterator(); i.hasNext();){
			Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
			if (!nr_prog_da_escludere.equals("")){
				nr_prog_da_escludere = nr_prog_da_escludere + ",";
			}			
			nr_prog_da_escludere = nr_prog_da_escludere + "('" + bene.getNr_inventario() + "','" + bene.getProgressivo() + "')";
		}
		sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_prog_da_escludere + ")");
	}
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
	return sql;
}
public SQLBuilder getListaBeniDaScaricare(UserContext userContext,  it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk inventario, boolean no_accessori, SimpleBulkList beni_da_escludere) throws IntrospectionException, PersistencyException{
	SQLBuilder sql = createSQLBuilder();
	String nr_da_escludere = "";
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,inventario.getPg_inventario());
	if (no_accessori){
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS, "0");
	}
	if (beni_da_escludere != null && beni_da_escludere.size()>0){
			for (java.util.Iterator i = beni_da_escludere.iterator(); i.hasNext();){
				Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
				if (!nr_da_escludere.equals("")){
					nr_da_escludere = nr_da_escludere + ",";
				}			
				nr_da_escludere = nr_da_escludere + "('" + bene.getNr_inventario() + "','" + bene.getProgressivo() + "')";
			}
			sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
		}
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
	return sql;
}
/**
 * Insert the method's description here.
 * Creation date: (04/01/2002 12.50.29)
 * @return java.util.List
 * @param userContext it.cnr.jada.UserContext
 */
public SQLBuilder getListaBeniDaScaricare(UserContext userContext, Buono_carico_scaricoBulk buonoS, boolean no_accessori, SimpleBulkList beni_da_escludere) throws IntrospectionException, PersistencyException{

	String nr_da_escludere = "";
	SQLBuilder sql = createSQLBuilder();	
	sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS,buonoS.getPg_inventario());
    sql.addSQLClause("AND","FL_TOTALMENTE_SCARICATO",sql.EQUALS,Boolean.FALSE,java.sql.Types.VARCHAR,0,new CHARToBooleanConverter(),true, false);
    sql.addSQLClause("AND","DT_VALIDITA_VARIAZIONE",sql.LESS_EQUALS,buonoS.getData_registrazione());
	if (no_accessori){
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS, "0");
	}
	if (beni_da_escludere != null && beni_da_escludere.size()>0){
		for (java.util.Iterator i = beni_da_escludere.iterator(); i.hasNext();){
			Inventario_beniBulk bene = (Inventario_beniBulk)i.next();
			if (!nr_da_escludere.equals("")){
				nr_da_escludere = nr_da_escludere + ",";
			}			
			nr_da_escludere = nr_da_escludere + "('" + bene.getNr_inventario() + "','" + bene.getProgressivo() + "')";
		}
		sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
	}
	if (buonoS instanceof Trasferimento_inventarioBulk && ((Trasferimento_inventarioBulk)buonoS).isTrasferimentoIntraInv()){
		nr_da_escludere = "";
		Inventario_beni_apgHome home=(Inventario_beni_apgHome)getHomeCache().getHome(Inventario_beni_apgBulk.class);
		SQLBuilder notExistsQuery=home.createSQLBuilder();
		notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.NR_INVENTARIO_PRINCIPALE IS NOT NULL");
		notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PROGRESSIVO_PRINCIPALE IS NOT NULL");
		notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.PG_INVENTARIO_PRINCIPALE IS NOT NULL");
		notExistsQuery.addSQLClause("AND","INVENTARIO_BENI_APG.LOCAL_TRANSACTION_ID",sql.EQUALS,buonoS.getLocal_transactionID());
		List beni = home.fetchAll(notExistsQuery);
		if (beni != null && beni.size()>0){
			for (java.util.Iterator iteratore = beni.iterator(); iteratore.hasNext();){
				Inventario_beni_apgBulk bene = (Inventario_beni_apgBulk)iteratore.next();
				if (!nr_da_escludere.equals("")){
					nr_da_escludere = nr_da_escludere + ",";
				}			
				nr_da_escludere = nr_da_escludere + "('" + bene.getNr_inventario_principale() + "','" + bene.getProgressivo_principale() + "')";
			}
			sql.addSQLClause("AND", "(NR_INVENTARIO, PROGRESSIVO) NOT IN (" + nr_da_escludere + ")");
		}
	}
	// Aggiunta clausola che visualizzi solo i beni che abbiano 
	//	ESERCIZIO_CARICO_BENE <= Esercizio di scrivania.
	sql.addClause("AND", "esercizio_carico_bene", sql.LESS_EQUALS, CNRUserContext.getEsercizio(userContext));
	return sql;
}	
public Long getMaxNr_Inventario(Long pg_inventario)
	throws PersistencyException {
	Long max = null;
	Inventario_beniBulk bulk =new Inventario_beniBulk();
	bulk.setInventario(new Id_inventarioBulk(pg_inventario));
	bulk.setFl_migrato(false);
	max=(Long)findMax(bulk,"nr_inventario",new Long(0));
	
	return max;
}

public Long getMaxProgressivo_Accessorio(Inventario_beniBulk bene_principale)
	throws PersistencyException{
	Long max = null;
	Inventario_beniBulk bulk =new Inventario_beniBulk();
	bulk.setInventario(bene_principale.getInventario());
	bulk.setNr_inventario(bene_principale.getNr_inventario());
	max=(Long)findMax(bulk,"progressivo",new Long(0));
	return max;
}
public java.util.Collection findUtilizzatori(it.cnr.jada.UserContext userContext,Inventario_beniBulk bene)throws IntrospectionException,PersistencyException
{				
		PersistentHome home = getHomeCache().getHome(Inventario_utilizzatori_laBulk.class );
		
		SQLBuilder sql = home.createSQLBuilder();
		sql.addSQLClause("AND","PG_INVENTARIO",sql.EQUALS, bene.getPg_inventario());
		sql.addSQLClause("AND","NR_INVENTARIO",sql.EQUALS, bene.getNr_inventario());
		sql.addSQLClause("AND","PROGRESSIVO",sql.EQUALS, bene.getProgressivo());
		sql.addOrderBy("NR_INVENTARIO");
		java.util.Collection result = home.fetchAll( sql);
		getHomeCache().fetchAll(userContext);
		return result;
}
public java.sql.Timestamp getMaxDataFor(UserContext userContext, Id_inventarioBulk inventario)throws PersistencyException{
	  
		Timestamp Max_data_val=null;
		Inventario_beniBulk bulk =new Inventario_beniBulk();
		bulk.setEsercizio_carico_bene(it.cnr.contab.utenze00.bp.CNRUserContext.getEsercizio(userContext));
		bulk.setInventario(inventario);
		bulk.setFl_totalmente_scaricato(false);
	  
		Max_data_val = (Timestamp) findMax(bulk,"dt_validita_variazione");
		return Max_data_val;
	}
public java.util.Collection findDettagliBuono(Buono_carico_scaricoBulk buono)throws IntrospectionException,PersistencyException
{				
	
		SQLBuilder sql = createSQLBuilder();
		sql.addTableToHeader("BUONO_CARICO_SCARICO_DETT");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","INVENTARIO_BENI.PG_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.NR_INVENTARIO","INVENTARIO_BENI.NR_INVENTARIO");
		sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PROGRESSIVO","INVENTARIO_BENI.PROGRESSIVO");
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO",sql.EQUALS, buono.getPg_inventario());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.ESERCIZIO",sql.EQUALS, buono.getEsercizio());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S",sql.EQUALS, buono.getPg_buono_c_s());
		sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO",sql.EQUALS, buono.getTi_documento());
		java.util.Collection result = fetchAll( sql);
		return result;
}
	public List findByTransito(Transito_beni_ordiniBulk transito_beni_ordiniBulk) throws PersistencyException {
		SQLBuilder sqlBuilder = createSQLBuilder();
		sqlBuilder.addSQLClause(FindClause.AND, "ID_TRANSITO_BENI_ORDINI", SQLBuilder.EQUALS, transito_beni_ordiniBulk.getId());
		return fetchAll(sqlBuilder);
	}
	public boolean IsEtichettaBeneAlreadyExist(Buono_carico_scarico_dettBulk dett) throws java.sql.SQLException{

		SQLBuilder sql = createSQLBuilder();
		sql.addSQLClause("AND","INVENTARIO_BENI.ETICHETTA",sql.EQUALS,dett.getEtichetta());

		return sql.executeExistsQuery(getConnection());
	}
}
