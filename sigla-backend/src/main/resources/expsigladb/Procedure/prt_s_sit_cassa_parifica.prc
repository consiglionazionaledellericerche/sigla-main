CREATE OR REPLACE PROCEDURE PRT_S_SIT_CASSA_PARIFICA
--
-- Date: 18/03/2008
-- Version: 1.1
--
-- Vista di stampa Situazione di Cassa Riscontrato per parifica con BNL per DATA
--
-- History:
--
-- Date: 18/03/2008
-- Version: 1.0
-- Creazione
--
-- Body
--
(inEs           in number,
 CDS            in varchar2,
 uo             in varchar2,
 DA_DATA        IN varchar2,
 A_DATA         IN VARCHAR2,
 GId            In NUMBER) is

 aId    number;

 FCI    NUMBER;
 RT     NUMBER;
 RI     NUMBER;
 RR     NUMBER;
 RS     NUMBER;
 RSP    NUMBER;
 RTP    NUMBER;
 TR     NUMBER;

 MA     NUMBER;
 MP     NUMBER;
 MR     NUMBER;
 MS     NUMBER;
 TM     NUMBER;

 TOT_SOS_E              NUMBER;
 TOT_SOS_E_STORNATI     NUMBER;

 TOT_SOS_S              NUMBER;
 TOT_SOS_S_STORNATI     NUMBER;

 TOT_SOS_PAG                    NUMBER;

 TOT_SOS_S_TRASM_NEL_PERIODO    NUMBER;
 TOT_SOS_S_TRASM_OLTRE_PERIODO  NUMBER;
 TOT_SOS_S_ANCORA_DA_TRASM      NUMBER;

 TOT_SOS_RIS                    NUMBER;

 TOT_SOS_E_TRASM_NEL_PERIODO    NUMBER;
 TOT_SOS_E_TRASM_OLTRE_PERIODO  NUMBER;
 TOT_SOS_E_ANCORA_DA_TRASM      NUMBER;

 PERIODO        VARCHAR2(100);
 GIORNO         VARCHAR2(100);

 --GId         NUMBER;
 LP     NUMBER;

 CDS_PCK         VARCHAR2(100);
 UO_PCK          VARCHAR2(100);
 CDS_ENTE        VARCHAR2(30);

Begin

 -- 12.03.2008 PERIODO (TESTO RELATIVO AD INTERVALLO TRA DATE)
 If da_data = a_data Then
  periodo := 'il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy');
  GIORNO  := 'giorno';
 Else
  periodo := 'dal '||To_Char(to_date(da_data, 'yyyy/mm/dd'), 'dd/mm/yyyy')||' al '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy');
  GIORNO  := 'periodo';
 End If;

 Select CD_UNITA_ORGANIZZATIVA
 Into CDS_ENTE
 From UNITA_ORGANIZZATIVA
 Where CD_TIPO_UNITA = 'ENTE'
   And FL_CDS = 'Y';
 If GId Is Null Then
   select IBMSEQ00_CR_PACKAGE.nextval
   into aId
   from dual;
 Else
   aId := GId;
 End If;

 If CDS = '*' THEN
        CDS_PCK := NULL;
 Else
        CDS_PCK := CDS;
 End IF;

 If UO = '*' THEN
        UO_PCK := NULL;
 Else
        UO_PCK := UO;
 End IF;

 If CDS_PCK = CDS_ENTE And GId Is Null Then
   For Rec In (Select CD_UNITA_ORGANIZZATIVA
                 From UNITA_ORGANIZZATIVA
                  Where FL_CDS = 'Y'
                  Order By 1) Loop
     Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
     VALUES (aId, 'chiave', 'C', '1'||Rec.CD_UNITA_ORGANIZZATIVA||'001', 1,
     Rec.CD_UNITA_ORGANIZZATIVA, 'CDS: '||Rec.CD_UNITA_ORGANIZZATIVA, null, Null, 'S');
     PRT_S_SIT_CASSA_PARIFICA(inEs, Rec.CD_UNITA_ORGANIZZATIVA, uo, DA_DATA, A_DATA , aId);
   End Loop;
   Return;
 End If;

FCI := PRT_CIR_sitcas.fondo_iniziale (ines, cds_PCK, NULL);

RT   := PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(A_DATA, 'yyyy/mm/dd'), NULL);
RI   := PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
RR   := PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'R', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
RS   := PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
TR   := PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, null, to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
MA   := PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'A',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
MP   := PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'P',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
MR   := PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'R',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
MS   := PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'S',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);
TM   := PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, null,   to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);

-- SOSPESI DI SPESA

-- TUTTI I VALIDI

TOT_SOS_S      := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'N', NULL);

-- GLI STORNATI

TOT_SOS_S_STORNATI    := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'Y', NULL);

-- ASSOCIATI A MANDATI:

TOT_SOS_PAG    := PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null);

--  - TRASMESSI NEL PERIODO

TOT_SOS_S_TRASM_NEL_PERIODO := PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK,
                                                                  To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA EMIS SOSPESO
                                                                  Null, Null,                                                      -- DATA MANDATO
                                                                  To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA TRASM MANDATO
                                                                  Null);

--  - TRASMESSI OLTRE IL PERIODO

TOT_SOS_S_TRASM_OLTRE_PERIODO := PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK,
                                                                    To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   --  DATA EMIS SOSPESO
                                                                    Null, Null,                                                      -- DATA MANDATO
                                                                    To_Date(a_data, 'yyyy/mm/dd', Null)+1, to_date(ines||'/12/31', 'yyyy/mm/dd'), -- DATA TRASM MANDATO
                                                                    Null);

--  - ANCORA DA TRASMETTERE

TOT_SOS_S_ANCORA_DA_TRASM        := PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null) -
                                    PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK,
                                                                       To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA EMIS SOSPESO
                                                                       Null, Null,                                                      -- DATA MANDATO
                                                                       To_Date(ines||'/01/01', 'yyyy/mm/dd'), to_date(ines||'/12/31', 'yyyy/mm/dd'),   -- DATA TRASM MANDATO
                                                                       NULL);

-- SOSPESI DI ENTRATA

-- TUTTI I VALIDI

TOT_SOS_E      := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'E', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'N', NULL);

-- TUTTI GLI STORNATI

TOT_SOS_E_STORNATI      := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'E', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'Y', NULL);

-- ASSOCIATI A REVERSALI:

TOT_SOS_RIS    := PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null);

--  - TRASMESSE NEL PERIODO

TOT_SOS_E_TRASM_NEL_PERIODO      := PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK,
                                                                         To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA EMIS SOSPESO
                                                                         Null, Null,                                                      -- DATA REVERSALE
                                                                         To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA TRASM REVERSALE
                                                                         NULL);

--  - TRASMESSE OLTRE IL PERIODO

TOT_SOS_E_TRASM_OLTRE_PERIODO := PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK,
                                                                      To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   --  DATA EMIS SOSPESO
                                                                      Null, Null,                                                      -- DATA REVERSALE
                                                                      To_Date(a_data, 'yyyy/mm/dd', Null)+1, to_date(ines||'/12/31', 'yyyy/mm/dd'), -- DATA TRASM REVERSALE
                                                                      Null);

--  - ANCORA DA TRASMETTERE

TOT_SOS_E_ANCORA_DA_TRASM        := PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null) -
                                    PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK,
                                                                         To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),   -- DATA EMIS SOSPESO
                                                                         Null, Null,                                                      -- DATA REVERSALE
                                                                         To_Date(ines||'/01/01', 'yyyy/mm/dd'), to_date(ines||'/12/31', 'yyyy/mm/dd'),   -- DATA TRASM REVERSALE
                                                                         NULL);


-- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)

/*
Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'008', 08, CDS_PCK,
'Fondo di Cassa iniziale', null, FCI, 'S');
*/

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'011', 11, CDS_PCK, 'Reversali di Trasferimento', RT, NULL);

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'020', 20, CDS_PCK, 'Reversali di Incasso', RI, NULL);

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'030', 30, CDS_PCK, 'Reversali a regolamento di sospeso', RS, NULL);

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'045', 45, CDS_PCK, 'Totale Reversali', null, TR, 'S', '(+)');

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'050', 50, CDS_PCK, 'Mandati di Accreditamento', MA, NULL);

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'060', 60, CDS_PCK, 'Mandati di Pagamento', MP, NULL);

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'070', 70, CDS_PCK, 'Mandati a regolamento di sospeso', MS, NULL);

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'085', 85, CDS_PCK, 'Totale Mandati', null, TM, 'S', '(-)');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'090', 90, CDS_PCK, Null);


--------------------------------------------------------------------------------------------------------------------------------------------
---------------------------------------------------------- SOSPESI DI ENTRATA --------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------

-- SOSPESI DI ENTRATA DA REGOLARE

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'100', 100, CDS_PCK, 'Sospesi di Entrata emessi '||PERIODO||' da regolare con Reversale',
null, TOT_SOS_E - TOT_SOS_RIS, 'S', '(+)');

-- riga vuota
Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'110', 110, CDS_PCK, Null);


Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'120', 120, CDS_PCK,
'Sospesi di entrata emessi '||periodo||' regolarizzati con reversali :', Null, NULL, 'N', NULL);

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'140', 140, CDS_PCK,
'   - trasmesse nello stesso '||giorno, Null,
TOT_SOS_E_TRASM_NEL_PERIODO,
'S', '(+)');

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'145', 145, CDS_PCK,
'   - trasmesse oltre il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy'), Null,
TOT_SOS_E_TRASM_OLTRE_PERIODO,
'S', '(+)');

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'150', 150, CDS_PCK,
'   - ancora da trasmettere', Null,
TOT_SOS_E_ANCORA_DA_TRASM,
'S', '(+)');


-- riga vuota
Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'165', 165, CDS_PCK, Null);


--------------------------------------------------------------------------------------------------------------------------------------------
----------------------------------------------------------- SOSPESI DI SPESA ---------------------------------------------------------------
--------------------------------------------------------------------------------------------------------------------------------------------

-- SOSPESI DI SPESA DA REGOLARE

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'170', 170, CDS_PCK, 'Sospesi di spesa emessi '||PERIODO||' da regolare con Mandato',
null, TOT_SOS_S - TOT_SOS_PAG, 'S', '(-)');

-- riga vuota
Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'173', 173, CDS_PCK, Null);


Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'175', 175, CDS_PCK,
'Sospesi di spesa emessi '||periodo||' regolarizzati con mandati :', Null, NULL, 'N', NULL);

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'180', 180, CDS_PCK,
'   - trasmessi nello stesso '||giorno, Null,
TOT_SOS_S_TRASM_NEL_PERIODO,
'S', '(-)');

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'190', 190, CDS_PCK,
'   - trasmessi oltre il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy'), Null,
TOT_SOS_S_TRASM_OLTRE_PERIODO,
'S', '(+)');

insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'200', 200, CDS_PCK,
'   - ancora da trasmettere', Null,
TOT_SOS_S_ANCORA_DA_TRASM,
'S', '(+)');


-- riga vuota
Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'210', 210, CDS_PCK, Null);


-- SOSPESI DI ENTRATA STORNATI

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'215', 215, CDS_PCK,
  'Sospesi di Entrata emessi '||periodo||' stornati ', Null, Nvl(TOT_SOS_E_STORNATI, 0), 'S', '(+)');

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'220', 220, CDS_PCK,
  'Sospesi di Spesa emessi '||periodo||' stornati', Null, Nvl(TOT_SOS_S_STORNATI, 0), 'S', '(+)');




-- SITUAZIONE FINALE
/*
  insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'200', 200, CDS_PCK,
  'Situazione di Cassa a quadratura con Istituto Cassiere', null,
  FCI + TR - TM + (TOT_SOS_E - TOT_SOS_RIS) - (TOT_SOS_S - TOT_SOS_PAG)
+ (PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL)-
   PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL))
+ (PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL)-
   PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL))
- (PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', NULL)-
   PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', NULL))
+ (PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL)-
   PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL))
+ (PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL)-
   PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL))
- (PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', NULL)-
   PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL)),
  'S', NULL);
*/

-- NUOVO RIEPILOGO DEI SOSPESI
/*
-- riga vuota
  insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'210', 210, CDS_PCK, Null);
  insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'220', 220, CDS_PCK, Null);


-- Titolo

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, FL_TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'230', 230, CDS_PCK,
  '                    Situazione riepilogativa dei Sospesi emessi '||periodo||' ancora da trasmettere', 'S');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'235', 235, CDS_PCK, Null);


-- Titoletto

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, FL_TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'240', 240, CDS_PCK,
        'Sospesi di Entrata:', 'S');

-- da trasmettere

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'250', 250, CDS_PCK,
'   - Collegati a reversali emesse '||periodo||' da trasmettere', TOT_SOS_RIS_NEL_PERIODO);

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'260', 260, CDS_PCK,
'   - Collegati a reversali emesse oltre il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy')||' da trasmettere',
  TOT_SOS_RIS_oltre_PERIODO);

-- NUOVO RIEPILOGO DEI SOSPESI DI SPESA

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, FL_TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'270', 270, CDS_PCK,
        'Sospesi di Spesa:', 'S');

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'280', 280, CDS_PCK,
'   - Collegati a mandati emessi '||periodo||' da trasmettere', TOT_SOS_PAG_NEL_PERIODO);

Insert into PRT_VPG_SIT_CASSA_PARIFICA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, TOTALE)
Values (aId, 'chiave', 't', '1'||CDS_PCK||'290', 290, CDS_PCK,
'   - Collegati a mandati emessi oltre il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy')||' da trasmettere',
TOT_SOS_pag_oltre_PERIODO);
*/

End;
/


