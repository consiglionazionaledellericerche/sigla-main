CREATE OR REPLACE procedure controlla_squadra_cdr_cmp_rev (aEs In NUMBER) as
BEGIN

FOR aCDS in (select cd_unita_organizzativa
             from   unita_organizzativa
             where  --cd_unita_organizzativa = '317' and
                    fl_cds = 'Y') LOOP


FOR RIGHE_SPORCHE IN
(SELECT SUBSTR(CD_CENTRO_RESPONSABILITA, 1, 3) cdr, CDR.CD_VOCE, SUM(IM_MANDATI_REVERSALI_PRO)+ SUM(IM_MANDATI_REVERSALI_IMP) reversali_CDR,
	   (SELECT SUM(IM_MANDATI_REVERSALI)
	   	FROM VOCE_F_SALDI_CMP
		WHERE ESERCIZIO = aEs AND
						 CD_VOCE  = CDR.CD_VOCE AND
						 TI_GESTIONE ='E') reversali_CMP,
SUM(IM_MANDATI_REVERSALI_PRO)+ SUM(IM_MANDATI_REVERSALI_IMP)-
	   (SELECT SUM(IM_MANDATI_REVERSALI)
	   	FROM VOCE_F_SALDI_CMP
		WHERE ESERCIZIO = aEs AND
						 CD_VOCE  = CDR.CD_VOCE AND
						 TI_GESTIONE ='E') differenza
FROM  VOCE_F_SALDI_CDR_LINEA CDR
WHERE ESERCIZIO = aEs
AND TI_GESTIONE = 'E'
AND Cnrutl001.GETCDSFROMCDR(cd_centro_responsabilita) = aCDS.cd_unita_organizzativa
GROUP BY Substr(CD_CENTRO_RESPONSABILITA, 1, 3), CDR.CD_VOCE) LOOP

IF RIGHE_SPORCHE.DIFFERENZA != 0 then
 dbms_output.put_line ('Differenza sul CDS '||RIGHE_SPORCHE.cdr||'       Voce '||RIGHE_SPORCHE.cd_voce||
                       '         Reversali sul CDR '||Ltrim(To_Char(RIGHE_SPORCHE.reversali_cdr, '999g999g999g999g999g990d99'))||
                       '         Reversali su CMP '||Ltrim(To_Char(RIGHE_SPORCHE.REVERSALI_CMP, '999g999g999g999g999g990d99'))||
                       '         differenza '||Ltrim(To_Char(RIGHE_SPORCHE.DIFFERENZA, '999g999g999g999g999g990d99')));
end if;

END LOOP;

end loop;
END;
/


