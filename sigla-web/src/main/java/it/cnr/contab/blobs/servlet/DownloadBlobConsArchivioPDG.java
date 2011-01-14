package it.cnr.contab.blobs.servlet;

import java.awt.geom.Arc2D;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.sql.Connection;
import java.util.Date;
import javax.servlet.*;
import javax.servlet.http.*;
import oracle.sql.BLOB;
import it.cnr.contab.config00.blob.bulk.*;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioBulk;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioHome;
import it.cnr.contab.pdg00.bulk.Pdg_variazione_archivioKey;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @version 	1.0
 * @author      RPagano
 */
public class DownloadBlobConsArchivioPDG extends HttpServlet {

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException 
		{
			HttpActionContext context = new HttpActionContext(this, req, resp);
			try
			{	
			  Connection conn = EJBCommonServices.getConnection(context);
			  try
			  {
			   String full = req.getPathInfo();
			   int ind_init = full.indexOf('/') + 1;
			   int ind_end  = full.indexOf('/', ind_init);
			   String  esercizioPDG = full.substring(ind_init,ind_end);	
			   ind_init = ind_end + 1;
			   ind_end  = full.indexOf('/', ind_init);
			   String  nrVariazionePDG = full.substring(ind_init,ind_end);	
			   ind_init = ind_end + 1;
			   ind_end  = full.indexOf('/', ind_init);
			   String  progPDG = full.substring(ind_init,ind_end);	
			   
			   HomeCache homeCache = new HomeCache(conn);			   
			   Pdg_variazione_archivioHome home=(Pdg_variazione_archivioHome)homeCache.getHome(Pdg_variazione_archivioBulk.class);
			   try
			   {
				   Pdg_variazione_archivioBulk pdgVar_bulk = (Pdg_variazione_archivioBulk)home.findByPrimaryKey(new Pdg_variazione_archivioBulk(new Integer(esercizioPDG), new Long(nrVariazionePDG), new Long(progPDG))); /*??*/
				   if(pdgVar_bulk == null)
				   {
					   resp.sendError(404, "");
					   return;
				   }			   
				   InputStream is;
				   int bufferSize;
				   BLOB blob = (BLOB)home.getSQLBlob(pdgVar_bulk, "BDATA");
				   bufferSize = blob.getBufferSize();
				   resp.setBufferSize(bufferSize);
				   String contentType = getServletContext().getMimeType(pdgVar_bulk.getNomeFile());
				   if(contentType != null)
					   resp.setContentType(contentType);
				   else
 				       resp.setContentType("application/vnd.ms-excel");  
				   resp.addHeader("Content-Disposition", "attachment; filename=\""+pdgVar_bulk.getNomeFile()+"\"");

 				   long length = blob.length();
				   if(length > 0L && length < 0x7fffffffL)
					   resp.setContentLength((int)length);
				   is = blob.getBinaryStream();
			 	   resp.setDateHeader("Last-Modified", pdgVar_bulk.getDuva().getTime());

				   OutputStream os = resp.getOutputStream();
				   
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
			   catch(Throwable tw){
			   	System.out.println(tw);
			   }
			  }
			  finally
			  {
				conn.commit();
				conn.close();
				conn = null;
			  }
			} catch(Throwable _ex)
			{
				resp.sendError(500);
			}

	}

}
