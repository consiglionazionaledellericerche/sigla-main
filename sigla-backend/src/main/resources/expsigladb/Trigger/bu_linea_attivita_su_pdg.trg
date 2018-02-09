CREATE OR REPLACE TRIGGER BU_LINEA_ATTIVITA_SU_PDG
  BEFORE Update Or Insert Of pg_progetto
  on LINEA_ATTIVITA
  for each row
begin
--
-- Trigger di abilitazione della modifica delle linea di attivit?
--
-- Date: 31/03/2009
-- Version: 1.1
--
-- Dependency: JAVA
--
-- History:
--
-- Date: 02/05/2005
-- Version: 1.0
-- Creazione
--
-- Date: 19/01/2007
-- Version: 1.1
-- Subordinato il controllo sul modulo di attivit? solo al caso in cui viene effettivamente modificato
--
-- Date: 31/03/2009
-- Version: 1.2
-- Aggiunta la verifica di esistenza GAE su variazioni a PDG e Stanziamenti Residui
--
-- Body:
--

    If updating Then
        Declare
            wRec1 NUMBER;
            wRec NUMBER;
			cdCdsFromCdr parametri_cds.cd_cds%type;
        Begin

			cdCdsFromCdr := CNRUTL001.getCdsFromCdr(:old.cd_centro_responsabilita);

-- vecchie tabelle pdg

            Select Count(1) into wRec1
            From   pdg_preventivo_etr_det a, parametri_cds b
            Where  a.cd_centro_responsabilita = :old.cd_centro_responsabilita
            And    a.cd_linea_attivita = :old.cd_linea_attivita
            And    b.esercizio = a.esercizio
            And    b.cd_cds = cdCdsFromCdr
            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';

            If wRec1=0 Then
				Select Count(1) into wRec1
	            From   pdg_preventivo_spe_det a, parametri_cds b
	            Where  a.cd_centro_responsabilita = :old.cd_centro_responsabilita
	            And    a.cd_linea_attivita = :old.cd_linea_attivita
	            And    b.esercizio = a.esercizio
	            And    b.cd_cds = cdCdsFromCdr
	            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';
			End If;

-- aggiunta il 15.11.2006 nuove tabelle pdg

            If wRec1=0 Then
	            Select Count(1) into wRec1
	            From   pdg_modulo_entrate_gest a, parametri_cds b
	            Where  a.cd_cdr_assegnatario = :old.cd_centro_responsabilita
	            And    a.cd_linea_attivita = :old.cd_linea_attivita
	            And    b.esercizio = a.esercizio
	            And    b.cd_cds = cdCdsFromCdr
	            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';
			End If;

            If wRec1=0 Then
	            Select Count(1) into wRec1
	            From   pdg_modulo_spese_gest a, parametri_cds b
	            Where  a.cd_cdr_assegnatario = :old.cd_centro_responsabilita
	            And    a.cd_linea_attivita = :old.cd_linea_attivita
	            And    b.esercizio = a.esercizio
	            And    b.cd_cds = cdCdsFromCdr
	            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';
			End If;

            If wRec1=0 Then
	            Select Count(1) into wRec1
	            From   pdg_variazione_riga_gest a, parametri_cds b
	            Where  a.cd_cdr_assegnatario = :old.cd_centro_responsabilita
	            And    a.cd_linea_attivita = :old.cd_linea_attivita
	            And    b.esercizio = a.esercizio
	            And    b.cd_cds = cdCdsFromCdr
	            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';
			End If;

            If wRec1=0 Then
	            Select Count(1) into wRec1
	            From   var_stanz_res_riga a, parametri_cds b
	            Where  a.cd_cdr = :old.cd_centro_responsabilita
	            And    a.cd_linea_attivita = :old.cd_linea_attivita
	            And    b.esercizio = a.esercizio
	            And    b.cd_cds = cdCdsFromCdr
	            And    b.FL_COMMESSA_OBBLIGATORIA = 'Y';
			End If;

            If wRec1>0 Then
                If ((:old.pg_progetto Is Null And :New.pg_progetto Is Not Null) Or
                    (:old.pg_progetto Is Not Null And :New.pg_progetto Is Null) Or
                    :old.pg_progetto!=:New.pg_progetto) Then

                        select Count(1) into wRec
                        from   utente
                        where  cd_utente = :New.utuv
                          and cd_gestore = 'CNRTUTTO';

                        If wRec=0 Then
                            IBMERR001.RAISE_ERR_GENERICO('Il '||cnrutil.getLabelGae()||' '||:old.cd_linea_attivita||' risulta utilizzato sul piano di gestione. Impossibile la modifica.');
                        End If;

                End If;
            End If;
        End;
    End If;

    If inserting Or
       (:Old.pg_progetto Is Null And :New.pg_progetto Is Not Null) Or
       (:Old.pg_progetto Is Not Null And :New.pg_progetto Is Not Null And :New.pg_progetto != :Old.pg_progetto) Then
       declare
           wRec NUMBER;
       Begin
           select Max(esercizio)
           into   wRec
           from   progetto
           where  esercizio  <= :New.ESERCIZIO_FINE And
                  pg_progetto = :New.pg_progetto    And
                  fl_utilizzabile = 'N';

           If wRec>0 Then
               IBMERR001.RAISE_ERR_GENERICO('Il modulo di attivit? impostato (con ID '||:New.pg_progetto||') non ? utilizzabile per l''esercizio '||wRec||'.');
           End If;
       End;
     End If;
End;
/


