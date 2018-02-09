CREATE OR REPLACE package CNRCTB000 as
--
-- CNRCTB000 - Package di gestione tabella ELEMENTO_VOCE E ASSOCIAZIONI
-- Date: 03/11/2005
-- Version: 1.10
--
-- Dependency: CNRCTB 001/015 IBMERR 001
--
-- History:
--
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
--
-- Date: 16/11/2001
-- Version: 1.1
-- Introduzione flag partita-giro in ELEMENTO_VOCE
--
-- Date: 19/11/2001
-- Version: 1.2
-- Introduzione capoconto  finanziario
--
-- Date: 03/12/2001
-- Version: 1.3, 1.4
-- Fix per aggiunta nuovi campi in elemento voce
--
-- Date: 29/12/2001
-- Version: 1.5
-- Aggiunti metodi di lettura delle voci del piano speciali
--
-- Date: 31/12/2001
-- Version: 1.6
-- Agiunto metodo di controllo eliminabilit? ass_ev_ev
--
-- Date: 27/01/2002
-- Version: 1.7
-- Aggiunta lettura ordinata per codice in metodi coie_XXX
--
-- Date: 18/07/2002
-- Version: 1.8
-- Aggiornamento della documentazione
--
-- Date: 20/08/2002
-- Version: 1.9
-- Il controllo sull'eliminazione dell'associazione con tipologia di intervento non va fatto
-- se l'esercizio del SAC non ? definito o ? in stato Iniziale (I)
--
-- Date: 03/11/2005
-- Version: 1.10
-- Aggiunta la ricerca delle voci per contratti a tempo determinato
-- getVoceTFRTEMPODET e getVoceONERICNRTEMPODET
--
-- Constants:

-- Descrizione Tipi Variazioni PDG
DESCR_STO_S_CDS  Constant  VARCHAR(60) := 'Storno di Spesa all''interno dello stesso Istituto';
DESCR_STO_E_CDS  Constant  VARCHAR(60) := 'Storno di Entrata all''interno dello stesso Istituto';
DESCR_STO_S_TOT  Constant  VARCHAR(60) := 'Storno di Spesa tra Istituti diversi';
DESCR_STO_E_TOT  Constant  VARCHAR(60) := 'Storno di Entrata tra Istituti diversi';
DESCR_PREL_FON  Constant  VARCHAR(60) := 'Prelievo dal Fondo';
DESCR_VAR_CDS   Constant  VARCHAR(60) := 'Variazione all''interno dello stesso Istituto';
DESCR_VAR_TOT   Constant  VARCHAR(60) := 'Variazione tra Istituti diversi';

-- Functions e Procedures:

-- Estrae da configurazione CNR l'elemento voce speciale ricavo figurativo altro cdr
-- aEsercizio -> Esercizio contabile

 function getVoceRicFigAltroCDR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale trattamento di fine rapporto
-- aEsercizio -> Esercizio contabile

 function getVoceTFR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale degli oneri cnr
-- aEsercizio -> Esercizio contabile

 function getVoceONERICNR(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale trattamento di fine rapporto
-- per personale a tempo determinato
-- aEsercizio -> Esercizio contabile

 function getVoceTFRTEMPODET(aEsercizio number) return elemento_voce%rowtype;

-- Estrae da configurazione CNR l'elemento voce speciale degli oneri cnr
-- per personale a tempo determinato
-- aEsercizio -> Esercizio contabile

 function getVoceONERICNRTEMPODET(aEsercizio number) return elemento_voce%rowtype;

-- dato esercizio/numero della variazione PDG restituisce il tipo

 Function TIPO_VAR_PDG (aEsercizio NUMBER, aNumVar NUMBER)  Return  VARCHAR2;

-- dato il tipo variazione PDG restituisce la descrizione

 Function DESCR_TIPO_VAR_PDG (Tipo VARCHAR) Return  VARCHAR2;

-- data la riga di mandato restituisce la natura del Mandato stesso
 Function TIPO_MANDATO (aMandatoRiga mandato_riga%Rowtype) Return VARCHAR2;

-- data la chiave della riga di mandato restituisce la natura del Mandato stesso
 Function TIPO_MANDATO (aCD_CDS VARCHAR2, aESERCIZIO NUMBER, aPG_MANDATO NUMBER, aESERCIZIO_OBBLIGAZIONE NUMBER,
                        aPG_OBBLIGAZIONE NUMBER, aPG_OBBLIGAZIONE_SCADENZARIO NUMBER, aCD_CDS_DOC_AMM VARCHAR2,
                        aCD_UO_DOC_AMM VARCHAR2, aESERCIZIO_DOC_AMM NUMBER, aCD_TIPO_DOCUMENTO_AMM VARCHAR2,
                        aPG_DOC_AMM NUMBER, aESERCIZIO_ORI_OBBLIGAZIONE NUMBER)  Return VARCHAR2;

-- Inserisce una riga in elemento_voce

 procedure ins_ELEMENTO_VOCE(aDest elemento_voce%rowtype);

-- Inserisce una riga in ass_ev_funz_tipocds

 procedure ins_ASS_EV_FUNZ_TIPOCDS (aDest ASS_EV_FUNZ_TIPOCDS%rowtype);

-- Copia dati interesercizio
-- aEsDest -> row type esercizio di destinazione
--            dal rowtype vengono letti anche utuvu/utcr/duva/dacr

 procedure coie_ELEMENTO_VOCE (aEsDest esercizio%rowtype);

-- Procedura di verifica eliminabilit? di una associazione da ass_ev_ev
-- Scatenata da trigger AD_ASS_EV_EV
--
-- Pre:
-- aAss contiene un'associazione in fase di eliminazione
-- Si verifica uno dei seguenti casi:
--
-- Caso 1: Associazione Titolo di spesa CNR -> Titolo di spesa CDS
--  Nella tabella VOCE_F_SALDI_CDR_LINEA esiste gi? un titolo = Titolo di spesa CNR presente in aAss
--  per l'esercizio specificato in aAss
--
-- Caso 2: Associazione Capitolo di Entrata CNR -> Natura
--  In pdg_preventivo_etr_det esiste almento un dettaglio che contiene la natura specificata
--  per l'esercizio specificato in aAss
--
-- Caso 3: Associazione Capitolo di spesa CDS -> Tipologia di intervento (Capitolo di spesa CNR per categoria 2)
--  Esiste in VOCE_F_SALDI_CDR_LINEA almeno un record con tipologia di intervento = a quella specificata in aAss
--  per l'esercizio specificato in aAss
--
-- Caso 4: Associazione Titolo spesa cds + Tipo di CDS + Natura -> Capitolo di entrata CDS
--  Tale associazione non ? eliminabile se esiste almeno un record in VOCE_F_SALDI_CDR_LINEA con capitolo di entrata
--  CDS = a quello specificato in aAss per l'esercizio specificato in aAss
--
-- Post:
-- Viene sollevata l'eccezione generica: Associazione non eliminabile perch? utilizzata
--
-- Parametri:
--
-- aAss -> rowtype contenente l'associazione in fase di eliminazione

 procedure checkEliminAssEvEv(aAss ass_ev_ev%rowtype);

end;
/


CREATE OR REPLACE package body CNRCTB000 is

 procedure checkEliminAssEvEv(aAss ass_ev_ev%rowtype) is
  aNum number;
  aEsercizio esercizio%rowtype;
  aCds unita_organizzativa%rowtype;
 begin
  aNum:=0;
  if -- Associzione n.1 Titolo di spesa CNR - > Titolo di spesa CDS
	       aAss.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and aAss.ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and aAss.ti_elemento_voce = CNRCTB001.TITOLO
	   and aAss.ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CDS
	   and aAss.ti_gestione_coll = CNRCTB001.GESTIONE_SPESE
	   and aAss.ti_elemento_voce_coll = CNRCTB001.TITOLO then

     select count(*) into aNum
     from   voce_f_saldi_cdr_linea
     Where  esercizio       = aAss.esercizio And
            ti_appartenenza = aAss.ti_appartenenza And
            ti_gestione     = aAss.ti_gestione And
            cd_voce         like aAss.cd_elemento_voce||'.%';

   if aNum > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Associazione non eliminabile perch? utilizzata!');
   end if;
  end if;

  aNum:=0;
  if -- Associazione n.2 Capitolo di Entrata CNR -> Natura
	       aAss.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and aAss.ti_gestione = CNRCTB001.GESTIONE_ENTRATE
	   and aAss.ti_elemento_voce = CNRCTB001.CAPITOLO
	   and aAss.cd_natura != '*'
  then
   select count(*) into aNum from pdg_preventivo_etr_det a, linea_attivita b where
               a.esercizio=aAss.esercizio
	       and a.ti_appartenenza=aAss.ti_appartenenza
	       and a.ti_gestione=aAss.ti_gestione
           and a.cd_elemento_voce = aAss.cd_elemento_voce
		   and a.cd_linea_attivita = b.cd_linea_attivita
		   and a.cd_centro_responsabilita = b.cd_centro_responsabilita
		   and b.cd_natura = aAss.cd_natura;
   if aNum > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Associazione non eliminabile perch? utilizzata!');
   end if;
  end if;

  aNum:=0;
  begin
   aCds:=CNRCTB020.GETCDSSACVALIDO(aAss.esercizio);
   select * into aEsercizio from esercizio where
           cd_cds = aCds.cd_unita_organizzativa
	   and esercizio = aAss.esercizio
	   and st_apertura_chiusura <> 'I';
   if -- Associzione n.3 Capitolo di spesa CDS - > Capitolo di spesa CNR categoria 2
	       aAss.ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
	   and aAss.ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and aAss.ti_elemento_voce = CNRCTB001.CAPITOLO
	   and aAss.ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CNR
	   and aAss.ti_gestione_coll = CNRCTB001.GESTIONE_SPESE
	   and aAss.ti_elemento_voce_coll = CNRCTB001.CAPITOLO Then

	select count(*) into aNum
	from   voce_f_saldi_cdr_linea a, voce_f b
	where  a.esercizio          = aAss.esercizio And
	       b.esercizio          = aAss.esercizio And
	       a.ti_appartenenza    = aAss.ti_appartenenza_coll And
	       a.ti_gestione        = aAss.ti_gestione_coll And
	       b.ti_appartenenza    = aAss.ti_appartenenza_coll And
	       b.ti_gestione        = aAss.ti_gestione_coll And
	       b.cd_titolo_capitolo = aAss.cd_elemento_voce_coll And
	       b.cd_voce            = a.cd_voce And
	       b.cd_parte           = CNRCTB001.PARTE1 And
	       b.cd_categoria       = CNRCTB001.CATEGORIA2_SPESE_CNR;

    if aNum > 0 then
     IBMERR001.RAISE_ERR_GENERICO('Associazione non eliminabile perch? utilizzata!');
    end if;
   end if;
  exception when NO_DATA_FOUND then
   null;
  end;

  aNum:=0;
  if -- Associazione n.4 Titolo spesa cds + Tipo di CDS + Natura -> Capitolo di entrata CDS
	       aAss.ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
	   and aAss.ti_gestione = CNRCTB001.GESTIONE_SPESE
	   and aAss.ti_elemento_voce = CNRCTB001.TITOLO
	   and aAss.cd_natura != '*'
	   and aAss.cd_cds != '*'
	   and aAss.ti_appartenenza_coll = CNRCTB001.APPARTENENZA_CDS
	   and aAss.ti_gestione_coll = CNRCTB001.GESTIONE_ENTRATE
	   and aAss.ti_elemento_voce_coll = CNRCTB001.CAPITOLO then
     Select count(*) into aNum
     From   voce_f_saldi_cdr_linea a, voce_f b, unita_organizzativa c
     Where  a.esercizio              = aAss.esercizio And
            b.esercizio              = aAss.esercizio And
            a.ti_appartenenza        = aAss.ti_appartenenza_coll And
            a.ti_gestione            = aAss.ti_gestione_coll And
            b.ti_appartenenza        = aAss.ti_appartenenza_coll And
            b.ti_gestione            = aAss.ti_gestione_coll And
            c.cd_unita_organizzativa = b.cd_cds And
            c.cd_tipo_unita          = aAss.cd_cds And -- in aAss c'? il tipo di cds
            b.cd_natura              = aAss.cd_natura And
            b.cd_voce                = a.cd_voce And
            b.cd_parte               = CNRCTB001.PARTE1 And
            b.cd_categoria           = CNRCTB001.CATEGORIA1_SPESE_CNR;

   if aNum > 0 then
    IBMERR001.RAISE_ERR_GENERICO('Associazione non eliminabile perch? utilizzata!');
   end if;
  end if;

 end;


 function getVoceRicFigAltroCDR(aEsercizio number) return elemento_voce%rowtype is
  aEV elemento_voce%rowtype;
 begin
     select * into aEV from elemento_voce where
           esercizio = aEsercizio
       and ti_appartenenza = CNRCTB001.APPARTENENZA_CNR
       and ti_gestione = CNRCTB001.GESTIONE_ENTRATE
       and cd_elemento_voce = CNRCTB015.getVal01PerChiave(aEsercizio, 'ELEMENTO_VOCE_SPECIALE','RICAVO_FIGURATIVO_ALTRO_CDR');
     return aEV;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Conto ricavo figurativo altro CDR non specificato in configurazione CNR per l''esercizio '||aEsercizio);
 end;


 function getVoceTFR(aEsercizio number) return elemento_voce%rowtype is
  aEV elemento_voce%rowtype;
 begin
     select * into aEV from elemento_voce where
           esercizio = aEsercizio
       and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
       and ti_gestione = CNRCTB001.GESTIONE_SPESE
       and cd_elemento_voce = CNRCTB015.getVal01PerChiave(aEsercizio, 'ELEMENTO_VOCE_SPECIALE','TFR');
     return aEV;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Conto Trattamento Fine Rapporto non specificato in configurazione CNR per l''esercizio '||aEsercizio);
 end;

 function getVoceTFRTEMPODET(aEsercizio number) return elemento_voce%rowtype is
  aEV elemento_voce%rowtype;
 begin
     select * into aEV from elemento_voce where
           esercizio = aEsercizio
       and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
       and ti_gestione = CNRCTB001.GESTIONE_SPESE
       and cd_elemento_voce = CNRCTB015.getVal01PerChiave(aEsercizio, 'ELEMENTO_VOCE_SPECIALE','TFR_TEMPO_DET');
     return aEV;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Conto Trattamento Fine Rapporto per personale a tempo determinato non specificato in configurazione CNR per l''esercizio '||aEsercizio);
 end;

 function getVoceONERICNR(aEsercizio number) return elemento_voce%rowtype is
  aEV elemento_voce%rowtype;
 begin
     select * into aEV from elemento_voce where
           esercizio = aEsercizio
       and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
       and ti_gestione = CNRCTB001.GESTIONE_SPESE
       and cd_elemento_voce = CNRCTB015.getVal01PerChiave(aEsercizio, 'ELEMENTO_VOCE_SPECIALE','ONERI_CNR');
     return aEV;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Conto Oneri Cnr non specificato in configurazione CNR per l''esercizio '||aEsercizio);
 end;

 function getVoceONERICNRTEMPODET(aEsercizio number) return elemento_voce%rowtype is
  aEV elemento_voce%rowtype;
 begin
     select * into aEV from elemento_voce where
           esercizio = aEsercizio
       and ti_appartenenza = CNRCTB001.APPARTENENZA_CDS
       and ti_gestione = CNRCTB001.GESTIONE_SPESE
       and cd_elemento_voce = CNRCTB015.getVal01PerChiave(aEsercizio, 'ELEMENTO_VOCE_SPECIALE','ONERI_CNR_TEMPO_DET');
     return aEV;
 exception when NO_DATA_FOUND then
  IBMERR001.RAISE_ERR_GENERICO('Conto Oneri Cnr per personale a tempo determinato non specificato in configurazione CNR per l''esercizio '||aEsercizio);
 end;

Function TIPO_VAR_PDG (aEsercizio NUMBER, aNumVar NUMBER)  Return  VARCHAR2 Is

Tipo                    VARCHAR2(20);
conta_prel              NUMBER;
CDS_PROP                unita_organizzativa.cd_unita_organizzativa%Type;
CDR_PROP                CDR%Rowtype;
CDS_DETTAGLI            unita_organizzativa.cd_unita_organizzativa%Type;
STORNO_SPESA            NUMBER;
STORNO_ENTRATA          NUMBER;
CONTA_STO_SPE_CDR       NUMBER;
CONTA_STO_SPE           NUMBER;
CONTA_STO_ETR_CDR       NUMBER;
CONTA_STO_ETR           NUMBER;
CONTA_VAR_CDR           NUMBER;
CONTA_VAR_TOT           NUMBER;
CONTA_ERRORE            NUMBER;

Begin

-- RECUPERO IL CDR PROPONENTE

Select CDR.*
Into   CDR_PROP
From   CDR, PDG_VARIAZIONE
Where  PDG_VARIAZIONE.ESERCIZIO = aESERCIZIO And
       PDG_VARIAZIONE.PG_VARIAZIONE_PDG = aNumVar And
       PDG_VARIAZIONE.CD_CENTRO_RESPONSABILITA = CDR.CD_CENTRO_RESPONSABILITA;

-- RECUPERO IL CDS DEL CDR PROPONENTE

CDS_PROP := CNRUTL001.GETCDSFROMCDR(CDR_PROP.cd_centro_responsabilita);

-- IMPOSSIBILE STABILIRE VARIAZIONE NON DEF E NON APP

Select Count(*)
Into   CONTA_ERRORE
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO Not In ('PRD', 'APP');

If conta_ERRORE > 0 Then
  tipo := 'NO TIPO';
  Return TIPO;
End If;

-- PRELIEVO DAL FONDO

Select Count(*)
Into   conta_prel
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
        (Select ESERCIZIO, PG_VARIAZIONE_PDG
         From   ASS_PDG_VARIAZIONE_CDR
         Where  ESERCIZIO = aESERCIZIO And
                PG_VARIAZIONE_PDG = aNumvar
         Group By ESERCIZIO, PG_VARIAZIONE_PDG
         Having Sum(IM_ENTRATA) != Sum(IM_SPESA));

If conta_prel > 0 Then
  tipo := 'PREL_FON';
  Return TIPO;
End If;


-- STORNI DI SPESA

Select Count(*)
Into   STORNO_SPESA
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
                (Select ESERCIZIO, PG_VARIAZIONE_PDG
                 From   ASS_PDG_VARIAZIONE_CDR
                 Where  ESERCIZIO = aESERCIZIO And
                        PG_VARIAZIONE_PDG = aNumvar
                 Group By ESERCIZIO, PG_VARIAZIONE_PDG
                 Having Sum(IM_ENTRATA) = Sum(IM_SPESA)) And Not Exists
               (Select 1 FROM PDG_PREVENTIVO_ETR_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG) And Exists
               (Select 1 FROM PDG_PREVENTIVO_SPE_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG);

If STORNO_SPESA > 0 Then

   Begin
    Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
    Into   CDS_DETTAGLI
    From   PDG_PREVENTIVO_SPE_DET
    WHERE   ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
        AND PG_VARIAZIONE_PDG        = aNumvar;

    If CDS_DETTAGLI = CDS_PROP Then
           CONTA_STO_SPE_CDR := 1;
    Else
           CONTA_STO_SPE := 1;
    End If;

   Exception
     When Too_Many_Rows Then
           CONTA_STO_SPE := 1;
   End;

End If;

If CONTA_STO_SPE_CDR > 0 Then
  tipo := 'STO_S_CDS';
  Return TIPO;
Elsif CONTA_STO_SPE > 0 Then
  tipo := 'STO_S_TOT';
  Return TIPO;
End If;


-- STORNI DI ENTRATA

Select Count(*)
Into   STORNO_ENTRATA
From   PDG_VARIAZIONE
WHERE  ESERCIZIO = aESERCIZIO And
       PG_VARIAZIONE_PDG = aNumVar And
       STATO In ('PRD', 'APP') And Exists
                (Select ESERCIZIO, PG_VARIAZIONE_PDG
                 From   ASS_PDG_VARIAZIONE_CDR
                 Where  ESERCIZIO = aESERCIZIO And
                        PG_VARIAZIONE_PDG = aNumvar
                 Group By ESERCIZIO, PG_VARIAZIONE_PDG
                 Having Sum(IM_ENTRATA) = Sum(IM_SPESA)) And Not Exists
               (Select 1 FROM PDG_PREVENTIVO_SPE_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG) And Exists
               (Select 1 FROM PDG_PREVENTIVO_ETR_DET
                WHERE ESERCIZIO_PDG_VARIAZIONE = PDG_VARIAZIONE.ESERCIZIO
                  AND PG_VARIAZIONE_PDG        = PDG_VARIAZIONE.PG_VARIAZIONE_PDG);

If STORNO_ENTRATA > 0 Then

   Begin
    Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
    Into   CDS_DETTAGLI
    From   PDG_PREVENTIVO_ETR_DET
    WHERE   ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
        AND PG_VARIAZIONE_PDG        = aNumvar;

    If CDS_DETTAGLI = CDS_PROP Then
           CONTA_STO_ETR_CDR := 1;
    Else
           CONTA_STO_ETR := 1;
    End If;

   Exception
     When Too_Many_Rows Then
           CONTA_STO_ETR := 1;
   End;

End If;

If CONTA_STO_ETR_CDR > 0 Then
  tipo := 'STO_E_CDS';
  Return TIPO;
Elsif CONTA_STO_ETR > 0 Then
  tipo := 'STO_E_TOT';
  Return TIPO;
End If;

-- VARIAZIONI STESSO CDS E A CAVALLO
/* -- */

Begin
   Begin
   Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
   Into   CDS_DETTAGLI
   From   PDG_PREVENTIVO_ETR_DET
   WHERE  ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
      And PG_VARIAZIONE_PDG        = aNumvar;
   Exception
      When No_Data_Found Then
        CDS_DETTAGLI := 1;
    End;
   If CDS_DETTAGLI = CDS_PROP Then

       Select Distinct CNRUTL001.GETCDSFROMCDR(cd_centro_responsabilita)
       Into   CDS_DETTAGLI
       From   PDG_PREVENTIVO_SPE_DET
       WHERE  ESERCIZIO_PDG_VARIAZIONE = aESERCIZIO
          And PG_VARIAZIONE_PDG        = aNumvar;

          If CDS_DETTAGLI = CDS_PROP Then
                  CONTA_VAR_CDR := 1;
          Else
                  CONTA_VAR_TOT := 1;
          End If;
   Else
          CONTA_VAR_TOT := 1;
   End If;

Exception
   When Too_Many_Rows Then
        CONTA_VAR_TOT := 1;
End;

If CONTA_VAR_CDR > 0 Then
  tipo := 'VAR_CDS';
  Return TIPO;
Elsif CONTA_VAR_TOT > 0 Then
  tipo := 'VAR_TOT';
  Return TIPO;
End If;

/* -- */

tipo := 'VARIAZIONE';
Return TIPO;

End;

-- dato esercizio/numero della variazione PDG restituisce il tipo

Function DESCR_TIPO_VAR_PDG (Tipo VARCHAR) Return  VARCHAR2 Is

Begin

If Tipo = 'STO_S_CDS' Then
        RETURN DESCR_STO_S_CDS;
Elsif Tipo = 'STO_E_CDS' THEN
        RETURN DESCR_STO_E_CDS;
Elsif Tipo = 'STO_S_TOT' THEN
        RETURN DESCR_STO_S_TOT;
Elsif Tipo = 'STO_E_TOT' THEN
        RETURN DESCR_STO_E_TOT;
Elsif Tipo = 'PREL_FON'  THEN
        Return DESCR_PREL_FON;
Elsif Tipo = 'VAR_CDS'   THEN
        RETURN DESCR_VAR_CDS;
Elsif Tipo = 'VAR_TOT'   THEN
        Return DESCR_VAR_TOT;
Elsif Tipo = 'NO TIPO' THEN
	Return null;
End If;

End;


 Procedure ins_ELEMENTO_VOCE (aDest ELEMENTO_VOCE%rowtype) is
  begin
   insert into ELEMENTO_VOCE (
     FL_VOCE_SAC
    ,FL_VOCE_NON_SOGG_IMP_AUT
    ,FL_VOCE_PERSONALE
    ,FL_PARTITA_GIRO
    ,CD_CAPOCONTO_FIN
    ,ESERCIZIO
    ,CD_ELEMENTO_VOCE
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,TI_ELEMENTO_VOCE
    ,CD_PARTE
    ,CD_PROPRIO_ELEMENTO
    ,DS_ELEMENTO_VOCE
    ,DUVA
    ,CD_ELEMENTO_PADRE
    ,UTUV
    ,DACR
    ,UTCR
    ,PG_VER_REC
    ,FL_LIMITE_ASS_OBBLIG
   ) values (
     aDest.FL_VOCE_SAC
    ,aDest.FL_VOCE_NON_SOGG_IMP_AUT
    ,aDest.FL_VOCE_PERSONALE
    ,aDest.FL_PARTITA_GIRO
    ,aDest.CD_CAPOCONTO_FIN
    ,aDest.ESERCIZIO
    ,aDest.CD_ELEMENTO_VOCE
    ,aDest.TI_APPARTENENZA
    ,aDest.TI_GESTIONE
    ,aDest.TI_ELEMENTO_VOCE
    ,aDest.CD_PARTE
    ,aDest.CD_PROPRIO_ELEMENTO
    ,aDest.DS_ELEMENTO_VOCE
    ,aDest.DUVA
    ,aDest.CD_ELEMENTO_PADRE
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.PG_VER_REC
    ,aDest.FL_LIMITE_ASS_OBBLIG
    );
 end;

 procedure ins_ASS_EV_FUNZ_TIPOCDS (aDest ASS_EV_FUNZ_TIPOCDS%rowtype) is
  begin
   insert into ASS_EV_FUNZ_TIPOCDS (
     ESERCIZIO
    ,CD_CONTO
    ,CD_FUNZIONE
    ,CD_TIPO_UNITA
    ,DUVA
    ,UTUV
    ,DACR
    ,UTCR
    ,PG_VER_REC
   ) values (
     aDest.ESERCIZIO
    ,aDest.CD_CONTO
    ,aDest.CD_FUNZIONE
    ,aDest.CD_TIPO_UNITA
    ,aDest.DUVA
    ,aDest.UTUV
    ,aDest.DACR
    ,aDest.UTCR
    ,aDest.PG_VER_REC
    );
 end;

 procedure coie_ELEMENTO_VOCE (aEsDest esercizio%rowtype) is
  begin
   insert into ELEMENTO_VOCE (
     ESERCIZIO
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_PROPRIO_ELEMENTO
    ,TI_ELEMENTO_VOCE
    ,FL_LIMITE_ASS_OBBLIG
    ,FL_VOCE_PERSONALE
    ,FL_PARTITA_GIRO
    ,CD_CAPOCONTO_FIN
    ,FL_VOCE_SAC
    ,FL_VOCE_NON_SOGG_IMP_AUT
    ,CD_PARTE
    ,DS_ELEMENTO_VOCE
    ,DACR
    ,UTUV
    ,UTCR
    ,DUVA
    ,PG_VER_REC
    ,CD_ELEMENTO_PADRE
   )
   select * from (
    select
     aEsDest.esercizio ESERCIZIO
    ,TI_APPARTENENZA
    ,TI_GESTIONE
    ,CD_ELEMENTO_VOCE
    ,CD_PROPRIO_ELEMENTO
    ,TI_ELEMENTO_VOCE
    ,FL_LIMITE_ASS_OBBLIG
    ,FL_VOCE_PERSONALE
	,FL_PARTITA_GIRO
    ,CD_CAPOCONTO_FIN
    ,FL_VOCE_SAC
    ,FL_VOCE_NON_SOGG_IMP_AUT
    ,CD_PARTE
    ,DS_ELEMENTO_VOCE
    ,aEsDest.dacr
    ,aEsDest.utuv
    ,aEsDest.utcr
    ,aEsDest.duva
    ,PG_VER_REC
    ,CD_ELEMENTO_PADRE
   from ELEMENTO_VOCE
   where esercizio = 0
   order by esercizio, ti_appartenenza, ti_gestione, cd_elemento_voce
  );
 end;

Function TIPO_MANDATO (aCD_CDS VARCHAR2, aESERCIZIO NUMBER, aPG_MANDATO NUMBER, aESERCIZIO_OBBLIGAZIONE NUMBER,
                        aPG_OBBLIGAZIONE NUMBER, aPG_OBBLIGAZIONE_SCADENZARIO NUMBER, aCD_CDS_DOC_AMM VARCHAR2,
                        aCD_UO_DOC_AMM VARCHAR2, aESERCIZIO_DOC_AMM NUMBER, aCD_TIPO_DOCUMENTO_AMM VARCHAR2,
                        aPG_DOC_AMM NUMBER, aESERCIZIO_ORI_OBBLIGAZIONE NUMBER)  Return VARCHAR2 Is

riga_intera mandato_riga%Rowtype;

Begin
                     Select *
                     Into  riga_intera
                     From  mandato_riga
                     Where CD_CDS                      = aCD_CDS                        and
                           ESERCIZIO                   = aESERCIZIO                     and
                           PG_MANDATO                  = aPG_MANDATO                    and
                           ESERCIZIO_OBBLIGAZIONE      = aESERCIZIO_OBBLIGAZIONE        and
                           PG_OBBLIGAZIONE             = aPG_OBBLIGAZIONE               and
                           PG_OBBLIGAZIONE_SCADENZARIO = aPG_OBBLIGAZIONE_SCADENZARIO   and
                           CD_CDS_DOC_AMM              = aCD_CDS_DOC_AMM                and
                           CD_UO_DOC_AMM               = aCD_UO_DOC_AMM                 and
                           ESERCIZIO_DOC_AMM           = aESERCIZIO_DOC_AMM             and
                           CD_TIPO_DOCUMENTO_AMM       = aCD_TIPO_DOCUMENTO_AMM         and
                           PG_DOC_AMM                  = aPG_DOC_AMM                    and
                           ESERCIZIO_ORI_OBBLIGAZIONE  = aESERCIZIO_ORI_OBBLIGAZIONE;

Return tipo_mandato(riga_intera);

End;

Function TIPO_MANDATO (aMandatoRiga mandato_riga%Rowtype) Return VARCHAR2 Is

-- Restituisce la tipologia di mandato. I tipi contemplati sono:
-- Apertura Fondo economale
-- Incrementi Fondo economale
-- Reintegri Fondo economale
-- Versamenti CORI Istituti
-- Versamenti IVA Istituti Commerciale
-- Versamenti IVA Istituti Intra
-- Versamenti CORI Ente
-- Versamenti IVA Ente
-- Pagamento Doc. Amministrativo

conta NUMBER;

Begin

-- Apertura Fondo economale

Select  count(0)
Into    conta
From    fondo_economale
Where   CD_CDS     = aMandatoRiga.cd_cds And
        ESERCIZIO  = aMandatoRiga.esercizio And
        PG_MANDATO = aMandatoRiga.PG_MANDATO;

If conta > 0 Then
  Return 'Apertura Fondo Economale';
End If;

-- Incrementi Fondo Economale

Select  count(0)
Into    conta
From    ass_fondo_eco_mandato
Where   CD_CDS_MANDATO    = aMandatoRiga.cd_cds And
        ESERCIZIO_MANDATO = aMandatoRiga.esercizio And
        PG_MANDATO        = aMandatoRiga.PG_MANDATO;

If conta > 0 Then
  Return 'Incrementi Fondo Economale';
End If;

-- Reintegri Fondo economale

Declare
  TIPO_man  mandato.TI_mandato%Type;

Begin

Select  Distinct MANDATO.TI_MANDATO
Into    TIPO_MAN
From    fondo_spesa, mandato
Where   fondo_spesa.CD_CDS_MANDATO     = aMandatoRiga.cd_cds And
        fondo_spesa.ESERCIZIO_MANDATO  = aMandatoRiga.esercizio And
        fondo_spesa.PG_MANDATO         = aMandatoRiga.PG_MANDATO And
        fondo_spesa.CD_CDS_MANDATO     = mandato.cd_cds And
        fondo_spesa.ESERCIZIO_MANDATO  = mandato.esercizio And
        fondo_spesa.PG_MANDATO         = mandato.PG_MANDATO;


If TIPO_MAN = 'R' Then
  Return 'Regolarizzazione per chiusura Fondo Economale';
Else
  Return 'Reintegri Fondo Economale';
End If;

Exception
   When No_Data_Found Then Null;

End;


-- Regolarizzazione Contabile che genera Variazione al bilancio di servizio

Select  count(0)
Into    conta
From    var_bilancio
Where   CD_CDS_MANDATO     = aMandatoRiga.cd_cds And
        ESERCIZIO_MANDATO  = aMandatoRiga.esercizio And
        PG_MANDATO         = aMandatoRiga.PG_MANDATO;

If conta > 0 Then
  Return 'Mandato di Regolarizzazione contabile che ha generato Variazione al Bilancio di servizio';
End If;

/*
-- Liquidazione iva centro

Select  count(0)
Into    conta
From    LIQUIDAZIONE_IVA_CENTRO
Where   CD_CDS_OBB_ACCENTR        = aMandatoRiga.CD_CDS And
        ESERCIZIO_OBB_ACCENTR     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
        PG_OBB_ACCENTR            = aMandatoRiga.PG_OBBLIGAZIONE And
        ESERCIZIO_ORI_OBB_ACCENTR = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE;

If conta > 0 Then
  Return 'Mandato di Liquidazione IVA al centro';
End If;
*/

-- Liquidazione iva


Declare
  CDS_LIQ   LIQUIDAZIONE_IVA.CD_CDS%Type;
  TIPO_LIQ  LIQUIDAZIONE_IVA.TIPO_LIQUIDAZIONE%Type;

Begin

Select  CD_CDS, TIPO_LIQUIDAZIONE
Into    CDS_LIQ, TIPO_LIQ
From    LIQUIDAZIONE_IVA
Where   --CD_CDS_OBB_ACCENTR        = aMandatoRiga.CD_CDS And
        --ESERCIZIO_OBB_ACCENTR     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
        --PG_OBB_ACCENTR            = aMandatoRiga.PG_OBBLIGAZIONE And
        --ESERCIZIO_ORI_OBB_ACCENTR = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
        ESERCIZIO_DOC_AMM         = aMandatoRiga.ESERCIZIO_DOC_AMM And
        CD_TIPO_DOCUMENTO         = aMandatoRiga.CD_TIPO_DOCUMENTO_AMM And
        CD_CDS_DOC_AMM            = aMandatoRiga.CD_CDS_DOC_AMM And
        CD_UO_DOC_AMM             = aMandatoRiga.CD_UO_DOC_AMM And
        PG_DOC_AMM                = aMandatoRiga.PG_DOC_AMM And
        STATO                     = 'D';

        If TIPO_LIQ = 'C' Then
          If CDS_LIQ = CNRCTB020.GETCDCDSENTE (aMandatoRiga.esercizio) Then
            Return 'Mandato di Versamento Definitivo IVA Commerciale';
          Else
            Return 'Mandato di Trasferimento IVA Commerciale';
          End If;
        Elsif TIPO_LIQ = 'I' Then
          If CDS_LIQ = CNRCTB020.GETCDCDSENTE (aMandatoRiga.esercizio) Then
            Return 'Mandato di Versamento Definitivo IVA Istituzionale Intra UE';
          Else
            Return 'Mandato di Trasferimento IVA Istituzionale Intra UE';
          End If;
        Elsif TIPO_LIQ = 'S' Then
          If CDS_LIQ = CNRCTB020.GETCDCDSENTE (aMandatoRiga.esercizio) Then
            Return 'Mandato di Versamento Definitivo IVA Istituzionale San Marino senza IVA';
          Else
            Return 'Mandato di Trasferimento IVA Istituzionale San Marino senza IVA';
          End If;
        End If;

Exception
   When No_Data_Found Then Null;
End;

-- LIQUID_GRUPPO_CENTRO

Declare
  DA_ES_PREC  CHAR(1);

Begin

  Select DA_ESERCIZIO_PRECEDENTE
  Into   DA_ES_PREC
  From   LIQUID_GRUPPO_CENTRO
  Where  CD_CDS_OBB_ACCENTR        = aMandatoRiga.CD_CDS And
         ESERCIZIO_OBB_ACCENTR     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
         ESERCIZIO_ORI_OBB_ACCENTR = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
         PG_OBB_ACCENTR            = aMandatoRiga.PG_OBBLIGAZIONE;

  If DA_ES_PREC = 'N' Then
       Return 'Mandato di Versamento Definitivo Contributi e Ritenute';
  Elsif DA_ES_PREC = 'Y' Then
       Return 'Mandato di Versamento Definitivo Contributi e Ritenute da esercizio precedente';
  End If;

Exception
   When No_Data_Found Then Null;
End;


-- LIQUID_GRUPPO_CORI

Select  count(0)
Into    conta
From    LIQUID_GRUPPO_CORI
Where   CD_CDS_DOC                = aMandatoRiga.CD_CDS And
        ESERCIZIO_DOC             = aMandatoRiga.ESERCIZIO And
        PG_DOC                    = aMandatoRiga.PG_MANDATO; /* And
        CD_CDS_OBB_ACCENTR        = aMandatoRiga.CD_CDS And
        ESERCIZIO_OBB_ACCENTR     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
        PG_OBB_ACCENTR            = aMandatoRiga.PG_OBBLIGAZIONE And
        ESERCIZIO_ORI_OBB_ACCENTR = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE;*/

If conta > 0 Then
  Return 'Mandato di Trasferimento Contributi e Ritenute';
End If;


-- Mandato Principale Stipendi

Select  count(0)
Into    conta
From    STIPENDI_COFI
Where   CD_CDS_MANDATO     = aMandatoRiga.cd_cds And
        ESERCIZIO_MANDATO  = aMandatoRiga.esercizio And
        PG_MANDATO         = aMandatoRiga.PG_MANDATO;

If conta > 0 Then
  Return 'Mandato Principale Stipendi';
End If;

-- STIPENDI_COFI_OBB

Select  count(0)
Into    conta
From    STIPENDI_COFI_OBB
Where   CD_CDS_OBBLIGAZIONE        = aMandatoRiga.CD_CDS And
        ESERCIZIO_OBBLIGAZIONE     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
        ESERCIZIO_ORI_OBBLIGAZIONE = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
        PG_OBBLIGAZIONE            = aMandatoRiga.PG_OBBLIGAZIONE;

If conta > 0 Then
  Return 'Mandato Stipendi';
End If;

-- Compensi per Conguagli

Select Count(0)
Into   conta
From   CONGUAGLIO C , CONTRIBUTO_RITENUTA CR
Where  CR.CD_CDS_OBBLIGAZIONE        = aMandatoRiga.CD_CDS And
       CR.ESERCIZIO_OBBLIGAZIONE     = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
       CR.ESERCIZIO_ORI_OBBLIGAZIONE = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
       CR.PG_OBBLIGAZIONE            = aMandatoRiga.PG_OBBLIGAZIONE And
       CR.CD_CDS                     = C.CD_CDS_COMPENSO     And
       CR.CD_UNITA_ORGANIZZATIVA     = C.CD_UO_COMPENSO      And
       CR.ESERCIZIO                  = C.ESERCIZIO_COMPENSO  And
       CR.PG_COMPENSO                = C.PG_COMPENSO;

If conta > 0 Then
  Return 'Mandato Conguaglio Fiscale';
End If;

-- MANDATI DI TRASFERIMENTO

Select  Count(0)
Into    conta
From    MANDATO
Where   CD_CDS     = aMandatoRiga.CD_CDS And
        ESERCIZIO  = aMandatoRiga.ESERCIZIO And
        PG_MANDATO = aMandatoRiga.PG_MANDATO And
        TI_MANDATO = CNRCTB038.TI_MAN_ACCRED;

If CONTA > 0 Then
  Return 'Mandato di Accreditamento';
End If;

-- aperture fondi non ufficiali

If aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = cnrctb100.TI_GEN_APERTURA_FONDO Then

         Select Count(0)
         Into   conta
         From   fondo_economale
         Where  CD_CDS     = aMandatoRiga.cd_cds And
                ESERCIZIO  = aMandatoRiga.esercizio And
                PG_MANDATO = aMandatoRiga.PG_MANDATO;

         If conta = 0 Then
                Return 'Apertura Fondo Economale senza Fondo associato';
         End If;

End If;

-- MANDATI DI RESTITUZIONE

Select  Count(0)
Into    CONTA
From    LIQUID_GRUPPO_CENTRO_COMP
Where   (CD_CDS_ACC_ACCENTR, ESERCIZIO_ACC_ACCENTR, ESERCIZIO_ORI_ACC_ACCENTR, PG_ACC_ACCENTR) In
        (Select CD_CDS, ESERCIZIO, ESERCIZIO_ORI_ACCERTAMENTO, PG_ACCERTAMENTO
         From   ASS_OBB_ACR_PGIRO
         Where  CD_CDS                     = aMandatoRiga.CD_CDS And
                ESERCIZIO                  = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
                ESERCIZIO_ORI_OBBLIGAZIONE = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
                PG_OBBLIGAZIONE            = aMandatoRiga.PG_OBBLIGAZIONE);

If CONTA > 0 Then
          Return 'Mandato di restituzione crediti Versamento CORI';
End If;

-- mandati su GEN_CORA_S su pertite di giro (compensi senza calcoli)

If aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_GEN_CORI_ACC_SPESA Then

  Select Count(0)
  Into   CONTA
  From   ASS_OBB_ACR_PGIRO
  Where  CD_CDS                     = aMandatoRiga.CD_CDS And
         ESERCIZIO                  = aMandatoRiga.ESERCIZIO_OBBLIGAZIONE And
         ESERCIZIO_ORI_OBBLIGAZIONE = aMandatoRiga.ESERCIZIO_ORI_OBBLIGAZIONE And
         PG_OBBLIGAZIONE            = aMandatoRiga.PG_OBBLIGAZIONE;

        If CONTA > 0 Then
                Return 'Mandato su Compensi senza calcoli su Partite di Giro';
        Else
                Return 'Mandato su Compensi senza calcoli NON su Partite di Giro';
        End If;

End If;

/************************************************************************************************************/
/****        ATTENZIONE !!!!!!!!!!!!         QUESTA "RETURN" VA SEMPRE PER ULTIMA    !!!!!!!!!!!!     *******/
/************************************************************************************************************/

If aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_FATTURA_PASSIVA Then
   Return 'Mandato di pagamento di Fattura Passiva';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_FATTURA_ATTIVA Then
   Return 'Mandato di pagamento di Fattura Attiva';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_GENERICO_ENTRATA Then
   Return 'Mandato di pagamento di Generico di Entrata';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_GENERICO_SPESA Then
   Return 'Mandato di pagamento di Generico di Spesa';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_GENERICO_TRASF_E Then
   Return 'Mandato di pagamento di Trasferimento di Entrata';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_GENERICO_TRASF_S Then
   Return 'Mandato di pagamento di Trasferimento di Spesa';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_COMPENSO Then
   Return 'Mandato di pagamento di Compenso';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_MISSIONE Then
   Return 'Mandato di pagamento di Missione';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_ANTICIPO Then
   Return 'Mandato di pagamento di Anticipo';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_RIMBORSO Then
   Return 'Mandato di pagamento di Rimborso';
Elsif aMandatoRiga.CD_TIPO_DOCUMENTO_AMM = CNRCTB100.TI_AUTOFATTURA Then
   Return 'Mandato di pagamento di Autofattura';
Else
   Return 'Mandato di pagamento di '||aMandatoRiga.CD_TIPO_DOCUMENTO_AMM;
End If;

End;

End;
/


