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

package it.cnr.contab.inventario01.comp;

import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sBulk;
import it.cnr.contab.inventario00.docs.bulk.Numeratore_buono_c_sHome;
import it.cnr.contab.inventario01.bulk.Buono_carico_scaricoBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

public class NumerazioneTempBuonoComponent extends it.cnr.jada.comp.CRUDComponent{

public NumerazioneTempBuonoComponent() {
	super();
}
//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo.
  *  validazione generazione consuntivo.
  *    PreCondition:
  *      Rilevata una condizione di errore.
  *    PostCondition:
  *      Negato il consenso alla registrazione del progressivo.
 */
//^^@@
public Long getNextTempPG (UserContext userContext,Buono_carico_scaricoBulk buono) 
	throws ComponentException {

		if (buono == null) return null;
			Numeratore_buono_c_sHome numHome = (Numeratore_buono_c_sHome) getHomeCache(userContext).getHome( Numeratore_buono_c_sBulk.class );
			try {
				return numHome.getNextTempPg(userContext,
										buono.getEsercizio(), 
										buono.getPg_inventario(), 
										buono.getTi_documento(), 
										userContext.getUser());
			} catch (it.cnr.jada.persistency.PersistencyException e) {
				throw handleException(e);
			}
		}	

}
