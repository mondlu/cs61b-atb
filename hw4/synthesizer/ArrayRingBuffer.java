// Make sure to make this class a part of the synthesizer package
package synthesizer;
public class ArrayRingBuffer extends AbstractBoundedQueue {
  /* Index for the next dequeue or peek. */
  private int first;           
  /* Index for the next enqueue. */
  private int last;             
  /* Array for storing the buffer data. */
  private double[] rb;

  /** Create a new ArrayRingBuffer with the given capacity. */
  public ArrayRingBuffer(int capacity) {
    // TODO: Create new array with capacity elements.
    //       first, last, and fillCount should all be set to 0. 
    //       this.capacity should be set appropriately. Note that the local variable
    //       here shadows the field we inherit from AbstractBoundedQueue.
	rb = new double[capacity];
	this.first = 0;
	this.last = 0;
	this.fillcount = 0;
	this.capacity = capacity;
  }

  /** Adds x to the end of the ring buffer. If there is no room, then
    * throw new RuntimeException("Ring buffer overflow") 
    */
  public void enqueue(double x) {
    // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
    // is there room?
	if (this.isFull()) {
		throw new RuntimeException("Ring buffer overflow");
	}
	else {
		rb[last] = x;
		if (last == this.capacity - 1) {
			this.last = 0;
		}
		else {
			this.last = this.last + 1;
		}
		this.fillcount = this.fillcount + 1;		
	}
  }

  /** Dequeue oldest item in the ring buffer. If the buffer is empty, then
    * throw new RuntimeException("Ring buffer underflow");
    */
  public double dequeue() {
    // TODO: Dequeue the first item. Don't forget to decrease fillCount and update first.
	if (this.isEmpty()) {
		throw new RuntimeException("Ring buffer underflow");
	}
	else {
		double temp = rb[first];
		rb[first] = 0;
		if (first == this.capacity - 1) {
			this.first = 0;	
		}
		else{
			this.first = this.first + 1;
			
		}
		this.fillcount = this.fillcount - 1;
		return temp;
	}
  }

  /** Return oldest item, but don't remove it. */
  public double peek() {
    // TODO: Return the first item. None of your instance variables should change.
	if (this.isEmpty()) {
		throw new RuntimeException("Ring buffer underflow");
	}
	else {
		return rb[first];
	}
  }

}
