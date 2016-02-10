package it.cnr.contab.docamm00.client;

public class TrovatoProperties {
	private String trovatoTargetEndpoint; //="http://siglaas4.cedrc.cnr.it:8480/";
	private String trovatoSiglaWSClientPassword;
	public String getTrovatoTargetEndpoint() {
		return trovatoTargetEndpoint;
	}
	public void setTrovatoTargetEndpoint(String trovatoTargetEndpoint) {
		this.trovatoTargetEndpoint = trovatoTargetEndpoint;
	}
	public String getTrovatoSiglaWSClientPassword() {
		return trovatoSiglaWSClientPassword;
	}
	public void setTrovatoSiglaWSClientPassword(String trovatoSiglaWSClientPassword) {
		this.trovatoSiglaWSClientPassword = trovatoSiglaWSClientPassword;
	}
}
