package it.cnr.contab.progettiric00.bp;

import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.jada.bulk.BulkInfo;

/**
 * @author user
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class TestataProgettiRicercaNuovoBP extends TestataProgettiRicercaBP implements IProgettoBP{

	/**
	 * Constructor for TestataProgettiRicercaNuovoBP.
	 */
	public TestataProgettiRicercaNuovoBP() {
		super();
	}

	/**
	 * Constructor for TestataProgettiRicercaNuovoBP.
	 * @param function
	 */
	public TestataProgettiRicercaNuovoBP(String function) {
		super(function);
	}
	
	public BulkInfo getBulkInfo()
	{
		BulkInfo infoBulk = super.getBulkInfo();
		infoBulk.setShortDescription("Progetti");
		return infoBulk;
	}	
	/*
	 * Utilizzato per la gestione del titolo
	 * Sovrascrive il metodo si CRUDBP
	 * */
	public String getFormTitle()
	{
		StringBuffer stringbuffer = new StringBuffer("Progetti di Ricerca");
		stringbuffer.append(" - ");
		switch(getStatus())
		{
		case 1: // '\001'
			stringbuffer.append("Inserimento");
			break;

		case 2: // '\002'
			stringbuffer.append("Modifica");
			break;

		case 0: // '\0'
			stringbuffer.append("Ricerca");
			break;

		case 5: // '\005'
			stringbuffer.append("Visualizza");
			break;
		}
		return stringbuffer.toString();
	}
	public String getSearchResultColumnSet() {

		return "filtro_ricerca_progetti";

	}	
	public int getLivelloProgetto() {
		return ProgettoBulk.LIVELLO_PROGETTO_PRIMO.intValue();
	}
}
