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
