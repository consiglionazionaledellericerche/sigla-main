 <%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>
<% CRUDAggiornamentoInventarioBP bp = (CRUDAggiornamentoInventarioBP)BusinessProcess.getBusinessProcess(request); 
   Aggiornamento_inventarioBulk aggiornamenti = (Aggiornamento_inventarioBulk)bp.getModel(); 
   Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)bp.getVutilizzatori().getModel(); 
    %>
    <table>	
	<div class="Group">	
			
		
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"find_assegnatario"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"find_assegnatario"); %>
			</td>
		</tr>
	
	
	  <div class="Group">
	
		  <tr>
			<td><% bp.getController().writeFormLabel(out,"findUbicazioneDestinazione"); %></td>
			<td>
				<% bp.getController().writeFormInput(out,"findUbicazioneDestinazione"); %>
			</td>
		  </tr>
	

	  </table> 
	<div class="Group">
	  <table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">CdR:</span>
		</td>
       </tr>
        <td>
   		<% bp.getVutilizzatori().writeHTMLTable(pageContext,"utilizzatoriSet",true,false,true,"100%","150px");%>
   		</td>
    </table> 	

	  <table>			
		<tr>
			<td>
				<% bp.getVutilizzatori().writeFormLabel(out,"find_cdr"); %>
			</td>
				<%
			if ( utilizzatore != null && utilizzatore.getCdr()!=null && utilizzatore.getCdCdr()!=null){
			%>
			<td>
				<% bp.getVutilizzatori().writeFormInput(out,null,"find_cdr",true,null,null); %>
			</td>
			<%
			}else{
			%>
			<td>
				<% bp.getVutilizzatori().writeFormInput(out,null,"find_cdr",false,null,null); %>
			</td>
			<%
			}
			%>
		</tr>	
		<tr>
			<td>
				<% bp.getVutilizzatori().writeFormLabel(out,"percentuale_utilizzo_cdr"); %>
			</td>
			<td colspan="3">
				<% bp.getVutilizzatori().writeFormInput(out,"percentuale_utilizzo_cdr"); %>
			</td>
		</tr>
	</table> 
	</div>
	<%
	if (aggiornamenti!=null && aggiornamenti.hasUtilizzatori() && utilizzatore != null && utilizzatore.getCdr()!=null && utilizzatore.getCdCdr()!=null){
	%>
	
	<div class="Group">
	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">GAE:</span>
		</td>
       </tr>
       <td>
   		<% bp.getUtilizzatori().writeHTMLTable(pageContext,"utilizzatoriSet",true,false,true,"100%","100px");%>
   		</td>
    </table> 	
	<table>	
	<tr>
			<td>
				<% bp.getUtilizzatori().writeFormLabel(out,"find_linea_attivita"); %>
			</td>
			<td>
				<% bp.getUtilizzatori().writeFormInput(out,"find_linea_attivita"); %>
			</td>
		</tr>		
		<tr>
			<td>
				<% bp.getUtilizzatori().writeFormLabel(out,"percentuale_utilizzo_la"); %>
			</td>
			<td colspan="2">
				<% bp.getUtilizzatori().writeFormInput(out,"percentuale_utilizzo_la"); %>
			</td>
		</tr>	
	</table>	
   </div>	 
  <% } /* end if(aggiornamenti.hasUtilizzatori()) */ %>
