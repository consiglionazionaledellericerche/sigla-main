CREATE OR REPLACE Procedure ASSEGNA_IBAN (INDT_CARICO In VARCHAR, INPROG_INVIO In NUMBER) As
  CONTA_CIN                     NUMBER := 0;
  CONTA_IBAN                    NUMBER := 0;
  CONTA_IBAN_GIA_CORRETTI       NUMBER := 0;
  CONTA_CIN_GIA_CORRETTI        NUMBER := 0;
  CONTA_ERR_IBAN                NUMBER := 0;
  CONTA_COORDINATE              NUMBER := 0;
  CONTA_COORDINATE_UNICHE       NUMBER := 0;
  CONTA_COORDINATE_FL_CANC      NUMBER := 0;
  TOT_CONTA_COORDINATE_FL_CANC  NUMBER := 0;
  TOT_COORDINATE_MULTIPLE       NUMBER := 0;
  TOT_COORDINATE_UNICHE         NUMBER := 0;
  CONTA_COORDINATE_PRESENTI     NUMBER := 0;
  CONTA_COORDINATE_ASSENTI      NUMBER := 0;
  ANAGRAFICHE_IBAN              NUMBER := 0;
  CONTO_ATTUALE                 VARCHAR2(50);
  CONTA_ABI_CAB_DIVERSI         NUMBER := 0;
  TOT_ABI_CAB_DIVERSI           NUMBER := 0;
  TOT_CONTA_CANCELLATI          NUMBER := 0;
  CONTA_CANCELLATI              NUMBER := 0;
  CONTA_001_003                 NUMBER := 0;
  CONTA_ASSENTI_ALTRO           NUMBER := 0;
  TOT_CONTA_ASSENTI_ALTRO       NUMBER := 0;

  GIRO NUMBER:= 0;
  GIRO_NO NUMBER:= 0;

Begin

-- DEVONO ESSERE PASSATI LA DATA FLUSSO (YYYYMMDD) E PROG FLUSSO

If INDT_CARICO Is Not Null And INPROG_INVIO Is Not Null Then

-- PRELEVO I DATI DALLA TABELLA DEL FLUSSO ELABORATA E ASSEGNO L'IBAN ALLE COORDINATE DEI TERZI

   For CUR_IBAN in (select   Distinct CD_PAESE, CHECK_DIG, CIN, ABI, CAB, CONTO, CD_TERZO
                    from     RITORNO_IBAN_ELAB
                    where    DT_CARICO = INDT_CARICO And
                             PROG_INVIO = INPROG_INVIO And
                             CD_ESITO In ('006', '007', '008') And
                             STATO In ('I', 'E')) Loop

--Dbms_Output.PUT_LINE ('RECORD DI '||CUR_IBAN.CD_TERZO||' '||CUR_IBAN.ABI||' '||CUR_IBAN.CAB||' '||CUR_IBAN.CONTO);

   ANAGRAFICHE_IBAN := ANAGRAFICHE_IBAN + 1;

--GIRO := GIRO+1;
--Dbms_Output.PUT_LINE ('GIRO '||GIRO);

     If Length(CUR_IBAN.CD_PAESE||CUR_IBAN.CHECK_DIG||CUR_IBAN.CIN||CUR_IBAN.ABI||CUR_IBAN.CAB||CUR_IBAN.CONTO) = 27 Then

-- CONTROLLA SE RISULTANO CANCELLATE LOGICAMENTE

/*
        Select Count(*)
        Into   CONTA_COORDINATE_FL_CANC
        From   BANCA
        Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
               ABI          = CUR_IBAN.ABI And
               CAB          = CUR_IBAN.CAB And
               FL_CANCELLATO = 'Y' And
               Instr(CUR_IBAN.CONTO, Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0;

      If CONTA_COORDINATE_FL_CANC > 0 Then

          TOT_CONTA_COORDINATE_FL_CANC := TOT_CONTA_COORDINATE_FL_CANC + CONTA_COORDINATE_FL_CANC;

          Update RITORNO_IBAN_ELAB
          Set    STATO = 'A',
                 CAUSA_ERRORE = 'COORDINATE GIA'' CANCELLATE LOGICAMENTE SU SIGLA'
          Where  CD_PAESE   = CUR_IBAN.CD_PAESE And
                 CHECK_DIG  = CUR_IBAN.CHECK_DIG And
                 CIN        = CUR_IBAN.CIN And
                 ABI        = CUR_IBAN.ABI And
                 CAB        = CUR_IBAN.CAB And
                 CONTO      = CUR_IBAN.CONTO And
                 CD_TERZO   = CUR_IBAN.CD_TERZO And
                 DT_CARICO  = INDT_CARICO And
                 PROG_INVIO = INPROG_INVIO And
                 CD_ESITO In ('006', '007', '008');

      Else
*/

        Select Count(*)
        Into   CONTA_COORDINATE
        From   BANCA
        Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
               ABI          = CUR_IBAN.ABI And
               CAB          = CUR_IBAN.CAB And
               FL_CANCELLATO = 'N' And
               Instr(CUR_IBAN.CONTO, Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0;

        If CONTA_COORDINATE > 0 Then

            CONTA_COORDINATE_PRESENTI := CONTA_COORDINATE_PRESENTI + CONTA_COORDINATE;

            For COORDINATE In (Select *
                               From   BANCA
                               Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
                                      ABI          = CUR_IBAN.ABI And
                                      CAB          = CUR_IBAN.CAB And
                                      FL_CANCELLATO = 'N' And
                                      Instr(CUR_IBAN.CONTO, Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0) Loop

               If ((COORDINATE.CODICE_IBAN Is Null) Or
                   (COORDINATE.CODICE_IBAN Is Not Null And
                        COORDINATE.CODICE_IBAN != CUR_IBAN.CD_PAESE||CUR_IBAN.CHECK_DIG||CUR_IBAN.CIN||CUR_IBAN.ABI||CUR_IBAN.CAB||CUR_IBAN.CONTO)) Then

                   Update BANCA
                   Set    CODICE_IBAN = CUR_IBAN.CD_PAESE||CUR_IBAN.CHECK_DIG||CUR_IBAN.CIN||CUR_IBAN.ABI||CUR_IBAN.CAB||CUR_IBAN.CONTO
                   Where  CD_TERZO     = COORDINATE.CD_TERZO And
                          PG_BANCA     = COORDINATE.PG_BANCA;

                   Update RITORNO_IBAN_ELAB
                   Set    STATO = 'T'
                   Where  CD_PAESE   = CUR_IBAN.CD_PAESE And
                          CHECK_DIG  = CUR_IBAN.CHECK_DIG And
                          CIN        = CUR_IBAN.CIN And
                          ABI        = CUR_IBAN.ABI And
                          CAB        = CUR_IBAN.CAB And
                          CONTO      = CUR_IBAN.CONTO And
                          CD_TERZO   = CUR_IBAN.CD_TERZO And
                          DT_CARICO  = INDT_CARICO And
                          PROG_INVIO = INPROG_INVIO And
                          CD_ESITO In ('006', '007', '008');

                   CONTA_IBAN := CONTA_IBAN + 1;

                Else
                   CONTA_IBAN_GIA_CORRETTI := CONTA_IBAN_GIA_CORRETTI + 1;

                   Update RITORNO_IBAN_ELAB
                   Set    STATO = 'T'
                   Where  CD_PAESE   = CUR_IBAN.CD_PAESE And
                          CHECK_DIG  = CUR_IBAN.CHECK_DIG And
                          CIN        = CUR_IBAN.CIN And
                          ABI        = CUR_IBAN.ABI And
                          CAB        = CUR_IBAN.CAB And
                          CONTO      = CUR_IBAN.CONTO And
                          CD_TERZO   = CUR_IBAN.CD_TERZO And
                          DT_CARICO  = INDT_CARICO And
                          PROG_INVIO = INPROG_INVIO And
                          CD_ESITO In ('006', '007', '008');
                End If;


                If ((COORDINATE.CIN Is Null) Or (COORDINATE.CIN Is Not Null And COORDINATE.CIN != CUR_IBAN.CIN)) Then

                   Update BANCA
                   Set    CIN = CUR_IBAN.CIN
                   Where  CD_TERZO     = COORDINATE.CD_TERZO And
                          PG_BANCA     = COORDINATE.PG_BANCA And
                          FL_CANCELLATO = 'N';

                   CONTA_CIN := CONTA_CIN + 1;

                 Else
                   CONTA_CIN_GIA_CORRETTI := CONTA_CIN_GIA_CORRETTI + 1;
                 End If;

            End Loop; -- LOOP SULLE BANCHE DI SIGLA

       Elsif CONTA_COORDINATE = 0 Then -- SE NON TROVA COORDINATE CORRISPONDENTI

        Select Count(*)
        Into   CONTA_ABI_CAB_DIVERSI
        From   BANCA
        Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
               FL_CANCELLATO = 'N' And
               Instr(CUR_IBAN.CONTO, Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0 And
               ((ABI = CUR_IBAN.ABI And CAB != CUR_IBAN.CAB) Or
                (ABI != CUR_IBAN.ABI And CAB = CUR_IBAN.CAB));

        If CONTA_ABI_CAB_DIVERSI > 0 Then

              TOT_ABI_CAB_DIVERSI := TOT_ABI_CAB_DIVERSI + CONTA_ABI_CAB_DIVERSI;

-- PER QUELLE NON TROVATE METTE COMUNQUE LO STATO AD ERRORE PER POTERLE RIELABORARE DOPO L'EVENTUALE UPDATE;

              Update RITORNO_IBAN_ELAB
              Set    STATO = 'E',
                     CAUSA_ERRORE = 'ABI O CAB NON CORRISPONDENTI'
              Where  CD_PAESE   = CUR_IBAN.CD_PAESE And
                     CHECK_DIG  = CUR_IBAN.CHECK_DIG And
                     CIN        = CUR_IBAN.CIN And
                     ABI        = CUR_IBAN.ABI And
                     CAB        = CUR_IBAN.CAB And
                     CONTO      = CUR_IBAN.CONTO And
                     CD_TERZO   = CUR_IBAN.CD_TERZO And
                     DT_CARICO  = INDT_CARICO And
                     PROG_INVIO = INPROG_INVIO And
                     CD_ESITO In ('006', '007', '008');

        Else

-- PER QUELLE NON TROVATE METTE COMUNQUE LO STATO AD ERRORE PER POTERLE RIELABORARE DOPO L'EVENTUALE UPDATE;

              Update RITORNO_IBAN_ELAB
              Set    STATO = 'E',
                     CAUSA_ERRORE = 'NUMERO CONTO NON RICONDUCIBILE'
              Where  CD_PAESE   = CUR_IBAN.CD_PAESE And
                     CHECK_DIG  = CUR_IBAN.CHECK_DIG And
                     CIN        = CUR_IBAN.CIN And
                     ABI        = CUR_IBAN.ABI And
                     CAB        = CUR_IBAN.CAB And
                     CONTO      = CUR_IBAN.CONTO And
                     CD_TERZO   = CUR_IBAN.CD_TERZO And
                     DT_CARICO  = INDT_CARICO And
                     PROG_INVIO = INPROG_INVIO And
                     CD_ESITO In ('006', '007', '008');

        End If;



--                Dbms_Output.PUT_LINE ('ASSENTE '||CUR_IBAN.CD_TERZO||' '||CUR_IBAN.ABI||' '||CUR_IBAN.CAB||' '||CUR_IBAN.CONTO);

              CONTA_COORDINATE_ASSENTI := CONTA_COORDINATE_ASSENTI + 1;

-- VEDO SE PER QUELLE ASSENTI C'E' UN SOLO CONTO PER IL QUALE CONSTRUISCO DINAMICAMENTE L'UPDATE

              Select Count(Distinct NUMERO_CONTO)
              Into   CONTA_COORDINATE_UNICHE
              From   BANCA
              Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
                     ABI          = CUR_IBAN.ABI And
                     CAB          = CUR_IBAN.CAB And
                     FL_CANCELLATO = 'N';

              If CONTA_COORDINATE_UNICHE = 1 Then

                TOT_COORDINATE_UNICHE := TOT_COORDINATE_UNICHE + 1;

                Select Distinct NUMERO_CONTO
                Into   CONTO_ATTUALE
                From   BANCA
                Where  CD_TERZO     = CUR_IBAN.CD_TERZO And
                       ABI          = CUR_IBAN.ABI And
                       CAB          = CUR_IBAN.CAB And
                       FL_CANCELLATO = 'N';

Dbms_Output.PUT_LINE ('UPDATE BANCA SET NUMERO_CONTO = '''||CUR_IBAN.CONTO||''' WHERE NUMERO_CONTO = '''||CONTO_ATTUALE||''' AND CD_TERZO = '''||Ltrim(Rtrim(CUR_IBAN.CD_TERZO))||''' AND ABI = '''||CUR_IBAN.ABI||''' AND CAB = '''||CUR_IBAN.CAB||''';');

              Elsif CONTA_COORDINATE_UNICHE > 1 Then

Dbms_Output.PUT_LINE ('COORDINATE MULTIPLE PER TERZO '||CUR_IBAN.CD_TERZO||' ABI '||CUR_IBAN.ABI||' CAB '||CUR_IBAN.CAB);

                 TOT_COORDINATE_MULTIPLE := TOT_COORDINATE_MULTIPLE + 1;

              Elsif CONTA_COORDINATE_UNICHE = 0 Then
                      TOT_CONTA_ASSENTI_ALTRO := TOT_CONTA_ASSENTI_ALTRO + 1;

              End If;

       End If;

--     End If; -- DEL CANCELLATO

     Else  -- LUNGHEZZA IBAN != 27
        CONTA_ERR_IBAN := CONTA_ERR_IBAN + 1;
     End If;

   End Loop; -- SU RITORNO_IBAN_ELAB


   For CUR_IBAN_DA_ANNULLARE in (select   Distinct CD_PAESE, CHECK_DIG, CIN, ABI, CAB, CONTO, CD_TERZO
                                 from     RITORNO_IBAN_ELAB
                                 where    DT_CARICO = INDT_CARICO And
                                          PROG_INVIO = INPROG_INVIO And
                                          CD_ESITO In ('001', '003')) Loop

      CONTA_001_003 := CONTA_001_003 + 1;

      Select Count(*)
      Into   CONTA_CANCELLATI
      From   BANCA
      Where  CD_TERZO     = CUR_IBAN_DA_ANNULLARE.CD_TERZO And
             ABI          = CUR_IBAN_DA_ANNULLARE.ABI And
             CAB          = CUR_IBAN_DA_ANNULLARE.CAB And
             FL_CANCELLATO = 'N' And
             Instr(Rtrim(Ltrim(Replace(Replace(CUR_IBAN_DA_ANNULLARE.CONTO, ' '), '/'), '0'), '0'), Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0;

      If CONTA_CANCELLATI > 0 Then

         TOT_CONTA_CANCELLATI := TOT_CONTA_CANCELLATI + CONTA_CANCELLATI;

         Update BANCA
         Set    FL_CANCELLATO = 'Y',
                DUVA = Trunc(Sysdate),
                UTUV = 'IBAN_SI'
         Where  CD_TERZO     = CUR_IBAN_DA_ANNULLARE.CD_TERZO And
                ABI          = CUR_IBAN_DA_ANNULLARE.ABI And
                CAB          = CUR_IBAN_DA_ANNULLARE.CAB And
                Instr(Rtrim(Ltrim(Replace(Replace(CUR_IBAN_DA_ANNULLARE.CONTO, ' '), '/'), '0'), '0'), Rtrim(Ltrim(Replace(Replace(NUMERO_CONTO, ' '), '/'), '0'), '0')) > 0;

         Update RITORNO_IBAN_ELAB
         Set    STATO = 'A',
                CAUSA_ERRORE = 'COORDINATE CANCELLATE LOGICAMENTE SU SIGLA'
         Where  CD_PAESE   = CUR_IBAN_DA_ANNULLARE.CD_PAESE And
                CHECK_DIG  = CUR_IBAN_DA_ANNULLARE.CHECK_DIG And
                CIN        = CUR_IBAN_DA_ANNULLARE.CIN And
                ABI        = CUR_IBAN_DA_ANNULLARE.ABI And
                CAB        = CUR_IBAN_DA_ANNULLARE.CAB And
                CONTO      = CUR_IBAN_DA_ANNULLARE.CONTO And
                CD_TERZO   = CUR_IBAN_DA_ANNULLARE.CD_TERZO And
                DT_CARICO  = INDT_CARICO And
                PROG_INVIO = INPROG_INVIO And
                CD_ESITO In ('001', '003');

      End If;

   End Loop;

End If; -- DATA CARICO VALORIZZATA

Dbms_Output.PUT_LINE ('COORDINATE COMPLETE DI IBAN FORNITE DALLA BNL NEL FLUSSO DEL '||To_Date(INDT_CARICO, 'YYYYMMDD')||' NON ANCORA ASSEGNATE AI CONTI SIGLA: '||ANAGRAFICHE_IBAN);
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('IBAN PASSATI DA BNL FORMALMENTE NON VALIDI '||CONTA_ERR_IBAN);
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('SULLE '||ANAGRAFICHE_IBAN||' ANAGRAFICHE IBAN ELABORATE N. '||CONTA_COORDINATE_ASSENTI||' NON CORRISPONDONO A TERZO/ABI/CAB/CONTO :');
Dbms_Output.PUT_LINE ('    - PER COORDINDATE GIA'' ANNULLATE SU SIGLA '||TOT_CONTA_COORDINATE_FL_CANC);
Dbms_Output.PUT_LINE ('    - PER COORDINDATE NON TROVATE (ABI/CAB/CONTO) '||TOT_CONTA_ASSENTI_ALTRO);
Dbms_Output.PUT_LINE ('    - PER ABI/CAB DIVERSI MA CONTO UGUALE '||TOT_ABI_CAB_DIVERSI);
Dbms_Output.PUT_LINE ('    - PER ABI/CAB UGUALI E CONTO DIVERSO SU CUI IL TERZO POSSIEDE UN SOLO CONTO '||TOT_COORDINATE_UNICHE);
Dbms_Output.PUT_LINE ('    - PER ABI/CAB UGUALI E CONTO DIVERSO SU CUI IL TERZO POSSIEDE PIU'' CONTI '||TOT_COORDINATE_MULTIPLE);
Dbms_Output.PUT_LINE ('    - COORDINATE TROVATE: '||CONTA_COORDINATE_PRESENTI||' (ANCHE DUPLICATE)');
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('DELLE '||CONTA_COORDINATE_PRESENTI||' COORDINATE TROVATE:');
Dbms_Output.PUT_LINE ('    - IBAN ASSOCIATI (PERCHE'' NULLI O ERRATI) '||CONTA_IBAN);
Dbms_Output.PUT_LINE ('    - IBAN GIA'' PRESENTI IN SIGLA '||CONTA_IBAN_GIA_CORRETTI);
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('    - CIN  ASSOCIATI (PERCHE'' NULLI O ERRATI) '||CONTA_CIN);
Dbms_Output.PUT_LINE ('    - CIN GIA'' PRESENTI IN SIGLA '||CONTA_CIN_GIA_CORRETTI);
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('CANCELLATI LOGICAMENTE '||TOT_CONTA_CANCELLATI||' SU '||CONTA_001_003);

-- COMMIT;

End;
/


