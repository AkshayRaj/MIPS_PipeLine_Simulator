/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.Scanner;


/**
 *
 * @author Kore
 */
public class PipeLine
{

//----------------------------initializePipeLine(inst.txt)------------------------------------------------------------------
//--------------------Initializes PipeLine Stages, Hazard variables---------------------------------------------------            
       public static void initializePipeLine(String path) throws FileNotFoundException
       {
       	Scanner scan=new Scanner(new FileInputStream(path));
       	int count=0;
       	String line;
       	while(scan.hasNext())
       	{
       		line=scan.nextLine();
               if(line.isEmpty()==false)
               count++;
       	}
       	Simulator.memory=new String[count][5];
       	Simulator.fetchStage=new int[count];
       	Simulator.decodeStage=new int[count];
       	Simulator.readOperand=new int[count];
       	Simulator.executeStage=new int[count];
       	Simulator.writeBackStage=new int[count];
       	Simulator.isRAW=new String[count];
       	Simulator.isWAW=new String[count];
       	Simulator.isWAR=new String[count];
       	Simulator.isSTRUC=new String[count];
       	Simulator.regOperand1=new int [count];
       	Simulator.regOperand2=new int [count];
       	Simulator.instructionNum=count;//number of instructions

       	for(int i=0;i<Simulator.instructionNum;i++)
       	{
       		Simulator.fetchStage[i]=0;
       		Simulator.decodeStage[i]=0;
       		Simulator.readOperand[i]=0;
       		Simulator.executeStage[i]=0;
       		Simulator.writeBackStage[i]=0;
       		Simulator.isWAW[i]="N";
       		Simulator.isRAW[i]="N";
       		Simulator.isWAR[i]="N";
       		Simulator.isSTRUC[i]="N";
       	}

       }

 //----------------------------------initializePipeLine(inst.txt)------------------------------------------------------------
 //----------------------------------------END-------------------------------------------------------------------------

//---------------------------returnAddInReg()------------------------------------------------------------------------    
    public static int returnAddInReg(String code)
    {
    	String temp[]=new String[2];
        if(code.contains("(R"))
        {
        	temp=code.split("R");
        	int i=Integer.valueOf(temp[0].substring(0,temp[0].length()-1));
        	int j=Integer.valueOf(temp[1].substring(0,temp[0].length()-1));
        	return ((i+Simulator.memoryData[j])-224);
        }
        else if(code.contains("R"))
        {
        	temp=code.split("R");
        	return Integer.valueOf(temp[1]);
        }
        else try
        {
        	Simulator.memoryData[64]=Integer.valueOf(code);
        	return 64;
        }
        catch(NumberFormatException e)
        {
        	return 64;
        }
    
    }
//---------------------------returnAddInReg() END -------------------------------------------------------------------
//-----------------------------------integerUnit() ------------------------------------------------------------------    
public static void integerUnit(int instructionNum)
    {
		String code=Simulator.memory[instructionNum][1];
		if(code.contains("LW"))
		{
			Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])]=
					Simulator.memoryData[Simulator.regOperand1[instructionNum]];
		}
		else if(code.contains("SW"))
		{
			Simulator.memoryData[Simulator.regOperand1[instructionNum]]=
					Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])];
		}
		else if(code.contains("DADD"))
		{
			Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])]=
					Simulator.regOperand1[instructionNum]+Simulator.regOperand2[instructionNum];
		}
		else if(code.contains("DSUB"))
		{
			Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])]=
					Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][3])]
							-Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][4])];

		}
		else if(code.contains("AND"))
		{
			BigInteger a=new BigInteger(String.valueOf( Simulator.regOperand1[instructionNum]));
			BigInteger b=new BigInteger(String.valueOf(Simulator.regOperand2[instructionNum]));
			Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])]=a.and(b).intValue();
		}
		else if(code.contains("OR"))
		{
			BigInteger a=new BigInteger(String.valueOf( Simulator.regOperand1[instructionNum]));
			BigInteger b=new BigInteger(String.valueOf(Simulator.regOperand2[instructionNum]));
			Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][2])]=a.or(b).intValue();
		}

    }
//------------------------------------integerUnit() END -------------------------------------------------------------

}
