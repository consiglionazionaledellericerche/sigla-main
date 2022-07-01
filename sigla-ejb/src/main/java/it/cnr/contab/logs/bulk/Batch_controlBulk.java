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

import it.cnr.jada.bulk.ValidationException;
import it.cnr.jada.util.OrderedHashtable;

import java.math.BigDecimal;
import java.util.List;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_controlBase, Batch_proceduraBulk

public class Batch_controlBulk extends Batch_controlBase
{

    public Batch_controlBulk()
    {
    }

    public Batch_controlBulk(BigDecimal bigdecimal)
    {
        super(bigdecimal);
    }

    public void calcolaIntervalloReale()
    {
        if(intervallo_calcolato == null)
            setIntervallo(null);
        else
            switch(tipo_intervallo)
            {
            case 0: // '\0'
                setIntervallo(intervallo_calcolato);
                break;

            case 1: // '\001'
                setIntervallo(intervallo_calcolato * 60L);
                break;

            case 2: // '\002'
                setIntervallo(intervallo_calcolato * 60L * 60L);
                break;

            case 3: // '\003'
                setIntervallo(intervallo_calcolato * 60L * 60L * 24L);
                break;
            }
    }

    public Long getIntervallo_calcolato()
    {
        if(intervallo_calcolato == null)
        {
            if(getIntervallo() == null)
                return null;
            long l = getIntervallo();
            if(l % 0x15180L == 0L)
            {
                setIntervallo_calcolato(l / 0x15180L);
                setTipo_intervallo(3);
            } else
            if(l % 3600L == 0L)
            {
                setIntervallo_calcolato(l / 3600L);
                setTipo_intervallo(2);
            } else
            if(l % 60L == 0L)
            {
                setIntervallo_calcolato(l / 60L);
                setTipo_intervallo(1);
            } else
            {
                setIntervallo_calcolato(l);
                setTipo_intervallo(0);
            }
        }
        return intervallo_calcolato;
    }

    public List<Batch_procedura_parametroBulk> getParametri()
    {
        return parametri;
    }

    public Batch_proceduraBulk getProcedura()
    {
        return procedura;
    }

    public int getTipo_intervallo()
    {
        return tipo_intervallo;
    }

    public OrderedHashtable getTipo_intervalloKeys()
    {
        return tipo_intervalloKeys;
    }

    public void setIntervallo_calcolato(Long long1)
    {
        intervallo_calcolato = long1;
    }

    public void setParametri(List<Batch_procedura_parametroBulk> list)
    {
        parametri = list;
    }

    public void setProcedura(Batch_proceduraBulk batch_procedurabulk)
    {
        procedura = batch_procedurabulk;
    }

    public void setTipo_intervallo(int i)
    {
        tipo_intervallo = i;
    }

    public void validate()
        throws ValidationException
    {
        if(getProcedura() == null)
            throw new ValidationException("E' necessario specificare una procedura");
        calcolaIntervalloReale();
        if(getIntervallo() != null && getDt_partenza() == null)
            throw new ValidationException("Se si specifica l'intervallo \350 necessario specificare l'ora di partenza.");
        if(getIntervallo() != null && getIntervallo() == 0L)
            throw new ValidationException("Intervallo non valido");
    }

    public static final int TIPO_INTERVALLO_SEC = 0;
    public static final int TIPO_INTERVALLO_MIN = 1;
    public static final int TIPO_INTERVALLO_ORE = 2;
    public static final int TIPO_INTERVALLO_GIORNI = 3;
    public static final OrderedHashtable tipo_intervalloKeys;
    private List<Batch_procedura_parametroBulk> parametri;
    private Batch_proceduraBulk procedura;
    private int tipo_intervallo;
    private Long intervallo_calcolato;

    static 
    {
        tipo_intervalloKeys = new OrderedHashtable();
        tipo_intervalloKeys.put(0, "secondi");
        tipo_intervalloKeys.put(1, "minuti");
        tipo_intervalloKeys.put(2, "ore");
        tipo_intervalloKeys.put(3, "giorni");
    }
}