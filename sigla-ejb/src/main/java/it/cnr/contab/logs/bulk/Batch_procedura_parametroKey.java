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

package it.cnr.contab.logs.bulk;

import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.persistency.KeyedPersistent;

public class Batch_procedura_parametroKey extends OggettoBulk
    implements KeyedPersistent
{

    public Batch_procedura_parametroKey()
    {
    }

    public Batch_procedura_parametroKey(String s, String s1, Integer integer)
    {
        cd_procedura = s;
        nome_parametro = s1;
        pg_valore_parametro = integer;
    }

    public boolean equalsByPrimaryKey(Object obj)
    {
        if(this == obj)
            return true;
        if(!(obj instanceof Batch_procedura_parametroKey))
            return false;
        Batch_procedura_parametroKey batch_procedura_parametrokey = (Batch_procedura_parametroKey)obj;
        if(!compareKey(getCd_procedura(), batch_procedura_parametrokey.getCd_procedura()))
            return false;
        if(!compareKey(getNome_parametro(), batch_procedura_parametrokey.getNome_parametro()))
            return false;
        return compareKey(getPg_valore_parametro(), batch_procedura_parametrokey.getPg_valore_parametro());
    }

    public String getCd_procedura()
    {
        return cd_procedura;
    }

    public String getNome_parametro()
    {
        return nome_parametro;
    }

    public Integer getPg_valore_parametro()
    {
        return pg_valore_parametro;
    }

    public int primaryKeyHashCode()
    {
        return calculateKeyHashCode(getCd_procedura()) + calculateKeyHashCode(getNome_parametro()) + calculateKeyHashCode(getPg_valore_parametro());
    }

    public void setCd_procedura(String s)
    {
        cd_procedura = s;
    }

    public void setNome_parametro(String s)
    {
        nome_parametro = s;
    }

    public void setPg_valore_parametro(Integer integer)
    {
        pg_valore_parametro = integer;
    }

    private Integer pg_valore_parametro;
    private String cd_procedura;
    private String nome_parametro;
}