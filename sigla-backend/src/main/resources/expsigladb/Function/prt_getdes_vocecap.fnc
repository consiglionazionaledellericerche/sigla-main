CREATE OR REPLACE FUNCTION Prt_Getdes_Vocecap
(atab VARCHAR2, aEser VARCHAR2, aTiApp VARCHAR2, aTiGes VARCHAR2, aCdVoce VARCHAR2, aTiVoce VARCHAR2)
RETURN VARCHAR2 IS
--
--Date: 16/01/2004
--Version: 1.1
--
--Funzione che estrae la descrizione del capitolo dalla tabella elemento_voce, oppure voce_f
--
--
--History:
--
--Date: 27/10/2003
--Version: 1.0
--Creazione Cineca (per ottimizzazione stampe)
--
--Date: 16/01/2004
--Version: 1.1
--AMPLIATA DEFINIZIONE DI ADESVOCE
--
aDesVoce VARCHAR2(200);
BEGIN
IF aTab='F' THEN
	 SELECT ds_voce INTO aDesVoce FROM VOCE_F
	 WHERE ESERCIZIO=aEser AND
	 ti_gestione=aTiGes AND
	 ti_appartenenza=aTiApp AND
	 cd_voce=aCdVoce AND
	 ti_voce=aTiVoce;
	RETURN aDesVoce;
ELSIF atab='E' THEN
SELECT ds_elemento_voce INTO aDesVoce FROM ELEMENTO_VOCE
	 WHERE ESERCIZIO=aEser AND
	 ti_gestione=aTiGes AND
	 ti_appartenenza=aTiApp AND
	 cd_elemento_voce=aCdVoce AND
	 ti_elemento_voce=aTiVoce;
	RETURN aDesVoce;
END IF;
EXCEPTION WHEN NO_DATA_FOUND THEN
RETURN NULL;
 -- IBMERR001.RAISE_ERR_GENERICO('elemento voce non trovato:'||aCdvoce);
END;
/


