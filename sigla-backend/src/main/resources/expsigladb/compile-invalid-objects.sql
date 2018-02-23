BEGIN
  FOR cur_rec IN (SELECT object_name,
                         object_type,
                         DECODE(object_type, 'PACKAGE', 1,
                                             'PACKAGE BODY', 2, 2) AS recompile_order
                  FROM   user_objects
                  WHERE  status != 'VALID'
                  ORDER BY 3)
  LOOP
    BEGIN
      IF cur_rec.object_type = 'PACKAGE BODY' THEN
        EXECUTE IMMEDIATE 'ALTER PACKAGE '|| cur_rec.object_name || ' COMPILE BODY';
      ElSE
          EXECUTE IMMEDIATE 'ALTER ' || cur_rec.object_type || 
            ' ' || cur_rec.object_name || ' COMPILE';
      END IF;
    EXCEPTION
      WHEN OTHERS THEN
        DBMS_OUTPUT.put_line(cur_rec.object_type || ' : ' ||' : ' || cur_rec.object_name);
    END;
  END LOOP;
END;
