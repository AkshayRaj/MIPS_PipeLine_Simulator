package simulator;

public class IF 
{
	public static void fetch(int cycle,int instructionNum)
	{
		Simulator.fetchStage[instructionNum]= Simulator.cache.iAccess(instructionNum,cycle);//cycleNum;
	}
	

}
