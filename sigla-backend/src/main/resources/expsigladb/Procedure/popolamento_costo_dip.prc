CREATE OR REPLACE PROCEDURE         POPOLAMENTO_COSTO_DIP( aEsercizio IN NUMBER,aMese IN NUMBER, aUtente in varchar2) is

aSum1 number := 0;
aSum2 number := 0;
v_conta number :=0;
Begin

 IBMUTL015.SETRBSBIG;
 -- =================================================================================================
 -- Esiste l'indicazione dell'UNITA_ORGANIZZATIVA
 -- =================================================================================================

 select count(*) into v_conta
 from COSTO_DEL_DIPENDENTE
 where esercizio = aEsercizio
 and mese = aMese;

    if v_conta = 0 then

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
         SELECT A.esercizio,
                A.ti_prev_cons,     -- e' sempre 'C'
                TO_CHAR(A.matricola),
                A.ti_appartenenza,
                A.ti_gestione,
                Rtrim(Ltrim(A.cd_elemento_voce)),
                Rtrim(Ltrim(A.cd_unita_organizzativa)),
                A.importo,
                0,
                0,
                TO_DATE(NULL),
                SYSDATE,
                aUtente,
                SYSDATE,
                aUtente,
                1,
                A.importo_oneri_cnr,
                0,
                0,
                A.importo_tfr,
                0,
                0,
                DECODE(B.rapp_impiego,'01','IND','05','IND','DET'),
                B.nominativo,
                B.data_cessazione,
                B.livello_1,
                B.livello_2,
                B.livello_3,
                B.profilo,
                B.desc_profilo,
                A.mese,
                Decode(ltrim(rtrim(B.TIPO_CONTRATTO)), '01','FIN','11','FIN','00','FIN',null,'FIN','FES'),
	              Decode(ltrim(rtrim(B.RAPP_IMPIEGO)), '13','Y','N')
         FROM   VM_COSTI_DIP_GEST_VIEW A, CNR_ANADIP B
         WHERE  A.esercizio = aEsercizio And
            A.mese = aMese And
                A.cd_unita_organizzativa IS NOT NULL AND
                B.matricola = A.matricola And
                B.anno_rif = aEsercizio And
                B.mese_rif = aMese And
               (A.importo		!=0 or
                 A.importo_oneri_cnr!= 0 or
                 A.importo_tfr!=0) and
                EXISTS
                   (SELECT 1
                    FROM   UNITA_ORGANIZZATIVA C
                    WHERE  C.cd_unita_organizzativa = A.cd_unita_organizzativa);

         -- =================================================================================================
         -- Non esiste l'indicazione dell'UNITA_ORGANIZZATIVA
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
                A.ti_prev_cons,     -- e' sempre 'C'
                TO_CHAR(A.matricola),
                A.ti_appartenenza,
                A.ti_gestione,
                Rtrim(Ltrim(A.cd_elemento_voce)),
                NULL,
                A.importo,
                0,
                0,
                TO_DATE(NULL),
                SYSDATE,
                aUtente,
                SYSDATE,
                aUtente,
                1,
                A.importo_oneri_cnr,
                0,
                0,
                A.importo_tfr,
                0,
                0,
                DECODE(B.rapp_impiego,'01','IND','05','IND','DET'),
                B.nominativo,
                B.data_cessazione,
                B.livello_1,
                B.livello_2,
                B.livello_3,
                B.profilo,
                B.desc_profilo,
                A.mese,
                Decode(ltrim(rtrim(B.TIPO_CONTRATTO)), '01','FIN','11','FIN','00','FIN',null,'FIN','FES'),
	              Decode(ltrim(rtrim(B.RAPP_IMPIEGO)), '13','Y','N')
         FROM   VM_COSTI_DIP_GEST_VIEW A, CNR_ANADIP B
         WHERE  A.esercizio = aEsercizio And
            A.mese = aMese And
                B.matricola = A.matricola And
                B.anno_rif = aEsercizio And
                B.mese_rif = aMese And
                (A.importo		!=0 or
                 A.importo_oneri_cnr!= 0 or
                 A.importo_tfr!=0) and
                NOT EXISTS
                   (SELECT 1
                    FROM   UNITA_ORGANIZZATIVA C
                    WHERE  C.cd_unita_organizzativa = A.cd_unita_organizzativa);

			begin

					select sum ( IMPORTO+ONERI_CNR+TFR) into aSum1
					from cnr_costi_gest
					where cnr_costi_gest.ANNO = aEsercizio
					and MESE = aMese;

            Dbms_Output.put_line('La somma di IMPORTO+ONERI_CNR+TFR ='||aSum1);

					select sum( IM_A1+IM_ONERI_CNR_A1+IM_TFR_A1) into aSum2
					from costo_del_dipendente
					where costo_del_dipendente.ESERCIZIO = aEsercizio
					and costo_del_dipendente.MESE = aMese;

            Dbms_Output.put_line('La somma di IM_A1+IM_ONERI_CNR_A1+IM_TFR_A1='||aSum2);


					if aSum1 = aSum2 then
						commit;
					else
						rollback;
								Dbms_Output.put_line('La somma di IMPORTO+ONERI_CNR+TFR ='||aSum1||' ? diversa dalla somma IM_A1+IM_ONERI_CNR_A1+IM_TFR_A1='||aSum2);
								Dbms_Output.put_line('? stato effettuato il rollback e non la commit!!');
					end if;
			end;

    else
        Dbms_Output.put_line('I dati per l''anno '||aEsercizio||' e per il mese '||aMese||' sono gi? stati inseriti!');
    end if;
end;
/


