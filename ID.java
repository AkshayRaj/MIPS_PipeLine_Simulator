package simulator;

import java.io.FileNotFoundException;
import java.nio.channels.Pipe;

public class ID extends PipeLine
{
	public static int branchCycle,jumpCycle=0;

//--------------------------------------decode()--------------------------------------------------------------------
	public static void decode(int instructionNum,int cycle) throws FileNotFoundException
    {
        if(Hazard.checkStructural(instructionNum) && Hazard.checkWAW(instructionNum,cycle))
        {
            Simulator.decodeStage[instructionNum] = cycle;
            Locks.occupyResource(instructionNum);
        
            if(Simulator.memory[instructionNum][1].matches("HLT") 
               ||Simulator.memory[instructionNum][1].matches("J"))
            {
            	Simulator.readOperand[instructionNum]=-1;
            	Simulator.executeStage[instructionNum]=-1;
            	Simulator.writeBackStage[instructionNum]=-1;
            	Locks.releaseResource(instructionNum);
            	if( Simulator.memory[instructionNum][1].matches("J") )
            	{
            		if(jumpCycle==0)
        			jumpCycle=cycle;
        		
            		for(int i=Simulator.start;i<instructionNum;i++)
            		{
            			if (Simulator.writeBackStage[i] == 0)
            			{
            				Simulator.decodeStage[instructionNum]=0;
            				return;
            			}
            		}
            		Simulator.decodeStage[instructionNum]=jumpCycle;
            		jumpCycle=0;
            		Scoreboard.onBranch(instructionNum);
            	}
            }
        }
    }
//--------------------------------------decode() END---------------------------------------------------------------	
//--------------------------------------checkRegOperands()---------------------------------------------------------	
    public static void checkRegOperands(int instructionNum,int cycle) throws FileNotFoundException
    {
        if(Hazard.checkRAW(instructionNum,cycle))
        {
        	String instruction=Simulator.memory[instructionNum][1];
        	if(instruction.matches("LW") || instruction.matches("SW") 
        		|| instruction.matches("L.D") || instruction.matches("S.D"))
        	{
        		Simulator.regOperand1[instructionNum]=
        				PipeLine.returnAddInReg(Simulator.memory[instructionNum][3]);
        		Simulator.regOperand2[instructionNum]=
        				(PipeLine.returnAddInReg(Simulator.memory[instructionNum][4])%256)+32;
        	}
        	else
        	{
                Simulator.regOperand1[instructionNum]=
                		Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][3])];
                Simulator.regOperand2[instructionNum]=
                		Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][4])];
            }
           
            if(Simulator.memory[instructionNum][1].matches("BEQ") || 
            		Simulator.memory[instructionNum][1].matches("BNE"))
            {
            	branchCycle=cycle;
                
            	if(Simulator.writeBackStage[instructionNum+1]!=0 )
                {
            		Simulator.executeStage[instructionNum] = -1;
            		Simulator.writeBackStage[instructionNum]=-1;

            		for(int i=Simulator.start;i<=instructionNum;i++)
                    {
            			if(Simulator.writeBackStage[i]==0)
                        return;
                    }
            		Simulator.readOperand[instructionNum] = branchCycle;
            		Scoreboard.onBranch(instructionNum);
                }
            }
            else
            Simulator.readOperand[instructionNum] = cycle;
                    
        }
    }

//--------------------------------------checkRegOperands() ENd-------------------------------------------------		
}
