CREATE OR REPLACE Procedure ELABORA_RITORNO_IBAN (INDT_CARICO In VARCHAR, INPROG_INVIO In NUMBER) As
  CONTA_DIP NUMBER;

Begin

-- DEVONO ESSERE PASSATI LA DATA FLUSSO (YYYYMMDD) E PROG FLUSSO

If INDT_CARICO Is Not Null And INPROG_INVIO Is Not Null Then

-- PRELEVO I DATI DALLA TABELLA DEL FLUSSO NON ELABORATA E LI SCARICO IN UNA TABELLA ELABORATA (CON I CAMPI SEPARATI)
-- TRAVASO SOLO IL RESCORD "12" DOVE CI SONO LE INFORMAZIONI UTILI

   For cur_12 in (select   *
                  from     ritorno_iban
                  where    DT_CARICO = INDT_CARICO And
                           PROG_INVIO = INPROG_INVIO And
                           substr(record, 2, 2) = '12' And
                           ELAB = 'N') loop


Dbms_Output.PUT_LINE ('INSERT INIZIALE CONTO '||Substr(cur_12.record, 80, 12));

      Insert Into ritorno_iban_elab
        (DT_CARICO, PROG_INVIO, tipo, numero, data_crea, data_crea_ori, cd_paese, check_dig, cin, abi, cab, conto, cd_terzo, stato)
      Values
       (INDT_CARICO,                     -- DATA CARICO
        INPROG_INVIO,                    -- PROGRESSIVO FLUSSO RITORNO
        Substr(cur_12.record, 2,  2),    -- tipo
        Substr(cur_12.record, 4,  7),    -- numero
        Substr(cur_12.record, 11, 6),    -- data_crea
        Substr(cur_12.record, 17, 6),    -- data_crea_ori
        Substr(cur_12.record, 44, 2),    -- cd_paese
        Substr(cur_12.record, 46, 2),    -- check_dig
        Substr(cur_12.record, 69, 1),    -- cin
        Substr(cur_12.record, 70, 5),    -- abi
        Substr(cur_12.record, 75, 5),    -- cab
        Substr(cur_12.record, 80, 12),   -- conto
        Substr(cur_12.record, 98, 16),
        'I');  -- cd_terzo


      Update ritorno_iban
      Set    ELAB = 'Y'
      Where  DT_CARICO = cur_12.DT_CARICO And
             PROG_INVIO = cur_12.PROG_INVIO And
             Record = cur_12.record;

   End Loop;

-- SULLA TABELLA ELABORATA (RECORD 12) ASSOCIO IL CODICE ESITO (DAL RECORD 70)

   For cur_70 in (select *
                  from   ritorno_iban
                  where  DT_CARICO = INDT_CARICO And
                         PROG_INVIO = INPROG_INVIO And
                         ELAB = 'N' And
                         Substr(record, 2, 2) = '70') Loop

      Update ritorno_iban_elab
      Set    cd_esito = substr(cur_70.record, 27, 3)
      Where  DT_CARICO = cur_70.DT_CARICO And
             PROG_INVIO = cur_70.PROG_INVIO And
             numero   = substr(cur_70.record, 4, 7);

      Update ritorno_iban
      Set    ELAB = 'Y'
      Where  DT_CARICO = cur_70.DT_CARICO And
             PROG_INVIO = cur_70.PROG_INVIO And
             Record = cur_70.record;

   End Loop;

-- RECUPERO DA TERZO LA DENOMINAZIONE DEL TERZO

Dbms_Output.PUT_LINE ('DENOMINAZIONE TERZO');

   Update ritorno_iban_elab
   Set    ds_terzo = (Select denominazione_sede From terzo Where cd_terzo = ritorno_iban_elab.cd_terzo)
   Where  DT_CARICO = INDT_CARICO And
          PROG_INVIO = INPROG_INVIO;

Dbms_Output.PUT_LINE ('DS ESITO');

   Update ritorno_iban_elab
   Set    ds_ESITO = Decode(CD_ESITO,
                            '001', 'Errore - Cliente sconosciuto',
                            '002', 'Errore - Conto intestato ad altro soggetto',
                            '003', 'Errore - Conto Chiuso/estinto',
                            '004', 'Errore - N. Conto/IBAN inesistente',
                            '006', 'IBAN Trasmesso - Codice fiscale coerente',
                            '007', 'IBAN Trasmesso - Codice fiscale non coerente',
                            '008', 'IBAN Trasmesso - Codice fiscale non controllato',
                            '009', 'Errore - Comunicazione non autorizzata dal cliente')
    Where DT_CARICO = INDT_CARICO And
          PROG_INVIO = INPROG_INVIO;

   For TUTTI_TERZI In (Select Distinct CD_TERZO
                       From   ritorno_iban_elab
                       Where  DT_CARICO = INDT_CARICO And
                              PROG_INVIO = INPROG_INVIO) Loop

   Select Count(*)
   Into   CONTA_DIP
   From   RAPPORTO
   Where  CD_TIPO_RAPPORTO = 'DIP' And
          CD_ANAG = (Select CD_ANAG
                     From   TERZO
                     Where  cd_terzo = TUTTI_TERZI.CD_TERZO);

   If CONTA_DIP > 0 Then
     Update ritorno_iban_elab
     Set    dipendente = 'Y'
     Where  CD_TERZO = TUTTI_TERZI.CD_TERZO And
            DT_CARICO = INDT_CARICO And
            PROG_INVIO = INPROG_INVIO;
   Else
     Update ritorno_iban_elab
     Set    dipendente = 'N'
     Where  CD_TERZO = TUTTI_TERZI.CD_TERZO And
            DT_CARICO = INDT_CARICO And
            PROG_INVIO = INPROG_INVIO;
   End If;

   End Loop;

   Commit;

End If;

End;
/


