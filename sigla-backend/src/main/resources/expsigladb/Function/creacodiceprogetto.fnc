CREATE OR REPLACE FUNCTION creaCodiceProgetto
(p_codice VARCHAR2, p_tipo_progetto VARCHAR2, p_lpad NUMBER) Return VARCHAR2 Is
    nProgressivo NUMBER := 1;
    sCodiceCompleto VARCHAR2(100);
    riga NUMBER;
    nLpad NUMBER := 0;
Begin
    If p_codice Is Null Then
        raise_application_error(-20030,'Il codice base del mumeratore deve essere valorizzato',TRUE);
    End If;

    Loop
        Begin
            Select progressivo Into nProgressivo
            From progetto_numeratore
            Where codice = p_codice
            For Update Of progressivo;

            nProgressivo := Nvl(nProgressivo,0)+1;

            Update progetto_numeratore Set progressivo = nProgressivo
            Where codice = p_codice;

            If (Length(To_Char(nProgressivo))>p_lpad) Then
                nLpad:=Length(To_Char(nProgressivo));
            Else
                nLpad:=p_lpad;
            End If;
            sCodiceCompleto := p_codice||'-'||p_tipo_progetto||To_Char(Lpad(nProgressivo,nLpad,'0'));

        Exception
            When No_Data_Found Then
                Insert Into progetto_numeratore (codice, progressivo)
                Values (p_codice, nProgressivo);

                If (Length(To_Char(nProgressivo))>p_lpad) Then
                    nLpad:=Length(To_Char(nProgressivo));
                Else
                    nLpad:=p_lpad;
                End If;
                sCodiceCompleto := p_codice||'-'||p_tipo_progetto||To_Char(Lpad(nProgressivo,nLpad,'0'));
        End;


        Begin
            Select 1 Into riga
            From progetto
            Where cd_progetto = sCodiceCompleto;
        Exception
            When No_Data_Found Then
                Exit;
        End;
    End Loop;

    Return (sCodiceCompleto);
End;
/


