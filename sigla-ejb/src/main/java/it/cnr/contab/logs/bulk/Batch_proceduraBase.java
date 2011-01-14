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