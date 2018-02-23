package it.cnr.contab.doccont00.bp;

import it.cnr.contab.doccont00.core.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.*;
/**
 * Business Process che gestisce le attività di CRUD per l'entita' Impegno Partita di Giro Tronco.
 */
public class CRUDImpegnoPGiroTroncoBP extends CRUDImpegnoPGiroBP {
/**
 * CRUDImpegnoPGiroTroncoBP constructor comment.
 */
public CRUDImpegnoPGiroTroncoBP() {
	super();
}
/**
 * CRUDImpegnoPGiroTroncoBP constructor comment.
 * @param function java.lang.String
 */
public CRUDImpegnoPGiroTroncoBP(String function) {
	super(function);
}
public String getFormTitle() {
	StringBuffer title = new StringBuffer( "Gestione Annotazione di Spesa su Partita di Giro tronca");
	title.append(" - ");
	switch(getStatus()) {
		case INSERT:
			title.append("Inserimento");
			break;
		case EDIT:
			title.append("Modifica");
			break;
		case SEARCH:
			title.append("Ricerca");
			break;
		case VIEW:
			title.append("Visualizza");
			break;
	}
	return title.toString();
}
/**
  * Inizializzo l'oggetto bulk in fase di ricerca libera, impostando a true il flag
  * che identifica l'impegno su partita di giro "tronco"
  */
public OggettoBulk initializeModelForFreeSearch(ActionContext context,OggettoBulk bulk) throws BusinessProcessException 
{
	try 
	{
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) super.initializeModelForFreeSearch( context, bulk );
		imp_pgiro.setFl_isTronco( true );
		return imp_pgiro;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
  * Inizializzo l'oggetto bulk in fase di inserimento, impostando a true il flag
  * che identifica l'impegno su partita di giro "tronco"
  */
public OggettoBulk initializeModelForInsert(ActionContext context,OggettoBulk bulk) throws BusinessProcessException 
{
	try 
	{
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) super.initializeModelForInsert( context, bulk );
		imp_pgiro.setFl_isTronco( true );
		return imp_pgiro;
	} catch(Exception e) {
		throw handleException(e);
	}
}
/**
  * Inizializzo l'oggetto bulk in fase di ricerca, impostando a true il flag
  * che identifica l'impegno su partita di giro "tronco"
  */
public OggettoBulk initializeModelForSearch(ActionContext context,OggettoBulk bulk) throws BusinessProcessException 
{
	try 
	{
		ImpegnoPGiroBulk imp_pgiro = (ImpegnoPGiroBulk) super.initializeModelForSearch( context, bulk );
		imp_pgiro.setFl_isTronco( true );
		return imp_pgiro;
	} catch(Exception e) {
		throw handleException(e);
	}
}
}
