<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.bulk.*"
%>
<%String onselect = (String)request.getAttribute("onselect");
	Dictionary columns = (Dictionary)request.getAttribute("columns");
	Enumeration rows = (Enumeration)request.getAttribute("rows");
	int numeroRiga = 0;%>
	<tr>
<%for (Enumeration f = columns.elements();f.hasMoreElements();) {
		ColumnFieldProperty column = (ColumnFieldProperty)f.nextElement();
%><td<% column.writeHeaderStyle(out,null,"TableHeader");%>><%
		column.writeLabel(out,null, HttpActionContext.isFromBootstrap(pageContext));
%></td><%
	}%>
	</tr>
<%for(Enumeration e = rows;e.hasMoreElements();numeroRiga++) {
		Object riga = (Object)e.nextElement();
		boolean operabile = riga instanceof OggettoBulk && ((OggettoBulk)riga).isOperabile();
 %>
 	 	<tr>
<%		for (Enumeration f = columns.elements();f.hasMoreElements();) {
				ColumnFieldProperty column = (ColumnFieldProperty)f.nextElement();
%><td<% column.writeColumnStyle(out,null,"TableColumn");%>><%
 				if (operabile) { 
%><a border=0 href="javascript:<%= onselect%>('<%= numeroRiga%>')" class="ListItem"><%
	 			} 
				column.writeReadonlyText(out,riga,"ListItem",null);
 				if (operabile) { 
%></a><%
	 			} 
%></td><%
			}%>
		</tr>
<%}%>