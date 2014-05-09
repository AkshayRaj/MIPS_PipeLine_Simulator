package simulator;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;




public class Scoreboard
{
	static Formatter fillScoreboard;
//-------------------------------------------INST.TXT-----------------------------------------------------------------	
	public static void readInst(String path) throws FileNotFoundException
    {Scanner scan=new Scanner(new FileInputStream(path));
     int count=0;
     String line;
    while(scan.hasNext()){
               line=scan.nextLine();
               if(line.isEmpty()==false)
               {
                   Simulator.memory[count] = Scoreboard.filterInstructionFromFile(line);
               count++;
                 }
                         }
     scan.close();
    }
 //---------------------------------------REG.TXT-----------------------------------------------------------------------------   
	public static void readRegMem(String pathReg,String pathData) throws FileNotFoundException
    {Scanner scan=new Scanner(new FileInputStream(pathReg));
     int count=0;
    while(scan.hasNext()){
               int i=1;
               int data=0;
               String temp;
               temp=scan.nextLine();
               if(temp.isEmpty()==false)
               {
                   for (int x = 31; x >= 0; x--)
                        {
                        data=data + Integer.parseInt(String.valueOf(temp.charAt(x))) * i;
                        i=i*2;
                        }
              // Simulator.register[count]= data;
               Simulator.memoryData[count]=data;
               count++;
                         }}
     scan.close();

     scan=new Scanner(new FileInputStream(pathData));
     count=32;
    while(scan.hasNext()){
               int i=1;
               int data=0;
               String temp;
               temp=scan.nextLine();
               if(temp.isEmpty()==false)
               {
                    for(int x=31;x>=0;x--)
                        {
                        data=data + Integer.parseInt(String.valueOf(temp.charAt(x))) * i;
                        i=i*2;
                        }
               Simulator.memoryData[count]= data;
               count++;
            }
        }
     scan.close();

    }
//---------------------------------------REG.TXT-----------------------------------------------------------------------------  
	
//------------------------CONFIG.TXT----------------------------------------------------------------------------------	
	public static void readConfigFile(String path) throws FileNotFoundException
    {
        
		 try {

                Scanner scanConfig=new Scanner(new FileInputStream(path));
                String configString = "";//the final string

                while (scanConfig.hasNextLine()) {
                    String line = scanConfig.nextLine();
                    configString = configString + line +"," ;
                }
                 scanConfig.close();
                 parseConfigFile(configString);
                 //System.out.println("Config File" +configString);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }       

    }
    public static void parseConfigFile(String fileText) 

	{
		String array[] = new String[100];
		
		fileText = fileText.replace(',',':');
		array = fileText.split(":");

		for(int i=0;i<array.length;i++)
		{
			String key = array[i].trim().toUpperCase();
			switch (key) 
			{
				case "FP ADDER":
					i++;
					Simulator.FPadder = Integer.parseInt(array[i].trim());
					Simulator.FPadd = Simulator.FPadder;
					System.out.println("FP Adder : "+Simulator.FPadder);
					i++;
					array[i] = array[i].trim();
					Simulator.isFPadderPipe = array[i].equalsIgnoreCase("yes");
					break;

				case "FP MULTIPLIER":
					i++;
					Simulator.FPmultiplier = Integer.parseInt(array[i].trim());
					Simulator.FPmult = Simulator.FPmultiplier;
					System.out.println("FP Multiplier : "+Simulator.FPmultiplier);
					i++;
					array[i] = array[i].trim();
					Simulator.isFPmultiplierPipe = array[i].equalsIgnoreCase("yes");
					break;

				case "FP DIVIDER":
					i++;
					Simulator.FPdivider = Integer.parseInt(array[i].trim());
					Simulator.FPdiv = Simulator.FPdivider;
					System.out.println("FP divider : "+Simulator.FPdivider);
					i++;
					array[i] = array[i].trim();
					Simulator.isFPdividerPipe = array[i].equalsIgnoreCase("yes");
					break;

				case "MAIN MEMORY":
					i++;
					Simulator.memoryCycle = Integer.parseInt(array[i].trim());
					System.out.println("Memory Cycles : "+Simulator.memoryCycle);
					break;

				case "I-CACHE":
					i++;
					Simulator.IcacheCycle = Integer.parseInt(array[i].trim());
					System.out.println("I Cache Cycle : "+Simulator.IcacheCycle);
					break;

				case "D-CACHE":
					i++;
					Simulator.DcacheCycle = Integer.parseInt(array[i].trim());
					System.out.println("D Cache Cycle : "+Simulator.DcacheCycle);
					break;
					
					default:
					//throw exception and exit?
					break;
				}
			}
		}
    //------------------------------------- GET COMMANDLINE ARGUMENTS--------------------------------------------------------
     public static void  getCommandLine()
     {
    	 Boolean com=true;
         while(com)
         {
        	 System.out.println("Enter simulator command(simulator inst.txt data.txt reg.txt config.txt result.txt):");
        	 Scanner scancommand=new Scanner(System.in);
        	 String command=scancommand.nextLine();
        	 String comm[]=new String[6];
        	 comm=command.split(" ");

        	 if(comm[0].matches("simulator"))
        	 {
        		 for(int i=1;i<6;i++)
        		 {
        			 if (comm[i].contains(".txt") == false)
        			 {
        				 System.out.println("Arguments should be .txt file");
        				 break;
        			 }
        			 else
        			 {
                        if(i==5)
                        {
                            com = false;
                            Simulator.instFile=comm[1];
                            Simulator.dataFile=comm[2];
                            Simulator.regFile=comm[3];
                            Simulator.configFile=comm[4];
                            Simulator.resultfile=comm[5];
                        }
        			 }
        		 }
        	 }
        	 else
        	 {
        		 System.out.println("wrong command");
        	 }
         }
     }
//-----------------------------------------COMMANDLINE ARGUMENTS---------------------------------------------------
//-----------------------------------------------END---------------------------------------------------------------  
     public static void initializeScoreboard() throws IOException
     {
    	 fillScoreboard=new Formatter(Simulator.resultfile);
    	 fillScoreboard.format("\t\t%s\t\t\t\t%s    %s\t   %s    %s     %s     %s     %s   %s%n",
    			 "Instruction","Fetch","Read","Execute","Write","RAW","WAR","WAW","Structural");
    	 
     }
//-------------------------------------------------writeOutput()------------------------------------------------------------------
    
     public static void writeOnScoreBoard(int start,int end) throws FileNotFoundException
     {
     	for(int i=start;end>=i;i++)
     	{
     		String string="";
     		if(Simulator.memory[i][0].matches("")==false)
     		{
     			string = string + Simulator.memory[i][0];
     			for(int j=0;j<7-Simulator.memory[i][0].length();j++)
     				string=string+" ";
     			string=string+":";
     		}
     		else
     		string=string+"        ";
     		
     		string = string + Simulator.memory[i][1];
             for(int j=0;j<7-Simulator.memory[i][1].length();j++)
             	string=string+" ";
             
             string = string + Simulator.memory[i][2];
             for(int j=0;j<5-Simulator.memory[i][2].length();j++)
             	string=string+" ";
             
             if(Simulator.memory[i][3].matches("")==false)
             {
             	string = string + ","+Simulator.memory[i][3];
             	for(int j=0;j<5-Simulator.memory[i][3].length();j++)
             		string=string+" ";
             }
             else
             string=string+"      ";

             if(Simulator.memory[i][4].matches("")==false)
             {
             	string = string + ","+Simulator.memory[i][4];
             	for(int j=0;j<5-Simulator.memory[i][4].length();j++)
             		string=string+" ";
             }
             else
             string=string+"      ";

             string=string+Simulator.fetchStage[i];
             for(int j=0;j<10-(String.valueOf(Simulator.fetchStage[i]).length());j++)
             	string=string+" ";
             
             string=string+Simulator.decodeStage[i];
             for(int j=0;j<10-(String.valueOf(Simulator.decodeStage[i]).length());j++)
             	string=string+" ";
           
             if(Simulator.executeStage[i]==-1)
             	string=string+"          ";
             else
             {
             	string=string+Simulator.executeStage[i];
             	for(int j=0;j<10-(String.valueOf(Simulator.executeStage[i]).length());j++)
             		string=string+" ";
             }
             
             if(Simulator.writeBackStage[i]==-1)
             	string=string+"    ";
             else 
             {
             	string=string+Simulator.writeBackStage[i];
             	for(int j=0;j<5-(String.valueOf(Simulator.writeBackStage[i]).length());j++)
             		string=string+" ";
             }
             string=string+"\t\t"+Simulator.isRAW[i]+"\t\t"+Simulator.isWAR[i]+"\t\t"+Simulator.isWAW[i]+"\t\t"+Simulator.isSTRUC[i];

             fillScoreboard.format("%s %n",string);
     	}
   }

     public static void onBranch(int instructionNum) throws FileNotFoundException
     {
         if(Simulator.memory[instructionNum][1].contains("BEQ"))
         {
        	 if(Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][3])]==Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][4])])
        	 {
        		 Scoreboard.writeOnScoreBoard(Simulator.start, instructionNum+1);
        		 int label=0;

   	             for(int i=0;i<Simulator.instructionNum;i++)
   	             {
   	            	 if(Simulator.memory[i][0].contains(Simulator.memory[instructionNum][2]))
	                 {
   	            		 label = i;
    	                 break;
	                 }
   	             }
    	             
    	         Simulator.start=label;
    	         for(int i=label;i<Simulator.instructionNum;i++)
    	         {
    	        	 Simulator.fetchStage[i]=0;
    	             Simulator.decodeStage[i]=0;
    	             Simulator.executeStage[i]=0;
    	             Simulator.readOperand[i]=0;
    	             Simulator.writeBackStage[i]=0;
    	         }

        	 }
         }
    	 else if(Simulator.memory[instructionNum][1].contains("BNE"))
    	 {
    		 if(Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][3])]!=
    				 Simulator.memoryData[PipeLine.returnAddInReg(Simulator.memory[instructionNum][4])])
    		 {
    			 Scoreboard.writeOnScoreBoard(Simulator.start, instructionNum+1);
    	         int label=0;
    	         
    	         for(int i=0;i<Simulator.instructionNum;i++)
    	         {
    	        	 if(Simulator.memory[i][0].contains(Simulator.memory[instructionNum][2]))
    	             {
    	        		 label = i;
    	                 break;
    	             }
    	         }
    	         Simulator.start=label;
    	         for(int i=label;i<Simulator.instructionNum;i++)
    	         {
    	        	 Simulator.fetchStage[i]=0;
    	             Simulator.decodeStage[i]=0;
    	             Simulator.executeStage[i]=0;
    	             Simulator.readOperand[i]=0;
    	             Simulator.writeBackStage[i]=0;
    	         }
    		 }
    	 }
    	 else if(Simulator.memory[instructionNum][1].contains("J"))
    	 {
    		 Scoreboard.writeOnScoreBoard(Simulator.start, instructionNum);
    	     int label=0;

    	     for(int i=0;i<Simulator.instructionNum;i++)
    	     {
    	    	 if(Simulator.memory[i][0].contains(Simulator.memory[instructionNum][2]))
    	         {
    	    		 label = i;
    	             break;
    	         }
    	     }
    	                     
    	     Simulator.start=label;
    	     for(int i=label;i<Simulator.instructionNum;i++)
    	     {
    	    	 Simulator.fetchStage[i]=0;
    	         Simulator.decodeStage[i]=0;
    	         Simulator.executeStage[i]=0;
    	         Simulator.readOperand[i]=0;
    	         Simulator.writeBackStage[i]=0;
    	     }
    	 }
     }
     
     public static String[] filterInstructionFromFile(String input)
     {
     	String s[]=new String[5];
     	for(int i=0;i<5;i++)
     		s[i]="";
     	String temp="";
     	int flag1=0,flag2=0,y=0;
     	for(int i=0;i<input.length();i++)
     	{
     		if(input.charAt(i)!=':' && input.charAt(i)!=' ' && input.charAt(i)!=',')
     		{
     			temp=temp+input.charAt(i);
     			flag1=1;
     		}
     		else
     		{
     			if(input.charAt(i)==':')
     				flag2=1;
     			if(flag1==1)
     			{
     				s[y]=temp.toUpperCase();
     				temp="";
     				y++;
     				flag1=0;
     			}
     		}
     	}
     	if(temp.matches("")==false)
     		s[y]=temp.toUpperCase();
     	if(flag2!=1)
     	{
     		s[4]=s[3];
     		s[3]=s[2];
     		s[2]=s[1];
     		s[1]=s[0];
     		s[0]="";
     	}
     	if(s[1].matches("BNE")||s[1].matches("BEQ"))
     	{
     		String temporary=s[4];
     		s[4]=s[2];
     		s[2]=temporary;
     		temporary=null;
     	}
     	return s;
     }
    
    
}
