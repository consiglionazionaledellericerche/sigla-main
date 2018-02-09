CREATE OR REPLACE PACKAGE CNRCTB940 is
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
/


CREATE OR REPLACE PACKAGE BODY CNRCTB940 AS
PROCEDURE estrazioneIndicatorePag
   (
    inData date
   ) IS
	aProcedure VARCHAR2(2000);
  inUtente Varchar2(20):='CED_RP';
BEGIN

	 -- Attivazione della gestione batch per estrazione

   aProcedure:='CNRCTB940.job_estrazioneIndicatore(job, ' ||
                                            'pg_exec, ' ||
                                            'next_date, ' ||
                                            to_char(inData,'dd/mm/yyyy') || ''','''||
                                            'CED' || ''');';


   IBMUTL210.creaBatchDinamico('Estrazione dati indicatore',
                               aProcedure,
                               inUtente,
                               inData);

   IBMUTL001.deferred_commit;

   IBMERR001.RAISE_ERR_GENERICO
      ('Operazione sottomessa per esecuzione. Al completamento l''utente ricever? un messaggio di notifica ' ||
       'dello stato dell''operazione');

END estrazioneIndicatorePag;

PROCEDURE job_estrazioneIndicatore
   (
    job NUMBER,
    pg_exec NUMBER,
    next_date DATE,
    inData VARCHAR2,
    inUtente VARCHAR2
   ) IS
   aStringa VARCHAR2(2000);
BEGIN

   -- Lancio start esecuzione log


   IBMUTL210.logStartExecutionUpd(pg_exec, IDTIPOLOG, job, 'Richiesta utente:' || inUtente,
                                  'Estrazione dati indicatore. Start:' || TO_CHAR(sysdate,'YYYY/MM/DD HH-MI-SS'));
   BEGIN

			pop_storico_indicatore(to_date(inData,'dd/mm/yyyy'),add_months(to_date(inData,'dd/mm/yyyy'),3));

      COMMIT;

      -- Messaggio di operazione completata ad utente

      IBMUTL205.LOGINF('Estrazione dati indicatore',
                       'Estrazione dati indicatore ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS'),
                       'Operazione completata con successo',
                       inUtente);
   EXCEPTION
      WHEN others THEN
           ROLLBACK;
      -- Messaggio di attenzione ad utente
      IBMUTL205.LOGWAR('Estrazione dati indicatore',
                       'Estrazione dati indicatore ' || TO_CHAR(sysdate,'DD/MM/YYYY HH:MI:SS') || ' ' ||
                       '(pg_exec = ' || pg_exec || ')', DBMS_UTILITY.FORMAT_ERROR_STACK, inUtente);
   END;
END job_estrazioneIndicatore;
End;
/


