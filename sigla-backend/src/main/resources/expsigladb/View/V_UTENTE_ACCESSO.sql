--------------------------------------------------------
--  DDL for View V_UTENTE_ACCESSO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_UTENTE_ACCESSO" ("CD_UTENTE", "CD_ACCESSO", "CD_UNITA_ORGANIZZATIVA", "ESERCIZIO", "ORIGINE") AS 
  SELECT
--
-- Date: 16/11/2002
-- Version: 1.2
--
-- Vista che restituisce l'elenco di tutti gli accessi per utente e unità organizzativa
-- unendo gli accessi diretti, ereditati dai gruppi, dall'utente template e dai
-- gruppi dell'utente template.
-- Non restituisce righe distinte.
-- La colonna ORIGINE vale:
-- - 'UTENTE_UNITA_ACCESSO' se l'accesso viene direttamente dall'utente
-- - 'RUOLO_ACCESSO' se l'accesso viene ereditato dai gruppi
-- - 'UTENTE_TEMPL_UNITA_ACCESSO' se l'accesso viene dall'utente template
-- - 'RUOLO_TEMPL_ACCESSO' se l'accesso viene ereditato dai gruppi dell'utente template
--
-- History:
--
-- Date: 11/11/2002
-- Version: 1.0
-- Creazione
--
-- Date: 14/11/2002
-- Version: 1.1
-- Corretta aggiungendo V_UNITA_ORGANIZZATIVA per aggiunger
-- l'elenco degli accessi dati dalle unità organizzative padri
--
-- Date: 16/11/2002
-- Version: 1.2
-- Aggiunto esercizio e corretto commento (eliminazione dei punti e virgola)
--
-- Body:
--
	UTENTE_UNITA_ACCESSO.CD_UTENTE,
	UTENTE_UNITA_ACCESSO.CD_ACCESSO,
	V_UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
	V_UNITA_ORGANIZZATIVA.ESERCIZIO,
	'UTENTE_UNITA_ACCESSO'
FROM
	UTENTE_UNITA_ACCESSO,
	V_UNITA_ORGANIZZATIVA
WHERE
	V_UNITA_ORGANIZZATIVA.CD_UO = UTENTE_UNITA_ACCESSO.CD_UNITA_ORGANIZZATIVA
UNION ALL
SELECT
	UTENTE_UNITA_RUOLO.CD_UTENTE,
	RUOLO_ACCESSO.CD_ACCESSO,
	V_UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
	V_UNITA_ORGANIZZATIVA.ESERCIZIO,
	'RUOLO_ACCESSO'
FROM
	UTENTE_UNITA_RUOLO,
	RUOLO_ACCESSO,
	RUOLO,
	V_UNITA_ORGANIZZATIVA
WHERE
	RUOLO_ACCESSO.CD_RUOLO = RUOLO.CD_RUOLO AND
	RUOLO.CD_RUOLO = UTENTE_UNITA_RUOLO.CD_RUOLO AND
	V_UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA = UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA
UNION ALL
SELECT
	UTENTE.CD_UTENTE,
	UTENTE_UNITA_ACCESSO.CD_ACCESSO,
	V_UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
	V_UNITA_ORGANIZZATIVA.ESERCIZIO,
	'UTENTE_TEMPL_UNITA_ACCESSO'
FROM
	UTENTE_UNITA_ACCESSO,
	UTENTE,
	V_UNITA_ORGANIZZATIVA
WHERE
	UTENTE_UNITA_ACCESSO.CD_UTENTE = UTENTE.CD_UTENTE_TEMPL AND
	V_UNITA_ORGANIZZATIVA.CD_UO = UTENTE_UNITA_ACCESSO.CD_UNITA_ORGANIZZATIVA
UNION ALL
SELECT
	UTENTE.CD_UTENTE,
	RUOLO_ACCESSO.CD_ACCESSO,
	V_UNITA_ORGANIZZATIVA.CD_UNITA_ORGANIZZATIVA,
	V_UNITA_ORGANIZZATIVA.ESERCIZIO,
	'RUOLO_TEMPL_ACCESSO'
FROM
	UTENTE_UNITA_RUOLO,
	RUOLO_ACCESSO,
	RUOLO,
	UTENTE,
	V_UNITA_ORGANIZZATIVA
WHERE
	RUOLO.CD_RUOLO = UTENTE_UNITA_RUOLO.CD_RUOLO AND
	RUOLO_ACCESSO.CD_RUOLO = RUOLO.CD_RUOLO AND
	UTENTE_UNITA_RUOLO.CD_UTENTE = UTENTE.CD_UTENTE_TEMPL AND
	V_UNITA_ORGANIZZATIVA.CD_UO = UTENTE_UNITA_RUOLO.CD_UNITA_ORGANIZZATIVA
UNION ALL
SELECT
	UTENTE_UNITA_ACCESSO.CD_UTENTE,
	UTENTE_UNITA_ACCESSO.CD_ACCESSO,
	'*',
	0,
	'UTENTE_*_ACCESSO'
FROM
	UTENTE_UNITA_ACCESSO
WHERE
	UTENTE_UNITA_ACCESSO.CD_UNITA_ORGANIZZATIVA = '*'
;

   COMMENT ON TABLE "V_UTENTE_ACCESSO"  IS 'Vista che restituisce l''elenco di tutti gli accessi per utente e unità organizzativa
unendo gli accessi diretti, ereditati dai gruppi, dall''utente template e dai
gruppi dell''utente template.
Non restituisce righe distinte.
La colonna ORIGINE vale:
- ''UTENTE_UNITA_ACCESSO'' se l''accesso viene direttamente dall''utente
- ''RUOLO_ACCESSO'' se l''accesso viene ereditato dai gruppi
- ''UTENTE_TEMPL_UNITA_ACCESSO'' se l''accesso viene dall''utente template
- ''RUOLO_TEMPL_ACCESSO'' se l''accesso viene ereditato dai gruppi dell''utente template';
