package it.cnr.contab.compensi00.comp;

import it.cnr.contab.compensi00.tabrif.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.bulk.*;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.sql.*;

/**
 * Insert the type's description here.
 * Creation date: (14/03/2002 10.18.33)
 * @author: Roberto Fantino
 */
public interface ITrattamentoCORIMgr extends ICRUDMgr {
/**
 * Ricerca righe associate al Tipo Trattamento
 *
 * Pre-post-conditions
 *
 * Nome: Ricerca intervalli associati al tipo trattamento selezionato
 * Pre: Viene richiesta la lista delle righe del Trattamento CO/RI
 *		che hanno il Tipo Trattamento selezionato
 * Post: Viene inserita nel Trattamento CO/RI <trattCORI> la lista
 *       di tutti i Trattamenti CO/RI con lo stesso Tipo Trattamento
 *
*/
public abstract it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk fillAllRows(it.cnr.jada.UserContext param0,it.cnr.contab.compensi00.tabrif.bulk.Trattamento_coriBulk param1) throws it.cnr.jada.comp.ComponentException;
}
