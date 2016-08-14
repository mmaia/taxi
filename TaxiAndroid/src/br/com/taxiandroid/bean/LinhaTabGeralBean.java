package br.com.taxiandroid.bean;

public class LinhaTabGeralBean {
	private int idObj;
	private String pl;
	private String espera30;
	private String espera15;
	private String taxista;
	private String Ult1h;
//	private int totalTaxista;
//	private int totalEspera15;
//	private int totalEspera30;

	public static LinhaTabGeralBean getLinhaTabGeralBean(String linha){
		LinhaTabGeralBean lt = new LinhaTabGeralBean();
		String dados[] = linha.split(",");
		lt.setPl(dados[0].split(":")[1].replace('"', ' ').trim());
		lt.setEspera30(dados[1].split(":")[1].replace('"', ' ').trim());
		lt.setUlt1h(dados[2].split(":")[1].replace('"', ' ').trim());
		lt.setTaxista(dados[3].split(":")[1].replace('"', ' ').trim());
		lt.setEspera15(dados[4].split(":")[1].replace('"', ' ').trim());
		lt.setEspera15(lt.getEspera15().replace('}', ' ').trim());
		return lt;
	}

	public int getIdObj() {
		return idObj;
	}
	public void setIdObj(int idObj) {
		this.idObj = idObj;
	}
	public String getPl() {
		return pl;
	}
	public void setPl(String pl) {
		this.pl = pl;
	}
	public String getEspera30() {
		return espera30;
	}
	public void setEspera30(String espera30) {
		this.espera30 = espera30;
	}
	public String getEspera15() {
		return espera15;
	}
	public void setEspera15(String espera15) {
		this.espera15 = espera15;
	}
	public String getTaxista() {
		return taxista;
	}
	public void setTaxista(String taxista) {
		this.taxista = taxista;
	}
	public String getUlt1h() {
		return Ult1h;
	}
	public void setUlt1h(String ult1h) {
		Ult1h = ult1h;
	}
}
