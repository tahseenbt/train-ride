import java.util.Arrays;
import java.util.Random;

public class TrainLine {

	private TrainStation leftTerminus;
	private TrainStation rightTerminus;
	private String lineName;
	private boolean goingRight;
	public TrainStation[] lineMap;
	public static Random rand;

	public TrainLine(TrainStation leftTerminus, TrainStation rightTerminus, String name, boolean goingRight) {
		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		this.lineMap = this.getLineArray();
	}

	public TrainLine(TrainStation[] stationList, String name, boolean goingRight)
	/*
	 * Constructor for TrainStation input: stationList - An array of TrainStation
	 * containing the stations to be placed in the line name - Name of the line
	 * goingRight - boolean indicating the direction of travel
	 */
	{
		TrainStation leftT = stationList[0];
		TrainStation rightT = stationList[stationList.length - 1];

		stationList[0].setRight(stationList[stationList.length - 1]);
		stationList[stationList.length - 1].setLeft(stationList[0]);

		this.leftTerminus = stationList[0];
		this.rightTerminus = stationList[stationList.length - 1];
		stationList[0].setLeftTerminal();
		stationList[stationList.length - 1].setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;

		for (int i = 1; i < stationList.length - 1; i++) {
			this.addStation(stationList[i]);
		}
	}

	public TrainLine(String[] stationNames, String name,
			boolean goingRight) {/*
									 * Constructor for TrainStation. input: stationNames - An array of String
									 * containing the name of the stations to be placed in the line name - Name of
									 * the line goingRight - boolean indicating the direction of travel
									 */
		TrainStation leftTerminus = new TrainStation(stationNames[0]);
		TrainStation rightTerminus = new TrainStation(stationNames[stationNames.length - 1]);

		leftTerminus.setRight(rightTerminus);
		rightTerminus.setLeft(leftTerminus);

		this.leftTerminus = leftTerminus;
		this.rightTerminus = rightTerminus;
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		this.leftTerminus.setTrainLine(this);
		this.rightTerminus.setTrainLine(this);
		this.lineName = name;
		this.goingRight = goingRight;
		for (int i = 1; i < stationNames.length - 1; i++) {
			this.addStation(new TrainStation(stationNames[i]));
		}

		this.lineMap = this.getLineArray();

	}

	// adds a station at the last position before the right terminus
	public void addStation(TrainStation stationToAdd) {
		TrainStation rTer = this.rightTerminus;
		TrainStation beforeTer = rTer.getLeft();
		rTer.setLeft(stationToAdd);
		stationToAdd.setRight(rTer);
		beforeTer.setRight(stationToAdd);
		stationToAdd.setLeft(beforeTer);

		stationToAdd.setTrainLine(this);

		this.lineMap = this.getLineArray();
	}

	public String getName() {
		return this.lineName;
	}

	public int getSize() {
		TrainStation current = this.getLeftTerminus();
		int count = 1;
		while (!current.isRightTerminal()) {
			count++;
			current = current.getRight();
		}
		// YOUR CODE GOES HERE
		return count; // change this!
	}

	public void reverseDirection() {
		this.goingRight = !this.goingRight;
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation travelOneStation(TrainStation current, TrainStation previous) throws StationNotFoundException {

		// YOUR CODE GOES HERE
		if ((current.hasConnection && current.getTransferStation().equals(previous)) || !current.hasConnection) {
			return this.getNext(current);
		}
		else {
			return current.getTransferStation();
		}
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation getNext(TrainStation station) throws StationNotFoundException{

		// YOUR CODE GOES HERE
		this.findStation(station.getName());
		if ((station.isLeftTerminal() && !goingRight) || (station.isRightTerminal()) && goingRight) {
			this.reverseDirection();
		}
		if (this.goingRight) {
			return station.getRight();
		}
		else {
			return station.getLeft();
		}
	}

	// You can modify the header to this method to handle an exception. You cannot make any other change to the header.
	public TrainStation findStation(String name) throws StationNotFoundException {

		// YOUR CODE GOES HERE
		TrainStation current = this.getLeftTerminus();
		while (current!= null) {
			if (current.getName() == name) {
				return current;
			}
			current = current.getRight();
			}
		if (current.getName() == name) {
			return current;
		}
		throw new StationNotFoundException(name);
	}

	public void sortLine() {
		TrainStation[] array = this.getLineArray();
		int len = array.length;
		for (int i = 0; i<len; i++) {
			for (int j = 1; j<len-i; j++) {
				if (array[j-1].getName().compareTo(array[j].getName()) > 0) {
					this.swap(array, j-1);
				}
			}
		}
		this.shuffleFixer(array);
		this.lineMap = this.getLineArray();
	}

	public TrainStation[] getLineArray() {
		TrainStation[] ret = new TrainStation[this.getSize()];
		TrainStation current = this.getLeftTerminus();
		for (int i = 0; i<this.getSize(); i++) {
			ret[i] = current;
			current = current.getRight();
		}
		return ret; // change this
	}

	private TrainStation[] shuffleArray(TrainStation[] array) {
		Random rand = new Random();

		for (int i = 0; i < array.length; i++) {
			int randomIndexToSwap = rand.nextInt(array.length);
			TrainStation temp = array[randomIndexToSwap];
			array[randomIndexToSwap] = array[i];
			array[i] = temp;
		}
		return array;
	}

	public void shuffleLine() {

		// you are given a shuffled array of trainStations to start with
		TrainStation[] lineArray = this.getLineArray();
		TrainStation[] shuffledArray = shuffleArray(lineArray);

		// YOUR CODE GOES HERE
			
		this.shuffleFixer(shuffledArray);
		
		this.lineMap = this.getLineArray();
	}

	public String toString() {
		TrainStation[] lineArr = this.getLineArray();
		String[] nameArr = new String[lineArr.length];
		for (int i = 0; i < lineArr.length; i++) {
			nameArr[i] = lineArr[i].getName();
		}
		return Arrays.deepToString(nameArr);
	}

	public boolean equals(TrainLine line2) {

		// check for equality of each station
		TrainStation current = this.leftTerminus;
		TrainStation curr2 = line2.leftTerminus;

		try {
			while (current != null) {
				if (!current.equals(curr2))
					return false;
				else {
					current = current.getRight();
					curr2 = curr2.getRight();
				}
			}

			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public TrainStation getLeftTerminus() {
		return this.leftTerminus;
	}

	public TrainStation getRightTerminus() {
		return this.rightTerminus;
	}
	
	private void swap(TrainStation[] t, int i) {
		TrainStation temp = t[i];
		t[i] = t[i+1];
		t[i+1] = temp;
	}
	
	private void shuffleFixer(TrainStation[] arr) {
		arr[arr.length-1].setNonTerminal();
		arr[0].setNonTerminal();
		this.leftTerminus = arr[0];
		this.rightTerminus = arr[arr.length-1];
		this.leftTerminus.setLeftTerminal();
		this.rightTerminus.setRightTerminal();
		arr[arr.length-1].setLeft(arr[0]);
		arr[0].setRight(arr[arr.length-1]);
		arr[arr.length-1].setRight(null);
		arr[0].setLeft(null);
	for (int i = 1; i < arr.length-1; i++) {
		arr[i].setNonTerminal();
		this.addStation(arr[i]);
		}
	}
}

//Exception for when searching a line for a station and not finding any station of the right name.
class StationNotFoundException extends RuntimeException {
	String name;

	public StationNotFoundException(String n) {
		name = n;
	}

	public String toString() {
		return "StationNotFoundException[" + name + "]";
	}
}
