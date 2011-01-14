package it.cnr.contab.doccont00.comp;

public class CheckDisponibilitaCassaFailed extends it.cnr.jada.comp.OptionRequestException {
public CheckDisponibilitaCassaFailed(String s) {
	
	this(s, true);
}
public CheckDisponibilitaCassaFailed(String s, boolean doDetail) {

	super("onCheckDisponibilitaCassaFailed", s + ((doDetail)?"\nVuoi continuare ?":""));
	if (doDetail)
		setDetail(new CheckDisponibilitaCassaFailed(s, false));
}
}
