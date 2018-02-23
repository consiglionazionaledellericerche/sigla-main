--------------------------------------------------------
--  DDL for Package CNRCTB800
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB800" as
--
-- CNRCTB800 - Package semaforo applicativo
-- Date: 23/09/2004
-- Version: 1.2
--
-- Package per la semaforizzazione di funzioni applicative
--
-- UTILIZZA AUTONOMOUS TRANSACTION PER L'INSERIMENTO DELLA RIGA IN SEMAFORO_BASE
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 22/01/2003
-- Version: 1.0
-- Creazione
--
-- Date: 15/09/2003
-- Version: 1.1
-- Documentazione
--
-- Date: 23/09/2004
-- Version: 1.2
-- Introduzione del semaforo statico
--
-- Constants:

INDEFINITO CONSTANT VARCHAR2(1) := '*';

-- Costanti per semafori statici

SEMAFORO_ROSSO CONSTANT CHAR(1) := 'R';
SEMAFORO_VERDE CONSTANT CHAR(1) := 'G';

-- Functions e Procedures:
-- =======================
--
-- Pre-post-name: Acquisisce semaforo applicativo
-- pre: viene richiesta l'acquisizione del semaforo applicativo utilizzando i parametri specificati
-- post: il semaforo applicativo è un lock esclusivo su di una riga della tabella SEMAFORO_BASE
--       che specifica un tipo oin un contesto di 4 parametri di cui alcuni necessari e altri non significativi.
--       I parametri sono: cds, uo, cdr, esercizio. Il motore di semaforizzazione provvede ad inserire la riga nella tabella
--       semaforo_base nel caso tale riga non esista.
--       I parametri specificati devono soddisfare la logica di pattern definita in SEMAFORO_TIPO, come segue.
--       Pattern di specifica parametri semaforo:
--       il pattern è composto da 4 caratteri del set * $
--         carattere 1 -> cd_cds è un parametro del semaforo ($) altrimenti (*)
--         carattere 2 -> cd_unita_organizzativa è un parametro del semaforo ($) altrimenti (*)
--         carattere 3 -> cd_centro_responsabilita è un parametro del semaforo ($) altrimenti (*)
--         carattere 4 -> esercizio è un parametro del semaforo ($) altrimenti (*)
--       In fase di inserimento (automatica) del record iniziale del semaforo, viene controllato che
--       i quattro parametri siano specificati (not null) e che abbiano un valore significativo o * per le
--       stringhe (primi 3) o 0 per esercizio in dipendenza del pattern specificato.
--       Esempio:
--        (*,'000',*,0) è compatibile con il pattern *$** ma non (*,'000',*,2002) perchè l''esercizio è stato
--        specificato mentre il pattern non lo prevedeva.
--
-- Parametri:
--     aEs -> Esercizio contabile
--     aCdUO -> Codice unita_organizzativa
--     aTipoSem -> Tipologia del semaforo semaforo_tipo (tipologie registrate in tabella SEMAFORO_TIPO)
--     aUser -> Utente che acquisice il semaforo
--
 procedure acquisisciSemaforo(aEs number, aCdUO varchar2, aTipoSem varchar2, aUser varchar2);

 procedure ins_SEMAFORO_BASE (aDest SEMAFORO_BASE%rowtype);

-- Pre-post-name: Acquisisce semaforo applicativo statico
--                La lettura del semaforo corrisponde ad una transazione committante
--                Lo stato della trasazione virtuale è nel campo stato presente sul semaforo (valori possibili R->Rosso G->Verde)
-- pre: viene richiesta l'acquisizione del semaforo applicativo utilizzando i parametri specificati
-- post: il semaforo applicativo è un record della tabella SEMAFORO_STATICO
--       che specifica un tipo in un contesto di 4 parametri di cui alcuni necessari e altri non significativi.
--       I parametri sono: cds, uo, cdr, esercizio. Il motore di semaforizzazione provvede ad inserire la riga nella tabella
--       semaforo_base nel caso tale riga non esista.
--       I parametri specificati devono soddisfare la logica di pattern definita in SEMAFORO_TIPO, come segue.
--       Pattern di specifica parametri semaforo:
--       il pattern è composto da 4 caratteri del set * $
--         carattere 1 -> cd_cds è un parametro del semaforo ($) altrimenti (*)
--         carattere 2 -> cd_unita_organizzativa è un parametro del semaforo ($) altrimenti (*)
--         carattere 3 -> cd_centro_responsabilita è un parametro del semaforo ($) altrimenti (*)
--         carattere 4 -> esercizio è un parametro del semaforo ($) altrimenti (*)
--       In fase di inserimento (automatica) del record iniziale del semaforo, viene controllato che
--       i quattro parametri siano specificati (not null) e che abbiano un valore significativo o * per le
--       stringhe (primi 3) o 0 per esercizio in dipendenza del pattern specificato.
--       Esempio:
--        (*,'000',*,0) è compatibile con il pattern *$** ma non (*,'000',*,2002) perchè l''esercizio è stato
--        specificato mentre il pattern non lo prevedeva.
--       Risultati acquisizione:
--		 Viene creato un record nella tabella SEMAFORO_STATICO in stato R (Rosso)
--
-- Parametri:
--     aEs -> Esercizio contabile
--     aCdCds -> Codice del CDS
--     aTipoSem -> Tipologia del semaforo semaforo_tipo (tipologie registrate in tabella SEMAFORO_TIPO)
--     aUser -> Utente che acquisice il semaforo

 procedure acquisisciSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2);

-- Pre-post-name: Libera semaforo applicativo statico
-- pre: viene richiesta la liberazione del semaforo applicativo utilizzando i parametri specificati
-- post: viene cambiato a G (Verde) lo stato del record corrispondente nella tabella SEMAFORO_STATICO
--
-- Parametri:
--     aEs -> Esercizio contabile
--     aCdCds -> Codice del CDS
--     aTipoSem -> Tipologia del semaforo semaforo_tipo (tipologie registrate in tabella SEMAFORO_TIPO)
--     aUser -> Utente che acquisice il semaforo

 procedure liberaSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2);

-- Pre-post-name: Check semaforo acquisito
-- pre: viene richiesto di verificare l'esistenza di un semaforo statico di un certo tipo per cds
--
-- Parametri:
--     aEs -> Esercizio contabile
--     aCdCds -> Codice del CDS
--     aTipoSem -> Tipologia del semaforo semaforo_tipo (tipologie registrate in tabella SEMAFORO_TIPO)

 function isSemStaticoCdsBloccato(aEs number, aCdCds varchar2, aTipoSem varchar2) return boolean;

 procedure ins_SEMAFORO_STATICO (aDest SEMAFORO_STATICO%rowtype);

end;
