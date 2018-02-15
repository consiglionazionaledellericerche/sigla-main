--------------------------------------------------------
--  DDL for Package Body IBMUTL050
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL050" is

    function getUtente return varchar2 is
     aUtente varchar2(50);
	begin
	 select username into aUtente from user_users;
	 if UPPER(aUtente) in ('SYS', 'SYSTEM') then
   	  ibmerr001.RAISE_ERR_GENERICO('Utente non supportato');
	 end if;
	 return aUtente;
	end;

	PROCEDURE schedula_compilazione_pkg (utente varchar2) IS
	num_max_iterazioni number;
	num_package_invalid number;
	i number;
	fine_testate boolean;
	fine_body boolean;

	BEGIN
		-- IDENTIFICHIAMO TUTTI I PACKAGE

		SELECT count(*)
		into num_max_iterazioni
		FROM all_OBJects OB
		WHERE UPPER(OB.OWNER) = UPPER(utente)
		AND   OB.object_TYPE='PACKAGE';

		i :=1;
		loop
			fine_testate := true;
			fine_body := true;

			for oggetto in (SELECT *
						FROM all_OBJects OB
						WHERE UPPER(OB.OWNER) = UPPER(utente)
						AND   OB.object_TYPE='PACKAGE'	and   OB.status='INVALID') loop
		      begin
			   execute immediate 'alter package '|| oggetto.object_name || ' compile package ';
              exception when others then
   			   fine_testate := false;
			  end;
			end loop;


			for oggetto1 in (SELECT *
					 	FROM all_OBJects OB
						WHERE UPPER(OB.OWNER) = UPPER(utente)
						AND   OB.object_TYPE='PACKAGE BODY'	and   OB.status='INVALID') loop
		   	   begin
			    execute immediate 'alter package '|| oggetto1.object_name || ' compile body ';
               exception when others then
			    fine_body := false;
			   end;
			end loop;

			if (i = num_max_iterazioni) then
			 ibmerr001.RAISE_ERR_GENERICO('Numero massimo di iterazioni superato per la compilazione dei packages');
			else
			   i := i+1;
			end if;
			if fine_testate and fine_body then
			 return;
            end if;
		end loop;

		-- CONTROLLARE SE TUTTI I PACKAGE SONO STATI COMPILATI
		SELECT count(*)
		into num_package_invalid
		FROM all_OBJects OB
		WHERE UPPER(OB.OWNER) = UPPER(utente)
		AND   ((OB.object_TYPE='PACKAGE' AND   OB.STATUS = 'INVALID')
			  OR   (OB.object_TYPE='PACKAGE BODY' AND   OB.STATUS = 'INVALID'));
		if 	num_package_invalid >0 then
			ibmerr001.RAISE_ERR_GENERICO('Alcuni package non risultano compilati correttamente');
		end if;

	END;

	PROCEDURE schedula_compilazione_view (utente varchar2) IS
	num_package_invalid number;
	BEGIN
		for vie in (	SELECT *
					 	FROM all_OBJects OB
						WHERE UPPER(OB.OWNER) = UPPER(utente)
						AND   OB.object_TYPE='VIEW'	and   OB.status='INVALID') loop
			begin
				 execute immediate 'alter view '|| vie.object_name || ' compile ';
			exception
			when others then
				 ibmerr001.RAISE_ERR_GENERICO('Alcune viste non risultano compilate correttamente');
			end;
		end loop;

		-- CONTROLLARE CHE TUTTE LE VISTE SONO STATE COMPILATE
		SELECT count(*)
		into num_package_invalid
		FROM all_OBJects OB
		WHERE UPPER(OB.OWNER) = UPPER(utente)
		AND   OB.object_TYPE='VIEW'
		AND   OB.STATUS = 'INVALID';
		if 	num_package_invalid >0 then
			ibmerr001.RAISE_ERR_GENERICO('Alcune viste non risultano compilate correttamente');
		end if;

	END;

	PROCEDURE schedula_compilazione_triggers (utente varchar2) IS
	num_trig_invalid number;
	BEGIN
		for tie in (	SELECT *
					 	FROM all_OBJects OB
						WHERE UPPER(OB.OWNER) = UPPER(utente)
						AND   OB.object_TYPE='TRIGGER'	and   OB.status='INVALID') loop
			execute immediate 'alter trigger '|| tie.object_name || ' compile ';
		end loop;

		-- CONTROLLARE CHE TUTTI I TRIGGERS SONO STATI COMPILATI
		SELECT count(*)
		into num_trig_invalid
		FROM all_OBJects OB
		WHERE UPPER(OB.OWNER) = UPPER(utente)
		AND   OB.object_TYPE='TRIGGER'
		AND   OB.STATUS = 'INVALID';
		if 	num_trig_invalid >0 then
			ibmerr001.RAISE_ERR_GENERICO('Alcuni triggers non risultano compilate correttamente');
		end if;

	END;

	PROCEDURE compila_tutto IS
	begin
     schedula_compilazione_view;
     schedula_compilazione_pkg;
 	 schedula_compilazione_triggers;
	end;

	PROCEDURE compila_tutto (utente varchar2) IS
	begin
     schedula_compilazione_view (utente);
     schedula_compilazione_pkg (utente);
 	 schedula_compilazione_triggers (utente);
	end;

	PROCEDURE schedula_compilazione_view IS
	begin
  	 schedula_compilazione_view  (getUtente);
	end;

	PROCEDURE schedula_compilazione_pkg IS
	begin
  	 schedula_compilazione_pkg (getUtente);
	end;

	PROCEDURE schedula_compilazione_triggers IS
	begin
  	 schedula_compilazione_triggers  (getUtente);
	end;

end;
