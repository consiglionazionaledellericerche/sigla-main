--------------------------------------------------------
--  DDL for Package CNRUTIL
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRUTIL" as
--
-- CNRUTIL - Package di utilità per l'estrapolazione di dati da tipi PL/SQL a SQL
-- Date: 06/02/2006
-- Version: 1.03
--
--
-- Dependency:
--
-- History:
--
-- Date: 22/03/2005
-- Version: 1.0
-- Creazione
--
-- Date: 22/03/2005
-- Version: 1.01
-- Aggiunta metodo loadTerzoVersCori
--
-- Date:
-- Version: 1.02
-- Aggiunta metodi etichette Obbligazione/Impegno
--
-- Date: 06/02/2006
-- Version: 1.03
-- Aggiunta metodi etichette Workpackage/GAE

  terzoVersCori VARCHAR2(100);

-- Functions e Procedures:

-- Estrae il codice dell'UO del SAC responsabile del versamento CORI accentrato

 function getUOVersCori(aEs number) return VARCHAR2;

-- Ottiene il codice terzo responsabile del versamento CORI accentrato

 function getTerzoVersCori(aEs number) return VARCHAR2;

-- Carica il codice terzo responsabile del versamento CORI accentrato

 Procedure loadTerzoVersCori(aEs number);

-- Estrae l'etichetta della dicitura Obbligazione/Impegno

 function getLabelObbligazione Return VARCHAR2;

-- Estrae l'etichetta della dicitura Obbligazione/Impegno

 function getLabelObbligazioneMin Return VARCHAR2;

-- Estrae l'etichetta della dicitura Obbligazione/Impegno

 function getLabelObbligazioni Return VARCHAR2;

-- Estrae l'etichetta della dicitura Obbligazione/Impegno

 function getLabelObbligazioniMin Return VARCHAR2;

-- Estrae l'etichetta della dicitura Obbligazione/Impegno

 function getLabelObbl Return VARCHAR2;

-- se true l'etichetta è Impegno

 function isLabelObbligazione Return Boolean;

-- se true l'etichetta è GAE

 function isLabelGae Return Boolean;

-- Estrae l'etichetta della dicitura Workpackage/GAE

 function getLabelGae Return VARCHAR2;

-- Estrae l'etichetta della dicitura Workpackage/GAE

 function getLabelGaeEst Return VARCHAR2;
End;
