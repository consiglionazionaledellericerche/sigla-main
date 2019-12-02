DROP ALIAS IF EXISTS ibmutl001_trace_user_connection;
CREATE ALIAS ibmutl001_trace_user_connection AS $$
void execute(java.sql.Connection connection) throws Exception {

}
$$;
