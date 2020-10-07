--------------------------------------------------------
--  DDL for Package Body CNRCTB080
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB080" IS

-- Ritorna il rowtype del terzo con codice aCdTerzo
FUNCTION getTerzo(aCdTerzo number) return terzo%rowtype is
 aTerzo terzo%rowtype;
begin
 begin
  select * into aTerzo from terzo where
   cd_terzo = aCdTerzo;
  return aTerzo;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato:'||aCdTerzo);
 end;
end;

-- Ritorna il rowtype del terzo valido con codice anagrafico aCdAnag
FUNCTION getTerzoDaCdAnag(aCdAnag number) return terzo%rowtype is
 aTerzo terzo%rowtype;
begin
 begin
  Select * Into aTerzo
  From terzo
  Where cd_anag = aCdAnag
    And dt_fine_rapporto Is Null;
  return aTerzo;
 exception
     When NO_DATA_FOUND then
        IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato per Anagrafico:'||aCdAnag);
     When Too_Many_Rows then
        IBMERR001.RAISE_ERR_GENERICO('Esistono più terzi validi per Anagrafico:'||aCdAnag);
 end;
end;

function getDescTipoPag(aTipo char) return varchar2 is
begin
 if aTipo is null then
  return 'tutti';
 elsif aTipo = TI_PAGAMENTO_BANCARIO then
  return 'bancario';
 elsif aTipo = TI_PAGAMENTO_BANCA_ITALIA then
  return 'banca d''Italia';
 elsif aTipo = TI_PAGAMENTO_QUIETANZA then
  return 'quietanza';
 elsif aTipo = TI_PAGAMENTO_ALTRO then
  return 'altro';
 elsif aTipo = TI_PAGAMENTO_POSTALE then
  return 'postale';
 end if;
 return '';
end;

PROCEDURE getTerzoPerUO(aCdUO varchar2, aCdTerzo out number) is
begin
 begin
  select cd_terzo into aCdTerzo from terzo where
   cd_unita_organizzativa = aCdUO
   and dt_fine_rapporto is null
   and cd_terzo in( select min(cd_terzo) from terzo where
   cd_unita_organizzativa = aCdUO
   and dt_fine_rapporto is null);
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato per UO:'||aCdUO);
 end;
end;

PROCEDURE getTerzoPerUO(aCdUO varchar2, aCdTerzo out number, aCdModPag out varchar2, aPgBanca out number,aEsercizio in number) is
 aTipoUnita varchar2(10);
 parametri  parametri_cnr%rowtype;
 aAbiCCEnte varchar2(50);
 aCabCCEnte varchar2(50);
 aNCCEnte varchar2(50);
begin
 begin

  select cd_terzo into aCdTerzo from terzo where
   cd_unita_organizzativa = aCdUO
   and dt_fine_rapporto is null
   and cd_terzo in( select min(cd_terzo) from terzo where
   cd_unita_organizzativa = aCdUO
   and dt_fine_rapporto is null);
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato per UO:'||aCdUO);
 end;
 for aTempModPag in (
     select distinct a.cd_modalita_pag
     from   modalita_pagamento a,
            rif_modalita_pagamento ar
     where      ar.cd_modalita_pag = a.cd_modalita_pag
            and ar.ti_pagamento = TI_PAGAMENTO_BANCARIO
            and a.cd_terzo = aCdTerzo
            and a.cd_terzo_delegato IS NULL
            and a.dacr = (
                          select max(a1.dacr)
                          from   modalita_pagamento a1, rif_modalita_pagamento ar1
                          where      ar1.ti_pagamento = TI_PAGAMENTO_BANCARIO
                                 and a1.cd_modalita_pag = ar1.cd_modalita_pag
                                 and a1.cd_terzo = a.cd_terzo
                                 and a1.cd_terzo_delegato IS NULL)
     order by a.cd_modalita_pag asc
 ) loop
  aCdModPag:=aTempModPag.cd_modalita_pag;
  exit;
 end loop;

 select cd_tipo_unita into aTipoUnita from unita_organizzativa where
    cd_unita_organizzativa = aCdUO;
	aAbiCCEnte:=CNRCTB015.GETVAL01PERCHIAVE(CONTO_CORRENTE_SPECIALE,ENTE);
  aCabCCEnte:=CNRCTB015.GETVAL02PERCHIAVE(CONTO_CORRENTE_SPECIALE,ENTE);
   aNCCEnte:=CNRCTB015.GETVAL03PERCHIAVE(CONTO_CORRENTE_SPECIALE,ENTE);
 if aTipoUnita = CNRCTB020.TIPO_ENTE then
  begin
      select a.pg_banca into aPgBanca from banca a
     where
	            a.ti_pagamento = TI_PAGAMENTO_BANCARIO
            and a.cd_terzo = aCdTerzo
            and a.fl_cancellato = 'N'
			and a.abi like '%'||aAbiCCEnte
			and a.cab like '%'||aCabCCEnte
			and a.numero_conto like '%'||aNCCEnte;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Ente) non trovate per terzo UO ENTE:'||aCdTerzo);
  end;
 else
   begin
	   select * into parametri from parametri_cnr where esercizio = aEsercizio;
		  if parametri.fl_tesoreria_unica='N' then
		  begin
		     select a.pg_banca into aPgBanca from banca a
		     where
			            a.ti_pagamento = TI_PAGAMENTO_BANCARIO
		            and a.cd_terzo = aCdTerzo
		            and a.fl_cancellato = 'N'
		            and a.fl_cc_cds = 'Y';
		  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Cds) non trovate per terzo UO:'||aCdTerzo);
		  end;
		 else
		 	begin
		 	select a.pg_banca into aPgBanca from banca a
     where
	            a.ti_pagamento = TI_PAGAMENTO_BANCARIO
            and a.cd_terzo = aCdTerzo
            and a.fl_cancellato = 'N'
			and a.abi = aAbiCCEnte
			and a.cab = aCabCCEnte
			and a.numero_conto like '%'||aNCCEnte;

	  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Cds) non trovate per terzo UO:'||aCdTerzo);
	  end;
	 end if;
	  exception when NO_DATA_FOUND then
		   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Cds) non trovate per terzo UO:'||aCdTerzo);
	  end;
 end if;

 if
	    aPgBanca is null
	 or aCdModPag is null
	then
	 IBMERR001.RAISE_ERR_GENERICO('Modalità di pagamento più recenti non trovate per terzo UO:'||aCdTerzo||' di tipo: '||getDescTipoPag(TI_PAGAMENTO_BANCARIO));
 end if;
end;

PROCEDURE getTerzoPerEnteContoBI(aCdUO varchar2, aCdTerzo out number, aCdModPag out varchar2, aPgBanca out number) is
begin
 begin
  select cd_terzo into aCdTerzo from terzo where
   cd_unita_organizzativa = aCdUO;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Terzo non trovato per UO:'||aCdUO);
 end;
 for aTempModPag in (
     select distinct a.cd_modalita_pag
     from   modalita_pagamento a,
            rif_modalita_pagamento ar
     where      ar.cd_modalita_pag = a.cd_modalita_pag
            and ar.ti_pagamento = TI_PAGAMENTO_BANCA_ITALIA
            and a.cd_terzo = aCdTerzo
            and a.cd_terzo_delegato IS NULL
            and a.dacr = (
                          select max(a1.dacr)
                          from   modalita_pagamento a1, rif_modalita_pagamento ar1
                          where      ar1.ti_pagamento = TI_PAGAMENTO_BANCA_ITALIA
                                 and a1.cd_modalita_pag = ar1.cd_modalita_pag
                                 and a1.cd_terzo = a.cd_terzo
                                 and a1.cd_terzo_delegato IS NULL)
     order by a.cd_modalita_pag asc
 ) loop
  aCdModPag:=aTempModPag.cd_modalita_pag;
  exit;
 end loop;

  declare
   aAbiCCEnteBI varchar2(50);
   aCabCCEnteBI varchar2(50);
   aNCCEnteBI varchar2(50);
  begin
     aAbiCCEnteBI:=CNRCTB015.GETVAL01PERCHIAVE(CONTO_CORRENTE_SPECIALE_BI,ENTE);
     aCabCCEnteBI:=CNRCTB015.GETVAL02PERCHIAVE(CONTO_CORRENTE_SPECIALE_BI,ENTE);
     aNCCEnteBI:=CNRCTB015.GETVAL03PERCHIAVE(CONTO_CORRENTE_SPECIALE_BI,ENTE);
     select a.pg_banca into aPgBanca from banca a
     where
	            a.ti_pagamento = TI_PAGAMENTO_BANCA_ITALIA
            and a.cd_terzo = aCdTerzo
            and a.fl_cancellato = 'N'
			and a.abi like '%'||aAbiCCEnteBI
			and a.cab like '%'||aCabCCEnteBI
			and a.numero_conto like '%'||aNCCEnteBI ;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Informazioni di tipo BANCA (Ente) non trovate per terzo UO ENTE:'||aCdTerzo);
  end;

 if aPgBanca is null
	  or aCdModPag is null
 then
	 IBMERR001.RAISE_ERR_GENERICO('Modalità di pagamento più recenti non trovate per terzo UO:'||aCdTerzo||' di tipo: '||getDescTipoPag(TI_PAGAMENTO_BANCA_ITALIA));
 end if;
end;

PROCEDURE getModPagUltime(aCdTerzo number, aCdModPag out varchar2, aPgBanca out number) is
begin
 getModPagUltime(aCdTerzo,aCdModPag,aPgBanca, null);
end;

PROCEDURE getModPagUltime(aCdTerzo number, aCdModPag out varchar2, aPgBanca out number, aTipo char) is
begin
 if
       aTipo is not null
   and aTipo not in (TI_PAGAMENTO_BANCARIO,TI_PAGAMENTO_ALTRO,TI_PAGAMENTO_QUIETANZA,TI_PAGAMENTO_POSTALE)
 then
  IBMERR001.RAISE_ERR_GENERICO('Tipo di modalità di pagamento specificata non valido:'||aTipo);
 end if;
 for aTempModPag in (
     select distinct a.cd_modalita_pag
     from   modalita_pagamento a,
            rif_modalita_pagamento ar
     where      ar.cd_modalita_pag = a.cd_modalita_pag
            and ar.ti_pagamento = nvl(aTipo,ar.ti_pagamento)
            and a.cd_terzo = aCdTerzo
            and a.cd_terzo_delegato IS NULL
            and a.dacr = (
                          select max(a1.dacr)
                          from   modalita_pagamento a1, rif_modalita_pagamento ar1
                          where      ar1.ti_pagamento = nvl(aTipo,ar1.ti_pagamento)
                                 and a1.cd_modalita_pag = ar1.cd_modalita_pag
                                 and a1.cd_terzo = a.cd_terzo
                                 and a1.cd_terzo_delegato IS NULL)
     order by a.cd_modalita_pag asc
 ) loop
  aCdModPag:=aTempModPag.cd_modalita_pag;
  exit;
 end loop;

 for aTempModPag in (
     select distinct a.pg_banca from banca a
     where      a.ti_pagamento = nvl(aTipo,a.ti_pagamento)
            and a.cd_terzo = aCdTerzo
            and a.fl_cancellato = 'N'
            and a.cd_terzo_delegato IS NULL
            and a.dacr = (
                          select max(a1.dacr)
                          from   banca a1
                          where      a1.ti_pagamento = nvl(aTipo,a1.ti_pagamento)
                                 and a1.cd_terzo = a.cd_terzo
                                 and a1.fl_cancellato = 'N'
                                 and a1.cd_terzo_delegato IS NULL)
     order by a.pg_banca desc
 ) loop
  aPgBanca:=aTempModPag.pg_banca;
  exit;
 end loop;

 if
	    aPgBanca is null
	 or aCdModPag is null
	then
	 IBMERR001.RAISE_ERR_GENERICO('Modalità di pagamento più recenti non trovate per terzo:'||aCdTerzo||' di tipo: '||getDescTipoPag(aTipo));
	end if;
end;

-- =============================================================================
-- Ritorna il codice del terzo diversi di default definito in CONFIGURAZIONE_CNR
-- =============================================================================

FUNCTION getTerziDiversiDefault
   RETURN NUMBER IS

BEGIN

   RETURN CNRCTB015.getIm01PerChiave('TERZO_SPECIALE','CODICE_DIVERSI_DEFAULT');

END getTerziDiversiDefault;

-- =================================================================================================
-- Ritorna il record di ANAGRAFICO dato un TERZO.cd_terzo di riferimento
-- =================================================================================================
FUNCTION getAnag
   (
    aCdTerzo NUMBER
   ) RETURN ANAGRAFICO%ROWTYPE IS
   aAnag ANAGRAFICO%ROWTYPE;

BEGIN

   SELECT B.* INTO aAnag
   FROM   TERZO A,
          ANAGRAFICO B
   WHERE  A.cd_terzo = aCdTerzo AND
          B.cd_anag = A.cd_anag;

   RETURN aAnag;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO('Entità anagrafica non trovata per terzo: '||aCdTerzo);

END getAnag;

-- =================================================================================================
-- Ritorna il record di ANAGRAFICO dato un ANAGRAFICO.cd_anag di riferimento
-- =================================================================================================
FUNCTION getAnagDaCdAnag
   (
    aCdAnag NUMBER
   ) RETURN ANAGRAFICO%ROWTYPE IS
   aRecAnagrafico ANAGRAFICO%ROWTYPE;

BEGIN

   SELECT * INTO aRecAnagrafico
   FROM   ANAGRAFICO
   WHERE  cd_anag = aCdAnag;

   RETURN aRecAnagrafico;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO('Entità anagrafica non trovata per codice: ' || aCdAnag);

END getAnagDaCdAnag;


-- =================================================================================================
-- Ritorna il record di MONTANTI dato un ANAGRAFICO.cd_anag di riferimento
-- =================================================================================================
FUNCTION getMontanti
   (
    aEsercizio NUMBER,
    aCdAnag NUMBER,
    eseguiLock CHAR
   ) RETURN MONTANTI%ROWTYPE IS
   aMontante MONTANTI%ROWTYPE;

BEGIN

   IF eseguiLock = 'Y' THEN

      SELECT * INTO aMontante
      FROM   MONTANTI
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag
      FOR UPDATE NOWAIT;

   ELSE

      SELECT * INTO aMontante
      FROM   MONTANTI
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   END IF;

   RETURN aMontante;

EXCEPTION

   WHEN no_data_found THEN
        aMontante.esercizio:=aEsercizio;
        aMontante.cd_anag:=aCdAnag;
        aMontante.irpef_dipendenti:=0;
        aMontante.inps_dipendenti:=0;
        aMontante.inps_tesoro_dipendenti:=0;
        aMontante.riduz_dipendenti:=0;
        aMontante.fondo_fs_dipendenti:=0;
        aMontante.irpef_altri:=0;
        aMontante.inps_altri:=0;
        aMontante.inail_altri:=0;
        aMontante.riduz_altri:=0;
        aMontante.irpef_lordo_dipendenti:=0;
        aMontante.irpef_lordo_altri:=0;
        aMontante.deduzione_irpef_dipendenti:=0;
        aMontante.deduzione_irpef_altri:=0;
        aMontante.deduzione_family_altri:=0;
        aMontante.inps_occasionali:=0;
        aMontante.inpgi_altri:=0;
        aMontante.enpapi_altri:=0;
        RETURN aMontante;

END getMontanti;

-- =================================================================================================
-- Inserisce un record in tabella MONTANTI
-- =================================================================================================
PROCEDURE insMontanti
   (
    aRecMontante MONTANTI%ROWTYPE
   ) IS

BEGIN
   INSERT INTO MONTANTI
          (esercizio,
           cd_anag,
           irpef_dipendenti,
           inps_dipendenti,
           inps_tesoro_dipendenti,
           riduz_dipendenti,
           fondo_fs_dipendenti,
           irpef_altri,
           inps_altri,
           inail_altri,
           riduz_altri,
           dacr,
           utcr,
           duva,
           utuv,
           pg_ver_rec,
           irpef_lordo_dipendenti,
           irpef_lordo_altri,
           deduzione_irpef_dipendenti,
           deduzione_irpef_altri,
           inps_occasionali,
           deduzione_family_altri,
           inpgi_altri,
           enpapi_altri)
   VALUES (aRecMontante.esercizio,
           aRecMontante.cd_anag,
           aRecMontante.irpef_dipendenti,
           aRecMontante.inps_dipendenti,
           aRecMontante.inps_tesoro_dipendenti,
           aRecMontante.riduz_dipendenti,
           aRecMontante.fondo_fs_dipendenti,
           aRecMontante.irpef_altri,
           aRecMontante.inps_altri,
           aRecMontante.inail_altri,
           aRecMontante.riduz_altri,
           aRecMontante.dacr,
           aRecMontante.utcr,
           aRecMontante.duva,
           aRecMontante.utuv,
           aRecMontante.pg_ver_rec,
           aRecMontante.irpef_lordo_dipendenti,
           aRecMontante.irpef_lordo_altri,
           aRecMontante.deduzione_irpef_dipendenti,
           aRecMontante.deduzione_irpef_altri,
           aRecMontante.inps_occasionali,
           aRecMontante.deduzione_family_altri,
           aRecMontante.inpgi_altri,
           aRecMontante.enpapi_altri);

END insMontanti;

-- =================================================================================================
-- Aggiorna un record in tabella MONTANTI
-- =================================================================================================
PROCEDURE upgMontantiAltri
   (
    aRecMontante MONTANTI%ROWTYPE
   ) IS

BEGIN

   UPDATE MONTANTI
   SET    irpef_altri = aRecMontante.irpef_altri,
          inps_altri = aRecMontante.inps_altri,
          inail_altri = aRecMontante.inail_altri,
          riduz_altri = aRecMontante.riduz_altri,
          irpef_lordo_altri = aRecMontante.irpef_lordo_altri,
          deduzione_irpef_altri = aRecMontante.deduzione_irpef_altri,
          deduzione_family_altri = aRecMontante.deduzione_family_altri,
          inps_occasionali = aRecMontante.inps_occasionali,
          inpgi_altri = aRecMontante.inpgi_altri,
          enpapi_altri = aRecMontante.enpapi_altri
   WHERE  esercizio = aRecMontante.esercizio AND
          cd_anag = aRecMontante.cd_anag;

END upgMontantiAltri;

-- =================================================================================================
-- Aggiorna un record in tabella MONTANTI per i Dip
-- =================================================================================================
PROCEDURE upgMontantiDip
   (
    aRecMontante MONTANTI%ROWTYPE
   ) IS

BEGIN

   UPDATE MONTANTI
   SET    irpef_dipendenti = aRecMontante.irpef_dipendenti,
          inps_dipendenti = aRecMontante.inps_dipendenti,
          inps_tesoro_dipendenti = aRecMontante.inps_tesoro_dipendenti,
          riduz_dipendenti = aRecMontante.riduz_dipendenti,
          fondo_fs_dipendenti = aRecMontante.fondo_fs_dipendenti,
          irpef_lordo_dipendenti = aRecMontante.irpef_lordo_dipendenti,
          deduzione_irpef_dipendenti = aRecMontante.deduzione_irpef_dipendenti
   WHERE  esercizio = aRecMontante.esercizio AND
          cd_anag = aRecMontante.cd_anag;

END upgMontantiDip;

-- =================================================================================================
-- Ritorna il record di RIF_INQUADRAMENTO dato un codice anagrafico, un tipo rapporto e una data
-- di validità di riferimento
-- =================================================================================================
FUNCTION getRifInquadramento
   (
    aCdTipoRapporto VARCHAR2,
    aCdAnag NUMBER,
    aDataRif DATE
   ) RETURN RIF_INQUADRAMENTO%ROWTYPE IS
   aRifInquadramento RIF_INQUADRAMENTO%ROWTYPE;

BEGIN

   SELECT C.* INTO aRifInquadramento
   FROM   RAPPORTO A,
          INQUADRAMENTO B,
          RIF_INQUADRAMENTO C
   WHERE  A.cd_tipo_rapporto = aCdTipoRapporto AND
          A.cd_anag = aCdAnag AND
          A.dt_ini_validita <= aDataRif AND
          A.dt_fin_validita >= aDataRif AND
          B.cd_tipo_rapporto = A.cd_tipo_rapporto AND
          B.cd_anag = A.cd_anag AND
          B.dt_ini_validita_rapporto = A.dt_ini_validita AND
          B.dt_ini_validita <= aDataRif AND
          B.dt_fin_validita >= aDataRif AND
          C.pg_rif_inquadramento = B.pg_rif_inquadramento;

   RETURN aRifInquadramento;

EXCEPTION

   WHEN no_data_found THEN
        IBMERR001.RAISE_ERR_GENERICO('Inquadramento non definito per il soggetto anagrafico: ' || aCdAnag);

   WHEN Too_Many_Rows THEN
        IBMERR001.RAISE_ERR_GENERICO('In data '||To_Char(aDataRif, 'dd/mm/yyyy')||' risulta presente più di un inquadramento per il soggetto anagrafico: ' || aCdAnag);

END getRifInquadramento;

-- =================================================================================================
-- Verifica l'esistenza di una entrata in V_TERZO_PER_COMPENSO per controllo validazione
-- (per cessazione e rapporto) del terzo
-- =================================================================================================
FUNCTION chkEsisteTerzoPerCompenso
   (
    aCdTerzo NUMBER,
    aCdTipoRapporto VARCHAR2,
    aTiDipendenteAltro CHAR,
    aDtValidaTerzo DATE,
    aDtValidaRapportoIni DATE,
    aDtValidaRapportoFin DATE
   ) RETURN INTEGER IS

   esisteTerzo INTEGER;
   memDataDa DATE;
   memDataA DATE;

   i BINARY_INTEGER;

   aRecVTerzoPerCompenso V_TERZO_PER_COMPENSO%ROWTYPE;

   -- Matrice di appoggio per normalizzazione intervallo rapporto

   TYPE intervalloDateRec IS RECORD
       (
        tDataDa DATE:=NULL,
        tDataA DATE:=NULL
       );
   TYPE intervalloDateTab IS TABLE OF IntervalloDateRec
        INDEX BY BINARY_INTEGER;
   aIntervalloDate intervalloDateTab;

   gen_cur GenericCurTyp;

BEGIN

   esisteTerzo:=0;
   aIntervalloDate.DELETE;
   memDataDa:=NULL;
   memDataA:=NULL;

   -------------------------------------------------------------------------------------------------
   -- Controllo validità del terzo

   BEGIN

      OPEN gen_cur FOR

           SELECT *
           FROM   V_TERZO_PER_COMPENSO
           WHERE  cd_terzo = aCdTerzo AND
                  cd_tipo_rapporto = aCdTipoRapporto AND
                  ti_dipendente_altro = aTiDipendenteAltro AND
                     (
                        dt_fine_validita_terzo IS NULL OR
                        dt_fine_validita_terzo >= aDtValidaTerzo
                     )
           ORDER BY dt_ini_validita;

      LOOP

         FETCH gen_cur INTO
               aRecVTerzoPerCompenso;

         EXIT WHEN gen_cur%NOTFOUND;

         esisteTerzo:=1;

         -- Ho scelto di controllare anche la validità del rapporto. Costruisco l'array delle date di validità
         -- accorpando periodi contigui

         IF (aDtValidaRapportoIni IS NOT NULL AND
             aDtValidaRapportoFin IS NOT NULL) THEN

            IF memDataDa IS NULL THEN
               memDataDa:=aRecVTerzoPerCompenso.dt_ini_validita;
               memDataA:=aRecVTerzoPerCompenso.dt_fin_validita;
            ELSE
               IF aRecVTerzoPerCompenso.dt_ini_validita = memDataA + 1 THEN
                  memDataA:=aRecVTerzoPerCompenso.dt_fin_validita;
               ELSE
                  i:=aIntervalloDate.COUNT + 1;
                  aIntervalloDate(i).tDataDa:=memDataDa;
                  aIntervalloDate(i).tDataA:=memDataA;
                  memDataDa:=aRecVTerzoPerCompenso.dt_ini_validita;
                  memDataA:=aRecVTerzoPerCompenso.dt_fin_validita;
               END IF;
            END IF;

         -- Ho scelto di non controllare anche la validità del rapporto.

         ELSE

            EXIT;

         END IF;

      END LOOP;

      CLOSE gen_cur;

   END;

   -- Se devo controllare la validità del rapporto memorizzo nell'array l'ultimo intervallo elaborato

   BEGIN

      IF (aDtValidaRapportoIni IS NOT NULL AND
          aDtValidaRapportoFin IS NOT NULL) THEN

         esisteTerzo:=0;

         IF memDataDa IS NOT NULL THEN
            i:=aIntervalloDate.COUNT + 1;
            aIntervalloDate(i).tDataDa:=memDataDa;
            aIntervalloDate(i).tDataA:=memDataA;
         END IF;

         IF aIntervalloDate.COUNT > 0 THEN

            FOR i IN aIntervalloDate.FIRST .. aIntervalloDate.LAST

            LOOP

               IF (aIntervalloDate(i).tDataDa <= aDtValidaRapportoIni AND
                   aIntervalloDate(i).tDataA >= aDtValidaRapportoIni AND
                   aIntervalloDate(i).tDataDa <= aDtValidaRapportoFin AND
                   aIntervalloDate(i).tDataA >= aDtValidaRapportoFin) THEN
                  esisteTerzo:=1;
                  EXIT;
               END IF;

            END LOOP;

         END IF;

      END IF;

   END;

   RETURN esisteTerzo;

END chkEsisteTerzoPerCompenso;

-- =================================================================================================
-- Verifica se, per un dato soggetto anagrafico, sono presenti detrazioni familiari
-- =================================================================================================
FUNCTION chkEsisteDetrazFam
   (
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDataDa DATE,
    aDataA DATE
   ) RETURN INTEGER IS
   esisteDetraz INTEGER;

BEGIN

   SELECT COUNT(*) INTO esisteDetraz
   FROM   DUAL
   WHERE  EXISTS
          (SELECT 1
           FROM   CARICO_FAMILIARE_ANAG
           WHERE  cd_anag = aCdAnag AND
                  (
                      (dt_ini_validita <= aDataDa AND
                       dt_fin_validita >= aDataDa)
                   OR
                      (dt_ini_validita <= aDataA AND
                       dt_fin_validita >= aDataA)
                   OR
                      (dt_ini_validita >= aDataDa AND
                       dt_fin_validita <= aDataA)
                  )
           );

   RETURN esisteDetraz;

END chkEsisteDetrazFam;

-- =================================================================================================
-- Verifica se, per un dato soggetto anagrafico, sono presenti carichi familiari non validi
-- =================================================================================================
FUNCTION chkEsisteCaricoFamNonValido
   (
    aCdAnag ANAGRAFICO.cd_anag%TYPE,
    aDataDa DATE,
    aDataA DATE
   ) RETURN INTEGER IS
   esisteCarico INTEGER;

BEGIN

   SELECT COUNT(*)
   INTO esisteCarico
   From   CARICO_FAMILIARE_ANAG
   Where  cd_anag = aCdAnag
     And (
            (dt_ini_validita <= aDataDa AND
             dt_fin_validita >= aDataDa)
          Or
            (dt_ini_validita <= aDataA AND
             dt_fin_validita >= aDataA)
          Or
            (dt_ini_validita >= aDataDa AND
             dt_fin_validita <= aDataA)
          )
     And codice_fiscale Is Null;
   RETURN esisteCarico;

END chkEsisteCaricoFamNonValido;

-- =================================================================================================
-- Recupero del flag no tax area per gestione finanziaria 2003
-- =================================================================================================
FUNCTION getAnagFlNoTaxArea
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   calcolaDeduzione CHAR(1);
   aFlNoTaxArea CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo deduzioni
   -- Y = Non attiva calcolo deduzioni

   aFlNoTaxArea:='Y';

   -- Controllo generale se attivare o meno il calcolo delle deduzioni
   -- Controllo non più valido perchè non gestito con gli anni
   /*
   calcolaDeduzione:=CNRCTB015.getVal01PerChiave(ATTIVA_DEDUZIONE_KEY1,
                                                 ATTIVA_DEDUZIONE_KEY2);
   */
   -- Il controllo precedente è stato sostituito leggendo dai parametri le attivazioni delle gestioni
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   calcolaDeduzione := rec_parametri_cnr.fl_deduzione_irpef;

   IF calcolaDeduzione = 'N' THEN
      RETURN aFlNoTaxArea;
   END IF;

   aFlNoTaxArea:='N';

   BEGIN

      SELECT NVL(fl_notaxarea,'N') INTO aFlNoTaxArea
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aFlNoTaxArea:='N';

   END;

   RETURN aFlNoTaxArea;

END getAnagFlNoTaxArea;

-- =================================================================================================
-- Recupero del flag no family area per gestione finanziaria 2005
-- =================================================================================================
FUNCTION getAnagFlNoFamilyArea
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   calcolaDeduzione CHAR(1);
   aFlNoFamilyArea CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;
Begin

   -- Dominio
   -- N = Attiva calcolo deduzioni
   -- Y = Non attiva calcolo deduzioni

   aFlNoFamilyArea:='Y';

   -- Controllo generale se attivare o meno il calcolo della deduzione family leggendo dai parametri
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   calcolaDeduzione := rec_parametri_cnr.fl_deduzione_family;

   IF calcolaDeduzione = 'N' THEN
      RETURN aFlNoFamilyArea;
   END IF;


   BEGIN

      SELECT NVL(fl_nofamilyarea,'N') INTO aFlNoFamilyArea
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aFlNoFamilyArea:='N';

   END;

   RETURN aFlNoFamilyArea;

END getAnagFlNoFamilyArea;
-- =================================================================================================
-- Recupero delle detrazioni personali impostate da utente a valere in sede di conguaglio
-- =================================================================================================
FUNCTION getAnagImDetrazionePer
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN NUMBER IS
   aImportoDetrazione NUMBER(15,2);

BEGIN

   aImportoDetrazione:=0;

   BEGIN

      SELECT im_detrazione_personale_anag INTO aImportoDetrazione
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aImportoDetrazione:=0;

   END;

   RETURN aImportoDetrazione;

END getAnagImDetrazionePer;
-- =================================================================================================
-- Recupero delle deduzioni della Family impostate da utente a valere in sede di conguaglio
-- =================================================================================================
FUNCTION getAnagImDeduzioniFam
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN NUMBER IS
   aImportoDeduzione NUMBER(15,2);

BEGIN

   aImportoDeduzione:=0;

   BEGIN

      SELECT im_deduzione_family_area INTO aImportoDeduzione
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aImportoDeduzione:=0;

   END;

   RETURN aImportoDeduzione;

END getAnagImDeduzioniFam;

-- =================================================================================================
-- Recupero del flag no_detrazioni_altre per gestione finanziaria 2007
-- =================================================================================================
FUNCTION getAnagFlNoDetrazioniAltre
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaDetrazioni CHAR(1);
   aFlNoDetrazioniAltre CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo detrazioni
   -- Y = Non attiva calcolo detrazioni

   aFlNoDetrazioniAltre:='Y';

   -- Controllo generale nei parametri se attivare o meno il calcolo delle detrazioni
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaDetrazioni := rec_parametri_cnr.fl_detrazioni_altre;

   IF attivaDetrazioni = 'N' THEN
      RETURN aFlNoDetrazioniAltre;
   END IF;

   BEGIN
      SELECT NVL(fl_no_detrazioni_altre,'N') INTO aFlNoDetrazioniAltre
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlNoDetrazioniAltre:='N';
   END;

   RETURN aFlNoDetrazioniAltre;

END getAnagFlNoDetrazioniAltre;

-- =================================================================================================
-- Recupero del flag no_detrazioni_family per gestione finanziaria 2007
-- =================================================================================================
FUNCTION getAnagFlNoDetrazioniFamily
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaDetrazioni CHAR(1);
   aFlNoDetrazioniFamily CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo detrazioni
   -- Y = Non attiva calcolo detrazioni

   aFlNoDetrazioniFamily:='Y';

   -- Controllo generale nei parametri se attivare o meno il calcolo delle detrazioni
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaDetrazioni := rec_parametri_cnr.fl_detrazioni_family;

   IF attivaDetrazioni = 'N' THEN
      RETURN aFlNoDetrazioniFamily;
   END IF;

   BEGIN
      SELECT NVL(fl_no_detrazioni_family,'N') INTO aFlNoDetrazioniFamily
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlNoDetrazioniFamily:='N';
   END;

   RETURN aFlNoDetrazioniFamily;

END getAnagFlNoDetrazioniFamily;

-- =================================================================================================
-- Recupero del flag detrazioni_altri_tipi per gestione finanziaria 2007
-- =================================================================================================
FUNCTION getAnagFlDetrazioniAltriTipi
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaDetrazioni CHAR(1);
   aFlDetrazioniAltriTipi CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- Y = Attiva calcolo detrazioni
   -- N = Non attiva calcolo detrazioni

   aFlDetrazioniAltriTipi:='N';

   -- Controllo generale nei parametri se attivare o meno il calcolo delle detrazioni
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaDetrazioni := rec_parametri_cnr.fl_detrazioni_altre;

   IF attivaDetrazioni = 'N' THEN
      RETURN aFlDetrazioniAltriTipi;
   END IF;

   BEGIN
      SELECT NVL(fl_detrazioni_altri_tipi,'N') INTO aFlDetrazioniAltriTipi
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlDetrazioniAltriTipi:='N';
   END;

   RETURN aFlDetrazioniAltriTipi;

END getAnagFlDetrazioniAltriTipi;
-- =================================================================================================
-- Recupero del flag no_credito_irpef per gestione spending 2014
-- =================================================================================================
FUNCTION getAnagFlNoCreditoIrpef
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaCredito CHAR(1);
   aFlNoCreditoIrpef CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo credito
   -- Y = Non attiva calcolo credito

   aFlNoCreditoIrpef:='Y';

   -- Controllo generale nei parametri se attivare o meno il calcolo del credito irpef
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaCredito := rec_parametri_cnr.fl_credito_irpef;

   IF attivaCredito = 'N' THEN
      RETURN aFlNoCreditoIrpef;
   END IF;

   BEGIN
      SELECT NVL(fl_no_credito_irpef,'N') INTO aFlNoCreditoIrpef
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlNoCreditoIrpef:='N';
   END;

   RETURN aFlNoCreditoIrpef;

END getAnagFlNoCreditoIrpef;


FUNCTION getAnagFlNoCreditoCuneoIrpef
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaCredito CHAR(1);
   aFlNoCreditoCuneoIrpef CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo credito
   -- Y = Non attiva calcolo credito

   aFlNoCreditoCuneoIrpef:='Y';

   -- Controllo generale nei parametri se attivare o meno il calcolo del credito irpef
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaCredito := rec_parametri_cnr.fl_credito_irpef;

   IF attivaCredito = 'N' THEN
      RETURN aFlNoCreditoCuneoIrpef;
   END IF;

   BEGIN
      SELECT NVL(fl_no_credito_cuneo_irpef,'N') INTO aFlNoCreditoCuneoIrpef
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlNoCreditoCuneoIrpef:='N';
   END;

   RETURN aFlNoCreditoCuneoIrpef;

END getAnagFlNoCreditoCuneoIrpef;

FUNCTION getAnagFlNoDetrCuneoIrpef
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN VARCHAR2 IS
   attivaCredito CHAR(1);
   aFlNoDetrCuneoIrpef CHAR(1);
   rec_parametri_cnr PARAMETRI_CNR%Rowtype;

BEGIN

   -- Dominio
   -- N = Attiva calcolo credito
   -- Y = Non attiva calcolo credito

   aFlNoDetrCuneoIrpef:='Y';

   -- Controllo generale nei parametri se attivare o meno il calcolo del credito irpef
   rec_parametri_cnr := cnrutl001.getRecParametriCnr(aEsercizio);
   attivaCredito := rec_parametri_cnr.fl_credito_irpef;

   IF attivaCredito = 'N' THEN
      RETURN aFlNoDetrCuneoIrpef;
   END IF;

   BEGIN
      SELECT NVL(fl_no_Detr_Cuneo_Irpef,'N') INTO aFlNoDetrCuneoIrpef
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;
   EXCEPTION
      WHEN no_data_found THEN
           aFlNoDetrCuneoIrpef:='N';
   END;

   RETURN aFlNoDetrCuneoIrpef;

END getAnagFlNoDetrCuneoIrpef;

-- =================================================================================================
-- Recupero del Reddito complessivo impostato da utente solo per il calcolo delle detrazioni
-- =================================================================================================
FUNCTION getAnagRedditoComplessivo
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN NUMBER IS
   aRedditoComplessivo NUMBER(15,2);

BEGIN

   aRedditoComplessivo:=0;

   BEGIN

      SELECT Nvl(im_reddito_complessivo,0) INTO aRedditoComplessivo
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aRedditoComplessivo:=0;

   END;

   RETURN aRedditoComplessivo;

END getAnagRedditoComplessivo;

-- =================================================================================================
-- Recupero del Reddito abitazione principale impostato da utente solo per il calcolo delle detrazioni
-- =================================================================================================
FUNCTION getAnagRedditoAbitazPrincipale
   (
    aEsercizio NUMBER,
    aCdAnag ANAGRAFICO.cd_anag%TYPE
   ) RETURN NUMBER IS
   aRedditoAbitazPrincipale NUMBER(15,2);

BEGIN

   aRedditoAbitazPrincipale:=0;

   BEGIN

      SELECT Nvl(im_reddito_abitaz_princ,0) INTO aRedditoAbitazPrincipale
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aRedditoAbitazPrincipale:=0;

   END;

   RETURN aRedditoAbitazPrincipale;

END getAnagRedditoAbitazPrincipale;

-- =================================================================================================
-- Recupero del flag per l'applicazione o meno della detrazione personale massima
-- =================================================================================================

FUNCTION getAnagFlApplicaDetrPersMax
      (
       aEsercizio NUMBER,
       aCdAnag ANAGRAFICO.cd_anag%TYPE
      ) RETURN VARCHAR2 Is
      aFlApplicaDetrPersMax CHAR(1);

BEGIN

   aFlApplicaDetrPersMax:='N';

   BEGIN

      SELECT fl_applica_detr_pers_max Into aFlApplicaDetrPersMax
      FROM ANAGRAFICO_ESERCIZIO
      WHERE  esercizio = aEsercizio AND
             cd_anag = aCdAnag;

   EXCEPTION

      WHEN no_data_found THEN
           aFlApplicaDetrPersMax:='N';

   END;

   RETURN aFlApplicaDetrPersMax;

END getAnagFlApplicaDetrPersMax;

-- Ritorna il reddito di quanto eventualmente pagato come dipendente (per il calcolo del reddito complessivo)

FUNCTION getImponibilePagatoDip
   (
    aEsercizio NUMBER,
    aAnag TERZO.cd_anag%TYPE
   ) RETURN NUMBER IS
   aImponibilePagatoDip NUMBER(15,2);

BEGIN
   aImponibilePagatoDip:=0;

      SELECT nvl(sum(tot_imponibile),0)
      into aImponibilePagatoDip
       FROM COSTI_GEST_DWH
       WHERE ANNO = aEsercizio
         AND DIP_ID IN (SELECT MATRICOLA_DIPENDENTE
                        FROM RAPPORTO
                        WHERE CD_TIPO_RAPPORTO = 'DIP'
                          AND CD_ANAG = aAnag);

   RETURN aImponibilePagatoDip;

END getImponibilePagatoDip;

End;
