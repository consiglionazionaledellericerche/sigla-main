DROP ALIAS IF EXISTS ibmutl001_unlock_transaction;
CREATE ALIAS ibmutl001_unlock_transaction AS $$
void execute(java.sql.Connection connection) throws Exception {
  java.sql.PreparedStatement preparedStatement = connection.prepareStatement("delete from TRANSLOCK where SID = SESSION_ID()");
  preparedStatement.execute();

  java.sql.PreparedStatement preparedStatementMaster = connection.prepareStatement("delete from TRANSLOCK_MASTER where SID = SESSION_ID()");
  preparedStatementMaster.execute();
}
$$;