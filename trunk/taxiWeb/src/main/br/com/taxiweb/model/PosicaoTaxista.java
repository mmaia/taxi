package br.com.taxiweb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PosicaoTaxista implements Serializable{
	

	private static final long serialVersionUID = -4139968972125247795L;
	
	private double latitude;
	private double longitude;
	
	@Column
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	@Column
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
}
