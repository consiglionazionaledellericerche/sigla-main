CREATE OR REPLACE FUNCTION getCurrentCdElementoVoce
    (esercizioOut ELEMENTO_VOCE.ESERCIZIO%TYPE,
     esercizioIn ELEMENTO_VOCE.ESERCIZIO%TYPE,        
     ti_gestione ELEMENTO_VOCE.TI_GESTIONE%TYPE,
     cd_elemento_voce ELEMENTO_VOCE.CD_ELEMENTO_VOCE%TYPE) 
    RETURN ELEMENTO_VOCE.CD_ELEMENTO_VOCE%TYPE IS
  currentEsercizio ELEMENTO_VOCE.ESERCIZIO%TYPE;
  currentCdVoce    ELEMENTO_VOCE.CD_ELEMENTO_VOCE%TYPE; 
Begin
  If (esercizioIn is null or esercizioOut is null or esercizioIn = esercizioOut) Then
     currentCdVoce := cd_elemento_voce;
  Elsif esercizioOut < esercizioIn Then
     currentEsercizio := esercizioIn;
     currentCdVoce := cd_elemento_voce;

     While currentEsercizio >= esercizioOut Loop
        Begin
          select a.cd_elemento_voce_old
          into currentCdVoce
          from ass_evold_evnew a
          where a.esercizio_new = currentEsercizio
          and   a.ti_gestione_new = ti_gestione
          and   a.cd_elemento_voce_new = currentCdVoce
          and   rownum < 2;
        Exception
          When no_data_found then
             null;  
        End;
        currentEsercizio := currentEsercizio - 1;
     End Loop;
  Else --esercizioIn < esercizioOut Then
     currentEsercizio := esercizioIn;
     currentCdVoce := cd_elemento_voce;

     While currentEsercizio <= esercizioOut Loop
        Begin
          select a.cd_elemento_voce_new
          into currentCdVoce
          from ass_evold_evnew a
          where a.esercizio_old = currentEsercizio
          and   a.ti_gestione_old = ti_gestione
          and   a.cd_elemento_voce_old = currentCdVoce
          and   rownum < 2;
        Exception
          When no_data_found then
             null;  
        End;
        currentEsercizio := currentEsercizio + 1;
     End Loop;
  End If;
  return currentCdVoce;
End;
/


