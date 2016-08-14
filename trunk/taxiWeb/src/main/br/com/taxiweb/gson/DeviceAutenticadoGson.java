package br.com.taxiweb.gson;

import com.google.gson.Gson;

//TODO - Esta classe e toda a hierarquia relacionada a ela deve evoluir logo, possivelmente no primeiro refactoring do projeto para 
//algum tipo de factory ou builder de objetos - ESTUDAR ISSO.

/**
* POJO simples para ser herdado por todos os objetos Gson que forem ser enviados pelo device para operações 
* em que o cliente precise estar com o device autenticado. 
* Estes objetos são responsáveis pelo mapeamento de JSon para Objetos java. 
* Cada objeto que herdar desta classe deve prover método para serializar e desserializar utilizando a API Gson. 
* Com a evolução do projeto possivelmente terei que add. metodos abstratos ou para sobrescrição 
* pelos objetos descendentes, forçando um padrão. 
* json utilizando a api GSon do google. Ver url: <a href="http://code.google.com/p/google-gson/">http://code.google.com/p/google-gson </a>
* @author mmaia
*/
public class DeviceAutenticadoGson {
	
private String senhaDevice;
	
	public String getSenhaDevice() {
		return senhaDevice;
	}

	public void setSenhaDevice(String senhaDevice) {
		this.senhaDevice = senhaDevice;
	}
	
	public static  DeviceAutenticadoGson fromJSON(String json)
	{
		Gson gson = new Gson();
		DeviceAutenticadoGson daGson = gson.fromJson(json, DeviceAutenticadoGson.class);
		return daGson;
	}
	
	public static String toJSON(DeviceAutenticadoGson dvaGson)
	{
		Gson gson = new Gson();
		String result = gson.toJson(dvaGson);
		return result;
	}
}
