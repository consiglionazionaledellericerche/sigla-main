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


// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_proceduraBase

import java.util.ArrayList;
import java.util.List;

public class Batch_proceduraBulk extends Batch_proceduraBase
{

    public Batch_proceduraBulk()
    {
    }

    public Batch_proceduraBulk(String s)
    {
        super(s);
    }

    public boolean isProceduraJava() {
        return "PROCEDURE_JAVA".equals(this.getObject_name());
    }

    public static List<Batch_procedura_parametroBulk> getParametriProceduraJava(String nomeProcedura) {
        ArrayList<Batch_procedura_parametroBulk> arraylist = new ArrayList<>();
        if ("REGISTRACOGECOANJAVA".equals(nomeProcedura)) {
            Batch_procedura_parametroBulk batch_procedura_parametrobulk = new Batch_procedura_parametroBulk(nomeProcedura, "AES", null);
            batch_procedura_parametrobulk.setTipoParametro(1);

            arraylist.add(batch_procedura_parametrobulk);

            Batch_procedura_parametroBulk batch_procedura_parametrobulk2 = new Batch_procedura_parametroBulk(nomeProcedura, "ACDCDS", null);
            batch_procedura_parametrobulk2.setTipoParametro(0);
            arraylist.add(batch_procedura_parametrobulk2);
        }
        return arraylist;
    }
}