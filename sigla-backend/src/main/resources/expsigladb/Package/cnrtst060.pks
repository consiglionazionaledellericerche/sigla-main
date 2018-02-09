CREATE OR REPLACE PACKAGE CNRTST060 AS
--
-- CNRTST060 - Package per la generazione di dati di test per COSTO DEL DIPENDENTE
--
-- >>>>> Date: 09/10/2001
-- >>>>> Version: 1.0
--
--
-- Dependency: CNRCTB 001/060 IBMCST001 IBMUTL001
--
-- History:
--
-- Constants:

CD_TITOLO_PERSONALE_TEST CONSTANT varchar2(10) := '1.03';

-- Functions & Procedures:
-- Creazione di dettagli di costo del dipendente fittizi
--
-- Data l'UO CDS crea 30 matricole (identificate da progressivo = cd_cds*1000 + i dove i in (1..30))
-- Per ogni matricola inserisce costi del personale su 4 capitoli di spesa cds del titolo CD_TITOLO_PERSONALE_TEST
-- con codice proprio in (001..004)
--
-- Gli importi sui 3 anni e sui 4 capitoli sono inseriti con la seguente logica:
--
-- anno 1: 100 + 10 * n. capitolo (da 1 a 4)
-- anno 2: 200 + 10 * n. capitolo (da 1 a 4)
-- anno 3: 500 + 10 * n. capitolo (da 1 a 4)
--
-- es. anno 1 capitolo 002 = 100 + 10*2 = 120
--

 procedure creaCDPTest(aUOCDS unita_organizzativa%rowtype);

end;
/


