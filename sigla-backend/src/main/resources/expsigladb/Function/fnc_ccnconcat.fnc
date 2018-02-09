CREATE OR REPLACE FUNCTION FNC_CCNCONCAT (p_cursor IN  SYS_REFCURSOR, p_separator IN Varchar2 default ',')
  RETURN  VARCHAR2
IS
  l_return  VARCHAR2(32767); 
  l_temp    VARCHAR2(32767);
BEGIN
  LOOP
    FETCH p_cursor
    INTO  l_temp;
    EXIT WHEN p_cursor%NOTFOUND;
    l_return := l_return || p_separator || l_temp;
  END LOOP;
  RETURN LTRIM(l_return, p_separator);
END;
/


