--------------------------------------------------------
--  DDL for Package Body CNRUTIL
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRUTIL" Is

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
