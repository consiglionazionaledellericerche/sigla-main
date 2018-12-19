script:{
	var json = jsonUtils.toObject(requestbody.content),
	    nodeRefSource = json.nodeRefSource,
	    nodoDoc = search.findNode(nodeRefSource),
	    username = json.username,
	    password = json.password,
	    otp = json.otp, mimetypeDoc, nameDoc, service, estensione, nameDocFirmato, docFirmato;
	mimetypeDoc = nodoDoc.properties.content.mimetype;
	nameDoc = nodoDoc.name;
	nameDocFirmato = nameDoc + ".p7m";
	//crea il doc firmato
	docFirmato = nodoDoc.parent.createFile(nameDocFirmato, "cnr:envelopedDocument");
	docFirmato.mimetype = "application/p7m";
	docFirmato.addAspect("sigla_fatture_attachment:fattura_elettronica_xml_post_firma");
	docFirmato.save();
	try{
		arubaSign.pkcs7SignV2(username, password, otp, nodoDoc.nodeRef, docFirmato.nodeRef);
	} catch(ex) {
		docFirmato.remove();
        status.code = 500;
        status.message = ex.javaException.message;
		status.redirect = true;
		break script;
	}  
	nodoDoc.addAspect("cnr:signedDocument");
	model.nodeRef = docFirmato.nodeRef.toString();
}