--------------------------------------------------------
--  DDL for Package CNRCTB043
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB043" as
--
-- CNRCTB043 - Modifiche a pratiche finanziarie/amministrative su partita di giro
-- Date: 13/07/2006
-- Version: 2.5
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 20/02/2003
-- Version: 1.0
-- Creazione
--
-- Date: 21/02/2003
-- Version: 1.1
-- Fix errore recupero doc generico
--
-- Date: 24/02/2003
-- Version: 1.2
-- Fix su modifica importo reveresale provvisoria
--
-- Date: 25/02/2003
-- Version: 1.3
-- Gestione dell'aggiornamento di partita di giro TRONCA
-- Se l'obbligazione di contropartita dell'accertamento ט cancellata, non viene aggiornata del delta
-- Controllo che l'accertamento che ha aperto la partita di giro non sia cancellato
--
-- Date: 27/02/2003
-- Version: 1.4
-- Fix su recupero obbligazione su partita di giro -> tolta la condizione che tale obbligazione NON SIA CANCELLATA
--
-- Date: 28/02/2003
-- Version: 1.5
-- Aggiornamento importo riga divisa
--
-- Date: 04/03/2003
-- Version: 1.6
-- Fix su aggiornamento doc generico importi -> tolto uso package 100 per problemi di formattazione degli importi
--
-- Date: 18/03/2003
-- Version: 1.7
-- Troncamento delle pratiche su partita di giro
--
-- Date: 15/05/2003
-- Version: 1.8
-- Fix su aggiornamento importo accertamento pratica gruppo centro
--
-- Date: 15/05/2003
-- Version: 2.0
-- Revisione liquidazione CORI per gestione in chiusura
--
-- Date: 12/06/2003
-- Version: 2.1
-- Modifica metodi per cambio interfaccia aggiornaSaldoDettScad
--
-- Date: 03/07/2003
-- Version: 2.2
-- ================== allinemento con corr.ver. 1.8.0
-- Fix errore non ann. accert. su troncamento partita di giro aperta da spesa
-- Impostata come data cancellazione quella di creazione del doc. che ha aperto la partita di giro
--
-- Date: 06/11/2003
-- Version: 2.3
-- Eliminato il cointrollo di obbligazione non cancellata su modificaPraticaObb
-- per fix errore che impediva le liquidazioni IVA su piש U.O.
--
-- Date: 19/04/2006
-- Version: 2.4
-- Aggiunto nella procedura modificaPraticaObb il parametro "aAccConScad" che per default ט 'N' e solo se chiamato
-- dalla liquidazione CORI ט 'Y' per indicare che occorre gestite le scadenze per le pgiro di entrata per consentire
-- la chiusura di una Reversale Provvisoria legata a gruppi CORI accentrati ancora aperti.
-- In questo caso, cioט se il gruppo ט aperto ma la Rev. Provv. ט chiusa, viene creata una nuova scadenza
-- per la pgiro di entrata ed una nuova rev. provv. a cui agganciarla
--
-- Date: 26/09/2005
-- Version: 2.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants:

-- Functions e Procedures:

-- SOLO PER DOCUMENTI IN EURO CREATI AUTOMATICAMENTE PER LIQUIDAZIONI CORI/IVA

 procedure modificaPraticaObb(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aImDelta number,aTSNow date,aUser VARCHAR2, aAccConScad CHAR Default 'N', aggiornaDocGenerico CHAR Default 'S');
 procedure modificaPraticaAcc(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aImDelta number,aTSNow date,aUser varchar2);

-- Tronca una pratica su partita di giro CDS aperta da obbligazione

 procedure troncaPraticaObbPgiro(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aTSNow date,aUser varchar2);

-- Tronca una pratica su partita di giro CDS aperta da obbligazione ma ribaltata e quindi con origine E (Liquidazione cori da 999)

 procedure troncaPraticaObbPgiroInv(aEs number,aCdCds varchar2,aEsOri number,aPgObb number,aTSNow date,aUser varchar2);

-- Tronca una pratica su partita di giro CDS aperta da accertamento

 procedure troncaPraticaAccPgiro(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aTSNow date,aUser varchar2);

 -- Tronca una pratica su partita di giro CDS aperta da accertamento ma ribaltata e quindi con origine S (Liquidazione cori da 999)

 procedure troncaPraticaAccPgiroInv(aEs number,aCdCds varchar2,aEsOri number,aPgAcc number,aTSNow date,aUser varchar2);

end;
