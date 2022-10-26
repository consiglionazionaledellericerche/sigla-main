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
* Date 19/01/2006
*/
package it.cnr.contab.inventario01.bulk;

import java.sql.Timestamp;
import java.util.Collection;

import it.cnr.contab.docamm00.docs.bulk.Documento_genericoBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_attivaBulk;
import it.cnr.contab.docamm00.docs.bulk.Fattura_passivaBulk;
import it.cnr.contab.inventario01.ejb.NumerazioneTempBuonoComponentSession;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sBulk;
import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sHome;
import it.cnr.contab.inventario00.tabrif.bulk.Id_inventarioBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoBulk;
import it.cnr.contab.inventario00.tabrif.bulk.Tipo_carico_scaricoHome;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.LoggableStatement;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;
public class Buono_carico_scaricoHome extends BulkHome {
	public Buono_carico_scaricoHome(java.sql.Connection conn) {
		super(Buono_carico_scaricoBulk.class, conn);
	}
	public Buono_carico_scaricoHome(java.sql.Connection conn, PersistentCache persistentCache) {
		super(Buono_carico_scaricoBulk.class, conn, persistentCache);
	}
  public Timestamp GetMaxDataRegistrazione(Id_inventarioBulk inventario,Integer esercizio )throws PersistencyException {
	  Timestamp Max_data_reg=null;
	  Buono_carico_scaricoBulk bulk =new Buono_carico_scaricoBulk();
	  bulk.setEsercizio(esercizio);
	  bulk.setInventario(inventario);
	  Max_data_reg = (Timestamp) findMax(bulk,"data_registrazione");
      return Max_data_reg;
  }
  public Timestamp getData_di_Scarico(Buono_carico_scaricoBulk buonoS) throws PersistencyException {
	  Timestamp Max_data_reg=null;
	  Buono_carico_scaricoBulk bulk =new Buono_carico_scaricoBulk();
	  bulk.setEsercizio(buonoS.getEsercizio());
	  bulk.setInventario(buonoS.getInventario());
	  bulk.setTi_documento(buonoS.SCARICO);
	  Max_data_reg = (Timestamp) findMax(bulk,"data_registrazione");
	  return Max_data_reg;
	}
 
	public Collection findTipoMovimenti(Buono_carico_scaricoBulk buonoCS, Tipo_carico_scaricoHome h, Tipo_carico_scaricoBulk clause) throws PersistencyException, IntrospectionException {

			return h.findTipoMovimenti(buonoCS);
	}
	
public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws PersistencyException, ComponentException 
{
			try
			{
				
				Buono_carico_scaricoBulk buono = (Buono_carico_scaricoBulk) bulk;
				if (buono.getPg_buono_c_s() == null ){
					Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache().getHome( Numeratore_buono_c_sBulk.class );
					Long pg = (!userContext.isTransactional()) ?
							numHome.getNextPg(userContext,
									buono.getEsercizio(),
									buono.getPg_inventario(),
									buono.getTi_documento(),
									userContext.getUser()):
								((NumerazioneTempBuonoComponentSession)EJBCommonServices.createEJB(
										"CNRINVENTARIO01_EJB_NumerazioneTempBuonoComponentSession",
										NumerazioneTempBuonoComponentSession.class)).getNextTempPG(userContext,buono);
					buono.setPg_buono_c_s(pg);
				}
			} catch ( ApplicationException e ) {
				throw new ComponentException( e );
			} catch ( Throwable e ) {
				throw new PersistencyException( e );
			}
		}
/*
 * Permette, una volta fatte tutte le operazioni di Fattura e di Inventario,
 *	di rendere persistente la tabella "righeFatturaHash", la quale contiene le associazioni
 *	fra le righe di Fattura e i Beni ad esse associati. 
*/
public void makePersistentScaricoDaFattura(Buono_carico_scaricoBulk buonoS, OggettoBulk oggetto) throws PersistencyException, IntrospectionException {
	java.sql.PreparedStatement ps = null;
	String schema = it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema();
	
	LoggableStatement cs = null;
	String msg = null;
	String tipo=null;
	
	try {	
		if(oggetto instanceof Fattura_attivaBulk)
			tipo=((Fattura_attivaBulk) oggetto).getTi_fattura();
		else if(oggetto instanceof Fattura_passivaBulk)
			tipo=((Fattura_passivaBulk) oggetto).getTi_fattura();
		else if (oggetto instanceof Documento_genericoBulk)
			tipo="D";
		/* Richiama la procedura che renderà persistenti le modifiche fatte sui beni,
		 *	creerà il Buono di Scarico ed i suoi dettagli. Inoltre, la stessa procedura,
		 *	provvederà a scrivere anche le associazioni fatte sull'apposita tabella (ASS_INV_BENE_FATTURA).
		*/
		cs = new LoggableStatement(getConnection(), 
				"{call " + schema +"CNRCTB400.updScaricoInventarioBeni(?,?,?,?,?,?,?,?,?,?,?)}",false,this.getClass());
		//cs.registerOutParameter( 1, java.sql.Types.VARCHAR );
		cs.setString(1, buonoS.getLocal_transactionID()); // local_trans_id
		cs.setLong(2, buonoS.getPg_inventario().longValue()); // pg_inventario
		cs.setInt(3, buonoS.getEsercizio().intValue()); // esercizio
		cs.setLong(4, buonoS.getPg_buono_c_s().longValue()); // pg_buono_carico_scarico
		cs.setString(5, buonoS.getDs_buono_carico_scarico()); // ds_buono_carico_scarico
		cs.setString(6, buonoS.getCd_tipo_carico_scarico()); // cd_tipo_carico_scarico
		cs.setString(7, buonoS.getUser()); // utente
		cs.setString(8, "Y"); // da_fattura
		cs.setString(9,tipo);
		cs.setTimestamp(10, buonoS.getData_registrazione()); // dt_registrazione
		cs.registerOutParameter(11, java.sql.Types.VARCHAR); // Eventuale Messaggio di ritorno
		cs.executeQuery();
		msg= cs.getString(11);
	} catch (java.sql.SQLException e) {
		throw new PersistencyException(e);
	} finally {
		try {
			if (ps != null)
				try{ps.close();}catch( java.sql.SQLException e ){};
			if (cs != null)
				cs.close();
		} catch (java.sql.SQLException e) {
			throw new PersistencyException(e);
		}
	}
	
}
/**
 *	Ritorna TRUE se esiste un'associazione con un documento amministrativo
**/
public boolean isAssociato_documento_testata(Buono_carico_scaricoBulk buono) throws java.sql.SQLException{

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("ASS_INV_BENE_FATTURA");
	sql.addSQLJoin("ASS_INV_BENE_FATTURA.ESERCIZIO","BUONO_CARICO_SCARICO.ESERCIZIO");
	sql.addSQLJoin("ASS_INV_BENE_FATTURA.PG_INVENTARIO","BUONO_CARICO_SCARICO.PG_INVENTARIO");
	sql.addSQLJoin("ASS_INV_BENE_FATTURA.TI_DOCUMENTO","BUONO_CARICO_SCARICO.TI_DOCUMENTO");
	sql.addSQLJoin("ASS_INV_BENE_FATTURA.PG_BUONO_C_S","BUONO_CARICO_SCARICO.PG_BUONO_C_S");
	
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.ESERCIZIO",sql.EQUALS,buono.getEsercizio());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_INVENTARIO",sql.EQUALS,buono.getPg_inventario());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
	
	return sql.executeExistsQuery(getConnection());
}
public boolean IsContabilizzato(Buono_carico_scaricoBulk buono) throws java.sql.SQLException{

	SQLBuilder sql = createSQLBuilder();
	sql.addTableToHeader("BUONO_CARICO_SCARICO_DETT");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.ESERCIZIO","BUONO_CARICO_SCARICO.ESERCIZIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_INVENTARIO","BUONO_CARICO_SCARICO.PG_INVENTARIO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.TI_DOCUMENTO","BUONO_CARICO_SCARICO.TI_DOCUMENTO");
	sql.addSQLJoin("BUONO_CARICO_SCARICO_DETT.PG_BUONO_C_S","BUONO_CARICO_SCARICO.PG_BUONO_C_S");	
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.ESERCIZIO",sql.EQUALS,buono.getEsercizio());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_INVENTARIO",sql.EQUALS,buono.getPg_inventario());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.TI_DOCUMENTO",sql.EQUALS,buono.getTi_documento());
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO.PG_BUONO_C_S",sql.EQUALS,buono.getPg_buono_c_s());
	sql.openParenthesis("AND");
	sql.addSQLClause("AND","BUONO_CARICO_SCARICO_DETT.STATO_COGE",sql.EQUALS,Buono_carico_scarico_dettBulk.STATO_COGE_C);
	sql.addSQLClause("OR","BUONO_CARICO_SCARICO_DETT.STATO_COGE_QUOTE",sql.EQUALS,Buono_carico_scarico_dettBulk.STATO_COGE_C);
	sql.closeParenthesis(); 	
	return sql.executeExistsQuery(getConnection());
}

}