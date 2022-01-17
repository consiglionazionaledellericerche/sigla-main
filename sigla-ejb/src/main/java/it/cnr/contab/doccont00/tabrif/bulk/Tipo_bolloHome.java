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

package it.cnr.contab.doccont00.tabrif.bulk;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.bulk.Configurazione_cnrHome;
import it.cnr.jada.DetailedRuntimeException;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ApplicationRuntimeException;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.beans.*;
import it.cnr.jada.persistency.sql.*;

import java.util.Optional;

public class Tipo_bolloHome extends BulkHome {
public Tipo_bolloHome(java.sql.Connection conn) {
	super(Tipo_bolloBulk.class,conn);
}
public Tipo_bolloHome(java.sql.Connection conn,PersistentCache persistentCache) {
	super(Tipo_bolloBulk.class,conn,persistentCache);
}
/**
 * Restituisce il SQLBuilder per selezionare fra tutti i tipi di bollo quelli 
 * che non sono stati (logicamente) cancellati.
 * @return SQLBuilder 
 */
public SQLBuilder createSQLBuilder( )	
{
	SQLBuilder sql = super.createSQLBuilder();
	sql.addClause("AND", "fl_cancellato", SQLBuilder.EQUALS, new Boolean(false) );
	return sql; 
}
public Tipo_bolloBulk findTipoBolloDefault( String ti_entrata_spesa ) throws PersistencyException, it.cnr.jada.comp.ApplicationException	
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause( "AND", "fl_default", sql.EQUALS, new Boolean(true));
	if ( ti_entrata_spesa.equals( Tipo_bolloBulk.TIPO_ENTRATA ))
		sql.addClause( "AND", "ti_entrata_spesa", sql.NOT_EQUALS, Tipo_bolloBulk.TIPO_SPESA);
	else 	if ( ti_entrata_spesa.equals( Tipo_bolloBulk.TIPO_SPESA ))
		sql.addClause( "AND", "ti_entrata_spesa", sql.NOT_EQUALS, Tipo_bolloBulk.TIPO_ENTRATA);	
	java.util.List result = fetchAll( sql );
	if ( result.size() == 0 )
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste il tipo bollo di default" );
	return (Tipo_bolloBulk) result.get(0); 
}
public Tipo_bolloBulk findTipoBolloEsente( ) throws PersistencyException, it.cnr.jada.comp.ApplicationException	
{
	SQLBuilder sql = createSQLBuilder();
	sql.addClause("AND", "classe_tipo_bollo", SQLBuilder.EQUALS, Tipo_bolloBulk.CLASSE_ESENTE );
	java.util.List result = fetchAll( sql );
	if ( result.size() == 0 )
		throw new it.cnr.jada.comp.ApplicationException( "Non esiste il tipo bollo con classe ESENTE" );
	return (Tipo_bolloBulk) result.get(0); 
}
	public Tipo_bolloBulk findTipoBolloStipendi( String ti_entrata_spesa ) throws PersistencyException, it.cnr.jada.comp.ApplicationException
	{
		String codiceBollo = ((Configurazione_cnrHome)getHomeCache().getHome(Configurazione_cnrBulk.class)).getCodiceBolloStipendi();
		return Optional.ofNullable(codiceBollo).map(cdBollo-> {
			try {
				return (Tipo_bolloBulk)this.findByPrimaryKey(new Tipo_bolloBulk(cdBollo));
			} catch (PersistencyException e) {
				throw new DetailedRuntimeException("Codice bollo "+cdBollo+" non presente in tabella TipoBollo.");
			}
		}).orElse(this.findTipoBolloDefault(ti_entrata_spesa));
	}
}
