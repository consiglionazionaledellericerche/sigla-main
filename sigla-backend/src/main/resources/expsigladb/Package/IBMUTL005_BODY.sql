--------------------------------------------------------
--  DDL for Package Body IBMUTL005
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "IBMUTL005" 
is
	procedure bblobCallback ( aTipo char
				, aBframeBlob in out BFRAME_BLOB%rowtype )
	is
		aBBT bframe_blob_tipo%rowtype;
		aSPos number;
		aCBString varchar2 ( 4000 );
	begin
		-- Carico il tipo di file
		select *
		  into aBBT
		  from bframe_blob_tipo
		 where cd_tipo = aBframeBlob.cd_tipo;
		-- Controllo il tipo di blob Ã¨ binario o caratteri
		if aBBT.fl_binario = 'Y'
		then
			-- per binario nessun callback
			return;
		end if;

		-- Se il callback Ã¨ vuoto ritorno
		if aTipo is null
		or aTipo not in ( 'U'
				, 'D' )
		then
			IBMERR001.RAISE_ERR_GENERICO ( 'Tipo di callback non supportato' );
		end if;

		 if aTipo = 'U'
		and aBBT.upd_callback is null
		then
			return;
		elsif aTipo = 'D' and aBBT.del_callback is null
		then
			return;
		else
			null;
		end if;

		-- Leggo il callback ed lo invoco se presente
		if aTipo = 'D'
		then
			aCBString := aBBT.del_callback;
		else
			aCBString := aBBT.upd_callback;
		end if;

		begin
			execute immediate '
	    declare
		 aCLOB clob:=:1;
	    begin
	     DBMS_LOB.open(aCLOB,DBMS_LOB.lob_readonly);
	     begin
		  ' || aCBString || '(:2,:3,:4,aCLOB);
	     exception when others then
	      if DBMS_LOB.ISOPEN(aCLOB)=1 then
		   DBMS_LOB.close(aCLOB);
		  end if;
		  raise;
		 end;
	     if DBMS_LOB.ISOPEN(aCLOB)=1 then
	      DBMS_LOB.close(aCLOB);
	     end if;
	    end;
	  '
			  USING IN
			    OUT aBframeBlob.cdata
			      , aBframeBlob.cd_tipo
			      , aBframeBlob.path
			      , aBframeBlob.filename;
			exception
				when others
				then
					IBMERR001.RAISE_ERR_GENERICO ( 'File non compatibile.' || SQLERRM(SQLCODE));
		end;

	end;

	procedure deleteCallback ( aBframeBlob in out BFRAME_BLOB%rowtype )
	is
	begin
		bblobCallback ( 'D'
			      , aBframeBlob );
	end;

	procedure updateCallback ( aBframeBlob in out BFRAME_BLOB%rowtype )
	is
	begin
		bblobCallback ( 'U'
			      , aBframeBlob );
	end;

	function openClob ( aTipo varchar2
			  , aPath varchar2
			  , aFileName varchar2 )
	  return clob
	is
		aClob clob;
	begin
		select cdata
		  into aClob
		  from bframe_blob
		 where cd_tipo = aTipo
		   and path = aPath
		   and filename = aFileName
		   for update nowait;
		DBMS_LOB.open ( aClob
			      , DBMS_LOB.lob_readonly );
		return aClob;
	end;

	function openClobForWrite ( aTipo varchar2
				  , aPath varchar2
				  , aFileName varchar2 )
	  return clob
	is
		aClob clob;
	begin
		select cdata
		  into aClob
		  from bframe_blob
		 where cd_tipo = aTipo
		   and path = aPath
		   and filename = aFileName
		   for update nowait;
		DBMS_LOB.open ( aClob
			      , DBMS_LOB.lob_readwrite );
		return aClob;
	end;

	function nextLine ( aClob clob
			  , aSPos IN OUT integer
			  , aString OUT varchar2 )
	  return integer
	is
		aEPos INTEGER;
		aAmount INTEGER;
		aNewLine varchar2 ( 1 );
	begin
		aNewLine := '
';
		aEPos := DBMS_LOB.instr ( aClob
					, aNewLine
					, aSPos
					, 1 ) - 1;
		if aEPos = - 1
		then
			return - 1;
		end if;

		aAmount := aEPos - aSPos + 1;
		DBMS_LOB.READ ( aClob
			      , aAmount
			      , aSPos
			      , aString );
		aSPos := aEPos + 2;
		return 1;
	end;

	procedure putLine ( aCLob IN OUT clob
			  , aString IN varchar2 )
	is
		aEPos INTEGER;
		aLocS varchar2 ( 32000 );
	begin
		aLocS := aString || '
';
		DBMS_LOB.WRITEAPPEND ( aCLob
				     , length ( aLocS )
				     , aLocS );
	end;

	procedure closeClob ( aClob IN OUT clob )
	is
	begin
		DBMS_LOB.close ( aClob );
	end;

	-- -------------------------------------------------------------------- --
	-- Procedura che imposta lo stato di una riga della tabella BFRAME_BLOB --
	-- -------------------------------------------------------------------- --
	procedure ShImpostaStato ( aTipo BFRAME_BLOB.CD_TIPO%type
				 , aPath BFRAME_BLOB.PATH%type
				 , aFilename BFRAME_BLOB.FILENAME%type
				 , aStato BFRAME_BLOB.STATO%type )
	as
	begin
		update BFRAME_BLOB
		   set stato = aStato
		 where cd_tipo = aTipo
		   and path = aPath
		   and filename = aFilename;
	end;

	-- ------------------------------------------------------------ --
	-- Procedura che inizializza una riga della tabella BFRAME_BLOB --
	-- ------------------------------------------------------------ --
	procedure ShIniCBlob ( aTipo BFRAME_BLOB.CD_TIPO%type
			     , aPath BFRAME_BLOB.PATH%type
			     , aFilename BFRAME_BLOB.FILENAME%type
			     , aTiVisibilita BFRAME_BLOB.TI_VISIBILITA%type
			     , aDsFile BFRAME_BLOB.DS_FILE%type
			     , aDsUtente BFRAME_BLOB.DS_UTENTE%type
			     , aUtente varchar2
			     , aCBlob in out clob )
	as
	begin
		-- Inizio Main
		begin
			-- inizio Insert
			insert into BFRAME_BLOB ( CD_TIPO
						, PATH
						, FILENAME
						, BDATA
						, CDATA
						, DACR
						, DUVA
						, UTUV
						, UTCR
						, PG_VER_REC
						, STATO
						, DS_FILE
						, DS_UTENTE
						, TI_VISIBILITA )
			values ( aTipo
			       , aPath
			       , aFilename
			       , empty_blob ( )
			       , empty_clob ( )
			       , sysdate
			       , sysdate
			       , aUtente
			       , aUtente
			       , 1
			       , null
			       , aDsFile
			       , aDsUtente
			       , aTiVisibilita );
			begin
				ShImpostaStato ( aTipo
					       , aPath
					       , aFilename
					       , gStatoIni );
				exception
					when others
					then
						ibmerr001.RAISE_ERR_GENERICO ( 'Stato non aggiornato' );
			end;

			exception
				when dup_val_on_index
				then
					ibmerr001.RAISE_ERR_GENERICO ( 'File giÃ  inserito' );
		end;

		-- Fine Insert
		begin
			aCBlob := openClobForWrite ( aTipo
						   , aPath
						   , aFileName );
			exception
				when others
				then
					ibmerr001.RAISE_ERR_GENERICO ( 'Impossibile aprire il Blob' );
		end;

	end;

	-- Fine Main
	-- ---------------------------------------------------------------------------- --
	-- Procedura che esegue append su un blob di una riga della tabella BFRAME_BLOB --
	-- ---------------------------------------------------------------------------- --
	procedure ShPutLine ( aTipo BFRAME_BLOB.CD_TIPO%type
			    , aPath BFRAME_BLOB.PATH%type
			    , aFilename BFRAME_BLOB.FILENAME%type
			    , aCLob IN OUT clob
			    , aString IN varchar2 )
	is
		aEPos INTEGER;
		aLocS varchar2 ( 32000 );
	begin
		aLocS := aString || '
';
		DBMS_LOB.WRITEAPPEND ( aCLob
				     , length ( aLocS )
				     , aLocS );
		exception
			when others
			then
				ShImpostaStato ( aTipo
					       , aPath
					       , aFilename
					       , gStatoErr );
				closeClob ( aClob );
				ibmerr001.RAISE_ERR_GENERICO ( 'Inserimetno Fallito' );
	end;

	-- ------------------------------------------------------------------------------------- --
	-- Procedura che chiude il blob di una riga della tabella BFRAME_BLOB e imposta lo stato --
	-- ------------------------------------------------------------------------------------- --
	procedure ShCloseClob ( aTipo BFRAME_BLOB.CD_TIPO%type
			      , aPath BFRAME_BLOB.PATH%type
			      , aFilename BFRAME_BLOB.FILENAME%type
			      , aClob IN OUT clob )
	is
	begin
		begin
			DBMS_LOB.close ( aClob );
			exception
				when others
				then
					ShImpostaStato ( aTipo
						       , aPath
						       , aFilename
						       , gStatoErr );
					ibmerr001.RAISE_ERR_GENERICO ( 'Chiusura Fallita o Blob giÃ  chiuso' );
		end;

		begin
			ShImpostaStato ( aTipo
				       , aPath
				       , aFilename
				       , gStatoEse );
			exception
				when others
				then
					ShImpostaStato ( aTipo
						       , aPath
						       , aFilename
						       , gStatoErr );
					ibmerr001.RAISE_ERR_GENERICO ( 'Stato non aggiornato' );
		end;

	end;

END;
