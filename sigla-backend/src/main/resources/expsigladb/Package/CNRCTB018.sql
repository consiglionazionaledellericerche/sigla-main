--------------------------------------------------------
--  DDL for Package CNRCTB018
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB018" as
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

FIRST_DOC_CNR_RES CONSTANT NUMBER    :=2011100001;    --cambiato il valore
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

FIRST_DOC_CDS_RESIM CONSTANT NUMBER    := 9140000001;
LAST_DOC_CDS_RESIM CONSTANT NUMBER     := 9999999999;

FIRST_FULL_RANGE CONSTANT NUMBER  :=          1;
LAST_FULL_RANGE CONSTANT NUMBER   := 9999999999;

-- Functions E Procedures:

 -- Inserisce in NUMERAZIONE_DOC_CONT il record aNum

  procedure ins_NUMERAZIONE_DOC_CONT (aDest NUMERAZIONE_DOC_CONT%rowtype);

 -- PRE E' stato aggiornato ad aperto lo stato dell'esercizio aEsercizio per il CDS aCdCds e il tipo di cds è uguale a ENTE
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

 -- PRE E' stato aggiornato ad aperto lo stato dell'esercizio aEsercizio per il CDS aCdCds e il tipo di cds è diverso da ENTE
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
 -- Se è stato raggiunto il massimo numeratorem viene sollevata un'eccezione applicativa
 --
 -- aTipo -> Tipo di documento (es. OBB)
 -- aEs -> Esercizio contabile
 -- aCdCds -> Codice del centro di spesa
 -- aUser -> Codice utente

 function getNextNumDocCont(aTipo varchar2, aEs number, aCdCDS varchar2, aUser varchar2) return number;

end;
