package it.cnr.contab.compensi00.comp;

import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;
/**
 * Insert the type's description here.
 * Creation date: (22/03/2002 11.10.22)
 * @author: Roberto Fantino
 */
public interface ITipologiaRischioMgr extends ICRUDMgr{
/**
 * Insert the method's description here.
 * Creation date: (14/03/2002 11.35.49)
 * @return it.cnr.jada.bulk.OggettoBulk
 * @param userContext it.cnr.jada.UserContext
 * @param bulk it.cnr.jada.bulk.OggettoBulk
 * @exception it.cnr.jada.comp.ComponentException The exception description.
 */
public abstract OggettoBulk creaConBulk(UserContext userContext,OggettoBulk bulk) throws ComponentException;
}
