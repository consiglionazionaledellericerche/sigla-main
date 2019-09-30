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

package it.cnr.contab.inventario00.comp;

import it.cnr.contab.inventario00.tabrif.bulk.*;
import it.cnr.contab.config00.sto.bulk.*;
import it.cnr.contab.config00.esercizio.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Insert the type's description here.
 * Creation date: (18/12/2001 13.49.33)
 * @author: Roberto Fantino
 */
public class TipoCaricoScaricoComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,ITipoCaricoScaricoMgr,Cloneable,Serializable{

/**
 * TipoCaricoScaricoComponent constructor comment.
 */
public TipoCaricoScaricoComponent() {
	super();
}
/** 
  * Eliminazione di un tipo di Movimento
  *    PreCondition:
  *      E' stata generata la richiesta di eliminazione di un tipo di Movimento Carico/Scarico dell'Inventario.
  *    PostCondition:
  *      La cancellazione è solo logica, quindi viene aggiornato il record sul DB relativo 
  *		al movimento di riferimento, impostando la data di cancellazione uguale al TimeStamp.
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param bulk <code>OggettoBulk</code> il Bulk da eliminare
  *
  * @return bulk l'oggetto <code>OggettoBulk</code> creato
**/ 
public void eliminaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException{

	try{

		Tipo_carico_scaricoBulk tipoCS= (Tipo_carico_scaricoBulk)bulk;
		tipoCS.setDt_cancellazione(getHome(userContext,Tipo_carico_scaricoBulk.class).getServerTimestamp());
		updateBulk( userContext, tipoCS);

	}catch (it.cnr.jada.persistency.PersistencyException e){
		throw handleException(bulk,e);
	}
}
/** 
  *  Ricerca di un Tipo di Movimento
  *    PreCondition:
  *      E' stata generata la richiesta di cercare un Tipo di movimento di Carico/Scarico Inventario.
  *    PostCondition:
  *		E' stato creato il SQLBuilder con le clausole implicite (presenti nell'istanza di Tipo_carico_scaricoBulk).
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param clauses <code>CompoundFindClause</code> le clausole della selezione
  * @param bulk <code>OggettoBulk</code> il Tipo Movimento modello
  *
  * @return sql <code>SQLBuilder</code> Risultato della selezione.
**/
protected Query select(UserContext userContext,CompoundFindClause clauses,OggettoBulk bulk) throws ComponentException, it.cnr.jada.persistency.PersistencyException {

	Tipo_carico_scaricoBulk tipoCS = (Tipo_carico_scaricoBulk)bulk;	
	
	SQLBuilder sql = (SQLBuilder)super.select(userContext,clauses, bulk);
		
	return sql;
	
}
}
