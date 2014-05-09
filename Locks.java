package simulator;

public class Locks 
{

    public static void occupyResource(int instructionNum)
    {
    	String instruction=Simulator.memory[instructionNum][1];
        if(instruction.matches("ADD.D") || instruction.matches("SUB.D"))
        {
        	if(Simulator.isFPadderPipe)
                Simulator.FPadder--;
        	else
        	 	Simulator.FPadder = 0;
        }
        else if(instruction.matches("MUL.D"))
        {
        	if(Simulator.isFPmultiplierPipe)
                Simulator.FPmultiplier--;
        	else
        	 	Simulator.FPmultiplier = 0;
        }
        else if(instruction.matches("DIV.D"))
        {
        	if(Simulator.isFPdividerPipe)
        		Simulator.FPdivider--;
    	 	else
    	 		Simulator.FPdivider = 0;
        }
        else if(instruction.matches("HLT")||instruction.matches("BNE")
        		||instruction.matches("BEQ")||instruction.matches("J"))
        {
        
        }
        else
        {
        	Simulator.integerUnit--;
        }
    }

    public static void releaseResource(int instructionNum)
    {
    	String instruction=Simulator.memory[instructionNum][1];
        if(instruction.matches("ADD.D") || instruction.matches("SUB.D"))
        {
        	if(Simulator.isFPadderPipe)
                Simulator.FPadderInc++;
        	else
        	 	Simulator.FPadderInc = Simulator.FPadd;
        }
        else if(instruction.matches("MUL.D"))
        {
        	if(Simulator.isFPmultiplierPipe)
        		Simulator.FPmultiplierInc++;
     	 	else
     	 		Simulator.FPmultiplierInc = Simulator.FPmult;
        }
        else if(instruction.matches("DIV.D"))
        {
        	if(Simulator.isFPdividerPipe)
        		Simulator.FPdividerInc++;
     	 	else
     	 	Simulator.FPdividerInc = Simulator.FPdiv;
        }
        else if(instruction.matches("HLT")||instruction.matches("BNE")
        		||instruction.matches("BEQ")||instruction.matches("J"))
        {

        }
        else
        {
        	Simulator.integerUnitInc++;
        }
    }



}
