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

<%	CRUDDocumentoGenericoPassivoBP bp = (CRUDDocumentoGenericoPassivoBP)BusinessProcess.getBusinessProcess(request);
	Documento_genericoBulk doc = (Documento_genericoBulk)bp.getModel(); %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Impegni</span>
			  	</td>
			</tr>
			<tr>
				<td>
					<%bp.getObbligazioniController().writeHTMLTable(pageContext,"default",true,false,true,"100%","100px"); %>
				</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerObbligazione"); %>			  	
				  	<% bp.writeFormInput(out,null,"importoTotalePerObbligazione",false,null,"style=\"color:black\"");%>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Obbligazione_scadenzarioBulk obbl = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		   				if (obbl == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli documento generico contabilizzati</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli documento generico contabilizzati su <%=obbl.getDs_scadenza()%></span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioObbligazioneController().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>