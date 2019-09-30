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

package it.cnr.contab.config00.sto.bulk;
/**
 * <!-- @TODO: da completare -->
 */

public class Unita_organizzativa_enteBulk extends Unita_organizzativaBulk 
{
public Unita_organizzativa_enteBulk() 
{
	super();
	inizializza();
}

public Unita_organizzativa_enteBulk(String cd_unita_organizzativa) 
{
	super(cd_unita_organizzativa);
	inizializza();
	this.setCd_unita_organizzativa(cd_unita_organizzativa);
}

private void inizializza()
{
	setFl_cds( new Boolean( false ) );
	setCd_tipo_unita(Tipo_unita_organizzativaHome.TIPO_UO_ENTE);
}	
}