package simulator;

public class WB 
{
	 public static void writeBack(int instructionNum,int cycle)
	    {  
		 	if(Hazard.checkWAR(instructionNum,cycle))
	        {
		 		Simulator.writeBackStage[instructionNum] = cycle;
	            PipeLine.integerUnit(instructionNum);
	            Locks.releaseResource(instructionNum);
	        }
	    }

}
