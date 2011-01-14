package it.cnr.contab.doccont00.comp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.*;
import it.cnr.jada.comp.*;
/**
 * Insert the type's description here.
 * Creation date: (20/11/2002 12.25.11)
 * @author: Roberto Fantino
 */
public interface IAssManualeMandatoReversaleMgr extends it.cnr.jada.comp.ICRUDMgr {
public java.util.Collection loadReversaliAssociate(UserContext userContext, MandatoBulk mandato) throws ComponentException;
public java.util.List loadReversaliDisponibili(UserContext userContext, MandatoBulk mandato) throws ComponentException;
}
