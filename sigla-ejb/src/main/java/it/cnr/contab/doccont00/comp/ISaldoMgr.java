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

package it.cnr.contab.doccont00.comp;

import it.cnr.contab.prevent00.bulk.*;
import it.cnr.contab.config00.pdcfin.bulk.*;
import java.math.*;
import java.util.*;
import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ComponentException;
public interface ISaldoMgr
{

/** 
  *  aggiornamento importo relativo a mandati e reversali
  *    PreCondition:
  *      E' stata cancellato un mandato o creata/cancellata una reversale 
  *    PostCondition:
  *      Viene aggiornato l'importo associato a mandati e reversali della voce del piano (di competenza o residuo) interessata dal mandato o
  *      dalla reversale senza eseguire il controllo di disponibilità di cassa
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione mandato
  *    PreCondition:
  *      E' stata creato un nuovo mandato e viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  creazione mandato - errore
  *    PreCondition:
  *      E' stata creato un nuovo mandato e non viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaMandati)
  *    PostCondition:
  *      Viene segnalato con un errore l'impossibilità di emettere il mandato
  *  annullamento mandato
  *    PreCondition:
  *      E' stata annullato un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dal mandato
  *  creazione/annullamento reversale
  *    PreCondition:
  *      E' stata creata una nuova reversale o e' stata annullata una reversale già emessa 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a mandati e reversali 
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  * @param checkDisponibilitaCassa  valore booleano che indica se eseguire la verifica della disponibilità di cassa sulla
  *        voce del piano
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaMandatiReversali(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4,boolean param5) throws it.cnr.jada.comp.ComponentException;
/** 
  *  aggiornamento importo relativo a obbligazioni/accertamento
  *    PreCondition:
  *      E' stata creato/modificato importo/cancellato un accertamento oppure e' stata
  *      cancellata un'obbligazione o e' stato diminuito l'importo del dettaglio di una scadenza dell'obbligazione
  *    PostCondition:
  *      Viene aggiornato l'importo associato a obbligazioni/accertamenti della voce del piano (di competenza o residuo) 
  *      interessata dall'accertamento o dai dettagli delle scadenze dell'obbligazione
  *      senza eseguire il controllo di disponibilità di cassa
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3, String ti_competenza_residuo) throws it.cnr.jada.comp.ComponentException;
/** 
  *  creazione obbligazione/modifica importo obbligazione
  *    PreCondition:
  *      E' stato creato un nuovo dettaglio di scadenza di obbligazione o ne e' stato incrementato l'importo
  *      e viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaObbligazione)
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'obbligazione l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dal dettaglio della scadenza di obbligazione
  *  creazione obbligazione/modifica importo obbligazione - errore
  *    PreCondition:
  *      E' stato creato un nuovo dettaglio di scadenza di obbligazione o ne e' stato incrementato l'importo
  *      e non viene superato il controllo di
  *      di disponibilità di cassa (metodo checkDisponabilitaCassaObbligazione)
  *    PostCondition:
  *      Viene segnalato con un errore l'impossibilità di creare/aggiornare l'obbligazione
  *  eliminazione obbligazione
  *    PreCondition:
  *      E' stata eliminato un dettaglio di scadenza di obbligazione
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'obbligazione l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dal dettaglio della scadenza di obbligazione  
  *  creazione/modifica/eliminazione accertamento
  *    PreCondition:
  *      E' stata creato un nuovo accertamento oppure e' stato modificato l'importo di un accertamento oppure e' stato
  *      cancellato un accertamento
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza dell'accertamento l'importo associato a obbligazione e accertamenti 
  *      della voce del piano (di competenza o residuo) interessata dall'accertamento
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
  * @param checkDisponibilitaCassa  valore booleano che indica se eseguire la verifica della disponibilità di cassa sulla
  *        voce del piano
  * 
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaObbligazioniAccertamenti(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3, String ti_competenza_residuo, boolean param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  riscontro mandato
  *    PreCondition:
  *      E' stata riscontrato un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  riscontro reversale
  *    PreCondition:
  *      E' stata riscontrata una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *  annullamento riscontro mandato
  *    PreCondition:
  *      E' stata annullato il riscontro di un mandato 
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza del mandato l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dal mandato 
  *  annullamento riscontro reversale
  *    PreCondition:
  *      E' stato annullato il riscontro di una reversale
  *    PostCondition:
  *      Viene aggiornato per il cds di appartenenza della reversale l'importo associato a pagamenti/incassi
  *      della voce del piano (di competenza o residuo) interessata dalla reversale
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui aggiornare i saldi
  * @param cd_cds il codice del Cds per cui aggiornare i saldi  
  * @param importo l'importo (positivo o negativo) della modifica da apportare al saldo
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk aggiornaPagamentiIncassi(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3, String ti_competenza_residuo) throws it.cnr.jada.comp.ComponentException;
/** 
  *  verifica disponibilità di cassa - errore
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' inferiore all'importo del mandato che l'utente vuole emettere
  *    PostCondition:
  *      Una segnalazione di errore comunica il problema all'utente e non consente il salvataggio del mandato
  *  verifica disponibilità di cassa - ok
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza o residuo + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      dei mandati già emessi e' superiore o uguale all'importo del mandato che l'utente vuole emettere  
  *    PostCondition:
  *      Il mandato supera la validazione di Cassa ed e' pertanto possibile proseguire con il suo salvataggio
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param cd_cds il codice del Cds per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) per cui effettuare la verifica di disponibilità di cassa
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaMandati(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3,java.lang.String param4) throws it.cnr.jada.comp.ComponentException;
/** 
  *  verifica disponibilità di cassa - errore
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      delle obbligazioni già emesse e' inferiore all'importo dell'obbligazione che l'utente vuole emettere
  *    PostCondition:
  *      Una segnalazione di errore comunica il problema all'utente, lasciondogli comunque la possibilità di forzare questo
  *      controllo e di salvare l'obbligazione
  *  verifica disponibilità di cassa - ok
  *    PreCondition:
  *      La somma dello stanziamento iniziale della voce del piano di competenza + variazioni in positivo 
  *      - variazione in negativo - importo 
  *      delle obbligazioni già emesse e' superiore o uguale all'importo dell'obbligazione che l'utente vuole emettere
  *    PostCondition:
  *      L'obbligazione supera la validazione di Cassa ed e' pertanto possibile proseguire con il suo salvataggio
  *
  * @param userContext lo <code>UserContext</code> che ha generato la richiesta
  * @param voce <code>Voce_fBulk</code> la voce del piano per cui effettuare la verifica di disponibilità di cassa
  * @param cd_cds il codice del Cds per cui effettuare la verifica di disponibilità di cassa
  * @param importo l'importo (positivo o negativo) per cui effettuare la verifica di disponibilità di cassa
  * @param ti_competenza_residuo identifica il tipo di voce (di competenza o residuo) da aggiornare  
  *  
*/

public abstract it.cnr.contab.prevent00.bulk.Voce_f_saldi_cmpBulk checkDisponabilitaCassaObbligazioni(it.cnr.jada.UserContext param0,it.cnr.contab.config00.pdcfin.bulk.Voce_fBulk param1,java.lang.String param2,java.math.BigDecimal param3, String ti_competenza_residuo) throws it.cnr.jada.comp.ComponentException;
}
