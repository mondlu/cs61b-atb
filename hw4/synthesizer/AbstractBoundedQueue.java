package synthesizer;
public abstract class AbstractBoundedQueue implements BoundedQueue {
	protected int fillcount;
	protected int capacity;
	
	public boolean isFull() {
		if (fillcount == capacity) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int capacity() {
		return capacity;
	}
	
	public int fillCount() {
		return fillcount;
	}
	
	public boolean isEmpty() {
		if (fillcount == 0) {
			return true;
		}
		else {
			return false;
		}
	}
}