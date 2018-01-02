package it.cnr.contab.ordmag.anag00.comp;

import java.io.Serializable;

import it.cnr.contab.ordmag.anag00.AssUnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdBulk;
import it.cnr.contab.ordmag.anag00.UnitaOperativaOrdHome;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.CRUDComponent;
import it.cnr.jada.comp.ComponentException;
import it.cnr.jada.comp.ICRUDMgr;

public class AssUnitaOperativaOrdComponent extends CRUDComponent implements ICRUDMgr,Cloneable,Serializable{
	public OggettoBulk inizializzaBulkPerModifica(UserContext usercontext, OggettoBulk oggettobulk) throws ComponentException {
		try {
			oggettobulk =  super.inizializzaBulkPerModifica(usercontext, oggettobulk);
			if (oggettobulk instanceof UnitaOperativaOrdBulk) {
				
				UnitaOperativaOrdHome uopHome = (UnitaOperativaOrdHome) getHome( usercontext, UnitaOperativaOrdBulk.class);
				BulkList<OggettoBulk> lista= new BulkList( uopHome.findAssUnitaOperativaList((UnitaOperativaOrdBulk)oggettobulk ) );
				
				for (OggettoBulk oggetto: lista){
					AssUnitaOperativaOrdBulk ass = (AssUnitaOperativaOrdBulk)oggetto;
					UnitaOperativaOrdBulk unitaRif = ass.getUnitaOperativaOrdRif();
					if (unitaRif != null){
						UnitaOperativaOrdBulk uopRif = (UnitaOperativaOrdBulk) getHome( usercontext, UnitaOperativaOrdBulk.class ).findByPrimaryKey( new UnitaOperativaOrdBulk( unitaRif.getCdUnitaOperativa()));
						ass.setUnitaOperativaOrdRif(uopRif);
					}
				}
				((UnitaOperativaOrdBulk)oggettobulk).setUnitaOperativaColl(lista);
			}
			return oggettobulk;
		}
		catch( Exception e )
		{
			throw handleException( e );
		}		
	}
}
