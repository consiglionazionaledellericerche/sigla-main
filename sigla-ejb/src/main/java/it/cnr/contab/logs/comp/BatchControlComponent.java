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

package it.cnr.contab.logs.comp;

import it.cnr.contab.logs.bulk.*;
import it.cnr.jada.UserContext;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.bulk.BulkList;
import it.cnr.jada.bulk.OggettoBulk;
import it.cnr.jada.comp.*;
import it.cnr.jada.persistency.Broker;
import it.cnr.jada.persistency.PersistencyException;
import it.cnr.jada.persistency.sql.*;
import it.cnr.jada.util.RemoteIterator;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.ejb.EJBException;
import javax.sql.DataSource;

// Referenced classes of package it.cnr.contab.logs.comp:
//            IBatchControlMgr

public class BatchControlComponent extends CRUDComponent
    implements IBatchControlMgr
{

    public BatchControlComponent()
    {
    }

    public Batch_controlBulk attivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
        	LoggableStatement callablestatement =new LoggableStatement( getConnection(usercontext),
        			"{  call " + EJBCommonServices.getDefaultSchema() 
        			+ "IBMUTL210.attivaBatch( ?, ?, ?, ? ) }",false,this.getClass());
            try
            {
                callablestatement.setBigDecimal(1, batch_controlbulk.getPg_batch());
                callablestatement.setString(2, usercontext.getUser());
                Timestamp timestamp = getHome(usercontext, batch_controlbulk).getServerTimestamp();
                long l = timestamp.getTime() - batch_controlbulk.getDt_partenza().getTime();
                long l1 = batch_controlbulk.getIntervallo().longValue() * 1000L;
                callablestatement.setTimestamp(3, new Timestamp(batch_controlbulk.getDt_partenza().getTime() + ((l - 1L) / l1 + 1L) * l1));
                callablestatement.setLong(4, batch_controlbulk.getIntervallo().longValue());
                callablestatement.execute();
            }
            finally
            {
                callablestatement.close();
            }
            return batch_controlbulk;
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public OggettoBulk creaBatchDinamico(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
        	LoggableStatement callablestatement = new LoggableStatement(getConnection(usercontext),
        			"{  call " + EJBCommonServices.getDefaultSchema() 
        			+ "IBMUTL210.CREABATCHDINAMICO(?, ?, ?, ?)}",false,this.getClass());
            try
            {
                callablestatement.setString(1, batch_controlbulk.getDs_batch());
                callablestatement.setString(2, creaChiamataProcedura(usercontext, batch_controlbulk));
                callablestatement.setString(3, usercontext.getUser());
                callablestatement.setTimestamp(4, null);
                callablestatement.execute();
                salvaValoriParametri(usercontext, batch_controlbulk);
                Batch_controlBulk batch_controlbulk1 = batch_controlbulk;
                return batch_controlbulk1;
            }
            finally
            {
                callablestatement.close();
            }
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public OggettoBulk creaBatchStatico(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
        	LoggableStatement callablestatement = new LoggableStatement(getConnection(usercontext),
        			"{  call " + EJBCommonServices.getDefaultSchema() 
        			+ "IBMUTL210.CREABATCH(?, ?, ?, 'N', ? , ?)}",false,this.getClass());
            try
            {
                callablestatement.setString(1, batch_controlbulk.getDs_batch());
                callablestatement.setString(2, creaChiamataProcedura(usercontext, batch_controlbulk));
                callablestatement.setString(3, usercontext.getUser());
                callablestatement.setTimestamp(4, batch_controlbulk.getDt_partenza());
                callablestatement.setLong(5, batch_controlbulk.getIntervallo().longValue());
                callablestatement.execute();
                salvaValoriParametri(usercontext, batch_controlbulk);
                Batch_controlBulk batch_controlbulk1 = batch_controlbulk;
                return batch_controlbulk1;
            }
            finally
            {
                callablestatement.close();
            }
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public String creaChiamataProcedura(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        StringBuffer stringbuffer = new StringBuffer();
        stringbuffer.append(batch_controlbulk.getProcedura().getPackage_name());
        stringbuffer.append('.');
        stringbuffer.append(batch_controlbulk.getProcedura().getObject_name());
        stringbuffer.append("(job,pg_exec,next_date");
        for(Iterator iterator = batch_controlbulk.getParametri().iterator(); iterator.hasNext();)
        {
            Batch_procedura_parametroBulk batch_procedura_parametrobulk = (Batch_procedura_parametroBulk)iterator.next();
            if(batch_procedura_parametrobulk.getValoreRiutilizzato() != null)
                batch_procedura_parametrobulk = batch_procedura_parametrobulk.getValoreRiutilizzato();
            stringbuffer.append(',');
            switch(batch_procedura_parametrobulk.getTipoParametro())
            {
            default:
                break;

            case 0: // '\0'
                if(batch_procedura_parametrobulk.getValore_varchar() != null)
                {
                    stringbuffer.append('\'');
                    stringbuffer.append(batch_procedura_parametrobulk.getValore_varchar());
                    stringbuffer.append('\'');
                }
                break;

            case 1: // '\001'
                if(batch_procedura_parametrobulk.getValore_number() != null)
                    stringbuffer.append(batch_procedura_parametrobulk.getValore_number());
                break;

            case 2: // '\002'
                if(batch_procedura_parametrobulk.getValore_date() != null)
                {
                    stringbuffer.append("TODATE('");
                    stringbuffer.append(oracleDateFormat.format(batch_procedura_parametrobulk.getValore_date()));
                    stringbuffer.append("','YYYYMMDDHH24MISS')");
                }
                break;
            }
        }

        stringbuffer.append(");");
        return stringbuffer.toString();
    }
	public void aggiornaViewSnapshot(UserContext usercontext) throws ComponentException{
		try {
			Connection connInformix = EJBCommonServices.getDatasource("java:/jdbc/GECO").getConnection();
			LoggableStatement aPS=null;
		    try {
			 aPS=new LoggableStatement(connInformix,
			  "select * from progetto",true,this.getClass());
		     ResultSet aRS= aPS.executeQuery();
		     while(aRS.next()) {
			  System.out.println(aRS.getString("descr_prog"));    
			 }
		     try{aRS.close();}catch( SQLException e ){};
			} catch (SQLException e) {
				throw handleException(e);
			} finally {
			 if(aPS != null) 
		         try{aPS.close();}catch( SQLException e ){};
		    }	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try
		{
			LoggableStatement callablestatement = new LoggableStatement(getConnection(usercontext),
					"{  call " + EJBCommonServices.getDefaultSchema() 
					+ "CNRMAT001.aggiornaMatView_RealTime }",false,this.getClass());
			try
			{
				callablestatement.execute();
			}
			finally
			{
				callablestatement.close();
			}
		}
		catch(Throwable throwable)
		{
			//throw handleException(throwable);
		}
		
	}
    public OggettoBulk creaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException
    {
        Batch_controlBulk batch_controlbulk = (Batch_controlBulk)oggettobulk;
        if(batch_controlbulk.getIntervallo() == null)
            return creaBatchDinamico(usercontext, batch_controlbulk);
        else
            return creaBatchStatico(usercontext, batch_controlbulk);
    }

    public Batch_controlBulk disattivaBatch(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
        	LoggableStatement callablestatement = new LoggableStatement(getConnection(usercontext),
        			"{  call " + EJBCommonServices.getDefaultSchema()
        			+ "IBMUTL210.disattivaBatch( ?, ?) }",false,this.getClass());
            try
            {
                callablestatement.setBigDecimal(1, batch_controlbulk.getPg_batch());
                callablestatement.setString(2, usercontext.getUser());
                callablestatement.execute();
            }
            finally
            {
                callablestatement.close();
            }
            return batch_controlbulk;
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public void eliminaConBulk(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException
    {
        try
        {
            Batch_controlBulk batch_controlbulk = (Batch_controlBulk)oggettobulk;
            LoggableStatement callablestatement = new LoggableStatement(getConnection(usercontext),
            		"{  call " + EJBCommonServices.getDefaultSchema() 
            		+ "IBMUTL210.RIMUOVIBATCH( ?, ?) }",false,this.getClass());
            try
            {
                callablestatement.setBigDecimal(1, batch_controlbulk.getPg_batch());
                callablestatement.setString(2, usercontext.getUser());
                callablestatement.execute();
            }
            finally
            {
                callablestatement.close();
            }
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public OggettoBulk inizializzaBulkPerInserimento(UserContext usercontext, OggettoBulk oggettobulk)
        throws ComponentException
    {
        try
        {
            Batch_controlBulk batch_controlbulk = (Batch_controlBulk)super.inizializzaBulkPerInserimento(usercontext, oggettobulk);
            batch_controlbulk.setTipo_intervallo(3);
            return batch_controlbulk;
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public Batch_controlBulk inizializzaParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
            ArrayList arraylist = new ArrayList();
            batch_controlbulk.setParametri(arraylist);
            if(batch_controlbulk.getProcedura() == null || batch_controlbulk.getProcedura().getObject_name() == null || batch_controlbulk.getProcedura().getPackage_name() == null)
                return batch_controlbulk;
            LoggableStatement statement = new LoggableStatement(getConnection(usercontext),
            		"SELECT ARGUMENT_NAME,DATA_TYPE FROM USER_ARGUMENTS WHERE OBJECT_NAME = ? AND PACKAGE_NAME = ? ORDER BY POSITION",true,this.getClass());
            try
            {
                statement.setString(1, batch_controlbulk.getProcedura().getObject_name());
                statement.setString(2, batch_controlbulk.getProcedura().getPackage_name());
                ResultSet resultset = statement.executeQuery();
                try
                {
                    for(int i = 0; i < 3; i++)
                        if(!resultset.next())
                            break;

                    while(resultset.next()) 
                    {
                        Batch_procedura_parametroBulk batch_procedura_parametrobulk = new Batch_procedura_parametroBulk(batch_controlbulk.getProcedura().getCd_procedura(), resultset.getString(1), null);
                        String s = resultset.getString(2);
                        if("VARCHAR".equalsIgnoreCase(s) || "VARCHAR2".equalsIgnoreCase(s) || "CHAR".equalsIgnoreCase(s))
                            batch_procedura_parametrobulk.setTipoParametro(0);
                        else
                        if("NUMBER".equalsIgnoreCase(s))
                            batch_procedura_parametrobulk.setTipoParametro(1);
                        else
                        if("DATE".equalsIgnoreCase(s))
                            batch_procedura_parametrobulk.setTipoParametro(2);
                        if(batch_procedura_parametrobulk.getTipoParametro() != -1)
                        {
                            arraylist.add(batch_procedura_parametrobulk);
                            SQLBuilder sqlbuilder = getHome(usercontext, batch_procedura_parametrobulk).createSQLBuilder();
                            sqlbuilder.addClause("AND", "cd_procedura", 8192, batch_procedura_parametrobulk.getCd_procedura());
                            sqlbuilder.addClause("AND", "nome_parametro", 8192, batch_procedura_parametrobulk.getNome_parametro());
                            sqlbuilder.addOrderBy("PG_VALORE_PARAMETRO DESC");
                            batch_procedura_parametrobulk.setValoriRiutilizzabili(new ArrayList());
                            Batch_procedura_parametroBulk batch_procedura_parametrobulk1;
                            for(SQLBroker sqlbroker = getHome(usercontext, batch_procedura_parametrobulk).createBroker(sqlbuilder); sqlbroker.next(); batch_procedura_parametrobulk.getValoriRiutilizzabili().add(batch_procedura_parametrobulk1))
                            {
                                batch_procedura_parametrobulk1 = (Batch_procedura_parametroBulk)sqlbroker.fetch(Batch_procedura_parametroBulk.class);
                                batch_procedura_parametrobulk1.setTipoParametro(batch_procedura_parametrobulk.getTipoParametro());
                            }

                        }
                    }
                }
                finally
                {
					try{resultset.close();}catch( SQLException e ){};
                }
            }
            finally
            {
				try{statement.close();}catch( SQLException e ){};
            }
            return batch_controlbulk;
        }
        catch(Throwable throwable)
        {
            throw handleException(throwable);
        }
    }

    public RemoteIterator listaBatch_control_jobs(UserContext usercontext)
        throws ComponentException
    {
        return iterator(usercontext, getHome(usercontext, V_batch_control_jobsBulk.class).createSQLBuilder(), V_batch_control_jobsBulk.class, null);
    }

    public void salvaValoriParametri(UserContext usercontext, Batch_controlBulk batch_controlbulk)
        throws ComponentException
    {
        try
        {
            for(Iterator iterator = batch_controlbulk.getParametri().iterator(); iterator.hasNext();)
            {
                Batch_procedura_parametroBulk batch_procedura_parametrobulk = (Batch_procedura_parametroBulk)iterator.next();
                for(int i = batch_procedura_parametrobulk.getValoriRiutilizzabili().size() - 1; i > 5; i--)
                    deleteBulk(usercontext, (Batch_procedura_parametroBulk)batch_procedura_parametrobulk.getValoriRiutilizzabili().get(i));

                if(batch_procedura_parametrobulk.getValoreRiutilizzato() != null)
                {
                    batch_procedura_parametrobulk = batch_procedura_parametrobulk.getValoreRiutilizzato();
                    if(batch_procedura_parametrobulk.getCrudStatus() == 5)
                        deleteBulk(usercontext, batch_procedura_parametrobulk);
                    batch_procedura_parametrobulk.setPg_valore_parametro(null);
                } else
                {
                    for(Iterator iterator1 = batch_procedura_parametrobulk.getValoriRiutilizzabili().iterator(); iterator1.hasNext();)
                    {
                        Batch_procedura_parametroBulk batch_procedura_parametrobulk1 = (Batch_procedura_parametroBulk)iterator1.next();
                        if(batch_procedura_parametrobulk1.getValore().equals(batch_procedura_parametrobulk.getValore()))
                        {
                            deleteBulk(usercontext, batch_procedura_parametrobulk1);
                            break;
                        }
                    }

                }
                batch_procedura_parametrobulk.setUser(usercontext.getUser());
                if(batch_procedura_parametrobulk.getValore_date() != null || batch_procedura_parametrobulk.getValore_varchar() != null || batch_procedura_parametrobulk.getValore_number() != null)
                    insertBulk(usercontext, batch_procedura_parametrobulk);
            }

        }
        catch(PersistencyException persistencyexception)
        {
            throw handleException(persistencyexception);
        }
    }

    public SQLBuilder selectBatch_log_tstaByClause(UserContext usercontext, Batch_controlBulk batch_controlbulk, Batch_log_tstaBulk batch_log_tstabulk, CompoundFindClause compoundfindclause)
        throws ComponentException
    {
        SQLBuilder sqlbuilder = getHome(usercontext, batch_log_tstabulk).createSQLBuilder();
        sqlbuilder.addClause(compoundfindclause);
        sqlbuilder.addSQLClause("AND", "PG_BATCH", 8192, batch_controlbulk.getPg_batch());
        return sqlbuilder;
    }

    public SQLBuilder selectProceduraByClause(UserContext usercontext, Batch_controlBulk batch_controlbulk, Batch_proceduraBulk batch_procedurabulk, CompoundFindClause compoundfindclause)
        throws ComponentException
    {
        SQLBuilder sqlbuilder = getHome(usercontext, batch_procedurabulk).createSQLBuilder();
        sqlbuilder.addClause(compoundfindclause);
        sqlbuilder.openParenthesis("AND");
        sqlbuilder.addClause("OR", "cd_utente", 8192, usercontext.getUser());
        sqlbuilder.addClause("OR", "cd_utente", 8201, null);
        sqlbuilder.closeParenthesis();
        sqlbuilder.addSQLClause("AND", "NOT EXISTS ( SELECT 1 FROM USER_ARGUMENTS WHERE BATCH_PROCEDURA.PACKAGE_NAME = USER_ARGUMENTS.PACKAGE_NAME AND BATCH_PROCEDURA.OBJECT_NAME = USER_ARGUMENTS.OBJECT_NAME AND USER_ARGUMENTS.OVERLOAD IS NOT NULL )");
        return sqlbuilder;
    }

    private static final DateFormat oracleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public OggettoBulk creaConBulkRequiresNew(UserContext userContext, OggettoBulk  oggettoBulk)
            throws ComponentException {
        return super.creaConBulk(userContext,oggettoBulk);
    }
}