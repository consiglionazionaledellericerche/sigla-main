package it.cnr.contab.util.servlet;

public class JSONRESTRequest {
	private Context context;
	
	public JSONRESTRequest() {
		super();
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public static class Context {
		private Integer esercizio;
		private String cd_cds, cd_unita_organizzativa, cd_cdr;
		
		public Context() {
			super();
		}
		public Integer getEsercizio() {
			return esercizio;
		}
		public void setEsercizio(Integer esercizio) {
			this.esercizio = esercizio;
		}
		public String getCd_cds() {
			return cd_cds;
		}
		public void setCd_cds(String cd_cds) {
			this.cd_cds = cd_cds;
		}
		public String getCd_unita_organizzativa() {
			return cd_unita_organizzativa;
		}
		public void setCd_unita_organizzativa(String cd_unita_organizzativa) {
			this.cd_unita_organizzativa = cd_unita_organizzativa;
		}
		public String getCd_cdr() {
			return cd_cdr;
		}
		public void setCd_cdr(String cd_cdr) {
			this.cd_cdr = cd_cdr;
		}		
	}
}