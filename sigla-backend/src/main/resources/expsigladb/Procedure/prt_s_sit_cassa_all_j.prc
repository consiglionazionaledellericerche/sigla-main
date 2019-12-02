CREATE OR REPLACE PROCEDURE PRT_S_SIT_CASSA_ALL_J
--
-- Date: 22/03/2005
-- Version: 1.3
--
-- Vista di stampa Situazione di Cassa Emesso/Inviato/Riscontrato
--
-- History:
--
-- Date: 28/07/2004
-- Version: 1.0
-- Creazione
--
-- Date: 06/08/2004
-- Version: 1.1
-- Rinominati il nome del package e della procedure che lo richiama
--
-- Date: 11/11/2004
-- Version: 1.2
-- sostituita "To_date(to_date" che a Crystal non piaceva
--
-- Date: 22/03/2005
-- Version: 1.3
-- nel calcolo del fondo di cassa non vengono pi? considerate le reversali di trasferimento provvisorie
--
-- Body
--
(inEs           in number,
 CDS            in varchar2,
 uo             in varchar2,
 EM_INV_RIS     in varchar2,  -- E/I/R (E = Emesso, I = INVIATO, R = Riscontrato)
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

 TOT_SOS_E   NUMBER;
 TOT_SOS_S   NUMBER;

 TOT_SOS_PAG               NUMBER;
 TOT_SOS_RIS               NUMBER;

 PERIODO        VARCHAR2(100);
 GIORNO         VARCHAR2(100);

 --GId         NUMBER;
 LP     NUMBER;

 CDS_PCK         VARCHAR2(100);
 UO_PCK          VARCHAR2(100);
 EM_INV_RIS_PCK  VARCHAR2(100);
 CDS_ENTE        VARCHAR2(30);
 parametri_esercizio  parametri_cnr%rowtype;
Begin

 -- 12.03.2008 PERIODO (TESTO RELATIVO AD INTERVALLO TRA DATE)
 If da_data = a_data Then
  periodo := 'il '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy');
  GIORNO  := 'giorno';
 Else
  periodo := 'dal '||To_Char(to_date(da_data, 'yyyy/mm/dd'), 'dd/mm/yyyy')||' al '||To_Char(to_date(a_data, 'yyyy/mm/dd'), 'dd/mm/yyyy');
  GIORNO  := 'periodo';
 End If;

 CDS_ENTE:=CNRCTB020.GETCDCDSENTE (inEs);

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

 If EM_INV_RIS = '*' THEN
        EM_INV_RIS_PCK := NULL;
 Else
        EM_INV_RIS_PCK := EM_INV_RIS;
 End IF;

 If CDS_PCK = CDS_ENTE And GId Is Null Then
   For Rec In (Select CD_UNITA_ORGANIZZATIVA
                 From UNITA_ORGANIZZATIVA
                  Where FL_CDS = 'Y'
                  Order By 1) Loop
     Insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
     VALUES (aId, 'chiave', 'C', '1'||Rec.CD_UNITA_ORGANIZZATIVA||'001', 1,
     Rec.CD_UNITA_ORGANIZZATIVA, 'CDS: '||Rec.CD_UNITA_ORGANIZZATIVA, null, Null, 'S');
     PRT_S_SIT_CASSA_ALL_J(inEs, Rec.CD_UNITA_ORGANIZZATIVA, uo, EM_INV_RIS, DA_DATA, A_DATA , aId);
   End Loop;
   Return;
 End If;
  parametri_esercizio:=CNRUTL001.getRecParametriCnr(inEs);
if (parametri_esercizio.fl_tesoreria_unica='N') then
	FCI := PRT_CIR_sitcas.fondo_iniziale (ines, cds_PCK, NULL);
else
	if  CDS_PCK = CDS_ENTE then
		FCI := PRT_CIR_sitcas.fondo_iniziale (ines, cds_PCK, NULL);
	else
  		FCI:=null;
  end if;
end if;

IF EM_INV_RIS = 'R' THEN
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
ELSIF EM_INV_RIS != 'R' THEN
 RT   := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A',  to_date(da_data, 'yyyy/mm/dd'), to_date(A_DATA, 'yyyy/mm/dd'), EM_INV_RIS_PCK, 'D', NULL);
 RI   := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I',  to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, null, NULL);
 RR   := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'R',  to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, null, NULL);
 RS   := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S',  to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, 'D', NULL);
 RSP  := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S',  to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, 'P', NULL);
 RTP  := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A',  to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, 'P', NULL);
 TR   := PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, null, to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, 'D', NULL);
 MA   := PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'A',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, NULL);
 MP   := PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'P',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, NULL);
 MR   := PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'R',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, NULL);
 MS   := PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S',    to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, NULL);
 TM   := PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, null,   to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), EM_INV_RIS_PCK, NULL);
END IF;

TOT_SOS_E   := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'E', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'N', NULL);
TOT_SOS_S   := PRT_CIR_sitcas.TOT_SOSPESI_E_S (INES, CDS_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'N', NULL);

TOT_SOS_PAG               := PRT_CIR_sitcas.TOT_SOSPESI_PAGATI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),
                             To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null, Null, Null);

TOT_SOS_RIS               := PRT_CIR_sitcas.TOT_SOSPESI_RISCOSSI (INES, CDS_PCK, To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),
                             To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), Null, Null, Null);

LP       := PRT_CIR_sitcas.TOT_LETTERE_PAGAMENTO_NON_ASS(INES, CDS_PCK, to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL);

-- inserimento dello schema di riclassificazione NELLA VIEW (FISSO)
if (FCI is not null) then
	Insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
	VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'008', 08, CDS_PCK,
	'Fondo di Cassa iniziale', null, FCI, 'S');
END IF;
if (parametri_esercizio.fl_tesoreria_unica='N') then
	insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
	VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'011', 11, CDS_PCK, 'Reversali di Trasferimento', RT, NULL);
end if;
insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'020', 20, CDS_PCK, 'Reversali di Incasso', RI, NULL);

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'030', 30, CDS_PCK, 'Reversali a regolamento di sospeso', RS, NULL);

IF EM_INV_RIS = 'E' THEN  -- ESCE SOLO PER L'EMESSO
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'040', 40, CDS_PCK, 'Reversali di Regolarizzazione', RR, NULL);
END IF;

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'045', 45, CDS_PCK, 'Totale Reversali', null, TR, 'S', '(+)');
if (parametri_esercizio.fl_tesoreria_unica='N') then
	insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
	VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'050', 50, CDS_PCK, 'Mandati di Accreditamento', MA, NULL);
end if;
insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'060', 60, CDS_PCK, 'Mandati di Pagamento', MP, NULL);

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'070', 70, CDS_PCK, 'Mandati a regolamento di sospeso', MS, NULL);

IF EM_INV_RIS = 'E' THEN  -- ESCE SOLO PER L'EMESSO
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'080', 80, CDS_PCK, 'Mandati di Regolarizzazione', MR, NULL);
END IF;

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'085', 85, CDS_PCK, 'Totale Mandati', null, TM, 'S', '(-)');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'086', 86, CDS_PCK, Null);


IF EM_INV_RIS = 'E' THEN  -- ESCE SOLO PER L'EMESSO
	if (parametri_esercizio.fl_tesoreria_unica='N') then
	 insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
	 VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'090', 90, CDS_PCK,
	 '* Reversali di Trasferimento Provvisorie (non contribuiscono al calcolo del Totale Reversali)',
	 RTP, NULL, 'C');

	 insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
	 VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'095', 95, CDS_PCK,
	 '* Reversali a regolamento di sospeso Provvisorie (non contribuiscono al calcolo del Totale Reversali)',
	 RSP, NULL, 'C');
 end if;
END IF;

-- SOSPESI DA REGOLARE (SIA DI ENTRATA CHE DI SPESA)

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'100', 100, CDS_PCK, 'Sospesi di Entrata emessi '||PERIODO||' da regolare con Reversale',
null, TOT_SOS_E - TOT_SOS_RIS, 'S', '(+)');

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'110', 110, CDS_PCK, 'Sospesi di Spesa emessi '||periodo||' da regolare con Mandato', null,
TOT_SOS_S - TOT_SOS_PAG, 'S', '(-)');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'115', 115, CDS_PCK, Null);


-- NON ESCE PER IL RISCONTRATO

IF EM_INV_RIS != 'R' THEN
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'120', 120, CDS_PCK, 'Lettere di Pagamento all''Estero da eseguire', null, LP,
  'S', '(-)');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'125', 125, CDS_PCK, Null);
if (FCI is not null) then
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'130', 130, CDS_PCK,
  'Situazione di Cassa Attuale', null,  NVL(FCI,0) + TR - TM - (TOT_SOS_S - TOT_SOS_PAG) +
  (TOT_SOS_E - TOT_SOS_RIS) - LP, 'S');
END IF;
ELSIF EM_INV_RIS = 'R' THEN

-- ESCE PER IL RISCONTRATO

-- REVERSALI E MANDATI DA TRASMETTERE
if (parametri_esercizio.fl_tesoreria_unica='N') then
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'140', 140, CDS_PCK,
  'Reversali di Trasferimento emesse '||periodo||' da trasmettere', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL)-
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL),
  'S', '(+)');
end if;
-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'145', 145, CDS_PCK, Null);


  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'150', 150, CDS_PCK,
  'Reversali a Regolamento di Sospesi emesse '||periodo||' ma non trasmesse (nello stesso '||giorno||')', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL)-
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S',
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo di emissione delle reversali
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo di emissione della distinta
                                Null, Null,
                                'D', null, NULL), 'S', '(+)');

  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'160', 160, CDS_PCK,
  'Mandati a Regolamento di Sospesi emessi '||periodo||' ma non trasmessi (nello stesso '||giorno||')', null,
  PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', NULL)-
  PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S',
                              To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo emissione mandati
                              to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo emissione distinte mandati
                              Null, Null,                                                    -- periodo invio distinte mandati
                              'D', NULL), 'S', '(-)');

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'161', 161, CDS_PCK,
  'Reversali di ritenute emesse '||periodo||' ma non trasmesse (nello stesso '||giorno||') collegate a mandati a Regolamento di Sospesi', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL,'S')-
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I',
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo di emissione delle reversali
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- periodo di emissione della distinta
                                Null, Null,
                                'D', null, NULL,'S'), 'S', '(+)');

-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'165', 165, CDS_PCK, Null);


-- REVERSALI DI TRASFERIMENTO DA ESEGUIRE
if (parametri_esercizio.fl_tesoreria_unica='N') then
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'170', 170, CDS_PCK,
  'Reversali di Trasferimento inserite in distinte emesse '||periodo||' ancora da eseguire', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL)-
  PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'A', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL),
  'S', '(+)');
end if;
-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'175', 175, CDS_PCK, Null);

-- REVERSALI A COPERTURA DI SOSPESO DA ESEGUIRE

  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'180', 180, CDS_PCK,
  'Reversali a Regolamento di Sospesi inserite in distinte emesse '||periodo||' ancora da eseguire', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'S',
                                Null, Null,
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),
                                Null, Null,
                                'D', null, NULL)-
  PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'S',
                                            to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),  -- PERIODO EMISSIONE DISTINTA
                                            to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),  -- PERIODO RISCONTRO
                                            NULL),
  'S', '(+)');

-- MANDATI A COPERTURA DI SOSPESO DA ESEGUIRE

  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'190', 190, CDS_PCK,
  'Mandati a Regolamento di Sospesi inseriti in distinte emesse '||periodo||' ancora da eseguire', null,
  PRT_CIR_sitcas.TOT_MANDATI (ines, CDS_PCK, UO_PCK, 'S',
                              Null, Null, -- PERIODO EMISSIONE MANDATI
                              To_Date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- PERIODO EMISSIONE DISTINTE
                              Null, Null,  -- PERIODO INVIO DISTINTE
                              'D', NULL)-
  PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'S',
                                          to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- PERIODO EMISSIONE DISTINTE
                                          to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), -- PERIODO RISCONTRO MANDATI
                                          NULL),
  'S', '(-)');

insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'191', 191, CDS_PCK,
  'Reversali di ritenute inserite in distinte emesse '||periodo||' ancora da eseguire collegate a mandati a Regolamento di Sospesi', null,
  PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I',
                                Null, Null,
                                to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),
                                Null, Null,
                                'D', null, NULL,'S')-
  PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'I',
                                            to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),  -- PERIODO EMISSIONE DISTINTA
                                            to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'),  -- PERIODO RISCONTRO
                                            NULL,'S'),
  'S', '(+)');
-- riga vuota
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'195', 195, CDS_PCK, Null);


-- SITUAZIONE FINALE
if (FCI is not null) then
  insert into PRT_VPG_SIT_CASSA  (ID, CHIAVE, TIPO, SEQUENZA, ORDINE, CDS, DESCRIZIONE, IMPORTO_PARZ, TOTALE, FL_TOTALE, SEGNO)
  VALUES (aId, 'chiave', 't', '1'||CDS_PCK||'200', 200, CDS_PCK,
  'Situazione di Cassa a quadratura con Istituto Cassiere', null,
  NVL(FCI,0) + TR - TM + (TOT_SOS_E - TOT_SOS_RIS) - (TOT_SOS_S - TOT_SOS_PAG)
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
   PRT_CIR_sitcas.TOT_MANDATI_RISCONTRATI (ines, CDS_PCK, UO_PCK, 'S', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL))
 +(PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D', null, NULL,'S') -
   PRT_CIR_sitcas.TOT_REVERSALI_RISCONTRATE (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), NULL,'S'))
 +(PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'E', 'D', NULL,'S') -
   PRT_CIR_sitcas.TOT_REVERSALI (ines, CDS_PCK, UO_PCK, 'I', to_date(da_data, 'yyyy/mm/dd'), to_date(a_data, 'yyyy/mm/dd'), 'D',null, NULL,'S')),
  'S', NULL);
END IF;
End IF;

End;
/


