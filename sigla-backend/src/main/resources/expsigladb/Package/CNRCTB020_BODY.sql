--------------------------------------------------------
--  DDL for Package Body CNRCTB020
--------------------------------------------------------

CREATE OR REPLACE PACKAGE BODY "CNRCTB020" is


 function getCDCDSENTE(aEs number) return unita_organizzativa.CD_unita_organizzativa%Type Is
  aUO unita_organizzativa.CD_unita_organizzativa%Type;
 begin
   select CD_unita_organizzativa
   into aUO from unita_organizzativa where
	    fl_cds = 'Y'
    and cd_tipo_unita = TIPO_ENTE
	and esercizio_inizio <= aEs
	and esercizio_fine >= aEs;
  return aUO;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('CDS ente valido non trovato in esercizio:'||aEs);
 end;

function getCdCdrEnte return cdr.cd_centro_responsabilita%Type Is
  aCDREnte cdr%rowtype;
begin
   aCDREnte:=getCdrEnte;
   return aCDREnte.cd_centro_responsabilita;
end;

 Function getUOENTE(aEs number) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
   select * into aUO from unita_organizzativa where
	    fl_cds = 'N'
	and to_number(cd_proprio_unita)=0
    and cd_tipo_unita = TIPO_ENTE
	and esercizio_inizio <= aEs
	and esercizio_fine >= aEs;
  return aUO;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('UO ente valida non trovata in esercizio:'||aEs);
 end;

 function getCDSValido(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype is
  aCDS unita_organizzativa%rowtype;
 begin
  begin
   select * into aCDS from unita_organizzativa a where
	    a.fl_cds = 'Y'
    and a.cd_unita_organizzativa = aCdCds
    and exists (select 1 from v_unita_organizzativa_valida where
	     esercizio = aEs
	 and cd_unita_organizzativa = a.cd_unita_organizzativa);
   return aCDS;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDS non valido o non definito in esercizio '||aEs);
  end;
 end;

 function getCDSSACValido(aEs number) return unita_organizzativa%rowtype is
  aCDS unita_organizzativa%rowtype;
 begin
  if (gEsercizioCDSSACValido Is Null Or gEsercizioCDSSACValido != aEs) Then
    gEsercizioCDSSACValido := aEs;
    begin
         select * into aCDS from unita_organizzativa where
              aEs >= esercizio_inizio
          and (
              esercizio_fine is null
         	 or aEs <= esercizio_fine
          )
      	and cd_tipo_unita = TIPO_SAC
      	and fl_cds = 'Y';
     gCDSSACValido := aCDS;
     return gCDSSACValido;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('CDS SAC non valido o non definito in esercizio '||aEs);
    end;
  Else
    Return gCDSSACValido;
  End If;
 end;

 Function getcdCDSSACValido(aEs number) Return VARCHAR2 is
  aCDS unita_organizzativa%rowtype;
 begin
  if (gEsercizioCDCDSSACValido Is Null Or gEsercizioCDCDSSACValido != aEs) Then
    gEsercizioCDCDSSACValido := aEs;
    begin
     select * into aCDS from unita_organizzativa where
          aEs >= esercizio_inizio
      and (
          esercizio_fine is null
     	 or aEs <= esercizio_fine
      )
	  and cd_tipo_unita = TIPO_SAC
	  and fl_cds = 'Y';
     gCDCDSSACValido := aCDS.cd_unita_organizzativa;
     return gCDCDSSACValido;
    exception when NO_DATA_FOUND then
     IBMERR001.RAISE_ERR_GENERICO('CDS SAC non valido o non definito in esercizio '||aEs);
    end;
  Else
    Return gCDCDSSACValido;
  End If;
 end;

 function getUOValida(aEs number,aCdUO varchar2) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  begin
   select * into aUO from unita_organizzativa where
        aEs >= esercizio_inizio
    and (
        esercizio_fine is null
   	 or aEs <= esercizio_fine
    )
	and cd_unita_organizzativa = aCdUO;
   return aUO;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Unita organizzativa '||aCdUO||'non valida o non definita in esercizio '||aEs);
  end;
 end;


 function getCDRValido(aEs number,aCdCDR varchar2) return cdr%rowtype is
  aCDR cdr%rowtype;
 begin
  begin
   select * into aCDR from cdr where
        aEs >= esercizio_inizio
    and (
        esercizio_fine is null
   	 or aEs <= esercizio_fine
    )
	and cd_centro_responsabilita = aCdCDR;
   return aCDR;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR '||aCdCDR||'non valido o non definito in esercizio '||aEs);
  end;
 end;



 function getCDRPrimoLivello(aCDR cdr %rowtype) return cdr%rowtype is
  aCDRPrimo cdr%rowtype;
 begin
  if(aCDR.livello = 1) then return aCDR; end if;
  begin
   select * into aCDRPrimo from cdr where
        cd_centro_responsabilita = aCDR.cd_cdr_afferenza;
   return aCDRPrimo;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Cdr di primo livello del CDR '||aCDR.cd_centro_responsabilita||' non trovato!');
  end;
 end;

--P.R. Da Verificare dopo l'inserimento dell'area di afferenza sul PDG
 function getCDRArea(aCDR cdr %rowtype) return cdr%rowtype is
  aCDRArea cdr%rowtype;
 begin
  begin
   select d.* into aCDRArea from
    unita_organizzativa a, -- unita organizzativa di aCDR
	unita_organizzativa b, -- cds AREA
	unita_organizzativa c, -- uo cds AREA
	cdr d -- cdr responsabile del CDS AREA
   where
        a.cd_unita_organizzativa = aCDR.cd_unita_organizzativa
    and b.cd_unita_organizzativa = a.cd_area_ricerca
    and c.cd_unita_padre = b.cd_unita_organizzativa
	and c.fl_uo_cds = 'Y'
    and d.cd_unita_organizzativa = c.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aCDRArea;
 end;

 function getCDREnte return cdr%rowtype is
  aCDREnte cdr%rowtype;
 begin
  begin
   select b.* into aCDREnte from
    unita_organizzativa a, -- unita organizzativa ENTE
	cdr b                  -- cdr responsabile ENTE
   where
        a.cd_unita_organizzativa = b.cd_unita_organizzativa
	and a.cd_tipo_unita = CNRCTB020.TIPO_ENTE;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR Ente non trovato!');
  end;
  return aCDREnte;
 end;

 function getUOPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
 begin
  begin
   select b.* into aUOPresArea From
    ass_uo_area a,
    unita_organizzativa b
   Where a.esercizio = aEs
   And   a.cd_area_ricerca = aCDSArea.cd_unita_organizzativa
   And   a.fl_presidente_area = 'Y'
   And   a.cd_unita_organizzativa = b.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aUOPresArea;
 end;


 function getCDSPresidenteArea(aEs NUMBER, aCDSArea unita_organizzativa %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
  aCDSPresArea unita_organizzativa%rowtype;
 begin
  aUOPresArea:=getUOPresidenteArea(aEs, aCDSArea);
  if aUOPresArea.cd_unita_organizzativa is not null then
   begin
    select * into aCDSPresArea from
     unita_organizzativa
    where
         cd_unita_organizzativa = aUOPresArea.cd_unita_padre
 	 and fl_cds = 'Y';
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  return aCDSPresArea;
 end;

 function getUOPresidenteArea(aEs NUMBER, aCDRArea cdr %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
 begin
  begin
   select c.* into aUOPresArea from
	unita_organizzativa a, -- uo dell'area
	ass_uo_area b, -- aree associate alla uo
    unita_organizzativa c  -- uo presidente dell'area
   Where a.cd_unita_organizzativa = aCDRArea.cd_unita_organizzativa
   And   b.esercizio = aEs
   And   b.cd_area_ricerca = a.cd_unita_padre
   And   b.fl_presidente_area = 'Y'
   And   b.cd_unita_organizzativa = c.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   null;
  end;
  return aUOPresArea;
 end;

 function getCDSPresidenteArea(aEs NUMBER, aCDRArea cdr %rowtype) return unita_organizzativa%rowtype is
  aUOPresArea unita_organizzativa%rowtype;
  aCDSPresArea unita_organizzativa%rowtype;
 begin
  aUOPresArea:=getUOPresidenteArea(aEs, aCDRArea);
  if aUOPresArea.cd_unita_organizzativa is not null then
   begin
    select * into aCDSPresArea from
     unita_organizzativa
    where
         cd_unita_organizzativa = aUOPresArea.cd_unita_padre
 	 and fl_cds = 'Y';
   exception when NO_DATA_FOUND then
    null;
   end;
  end if;
  return aCDSPresArea;
 end;

 function getUOCDS(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
  aUO unita_organizzativa%rowtype;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;

  begin
   -- estraggo l'UO CDS dall'UO
   select * into aUOCDS from unita_organizzativa where
       cd_unita_padre = aUO.cd_unita_padre
   and fl_uo_cds = 'Y';
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO CDS dell''UO '||aUO.cd_unita_organizzativa||' non trovata!');
  end;
  return aUOCDS;
 end;

 function getUO(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;
  return aUO;
 end;

 Function getCDUO(aCDR cdr.cd_unita_organizzativa%Type) Return VARCHAR2 is
  aUO unita_organizzativa.cd_unita_organizzativa%Type;
 begin
  -- estraggo l'UO di appartenenza del CDR
  begin
   Select cd_unita_organizzativa Into aUO from cdr where
       cd_centro_responsabilita = aCDR;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di appartenenza del CDR '||aCDR||' non trovata!');
  end;
  return aUO;
 end;

-- Dato il codice unita_organizzativa, ritorna la descrizione
 Function getdesUO(aCDUO unita_organizzativa.cd_unita_organizzativa%Type) return VARCHAR2 Is
 des    VARCHAR2(4000);
 Begin
   Select DS_UNITA_ORGANIZZATIVA
   Into   des
   From   UNITA_ORGANIZZATIVA
   Where  CD_UNITA_ORGANIZZATIVA = aCDUO;
   Return des;
 Exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('La UO '||aCDUO||' non esiste!');
 End;

 function getUOCDS(aEs number, aCdCds varchar2) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO CDS dall'UO
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_padre = aCdCds
   and a.fl_uo_cds = 'Y'
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO CDS del CDS '||aCdCds||' non trovata!');
  end;
  return aUOCDS;
 end;

 function getUOVersCori(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori (tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = getCdUOVersCori(aEs)
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento CORI accentrato non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersCoriTuttaSAC(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori unificato per tutta la SAC(tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = getCdUOVersCoriTuttaSAC(aEs)
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
		 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento CORI unificato non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersCoriContoBI(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamento cori su Conto Banca d'Ialia
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = getCdUOVersCoriContoBI(aEs)
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
		 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO responsabile del versamento CORI su Conto BI non trovata');
  end;
  return aUOCDS;
 end;

 function getUOVersIVA(aEs number) return unita_organizzativa%rowtype is
  aUOCDS unita_organizzativa%rowtype;
 begin
  begin
   -- estraggo l'UO di servizio versamneto IVA (deve essere di tipo SAC)
   select * into aUOCDS from unita_organizzativa a where
       a.cd_unita_organizzativa = getCdUOVersIVA(aEs)
   and cd_tipo_unita = TIPO_SAC
   and exists (select 1 from v_unita_organizzativa_valida where
                     esercizio = aEs
				 and cd_unita_organizzativa = a.cd_unita_organizzativa);
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO SAC responsabile del versamento IVA non trovata');
  end;
  return aUOCDS;
 end;

 function isCDRENTE(aCDR cdr%rowtype) return boolean is
  aUO unita_organizzativa%rowtype;
  aLC lunghezza_chiavi%rowtype;
 begin
  begin
   select * into aLC from lunghezza_chiavi where
        tabella = 'UNITA_ORGANIZZATIVA'
    and attributo = 'CD_UNITA_ORGANIZZATIVA'
    and esercizio = 0
    and livello = 1;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Configurazione della lunghezza chiavi Struttura Organizzativa non trovata!');
  end;
  if substr(aCDR.cd_centro_responsabilita,1,instr(aCDR.cd_centro_responsabilita,'.') - 1) = lpad('9',aLC.lunghezza,'9') then
   return true;
  end if;
  return false;
 end;

 function getUOPersonale(aEs number) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
  aCDR cdr%rowtype;
  aCdUo varchar2(30);
 begin
  aCDR:=getCDRPersonale(aEs);
  aCdUO:=aCDR.cd_unita_organizzativa;
  if aCdUo is null then
   return null;
  end if;

  select * into aUO from unita_organizzativa where
   cd_unita_organizzativa = aCdUo;
  return aUO;
 end;

 function getCDRPersonale(aEs number) return cdr%rowtype is
  aCdCDR cdr.cd_centro_responsabilita%Type;
  aCDR cdr%rowtype;
 begin
   aCdCDR := getCdCDRPersonale(aEs);
   if aCdCDR is null then
     return null;
   end if;
   select * into aCDR
   from cdr
   where cd_centro_responsabilita = aCdCDR;

   return aCDR;
 End;

 function getCDRResponsabileUO(aUO unita_organizzativa%rowtype) return cdr%rowtype is
  aCdr cdr%rowtype;
 begin
  begin
   select b.* into aCdr from cdr b, unita_organizzativa a where
       a.cd_unita_organizzativa = aUO.cd_unita_organizzativa
   and b.cd_unita_organizzativa = a.cd_unita_organizzativa
   and to_number(b.cd_proprio_cdr) = 0;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR responsabilile dell''UO'||aUO.cd_unita_organizzativa||' non trovato!');
  end;
  return aCDR;
 end;

 function getCDRResponsabileUO(aCdUO VARCHAR2) return cdr%rowtype is
  aCdr cdr%rowtype;
 begin
  begin
   select b.* into aCdr from cdr b, unita_organizzativa a where
       a.cd_unita_organizzativa = aCdUO
   and b.cd_unita_organizzativa = a.cd_unita_organizzativa
   and to_number(b.cd_proprio_cdr) = 0;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('CDR responsabilile dell''UO ' || aCdUO ||' non trovato!');
  end;
  return aCDR;
 end;

 function getUOAfferenza(aCDR cdr%rowtype) return unita_organizzativa%rowtype is
  aUO unita_organizzativa%rowtype;
 begin
  begin
   select * into aUO from unita_organizzativa where
       cd_unita_organizzativa = aCDR.cd_unita_organizzativa;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('UO di afferenza del CDR '||aCDR.cd_centro_responsabilita||' non trovata!');
  end;
  return aUO;
 end;

 Procedure ins_UNITA_ORGANIZZATIVA (aDest UNITA_ORGANIZZATIVA%rowtype) is
  begin
   insert into UNITA_ORGANIZZATIVA (
     FL_RUBRICA
    ,LIVELLO
    ,PRC_COPERTURA_OBBLIG_2
    ,PRC_COPERTURA_OBBLIG_3
    ,CD_RESPONSABILE
    ,CD_RESPONSABILE_AMM
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,PG_VER_REC
    ,FL_PRESIDENTE_AREA
    ,CD_AREA_RICERCA
    ,CD_UNITA_PADRE
    ,ESERCIZIO_INIZIO
    ,ESERCIZIO_FINE
    ,CD_UNITA_ORGANIZZATIVA
    ,DS_UNITA_ORGANIZZATIVA
    ,CD_PROPRIO_UNITA
    ,CD_TIPO_UNITA
    ,FL_CDS
    ,FL_UO_CDS
	,CD_AREA_SCIENTIFICA
   ) values (
     aDest.FL_RUBRICA
    ,aDest.LIVELLO
    ,aDest.PRC_COPERTURA_OBBLIG_2
    ,aDest.PRC_COPERTURA_OBBLIG_3
    ,aDest.CD_RESPONSABILE
    ,aDest.CD_RESPONSABILE_AMM
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.PG_VER_REC
    ,aDest.FL_PRESIDENTE_AREA
    ,aDest.CD_AREA_RICERCA
    ,aDest.CD_UNITA_PADRE
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.ESERCIZIO_FINE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.DS_UNITA_ORGANIZZATIVA
    ,aDest.CD_PROPRIO_UNITA
    ,aDest.CD_TIPO_UNITA
    ,aDest.FL_CDS
    ,aDest.FL_UO_CDS
	,aDest.CD_AREA_SCIENTIFICA
    );
 end;

Function getdeslivello(aEs NUMBER, ati_gestione VARCHAR, aliv1 VARCHAR2, aliv2 VARCHAR2, aliv3 VARCHAR2,
                                                 aliv4 VARCHAR2, aliv5 VARCHAR2, aliv6 VARCHAR2,
                                                 aliv7 VARCHAR2) return VARCHAR2 Is

RET      CLASSIFICAZIONE_VOCI.DS_CLASSIFICAZIONE%Type;

Begin

If aliv7 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 = aliv6 And
        CD_LIVELLO7 = aliv7;

Elsif aliv6 IS NOT NULL Then

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 = aliv6 And
        CD_LIVELLO7 Is Null;

Elsif aliv5 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 = aliv5 And
        CD_LIVELLO6 Is Null;

Elsif aliv4 IS NOT NULL Then

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 = aliv4 And
        CD_LIVELLO5 Is Null;

Elsif aliv3 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 = aliv3 And
        CD_LIVELLO4 Is Null;

Elsif aliv2 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 = aliv2 And
        CD_LIVELLO3 Is Null;

Elsif aliv1 IS NOT NULL THEN

Select  DS_CLASSIFICAZIONE
Into    RET
From    CLASSIFICAZIONE_VOCI
Where   ESERCIZIO = aEs And
        TI_GESTIONE = ati_gestione And
        CD_LIVELLO1 = aliv1 And
        CD_LIVELLO2 Is Null;

End If;

Return RET;

Exception
   When Others Then Return (' ');
End;

Function GETDESDIPARTIMENTO (aCd_dip VARCHAR2) return VARCHAR2 Is
ret     dipartimento.cd_dipartimento%Type;
Begin
 Select DS_DIPARTIMENTO
 Into   ret
 From   DIPARTIMENTO
 Where CD_DIPARTIMENTO = aCd_dip;

 Return ret;
Exception
 When Others Then Return (' ');
End;


Procedure ins_CDR (aDest CDR%rowtype) is
  begin
   insert into CDR (
     CD_CENTRO_RESPONSABILITA
    ,ESERCIZIO_INIZIO
    ,ESERCIZIO_FINE
    ,CD_UNITA_ORGANIZZATIVA
    ,LIVELLO
    ,CD_PROPRIO_CDR
    ,DS_CDR
    ,CD_RESPONSABILE
    ,INDIRIZZO
    ,DACR
    ,UTCR
    ,DUVA
    ,UTUV
    ,CD_CDR_AFFERENZA
    ,PG_VER_REC
   ) values (
     aDest.CD_CENTRO_RESPONSABILITA
    ,aDest.ESERCIZIO_INIZIO
    ,aDest.ESERCIZIO_FINE
    ,aDest.CD_UNITA_ORGANIZZATIVA
    ,aDest.LIVELLO
    ,aDest.CD_PROPRIO_CDR
    ,aDest.DS_CDR
    ,aDest.CD_RESPONSABILE
    ,aDest.INDIRIZZO
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.CD_CDR_AFFERENZA
    ,aDest.PG_VER_REC
    );
 end;
 function isUOSAC(aUO varchar2) return boolean is
  conta number;
 begin
   select count(1) into conta
   from unita_organizzativa
   where cd_unita_organizzativa = aUO
    and fl_cds = 'N'
    and cd_tipo_unita = TIPO_SAC;

    if conta > 0 then
	  	 return true;
	  else
	  	 return false;
	  end if;
 end;
 function getCdCDRPersonale(aEs number) return cdr.cd_centro_responsabilita%Type Is
  aCdCDR varchar2(30);
  noData EXCEPTION;
  pragma exception_init(noData,-20020);
 begin
  Begin
    aCdCDR:=CNRCTB015.GETVAL01PERCHIAVE(aEs,'CDR_SPECIALE','CDR_PERSONALE');
  Exception
    When noData Then
      if aEs=0 Then
        IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: CDR_SPECIALE-CDR_PERSONALE');
     end if;
  End;
  if aCdCDR is null AND aEs!=0 then
     aCdCDR:=CNRCTB015.GETVAL01PERCHIAVE(0,'CDR_SPECIALE','CDR_PERSONALE');
  end if;
  return aCdCDR;
 end;
 function getCdUOVersCori(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type Is
   aCdUO varchar2(30);
   noData EXCEPTION;
   pragma exception_init(noData,-20020);
  begin
   Begin
     aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(aEs,'UO_SPECIALE','UO_VERSAMENTO_CORI');
   Exception
     When noData Then
       if aEs=0 Then
         IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: UO_SPECIALE-UO_VERSAMENTO_CORI');
      end if;
   End;
   if aCdUO is null AND aEs!=0 then
      aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(0,'UO_SPECIALE','UO_VERSAMENTO_CORI');
   end if;
   return aCdUO;
 end;
 function getCdUOVersCoriTuttaSAC(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type Is
   aCdUO varchar2(30);
   noData EXCEPTION;
   pragma exception_init(noData,-20020);
  begin
   Begin
     aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(aEs,'UO_SPECIALE','UO_VERSAMENTO_CORI_TUTTA_SAC');
   Exception
     When noData Then
       if aEs=0 Then
         IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: UO_SPECIALE-UO_VERSAMENTO_CORI_TUTTA_SAC');
      end if;
   End;
   if aCdUO is null AND aEs!=0 then
      aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(0,'UO_SPECIALE','UO_VERSAMENTO_CORI_TUTTA_SAC');
   end if;
   return aCdUO;
 end;
 function getCdUOVersCoriContoBI(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type Is
    aCdUO varchar2(30);
    noData EXCEPTION;
    pragma exception_init(noData,-20020);
   begin
    Begin
      aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(aEs,'UO_SPECIALE','UO_VERSAMENTO_CORI_CONTO_BI');
    Exception
      When noData Then
        if aEs=0 Then
          IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: UO_SPECIALE-UO_VERSAMENTO_CORI_CONTO_BI');
       end if;
    End;
    if aCdUO is null AND aEs!=0 then
       aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(0,'UO_SPECIALE','UO_VERSAMENTO_CORI_CONTO_BI');
    end if;
    return aCdUO;
 end;
 function getCdUOVersIVA(aEs number) return unita_organizzativa.cd_unita_organizzativa%Type Is
    aCdUO varchar2(30);
    noData EXCEPTION;
    pragma exception_init(noData,-20020);
   begin
    Begin
      aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(aEs,'UO_SPECIALE','UO_VERSAMENTO_IVA');
    Exception
      When noData Then
        if aEs=0 Then
          IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: UO_SPECIALE-UO_VERSAMENTO_IVA');
       end if;
    End;
    if aCdUO is null AND aEs!=0 then
       aCdUO:=CNRCTB015.GETVAL01PERCHIAVE(0,'UO_SPECIALE','UO_VERSAMENTO_IVA');
    end if;
    return aCdUO;
 end;
end;