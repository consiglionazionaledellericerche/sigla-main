package it.cnr.contab.progettiric00.ejb.geco;

import java.util.List;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentoIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_commessaIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_moduloIBulk;
import it.cnr.contab.progettiric00.geco.bulk.Geco_progettoIBulk;
import it.cnr.jada.bulk.OggettoBulk;

import javax.ejb.Remote;

@Remote
public interface ProgettoGecoComponentSession extends it.cnr.jada.ejb.CRUDComponentSession{
	public abstract List<Geco_progettoIBulk>  cercaProgettiGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException;
	public abstract List<Geco_commessaIBulk>  cercaCommesseGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException;
	public abstract List<Geco_moduloIBulk>  cercaModuliGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException;
	public abstract List<Geco_dipartimentoIBulk>  cercaDipartimentiGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException;
}
