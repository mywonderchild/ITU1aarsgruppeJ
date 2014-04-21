package Map.Model;

import java.util.Iterator;
import java.lang.UnsupportedOperationException;
import java.util.NoSuchElementException;

public class Bag<Item> implements Iterable<Item> {
	private Node first;
	private int n;

	public Bag() {
		first = null;
		n = 0;
	}

	public void add(Item item) {
		Node old = first;
		first = new Node(item);
		first.next = old;
		n++;
	}

	public int size() {
		return n;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public Iterator<Item> iterator() {
		return new BagIterator();
	}

	private class BagIterator implements Iterator<Item> {
		Node node;

		public BagIterator() {
			node = new Node(null);
			node.next = first;
		}

		@Override
		public boolean hasNext() {
			return node.next != null;
		}

		@Override
		public Item next() {
			if(!hasNext()) throw new NoSuchElementException();
			node = node.next;
			return node.item;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
	} 

	private class Node {
		public final Item item;
		public Node next;

		public Node(Item item) {
			this.item = item;
		}
	}
}