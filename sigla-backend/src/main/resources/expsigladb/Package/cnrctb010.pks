CREATE OR REPLACE package CNRCTB010 as
--
-- CNRCTB010 - Package di gestione delle linee di attivita (comuni e non) e insieme di linee di attivita
-- Date: 17/04/2002
-- Version: 2.2
--
-- L'aggiunzione un CDR
-- produce l'inserimento delle associazioni con tipi di linee di attivita proprie e di sistema.
--
-- Dependency: CNRCTB008 CNRCTB020 IBMUTL001 IBMERR001
--
-- History:
--
-- Date: 29/08/2001
-- Version: 1.0
-- Creazione
-- Date: 29/08/2001
-- Version: 1.1
-- Aggiunto aggiornamento indotto da inserimento(eliminazione di cdr
--
-- Date: 16/09/2001
-- Version: 1.2
-- Aggiunte tipo di linee di attivita speciali (sistema)/procedure di inserimento della linea di attivita
--
-- Date: 02/10/2001
-- Version: 1.3
-- Aggiunte metodi di creazione di dati di test in automatico
--
-- Date: 16/10/2001
-- Version: 1.4
-- Spostati in CNRTST010 i metodi di generazione dei dati di test
-- Gestione del tipo unita ENTE
--
-- Date: 18/10/2001
-- Version: 1.5
-- Aggiunti metodi per l'estrazione e creazione delle linee di attivita SAUOP
--
-- Date: 08/11/2001
-- Version: 1.6
-- Gestione della Linea di attivita
--
-- Date: 15/11/2001
-- Version: 1.7
-- Gestione dell'inserimento delle associazioni in automatico per tipi di la comuni e di sistema
-- Rimozione delle procedure di inseirmnto ed eliminazione delle linee di attivita comuni

-- Date: 16/11/2001
-- Version: 1.8
-- Fix errori per migrazione eliminazione esercizio da STO

-- Date: 20/02/2002
-- Version: 1.9
-- Gestione INSIEME e SEZIONE E/S in Linea di attivita e Tipo linea attivita

-- Date: 18/03/2002
-- Version: 2.0
-- Allineamento con versione 1.9.0 correttiva/evolutiva
-- Aggiunta gestione dell'insieme automatico in scarico verso area

-- Date: 21/03/2002
-- Version: 2.1
-- Tolto esercizio_inizio da risultato

-- Date: 17/04/2002
-- Version: 2.2
-- Richiesta CNR 129E - Numerazione insieme per CDR

-- Constants:

-- Tipologie di tipi di linee di attivita
--
-- Tipo LA comune
TI_TIPO_LA_COMUNE CONSTANT varchar2(1) := 'C';
-- Tipo LA di sistema
TI_TIPO_LA_SISTEMA CONSTANT varchar2(1) := 'S';
-- Tipo LA propria
TI_TIPO_LA_PROPRIA CONSTANT varchar2(1) := 'P';

-- Tipi di linee di attivita di SISTEMA
--
-- Tipo LA Spesa verso UO Personale
TI_LA_SAUOP CONSTANT varchar2(10):='SAUOP';
-- Tipo LA Spesa verso altra UO
TI_LA_SAUO CONSTANT varchar2(10):='SAUO';
-- Tipo LA Costo verso altro CDR
TI_LA_CSSAC CONSTANT varchar2(10):='CSSAC';
-- Tipo LA Propria
TI_LA_PROP CONSTANT varchar2(10):='PROP';

-- Sezione linee di attivita
TI_GESTIONE_SPESE CONSTANT CHAR(1) := 'S';
TI_GESTIONE_ENTRATE CONSTANT CHAR(1) := 'E';

-- Denominazione linea di attivita SPESE PER COSTI ALTRUI
SPESE_PER_COSTI_ALTRUI CONSTANT varchar2(300) := 'Spese per costi altrui';

LUNGHEZZA_COD_INSIEME_LA CONSTANT NUMBER := 8;

-- Descrizione insieme collegamento entrate CSSAC/spese automatico in AREA
DESC_INS_SCR_N5_AREA CONSTANT varchar2(200) := 'Insieme di collegamento entrata CSSAC natura 5 con spese dettagli scaricati su AREA';

-- Functions  Procedures:

-- Azioni effettuate alla creazione di un cdr
-- Vengono Aggiunte le associazioni di quel cdr con tutte le linee di attivita proprie e di sistema
-- aCdr -> row type cdr proveniente da trigger di inserimento del CDR

 procedure aggiornaAssTipoLaCdrOnCreaCdr(aCdr cdr%rowtype);

-- Inserisce la linea di attivita dato il rowtype

 procedure ins_LINEA_ATTIVITA (aDest LINEA_ATTIVITA%rowtype);

-- Inserisce l'associazione della linea di attivita con l'esercizio

 procedure ins_ASS_LINEA_ATTIVITA_ESER (aDest ASS_LINEA_ATTIVITA_ESERCIZIO%rowtype);

-- inserimento nella associazione per accentatore

 procedure ins_ASS_LA_CLASS_VOCI (aDest ASS_LA_CLASS_VOCI%rowtype);

-- Inserisce il tipo di linea di attivita dato il rowtype

 procedure ins_TIPO_LINEA_ATTIVITA (aDest TIPO_LINEA_ATTIVITA%rowtype);

-- Inserisce associazione tra tipo di linea di attivita e Cdr dato il rowtype

 procedure ins_ASS_TIPO_LA_CDR (aDest ASS_TIPO_LA_CDR%rowtype);

-- Inserisce un insieme di linee di attivita dato il rowtype

 procedure ins_INSIEME_LA (aDest INSIEME_LA%rowtype);

-- Copia i risultati della linea origine in quella destinazione
-- Si assume che la linea destinazione non abbia risultati

 procedure copiaRisultati(aLADestinazione linea_attivita%rowtype, aLAOrigine linea_attivita%rowtype);

-- Calcola la prossima chiave di una linea di attivita in un dato CDR
-- Da utilizzare solo per linee di attivita Proprie o di Sistema (tipo = 'P', 'S')

 function getNextCodice(tipologia char,aLA linea_attivita%rowtype) return varchar2;

-- Ritorna la linea di attivita SAUOP per il CDR aCDR
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
--

 function getLASAUOP(aEs number, aCdCdr varchar2) return linea_attivita%rowtype;

-- Crea la linea di attivita SAUOP per il CDR aCDR
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
-- aUser -> utenza di creazione del record
--

 function creaLASAUOP(aEs number, aCdCdr varchar2, aUser varchar2) return linea_attivita%rowtype;

-- Ritorna l'insieme automatico per collegamento entrata CSSAC in AREA per natura 5 (Scarico verso AREA)
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
--

 function getInsiemeScrArea(aEs number, aCdCdrArea varchar2) return insieme_la%rowtype;

-- Crea l'insieme automatico per collegamento entrata CSSAC in AREA per natura 5 (Scarico verso AREA)
-- aEs -> esercizio contabile
-- aCdCdr -> codice del centro di responsabilita
-- aUser -> utenza di creazione del record
--

 function creaInsiemeScrArea(aEs number, aCdCdrArea varchar2, aUser varchar2) return insieme_la%rowtype;

end;
/


CREATE OR REPLACE package body CNRCTB010 is

 function getNextCodice(tipologia char,aLA linea_attivita%rowtype) return varchar2 is
  cdLA varchar2(10);
  aLaTemp linea_attivita%rowtype;
  aLC lunghezza_chiavi%rowtype;
 begin
  select max(cd_linea_attivita) into cdLA from linea_attivita where
            CD_CENTRO_RESPONSABILITA=aLA.cd_centro_responsabilita
		and cd_linea_attivita like tipologia||'%'
		and (
		  (tipologia=TI_TIPO_LA_PROPRIA) and (IBMUTL001.isNumeric(substr(cd_linea_attivita,2,length(cd_linea_attivita)-1)) = 'Y')
		 or
          (tipologia=TI_TIPO_LA_SISTEMA)
		);
  if cdLA is null then
   -- Calcola la lunghezza della parte numerica del codice della linea di attivita
   -- per generare la linea 0
   select * into aLC from lunghezza_chiavi
   where
        esercizio = 0
    and tabella = 'LINEA_ATTIVITA'
	and attributo = 'CD_LINEA_ATTIVITA'
	and livello = 1;
    cdLA:=tipologia||(LPAD('1',aLC.lunghezza - 1,'0'));
	return cdLA;
  end if;

  select * into aLaTemp from linea_attivita where
            CD_CENTRO_RESPONSABILITA=aLA.cd_centro_responsabilita
		and cd_linea_attivita = cdLA
	for update nowait;

  cdLA:=tipologia||LPAD(TO_CHAR(TO_NUMBER(substr(cdLa,2,LENGTH(cdLA)-1)) + 1),LENGTH(cdLA)-1,'0');
  return cdLA;
 end;

 procedure copiaRisultati(aLADestinazione linea_attivita%rowtype, aLAOrigine linea_attivita%rowtype) is
 begin
  insert into risultato (
     CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,PG_RISULTATO
    ,CD_TIPO_RISULTATO
    ,DS_RISULTATO
    ,QUANTITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
  ) select
     aLADestinazione.CD_CENTRO_RESPONSABILITA
    ,aLADestinazione.CD_LINEA_ATTIVITA
    ,PG_RISULTATO
    ,CD_TIPO_RISULTATO
    ,DS_RISULTATO
    ,QUANTITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   from risultato where
       cd_centro_responsabilita = aLAOrigine.cd_centro_responsabilita
   and cd_linea_attivita = aLAOrigine.cd_linea_attivita;
 end;

 procedure aggiornaAssTipoLaCdrOnCreaCdr(aCdr cdr%rowtype) is
  aAss ass_tipo_la_Cdr%rowtype;
 begin
  -- Se si tratta di CDR del UO di tipo ENTE nessun aggiornamento e necessario
  if CNRCTB020.isCDRENTE(aCdr) then
   return;
  end if;

  for aTipo in (
   select * from
	tipo_linea_attivita
   where
	    ti_tipo_la = TI_TIPO_LA_PROPRIA
	 or ti_tipo_la = TI_TIPO_LA_SISTEMA
  ) loop
   aAss.cd_tipo_linea_attivita:=aTipo.cd_tipo_linea_attivita;
   aAss.cd_centro_responsabilita:=aCdr.cd_centro_responsabilita;
   aAss.dacr:=aCdr.dacr;
   aAss.utcr:=aCdr.utcr;
   aAss.duva:=aCdr.dacr;
   aAss.utuv:=aCdr.utcr;
   aAss.pg_ver_rec:=1;
   ins_ASS_TIPO_LA_CDR(aAss);
  end loop;
 end;

 function creaLASAUOP(aEs number, aCdCdr varchar2, aUser varchar2) return linea_attivita%rowtype is
  aLASAUOP linea_attivita%rowtype;
  aTSNow date;
 begin
  aTSNow:=sysdate;
  aLASAUOP.esercizio_inizio:=aEs;
  aLASAUOP.esercizio_fine:=CNRCTB008.ESERCIZIO_INFINITO;
  aLASAUOP.cd_centro_responsabilita:=aCdCdr;
  aLASAUOP.cd_tipo_linea_attivita:=TI_LA_SAUOP;
  aLASAUOP.cd_funzione:='01';
  aLASAUOP.cd_natura:='1';
  aLASAUOP.ds_linea_attivita:='Spese per costi altrui';
  aLASAUOP.ti_gestione:=TI_GESTIONE_SPESE;
  aLASAUOP.DACR:=aTSNow;
  aLASAUOP.UTCR:=aUser;
  aLASAUOP.DUVA:=aTSNow;
  aLASAUOP.UTUV:=aUser;
  aLASAUOP.PG_VER_REC:=1;
  aLASAUOP.cd_linea_attivita:=getNextCodice(TI_TIPO_LA_SISTEMA,aLASAUOP);
  INS_LINEA_ATTIVITA(aLASAUOP);
  return aLASAUOP;
 end;

 function creaInsiemeScrArea(aEs number, aCdCdrArea varchar2, aUser varchar2) return insieme_la%rowtype is
  aInsieme insieme_la%rowtype;
  aTempInsieme insieme_la%rowtype;
  aTSNow date;
  aNewCod number;
 begin
  aTSNow:=sysdate;
  select nvl(max(to_number(cd_insieme_la)),0) into aNewCod from insieme_la -- la numerazione degli insiemi e comune all'ente
   where cd_centro_responsabilita = aCdCdrArea; -- Richiesta CNR 129E Numerazione per cdr
  begin -- lettura lockante dell'ultimo insieme inserito
   select * into aTempInsieme from insieme_la where
        cd_insieme_la = aNewCod
    and cd_centro_responsabilita = aCdCdrArea -- Richiesta CNR 129E Numerazione per cdr
   for update nowait;
  exception when NO_DATA_FOUND then
   null;
  end;
  aInsieme.CD_INSIEME_LA:=lpad(to_char(aNewCod+1),LUNGHEZZA_COD_INSIEME_LA,'0');
  aInsieme.DS_INSIEME_LA:=DESC_INS_SCR_N5_AREA;
  aInsieme.CD_CENTRO_RESPONSABILITA:=aCdCdrArea;
  aInsieme.DACR:=aTSNow;
  aInsieme.UTCR:=aUser;
  aInsieme.DUVA:=aTSNow;
  aInsieme.UTUV:=aUser;
  aInsieme.PG_VER_REC:=1;
  ins_INSIEME_LA(aInsieme);
  return aInsieme;
 end;

 function getInsiemeScrArea(aEs number, aCdCdrArea varchar2) return insieme_la%rowtype is
  aInsieme insieme_la%rowtype;
 begin
  begin
   select * into aInsieme from insieme_la a where cd_centro_responsabilita = aCdCdrArea and exists (
    select 1 from linea_attivita
	 where
    	  cd_centro_responsabilita = a.cd_centro_responsabilita
	  and cd_tipo_linea_attivita = TI_LA_CSSAC
	  and cd_natura = '5'
	  and cd_insieme_la = a.cd_insieme_la
   );
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Insieme per collegamento con linea di entrata CSSAC nat. 5 - scarico verso AREA, non trovata');
  end;
  return aInsieme;
 end;

 function getLASAUOP(aEs number, aCdCdr varchar2) return linea_attivita%rowtype is
  aLA linea_attivita%rowtype;
 begin
  select * into aLA from linea_attivita where
       cd_centro_responsabilita = aCdCdr
   and cd_tipo_linea_attivita = TI_LA_SAUOP;
  return aLA;
 end;

procedure ins_LINEA_ATTIVITA (aDest LINEA_ATTIVITA%rowtype) is
  begin
   insert into LINEA_ATTIVITA (
     CD_INSIEME_LA
    ,TI_GESTIONE
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,CD_TIPO_LINEA_ATTIVITA
    ,DENOMINAZIONE
    ,CD_GRUPPO_LINEA_ATTIVITA
    ,CD_FUNZIONE
    ,CD_NATURA
    ,DS_LINEA_ATTIVITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDR_COLLEGATO
    ,CD_LA_COLLEGATO
    ,ESERCIZIO_FINE
    ,ESERCIZIO_INIZIO
    ,PG_PROGETTO
    ,FL_LIMITE_ASS_OBBLIG
    ,cd_cofog
    ,cd_programma
    ,cd_missione
   ) values (
     aDest.CD_INSIEME_LA
    ,aDest.TI_GESTIONE
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.CD_TIPO_LINEA_ATTIVITA
    ,aDest.DENOMINAZIONE
    ,aDest.CD_GRUPPO_LINEA_ATTIVITA
    ,aDest.CD_FUNZIONE
    ,aDest.CD_NATURA
    ,aDest.DS_LINEA_ATTIVITA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDR_COLLEGATO
    ,aDest.CD_LA_COLLEGATO
    ,aDest.ESERCIZIO_FINE
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.PG_PROGETTO
    ,Nvl(aDest.FL_LIMITE_ASS_OBBLIG, 'N')
    ,aDest.cd_cofog
    ,aDest.cd_programma
    ,aDest.cd_missione
    );
 end;

procedure ins_ASS_LINEA_ATTIVITA_ESER (aDest ASS_LINEA_ATTIVITA_ESERCIZIO%rowtype) is
  begin
   insert into ASS_LINEA_ATTIVITA_ESERCIZIO (
     ESERCIZIO
    ,CD_CENTRO_RESPONSABILITA
    ,CD_LINEA_ATTIVITA
    ,PG_PROGETTO
    ,ESERCIZIO_FINE
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.CD_LINEA_ATTIVITA
    ,aDest.PG_PROGETTO
    ,aDest.ESERCIZIO_FINE
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC);
 end;

procedure ins_ASS_LA_CLASS_VOCI (aDest ASS_LA_CLASS_VOCI%rowtype) Is
Begin
Insert Into ASS_LA_CLASS_VOCI
(CD_CENTRO_RESPONSABILITA,
CD_LINEA_ATTIVITA,
CD_CDR_ORIGINE,
CD_NATURA_ORIGINE,
ID_CLASS_ORIGINE,
DS_ASSOCIAZIONE,
UTCR,
DACR,
UTUV,
DUVA,
PG_VER_REC,
cd_cofog)
Values
(aDest.CD_CENTRO_RESPONSABILITA,
aDest.CD_LINEA_ATTIVITA,
aDest.CD_CDR_ORIGINE,
aDest.CD_NATURA_ORIGINE,
aDest.ID_CLASS_ORIGINE,
aDest.DS_ASSOCIAZIONE,
aDest.UTCR,
aDest.DACR,
aDest.UTUV,
aDest.DUVA,
aDest.PG_VER_REC,
aDest.cd_cofog);
End;

 procedure ins_TIPO_LINEA_ATTIVITA (aDest TIPO_LINEA_ATTIVITA%rowtype) is
  begin
   insert into TIPO_LINEA_ATTIVITA (
     CD_TIPO_LINEA_ATTIVITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,DS_TIPO_LINEA_ATTIVITA
    ,TI_TIPO_LA
    ,CD_NATURA
    ,CD_FUNZIONE
    ,TI_GESTIONE
   ) values (
     aDest.CD_TIPO_LINEA_ATTIVITA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.DS_TIPO_LINEA_ATTIVITA
    ,aDest.TI_TIPO_LA
    ,aDest.CD_NATURA
    ,aDest.CD_FUNZIONE
    ,aDest.TI_GESTIONE
    );
 end;

 procedure ins_ASS_TIPO_LA_CDR (aDest ASS_TIPO_LA_CDR%rowtype) is
 begin
   insert into ASS_TIPO_LA_CDR (
     CD_TIPO_LINEA_ATTIVITA
    ,CD_CENTRO_RESPONSABILITA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_TIPO_LINEA_ATTIVITA
    ,aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
 );
 end;

 procedure ins_INSIEME_LA (aDest INSIEME_LA%rowtype) is
  begin
   insert into INSIEME_LA (
     CD_INSIEME_LA
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,DS_INSIEME_LA
    ,CD_CENTRO_RESPONSABILITA
   ) values (
     aDest.CD_INSIEME_LA
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.DS_INSIEME_LA
    ,aDest.CD_CENTRO_RESPONSABILITA
    );
 end;
end;
/


