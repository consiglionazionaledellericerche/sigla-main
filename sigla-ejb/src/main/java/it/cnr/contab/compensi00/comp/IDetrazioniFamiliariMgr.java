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

package it.cnr.contab.compensi00.comp;

/**
 * Insert the type's description here.
 * Creation date: (05/12/2001 11.53.30)
 * @author: CNRADM
 */
import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public interface IDetrazioniFamiliariMgr extends ICRUDMgr {


/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      La tariffa inserita ha data inizio validità interna all'ultimo periodo preesistente (con data fine = infinito) OR è il primo record della validità della tariffa e ha fine = infinito.
  *    PostCondition:
  *      Consente l'inserimento della tariffa.
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *      Si e verificato un errore.
  *      
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione,  si è verificato un errore".
 */
//^^@@
public abstract OggettoBulk creaConBulk (UserContext userContext,OggettoBulk bulk) throws ComponentException;

/**
 * Insert the method's description here.
 * Creation date: (05/12/2001 12.03.36)
 *//** 
  *  Tutti i controlli per la cancellazione del record sono stati superati
  *    	PreCondition:
  *		   	Deve esistere un record con chiave uguale a quella del record da cancellare e con data fine validita 
  *		   	uguale a data di inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Cancello il record ed aggiorno il record di periodo precendente a quello cancvellato 
  *		   	mettendo la sua data di fine ad infinito (31/12/2200)
  *
  *  Riscontrata condizione di errore.
  *    PreCondition:
  *        	la tabella non contiene un altro record con stessa chiave di quello che sto cancellando
  *			e con data fine validita uguale alla data inizio validita del record da cancellare meno un giorno
  *    PostCondition:
  *        	Viene inviato il messaggio "Attenzione, deve esistere almeno un periodo".
**/

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}
