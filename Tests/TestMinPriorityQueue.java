package Tests;

import static org.junit.Assert.*;
import org.junit.*;

import Map.Model.MinPriorityQueue;
import java.util.NoSuchElementException;

public class TestMinPriorityQueue {
	MinPriorityQueue<Integer> pq;
	double delta = 1e-3;

	@Before
	public void setup() {
		pq = new MinPriorityQueue<>();
	}

	@Test
	public void pushPop() {
		Integer[] ints = new Integer[] {
			0, 1, 1, -1, 2
		};
		pq.push(ints);

		Integer[] pqints = new Integer[]{ // pq sorted
			-1, 0, 1, 1, 2
		};
		for(int i = 0; i < pqints.length; i++) {
			assertEquals(pq.pop(), pqints[i], delta);
		}
	}

	@Test
	public void peek() {
		pq.push(1);
		pq.push(-1);
		assertEquals(pq.peek(), -1, delta);
	}

	@Test(expected=NoSuchElementException.class)
	public void popPeekFail() {
		pq.peek(); // empty fail
		pq.pop(); // empty fail

		pq.push(0);
		pq.pop();

		pq.pop(); // empty fail
		pq.peek(); // empty fail
	}

	@Test
	public void size() {
		assertEquals(pq.size(), 0, delta);

		pq.push(0);
		assertEquals(pq.size(), 1, delta);

		pq.pop();
		assertEquals(pq.size(), 0, delta);
	}

	@Test
	public void isEmpty() {
		assertTrue(pq.isEmpty());

		pq.push(0);
		assertFalse(pq.isEmpty());

		pq.pop();
		assertTrue(pq.isEmpty());
	}

	@Test
	public void toStringTest() {
		// Difficult to test for correctness as the
		// return string contains the obj ref.

		assertNotNull(pq.toString());

		pq.push(1);
		assertNotNull(pq.toString());

		pq.pop();
		assertNotNull(pq.toString());
	}
}