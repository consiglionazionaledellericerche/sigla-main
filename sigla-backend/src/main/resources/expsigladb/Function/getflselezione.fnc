CREATE OR REPLACE FUNCTION         getFlSelezione
--==================================================================================================
--
-- Date: 06/08/2003
-- Version: 1.3
--
-- Verifico se il documento ? associato ad un modello 1210.
-- Se aPgLettera ? NULL il documento non ? asociato ad una lettera di pagamento estero
-- Se aPgLettera ? NOT NULL allora il documento ? associato ad una lettera di pagamento estero.
--    Se aCdSospeso ? NULL non ? stato completato il modello 1210 e quindi non posso fare mandato
--    altrimenti torna Y
--
-- History:
--
-- Date: 17/09/2002
-- Version: 1.0
--
-- Creazione function
--
-- Date: 08/11/2002
-- Version: 1.1
--
-- Fix errore interno 2783 estesa gestione alla funzione getFlSelezione anche alle missoni
--
-- Date: 10/12/2002
-- Version: 1.2
--
-- Fix errore interno 2988 estesa gestione alla funzione getFlSelezione anche ad anticipi. Non
-- era controllato il fatto che un anticipo fosse rimborsato.
-- Era errato il ritorno della funzione in caso di documento associato a fondo economale
--
-- Date: 06/08/2003
-- Version: 1.3
--
-- Aggiunta gestione del flag fl_congelata, per i documenti di tipo Fattura Passiva.
--
-- Body:
--
--==================================================================================================
   (
    aTipoDocAmm VARCHAR2,
    aStatoPagamentoFondoEco VARCHAR2,
    aTiFattura VARCHAR2,
    aPgLettera NUMBER,
    aCdSospeso VARCHAR2,
    isMissioneAssCompenso VARCHAR2,
    aImportoMissione NUMBER,
    aImportoAnticipo NUMBER,
    aImportoRimborso NUMBER,
	aFlCongelata VARCHAR2,
	aStatoLiquidazione VARCHAR2 default null
   ) RETURN VARCHAR2 IS
   isSelezionePerMandato VARCHAR2(1);
   isNotSelezionePerMandato VARCHAR2(1);

BEGIN

   isSelezionePerMandato:='Y';
   isNotSelezionePerMandato:='N';

   -------------------------------------------------------------------------------------------------
   -- Composizione del valore di ritorno

   -- Gestione specifiche per ogni documento amministrativo

   IF    aTipoDocAmm = 'MISSIONE' THEN

         -- La missione con anticipo superiore all'importo o associata ad un compenso non pu? essere
         -- associata ad un mandato

         IF (
                (aImportoMissione - aImportoAnticipo) < 0
             OR
                isMissioneAssCompenso = 'Y'
            ) THEN
            RETURN isNotSelezionePerMandato;
         END IF;

   ELSIF aTipoDocAmm = 'ANTICIPO' THEN

         -- L'anticipo risulta associato ad un rimborso

         IF aImportoRimborso > 0 THEN
            RETURN isNotSelezionePerMandato;
         END IF;

   ELSIF (aTipoDocAmm = 'FATTURA_P' OR
          aTipoDocAmm = 'DOC_GENERICO') THEN

         -- Verifico se il documento ? associato ad un modello 1210.
         -- Se aPgLettera ? NOT NULL allora il documento ? associato ad una lettera di pagamento estero.
         --    Se aCdSospeso ? NULL non ? stato completato il modello 1210 e quindi non posso fare mandato

         IF (aPgLettera IS NOT NULL AND
             aCdSospeso IS NULL) THEN
            RETURN isNotSelezionePerMandato;
         END IF;

         -- Le note credito e debito non sono mai associabili direttamente ad un mandato

         IF aTipoDocAmm = 'FATTURA_P' THEN
            IF (aTiFattura != 'F' OR
			    aFlCongelata = 'Y') THEN
               RETURN isNotSelezionePerMandato;
            END IF;
         END IF;

   END IF;
		-- r.p. verifico nel caso che sia valorizzato lo stato liquidazione che sia Liquidabile
	  if aStatoLiquidazione is not null and aStatoLiquidazione != 'LIQ' then
		           RETURN isNotSelezionePerMandato;
		end if;
   -- Controllo generale per associazione del documento amministrativo al fondo economale

   IF aStatoPagamentoFondoEco = 'N' THEN
      RETURN isSelezionePerMandato;
   ELSE
      RETURN isNotSelezionePerMandato;
   END IF;

END getFlSelezione;
/


