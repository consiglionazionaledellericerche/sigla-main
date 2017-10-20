package it.cnr.contab.ordmag.magazzino.comp;

import java.io.Serializable;

import it.cnr.contab.ordmag.magazzino.bulk.MovimentiMagBulk;
import it.cnr.contab.ordmag.ordini.bulk.EvasioneOrdineBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public class MovimentiMagComponent
        extends it.cnr.jada.comp.CRUDComponent
        implements ICRUDMgr, Cloneable, Serializable {

    public final static String TIPO_TOTALE_COMPLETO = "C";
    public final static String TIPO_TOTALE_PARZIALE = "P";
    private final static int INSERIMENTO = 1;
    private final static int MODIFICA = 2;
    private final static int CANCELLAZIONE = 3;

    public MovimentiMagComponent() {

        /*Default constructor*/


    }
    public MovimentiMagBulk caricaOrdine(it.cnr.jada.UserContext userContext, EvasioneOrdineBulk evasioneOrdine)
            throws ComponentException {
	/*
	    try {
			for (java.util.Iterator i= evasioneOrdine.getListaRigheConsegnaEvase().iterator(); i.hasNext();) {
				EvasioneOrdineRigaBulk riga = (EvasioneOrdineRigaBulk) i.next();
				if (riga.getDspObbligazioneScadenzario() != null && riga.getDspObbligazioneScadenzario().getPg_obbligazione() != null){
					  throw new ApplicationException("Scollegare prima gli impegni collegati all'ordine prima di procedere alla cancellazione.");
				}
				if (riga != null){
					for (java.util.Iterator c= riga.getRigheConsegnaColl().iterator(); c.hasNext();) {
						OggettoBulk consbulk= (OggettoBulk) c.next();
						OrdineAcqConsegnaBulk cons= (OrdineAcqConsegnaBulk) consbulk;
						if (cons.getObbligazioneScadenzario() != null && cons.getObbligazioneScadenzario().getPg_obbligazione() != null){
							  throw new ApplicationException("Scollegare prima gli impegni collegati all'ordine prima di procedere alla cancellazione.");
						}
					}
				}
			}
	            
			ordine.setAnnullato(DateServices.getDt_valida(aUC));
			ordine.setToBeUpdated();
			makeBulkPersistent( aUC, ordine);
			return ordine;
	    } catch (Exception e) {
	        throw handleException(e);
	    }
*/
        return null;
    }

}
