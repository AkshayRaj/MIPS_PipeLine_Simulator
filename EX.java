package simulator;

public class EX 
{
	 public static void execute(int instructionNum,int cycleNumber)
	 {
		 String executePath = "";
		 String instruction=Simulator.memory[instructionNum][1];
	        if(instruction.matches("MUL.D"))
	            executePath = "FP Mult";
	        else if(instruction.matches("DIV.D"))
	            	executePath = "FP Divide";
	        else if(instruction.matches("ADD.D") || instruction.matches("SUB.D") )
	            {
	            	executePath = "FP Add";
	            }
	        else if(instruction.matches("LW") || instruction.matches("SW") || 
	        		instruction.matches("L.D") || instruction.matches("S.D") )
	            {
	            	executePath = "Memory Instruction" ;                
	            }
	       	else
	       		executePath = "Integer Instruction";
		 
		 switch(executePath)
		 {
		 	case "Integer Instruction":  	Simulator.executeStage[instructionNum]=cycleNumber + 1-1;
		 									break;
		 	case "Memory Instruction":		Simulator.executeStage[instructionNum] = Simulator.cache.dAccess(instructionNum, cycleNumber) - 1;//cycleNumber + Simulator.memoryCycle -1;
		 									break;//Simulator.cache.dAccess(instructionNum, cycleNumber) - 1;	
		 	case "FP Add":					Simulator.executeStage[instructionNum]=cycleNumber + Simulator.FPadder -1;
		 									break;
		 	case "FP Mult":					Simulator.executeStage[instructionNum]=cycleNumber + Simulator.FPmultiplier -1;
		 									break;
		 	case "FP Divide":				Simulator.executeStage[instructionNum]=cycleNumber + Simulator.FPdivider -1;
											break;
		 }
		   			 	
	 }

}
