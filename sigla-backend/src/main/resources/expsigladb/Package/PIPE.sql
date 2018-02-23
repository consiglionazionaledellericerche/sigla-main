--------------------------------------------------------
--  DDL for Package PIPE
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "PIPE" As
 PROCEDURE get_message;
 Procedure send_message  (MESSAGGIO IN varchar2);
End pipe;
