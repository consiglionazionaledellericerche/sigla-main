CREATE OR REPLACE function get_coge_ult_terzo(
 aTipoDocCont varchar2,
 aTipoDocAmm varchar2,
 aCdCdsDocAmm varchar2,
 aEsDocAmm number,
 aCdUoDocAmm varchar2,
 aPgDocAmm number,
 aEsObbAcc number,
 aEsOriObbAcc number,
 aPgObbAcc number,
 aPgObbAccScad number
) return number is
--==================================================================================================
--
-- Date: 12/07/2006
-- Version: 1.4
--
-- Ritorna il terzo del doc amministrativo collegato alla scadenza di obb/acc per il tipo di doc. cont spec.
-- per ECONOMICA
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
-- Tolto il riferimento alla vista v_doc_amm_coge_nogen_ter_fon
--
-- Date: 16/01/2004
-- Version: 1.3
-- Aggiunta la distinct su estrazione terzo doc generico collegato a reversale
--
-- Date: 12/07/2006
-- Version: 1.4
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il parametro di ingresso aEsOriObbAcc: Esercizio Originale Impegno/Accertamento
--
-- Body:
--
--==================================================================================================
 aTerzo number(8);
 aFLDG char(1);
begin
 SELECT fl_doc_generico INTO aFLDG FROM TIPO_DOCUMENTO_AMM WHERE
        CD_TIPO_DOCUMENTO_AMM = aTipoDocAmm;
 if aFLDG = 'Y' then
  if aTipoDocCont = 'MAN' then
   select
      distinct a.cd_terzo into aTerzo
   from documento_generico_riga a, documento_generico b
    where
         a.cd_tipo_documento_amm = aTipoDocAmm
  	 and a.esercizio = aEsDocAmm
	 and a.pg_documento_generico = aPgDocAmm
	 and a.cd_cds = aCdCdsDocAmm
	 and a.cd_unita_organizzativa = aCdUoDocAmm
	 and a.cd_tipo_documento_amm = b.cd_tipo_documento_amm
 	 and a.esercizio = b.esercizio
	 and a.pg_documento_generico = b.pg_documento_generico
	 and a.cd_cds = b.cd_cds
	 and a.cd_unita_organizzativa = b.cd_unita_organizzativa
     and a.esercizio_obbligazione = aEsObbAcc
     and a.esercizio_ori_obbligazione = aEsOriObbAcc
	 and a.pg_obbligazione = aPgObbAcc
	 and a.pg_obbligazione_scadenzario = aPgObbAccScad
     and a.pg_documento_generico >= 0;
   else
    select distinct
      a.cd_terzo into aTerzo
     from documento_generico_riga a, documento_generico b
    where
         a.cd_tipo_documento_amm = aTipoDocAmm
 	 and a.esercizio = aEsDocAmm
	 and a.pg_documento_generico = aPgDocAmm
	 and a.cd_cds = aCdCdsDocAmm
	 and a.cd_unita_organizzativa = aCdUoDocAmm
	 and a.cd_tipo_documento_amm = b.cd_tipo_documento_amm
 	 and a.esercizio = b.esercizio
	 and a.pg_documento_generico = b.pg_documento_generico
	 and a.cd_cds = b.cd_cds
	 and a.cd_unita_organizzativa = b.cd_unita_organizzativa
     and a.esercizio_accertamento = aEsObbAcc
     and a.esercizio_ori_accertamento = aEsOriObbAcc
	 and a.pg_accertamento = aPgObbAcc
	 and a.pg_accertamento_scadenzario = aPgObbAccScad
     and a.pg_documento_generico >= 0;
   end if;
  return aTerzo;
 elsif aTipoDocAmm = 'FATTURA_P' then
  select cd_terzo into aTerzo from FATTURA_PASSIVA
   where
          pg_fattura_passiva >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_FATTURA_PASSIVA = aPgDocAmm;
  return aTerzo;
 elsif aTipoDocAmm = 'FATTURA_A' then
  select cd_terzo into aTerzo from FATTURA_ATTIVA
   where
          pg_fattura_attiva >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_FATTURA_ATTIVA = aPgDocAmm;
  return aTerzo;
 elsif aTipoDocAmm = 'COMPENSO' then
  select cd_terzo into aTerzo from COMPENSO
    where
		  pg_compenso >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_COMPENSO = aPgDocAmm;
  return aTerzo;
 elsif aTipoDocAmm = 'ANTICIPO' then
  select cd_terzo into aTerzo from ANTICIPO
    where
		  pg_anticipo >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_ANTICIPO = aPgDocAmm;
  return aTerzo;
 elsif aTipoDocAmm = 'RIMBORSO' then
  select cd_terzo into aTerzo from RIMBORSO
   where
          pg_rimborso >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_RIMBORSO = aPgDocAmm;
  return aTerzo;
 elsif aTipoDocAmm = 'MISSIONE' then
  select cd_terzo into aTerzo from MISSIONE
    where
		  pg_missione >= 0
      and ESERCIZIO = aEsDocAmm
      and CD_UNITA_ORGANIZZATIVA = aCdUoDocAmm
      and CD_CDS = aCdCdsDocAmm
      and PG_missione = aPgDocAmm;
  return aTerzo;
 else
  IBMERR001.RAISE_ERR_GENERICO('Tipo documento non gestibile'||aTipoDocAmm);
 end if;
end;
/


