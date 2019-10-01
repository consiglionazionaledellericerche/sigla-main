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

/**
 * Insert the type's description here.
 * Creation date: (1/11/2002 12:07:54 PM)
 * @author: Simonetta Costa
 */
public interface INumerazioneTemporaneaDocCont {
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo temporaneo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo temporaneo.
  *  validazione
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  *  esistenza della numerazione
  *    PreCondition:
  *      Non esiste la numerazione temporanea per il tipo di documento contabile
  *    PostCondition:
  *      Viene inserita una nuova numerazione temporanea per il tipo documento contabile, CDS e esercizio correnti
  *  esistenza della numerazione definitiva
  *    PreCondition:
  *      Non esiste la numerazione definitiva per il tipo di documento contabile
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  */
//^^@@

public Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento)
	throws it.cnr.jada.comp.ComponentException;
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo temporaneo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo temporaneo.
  *  validazione
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  *  esistenza della numerazione
  *    PreCondition:
  *      Non esiste la numerazione temporanea per il tipo di documento contabile
  *    PostCondition:
  *      Viene inserita una nuova numerazione temporanea per il tipo documento contabile, CDS e esercizio correnti
  *  esistenza della numerazione definitiva
  *    PreCondition:
  *      Non esiste la numerazione definitiva per il tipo di documento contabile
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo temporaneo.
  */
//^^@@

public Long getNextTempPg(
	it.cnr.jada.UserContext userContext,
	it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk obbligazione)
	throws it.cnr.jada.comp.ComponentException;
}
