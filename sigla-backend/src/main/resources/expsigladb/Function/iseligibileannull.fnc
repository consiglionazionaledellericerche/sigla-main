CREATE OR REPLACE FUNCTION isEligibileAnnull(aTiGestione char,aCdCdsDoc varchar2,aEsDoc number,aEsOriDoc number,aPgDoc number) RETURN CHAR IS
--==================================================================================================
--
-- Date: 12/07/2006
-- Version: 1.3
--
-- Estrazione documenti eligibili per annullamento
--
-- History:
--
-- Date: 10/04/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 08/07/2003
-- Version: 1.1
-- Fix
--
-- Date: 06/08/2003
-- Version: 1.2
-- Aggiunta documentazione
--
-- Date: 12/07/2006
-- Version: 1.3
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il parametro di ingresso aEsOriDoc: Esercizio Originale Impegno/Accertamento
--
-- Body:
--
--==================================================================================================
 aEsOriAcc number(4);
 aEsOriObb number(4);
 aPgAcc number(10);
 aPgObb number(10);
begin
-- Per documenti contabili non su partita di giro (ente o cds):
-- - se l'importo associato a documenti amministrativi ? nullo e l'importo totale del
--   documento ? positivo, allora il documento ? annullabile
-- - se l''importo associato a documenti amministrati non ? nullo oppure l'importo totale
--   del documento ? nullo, allora il documento non ? annullabile
-- Per documenti contabili su partita di giro (ente o cds):
-- - se il documento apre la partita di giro, allora valgono le regole precedenti
-- - se il documento non apre la partita di giro, allora non ? annullabile (eventual-
--   mente lo ? la sua controparte
 if aTiGestione = 'S' then
  for aDocDett in (
   select a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont, sum(b.im_associato_doc_amm) im_associato_doc_amm from
    obbligazione a,
	obbligazione_scadenzario b
   where
	       a.cd_cds = aCdCdsDoc
       and a.esercizio = aEsDoc
       and a.esercizio_originale = aEsOriDoc
	   and a.pg_obbligazione = aPgDoc
	   and a.cd_cds = b.cd_cds
	   and a.esercizio = b.esercizio
	   and a.esercizio_originale = b.esercizio_originale
	   and a.pg_obbligazione = b.pg_obbligazione
       and a.DT_CANCELLAZIONE is null
       and (
	           a.STATO_OBBLIGAZIONE  <> 'P'
  	        or a.STATO_OBBLIGAZIONE = 'P' and a.esercizio <> a.esercizio_competenza
		   )
   group by
    a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont
  ) loop
   if
        aDocDett.im_associato_doc_amm = 0
    and aDocDett.im_obbligazione > 0
   then
    if aDocDett.fl_pgiro = 'Y' then
     begin
	  select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc from ass_obb_acr_pgiro where
	        cd_cds = aCdCdsDoc
        and esercizio = aEsDoc
        and esercizio_ori_obbligazione = aEsOriDoc
	    and pg_obbligazione = aPgDoc
	    and ti_origine = 'S';
      return 'Y';
	 exception when NO_DATA_FOUND then
      return 'N';
     end;
     return 'Y';
    end if;
	return 'Y';
   end if;
  end loop;
  return 'N';
 else
-- =======================
-- PARTE ENTRATE
-- =======================
  for aDocDett in (
   select a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont, sum(b.im_associato_doc_amm) im_associato_doc_amm from
    accertamento a,
	accertamento_scadenzario b
   where
	       a.cd_cds = aCdCdsDoc
       and a.esercizio = aEsDoc
       and a.esercizio_originale = aEsOriDoc
	   and a.pg_accertamento = aPgDoc
	   and a.cd_cds = b.cd_cds
	   and a.esercizio = b.esercizio
	   and a.esercizio_originale = b.esercizio_originale
	   and a.pg_accertamento = b.pg_accertamento
       and a.DT_CANCELLAZIONE is null
   group by
    a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont
  ) loop
   if
        aDocDett.im_associato_doc_amm = 0
    and aDocDett.im_accertamento > 0
   then
    if aDocDett.fl_pgiro = 'Y' then
     begin
	  select esercizio_ori_obbligazione, pg_obbligazione into aEsOriObb, aPgObb from ass_obb_acr_pgiro where
	       cd_cds = aCdCdsDoc
       and esercizio = aEsDoc
       and esercizio_ori_accertamento = aEsOriDoc
	   and pg_accertamento = aPgDoc
	   and ti_origine = 'E';
      return 'Y';
	 exception when NO_DATA_FOUND then
      return 'N';
	 end;
     return 'Y';
    end if;
	return 'Y';
   end if;
  end loop;
  return 'N';
 end if;
 return 'N';
end;
/


