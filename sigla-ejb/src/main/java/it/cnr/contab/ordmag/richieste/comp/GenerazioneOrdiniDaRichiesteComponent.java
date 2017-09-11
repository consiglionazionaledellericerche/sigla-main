package it.cnr.contab.ordmag.richieste.comp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJBException;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;

import it.cnr.contab.cmis.MimeTypes;
import it.cnr.contab.cmis.bulk.CMISFile;
import it.cnr.contab.cmis.service.CMISPath;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.ejb.Parametri_cnrComponentSession;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceHome;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.V_struttura_organizzativaHome;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Bene_servizioHome;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventBulk;
import it.cnr.contab.docamm00.tabrif.bulk.Categoria_gruppo_inventHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneHome;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneOrdBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneResBulk;
import it.cnr.contab.doccont00.core.bulk.ObbligazioneRes_impropriaBulk;
import it.cnr.contab.doccont00.core.bulk.V_obbligazione_im_mandatoBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperBulk;
import it.cnr.contab.ordmag.anag00.AbilUtenteUopOperHome;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.NumerazioneOrdHome;
import it.cnr.contab.ordmag.anag00.TipoOperazioneOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraBulk;
import it.cnr.contab.ordmag.anag00.UnitaMisuraHome;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.contab.ordmag.ejb.NumeratoriOrdMagComponentSession;
import it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopBulk;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopHome;
import it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk;
import it.cnr.contab.ordmag.richieste.bulk.VRichiestaPerOrdiniBulk;
import it.cnr.contab.ordmag.richieste.service.RichiesteCMISService;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.reports.bulk.Print_spoolerBulk;
import it.cnr.contab.reports.bulk.Report;
import it.cnr.contab.reports.service.PrintService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.util.Utility;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.GenerazioneReportException;
import it.cnr.jada.comp.ICRUDMgr;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;

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
