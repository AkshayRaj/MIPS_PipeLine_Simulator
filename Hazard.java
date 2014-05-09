package simulator;

public class Hazard
{
	public static boolean checkStructural(int instructionNum)
    {
		String instruction=Simulator.memory[instructionNum][1];
        if(instruction.matches("ADD.D") || instruction.matches("SUB.D"))
            {
        		if(Simulator.FPadder>0)
        			return true;
        		else
                    {
        				Simulator.isSTRUC[instructionNum]="Y";
        				return false;
                    }
             }
        else if(instruction.matches("MUL.D"))
        {
        	if(Simulator.FPmultiplier>0)
        		return true;
            else
            {
            	Simulator.isSTRUC[instructionNum]="Y";
                return false;
            }
        }
        
        else if(instruction.matches("DIV.D"))
        {
        	if(Simulator.FPdivider>0)
        		return true;
        	else
            {
        		Simulator.isSTRUC[instructionNum]="Y";
                return false;
            }
        }
        else
        {
        	if(instruction.matches("HLT")||instruction.matches("BNE")
        			||instruction.matches("BEQ")||instruction.matches("J"))
        	{
        		return true;
        	}
        	else
        	{
        		if (Simulator.integerUnit > 0)
        			return true;
        		else
        		{
        			Simulator.isSTRUC[instructionNum]="Y";
        			return false;
        		}
        	}
        }
    }

    public static boolean checkWAW(int instructionNum,int cycle)
    {
    	for(int i=0;i<instructionNum;i++)
        {
    		if((Simulator.memory[i][2].matches(Simulator.memory[instructionNum][2])) 
    				&& (Simulator.writeBackStage[i]==0 || Simulator.writeBackStage[i]==cycle))
    		{
    			Simulator.isWAW[instructionNum]="Y";
    			return false;
    		}
        }
    	return true;
    }
    
    public static boolean checkRAW(int instructionNum,int cycle)
    {
    	for(int i=0;i<instructionNum;i++)
        {
    		if(((Simulator.memory[i][2].matches(Simulator.memory[instructionNum][3]))
        	  ||(Simulator.memory[i][2].matches(Simulator.memory[instructionNum][4])))
        		 && (Simulator.writeBackStage[i]==0 || Simulator.writeBackStage[i]==cycle))
    		{ 
    			Simulator.isRAW[instructionNum]="Y";
    			return false;
    		}
        }
    	return true;
    }

    public static boolean checkWAR(int instructionNum,int cycle)
    {
    	for(int i=0;i<instructionNum;i++)
        {
    		if(((Simulator.memory[i][3].matches(Simulator.memory[instructionNum][2]))
    			||(Simulator.memory[i][4].matches(Simulator.memory[instructionNum][2]))) 
    			&&(Simulator.readOperand[i]==0 || Simulator.readOperand[i]==cycle))
    		{  
    			Simulator.isWAR[instructionNum]="Y";
    			return false;
    		}
        }
    	return true;
    }

}
