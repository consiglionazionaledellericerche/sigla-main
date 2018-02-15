--------------------------------------------------------
--  DDL for Package CNRCTB039
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB039" as
--
-- CNRCTB039 - Metodi di controllo associazione tra doc autorizzatori
-- Date: 19/07/2006
-- Version: 1.5
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 15/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 19/05/2003
-- Version: 1.1
-- Documentazione + gestione mandato stipendi
--
-- Date: 20/05/2003
-- Version: 1.2
-- Fix errore su annullamento del mandato di liquidazione CORI
--
-- Date: 26/05/2003
-- Version: 1.3
-- Esclusi dal controllo i mandati di accreditamento e regolarizzazione
--
-- Date: 19/06/2003
-- Version: 1.4
-- Errore in controllo reversali associate a liquidazioni gruppi CORI
--
-- Date: 19/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 08/04/2011
-- Version: 1.6
-- Gestione blocco annullamento mandato quando la liquidazione cori è stata effettuata dalla 999
--
-- Constants:

-- Functions e Procedures:

-- Tale metodo deve essere chiamato all'interno di un metodo che aggiorni utuv,duva e pg_ver rec
--
-- Pre-post name: Controllo di annullabilità del documento autorizzatorio specificato
-- Pre condizione: Il documento passato è un mandato o una reversale non dipendente da altro mandato tramite
--                 associazioni ASS_MANDATO_REVERSALE o ASS_MANDATO_MANDATO
-- Post condizione:
--  Se si tratta di mandato:
--   Se il mandato è quello degli stipendi solleva eccezzione di non annullabilità
--   Se il mandato è di liquidazione CORI
--    Carica le reversali associate
--     Se num. revers. coll. = 0 => ritorna senza sollevare eccezioni
--     Se num. revers. coll. >= 1 =>
--      Cicla sulle reversali associate
--       Se una di queste non è collegata a GENERICO DI VERSAMENTO DI ENTRATA solleva eccezione
--   Se il mandato ha più di una riga non è legato a compensi:
--    verifica l'esistenza di associazioni con reversali effettuate necessariamente a mano:
--    nel caso selleva eccezione
--   Se il mandato non è principale di compenso
--    verifica l'esistenza di associazioni con reversali effettuate necessariamente a mano:
--    nel caso selleva eccezione
--   Se il mandato è principale di compenso
--    Cicla su tutte le reversali figlie di questo mandato
--     Per ogni figlio devo verificare se appartenenva o meno all pratica principale
--     del compenso del mandato principale a cui tale reversale è collegata
--     nel caso la reversale non appartenga allo stesso compenso del mandato principale -> l'associazione
--     è stata fatta a mano e quindi viene sollevata eccezione
--    Cicla su tutti i mandati figli e verifico se hanno relazioni in ASS_MANDATO_REVERSALE:
--     nel caso sono state effettuate manualmente e quindi sollevo eccezione
--  Se si tratta di reversale, questa è necessariamente reversale paritetica di compenso senza mandato principale
--   Cicla su tutte le reversali/mandati figli dello stesso compenso (compresa quella in processo)
--   Per ogni figlio devo verificare che non sia presente in ASS_MANDATO_REVERSALE:
--   nel caso solleva eccezione
--
-- Parametri:
--  aTipoDoc -> M per mandato R per reveresale
--  aEs -> esercizio
--  aCdCds -> codice cds
--  aPgDoc -> progressivo documento autorizzatorio
--


 procedure checkAnnullabilita(
  aTipoDoc varchar2,
  aEs number,
  aCdCds varchar2,
  aPgDoc number
 );

end;
