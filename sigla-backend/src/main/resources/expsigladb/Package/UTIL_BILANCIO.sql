--------------------------------------------------------
--  DDL for Package UTIL_BILANCIO
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "UTIL_BILANCIO" is

-- PACKAGE DI UTILITA' PER LE ATTIVITA' DI CHIUSURA BILANCIO DI FINE ANNO (GIUGNO)
-- CONTIENE ANCHE LE PROCEDURE PER LE ESTRAZIONI DEI RIEPILOGHI COMMERCIALI PER CINTI
-- CONTIENE LE PROCEDURE CHE PRIMA ERANO SCIOLTE SUL DB

PROCEDURE Cancella_Saldi (AES IN NUMBER, IN_CDS IN VARCHAR2);
PROCEDURE CANCELLA_ALL_SALDI (AES IN NUMBER);
PROCEDURE Aggiorna_Saldi (AES IN NUMBER, IN_CDS IN VARCHAR2);
PROCEDURE Aggiorna_ALL_Saldi (AES IN NUMBER);
PROCEDURE chiudi_provv_all_cds (aes in number);
PROCEDURE Chiudi_Cds (aes in number, ACDS IN VARCHAR2);
PROCEDURE Annulla_Chiusura_Cds (ines in number, ACDS IN VARCHAR2);
PROCEDURE Annulla_All_Chiusure (aes IN NUMBER);
PROCEDURE STORNO_SP_FINALE_A_UT_ES_PREC (aES In NUMBER, aCDS In VARCHAR2);
PROCEDURE Annulla_Chiusura_Gruppo (aes IN NUMBER);
PROCEDURE Popola_Sp_Per_Conto_Uo (INTIPO         IN VARCHAR2, -- SP / CE
                                  inEs           IN NUMBER,  -- obbligatorio
                                  INDATA	 IN VARCHAR2,  -- obbligatorio
                                  IS_CO          IN CHAR); -- obbligatorio


End;
