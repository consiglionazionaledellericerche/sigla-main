CREATE OR REPLACE function get_coge_ult_fondo(
 aTipoDocAmm varchar2,
 aCdCdsDocAmm varchar2,
 aEsDocAmm number,
 aCdUoDocAmm varchar2,
 aPgDocAmm number
) return char is
--==================================================================================================
--
-- Date: 18/06/2003
-- Version: 1.2
--
-- Ritorna lo stato di associazione a fondo econmale del doc amministrativo specificato
-- per utilizzo in ECONOMICA
--
-- History:
--
-- Date: 16/06/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 18/06/2003
-- Version: 1.1
-- Uso la vecchia vista v_doc_amm_coge_nogen_ter_fon
--
-- Date: 18/06/2003
-- Version: 1.2
-- Tolti riferimenti a vista v_doc_amm_coge_nogen_ter_fon
--
-- Body:
--
--==================================================================================================
 aSPFE char(1);
 aFLDG char(1);
begin
 if aTipoDocAmm in ('GEN_AP_FON','GEN_RE_FON','GEN_CH_FON') then
  return 'R';
 end if;
 SELECT fl_doc_generico INTO aFLDG FROM TIPO_DOCUMENTO_AMM WHERE
        CD_TIPO_DOCUMENTO_AMM = aTipoDocAmm;

 if aFLDG = 'Y' then
  select
     a.stato_pagamento_fondo_eco into aSPFE
  from documento_generico a
   where
        a.cd_tipo_documento_amm = aTipoDocAmm
 	and a.esercizio = aEsDocAmm
	and a.pg_documento_generico = aPgDocAmm
	and a.cd_cds = aCdCdsDocAmm
	and a.cd_unita_organizzativa = aCdUoDocAmm
    and a.pg_documento_generico >= 0;
  return aSPFE;
 elsif aTipoDocAmm = 'FATTURA_P' then
  select stato_pagamento_fondo_eco into aSPFE from FATTURA_PASSIVA
   where
          pg_fattura_passiva >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_FATTURA_PASSIVA = aPgDocAmm;
  return aSPFE;
 elsif aTipoDocAmm = 'FATTURA_A' then
  return 'N';
 elsif aTipoDocAmm = 'COMPENSO' then
  select stato_pagamento_fondo_eco into aSPFE from COMPENSO
    where
		  pg_compenso >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_COMPENSO = aPgDocAmm;
  return aSPFE;
 elsif aTipoDocAmm = 'ANTICIPO' then
  select stato_pagamento_fondo_eco into aSPFE from ANTICIPO
    where
		  pg_anticipo >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_ANTICIPO = aPgDocAmm;
  return aSPFE;
 elsif aTipoDocAmm = 'RIMBORSO' then
  return 'N';
 elsif aTipoDocAmm = 'MISSIONE' then
  select stato_pagamento_fondo_eco into aSPFE from MISSIONE
    where
		  pg_missione >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_missione = aPgDocAmm;
  return aSPFE;
 else
  IBMERR001.RAISE_ERR_GENERICO('Tipo documento non gestibile'||aTipoDocAmm);
 end if;
end;
/


