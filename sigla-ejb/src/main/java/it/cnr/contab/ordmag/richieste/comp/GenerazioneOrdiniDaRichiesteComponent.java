package it.cnr.contab.ordmag.richieste.comp;

import java.io.Serializable;

import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public class GenerazioneOrdiniDaRichiesteComponent
	extends it.cnr.jada.comp.CRUDComponent
	implements ICRUDMgr,Cloneable,Serializable {

	public final static String TIPO_TOTALE_COMPLETO = "C";
	public final static String TIPO_TOTALE_PARZIALE = "P";
	
    public  GenerazioneOrdiniDaRichiesteComponent()
    {

        /*Default constructor*/


    }
    
public RichiestaUopRigaBulk selezionaRichiestaPerOrdine (UserContext aUC,VRichiestaPerOrdiniBulk richiesta) throws ComponentException
{
	try
	{

		RichiestaUopRigaBulk richiestaRiga = (RichiestaUopRigaBulk) getHome( aUC, RichiestaUopRigaBulk.class).findByPrimaryKey( new RichiestaUopRigaBulk( richiesta.getCdCds(), richiesta.getCdUnitaOperativa(), richiesta.getEsercizio(), richiesta.getCdNumeratore(), richiesta.getNumero(), richiesta.getRiga() ));

		if ( richiestaRiga == null )
			throw new ApplicationException( "Richiesta non esistente" );

		lockBulk( aUC, richiestaRiga );
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_ANNULLATO))
			throw new ApplicationException("La richiesta è stata annullata.");
		if ( richiestaRiga.getStato().equals(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE))
			throw new ApplicationException("La richiesta è già stata trasformata in ordine.");
		if ( !richiestaRiga.getRichiestaUop().getStato().equals(RichiestaUopBulk.STATO_INVIATA_ORDINE))
			throw new ApplicationException("La richiesta non è stata inviata in ordine");
		richiestaRiga.setStato(RichiestaUopRigaBulk.STATO_TRASFORMATA_ORDINE);
		richiestaRiga.setUser( aUC.getUser());
		updateBulk( aUC, richiestaRiga );
		return richiestaRiga;
	}
	catch ( Exception e )
	{
		throw handleException( richiesta, e )	;
	}
}
}
