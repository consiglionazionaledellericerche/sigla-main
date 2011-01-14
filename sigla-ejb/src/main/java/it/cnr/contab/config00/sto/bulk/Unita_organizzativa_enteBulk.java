package it.cnr.contab.config00.sto.bulk;
/**
 * <!-- @TODO: da completare -->
 */

public class Unita_organizzativa_enteBulk extends Unita_organizzativaBulk 
{
public Unita_organizzativa_enteBulk() 
{
	super();
	inizializza();
}

public Unita_organizzativa_enteBulk(String cd_unita_organizzativa) 
{
	super(cd_unita_organizzativa);
	inizializza();
	this.setCd_unita_organizzativa(cd_unita_organizzativa);
}

private void inizializza()
{
	setFl_cds( new Boolean( false ) );
	setCd_tipo_unita(Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
}	
}