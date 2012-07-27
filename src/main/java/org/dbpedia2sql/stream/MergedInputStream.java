package org.dbpedia2sql.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.dbpedia2sql.model.Triple;

public class MergedInputStream implements TriplesInputStream {
	static class HeapEntry implements Comparable<HeapEntry>{
		HeapEntry(TriplesInputStream input, Triple data, int index) {
			this.input = input; this.data = data; this.index = index;
		}
		int index;
		TriplesInputStream input;
		Triple data;
		
		@Override
		public int compareTo(HeapEntry o) {
			int ret = this.data.getSubject().compareTo(o.data.getSubject());
//			System.out.println("  COMPARE [" + index + "]" + this.data.getSubject() + " TO [" + o.index + "]" + o.data.getSubject() + " --> " + ret);
			return ret;
		}
	}
	List<TriplesInputStream> inputs = new ArrayList<TriplesInputStream>();
	int[] countPerIndex;
	PriorityQueue<HeapEntry> heap = new PriorityQueue<HeapEntry>();
	
	public void addInput(TriplesInputStream input) {
		inputs.add(input);
	}
	
	public void debug() {
		for (int i = 0; i < inputs.size(); i++) {
			System.out.println("Heap input " + i +  " is " + inputs.get(i)+ " fetched " + countPerIndex[i]);
		}
		for (HeapEntry he : heap) {
			System.out.println("Heap elt is from input " + he.index + " and is " + he.data);
		}
	}
	
	public void start() throws IOException {
		countPerIndex = new int[inputs.size()];

		/* Fill the heap with one element from each input */
		for (int i = 0; i < inputs.size(); i++) {
			Triple data = inputs.get(i).nextTriple();
			if (data == null) {
				System.out.println("EOF ON " + inputs.get(i));
				continue; // One of the input is empty
			}
			heap.add(new HeapEntry(inputs.get(i), data, i));
		}
	}

	@Override
	public Triple nextTriple() throws IOException {
		if (heap.size() == 0) return null; // EOF on all streams
		
		HeapEntry root = heap.remove();
//		System.out.println("TOOK FROM " + root.input + " LINE=" + countPerIndex[root.index]++ + " -> " + root.data.getSubject());
		
		/* Replace the root with a new element from the same stream */
		Triple newTriple = root.input.nextTriple();
		if (newTriple != null) {
//			System.out.println("  GOT replacement: " + newTriple.getSubject());
			heap.add(new HeapEntry(root.input, newTriple, root.index));
		} else {
			System.out.println("EOF ON " + root.input);
		}

		return root.data;
	}
}