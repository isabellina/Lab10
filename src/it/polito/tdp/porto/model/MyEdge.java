package it.polito.tdp.porto.model;

import org.jgrapht.graph.DefaultEdge;

public class MyEdge extends DefaultEdge
{

	public Author getSource() {
		return (Author) super.getSource();
	}
	
	public Author getTarget()
	{
		return (Author) super.getTarget();
	}
}
