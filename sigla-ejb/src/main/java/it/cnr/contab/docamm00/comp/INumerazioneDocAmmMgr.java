package it.cnr.contab.docamm00.comp;

import it.cnr.contab.docamm00.docs.bulk.Numerazione_doc_ammBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface INumerazioneDocAmmMgr
{


//^^@@
/** 
  *  tutti i controlli superati.
  *    PreCondition:
  *      Il progressivo è stato generato senza errori.
  *    PostCondition:
  *      Viene consentita la registrazione del progressivo.
  *  esistenza della tipologia della numerazione
  *    PreCondition:
  *      Non esiste la numerazione per il tipo di documento amministrativo
  *    PostCondition:
  *      Viene inserita una nuova numerazione per il tipo documento amministrativo, CDS, UO e esercizio correnti
  */
//^^@@

public abstract Long getNextPG (UserContext uAC,Numerazione_doc_ammBulk progressivo) throws ComponentException;
}
