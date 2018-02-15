--------------------------------------------------------
--  DDL for Package Body CNRCTB062
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "CNRCTB062" is

 function recupero_reversale_riga(cds varchar2, uo varchar2, P_esercizio number, tipo_doc_amm varchar2, P_pg_doc_amm number) return reversale_riga%rowtype is
 	rr reversale_riga%rowtype;
 begin
	select *
	into	rr
	from reversale_riga
	where	cd_cds_doc_amm = cds	and
				cd_uo_doc_amm = uo		and
				esercizio_doc_amm = P_esercizio		and
				cd_tipo_documento_amm = tipo_doc_amm		and
				PG_doc_amm = P_PG_DOC_AMM		and
				stato != 'A' AND
				ROWNUM = 1;
	return rr;					
 end;

 function recupero_reversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return reversale%rowtype is
 	rr reversale_riga%rowtype;
 	r  reversale%rowtype;
 begin
   rr := recupero_reversale_riga( cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	 if rr.esercizio is not null then
		 select *
		 into	r
		 from reversale
		 where cd_cds = rr.cd_cds	and
					esercizio = rr.esercizio		and
					pg_reversale = rr.pg_reversale;
	 end if;
	 return r;
 END;
 	
 function getEsercizioReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number is
 	rr reversale_riga%rowtype;
 begin
	rr := recupero_reversale_riga(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return rr.esercizio;
 end;

 function getPgReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number is
 	rr reversale_riga%rowtype;
 begin
	rr := recupero_reversale_riga(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return rr.pg_reversale;
 end;

 function getDataReversale(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return date is
 	r  reversale%rowtype;
 begin
	r := recupero_reversale(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return r.dt_trasmissione;
 end;

 function recupero_mandato_riga(cds varchar2, uo varchar2, P_esercizio number, tipo_doc_amm varchar2, P_pg_doc_amm number) return mandato_riga%rowtype is
 	mr mandato_riga%rowtype;
 begin
	select *
	into	mr
	from mandato_riga
	where	cd_cds_doc_amm = cds	and
				cd_uo_doc_amm = uo		and
				esercizio_doc_amm = P_esercizio		and
				cd_tipo_documento_amm = tipo_doc_amm		and
				PG_doc_amm = P_PG_doc_amm		and
				stato != 'A' AND
				ROWNUM = 1;
	return mr;					
 end;

 function recupero_mandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return mandato%rowtype is
 	mr mandato_riga%rowtype;
 	m  mandato%rowtype;
 begin
   mr := recupero_mandato_riga( cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	 if mr.esercizio is not null then
		 select *
		 into	m
		 from mandato
		 where cd_cds = mr.cd_cds	and
					esercizio = mr.esercizio		and
					pg_mandato = mr.pg_mandato;
	 end if;
	 return m;
 END;
 	
 function getEsercizioMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number is
 	mr mandato_riga%rowtype;
 begin
	mr := recupero_mandato_riga(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return mr.esercizio;
 end;

 function getPgMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return number is
 	mr mandato_riga%rowtype;
 begin
	mr := recupero_mandato_riga(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return mr.pg_mandato;
 end;

 function getDataMandato(cds varchar2, uo varchar2, esercizio number, tipo_doc_amm varchar2, pg_doc_amm number) return date is
 	m  mandato%rowtype;
 begin
	m := recupero_mandato(cds, uo, esercizio, tipo_doc_amm, pg_doc_amm);
	return m.dt_trasmissione;
 end;
End;
