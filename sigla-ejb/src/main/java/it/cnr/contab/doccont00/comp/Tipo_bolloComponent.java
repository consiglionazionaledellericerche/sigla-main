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

package it.cnr.contab.doccont00.comp;

import java.sql.*;
import java.util.*;
import it.cnr.contab.doccont00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.*;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;

public class Tipo_bolloComponent extends it.cnr.jada.comp.CRUDComponent implements ITipo_bolloMgr,ICRUDMgr,Cloneable,Serializable
{

//@@<< CONSTRUCTORCST
    public  Tipo_bolloComponent()
    {
//>>

//<< CONSTRUCTORCSTL
        /*Default constructor*/
//>>

//<< CONSTRUCTORCSTT

    }
/** 
  *  Creazione di un tipo bollo non default
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo non default e' stata generata
  *    PostCondition:
  *      il tipo bollo e' stato creato 
  *  Creazione di un tipo bollo default - errore
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo di default e' stata generata ed esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      una ComponentException viene generata per segnalare all'utente l'impossibilità ad effettuare l'inserimento
  *  Creazione di un tipo bollo default - OK
  *    PreCondition:
  *      la richiesta di creazione di un tipo bollo di default e' stata generata e non esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      il tipo bollo e' stato creato dopo essere stato validato (metodo verificaFl_default)
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>Tipo_bolloBulk</code> da creare
  *
  * @return OggettoBulk <code>Tipo_bolloBulk</code> creato
  */
public OggettoBulk creaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	Tipo_bolloBulk tipo_bollo = (Tipo_bolloBulk) bulk;

	try
	{
		validaCreaConBulk( uc, tipo_bollo );
		if ( tipo_bollo.getFl_default().booleanValue() )
			verificaFl_default(uc, tipo_bollo);
		return super.creaConBulk( uc, tipo_bollo);
	}
	catch ( CRUDDuplicateKeyException e )
	{
		try
		{
			tipo_bollo = (Tipo_bolloBulk) getHome( uc, Tipo_bolloBulk.class ).findByPrimaryKey( tipo_bollo );
			if ( tipo_bollo.getFl_cancellato().booleanValue() )
				throw handleException( new ApplicationException( "Inserimento impossibile: chiave duplicata"));
			else
				throw handleException( e );
		}
		catch ( Exception x )
		{
			throw handleException( x );		
		}	
	}
	catch ( Exception e )
	{
		throw handleException( e );		
	}	
	
}
/**
 * Esegue una operazione di eliminazione logica o fisica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione logica di un Tipo_bollo
 * Pre: Una richiesta di cancellazione logica di un tipo bollo e' stata generata
 * Post: Il flag cancellazione del Tipo Bollo e' stato impostato a true
 *
 * Nome: Cancellazione fisica di un Tipo_bollo
 * Pre: Una richiesta di cancellazione fisica di un tipo bollo e' stata generata
 * Post: Il Tipo Bollo e' stato eliminato
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	il <code>Tipo_bolloBulk</code> che deve essere cancellato
 */	
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	Tipo_bolloBulk tipo_bollo = (Tipo_bolloBulk) bulk;
	try
	{
		deleteBulk( userContext, tipo_bollo );
	}
	catch ( it.cnr.jada.persistency.sql.ReferentialIntegrityException e )
	{
		try
		{
			tipo_bollo.setFl_cancellato( new Boolean(true) );
			tipo_bollo.setUser( userContext.getUser());
			updateBulk( userContext, tipo_bollo);
		}
		catch ( Exception ex )
		{
			throw handleException( tipo_bollo, ex );
		}
	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException( tipo_bollo, e );	
	}

}
/** 
  *  Modifica di un tipo bollo non default
  *    PreCondition:
  *      la richiesta di modifica di un tipo bollo non default e' stata generata
  *    PostCondition:
  *      il tipo bollo e' stato modificato 
  *  Modifica di un tipo bollo default - OK
  *    PreCondition:
  *      la richiesta di modifica di un tipo bollo di default e' stata generata e non esiste un altro tipo bollo
  *      definito come default
  *    PostCondition:
  *      il tipo bollo e' stato modificato dopo essere stato validato (metodo verificaFl_default)
  *
  *
  * @param uc lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>Tipo_bolloBulk</code> da modificare
  *
  * @return OggettoBulk <code>Tipo_bolloBulk</code> modificato
  */
public OggettoBulk modificaConBulk (UserContext uc,OggettoBulk bulk) throws ComponentException
{
	Tipo_bolloBulk tipo_bollo = (Tipo_bolloBulk) bulk;
	
		if ( tipo_bollo.getFl_default().booleanValue() )
			verificaFl_default(uc, tipo_bollo);
			
		return super.modificaConBulk( uc, tipo_bollo);
}
//^^@@
/** 
  *  Flag di default selezionato
  *    PreCondition:
  *      Può essere creato un solo tipo di bollo di default.
  *    PostCondition:
  *      Il metodo utilizza un Throw Exception per comunicare se esiste già un tipo di bollo di default.
  *  Tipo bollo di default di entrata/spesa
  *    PreCondition:
  *      Può essere creato un solo tipo di bollo di default di tipo ENTRATA/SPESA
  *    PostCondition:
  *      Il sistema consente di procedere alla creazione o modifica di un tipo di bollo di default
  *		 con queste caratteristiche purchè non ne esista già uno di entrata o di spesa.
  *  Tipo bollo di default di entrata o di spesa
  *    PreCondition:
  *      Può essere creato un solo tipo di bollo di default di tipo ENTRATA o di SPESA
  *    PostCondition:
  *      Il sistema consente di procedere alla creazione o modifica di un tipo di bollo di default
  *		 con queste caratteristiche purchè non ne esista già uno di entrata/spesa.
  *  Flag di default NON selezionato
  *    PreCondition:
  *      Il tipo di bollo non è di default.
  *    PostCondition:
  *      Il sistema consente l'aggiornamento del tipo di bollo.
  *
  *
  * @param aUC lo <code>UserContext</code> che ha generato la richiesta
  * @param tipo_bollo <code>Tipo_bolloBulk</code> su cui viene fatto il controllo
  *
 */
//^^@@
private void verificaFl_default (UserContext aUC,Tipo_bolloBulk tipo_bollo) throws ComponentException
        {
			LoggableStatement ps = null;
	        try
	        {

	            if ( tipo_bollo.getTi_entrata_spesa().equals( tipo_bollo.TIPO_ENTRATA_SPESA ))
	            {
		            ps = new LoggableStatement(getConnection( aUC ),
					"SELECT CD_TIPO_BOLLO " +			
					"FROM " +
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
					"TIPO_BOLLO WHERE " +
					"FL_DEFAULT = 'Y' AND " + 
					"FL_CANCELLATO = 'N' ",true,this.getClass());
	            }
	            else // tipo bollo == ENTRATA o tipo bollo = SPESA
	            {
		            ps = new LoggableStatement(getConnection( aUC ),
					"SELECT CD_TIPO_BOLLO " +			
					"FROM " +
					it.cnr.jada.util.ejb.EJBCommonServices.getDefaultSchema() + 			
					"TIPO_BOLLO WHERE " +
					"FL_DEFAULT = 'Y' AND " + 
					"FL_CANCELLATO = 'N' AND " +
					"TI_ENTRATA_SPESA <> ? ",true,this.getClass());
	            }
	            try
	            {
		           	if ( tipo_bollo.getTi_entrata_spesa().equals( tipo_bollo.TIPO_ENTRATA ))
			           	ps.setString(1, tipo_bollo.TIPO_SPESA );
			       	else if ( tipo_bollo.getTi_entrata_spesa().equals( tipo_bollo.TIPO_SPESA ))
		 	   		    ps.setString(1, tipo_bollo.TIPO_ENTRATA );
		     
	      			ResultSet rs = ps.executeQuery();
					try
					{
						if ( rs.next() )
						{
							String tipo2 =  rs.getString(1);				
							if( tipo2 != null && !tipo_bollo.getCd_tipo_bollo().equals( tipo2 ) )
								throw handleException( new it.cnr.jada.comp.ApplicationException( "E' già stato creato un tipo di bollo di default"));
						}	
					}
					catch( SQLException e )
					{
						throw handleException( e );
					}
					finally
					{
						try{rs.close();}catch( java.sql.SQLException e ){};
					}
	            }
				catch ( SQLException e )
				{
					throw handleException( tipo_bollo, e );	
				}
 				finally
		   	 	{
			   	 	try{ps.close();}catch( java.sql.SQLException e ){};
		    	}
	      	}
			catch ( SQLException e )
			{
				throw handleException( tipo_bollo, e );	
			}
       }
}
