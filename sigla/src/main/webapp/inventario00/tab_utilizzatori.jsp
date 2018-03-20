<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.Utilizzatore_CdrVBulk,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 	
   Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); 
   Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel(); 
   Utilizzatore_CdrVBulk utilizzatore = (Utilizzatore_CdrVBulk)bp.getVUtilizzatori().getModel(); %>
    <table class="Group" style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">Beni:</span>
		</td>
       </tr>
       <tr>
         <td>
   		<% bp.getDettaglio().writeHTMLTable(pageContext,"righeSet",false,false,false,"100%","100px");  %>
   		</td>
       </tr>
    </table>
		
<%   if (buonoCarico.hasDettagli()){	%>
   <div class="Group">
	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">Utilizzatori:</span>
		</td>
       </tr>
       <tr>
         <td>
   		<% bp.getVUtilizzatori().writeHTMLTable(pageContext,"utilizzatoriSet",(riga!=null && (!riga.isBeneAccessorio())),false,(riga!=null && (!riga.isBeneAccessorio())),"100%","200px");%>
   		</td>
       </tr>
    </table>   	
	<table>			
		<tr>
			<td>
				<% bp.getVUtilizzatori().writeFormLabel(out,"find_cdr"); %>
			</td>
			<%
			if ( utilizzatore != null && utilizzatore.getCdr()!=null && utilizzatore.getCdCdr()!=null){
			%>
			<td>
				<% bp.getVUtilizzatori().writeFormInput(out,null,"find_cdr",true,null,null); %>
			</td>
			<%
			}else{
			%>
			<td>
				<% bp.getVUtilizzatori().writeFormInput(out,null,"find_cdr",false,null,null); %>
			</td>
			<%
			}
			%>
		</tr>	
		<tr>
			<td>
				<% bp.getVUtilizzatori().writeFormLabel(out,"percentuale_utilizzo_cdr"); %>
			</td>
			<td colspan="3">
				<% bp.getVUtilizzatori().writeFormInput(out,"percentuale_utilizzo_cdr"); %>
			</td>
		</tr>
	</table> 
	</div>
	<%
		if (riga!=null && riga.hasUtilizzatori() && utilizzatore != null && utilizzatore.getCdr()!=null && utilizzatore.getCdCdr()!=null){
	%>
	<div class="Group">
	<table style="width:100%">
       <tr>
    	<td>
		  <span class="FormLabel" style="color:blue">Gruppi azioni elementari:</span>
		</td>
       </tr>
       <tr>
         <td>
   		<% bp.getUtilizzatori().writeHTMLTable(pageContext,"utilizzatoriSet",bp.isInserting(),false,bp.isInserting(),"100%","100px"); %>
   		</td>
       </tr>
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
   <% } /* end if(rigo.hasUtilizzatori()) */ %> 
  <% } /* end if(buono.hasDettagli())*/ %>