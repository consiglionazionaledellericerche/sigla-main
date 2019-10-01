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

import it.cnr.jada.bulk.FieldProperty;
import it.cnr.jada.bulk.ValidationException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.Format;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

// Referenced classes of package it.cnr.contab.logs.bulk:
//            Batch_procedura_parametroBase

public class Batch_procedura_parametroBulk extends Batch_procedura_parametroBase
{

    public Batch_procedura_parametroBulk()
    {
        tipoParametro = -1;
    }

    public Batch_procedura_parametroBulk(String s, String s1, Integer integer)
    {
        super(s, s1, integer);
        tipoParametro = -1;
    }

    public int getTipoParametro()
    {
        return tipoParametro;
    }

    public String getValore()
    {
        switch(tipoParametro)
        {
        case 0: // '\0'
            return getValore_varchar();

        case 1: // '\001'
            if(getValore_number() == null)
                return null;
            else
                return number_format.format(getValore_number());

        case 2: // '\002'
            if(getValore_date() == null)
                return null;
            else
                return date_format.format(getValore_date());
        }
        return null;
    }

    public Object getValoreObject()
    {
        switch(tipoParametro)
        {
        case 0: // '\0'
            return getValore_varchar();

        case 1: // '\001'
            return getValore_number();

        case 2: // '\002'
            return getValore_date();
        }
        return null;
    }

    public Batch_procedura_parametroBulk getValoreRiutilizzato()
    {
        return valoreRiutilizzato;
    }

    public List getValoriRiutilizzabili()
    {
        return valoriRiutilizzabili;
    }

    public void setTipoParametro(int i)
    {
        tipoParametro = i;
    }

    public void setValore(String s)
        throws ValidationException
    {
        try
        {
            switch(tipoParametro)
            {
            default:
                break;

            case 0: // '\0'
                setValore_varchar(s);
                break;

            case 1: // '\001'
                if(s == null)
                    setValore_number(null);
                else
                    setValore_number((BigDecimal)number_format.parseObject(s));
                break;

            case 2: // '\002'
                if(s == null)
                    setValore_date(null);
                else
                    setValore_date(new Timestamp(((Date)date_format.parseObject(s)).getTime()));
                break;
            }
            valoreRiutilizzato = null;
        }
        catch(ParseException _ex)
        {
            throw new ValidationException("Errore di formattazione. Valore non valido per il tipo di parametro");
        }
    }

    public Object setValoreObject(Object obj)
    {
        switch(tipoParametro)
        {
        case 0: // '\0'
            setValore_varchar((String)obj);
            break;

        case 1: // '\001'
            setValore_number((BigDecimal)obj);
            break;

        case 2: // '\002'
            setValore_date((Timestamp)obj);
            break;
        }
        return null;
    }

    public void setValoreRiutilizzato(Batch_procedura_parametroBulk batch_procedura_parametrobulk)
    {
        valoreRiutilizzato = batch_procedura_parametrobulk;
        if(valoreRiutilizzato != null)
            setValoreObject(valoreRiutilizzato.getValoreObject());
    }

    public void setValoriRiutilizzabili(List list)
    {
        valoriRiutilizzabili = list;
    }

    private List valoriRiutilizzabili;
    private Batch_procedura_parametroBulk valoreRiutilizzato;
    private int tipoParametro;
    public static final int TIPO_PARAMETRO_VARCHAR = 0;
    public static final int TIPO_PARAMETRO_NUMBER = 1;
    public static final int TIPO_PARAMETRO_DATE = 2;
    public static final int TIPO_PARAMETRO_SCONOSCIUTO = -1;
    public static Format number_format;
    public static Format date_format = FieldProperty.getFormat("timestamp");

    static 
    {
        number_format = FieldProperty.getFormat(java.math.BigDecimal.class);
    }
}