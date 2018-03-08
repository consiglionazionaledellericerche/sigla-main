<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDDocumentoGenericoAttivoBP bp = (CRUDDocumentoGenericoAttivoBP)BusinessProcess.getBusinessProcess(request);
	Documento_genericoBulk doc = (Documento_genericoBulk)bp.getModel(); %>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Accertamenti</span>
			  	</td>
			</tr>
			<tr>
				<td>
					<%bp.getAccertamentiController().writeHTMLTable(pageContext,"default",true,false,true,"100%","100px"); %>
				</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerAccertamento"); %>			  	
				  	<% bp.writeFormInput(out,null,"importoTotalePerAccertamento",false,null,"style=\"color:black\"");%>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Accertamento_scadenzarioBulk accertamento = (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel();
		   				if (accertamento == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli documento generico contabilizzati</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli documento generico contabilizzati su <%=accertamento.getDs_scadenza()%></span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioAccertamentoController().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>