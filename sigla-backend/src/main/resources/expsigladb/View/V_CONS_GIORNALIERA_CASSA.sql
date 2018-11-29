--------------------------------------------------------
--  DDL for View V_CONS_GIORNALIERA_CASSA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_CONS_GIORNALIERA_CASSA" ("ESERCIZIO", "NOME_FILE", "PG_REC", "TR", "DATA_GIORNALIERA", "DATA_MOVIMENTO", "CD_CDS", "PG_MANDATO", "PG_REVERSALE", "CD_SOSPESO_E", "CD_SOSPESO_S", "IM_SOS_E_APERTI", "IM_SOS_E_STORNI", "IM_REV_SOSPESI", "IM_REVERSALI", "IM_REV_STORNI", "IM_SOS_S_APERTI", "IM_SOS_S_STORNI", "IM_MAN_SOSPESI", "IM_MANDATI", "IM_MAN_STORNI", "TOT_SBILANCIO") AS 
  Select ESERCIZIO,                                 -- ESERCIZIO
       NOME_FILE,                                 -- NOME_FILE
       PG_REC,                                    -- PG_REC
       TR,                                        -- TR
      (Select To_Date(Substr(DATA, 125, 8), 'YYYYMMDD')
       From  EXT_CASSIERE00 EXT_DATA1
       Where EXT_DATA1.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
             EXT_DATA1.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
             EXT_DATA1.TR = '01' And
             EXT_DATA1.PG_REC = (Select Min(PG_REC)
                                 From   EXT_CASSIERE00 EXT_DATA
                                 Where  EXT_DATA.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                                        EXT_DATA.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                                        EXT_DATA.TR = '01')) DATA_GIORNALIERA, -- DATA_GIORNALIERA
       To_Date(Substr(DATA, 108, 8) ,'YYYYMMDD') DATA_MOVIMENTO,               -- DATA_MOVIMENTO
(Select CD_CDS
 From   EXT_CASSIERE_CDS
 Where  esercizio    = EXT_CASSIERE00.ESERCIZIO And
        codice_proto = (Select Substr(EXT2.DATA, 5, 4)
                        From   EXT_CASSIERE00 EXT2
                        Where  EXT2.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                               EXT2.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                               EXT2.TR     = '01'                        And
                               EXT2.PG_REC = (Select Max(PG_REC)
                                              From   EXT_CASSIERE00 EXT3
                                              Where  EXT3.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                                                     EXT3.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                                                     EXT3.TR = '01' And
                                                     EXT3.PG_REC < EXT_CASSIERE00.PG_REC))) CD_CDS,  -- CD_CDS
       Null PG_MANDATO    ,                                      -- PG_MANDATO
       Null PG_REVERSALE  ,                                      -- PG_REVERSALE
       Decode(Substr(DATA, 21, 1), 'E', Substr(DATA, 3, 18)) CD_SOSPESO_E,  -- CD_SOSPESO_E
       Decode(Substr(DATA, 21, 1), 'U', Substr(DATA, 3, 18)) CD_SOSPESO_S,  -- CD_SOSPESO_S
       Decode(Substr(DATA, 21, 1), 'E', Decode(Substr(DATA, 32, 2), '01', To_Number(Substr(DATA, 92, 15))/100)) IM_SOS_E_APERTI,   -- IM_SOS_E_APERTI
       Decode(Substr(DATA, 21, 1), 'E', Decode(Substr(DATA, 32, 2), '05', -1*To_Number(Substr(DATA, 92, 15))/100)) IM_SOS_E_STORNI,-- IM_SOS_E_STORNI
       To_Number(Null)  IM_REV_SOSPESI,                                      -- IM_REV_SOSPESI
       To_Number(Null)  IM_REVERSALI  ,                                      -- IM_REVERSALI
       To_Number(Null)  IM_REV_STORNI ,                                      -- IM_REV_STORNI
       Decode(Substr(DATA, 21, 1), 'U', Decode(Substr(DATA, 32, 2), '01', To_Number(Substr(DATA, 92, 15))/100)) IM_SOS_S_APERTI,    -- IM_SOS_S_APERTI
       Decode(Substr(DATA, 21, 1), 'U', Decode(Substr(DATA, 32, 2), '05', -1*To_Number(Substr(DATA, 92, 15))/100)) IM_SOS_S_STORNI, -- IM_SOS_S_STORNI
       To_Number(Null) IM_MAN_SOSPESI  ,                                      -- IM_MANDATI A COPERTURA DI SOSPESI
       To_Number(Null) IM_MANDATI      ,                                      -- IM_MANDATI
       To_Number(Null) IM_MAN_STORNI                                          -- IM_MAN_STORNI
       ,0
From   EXT_CASSIERE00
Where  TR = '32' And
       Substr(DATA, 32, 2) In ('01', '05')
Union
-- MANDATI/REVERSALI RISCONTRI/STORNI
Select ESERCIZIO,                                 -- ESERCIZIO
       NOME_FILE,                                 -- NOME_FILE
       PG_REC,                                    -- PG_REC
       TR,                                        -- TR
      (Select To_Date(Substr(DATA, 125, 8), 'YYYYMMDD')
       From  EXT_CASSIERE00 EXT_DATA1
       Where EXT_DATA1.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
             EXT_DATA1.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
             EXT_DATA1.TR = '01' And
             EXT_DATA1.PG_REC = (Select Min(PG_REC)
                                 From   EXT_CASSIERE00 EXT_DATA
                                 Where  EXT_DATA.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                                        EXT_DATA.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                                        EXT_DATA.TR = '01')) DATA_GIORNALIERA, -- DATA_GIORNALIERA
       To_Date(Substr(DATA, 120, 8) ,'YYYYMMDD') DATA_MOVIMENTO,    -- DATA_MOVIMENTO
(Select CD_CDS
 From   EXT_CASSIERE_CDS
 Where  esercizio    = EXT_CASSIERE00.ESERCIZIO And
        codice_proto = (Select Substr(EXT2.DATA, 5, 4)
                        From   EXT_CASSIERE00 EXT2
                        Where  EXT2.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                               EXT2.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                               EXT2.TR     = '01'                        And
                               EXT2.PG_REC = (Select Max(PG_REC)
                                              From   EXT_CASSIERE00 EXT3
                                              Where  EXT3.ESERCIZIO = EXT_CASSIERE00.ESERCIZIO And
                                                     EXT3.NOME_FILE = EXT_CASSIERE00.NOME_FILE And
                                                     EXT3.TR = '01' And
                                                     EXT3.PG_REC < EXT_CASSIERE00.PG_REC))) CD_CDS,  -- CD_CDS
       Decode(Substr(DATA, 19, 1), 'M', Ltrim(Substr(DATA, 4, 13), '0')) PG_MANDATO,          -- PG_MANDATO
       Decode(Substr(DATA, 19, 1), 'R', Ltrim(Substr(DATA, 4, 13), '0')) PG_REVERSALE,        -- PG_REVERSALE
       Decode(Substr(DATA, 19, 1), 'R', Ltrim(Rtrim(Substr(DATA, 308, 18))) ) CD_SOSPESO_E,   -- CD_SOSPESO_E
       Decode(Substr(DATA, 19, 1), 'M', Ltrim(Rtrim(Substr(DATA, 308, 18))) ) CD_SOSPESO_S,   -- CD_SOSPESO_S
       To_Number(Null)  IM_SOS_E_APERTI,   -- IM_SOS_E_APERTI
       To_Number(Null)  IM_SOS_E_STORNI,   -- IM_SOS_E_STORNI
       -- SE E' 'R' (REVERSALE) ED IL CODICE SOSPESO E' PIENO PRENDO IL VALORE SENNO' NIENTE
       To_Number(Decode(Substr(DATA, 19, 1),
                        'R', Decode(Rtrim(Ltrim(Substr(DATA, 308, 18))), -- CODICE SOSPESO
                                    Null,
                                    Null,
                                    To_Number(Substr(DATA, 128, 15))/100  -- IMPORTO CON DECIMALI
                                   )
                        )
                 ) IM_REV_SOSPESI, -- IM_REV_SOSPESI
       -- SE E' 'R' (REVERSALE) ED IL CODICE SOSPESO E' VUOTO PRENDO IL VALORE SENNO' NIENTE
       To_Number(Decode(Substr(DATA, 19, 1),
                        'R', Decode(Rtrim(Ltrim(Substr(DATA, 308, 18))), -- CODICE SOSPESO
                                    Null,
                                    To_Number(Substr(DATA, 128, 15))/100, -- IMPORTO CON DECIMALI
                                    Null
                                   )
                        )
                 ) IM_REVERSALI,   -- IM_REVERSALI
       To_Number(Null) IM_REV_STORNI ,    -- IM_REV_STORNI
       To_Number(Null) IM_SOS_S_APERTI ,  -- IM_SOS_S_APERTI
       To_Number(Null) IM_SOS_S_STORNI ,  -- IM_SOS_S_STORNI
       To_Number(Decode(Substr(DATA, 19, 1),
                        'M', Decode(Rtrim(Ltrim(Substr(DATA, 308, 18))), -- CODICE SOSPESO
                                    Null,
                                    Null,
                                    To_Number(Substr(DATA, 128, 15))/100  -- IMPORTO CON DECIMALI
                                    )
                        )
                 ) IM_MAN_SOSPESI,    -- IM_MANDATI A COPERTURA DI SOSPESI
       To_Number(Decode(Substr(DATA, 19, 1),
                        'M', Decode(Rtrim(Ltrim(Substr(DATA, 308, 18))),  -- CODICE SOSPESO
                                    Null,
                                    To_Number(Substr(DATA, 128, 15))/100, -- IMPORTO CON DECIMALI
                                    Null
                                    )
                        )
                 ) IM_MANDATI,   -- IM_MANDATI
       To_Number(Null) IM_MAN_STORNI     -- IM_MAN_STORNI
         ,0
From   EXT_CASSIERE00
Where  TR = '30'
UNION
 Select MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO,                                 -- ESERCIZIO
        MOVIMENTO_CONTO_EVIDENZA.IDENTIFICATIVO_FLUSSO,                     -- NOME_FILE
        PROGRESSIVO,                                    -- PG_REC
        '32',                                        -- TR
        DATA_INIZIO_PERIODO_RIF, -- DATA_GIORNALIERA
        DATA_MOVIMENTO,               -- DATA_MOVIMENTO
(Select CD_CDS
 From   EXT_CASSIERE_CDS
 Where  esercizio    = MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO And
        CODICE_ENTE_BT like '%'||codice_proto) CD_CDS,  -- CD_CDS
       Null PG_MANDATO    ,                                      -- PG_MANDATO
       Null PG_REVERSALE  ,                                      -- PG_REVERSALE
       to_char(Decode( tipo_movimento,'ENTRATA',lpad(NUMERO_DOCUMENTO,18,'0'),NULL))CD_SOSPESO_E,  -- CD_SOSPESO_E
       to_char(Decode(tipo_movimento, 'USCITA', lpad(NUMERO_DOCUMENTO,18,'0'),NULL)) CD_SOSPESO_S,  -- CD_SOSPESO_S
       Decode(tipo_movimento,'ENTRATA', Decode(TIPO_OPERAZIONE, 'ESEGUITO', IMPORTO,0),0) IM_SOS_E_APERTI,   -- IM_SOS_E_APERTI
       Decode(tipo_movimento,'ENTRATA', Decode(TIPO_OPERAZIONE, 'STORNATO', IMPORTO,0),0) IM_SOS_E_STORNI,-- IM_SOS_E_STORNI
       To_Number(Null)  IM_REV_SOSPESI,                                      -- IM_REV_SOSPESI
       To_Number(Null)  IM_REVERSALI  ,                                      -- IM_REVERSALI
       To_Number(Null)  IM_REV_STORNI ,                                      -- IM_REV_STORNI
       Decode(tipo_movimento, 'USCITA', Decode(TIPO_OPERAZIONE, 'ESEGUITO',IMPORTO,0),0) IM_SOS_S_APERTI,    -- IM_SOS_S_APERTI
       Decode(tipo_movimento, 'USCITA', Decode(TIPO_OPERAZIONE, 'STORNATO',IMPORTO,0),0) IM_SOS_S_STORNI, -- IM_SOS_S_STORNI
       To_Number(Null) IM_MAN_SOSPESI  ,                                      -- IM_MANDATI A COPERTURA DI SOSPESI
       To_Number(Null) IM_MANDATI      ,                                      -- IM_MANDATI
       To_Number(Null) IM_MAN_STORNI                                          -- IM_MAN_STORNI
       ,0
From   MOVIMENTO_CONTO_EVIDENZA,FLUSSO_GIORNALE_DI_CASSA
Where   tipo_documento   LIKE  'SOSPESO%' And
       TIPO_OPERAZIONE In ('ESEGUITO', 'STORNATO') AND
       MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO = FLUSSO_GIORNALE_DI_CASSA.ESERCIZIO   AND
       MOVIMENTO_CONTO_EVIDENZA.IDENTIFICATIVO_FLUSSO = FLUSSO_GIORNALE_DI_CASSA.IDENTIFICATIVO_FLUSSO
Union
-- MANDATI/REVERSALI RISCONTRI/STORNI
Select MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO,                                 -- ESERCIZIO
       MOVIMENTO_CONTO_EVIDENZA.IDENTIFICATIVO_FLUSSO,                                 -- NOME_FILE
       PROGRESSIVO,                                    -- PG_REC
       '30',                                        -- TR
       DATA_INIZIO_PERIODO_RIF, -- DATA_GIORNALIERA
       DATA_MOVIMENTO,               -- DATA_MOVIMENTO
			(Select CD_CDS
			 From   EXT_CASSIERE_CDS
			 Where  esercizio    = MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO And
			        CODICE_ENTE_BT like '%'||codice_proto) CD_CDS,  -- CD_CDS
       to_char(Decode(tipo_documento, 'MANDATO', NUMERO_DOCUMENTO)) PG_MANDATO,          -- PG_MANDATO
       to_char(Decode(tipo_documento, 'REVERSALE',NUMERO_DOCUMENTO)) PG_REVERSALE,        -- PG_REVERSALE
       to_char(Decode(tipo_documento, 'REVERSALE', lpad(numero_SOSPESO,18,'0'))) CD_SOSPESO_E,   -- CD_SOSPESO_E
       to_char(Decode(tipo_documento, 'MANDATO', lpad(numero_SOSPESO,18,'0'))) CD_SOSPESO_S,   -- CD_SOSPESO_S
       To_Number(Null)  IM_SOS_E_APERTI,   -- IM_SOS_E_APERTI
       To_Number(Null)  IM_SOS_E_STORNI,   -- IM_SOS_E_STORNI
       -- SE E' 'R' (REVERSALE) ED IL CODICE SOSPESO E' PIENO PRENDO IL VALORE SENNO' NIENTE
       TO_NUMBER(Decode(tipo_documento, 'REVERSALE',
                         Decode(NUMERO_SOSPESO, -- CODICE SOSPESO
                                    Null,
                                    Null,
                                    IMPORTO  -- IMPORTO CON DECIMALI
                                   )
                        )
                 ) IM_REV_SOSPESI, -- IM_REV_SOSPESI
       -- SE E' 'R' (REVERSALE) ED IL CODICE SOSPESO E' VUOTO PRENDO IL VALORE SENNO' NIENTE
       To_Number(Decode(tipo_documento, 'REVERSALE',
                        	 Decode(NUMERO_SOSPESO, -- CODICE SOSPESO
                                    Null,
                                    IMPORTO, -- IMPORTO CON DECIMALI
                                    Null
                                   )
                        )
                 ) IM_REVERSALI,   -- IM_REVERSALI
       To_Number(Null) IM_REV_STORNI ,    -- IM_REV_STORNI
       To_Number(Null) IM_SOS_S_APERTI ,  -- IM_SOS_S_APERTI
       To_Number(Null) IM_SOS_S_STORNI ,  -- IM_SOS_S_STORNI
       To_Number(Decode(tipo_documento, 'MANDATO',
                        Decode(NUMERO_SOSPESO, -- CODICE SOSPESO
                                    Null,
                                    Null,
                                    IMPORTO  -- IMPORTO CON DECIMALI
                                    )
                        )
                 ) IM_MAN_SOSPESI,    -- IM_MANDATI A COPERTURA DI SOSPESI
       To_Number(Decode(tipo_documento, 'MANDATO',
                         Decode(NUMERO_SOSPESO,  -- CODICE SOSPESO
                                    Null,
                                    IMPORTO, -- IMPORTO CON DECIMALI
                                    Null
                                    )
                        )
                 ) IM_MANDATI,   -- IM_MANDATI
       To_Number(Null) IM_MAN_STORNI     -- IM_MAN_STORNI
         ,0
From   MOVIMENTO_CONTO_EVIDENZA,FLUSSO_GIORNALE_DI_CASSA
Where  tipo_documento IN('MANDATO','REVERSALE')  AND
			 MOVIMENTO_CONTO_EVIDENZA.ESERCIZIO = FLUSSO_GIORNALE_DI_CASSA.ESERCIZIO   AND
       MOVIMENTO_CONTO_EVIDENZA.IDENTIFICATIVO_FLUSSO = FLUSSO_GIORNALE_DI_CASSA.IDENTIFICATIVO_FLUSSO
/