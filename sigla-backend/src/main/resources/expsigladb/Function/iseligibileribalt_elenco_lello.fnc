CREATE OR REPLACE Function isEligibileRibalt_elenco_LELLO(aTiGestione char,aCdCdsDoc varchar2,aEsDoc number,aEsOriDoc number,aPgDoc number) RETURN CHAR IS
 aImRisc number(15,2);
 aImDoc number(15,2);
 aImScad number(15,2);
 aEsOriAcc number(4);
 aEsOriObb number(4);
 aPgAcc number(10);
 aPgObb number(10);
 aNum number;
 aConta_rev_provv NUMBER;
 aConta_cori_compenso NUMBER;
 aConta_liq_gruppo_centro number;
begin
If aTiGestione = 'S' then

-- PEZZO AGGIUNTO IL 07.01.2010 PER ESCLUDERE DAI RIBALTAMENTI MASSIVI GLI IMPEGNI
-- PRESENTI IN LIQUID_GRUPPO_CORI

  Begin
     Select count(*)
     into   aConta_liq_gruppo_centro
     From   liquid_gruppo_centro
     Where  cd_cds_obb_accentr        = aCdCdsDoc And
            esercizio_obb_accentr     = aEsDoc And
            esercizio_ori_obb_accentr = aEsOriDoc And
            pg_obb_accentr            = aPgDoc And
            rownum < 2;

     If aConta_liq_gruppo_centro > 0 Then
        Return 'N';
     End If;

  Exception
   When No_Data_Found Then Null;
  End;

            
-- PEZZO AGGIUNTO IL 10.01.2007 PER ESCLUDERE DAI RIBALTAMENTI MASSIVI GLI IMPEGNI
-- I CUI ACCERTAMENTI SONO LEGATI A CORI

  Begin
   Select esercizio_ori_accertamento, pg_accertamento
   Into   aEsOriAcc, aPgAcc
   From   ass_obb_acr_pgiro
   Where  cd_cds = aCdCdsDoc And
          esercizio = aEsDoc And
          esercizio_ori_obbligazione = aEsOriDoc And
          pg_obbligazione = aPgDoc;

   Select Count(*)
   Into   aConta_cori_compenso
   From   contributo_ritenuta
   Where  cd_cds_accertamento = aCdCdsDoc And
          esercizio_accertamento = aEsDoc And
          esercizio_ori_accertamento = aEsOriAcc And
          pg_accertamento = aPgAcc And
          rownum < 2;

   If aConta_cori_compenso > 0 Then
        Return 'N';
   End If;

  Exception
   When No_Data_Found Then Null;
  End;

-- FINE PEZZO AGGIUNTO

  For aDocDett in (Select a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont, sum(b.im_associato_doc_contabile) im_associato_doc_contabile
                   From obbligazione a,    obbligazione_scadenzario b
                   Where a.cd_cds = aCdCdsDoc And
                         a.esercizio = aEsDoc And
                         a.esercizio_originale = aEsOriDoc And
                         a.pg_obbligazione = aPgDoc And
                         a.cd_cds = b.cd_cds And
                         a.esercizio = b.esercizio And
                         a.esercizio_originale = b.esercizio_originale And
                         a.pg_obbligazione = b.pg_obbligazione And
                         a.DT_CANCELLAZIONE is Null And
                         riportato = 'N'
                   Group By a.esercizio_originale, a.pg_obbligazione, a.fl_pgiro, a.im_obbligazione, a.cd_tipo_documento_cont) loop

-- SE L'OBBLIGAZIONE E' TUTTA PAGATA E TUTTA RISCONTRATA NON SI RIBALTA

If aDocDett.im_obbligazione = aDocDett.im_associato_doc_contabile Then

   Return 'N';

-- ALTRIMENTI, SE PARTITA DI GIRO...

Elsif aDocDett.fl_pgiro = 'Y' Then

-- ... DEGLI ISTITUTI

  If aDocDett.cd_tipo_documento_cont not in ('IMP', 'IMP_RES') Then

     Begin
      Select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc
      From ass_obb_acr_pgiro
      Where  cd_cds = aCdCdsDoc
             and esercizio = aEsDoc
             and esercizio_ori_obbligazione = aEsOriDoc
         and pg_obbligazione = aPgDoc
         and ti_origine = 'S';
        return 'Y';

-- SE L'OBBLIGAZIONE E' L'ORIGINE DELLA PARTITA DI GIRO ALLORA SI RIBALTA

     Exception when NO_DATA_FOUND then

      Select esercizio_ori_accertamento, pg_accertamento into aEsOriAcc, aPgAcc
      From ass_obb_acr_pgiro
      Where   cd_cds = aCdCdsDoc
              and esercizio = aEsDoc
          and esercizio_ori_obbligazione = aEsOriDoc
          and pg_obbligazione = aPgDoc
          and ti_origine = 'E';

          select Sum(IM_SCADENZA), Sum(im_associato_doc_contabile)
          into  aImScad, aImDoc
          from  accertamento_scadenzario
          Where cd_cds = aCdCdsDoc
            and esercizio = aEsDoc
        and esercizio_originale = aEsOriAcc
        And pg_accertamento = aPgAcc;

         If aImScad > 0 and aImDoc = 0 Then

-- SE ERA L'ACCERTAMENTO L'ORIGINE DELLA PARTITA DI GIRO, E L'ACCERTAMENTO NON E' RISCOSSO
-- ALLORA L'OBBLIGAZIONE NON SI RIBALTA
           return 'N';
         Else

-- SE INVECE L'ACCERTAMENTO (ORIGINE DELLA PARTITA DI GIRO) E' RISCOSSO ...

        aImRisc := getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriAcc,aPgAcc,'E');

-- ... SI RIBALTA SOLO SE E' TOTALMENTE RISONTRATO, ALTRIMENTI NO

        if aImDoc = aImRisc then
             return 'Y';
        else
          return 'N';
        end if;
     End if;
     End;

-- SE INVECE PARTITA DI GIRO DELL'ENTE

  Elsif aDocDett.cd_tipo_documento_cont In ('IMP', 'IMP_RES') Then

    Return 'Y';

  End If;

-- TUTTO IL RESTO DELLE OBBLIGAZIONI

Elsif aDocDett.cd_tipo_documento_cont In ('OBB', 'OBB_RES', 'OBB_RESIM', 'IMP', 'IMP_RES') Then
      Return 'Y';
End if;

End loop; -- DELLA SPESA
Return 'N';

Else  -- TIPO GESTIONE 'S' O 'E'

-- =======================
-- PARTE ENTRATE
-- =======================
For aDocDett in (Select a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont, sum(b.im_associato_doc_contabile) im_associato_doc_contabile
                   From accertamento a, accertamento_scadenzario b
                   Where a.cd_cds = aCdCdsDoc And
                         a.esercizio = aEsDoc And
                         a.esercizio_originale = aEsOriDoc And
                         a.pg_accertamento = aPgDoc And
                         a.cd_cds = b.cd_cds And
                         a.esercizio = b.esercizio And
                         a.esercizio_originale = b.esercizio_originale And
                         a.pg_accertamento = b.pg_accertamento And
                         a.DT_CANCELLAZIONE is Null And
                         riportato = 'N'
                   Group By a.esercizio_originale, a.pg_accertamento, a.fl_pgiro, a.im_accertamento, a.cd_tipo_documento_cont) loop

If aDocDett.cd_tipo_documento_cont = 'ACR_SIST' then

-- Accertamenti di sistema non si ribaltano
   Return 'N';

Elsif aDocDett.im_accertamento = aDocDett.im_associato_doc_contabile Then

-- neanche i documenti totalmente riscossi e riscontrati

   Return 'N';

-- ALTRIMENTI, SE PARTITA DI GIRO...

Elsif aDocDett.fl_pgiro = 'Y' Then
     If aDocDett.cd_tipo_documento_cont not in ('ACR', 'ACR_RES') then

-- PER ROBERTO, CONTROLLO SE LA PARTITA DI GIRO E' AGGANCIATA A REVERSALI PROVVISORIE,
-- NEL QUAL CASO L'ACCERTAMENTO NON E' DA RIBALTARE

        Select Count(*)
        Into  aConta_rev_provv
        From  accertamento a, accertamento_scadenzario b
        Where a.cd_cds = aCdCdsDoc And
              a.esercizio = aEsDoc And
              a.esercizio_originale = aEsOriDoc And
              a.pg_accertamento = aPgDoc And
              a.cd_cds = b.cd_cds And
              a.esercizio = b.esercizio And
              a.esercizio_originale = b.esercizio_originale And
              a.pg_accertamento = b.pg_accertamento And
              a.DT_CANCELLAZIONE is Null And
              riportato = 'N' AND
              a.cd_tipo_documento_cont = 'ACR_PGIRO' AND
              EXISTS (SELECT 1 FROM REVERSALE_RIGA RR, REVERSALE R
                      WHERE RR.CD_CDS                         = A.CD_CDS AND
                            RR.ESERCIZIO_ACCERTAMENTO         = A.ESERCIZIO AND
                            RR.ESERCIZIO_ORI_ACCERTAMENTO     = A.ESERCIZIO_ORIGINALE AND
                            RR.PG_ACCERTAMENTO                = A.PG_ACCERTAMENTO AND
                            RR.PG_ACCERTAMENTO_SCADENZARIO    = B.PG_ACCERTAMENTO_SCADENZARIO AND
                            R.CD_CDS       = RR.CD_CDS       AND
                            R.ESERCIZIO    = RR.ESERCIZIO    AND
                            R.PG_REVERSALE = RR.PG_REVERSALE AND
                            R.CD_TIPO_DOCUMENTO_CONT = 'REV_PROVV') And
              rownum < 2;

              If aConta_rev_provv > 0 Then
                 Return 'N';
              End If;

-- PEZZO AGGIUNTO IL 26.10.2007 PER ESCLUDERE DAI RIBALTAMENTI MASSIVI GLI ACCERTAMENTI
-- I CUI IMPEGNI SONO LEGATI A CORI

              Begin
               Select esercizio_ori_obbligazione, pg_obbligazione
               Into   aEsOriObb, aPgObb
               From   ass_obb_acr_pgiro
               Where  cd_cds = aCdCdsDoc And
                      esercizio = aEsDoc And
                      esercizio_ori_accertamento = aEsOriDoc And
                      pg_accertamento = aPgDoc;

               Select Count(*)
               Into   aConta_cori_compenso
               From   contributo_ritenuta
               Where  cd_cds_OBBLIGAZIONE = aCdCdsDoc And
                      esercizio_OBBLIGAZIONE = aEsDoc And
                      esercizio_ori_OBBLIGAZIONE = aEsOriObb And
                      pg_OBBLIGAZIONE = aPgObb And
                      rownum < 2;

               If aConta_cori_compenso > 0 Then
                    Return 'N';
               End If;

              Exception
               When No_Data_Found Then Null;
              End;

-- FINE PEZZO AGGIUNTO


     Begin
     -- recupero l'obbligazione generata dall'accertamento
       Select esercizio_ori_obbligazione, pg_obbligazione
       Into   aEsOriObb, aPgObb
       From   ass_obb_acr_pgiro
       Where  cd_cds = aCdCdsDoc And
              esercizio = aEsDoc And
              esercizio_ori_accertamento = aEsOriDoc And
              pg_accertamento = aPgDoc And
              ti_origine = 'E';

           Return 'Y';

     Exception
       When NO_DATA_FOUND Then

       -- se non la trovo perch? ? stata l'obbligazione a generare l'accertamento
       -- recupero comunque l'obbligazione che ha generato l'accertamento (recupero comunque
       -- l'obbligazione collegata a quell'accertamento)

         Select esercizio_ori_obbligazione, pg_obbligazione
         Into   aEsOriObb, aPgObb
         From   ass_obb_acr_pgiro
         Where  cd_cds = aCdCdsDoc And
                esercizio = aEsDoc And
                esercizio_ori_accertamento = aEsOriDoc And
                pg_accertamento = aPgDoc And
                ti_origine = 'S';

           -- vedo se ? pagata
             Select Sum(im_associato_doc_contabile)
             Into   aImDoc
             From   obbligazione_scadenzario
             Where  cd_cds = aCdCdsDoc And
                    esercizio = aEsDoc And
                    esercizio_originale = aEsOriObb And
                pg_obbligazione = aPgObb;

             If aImDoc = 0 Then
             -- se l'obbligazione non ? pagata allora l'accertamento non si ribalta (immagino perch? se lo trascina
             -- l'obbligazione)
               Return 'N';
             Else
             -- se l'obbligazione ? pagata vedo per quanto ? riscontrata
               aImRisc := getIm_riscontrato (aEsDoc,aCdCdsDoc,aEsOriObb,aPgObb,'S');
          -- Se l'obbligazione che ha aperto la partita di giro ? pagata e riscontrata torno Y altrimenti N
           If aImDoc = aImRisc then
                Return 'Y';
               Else
             Return 'N';
           End If;
         End If;
       End;

    Elsif aDocDett.cd_tipo_documento_cont In ('ACR', 'ACR_RES') Then
      Return 'Y';
    End If;

-- TUTTO IL RESTO DEGLI ACCERTAMENTI

Elsif aDocDett.cd_tipo_documento_cont In ('ACR', 'ACR_RES') Then
      Return 'Y';
End if;

end loop; -- DELL'ENTRATA
Return 'N';

end if;  -- GESTIONE = 'S' O 'E'
Return 'N';

end;
/


