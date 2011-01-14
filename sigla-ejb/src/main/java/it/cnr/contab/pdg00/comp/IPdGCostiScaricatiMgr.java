package it.cnr.contab.pdg00.comp;
public interface IPdGCostiScaricatiMgr extends it.cnr.jada.comp.IMultipleCRUDMgr {
/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di eliminare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
	public void eliminaConBulk (it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;

/** 
  *  default
  *    PreCondition:
  *      Viene richiesto di modificare un dettaglio scaricato
  *    PostCondition:
  *      Restituisce un'eccezione
 */
	public it.cnr.jada.bulk.OggettoBulk modificaConBulk(it.cnr.jada.UserContext userContext,it.cnr.jada.bulk.OggettoBulk bulk) throws it.cnr.jada.comp.ComponentException;
}