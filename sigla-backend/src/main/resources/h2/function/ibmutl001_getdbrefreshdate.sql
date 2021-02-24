DROP ALIAS IF EXISTS ibmutl001_getdbrefreshdate;
CREATE ALIAS ibmutl001_getdbrefreshdate AS $$
java.util.Date execute(java.sql.Connection connection) throws Exception {
  return new java.util.Date();
}
$$;
