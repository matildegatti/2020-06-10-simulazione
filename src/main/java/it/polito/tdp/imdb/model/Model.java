package it.polito.tdp.imdb.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;
import it.polito.tdp.imdb.model.Evento.EventType;

public class Model {

	private ImdbDAO dao;
	private SimpleWeightedGraph<Actor,DefaultWeightedEdge> grafo;
	private Map<Integer,Actor> idMap;
	private PriorityQueue<Evento> queue;
	private int T;
	private List<Actor> intervistati;
	private List<Actor> nonIntervistati;
	private int giorniPausa;
	private List<String> controlloGenereIntervistati;
	
	public Model() {
		dao=new ImdbDAO();
		idMap=new HashMap<Integer,Actor>();
		
		this.dao.listAllActors(idMap);
	
	}
	
	public List<String> getGeneri(){
		return this.dao.listAllGenres();
	}

	public void creaGrafo(String genere) {
		grafo=new SimpleWeightedGraph<Actor,DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, this.dao.getVertici(idMap, genere));
		
		for(Archi a:this.dao.getArchi(idMap, genere)) {
			if(grafo.containsVertex(a.getA1()) && grafo.containsVertex(a.getA2())) {
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	public boolean getGrafo() {
		if(this.grafo==null) {
			return false;
		}
		return true;
	}

	public List<Actor> getVertici() {
		List<Actor> list=new LinkedList<>();
		for(Actor a:this.grafo.vertexSet()) {
			list.add(a);
		}
		return list;
	}

	public List<Actor> doAttoriSimili(Actor attore) {
		ConnectivityInspector<Actor,DefaultWeightedEdge> ci=new ConnectivityInspector<>(grafo);
		List<Actor> result=new LinkedList<>(ci.connectedSetOf(attore));
		result.remove(attore);
		
		Collections.sort(result, new Comparator<Actor>(){
			@Override
			public int compare(Actor a1, Actor a2) {
				return a1.getLastName().compareTo(a2.getLastName());
			}
		});
		
		return result;
	}

	public void simula(int n) {
		this.intervistati=new LinkedList<Actor>();
		this.nonIntervistati=this.getVertici();
		this.controlloGenereIntervistati=new LinkedList<String>();
		this.giorniPausa=0;
		T=1;
		
		this.queue=new PriorityQueue<Evento>();
		
		int random=(int) (Math.random()*(this.grafo.vertexSet().size()));
		Actor attore=this.getVertici().get(random);
		this.nonIntervistati.remove(attore);
		this.intervistati.add(attore);
		this.controlloGenereIntervistati.add(attore.gender);
		Evento e=new Evento(T,attore);
		this.queue.add(e);
		run(n);
	}
	
	public void run(int n) {
		Evento e;
		while((e=this.queue.poll())!=null) {
			this.T=e.getT();
			if(T<n) {
				Actor attore;
				if(e.getIntervistato()==null){
					int r=(int) (Math.random()*(this.nonIntervistati.size()));
					attore=this.nonIntervistati.get(r);
				}
				else {
					attore=e.getIntervistato();
				}
			
				Evento evento;
				//non controllo la pausa se non ci sono dua attori consecutivi senza pausa 
				//potrei fare una lista di n elementi in cui sovrascrivo gli attori intervistati, lasciando a null le pause, in modo tale da poter fare un controllo preciso sugli ultimi due giorni
				double ranPausa=Math.random()*100;
				if(ranPausa<90 && this.genereUguale()) {
					evento=new Evento(T+1,null);
					this.controlloGenereIntervistati.add("");
					this.giorniPausa++;
				}
				else {
					double random=Math.random()*100;
					
					int ran=(int) (Math.random()*(this.nonIntervistati.size()));
					Actor prossimo=this.nonIntervistati.get(ran);
					
					if(random<60) {
						evento=new Evento(T+1,prossimo);
					}
					else {
						if(this.vicinoMigliore(attore)==null) {
							evento=new Evento(T+1,prossimo);
						}
						else {
							prossimo=this.vicinoMigliore(attore);
							evento=new Evento(T+1,prossimo);
						}
					}
					this.nonIntervistati.remove(prossimo);
					this.intervistati.add(prossimo);
					this.controlloGenereIntervistati.add(prossimo.gender);
				}
				this.queue.add(evento);
			}
		}
			
	}
	
	private Actor vicinoMigliore(Actor attore) {
		Actor migliore=null;
		double peso=Integer.MIN_VALUE;
		for(Actor a:Graphs.neighborListOf(grafo, attore)) {
			DefaultWeightedEdge e=this.grafo.getEdge(attore, a);
			double p=this.grafo.getEdgeWeight(e);
			if(p>peso) {
				peso=p;
				migliore=a;
			}
		}
		
		return migliore;
	}
	
	private boolean genereUguale() {
		int size=this.controlloGenereIntervistati.size();
		if(size>2) {
			String ultimo=this.controlloGenereIntervistati.get(size-1);
			String penultimo=this.controlloGenereIntervistati.get(size-2);
			if(ultimo.equals(penultimo)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Actor> listIntervistati(){
		return this.intervistati;
	}
	public int giorniPausa() {
		return this.giorniPausa;
	}
}
