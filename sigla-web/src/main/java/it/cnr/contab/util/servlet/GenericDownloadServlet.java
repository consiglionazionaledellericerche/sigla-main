package it.cnr.contab.util.servlet;

import it.cnr.cmisdl.model.Node;
import it.cnr.contab.cmis.service.CMISService;
import it.cnr.contab.service.SpringUtil;
import it.cnr.jada.action.BusinessProcess;
import it.cnr.jada.action.HttpActionContext;
import it.cnr.jada.util.Introspector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class GenericDownload
 */
public class GenericDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenericDownloadServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		CMISService cmisService = SpringUtil.getBean("cmisService", CMISService.class);
		if (request.getParameter("nodeRef")!= null){
			Node node = cmisService.getNodeByNodeRef(request.getParameter("nodeRef"));
			InputStream is = cmisService.getResource(node);
			response.setContentLength(node.getContentLength().intValue());
			response.setContentType(node.getContentType());
			response.setContentLength(node.getContentLength().intValue());
			OutputStream os = response.getOutputStream();
			response.setDateHeader("Expires", 0);
			byte[] buffer = new byte[response.getBufferSize()];
			int buflength;
			while ((buflength = is.read(buffer)) > 0) {
				os.write(buffer,0,buflength);
			}
			is.close();
			os.flush();
		}else{
			HttpActionContext actionContext = new HttpActionContext(this, request, response);
			try {
				Introspector.invoke(BusinessProcess.getBusinessProcess(request), request.getParameter("methodName"), actionContext);
			} catch (NoSuchMethodException e) {
				throw new ServletException(e);
			} catch (IllegalAccessException e) {
				throw new ServletException(e);
			} catch (InvocationTargetException e) {
				throw new ServletException(e);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
