CREATE OR REPLACE package CNRCTB018 as
--
-- CNRCTB018 - Package di gestione dei numeratori dei documenti contabili NUMERAZIONE_DOC_CONT:
-- Date: 25/06/2002
-- Version: 1.7
--
-- Dependency: CNRCTB020 IBMERR001
--
-- History:
--
-- Date: 09/01/2002
-- Version: 1.0
-- Creazione
--
-- Date: 17/01/2002
-- Version: 1.1
-- Gestione dei sezionali documenti residui
--
-- Date: 23/01/2002
-- Version: 1.2
-- Aggiunta funzione di numerazione dei documenti
--
-- Date: 20/02/2002
-- Version: 1.3
-- Aggiunta la reversale
--
-- Date: 01/03/2002
-- Version: 1.4
-- Aggiunta reversale provvisoria CDS e accertamenti sistema CDS
--
-- Date: 22/04/2002
-- Version: 1.5
-- Aggiunto accertamento pluriennale
--
-- Date: 06/06/2002
-- Version: 1.6
-- Aggiunta la liquidazione CORI
--
-- Date: 25/06/2002
-- Version: 1.7
-- Eliminata la liquidazione CORI come tipo di documento contabile
--
-- Constants:

-- Tipi di documenti contabili
--
--
-- Registro obbligazioni
TI_DOC_OBB CONSTANT VARCHAR2(10):='OBB';

-- Registro obbligazioni Residue proprie
TI_DOC_OBB_RES_PRO CONSTANT VARCHAR2(10):='OBB_RES';

-- Registro obbligazioni Residue Improprie
TI_DOC_OBB_RES_IMPRO CONSTANT VARCHAR2(10):='OBB_RESIM';

-- Registro obbligazioni su partite di giro
TI_DOC_OBB_PGIRO CONSTANT VARCHAR2(10):='OBB_PGIRO';

-- Registro obbligazioni su partite di giro residue
TI_DOC_OBB_PGIRO_RES CONSTANT VARCHAR2(10):='OBB_PGIR_R';

-- Modifiche alle obbligazioni
TI_DOC_OBB_MOD CONSTANT VARCHAR2(10):='OBB_MOD';

--Impegni
TI_DOC_IMP CONSTANT VARCHAR2(10):='IMP';

--Impegni residui
TI_DOC_IMP_RES CONSTANT VARCHAR2(10):='IMP_RES';

--Accertamenti
TI_DOC_ACC CONSTANT VARCHAR2(10):='ACR';
--Accertamenti residui
TI_DOC_ACC_RES CONSTANT VARCHAR2(10):='ACR_RES';

-- Modifiche agli Accertamenti residui
TI_DOC_ACR_MOD CONSTANT VARCHAR2(10):='ACR_MOD';

--Accertamenti su partite di giro
TI_DOC_ACC_PGIRO CONSTANT VARCHAR2(10):='ACR_PGIRO';

--Accertamenti su partite di giro RESIDUE
TI_DOC_ACC_PGIRO_RES CONSTANT VARCHAR2(10):='ACR_PGIR_R';

--Accertamenti di sistema
TI_DOC_ACC_SIST  CONSTANT VARCHAR2(10):= 'ACR_SIST';

--Accertamenti plurinennale
TI_DOC_ACC_PLUR  CONSTANT VARCHAR2(10):= 'ACR_PLUR';

--Mandato
TI_DOC_MAN CONSTANT VARCHAR2(10):='MAN';

-- Reversale
TI_DOC_REV CONSTANT VARCHAR2(10):='REV';

-- Reversale
TI_DOC_REV_PROVV CONSTANT VARCHAR2(10):='REV_PROVV';

--Range di numerazione

FIRST_DOC_CNR CONSTANT NUMBER        :=         1;
LAST_DOC_CNR CONSTANT NUMBER         :=  99999999;

FIRST_DOC_CNR_PRI CONSTANT NUMBER    :=         1;
LAST_DOC_CNR_PRI CONSTANT NUMBER     :=    999999;

FIRST_DOC_CNR_RES CONSTANT NUMBER    :=2010100001;    --cambiato il valore
LAST_DOC_CNR_RES CONSTANT NUMBER     :=2999999999;

FIRST_DOC_CNR_PLUR CONSTANT NUMBER    :=5000000001;
LAST_DOC_CNR_PLUR CONSTANT NUMBER     :=5000999999;

FIRST_DOC_CNR_PGIRO CONSTANT NUMBER    :=7000000001;   --nuovo
LAST_DOC_CNR_PGIRO CONSTANT NUMBER     :=7000999999;   --nuovo


FIRST_DOC_CDS CONSTANT NUMBER        :=         1;
LAST_DOC_CDS CONSTANT NUMBER         :=  99999999;

FIRST_DOC_CDS_PGIRO CONSTANT NUMBER  := 100000001;
LAST_DOC_CDS_PGIRO CONSTANT NUMBER   := 199999999;

FIRST_DOC_CDS_PROVV CONSTANT NUMBER  := 500000001;
LAST_DOC_CDS_PROVV CONSTANT NUMBER   := 599999999;

FIRST_DOC_CDS_RES CONSTANT NUMBER    := 1900000001;
LAST_DOC_CDS_RES CONSTANT NUMBER     := 2999999999;

FIRST_DOC_CDS_RESIM CONSTANT NUMBER    := 9130000001;
LAST_DOC_CDS_RESIM CONSTANT NUMBER     := 9999999999;

FIRST_FULL_RANGE CONSTANT NUMBER  :=          1;
LAST_FULL_RANGE CONSTANT NUMBER   := 9999999999;

-- Functions E Procedures:

 -- Inserisce in NUMERAZIONE_DOC_CONT il record aNum

  procedure ins_NUMERAZIONE_DOC_CONT (aDest NUMERAZIONE_DOC_CONT%rowtype);

 -- PRE E' stato aggiornato ad aperto lo stato dell'esercizio aEsercizio per il CDS aCdCds e il tipo di cds ? uguale a ENTE
 -- POST Viene creata in NUMERAZIONE_DOC_CONT una entry per ognuno dei seguenti casi:
 --
 -- Tipo documento: IMP - Impegno CNR
 -- Primo numeratore:       1
 -- Ultimo numeratore: 999999
 -- Corrente: 0

 -- Tipo documento: IMP_RES - Impegno residuo CNR (concatenzaione di anno di origine e chiave propria del documento in competenza)
 -- Primo numeratore:  1900 000001
 -- Ultimo numeratore: 2999 999999
 -- Corrente: 0

 -- Tipo documento: ACR - Accertamento CNR
 -- Primo numeratore:       1
 -- Ultimo numeratore: 999999
 -- Corrente: 1
 --
 -- Tipo documento: ACR_RES - Accertamento residuo CNR (concatenzaione di anno di origine e chiave propria del documento in competenza)
 -- Primo numeratore:  1900 000001
 -- Ultimo numeratore: 2999 999999
 -- Corrente: 0
 --
 -- Tipo documento: ACR_PLUR - Accertamento pluriennale CNR
 -- Primo numeratore:  5000 000001
 -- Ultimo numeratore: 5000 999999
 -- Corrente: 5000 000000

 -- Tipo documento: MAN - Mandato
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0
 --
 -- Tipo documento: REV - Reversale
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0

 -- PRE E' stato aggiornato ad aperto lo stato dell'esercizio aEsercizio per il CDS aCdCds e il tipo di cds ? diverso da ENTE
 -- POST Viene creata in NUMERAZIONE_DOC_CONT una entry per ognuno dei seguenti casi:
 --
 -- Tipo documento: OBB - Registro delle obbligazioni
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0

 -- Tipo documento: OBB_PGIRO - Registro delle obbligazioni su partite di giro
 -- Primo numeratore:  100000001
 -- Ultimo numeratore: 199999999
 -- Corrente: 100000000

 -- Tipo documento: ACR_SIST - Accertamento di sistema CDS (hidden)
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0

 -- Tipo documento: ACR_PGIRO - Accertamento CDS partite di giro
 -- Primo numeratore:  100000001
 -- Ultimo numeratore: 199999999
 -- Corrente: 100000000

 -- Tipo documento: MAN - Mandato
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0

 -- Tipo documento: REV - Reversale
 -- Primo numeratore:          1
 -- Ultimo numeratore:  99999999
 -- Corrente: 0

 -- Tipo documento: REV_PROVV - Reversale provvisoria
 -- Primo numeratore:  500000001
 -- Ultimo numeratore: 599999999
 -- Corrente: 500000000

 --
 -- aUser: Utente che opera l'aggiornamento

 procedure aggiornaNumeratori(aEsercizio number, aCdCds varchar2, aUser varchar2);

 -- Ritorna il successivo numeratore di documento contabile del tipo specificato
 -- Contestualmente aggiorna il campo corrente nella tabella dei numeratori per il tipo specificato
 -- Se ? stato raggiunto il massimo numeratorem viene sollevata un'eccezione applicativa
 --
 -- aTipo -> Tipo di documento (es. OBB)
 -- aEs -> Esercizio contabile
 -- aCdCds -> Codice del centro di spesa
 -- aUser -> Codice utente

 function getNextNumDocCont(aTipo varchar2, aEs number, aCdCDS varchar2, aUser varchar2) return number;

end;


CREATE OR REPLACE package body CNRCTB018 is

  procedure aggiornaNumeratori(aEsercizio number, aCdCds varchar2, aUser varchar2) is
   aNewNum NUMERAZIONE_DOC_CONT%rowtype;
   aTSNow date;
   aTipoUnita varchar2(50);
  begin
     aTSNow:=sysdate;

	begin
	 select cd_tipo_unita into aTipoUnita from unita_organizzativa where
	  cd_unita_organizzativa = aCdCds;
	exception when NO_DATA_FOUND then
	  IBMERR001.RAISE_ERR_GENERICO('CDS o ENTE non trovati!');
	end;

	-- Caso CDS ENTE
	if aTipoUnita = CNRCTB020.TIPO_ENTE then
     begin
	  aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_IMP;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PRI;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PRI-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PRI;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
     exception when dup_val_on_index then
	  NULL;
 	 end;

     begin
	  aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_IMP_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
     exception when dup_val_on_index then
	  NULL;
 	 end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PRI;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PRI-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PRI;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PLUR;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PLUR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PLUR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PLUR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	  NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_MAN;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACR_MOD;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;
	-- Caso CDS NON ENTE
   else

	begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_SIST;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_MAN;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV;
      aNewNum.PRIMO:=FIRST_DOC_CDS;
      aNewNum.CORRENTE:=FIRST_DOC_CDS-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_REV_PROVV;
      aNewNum.PRIMO:=FIRST_DOC_CDS_PROVV;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_PROVV-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_PROVV;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_RES_PRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_RES;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_RES-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_RES;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_RES_IMPRO;
      aNewNum.PRIMO:=FIRST_DOC_CDS_RESIM;
      aNewNum.CORRENTE:=FIRST_DOC_CDS_RESIM-1;
      aNewNum.ULTIMO:=LAST_DOC_CDS_RESIM;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_ACC_PGIRO_RES;
      aNewNum.PRIMO:=FIRST_DOC_CNR_PGIRO;
      aNewNum.CORRENTE:=FIRST_DOC_CNR_PGIRO-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR_PGIRO;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

    begin
      aNewNum.ESERCIZIO:=aEsercizio;
      aNewNum.CD_CDS:=aCdCds;
      aNewNum.CD_TIPO_DOCUMENTO_CONT:=TI_DOC_OBB_MOD;
      aNewNum.PRIMO:=FIRST_DOC_CNR;
      aNewNum.CORRENTE:=FIRST_DOC_CNR-1;
      aNewNum.ULTIMO:=LAST_DOC_CNR;
      aNewNum.UTCR:=aTSNow;
      aNewNum.DACR:=aTSNow;
      aNewNum.UTUV:=aUser;
      aNewNum.DUVA:=aTSNow;
      aNewNum.PG_VER_REC:=1;
      ins_NUMERAZIONE_DOC_CONT (aNewNum);
    exception when dup_val_on_index then
	 NULL;
	end;

   end if;
  end;

  function getNextNumDocCont(aTipo varchar2, aEs number, aCdCDS varchar2, aUser varchar2) return number is
   aNum numerazione_doc_cont%rowtype;
   aTSNow date;
   AtIPOnEW VARCHAR2(10);
  begin
   aTSNow:=sysdate;
   Begin

   /* 05.01.2006 STANI */

   aTipoNEW := aTipo;

   If aTipo = CNRCTB018.TI_DOC_OBB_PGIRO_RES Then
     aTipoNEW := CNRCTB018.TI_DOC_OBB_PGIRO;
   Elsif aTipo = CNRCTB018.TI_DOC_ACC_PGIRO_RES Then
     aTipoNEW := CNRCTB018.TI_DOC_ACC_PGIRO;
   End If;

    select * into aNum from numerazione_doc_cont where
            esercizio = aEs
  	  and cd_cds = aCdCDS
  	  and cd_tipo_documento_cont = aTipoNew
    for update nowait;
   exception when NO_DATA_FOUND then
    IBMERR001.RAISE_ERR_GENERICO('Configurazione numerazione doc. cont. di tipo '||aTipo||' non trovata per esercizio: '||aEs||' su STO:'||aCdCDS);
   end;
   if aNum.corrente + 1 > aNum.ultimo then
    IBMERR001.RAISE_ERR_GENERICO('Numerazione esaurita per doc. cont. di tipo '||aTipo||' in esercizio: '||aEs||' su STO:'||aCdCDS);
   end if;
   update numerazione_doc_cont set
    corrente=corrente+1,
    utuv=aUser,
    duva=aTSNow,
    pg_ver_rec=pg_ver_rec+1
   where
        esercizio=aEs
    and cd_cds = aCdCDS
    and cd_tipo_documento_cont = aTipoNew;
   return aNum.corrente + 1;
  end;

  procedure ins_NUMERAZIONE_DOC_CONT (aDest NUMERAZIONE_DOC_CONT%rowtype) is
  begin
   insert into NUMERAZIONE_DOC_CONT (
     ESERCIZIO
    ,CD_CDS
    ,CD_TIPO_DOCUMENTO_CONT
    ,PRIMO
    ,CORRENTE
    ,ULTIMO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CDS
    ,aDest.CD_TIPO_DOCUMENTO_CONT
    ,aDest.PRIMO
    ,aDest.CORRENTE
    ,aDest.ULTIMO
    ,aDest.UTCR
    ,aDest.DACR
    ,aDest.UTUV
    ,aDest.DUVA
    ,aDest.PG_VER_REC
    );
 end;
end;


