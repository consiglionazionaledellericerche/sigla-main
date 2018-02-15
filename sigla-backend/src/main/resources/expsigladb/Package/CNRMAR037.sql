--------------------------------------------------------
--  DDL for Package CNRMAR037
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRMAR037" as
--
-- CNRMAR037 - Package controllo/martello mandati/reversali (batch)
-- Date: 14/07/2006
-- Version: 1.15
--
-- Package per il martellamento/verifica disallineamento mandati/reversali
--
-- Dependency: IBMUTL 200/210
--
-- History:
--
-- Date: 18/12/2002
-- Version: 1.0
-- Creazione
--
-- Date: 28/01/2002
-- Version: 1.1
-- Controllo totale mandati/reversali con saldo CDS
--
-- Date: 17/04/2002
-- Version: 1.2
-- Inserimento verifica per mandati/reversali aggregati per capitolo (COMPETENZA RESIDUO)
-- Inserimento verifica per mandati/reversali aggregati per cds esercizio (COMPETENZA RESIDUO)
--
-- Date: 22/04/2002
-- Version: 1.3
-- Inserita costante per tipo gestione spesa (TIPO_SPESA='S') e tipo gestione entrat (TIPO_ENTRATA = 'E')
--
-- Date: 22/04/2002
-- Version: 1.4
-- Inserita modifica voce_f_saldi_cmp per allineare i saldi (COMMENTATA)
--
-- Date: 23/04/2002
-- Version: 1.5
-- Aggiunti dettagli informativi nel logging
--
-- Date: 24/04/2002
-- Version: 1.6
-- Introdotta funzione per codificare output della voce_f
--
-- Date: 13/05/2002
-- Version: 1.7
-- corretta query di estrazione per i mandati legati ad una obbligazione
-- la vecchia query duplicava i mandati che erano legati ad una scadenza obbligazione
-- distribuita su più scadenze voce
--
-- Date: 20/05/2003
-- Version: 1.8
-- Introdotto esercizio e cds in log per 102,104,602,604
--
-- Date: 20/05/2003
-- Version: 1.9
-- Correzione numero errore da 010 a 110
--
-- Date: 21/05/2003
-- Version: 1.10
-- modificata layout log
--
-- Date: 22/05/2003
-- Version: 1.11
-- Inserita condizione di ti_appartenenza per consultare voce_f_saldi_cmp
--
-- Date: 07/07/2003
-- Version: 1.12
-- Corretta query di estrazione che totalizza importi mandati sui singoli capitoli
--
-- Date: 07/07/2003
-- Version: 1.13
-- Corretta stato obbligazione stornato
--
-- Date: 08/07/2003
-- Version: 1.14
-- Corretta query estrazione che totalizza importi mandati sui singoli capitoli in modo che non
-- consideri i mandati che sono generati da Note di Credito
--
-- Date: 14/07/2006
-- Version: 1.15
-- Gestione Impegni/Accertamenti Residui:
-- aggiornata la funzione per tener conto anche del campo Esercizio Originale Impegno/Accertamento
--
-- Constants

TIPO_LOG_MAR_MANREV CONSTANT VARCHAR2(20):='MAR_MAN_REV00';
TIPO_COMPETENZA CONSTANT CHAR(1):='C';
TIPO_RESIDUO CONSTANT CHAR(1):='R';
TIPO_SPESA CONSTANT CHAR(1):='S';
TIPO_ENTRATA CONSTANT CHAR(1):='E';
STATO_ANNULLATO CONSTANT CHAR(1):='A';
STATO_ANNULLATO_OBB CONSTANT CHAR(1):='S';
-- Tipi di disallineamenti supportati

D_AUTOR CONSTANT VARCHAR2(10):='D_AUTOR'; --  Tipo disallineamento


D_AUTOR000 CONSTANT VARCHAR2(70):='MAN-D_AUTOR000-MAN-SUM(RIGA)'; -- Importo testata diverso da somma righe: ritorna im. testata - somma(im. righe)
--D_AUTOR005 CONSTANT VARCHAR2(10):='D_AUTOR005'; --
--D_AUTOR010 CONSTANT VARCHAR2(10):='D_AUTOR010'; --
--D_AUTOR015 CONSTANT VARCHAR2(10):='D_AUTOR015'; --
--D_AUTOR020 CONSTANT VARCHAR2(10):='D_AUTOR020'; --
--D_AUTOR025 CONSTANT VARCHAR2(10):='D_AUTOR025'; --
D_AUTOR100 CONSTANT VARCHAR2(70):='MAN-D_AUTOR100-TOT CAP CDS COMP-SALDO'; -- Totale mandati CDS Comp. != saldo mandati CDS Comp. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR105 CONSTANT VARCHAR2(70):='MAN-D_AUTOR105-TOT CAP CDS RES-SALDO'; -- Totale mandati CDS Res. != saldo mandati CDS Res. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR110 CONSTANT VARCHAR2(70):='MAN-D_AUTOR110-TOT CAP COMP-SALDO'; -- Totale mandati CNR comp. capitolo != saldo capitolo CNR comp. (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR115 CONSTANT VARCHAR2(70):='MAN-D_AUTOR115-TOT CAP RES-SALDO'; -- Totale mandati CNR res. capitolo != saldo capitolo CNR res. (voce_f_saldi_cmp): ritorna la differenza dei due
--
D_AUTOR500 CONSTANT VARCHAR2(70):='REV-D_AUTOR500-REV-SUM(IM_RIGA)'; -- Importo testata diverso da somma righe: ritorna im. testata - somma(im. righe)
-- D_AUTOR505 CONSTANT VARCHAR2(10):='D_AUTOR505'; --
-- D_AUTOR510 CONSTANT VARCHAR2(10):='D_AUTOR510'; --
-- D_AUTOR515 CONSTANT VARCHAR2(10):='D_AUTOR515'; --
-- D_AUTOR520 CONSTANT VARCHAR2(10):='D_AUTOR520'; --
-- D_AUTOR525 CONSTANT VARCHAR2(10):='D_AUTOR525'; --
D_AUTOR600 CONSTANT VARCHAR2(70):='REV-D_AUTOR600-TOT CAP CDS COMP-SALDO'; -- Totale reversali CDS Comp. != saldo reversali CDS (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR605 CONSTANT VARCHAR2(70):='REV-D_AUTOR605-TOT CAP CDS RES-SALDO'; -- Totale reversali CDS Res. != saldo reversali CDS (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR610 CONSTANT VARCHAR2(70):='REV-D_AUTOR610-TOT CAP COMP-SALDO'; -- Totale reversali CNR comp. su capitolo != saldo capitolo comp. CNR (voce_f_saldi_cmp): ritorna la differenza dei due
D_AUTOR615 CONSTANT VARCHAR2(70):='REV-D_AUTOR615-TOT CAP RES-SALDO'; -- Totale reversali CNR res. su capitolo != saldo capitolo res. CNR (voce_f_saldi_cmp): ritorna la differenza dei due

-- Functions e Procedures

-- Parametri:

-- aEs -> Esercizio
-- aCDS -> Esercizio
-- isModifica -> Y = update N preview

 procedure job_mar_autor00(job number, pg_exec number, next_date date, aEs number, aCdCds varchar2, isModifica char);

 function MSG_DIS_AUTOR(aTipo varchar2, aMan mandato%rowtype, aNota varchar2) return varchar2;
 function MSG_DIS_AUTOR(aTipo varchar2, aRev reversale%rowtype, aNota varchar2) return varchar2;

end;
