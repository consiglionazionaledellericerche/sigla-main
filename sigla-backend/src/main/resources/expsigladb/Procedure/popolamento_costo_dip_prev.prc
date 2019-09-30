CREATE OR REPLACE PROCEDURE         POPOLAMENTO_COSTO_DIP_PREV( aEsAnnoPrev IN NUMBER,bMese IN NUMBER) is
-- =================================================================================================
-- =================================================================================================
--
-- ATTENZIONE RICORDARSI DI CAMBIARE IL MESE DI RIFERIMENTO E L'ESERCIZIO !!!!!!!!!----
--
--script per riempire costo del dipendente con mese =  0  (PREVENTIVO)
-- !!!ATTENZIONE!!! SCRIPT UTILIZZABILE SOLO PER MIGRAZIONE COSTI_DEL_DIPENDENTE PREVENTIVO
--
--  Lo script e le viste su cui ? basato assumono che i dati in COSTI_PREV siano targati con
--  ANNO_GEST=n-1 e ANNO_PREV in n, n+1, n+2
-- (ad esempio se  ESERCIZIO = 2004 deve essere ANNO_GEST=2003 e ANNO_PREV in 2004,2005,2006)
--
-- Lo script utilizza l'RBSBIG
--
-- 30/11/2006
-- Per evitare incongruenze tra i dati di SIGLA e quelli di NSIP dovuti ad un disallineamento
-- di CNR_ANADIP, ? stata caricata un'anagrafica di previsione (avente anno_rif = aEsAnnoPrev e
-- mese_rif = 0) dalla quale vengono letti i dati anagrafici
--
-- 29/09/2011
-- Valorizzato nella tabella COSTO_DEL_DIPENDENTE il flag FL_RAPPORTO13 con il valore Y solo
-- per le matricole che hanno il rapporto 13
-- =================================================================================================
--
begin

 IBMUTL015.SETRBSBIG;
 -- =================================================================================================
 -- Esiste l'indicazione dell'UNITA_ORGANIZZATIVA
 -- =================================================================================================
 INSERT INTO COSTO_DEL_DIPENDENTE
       (ESERCIZIO,
        TI_PREV_CONS,
        ID_MATRICOLA,
        TI_APPARTENENZA,
        TI_GESTIONE,
        CD_ELEMENTO_VOCE,
        CD_UNITA_ORGANIZZATIVA,
        IM_A1,
        IM_A2,
        IM_A3,
        DT_SCARICO,
        DACR,
        UTCR,
        DUVA,
        UTUV,
        PG_VER_REC,
        IM_ONERI_CNR_A1,
        IM_ONERI_CNR_A2,
        IM_ONERI_CNR_A3,
        IM_TFR_A1,
        IM_TFR_A2,
        IM_TFR_A3,
        TI_RAPPORTO,
        NOMINATIVO,
        DT_SCAD_CONTRATTO,
        CD_LIVELLO_1,
        CD_LIVELLO_2,
        CD_LIVELLO_3,
        CD_PROFILO,
        DS_PROFILO,
        MESE,
	      ORIGINE_FONTI,
	      FL_RAPPORTO13)
 SELECT  A.ESERCIZIO,
        'P',
        TO_CHAR(A.matricola),
        A.ti_appartenenza,
        A.ti_gestione,
        A.cd_elemento_voce,
        A.cd_unita_organizzativa,
        A.im_a1,
        A.im_a2,
        A.im_a3,
        TO_DATE(NULL),
        SYSDATE,
        '$$$$$MIGRAZIONE$$$$',
        SYSDATE,
        '$$$$$MIGRAZIONE$$$$',
        1,
        A.im_oneri_cnr_a1,
        A.im_oneri_cnr_a2,
        A.im_oneri_cnr_a3,
        A.im_tfr_a1,
        A.im_tfr_a2,
        A.im_tfr_a3,
        DECODE(B.rapp_impiego,'01','IND','05','IND','DET'),
        B.nominativo,
        B.data_cessazione,
        B.livello_1,
        B.livello_2,
        B.livello_3,
        B.profilo,
        B.desc_profilo,
        bMese,
	      Decode(ltrim(rtrim(B.TIPO_CONTRATTO)), '01','FIN','11','FIN','00','FIN',null,'FIN','FES'),
	      Decode(ltrim(rtrim(B.RAPP_IMPIEGO)), '13','Y','N')
 FROM    VM_COSTI_DIP_PREV_VIEW A, CNR_ANADIP B
 WHERE   A.ESERCIZIO = aEsAnnoPrev AND
        A.cd_unita_organizzativa IS NOT NULL AND
        B.matricola = A.matricola And
        /*
        B.anno_rif = aEsAnnoPrev-1 And
        B.mese_rif = (Select Max(p.mese_rif)
                      From CNR_ANADIP P
                      Where p.anno_rif = b.anno_rif
                        And p.matricola = b.matricola) And
        */
        B.anno_rif = aEsAnnoPrev And
        B.mese_rif = bMese And
        EXISTS
           (SELECT 1
            FROM   UNITA_ORGANIZZATIVA C
            WHERE  C.cd_unita_organizzativa = A.cd_unita_organizzativa);
 -- =================================================================================================
 -- Non esiste l'indicazione dell'UNITA_ORGANIZZATIVA
 -- =================================================================================================
 begin
 INSERT INTO COSTO_DEL_DIPENDENTE
       (ESERCIZIO,
        TI_PREV_CONS,
        ID_MATRICOLA,
        TI_APPARTENENZA,
        TI_GESTIONE,
        CD_ELEMENTO_VOCE,
        CD_UNITA_ORGANIZZATIVA,
        IM_A1,
        IM_A2,
        IM_A3,
        DT_SCARICO,
        DACR,
        UTCR,
        DUVA,
        UTUV,
        PG_VER_REC,
        IM_ONERI_CNR_A1,
        IM_ONERI_CNR_A2,
        IM_ONERI_CNR_A3,
        IM_TFR_A1,
        IM_TFR_A2,
        IM_TFR_A3,
        TI_RAPPORTO,
        NOMINATIVO,
        DT_SCAD_CONTRATTO,
        CD_LIVELLO_1,
        CD_LIVELLO_2,
        CD_LIVELLO_3,
        CD_PROFILO,
        DS_PROFILO,
        MESE,
	      ORIGINE_FONTI,
	      FL_RAPPORTO13)
 SELECT  A.ESERCIZIO,
        'P',
        TO_CHAR(A.matricola),
        A.ti_appartenenza,
        A.ti_gestione,
        A.cd_elemento_voce,
        NULL,
        A.im_a1,
        A.im_a2,
        A.im_a3,
        TO_DATE(NULL),
        SYSDATE,
        '$$$$MIGRAZIONE$$$$',
        SYSDATE,
        '$$$$MIGRAZIONE$$$$',
        1,
        A.im_oneri_cnr_a1,
        A.im_oneri_cnr_a2,
        A.im_oneri_cnr_a3,
        A.im_tfr_a1,
        A.im_tfr_a2,
        A.im_tfr_a3,
        DECODE(B.rapp_impiego,'01','IND','05','IND','DET'),
        B.nominativo,
        B.data_cessazione,
        B.livello_1,
        B.livello_2,
        B.livello_3,
        B.profilo,
        B.desc_profilo,
        bMese,
	      Decode(ltrim(rtrim(B.TIPO_CONTRATTO)), '01','FIN','11','FIN','00','FIN',null,'FIN','FES'),
	      Decode(ltrim(rtrim(B.RAPP_IMPIEGO)), '13','Y','N')
 FROM    VM_COSTI_DIP_PREV_VIEW A, CNR_ANADIP B
 WHERE   A.ESERCIZIO = aEsAnnoPrev AND
        B.matricola = A.matricola And
        /*
        B.anno_rif = aEsAnnoPrev-1 And
        B.mese_rif = (Select Max(p.mese_rif)
                      From CNR_ANADIP P
                      Where p.anno_rif = b.anno_rif
                        And p.matricola = b.matricola) And
        */
        B.anno_rif = aEsAnnoPrev And
        B.mese_rif = bMese And
        NOT EXISTS
           (SELECT 1
            FROM   UNITA_ORGANIZZATIVA C
            WHERE  C.cd_unita_organizzativa = A.cd_unita_organizzativa);
	exception when dup_val_on_index then
	  for duplicati in
  	(select  A.im_a1, A.im_oneri_cnr_a1, A.im_tfr_a1,A.matricola,a.cd_elemento_voce
   	FROM    VM_COSTI_DIP_PREV_VIEW A, CNR_ANADIP B
 		WHERE   A.ESERCIZIO = aEsAnnoPrev AND
        B.matricola = A.matricola And
        B.anno_rif = aEsAnnoPrev And
        B.mese_rif = bMese And
        NOT EXISTS
           (SELECT 1
            FROM   UNITA_ORGANIZZATIVA C
            WHERE  C.cd_unita_organizzativa = A.cd_unita_organizzativa)
        and exists
        (select 1 from costo_del_dipendente d
            where
             d.esercizio = aEsAnnoPrev and
             d.mese = bMese and
             d.TI_PREV_CONS ='P'  and
             d.ti_appartenenza='D' and
             d.ti_gestione = 'S' and
             d.id_matricola=  a.matricola and
             d.cd_elemento_voce = a.cd_elemento_voce)
        ) loop
            update costo_del_dipendente
              set im_a1=im_a1+duplicati.im_a1,
              		im_oneri_cnr_a1 = im_oneri_cnr_a1+duplicati.im_oneri_cnr_a1,
              		im_tfr_a1=im_tfr_a1+duplicati.im_tfr_a1
             where
             esercizio = aEsAnnoPrev and
             mese = bMese and
             id_matricola = duplicati.matricola and
             TI_PREV_CONS = 'P'  and
             ti_appartenenza ='D' and
             ti_gestione = 'S' and
             cd_elemento_voce = duplicati.cd_elemento_voce;
            end loop;

end;

End;
/


