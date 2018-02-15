--------------------------------------------------------
--  DDL for Package Body CNRCTB005
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB005" is

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
