CREATE OR REPLACE package CNRUTIL as
--
-- CNRUTIL - Package di utilit? per l'estrapolazione di dati da tipi PL/SQL a SQL
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

-- se true l'etichetta ? Impegno

 function isLabelObbligazione Return Boolean;

-- se true l'etichetta ? GAE

 function isLabelGae Return Boolean;

-- Estrae l'etichetta della dicitura Workpackage/GAE

 function getLabelGae Return VARCHAR2;

-- Estrae l'etichetta della dicitura Workpackage/GAE

 function getLabelGaeEst Return VARCHAR2;
End;


CREATE OR REPLACE package body CNRUTIL Is

 Function getUOVersCori(aEs number) return VARCHAR2 is
  aUOCDS VARCHAR2(1000);
 Begin
    return cnrctb020.getUOVersCori(aEs).cd_unita_organizzativa;
 end;

 Function getTerzoVersCori(aEs number) return VARCHAR2 is
  aTerzoCDS VARCHAR2(1000);
  aCdModPag VARCHAR2(1000);
  aPgBanca NUMBER;
 Begin
     If terzoVersCori Is Null Then
        cnrctb080.getTerzoPerUO(getUOVersCori(aEs), aTerzoCDS, aCdModPag, aPgBanca,aEs);
        Return(aTerzoCDS);
     Else
        Return(terzoVersCori);
     End If;
 end;

 Procedure loadTerzoVersCori(aEs number) Is
  aTerzoCDS VARCHAR2(1000);
  aCdModPag VARCHAR2(1000);
  aPgBanca NUMBER;
 Begin
    cnrctb080.getTerzoPerUO(getUOVersCori(aEs), aTerzoCDS, aCdModPag, aPgBanca,aEs);
    terzoVersCori := aTerzoCDS;
 End;

 Function getLabelObbligazione Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelObbligazione() Then
     Return 'Obbligazione';
   Else
     Return 'Impegno';
   End If;
 End;

 Function getLabelObbligazioneMin Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelObbligazione() Then
     Return 'obbligazione';
   Else
     Return 'impegno';
   End If;
 End;

 Function getLabelObbligazioni Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelObbligazione() Then
     Return 'Obbligazioni';
   Else
     Return 'Impegni';
   End If;
 End;

 Function getLabelObbligazioniMin Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelObbligazione() Then
     Return 'obbligazioni';
   Else
     Return 'impegni';
   End If;
 End;

 function isLabelObbligazione Return Boolean Is
 Begin
    Return False;
 End;

 Function getLabelObbl Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelObbligazione() Then
     Return 'Obbl.';
   Else
     Return 'Imp.';
   End If;
 End;

 function isLabelGae Return Boolean Is
 Begin
    Return True;
 End;

 Function getLabelGae Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelGae() Then
     Return 'GAE';
   Else
     Return 'WP';
   End If;
 End;

 Function getLabelGaeEst Return VARCHAR2 Is
 Begin
   If cnrutil.isLabelGae() Then
     Return 'Gruppo di Azioni Elementari';
   Else
     Return 'Workpackage';
   End If;
 End;

End;


