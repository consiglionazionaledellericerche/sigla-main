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

import it.cnr.jada.persistency.Keyed;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_proceduraKey

public class Batch_proceduraBase extends Batch_proceduraKey
    implements Keyed
{

    public Batch_proceduraBase()
    {
    }

    public Batch_proceduraBase(String s)
    {
        super(s);
    }

    public String getCd_utente()
    {
        return cd_utente;
    }

    public String getDs_procedura()
    {
        return ds_procedura;
    }

    public String getObject_name()
    {
        return object_name;
    }

    public String getPackage_name()
    {
        return package_name;
    }

    public void setCd_utente(String s)
    {
        cd_utente = s;
    }

    public void setDs_procedura(String s)
    {
        ds_procedura = s;
    }

    public void setObject_name(String s)
    {
        object_name = s;
    }

    public void setPackage_name(String s)
    {
        package_name = s;
    }

    private String cd_utente;
    private String ds_procedura;
    private String object_name;
    private String package_name;
}