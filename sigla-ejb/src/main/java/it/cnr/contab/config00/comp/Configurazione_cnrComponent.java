package it.cnr.contab.config00.comp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ComponentException;
public class Configurazione_cnrComponent extends it.cnr.jada.comp.GenericComponent implements IConfigurazione_cnrMgr,Cloneable,Serializable 
{



    public  Configurazione_cnrComponent()
    {

        /*Default constructor*/


    }
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto un istanza di Configurazioe_cnrBulk dalle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito un'istanza di Configurazione_cnrBulk o null se la ricerca non restituisce nulla
 */
public Configurazione_cnrBulk getConfigurazione (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		return (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public Timestamp getDt01 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getDt01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public Timestamp getDt01 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getDt01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public Timestamp getDt02 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getDt02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public Timestamp getDt02 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getDt02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public BigDecimal getIm01 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getIm01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public BigDecimal getIm01 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getIm01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public BigDecimal getIm02 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getIm02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public BigDecimal getIm02 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getIm02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal01 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getVal01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal01 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getVal01();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal02 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getVal02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal02 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getVal02();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal03 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getVal03();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal03 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getVal03();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  esercizio nullo
  *    PreCondition:
  *      esercizio = null
  *    PostCondition:
  *      Effettua la ricerca usando la condizione SQL esercizio = '*'
  *  unita funzionale nulla
  *    PreCondition:
  *      unita_funzionale = null
  *    PostCondition:
  *      Effettua la ricerca con la codizione SQL cd_unita_funzionale = '*'
  *  chiave secondaria nulla
  *    PreCondition:
  *      chiave_secondaria = null
  *    PostCondition:
  *      Effettua la ricerca usando la clausola SQL chiave_secondaria = '*'
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal04 (UserContext userContext,Integer esercizio,String unita_funzionale,String chiave_primaria,String chiave_secondaria) throws ComponentException {
	try {
		if (esercizio == null) esercizio = new Integer(0);
		if (unita_funzionale == null) unita_funzionale = "*";
		if (chiave_secondaria == null) chiave_secondaria = "*";
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,chiave_secondaria,unita_funzionale,esercizio));
		if (bulk == null) return null;
		return bulk.getVal04();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
/** 
  *  Normale
  *    PreCondition:
  *      Viene richiesto il valore del campo val01 delle costanti cnr
  *    PostCondition:
  *      Viene effettuata una ricerca sulla tabella CONFIGURAZIONE_CNR con le chiavi specificate e viene restituito il valore del campo; se non viene trovato nessun record restituisce null
 */
public String getVal04 (UserContext userContext,String chiave_primaria) throws ComponentException {
	try {
		Configurazione_cnrBulk bulk = (Configurazione_cnrBulk)getHome(userContext,Configurazione_cnrBulk.class).findByPrimaryKey(new it.cnr.contab.config00.bulk.Configurazione_cnrKey(chiave_primaria,"*","*",new Integer(0)));
		if (bulk == null) return null;
		return bulk.getVal04();
	} catch(it.cnr.jada.persistency.PersistencyException e) {
		throw handleException(e);
	}
}
}
