package it.polito.tdp.imdb.model;

public class Evento implements Comparable<Evento>{
	
	enum EventType{
		Scelta_casuale,
		Consiglio_attore,
		Pausa
	};
	
	private int t;
	private Actor intervistato;
	private EventType type;
	public Evento(int t, Actor intervistato) {
		super();
		this.t = t;
		this.intervistato = intervistato;
	}
	public int getT() {
		return t;
	}
	public void setT(int t) {
		this.t = t;
	}
	public Actor getIntervistato() {
		return intervistato;
	}
	public void setIntervistato(Actor intervistato) {
		this.intervistato = intervistato;
	}
	
	public EventType getType() {
		return type;
	}
	public void setType(EventType type) {
		this.type = type;
	}
	@Override
	public int compareTo(Evento o) {
		return this.t-o.t;
	}
	
}
