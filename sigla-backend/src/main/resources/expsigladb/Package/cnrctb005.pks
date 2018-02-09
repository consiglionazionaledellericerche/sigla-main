CREATE OR REPLACE package CNRCTB005 as
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
/


CREATE OR REPLACE package body CNRCTB005 is

 procedure onCreazioneEsercizio(aEsercizio esercizio%rowtype) is
 begin

  -- Copia dei dati relativi ai gruppi
/* QUESTA PROCEDURA DI COPIA DATI INTERESERCIZIO RISULTA ESSERE OBSOLETA
   SARA' CURA DEI PROGRAMMI DI RIBALTAMENTO DA ESERCIZIO A ESERCIZIO, REPLICARE LE SEGUENTI STRUTTURE
   COPIANDOLE DALL'ESERCIZIO PRECEDENTE E NON DALL'ESERCIZIO 0 COME FANNO I SEGUENTI PROGRAMMI
  begin
   CNRCTB002.coie_VOCE_EP (aEsercizio);
  exception when dup_val_on_index then
   null;
  end;

  -- Copia dei dati relativi alla lunghezza delle chiavi

  begin
   CNRCTB015.coie_LUNGHEZZA_CHIAVI (aEsercizio);
  exception when dup_val_on_index then
   null;
  end;

  -- Copia dei dati relativi all'ELEMENTO_VOCE

  begin
   CNRCTB000.coie_ELEMENTO_VOCE (aEsercizio);
  exception when dup_val_on_index then
   null;
  end;
*/

  CNRCTB055.inizializzaBilancioPreventivo(aEsercizio.esercizio, aEsercizio.cd_cds, aEsercizio.utcr);

  CNRCTB055.CREAESPLSALDI(aEsercizio.esercizio, aEsercizio.cd_cds, aEsercizio.utcr);
 end;
end;
/


