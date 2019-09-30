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

package it.cnr.contab.missioni00.docs.bulk;

import it.cnr.contab.docamm00.ejb.*;
import it.cnr.contab.docamm00.docs.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.EsercizioBulk;
import it.cnr.jada.bulk.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

public class AnticipoHome extends BulkHome implements it.cnr.contab.docamm00.docs.bulk.IDocumentoAmministrativoSpesaHome
{
public AnticipoHome(java.sql.Connection conn) {
	super(AnticipoBulk.class,conn);
}
public AnticipoHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(AnticipoBulk.class,conn,persistentCache);
}
/**
 * Il metodo ritorna la data di registrazione piu' alta degli anticipi fino ad ora registrati
 */
public java.sql.Timestamp findDataRegistrazioneUltimoAnticipo( AnticipoBulk anticipo ) throws PersistencyException, java.sql.SQLException
{
	LoggableStatement ps = new LoggableStatement(getConnection(),
		"SELECT MAX(DT_REGISTRAZIONE) " +			
		"FROM " +
		it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
		"ANTICIPO WHERE " +
		"ESERCIZIO = ? AND CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? ",true,this.getClass());
		ps.setObject( 1, anticipo.getEsercizio() );
		ps.setString( 2, anticipo.getCd_cds() );
		ps.setString( 3, anticipo.getCd_unita_organizzativa());
	
	java.sql.ResultSet rs = ps.executeQuery();
	if(rs.next())
		return rs.getTimestamp(1);
	else
		return null;
}
/**
 * Imposta il pg_anticipo di un oggetto AnticipoBulk.
 */

public void initializePrimaryKeyForInsert(it.cnr.jada.UserContext userContext,OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException 
{
	AnticipoBulk anticipo = (AnticipoBulk) bulk;

	try
	{
		// Assegno un nuovo progressivo all'anticipo 
		ProgressiviAmmComponentSession progressiviSession = (ProgressiviAmmComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRDOCAMM00_EJB_ProgressiviAmmComponentSession", ProgressiviAmmComponentSession.class);
		Numerazione_doc_ammBulk numerazione = new Numerazione_doc_ammBulk(anticipo);
		anticipo.setPg_anticipo(progressiviSession.getNextPG(userContext, numerazione));
	}
	catch(Throwable e)
	{
		throw new it.cnr.jada.comp.ComponentException(e);
	}
}
/**
 * Metodo richiesto dall' interfaccia IDocumentoAmministrativoSpesaHome
 * Il metodo aggiorna l'anticipo dopo che e' stato collegato ad una spesa del Fondo Economale
 */

public void updateFondoEconomale(it.cnr.contab.fondecon00.core.bulk.Fondo_spesaBulk spesa) throws PersistencyException, OutdatedResourceException, BusyResourceException 
{
	if (spesa == null) 
		return;

	AnticipoBulk anticipo = (AnticipoBulk)spesa.getDocumento();

	lock(anticipo);
	
	StringBuffer stm = new StringBuffer("UPDATE ");
	stm.append(it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema());
	stm.append(getColumnMap().getTableName());
	stm.append(" SET STATO_PAGAMENTO_FONDO_ECO = ?, DT_PAGAMENTO_FONDO_ECO = ?, PG_VER_REC = PG_VER_REC+1, DUVA = ?, UTUV = ?");
	stm.append(" WHERE (");
	stm.append("CD_CDS = ? AND CD_UNITA_ORGANIZZATIVA = ? AND ESERCIZIO = ? AND PG_ANTICIPO = ? )");
	
	try 
	{
		LoggableStatement ps = new LoggableStatement(getConnection(),stm.toString(),true,this.getClass());
		try 
		{	
			ps.setString(1, (spesa.isToBeCreated() || spesa.isToBeUpdated()) ? anticipo.STATO_REGISTRATO_FONDO_ECO : anticipo.STATO_ASSEGNATO_FONDO_ECO);
			if (spesa.isToBeCreated() || spesa.isToBeUpdated())
				ps.setTimestamp(2, spesa.getDt_spesa());
			else 
				ps.setNull(2, java.sql.Types.TIMESTAMP);

			ps.setTimestamp(3, getServerTimestamp());
			ps.setString(4, spesa.getUser());
				
			ps.setString(5, anticipo.getCd_cds());
			ps.setString(6, anticipo.getCd_unita_organizzativa());
			ps.setInt(7, anticipo.getEsercizio().intValue());
			ps.setLong(8, anticipo.getPg_anticipo().longValue());

			ps.executeUpdate();
		} 
		finally 
		{
			try{ps.close();}catch( java.sql.SQLException e ){};
		}
	} 
	catch(java.sql.SQLException e) 
	{
		throw it.cnr.jada.persistency.sql.SQLExceptionHandler.getInstance().handleSQLException(e,spesa);
	}
}
}
