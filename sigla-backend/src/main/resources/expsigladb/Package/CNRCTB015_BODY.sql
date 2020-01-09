  CREATE OR REPLACE PACKAGE BODY "CNRCTB015" is

 function getIm01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number is
  aIm01 number;
 begin
  begin
   select im01 into aIm01 from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aIm01;
 end;

 function getIm01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number is
 begin
  return getIm01PerChiave(0, aChiavePrimaria, aChiaveSecondaria);
 end;

 function getIm02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number is
  aIm02 number;
 begin
  begin
   select im02 into aIm02 from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aIm02;
 end;

 function getIm02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number is
 begin
  return getIm02PerChiave(0, aChiavePrimaria, aChiaveSecondaria);
 end;

 function getVal01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
  aVal varchar2(1000);
 begin
  begin
   select val01 into aVal from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aVal;
 end;

 function getVal01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
 begin
  return getVal01PerChiave(0, aChiavePrimaria, aChiaveSecondaria);
 end;

 function getVal02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
  aVal varchar2(1000);
 begin
  begin
   select val02 into aVal from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aVal;
 end;

 function getVal03PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
 begin
  return getVal03PerChiave(0, aChiavePrimaria, aChiaveSecondaria);
 end;

 function getVal03PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
  aVal varchar2(1000);
 begin
  begin
   select val03 into aVal from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aVal;
 end;

 function getVal02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2 is
 begin
  return getVal02PerChiave(0, aChiavePrimaria, aChiaveSecondaria);
 end;

 function getDt01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date is
 aDt date;
 begin
  begin
   select dt01 into aDt from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aDt;
 end;

 function getDt01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date is
 begin
 	  return getDt01PerChiave(0,aChiavePrimaria, aChiaveSecondaria);
 end;

 function getDt02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date is
 aDt date;
 begin
  begin
   select dt02 into aDt from configurazione_cnr where
        esercizio = aEsercizio
    and cd_unita_funzionale = '*'
    and cd_chiave_primaria = aChiavePrimaria
    and cd_chiave_secondaria = aChiaveSecondaria;
  exception when NO_DATA_FOUND then
   IBMERR001.RAISE_ERR_GENERICO('Chiave non trovata in tabella di configurazione cnr: '||aChiavePrimaria||'-'||aChiaveSecondaria);
  end;
  return aDt;
 end;

 function getDt02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date is
 begin
 	  return getDt02PerChiave(0,aChiavePrimaria, aChiaveSecondaria);
 end;

 procedure coie_LUNGHEZZA_CHIAVI (aEsDest esercizio%rowtype) is
  begin
   insert into LUNGHEZZA_CHIAVI (
     ESERCIZIO
    ,LIVELLO
    ,TABELLA
    ,ATTRIBUTO
    ,UTCR
    ,DACR
    ,UTUV
    ,DUVA
    ,PG_VER_REC
    ,LUNGHEZZA
   ) select
     aEsDest.esercizio
    ,LIVELLO
    ,TABELLA
    ,ATTRIBUTO
    ,aEsDest.utcr
    ,aEsDest.dacr
    ,aEsDest.utuv
    ,aEsDest.duva
    ,PG_VER_REC
    ,LUNGHEZZA
  from LUNGHEZZA_CHIAVI
    where
	            esercizio = 0
	        and tabella not in (
             'CDR',
             'UNITA_ORGANIZZATIVA',
             'LINEA_ATTIVITA'
            );
 end;

 function testEsercizioGiaPresente(aEsDest esercizio%rowtype) return boolean is
  aTemp number(4);
 begin
  begin
   select distinct esercizio into aTemp from lunghezza_chiavi where
    esercizio = aEsDest.esercizio;
  exception when no_data_found then
   return false;
  end;
  return true;
 end;

-- Verifica se per il CDS occorre utilizzare la linea dedicata per Partita di Giro dai Parametri CDS
-- (Entrata / Spesa a seconda del tipo gestione)

Function UtilizzaGAEdedicataPgiroCDS (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Boolean Is
 flag CHAR(1);
Begin
   If ati_gestione Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Tipo Gestione non valorizzato nella ricerca del parametro "Usa GAE dedicata per Partite di Giro del CDS".');
   Elsif ati_gestione = CNRCTB001.GESTIONE_ENTRATE Then
     Select FL_LINEA_PGIRO_E_CDS
     Into   flag
     From   parametri_cds par
     Where  par.CD_CDS    = aCDS And
            par.ESERCIZIO = aEsercizio;
   Elsif ati_gestione = CNRCTB001.GESTIONE_spese Then
     Select FL_LINEA_PGIRO_S_CDS
     Into   flag
     From   parametri_cds par
     Where  par.CD_CDS    = aCDS And
            par.ESERCIZIO = aEsercizio;
   Else
     IBMERR001.RAISE_ERR_GENERICO('Tipo Gestione non valido nella ricerca del parametro "Usa GAE dedicata per Partite di Giro del CDS".');
   End If;

     If flag = 'Y' Then
     Return True;
   Else
     Return False;
   End If;
End;


-- Estrae la linea dedicata per Partita di Giro dai Parametri CDS (Entrata / Spesa a seconda del tipo gestione)
Function get_LINEA_PGIRO_cds (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Linea_attivita%Rowtype Is
  aLa Linea_attivita%Rowtype;
 Begin
   If ati_gestione Is Null Then
     IBMERR001.RAISE_ERR_GENERICO('Tipo Gestione non valorizzato nella ricerca della GAE per Partite di Giro del CDS.');
   Elsif ati_gestione = CNRCTB001.GESTIONE_ENTRATE Then
     Select la.*
     Into   aLa
     From   Linea_attivita la, parametri_cds par
     Where  par.CD_CDS    = aCDS And
            par.ESERCIZIO = aEsercizio And
            la.CD_CENTRO_RESPONSABILITA = par.CD_CDR_LINEA_PGIRO_E And
            la.CD_LINEA_ATTIVITA        = par.CD_LINEA_PGIRO_E;

   Elsif ati_gestione = CNRCTB001.GESTIONE_spese Then

     Select la.*
     Into   aLa
     From   Linea_attivita la, parametri_cds par
     Where  par.CD_CDS    = aCDS And
            par.ESERCIZIO = aEsercizio And
            la.CD_CENTRO_RESPONSABILITA = par.CD_CDR_LINEA_PGIRO_S And
            la.CD_LINEA_ATTIVITA        = par.CD_LINEA_PGIRO_S;

   Else
     IBMERR001.RAISE_ERR_GENERICO('Tipo Gestione non valido nella ricerca della GAE per Partite di Giro del CDS.');
   End If;

   Return aLa;

 End;

end;
