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

import java.io.Serializable;
import it.cnr.contab.docamm00.tabrif.bulk.CambioBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.util.RemoteIterator;

import java.io.PrintWriter;
public class CambioComponent
	extends it.cnr.jada.comp.CRUDComponent
    implements ICRUDMgr, ICambioMgr, Cloneable, Serializable {

   
    public  CambioComponent()
    {

        /*Default constructor*/


    }
//^^@@
/** 
  *  tutti i controlli superati
  *    PreCondition:
  *      validaCambio = true
  *    PostCondition:
  *      Consente la modifica del cambio.
  *  validaCambio  non superata
  *    PreCondition:
  *      L'inizio del periodo è incongruente.
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, non si possono creare cambi con data inizio validita inferiore a quella esistente".
 */
//^^@@
public OggettoBulk creaConBulk(UserContext aUC, OggettoBulk cambio) throws ComponentException {
    /*  
    validaCambio(aUC,(CambioBulk)cambio);*/
    if (!validaCambio(aUC, (CambioBulk) cambio))
        throw handleException(new it.cnr.jada.comp.ApplicationException("Attenzione, il cambio per il periodo impostato già esiste"));
    ((CambioBulk) cambio).setCd_divisa(((CambioBulk) cambio).getDivisa().getCd_divisa());
    return super.creaConBulk(aUC, cambio);
}
/**
  *  Controllo cambio di default
  *    PreCondition: Il cambio è quello di default
  *    PostCondition: Viene sollevata un'eccezione
  *
  *  Tutti i controlli superati.
  *    PreCondition: Nessun errore rilevato.
  *    PostCondition: Viene consentita la camcellazione.
  *
 */

public void eliminaConBulk(UserContext aUC,OggettoBulk cambio) throws it.cnr.jada.comp.ComponentException {

	if (((CambioBulk)cambio).iscambioDefault())
    	throw handleException( new it.cnr.jada.comp.ApplicationException("Attenzione, l' eliminazione di questi dati non è consentita"))	;
    
    super.eliminaConBulk( aUC, cambio );
	
}
/**
 * Gestisce un cambiamento di pagina su un controllo tabbed {@link it.cnr.jada.util.jsp.JSPUtils.tabbed}
 */
private String getEuro(UserContext userContext) throws ComponentException {

	String cd_euro = null;
	try {
		cd_euro = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession", it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession.class)).getVal01(userContext, new Integer(0), "*", "CD_DIVISA", "EURO");
	} catch (javax.ejb.EJBException e) {
		handleException(e);
	} catch (java.rmi.RemoteException e) {
		handleException(e);
	}

	return cd_euro;	
}
//^^@@
/** 
  *  Oggetto non esistente
  *    PreCondition:
  *      L'OggettoBulk specificato non esiste.
  *    PostCondition:
  *      Viene generata una CRUDException con la descrizione dell'errore.
 */
//^^@@
public OggettoBulk inizializzaBulkPerModifica (UserContext aUC,OggettoBulk bulk) throws ComponentException
        {

            if (((CambioBulk) bulk).getCd_divisa().equals(getEuro(aUC)))
	            ((CambioBulk) bulk).setCambioDefault(true);
	        return bulk;
        }
//^^@@
/** 
  *  Tutti i controlli superati
  *    PreCondition:
  *      validaVoceIva = true
  *    PostCondition:
  *      Consente la modifica della voce iva.
  *  validaCambio non superata
  *    PreCondition:
  *      validaCambio = false
  *    PostCondition:
  *      Viene inviato il messaggio "Attenzione, la modifica di questi dati non è consentita".
 */
//^^@@
public OggettoBulk modificaConBulk (UserContext aUC,OggettoBulk cambio) throws ComponentException
        {
           throw new it.cnr.jada.comp.ApplicationException("Non e' possibile modificare i cambi");
        }
public it.cnr.jada.persistency.sql.SQLBuilder selectDivisaByClause(UserContext aUC,CambioBulk cambio, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk divisa, CompoundFindClause clauses) 
	throws ComponentException {

	it.cnr.contab.docamm00.tabrif.bulk.DivisaHome divisaHome = (it.cnr.contab.docamm00.tabrif.bulk.DivisaHome)getHome(aUC, it.cnr.contab.docamm00.tabrif.bulk.DivisaBulk.class);
	it.cnr.jada.persistency.sql.SQLBuilder sql = divisaHome.createSQLBuilder();
	if (cambio.getDivisa()!=null && cambio.getDivisa().getCd_divisa()!=null)
		sql.addClause("AND", "cd_divisa", sql.EQUALS, cambio.getDivisa().getCd_divisa());	
	sql.addClause("AND", "cd_divisa", sql.NOT_EQUALS, getEuro(aUC));
	return sql;
}
//^^@@
/** 
  *  Tutti i controlli superati.
  *    PreCondition:
  *      Nessuna voce iva di default preesistente.
  *    PostCondition:
  *      restituisce true
  *  Cambio non validato.
  *    PreCondition:
  *      E' stato modificata il valore o data inizio validita di un periodo esistente o inserito un nuovo periodo con data inizio inferiore aperiodo esistente.
  *    PostCondition:
  *      Ritorna false
 */
//^^@@
public boolean validaCambio (UserContext aUC,CambioBulk cambio)
        {
           try
			{
	   			it.cnr.contab.docamm00.tabrif.bulk.CambioHome cambioH = (it.cnr.contab.docamm00.tabrif.bulk.CambioHome)getHome(aUC,CambioBulk.class);								
				
				cambioH.checkInserimentoSuccessivo(cambio);
	   			return cambioH.checkPeriodi(cambio);		        
			}
			catch (Exception e)
			{
				return false;
			}   	
        }
}
