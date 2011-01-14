package it.cnr.contab.pdg00.comp;

/**
 * Insert the type's description here.
 * Creation date: (17/10/2001 10.08.43)
 * @author: Luca Bessi
 */
public interface IPdGEntrateFigurativeMgr extends it.cnr.jada.comp.IMultipleCRUDMgr {
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio caricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */

public abstract void eliminaConBulk(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
		public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
}
