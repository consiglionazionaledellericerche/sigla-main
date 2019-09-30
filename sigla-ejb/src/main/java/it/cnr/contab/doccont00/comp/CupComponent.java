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

import it.cnr.contab.config00.sto.bulk.EnteBulk;
import it.cnr.contab.doccont00.tabrif.bulk.*;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.PersistencyException;

public class CupComponent extends it.cnr.jada.comp.CRUDComponent 
{

    public  CupComponent()
    {
    }

/**
 * Esegue una operazione di eliminazione logica o fisica di un OggettoBulk.
 *
 * Pre-post-conditions:
 *
 * Nome: Cancellazione logica di un cup
 * Pre: Una richiesta di cancellazione logica di un cup e' stata generata
 * Post: Il data cancellazione del cup e' stato valorizzata
 *
 * Nome: Cancellazione fisica di un cup
 * Pre: Una richiesta di cancellazione fisica di un cup e' stata generata
 * Post: cup e' stato eliminato
 * 
 * @param	userContext	lo UserContext che ha generato la richiesta
 * @param	bulk	il <code>CupBulk</code> che deve essere cancellato
 */	
public void eliminaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException
{
	CupBulk cup = (CupBulk) bulk;
	try
	{
		deleteBulk( userContext, cup );
	}
	catch ( it.cnr.jada.persistency.sql.ReferentialIntegrityException e )
	{
		try
		{
			if(cup.getDt_canc()==null){
				java.sql.Timestamp dataOdierna = it.cnr.jada.util.ejb.EJBCommonServices.getServerDate();
				cup.setDt_canc(dataOdierna);
				cup.setUser( userContext.getUser());
				updateBulk( userContext, cup);
			}else
				throw new ApplicationException("Codice Cup presente sui documenti. Già cancellato logicamente.");
		}
		catch ( Exception ex )
		{
			throw handleException(cup, ex );
		}
	}
	catch ( it.cnr.jada.persistency.PersistencyException e )
	{
		throw handleException( cup, e );	
	}

}
public String recuperoCds(UserContext userContext) throws ComponentException, PersistencyException
	{
	EnteBulk ente = (EnteBulk) getHome(userContext, EnteBulk.class).findAll().get(0);
	
		 if (CNRUserContext.getCd_cds(userContext).compareTo(ente.getCd_unita_organizzativa())==0)
			 return "%";
		 else
			 return CNRUserContext.getCd_cds(userContext);
	}
}
