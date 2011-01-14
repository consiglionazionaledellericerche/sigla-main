package it.cnr.contab.docamm00.comp;
import it.cnr.contab.docamm00.tabrif.bulk.Voce_ivaBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface ISezionaleMgr
{

    /** 
  *  normale
  *    PreCondition:
  *      Viene richiesta una possibile operazione di creazione.
  *    PostCondition:
  *      L'OggettoBulk viene inizializzato con tutti gli oggetti collegati e preparato per una operazione di creazione.
 */

public abstract it.cnr.jada.bulk.OggettoBulk inizializzaBulkPerInserimento(it.cnr.jada.UserContext param0,it.cnr.jada.bulk.OggettoBulk param1) throws it.cnr.jada.comp.ComponentException;
}