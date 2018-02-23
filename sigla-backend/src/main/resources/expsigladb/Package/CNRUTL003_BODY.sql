--------------------------------------------------------
--  DDL for Package Body CNRUTL003
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRUTL003" AS

 FUNCTION cognome(v_str varchar2) RETURN varchar2
  IS
    i NUMBER;
    y NUMBER;
    z NUMBER;
    n_ascii NUMBER;
    v_cara VARCHAR2(1 char);
    v_vocali VARCHAR2(3000);
    v_consonanti VARCHAR2(3 char);
    v_cod_fis VARCHAR2(3000);
  BEGIN
      i := 0;
      y := 0;
      z := 0;

      while i < length(v_str) and y <> 3 loop
        v_cara  := substr(upper(v_str), i+1, 1); -- estraggo il carattere
        n_ascii := ascii(v_cara); -- estraggo il codice ascii
        -- controllo se il codice ascii appartiene al range di valori accettati compreso fra 65 e 90
        if(n_ascii > 64 and n_ascii < 91) then
        -- controllo se il codice ascii è una vocale
          if(n_ascii = 65 or n_ascii = 69 or n_ascii = 73 or n_ascii = 79 or n_ascii = 85 ) then
                v_vocali:=v_vocali||v_cara;
          else
              v_consonanti:=v_consonanti||v_cara;
              y := y + 1;
          end if;
        end if;
        i := i + 1;
       end loop;
        --assegno il valore del codice fiscale
        v_cod_fis := substr(v_consonanti|| v_vocali, 1, 3);
        while length(v_cod_fis) < 3  loop
          v_cod_fis := v_cod_fis || 'X';
        end loop;
        --dbms_output.put_line('Codice Fiscale:' || v_cod_fis);
        return(v_cod_fis);
    EXCEPTION
      WHEN OTHERS THEN
        RETURN('CF-COGNOME');
   END cognome;

  FUNCTION nome(v_str varchar2) RETURN varchar2
  IS
    i NUMBER;
    y NUMBER;
    n_ascii NUMBER;
    v_cara VARCHAR2(1 char);
    v_vocali VARCHAR2(3000);
    v_consonanti VARCHAR2(4 char);
    v_cod_fis VARCHAR2(3000);
  BEGIN
      i := 0;
      y := 0;

      while i < length(v_str) and y <> 4 loop
        v_cara  := substr(upper(v_str), i+1, 1); -- estraggo il carattere
        n_ascii := ascii(v_cara); -- estraggo il codice ascii
        -- controllo se il codice ascii appartiene al range di valori accettati compreso fra 65 e 90
        if(n_ascii > 64 and n_ascii < 91) then
        -- controllo se il codice ascii è una vocale
          if(n_ascii = 65 or n_ascii = 69 or n_ascii = 73 or n_ascii = 79 or n_ascii = 85 ) then
              v_vocali:=v_vocali||v_cara;
          else
              v_consonanti:=v_consonanti||v_cara;
              y := y + 1;
          end if;
        end if;
        i := i + 1;
       end loop;
        --assegno il valore del codice fiscale
       -- dbms_output.put_line('Codice Fiscale:' || length(v_consonanti));
        IF( length(v_consonanti)>3 ) THEN
          v_cod_fis  := substr(upper(v_consonanti), 1, 1);
          v_cod_fis  := v_cod_fis || substr(upper(v_consonanti), 3, 2);
        ELSE
            v_cod_fis := substr(v_consonanti|| v_vocali, 1, 3);
              while length(v_cod_fis) < 3  loop
                v_cod_fis := v_cod_fis || 'X';
              end loop;
         END IF;
        --dbms_output.put_line('Codice Fiscale:' || v_cod_fis);
        return(v_cod_fis);
      EXCEPTION
      WHEN OTHERS THEN
        RETURN('CF-NOME');
     END nome;

  --funzione che accetta la data in formato testo gg/mm/aaaa sesso stringa 'F' o 'M'
  FUNCTION data_nascita(v_data varchar2, v_sesso char) RETURN varchar2
  is

    TYPE mesi_type IS TABLE OF char INDEX BY BINARY_INTEGER;
    v_mesi mesi_type;
    d_data date;
    c_str varchar2(5 char);

  BEGIN
    -- converto la stringa in ingresso in data
    d_data := to_date(v_data, 'DD/MM/YYYY');

    -- Codifica mesi dell'anno
      v_mesi(1) := 'A';
      v_mesi(2) := 'B';
      v_mesi(3) := 'C';
      v_mesi(4) := 'D';
      v_mesi(5) := 'E';
      v_mesi(6) := 'H';
      v_mesi(7) := 'L';
      v_mesi(8) := 'M';
      v_mesi(9) := 'P';
      v_mesi(10) := 'R';
      v_mesi(11) := 'S';
      v_mesi(12) := 'T';

    -- converto l'anno
    c_str := to_char(d_data, 'YY');
    -- converto il mese
    c_str := c_str || v_mesi(to_number(to_char(d_data, 'MM')));
    --converto il giorno
    if(upper(v_sesso)= 'F') then
        c_str := c_str || to_char(to_number(to_char(d_data, 'DD')) + 40);
      else
        c_str := c_str || to_char(d_data, 'DD');
    end if;
    return(c_str);
  EXCEPTION
      WHEN OTHERS THEN
        RETURN('CF-DATA');
end data_nascita;

  FUNCTION comune_di_nascita(pg number) RETURN varchar2
  IS
     v_codice_catastale varchar2(4 char);
     appo_comune comune%rowtype;
  BEGIN

      select * into appo_comune from comune where pg_comune=pg;
      if(appo_comune.cd_catastale is null or appo_comune.cd_catastale='*') then
           select cd_catastale into v_codice_catastale  from nazione where pg_nazione=appo_comune.pg_nazione;
           return v_codice_catastale;
      end if;
      RETURN(appo_comune.cd_catastale);
    EXCEPTION
      WHEN OTHERS THEN
        RETURN('CF-CATASTALE');
  END comune_di_nascita;

  FUNCTION codice_controllo(v_str varchar2) RETURN varchar2
  IS

    TYPE caratteri_type IS TABLE OF number INDEX BY varchar2(1 char);
    TYPE controllo_type IS TABLE OF char INDEX BY BINARY_INTEGER;

    v_pari caratteri_type;
    v_dispari caratteri_type;
    v_controllo controllo_type;

    i number;
    n_pari number;
    n_dispari number;

  BEGIN

        v_pari('A') := 0 ;
        v_pari('B') := 1 ;
        v_pari('C') := 2 ;
        v_pari('D') := 3 ;
        v_pari('E') := 4 ;
        v_pari('F') := 5 ;
        v_pari('G') := 6 ;
        v_pari('H') := 7 ;
        v_pari('I') := 8 ;
        v_pari('J') := 9 ;
        v_pari('K') := 10 ;
        v_pari('L') := 11 ;
        v_pari('M') := 12 ;
        v_pari('N') := 13 ;
        v_pari('O') := 14 ;
        v_pari('P') := 15 ;
        v_pari('Q') := 16 ;
        v_pari('R') := 17 ;
        v_pari('S') := 18 ;
        v_pari('T') := 19 ;
        v_pari('U') := 20 ;
        v_pari('V') := 21 ;
        v_pari('W') := 22 ;
        v_pari('X') := 23 ;
        v_pari('Y') := 24 ;
        v_pari('Z') := 25 ;
        v_pari('1') := 1 ;
        v_pari('2') := 2 ;
        v_pari('3') := 3 ;
        v_pari('4') := 4 ;
        v_pari('5') := 5 ;
        v_pari('6') := 6 ;
        v_pari('7') := 7 ;
        v_pari('8') := 8 ;
        v_pari('9') := 9 ;
        v_pari('0') := 0 ;

      --ARRAY DISPARY
        v_dispari('A') := 1 ;
        v_dispari('B') := 0;
        v_dispari('C') := 5;
        v_dispari('D') := 7;
        v_dispari('E') := 9;
        v_dispari('F') := 13;
        v_dispari('G') := 15;
        v_dispari('H') := 17 ;
        v_dispari('I') := 19 ;
        v_dispari('J') := 21;
        v_dispari('K') := 2;
        v_dispari('L') := 4;
        v_dispari('M') := 18;
        v_dispari('N') := 20;
        v_dispari('O') := 11;
        v_dispari('P') := 3;
        v_dispari('Q') := 6;
        v_dispari('R') := 8;
        v_dispari('S') := 12;
        v_dispari('T') := 14;
        v_dispari('U') := 16;
        v_dispari('V') := 10;
        v_dispari('W') := 22;
        v_dispari('X') := 25;
        v_dispari('Y') := 24;
        v_dispari('Z') := 23;
        v_dispari('1') := 0;
        v_dispari('2') := 5;
        v_dispari('3') := 7;
        v_dispari('4') := 9;
        v_dispari('5') := 13;
        v_dispari('6') := 15;
        v_dispari('7') := 17;
        v_dispari('8') := 19;
        v_dispari('9') := 21;
        v_dispari('0') :=  1;

      --VARIABILI DI CONTROLLO
        v_controllo(0) := 'A';
        v_controllo(1) := 'B';
        v_controllo(2) := 'C';
        v_controllo(3) := 'D';
        v_controllo(4) := 'E';
        v_controllo(5) := 'F';
        v_controllo(6) := 'G';
        v_controllo(7) := 'H';
        v_controllo(8) := 'I';
        v_controllo(9) := 'J';
        v_controllo(10) := 'K';
        v_controllo(11) := 'L';
        v_controllo(12) := 'M';
        v_controllo(13) := 'N';
        v_controllo(14) := 'O';
        v_controllo(15) := 'P';
        v_controllo(16) := 'Q';
        v_controllo(17) := 'R';
        v_controllo(18) := 'S';
        v_controllo(19) := 'T';
        v_controllo(20) := 'U';
        v_controllo(21) := 'V';
        v_controllo(22) := 'W';
        v_controllo(23) := 'X';
        v_controllo(24) := 'Y';
        v_controllo(25) := 'Z';

      i := 1;
      n_dispari := 0;
      while i < length(v_str) +1 loop
          n_dispari := n_dispari + v_dispari(substr(v_str,i,1));
          i := i +2;
      end loop;

      i := 2;
      n_pari := 0;
      while i < length(v_str) +1 loop
          n_pari := n_pari + v_pari(substr(v_str,i,1));
        i := i +2;
      end loop;
       RETURN(v_controllo(mod(n_dispari + n_pari, 26)));
      EXCEPTION
      WHEN OTHERS THEN
        RETURN('CF-CHECK');
  END codice_controllo;

  FUNCTION calcola( v_cognome varchar2,
  									v_nome varchar2,
                    v_data_di_nascita varchar2,
                    v_sesso char,
                    pg_comune_di_nascita number) RETURN varchar2
  IS
       v_codice_fiscale varchar2(16 char);

  BEGIN

 -- COGNOME
  if('CF-COGNOME' = cognome(v_cognome) or cognome(v_cognome) is null) then
    RETURN('ECF-COGNOME');
  else
    v_codice_fiscale := v_codice_fiscale || cognome(v_cognome);
  end if;

 -- NOME
   if('CF-NOME' = nome(v_nome) or nome(v_nome) is null) then
    RETURN('ECF-NOME');
  else
     v_codice_fiscale := v_codice_fiscale || nome(v_nome);
  end if;

 -- DATA DI NASCITA E SESSO
   if('CF-DATA' = data_nascita(v_data_di_nascita, v_sesso) or v_sesso is null) then
    RETURN('ECF-DATA');
  else
     v_codice_fiscale := v_codice_fiscale || data_nascita(v_data_di_nascita, v_sesso);
  end if;

  -- PAESE DI NASCITA
   if('CF-CATASTALE' = comune_di_nascita(pg_comune_di_nascita) or pg_comune_di_nascita is null) then
    RETURN('ECF-CATASTALE');
  else
     v_codice_fiscale := v_codice_fiscale || comune_di_nascita(pg_comune_di_nascita);
  end if;

   if('CF-CHECK' = codice_controllo(v_codice_fiscale) or v_codice_fiscale is null) then
    RETURN('ECF-CHECK');
  else
     v_codice_fiscale := v_codice_fiscale || codice_controllo(v_codice_fiscale);
  end if;

     RETURN(v_codice_fiscale);
  EXCEPTION
     WHEN OTHERS THEN
      RETURN('ECF-ERRORE '||sqlerrm);
  END calcola;

END CNRUTL003;
