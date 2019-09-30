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
public class Classificazione_speseKey extends OggettoBulk implements KeyedPersistent
{
	private java.lang.Integer esercizio;
	private String codice_cla_s;

    /**
     * 
     */
    public Classificazione_speseKey()
    {
        super();
    }

	public Classificazione_speseKey(java.lang.Integer esercizio, String codice_cla_s)
	{
		super();
		this.esercizio = esercizio;
		this.codice_cla_s = codice_cla_s;
	}
	
	public boolean equalsByPrimaryKey(Object o) {
		if (this == o) return true;
		if (!(o instanceof Classificazione_speseKey)) return false;
		Classificazione_speseKey k = (Classificazione_speseKey)o;
		if(!compareKey(getEsercizio(),k.getEsercizio())) return false;
		if(!compareKey(getCodice_cla_s(),k.getCodice_cla_s())) return false;
		return true;
	}

	public int primaryKeyHashCode() {
		return
			calculateKeyHashCode(getEsercizio())+
		    calculateKeyHashCode(getCodice_cla_s());
	}

    /**
     * @return
     */
    public String getCodice_cla_s()
    {
        return codice_cla_s;
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
    public void setCodice_cla_s(String string)
    {
        codice_cla_s = string;
    }

    /**
     * @param integer
     */
    public void setEsercizio(java.lang.Integer integer)
    {
        esercizio = integer;
    }

}
