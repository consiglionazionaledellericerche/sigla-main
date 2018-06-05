package it.cnr.contab.incarichi00.servlet;

import it.cnr.contab.incarichi00.service.ContrattiService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.si.spring.storage.StorageObject;
import it.cnr.si.spring.storage.config.StoragePropertyNames;
import it.cnr.jada.action.HttpActionContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Optional;

public class DownloadIncarichiServlet extends HttpServlet {
	public DownloadIncarichiServlet() {
        super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpActionContext actionContext = new HttpActionContext(this, request, response);
		ContrattiService contrattiService = SpringUtil.getBean("contrattiService", ContrattiService.class);
		StorageObject storageObject = Optional.ofNullable(contrattiService.getStorageObjectBykey(request.getParameter("cmisNodeRef")))
				.orElseThrow(() -> new ServletException("Document not found"));
		InputStream is = contrattiService.getResource(storageObject);
		((HttpActionContext)actionContext).getResponse().setContentLength(storageObject.<BigInteger>getPropertyValue(StoragePropertyNames.CONTENT_STREAM_LENGTH.value()).intValue());
		((HttpActionContext)actionContext).getResponse().setContentType(storageObject.getPropertyValue(StoragePropertyNames.CONTENT_STREAM_MIME_TYPE.value()));
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
