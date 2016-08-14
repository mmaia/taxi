var mapa, geocoder, markerOrigem, markerDestino, coordOrigem, coordDestino, locais, bounds, taxiMarker;
var latOrigem, longOrigem, latDestino, longDestino;
(
	//carrega o mapa
	function(){
	window.onload = function (){
		var options = {
				center: new google.maps.LatLng(0.0, 3,51),
				zoom: 1,
				mapTypeId:google.maps.MapTypeId.ROADMAP
		};
		
		mapa = new google.maps.Map(document.getElementById('solicitacaoMap'), options);
		
		//checa se geolocalização está disponível, o browser mostra uma mensagem solicitando ao usuário para autorizar recuperação da localização.
		if(geo_position_js.init())
		{
			var configuracoes = 
			{
					enableHighAccuracy: true
			};
			geo_position_js.getCurrentPosition(posicionaMapa, trataErros, configuracoes);
		}
		else
		{
			alert('Infelizmente sua localização não está disponível!');
		}
		
		var endOrigem = document.getElementById('solicitarTaxiForm:origemField:origem');
		var autocompleteOrigem = new google.maps.places.Autocomplete(endOrigem);
		autocompleteOrigem.bindTo('bounds', mapa);
		
		var endDestino = document.getElementById('solicitarTaxiForm:destinoField:destino');
		var autocompleteDestino = new google.maps.places.Autocomplete(endDestino);
		autocompleteDestino.bindTo('bounds', mapa);
		
		//desabilita submit para só habilitar qdo campos estiverem preenchidos
		
		habilitaBotaoSolicitacao();
	};
	
	
	function trataErros(erro)
	{
		alert('Ooops, ocorreu um erro: ' + erro.message);
	};
	
	//função que posiciona o mapa inicial da tela no local do acesso do usuário se ele autorizou.
	function posicionaMapa(position)
	{
		//recuperando latitude e longitude para posicionar o mapa
		var latLong = new google.maps.LatLng(position.coords.latitude, position.coords.longitude);

		mapa.setCenter(latLong);
		mapa.setZoom(12);
	};
	
})();


function habilitaBotaoSolicitacao()
{
	var submitButton = document.getElementById('solicitarTaxiForm:submit:save');
	var endOrigem = document.getElementById('solicitarTaxiForm:origemField:origem');
	submitButton.disabled = ((endOrigem.value == ""));
}

//Função chamada ao preencher dados da solicitação do taxi para colocar o posicionamento e marcadores da solicitação no mapa.
function atualizaOrigem()
{
//	var form = document.getElementById('solicitarTaxiForm');
	var endOrigem = document.getElementById('solicitarTaxiForm:origemField:origem').value;
	var endDestino = document.getElementById('solicitarTaxiForm:destinoField:destino').value;
	if(endOrigem == "" || endDestino == "")
	{
		return;
	}
	locais = [];
	
	desenhaRota(endOrigem, endDestino);
	
//	centralizaMapa();
	
//	desenhaTaxi();
//	return false;
}

//Função que trata endereço de origem e põe marcação da origem no mapa.
function desenhaRota(enderecoOrigem, endDestino)
{
	if(!geocoder)
	{
		geocoder = new google.maps.Geocoder();
	}
	
	var geocoderRequest = 
	{
			address: enderecoOrigem
	};
	
	geocoder.geocode(geocoderRequest, function(results, status)
	{
		if(status == google.maps.GeocoderStatus.OK)
		{
//			mapa.setCenter(results[0].geometry.location);
//			if(!markerOrigem){
//				markerOrigem = new google.maps.Marker({
//					map: mapa
//				});
//			}
			coordOrigem = results[0].geometry.location;
			locais.push(coordOrigem);
			latOrigem = coordOrigem.lat();
			longOrigem = coordOrigem.lng();
			getCoordinatesDestino(endDestino);
//			markerOrigem.setPosition(coordOrigem);
			
		}
		else if(status == google.maps.GeocoderStatus.INVALID_REQUEST)
		{
			alert('Endereço Inválido!');
			return false;
		}
		else
		{
//			alert('nao inicializou coord de origem...');
			return false;
		}
	});
}

//Função que trata endereço de destino e põe marcação do destino no mapa.
function getCoordinatesDestino(enderecoDestino)
{
	if(!geocoder)
	{
		geocoder = new google.maps.Geocoder();
	}
	
	var geocoderRequest = 
	{
			address: enderecoDestino
	};
	
	geocoder.geocode(geocoderRequest, function(results, status)
	{
		if(status == google.maps.GeocoderStatus.OK)
		{
//			if(!markerDestino){
//				markerDestino = new google.maps.Marker({
//					map: mapa
//				});
//			}
		    coordDestino = results[0].geometry.location;
			locais.push(coordDestino);
			latDestino = coordDestino.lat();
			longDestino = coordDestino.lng();
//			markerDestino.setPosition(coordDestino);
			renderizaRota();
			
		}
		else if(status == google.maps.GeocoderStatus.INVALID_REQUEST)
		{
			alert('Endereço Inválido!');
			return false;
		}
		else
		{
//			alert('nao inicializou coordDestino ...');
			return false;
		}
	});
}

//função javascript que desenha rota no mapa.
function renderizaRota()
{
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();
	var rotaRequest = {
			avoidHighways: false,
			origin: new google.maps.LatLng(latOrigem, longOrigem),
			destination: new google.maps.LatLng(latDestino, longDestino),
			travelMode: google.maps.TravelMode.DRIVING,
			unitSystem: google.maps.DirectionsUnitSystem.METRIC
	};
	
	directionsService.route(rotaRequest, tracaRotaCallBack);

}


function tracaRotaCallBack(result, status) {
    if (status == google.maps.DirectionsStatus.OK) {
      directionsDisplay.setDirections(result);
      directionsDisplay.setMap(mapa);
  	  directionsDisplay.setPanel(document.getElementById("painelRota"));
    }
    else
    {
//    	alert('wtf no results?????');
    }
  }


//Pega solicitação e centraliza o mapa colocando os markers da origem e destino na tela.
function centralizaMapa()
{
	bounds = new google.maps.LatLngBounds(locais[0], locais[1]);
	mapa.fitBounds(bounds);
	mapa.setZoom(10);
}

function desenhaTaxi(latitude, longitude)
{
	var image = '../img/taxi-icon.png';
	  var myLatLng = new google.maps.LatLng(latitude, longitude);
	  taxiMarker = new google.maps.Marker({
	      position: myLatLng,
	      map: mapa,
	      icon: image
	  });
}