CREATE OR REPLACE PROCEDURE SPEDISCI_MAIL
(
Mittente IN VARCHAR2,
Destinatario IN VARCHAR2,
Oggetto IN VARCHAR2,
Messaggio IN VARCHAR2
)
IS
-- dichiarazione di una variabile a cui assegnare il nome del server SMTP utilizzato
mailhost VARCHAR2(30) := 'smtp.cnr.it';
-- dichiarazione di una variabile di tipo utl_smtp.connection
-- a cui verr? assegnato l'SMTP ed il numero di porta (25)
conn utl_smtp.connection;
crlf VARCHAR2( 2 ):= CHR( 13 ) || CHR( 10 );
mesg VARCHAR2( 1000 );
local_mittente VARCHAR2(2000) := mittente;
BEGIN
-- Apertura di una connessione al server
conn := utl_smtp.open_connection (mailhost,25);
--conn := utl_smtp.open_connection (mailhost,465);
-- Assegnazione alla variabile mesg dell'intestazione della mail
mesg:='Date:'||TO_CHAR(SYSDATE,'dd mon yy hh24:mi:ss')||crlf||
'From:<'||mittente||'>'||crlf||
'Subject:'||Oggetto||crlf||
'To:'||destinatario||crlf||''
||crlf||messaggio;
-- Apertura di un collegamento al server
utl_smtp.helo(conn, mailhost);
-- Inizio di una transazione di posta con il server passandogli il mittente
utl_smtp.mail(conn,local_mittente);
-- il destinatario
utl_smtp.rcpt(conn,destinatario);
-- ed il testo del messaggio
utl_smtp.data(conn, mesg);
-- Chiusura della connessione
utl_smtp.quit(conn);
END;
/


