package it.polito.tdp.imdb.model;

public class Archi {

	private Actor a1;
	private Actor a2;
	private double peso;
	public Archi(Actor a1, Actor a2, double peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Actor getA1() {
		return a1;
	}
	public void setA1(Actor a1) {
		this.a1 = a1;
	}
	public Actor getA2() {
		return a2;
	}
	public void setA2(Actor a2) {
		this.a2 = a2;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
}
