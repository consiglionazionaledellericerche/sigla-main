DROP ALIAS IF EXISTS ibmutl001_lock_transaction;
CREATE ALIAS ibmutl001_lock_transaction AS $$
void execute(java.sql.Connection connection) throws Exception {
  java.sql.PreparedStatement preparedStatementMaster = connection.prepareStatement("insert into TRANSLOCK_MASTER(SID) VALUES (SESSION_ID())");
  preparedStatementMaster.execute();

  java.sql.PreparedStatement preparedStatement = connection.prepareStatement("insert into TRANSLOCK(SID,STATUS) VALUES (SESSION_ID(),0)");
  preparedStatement.execute();
}
$$;
