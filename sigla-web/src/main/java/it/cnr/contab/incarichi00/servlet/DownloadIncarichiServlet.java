package it.cnr.contab.incarichi00.servlet;

import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.comp.ApplicationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.chemistry.opencmis.client.api.Document;

public class DownloadIncarichiServlet extends HttpServlet {
	public DownloadIncarichiServlet() {
        super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpActionContext actionContext = new HttpActionContext(this, request, response);
		ContrattiService contrattiService = SpringUtil.getBean("contrattiService", ContrattiService.class);
		Document node;
		try {
			node = (Document) contrattiService.getNodeByNodeRef(request.getParameter("cmisNodeRef"));
		} catch (ApplicationException e) {
			throw new ServletException(e);
		}
		InputStream is = contrattiService.getResource(node);
		((HttpActionContext)actionContext).getResponse().setContentLength(Long.valueOf(node.getContentStreamLength()).intValue());		
		((HttpActionContext)actionContext).getResponse().setContentType(node.getContentStreamMimeType());
		OutputStream os = ((HttpActionContext)actionContext).getResponse().getOutputStream();
		((HttpActionContext)actionContext).getResponse().setDateHeader("Expires", 0);
		byte[] buffer = new byte[((HttpActionContext)actionContext).getResponse().getBufferSize()];
		int buflength;
		while ((buflength = is.read(buffer)) > 0) {
			os.write(buffer,0,buflength);
		}
		is.close();
		os.flush();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
