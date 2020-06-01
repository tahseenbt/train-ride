public class TrainNetwork {
	final int swapFreq = 2;
	TrainLine[] networkLines;

    public TrainNetwork(int nLines) {
    	this.networkLines = new TrainLine[nLines];
    }
    
    public void addLines(TrainLine[] lines) {
    	this.networkLines = lines;
    }
    
    public TrainLine[] getLines() {
    	return this.networkLines;
    }
    
    public void dance() {
    	System.out.println("The tracks are moving!");
    	//YOUR CODE GOES HERE
    	for (TrainLine line: this.networkLines) {
    		line.shuffleLine();
    	}
    }
    
    public void undance() {
    	//YOUR CODE GOES HERE
    	for (TrainLine line: this.networkLines) {
    		line.sortLine();
    	}
    }
    
    public int travel(String startStation, String startLine, String endStation, String endLine) {
    	TrainLine curLine = getLineByName(startLine);
    	TrainStation curStation;
    	int hoursCount = 0;
    	
    	try {
    	 //use this variable to store the current line.
    	curStation = curLine.findStation(startStation); //use this variable to store the current station. 
    	TrainStation end = getLineByName(endLine).findStation(endStation);
    	
    	
    	TrainStation prev = curStation;
    	System.out.println("Departing from "+startStation);
    	
    	//YOUR CODE GOES HERE
    	
    	
    	while(!(curStation.getName() == endStation)) {
    		
    		if(hoursCount == 168) {
    			System.out.println("Jumped off after spending a full week on the train. Might as well walk.");
    			return hoursCount;
    		}
    		
    		//prints an update on your current location in the network.
    		TrainStation next = curLine.travelOneStation(curStation, prev);
    		prev = curStation;
    		curStation = next;
    		curLine = curStation.getLine();
    		hoursCount ++;
	    	System.out.println("Traveling on line "+curLine.getName()+":"+curLine.toString());
	    	System.out.println("Hour "+hoursCount+". Current station: "+curStation.getName()+" on line "+curLine.getName());
	    	System.out.println("=============================================");
	    	if (hoursCount % 2 == 0 && hoursCount != 0) {
	    		this.dance();
	    	}
    	}
    	System.out.println("Arrived at destination after "+hoursCount+" hours!");
	    	return hoursCount;
    	}
    catch (StationNotFoundException e) {
		hoursCount = 168;
		return hoursCount;
	}
    }
    
    //you can extend the method header if needed to include an exception. You cannot make any other change to the header.
    public TrainLine getLineByName(String lineName) throws LineNotFoundException {
    	//YOUR CODE GOES HERE
    	for (TrainLine line: this.networkLines) {
    		if (line.getName().contentEquals(lineName)) {
    			return line;
    		}
    	}
    	throw new LineNotFoundException(lineName);
    }
    
  //prints a plan of the network for you.
    public void printPlan() {
    	System.out.println("CURRENT TRAIN NETWORK PLAN");
    	System.out.println("----------------------------");
    	for(int i=0;i<this.networkLines.length;i++) {
    		System.out.println(this.networkLines[i].getName()+":"+this.networkLines[i].toString());
    		}
    	System.out.println("----------------------------");
    }
}

//exception when searching a network for a LineName and not finding any matching Line object.
class LineNotFoundException extends RuntimeException {
	   String name;

	   public LineNotFoundException(String n) {
	      name = n;
	   }

	   public String toString() {
	      return "LineNotFoundException[" + name + "]";
	   }
	}