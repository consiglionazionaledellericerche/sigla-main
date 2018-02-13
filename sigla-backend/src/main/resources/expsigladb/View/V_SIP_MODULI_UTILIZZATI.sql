--------------------------------------------------------
--  DDL for View V_SIP_MODULI_UTILIZZATI
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "V_SIP_MODULI_UTILIZZATI" ("ESERCIZIO", "PG_PROGETTO", "TIPO_FASE", "ERR") AS 
  Select
--
-- Date: 09/02/2007
-- Version: 1.2
--
-- Vista di estrazione di progetti impiegati in fase Previsionale
--
--
-- History:
--
-- Date: 01/01/2006
-- Version: 1.0
-- Creazione
--
-- Date: 09/11/2006
-- Version: 1.1
-- Aggiunto l'anno e la tipologia PREV/GEST nella selezione del progetto
--
-- Date: 09/02/2007
-- Version: 1.2
-- Aggiunto la descrizione più puntuale dell'errore
--
-- Body:
--
Distinct esercizio, pg_progetto, 'P',
       'Impossibile terminare il Modulo, risultano previsioni nel PdGP per l''esercizio '||esercizio
From V_SIP_MODULI_NUOVA_PREVISIONE
Union
Select Distinct esercizio, pg_progetto, 'P',
       'Impossibile eliminare il Modulo, risulta associato a linee di attivita'' valide nell''anno '||esercizio
From   v_linea_attivita_valida
Where pg_progetto Is Not Null
Union
Select Distinct esercizio, pg_progetto, 'G',
       'Impossibile eliminare il Modulo, risulta associato a linee di attivita'' valide nell''anno '||esercizio
From   v_linea_attivita_valida
Where pg_progetto Is Not Null
Union
Select esercizio, pg_progetto, 'G', 'Impossibile terminare il Modulo, risulta associato al PdG per l''esercizio '||esercizio
From (Select Distinct esercizio, pg_progetto
      From   pdg_modulo_spese_gest
      Union
      Select Distinct esercizio, pg_progetto
      From   pdg_modulo_entrate_gest);
