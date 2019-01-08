package it.cnr.contab.config00.ejb;

import javax.ejb.Remote;

import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;

@Remote
public interface CRUDConfigAssEvoldEvnewComponentSession extends it.cnr.jada.ejb.CRUDComponentSession {
	Elemento_voceBulk getCurrentElementoVoce(UserContext usercontext, Elemento_voceBulk voceOld, int annoVoceNew) throws ComponentException;
}
