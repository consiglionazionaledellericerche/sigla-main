/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
