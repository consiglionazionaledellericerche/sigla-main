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

package it.cnr.contab.progettiric00.ejb.geco;

import java.util.List;

import it.cnr.contab.config00.geco.bulk.Geco_dipartimentiIBulk;
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
	public abstract List<Geco_dipartimentiIBulk>  cercaDipartimentiGeco(it.cnr.jada.UserContext param0, OggettoBulk oggettoBulk, Class<? extends OggettoBulk> bulkClass) throws it.cnr.jada.comp.ComponentException;
}
