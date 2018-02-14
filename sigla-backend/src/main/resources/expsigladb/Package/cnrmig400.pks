CREATE OR REPLACE PACKAGE CNRMIG400 AS
-- =================================================================================================
--
-- CNRCTB400 - Package per la migrazione dell'inventario
--
-- Date: 16/11/2004
-- Version: 1.3
--
-- Dependency: CNRCTB015/020/100/400/800 - IBMERR001 - IBMUTL200
--
----------------------------------------------------------------------------------------------------
-- History:
--
-- Date: 02/11/2004
-- Version: 1.0
--
-- Creazione
--
-- Date: 08/11/2004
-- Version: 1.1
--
-- Modifica chiamata per ottenere il codice dell'Ubicazione di default
--
-- Date: 09/11/2004
-- Version: 1.2
--
-- Sistemazione con dati CIR e pre-post conditions
-- Fix errore su cursore invalido
--
-- Date: 16/11/2004
-- Version: 1.3
--
-- Inserito controllo inventario aperto
--

-- 06/09/2005 Inserita procedura di inserimento in Inventario_beni per la gestione di fl_migrato
-- =================================================================================================
--
-- Constants:
--

   FASE_UNO CONSTANT VARCHAR2(50) :='MIGRAZIONE INVENTARIO - FASE BASE';
   FASE_DUE CONSTANT VARCHAR2(50) :='MIGRAZIONE INVENTARIO - CARICAMENTO IN CIR';
   SEMAFORO CONSTANT VARCHAR2(20) :='MIGRA_BENI00';
   CONFIG_CNR_KEY1 VARCHAR2(50) := 'LINEA_ATTIVITA_SPECIALE';
   CONFIG_CNR_KEY2 VARCHAR2(100) := 'LINEA_COMUNE_MIGRAZIONE_BENI';
   STATO_INIZIALE CHAR(1) := 'I';
   STATO_MIGRATO CHAR(1) := 'M';
   STATO_ERRORE CHAR(1) := 'E';
   CONDIZIONE_BENE_DEFAULT VARCHAR2(10) := '1';

-- Variabili globali

   dataOdierna DATE;
   aEsercizioBase NUMBER(4);
   aUtente VARCHAR2(20):= '$$$$$MIGRAZIONE$$$$$';

   TYPE beniDaMigrareOriList
        IS TABLE OF CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE
   INDEX BY BINARY_INTEGER;
   beniDaMigrareOri_tab beniDaMigrareOriList;

   TYPE beniDaMigrareList
        IS TABLE OF CNR_INVENTARIO_BENI_MIG%ROWTYPE
   INDEX BY BINARY_INTEGER;
   beniDaMigrare_tab beniDaMigrareList;

   TYPE GenericCurTyp IS REF CURSOR;

--
-- Functions e Procedures:
--
-- La procedure rappresenta la prima fase del processo di migrazione beni. I beni da migrare sono portati dalla tabella
-- compilata a cura CNR (CNR_INVENTARIO_BENI_MIG_ORI) alle tabelle base della migrazione CNR_INVENTARIO_BENI_MIG,
-- CNR_INVENTARIO_BENI_MIG_DETT e CNR_INVENTARIO_BENI_MIG_SCARTO.
--
-- Pre-post name: Acquisizione del semaforo applicativo
-- Pre:           E' in corso una migrazione beni
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo log
-- Pre:           Non ? possibile recuperare il progressivo identificativo del log (testata e dettaglio)
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo di caricamento per CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT.
-- Pre:           Non ? possibile recuperare il progressivo di caricamento
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Esecuzione della prima fase della migrazione beni
-- Pre:           Nessuna delle precondizioni precedenti ? verificata
-- Post:   Vengono letti tutti i beni da caricare presenti in CNR_INVENTARIO_BENI_MIG_ORI ordinati per id_bene_origine
--         e ti_movimento (l'eventuale carico ? il primo record).
--         Per ogni blocco di record omogenei per id_bene_origine si processa la migrazione in CNR_INVENTARIO_BENI_MIG e
--         CNR_INVENTARIO_BENI_MIG_DETT. Gli eventuali errori trovati non bloccano la procedura di migrazione.
--         Si eseguono i seguenti controlli e azioni (con generazione errore).
--            Controllo che la matrice beni sia piena
--            Controllo se il primo record riguarda un carico o una variazione di valore.
--            Recupero il record bene con parametro id_bene_origine. Se non esiste carico allora deve esistere
--            il bene altrimenti il bene non deve esistere
--            Recupero dell'esercizio.
--               Se non esiste il carico
--                  Si recupera il valore di INVENTARIO_BENI.esercizio_carico_bene
--               altrimenti si usa l'attributo CNR_INVENTARIO_BENI_MIG_ORI.esercizio_coep_bene
--            Recupero degli identificativi del Cds e UO di CIR dai riferimenti SCI
--            Recupero l'identificativo dell'inventario (pg_inventario)
--            Si compone il record di CNR_INVENTARIO_BENI_MIG definendo i valori in modo differente a seconda che
--            nel blocco sia presente o meno un carico.
--            Se non ? presente un carico
--               alcuni valori sono presi da INVENTARIO_BENI
--            altrimenti si verificano i dati in input da CNR_INVENTARIO_BENI_MIG_ORI:
--               La descrizione del bene
--               La categoria gruppo deve esistere, deve essere per gestione inventario e deve essere un gruppo e
--               non una categoria
--               Il flag ammortamento.
--                  Se ? NULL
--                     Si valorizza con quanto presente nella categoria gruppo.
--                  altrimenti
--                     Deve essere consistente con la categoria gruppo, Il bene non pu? essere definito come
--                     ammortizzabile in una categoria gruppo che non ? ammortizzabile
--               Il tipo ammortamento (pu? essere NULL).
--                  Se il flag ammortamento ? falso
--                     Non pu? essere valorizzato il tipo ammortamento
--                  altrimenti
--                     Si verifica che il tipo ammortamento indicato sia tra quelli previsti nell'esercizio per
--                     la categoria gruppo di riferimento
--               Ubicazione. Si prende quella di default
--               Si compone l'etichetta del bene
--               Si valorizza la condizione bene con il valore di default o con quanto indicato in CNR_INVENTARIO_BENI_MIG_ORI
--               Si valorizza il bene come istituzionale
--            Se tutto bene allora:
--               Si inserisce il record di testata in CNR_INVENTARIO_BENI_MIG (senza importi)
--               Si cicla sui dettagli del gruppo per il recupero dei valori (valore iniziale, variazioni, ecc.)
--                  Se il record ? per carico l'importo non pu? essere negativo e questo ? portato come valore iniziale del bene
--                  Se il record ? per variazione si controllo il flag forza valore iniziale per portare l'importo in
--                  somma algebrica con il valore iniziale altrimenti, in base al segno, si valorizzano le variazioni piu e meno
--                  Si calcola l'imponibile ammortamento. E' pari all'assestato valore iniziale + variazioni solo per i beni
--                  dal 2000 in avanti e se il bene ? ammortizzabile.
--                  Si calcola il valore ammortizzato. Si legge solo per i beni con esercizio carico dal 2000 in avanti e
--                  se il bene ? ammortizzabile
--                  Se tutto bene si inserisce il record in CNR_INVENTARIO_BENI_MIG_DETT
--               Al termine del ciclo si controlla la consistenza complessiva dei valori migrati includendo, se il blocco
--               non prevede il carico, anche i valori gi? presenti in INVENTARIO_BENI.
--               Si aggiornano i campi valore su CNR_INVENTARIO_BENI_MIG (ora con importi)
--               Se tutto bene allora
--                  Si azzera il blocco e si prosegue con la fetch
--               altrimenti
--                  Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_ORI sono portati in
--                  CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione
--            altrimenti
--               Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_ORI sono portati in
--               CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione
--         In uscita dalla procedura:
--            Se non vi sono errori
--               Se non ? stato letto alcun record si solleva eccezione
--               Si valorizza il rigo di dettaglio del log indicando il successo dell'operazione.
--                  Se vi sono stati degli scarti il dettaglio di log ? messo in warning
--            Se vi sono errori
--               Si valorizza il rigo di dettaglio del log indicando l'errore dell'operazione.
--            Si rimuovono i record da CNR_INVENTARIO_BENI_MIG_ORI. Ora sono:
--               In CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT quelli andati a buon fine
--               In CNR_INVENTARIO_BENI_MIG_SCARTO quelli con errore
--            Si rilascia il semaforo di esecuzione migrazione in corso.
--
-- Parametri:
--

   PROCEDURE migrazioneBeniBase;

-- La procedura migra i beni in stato 'I' (iniziale) presenti sulla tabella INVENTARIO_BENI_MIG sulla tabella
-- INVENTARIO_BENI ed eventualmente su INVENTARIO_UTILIZZATORI_LA
--
-- Pre-post name: Acquisizione del semaforo applicativo
-- Pre:           E' in corso una migrazione beni
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo log
-- Pre:           Non ? possibile recuperare il progressivo identificativo del log (testata e dettaglio)
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero del progressivo di caricamento per CNR_INVENTARIO_BENI_MIG e CNR_INVENTARIO_BENI_MIG_DETT.
-- Pre:           Non ? possibile recuperare il progressivo di caricamento
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Recupero della linea di attivit? (CONFIGURAZIONE_CNR) per la valorizzazione dell'utilizzatore
-- Pre:           Non ? possibile recuperare la linea di attivit?
-- Post:          Viene sollevata un'eccezione
--
-- Pre-post name: Esecuzione della prima fase della migrazione beni
-- Pre:           Nessuna delle precondizioni precedenti ? verificata
-- Post:   Vengono letti tutti i beni da caricare presenti in CNR_INVENTARIO_BENI_MIG che siano in stato I ed in base
--         agli eventuali parametri indicati. I record sono ordinati per id_bene_origine e pg_caricamento
--         (l'eventuale carico ? il primo record).
--         Per ogni blocco di record omogenei per id_bene_origine si processa la migrazione in INVENTARIO_BENI.
--         Gli eventuali errori trovati non bloccano la procedura di migrazione.
--         Si eseguono i seguenti controlli e azioni (con generazione errore).
--            Controllo che la matrice beni sia piena
--            Controllo se il primo record riguarda un carico o una variazione di valore. (creazione bene o modifica
--            dello stesso
--            Se non deve essere creato il bene
--               Si recupera il valore dello stesso con la chiave primaria di INVENTARIO_BENI riportata in
--               CNR_INVENTARIO_BENI_MIG
--            altrimenti il controllo ? fatto con parametro id_bene_origine.
--            Se non esiste carico allora deve esistere il bene altrimenti il bene non deve esistere
--            Si compone il record INVENTARIO_BENI (senza importi) recuperando, se il bene deve essere creato, il
--            numero di riferimento del bene (da 1 al valore iniziale impostato in ID_INVENTARIO)
--            Si cicla sui dettagli del gruppo per il recupero dei valori (valore iniziale, variazioni, ecc.)
--            Si aggiornano i campi relativi al numero bene ed allo stato della migrazione in CNR_INVENTARIO_BENI_MIG.
--            Al termine del ciclo si controlla la consistenza complessiva dei valori migrati includendo, se il blocco
--            non prevede il carico, anche i valori gi? presenti in INVENTARIO_BENI.
--            Si aggiornano i campi valore su INVENTARIO_BENI (ora con importi)
--            Se il bene deve essere creato si valorizza anche INVENTARIO_UTILIZZATORI_LA
--            Se tutto bene allora
--               Si azzera il blocco e si prosegue con la fetch
--            altrimenti
--               Si annulla quanto fatto e i record di CNR_INVENTARIO_BENI_MIG_DETT sono copiati in
--               CNR_INVENTARIO_BENI_MIG_SCARTO con la valorizzazione della motivazione.
--               Il record in processo di CNR_INVENTARIO_BENI_MIG ? posto in stato errore.
--         In uscita dalla procedura:
--            Se non vi sono errori
--               Se non ? stato letto alcun record si solleva eccezione
--               Si valorizza il rigo di dettaglio del log indicando il successo dell'operazione.
--                  Se vi sono stati degli scarti il dettaglio di log ? messo in warning
--            Se vi sono errori
--               Si valorizza il rigo di dettaglio del log indicando l'errore dell'operazione.
--            Si rilascia il semaforo di esecuzione migrazione in corso.
--
-- Parametri: (possono essere nulli per processare tutti i records)
--   aCdCds         -> Codice del cds
--   aCdUO          -> Codice della UO
--   aPgInventario  -> Progressivo identificativo dell'inventario
--


   PROCEDURE migrazioneBeniCompleta
      (aCdCds VARCHAR2,
       aCdUo  VARCHAR2,
       aPgInventario NUMBER);


-- Inserisce un record in INVENTARIO_BENI

   PROCEDURE insInventarioBeni
      (aRecInventarioBeni INVENTARIO_BENI%Rowtype);
 END;


CREATE OR REPLACE PACKAGE BODY CNRMIG400 AS

-- =================================================================================================
-- Recupero del codice cds di CIR da SCI
-- =================================================================================================
PROCEDURE getUoCdsDaSci
   (aCdTitSci VARCHAR2,
    aCdUbicazioneSci VARCHAR2,
    aEsercizio NUMBER,
    aCdCds IN OUT VARCHAR2,
    aCdUo IN OUT VARCHAR2
   ) IS

   aRecUnitaOrganizzativa UNITA_ORGANIZZATIVA%ROWTYPE;

BEGIN

   -- Recupero del codice unit? organizzativa da CNR_TIT_UBICAZIONE_UO

   BEGIN

      SELECT cd_uo_cir INTO aCdUo
      FROM   CNR_TIT_UBICAZIONE_UO
      WHERE  cd_tit_sci = aCdTitSci AND
             cd_ubicazione_sci = aCdUbicazioneSci;

   EXCEPTION

      WHEN others THEN
         IBMERR001.RAISE_ERR_GENERICO ('Impossibile recuperare codice UO di CIR da CNR_TIT_UBICAZIONE_UO');

   END;

   -- Recupero dell'unit? organizzativa valida

   aRecUnitaOrganizzativa:=CNRCTB020.getUoValida(aEsercizio,
                                                 aCdUo);

   -- Verifico che si tratti di UO

   IF aRecUnitaOrganizzativa.fl_cds = 'Y' THEN
      IBMERR001.RAISE_ERR_GENERICO ('E'' indicato un codice Cds invece di una UO in CNR_TIT_UBICAZIONE_UO');
   END IF;

   -- Valorizza Cds

   aCdCds:=aRecUnitaOrganizzativa.cd_unita_padre;

END getUoCdsDaSci;

-- =================================================================================================
-- Controllo esistenza tipo ammortamento
-- =================================================================================================
PROCEDURE checkTiAmmortamento
   (aCdCategoriaGruppo VARCHAR2,
    aTiAmmortamento CHAR,
    aEsercizio NUMBER
   ) IS

   aRecAssTipoAmmCatGrupInvent ASS_TIPO_AMM_CAT_GRUP_INV%ROWTYPE;

BEGIN

   SELECT * INTO aRecAssTipoAmmCatGrupInvent
   FROM   ASS_TIPO_AMM_CAT_GRUP_INV
   WHERE  ti_ammortamento = aTiAmmortamento AND
          cd_categoria_gruppo = aCdCategoriaGruppo AND
          esercizio_competenza = aEsercizio;

EXCEPTION

   WHEN others THEN
        IBMERR001.RAISE_ERR_GENERICO ('Impossibile validare il tipo ammortamento');

END checkTiAmmortamento;

-- =================================================================================================
-- Inserimento record CNR_INVENTARIO_BENI_MIG
-- =================================================================================================
PROCEDURE insCnrInventarioBeniMig
   (aRecCnrInventarioBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE) IS

BEGIN

   INSERT INTO CNR_INVENTARIO_BENI_MIG
          (pg_caricamento,
           id_bene_origine,
           fl_crea_bene,
           cir_cd_cds,
           cir_cd_uo,
           cir_pg_inventario,
           cir_nr_inventario,
           cir_progressivo,
           cir_ds_bene,
           cir_cd_categoria_gruppo,
           cir_ti_ammortamento,
           cir_fl_ammortamento,
           cir_cd_condizione_bene,
           cir_ti_commerc_istituz,
           cir_valore_iniziale,
           cir_valore_ammortizzato,
           cir_variazione_piu,
           cir_variazione_meno,
           cir_imponibile_ammortamento,
           cir_cd_ubicazione,
           cir_etichetta,
           cir_esercizio_carico_bene,
           cir_stato_coge,
           cir_stato_coan,
           stato,
           dt_creazione)
   VALUES (aRecCnrInventarioBeniMig.pg_caricamento,
           aRecCnrInventarioBeniMig.id_bene_origine,
           aRecCnrInventarioBeniMig.fl_crea_bene,
           aRecCnrInventarioBeniMig.cir_cd_cds,
           aRecCnrInventarioBeniMig.cir_cd_uo,
           aRecCnrInventarioBeniMig.cir_pg_inventario,
           aRecCnrInventarioBeniMig.cir_nr_inventario,
           aRecCnrInventarioBeniMig.cir_progressivo,
           aRecCnrInventarioBeniMig.cir_ds_bene,
           aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo,
           aRecCnrInventarioBeniMig.cir_ti_ammortamento,
           aRecCnrInventarioBeniMig.cir_fl_ammortamento,
           aRecCnrInventarioBeniMig.cir_cd_condizione_bene,
           aRecCnrInventarioBeniMig.cir_ti_commerc_istituz,
           aRecCnrInventarioBeniMig.cir_valore_iniziale,
           aRecCnrInventarioBeniMig.cir_valore_ammortizzato,
           aRecCnrInventarioBeniMig.cir_variazione_piu,
           aRecCnrInventarioBeniMig.cir_variazione_meno,
           aRecCnrInventarioBeniMig.cir_imponibile_ammortamento,
           aRecCnrInventarioBeniMig.cir_cd_ubicazione,
           aRecCnrInventarioBeniMig.cir_etichetta,
           aRecCnrInventarioBeniMig.cir_esercizio_carico_bene,
           aRecCnrInventarioBeniMig.cir_stato_coge,
           aRecCnrInventarioBeniMig.cir_stato_coan,
           aRecCnrInventarioBeniMig.stato,
           aRecCnrInventarioBeniMig.dt_creazione);

END insCnrInventarioBeniMig;

-- =================================================================================================
-- Inserimento record CNR_INVENTARIO_BENI_MIG_DETT
-- =================================================================================================
PROCEDURE insCnrInventarioBeniMigDett
   (aPgCaricamento NUMBER,
    aPgMovimento NUMBER,
    aRecCnrInventarioBeniMigOri CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE) IS

BEGIN

   INSERT INTO CNR_INVENTARIO_BENI_MIG_DETT
          (pg_caricamento,
           id_bene_origine,
           pg_movimento,
           cd_tit_sci,
           cd_ubicazione_sci,
           ti_movimento,
           ds_bene,
           cd_categoria_gruppo,
           ti_ammortamento,
           fl_ammortamento,
           cd_condizione_bene,
           importo,
           fl_forza_valore_iniziale,
           valore_ammortizzato,
           esercizio_carico_bene,
           esercizio_coep_bene)
   VALUES (aPgCaricamento,
           aRecCnrInventarioBeniMigOri.id_bene_origine,
           aPgMovimento,
           aRecCnrInventarioBeniMigOri.cd_tit_sci,
           aRecCnrInventarioBeniMigOri.cd_ubicazione_sci,
           aRecCnrInventarioBeniMigOri.ti_movimento,
           aRecCnrInventarioBeniMigOri.ds_bene,
           aRecCnrInventarioBeniMigOri.cd_categoria_gruppo,
           aRecCnrInventarioBeniMigOri.ti_ammortamento,
           aRecCnrInventarioBeniMigOri.fl_ammortamento,
           aRecCnrInventarioBeniMigOri.cd_condizione_bene,
           aRecCnrInventarioBeniMigOri.importo,
           aRecCnrInventarioBeniMigOri.fl_forza_valore_iniziale,
           aRecCnrInventarioBeniMigOri.valore_ammortizzato,
           aRecCnrInventarioBeniMigOri.esercizio_carico_bene,
           aRecCnrInventarioBeniMigOri.esercizio_coep_bene);

END insCnrInventarioBeniMigDett;

-- =================================================================================================
-- Inserimento record CNR_INVENTARIO_BENI_MIG_SCARTO
-- =================================================================================================
PROCEDURE insCnrInventarioBeniMigScarto
   (aPgCaricamento NUMBER,
    aPgMovimento NUMBER,
    aData DATE,
    aErrore VARCHAR2,
    aRecCnrInventarioBeniMigOri CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE) IS

BEGIN

   INSERT INTO CNR_INVENTARIO_BENI_MIG_SCARTO
          (pg_caricamento,
           id_bene_origine,
           pg_movimento,
           cd_tit_sci,
           cd_ubicazione_sci,
           ti_movimento,
           ds_bene,
           cd_categoria_gruppo,
           ti_ammortamento,
           fl_ammortamento,
           cd_condizione_bene,
           importo,
           fl_forza_valore_iniziale,
           valore_ammortizzato,
           esercizio_carico_bene,
           esercizio_coep_bene,
           dt_creazione,
           nota_errore)
   VALUES (aPgCaricamento,
           aRecCnrInventarioBeniMigOri.id_bene_origine,
           aPgMovimento,
           aRecCnrInventarioBeniMigOri.cd_tit_sci,
           aRecCnrInventarioBeniMigOri.cd_ubicazione_sci,
           aRecCnrInventarioBeniMigOri.ti_movimento,
           aRecCnrInventarioBeniMigOri.ds_bene,
           aRecCnrInventarioBeniMigOri.cd_categoria_gruppo,
           aRecCnrInventarioBeniMigOri.ti_ammortamento,
           aRecCnrInventarioBeniMigOri.fl_ammortamento,
           aRecCnrInventarioBeniMigOri.cd_condizione_bene,
           aRecCnrInventarioBeniMigOri.importo,
           aRecCnrInventarioBeniMigOri.fl_forza_valore_iniziale,
           aRecCnrInventarioBeniMigOri.valore_ammortizzato,
           aRecCnrInventarioBeniMigOri.esercizio_carico_bene,
           aRecCnrInventarioBeniMigOri.esercizio_coep_bene,
           aData,
           aErrore);

END insCnrInventarioBeniMigScarto;

-- =================================================================================================
-- Controllo globale di consistenza importi
-- =================================================================================================
PROCEDURE checkConsistenzaImporti
   (inImAssestato NUMBER,
    inImImponibileAmmortamento NUMBER,
    inImValoreAmmortizzato NUMBER
   ) IS

BEGIN

   IF (inImAssestato < 0 OR
       inImImponibileAmmortamento < 0 OR
       inImValoreAmmortizzato < 0) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''assestato o l''imponibile ammortamento o il valore ammortizzato risultano negativi');
   END IF;

   IF inImImponibileAmmortamento > inImAssestato THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''imponibile ammortamento risulta superiore all''assestato');
   END IF;

   IF inImValoreAmmortizzato > inImImponibileAmmortamento THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Il valore ammortizzato non pu? essere superiore all''imponibile ammortamento');
   END IF;

END checkConsistenzaImporti;

-- =================================================================================================
-- Controllo globale di consistenza importi
-- =================================================================================================
PROCEDURE checkConsistenzaImportiBase
   (aRecCnrInventarioBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE,
    aRecInventarioBeni INVENTARIO_BENI%ROWTYPE,
    aFlEsisteCarico CHAR
   ) IS

   aImAssestato NUMBER(15,2);
   aImImponibileAmmortamento NUMBER(15,2);
   aImValoreAmmortizzato NUMBER(15,2);

BEGIN

   aImAssestato:=aRecCnrInventarioBeniMig.cir_valore_iniziale + aRecCnrInventarioBeniMig.cir_variazione_piu -
                 aRecCnrInventarioBeniMig.cir_variazione_meno;
   aImImponibileAmmortamento:=aRecCnrInventarioBeniMig.cir_imponibile_ammortamento;
   aImValoreAmmortizzato:=aRecCnrInventarioBeniMig.cir_valore_ammortizzato;

   IF aFlEsisteCarico = 'N' THEN
      aImAssestato:=aImAssestato + aRecInventarioBeni.valore_iniziale + aRecInventarioBeni.variazione_piu -
                    aRecInventarioBeni.variazione_meno;
      aImImponibileAmmortamento:=aImImponibileAmmortamento + aRecInventarioBeni.imponibile_ammortamento;
      aImValoreAmmortizzato:=aImValoreAmmortizzato + aRecInventarioBeni.valore_ammortizzato;
   END IF;

   checkConsistenzaImporti(aImAssestato,
                           aImImponibileAmmortamento,
                           aImValoreAmmortizzato);

END checkConsistenzaImportiBase;

-- =================================================================================================
-- Cancellazione record da CNR_INVENTARIO_BENI_MIG_ORI
-- =================================================================================================
PROCEDURE delCnrInventarioBeniMigOri
   (aPgCaricamento NUMBER) IS

   aConta INTEGER;
   aIdBeneOrigine VARCHAR2(30);

   gen_cv GenericCurTyp;

BEGIN

   aConta:=0;

   -------------------------------------------------------------------------------------------------
   -- Cancellazione da CNR_INVENTARIO_BENI_MIG

   BEGIN

      OPEN gen_cv FOR

           SELECT id_bene_origine
           FROM   CNR_INVENTARIO_BENI_MIG
           WHERE  pg_caricamento = aPgCaricamento;

      LOOP

         FETCH gen_cv INTO
               aIdBeneOrigine;

         EXIT WHEN gen_cv%NOTFOUND;

         aConta:=aConta + 1;

         DELETE FROM CNR_INVENTARIO_BENI_MIG_ORI
         WHERE  id_bene_origine = aIdBeneOrigine;

         IF aConta > 200 THEN
            COMMIT;
         END IF;

      END LOOP;

      CLOSE gen_cv;

      COMMIT;

   END;

   -------------------------------------------------------------------------------------------------
   -- Cancellazione da CNR_INVENTARIO_BENI_MIG

   aConta:=0;

   BEGIN

      OPEN gen_cv FOR

           SELECT DISTINCT id_bene_origine
           FROM   CNR_INVENTARIO_BENI_MIG_SCARTO
           WHERE  pg_caricamento = aPgCaricamento;

      LOOP

         FETCH gen_cv INTO
               aIdBeneOrigine;

         EXIT WHEN gen_cv%NOTFOUND;

         aConta:=aConta + 1;

         DELETE FROM CNR_INVENTARIO_BENI_MIG_ORI
         WHERE  id_bene_origine = aIdBeneOrigine;

         IF aConta > 200 THEN
            COMMIT;
         END IF;

      END LOOP;

      CLOSE gen_cv;

      COMMIT;

   END;

END delCnrInventarioBeniMigOri;

-- =================================================================================================
-- Esecuzione migrazione base
-- =================================================================================================
PROCEDURE eseguiMigraBeniBase
   (aPgCaricamento NUMBER
   ) IS

   flEsisteCarico CHAR(1);
   eseguiLock CHAR(1);
   aCdCds UNITA_ORGANIZZATIVA.cd_unita_organizzativa%TYPE;
   aCdUo UNITA_ORGANIZZATIVA.cd_unita_organizzativa%TYPE;
   aPgInventario NUMBER(10);
   aEsercizio NUMBER(4);

   i BINARY_INTEGER;

   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;
   aRecCnrInventarioBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE;
   aRecCnrInventarioBeniMigDett CNR_INVENTARIO_BENI_MIG_DETT%ROWTYPE;
   aRecCategoriaGruppoInvent CATEGORIA_GRUPPO_INVENT%ROWTYPE;
   aRecUbicazioneBene UBICAZIONE_BENE%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazioni

   flEsisteCarico:='N';
   eseguiLock:='Y';
   aCdCds:=NULL;
   aCdUo:=NULL;
   aRecInventarioBeni:=NULL;
   aRecCnrInventarioBeniMig:=NULL;
   aRecCnrInventarioBeniMigDett:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Controlli base

   -- Controllo che la matrice beni sia piena

   IF beniDaMigrareOri_tab.COUNT = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO ('Matrice gruppo beni da migrare vuota');
   END IF;

   -- Si controlla se il primo record riguarda un carico o una variazione di valore

   IF (beniDaMigrareOri_tab(1).ti_movimento IS NULL OR
         (beniDaMigrareOri_tab(1).ti_movimento != 'C' AND
          beniDaMigrareOri_tab(1).ti_movimento != 'V')) THEN
      IBMERR001.RAISE_ERR_GENERICO ('L''attributo ti_movimento del primo record non ? correttamente valorizzato (C/V)');
   ELSE
      IF beniDaMigrareOri_tab(1).ti_movimento = 'C' THEN
         flEsisteCarico:='Y';
      END IF;
   END IF;

   -- Recupero il record bene con parametro id_bene_origine.
   -- Se non esiste carico allora deve esistere il bene altrimenti il bene non deve esistere

   IF flEsisteCarico = 'N' THEN
      aRecInventarioBeni:=CNRCTB400.getInventarioBeni(beniDaMigrareOri_tab(1).id_bene_origine,
                                                      eseguiLock);
   ELSE
      IF CNRCTB400.checkEsisteBene(beniDaMigrareOri_tab(1).id_bene_origine) = 'Y' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Esiste un record per carico bene e lo stesso id_bene_origine ? gi? presente in INVENTARIO_BENI');
      END IF;
   END IF;

   -- Recupero dell'esercizio

   IF flEsisteCarico = 'N' THEN
      aEsercizio:=aRecInventarioBeni.esercizio_carico_bene;
   ELSE
      IF beniDaMigrareOri_tab(1).esercizio_coep_bene < 2004 THEN
         IBMERR001.RAISE_ERR_GENERICO ('La migrazione beni deve riferirsi alla COEP almeno per l''esercizio 2004');
      ELSE
         aEsercizio:=beniDaMigrareOri_tab(1).esercizio_coep_bene;
      END IF;
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Recupero Cds, UO e pg_inventario

   getUoCdsDaSci(beniDaMigrareOri_tab(1).cd_tit_sci,
                 beniDaMigrareOri_tab(1).cd_ubicazione_sci,
                 aEsercizio,
                 aCdCds,
                 aCdUo);
   aPgInventario:=CNRCTB400.getPgInventario(aCdCds,
                                            aCdUo);

   IF (flEsisteCarico = 'N' AND
       aPgInventario != aRecInventarioBeni.pg_inventario) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''identificativo inventario definito dalla variazione in caricamento ? diverso da quello ' ||
          'del bene referenziato');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Composizione base del record di CNR_INVENTARIO_BENI_MIG

   aRecCnrInventarioBeniMig.pg_caricamento:=aPgCaricamento;
   aRecCnrInventarioBeniMig.id_bene_origine:=beniDaMigrareOri_tab(1).id_bene_origine;
   aRecCnrInventarioBeniMig.cir_cd_cds:=aCdCds;
   aRecCnrInventarioBeniMig.cir_cd_uo:=aCdUo;
   aRecCnrInventarioBeniMig.cir_pg_inventario:=aPgInventario;
   aRecCnrInventarioBeniMig.cir_valore_iniziale:=0;
   aRecCnrInventarioBeniMig.cir_valore_ammortizzato:=0;
   aRecCnrInventarioBeniMig.cir_variazione_piu:=0;
   aRecCnrInventarioBeniMig.cir_variazione_meno:=0;
   aRecCnrInventarioBeniMig.cir_imponibile_ammortamento:=0;
   aRecCnrInventarioBeniMig.cir_esercizio_carico_bene:=aEsercizio;
   aRecCnrInventarioBeniMig.cir_stato_coge:=CNRCTB100.STATO_COEP_EXC;
   aRecCnrInventarioBeniMig.cir_stato_coan:=CNRCTB100.STATO_COEP_EXC;
   aRecCnrInventarioBeniMig.stato:=STATO_INIZIALE;
   aRecCnrInventarioBeniMig.dt_creazione:=dataOdierna;

   IF flEsisteCarico = 'N' THEN
      aRecCnrInventarioBeniMig.fl_crea_bene:='N';
      aRecCnrInventarioBeniMig.cir_nr_inventario:=aRecInventarioBeni.nr_inventario;
      aRecCnrInventarioBeniMig.cir_progressivo:=aRecInventarioBeni.progressivo;
      aRecCnrInventarioBeniMig.cir_ds_bene:=aRecInventarioBeni.ds_bene;
      aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo:=aRecInventarioBeni.cd_categoria_gruppo;
      aRecCnrInventarioBeniMig.cir_ti_ammortamento:=aRecInventarioBeni.ti_ammortamento;
      aRecCnrInventarioBeniMig.cir_fl_ammortamento:=aRecInventarioBeni.fl_ammortamento;
      aRecCnrInventarioBeniMig.cir_cd_ubicazione:=aRecInventarioBeni.cd_ubicazione;
      aRecCnrInventarioBeniMig.cir_etichetta:=aRecInventarioBeni.etichetta;
      aRecCnrInventarioBeniMig.cir_cd_condizione_bene:=aRecInventarioBeni.cd_condizione_bene;
      aRecCnrInventarioBeniMig.cir_ti_commerc_istituz:=aRecInventarioBeni.ti_commerciale_istituzionale;
   ELSE
      aRecCnrInventarioBeniMig.fl_crea_bene:='Y';
      aRecCnrInventarioBeniMig.cir_nr_inventario:=0;
      aRecCnrInventarioBeniMig.cir_progressivo:=0;

      -- Controllo descrizione bene

      IF beniDaMigrareOri_tab(1).ds_bene IS NULL THEN
         IBMERR001.RAISE_ERR_GENERICO ('Manca l''indicazione della descrizione del bene');
      ELSE
         aRecCnrInventarioBeniMig.cir_ds_bene:=beniDaMigrareOri_tab(1).ds_bene;
      END IF;

      -- Controllo categoria gruppo

      aRecCategoriaGruppoInvent:=CNRCTB400.getCategoriaGruppoInvent(beniDaMigrareOri_tab(1).cd_categoria_gruppo);
      IF aRecCategoriaGruppoInvent.fl_gestione_inventario = 'N' THEN
         IBMERR001.RAISE_ERR_GENERICO ('La categoria gruppo di riferimento non ? per gestione inventario');
      ELSE
         IF aRecCategoriaGruppoInvent.livello = 0 THEN
            IBMERR001.RAISE_ERR_GENERICO ('Deve essere indicata una categoria gruppo e non una categoria inventariale');
         ELSE
            aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo:=aRecCategoriaGruppoInvent.cd_categoria_gruppo;
          END IF;
      END IF;

      -- Controllo flag ammortamento

      IF beniDaMigrareOri_tab(1).fl_ammortamento IS NULL THEN
         aRecCnrInventarioBeniMig.cir_fl_ammortamento:=aRecCategoriaGruppoInvent.fl_ammortamento;
      ELSE
         IF (beniDaMigrareOri_tab(1).fl_ammortamento = 'Y' AND
             aRecCategoriaGruppoInvent.fl_ammortamento = 'N') THEN
            IBMERR001.RAISE_ERR_GENERICO
                ('Il bene ? definito come ammortizzabile in una categoria gruppo che non ? ammortizzabile');
         ELSE
            aRecCnrInventarioBeniMig.cir_fl_ammortamento:=beniDaMigrareOri_tab(1).fl_ammortamento;
         END IF;
      END IF;

      -- Controllo tipo ammortamento

      IF beniDaMigrareOri_tab(1).ti_ammortamento IS NULL THEN
         aRecCnrInventarioBeniMig.cir_ti_ammortamento:=beniDaMigrareOri_tab(1).ti_ammortamento;
      ELSE
         IF aRecCnrInventarioBeniMig.cir_fl_ammortamento = 'N' THEN
            IBMERR001.RAISE_ERR_GENERICO
                ('E'' stato definito un tipo ammortamento per un bene che non deve essere ammortizzato');
         ELSE
            checkTiAmmortamento(aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo,
                                beniDaMigrareOri_tab(1).ti_ammortamento,
                                aEsercizio);
            aRecCnrInventarioBeniMig.cir_ti_ammortamento:=beniDaMigrareOri_tab(1).ti_ammortamento;
         END IF;
      END IF;

      -- Ubicazione

      aRecCnrInventarioBeniMig.cir_cd_ubicazione:=CNRCTB400.getCdUbicazioneDefault(aCdCds,
                                                                                   aCdUo);

      -- Etichetta

      aRecCnrInventarioBeniMig.cir_etichetta:=aCdUo || '-' || beniDaMigrareOri_tab(1).id_bene_origine || '-000';

      -- Condizione bene

      IF beniDaMigrareOri_tab(1).cd_condizione_bene IS NULL THEN
         aRecCnrInventarioBeniMig.cir_cd_condizione_bene:=CONDIZIONE_BENE_DEFAULT;
      ELSE
         aRecCnrInventarioBeniMig.cir_cd_condizione_bene:=beniDaMigrareOri_tab(1).cd_condizione_bene;
      END IF;

      -- Commerciale/Istituzionale

      aRecCnrInventarioBeniMig.cir_ti_commerc_istituz:=CNRCTB100.TI_ISTITUZIONALE;

   END IF;

   -------------------------------------------------------------------------------------------------
   -- Inserimento record su CNR_INVENTARIO_BENI_MIG

   insCnrInventarioBeniMig(aRecCnrInventarioBeniMig);

   -------------------------------------------------------------------------------------------------
   -- Ciclo lettura matrice beni in migrazione

   FOR i IN 1 .. beniDaMigrareOri_tab.COUNT

   LOOP

      -- Controllo importo a zero

      IF beniDaMigrareOri_tab(i).importo = 0 THEN
         IBMERR001.RAISE_ERR_GENERICO ('Importo a zero non ammesso');
      END IF;

      -- Valorizzo valore iniziale e variazioni pi? e meno

      IF beniDaMigrareOri_tab(i).ti_movimento = 'C' THEN
         IF beniDaMigrareOri_tab(i).importo < 0 THEN
            IBMERR001.RAISE_ERR_GENERICO ('L''importo definito per un movimento di carico non pu? essere negativo');
         END IF;
         aRecCnrInventarioBeniMig.cir_valore_iniziale:=aRecCnrInventarioBeniMig.cir_valore_iniziale +
                                                       beniDaMigrareOri_tab(i).importo;
      ELSE
         IF beniDaMigrareOri_tab(i).fl_forza_valore_iniziale = 'Y' THEN
            aRecCnrInventarioBeniMig.cir_valore_iniziale:=aRecCnrInventarioBeniMig.cir_valore_iniziale +
                                                          beniDaMigrareOri_tab(i).importo;
         ELSE
            IF beniDaMigrareOri_tab(i).importo < 0 THEN
               aRecCnrInventarioBeniMig.cir_variazione_meno:=aRecCnrInventarioBeniMig.cir_variazione_meno +
                                                             ABS(beniDaMigrareOri_tab(i).importo);
            ELSE
               aRecCnrInventarioBeniMig.cir_variazione_piu:=aRecCnrInventarioBeniMig.cir_variazione_piu +
                                                             beniDaMigrareOri_tab(i).importo;
            END IF;
         END IF;
      END IF;

      -- Imponibile ammortamento. E' pari all'assestato valore iniziale + variazioni solo per i beni dal
      -- 2000 in avanti e se il bene ? ammortizzabile

      IF beniDaMigrareOri_tab(i).esercizio_carico_bene > 1999 THEN
         IF flEsisteCarico = 'N' THEN
            IF aRecInventarioBeni.fl_ammortamento = 'Y' THEN
               aRecCnrInventarioBeniMig.cir_imponibile_ammortamento:=aRecCnrInventarioBeniMig.cir_imponibile_ammortamento +
                                                                     beniDaMigrareOri_tab(i).importo;
            END IF;
         ELSE
            IF aRecCnrInventarioBeniMig.cir_fl_ammortamento = 'Y' THEN
               aRecCnrInventarioBeniMig.cir_imponibile_ammortamento:=aRecCnrInventarioBeniMig.cir_imponibile_ammortamento +
                                                                     beniDaMigrareOri_tab(i).importo;
            END IF;
         END IF;
      END IF;

      -- Valore ammortizzato. Si legge solo per i beni con esercizio carico dal 2000 in avanti e se il bene ?
      -- ammortizzabile

      IF beniDaMigrareOri_tab(i).esercizio_carico_bene > 1999 THEN
         IF beniDaMigrareOri_tab(i).valore_ammortizzato != 0 THEN
            IF flEsisteCarico = 'N' THEN
               IF aRecInventarioBeni.fl_ammortamento = 'Y' THEN
                  aRecCnrInventarioBeniMig.cir_valore_ammortizzato:=aRecCnrInventarioBeniMig.cir_valore_ammortizzato +
                                                                    beniDaMigrareOri_tab(i).valore_ammortizzato;
               ELSE
                 IBMERR001.RAISE_ERR_GENERICO ('E'' definito un importo ammortizzato per un bene non ammortizzabile');
               END IF;
            ELSE
               IF aRecCnrInventarioBeniMig.cir_fl_ammortamento = 'Y' THEN
                  aRecCnrInventarioBeniMig.cir_valore_ammortizzato:=aRecCnrInventarioBeniMig.cir_valore_ammortizzato +
                                                                    beniDaMigrareOri_tab(i).valore_ammortizzato;
               ELSE
                 IBMERR001.RAISE_ERR_GENERICO ('E'' definito un importo ammortizzato per un bene non ammortizzabile');
               END IF;
            END IF;
         END IF;
      END IF;

      -- Inserimento record in CNR_INVENTARIO_BENI_MIG_DETT

      insCnrInventarioBeniMigDett(aPgCaricamento,
                                  i,
                                  beniDaMigrareOri_tab(i));

   END LOOP;

   -------------------------------------------------------------------------------------------------
   -- Controllo globale di consistenza includendo anche il valore presente sul bene

   checkConsistenzaImportiBase(aRecCnrInventarioBeniMig,
                               aRecInventarioBeni,
                               flEsisteCarico);

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dei campi valore su CNR_INVENTARIO_BENI_MIG

   BEGIN

      UPDATE CNR_INVENTARIO_BENI_MIG
      SET    cir_valore_iniziale = aRecCnrInventarioBeniMig.cir_valore_iniziale,
             cir_variazione_piu = aRecCnrInventarioBeniMig.cir_variazione_piu,
             cir_variazione_meno = aRecCnrInventarioBeniMig.cir_variazione_meno,
             cir_imponibile_ammortamento = aRecCnrInventarioBeniMig.cir_imponibile_ammortamento,
             cir_valore_ammortizzato = aRecCnrInventarioBeniMig.cir_valore_ammortizzato
      WHERE  pg_caricamento = aRecCnrInventarioBeniMig.pg_caricamento AND
             id_bene_origine = aRecCnrInventarioBeniMig.id_bene_origine;

   EXCEPTION

      WHEN others THEN
           IBMERR001.RAISE_ERR_GENERICO ('Errore in aggiornamento valori su CNR_INVENTARIO_BENI_MIG');

   END;

   COMMIT;

END eseguiMigraBeniBase;

-- =================================================================================================
-- Esecuzione scarto migrazione base
-- =================================================================================================
PROCEDURE eseguiScartoBeniBase
   (aPgCaricamento NUMBER,
    aErrore VARCHAR2,
    aNumero NUMBER
   ) IS

   i BINARY_INTEGER;

BEGIN

   FOR i IN 1 .. beniDaMigrareOri_tab.COUNT

   LOOP

      insCnrInventarioBeniMigScarto(aPgCaricamento,
                                    aNUmero + i,
                                    dataOdierna,
                                    aErrore,
                                    beniDaMigrareOri_tab(i));

   END LOOP;

   COMMIT;

END eseguiScartoBeniBase;

-- =================================================================================================
-- Caricamento dati sulla tabella di appoggio CNR_INVENTARIO_BENI_MIG con i dati presenti nella tabella
-- CNR_INVENTARIO_BENI_MIG_ORI
-- =================================================================================================
PROCEDURE caricaBeniBase
   (aPgCaricamento NUMBER,
    aNumRecLetto IN OUT NUMBER,
    aNumRecScritto IN OUT NUMBER,
    aNumRecScarto IN OUT NUMBER
   ) IS

   memIdBeneOrigine CNR_INVENTARIO_BENI_MIG_ORI.id_bene_origine%TYPE;
   aFlMigrazione CHAR(1);
   aMessaggioErrore VARCHAR2(4000);
   i BINARY_INTEGER;

   aRecCnrInventarioBeniMigOri CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazioni iniziali

   memIdBeneOrigine:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale lettura CNR_INVENTARIO_BENI_MIG_ORI

   BEGIN

      OPEN gen_cv FOR

           SELECT *
           FROM   CNR_INVENTARIO_BENI_MIG_ORI
           ORDER BY id_bene_origine,
                    ti_movimento;

      LOOP

         BEGIN

            FETCH gen_cv INTO
                  aRecCnrInventarioBeniMigOri;

            EXIT WHEN gen_cv%NOTFOUND;

            aNumRecLetto:=aNumRecLetto + 1;

            -- E' il primo record letto

            IF memIdBeneOrigine IS NULL THEN
               memIdBeneOrigine:=aRecCnrInventarioBeniMigOri.id_bene_origine;
               beniDaMigrareOri_tab.DELETE;
               i:=0;
            END IF;

            -- Se l'identificativo del bene origine ? diverso da quello memorizzato allora indico l'esecuzione
            -- della migrazione

            IF aRecCnrInventarioBeniMigOri.id_bene_origine != memIdBeneOrigine THEN
               aFlMigrazione:='Y';
            END IF;

            -- Esecuzione della migrazione e azzeramento della tabella per memorizzare la nuova occorrenza

            BEGIN

               IF aFlMigrazione = 'Y' THEN
                  eseguiMigraBeniBase(aPgCaricamento);
                  aNumRecScritto:=aNumRecScritto + beniDaMigrareOri_tab.COUNT;
                  beniDaMigrareOri_tab.DELETE;
                  i:=0;
               END IF;

            EXCEPTION

               WHEN others THEN
                    aMessaggioErrore:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
                    ROLLBACK;
                    eseguiScartoBeniBase(aPgCaricamento,
                                         aMessaggioErrore,
                                         aNumRecScarto);
                    aNumRecScarto:=aNumRecScarto + beniDaMigrareOri_tab.COUNT;
                    beniDaMigrareOri_tab.DELETE;
                    i:=0;

            END;

            -- Memorizzo sulla tabella di appoggio i beni

            aFlMigrazione:='N';
            i:=i + 1;
            memIdBeneOrigine:=aRecCnrInventarioBeniMigOri.id_bene_origine;
            beniDaMigrareOri_tab(i).cd_tit_sci:=aRecCnrInventarioBeniMigOri.cd_tit_sci;
            beniDaMigrareOri_tab(i).cd_ubicazione_sci:=aRecCnrInventarioBeniMigOri.cd_ubicazione_sci;
            beniDaMigrareOri_tab(i).id_bene_origine:=aRecCnrInventarioBeniMigOri.id_bene_origine;
            beniDaMigrareOri_tab(i).ti_movimento:=aRecCnrInventarioBeniMigOri.ti_movimento;
            beniDaMigrareOri_tab(i).ds_bene:=aRecCnrInventarioBeniMigOri.ds_bene;
            beniDaMigrareOri_tab(i).cd_categoria_gruppo:=aRecCnrInventarioBeniMigOri.cd_categoria_gruppo;
            beniDaMigrareOri_tab(i).esercizio_carico_bene:=aRecCnrInventarioBeniMigOri.esercizio_carico_bene;
            beniDaMigrareOri_tab(i).esercizio_coep_bene:=aRecCnrInventarioBeniMigOri.esercizio_coep_bene;
            beniDaMigrareOri_tab(i).importo:=aRecCnrInventarioBeniMigOri.importo;
            beniDaMigrareOri_tab(i).fl_forza_valore_iniziale:=aRecCnrInventarioBeniMigOri.fl_forza_valore_iniziale;
            beniDaMigrareOri_tab(i).valore_ammortizzato:=aRecCnrInventarioBeniMigOri.valore_ammortizzato;
            beniDaMigrareOri_tab(i).ti_ammortamento:=aRecCnrInventarioBeniMigOri.ti_ammortamento;
            beniDaMigrareOri_tab(i).fl_ammortamento:=aRecCnrInventarioBeniMigOri.fl_ammortamento;
            beniDaMigrareOri_tab(i).cd_condizione_bene:=aRecCnrInventarioBeniMigOri.cd_condizione_bene;

         END;

      END LOOP;

      CLOSE gen_cv;

      BEGIN

         eseguiMigraBeniBase(aPgCaricamento);
         aNumRecScritto:=aNumRecScritto + beniDaMigrareOri_tab.COUNT;
         beniDaMigrareOri_tab.DELETE;
         i:=0;

      EXCEPTION

         WHEN others THEN
              aMessaggioErrore:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
              ROLLBACK;
              eseguiScartoBeniBase(aPgCaricamento,
                                   aMessaggioErrore,
                                   aNumRecScarto);
              aNumRecScarto:=aNumRecScarto + beniDaMigrareOri_tab.COUNT;
              beniDaMigrareOri_tab.DELETE;
              i:=0;

      END;

   END;

END caricaBeniBase;

-- =================================================================================================
-- Main prima fase della migrazione beni.
-- Caricamento dati sulla tabella di appoggio CNR_INVENTARIO_BENI_MIG da CNR_INVENTARIO_BENI_MIG_ORI
-- =================================================================================================
PROCEDURE migrazioneBeniBase IS

   aPgCaricamento NUMBER(10);
   aPgLog INTEGER;
   aNumRecLetto NUMBER(10);
   aNumRecScritto NUMBER(10);
   aNumRecScarto NUMBER(10);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazioni iniziali

   dataOdierna:=SYSDATE;
   aEsercizioBase:=TO_NUMBER(TO_CHAR(dataOdierna,'YYYY'));
   aNumRecLetto:=0;
   aNumRecScritto:=0;
   aNumRecScarto:=0;

   -------------------------------------------------------------------------------------------------
   -- Recupero informazioni base

   -- Acquisizione semaforo applicativo

   CNRCTB800.acquisisciSemStaticoCds(0, '*', SEMAFORO, aUtente);

   -- Recupero progressivo log

   aPgLog:= IBMUTL200.LOGSTART('MIGRAZIONE INVENTARIO - FASE BASE' , aUtente, NULL, NULL);

   -- Recupero del progressivo di caricamento. Si recupera il valore da CNR_INVENTARIO_BENI_MIG e
   -- da CNR_INVENTARIO_BENI_MIG_SCARTO e si seleziona il maggiore dei due

   SELECT MAX(valore) INTO aPgCaricamento
   FROM   (SELECT NVL(MAX(pg_caricamento),0) valore
           FROM   CNR_INVENTARIO_BENI_MIG
           UNION ALL
           SELECT NVL(MAX(pg_caricamento),0) valore
           FROM   CNR_INVENTARIO_BENI_MIG_SCARTO);

   aPgCaricamento:=aPgCaricamento + 1;

   -------------------------------------------------------------------------------------------------
   -- Migrazione beni

   caricaBeniBase(aPgCaricamento,
                  aNumRecLetto,
                  aNumRecScritto,
                  aNumRecScarto);

   -- Controllo esecuzione della migrazione

   IF aNumRecLetto = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Nessun record elaborato, non sono stati trovati beni in CNR_INVENTARIO_BENI_MIG_ORI');
   END IF;

   COMMIT;

   -- Messaggio di operazione completata ad utente

   IF aNumRecScarto = 0 THEN
      IBMUTL200.logInf(aPgLog,
                       FASE_UNO || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       'Operazione completata con successo',
                       'Record letti ' || LPAD(aNumRecLetto,10,' ') || ' ' ||
                       'Record scritti ' || LPAD(aNumRecScritto,10,' ') || ' ' ||
                       'Record scartati ' || LPAD(aNumRecScarto,10,' '),
                       NULL);
   ELSE
      IBMUTL200.logWar(aPgLog,
                       FASE_UNO || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       'Operazione completata con successo ma con errori',
                       'Record letti ' || LPAD(aNumRecLetto,10,' ') || ' ' ||
                       'Record scritti ' || LPAD(aNumRecScritto,10,' ') || ' ' ||
                       'Record scartati ' || LPAD(aNumRecScarto,10,' '),
                       NULL);
   END IF;

   -- Cancellazione record da CNR_INVENTARIO_BENI_MIG_ORI

   delCnrInventarioBeniMigOri(aPgCaricamento);
   CNRCTB800.liberaSemStaticoCds(0, '*', SEMAFORO, aUtente);

EXCEPTION

   WHEN others THEN
        ROLLBACK;

   IBMUTL200.LOGERR(aPgLog,
                    FASE_UNO || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                    'Operazione in errore',
                    SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900),
                    NULL);

   delCnrInventarioBeniMigOri(aPgCaricamento);
   CNRCTB800.liberaSemStaticoCds(0, '*', SEMAFORO, aUtente);

END migrazioneBeniBase;

-- =================================================================================================
-- Esecuzione scarto migrazione completa
-- =================================================================================================
PROCEDURE eseguiScartoBeniCompleta
   (aPgCaricamento NUMBER,
    aErrore VARCHAR2
   ) IS

   aPgMovimento NUMBER(10);

   i BINARY_INTEGER;

   aRecCnrInventarioBeniMigDett CNR_INVENTARIO_BENI_MIG_DETT%ROWTYPE;
   aRecCnrInventarioBeniMigOri CNR_INVENTARIO_BENI_MIG_ORI%ROWTYPE;

   gen_cv_b GenericCurTyp;

BEGIN

   -- Recupero del valore massimo del pg_movimento in scarico

   BEGIN

      SELECT NVL(MAX(pg_movimento),0) INTO aPgMovimento
      FROM   CNR_INVENTARIO_BENI_MIG_SCARTO
      WHERE  pg_caricamento = aPgCaricamento;

   END;

   -- Ciclo per scarto beni


   FOR i IN 1 .. beniDaMigrare_tab.COUNT

   LOOP

      OPEN gen_cv_b FOR

           SELECT *
           FROM   CNR_INVENTARIO_BENI_MIG_DETT
           WHERE  pg_caricamento = beniDaMigrare_tab(i).pg_caricamento AND
                  id_bene_origine = beniDaMigrare_tab(i).id_bene_origine
           ORDER BY pg_caricamento,
                    id_bene_origine,
                    pg_movimento;

      LOOP

         FETCH gen_cv_b INTO
               aRecCnrInventarioBeniMigDett;

         EXIT WHEN gen_cv_b%NOTFOUND;

         aRecCnrInventarioBeniMigOri.cd_tit_sci:=aRecCnrInventarioBeniMigDett.cd_tit_sci;
         aRecCnrInventarioBeniMigOri.cd_ubicazione_sci:=aRecCnrInventarioBeniMigDett.cd_ubicazione_sci;
         aRecCnrInventarioBeniMigOri.id_bene_origine:=aRecCnrInventarioBeniMigDett.id_bene_origine;
         aRecCnrInventarioBeniMigOri.ti_movimento:=aRecCnrInventarioBeniMigDett.ti_movimento;
         aRecCnrInventarioBeniMigOri.ds_bene:=aRecCnrInventarioBeniMigDett.ds_bene;
         aRecCnrInventarioBeniMigOri.cd_categoria_gruppo:=aRecCnrInventarioBeniMigDett.cd_categoria_gruppo;
         aRecCnrInventarioBeniMigOri.esercizio_carico_bene:=aRecCnrInventarioBeniMigDett.esercizio_carico_bene;
         aRecCnrInventarioBeniMigOri.esercizio_coep_bene:=aRecCnrInventarioBeniMigDett.esercizio_coep_bene;
         aRecCnrInventarioBeniMigOri.importo:=aRecCnrInventarioBeniMigDett.importo;
         aRecCnrInventarioBeniMigOri.fl_forza_valore_iniziale:=aRecCnrInventarioBeniMigDett.fl_forza_valore_iniziale;
         aRecCnrInventarioBeniMigOri.valore_ammortizzato:=aRecCnrInventarioBeniMigDett.valore_ammortizzato;
         aRecCnrInventarioBeniMigOri.ti_ammortamento:=aRecCnrInventarioBeniMigDett.ti_ammortamento;
         aRecCnrInventarioBeniMigOri.fl_ammortamento:=aRecCnrInventarioBeniMigDett.fl_ammortamento;
         aRecCnrInventarioBeniMigOri.cd_condizione_bene:=aRecCnrInventarioBeniMigDett.cd_condizione_bene;

         aPgMovimento:=aPgMovimento + 1;

         insCnrInventarioBeniMigScarto(aPgCaricamento,
                                       aPgMovimento,
                                       dataOdierna,
                                       aErrore,
                                       aRecCnrInventarioBeniMigOri);

      END LOOP;

      CLOSE gen_cv_b;

      UPDATE CNR_INVENTARIO_BENI_MIG
      SET    cir_stato_coge = CNRCTB100.STATO_COEP_EXC,
             cir_stato_coan = CNRCTB100.STATO_COEP_EXC,
             stato = STATO_ERRORE
      WHERE  pg_caricamento = beniDaMigrare_tab(i).pg_caricamento AND
             id_bene_origine = beniDaMigrare_tab(i).id_bene_origine;

   END LOOP;

   COMMIT;

END eseguiScartoBeniCompleta;

-- =================================================================================================
-- Crea struttura base del record INVENTARIO_BENI
-- =================================================================================================
FUNCTION buildRecInventarioBeni
   (aRecCnrInventarioBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE
   ) RETURN INVENTARIO_BENI%ROWTYPE IS

   aNrInventarioMax INVENTARIO_BENI.nr_inventario%TYPE;
   aNrInventario INVENTARIO_BENI.nr_inventario%TYPE;

   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;

BEGIN

   -- Recupero del numero inventario iniziale di numerazione dei beni non da migrazione

   aNrInventarioMax:=CNRCTB400.getNrInventarioIniziale(aRecCnrInventarioBeniMig.cir_pg_inventario);

   -- Trovo il valore massimo del numeratore beni attualmente in archivio per un dato inventario e che
   -- derivi da migrazione

   SELECT NVL(MAX(nr_inventario),0) INTO aNrInventario
   FROM   INVENTARIO_BENI
   WHERE  pg_inventario = aRecCnrInventarioBeniMig.cir_pg_inventario AND
          nr_inventario < aNrInventarioMax;

   aNrInventario:=aNrInventario + 1;

   -- Costruzione del record INVENTARIO_BENI

   aRecInventarioBeni:=NULL;
   aRecInventarioBeni.pg_inventario:=aRecCnrInventarioBeniMig.cir_pg_inventario;
   aRecInventarioBeni.nr_inventario:=aNrInventario;
   aRecInventarioBeni.progressivo:=0;
   aRecInventarioBeni.ds_bene:=aRecCnrInventarioBeniMig.cir_ds_bene;
   aRecInventarioBeni.cd_categoria_gruppo:=aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo;
   aRecInventarioBeni.ti_ammortamento:=aRecCnrInventarioBeniMig.cir_ti_ammortamento;
   aRecInventarioBeni.fl_ammortamento:=aRecCnrInventarioBeniMig.cir_fl_ammortamento;
   aRecInventarioBeni.cd_condizione_bene:=aRecCnrInventarioBeniMig.cir_cd_condizione_bene;
   aRecInventarioBeni.ti_commerciale_istituzionale:=aRecCnrInventarioBeniMig.cir_ti_commerc_istituz;
   aRecInventarioBeni.valore_iniziale:=0;
   aRecInventarioBeni.valore_ammortizzato:=0;
   aRecInventarioBeni.variazione_piu:=0;
   aRecInventarioBeni.variazione_meno:=0;
   aRecInventarioBeni.imponibile_ammortamento:=0;
   aRecInventarioBeni.valore_alienazione:=0;
   aRecInventarioBeni.fl_totalmente_scaricato:='N';
   aRecInventarioBeni.cd_cds:=aRecCnrInventarioBeniMig.cir_cd_cds;
   aRecInventarioBeni.cd_unita_organizzativa:=aRecCnrInventarioBeniMig.cir_cd_uo;
   aRecInventarioBeni.cd_ubicazione:=aRecCnrInventarioBeniMig.cir_cd_ubicazione;
   aRecInventarioBeni.dt_validita_variazione:=TRUNC(dataOdierna);
   aRecInventarioBeni.dacr:=dataOdierna;
   aRecInventarioBeni.utcr:=aUtente;
   aRecInventarioBeni.duva:=dataOdierna;
   aRecInventarioBeni.utuv:=aUtente;
   aRecInventarioBeni.pg_ver_rec:=1;
   aRecInventarioBeni.etichetta:=aRecCnrInventarioBeniMig.cir_etichetta;
   aRecInventarioBeni.esercizio_carico_bene:=aRecCnrInventarioBeniMig.cir_esercizio_carico_bene;
   aRecInventarioBeni.id_bene_origine:=aRecCnrInventarioBeniMig.id_bene_origine;
   aRecInventarioBeni.fl_migrato:='Y';

   RETURN aRecInventarioBeni;

END buildRecInventarioBeni;

-- =================================================================================================
-- Esecuzione migrazione base
-- =================================================================================================
PROCEDURE eseguiMigraBeniCompleta
   (aLineaAttivita VARCHAR2
   ) IS

   aFlCreaBene CHAR(1);
   eseguiLock CHAR(1);
   aStatoInvApCh CHAR(1);
   aDataRif DATE;

   i BINARY_INTEGER;

   aRecInventarioBeni INVENTARIO_BENI%ROWTYPE;
   aRecCdr CDR%ROWTYPE;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Inizializzazioni

   aFlCreaBene:='N';
   eseguiLock:='Y';
   aDataRif:=TRUNC(dataOdierna);

   -------------------------------------------------------------------------------------------------
   -- Controlli base

   -- Controllo che la matrice beni sia piena

   IF beniDaMigrare_tab.COUNT = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO ('Matrice gruppo beni da migrare vuota');
   END IF;

   -- Si controlla se il primo record riguarda un carico o una variazione di valore

   IF beniDaMigrare_tab(1).fl_crea_bene = 'Y' THEN
      aFlCreaBene:='Y';
   END IF;

   -- Recupero il record bene
   -- Se non devo creare il bene allora questo non deve esistere altrimenti il bene non deve esistere.
   -- Si costruisce la struttura base del record INVENTARIO_BENI

   IF aFlCreaBene = 'N' THEN
      aRecInventarioBeni:=CNRCTB400.getInventarioBeni(beniDaMigrare_tab(1).cir_pg_inventario,
                                                      beniDaMigrare_tab(1).cir_nr_inventario,
                                                      beniDaMigrare_tab(1).cir_progressivo,
                                                      eseguiLock);
   ELSE
      IF CNRCTB400.checkEsisteBene(beniDaMigrare_tab(1).id_bene_origine) = 'Y' THEN
         IBMERR001.RAISE_ERR_GENERICO
            ('Esiste un record per carico bene e lo stesso id_bene_origine ? gi? presente in INVENTARIO_BENI');
      END IF;
      aRecInventarioBeni:=buildRecInventarioBeni(beniDaMigrare_tab(1));
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Inserimento record su INVENTARIO_BENI

   -- Controllo stato aperto/chiuso dell'inventario

   IF aDataRif > TO_DATE('3112' || aRecInventarioBeni.esercizio_carico_bene, 'DDMMYYYY') THEN
      aDataRif:=TO_DATE('3112' || aRecInventarioBeni.esercizio_carico_bene, 'DDMMYYYY');
   END IF;

   aStatoInvApCh:=CNRCTB400.checkStatoInventApCh(aRecInventarioBeni.pg_inventario,
                                                 aRecInventarioBeni.esercizio_carico_bene,
                                                 aDataRif);

   IF aStatoInvApCh = 'C' THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''inventario ' || aRecInventarioBeni.pg_inventario || ' risulta non aperto alla data per l''esercizio ' || aRecInventarioBeni.esercizio_carico_bene);
   END IF;

   -- Inserimento del record bene

   IF aFlCreaBene = 'Y' THEN
      insInventarioBeni(aRecInventarioBeni);
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Ciclo lettura matrice beni in migrazione

   FOR i IN 1 .. beniDaMigrare_tab.COUNT

   LOOP

      aRecInventarioBeni.valore_iniziale:=aRecInventarioBeni.valore_iniziale + beniDaMigrare_tab(i).cir_valore_iniziale;
      aRecInventarioBeni.variazione_piu:=aRecInventarioBeni.variazione_piu + beniDaMigrare_tab(i).cir_variazione_piu;
      aRecInventarioBeni.variazione_meno:=aRecInventarioBeni.variazione_meno + beniDaMigrare_tab(i).cir_variazione_meno;
      aRecInventarioBeni.imponibile_ammortamento:=aRecInventarioBeni.imponibile_ammortamento +
                                                  beniDaMigrare_tab(i).cir_imponibile_ammortamento;
      aRecInventarioBeni.valore_ammortizzato:=aRecInventarioBeni.valore_ammortizzato +
                                              beniDaMigrare_tab(i).cir_valore_ammortizzato;

      -- Aggiornamento CNR_INVENTARIO_BENI_MIG

      UPDATE CNR_INVENTARIO_BENI_MIG
      SET    cir_nr_inventario = aRecInventarioBeni.nr_inventario,
             cir_stato_coge = CNRCTB100.STATO_COEP_INI,
             cir_stato_coan = CNRCTB100.STATO_COEP_INI,
             stato = STATO_MIGRATO
      WHERE  pg_caricamento = beniDaMigrare_tab(i).pg_caricamento AND
             id_bene_origine = beniDaMigrare_tab(i).id_bene_origine;

   END LOOP;

   -------------------------------------------------------------------------------------------------
   -- Controllo globale di consistenza includendo anche il valore presente sul bene

   checkConsistenzaImporti(aRecInventarioBeni.valore_iniziale + aRecInventarioBeni.variazione_piu - aRecInventarioBeni.variazione_meno,
                           aRecInventarioBeni.imponibile_ammortamento,
                           aRecInventarioBeni.valore_ammortizzato);

   -------------------------------------------------------------------------------------------------
   -- Aggiornamento dei campi valore su INVENTARIO_BENI

   BEGIN

      IF aFlCreaBene = 'Y' THEN

         UPDATE INVENTARIO_BENI
         SET    valore_iniziale = aRecInventarioBeni.valore_iniziale,
                variazione_piu = aRecInventarioBeni.variazione_piu,
                variazione_meno = aRecInventarioBeni.variazione_meno,
                imponibile_ammortamento = aRecInventarioBeni.imponibile_ammortamento,
                valore_ammortizzato = aRecInventarioBeni.valore_ammortizzato
         WHERE  pg_inventario = aRecInventarioBeni.pg_inventario AND
                nr_inventario = aRecInventarioBeni.nr_inventario AND
                progressivo = aRecInventarioBeni.progressivo;

      ELSE

         UPDATE INVENTARIO_BENI
         SET    valore_iniziale = aRecInventarioBeni.valore_iniziale,
                variazione_piu = aRecInventarioBeni.variazione_piu,
                variazione_meno = aRecInventarioBeni.variazione_meno,
                imponibile_ammortamento = aRecInventarioBeni.imponibile_ammortamento,
                valore_ammortizzato = aRecInventarioBeni.valore_ammortizzato,
                duva = dataOdierna,
                utuv = aUtente,
                pg_ver_rec = pg_ver_rec + 1
         WHERE  pg_inventario = aRecInventarioBeni.pg_inventario AND
                nr_inventario = aRecInventarioBeni.nr_inventario AND
                progressivo = aRecInventarioBeni.progressivo;

      END IF;

   EXCEPTION

      WHEN others THEN
           IBMERR001.RAISE_ERR_GENERICO ('Errore in aggiornamento valori su INVENTARIO_BENI');

   END;

   -------------------------------------------------------------------------------------------------
   -- Costruzione record INVENTARIO_UTILIZZATORI_LA

   IF aFlCreaBene = 'Y' THEN

      BEGIN

         aRecCdr:=CNRCTB020.getCDRResponsabileUO(beniDaMigrare_tab(1).cir_cd_uo);

         INSERT INTO INVENTARIO_UTILIZZATORI_LA
                (pg_inventario, nr_inventario,
                 progressivo, cd_utilizzatore_cdr,
                 cd_linea_attivita, percentuale_utilizzo_cdr, percentuale_utilizzo_la,
                 dacr, utcr, duva, utuv, pg_ver_rec)
         VALUES (aRecInventarioBeni.pg_inventario, aRecInventarioBeni.nr_inventario,
                 aRecInventarioBeni.progressivo, aRecCdr.cd_centro_responsabilita,
                 aLineaAttivita, 100, 100,
                 dataOdierna, aUtente, dataOdierna, aUtente, 1);

      EXCEPTION

         WHEN others THEN
              IBMERR001.RAISE_ERR_GENERICO ('Errore in aggiornamento valori su INVENTARIO_UTILIZZATORI_LA');

      END;

   END IF;

   COMMIT;

END eseguiMigraBeniCompleta;

-- =================================================================================================
-- Caricamento dati per migrazione beni in INVENTARIO_BENI e INVENTARIO_UTILIZZATORI_LA a partire
-- dalla tabella CNR_INVENTARIO_BENI_MIG
-- =================================================================================================
PROCEDURE caricaBeniCompleta
   (aPgCaricamento NUMBER,
    aCdCds VARCHAR2,
    aCdUo VARCHAR2,
    aPgInventario NUMBER,
    aLineaAttivita VARCHAR2,
    aNumRecLetto IN OUT NUMBER,
    aNumRecScritto IN OUT NUMBER,
    aNumRecScarto IN OUT NUMBER
   ) IS

   memIdBeneOrigine CNR_INVENTARIO_BENI_MIG.id_bene_origine%TYPE;
   aFlMigrazione CHAR(1);
   aMessaggioErrore VARCHAR2(4000);
   i BINARY_INTEGER;

   aRecCnrInventarioBeniMig CNR_INVENTARIO_BENI_MIG%ROWTYPE;

   gen_cv GenericCurTyp;

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazioni iniziali

   memIdBeneOrigine:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Ciclo principale lettura CNR_INVENTARIO_BENI_MIG

   BEGIN

      IF    (aCdCds IS NULL AND
             aCdUo IS NULL AND
             aPgInventario IS NULL) THEN

            OPEN gen_cv FOR

                 SELECT *
                 FROM   CNR_INVENTARIO_BENI_MIG
                 WHERE  stato = STATO_INIZIALE
                 ORDER BY id_bene_origine,
                          pg_caricamento;

      ELSIF (aCdCds IS NOT NULL AND
             aCdUo IS NOT NULL AND
             aPgInventario IS NULL) THEN

            OPEN gen_cv FOR

                 SELECT *
                 FROM   CNR_INVENTARIO_BENI_MIG
                 WHERE  stato = STATO_INIZIALE AND
                        cir_cd_cds = aCdCds AND
                        cir_cd_uo = aCdUo
                 ORDER BY id_bene_origine,
                          pg_caricamento;

      ELSIF (aCdCds IS NULL AND
             aCdUo IS NULL AND
             aPgInventario IS NOT NULL) THEN

            OPEN gen_cv FOR

                 SELECT *
                 FROM   CNR_INVENTARIO_BENI_MIG
                 WHERE  stato = STATO_INIZIALE AND
                        cir_pg_inventario = aPgInventario
                 ORDER BY id_bene_origine,
                          pg_caricamento;

      ELSIF (aCdCds IS NOT NULL AND
             aCdUo IS NOT NULL AND
             aPgInventario IS NOT NULL) THEN

            OPEN gen_cv FOR

                 SELECT *
                 FROM   CNR_INVENTARIO_BENI_MIG
                 WHERE  stato = STATO_INIZIALE AND
                        cir_pg_inventario = aPgInventario AND
                        cir_cd_cds = aCdCds AND
                        cir_cd_uo = aCdUo
                 ORDER BY id_bene_origine,
                          pg_caricamento;

      END IF;

      LOOP

         BEGIN

            FETCH gen_cv INTO
                  aRecCnrInventarioBeniMig;

            EXIT WHEN gen_cv%NOTFOUND;

            aNumRecLetto:=aNumRecLetto + 1;

            -- E' il primo record letto

            IF memIdBeneOrigine IS NULL THEN
               memIdBeneOrigine:=aRecCnrInventarioBeniMig.id_bene_origine;
               beniDaMigrare_tab.DELETE;
               i:=0;
            END IF;

            -- Se l'identificativo del bene origine ? diverso da quello memorizzato allora indico l'esecuzione
            -- della migrazione

            IF aRecCnrInventarioBeniMig.id_bene_origine != memIdBeneOrigine THEN
               aFlMigrazione:='Y';
            END IF;

            -- Esecuzione della migrazione e azzeramento della tabella per memorizzare la nuova occorrenza

            BEGIN

               IF aFlMigrazione = 'Y' THEN
                  eseguiMigraBeniCompleta(aLineaAttivita);
                  aNumRecScritto:=aNumRecScritto + beniDaMigrare_tab.COUNT;
                  beniDaMigrare_tab.DELETE;
                  i:=0;
               END IF;

            EXCEPTION

               WHEN others THEN
                    aMessaggioErrore:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
                    ROLLBACK;
                    eseguiScartoBeniCompleta(aPgCaricamento,
                                             aMessaggioErrore);
                    aNumRecScarto:=aNumRecScarto + beniDaMigrare_tab.COUNT;
                    beniDaMigrare_tab.DELETE;
                    i:=0;

            END;

            -- Memorizzo sulla tabella di appoggio i beni

            aFlMigrazione:='N';
            i:=i + 1;
            memIdBeneOrigine:=aRecCnrInventarioBeniMig.id_bene_origine;
            beniDaMigrare_tab(i).pg_caricamento:=aRecCnrInventarioBeniMig.pg_caricamento;
            beniDaMigrare_tab(i).id_bene_origine:=aRecCnrInventarioBeniMig.id_bene_origine;
            beniDaMigrare_tab(i).fl_crea_bene:=aRecCnrInventarioBeniMig.fl_crea_bene;
            beniDaMigrare_tab(i).cir_cd_cds:=aRecCnrInventarioBeniMig.cir_cd_cds;
            beniDaMigrare_tab(i).cir_cd_uo:=aRecCnrInventarioBeniMig.cir_cd_uo;
            beniDaMigrare_tab(i).cir_pg_inventario:=aRecCnrInventarioBeniMig.cir_pg_inventario;
            beniDaMigrare_tab(i).cir_nr_inventario:=aRecCnrInventarioBeniMig.cir_nr_inventario;
            beniDaMigrare_tab(i).cir_progressivo:=aRecCnrInventarioBeniMig.cir_progressivo;
            beniDaMigrare_tab(i).cir_ds_bene:=aRecCnrInventarioBeniMig.cir_ds_bene;
            beniDaMigrare_tab(i).cir_cd_categoria_gruppo:=aRecCnrInventarioBeniMig.cir_cd_categoria_gruppo;
            beniDaMigrare_tab(i).cir_ti_ammortamento:=aRecCnrInventarioBeniMig.cir_ti_ammortamento;
            beniDaMigrare_tab(i).cir_fl_ammortamento:=aRecCnrInventarioBeniMig.cir_fl_ammortamento;
            beniDaMigrare_tab(i).cir_cd_condizione_bene:=aRecCnrInventarioBeniMig.cir_cd_condizione_bene;
            beniDaMigrare_tab(i).cir_ti_commerc_istituz:=aRecCnrInventarioBeniMig.cir_ti_commerc_istituz;
            beniDaMigrare_tab(i).cir_valore_iniziale:=aRecCnrInventarioBeniMig.cir_valore_iniziale;
            beniDaMigrare_tab(i).cir_valore_ammortizzato:=aRecCnrInventarioBeniMig.cir_valore_ammortizzato;
            beniDaMigrare_tab(i).cir_variazione_piu:=aRecCnrInventarioBeniMig.cir_variazione_piu;
            beniDaMigrare_tab(i).cir_variazione_meno:=aRecCnrInventarioBeniMig.cir_variazione_meno;
            beniDaMigrare_tab(i).cir_imponibile_ammortamento:=aRecCnrInventarioBeniMig.cir_imponibile_ammortamento;
            beniDaMigrare_tab(i).cir_cd_ubicazione:=aRecCnrInventarioBeniMig.cir_cd_ubicazione;
            beniDaMigrare_tab(i).cir_etichetta:=aRecCnrInventarioBeniMig.cir_etichetta;
            beniDaMigrare_tab(i).cir_esercizio_carico_bene:=aRecCnrInventarioBeniMig.cir_esercizio_carico_bene;
            beniDaMigrare_tab(i).cir_stato_coge:=aRecCnrInventarioBeniMig.cir_stato_coge;
            beniDaMigrare_tab(i).cir_stato_coan:=aRecCnrInventarioBeniMig.cir_stato_coan;
            beniDaMigrare_tab(i).stato:=aRecCnrInventarioBeniMig.stato;
            beniDaMigrare_tab(i).dt_creazione:=aRecCnrInventarioBeniMig.dt_creazione;

         END;

      END LOOP;

      CLOSE gen_cv;

      BEGIN

         eseguiMigraBeniCompleta(aLineaAttivita);
         aNumRecScritto:=aNumRecScritto + beniDaMigrare_tab.COUNT;
         beniDaMigrare_tab.DELETE;
         i:=0;

      EXCEPTION

         WHEN others THEN
              aMessaggioErrore:=SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900);
              ROLLBACK;
              eseguiScartoBeniCompleta(aPgCaricamento,
                                       aMessaggioErrore);
              aNumRecScarto:=aNumRecScarto + beniDaMigrare_tab.COUNT;
              beniDaMigrare_tab.DELETE;
              i:=0;

      END;

   END;

END caricaBeniCompleta;

-- =================================================================================================
-- Migrazione dei beni in stato 'I' (iniziale) presenti sulla tabella INVENTARIO_BENI_MIG in CIR
-- =================================================================================================
PROCEDURE migrazioneBeniCompleta
   (aCdCds VARCHAR2,
    aCdUo  VARCHAR2,
    aPgInventario NUMBER) IS

   aPgCaricamento NUMBER(10);
   aPgLog INTEGER;
   aNumRecLetto NUMBER(10);
   aNumRecScritto NUMBER(10);
   aNumRecScarto NUMBER(10);
   aLineaAttivita VARCHAR2(100);

BEGIN

   -------------------------------------------------------------------------------------------------
   -- Valorizzazioni iniziali

   dataOdierna:=SYSDATE;
   aEsercizioBase:=TO_NUMBER(TO_CHAR(dataOdierna,'YYYY'));
   aNumRecLetto:=0;
   aNumRecScritto:=0;
   aNumRecScarto:=0;

   -------------------------------------------------------------------------------------------------
   -- Controlla parametri

   IF (
         (aCdCds IS NULL AND
          aCdUo IS NOT NULL)
       OR
         (aCdCds IS NOT NULL AND
          aCdUo IS NULL)
      ) THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('L''identificativo del Cds e della UO devono essere entrambi valorizzati o nulli');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Recupero informazioni base

   -- Acquisizione semaforo applicativo

   CNRCTB800.acquisisciSemStaticoCds(0, '*', SEMAFORO, aUtente);

   -- Recupero progressivo log

   aPgLog:= IBMUTL200.LOGSTART('MIGRAZIONE INVENTARIO - FASE CARICAMENTO IN CIR' , aUtente, NULL, NULL);

   -- Recupero del progressivo di caricamento. Si recupera il valore da CNR_INVENTARIO_BENI_MIG e
   -- da CNR_INVENTARIO_BENI_MIG_SCARTO e si seleziona il maggiore dei due

   SELECT MAX(valore) INTO aPgCaricamento
   FROM   (SELECT NVL(MAX(pg_caricamento),0) valore
           FROM   CNR_INVENTARIO_BENI_MIG
           UNION ALL
           SELECT NVL(MAX(pg_caricamento),0) valore
           FROM   CNR_INVENTARIO_BENI_MIG_SCARTO);

   aPgCaricamento:=aPgCaricamento + 1;

   -- Recupero dela linea di attivit? per la valorizzazione dell'utilizzatore

   aLineaAttivita:=CNRCTB015.getVal01PerChiave(0,
                                               CONFIG_CNR_KEY1,
                                               CONFIG_CNR_KEY2);
   IF aLineaAttivita IS NULL THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('La linea attivit? speciale per migrazione beni non ? valorizzata in CONFIGURAZIONE_CNR');
   END IF;

   -------------------------------------------------------------------------------------------------
   -- Migrazione beni

   caricaBeniCompleta(aPgCaricamento,
                      aCdCds,
                      aCdUo,
                      aPgInventario,
                      aLineaAttivita,
                      aNumRecLetto,
                      aNumRecScritto,
                      aNumRecScarto);

   -- Controllo esecuzione della migrazione

   IF aNumRecLetto = 0 THEN
      IBMERR001.RAISE_ERR_GENERICO
         ('Nessun record elaborato, non sono stati trovati beni in CNR_INVENTARIO_BENI');
   END IF;

   COMMIT;

   -- Messaggio di operazione completata ad utente

   IF aNumRecScarto = 0 THEN
      IBMUTL200.logInf(aPgLog,
                       FASE_DUE || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       'Operazione completata con successo',
                       'Record letti ' || LPAD(aNumRecLetto,10,' ') || ' ' ||
                       'Record scritti ' || LPAD(aNumRecScritto,10,' ') || ' ' ||
                       'Record scartati ' || LPAD(aNumRecScarto,10,' '),
                       NULL);
   ELSE
      IBMUTL200.logWar(aPgLog,
                       FASE_DUE || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       'Operazione completata con successo ma con errori',
                       'Record letti ' || LPAD(aNumRecLetto,10,' ') || ' ' ||
                       'Record scritti ' || LPAD(aNumRecScritto,10,' ') || ' ' ||
                       'Record scartati ' || LPAD(aNumRecScarto,10,' '),
                       NULL);
   END IF;

   -- Cancellazione record da CNR_INVENTARIO_BENI_MIG

--   delCnrInventarioBeniMigOri(aPgCaricamento);
   CNRCTB800.liberaSemStaticoCds(0, '*', SEMAFORO, aUtente);

EXCEPTION

   WHEN others THEN
        ROLLBACK;

   IBMUTL200.LOGERR(aPgLog,
                    FASE_DUE || ' ' || TO_CHAR(dataOdierna,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                    'Operazione in errore',
                    SUBSTR(DBMS_UTILITY.FORMAT_ERROR_STACK,1,3900),
                    NULL);

--   delCnrInventarioBeniMigOri(aPgCaricamento);
   CNRCTB800.liberaSemStaticoCds(0, '*', SEMAFORO, aUtente);

END migrazioneBeniCompleta;
PROCEDURE insInventarioBeni
   (aRecInventarioBeni INVENTARIO_BENI%Rowtype) IS

BEGIN

   INSERT INTO INVENTARIO_BENI
          (pg_inventario,
           nr_inventario,
           progressivo,
           ds_bene,
           cd_categoria_gruppo,
           ti_ammortamento,
           fl_ammortamento,
           cd_condizione_bene,
           ti_commerciale_istituzionale,
           valore_iniziale,
           valore_ammortizzato,
           variazione_piu,
           variazione_meno,
           imponibile_ammortamento,
           valore_alienazione,
           fl_totalmente_scaricato,
           collocazione,
           cd_cds,
           cd_unita_organizzativa,
           cd_ubicazione,
           cd_assegnatario,
           dt_validita_variazione,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           etichetta,
           esercizio_carico_bene,
           id_bene_origine,
           fl_migrato)
   VALUES (aRecInventarioBeni.pg_inventario,
           aRecInventarioBeni.nr_inventario,
           aRecInventarioBeni.progressivo,
           aRecInventarioBeni.ds_bene,
           aRecInventarioBeni.cd_categoria_gruppo,
           aRecInventarioBeni.ti_ammortamento,
           aRecInventarioBeni.fl_ammortamento,
           aRecInventarioBeni.cd_condizione_bene,
           aRecInventarioBeni.ti_commerciale_istituzionale,
           aRecInventarioBeni.valore_iniziale,
           aRecInventarioBeni.valore_ammortizzato,
           aRecInventarioBeni.variazione_piu,
           aRecInventarioBeni.variazione_meno,
           aRecInventarioBeni.imponibile_ammortamento,
           aRecInventarioBeni.valore_alienazione,
           aRecInventarioBeni.fl_totalmente_scaricato,
           aRecInventarioBeni.collocazione,
           aRecInventarioBeni.cd_cds,
           aRecInventarioBeni.cd_unita_organizzativa,
           aRecInventarioBeni.cd_ubicazione,
           aRecInventarioBeni.cd_assegnatario,
           aRecInventarioBeni.dt_validita_variazione,
           aRecInventarioBeni.dacr,
           aRecInventarioBeni.utcr,
           aRecInventarioBeni.duva,
           aRecInventarioBeni.utuv,
           aRecInventarioBeni.pg_ver_rec,
           aRecInventarioBeni.etichetta,
           aRecInventarioBeni.esercizio_carico_bene,
           aRecInventarioBeni.id_bene_origine,
           aRecInventarioBeni.fl_migrato
           );

END insInventarioBeni;
END;


GRANT EXECUTE ON CNRMIG400 TO INVENTARIO_ROLE;

