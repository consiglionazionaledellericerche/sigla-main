CREATE OR REPLACE FUNCTION         isEligibileRibalt(aTiGestione char,aCdCdsDoc varchar2,aEsDoc number,aEsOriDoc number,aPgDoc number) RETURN CHAR IS
--==================================================================================================
--
-- Date: 12/07/2006
-- Version: 1.5
--
-- Estrazione documenti ribaltabili a nuovo esercizio (obb/acc)
--
-- History:
--
-- Date: 10/04/2003
-- Version: 1.0
-- Creazione function
--
-- Date: 01/07/2003
-- Version: 1.1
-- Tolto il controllo sul fatto che si tratti di obb./acc. cori
--
-- Date: 03/07/2003
-- Version: 1.2
-- Controllo che nel caso il doc non apra la pgiro cds, il doc che apre la partita non sia
-- libero da man/rev o con man/rev non riscontrati
--
-- Date: 04/07/2003
-- Version: 1.3
-- Esclusi dal ribaltamento accertamenti ACR_SIST
--
-- Date: 06/08/2003
-- Version: 1.4
-- Aggiunta documentazione
--
-- Date: 12/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- aggiunto il parametro di ingresso aEsOriDoc: Esercizio Originale Impegno/Accertamento
--
-- Body:
--
--==================================================================================================
 aImRisc number(15,2);
 aImDoc number(15,2);
 aEsOriAcc number(4);
 aEsOriObb number(4);
 aPgAcc number(10);
 aPgObb number(10);
 aNum number;
begin
-- I seguenti documenti contabili:
-- - Impegni e impegni residui, su partita di giro o meno
-- - Accertamenti e accertamenti residui, su partita di giro o meno
-- - Accertamenti pluriennali
-- - Obbligazioni e obbligazioni pluriennali nel bilancio cds
-- possono essere riportati all'esercizio successivo se sono soddisfatte le seguenti condizioni:
-- 1. la somma degli importi associati a documenti contabili (mandati/reversali) delle scadenze
--    ? pari all'importo riscontrato di tali documenti
-- 2. l'importo totale del documento contabile ? maggiore dell'importo riscontrato
-- Se le condizioni 1. e 2. non sono soddisfatte allora il documento non ? riportabile
--
-- Per annotazioni su partita di giro nbel bilancio CdS valgono le seguenti regole:
-- Se valgono le condizioni 1. e 2.
-- - se il documento apre la partita di giro allora il documento ? riportabile
-- - se il documento non apre la partita di giro, e la controparte non ? associata
--   a documenti contabili allora il documento non ? riportabile (lo ? la controparte)
-- - se il documento non apre la partita di giro, e la controparte ? completamente
--   riscontrata (condizione 1.), allora ? riportabile
-- - se il documento non apre la partita di giro, e la controparte non ? comple-
--   tamente riscontrata, allora non ? riportabile
 if aTiGestione = 'S' then
  for aDocDett in (
   select a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont, sum(b.im_associato_doc_contabile) im_associato_doc_contabile from
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
	   and riportato = 'N'
       and (
	           a.STATO_OBBLIGAZIONE  <> 'P'  or (a.STATO_OBBLIGAZIONE  = 'P' and a.FL_GARA_IN_CORSO ='Y' )
  	        or a.STATO_OBBLIGAZIONE = 'P' and a.esercizio <> a.esercizio_competenza
		   )
   group by
    a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont
  ) loop
   aImRisc:=getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriDoc,aPgDoc,aTiGestione);
   if
        aDocDett.im_associato_doc_contabile = aImRisc
    and aDocDett.im_obbligazione > aImRisc
   then
    if aDocDett.fl_pgiro = 'Y' and aDocDett.cd_tipo_documento_cont not in ('IMP','IMP_RES') then
     begin
	  select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc from ass_obb_acr_pgiro where
	       cd_cds = aCdCdsDoc
       and esercizio = aEsDoc
       and esercizio_ori_obbligazione = aEsOriDoc
	   and pg_obbligazione = aPgDoc
	   and ti_origine = 'S';
      return 'Y';
 	 exception when NO_DATA_FOUND then
	  select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc from ass_obb_acr_pgiro where
	       cd_cds = aCdCdsDoc
       and esercizio = aEsDoc
       and esercizio_ori_obbligazione = aEsOriDoc
	   and pg_obbligazione = aPgDoc
	   and ti_origine = 'E';

      select Sum(im_associato_doc_contabile)
      into   aImDoc
      from   accertamento_scadenzario
      Where  cd_cds = aCdCdsDoc And
             esercizio = aEsDoc And
             esercizio_originale = aEsOriAcc And
             pg_accertamento = aPgAcc;

      if
       aImDoc = 0
      then
	   return 'N';
      else
       aImRisc:=getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriAcc,aPgAcc,'E');
	   -- Se il doc che ha aperto la partita di giro ? incassato e riscontrato torno Y altrimenti N
	   if aImDoc = aImRisc then
   	    return 'Y';
       else
	    return 'N';
	   end if;
	  end if;
 	 end;
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
   select a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont, sum(b.im_associato_doc_contabile) im_associato_doc_contabile from
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
	   and riportato = 'N'
   group by
    a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont
  ) Loop

  If aDocDett.cd_tipo_documento_cont = 'ACR_SIST' then
  	 return 'N';
  Else

   aImRisc := getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriDoc,aPgDoc,aTiGestione);

   If aDocDett.im_associato_doc_contabile = aImRisc And aDocDett.im_accertamento > aImRisc Then

     If aDocDett.fl_pgiro = 'Y' and aDocDett.cd_tipo_documento_cont not in ('ACR','ACR_RES') then

      Begin
	  select esercizio_ori_obbligazione, pg_obbligazione
	  into   aEsOriObb, aPgObb from ass_obb_acr_pgiro
	  Where  cd_cds = aCdCdsDoc And
	         esercizio = aEsDoc And
	         esercizio_ori_accertamento = aEsOriDoc And
	         pg_accertamento = aPgDoc And
	         ti_origine = 'E';
          return 'Y';
      Exception
        When NO_DATA_FOUND Then

	  Select esercizio_ori_obbligazione, pg_obbligazione into aEsOriObb, aPgObb
	  From   ass_obb_acr_pgiro
	  Where  cd_cds = aCdCdsDoc And
	         esercizio = aEsDoc And
	         esercizio_ori_accertamento = aEsOriDoc And
	         pg_accertamento = aPgDoc And
	         ti_origine = 'S';

          Select Sum(im_associato_doc_contabile)
          Into   aImDoc
          From   obbligazione_scadenzario
          Where  cd_cds = aCdCdsDoc And
                 esercizio = aEsDoc And
                 esercizio_originale = aEsOriObb And
                 pg_obbligazione = aPgObb;

          If aImDoc = 0 then
             Return 'N';
          Else
             aImRisc:=getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriObb,aPgObb,'S');
	      -- Se il doc che ha aperto la partita di giro ? pagato e riscontrato torno Y altrimenti N
	      If aImDoc = aImRisc then
   	         Return 'Y';
              Else
	         Return 'N';
	      End If;
	  End if;
      End;

    End If;  -- aDocDett.fl_pgiro = 'Y' and aDocDett.cd_tipo_documento_cont not in ('ACR','ACR_RES') then

    Return 'Y';

   End If; -- aDocDett.im_associato_doc_contabile = aImRisc And aDocDett.im_accertamento > aImRisc Then

  end if;
  end loop;
  return 'N';
 end if;
 return 'N';
end;
/


