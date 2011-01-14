package it.cnr.contab.doccont00.comp;

public class CheckDisponibilitaContrattoFailed extends it.cnr.jada.comp.OptionRequestException {
public CheckDisponibilitaContrattoFailed(String s) {
	
	this(s, true);
}
public CheckDisponibilitaContrattoFailed(String s, boolean doDetail) {

	super("onCheckDisponibilitaContrattoFailed", s + ((doDetail)?"\nVuoi continuare ?":""));
	if (doDetail)
		setDetail(new CheckDisponibilitaContrattoFailed(s, false));
}
}
