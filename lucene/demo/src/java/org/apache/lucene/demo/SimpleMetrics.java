/*
Before asking you to implement a ranking function, we want you to make use of Lucene to
compute some basic metrics:
1) Document Frequency: Returns the number of documents containing the term.
2) Term Frequency: Returns the total number of occurrences of the term across all
documents (the sum of the freq() for each doc that has this term).
You need to create a SimpleMetrics.java program to demonstrate how you can find the above
tow metric scores for a given term.
*/
package org.apache.lucene.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.demo.CMPT456Analyzer;
import org.apache.lucene.index.Term;

public final class SimpleMetrics{
	private int docfreq;
	private long termfreq;

	public static void main(String[] args){
		String index = "index";
		String field = "contents";
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		Analyzer analyzer = new CMPT456Analyzer();
		QueryParser parser = new QueryParser(field, analyzer);
		while(true){
			try{
				System.out.println("\nEnter a term: ");
				String new_string = in.readLine();
				if (new_string == null || new_string.length() == -1) {
	        		break;
	      		}
				new_string = new_string.trim();
	      		if (new_string.length() == 0) {
	        		break;
	      		}
	      		Query new_query = parser.parse(new_string);
      			System.out.println("Searching for: " + new_query.toString(field));
	      		Term new_term = new Term(field, new_query.toString(field));
	      		SimpleMetrics term_query = new SimpleMetrics(new_term);
	      		//print out the result
	      		System.out.println("For the term " + new_string + " :");
	      		System.out.println("The Document Frequency is " + term_query.docfreq);
	      		System.out.println("The Term Frequency is " + term_query.termfreq);
      		} catch (Exception e){
      	  		System.err.println("errors:" + e);
    		}
		}
	}

	public SimpleMetrics(){
		this.docfreq = 0;
		this.termfreq = 0;
	}

	public SimpleMetrics(int doc_freq, long term_freq){
		this.docfreq = doc_freq;
		this.termfreq = term_freq;
	}

	public SimpleMetrics(Term t){
		String index = "index";
		String field = "contents";
		try{
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		this.docfreq = reader.docFreq(t);
		this.termfreq = reader.totalTermFreq(t);
		} catch (Exception e){
      	  System.err.println("read errors:" + e);
    	}
	}

	public int get_docfreq(){
		return this.docfreq;
	}

	public long get_termfreq(){
		return this.termfreq;
	}


}