package Map.Model;

import java.util.NoSuchElementException;

public class MinPriorityQueue<Key extends Comparable<Key>> {
	private Key[] a;
	private int n;
	
	@SuppressWarnings("unchecked")
	public MinPriorityQueue(int initSize) {
		a = (Key[]) new Comparable[initSize];
		n = 0;
	}

	public MinPriorityQueue() {
		this(1);
	}

	public void push(Key key) {
		if(n == a.length-1) resize();
		a[++n] = key;
		swim(n);
	}

	public void push(Key[] keys) {
		for(Key key : keys) {
			push(key);
		}
	}

	public Key pop() {
		if(isEmpty()) throw new NoSuchElementException("MinPriorityQueue underflow");
		
		Key ret = a[1];
		exch(1, n--);
		sink(1);

		a[n+1] = null; // gc
		if(n > 0 && n == (a.length-1)/4) resize();
		return ret;
	}

	public Key peek() {
		if(isEmpty()) throw new NoSuchElementException("MinPriorityQueue underflow");
		return a[1];
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return n == 0;
	}

	private void swim(int i) {
		while(i > 1 && greater(i/2, i)) {
			exch(i/2, i);
			i = i/2;
		}
	}

	private void sink(int i) {
		while(i*2 <= n) {
			int j = 2*i;
			if(j < n && greater(j, j+1)) j++;
			if(!greater(i, j)) break;
			exch(i, j);
			i = j;
		}
	}

	private boolean greater(int i, int j) {
		return a[i].compareTo(a[j]) == 1;
	}

	private void exch(int i, int j) {
		Key temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	@SuppressWarnings("unchecked")
	private void resize() {
		int size = n != 0 ? n*2 : 2; // 2 if n is 0. else 2n.
		Key[] temp = (Key[]) new Comparable[(n+1)*2];
		for(int i = 0; i <= n; i++) {
			temp[i] = a[i];
		}
		a = temp;
	}

	public String toString() {
		String ret = super.toString() + ": ";
		if(!isEmpty()) {
			for(int i = 1; i <= n; i++) {
				ret += a[i].toString() + ", ";
			}
			ret = ret.substring(0,ret.length()-2);
		}
		return ret;
	}
}