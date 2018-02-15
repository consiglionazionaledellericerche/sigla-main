--------------------------------------------------------
--  DDL for Package CNRCTB575
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB575" AS
--==============================================================================
--
-- CNRCTB575 - Gestione tabelle liquidazione CORI
--
-- Date: 19/07/2006
-- Version: 3.6
--
-- Dependency: CNRCTB 015
--
-- History:
--
-- Date: 02/06/2002
-- Version: 1.0
-- Creazione
--
-- Date: 24/06/2002
-- Version: 1.1
-- Gestione del momento in cui estrarre i CORI per la liquidazione
--
-- Date: 24/06/2002
-- Version: 1.2
-- Revisione tabelle liquidazione + aggiunta numerazione liquidazione con max+1 per UO
--
-- Date: 25/06/2002
-- Version: 1.3
-- Nuova costante di stato + revisione tabelle
--
-- Date: 03/07/2002
-- Version: 1.4
-- Aggiunto esercizio obb accentr su liquid gruppo cori
-- Aggiunto tipo cori speciale IVA
--
-- Date: 04/07/2002
-- Version: 1.5
-- Aggiunto flag_accentrato ed esercizio origine del documento autorizzatorio
--
-- Date: 18/07/2002
-- Version: 1.6
-- Aggiornamento documentazione
--
-- Date: 20/02/2003
-- Version: 2.0
-- Metodo di insert liquid_gruppo_centro
-- Revisione per nuova versione versamenti CORI
--
-- Date: 07/05/2003
-- Version: 3.0
-- Modifiche per gestione liq. CORI in chiusura esercizio: aggiunto esercizio origine
--
-- Date: 14/05/2003
-- Version: 3.1
-- Gestione compensazioni a fine esercizio e separazione liquidazioni al centro
--
-- Date: 20/05/2003
-- Version: 3.2
-- Nuove costanti
--
-- Date: 19/06/2003
-- Version: 3.3
-- Gestione liquid_gruppo_centro_comp
--
-- Date: 23/06/2003
-- Version: 3.4
-- Tolto rif. acc accentr da liquid gruppo centro ed aggiunto in liquid gruppo cori
-- il riferimento alla reversale libera di credito CORI verso erario
--
-- Date: 25/06/2003
-- Version: 3.5
-- Controllo liquidazione locale/centro aperta
--
-- Date: 19/07/2006
-- Version: 3.6
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Creazione Package.
--
--==============================================================================
--
-- Constants
--

STATO_INIZIALE CONSTANT VARCHAR2(5):='I';
STATO_LIQUIDATO CONSTANT VARCHAR2(5):='L';
STATO_TRASFERITO CONSTANT VARCHAR2(5):='T';

STATO_GRUPPO_CENTRO_INIZIALE CONSTANT VARCHAR2(5):='I';
STATO_GRUPPO_CENTRO_CHIUSO CONSTANT VARCHAR2(5):='C';

GESTIONE_CORI_SPEC CONSTANT VARCHAR(50):='GESTIONE_CORI_SPECIALE';
STATO_LIQUIDA_CORI CONSTANT VARCHAR(100):='STATO_LIQUIDA_CORI';

STATO_LIQUID_CORI_EME CONSTANT VARCHAR(100):='EME';
STATO_LIQUID_CORI_INV CONSTANT VARCHAR(100):='INV';

TI_CORI_SPECIALE CONSTANT VARCHAR2(50):='CORI_SPECIALE';
TI_CORI_IVA CONSTANT VARCHAR2(100):='IVA';

ELEMENTO_VOCE_SPECIALE CONSTANT VARCHAR2(100):='ELEMENTO_VOCE_SPECIALE';
COMPENSAZIONE_CORI CONSTANT VARCHAR2(100):='COMPENSAZIONE_CORI';

LC CONSTANT VARCHAR2(100):='LIQUID_CORI';
LC_LOCALE_ATTIVA CONSTANT VARCHAR2(100):='LOCALE_ATTIVA';
LC_CENTRO_ATTIVA CONSTANT VARCHAR2(100):='CENTRO_ATTIVA';

--
-- Functions e Procedures
--

-- Controlla lo stato di apertura della liquidazione locale de centrale
 function isLiquidLocaleAperta(aEs number) return boolean;
 function isLiquidCentroAperta(aEs number) return boolean;
 procedure checkLiquidCentroAperta(aEs number);
 procedure checkLiquidLocaleAperta(aEs number);

-- Ritorna true se i contributi vanno raccolti sullo stato di inviato a cassiere del relativo mandato su compenso
 function isLiquidaCoriInviato(aEs number) return char;

 procedure ins_LIQUID_CORI (aDest LIQUID_CORI%rowtype);
 procedure ins_LIQUID_GRUPPO_CORI (aDest LIQUID_GRUPPO_CORI%rowtype);
 procedure ins_LIQUID_GRUPPO_CORI_DET (aDest LIQUID_GRUPPO_CORI_DET%rowtype);
 procedure ins_LIQUID_GRUPPO_CENTRO (aDest LIQUID_GRUPPO_CENTRO%rowtype);
 procedure ins_LIQUID_GRUPPO_CENTRO_COMP (aDest LIQUID_GRUPPO_CENTRO_COMP%rowtype);

-- Ritorna il prossimo progressivo da utilizzare per la numerazione della liquidazione nell'ambito di un CDS/UO
--
-- pre-post-name: Ritorna zero per la prima liquidazione dei contributi ritenuta effettuata
-- pre: non esistono contributi ritenuta già liquidati per il CdS, l.unità organizzativa e l.esercizio
-- post: ritorna zero come numero progressivo di liquidazione
--
-- pre-post-name: Ritorna il numero progressivo da utilizzare per la numerazione della liquidazione dei contributi ritenuta
-- pre: non è soddisfatta nessuna pre-post precedente
-- post: ritorna il massimo numero progressivo di liquidazione incrementato di uno. Il massimo viene individuato per esercizio, CdS e unità organizzativa nella tabella LIQUID_CORI, contenente le informazioni di testata della liquidazione dei contributi ritenuta dei compensi.
--
-- Parametri:
-- aCdCds -> CdS che effettua il calcolo della liquidazione
-- aEs -> esercizio di riferimento
-- aCdUo -> unità organizzativa che effettua il calcolo della liquidazione

 function getNextNumLiquid(aCdCds varchar2, aEs number, aCdUo varchar2) return number;

-- Funzione di recupero della descrizione di gruppo di liquidazione cori

 function getDesc(aLGC liquid_gruppo_cori%rowtype) return varchar2;

END;
