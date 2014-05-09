/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simulator;






/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

public class Cache 
{
	private int i_cache[][]=new int[4][6];
	private int d_cache[][]=new int[4][13];
	private int busOccupiedCycle;
	public int access_Icache,access_Dcache,cacheHit_D,cacheHit_I;

	public Cache()
	{
		for(int i=0;i<4;i++)
		{   
			busOccupiedCycle=0;
        	i_cache[i][5]=0;
        	d_cache[i][12]=0;
        	d_cache[i][6]=0;
        	d_cache[i][0]=0;
		}
		access_Icache=0;
		access_Dcache=0;
		cacheHit_D=0;
		cacheHit_I=0;
    }
int dAccess(int instructionNum,int cycle)
{
    int address=Simulator.regOperand1[instructionNum];
    int block=address%16;
    block=block/4;
    if(((d_cache[block][1]==address/16 && d_cache[block][6]==1)||(d_cache[block][7]==address/16 &&d_cache[block][12]==1 )))//hit
    {  
    	if(Simulator.memory[instructionNum][1].contains("L.D")||Simulator.memory[instructionNum][1].contains("S.D"))
        {
    		access_Dcache=access_Dcache+Simulator.IcacheCycle+Simulator.DcacheCycle;
            cacheHit_D=cacheHit_D+Simulator.IcacheCycle+Simulator.DcacheCycle;
            return cycle + 1;
        }
        else
        {
        	access_Dcache++;
            cacheHit_D++;
            return cycle;
        }
              
    }
    else    //miss     
    {
    	if(d_cache[block][0]==0)
        {
    		d_cache[block][1]=address/16 ;
            d_cache[block][6]=1;
        }
    	else
        {
    		d_cache[block][7]=address/16;
            d_cache[block][12]=1;
        }
        if(Simulator.memory[instructionNum][1].contains("L.D")||Simulator.memory[instructionNum][1].contains("S.D"))
        {
        	access_Dcache=access_Dcache+Simulator.IcacheCycle+Simulator.DcacheCycle;
            cacheHit_D++;
            if(cycle>busOccupiedCycle)
            {
            	busOccupiedCycle=cycle + (Simulator.memoryCycle*4)+1;
                return cycle + (Simulator.memoryCycle*4)+1;
            }
            else
            {
            	busOccupiedCycle=busOccupiedCycle + (Simulator.memoryCycle*4);
                return busOccupiedCycle;
            }

        }
        else
        {
        	access_Dcache++;
            if(cycle>busOccupiedCycle)
            {
            	busOccupiedCycle=cycle+ (Simulator.memoryCycle * 4);
                return cycle + (Simulator.memoryCycle * 4);
            }
            else
            {
            	busOccupiedCycle=busOccupiedCycle + (Simulator.memoryCycle * 4);
                return busOccupiedCycle;
            }
               
        }
    }
}
//---------------------------------------------dAccess() END-----------------------------------------------------------------

	int iAccess(int instructionNum,int cycle)
	{
		access_Icache++;
		int block=instructionNum%16;
		block=block/4;
    
		if(i_cache[block][5]==1 && i_cache[block][0]==instructionNum/16)//hit
		{
			cacheHit_I++;
			return cycle;
		}
		else//miss
		{
			i_cache[block][5]=1;
			i_cache[block][0]=instructionNum/16;
			if(cycle>busOccupiedCycle)
			{
				busOccupiedCycle=cycle+5; //Cant remember why +5???
				return cycle + 5;
			}
			else
			{
				busOccupiedCycle=busOccupiedCycle+5;
				return busOccupiedCycle;
			}
    
		}
    }
//----------------------------------iAccess() END---------------------------------------------------------------------	
}




