CREATE OR REPLACE Procedure INSERISCI_COORDINATE_IBAN As

ESISTE_GIA                      NUMBER := 0;
NEW_PG                          NUMBER := 0;
CONTA_INSERT                    NUMBER := 0;
ANAGRAFICHE_IBAN                NUMBER := 0;
TOT_ESISTENTI                   NUMBER := 0;
ESISTE_ABICAB                   NUMBER := 0;
ABICAB_INESISTENTI              NUMBER := 0;

Begin


-- PRELEVO I DATI DALLA TABELLA DEL FLUSSO ELABORATA E ASSEGNO L'IBAN ALLE COORDINATE DEI TERZI

For CUR_IBAN in (Select   Distinct CD_TERZO, ABI, CAB, CONTO, CD_PAESE, CHECK_DIG, CIN, DS_TERZO
                 From     RITORNO_IBAN_ELAB
                 Where    CD_ESITO In ('006', '007', '008') And
                          STATO Not In ('A', 'T', 'N')) Loop

--Dbms_Output.PUT_LINE ('RECORD DI '||CUR_IBAN.CD_TERZO||' '||CUR_IBAN.ABI||' '||CUR_IBAN.CAB||' '||CUR_IBAN.CONTO);

ANAGRAFICHE_IBAN := ANAGRAFICHE_IBAN + 1;

If Length(CUR_IBAN.CD_PAESE||CUR_IBAN.CHECK_DIG||CUR_IBAN.CIN||CUR_IBAN.ABI||CUR_IBAN.CAB||CUR_IBAN.CONTO) = 27 Then

-- CONTROLLA SE ESISTONO GIA'

      Select Count(*)
      Into   ESISTE_GIA
      From   BANCA
      Where  CD_TERZO               = CUR_IBAN.CD_TERZO And
             Nvl(ABI, 'x')          = CUR_IBAN.ABI And
             Nvl(CAB, 'x')          = CUR_IBAN.CAB And
             Nvl(NUMERO_CONTO, 'x') = CUR_IBAN.CONTO And
             FL_CANCELLATO          = 'N';

      If ESISTE_GIA > 0 Then

          Dbms_Output.PUT_LINE ('GIA'' ESISTE '||CUR_IBAN.CD_TERZO||' '||CUR_IBAN.ABI||' '||CUR_IBAN.CAB||' '||CUR_IBAN.CONTO);

          TOT_ESISTENTI := TOT_ESISTENTI + 1;

      Else

        Select Count(*)
        Into   ESISTE_ABICAB
        From   ABICAB
        Where  ABI = CUR_IBAN.ABI And
               CAB = CUR_IBAN.CAB;

        If ESISTE_ABICAB > 0 Then

              Select Max(PG_BANCA)+1
              Into   NEW_PG
              From   BANCA
              Where  CD_TERZO     = CUR_IBAN.CD_TERZO;

              Insert Into BANCA (CD_TERZO, PG_BANCA, CAB, ABI, DACR,
                                 INTESTAZIONE, NUMERO_CONTO, TI_PAGAMENTO, CODICE_IBAN,
                                 UTCR, DUVA, UTUV, PG_VER_REC, FL_CANCELLATO, ORIGINE, FL_CC_CDS, CIN)
              Values (CUR_IBAN.CD_TERZO, NEW_PG, CUR_IBAN.CAB, CUR_IBAN.ABI, Trunc(Sysdate),
                      CUR_IBAN.DS_TERZO, CUR_IBAN.CONTO, 'B', CUR_IBAN.CD_PAESE||CUR_IBAN.CHECK_DIG||CUR_IBAN.CIN||CUR_IBAN.ABI||CUR_IBAN.CAB||CUR_IBAN.CONTO,
                      'NEW_INS_SI_IBAN', Trunc(Sysdate), 'NEW_INS_SI_IBAN', 1, 'N', 'O', 'N', CUR_IBAN.CIN);

                         Update RITORNO_IBAN_ELAB
                         Set    STATO = 'N'
                         Where  CD_PAESE       = CUR_IBAN.CD_PAESE       AND
                                CHECK_DIG      = CUR_IBAN.CHECK_DIG      AND
                                CIN            = CUR_IBAN.CIN            AND
                                ABI            = CUR_IBAN.ABI            AND
                                CAB            = CUR_IBAN.CAB            AND
                                CONTO          = CUR_IBAN.CONTO          AND
                                CD_TERZO       = CUR_IBAN.CD_TERZO       AND
                                DS_TERZO       = CUR_IBAN.DS_TERZO;

                         CONTA_INSERT := CONTA_INSERT + 1;
        Else

           Dbms_Output.PUT_LINE ('ABI/CAB INESISTENTE '||CUR_IBAN.CD_TERZO||' '||CUR_IBAN.ABI||' '||CUR_IBAN.CAB||' '||CUR_IBAN.CONTO);

           ABICAB_INESISTENTI := ABICAB_INESISTENTI + 1;

        End If;

   End If;

End If; -- IBAN != 27

End Loop;

Dbms_Output.PUT_LINE ('COORDINATE COMPLETE DI IBAN DA INSERIRE: '||ANAGRAFICHE_IBAN);
Dbms_Output.PUT_LINE (' *************************************************************** ');
Dbms_Output.PUT_LINE ('    - INSERITE '||CONTA_INSERT);
Dbms_Output.PUT_LINE ('    - GIA'' ESISTENTI '||TOT_ESISTENTI);
Dbms_Output.PUT_LINE ('    - NON INSERITE PER ABI/CAB INESISTENTI '||ABICAB_INESISTENTI);

-- COMMIT;

End;
/


