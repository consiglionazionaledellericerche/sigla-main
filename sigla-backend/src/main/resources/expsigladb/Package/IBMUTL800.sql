--------------------------------------------------------
--  DDL for Package IBMUTL800
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "IBMUTL800" AS
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
