package it.cnr.contab.blobs.servlet;

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
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.persistency.sql.HomeCache;
import it.cnr.jada.util.ejb.EJBCommonServices;

/**
 * @version 	1.0
 * @author      Angelo
 */
public class DownloadBlobPostIt extends HttpServlet {

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
			   String idPost = full.substring(full.indexOf('/') + 1,full.lastIndexOf('/'));
			   
			   HomeCache homeCache = new HomeCache(conn);			   
			   PostItHome home=(PostItHome)homeCache.getHome(PostItBulk.class);
			   try
			   {
			   	   PostItBulk postIt_bulk = (PostItBulk)home.findByPrimaryKey(new PostItKey(new Integer(idPost))); /*??*/
				   if(postIt_bulk == null)
				   {
					   resp.sendError(404, "");
					   return;
				   }			   
				   InputStream is;
				   int bufferSize;
				   BLOB blob = (BLOB)home.getSQLBlob(postIt_bulk, "BDATA");
				   bufferSize = blob.getBufferSize();
				   resp.setBufferSize(bufferSize);
				   String contentType = getServletContext().getMimeType(postIt_bulk.getNome_file());
				   if(contentType != null)
				   	   resp.setContentType(contentType);
				   else
				   	   resp.setContentType("www/unknown");				   
				   long length = blob.length();
				   resp.setContentLength((int)length);
				   is = blob.getBinaryStream();
				   resp.setDateHeader("Last-Modified", postIt_bulk.getDuva().getTime());
	
				   
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
			   catch(Throwable tw){}
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
