CREATE OR REPLACE FUNCTION Prt_Getdati_Confcnr (aEser VARCHAR2, aKeyPRIM VARCHAR2, aKeySEC VARCHAR2)
RETURN VARCHAR2 IS
--
--Date: 27/10/2003
--Version: 1.0
--
--Funzione che estrae i dati dalla tabella di configurazione_cnr in base ai valori di chiave primaria
--e secondaria
--
--History:
--
--Date: 27/10/2003
--Version: 1.0
--Creazione Cineca (per ottimizzazione stampe)
--
aVAL01 VARCHAR2(100);
BEGIN
	 SELECT VAL01 INTO aVAL01 FROM CONFIGURAZIONE_CNR
	 WHERE ESERCIZIO=aEser AND
	 cd_chiave_primaria=akeyprim AND
	 cd_chiave_secondaria=akeysec;
	RETURN aval01;
EXCEPTION WHEN NO_DATA_FOUND THEN
RETURN NULL;
Ibmerr001.RAISE_ERR_GENERICO('valore non trovato per le chiavi:'||aKeyPrim ||',' || aKeySec);
END;
/


