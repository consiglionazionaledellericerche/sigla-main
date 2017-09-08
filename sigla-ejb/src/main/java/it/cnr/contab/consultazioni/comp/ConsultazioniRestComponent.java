package it.cnr.contab.consultazioni.comp;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Optional;

import javax.ejb.EJBException;

import it.cnr.contab.anagraf00.core.bulk.InquadramentoBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneBulk;
import it.cnr.contab.anagraf00.tabter.bulk.NazioneHome;
import it.cnr.contab.anagraf00.util.ExPartitaIVA;
import it.cnr.contab.anagraf00.util.PartitaIVAControllo;
import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageBulk;
import it.cnr.contab.config00.latt.bulk.WorkpackageHome;
import it.cnr.contab.config00.pdcfin.bulk.Elemento_voceBulk;
import it.cnr.contab.config00.sto.bulk.CdrBulk;
import it.cnr.contab.config00.sto.bulk.CdrHome;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativaBulk;
import it.cnr.contab.config00.sto.bulk.Unita_organizzativa_enteBulk;
import it.cnr.contab.consultazioni.bulk.ConsultazioniRestHome;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmAttiviBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VDocAmmBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaAttivaRigaBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaAttivaRigaBrevettiHome;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaPassivaRigaBrevettiBulk;
import it.cnr.contab.docamm00.consultazioni.bulk.VFatturaPassivaRigaBrevettiHome;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VConsObbligazioniGaeBulk;
import it.cnr.contab.doccont00.consultazioni.bulk.VSitGaeResiduiSpesaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaBulk;
import it.cnr.contab.doccont00.core.bulk.Mandato_rigaHome;
import it.cnr.contab.doccont00.intcass.bulk.V_mandato_reversaleBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoBulk;
import it.cnr.contab.missioni00.docs.bulk.RimborsoHome;
import it.cnr.contab.missioni00.ejb.MissioneComponentSession;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_rimborso_kmBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_pastoBulk;
import it.cnr.contab.missioni00.tabrif.bulk.Missione_tipo_spesaBulk;
import it.cnr.contab.pdg01.consultazioni.bulk.VConsVarCompResBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestHome;
import it.cnr.contab.progettiric00.core.bulk.ProgettoGestUoBulk;
import it.cnr.contab.progettiric00.core.bulk.ProgettoHome;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.persistency.IntrospectionException;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.CompoundFindClause;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.Query;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.persistency.sql.SimpleFindClause;

/**
 * @author mspasiano
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ConsultazioniRestComponent extends CRUDComponent {

	@Override
	protected Query select(UserContext userContext, CompoundFindClause compoundfindclause, OggettoBulk oggettobulk) throws ComponentException, PersistencyException {
		SQLBuilder sql =  (SQLBuilder)super.select(userContext, compoundfindclause, oggettobulk);
        final Optional<ConsultazioniRestHome> consultazioniRestHome = Optional.ofNullable(getHome(userContext, oggettobulk))
                .filter(ConsultazioniRestHome.class::isInstance)
                .map(ConsultazioniRestHome.class::cast);
        if (consultazioniRestHome.isPresent()) {
            consultazioniRestHome.get().restSelect(userContext, sql, compoundfindclause, oggettobulk);
        }
		return sql;
	}
}