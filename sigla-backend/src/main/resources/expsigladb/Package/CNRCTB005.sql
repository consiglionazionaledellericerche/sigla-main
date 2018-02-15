--------------------------------------------------------
--  DDL for Package CNRCTB005
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB005" as
--
-- CNRCTB005 - Package di gestione della copia di dati interesercizio
-- Date: 26/11/2002
-- Version: 1.4
--
-- Dependency: CNRCTB 000/002/015/055
--
-- History:
-- Date: 05/10/2001
-- Version: 1.0
-- Creazione
-- Date: 05/10/2001
-- Version: 1.1
-- razionalizzazione copia dei dati
--
-- Date: 08/11/2001
-- Version: 1.2
-- Eliminazione esercizio da STO
--
-- Date: 18/01/2002
-- Version: 1.3
-- Creazione dei saldi iniziali in VOCE_F_SALDI_CMP
--
-- Date: 26/11/2002
-- Version: 1.4
-- Eliminata copia automatica delle configurazioni base interesercizio usando l'esercizio 0 come template
--
-- Constants:
--

-- Functions e Procedures:

-- Azioni effettuate alla creazione di un nuovo esercizio
--  aEsercizio -> rowtype di esercizio in creazione

 procedure onCreazioneEsercizio(aEsercizio esercizio%rowtype);

end;
