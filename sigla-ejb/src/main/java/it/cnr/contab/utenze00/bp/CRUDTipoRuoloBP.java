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

package it.cnr.contab.utenze00.bp;

import it.cnr.contab.utenze00.bulk.*;
import it.cnr.jada.action.*;
import it.cnr.jada.util.action.*;

public class CRUDTipoRuoloBP extends SimpleCRUDBP {
	private final SimpleDetailCRUDController crudPrivilegi_disponibili = new SimpleDetailCRUDController("Privilegi_disponibili",PrivilegioBulk.class,"privilegi_disponibili",this);
	private final SimpleDetailCRUDController crudPrivilegi = new SimpleDetailCRUDController("Privilegi",PrivilegioBulk.class,"privilegi",this);
	
public CRUDTipoRuoloBP() throws BusinessProcessException {
	super();

}
public CRUDTipoRuoloBP( String function ) throws BusinessProcessException {
	super(function);

}

/**
 * Restituisce il Controller che gestisce il dettaglio dei Privilegi già assegnati ad un Tipo Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudPrivilegi() {
	return crudPrivilegi;
}
/**
 * Restituisce il Controller che gestisce il dettaglio dei Privilegi ancora disponibili per un Tipo Ruolo
 * @return it.cnr.jada.util.action.SimpleDetailCRUDController controller
 */
public final it.cnr.jada.util.action.SimpleDetailCRUDController getCrudPrivilegi_disponibili() {
	return crudPrivilegi_disponibili;
}
}
