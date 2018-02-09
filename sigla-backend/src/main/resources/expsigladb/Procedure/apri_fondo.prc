CREATE OR REPLACE Procedure APRI_FONDO (AES      In      NUMBER,
                                        aCdCds   In      VARCHAR2,
                                        aCdUO    In      VARCHAR2,
                                        aCdTerzo In      NUMBER,
                                        aImFondo In      NUMBER) As
-- Generatore di pratica finanziaria per apertura del fondo economale
-- Il metodo genera la pratica contabile relativa all'apertura/incremento del fondo economale
--
-- Vengono generati i seguenti documenti:
--
-- 1. Obbligazione su partita di giro tronca intestata all'economo letto dalla testata valida del fondo (deve essere aperto)
-- 2. Documento generico di apertura del fondo economale
-- 3. Mandato di pagamento all'economo collegato al generico
--
-- ====================================================
-- Parametri da impostare
-- ====================================================
 --
 -- Esercizio del fondo
 -- Cds del fondo
 -- Uo del fondo
 -- Codice del terzo economo
 -- Importo del mandato di apertura o incremento del fondo

 -- Descrizione della pratica (esempio nome del fondo da aprire o altro)
  aDescPratica VARCHAR2(150) := 'Fondo economale 2005';

 -- Utente che effettua l'operazione
 aUser VARCHAR2(20):='$$$CED_SF$$$';

BEGIN
  CNRCTB130.creaPraticaAperturaFondo(
   aEs,
   aCdCds,
   aCdUO,
   aCdTerzo,
   aDescPratica,
   aImFondo,
   aUser
  );
END;
/


