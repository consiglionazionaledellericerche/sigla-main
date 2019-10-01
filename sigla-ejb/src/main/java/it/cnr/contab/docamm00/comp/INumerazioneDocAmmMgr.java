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

package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface INumerazioneDocAmmMgr
{


//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo.
  *  esistenza della tipologia della numerazione
  *    PreCondition:
  *      Non esiste la numerazione per il tipo di documento amministrativo
  *    PostCondition:
  *      Viene inserita una nuova numerazione per il tipo documento amministrativo, CDS, UO e esercizio correnti
  */
//^^@@

public abstract Long getNextPG (UserContext uAC,Numerazione_doc_ammBulk progressivo) throws ComponentException;
}
