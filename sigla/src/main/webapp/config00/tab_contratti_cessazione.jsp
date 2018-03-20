<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
%>
      <table>             
      <tr>  
        <td><% bp.getController().writeFormLabel(out,"dt_annullamento");%></td>
        <td><% bp.getController().writeFormInput(out,"dt_annullamento");%></td>
      </tr>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_annullamento");%></td>
         <td colspan="4"><% bp.getController().writeFormInput(out,"ds_annullamento");%></td>
      </tr>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"atto_annullamento");%></td>
         <td colspan="3"><% bp.getController().writeFormInput(out,"atto_annullamento");%></td>
         <td><% bp.getController().writeFormInput(out,"crea_atto_annullamento");%></td>
      </tr>
      <% if (((ContrattoBulk)bp.getModel()).isDs_atto_ann_non_definitoVisible()){%>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_atto_ann_non_definito");%></td>
         <td colspan=4><% bp.getController().writeFormInput(out,"ds_atto_ann_non_definito");%></td>
      </tr>      
      <%}%>            
      <tr>
         <td><% bp.getController().writeFormLabel(out,"organo_annullamento");%></td>
         <td colspan="3"><% bp.getController().writeFormInput(out,"organo_annullamento");%></td>
         <td><% bp.getController().writeFormInput(out,"crea_organo_annullamento");%></td>
      </tr>      
      <% if (((ContrattoBulk)bp.getModel()).isDs_organo_ann_non_definitoVisible()){%>
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"ds_organo_ann_non_definito");%></td>
         <td colspan=4><% bp.getController().writeFormInput(out,"ds_organo_ann_non_definito");%></td>
      </tr>      
      <%}%>                  
	  <tr>         
         <td><% bp.getController().writeFormLabel(out,"fl_art82");%></td>
         <td colspan=4><% bp.getController().writeFormInput(out,"fl_art82");%></td>
      </tr>            
      </table>