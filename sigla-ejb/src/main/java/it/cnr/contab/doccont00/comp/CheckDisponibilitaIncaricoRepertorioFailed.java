package it.cnr.contab.doccont00.comp;

public class CheckDisponibilitaIncaricoRepertorioFailed extends it.cnr.jada.comp.OptionRequestException {
public CheckDisponibilitaIncaricoRepertorioFailed(String s) {
	
	this(s, true);
}
public CheckDisponibilitaIncaricoRepertorioFailed(String s, boolean doDetail) {

	super("onCheckDisponibilitaIncaricoRepertorioFailed", s + ((doDetail)?"\nVuoi continuare ?":""));
	if (doDetail)
		setDetail(new CheckDisponibilitaIncaricoRepertorioFailed(s, false));
}
}
