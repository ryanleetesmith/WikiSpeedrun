import java.util.List;

/* 
 * Author: Ryan Smith
 * File: MaxPQ.java
 * 
 * Purpose: Priority queue (Max bin heap) backed by an array. Used to store
 * every ladder we create in WikiRacer.java and sort them based on priority (common links). 
 * This makes it so our WikiRacer searches through pages more closely related to the goal.
 * 
 * Usage: WikiRacer does the work! Just sit back and relax.
 */
public class MaxPQ {

    private int maxSize = 10;
	private Ladder[] array;
    private int curSize;

    /*
     * this constructor creates new instance of the MaxPQ class
     * with no parameters.
     */
    public MaxPQ() {
		this.array = new Ladder[maxSize];
        this.array[0] = null;
		this.curSize = 0;
    }

    /*
     * This private helper method increases the size of the array backing
     * our priority queue by copying all of the elements from the previous array
     * to a new one with twice the capacity.
     */
    private void growArray() {
    	// double array size
        this.maxSize = 2 * maxSize;
		Ladder[] newArray = new Ladder[maxSize];
		// copy over elements
		for (int i = 0; i <= curSize; i++) {
            newArray[i] = array[i];
        }
        this.array = newArray;
    }

    /*
	 * this private helper method performs the bubble up operation required by the
	 * change priority and enqueue methods to ensure or priority follows the
	 * criteria of a binary max heap. Takes an integer representing the index that
	 * gets bubbled up within the array.
	 */
    private void bubbleUp(int i) {
        int p = i / 2;
		while (p > 0 && array[p].priority < array[i].priority) {
			Ladder temp = array[i];
            array[i] = array[p];
            array[p] = temp;
            i = p;
            p = p / 2;
        }        
    }
    
    /*
	 * this private helper method performs the bubble down operation required by the
	 * change priority and dequeue methods to ensure or priority follows the
	 * criteria of a binary max heap. Takes in an integer representing an index to
	 * bubble down within our array.
	 */
    private void bubbleDown(int parentIndex) {
        int childIndex = 2 * parentIndex;
        int childIndex2 = 2 * parentIndex + 1;
        int compare = 0;
        
        if (childIndex2 < curSize
				&& array[childIndex].priority < array[childIndex2].priority) {
            compare = childIndex2;
        } else if (childIndex < curSize
				&& array[childIndex2].priority < array[childIndex].priority) {
            compare = childIndex;
        } else {
            return;
        }

		if (array[parentIndex].priority < array[compare].priority) {
			Ladder temp = array[parentIndex];
            array[parentIndex] = array[compare];
            array[compare] = temp;
            bubbleDown(compare);
        } else {
            return;
        }
    }

    /*
	 * This method takes in a list representing a ladder and the priority for that
	 * ladder within our priority queue, and creates a new Ladder object with this
	 * information. It then adds this information to the priority queue and bubbles
	 * up
	 */
	public void enqueue(List<String> content, int priority) {
		// make a new ladder with the info weve been given
		Ladder newGuy = new Ladder(content, priority);
		if (curSize >= maxSize - 1) {
			growArray();
		}
		// add this ladder to the end and bubble up to retain heap-ness
		this.curSize += 1;
		array[curSize] = newGuy;
		bubbleUp(this.curSize);
    }

    /*
	 * This method removes the frontmost Ladder object from our priority queue and
	 * returns the List holding the ladder itself. It also decrements the size field
	 * and bubbles down using the index 1 to ensure our priority queue still follows
	 * the criteria of binary max heap.
	 */
	public List<String> dequeue() {
		if (curSize == 0) {
			System.out.println("attempting to dequeue empty queue, ret null");
			return null;
        }
		// grab frontmost element ( not zero indexed)
		List<String> retVal = array[1].content;
		// take last element and bubble down
		array[1] = array[curSize];
        this.curSize -= 1;
        bubbleDown(1);
        return retVal;
    }

    /*
     * This method returns an integer representing the size of our priority
     * queue
     */
    public int size() {
		return this.curSize;
    }

	/* This method checks if the priority queue is empty using the size field */
	public boolean isEmpty() {
		return (curSize == 0);
	}

    /*
	 * This method returns a string representation of our priority queue. This
	 * string contains the each ladder stored within the queue and their priorities.
	 * Utilized mostly for debugging.
	 */
    public String toString() {
        String result = "{";
		for (int i = 1; i <= curSize; i++) {
			result += array[i].content + " (" + array[i].priority + ")";
			if (i < curSize) {
                result += ", ";
            }
        }
        result += "}";
        return result;
    }

	/*
	 * This class helps organize the max bin heap by giving each Ladder a priprity.
	 */
	public class Ladder {

		public List<String> content; // sequence of pages we have taken so far
		public int priority; // number of links the last entry in the ladder has in common with end

		public Ladder(List<String> content, int priority) {
            this.content = content;
            this.priority = priority;
        }

		// for testing
		public String toString() {
			return content + " (" + priority + ")";
		}

	}

}