DROP ALIAS IF EXISTS ibmutl001_lock_transaction;
CREATE ALIAS ibmutl001_lock_transaction AS $$
void execute(java.sql.Connection connection) throws Exception {
}
$$;
