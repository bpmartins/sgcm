function MascaraMoeda(objTextBox, SeparadorMilesimo, SeparadorDecimal, e, maxlength){
    	var sep = 0;
	    var key = '';
	    var i = j = 0;
	    var len = len2 = 0;
	    var strCheck = '0123456789';
	    var aux = aux2 = '';
	    var whichCode = (e.charCode) ? e.charCode : ((e.keyCode) ? e.keyCode : ((e.which) ? e.which : 0));
	    if (whichCode == 13 || whichCode == 8 || whichCode == 9) return true;
		    key = String.fromCharCode(whichCode); // Valor para o código da Chave
	    if (strCheck.indexOf(key) == -1){
	        return false; // Chave invalida
	    }

	    strTemp = objTextBox.value.replace(SeparadorDecimal,"");

    	while(i < objTextBox.value.length){
        	strTemp = strTemp.replace(SeparadorMilesimo,"");
        	i++;
    	}

    	if(strTemp.length > maxlength){	
        	objTextBox.value = strTemp.substring(0, maxlength)
        }

	    len = objTextBox.value.length;

    	if (len >= maxlength){
	        return false;
    	}

    	for(i = 0; i < len; i++){
	        if ((objTextBox.value.charAt(i) != '0') && (objTextBox.value.charAt(i) != SeparadorDecimal)) break;
	    }
	    aux = '';
    	
    	for(; i < len; i++){
	        if (strCheck.indexOf(objTextBox.value.charAt(i))!=-1) aux += objTextBox.value.charAt(i);
		}
		
	    aux += key;
	    len = aux.length;
    
	    if (len == 0){
    		objTextBox.value = '';
	    }
    
	    if (len == 1){
	    	objTextBox.value = '0'+ SeparadorDecimal + '0' + aux;
	    }
	    
	    if (len == 2){
	    	objTextBox.value = '0'+ SeparadorDecimal + aux;
	    }
	    
	    if (len > 2){
	        aux2 = '';
        	
        	for (j = 0, i = len - 3; i >= 0; i--) {
            	if (j == 3) {
                	aux2 += SeparadorMilesimo;
                	j = 0;
            	}
            	aux2 += aux.charAt(i);
            	j++;
        	}
        	
        	objTextBox.value = '';
        	len2 = aux2.length;

	        for (i = len2 - 1; i >= 0; i--){
    	        objTextBox.value += aux2.charAt(i);
    	    }
	        objTextBox.value += SeparadorDecimal + aux.substr(len - 2, len);
    	}
	    return false;
	}

function SomenteNumero(e) {
		var key = (e.charCode) ? e.charCode : ((e.keyCode) ? e.keyCode : ((e.which) ? e.which : 0));
		if (key > 47 && key < 58) {
			return true;
		} else {
			if (key == 8 || key == 9 || key == 13) {
				return true;
			} else {
				return false;
			}
		}
	}
    
 	function ValidaCPF(cpf) {
		
		if (cpf == null) {
			return false;
		}
		
		var numeros, digitos, soma, i, resultado, digitos_iguais, temp;
		digitos_iguais = 1;
		temp = cpf.value.replace(".","").replace(".","").replace("-","");	
	
		if (temp.length < 11){
			return false;
		}

		for (i = 0; i < temp.length - 1; i++){
			if (temp.charAt(i) != temp.charAt(i + 1)) {
				digitos_iguais = 0;
				break;
			}
		}

		if (!digitos_iguais) {
			numeros = temp.substring(0,9);
			digitos = temp.substring(9);
			soma = 0;
		
			for (i = 10; i > 1; i--){
				soma += numeros.charAt(10 - i) * i;
			}

			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;

			if (resultado != digitos.charAt(0)){
				return false;
			}
			numeros = temp.substring(0,10);
			soma = 0;
			
			for (i = 11; i > 1; i--){
				soma += numeros.charAt(11 - i) * i;
			}
			
			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;

			if (resultado != digitos.charAt(1)){
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	function ValidaCNPJ(cnpj) {
		var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais, temp;	
		temp = cnpj.value.replace(".","").replace(".","").replace("/","").replace("-","");	
		digitos_iguais = 1;
		if (temp.length < 14 && temp.length < 15)
			return false;
		for (i = 0; i < temp.length - 1; i++)
			if (temp.charAt(i) != temp.charAt(i + 1)) {
				digitos_iguais = 0;
				break;
			}
		if (!digitos_iguais) {
			tamanho = temp.length - 2
			numeros = temp.substring(0,tamanho);
			digitos = temp.substring(tamanho);
			soma = 0;
			pos = tamanho - 7;
			for (i = tamanho; i >= 1; i--) {
				soma += numeros.charAt(tamanho - i) * pos--;
				if (pos < 2)
					pos = 9;
			}
			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
			if (resultado != digitos.charAt(0))
				return false;
			tamanho = tamanho + 1;
			numeros = temp.substring(0,tamanho);
			soma = 0;
			pos = tamanho - 7;
			for (i = tamanho; i >= 1; i--) {
				soma += numeros.charAt(tamanho - i) * pos--;
				if (pos < 2)
					pos = 9;
			}
			resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
			if (resultado != digitos.charAt(1))
				return false;
			return true;
		} else
			return false;
	} 
	
	function preencheZeros(param,tamanho){   
		var contador = param.length;   
		if (param.length != tamanho){   
      		do{   
         		param = "0" + param;   
         		contador += 1;   
			}while (contador <tamanho)   
		}   
	   return param
	}
	
	function Trim(str){
		return str.replace(/^\s+|\s+$/g,"");
	}
	
	function validarData(data){
		var data1 = data.replace("/", "").replace("/", "");
		if(data == undefined || Trim(data1) == "") { return true; };
		dp = data.split("/");
		d = dp[0];
		m = dp[1];
		y = dp[2];
		var o = new Date(y, --m, d);
		var p = new Date();
    	return (o.getFullYear() == y && o.getMonth() == m && o.getDate() == d);
	}
	
	function compararData(dataInicial, dataFinal){
		dataIni = dataInicial.split("/");
		dataFim = dataFinal.split("/");
		dIni = dataIni[0]; mIni = dataIni[1]; yIni = dataIni[2];
		dFim = dataFim[0]; mFim = dataFim[1]; yFim = dataFim[2];
		var dateIni = new Date(yIni, --mIni, dIni);
		var dateFim = new Date(yFim, --mFim, dFim);
	    return (dateFim < dateIni);
	}