CREATE OR REPLACE PACKAGE CNRCTB575 AS
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
-- pre: non esistono contributi ritenuta gi? liquidati per il CdS, l.unit? organizzativa e l.esercizio
-- post: ritorna zero come numero progressivo di liquidazione
--
-- pre-post-name: Ritorna il numero progressivo da utilizzare per la numerazione della liquidazione dei contributi ritenuta
-- pre: non ? soddisfatta nessuna pre-post precedente
-- post: ritorna il massimo numero progressivo di liquidazione incrementato di uno. Il massimo viene individuato per esercizio, CdS e unit? organizzativa nella tabella LIQUID_CORI, contenente le informazioni di testata della liquidazione dei contributi ritenuta dei compensi.
--
-- Parametri:
-- aCdCds -> CdS che effettua il calcolo della liquidazione
-- aEs -> esercizio di riferimento
-- aCdUo -> unit? organizzativa che effettua il calcolo della liquidazione

 function getNextNumLiquid(aCdCds varchar2, aEs number, aCdUo varchar2) return number;

-- Funzione di recupero della descrizione di gruppo di liquidazione cori

 function getDesc(aLGC liquid_gruppo_cori%rowtype) return varchar2;

END;
/


CREATE OR REPLACE PACKAGE BODY CNRCTB575 AS
 procedure checkLiquidCentroAperta(aEs number) is
 begin
  if not ISLIQUIDCENTROAPERTA(aEs) then
   	 IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro per esercizio '||aEs||' risulta chiusa');
  end if;
 end;

 procedure checkLiquidLocaleAperta(aEs number) is
 begin
  if not ISLIQUIDLOCALEAPERTA(aEs) then
   	 IBMERR001.RAISE_ERR_GENERICO('La liquidazione CORI al centro per esercizio '||aEs||' risulta chiusa');
  end if;
 end;

 function isLiquidLocaleAperta(aEs number) return boolean is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,LC,LC_LOCALE_ATTIVA)='Y' then
   return true;
  else
   return false;
  end if;
 end;

 function isLiquidCentroAperta(aEs number) return boolean is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,LC,LC_CENTRO_ATTIVA)='Y' then
   return true;
  else
   return false;
  end if;
 end;

 function getDesc(aLGC liquid_gruppo_cori%rowtype) return varchar2 is
 begin
  return ' liq.n.:'||aLGC.pg_liquidazione||' es.'||aLGC.esercizio||' cds:'||aLGC.cd_cds||' uo:'||aLGC.cd_unita_organizzativa||' gr.:'||aLGC.cd_gruppo_cr||' reg.:'||aLGC.cd_regione||' com.:'||aLGC.pg_comune;
 end;

 function getNextNumLiquid(aCdCds varchar2, aEs number, aCdUo varchar2) return number is
  aNum number(10);
 begin
  begin
   select pg_liquidazione into aNum from liquid_cori where
        esercizio = aEs
    and cd_cds = aCdCds
    and cd_unita_organizzativa = aCdUo
    and pg_liquidazione = (select max(pg_liquidazione) from liquid_cori where
                               esercizio = aEs
                           and cd_cds = aCdCds
                           and cd_unita_organizzativa = aCdUo
						 )
   for update nowait;
  exception when NO_DATA_FOUND then
   return 0;
  end;
  return aNum+1;
 end;

 function isLiquidaCoriInviato(aEs number) return char is
 begin
  if CNRCTB015.GETVAL01PERCHIAVE(aEs,GESTIONE_CORI_SPEC,STATO_LIQUIDA_CORI) = STATO_LIQUID_CORI_INV then
   return 'Y';
  else
   return 'N';
  end if;
 end;

 procedure ins_LIQUID_CORI (aDest LIQUID_CORI%rowtype) is
  begin
   insert into LIQUID_CORI (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,DA_ESERCIZIO_PRECEDENTE
    ,DT_DA
    ,DT_A
    ,STATO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.DA_ESERCIZIO_PRECEDENTE
    ,aDest.DT_DA
    ,aDest.DT_A
    ,aDest.STATO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;

 procedure ins_LIQUID_GRUPPO_CORI (aDest LIQUID_GRUPPO_CORI%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CORI (
     CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,PG_LIQUIDAZIONE_ORIGINE
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,IM_LIQUIDATO
	,FL_ACCENTRATO
	,ESERCIZIO_DOC
    ,CD_CDS_DOC
    ,PG_DOC
    ,CD_CDS_OBB_ACCENTR
    ,ESERCIZIO_OBB_ACCENTR
    ,ESERCIZIO_ORI_OBB_ACCENTR
    ,PG_OBB_ACCENTR
	,STATO
	,PG_GRUPPO_CENTRO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,CD_CDS_ACC_COMPENS
	,ESERCIZIO_ACC_COMPENS
	,ESERCIZIO_ORI_ACC_COMPENS
	,PG_ACC_COMPENS
	,CD_CDS_REV
	,ESERCIZIO_REV
	,PG_REV
   ) values (
     aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.PG_LIQUIDAZIONE_ORIGINE
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.IM_LIQUIDATO
	,aDest.FL_ACCENTRATO
	,aDest.ESERCIZIO_DOC
    ,aDest.CD_CDS_DOC
    ,aDest.PG_DOC
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
	,aDest.STATO
	,aDest.PG_GRUPPO_CENTRO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.CD_CDS_ACC_COMPENS
	,aDest.ESERCIZIO_ACC_COMPENS
	,aDest.ESERCIZIO_ORI_ACC_COMPENS
	,aDest.PG_ACC_COMPENS
	,aDest.CD_CDS_REV
	,aDest.ESERCIZIO_REV
	,aDest.PG_REV
    );
 end;
 procedure ins_LIQUID_GRUPPO_CORI_DET (aDest LIQUID_GRUPPO_CORI_DET%rowtype) is
  Begin
   insert into LIQUID_GRUPPO_CORI_DET (
     UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,CD_CDS
    ,ESERCIZIO
    ,CD_UNITA_ORGANIZZATIVA
    ,PG_LIQUIDAZIONE
    ,CD_CDS_ORIGINE
    ,CD_UO_ORIGINE
    ,PG_LIQUIDAZIONE_ORIGINE
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,CD_CONTRIBUTO_RITENUTA
    ,PG_COMPENSO
    ,TI_ENTE_PERCIPIENTE
    ,DACR
	,ESERCIZIO_CONTRIBUTO_RITENUTA
   ) values (
     aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.CD_CDS
    ,aDest.ESERCIZIO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.PG_LIQUIDAZIONE
    ,aDest.CD_CDS_ORIGINE
    ,aDest.CD_UO_ORIGINE
    ,aDest.PG_LIQUIDAZIONE_ORIGINE
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.CD_CONTRIBUTO_RITENUTA
    ,aDest.PG_COMPENSO
    ,aDest.TI_ENTE_PERCIPIENTE
    ,aDest.DACR
	,aDest.ESERCIZIO_CONTRIBUTO_RITENUTA
    );
 End;

 procedure ins_LIQUID_GRUPPO_CENTRO (aDest LIQUID_GRUPPO_CENTRO%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CENTRO (
     ESERCIZIO
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,PG_GRUPPO_CENTRO
    ,STATO
    ,CD_CDS_OBB_ACCENTR
    ,ESERCIZIO_OBB_ACCENTR
    ,ESERCIZIO_ORI_OBB_ACCENTR
    ,PG_OBB_ACCENTR
    ,CD_CDS_LC
    ,CD_UO_LC
    ,PG_LC
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
	,DA_ESERCIZIO_PRECEDENTE
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.PG_GRUPPO_CENTRO
    ,aDest.STATO
    ,aDest.CD_CDS_OBB_ACCENTR
    ,aDest.ESERCIZIO_OBB_ACCENTR
    ,aDest.ESERCIZIO_ORI_OBB_ACCENTR
    ,aDest.PG_OBB_ACCENTR
    ,aDest.CD_CDS_LC
    ,aDest.CD_UO_LC
    ,aDest.PG_LC
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
	,aDest.DA_ESERCIZIO_PRECEDENTE
    );
 end;

 procedure ins_LIQUID_GRUPPO_CENTRO_COMP (aDest LIQUID_GRUPPO_CENTRO_COMP%rowtype) is
  begin
   insert into LIQUID_GRUPPO_CENTRO_COMP (
     ESERCIZIO
    ,CD_GRUPPO_CR
    ,CD_REGIONE
    ,PG_COMUNE
    ,PG_GRUPPO_CENTRO
    ,CD_UNITA_ORGANIZZATIVA
    ,CD_CDS_ACC_ACCENTR
    ,ESERCIZIO_ACC_ACCENTR
    ,ESERCIZIO_ORI_ACC_ACCENTR
    ,PG_ACC_ACCENTR
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_GRUPPO_CR
    ,aDest.CD_REGIONE
    ,aDest.PG_COMUNE
    ,aDest.PG_GRUPPO_CENTRO
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.CD_CDS_ACC_ACCENTR
    ,aDest.ESERCIZIO_ACC_ACCENTR
    ,aDest.ESERCIZIO_ORI_ACC_ACCENTR
    ,aDest.PG_ACC_ACCENTR
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    );
 end;
END;
/


