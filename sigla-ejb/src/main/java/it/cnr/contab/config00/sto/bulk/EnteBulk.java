package it.cnr.contab.config00.sto.bulk;
/**
 * <!-- @TODO: da completare -->
 */

public class EnteBulk extends CdsBulk {
public EnteBulk() 
{
	super();
	inizializza();
}

public EnteBulk(String cd_unita_organizzativa) 
{
	super(cd_unita_organizzativa);
	inizializza();
	this.setCd_unita_organizzativa(cd_unita_organizzativa);
}
}