CREATE OR REPLACE Procedure estrai_flusso_per_iban Is

-- filler a seconda delle dimensioni
filler1                  VARCHAR2(1)  := ' ';
filler5                  VARCHAR2(6)  := '     ';
filler6                  VARCHAR2(6)  := '      ';
filler7                  VARCHAR2(7)  := '       ';
filler10                 VARCHAR2(10) := '          ';
filler12                 VARCHAR2(12) := '            ';
filler15                 VARCHAR2(15) := '               ';
filler16                 VARCHAR2(16) := '                ';
filler20                 VARCHAR2(20) := '                    ';
filler25                 VARCHAR2(25) := '                         ';
filler30                 VARCHAR2(30) := '                              ';
filler70                 VARCHAR2(70) := '                                                                      ';

-- record di testa e di coda
tipo_record              VARCHAR2(2);
mittente                 VARCHAR2(5) := '92E58'; -- CODICE SIA
ricevente                VARCHAR2(5)   := '01005'; -- ABI BANCA DI ALLINEAMENTO
data_creazione           NUMBER(6)   := To_Char(Sysdate, 'DDMMYY');
Nome_supporto            VARCHAR2(20);
campo_a_disposizione     VARCHAR2(6);
numero_disposizioni      NUMBER(7);
numero_record            NUMBER(7);

-- RECORD 12

progressivo_disp         NUMBER(7) := 0;
causale                  NUMBER(5);
Codice_Paese	         VARCHAR2(2);
check_digit	         VARCHAR2(2);
CIN_terzo                CHAR(1);
abi_terzo                VARCHAR2(5);
cab_terzo                VARCHAR2(5);
conto_terzo              VARCHAR2(12 CHAR);
tipo_codice_individuale	 CHAR(1);
codice_individuale	 VARCHAR2(16 CHAR);

-- RECORD 30

stringa_descr_terzo      VARCHAR2(500);
cod_fis                  VARCHAR2(16 CHAR);
CD_ANAGRAFICO            NUMBER(8);
rec_anagrafico           anagrafico%Rowtype;

-- RECORD 70

codice_riferimento       VARCHAR2(15);
descrizione              VARCHAR2(75 CHAR);

indice                   NUMBER := 0;
riga_tabella             VARCHAR2(130 CHAR);

Begin

Nome_supporto := mittente||ricevente||To_Char(Sysdate, 'DDMMYYHH24MI');

-- record di testa

tipo_record := 'AL';
campo_a_disposizione := FILLER6;
riga_tabella := filler1||tipo_record||mittente||ricevente||data_creazione||Nome_supporto||campo_a_disposizione||filler70||filler5;

indice := indice + 1;
Insert Into flusso_invio_iban Values (indice, Riga_tabella);

For ANAG_BANCARIE In (Select BANCA.CD_TERZO, ABI, CAB, NUMERO_CONTO, PG_BANCA
                      From   BANCA, TERZO, ANAGRAFICO
                      Where  BANCA.CD_TERZO = TERZO.CD_TERZO And
                             TERZO.CD_ANAG = ANAGRAFICO.CD_ANAG And
                             ABI IS NOT NULL And
                             CAB IS NOT NULL And
                             NUMERO_CONTO IS NOT NULL And
                             LENGTH(NUMERO_CONTO) <= 12 And
                             FL_CANCELLATO = 'N' And
                             Exists (Select 1
                                     From   RAPPORTO
                                     Where  CD_ANAG = ANAGRAFICO.CD_ANAG And
                                            CD_TIPO_RAPPORTO = 'DIP')
                      Union
                      Select CD_TERZO, ABI, CAB, NUMERO_CONTO, PG_BANCA
                      From   banca
                      Where  ABI Is Not Null And
                             CAB Is Not Null And
                             NUMERO_CONTO Is Not Null And
                             LENGTH(NUMERO_CONTO) <= 12 And
                             fl_cancellato = 'N' And
                             (CD_TERZO, PG_BANCA) In
                             (Select CD_TERZO, PG_BANCA
                              From   mandato_riga
                              Where  esercizio >= 2007)) Loop

progressivo_disp := progressivo_disp + 1;

-- record 12

tipo_record := '12';
CAUSALE     := '90717';

tipo_codice_individuale := '5';
codice_individuale := ANAG_BANCARIE.CD_TERZO;

RIGA_TABELLA := FILLER1||tipo_record||Lpad(progressivo_disp, 7, '0')||filler6||FILLER12||CAUSALE||FILLER10||'  '||'  '||ricevente||
                FILLER16||' '||ANAG_BANCARIE.ABI||ANAG_BANCARIE.CAB||Rpad(ANAG_BANCARIE.NUMERO_CONTO, 12, ' ')||mittente||
                tipo_codice_individuale||Rpad(codice_individuale, 16, ' ')||FILLER7;

indice := indice + 1;
Insert Into flusso_invio_iban Values (indice, Riga_tabella);

-- record 30

tipo_record := '30';

Select denominazione_sede --||' '||via_sede||' '||NUMERO_CIVICO_SEDE||' '||DS_COMUNE
        , cd_anag
Into   stringa_descr_terzo, cd_anagrafico
From   terzo, comune
Where  cd_terzo = ANAG_BANCARIE.cd_terzo And
       terzo.pg_comune_sede = comune.pg_comune;

-- QUESTA ISTRUZIONE O RIEMPIE A DESTRA DI SPAZI FINO A 74 CARATTERI O SE ? PI? LUNGA TRONCA A 74
stringa_descr_terzo := Rpad(stringa_descr_terzo, 74, ' ');

Select *
Into   rec_anagrafico
From   anagrafico
Where  cd_anag = cd_anagrafico;

If rec_anagrafico.ti_entita = 'F' And Length(rec_anagrafico.CODICE_FISCALE) = 16 Then
  COD_FIS := rec_anagrafico.CODICE_FISCALE;
End If;

RIGA_TABELLA := FILLER1||tipo_record||Lpad(progressivo_disp, 7, '0')||Lpad(Substr(stringa_descr_terzo, 1, 30), 30, ' ')||
                Lpad(Substr(stringa_descr_terzo, 31, 30), 30, ' ')||Lpad(Substr(stringa_descr_terzo, 61, 14), 14, ' ')||Nvl(COD_FIS, '                ')||filler20;

indice := indice + 1;

Insert Into flusso_invio_iban Values (indice, Riga_tabella);


-- record 70

tipo_record := '70';

codice_riferimento := filler15;
descrizione  := Rpad('IBAN CD_TERZO '||ANAG_BANCARIE.cd_terzo||' PG_BANCA '||ANAG_BANCARIE.PG_BANCA||': ABI '||ANAG_BANCARIE.ABI||' CAB '||ANAG_BANCARIE.CAB||' C/C '||ANAG_BANCARIE.NUMERO_CONTO, 75, ' ');

RIGA_TABELLA := FILLER1||tipo_record||Lpad(progressivo_disp, 7, '0')||codice_riferimento||descrizione||FILLER20;

indice := indice + 1;
Insert Into flusso_invio_iban Values (indice, Riga_tabella);

End Loop;

-- record di coda

tipo_record := 'EF';

RIGA_TABELLA := FILLER1||tipo_record||mittente||ricevente||data_creazione||Nome_supporto||FILLER6||Lpad(progressivo_disp, 7, '0')||FILLER30||Lpad(INDICE+1, 7, '0')||
                FILLER25||filler6;

indice := indice + 1;
Insert Into flusso_invio_iban Values (indice, Riga_tabella);


Commit;

End;
/


