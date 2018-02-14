CREATE OR REPLACE PACKAGE pipe As
 PROCEDURE get_message;
 Procedure send_message  (MESSAGGIO IN varchar2);
End pipe;


CREATE OR REPLACE PACKAGE BODY pipe AS
  namedpipe             VARCHAR2(30) :='SPASIA';
  endofpipe             NUMBER;
  timeonpipe            NUMBER;
  pipe_returncode       NUMBER;
  p_thetime             DATE;
  load_timeout          NUMBER := 5;
  load_size             NUMBER := 4096;

  PROCEDURE send_message (MESSAGGIO IN varchar2) IS
  BEGIN
   DBMS_PIPE.PACK_MESSAGE(MESSAGGIO);
   pipe_returncode := DBMS_PIPE.SEND_MESSAGE(namedpipe,load_timeout,load_size);
  END send_message;

  PROCEDURE get_message (timeonpipe IN NUMBER,
                         endofpipe OUT NUMBER) IS
    mess Varchar2(250);
  BEGIN
   endofpipe := 0;
     pipe_returncode := DBMS_PIPE.RECEIVE_MESSAGE(namedpipe, timeonpipe);
     IF pipe_returncode = 0 THEN
      DBMS_PIPE.UNPACK_MESSAGE(mess);
      DBMS_OUTPUT.PUT_LINE(mess);
     ELSE
      endofpipe := 1;
     END IF;
  Exception
    When Others Then DBMS_OUTPUT.PUT_LINE(Sqlerrm);
  END get_message;

  PROCEDURE get_message IS
    endofpipe NUMBER := 0;
  BEGIN
    While endofpipe != 1 Loop
      pipe.get_message(1,endofpipe);
    End Loop;
  END get_message;
End pipe;


