package it.cnr.contab.incarichi00.servlet;

import it.cnr.contab.config00.bulk.Configurazione_cnrBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_procedura_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_archivioBulk;
import it.cnr.contab.incarichi00.bulk.Incarichi_repertorio_varBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elencoBulk;
import it.cnr.contab.incarichi00.bulk.V_incarichi_elencoHome;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.blobs.bulk.Bframe_blobBulk;
import it.cnr.jada.blobs.bulk.Bframe_blobKey;
import it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk;
import it.cnr.jada.blobs.bulk.Bframe_blob_tipoKey;
import it.cnr.jada.bulk.BulkHome;
import it.cnr.jada.persistency.sql.FindClause;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.persistency.sql.SQLBuilder;
import it.cnr.jada.util.ejb.EJBCommonServices;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.sql.Connection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.sql.BLOB;
import oracle.sql.CLOB;

public class DownloadIncarichiServlet extends HttpServlet
{
	public DownloadIncarichiServlet()
	{
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException
	{
		HttpActionContext context = new HttpActionContext(this, request, response);
		try
		{
			Connection conn = EJBCommonServices.getConnection();
			conn.setAutoCommit(false);
			try
			{
				InputStream is;
				int bufferSize;				
				String fullpath = request.getPathInfo();
				HomeCache homeCache = new HomeCache(conn);
				
				String tipo = fullpath.substring(1, fullpath.indexOf('/', 1));				
				if(tipo.equals("fileIncaricoAllegato")){
				    int ind_init = fullpath.indexOf('/') + 1;
				    int ind_end  = fullpath.indexOf('/', ind_init);
				    ind_init = ind_end + 1;
				    ind_end  = fullpath.indexOf('/', ind_init);
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
				    String esercizio = fullpath.substring(ind_init,ind_end);	
				    ind_init = ind_end + 1;
				    ind_end  = fullpath.indexOf('/', ind_init);
				    String pg_repertorio = fullpath.substring(ind_init,ind_end);	
				    ind_init = ind_end + 1;
				    ind_end  = fullpath.indexOf('/', ind_init);
				    String progressivo_riga = fullpath.substring(ind_init,ind_end);	
					   
				    BulkHome home = (BulkHome)homeCache.getHome(Incarichi_repertorio_archivioBulk.class);
				    Incarichi_repertorio_archivioBulk allegatoBulk = (Incarichi_repertorio_archivioBulk)homeCache.getHome(Incarichi_repertorio_archivioBulk.class).findByPrimaryKey(new Incarichi_repertorio_archivioBulk(new Integer(esercizio),new Long(pg_repertorio),new Long(progressivo_riga)));
					if (allegatoBulk == null || 
						(context.getUserInfo()==null && !allegatoBulk.isContratto() && !allegatoBulk.isBando())) {
						response.sendError(404, request.getPathInfo());
						return;
					} else if (context.getUserInfo()==null && allegatoBulk.isContratto()) {
						V_incarichi_elencoHome homeElenco = (V_incarichi_elencoHome)homeCache.getHome(V_incarichi_elencoBulk.class);
						SQLBuilder sqlElenco = homeElenco.createSQLBuilder();
						sqlElenco.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,new Integer(esercizio));
						sqlElenco.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,new Long(pg_repertorio));
						List listaElenco = homeElenco.fetchAll(sqlElenco);
						if (listaElenco.isEmpty()) {
							response.sendError(404, request.getPathInfo());
							return;
						}
						String value = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(context.getUserContext(), new Integer(0), "*", Configurazione_cnrBulk.PK_INCARICHI_MODIFICA_ALLEGATI, Configurazione_cnrBulk.SK_INCARICHI_MOD_CONTRATTO);
					    if (value!=null && value.equals("Y")) {
							response.sendError(404, request.getPathInfo());
							return;
					    }
					}
					BLOB blob = (BLOB)home.getSQLBlob(allegatoBulk, "BDATA");
					bufferSize = blob.getBufferSize();
					response.setBufferSize(bufferSize);
					String contentType = getServletContext().getMimeType(filename);
					if(contentType != null)
					  response.setContentType(contentType);
					else
					  response.setContentType("application/pdf");  
					response.addHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
	 						
					long length = blob.length();
					if(length > 0L && length < 0x7fffffffL)
						response.setContentLength((int)length);
					is = blob.getBinaryStream();
					response.setDateHeader("Last-Modified", allegatoBulk.getDuva().getTime());
				} else if(tipo.equals("fileProceduraAllegato")){
					int ind_init = fullpath.indexOf('/') + 1;
					int ind_end  = fullpath.indexOf('/', ind_init);
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
					String esercizio = fullpath.substring(ind_init,ind_end);	
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String pg_repertorio = fullpath.substring(ind_init,ind_end);	
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String progressivo_riga = fullpath.substring(ind_init,ind_end);	
					   
					BulkHome home = (BulkHome)homeCache.getHome(Incarichi_procedura_archivioBulk.class);
					Incarichi_procedura_archivioBulk allegatoBulk = (Incarichi_procedura_archivioBulk)homeCache.getHome(Incarichi_procedura_archivioBulk.class).findByPrimaryKey(new Incarichi_procedura_archivioBulk(new Integer(esercizio),new Long(pg_repertorio),new Long(progressivo_riga)));
					if (allegatoBulk == null || 
						(context.getUserInfo()==null && !allegatoBulk.isBando() && !allegatoBulk.isContratto()))	{
						response.sendError(404, fullpath);
						return;
					} else if (context.getUserInfo()==null && allegatoBulk.isContratto()) {
						String value = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(context.getUserContext(), new Integer(0), "*", Configurazione_cnrBulk.PK_INCARICHI_MODIFICA_ALLEGATI, Configurazione_cnrBulk.SK_INCARICHI_MOD_CONTRATTO);
					    if (value!=null && value.equals("Y")) {
							response.sendError(404, request.getPathInfo());
							return;
					    }
					}
					BLOB blob = (BLOB)home.getSQLBlob(allegatoBulk, "BDATA");
					bufferSize = blob.getBufferSize();
					response.setBufferSize(bufferSize);
					String contentType = getServletContext().getMimeType(filename);
					if(contentType != null)
					  response.setContentType(contentType);
					else
					  response.setContentType("application/pdf");  
					response.addHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
		 						
					long length = blob.length();
					if(length > 0L && length < 0x7fffffffL)
						response.setContentLength((int)length);
					is = blob.getBinaryStream();
					response.setDateHeader("Last-Modified", allegatoBulk.getDuva().getTime());					
				} else if(tipo.equals("fileIncaricoVariazioneAllegato")){
					int ind_init = fullpath.indexOf('/') + 1;
					int ind_end  = fullpath.indexOf('/', ind_init);
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
					String esercizio = fullpath.substring(ind_init,ind_end);	
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String pg_repertorio = fullpath.substring(ind_init,ind_end);	
					ind_init = ind_end + 1;
					ind_end  = fullpath.indexOf('/', ind_init);
					String progressivo_riga = fullpath.substring(ind_init,ind_end);	
					   
					BulkHome home = (BulkHome)homeCache.getHome(Incarichi_repertorio_varBulk.class);
					Incarichi_repertorio_varBulk allegatoBulk = (Incarichi_repertorio_varBulk)homeCache.getHome(Incarichi_repertorio_varBulk.class).findByPrimaryKey(new Incarichi_repertorio_varBulk(new Integer(esercizio),new Long(pg_repertorio),new Long(progressivo_riga)));
					if (allegatoBulk == null || 
						(context.getUserInfo()==null && !allegatoBulk.isContratto() && !allegatoBulk.isBando())) {
						response.sendError(404, fullpath);
						return;
					} else if (context.getUserInfo()==null && allegatoBulk.isContratto()) {
						V_incarichi_elencoHome homeElenco = (V_incarichi_elencoHome)homeCache.getHome(V_incarichi_elencoBulk.class);
						SQLBuilder sqlElenco = homeElenco.createSQLBuilder();
						sqlElenco.addClause(FindClause.AND,"esercizio",SQLBuilder.EQUALS,new Integer(esercizio));
						sqlElenco.addClause(FindClause.AND,"pg_repertorio",SQLBuilder.EQUALS,new Long(pg_repertorio));
						List listaElenco = homeElenco.fetchAll(sqlElenco);
						if (listaElenco.isEmpty()) {
							response.sendError(404, request.getPathInfo());
							return;
						}
						String value = ((it.cnr.contab.config00.ejb.Configurazione_cnrComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRCONFIG00_EJB_Configurazione_cnrComponentSession")).getVal01(context.getUserContext(), new Integer(0), "*", Configurazione_cnrBulk.PK_INCARICHI_MODIFICA_ALLEGATI, Configurazione_cnrBulk.SK_INCARICHI_MOD_CONTRATTO);
					    if (value!=null && value.equals("Y")) {
							response.sendError(404, request.getPathInfo());
							return;
					    }
					}
					BLOB blob = (BLOB)home.getSQLBlob(allegatoBulk, "BDATA");
					bufferSize = blob.getBufferSize();
					response.setBufferSize(bufferSize);
					String contentType = getServletContext().getMimeType(filename);
					if(contentType != null)
					  response.setContentType(contentType);
					else
					  response.setContentType("application/pdf");  
					response.addHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
		 						
					long length = blob.length();
					if(length > 0L && length < 0x7fffffffL)
						response.setContentLength((int)length);
					is = blob.getBinaryStream();
					response.setDateHeader("Last-Modified", allegatoBulk.getDuva().getTime());					
				}else{
					String filename = fullpath.substring(fullpath.lastIndexOf('/') + 1);
					String path = fullpath.substring(fullpath.indexOf('/', 1) + 1, fullpath.lastIndexOf('/') + 1);
					BulkHome home = (BulkHome)homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blobBulk.class);
					Bframe_blob_tipoBulk blob_tipo = (Bframe_blob_tipoBulk)homeCache.getHome(it.cnr.jada.blobs.bulk.Bframe_blob_tipoBulk.class).findByPrimaryKey(new Bframe_blob_tipoKey(tipo));
					if(blob_tipo == null)
					{
						response.sendError(404, fullpath);
						return;
					}
					Bframe_blobBulk blob_bulk = (Bframe_blobBulk)home.findByPrimaryKey(new Bframe_blobKey(tipo, filename, path));
					if(blob_bulk == null)
					{
						response.sendError(404, fullpath);
						return;
					}
					if(blob_bulk.getStato() != null && !"S".equals(blob_bulk.getStato()))
					{
						response.sendError(404, fullpath);
						return;
					}
					if(blob_tipo.getFl_binario().booleanValue())
					{
						BLOB blob = (BLOB)home.getSQLBlob(blob_bulk, "BDATA");
						bufferSize = blob.getBufferSize();
						response.setBufferSize(bufferSize);
						String contentType = getServletContext().getMimeType(filename);
						if(contentType != null)
							response.setContentType(contentType);
						else
							response.setContentType("www/unknown");													
						long length = blob.length();
						if(length > 0L && length < 0x7fffffffL)
							response.setContentLength((int)length);
						is = blob.getBinaryStream();
					} else
					{
						CLOB clob = (CLOB)home.getSQLClob(blob_bulk, "CDATA");
						bufferSize = clob.getBufferSize();
						response.setBufferSize(bufferSize);
						String contentType = getServletContext().getMimeType(filename);
						if(contentType != null)
							response.setContentType(contentType);
						else
						    response.setContentType("www/unknown");	
						long length = clob.length();
						//if(length > 0L && length < 0x7fffffffL)
							response.setContentLength((int)length);
						is = clob.getAsciiStream();
					}
				    response.setDateHeader("Last-Modified", blob_bulk.getDuva().getTime());
				}									    
				OutputStream os = response.getOutputStream();
				try
				{
					byte buffer[] = new byte[bufferSize];
					int size;
					while((size = is.read(buffer)) > 0) 
						os.write(buffer, 0, size);
				}
				catch(SocketException ex){}				
				finally
				{
					is.close();
				}
			}
			finally
			{
				//conn.commit();
				conn.close();
				conn = null;
			}
		}
		catch(Throwable _ex)
		{
			response.sendError(500);
		}
	}
}
