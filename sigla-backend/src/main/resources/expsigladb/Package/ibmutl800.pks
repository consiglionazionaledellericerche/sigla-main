CREATE OR REPLACE PACKAGE IBMUTL800 AS
--
-- IBMUTL800 - Package di utilita per la gestione dell'Albero_Main
--
-- Date: 30/08/2002
-- Version: 1.0
--
-- History:
-- Date: 30/08/2002
-- Version: 1.0
-- Creation
--
-- Date: 10/09/2002
-- Version: 1.1
-- Aggiunta di Procedura per il controllo della tabella CONFIGURAZIONE_CNR
--
-- Constants:
--
DIFFACC005 CONSTANT VARCHAR2(100):='DIFFACC005 - Differenza in descrizione';
DIFFACC010 CONSTANT VARCHAR2(100):='DIFFACC010 - Accesso non presente in origine';
DIFFACC015 CONSTANT VARCHAR2(100):='DIFFACC015 - Accesso non presente in destinazione';
DIFFACC020 CONSTANT VARCHAR2(100):='DIFFACC020 - Differenza in tipo di accesso';
DIFFACC025 CONSTANT VARCHAR2(100):='DIFFACC025 - Utente di creazione o modifica non corretto';
DIFFACC030 CONSTANT VARCHAR2(100):='DIFFACC030 - Differenza in descrizione';
DIFFACC035 CONSTANT VARCHAR2(100):='DIFFACC035 - Nodo non presente in destinazione';
DIFFACC040 CONSTANT VARCHAR2(100):='DIFFACC040 - Nodo non presente in origine';
DIFFACC045 CONSTANT VARCHAR2(100):='DIFFACC045 - Differenza in accesso';
DIFFACC050 CONSTANT VARCHAR2(100):='DIFFACC050 - Differenza in business_process';
DIFFACC055 CONSTANT VARCHAR2(100):='DIFFACC055 - Numero livello errato';
DIFFACC060 CONSTANT VARCHAR2(100):='DIFFACC060 - Differenza in posizione';
DIFFACC065 CONSTANT VARCHAR2(100):='DIFFACC065 - Nodo padre errato';
DIFFACC070 CONSTANT VARCHAR2(100):='DIFFACC070 - Chiave propria errata';
DIFFACC075 CONSTANT VARCHAR2(100):='DIFFACC075 - Associazione BP ACCESSO corrispondente non trovata';
DIFFACC080 CONSTANT VARCHAR2(100):='DIFFACC080 - Utente di creazione o modifica non corretto';
DIFFACC105 CONSTANT VARCHAR2(100):='DIFFACC105 - Differenza in tipo_funzione';
DIFFACC110 CONSTANT VARCHAR2(100):='DIFFACC110 - Ass bp-accesso non presente in destinazione';
DIFFACC115 CONSTANT VARCHAR2(100):='DIFFACC115 - Ass bp-accesso non presente in origine';
DIFFACC125 CONSTANT VARCHAR2(100):='DIFFACC125 - Utente di creazione o modifica non corretto';
--
-- Messaggi usati per CHKCONFIGURAZIONE_CNR
--
DIFFACC200 CONSTANT VARCHAR2(100):='DIFFACC200 - Differenza per valore testo VAL01';
DIFFACC205 CONSTANT VARCHAR2(100):='DIFFACC205 - Differenza per valore testo VAL02';
DIFFACC210 CONSTANT VARCHAR2(100):='DIFFACC210 - Differenza per valore testo VAL03';
DIFFACC215 CONSTANT VARCHAR2(100):='DIFFACC215 - Differenza per valore testo VAL04';
DIFFACC220 CONSTANT VARCHAR2(100):='DIFFACC220 - Differenza per importo IM01';
DIFFACC225 CONSTANT VARCHAR2(100):='DIFFACC225 - Differenza per importo IM02';
DIFFACC230 CONSTANT VARCHAR2(100):='DIFFACC230 - Differenza per data DT01';
DIFFACC235 CONSTANT VARCHAR2(100):='DIFFACC235 - Differenza per data DT02';
DIFFACC240 CONSTANT VARCHAR2(100):='DIFFACC240 - Chiave non presente in origine';
DIFFACC245 CONSTANT VARCHAR2(100):='DIFFACC245 - Chiave non presente in destinazione';

aMigUser CONSTANT VARCHAR2(20):='$$$$$MIGRAZIONE$$$$$';

TYPE GenCurTyp IS REF CURSOR;
aSql01 VARCHAR2(2000);
aSql02 VARCHAR2(2000);
--
-- Functions & Procedures:
--
-- Contiene procedure per il controllo delle tabelle ACCESSO, ALBERO_MAIN e ASS_BP_ACCESSO,
-- utilizzate per la gestione dell'albero_main

 PROCEDURE CHKACCESSO(aOrigOwner varchar2, aDestOwner varchar2);

 PROCEDURE CHKALBERO_MAIN(aOrigOwner varchar2, aDestOwner varchar2);

 PROCEDURE CHKALBERO_MAIN(aOrigOwner varchar2, aDestOwner varchar2, aOption varchar2);

 PROCEDURE CHKASS_BP_ACCESSO(aOrigOwner varchar2, aDestOwner varchar2);

 PROCEDURE CHKCONFIGURAZIONE_CNR(aOrigOwner varchar2, aDestOwner varchar2);

END;
/


CREATE OR REPLACE PACKAGE BODY IBMUTL800 AS

-- Procedura per la ricerca di discordanze nella tabella ACCESSO
 PROCEDURE CHKACCESSO(aOrigOwner varchar2, aDestOwner varchar2) IS
   aAccesso accesso%ROWTYPE;
   aDestAccesso accesso%ROWTYPE;
   aCurs GenCurTyp;
   BEGIN
   -- Leggo dal DB ORIGINE tutti gli accessi e li confronto con quelli del db corrente
   aSql01 := 'SELECT * FROM '||aDestOwner||'.accesso';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestAccesso;
      EXIT WHEN aCurs%NOTFOUND;
	  -- Leggo dalla tabella di destinazione e confronto i records
      begin
	   aSql02 := 'SELECT * FROM '||aOrigOwner||'.accesso where cd_accesso = :1';
       EXECUTE IMMEDIATE aSql02 INTO aAccesso USING aDestAccesso.cd_accesso;
	   if aDestAccesso.ds_accesso != aAccesso.ds_Accesso then
        IBMUTL010.SCRIVI(DIFFACC005);
        IBMUTL010.SCRIVI(' >'||aDestAccesso.cd_accesso);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAccesso.ds_accesso);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAccesso.ds_accesso);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAccesso.ti_accesso != aAccesso.ti_Accesso then
        IBMUTL010.SCRIVI(DIFFACC020);
        IBMUTL010.SCRIVI(' >'||aDestAccesso.cd_accesso||' '||aDestAccesso.ds_accesso);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAccesso.ti_accesso);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAccesso.ti_accesso);
		IBMUTL010.SCRIVI(' ');
	   end if;
	  if aDestAccesso.utuv != aMigUser or aDestAccesso.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC025);
        IBMUTL010.SCRIVI(' >'||aDestAccesso.cd_accesso||' '||aDestAccesso.ds_accesso);
        IBMUTL010.SCRIVI(' >UTCR ->'||aDestAccesso.utcr||' UTUV ->'||aDestAccesso.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	  exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC010);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aAccesso.cd_accesso||' '||aAccesso.ds_accesso);
		IBMUTL010.SCRIVI(' ');
	  end;
   END LOOP;
   CLOSE aCurs;
   -- Accessi non presenti in destinazione ma solo in origine
   aSql01 := 'SELECT * FROM '||aOrigOwner||'.accesso a where not exists (select 1 from '||aDestOwner||'.accesso where cd_accesso = a.cd_accesso)';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestAccesso;
      EXIT WHEN aCurs%NOTFOUND;
      IBMUTL010.SCRIVI(DIFFACC015);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aDestAccesso.cd_accesso||' '||aDestAccesso.ds_accesso);
		IBMUTL010.SCRIVI(' ');
   END LOOP;
   CLOSE aCurs;
 END CHKACCESSO;

 -- Procedura per la ricerca di discordanze nella tabella ALBERO_MAIN
 PROCEDURE CHKALBERO_MAIN(aOrigOwner varchar2, aDestOwner varchar2) IS
   aAlberoMain albero_main%ROWTYPE;
   aDestAlberoMain albero_main%ROWTYPE;
   aCurs GenCurTyp;
   aNumOccorrenze number;
   aTempStr varchar2(500);
   aTAssBpAccesso ass_bp_accesso%rowtype;
 BEGIN
 -- Leggo dal DB ORIGINE tutti gli accessi e li confronto con quelli del db corrente
   aSql01 := 'SELECT * FROM '||aDestOwner||'.albero_main';
   OPEN aCurs FOR aSql01;
   LOOP
   FETCH aCurs into aDestAlberoMain;
   EXIT WHEN aCurs%NOTFOUND;
	  -- Leggo dalla tabella di destinazione e confronto i records
      begin
	   aSql02 := 'SELECT * FROM '||aOrigOwner||'.albero_main where cd_nodo = :1';
       EXECUTE IMMEDIATE aSql02 INTO aAlberoMain USING aDestAlberoMain.cd_nodo;
	   if aDestAlberoMain.ds_nodo != aAlberoMain.ds_nodo then
        IBMUTL010.SCRIVI(DIFFACC030);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.ds_nodo);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAlberoMain.cd_accesso != aAlberoMain.cd_accesso then
        IBMUTL010.SCRIVI(DIFFACC045);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.cd_accesso);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.cd_accesso);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAlberoMain.business_process != aAlberoMain.business_process then
        IBMUTL010.SCRIVI(DIFFACC050);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.business_process);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.business_process);
		IBMUTL010.SCRIVI(' ');
	   end if;
       aNumOccorrenze:=IBMUTL001.contaOccorrenze(aDestAlberoMain.cd_nodo,'.');
	   if aNumOccorrenze != aDestAlberoMain.livello then
        IBMUTL010.SCRIVI(DIFFACC055);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >Livello attuale->'||aDestAlberoMain.livello);
        IBMUTL010.SCRIVI(' >Livello calcolato->'||aNumOccorrenze);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAlberoMain.pg_ordinamento != aAlberoMain.pg_ordinamento then
        IBMUTL010.SCRIVI(DIFFACC060);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.pg_ordinamento);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.pg_ordinamento);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAlberoMain.cd_nodo_padre is not null then
        aTempStr:=substr(aDestAlberoMain.cd_nodo,1,instr(aDestAlberoMain.cd_nodo,'.',-1)-1);
	    if aTempStr != aDestAlberoMain.cd_nodo_padre then
         IBMUTL010.SCRIVI(DIFFACC065);
         IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
         IBMUTL010.SCRIVI(' >Nodo padre attuale->'||aDestAlberoMain.cd_nodo_padre);
         IBMUTL010.SCRIVI(' >Nodo padre calcolato->'||aTempStr);
		IBMUTL010.SCRIVI(' ');
	    end if;
	   end if;
       aTempStr:=substr(aDestAlberoMain.cd_nodo,instr(aDestAlberoMain.cd_nodo,'.',-1)+1,length(aDestAlberoMain.cd_nodo));
       if aTempStr != aDestAlberoMain.cd_proprio_nodo then
        IBMUTL010.SCRIVI(DIFFACC070);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >Codice proprio nodo attuale->'||aDestAlberoMain.cd_proprio_nodo);
        IBMUTL010.SCRIVI(' >Codice_proprio nodo calcolato->'||aTempStr);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   begin
        if aDestAlberoMain.cd_accesso is not null or aDestAlberoMain.business_process is not null then
		 select * into aTAssBpAccesso from ass_bp_accesso where
		      cd_accesso = aDestAlberoMain.cd_accesso
		  and business_process = aDestAlberoMain.business_process;
	    end if;
	   exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC075);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_accesso||' '||aDestAlberoMain.business_process);
        IBMUTL010.SCRIVI(' >ASS. BP ACCESSO NON TROVATA ->'||aDestAlberoMain.cd_accesso||' '||aDestAlberoMain.business_process);
		IBMUTL010.SCRIVI(' ');
	   end;
	   if aDestAlberoMain.utuv != aMigUser or aDestAlberoMain.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC080);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >UTCR ->'||aDestAlberoMain.utcr||' UTUV ->'||aDestAlberoMain.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	  exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC040);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.cd_nodo||' '||aAlberoMain.ds_nodo);
		IBMUTL010.SCRIVI(' ');
	  end;
   END LOOP;
   CLOSE aCurs;
   -- Nodi non presenti in destinazione ma solo in origine
   aSql01 := 'SELECT * FROM '||aOrigOwner||'.albero_main a where not exists (select 1 from '||aDestOwner||'.albero_main where cd_nodo = a.cd_nodo)';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aAlberoMain;
      EXIT WHEN aCurs%NOTFOUND;
      IBMUTL010.SCRIVI(DIFFACC035);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.cd_accesso||' '||aAlberoMain.ds_nodo);
		IBMUTL010.SCRIVI(' ');
   END LOOP;
   CLOSE aCurs;
 END CHKALBERO_MAIN;

 -- Procedura per la ricerca di discordanze nella tabella ALBERO_MAIN
 PROCEDURE CHKALBERO_MAIN(aOrigOwner varchar2, aDestOwner varchar2, aOption varchar2) IS
   aAlberoMain albero_main%ROWTYPE;
   aDestAlberoMain albero_main%ROWTYPE;
   aCurs GenCurTyp;
   aNumOccorrenze number;
   aTempStr varchar2(500);
   aTAssBpAccesso ass_bp_accesso%rowtype;
 BEGIN
 -- Leggo dal DB ORIGINE tutti gli accessi e li confronto con quelli del db corrente
   aSql01 := 'SELECT * FROM '||aDestOwner||'.albero_main';
   OPEN aCurs FOR aSql01;
   LOOP
   FETCH aCurs into aDestAlberoMain;
   EXIT WHEN aCurs%NOTFOUND;
	  -- Leggo dalla tabella di destinazione e confronto i records
      begin
	   aSql02 := 'SELECT * FROM '||aOrigOwner||'.albero_main where cd_nodo = :1';
       EXECUTE IMMEDIATE aSql02 INTO aAlberoMain USING aDestAlberoMain.cd_nodo;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'NODO' then
		   if aDestAlberoMain.ds_nodo != aAlberoMain.ds_nodo then
	        IBMUTL010.SCRIVI(DIFFACC030);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo);
	        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.ds_nodo);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'ACCESSO' then
		   if aDestAlberoMain.cd_accesso != aAlberoMain.cd_accesso then
	        IBMUTL010.SCRIVI(DIFFACC045);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.cd_accesso);
	        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.cd_accesso);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'BP' then
		   if aDestAlberoMain.business_process != aAlberoMain.business_process then
	        IBMUTL010.SCRIVI(DIFFACC050);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.business_process);
	        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.business_process);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'LIVELLO' then
       aNumOccorrenze:=IBMUTL001.contaOccorrenze(aDestAlberoMain.cd_nodo,'.');
		   if aNumOccorrenze != aDestAlberoMain.livello then
	        IBMUTL010.SCRIVI(DIFFACC055);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >Livello attuale->'||aDestAlberoMain.livello);
	        IBMUTL010.SCRIVI(' >Livello calcolato->'||aNumOccorrenze);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'ORDINAMENTO' then
		   if aDestAlberoMain.pg_ordinamento != aAlberoMain.pg_ordinamento then
	        IBMUTL010.SCRIVI(DIFFACC060);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.pg_ordinamento);
	        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.pg_ordinamento);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   if UPPER(aOption) = 'ALL' or UPPER(aOption) = 'NODO' then
		   if aDestAlberoMain.cd_nodo_padre is not null then
	        aTempStr:=substr(aDestAlberoMain.cd_nodo,1,instr(aDestAlberoMain.cd_nodo,'.',-1)-1);
		    if aTempStr != aDestAlberoMain.cd_nodo_padre then
	         IBMUTL010.SCRIVI(DIFFACC065);
	         IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	         IBMUTL010.SCRIVI(' >Nodo padre attuale->'||aDestAlberoMain.cd_nodo_padre);
	         IBMUTL010.SCRIVI(' >Nodo padre calcolato->'||aTempStr);
			IBMUTL010.SCRIVI(' ');
		    end if;
		   end if;
	       aTempStr:=substr(aDestAlberoMain.cd_nodo,instr(aDestAlberoMain.cd_nodo,'.',-1)+1,length(aDestAlberoMain.cd_nodo));
	       if aTempStr != aDestAlberoMain.cd_proprio_nodo then
	        IBMUTL010.SCRIVI(DIFFACC070);
	        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
	        IBMUTL010.SCRIVI(' >Codice proprio nodo attuale->'||aDestAlberoMain.cd_proprio_nodo);
	        IBMUTL010.SCRIVI(' >Codice_proprio nodo calcolato->'||aTempStr);
			IBMUTL010.SCRIVI(' ');
		   end if;
	   end if;
	   begin
        if aDestAlberoMain.cd_accesso is not null or aDestAlberoMain.business_process is not null then
		 select * into aTAssBpAccesso from ass_bp_accesso where
		      cd_accesso = aDestAlberoMain.cd_accesso
		  and business_process = aDestAlberoMain.business_process;
	    end if;
	   exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC075);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_accesso||' '||aDestAlberoMain.business_process);
        IBMUTL010.SCRIVI(' >ASS. BP ACCESSO NON TROVATA ->'||aDestAlberoMain.cd_accesso||' '||aDestAlberoMain.business_process);
		IBMUTL010.SCRIVI(' ');
	   end;
	   if aDestAlberoMain.utuv != aMigUser or aDestAlberoMain.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC080);
        IBMUTL010.SCRIVI(' >'||aDestAlberoMain.cd_nodo||' '||aDestAlberoMain.ds_nodo);
        IBMUTL010.SCRIVI(' >UTCR ->'||aDestAlberoMain.utcr||' UTUV ->'||aDestAlberoMain.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	  exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC040);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAlberoMain.cd_nodo||' '||aAlberoMain.ds_nodo);
		IBMUTL010.SCRIVI(' ');
	  end;
   END LOOP;
   CLOSE aCurs;
   -- Nodi non presenti in destinazione ma solo in origine
   aSql01 := 'SELECT * FROM '||aOrigOwner||'.albero_main a where not exists (select 1 from '||aDestOwner||'.albero_main where cd_nodo = a.cd_nodo)';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aAlberoMain;
      EXIT WHEN aCurs%NOTFOUND;
      IBMUTL010.SCRIVI(DIFFACC035);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAlberoMain.cd_accesso||' '||aAlberoMain.ds_nodo);
		IBMUTL010.SCRIVI(' ');
   END LOOP;
   CLOSE aCurs;
 END CHKALBERO_MAIN;


-- Procedura per la ricerca di discordanze nella tabella ASS_BP_ACCESSO
 PROCEDURE CHKASS_BP_ACCESSO(aOrigOwner varchar2, aDestOwner varchar2) IS
   aAssBpAccesso ass_bp_accesso%ROWTYPE;
   aDestAssBpAccesso ass_bp_accesso%ROWTYPE;
   aCurs GenCurTyp;
 BEGIN
 -- Leggo dal DB ORIGINE tutti gli accessi e li confronto con quelli del db corrente
   aSql01 := 'SELECT * FROM '||aDestOwner||'.ass_bp_accesso';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestAssBpAccesso;
      EXIT WHEN aCurs%NOTFOUND;
	  -- Leggo dalla tabella di destinazione e confronto i records
      begin
	   aSql02 := 'SELECT * FROM '||aOrigOwner||'.ass_bp_accesso where cd_accesso = :1 and business_process = :2';
       EXECUTE IMMEDIATE aSql02 INTO aAssBpAccesso USING aDestAssBpAccesso.cd_accesso, aDestAssBpAccesso.business_process;
	   if aDestAssBpAccesso.ti_funzione != aAssBpAccesso.ti_funzione then
        IBMUTL010.SCRIVI(DIFFACC105);
        IBMUTL010.SCRIVI(' >'||aDestAssBpAccesso.cd_accesso||' '||aDestAssBpAccesso.business_process);
        IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestAssBpAccesso.ti_funzione);
        IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aAssBpAccesso.ti_funzione);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   if aDestAssBpAccesso.utuv != aMigUser or aDestAssBpAccesso.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC125);
        IBMUTL010.SCRIVI(' >'||aDestAssBpAccesso.cd_accesso||' '||aDestAssBpAccesso.business_process);
        IBMUTL010.SCRIVI(' >UTCR ->'||aDestAssBpAccesso.utcr||' UTUV ->'||aDestAssBpAccesso.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	  exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC115);
        IBMUTL010.SCRIVI(' >'||aDestAssBpAccesso.cd_accesso||' '||aDestAssBpAccesso.business_process);
		IBMUTL010.SCRIVI(' ');
	  end;
   END LOOP;
   CLOSE aCurs;
   -- Accessi non presenti in origine ma solo in destinazione
   aSql01 := 'SELECT * FROM '||aOrigOwner||'.ass_bp_accesso a where not exists (select 1 from '||aDestOwner||'.ass_bp_accesso where cd_accesso = a.cd_accesso and business_process = a.business_process)';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestAssBpAccesso;
      EXIT WHEN aCurs%NOTFOUND;
      IBMUTL010.SCRIVI(DIFFACC110);
        IBMUTL010.SCRIVI(' >'||aDestAssBpAccesso.cd_accesso||' '||aDestAssBpAccesso.business_process);
		IBMUTL010.SCRIVI(' ');
   END LOOP;
   CLOSE aCurs;
 END CHKASS_BP_ACCESSO;


 -- Procedura per la ricerca di discordanze nella tabella CONFIGURAZIONE_CNR
 PROCEDURE CHKCONFIGURAZIONE_CNR(aOrigOwner varchar2, aDestOwner varchar2) IS
   aConfigurazioneCnr configurazione_cnr%ROWTYPE;
   aDestConfigurazioneCnr configurazione_cnr%ROWTYPE;
   aCurs GenCurTyp;
 BEGIN
 -- Leggo dal DB ORIGINE tutti gli accessi e li confronto con quelli del db corrente
   aSql01 := 'SELECT * FROM '||aDestOwner||'.configurazione_cnr';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestConfigurazioneCnr;
      EXIT WHEN aCurs%NOTFOUND;
	  -- Leggo dalla tabella di destinazione e confronto i records
      begin
	   aSql02 := 'SELECT * FROM '||aOrigOwner||'.configurazione_cnr where esercizio = :1 and cd_unita_funzionale = :2 and cd_chiave_primaria = :3 and cd_chiave_secondaria = :4';
       EXECUTE IMMEDIATE aSql02 INTO aConfigurazioneCnr USING aDestConfigurazioneCnr.esercizio, aDestConfigurazioneCnr.cd_unita_funzionale,
	   		   aDestConfigurazioneCnr.cd_chiave_primaria, aDestConfigurazioneCnr.cd_chiave_secondaria;
		-- Controlla VAL01
	   if aDestConfigurazioneCnr.val01 != aConfigurazioneCnr.val01 then
        IBMUTL010.SCRIVI(DIFFACC200);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.val01);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.val01);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla VAL02
	   if aDestConfigurazioneCnr.val02 != aConfigurazioneCnr.val02 then
        IBMUTL010.SCRIVI(DIFFACC205);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.val02);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.val02);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla VAL03
	   if aDestConfigurazioneCnr.val03 != aConfigurazioneCnr.val03 then
        IBMUTL010.SCRIVI(DIFFACC210);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.val03);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.val03);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla VAL04
	   if aDestConfigurazioneCnr.val04 != aConfigurazioneCnr.val04 then
        IBMUTL010.SCRIVI(DIFFACC215);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.val04);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.val04);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla IM01
	   if aDestConfigurazioneCnr.im01 != aConfigurazioneCnr.im01 then
        IBMUTL010.SCRIVI(DIFFACC220);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.im01);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.im01);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla IM02
	   if aDestConfigurazioneCnr.im02 != aConfigurazioneCnr.im02 then
        IBMUTL010.SCRIVI(DIFFACC225);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.im02);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.im02);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla DT01
	   if aDestConfigurazioneCnr.dt01 != aConfigurazioneCnr.dt01 then
        IBMUTL010.SCRIVI(DIFFACC230);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.dt01);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.dt01);
		IBMUTL010.SCRIVI(' ');
	   end if;
		-- Controlla DT02
	   if aDestConfigurazioneCnr.dt02 != aConfigurazioneCnr.dt02 then
        IBMUTL010.SCRIVI(DIFFACC230);
       IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >'||aOrigOwner||'->'||aConfigurazioneCnr.dt02);
		IBMUTL010.SCRIVI(' >'||aDestOwner||'->'||aDestConfigurazioneCnr.dt02);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   -- Utente di creazione ORIGINE errato, (!= da '$$$$$MIGRAZIONE$$$$$')
	   if aConfigurazioneCnr.utuv != aMigUser or aConfigurazioneCnr.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC125);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >UTCR ->'||aConfigurazioneCnr.utcr||' UTUV ->'||aConfigurazioneCnr.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   -- Utente di creazione DESTINAZIONE errato, (!= da '$$$$$MIGRAZIONE$$$$$')
	   if aDestConfigurazioneCnr.utuv != aMigUser or aDestConfigurazioneCnr.utcr != aMigUser then
        IBMUTL010.SCRIVI(DIFFACC125);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' >UTCR ->'||aDestConfigurazioneCnr.utcr||' UTUV ->'||aDestConfigurazioneCnr.UTUV);
		IBMUTL010.SCRIVI(' ');
	   end if;
	   -- Chiave NON presente in Destinazione
	  exception when NO_DATA_FOUND then
        IBMUTL010.SCRIVI(DIFFACC240);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||aDestConfigurazioneCnr.cd_unita_funzionale||
						   '   '||aDestConfigurazioneCnr.cd_chiave_primaria||'   '||aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' ');
	  end;
   END LOOP;
   CLOSE aCurs;
   -- Chiave NON presente in origine ma solo in destinazione
   aSql01 := 'SELECT * FROM '||aOrigOwner||'.configurazione_cnr a where not exists '||
   		  	 '(select 1 from '||aDestOwner||'.configurazione_cnr '||
			 'where esercizio = a.esercizio '||
			 'and cd_unita_funzionale = a.cd_unita_funzionale '||
			 'and cd_chiave_primaria = a.cd_chiave_primaria '||
			 'and cd_chiave_secondaria = a.cd_chiave_secondaria)';
   OPEN aCurs FOR aSql01;
   LOOP
      FETCH aCurs into aDestConfigurazioneCnr;
      EXIT WHEN aCurs%NOTFOUND;
      IBMUTL010.SCRIVI(DIFFACC245);
        IBMUTL010.SCRIVI(' >'||aDestConfigurazioneCnr.esercizio||'   '||
						   aDestConfigurazioneCnr.cd_unita_funzionale||'   '||
						   aDestConfigurazioneCnr.cd_chiave_primaria||'   '||
						   aDestConfigurazioneCnr.cd_chiave_secondaria);
		IBMUTL010.SCRIVI(' ');
   END LOOP;
   CLOSE aCurs;
 END CHKCONFIGURAZIONE_CNR;


END;
/


