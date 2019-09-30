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

package it.cnr.contab.config00.pdcfin.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

/**
 * @author aimprota
 *
 */
public class Classificazione_entrateKey extends OggettoBulk implements KeyedPersistent
{
	private java.lang.Integer esercizio;
	private String codice_cla_e;

    /**
     * 
     */
    public Classificazione_entrateKey()
    {
        super();
    }

	public Classificazione_entrateKey(java.lang.Integer esercizio, String codice_cla_e)
	{
		super();
		this.esercizio = esercizio;
		this.codice_cla_e = codice_cla_e;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Classificazione_entrateKey)) return false;
		Classificazione_entrateKey k = (Classificazione_entrateKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCodice_cla_e(),k.getCodice_cla_e())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
		    calculateKeyHashCode(getCodice_cla_e());
	}

    /**
     * @return
     */
    public String getCodice_cla_e()
    {
        return codice_cla_e;
    }

    /**
     * @return
     */
    public java.lang.Integer getEsercizio()
    {
        return esercizio;
    }

    /**
     * @param string
     */
    public void setCodice_cla_e(String string)
    {
        codice_cla_e = string;
    }

    /**
     * @param integer
     */
    public void setEsercizio(java.lang.Integer integer)
    {
        esercizio = integer;
    }

}
