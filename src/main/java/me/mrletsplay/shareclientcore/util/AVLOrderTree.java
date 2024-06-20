package me.mrletsplay.shareclientcore.util;

import java.util.AbstractCollection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A balanced binary tree that supports order statistics ({@link #get(int)} and {@link #indexOf(Object)}).<br>
 * Can only hold unique elements
 * @param <E> The element type
 */
public class AVLOrderTree<E> extends AbstractCollection<E> {

	private Node root;
	private Comparator<E> comparator;

	public AVLOrderTree(Comparator<E> comparator) {
		this.comparator = comparator;
	}

	@SuppressWarnings("unchecked")
	public AVLOrderTree() {
		this.comparator = (Comparator<E>) Comparator.naturalOrder();
	}

	@Override
	public boolean add(E e) {
		return addIndex(e) != -1;
	}

	public int addIndex(E e) {
		if(root == null) {
			root = new Node(e);
			return 0;
		}

		return index(root.add(e));
	}

	@Override
	public boolean remove(Object o) {
		return removeIndex(o) != -1;
	}

	@SuppressWarnings("unchecked")
	public int removeIndex(Object o) {
		if(root == null) return -1;

		Node removed = root.remove((E) o);
		if(removed == root) root = null;
		return index(removed);
	}

	@Override
	public void clear() {
		root = null;
	}

	public int indexOf(E e) {
		return 0; // TODO
	}

	private int index(Node n) {
		return 0; // TODO
	}

	public E get(int index) {
		return null;
	}

	@Override
	public Iterator<E> iterator() {
		return new TreeIterator();
	}

	@Override
	public int size() {
		Iterator<E> it = iterator();
		int i = 0;
		while(it.hasNext()) {
			it.next();
			i++;
		}
		return i;
	}

	private class TreeIterator implements Iterator<E> {

		private Node current;
		private Node next;

		public TreeIterator() {
			this.current = null;
			this.next = root == null ? null : root.leftmostChild();
		}

		@Override
		public boolean hasNext() {
			return next != null;
		}

		@Override
		public E next() {
			// FIXME incorrect
			if(next == null) throw new NoSuchElementException();

			current = next;

			Node afterNext;
			if(next.right == null) {
				afterNext = (next.parent != null && next.parent.left == next) ? next.parent : null;
			}else {
				afterNext = next.right.leftmostChild();
			}

			next = afterNext;
			return current.value;
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException(); // TODO
		}

	}

	private class Node {

		private E value;
		private Node parent;
		private Node left;
		private Node right;
		private int balance;
		private int size;

		public Node(E value) {
			this.value = value;
		}

		public Node add(E value) {
			// TODO: rebalance
			int comp = comparator.compare(this.value, value);
			if(comp == 0) return null;

			if(comp < 0) {
				if(left == null) {
					return left = new Node(value);
				}

				return left.add(value);
			}else {
				if(right == null) {
					return right = new Node(value);
				}

				return right.add(value);
			}
		}

		public Node remove(E value) {
			// TODO: rebalance
			int comp = comparator.compare(value, value);
			if(comp == 0) return this;

			if(comp < 0) {
				if(left == null) return null;

				Node removed = left.remove(value);
				if(removed == left) left = null;
				return removed;
			}else {
				if(right == null) return null;

				Node removed = right.remove(value);
				if(removed == right) right = null;
				return removed;
			}
		}

		public Node leftmostChild() {
			if(left == null) return this;
			return left.leftmostChild();
		}

	}

}
