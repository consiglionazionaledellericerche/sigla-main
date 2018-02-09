CREATE OR REPLACE package CNRCTB800 as
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
-- post: il semaforo applicativo ? un lock esclusivo su di una riga della tabella SEMAFORO_BASE
--       che specifica un tipo oin un contesto di 4 parametri di cui alcuni necessari e altri non significativi.
--       I parametri sono: cds, uo, cdr, esercizio. Il motore di semaforizzazione provvede ad inserire la riga nella tabella
--       semaforo_base nel caso tale riga non esista.
--       I parametri specificati devono soddisfare la logica di pattern definita in SEMAFORO_TIPO, come segue.
--       Pattern di specifica parametri semaforo:
--       il pattern ? composto da 4 caratteri del set * $
--         carattere 1 -> cd_cds ? un parametro del semaforo ($) altrimenti (*)
--         carattere 2 -> cd_unita_organizzativa ? un parametro del semaforo ($) altrimenti (*)
--         carattere 3 -> cd_centro_responsabilita ? un parametro del semaforo ($) altrimenti (*)
--         carattere 4 -> esercizio ? un parametro del semaforo ($) altrimenti (*)
--       In fase di inserimento (automatica) del record iniziale del semaforo, viene controllato che
--       i quattro parametri siano specificati (not null) e che abbiano un valore significativo o * per le
--       stringhe (primi 3) o 0 per esercizio in dipendenza del pattern specificato.
--       Esempio:
--        (*,'000',*,0) ? compatibile con il pattern *$** ma non (*,'000',*,2002) perch? l''esercizio ? stato
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
--                Lo stato della trasazione virtuale ? nel campo stato presente sul semaforo (valori possibili R->Rosso G->Verde)
-- pre: viene richiesta l'acquisizione del semaforo applicativo utilizzando i parametri specificati
-- post: il semaforo applicativo ? un record della tabella SEMAFORO_STATICO
--       che specifica un tipo in un contesto di 4 parametri di cui alcuni necessari e altri non significativi.
--       I parametri sono: cds, uo, cdr, esercizio. Il motore di semaforizzazione provvede ad inserire la riga nella tabella
--       semaforo_base nel caso tale riga non esista.
--       I parametri specificati devono soddisfare la logica di pattern definita in SEMAFORO_TIPO, come segue.
--       Pattern di specifica parametri semaforo:
--       il pattern ? composto da 4 caratteri del set * $
--         carattere 1 -> cd_cds ? un parametro del semaforo ($) altrimenti (*)
--         carattere 2 -> cd_unita_organizzativa ? un parametro del semaforo ($) altrimenti (*)
--         carattere 3 -> cd_centro_responsabilita ? un parametro del semaforo ($) altrimenti (*)
--         carattere 4 -> esercizio ? un parametro del semaforo ($) altrimenti (*)
--       In fase di inserimento (automatica) del record iniziale del semaforo, viene controllato che
--       i quattro parametri siano specificati (not null) e che abbiano un valore significativo o * per le
--       stringhe (primi 3) o 0 per esercizio in dipendenza del pattern specificato.
--       Esempio:
--        (*,'000',*,0) ? compatibile con il pattern *$** ma non (*,'000',*,2002) perch? l''esercizio ? stato
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
/


CREATE OR REPLACE package body CNRCTB800 is

 procedure acquisisciSemaforo(aEs number, aCdUO varchar2, aTipoSem varchar2, aUser varchar2) is
  aSem semaforo_base%rowtype;
  aTSNow date;
 begin
  aTSNow:=sysdate;
  begin
   select * into aSem from SEMAFORO_BASE where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO for update nowait;
   update semaforo_base set
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO;
  exception
   when NO_DATA_FOUND then
    aSem.CD_TIPO_SEMAFORO:=aTipoSem;
    aSem.CD_CDS:=INDEFINITO;
    aSem.CD_UNITA_ORGANIZZATIVA:=aCdUO;
    aSem.CD_CENTRO_RESPONSABILITA:=INDEFINITO;
    aSem.ESERCIZIO:=aEs;
    aSem.UTCR:=aUser;
    aSem.DACR:=aTSNow;
    aSem.UTUV:=aUser;
    aSem.DUVA:=aTSNow;
    aSem.PG_VER_REC:=1;
    ins_SEMAFORO_BASE(aSem);
    select * into aSem from SEMAFORO_BASE where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = aCdUO
 	 and cd_cds = INDEFINITO
 	 and cd_centro_responsabilita = INDEFINITO for update nowait;
   when DUP_VAL_ON_INDEX  then
    IBMERR001.RAISE_ERR_GENERICO('Errore interno di acquisizione semaforo applicativo: pi? di un semaforo trovato per la funzione specificata');
   end;
 end;




 procedure ins_SEMAFORO_BASE (aDest SEMAFORO_BASE%rowtype) is
  pragma autonomous_transaction;
 begin
   insert into SEMAFORO_BASE (
     CD_TIPO_SEMAFORO
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_TIPO_SEMAFORO
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
  commit;
 exception when OTHERS then
  rollback;
  raise;
 end;

 -- SEMAFORO STATICO

 function getStatoSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, isLock boolean) return char is
  aStato char;
 begin
  if isLock then
   select stato into aStato from SEMAFORO_STATICO where
        cd_tipo_semaforo = aTipoSem
    and esercizio = aEs
    and cd_unita_organizzativa = INDEFINITO
	and cd_cds = aCdCds
    and cd_centro_responsabilita = INDEFINITO
	for update nowait;
  else
   select stato into aStato from SEMAFORO_STATICO where
        cd_tipo_semaforo = aTipoSem
    and esercizio = aEs
    and cd_unita_organizzativa = INDEFINITO
	and cd_cds = aCdCds
    and cd_centro_responsabilita = INDEFINITO;
  end if;
  return aStato;
 end;

 procedure acquisisciSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2) is
  aSem semaforo_statico%rowtype;
  aTSNow date;
  pragma autonomous_transaction;
 begin
  aTSNow:=sysdate;
  begin
   if (getStatoSemStaticoCds(aEs, aCdCds, aTipoSem, true)=SEMAFORO_ROSSO) then
    IBMERR001.RAISE_ERR_GENERICO('Operazione non permessa al momento (operazione gi? in corso per un altro utente o semaforo applicativo bloccato '||'sem:'||aTipoSem||' es:'||aEs||' cds:'||aCdCds);
   end if;
   update semaforo_statico set
    stato=SEMAFORO_ROSSO,
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = INDEFINITO
 	 and cd_cds = aCdCds
 	 and cd_centro_responsabilita = INDEFINITO;
   COMMIT;
  exception
   when NO_DATA_FOUND then
    aSem.CD_TIPO_SEMAFORO:=aTipoSem;
    aSem.CD_CDS:=aCdCds;
    aSem.CD_UNITA_ORGANIZZATIVA:=INDEFINITO;
    aSem.CD_CENTRO_RESPONSABILITA:=INDEFINITO;
    aSem.ESERCIZIO:=aEs;
	aSem.STATO:=SEMAFORO_ROSSO;
    aSem.UTCR:=aUser;
    aSem.DACR:=aTSNow;
    aSem.UTUV:=aUser;
    aSem.DUVA:=aTSNow;
    aSem.PG_VER_REC:=1;
    ins_SEMAFORO_STATICO(aSem);
    COMMIT;
   when OTHERS then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Errore in fase di acquisizione del semaforo applicativo');
   end;
 end;

 function isSemStaticoCdsBloccato(aEs number, aCdCds varchar2, aTipoSem varchar2) return boolean is
 begin
  if(getStatoSemStaticoCds(aEs, aCdCds, aTipoSem, false)=SEMAFORO_ROSSO) then
   return true;
  end if;
  return false;
 exception when NO_DATA_FOUND then
  return false;
 end;

 procedure liberaSemStaticoCds(aEs number, aCdCds varchar2, aTipoSem varchar2, aUser varchar2) is
  aTSNow date;
  pragma autonomous_transaction;
 begin
  aTSNow:=sysdate;
  begin
   -- La lettura non ? effettuata in modo lockante per evitare che al momento del riulascio del semaforo
   -- Qualcun altro se ne sia impadronito (anche solo per controllo)
   if not (getStatoSemStaticoCds(aEs, aCdCds, aTipoSem,false)=SEMAFORO_ROSSO) then
    IBMERR001.RAISE_ERR_GENERICO('Tentativo di liberazione di un semaforo statico non acquisito in precedenza');
   end if;
   update semaforo_statico set
    stato=SEMAFORO_VERDE,
    utuv=aUser,
	duva=aTSNow,
	pg_ver_rec=pg_ver_rec+1
   where
         cd_tipo_semaforo = aTipoSem
     and esercizio = aEs
     and cd_unita_organizzativa = INDEFINITO
 	 and cd_cds = aCdCds
 	 and cd_centro_responsabilita = INDEFINITO;
   COMMIT;
  exception
   when NO_DATA_FOUND then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Tentativo di liberazione di un semaforo statico non acquisito in precedenza');
   when OTHERS then
    ROLLBACK;
    IBMERR001.RAISE_ERR_GENERICO('Errore in fase di rilascio del semaforo applicativo');
  end;
 end;

 procedure ins_SEMAFORO_STATICO (aDest SEMAFORO_STATICO%rowtype) is
 begin
   insert into SEMAFORO_STATICO (
     CD_TIPO_SEMAFORO
    ,CD_CDS
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO
	,STATO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.CD_TIPO_SEMAFORO
    ,aDest.CD_CDS
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO
	,aDest.STATO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;

end;
/


