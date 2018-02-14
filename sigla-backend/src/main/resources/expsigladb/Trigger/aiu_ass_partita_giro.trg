CREATE OR REPLACE TRIGGER aiu_ass_partita_giro
AFTER INSERT OR UPDATE OF ESERCIZIO, TI_APPARTENENZA, TI_GESTIONE, CD_VOCE,
TI_APPARTENENZA_CLG, TI_GESTIONE_CLG, CD_VOCE_CLG
 ON ASS_PARTITA_GIRO REFERENCING NEW AS NEW OLD AS OLD
FOR EACH ROW
declare
 recParametriCNR parametri_cnr%rowtype;
begin
--
-- Trigger attivato su inserimento  e aggiornamento voce nella tabella ASS_PARTITA_GIRO (After)
--
-- Date: 08/01/2016
-- Version: 1.0
--
-- Dependency: CNRCTB035
--
-- History:
--
-- Date: 08/09/2015
-- Version: 1.0
-- Gestione Associazione Partite Giro:
-- spostato a livello trigger il controllo dell'esistenza voce bilancio
--
-- Body:
--
    recParametriCNR := CNRUTL001.getRecParametriCnr(:new.ESERCIZIO);

    If nvl(recParametriCNR.fl_nuovo_pdg,'N')='Y' Then
        Declare
            recElementoVoce elemento_voce%rowtype;
        Begin
            Select * into recElementoVoce
            from elemento_voce
            Where esercizio = :new.ESERCIZIO
            and   ti_appartenenza = :new.TI_APPARTENENZA
            and   ti_gestione = :new.TI_GESTIONE
            and   cd_elemento_voce = :new.CD_VOCE;
        Exception
            When others Then
        IBMERR001.RAISE_ERR_GENERICO('Voce di Bilancio '||to_char(:new.ESERCIZIO)||'/'||:new.TI_APPARTENENZA||'/'||:new.TI_GESTIONE||'/'||:new.CD_VOCE||' non presente nella tabella ELEMENTO_VOCE. Inserimento non possibile.');
        End;

        Declare
            recElementoVoce elemento_voce%rowtype;
        Begin
            Select * into recElementoVoce
            from elemento_voce
            Where esercizio = :new.ESERCIZIO
            and   ti_appartenenza = :new.TI_APPARTENENZA_CLG
            and   ti_gestione = :new.TI_GESTIONE_CLG
            and   cd_elemento_voce = :new.CD_VOCE_CLG;
        Exception
            When others Then
        IBMERR001.RAISE_ERR_GENERICO('Voce di Bilancio '||to_char(:new.ESERCIZIO)||'/'||:new.TI_APPARTENENZA_CLG||'/'||:new.TI_GESTIONE_CLG||'/'||:new.CD_VOCE_CLG||' non presente nella tabella ELEMENTO_VOCE. Inserimento non possibile.');
        End;
    Else
        Declare
            recElementoVoce voce_f%rowtype;
        Begin
            Select * into recElementoVoce
            from voce_f
            Where esercizio = :new.ESERCIZIO
            and   ti_appartenenza = :new.TI_APPARTENENZA
            and   ti_gestione = :new.TI_GESTIONE
            and   cd_voce = :new.CD_VOCE;
        Exception
            When others Then
        IBMERR001.RAISE_ERR_GENERICO('Voce di Bilancio '||to_char(:new.ESERCIZIO)||'/'||:new.TI_APPARTENENZA||'/'||:new.TI_GESTIONE||'/'||:new.CD_VOCE||' non presente nella tabella VOCE_F. Inserimento non possibile.');
        End;

        Declare
            recElementoVoce voce_f%rowtype;
        Begin
            Select * into recElementoVoce
            from voce_f
            Where esercizio = :new.ESERCIZIO
            and   ti_appartenenza = :new.TI_APPARTENENZA_CLG
            and   ti_gestione = :new.TI_GESTIONE_CLG
            and   cd_voce = :new.CD_VOCE_CLG;
        Exception
            When others Then
        IBMERR001.RAISE_ERR_GENERICO('Voce di Bilancio '||to_char(:new.ESERCIZIO)||'/'||:new.TI_APPARTENENZA_CLG||'/'||:new.TI_GESTIONE_CLG||'/'||:new.CD_VOCE_CLG||' non presente nella tabella VOCE_F. Inserimento non possibile.');
        End;
    End If;
end;
/


