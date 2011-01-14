package it.cnr.contab.reports.bulk;

public class Print_spooler_paramBulk extends Print_spooler_paramBase {

	private Print_spooler_paramKey key;
	
	public Print_spooler_paramBulk() {
		super();
		key = new Print_spooler_paramKey();
	}

	public Print_spooler_paramBulk(java.lang.String nome_param,
			java.lang.Long pg_stampa) {
		super(nome_param, pg_stampa);
		key = new Print_spooler_paramKey(nome_param, pg_stampa);
	}
	
	@Override
	public void setNomeParam(String nomeParam) {
		super.setNomeParam(nomeParam);
		key.setNomeParam(nomeParam);
	}
	
	@Override
	public void setPgStampa(Long pgStampa) {
		super.setPgStampa(pgStampa);
		key.setPgStampa(pgStampa);
	}
}