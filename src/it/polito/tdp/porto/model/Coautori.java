package it.polito.tdp.porto.model;

public class Coautori {

	private Author autore1;
	private Author autore2;
	
	
	public Coautori(Author autore1, Author autore2) {
		
		this.autore1 = autore1;
		this.autore2 = autore2;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autore1 == null) ? 0 : autore1.hashCode());
		result = prime * result + ((autore2 == null) ? 0 : autore2.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coautori other = (Coautori) obj;
		if (autore1 == null) {
			if (other.autore1 != null)
				return false;
		} else if (!autore1.equals(other.autore1))
			return false;
		if (autore2 == null) {
			if (other.autore2 != null)
				return false;
		} else if (!autore2.equals(other.autore2))
			return false;
		return true;
	}


	public Author getAutore1() {
		return autore1;
	}


	public Author getAutore2() {
		return autore2;
	}
	
	
	
}
