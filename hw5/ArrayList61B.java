import java.util.AbstractList;
	
public class ArrayList61B<E> extends AbstractList<E> {
	
	private int size;
	private E[] items;
	
	public ArrayList61B(int initialCapacity) {
		if (initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity less than 1");
		} 
		else {
			items = (E[]) new Object[initialCapacity];
			size = 0;
		}
	}
	
	public ArrayList61B() {
		size = 0;
		items = (E[]) new Object[1];
	}
	
	@Override
	public E get(int i) {
		if (i < 0 || i >= size) {
			throw new IllegalArgumentException("i is out of range");
		}
		else {
			return items[i];
		}
	}
	
	@Override
	public boolean add(E item) {
		if (size == items.length) {
			E[] newArray = (E[]) new Object[size*2];
			for (int i =0; i < items.length; i = i + 1) {
				newArray[i] = items[i];
			}
			items = newArray;	
			newArray[size] = item;
			size = size + 1;
			return true;
		}
		else {
			items[size] = item;
			size = size + 1;	
			return true;	
		}
	}
	
	@Override
	public int size() {
		return size;
	}	
}