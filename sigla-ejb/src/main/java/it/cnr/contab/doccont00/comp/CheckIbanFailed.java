package it.cnr.contab.doccont00.comp;

public class CheckIbanFailed extends it.cnr.jada.comp.OptionRequestException {
public CheckIbanFailed(String s) {
	
	this(s, true);
}
public CheckIbanFailed(String s, boolean doDetail) {

	super("onCheckIbanFailed", s + ((doDetail)?"\n Vuoi continuare ?":""));
	if (doDetail)
		setDetail(new CheckIbanFailed(s, false));
}
}
