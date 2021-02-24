--------------------------------------------------------
--  DDL for Package CNRCTB545
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB545" AS
--==================================================================================================
--
-- CNRCTB545 - package di utilità per gestione COMPENSI
--
-- Date: 14/07/2006
-- Version: 2.7
--
-- Dependency: CNRCTB 015/100 IBMERR 001 IBMUTL 001
--
-- History:
--
-- Date: 20/06/2002
-- Version: 1.0
--
-- Creazione package
--
-- Date: 27/06/2002
-- Version: 1.1
--
-- Adeguamento del package alle modifiche della tabella COMPENSO
--
-- Date: 02/07/2002
-- Version: 1.2
--
-- Adeguamento del package alle modifiche della tabella COMPENSO
-- Ammontari netto detrazioni + legame per contabilizzazione compenso negativo
--
-- Date: 15/07/2002
-- Version: 1.3
--
-- Introdotte routine di gestione conguaglio
--
-- Date: 16/07/2002
-- Version: 1.4
--
-- Aggiunta alla tabella insiemeCoriTab il campo che memorizza la data di inizio validità del cori
-- in elaborazione; soluzione errore in valorizzazione della FK CONTRIBUTO_RITENUTA01
--
-- Date: 17/07/2002
-- Version: 1.5
--
-- Inserita routine di inserimento compenso.
-- Inserite matrici e routine per calcolo intervllo rate per conguaglio
--
-- Date: 18/07/2002
-- Version: 1.6
-- Aggiornamento della documentazione
--
-- Date: 22/07/2002
-- Version: 1.7
--
-- Inserite costanti per l'identificazione della tipologia dei diversi codice contributo_ritenuta
-- Inserite routine per identificazione dell'irpef a scaglioni e di addizionali territorio
-- (regione, provincia e comune).
-- Inserita matrice per la memorizzazione dei carichi familiari (numero familiari e dettaglio)
--
-- Date: 24/07/2002
-- Version: 1.8
--
-- Fix routine di calcolo dell'imponibile base cori per le nuove previsioni di classificazione in tipo
-- contributo ritenuta.
--
-- Date: 01/08/2002
-- Version: 1.9
--
-- Aggiornamento documentazione.
--
-- Date: 07/08/2002
-- Version: 1.10
--
-- Aggiunta routine per definizione dell'origine di un compenso
--
-- Date: 29/08/2002
-- Version: 1.11
--
-- Aggiunta routine inserimento ASS_COMPENSO_CONGUAGLIO
--
-- Date: 02/09/2002
-- Version: 1.12
--
-- Modificata la routine di recupero dello scaglione per errore in caso di configurazione sia ente
-- che percipiente.
--
-- Date: 05/09/2002
-- Version: 1.13
--
-- Aggiunta costante per descrivere i compensi che sono stati inclusi in un conguaglio ed aggiornata
-- la routine di determinazione del tipo origine compenso
--
-- Date: 13/09/2002
-- Version: 1.14
--
-- Aggiunta costante costante per identificare il cori di tipo RIVALSA
--
-- Date: 18/09/2002
-- Version: 1.15
--
-- Inserita routine per ottenere il tipo contributo ritenuta da un record di CONTRIBUTO_RITENUTA
--
-- Date: 24/09/2002
-- Version: 1.16
--
-- Modifica della routine di inserimento di un compenso per la gestione dei nuovi campi in tabella
-- ti_istituz_commerc, imponibile_inail, esercizio_fattura_fornitore, dt_fattura_fornitore,
-- nr_fattura_fornitore, fl_generata_fattura
--
-- Date: 26/09/2002
-- Version: 1.17
--
-- Modificata matrice insiemeCoriRec inserendo la mappatura di CONTRIBUTO_RITENUTA.montante per
-- memorizzare il valore di imponibile effettivamente utilizzato nella riga CORI.
-- Inserimento routine di lettura compenso da missione e di cancellazione del compenso
--
-- Date: 30/09/2002
-- Version: 1.18
-- Introdotte routine per controllo ed inserimento fattura da compenso
--
-- Date: 30/09/2002
-- Version: 1.19
--
-- Inserito il controllo di non recuperare i cloni quando si cerca il compenso da missione
--
-- Date: 02/10/2002
-- Version: 1.20
-- Fix errore in cancellazione compenso con generazione fattura (chiave duplicata)
--
-- Date: 02/16/2002
-- Version: 1.21
-- FL_COMPENSO_STIPENDI, FL_COMPENSO_CONGUAGLIO nella funzione copiaCompenso e insCompenso
--
-- Date: 17/10/2002
-- Version: 1.22
--
-- Adeguamento procedura alla nuova struttura della tabella FATTURA_PASSIVA
--
-- Date: 11/11/2002
-- Version: 1.23
--
-- Fix errore 347. Mancata segnalazione dell'assenza  della giusta entrata nella tabella scaglione.
-- Il sistema, al momento del calcolo del compenso, se non trova la giusta entrata nella tabella
-- scaglione non segnala l'errore e procede saltando quel Contributo/ritenuta.
--
-- Date: 18/11/2002
-- Version: 1.24
--
-- Adeguamento procedura alla nuova struttura della tabella COMPENSO. Aggiunti i seguenti attributi:
-- fl_compenso_minicarriera, aliquota_irpef_da_missione.
-- Modificate procedure copiaCompenso, insCompenso e getTipoOrigineCompenso
--
-- Date: 26/11/2002
-- Version: 1.25
--
-- Inserimento di tutti i campi in CONTRIBUTO_RITENUTA -> procedura inscontributoritenuta
--
-- Date: 03/12/2002
-- Version: 1.26
--
-- Fix errore interno 2948. In eliminazione compenso da missione entrava nella routine di annullamento
-- compenso da minicarriera.
-- L'errore si verifica anche in altri casi quando il compenso è stato incluso in un conguaglio, la
-- routine getTipoOrigineCompenso tornava un valore errato in questi casi.
--
-- Date: 12/12/2002
-- Version: 1.27
--
-- Se cancello logicamente un compenso da missione inizializzo i campi della missione
-- contenuti nella tabella COMPENSO
--
-- Date: 23/12/2002
-- Version: 1.28
--
-- Adeguamento procedura alla nuova struttura della tabella COMPENSO. Aggiunti i seguenti attributi:
-- FL_COMPENSO_MCARRIERA_TASSEP, ALIQUOTA_IRPEF_TASSEP
-- Modificate procedure copiaCompenso, insCompenso
--
-- Date: 16/01/2003
-- Version: 1.29
--
-- Adeguamento procedura alla nuova struttura della tabella COMPENSO e CONTRIBUTO_RITENUTA per
-- finanziaria 2003. Aggiunti i seguenti attributi:
-- COMPENSO            --> im_deduzione_irpef, imponibile_fiscale_netto
-- CONTRIBUTO_RITENUTA --> imponibile_lordo, im_deduzione_irpef
-- Modificate procedure copiaCompenso, insCompenso, insContributoRitenuta
--
-- Date: 17/01/2003
-- Version: 1.30
--
-- Modificata matrice insiemeCoriRec inserendo la mappatura di CONTRIBUTO_RITENUTA.imponibile_lordo e
-- CONTRIBUTO_RITENUTA.im_deduzione_irpef per adeguamento a finanziaria 2003
--
-- Date: 22/01/2003
-- Version: 1.31
--
-- Modifiche per gestione finanziaria 2003
-- Modifica alla matrice montantiRec per adeguamento alle modifiche della tabella MONTANTI, gestione
-- del montante lordo e netto per IRPEF e della deduzione
--
-- Date: 27/01/2003
-- Version: 1.32
--
-- Modifiche per gestione finanziaria 2003
-- Inserite routine per la lettura della configurazione base della deduzione IRPEF
-- Aggiunto attributo al compenso COMPENSO.numero_giorni
-- Modificate procedure copiaCompenso, insCompenso
--
-- Date: 20/03/2003
-- Version: 1.33
--
-- Richiesta CINECA n. 545, 546, 547
-- Mapping nuovi attributi (fl_escludi_qvaria_deduzione, fl_intera_qfissa_deduzione,
-- im_detrazione_personale_anag) in tabella COMPENSO
-- Modificate procedure copiaCompenso, insCompenso
--
-- Date: 11/04/2003
-- Version: 1.34
--
-- Fix errore CINECA n. 560. Attivata gestione quota esente IVA. Si applica solo in presenta di
-- CORI IVA
-- Modificato metodo getImponibileBaseCori
--
-- Date: 16/04/2003
-- Version: 1.35
--
-- Richiesta CINECA n. 562. Implementazione della cancellazione del conguaglio.
--
-- Date: 09/07/2003
-- Version: 1.36
--
-- Richiesta CINECA n. ???. Attivazione gestione recupero rate.
-- Mapping nuovo attributo fl_recupero_rate in tabella COMPENSO
-- Modificate procedure copiaCompenso, insCompenso
--
-- Date: 10/07/2003
-- Version: 1.37
--
-- Richiesta CINECA n. ???. Attivazione gestione recupero rate.
-- Modifica routine di memorizzazione della matrice date per introdurre l'indicatore di non eseguire
-- la prima fase dello schiacciamento di sovrapposizione periodi.
--
-- Date: 18/07/2003
-- Version: 1.38
--
-- Adeguamento package per nuovi attributi su FATTURA_PASSIVA (fl_congelata).
--
-- Date: 28/10/2003
-- Version: 2.0
--
-- Rilascio richiesta CINECA n. 655. Gestione conguaglio per aliquota massima.
--
-- Date: 04/11/2003
-- Version: 2.1
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio.
-- Inserimento di un attributo per memorizzare l'indicazione di compenso calcolato con aliquota
-- massima in anagrafica. (allineamento base dati)
--
-- Date: 11/11/2003
-- Version: 2.2
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio.
-- (Attivazione dell'accantonamento delle addizionali territorio).
--
-- Date: 24/11/2003
-- Version: 2.3
--
-- Fix errore interno in calcola accantonamento addizionali territorio
--
-- Date: 09/12/2003
-- Version: 2.4
--
-- Rilascio richiesta CINECA n. 471. Gestione rateizzazione addizionali territorio (seconda fase =
-- addebito della rata in esercizio successivo).
--
-- Date: 06/02/2004
-- Version: 2.5
--
-- Rilascio richiesta CINECA n. 750. Inserimento costanti per CUD.
--
-- Date: 02/11/2004
-- Version: 2.6
--
-- Rilascio errore CINECA n. 850. errore in esegui calcolo di compenso da minicarriera di un terzo
-- avente anche una minicarriera con trattamento cessato.
--
-- Date: 14/07/2006
-- Version: 2.7
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Date: 01/08/2006
-- Version: 2.8
--
-- Aggiunte le function getIsCoriPrevid e getIsCoriInail per la "Gestione dei Cervelli"
--
-- Date: 22/02/2007
-- Version: 2.9
--
-- Addizionali Regionali: Aggiunta la function "getIsRegConAliqMax" per recuperare le regioni
-- che hanno la gestione dell'aliquota massima per l'intero importo
--
-- Date: 10/05/2007
-- Version: 2.10
--
-- Addizionali Comunali: Aggiunta la function "getIsComConAliqMax" per recuperare i comuni
-- che hanno la gestione dell'aliquota massima per l'intero importo
--
-- Date: 24/01/2008
-- Version: 2.11
--
-- Addizionali Comunali: Aggiunta la function "getIsAddComunale" per verificare che il cd_classificazione sia per l'addizionale comunale
--
-- Date: 25/05/2014
-- Version: 2.12
-- Adeguamenti relativi al Bonus DL 66/2014
-- Inserita FUNCTION IsCoriCreditoIrpef
--==================================================================================================
--
-- Constants
--

   -- Configurazione deduzione IRPEF

   isChiavePrimaria CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'BASE_DEDUZIONE_IRPEF';
   isChiaveSecondariaBase CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'VALORE_BASE';
   isChiaveSecondariaQuota CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'QUOTE_DEDUZIONE';

   -- Configurazione deduzione FAMILY
   -- CSD sta per ChiaveSecondariaDeduzione

   isChiavePrimariaFamily CONSTANT CONFIGURAZIONE_CNR.cd_chiave_primaria%TYPE := 'BASE_DEDUZIONE_FAMILY';
   isCSDFamilyBase CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'VALORE_BASE';
   isCSDFamilyConiuge CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'CONIUGE';
   isCSDFamilyFiglio CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'FIGLIO';
   isCSDFamilyAltro CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'ALTRO';
   isCSDFamilyFiglioMenoTre CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'FIGLIO_MENO_TRE';
   isCSDFamilyFiglioSenzaConiuge CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'FIGLIO_SENZA_CONIUGE';
   isCSDFamilyFiglioHandicap CONSTANT CONFIGURAZIONE_CNR.cd_chiave_secondaria%TYPE := 'FIGLIO_HANDICAP';

   -- Tipizzazione di codici contributo ritenuta

   isCoriFiscale CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'FI';
   isCoriAddReg CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'R0';
   isCoriAddPro CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'P0';
   isCoriAddCom CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'C0';
   isCoriAddRegRecRate CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'R9';
   isCoriAddProRecRate CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'P9';
   isCoriAddComRecRate CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'C9';
   isCoriAddComAcconto CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'C1';
   isCoriIrap CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'IP';
   isCoriInail CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'IL';
   isCoriIva CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'IV';
   isCoriRivalsa CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'RV';
   isCoriPrevid CONSTANT TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE := 'PR';


   -- Tipizzazione dei compensi per origine

   isCompensoNormale CONSTANT INTEGER := 1;
   isCompensoNormaleAssCong CONSTANT INTEGER := 11;
   isCompensoSenzaCalcoli CONSTANT INTEGER := 2;
   isCompensoSenzaCalcoliAssCong CONSTANT INTEGER := 21;
   isCompensoConguaglio CONSTANT INTEGER := 3;
   isCompensoMissione CONSTANT INTEGER := 4;
   isCompensoMissioneAssCong CONSTANT INTEGER := 41;
   isCompensoMinicarriera CONSTANT INTEGER := 5;
   isCompensoMinicarrieraAssCong CONSTANT INTEGER := 51;

   isCancellazioneLogica CONSTANT INTEGER := 1;
   isCancellazioneFisica CONSTANT INTEGER := 2;

   -- Variabili globali

   i BINARY_INTEGER;

   -- Dichiarazione di un cursore generico

   TYPE GenericCurTyp IS REF CURSOR;

   -- Tabelle PL/SQL

   -- Matrice di appoggio per calcolato contributi e ritenute

   TYPE insiemeCoriRec IS RECORD
       (
        tCdCori CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE,
        tDtIniValCori TIPO_CONTRIBUTO_RITENUTA.dt_ini_validita%TYPE,
        tTiCassaCompetenza TIPO_CONTRIBUTO_RITENUTA.ti_cassa_competenza%TYPE,
        tPrecisione TIPO_CONTRIBUTO_RITENUTA.precisione%TYPE,
        tPgClassificazioneMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
        tCdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
        tFlScriviMontanti TIPO_CONTRIBUTO_RITENUTA.fl_scrivi_montanti%TYPE,
        tIdRiga TRATTAMENTO_CORI.id_riga%TYPE,
        tSegno TRATTAMENTO_CORI.segno%TYPE,
        tCalcoloImponibile TRATTAMENTO_CORI.calcolo_imponibile%TYPE,
        tFlSospensioneIrpef TIPO_CONTRIBUTO_RITENUTA.fl_sospensione_irpef%TYPE,
        tFlCreditoIrpef TIPO_CONTRIBUTO_RITENUTA.fl_credito_irpef%TYPE,
        tMontante CONTRIBUTO_RITENUTA.montante%TYPE,
        tImponibileLordo CONTRIBUTO_RITENUTA.imponibile_lordo%TYPE,
        tImDeduzioneIrpef CONTRIBUTO_RITENUTA.im_deduzione_irpef%TYPE,
        tImDeduzioneFamily CONTRIBUTO_RITENUTA.im_deduzione_family%TYPE,
        tImponibileNetto CONTRIBUTO_RITENUTA.imponibile%TYPE,
        tAliquotaEnte CONTRIBUTO_RITENUTA.aliquota%TYPE,
        tBaseCalcoloEnte CONTRIBUTO_RITENUTA.base_calcolo%TYPE,
        tAmmontareEnteLordo CONTRIBUTO_RITENUTA.ammontare_lordo%TYPE,
        tAmmontareEnte CONTRIBUTO_RITENUTA.ammontare%TYPE,
        tAliquotaPercip CONTRIBUTO_RITENUTA.aliquota%TYPE,
        tBaseCalcoloPercip CONTRIBUTO_RITENUTA.base_calcolo%TYPE,
        tAmmontarePercipLordo CONTRIBUTO_RITENUTA.ammontare_lordo%TYPE,
        tAmmontarePercip CONTRIBUTO_RITENUTA.ammontare%TYPE
       );
   TYPE insiemeCoriTab IS TABLE OF insiemeCoriRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio per montanti

   TYPE montantiRec IS RECORD
       (
        tNomeDip VARCHAR2(100),
        tValoreLordoDip NUMBER(15,2),
        tValoreNettoDip NUMBER(15,2),
        tImDeduzioneDip NUMBER(15,2),
        tNomeAltro VARCHAR2(100),
        tValoreLordoAltro NUMBER(15,2),
        tValoreNettoAltro NUMBER(15,2),
        tValoreLordoOcca NUMBER(15,2) Default 0,
        tValoreNettoOcca NUMBER(15,2) Default 0,
        tImDeduzioneAltro NUMBER(15,2),
        tImDeduzioneFamilyAltro NUMBER(15,2) Default 0
       );
   TYPE montantiTab IS TABLE OF montantiRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio per carichi familiari (numero occorrenze)

   TYPE numCaricoFamRec IS RECORD
       (
        tDataDa DATE:=NULL,
        tDataA DATE:=NULL,
        tNAltro INTEGER:=NULL,
        tNConiuge INTEGER:=NULL,
        tNFiglio INTEGER:=NULL
       );
   TYPE numCaricoFamTab IS TABLE OF numCaricoFamRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio per carichi familiari (dettaglio mormalizzato in date dei singoli carichi)

   TYPE caricoFamRec IS RECORD
       (
        tTiPersona CARICO_FAMILIARE_ANAG.ti_persona%TYPE:=NULL,
        tCodiceFiscale CARICO_FAMILIARE_ANAG.codice_fiscale%TYPE:=NULL,
        tDataDa CARICO_FAMILIARE_ANAG.dt_ini_validita%TYPE:=NULL,
        tDataA CARICO_FAMILIARE_ANAG.dt_fin_validita%TYPE:=NULL,
        tPrcCarico CARICO_FAMILIARE_ANAG.prc_carico%TYPE:=NULL,
        tFlPrimoFiglio CARICO_FAMILIARE_ANAG.fl_primo_figlio%TYPE:=NULL,
        tFlHandicap CARICO_FAMILIARE_ANAG.fl_handicap%TYPE:=NULL,
        tDtMinoreTre CARICO_FAMILIARE_ANAG.dt_fine_figlio_ha_treanni%TYPE:=NULL,
        tFlPrimoFiglioMancaCon CARICO_FAMILIARE_ANAG.fl_primo_figlio_manca_con%TYPE:=NULL,
        tNumeroMesi INTEGER:=NULL
       );
   TYPE caricoFamTab IS TABLE OF caricoFamRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio per normalizzazione intervallo date

   TYPE intervalloDateRec IS RECORD
       (
        tDataDa DATE:=NULL,
        tDataA DATE:=NULL
       );
   TYPE intervalloDateTab IS TABLE OF IntervalloDateRec
        INDEX BY BINARY_INTEGER;

   -- Matrice di appoggio per calcolo dei mesi

   TYPE intervalloMesiRec IS RECORD
       (
        tMesi NUMBER(2):=Null
       );
   TYPE intervalloMesiTab IS TABLE OF IntervalloMesiRec
        INDEX BY BINARY_INTEGER;
--
-- Functions e Procedures
--

-- Ritorna un record della tabella COMPENSO

   FUNCTION getCompenso
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE,
       eseguiLock CHAR
      ) RETURN COMPENSO%ROWTYPE;

-- Ritorna un record della tabella COMPENSO che risulta collegatom a missione

   FUNCTION getCompensoDaMissione
      (
       aCdCds MISSIONE.cd_cds%TYPE,
       aCdUnitaOrganizzativa MISSIONE.cd_unita_organizzativa%TYPE,
       aEsercizio MISSIONE.esercizio%TYPE,
       aPgMissione MISSIONE.pg_missione%TYPE,
       eseguiLock CHAR
      ) RETURN COMPENSO%ROWTYPE;

-- Ritorna un record della tabella TIPO_TRATTAMENTO

   FUNCTION getTipoTrattamento
      (
       aCdTrattamento VARCHAR2,
       aDataRif DATE
      ) RETURN TIPO_TRATTAMENTO%ROWTYPE;

   FUNCTION getTipoTrattamento
      (
       aCdTrattamento VARCHAR2,
       aDataRif DATE,
       aGestioneChiuso CHAR
      ) RETURN TIPO_TRATTAMENTO%ROWTYPE;

-- Ritorna un record della tabella SCAGLIONE

   FUNCTION getScaglione
      (
       aCdContributoRitenuta SCAGLIONE.cd_contributo_ritenuta%TYPE,
       aTiAnagrafico SCAGLIONE.ti_anagrafico%TYPE,
       aDataRif DATE,
       aImportoRif IN OUT SCAGLIONE.im_inferiore%TYPE,
       aAliquota IN OUT SCAGLIONE.aliquota%TYPE,
       aCdRegione SCAGLIONE.cd_regione%TYPE,
       aCdProvincia SCAGLIONE.cd_provincia%TYPE,
       aPgComune SCAGLIONE.pg_comune%TYPE
      ) RETURN V_PRE_SCAGLIONE%ROWTYPE;

-- Ritorna 'Y' o 'N' a seconda che esista o meno il compenso

   FUNCTION chkEsisteCompenso
      (
       aCdCds VARCHAR2,
       aCdUnitaOrganizzativa VARCHAR2,
       aEsercizio NUMBER,
       aPgCompenso NUMBER
      ) RETURN CHAR;

-- Ritorna l'imponibile base di un tipo contributo ritenuta

   FUNCTION getImponibileBaseCori
      (
       aRecCompenso COMPENSO%ROWTYPE,
       cdClassificazioneCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN NUMBER;

-- Elimina CONTRIBUTO_RITENUTA e CONTRIBUTO_RITENUTA_DET

   PROCEDURE cancellaDetCompenso
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE
      );

-- Elimina un intero COMPENSO

   PROCEDURE eliminaFisicoCompenso
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE
      );

-- Elimina logicamente un intero COMPENSO

   PROCEDURE eliminaLogicoCompenso
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUnitaOrganizzativa COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE,
       aUtente COMPENSO.utuv%TYPE,
       aTiOrigineElimina CHAR
      );

-- Inserimento in COMPENSO

   PROCEDURE insCompenso
      (
       aRecCompenso COMPENSO%ROWTYPE
      );

-- Inserimento in CONTRIBUTO_RITENUTA

   PROCEDURE insContributoRitenuta
      (
       aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE
      );

-- Inserimento in CONTRIBUTO_RITENUTA_DET

   PROCEDURE insContributoRitenutaDet
      (
       aRecContributoRitenutaDet CONTRIBUTO_RITENUTA_DET%ROWTYPE
      );

-- Copia l'intera struttura dati di un compenso (utilizzato in modifica COMPENSO)

   PROCEDURE copiaCompenso
      (
       aCdCds VARCHAR2,
       aCdUnitaOrganizzativa VARCHAR2,
       aEsercizio NUMBER,
       aPgCompenso NUMBER,
       aCdCdsCopia VARCHAR2,
       aCdUnitaOrganizzativaCopia VARCHAR2,
       aEsercizioCopia NUMBER,
       aPgCompensoCopia NUMBER
      );

-- Ritorna un record della tabella CONGUAGLIO

   FUNCTION getConguaglio
      (
       aCdCds CONGUAGLIO.cd_cds%TYPE,
       aCdUnitaOrganizzativa CONGUAGLIO.cd_unita_organizzativa%TYPE,
       aEsercizio CONGUAGLIO.esercizio%TYPE,
       aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       eseguiLock CHAR
      ) RETURN CONGUAGLIO%ROWTYPE;

-- Inserimento in ASS_COMPENSO_CONGUAGLIO

   PROCEDURE insAssCompensoConguaglio
      (
       aRecAssCompensoConguaglio ASS_COMPENSO_CONGUAGLIO%ROWTYPE
      );

-- Costruzione matrice date

   PROCEDURE componiMatriceDate
      (
       aIntervalloDate IN OUT intervalloDateTab,
       aDataDa DATE,
       aDataA  DATE,
       aTipoData CHAR,
       flNoSchiacciaSovrapposti CHAR
      );

-- Ritorna il numero di giorni presenti in una matrice date

   FUNCTION getGiorniMatriceDate
      (
       aIntervalloDate intervalloDateTab
      ) RETURN INTEGER;

-- Ritorna la massima data presente in una matrice date
   FUNCTION getMassimaMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN date;
-- Ritorna la minima data presente in una matrice date
   FUNCTION getMinimaMatriceDate
   (
    aIntervalloDate intervalloDateTab
   ) RETURN date;

-- Ritorna il numero di mesi presenti in una matrice date

   FUNCTION getMesiMatriceDate
      (
       aIntervalloDate intervalloDateTab
      ) RETURN INTEGER;

-- Ritorna il numero di mesi presenti in una matrice date in un dato Esercizio

   FUNCTION getMesiMatriceDateEsercizio
      (
       aIntervalloDate intervalloDateTab,
       aEsercizio INTEGER
      ) RETURN INTEGER;
-- Ritorna il numero di Mesi presenti in una matrice date in un dato Esercizio calcolandoli in base ai giorni

   FUNCTION getMesiMatriceDateEsForDays
      (
       aIntervalloDate intervalloDateTab,
       aEsercizio INTEGER
      ) RETURN INTEGER;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come irpef a scaglioni

   FUNCTION getIsIrpefScaglioni
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE,
       aPgClassifMontanti TIPO_CONTRIBUTO_RITENUTA.pg_classificazione_montanti%TYPE,
       aFlScriviMontanti TIPO_CONTRIBUTO_RITENUTA.fl_scrivi_montanti%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale territorio

   FUNCTION getIsAddTerritorio
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale territorio
-- per recupero rate

   FUNCTION getIsAddTerritorioRecRate
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale comunale acconto

   FUNCTION getIsAddTerritorioAcconto
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come previdenziale

   FUNCTION getIsCoriPrevid
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come assicurativo

   FUNCTION getIsCoriInail
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come Credito Irpef

   FUNCTION IsCoriCreditoIrpef
     (aCdCori TIPO_CONTRIBUTO_RITENUTA.cd_contributo_ritenuta%TYPE
     ) RETURN CHAR;

-- Ritorna un record della tabella IVA

   FUNCTION getVoceIva
      (
       aCdVoceIva VOCE_IVA.cd_voce_iva%TYPE
      ) RETURN VOCE_IVA%ROWTYPE;

-- Ritorna un record della tabella TIPOLOGIA_RISCHIO

   FUNCTION getTipologiaRischio
      (
       aCdTipologiaRischio TIPOLOGIA_RISCHIO.cd_tipologia_rischio%TYPE,
       aDataRif DATE
      ) RETURN TIPOLOGIA_RISCHIO%ROWTYPE;

-- Ritorna la tipologia di origine di un compenso

   FUNCTION getTipoOrigineCompenso
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUo COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE
      ) RETURN INTEGER;

-- Ritorna la clessificazione di un contributo ritenuta da una occorrenza di CONTRIBUTO_RITENUTA

   FUNCTION getTipoCoriDaRigaCompenso
      (
       aRecContributoRitenuta CONTRIBUTO_RITENUTA%ROWTYPE
      ) RETURN VARCHAR2;

-- Compone la generazione della fattura per un compenso sia in inserimento che in modifica

   PROCEDURE generaFatturaPassiva
      (
       aRecCompenso COMPENSO%ROWTYPE,
       aRecCompensoOri COMPENSO%ROWTYPE,
       aRecAnagrafico ANAGRAFICO%ROWTYPE
      );

-- Ritorna il record di CONTRIBUTO_RITENUTA (uno solo) che rappresenta IVA

   FUNCTION getCoriIva
      (
       aCdCds COMPENSO.cd_cds%TYPE,
       aCdUo COMPENSO.cd_unita_organizzativa%TYPE,
       aEsercizio COMPENSO.esercizio%TYPE,
       aPgCompenso COMPENSO.pg_compenso%TYPE
      ) RETURN CONTRIBUTO_RITENUTA%ROWTYPE;

-- Ritorna la configurazione base della gestione deduzione IRPEF

   PROCEDURE getConfigDeduzioneIrpef
      (
       aEsercizio NUMBER,
       aBaseDeduzione IN OUT NUMBER,
       aQuotaFissaDeduzione IN OUT NUMBER,
       aQuotaVariabileDeduzione IN OUT NUMBER
      );

-- Ritorna la configurazione base della gestione deduzione FAMILY

   PROCEDURE getConfigDeduzioneFamily
      (
       aEsercizio NUMBER,
       glbBaseDeduzioneFamily IN OUT NUMBER,
       glbDFamilyConiuge IN OUT NUMBER,
       glbDFamilyFiglio IN OUT NUMBER,
       glbDFamilyAltro IN OUT NUMBER,
       glbDFamilyFiglioMenoTre IN OUT NUMBER,
       glbDFamilyFiglioSenzaConiuge IN OUT NUMBER,
       glbDFamilyFiglioHandicap IN OUT NUMBER
      );

-- Ritorna il valore minimo e massimo dello scaglione IRPEF in caso di gestione per aliquota
-- massima in anagrafica

   PROCEDURE getValMinMaxSclAlqMaxIrpef
      (
       aDataRegistrazione DATE,
       aRecTipoTrattamento TIPO_TRATTAMENTO%ROWTYPE,
       aAliquotaFiscaleAnag NUMBER,
       aImportoscaglioneMin IN OUT NUMBER,
       aImportoscaglioneMax IN OUT NUMBER
      );

-- Ritorna il record di RATEIZZA_CLASSIFIC_CORI presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli

   FUNCTION getRateizzaClassificCori
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       isTemporaneo CHAR,
       aCdClassificazioneCori CHAR,
       eseguiLock CHAR
      ) RETURN RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

-- Ritorna il record di RATEIZZA_CLASSIFIC_CORI_S presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli

   FUNCTION getRateizzaClassificCoriStr
      (
       aEsercizio NUMBER,
       aCdsConguaglio CONGUAGLIO.cd_cds%TYPE,
       aCdUoConguaglio CONGUAGLIO.cd_unita_organizzativa%TYPE,
       aPgConguaglio CONGUAGLIO.pg_conguaglio%TYPE,
       aCdAnag NUMBER,
       isTemporaneo CHAR,
       aCdClassificazioneCori CHAR,
       eseguiLock CHAR
      )  RETURN RATEIZZA_CLASSIFIC_CORI%ROWTYPE;

-- Inserimento o aggiornamento di un record in RATEIZZA_CLASSIFIC_CORI da conguaglio

   PROCEDURE generaRateizzaClassificCori
      (
       aRecConguaglio CONGUAGLIO%ROWTYPE,
       aAmmontareDaRateizzare NUMBER,
       aRecRateizzaClassificCoriIn RATEIZZA_CLASSIFIC_CORI%ROWTYPE
      );

-- Inserimento di un record in RATEIZZA_CLASSIFIC_CORI

   PROCEDURE insRateizzaClassificCori
      (
       aRecRateizzaClassificCori RATEIZZA_CLASSIFIC_CORI%ROWTYPE,
       isStorico CHAR
      );

-- Ritorna l'indicazione se applicare o meno, per la regione in oggetto, l'aliquota massima
-- per l'intero importo

   FUNCTION getIsRegConAliqMax
      (aCdRegione REGIONE.cd_regione%TYPE
      ) RETURN CHAR;

-- Ritorna l'indicazione se applicare o meno, per il comune in oggetto, l'aliquota massima
-- per l'intero importo

   FUNCTION getIsComConAliqMax
      (aPgComune COMUNE.pg_comune%TYPE
      ) RETURN CHAR;

-- Ritorna il record di ACCONTO_CLASSIFIC_CORI presente nell'esercizio per l'anagrafico in calcolo di
-- compensi o conguagli

   FUNCTION getAccontoClassificCori
      (
       aEsercizio NUMBER,
       aCdAnag NUMBER,
       isTemporaneo CHAR,
       aCdClassificazioneCori CHAR,
       eseguiLock CHAR
      ) RETURN ACCONTO_CLASSIFIC_CORI%ROWTYPE;

-- Ritorna il record di ESENZIONI_ADDCOM relativo al comune di residenza e valido alla data odierna

   FUNCTION getEsenzioniAddcom
      (
       aPgComune NUMBER,
       aDataOdierna DATE
      ) RETURN ESENZIONI_ADDCOM%ROWTYPE;

-- Ritorna l'indicazione di un tipo contributo ritenuta classificato come addizionale comunale

   FUNCTION getIsAddComunale
      (aCdClassifCori TIPO_CONTRIBUTO_RITENUTA.cd_classificazione_cori%TYPE
      ) RETURN CHAR;
END CNRCTB545;
