CREATE OR REPLACE package CNRCTB015 as
--
-- CNRCTB015 - Package di gestione tabelle di CONFIGURAZIONE GENERALI: CONFIGURAZIONE_CNR LUNGHEZZA_CHIAVI
-- Date: 10/07/2003
-- Version: 1.10
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.1
-- Creazione
--
-- Date: 05/10/2001
-- Version: 1.1
-- Agiunta gestione copia dati interesercizio per tabella LUNGHEZZA_CHIAVI
--
-- Date: 18/10/2001
-- Version: 1.2
-- Aggiunto il metodo per il controllo di esistenza dell'esercizio indiretto in tabella esercizio
-- Nuovi metodi per lettura da tabella CONFIGURAZIONE_CNR
--
-- Date: 08/11/2001
-- Version: 1.3
-- Eliminazione esercizio da STO
--
-- Date: 16/11/2001
-- Version: 1.4
-- Enforcing error handling
--
-- Date: 21/01/2002
-- Version: 1.5
-- Lettura di val02 da CONFIGURAZIONE_CNR
--
-- Date: 25/03/2002
-- Version: 1.6
-- Funzioni di lettura dei dati numerici di CONFIGURAZIONE_CNR
--
-- Date: 18/07/2002
-- Version: 1.7
-- Aggiornamento documentazione
--
-- Date: 01/12/2002
-- Version: 1.8
-- Aggiunto metodo per estrazione val03 da configurazione CNR
--
-- Date: 24/01/2003
-- Version: 1.9
-- Aggiunto il metodo di estrazione im02 da configurazione CNR
--
-- Date: 10/07/2003
-- Version: 1.10
-- Aggiunto il metodo di estrazione dt01 da configurazione CNR
--
-- Constants:
--
-- Functions e Procedures:

-- Estrae il valore val01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (val01)

 Function getVal01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Si assume che eserecizio sia significativo
 function getVal01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Estrae il valore im01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (im01)

 function getIm01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Si assume che eserecizio sia significativo
 function getIm01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Estrae il valore im02 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo stringa del record trovato (im02)

 function getIm02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;

-- Si assume che eserecizio sia significativo
 function getIm02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return number;


-- Ritorna il secondo valore di tipo stringa del record trovato (val02) per esercizio

 function getVal02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;
-- Si assume che eserecizio sia significativo
 function getVal02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Ritorna il terzo valore di tipo stringa del record trovato (val03) per esercizio

 function getVal03PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;
-- Si assume che eserecizio sia significativo
 function getVal03PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return varchar2;

-- Estrae il valore dt01 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il primo valore di tipo data del record trovato (dt01)

 function getDt01PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Si assume che eserecizio sia significativo
 function getDt01PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Estrae il valore dt02 corrispondente alla chiave aChiavePrimaria/aChiaveSecondaria
-- Si assume che eserecizio e cd_unita_funzionale non siano significativi
-- Ritorna il secondo valore di tipo data del record trovato (dt02)

 function getDt02PerChiave(aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Si assume che eserecizio sia significativo
 function getDt02PerChiave(aEsercizio number, aChiavePrimaria varchar2, aChiaveSecondaria varchar2) return date;

-- Copia dati interesercizio per tabella LUNGHEZZA_CHIAVI
-- aEsDest -> row type esercizio di destinazione
--            dal rowtype vengono letti anche utuvu/utcr/duva/dacr

 procedure coie_LUNGHEZZA_CHIAVI (aEsDest esercizio%rowtype);

--
-- Funzione che verifica l'esistenza di un esercizio in tabella esercizio indirettamente, controllando se
-- esistono in lunghezza_chiavi records per quell'esercizio
-- Tale metodo indiretto viene chiamato quando non sia possibile rileggere nel contesto di chiamata al metodo
-- la tabella esercizio (ad esempio nell'ambito di chiamata via trigger)
--

 function testEsercizioGiaPresente(aEsDest esercizio%rowtype) return boolean;

-- Verifica se per il CDS occorre utilizzare la linea dedicata per Partita di Giro dai Parametri CDS
-- (Entrata / Spesa a seconda del tipo gestione)
 Function UtilizzaGAEdedicataPgiroCDS (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Boolean;

-- Estrae la linea dedicata per Partita di Giro dai Parametri CDS (Entrata / Spesa a seconda del tipo gestione)
 Function get_LINEA_PGIRO_cds (aEsercizio NUMBER, aCDS VARCHAR2, ati_gestione CHAR) return Linea_attivita%Rowtype;


end;
/


CREATE OR REPLACE package body CNRCTB015 is

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
/


