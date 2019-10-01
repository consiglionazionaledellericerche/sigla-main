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
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface IVoceIvaMgr
{

    
//^^@@
/** 
  *  Voce iva non verificata.
  *    PreCondition:
  *      Una voce iva di default esiste gia.
  *    PostCondition:
  *      restituisce false come valore
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
 */
//^^@@
public abstract boolean validaVoceIva (UserContext aUC,Voce_ivaBulk voceIva);

/** 
  *  Voce iva non verificata.
  *    PreCondition:
  *      Una voce iva di default esiste gia.
  *    PostCondition:
  *      restituisce false come valore
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
 */

public abstract it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk caricaVoceIvaDefault(it.cnr.jada.UserContext param0) throws it.cnr.jada.comp.ComponentException;

/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaVoceIva  non superata
  *    PreCondition:
  *      validaVoceIva = False.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, una voce iva di default è gia presente".
 */

public abstract it.cnr.jada.bulk.OggettoBulk creaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;

/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaVoceIva  non superata
  *    PreCondition:
  *      validaVoceIva = False.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, una voce iva di default è gia presente".
 */

public abstract it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}