CREATE OR REPLACE TRIGGER AU_DOCUMENTO_GENERICO
  AFTER UPDATE Of STATO_COFI,
                  STATO_COGE
  On DOCUMENTO_GENERICO
  For EACH ROW

--
-- Trigger attivato su DOCUMENTO_GENERICO che trasferisce al figlio DOCUMENTO_GENERICO_RIGA il nuovo STATO_COGE della testata
-- ma solo per le righe non ANNULLATE in finanziaria, che vengono contabilizzate separatamente
--
-- Date: 07/05/2007
-- Version: 1.0
--
-- Body
--
Declare
  CONTA_RIGHE_ANN    NUMBER;

Begin

-- non si pu? annullare la testata di un generico se su di esso ? stata gi? annullata una riga (da scadenza)

If :OLD.STATO_COFI != CNRCTB100.STATO_GEN_COFI_ANN And :New.STATO_COFI = CNRCTB100.STATO_GEN_COFI_ANN And
   :New.CD_TIPO_DOCUMENTO_AMM = 'GENERICO_E' Then

  Select  Count(*)
  Into    CONTA_RIGHE_ANN
  From    DOCUMENTO_GENERICO_RIGA dgr,
          ACCERTAMENTO_SCADENZARIO acs,
          ACCERTAMENTO_MODIFICA acm
  Where   dgr.CD_CDS                 = :New.CD_CDS                 And
          dgr.CD_UNITA_ORGANIZZATIVA = :New.CD_UNITA_ORGANIZZATIVA And
          dgr.ESERCIZIO              = :New.ESERCIZIO              And
          dgr.CD_TIPO_DOCUMENTO_AMM  = :New.CD_TIPO_DOCUMENTO_AMM  And
          dgr.PG_DOCUMENTO_GENERICO  = :New.PG_DOCUMENTO_GENERICO  And
          dgr.STATO_COFI             = CNRCTB100.STATO_GEN_COFI_ANN And
          dgr.CD_CDS_ACCERTAMENTO    = acs.CD_CDS                 And
          dgr.ESERCIZIO_ACCERTAMENTO = acs.ESERCIZIO              And
          dgr.ESERCIZIO_ORI_ACCERTAMENTO = acs.ESERCIZIO_ORIGINALE And
          dgr.PG_ACCERTAMENTO        = acs.PG_ACCERTAMENTO         And
          dgr.PG_ACCERTAMENTO_SCADENZARIO = acs.PG_ACCERTAMENTO_SCADENZARIO And
          acs.CD_CDS                 = acm.CD_CDS                 And
          acs.ESERCIZIO              = acm.ESERCIZIO              And
          acs.ESERCIZIO_ORIGINALE    = acm.ESERCIZIO_ORIGINALE And
          acs.PG_ACCERTAMENTO        = acm.PG_ACCERTAMENTO         And
          acs.IM_SCADENZA=0;

  If CONTA_RIGHE_ANN > 0 Then
      Ibmerr001.RAISE_ERR_GENERICO('Impossibile annullare interamente il documento, esistono dei dettagli annullati ('||
      :New.CD_CDS||'/'||:New.CD_UNITA_ORGANIZZATIVA||'/'||:New.ESERCIZIO||'/'||:New.CD_TIPO_DOCUMENTO_AMM||'/'||
      :New.PG_DOCUMENTO_GENERICO||'.');
  End If;

End If;

Update DOCUMENTO_GENERICO_RIGA
Set    STATO_COGE = :NEW.STATO_COGE--, PG_VER_REC = PG_VER_REC + 1
Where  CD_CDS                 = :New.CD_CDS                 And
       CD_UNITA_ORGANIZZATIVA = :New.CD_UNITA_ORGANIZZATIVA And
       ESERCIZIO              = :New.ESERCIZIO              And
       CD_TIPO_DOCUMENTO_AMM  = :New.CD_TIPO_DOCUMENTO_AMM  And
       PG_DOCUMENTO_GENERICO  = :New.PG_DOCUMENTO_GENERICO  And
       STATO_COFI            != CNRCTB100.STATO_GEN_COFI_ANN;

End;
/


