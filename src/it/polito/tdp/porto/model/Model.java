package it.polito.tdp.porto.model;

import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedGraph;

import it.polito.tdp.porto.db.PortoDAO;

public class Model {
	
	private PortoDAO  portoDAO ;
	private Graph grafo;
	
	
	public Model () {
		this.portoDAO = new PortoDAO();
		System.out.println("Creo il grafo...");
		this.creaGrafo();
		System.out.println("Finisco il grafo...");

	}
	
	public List<Author> getAutori(){
		List<Author> listaAutori = this.portoDAO.getAutore();
		return listaAutori;
	}

	
	public void creaGrafo() {
		 this.grafo = new DefaultUndirectedGraph<Author,Coautori>(Coautori.class);
		 for(Author a : this.getAutori()) {
			 this.grafo.addVertex(a);
		 }
		 for(Object a: this.grafo.vertexSet()) {
			 for(Object b: this.grafo.vertexSet()) {
				 if(!a.equals(b)) {
					 Author a1 = (Author) a;
					 Author b1 = (Author) b;
					 if(this.portoDAO.getCoautori(a1.getId(), b1.getId())) {
						 this.grafo.addEdge(a,b);
					 }
				 }
			 }
		 }
	}
	
	public String getCoautori(Author a) {
		String s = "";
		Set<Coautori> ltemp = this.grafo.edgesOf(a);
		if (ltemp.size() == 0)
			return "";
		for(Coautori c : ltemp) {
			if(c.getAutore1().equals(a)) {
				s += c.getAutore2().getFirstname();
			}
			else {
				s += c.getAutore1().getFirstname();
			}
		}
		return s;
	}
}
