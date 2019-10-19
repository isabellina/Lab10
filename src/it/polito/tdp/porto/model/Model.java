package it.polito.tdp.porto.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultUndirectedGraph;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO  portoDAO ;
	private Graph grafo;
	private List<Pubblication> pubblications;
	
	
	public Model () {
		this.portoDAO = new PortoDAO();
		System.out.println("Creo il grafo...");
		this.test();
		this.creaGrafo();
		System.out.println("Finisco il grafo...");

	}
	
	public List<Author> getAutori(){
		List<Author> listaAutori = this.portoDAO.getAutore();
		return listaAutori;
	}

	public void test() {
		this.grafo = new SimpleGraph<>(MyEdge.class);
		
		List<Pubblication> pubblications = new LinkedList<Pubblication>();
				
		for(Author a: this.getAutori()) {
			this.grafo.addVertex(a);
			
			List<Pubblication> temp = this.portoDAO.getc(a.getId());
			
			for(Pubblication pub : temp) {
				pubblications.add(pub);
			}
		}
		this.pubblications = pubblications;
		System.out.println("finisco di prendere le pubblications");
	}
	
	public List<Pubblication> getAuthorPubblications(int authorid){
		List<Pubblication> temp = new LinkedList<Pubblication>();
		for(Pubblication p : this.pubblications) {
			if(p.getAuthorid() == authorid) {
				temp.add(p);
			}
		}
		return temp;
	}
	
	public void creaGrafo()
	{
		for(Object a : this.grafo.vertexSet()) {
			for(Author b : this.portoDAO.getCoauthors((Author) a)) {
				this.grafo.addEdge(a, b);
			}
		}
	}
	
	public List<Author> getNoCoautori(Author a){
		List<Author> ltemp = new LinkedList<Author>();
		ltemp = this.portoDAO.getAutore();
		ltemp.removeAll(this.portoDAO.getCoauthors(a));
		ltemp.remove(a);
		return ltemp;
	}
	
	public String getCoautori(Author a) {
		String s = "";
		Set<DefaultEdge> ltemp = this.grafo.edgesOf(a);
		if (ltemp.size() == 0)
			return "";
		for(DefaultEdge c : ltemp) {
			s+= c.toString();
		}
		return s;
	}
	
	public String getPercorso(Author a1, Author a2) {
		String s ="";
		ShortestPathAlgorithm<Author, MyEdge> path = new DijkstraShortestPath<Author, MyEdge>(this.grafo);
	
		GraphPath<Author, MyEdge> gp = path.getPath(a1, a2);
		
		String ret = "";
		for(Object edge : gp.getEdgeList()) {
			MyEdge my = (MyEdge) edge;
			Author ai = (Author) my.getSource();
			Author af = (Author) my.getTarget();
			ret += this.portoDAO.getArticoloComune(ai, af) + "\n";
		}
		
		return ret;
		} 
	
	
	
	
	
	
}
