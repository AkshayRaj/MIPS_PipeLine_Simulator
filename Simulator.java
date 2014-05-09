/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;

/**
 *
 * @author Kore
 */
public class Simulator 
{

public static int memoryData[]=new int[65];
public static String memory[][];
public static int fetchStage[];
public static int decodeStage[];
public static int readOperand[];
public static int executeStage[];
public static int writeBackStage[];
public static String isRAW[];
public static String isWAW[];
public static String isWAR[];
public static String isSTRUC[];
public static int regOperand1[];
public static int regOperand2[];
public static int instructionNum;//number of instructions
public static int FPadder=0;
public static int FPmultiplier=0;
public static int FPdivider=0;
public static int integerUnit=1;
public static int FPadderInc=0;
public static int FPmultiplierInc=0;
public static int FPdividerInc=0;
public static int integerUnitInc=0;
public static int cycleNum=1;
public static int start=0;
public static boolean notHLT=true;
public static Cache cache=new Cache();
public static String instFile,regFile,dataFile,configFile,resultfile;
public static String commandArguments[]=new String[6];
public static boolean isFPadderPipe;
public static boolean isFPmultiplierPipe;
public static boolean isFPdividerPipe;
public static int memoryCycle;
public static int IcacheCycle;
public static int DcacheCycle;
public static int FPadd;
public static int FPmult;
public static int FPdiv;

    public static void main(String[] args) throws FileNotFoundException, IOException 
    {
         Scoreboard.getCommandLine();//command
         Scoreboard.initializeScoreboard();
         PipeLine.initializePipeLine(instFile);
         Scoreboard.readConfigFile(configFile);
         Scoreboard.readRegMem(regFile, dataFile);
         Scoreboard.readInst(instFile);

         Simulator.startPipeLine();
       
         Scoreboard.writeOnScoreBoard(start, instructionNum-1);
         Scoreboard.fillScoreboard.format("%n%nTotal number of access requests for instruction cache: %d %n", cache.access_Icache-1);
         Scoreboard.fillScoreboard.format("Number of instruction cache hits:  %d %n", cache.cacheHit_I-1);
         Scoreboard.fillScoreboard.format("Total number of access requests for data cache:  %d %n", cache.access_Dcache);
         Scoreboard.fillScoreboard.format("Number of data cache hits:  %d %n", cache.cacheHit_D );
         Scoreboard.fillScoreboard.close();
    
    }
    
    public static void startPipeLine() throws FileNotFoundException, IOException
    {
    	try
    	{
    		while(notHLT)
    		{
    			notHLT=false;

    			for(int i=0;i<FPadderInc;i++)
    			{
    				FPadder++;
    			}
    			for(int i=0;i<FPmultiplierInc;i++)
    			{
    				FPmultiplier++;
    			}
    			for(int i=0;i<FPdividerInc;i++)
    			{
    				FPdivider++;
    			}
    			for(int i=0;i<integerUnitInc;i++)
    			{
    				integerUnit++;
    			}
    			FPadderInc=0;
    			FPmultiplierInc=0;
    			FPdividerInc=0;
    			integerUnitInc=0;

    			for(int i=start;i<instructionNum;i++)
    			{
    				if(decodeStage[i]==0)
    				{
    					notHLT=true;
    					if(fetchStage[i]<cycleNum &&fetchStage[i]!=0)
    					{
    						if(i==start)
    						{
    							ID.decode(start, cycleNum);
    							break;
    						}
    						else
    							if(decodeStage[i-1]!=0 && decodeStage[i-1]!=cycleNum)
    							{
    								ID.decode(i, cycleNum);
    								break;
    							}
    					}
    				}
    			}
    			
    			for(int i=start;i<instructionNum;i++)
    			{
    				if(fetchStage[i]==0)
    				{
    					notHLT=true;
    					if(i==start)
    					{
    						IF.fetch(cycleNum, start);
    						break;
    					}
    					else if(decodeStage[i-1]!=0 && decodeStage[i-1]<=cycleNum)
    					{
    						IF.fetch(cycleNum,i);
    						break;
    					}
    				}
    			}

    			for(int i=start;i<instructionNum;i++)
    			{
    				if(readOperand[i]==0 && decodeStage[i]!=0 && decodeStage[i]<cycleNum )
    				{
    					ID.checkRegOperands(i, cycleNum);
    					notHLT=true;
    				}
    			}

    			for(int i=start;i<instructionNum;i++)
    			{
    				if (readOperand[i]!=0 && readOperand[i]<cycleNum && executeStage[i]==0 )
    				{
    					notHLT=true;
    					EX.execute(i,cycleNum);
    				}
    			}

    			for(int i=start;i<instructionNum;i++)
    			{
    				if(executeStage[i]!=0 && executeStage[i]<cycleNum && writeBackStage[i]==0)
    				{
    					WB.writeBack(i, cycleNum);
    					notHLT=true;
    				}
    			}
    			cycleNum++;

    		}//END WHILE
    	}
    	catch(FileNotFoundException noFile)
    	{
    		noFile.printStackTrace();
    	}
    	catch(IOException io)
    	{
    		io.printStackTrace();
    	}
    }



    
    
    
    
}
