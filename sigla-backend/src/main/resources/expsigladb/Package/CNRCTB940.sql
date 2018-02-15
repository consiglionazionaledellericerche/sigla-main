--------------------------------------------------------
--  DDL for Package CNRCTB940
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "CNRCTB940" is
   IDTIPOLOG CONSTANT VARCHAR2(20) := 'UNDEF00';

 PROCEDURE estrazioneIndicatorePag(
    inData date
   );
PROCEDURE job_estrazioneIndicatore
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inData VARCHAR2,
    inUtente VARCHAR2
   );
  End;
