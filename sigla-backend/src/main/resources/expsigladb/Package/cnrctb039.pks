CREATE OR REPLACE package CNRCTB039 as
--
-- CNRCTB039 - Metodi di controllo associazione tra doc autorizzatori
-- Date: 19/07/2006
-- Version: 1.5
--
-- Dependency: IBMERR 001
--
-- History:
--
-- Date: 15/05/2003
-- Version: 1.0
-- Creazione
--
-- Date: 19/05/2003
-- Version: 1.1
-- Documentazione + gestione mandato stipendi
--
-- Date: 20/05/2003
-- Version: 1.2
-- Fix errore su annullamento del mandato di liquidazione CORI
--
-- Date: 26/05/2003
-- Version: 1.3
-- Esclusi dal controllo i mandati di accreditamento e regolarizzazione
--
-- Date: 19/06/2003
-- Version: 1.4
-- Errore in controllo reversali associate a liquidazioni gruppi CORI
--
-- Date: 19/07/2006
-- Version: 1.5
-- Gestione Impegni/Accertamenti Residui:
-- gestito il nuovo campo ESERCIZIO_ORIGINALE
--
-- Date: 08/04/2011
-- Version: 1.6
-- Gestione blocco annullamento mandato quando la liquidazione cori ? stata effettuata dalla 999
--
-- Constants:

-- Functions e Procedures:

-- Tale metodo deve essere chiamato all'interno di un metodo che aggiorni utuv,duva e pg_ver rec
--
-- Pre-post name: Controllo di annullabilit? del documento autorizzatorio specificato
-- Pre condizione: Il documento passato ? un mandato o una reversale non dipendente da altro mandato tramite
--                 associazioni ASS_MANDATO_REVERSALE o ASS_MANDATO_MANDATO
-- Post condizione:
--  Se si tratta di mandato:
--   Se il mandato ? quello degli stipendi solleva eccezzione di non annullabilit?
--   Se il mandato ? di liquidazione CORI
--    Carica le reversali associate
--     Se num. revers. coll. = 0 => ritorna senza sollevare eccezioni
--     Se num. revers. coll. >= 1 =>
--      Cicla sulle reversali associate
--       Se una di queste non ? collegata a GENERICO DI VERSAMENTO DI ENTRATA solleva eccezione
--   Se il mandato ha pi? di una riga non ? legato a compensi:
--    verifica l'esistenza di associazioni con reversali effettuate necessariamente a mano:
--    nel caso selleva eccezione
--   Se il mandato non ? principale di compenso
--    verifica l'esistenza di associazioni con reversali effettuate necessariamente a mano:
--    nel caso selleva eccezione
--   Se il mandato ? principale di compenso
--    Cicla su tutte le reversali figlie di questo mandato
--     Per ogni figlio devo verificare se appartenenva o meno all pratica principale
--     del compenso del mandato principale a cui tale reversale ? collegata
--     nel caso la reversale non appartenga allo stesso compenso del mandato principale -> l'associazione
--     ? stata fatta a mano e quindi viene sollevata eccezione
--    Cicla su tutti i mandati figli e verifico se hanno relazioni in ASS_MANDATO_REVERSALE:
--     nel caso sono state effettuate manualmente e quindi sollevo eccezione
--  Se si tratta di reversale, questa ? necessariamente reversale paritetica di compenso senza mandato principale
--   Cicla su tutte le reversali/mandati figli dello stesso compenso (compresa quella in processo)
--   Per ogni figlio devo verificare che non sia presente in ASS_MANDATO_REVERSALE:
--   nel caso solleva eccezione
--
-- Parametri:
--  aTipoDoc -> M per mandato R per reveresale
--  aEs -> esercizio
--  aCdCds -> codice cds
--  aPgDoc -> progressivo documento autorizzatorio
--


 procedure checkAnnullabilita(
  aTipoDoc varchar2,
  aEs number,
  aCdCds varchar2,
  aPgDoc number
 );

end;
/


CREATE OR REPLACE package body CNRCTB039 is

 function getArticolo(aCdTipo char) return varchar2 is
 begin
  if aCdTipo = 'M' then
   return 'Il ';
  else
   return 'La ';
  end if;
 end;
 function getDesc(aEs number,aCdCds varchar2,aPgDoc number,aCdTipo char) return varchar2 is
 begin
  if aCdTipo = 'M' then
   return ' mandato n.'||aPgDoc||' es.:'||aEs||' cds:'||aCdCds;
  else
   return ' reversale n.'||aPgDoc||' es.:'||aEs||' cds:'||aCdCds;
  end if;
 end;

 procedure checkAssManRev(aEsR number,aCdCdsR varchar2,aPgDocR number, aCdTipoR char,
                          aEsS number,aCdCdsS varchar2,aPgDocS number, aCdTipoS char) is
  aASSMR ass_mandato_reversale%rowtype;
 begin
  if aCdTipoS = 'M' then
   begin
    select * into aASSMR from ass_mandato_reversale where
         esercizio_mandato = aEsS
     and cd_cds_mandato = aCdCdsS
     and pg_mandato = aPgDocS
     and ti_origine = 'S'
	for update nowait;
	if
	   aEsR=aEsS
	   and aCdCdsR=aCdCdsS
	   and aPgDocR=aPgDocS
	   and aCdTipoR=aCdTipoS
	then
     IBMERR001.RAISE_ERR_GENERICO(
	  getArticolo(aCdTipoR)||getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' risulta associato manualmente con la '||
	  getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'R')
	 );
    else
     IBMERR001.RAISE_ERR_GENERICO(
	  getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta connesso a '||
	  getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' che a sua volta risulta associato manualmente con la '||
	  getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'R')
	 );
	end if;
   exception
    when NO_DATA_FOUND then
     null;
    when TOO_MANY_ROWS then
	 if
	    aEsR=aEsS
	    and aCdCdsR=aCdCdsS
	    and aPgDocR=aPgDocS
	    and aCdTipoR=aCdTipoS
	 then
      IBMERR001.RAISE_ERR_GENERICO(
	   getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta associato a pi? di una reversale '||
	   getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'R')
	  );
     else
      IBMERR001.RAISE_ERR_GENERICO(
	   getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta paritetico di '||
	   getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' che a sua volta risulta associato a pi? di una reversale '||
	   getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'R')
	  );
	 end if;
   end;
  else
   begin
    select * into aASSMR from ass_mandato_reversale where
         esercizio_reversale = aEsS
     and cd_cds_reversale = aCdCdsS
     and pg_reversale = aPgDocS
     and ti_origine = 'S'
	for update nowait;
	if
	    aEsR=aEsS
	    and aCdCdsR=aCdCdsS
	    and aPgDocR=aPgDocS
	    and aCdTipoR=aCdTipoS
	then
     IBMERR001.RAISE_ERR_GENERICO(
	  getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta paritetica di '||
	  getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' che a sua volta risulta associato manualmente con il mandato '||
	  getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'M')
	 );
	else
     IBMERR001.RAISE_ERR_GENERICO(
	  getArticolo(aCdTipoR)||getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' risulta associato manualmente con il mandato '||
	  getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'M')
	 );
	end if;
   exception
    when NO_DATA_FOUND then
     null;
    when TOO_MANY_ROWS then
	 if
	    aEsR=aEsS
	    and aCdCdsR=aCdCdsS
	    and aPgDocR=aPgDocS
	    and aCdTipoR=aCdTipoS
	 then
      IBMERR001.RAISE_ERR_GENERICO(
	   getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta paritetica di '||
	   getDesc(aEsS,aCdCdsS,aPgDocS,aCdTipoS)||' che a sua volta risulta associato a pi? di un mandato '||
	   getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'M')
	  );
	 else
      IBMERR001.RAISE_ERR_GENERICO(
	   getArticolo(aCdTipoR)||getDesc(aEsR,aCdCdsR,aPgDocR,aCdTipoR)||' risulta associato a pi? di un mandato '||
	   getDesc(aASSMR.esercizio_reversale,aASSMR.cd_cds_reversale,aASSMR.pg_reversale,'M')
	  );
	 end if;
   end;
  end if;
 end;

 procedure checkAnnullabilita(
  aTipoDoc varchar2,
  aEs number,
  aCdCds varchar2,
  aPgDoc number
 ) is
  aASSDCNMP ass_comp_doc_cont_nmp%rowtype;
  aMR mandato_riga%rowtype;
  aRR reversale_riga%rowtype;
  aCori contributo_ritenuta%rowtype;
  aNum number;
  aManNAR v_mandato_non_associabile_rev%rowtype;
 begin
  -- Start controlli se il documento ? una reversale
  -- Si assume che se si tratta di reversale di compenso, non pu? che essere
  -- di compenso SENZA mandato principale (precondizione)
  if aTipoDoc='R' then
   -- Devo ciclare su tutte le reversali paritetiche e verificare che non siano
   -- Presenti in ASS_MANDATO_REVERSALE
   -- Ciclo su tutte le reversali/mandati figli dello stesso compenso (compresa quella in processo)
   -- Per ogni figlio devo verificare che non sia presente in ASS_MANDATO_REVERSALE
   begin
    select * into aASSDCNMP from ass_comp_doc_cont_nmp where
	      esercizio_doc = aEs
	  and cd_cds_doc = aCdCds
	  and pg_doc = aPgDoc
	  and cd_tipo_doc = aTipoDoc
	  for update nowait;

    for aASS in (select * from ass_comp_doc_cont_nmp where
	      esercizio_compenso = aASSDCNMP.esercizio_compenso
	   and cd_cds_compenso = aASSDCNMP.cd_cds_compenso
	   and cd_uo_compenso = aASSDCNMP.cd_uo_compenso
	   and pg_compenso = aASSDCNMP.pg_compenso
	   for update nowait
	) loop
     -- NON DEVE ESSERE PRESENTE IN ASS_MANDATO_REVERSALE
     checkAssManRev(aEs,aCdCds,aPgDoc,aTipoDoc,aASS.esercizio_doc,aASS.cd_cds_doc,aASS.pg_doc,aASS.cd_tipo_doc);
    end loop;
   exception
    when NO_DATA_FOUND then
     null;
    when TOO_MANY_ROWS then
     IBMERR001.RAISE_ERR_GENERICO('La '||getDesc(aEs,aCdCds,aPgDoc,aTipoDoc)||' risulta collegata a pi? di un compenso');
   end;


   -- rospuc 24/06/2009
  -- Se si cerca di annullare un mandato che ? stato gi? inserito in distinta ed inviato, con fatture gi? protocollate(Solo differite??)
  -- bisogna blocclare l'annullamento
    Begin
           for differite in (select 1 from reversale_riga m_riga,fattura_passiva a,reversale,fattura_passiva_riga b
                                                 Where
                                                        reversale.esercizio = aEs         And
                                                        reversale.cd_cds    = aCdCds      And
                                                        reversale.pg_reversale= aPgDoc      And
                                                        reversale.esercizio = m_riga.esercizio   And
                                                        reversale.cd_cds    = m_riga.cd_cds      And
                                                        reversale.pg_reversale= m_riga.pg_reversale  And
        	                                        reversale.stato_trasmissione ='T'           And
        	                                        a.fl_liquidazione_differita ='Y' And
        	                                        a.protocollo_iva Is Not Null And
        	                                        B.cd_cds = A.cd_cds AND
                                                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                                        B.esercizio = A.esercizio AND
                                                        B.pg_fattura_passiva = A.pg_fattura_passiva AND
                                                        B.dt_cancellazione IS Null   And
                                                        B.cd_cds_accertamento   = m_riga.cd_cds And
                                                        B.esercizio_accertamento =m_riga.ESERCIZIO_ACCERTAMENTO And
                                                        B.esercizio_ori_accertamento =m_riga.ESERCIZIO_ORI_ACCERTAMENTO And
                                                        B.pg_accertamento=m_riga.PG_ACCERTAMENTO  And
                                                        B.pg_accertamento_scadenzario=m_riga.PG_ACCERTAMENTO_SCADENZARIO
           ) loop
              IBMERR001.RAISE_ERR_GENERICO('La reversale non ? annullabile! Include delle fatture passive differite per le quali ? stato gi? stampato il registro iva definitivo');
           end loop;
    End;
  end if;
  -- Start controlli se il documento ? un mandato
  if aTipoDoc='M' Then

  -- rospuc 24/06/2009
  -- Se si cerca di annullare un mandato che ? stato gi? inserito in distinta ed inviato, con fatture gi? protocollate(Solo differite??)
  -- bisogna blocclare l'annullamento
    Begin
           for differite in (select 1 from mandato_riga m_riga,fattura_passiva a,mandato,fattura_passiva_riga b
                                                 Where
                                                        mandato.esercizio = aEs         And
                                                        mandato.cd_cds    = aCdCds      And
                                                        mandato.pg_mandato= aPgDoc      And
                                                        mandato.esercizio = m_riga.esercizio   And
                                                        mandato.cd_cds    = m_riga.cd_cds      And
                                                        mandato.pg_mandato= m_riga.pg_mandato  And
        	                                        mandato.stato_trasmissione ='T'           And
        	                                        a.fl_liquidazione_differita ='Y' And
        	                                        a.protocollo_iva Is Not Null And
        	                                        B.cd_cds = A.cd_cds AND
                                                        B.cd_unita_organizzativa = A.cd_unita_organizzativa AND
                                                        B.esercizio = A.esercizio AND
                                                        B.pg_fattura_passiva = A.pg_fattura_passiva AND
                                                        B.dt_cancellazione IS Null   And
                                                        B.cd_cds_obbligazione   = m_riga.cd_cds And
                                                        B.esercizio_obbligazione =m_riga.ESERCIZIO_OBBLIGAZIONE And
                                                        B.esercizio_ori_obbligazione =m_riga.ESERCIZIO_ORI_OBBLIGAZIONE And
                                                        B.pg_obbligazione=m_riga.PG_OBBLIGAZIONE  And
                                                        B.pg_obbligazione_scadenzario=m_riga.PG_OBBLIGAZIONE_SCADENZARIO
           ) loop
              IBMERR001.RAISE_ERR_GENERICO('Il mandato non ? annullabile! Include delle fatture passive differite per le quali ? stato gi? stampato il registro iva definitivo');
           end loop;
           for differite in (select 1 from mandato_riga m_riga,fattura_passiva a,mandato,COMPENSO c
                                                 Where
                                                        mandato.esercizio = aEs         And
                                                        mandato.cd_cds    = aCdCds      And
                                                        mandato.pg_mandato= aPgDoc      And
                                                        mandato.esercizio = m_riga.esercizio   And
                                                        mandato.cd_cds    = m_riga.cd_cds      And
                                                        mandato.pg_mandato= m_riga.pg_mandato  And
        	                                        mandato.stato_trasmissione ='T'           And
        	                                        c.fl_liquidazione_differita ='Y' And
                                                        c.cd_cds_obbligazione   = m_riga.cd_cds And
                                                        c.esercizio_obbligazione =m_riga.ESERCIZIO_OBBLIGAZIONE And
                                                        c.esercizio_ori_obbligazione =m_riga.ESERCIZIO_ORI_OBBLIGAZIONE And
                                                        c.pg_obbligazione=m_riga.PG_OBBLIGAZIONE  And
                                                        c.pg_obbligazione_scadenzario=m_riga.PG_OBBLIGAZIONE_SCADENZARIO And
                                                        a.ESERCIZIO_FATTURA_FORNITORE=C.ESERCIZIO_FATTURA_FORNITORE And
                                                	a.DT_FATTURA_FORNITORE=C.DT_FATTURA_FORNITORE And
                                                	a.NR_FATTURA_FORNITORE=C.NR_FATTURA_FORNITORE And
                                                	a.DT_REGISTRAZIONE=C.DT_REGISTRAZIONE And
			    			        a.fl_liquidazione_differita ='Y' And
        	                                        a.protocollo_iva Is Not Null)Loop
              IBMERR001.RAISE_ERR_GENERICO('Il mandato non ? annullabile! Include delle fatture passive differite per le quali ? stato gi? stampato il registro iva definitivo');
           end loop;
    End;
    -- mdurso 08/04/2011
    -- Se si cerca di annullare un mandato legato ad un compenso per il quale sono state gi? versate le ritenute
    -- bisogna blocclare l'annullamento (se la liquidazione avviene dalla 999, il controllo gi? esistente sulle PGIRO
    -- associate a documenti amministrativi non ? sufficiente poich? le pgiro sono chiuse e riaperte sulla 999)
    -- Compenso con mandato principale
    -- Rospuc 16/12/2016 Modificato controllo nuova logica liquidazione cori
    Begin
       for pgiros in (select 1 from mandato m, mandato_riga mr,compenso c,contributo_ritenuta cr, ass_obb_acr_pgiro ass, liquid_gruppo_cori_det
                     Where m.esercizio = aEs
                       And m.cd_cds    = aCdCds
                       And m.pg_mandato= aPgDoc
                       And m.esercizio = mr.esercizio
                       And m.cd_cds    = mr.cd_cds
                       And m.pg_mandato= mr.pg_mandato
                       And c.cd_cds_obbligazione    = mr.cd_cds
                       And c.esercizio_obbligazione = mr.ESERCIZIO_OBBLIGAZIONE
                       And c.esercizio_ori_obbligazione = mr.ESERCIZIO_ORI_OBBLIGAZIONE
                       And c.pg_obbligazione = mr.PG_OBBLIGAZIONE
                       And c.pg_obbligazione_scadenzario = mr.PG_OBBLIGAZIONE_SCADENZARIO
                       And cr.cd_cds = c.cd_cds
                       And cr.cd_unita_organizzativa = c.cd_unita_organizzativa
                       And cr.esercizio = c.esercizio
                       And cr.pg_compenso = c.pg_compenso
                       And ass.cd_cds = cr.cd_cds_accertamento
                       And ass.esercizio = cr.esercizio_accertamento
                       And ass.esercizio_ori_accertamento = cr.esercizio_ori_accertamento
                       And ass.pg_accertamento = cr.pg_accertamento
                       And cr.cd_cds =   liquid_gruppo_cori_det.cd_cds_origine
							         AND cr.cd_unita_organizzativa = liquid_gruppo_cori_det.cd_uo_origine
        							 AND cr.esercizio = liquid_gruppo_cori_det.esercizio_contributo_ritenuta
        							 AND cr.pg_compenso = liquid_gruppo_cori_det.pg_compenso
        							 AND cr.cd_contributo_ritenuta = liquid_gruppo_cori_det.cd_contributo_ritenuta
							         AND cr.ti_ente_percipiente =  liquid_gruppo_cori_det.ti_ente_percipiente)
       loop
              IBMERR001.RAISE_ERR_GENERICO('Il mandato non ? annullabile! Per il Compenso ad esso associato sono state gi? versate le ritenute.');
       end loop;
    End;
    -- Compenso senza mandato principale, quindi si sta annullando un mandato di ritenute
    -- Rospuc 16/12/2016 Modificato controllo nuova logica liquidazione cori
    Begin
       for pgiros in (select 1 from mandato m, mandato_riga mr,contributo_ritenuta cr, ass_obb_acr_pgiro ass, liquid_gruppo_cori_det
                     Where m.esercizio = aEs
                       And m.cd_cds    = aCdCds
                       And m.pg_mandato= aPgDoc
                       And m.esercizio = mr.esercizio
                       And m.cd_cds    = mr.cd_cds
                       And m.pg_mandato= mr.pg_mandato
                       And cr.cd_cds_obbligazione    = mr.cd_cds
                       And cr.esercizio_obbligazione = mr.ESERCIZIO_OBBLIGAZIONE
                       And cr.esercizio_ori_obbligazione = mr.ESERCIZIO_ORI_OBBLIGAZIONE
                       And cr.pg_obbligazione = mr.PG_OBBLIGAZIONE
                       And cr.pg_obbligazione_scadenzario = mr.PG_OBBLIGAZIONE_SCADENZARIO
                       And ass.cd_cds = cr.cd_cds_obbligazione
                       And ass.esercizio = cr.esercizio_obbligazione
                       And ass.esercizio_ori_obbligazione = cr.esercizio_ori_obbligazione
                       And ass.pg_obbligazione = cr.pg_obbligazione
                       And cr.cd_cds =   liquid_gruppo_cori_det.cd_cds_origine
							         AND cr.cd_unita_organizzativa = liquid_gruppo_cori_det.cd_uo_origine
        							 AND cr.esercizio = liquid_gruppo_cori_det.esercizio_contributo_ritenuta
        							 AND cr.pg_compenso = liquid_gruppo_cori_det.pg_compenso
        							 AND cr.cd_contributo_ritenuta = liquid_gruppo_cori_det.cd_contributo_ritenuta
							         AND cr.ti_ente_percipiente =  liquid_gruppo_cori_det.ti_ente_percipiente)
       loop
              IBMERR001.RAISE_ERR_GENERICO('Il mandato non ? annullabile! Esso ? relativo ad una ritenuta per la quale ? stato gi? effettuato il versamento.');
       end loop;
    End;
   -- Se si tratta di mandato non associabile a reversale esco direttamente
   begin
    select * into aManNAR from V_MANDATO_NON_ASSOCIABILE_REV where
	      esercizio=aEs
	  and cd_cds = aCdCds
	  and pg_mandato = aPgDoc;
	return;
   exception when NO_DATA_FOUND then
    null;
   end;
   -- Verifica che non si tratti del mandato degli stipendi
   for aStipCofi in (select * from stipendi_cofi where
	                            esercizio_mandato = aEs
							and cd_cds_mandato = aCdCds
							and pg_mandato = aPgDoc
					for update nowait
   ) loop
      IBMERR001.RAISE_ERR_GENERICO('Il mandato di liquidazione mensile degli stipendi non ? annullabile');
   end loop;
   -- Verifica che non si tratti di mandato di liquidazione CORI locale: in quel caso exce dalla procedura senza sollevare eccezioni
   for aLC in (select * from liquid_gruppo_cori where
	                            esercizio_doc = aEs
							and cd_cds_doc = aCdCds
							and pg_doc = aPgDoc
					for update nowait
   ) loop
    for aASSMRLC in (select * from ass_mandato_reversale where
         esercizio_mandato=aEs
	 and cd_cds_mandato = aCdCds
	 and pg_mandato =aPgDoc
	 and ti_origine = 'S'
	 for update nowait
	) loop
	 begin
	  for aRRL in (
	   select * from reversale_riga where
	         esercizio = aASSMRLC.esercizio_reversale
	 	 and cd_cds = aASSMRLC.cd_cds_reversale
		 and pg_reversale = aASSMRLC.pg_reversale
	   for update nowait
	  ) loop
	   if aRRL.cd_tipo_documento_amm = CNRCTB100.TI_GEN_CORI_VER_ENTRATA then
        null;
	   else
	    IBMERR001.RAISE_ERR_GENERICO('La '||getDesc(aRRL.esercizio,aRRL.cd_cds,aRRL.pg_reversale,'R')||
	    ' ? stata associata manualmente al '||getDesc(aEs,aCdCds,aPgDoc,aTipoDoc));
	   end if;
	  end loop;
	 end;
	end loop;
	return;
   end loop;
   begin
    select * into aMR from mandato_riga where
         esercizio  = aEs
     and cd_cds = aCdCds
     and pg_mandato = aPgDoc
     for update nowait;
   exception when TOO_MANY_ROWS then
    -- Se il mandato ha pi? di una riga non ? legato a compensi
    begin
	 checkAssManRev(aEs,aCdCds,aPgDoc,aTipoDoc,aEs,aCdCds,aPgDoc,aTipoDoc);
	 return;
    end;
    return;
   end;
   -- Il mandato ha una sola riga e non e' principale di compenso
   if aMR.cd_tipo_documento_amm <> CNRCTB100.TI_COMPENSO then
    -- Ogni collegamento con reversale in ASS_MANDATO_REVERSALE ? stato fatto a mano
    checkAssManRev(aEs,aCdCds,aPgDoc,aTipoDoc,aEs,aCdCds,aPgDoc,aTipoDoc);
	return;
   end if;

   -- Il mandato ? principale di compenso
   -- Devo scendere sui figli ed effettuare le dovute verifiche

   for aAssManRev in (select * from ass_mandato_reversale where
         cd_cds_mandato =aCdCds
	 and esercizio_mandato =aEs
	 and pg_mandato = aPgDoc
	 and ti_origine = 'S'
   ) loop
    -- Ciclo su tutte le reversali figlie di questo mandato
    -- Per ogni figlio devo verificare se appartenenva o meno all pratica principale
    -- del compenso
    begin
     -- Devo trovare il compenso di riferimento della reversale CORI
	 select * into aRR from reversale_riga where
	       esercizio = aAssManRev.esercizio_reversale
	   and cd_cds = aAssManRev.cd_cds_reversale
	   and pg_reversale = aAssManRev.pg_reversale
	 for update nowait;
    exception
	 when TOO_MANY_ROWS then
      IBMERR001.RAISE_ERR_GENERICO('La '||getDesc(aAssManRev.esercizio_reversale,aAssManRev.cd_cds_reversale,aAssManRev.pg_reversale,'R')||
	  ' ? stata associata manualmente al '||getDesc(aEs,aCdCds,aPgDoc,aTipoDoc));
	end;
	begin
     select * into aCORI from contributo_ritenuta where
	        cd_cds_accertamento = aRR.cd_cds
		and esercizio_accertamento = aRR.esercizio
		and esercizio_ori_accertamento = aRR.esercizio_ori_accertamento
		and pg_accertamento = aRR.pg_accertamento
		and pg_accertamento_scadenzario = aRR.pg_accertamento_scadenzario
		and cd_cds = aMR.cd_cds_doc_amm
		and cd_unita_organizzativa = aMR.cd_uo_doc_amm
		and esercizio = aMR.esercizio_doc_amm
		and pg_compenso = aMR.pg_doc_amm
	 for update nowait;
    exception
	 when NO_DATA_FOUND Then
	 --se l'accertamento collegato ? relativo alla sospensione del netto da pagare,
	 --vuol dire che la reversale ? stata associata automaticamente
	 Declare
	    fl_sosp   VARCHAR2(1):= 'N';
	 Begin
	    Select fl_netto_sospeso
	    Into fl_sosp
	    From accertamento
	    Where cd_cds = aRR.cd_cds
	      And esercizio = aRR.esercizio
	      And esercizio_originale = aRR.esercizio_ori_accertamento
	      And pg_accertamento = aRR.pg_accertamento;

	    If fl_sosp = 'N' Then
      IBMERR001.RAISE_ERR_GENERICO('La '||getDesc(aAssManRev.esercizio_reversale,aAssManRev.cd_cds_reversale,aAssManRev.pg_reversale,'R')||
	  ' ? stata associata manualmente al '||getDesc(aEs,aCdCds,aPgDoc,aTipoDoc));
	    End If;
	 End;
	 when TOO_MANY_ROWS then
      IBMERR001.RAISE_ERR_GENERICO('La '||getDesc(aAssManRev.esercizio_reversale,aAssManRev.cd_cds_reversale,aAssManRev.pg_reversale,'R')||
	  ' risulta collegata a pi? di un CORI');
	end;
   end loop;
   for aAssManMan in (select * from ass_mandato_mandato where
         cd_cds =aCdCds
	 and esercizio =aEs
	 and pg_mandato = aPgDoc
   ) loop
    -- Ciclo su tutti i mandati figli e verifico se hanno relazioni in ASS_MANDATO_REVERSALE
    checkAssManRev(aEs,aCdCds,aPgDoc,aTipoDoc,aAssManMan.esercizio_coll,aAssManMan.cd_cds_coll,aAssManMan.pg_mandato_coll,'M');
   end loop;
  end if; -- Fine controlli a partire da MANDATO
 end;
end;
/


