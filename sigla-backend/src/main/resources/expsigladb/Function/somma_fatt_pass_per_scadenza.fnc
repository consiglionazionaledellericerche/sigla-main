CREATE OR REPLACE FUNCTION SOMMA_FATT_PASS_PER_SCADENZA
   (CD_CDS_OBB     VARCHAR2,
    ES_OBB         NUMBER,
    ES_ORI_OBB     NUMBER,
    PG_OBB         NUMBER,
    PG_SCAD        NUMBER)

    --CD_CDS_FATT    VARCHAR2,
    --CD_UO_FATT     VARCHAR2,
    --ES_FATT        NUMBER,
    --PG_FATT        NUMBER)

RETURN NUMBER IS

RISULTATO NUMBER;

BEGIN
 SELECT Nvl(
            Sum(
                Decode(FP.TI_FATTURA, 'C', -Nvl(FPR.IM_IMPONIBILE, 0), Nvl(FPR.IM_IMPONIBILE, 0))+
                Decode(FP.TI_FATTURA, 'C', -Nvl(FPR.IM_IVA, 0), Nvl(FPR.IM_IVA, 0))
               )
            , 0)
 INTO   RISULTATO
 FROM   FATTURA_PASSIVA_RIGA FPR, FATTURA_PASSIVA FP
 WHERE  --FPR.CD_CDS                       = CD_CDS_FATT AND
        --FPR.CD_UNITA_ORGANIZZATIVA       = CD_UO_FATT  AND
        --FPR.ESERCIZIO                    = ES_FATT     AND
        --FPR.PG_FATTURA_PASSIVA           = PG_FATT     AND
        FPR.CD_CDS_OBBLIGAZIONE          = CD_CDS_OBB  AND
        FPR.ESERCIZIO_OBBLIGAZIONE       = ES_OBB      AND
        FPR.ESERCIZIO_ORI_OBBLIGAZIONE   = ES_ORI_OBB  AND
        FPR.PG_OBBLIGAZIONE              = PG_OBB      AND
        FPR.PG_OBBLIGAZIONE_SCADENZARIO  = PG_SCAD     And
        FP.CD_CDS                        = FPR.CD_CDS                      AND
        FP.CD_UNITA_ORGANIZZATIVA        = FPR.CD_UNITA_ORGANIZZATIVA      AND
        FP.ESERCIZIO                     = FPR.ESERCIZIO                   AND
        FP.PG_FATTURA_PASSIVA            = FPR.PG_FATTURA_PASSIVA And
        FPR.STATO_COFI != 'A';

 SELECT RISULTATO +
        Nvl(
            Sum(Nvl(FAR.IM_IMPONIBILE, 0) + Nvl(FAR.IM_IVA, 0))
            , 0)
 INTO   RISULTATO
 FROM   FATTURA_ATTIVA_RIGA FAR, FATTURA_ATTIVA FA
 WHERE  FAR.CD_CDS_OBBLIGAZIONE          = CD_CDS_OBB  AND
        FAR.ESERCIZIO_OBBLIGAZIONE       = ES_OBB      AND
        FAR.ESERCIZIO_ORI_OBBLIGAZIONE   = ES_ORI_OBB  AND
        FAR.PG_OBBLIGAZIONE              = PG_OBB      AND
        FAR.PG_OBBLIGAZIONE_SCADENZARIO  = PG_SCAD     And
        FA.CD_CDS                        = FAR.CD_CDS                      AND
        FA.CD_UNITA_ORGANIZZATIVA        = FAR.CD_UNITA_ORGANIZZATIVA      AND
        FA.ESERCIZIO                     = FAR.ESERCIZIO                   AND
        FA.PG_FATTURA_ATTIVA             = FAR.PG_FATTURA_ATTIVA And
        FA.TI_FATTURA = 'C' And
        FAR.STATO_COFI != 'A';

RETURN NVL(RISULTATO,0);
END;
/


