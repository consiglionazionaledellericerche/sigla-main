--------------------------------------------------------
--  DDL for View VP_PARTITARIO_COMPENSI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "VP_PARTITARIO_COMPENSI" ("CD_ANAG", "TI_RECORD", "COGNOME", "NOME", "DS_COMUNE_NASCITA", "CD_PV_NASCITA", "DT_NASCITA", "CODICE_FISCALE", "PARTITA_IVA", "TI_ITALIANO_ESTERO", "DS_NAZIONE", "VIA_FISCALE", "DS_COMUNE_FISCALE", "CD_PV_FISCALE", "CAP_COMUNE_FISCALE", "FRAZIONE_FISCALE", "CD_TERZO", "ALTRA_VIA_SEDE", "ALTRA_DS_COMUNE_SEDE", "ALTRA_CD_PV_SEDE", "ALTRA_CAP_COMUNE_SEDE", "DS_MODALITA_PAG", "TI_PAGAMENTO", "DS_ABICAB", "ABI", "CAB", "NUMERO_CONTO", "CIN", "INTESTAZIONE", "VIA_BANCA", "CAP_BANCA", "FRAZIONE_BANCA", "DS_COMUNE_BANCA", "CD_PV_BANCA", "CD_CDS_MINICARRIERA", "CD_UO_MINICARRIERA", "ESERCIZIO_MINICARRIERA", "PG_MINICARRIERA", "DS_MINICARRIERA", "DT_DURATA_DA", "DT_DURATA_A", "NUMERO_RATE", "IM_TOTALE_MINICARRIERA", "DS_TI_TRATTAMENTO", "CD_CDS_COMPENSO", "CD_UO_COMPENSO", "ESERCIZIO_COMPENSO", "PG_COMPENSO", "DS_COMPENSO", "STATO_COMPENSO", "CD_CDS_OBBLIGAZIONE", "ESERCIZIO_OBBLIGAZIONE", "ESERCIZIO_ORI_OBBLIGAZIONE", "PG_OBBLIGAZIONE", "PG_OBBLIGAZIONE_SCADENZARIO", "IM_COMPENSO", "DELTA_RATE", "DS_CDS", "DS_UO", "FL_COMPENSO_MINICARRIERA", "FL_COMPENSO_CONGUAGLIO", "IBAN") AS 
  SELECT
--
-- Date: 18/07/2006
-- Version: 1.3
--
-- Vista per la stampa Partitario Anagrafico e Dati Contabili
--
-- History:
--
-- Date: 14/03/03
-- Version: 1.0
-- Creazione
--
-- Date: 26/03/03
-- Version: 1.1
-- Riorganizzazione vista per ottimizzazione
--
-- Date: 21/01/2004
-- Version: 1.2
-- Estrazione CIN dall BANCA (richiesta n. 697)
--
-- Date: 18/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Body:
   anag.CD_ANAG -- Testata del Partitario: estrae i dati relativi all'Anagrafico ed ai Terzi ad esso associati
  ,'T1'
  ,anag.COGNOME
  ,anag.NOME
  ,com_nasc.DS_COMUNE
  ,com_nasc.CD_PROVINCIA
  ,anag.DT_NASCITA
  ,anag.CODICE_FISCALE
  ,anag.PARTITA_IVA
  ,anag.TI_ITALIANO_ESTERO
  ,naz.DS_NAZIONE
  ,anag.VIA_FISCALE || decode(anag.NUM_CIVICO_FISCALE, null, null, ', ' || anag.NUM_CIVICO_FISCALE)
  ,com_fisc.DS_COMUNE
  ,com_fisc.CD_PROVINCIA
  ,anag.CAP_COMUNE_FISCALE
  ,anag.FRAZIONE_FISCALE
  ,t.CD_TERZO
  ,t.VIA_SEDE || decode(t.NUMERO_CIVICO_SEDE, null, null, ', ' || t.NUMERO_CIVICO_SEDE)
  ,com_terzo.DS_COMUNE
  ,com_terzo.CD_PROVINCIA
  ,t.CAP_COMUNE_SEDE
  ,null     -- mod_pag.MOD_PAG
  ,null     -- mod_pag.TI_PAG
  ,null     -- mod_pag.DS_ABI
  ,null     -- mod_pag.AB
  ,null     -- mod_pag.CAB
  ,null     -- mod_pag.CONTO
  ,null     -- cin
  ,null     -- mod_pag.INTEST
  ,null     -- abi.VIA
  ,null     -- abi.CAP
  ,null     -- abi.FRAZIONE
  ,null     -- com_banca.DS_COMUNE
  ,null     -- CD_PV_BANCA
  ,to_char(null) -- CD_CDS
  ,to_char(null) -- CD_UNITA_ORGANIZZATIVA
  ,to_number(0)  -- ESERCIZIO
  ,to_number(0)  -- PG_MINICARRIERA
  ,to_char(null) -- DESCRIZIONE
  ,to_date(null) -- DT_DURATA_DA
  ,to_date(null) -- DT_DURATA_A
  ,to_number(0)  -- NUMERO_RATE
  ,0    -- carr.IM_TOTALE_MINICARRIERA
  ,to_char(null) -- DS_TI_TRATTAMENTO
  ,to_char(null) -- CD_CDS_COMPENSO
  ,to_char(null) -- CD_UO_COMPENSO
  ,0      -- ESERCIZIO_COMPENSO
  ,0    -- PG_COMPENSO
  ,to_char(null) -- DS_COMPENSO
  ,null    -- STATO_COMPENSO
  ,to_char(null) -- CD_CDS_OBBLIGAZIONE
  ,to_number(0)  -- ESERCIZIO_OBBLIGAZIONE
  ,to_number(0)  -- ESERCIZIO_ORI_OBBLIGAZIONE
  ,to_number(0)  -- PG_OBBLIGAZIONE
  ,to_number(0)  -- PG_OBBLIGAZIONE_SCADENZARIO
  ,0    -- IM_LORDO_PERCIPIENTE
  ,to_char(null)   -- delta_rate
  ,to_char(null) -- ds_cds
  ,to_char(null) -- ds_uo
  ,null --fl_compenso_minicarriera
  ,null --fl_compenso_conguaglio
  ,to_char(Null) -- IBAN
FROM ANAGRAFICO anag
 ,TERZO t
  ,COMUNE com_nasc
  ,COMUNE com_fisc
  ,COMUNE com_terzo
 ,NAZIONE naz
WHERE t.CD_ANAG         = anag.CD_ANAG
  AND com_nasc.PG_COMUNE(+) = anag.PG_COMUNE_NASCITA
  AND com_fisc.PG_COMUNE = anag.PG_COMUNE_FISCALE
  AND com_terzo.PG_COMUNE = t.PG_COMUNE_SEDE
  AND naz.PG_NAZIONE    (+) = anag.PG_NAZIONE_NAZIONALITA
union all
SELECT --DISTINCT -- Estrae i dati relativi alle Modalità di pagamento ed alle Banche dei Terzi
    t.CD_ANAG
   ,'T2'
   ,null     -- anag.COGNOME
   ,null     -- anag.NOME
   ,null     -- com_nasc.DS_COMUNE
   ,null     -- com_nasc.CD_PROVINCIA
   ,to_date(null)  -- anag.DT_NASCITA
   ,null     -- anag.CODICE_FISCALE
   ,null     -- anag.PARTITA_IVA
   ,null     -- anag.TI_ITALIANO_ESTERO
   ,null     -- naz.DS_NAZIONE
   ,null     -- anag.VIA_FISCALE || decode(anag.NUM_CIVICO_FISCALE, null, null, ', ' || anag.NUM_CIVICO_FISCALE)
   ,null     -- com_fisc.DS_COMUNE
   ,null      -- com_fisc.CD_PROVINCIA
   ,null     -- anag.CAP_COMUNE_FISCALE
   ,null     -- anag.FRAZIONE_FISCALE
   ,t.CD_TERZO
   ,null     -- t.VIA_SEDE || decode(t.NUMERO_CIVICO_SEDE, null, null, ', ' || t.NUMERO_CIVICO_SEDE)
   ,null     -- com_terzo.DS_COMUNE
   ,null     -- com_terzo.CD_PROVINCIA
   ,null     -- t.CAP_COMUNE_SEDE
   ,mod_pag.CD_MODALITA_PAG
   ,b.TI_PAGAMENTO
   ,abi.DS_ABICAB
   ,abi.ABI
   ,abi.CAB
   ,b.NUMERO_CONTO
   ,nvl(b.CIN,' ')
   ,b.INTESTAZIONE
   ,abi.VIA
   ,abi.CAP
   ,abi.FRAZIONE
    ,com.DS_COMUNE
    ,com.CD_PROVINCIA
   ,to_char(null) -- CD_CDS
   ,to_char(null) -- CD_UNITA_ORGANIZZATIVA
   ,to_number(0)  -- ESERCIZIO
   ,to_number(0)  -- PG_MINICARRIERA
   ,to_char(null) -- DESCRIZIONE
   ,to_date(null) -- DT_DURATA_DA
   ,to_date(null) -- DT_DURATA_A
   ,to_number(0)  -- NUMERO_RATE
   ,0    -- carr.IM_TOTALE_MINICARRIERA
   ,to_char(null) -- DS_TI_TRATTAMENTO
   ,to_char(null) -- CD_CDS_COMPENSO
   ,to_char(null) -- CD_UO_COMPENSO
   ,0      -- ESERCIZIO_COMPENSO
   ,0    -- PG_COMPENSO
   ,to_char(null) -- DS_COMPENSO
   ,null    -- STATO_COMPENSO
   ,to_char(null) -- CD_CDS_OBBLIGAZIONE
   ,to_number(0)  -- ESERCIZIO_OBBLIGAZIONE
   ,to_number(0)  -- ESERCIZIO_ORI_OBBLIGAZIONE
   ,to_number(0)  -- PG_OBBLIGAZIONE
   ,to_number(0)  -- PG_OBBLIGAZIONE_SCADENZARIO
   ,0    -- IM_LORDO_PERCIPIENTE
   ,to_char(null)   -- delta_rate
   ,to_char(null) -- ds_cds
   ,to_char(null) -- ds_uo
   ,null --fl_compenso_minicarriera
     ,null --fl_compenso_conguaglio
     ,b.codice_iban -- iban
FROM  TERZO t
  ,MODALITA_PAGAMENTO mod_pag
  ,RIF_MODALITA_PAGAMENTO rif
  ,BANCA b
  ,ABICAB abi
     ,COMUNE com
WHERE mod_pag.CD_TERZO   = t.CD_TERZO
AND   rif.CD_MODALITA_PAG = mod_pag.CD_MODALITA_PAG
AND   b.CD_TERZO    = t.CD_TERZO
AND   b.TI_PAGAMENTO      = rif.TI_PAGAMENTO
AND   abi.ABI(+)    = b.ABI
AND   abi.CAB(+)    = b.CAB
AND   com.PG_COMUNE(+)   = abi.PG_COMUNE
UNION ALL
SELECT
-- Compensi
   t.CD_ANAG
  ,'C' --,decode(comp.fl_compenso_minicarriera,'Y','CM',decode(comp.fl_compenso_conguaglio,'Y','CM','C'),'C')
  ,to_char(null)   -- COGNOME
  ,to_char(null)   -- NOME
  ,to_char(null)   -- DS_COMUNE
  ,null      -- com_nasc.CD_PROVINCIA
  ,to_date(null)   -- DT_NASCITA
  ,to_char(null)   -- CODICE_FISCALE
  ,to_char(null)   -- PARTITA_IVA
  ,to_char(null)   -- TI_ITALIANO_ESTERO
  ,to_char(null)   -- DS_NAZIONE
  ,to_char(null)   -- VIA_FISCALE
  ,to_char(null)   -- DS_COMUNE
  ,null       -- com_fisc.CD_PROVINCIA
  ,to_char(null)   -- CAP_COMUNE_FISCALE
  ,to_char(null)   -- FRAZIONE_FISCALE
  ,t.CD_TERZO
  ,to_char(null)   -- VIA_SEDE
  ,to_char(null)   -- DS_COMUNE_SEDE
  ,null      -- com_terzo.CD_PROVINCIA
  ,to_char(null)   -- CAP_COMUNE_SEDE
  ,to_char(null)   -- MOD_PAG
  ,to_char(null)   -- TI_PAG
  ,to_char(null)   -- DS_ABI
  ,to_char(null)   -- ABI
  ,to_char(null)   -- CAB
  ,to_char(null)   -- CONTO
  ,null      -- cin
  ,null      -- intestazione
  ,to_char(null)   -- VIA_BANCA
  ,to_char(null)   -- CAP_BANCA
  ,to_char(null)   -- FRAZIONE_BANCA
  ,to_char(null)   -- DS_COMUNE_BANCA
  ,null      -- cd_pv_banca
  ,to_char(null)   -- carr.CD_CDS
  ,to_char(null)   -- carr.CD_UNITA_ORGANIZZATIVA
  ,0      -- carr.ESERCIZIO
  ,0      -- carr.PG_MINICARRIERA
  ,to_char(null)   -- carr.DS_MINICARRIERA
  ,comp.DT_DA_COMPETENZA_COGE
  ,comp.DT_A_COMPETENZA_COGE
  ,0      -- carr.NUMERO_RATE
  ,0      -- carr.IM_TOTALE_MINICARRIERA
  ,ti_trat.DS_TI_TRATTAMENTO
  ,comp.CD_CDS
  ,comp.CD_UNITA_ORGANIZZATIVA
  ,comp.ESERCIZIO
  ,comp.PG_COMPENSO
  ,comp.DS_COMPENSO
  ,comp.STATO_COFI
  ,comp_riga.CD_CDS_OBBLIGAZIONE
  ,comp_riga.ESERCIZIO_OBBLIGAZIONE
  ,comp_riga.ESERCIZIO_ORI_OBBLIGAZIONE
  ,comp_riga.PG_OBBLIGAZIONE
  ,comp_riga.PG_OBBLIGAZIONE_SCADENZARIO
  ,DECODE(comp.IM_LORDO_PERCIPIENTE,null,0,
    round(comp.IM_LORDO_PERCIPIENTE*comp_riga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2))  IM_LORDO_PERCIPIENTE
  ,to_char(null)   -- delta_rate
  ,uo1.ds_unita_organizzativa
  ,uo2.ds_unita_organizzativa
  ,comp.fl_compenso_minicarriera
  ,comp.fl_compenso_conguaglio
  ,To_Char(Null) -- iban
FROM TERZO t
 ,COMPENSO comp
 ,COMPENSO_RIGA comp_riga
 ,TIPO_TRATTAMENTO ti_trat
 ,unita_organizzativa uo1
 ,unita_organizzativa uo2
WHERE t.CD_TERZO = comp.CD_TERZO
AND comp.CD_CDS = comp_riga.CD_CDS 
AND comp.CD_UNITA_ORGANIZZATIVA = comp_riga.CD_UNITA_ORGANIZZATIVA
AND comp.ESERCIZIO = comp_riga.ESERCIZIO
AND comp.PG_COMPENSO = comp_riga.PG_COMPENSO
AND ti_trat.CD_TRATTAMENTO = comp.CD_TRATTAMENTO
AND ti_trat.DT_INI_VALIDITA <= comp.DT_DA_COMPETENZA_COGE
AND ti_trat.DT_FIN_VALIDITA >= comp.DT_DA_COMPETENZA_COGE
and not exists ( select 1 from minicarriera_rata mrata
        where mrata.CD_CDS_COMPENSO = comp.CD_CDS
      and mrata.CD_UO_COMPENSO  = comp.CD_UNITA_ORGANIZZATIVA
      and mrata.ESERCIZIO_COMPENSO = comp.ESERCIZIO
      and mrata.PG_COMPENSO     = comp.PG_COMPENSO)
and uo1.cd_unita_organizzativa = comp.CD_CDS
and uo2.cd_unita_organizzativa = comp.CD_UNITA_ORGANIZZATIVA
union all
SELECT DISTINCT
-- Minicarriera
   t.CD_ANAG
  ,'M'
  ,to_char(null)   -- COGNOME
  ,to_char(null)   -- NOME
  ,to_char(null)   -- DS_COMUNE
  ,null      -- com_nasc.CD_PROVINCIA
  ,to_date(null)   -- DT_NASCITA
  ,to_char(null)   -- CODICE_FISCALE
  ,to_char(null)   -- PARTITA_IVA
  ,to_char(null)   -- TI_ITALIANO_ESTERO
  ,to_char(null)   -- DS_NAZIONE
  ,to_char(null)   -- VIA_FISCALE
  ,to_char(null)   -- DS_COMUNE
  ,null       -- com_fisc.CD_PROVINCIA
  ,to_char(null)   -- CAP_COMUNE_FISCALE
  ,to_char(null)   -- FRAZIONE_FISCALE
  ,t.CD_TERZO
  ,to_char(null)   -- VIA_SEDE
  ,to_char(null)   -- DS_COMUNE_SEDE
  ,null      -- com_terzo.CD_PROVINCIA
  ,to_char(null)   -- CAP_COMUNE_SEDE
  ,to_char(null)   -- MOD_PAG
  ,to_char(null)   -- TI_PAG
  ,to_char(null)   -- DS_ABI
  ,to_char(null)   -- ABI
  ,to_char(null)   -- CAB
  ,to_char(null)   -- CONTO
  ,null      -- cin
  ,null      -- intestazione
  ,to_char(null)   -- VIA_BANCA
  ,to_char(null)   -- CAP_BANCA
  ,to_char(null)   -- FRAZIONE_BANCA
  ,to_char(null)   -- DS_COMUNE_BANCA
  ,null      -- cd_pv_banca
  ,carr.CD_CDS
  ,carr.CD_UNITA_ORGANIZZATIVA
  ,carr.ESERCIZIO
  ,carr.PG_MINICARRIERA
  ,carr.DS_MINICARRIERA
  ,carr.DT_INIZIO_MINICARRIERA
  ,carr.DT_FINE_MINICARRIERA
  ,carr.NUMERO_RATE
  ,carr.IM_TOTALE_MINICARRIERA
  ,ti_trat.DS_TI_TRATTAMENTO
  ,comp.CD_CDS
  ,comp.CD_UNITA_ORGANIZZATIVA
  ,comp.ESERCIZIO
  ,comp.PG_COMPENSO
  ,comp.DS_COMPENSO
  ,comp.STATO_COFI
  ,comp_riga.CD_CDS_OBBLIGAZIONE
  ,comp_riga.ESERCIZIO_OBBLIGAZIONE
  ,comp_riga.ESERCIZIO_ORI_OBBLIGAZIONE
  ,comp_riga.PG_OBBLIGAZIONE
  ,comp_riga.PG_OBBLIGAZIONE_SCADENZARIO
  ,DECODE(comp.IM_LORDO_PERCIPIENTE,null,0,
    round(comp.IM_LORDO_PERCIPIENTE*comp_riga.IM_TOTALE_RIGA_COMPENSO/comp.IM_TOTALE_COMPENSO,2))  IM_LORDO_PERCIPIENTE
  ,substr((SELECT lpad(MIN(mrata.PG_RATA),3,'0') || ' - ' || lpad(MAX(mrata.PG_RATA),3,'0')
     FROM MINICARRIERA_RATA mrata
     where mrata.CD_CDS_COMPENSO   = comp.CD_CDS
       AND   mrata.CD_UO_COMPENSO   = comp.CD_UNITA_ORGANIZZATIVA
    AND   mrata.ESERCIZIO_COMPENSO = comp.ESERCIZIO
    AND   mrata.PG_COMPENSO    = comp.PG_COMPENSO
      group by  comp.CD_CDS
     ,comp.CD_UNITA_ORGANIZZATIVA
     ,comp.ESERCIZIO
     ,comp.PG_COMPENSO),1,10)
  ,uo1.ds_unita_organizzativa
  ,uo2.ds_unita_organizzativa
  ,comp.fl_compenso_minicarriera
  ,comp.fl_compenso_conguaglio
   ,to_char(null)   -- iban
FROM TERZO t
 ,MINICARRIERA carr
 ,MINICARRIERA_RATA rata
 ,COMPENSO comp
 ,COMPENSO_RIGA comp_riga
 ,TIPO_TRATTAMENTO ti_trat
 ,unita_organizzativa uo1
 ,unita_organizzativa uo2
WHERE t.CD_TERZO        = carr.CD_TERZO
AND   rata.CD_CDS     = carr.CD_CDS
AND   rata.CD_UNITA_ORGANIZZATIVA = carr.CD_UNITA_ORGANIZZATIVA
AND   rata.ESERCIZIO    = carr.ESERCIZIO
AND   rata.PG_MINICARRIERA   = carr.PG_MINICARRIERA
AND   rata.CD_CDS_COMPENSO   = comp.CD_CDS(+)
AND   rata.CD_UO_COMPENSO   = comp.CD_UNITA_ORGANIZZATIVA(+)
AND   rata.ESERCIZIO_COMPENSO = comp.ESERCIZIO(+)
AND   rata.PG_COMPENSO    = comp.PG_COMPENSO(+)
AND   comp.CD_CDS = comp_riga.CD_CDS (+)
AND   comp.CD_UNITA_ORGANIZZATIVA = comp_riga.CD_UNITA_ORGANIZZATIVA (+)
AND   comp.ESERCIZIO = comp_riga.ESERCIZIO (+)
AND   comp.PG_COMPENSO = comp_riga.PG_COMPENSO (+)
AND   ti_trat.CD_TRATTAMENTO  = carr.CD_TRATTAMENTO
AND   ti_trat.DT_INI_VALIDITA <= carr.DT_INIZIO_MINICARRIERA
AND   ti_trat.DT_FIN_VALIDITA >= carr.DT_INIZIO_MINICARRIERA
and uo1.cd_unita_organizzativa = comp.CD_CDS
and uo2.cd_unita_organizzativa = comp.CD_UNITA_ORGANIZZATIVA
;
