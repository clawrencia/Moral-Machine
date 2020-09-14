/**
* This class contains all relevant information about a presented scenario, including the car's passengers
* and the pedestrians on the street as well as whether the pedestrians are crossing legally.
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

package ethicalengine;

import java.util.ArrayList;


public class Scenario 
{ 
	private boolean legalCrossing;
	//create an empty array for passengers and pedestrians

	Character [] passengers = new Character[100];
	Character [] pedestrians = new Character[100];
	
	/**
	* A default constructor class for Scenario
	* @param an array of passengers and pedestrians, along with the boolean legalCrossing 
	*/
	public Scenario(Character [] passengers, Character [] pedestrians,boolean legalCrossing)
	{
		this.passengers = passengers;
		this.pedestrians = pedestrians;
		this.legalCrossing = legalCrossing;
	}
	
	//A function to check if isYou is present
	public static boolean checkIsYou(ArrayList <Character> passengersList, ArrayList <Character>pedestriansList)
	{
		boolean isYouException = false;
		int counter =0;
		ArrayList <Character> allCharacter = new ArrayList<Character>();
		allCharacter.addAll(passengersList);
		allCharacter.addAll(pedestriansList);
		
		
		for(Character c: allCharacter)
		{
			if(c.getClass().equals(Person.class))
			{
				
				if(((Person) c).isYou()==true)
				{
					counter = counter +1;
				}
			}
		}
		
		if(counter >1)
		{
			isYouException = true;
		}
		return isYouException;
	}
	
	/**
	* returns a boolean indicating whether you (the user)
	* is in the car
	*/
	public boolean hasYouInCar()
	{
		boolean hasYou = false;
		for(Character c : this.passengers)
		{
			if(c.getClass().equals(Person.class))
			{
				
				if(((Person) c).isYou()==true)
				{
					return hasYou=true;
				}
			}
		}
		return hasYou=false;
	}
	
	/**
	* returns a boolean indicating whether you (the user)
	* are in the lane, i.e. crossing the street
	*/
	public boolean hasYouInLane()
	{
		boolean hasYou = false;
		for(Character c : this.pedestrians)
		{
			if(c.getClass().equals(Person.class))
			{
				if(((Person) c).isYou()==true)
				{
					return hasYou=true;
				}
			}
		}
		return hasYou=false;
	}
	
	/**
	* returns the cars' passengers as a Character [] array
	*/
	public Character [] getPassengers() 
	{
		 return this.passengers;
	}
	
	/**
	* returns the pedestrians as a Character [] array
	*/
	public Character [] getPedestrians() 
	{
		return this.pedestrians;
	}
	
	/**
	* sets whether the pedestrians are legally crossing the street
	* @param a boolean that states if the crossing is legal or not
	*/
	public void setLegalCrossing(boolean legalCrossing)
	{
		this.legalCrossing = legalCrossing;
	}
	
	/**
	* returns whether the pedestrians are legally crossing at the traffic
	* light
	*/
	public boolean isLegalCrossing() 
	{
        return this.legalCrossing;
    }
	
	/**
	* returns the number of passengers in the car
	*/
    public int getPassengerCount()
    {
    	int count=0;
    	for(Character i: this.passengers)
    	{
    		count = count+1;
  
    	}
    	return count;
    }
    
	/**
	* returns the number of pedestrians in lane
	*/
    public int getPedestrianCount()
    {
    	int count=0;
    	for(Character i: this.pedestrians)
    	{
    		count = count+1;
    	}
    	return count;
    }
    
    @Override 
    public String toString()
    {
    	String s;
    	s="======================================\n";
    	s+="# Scenario\n";
    	s+="======================================\n";
    	
    	if(this.legalCrossing==false)
    	{
    		s+="Legal Crossing: no\n";
    	}
    	else
    	{
    		s+="Legal Crossing: yes\n";
    	}
    	
    	s+="Passengers ("+getPassengerCount()+")";
    	
    	for (Character p : this.passengers) 
    	{	  	
    		s += "\n- " + p;
    	}
    	s+="\nPedestrians ("+getPedestrianCount()+")";
    	
    	for (Character peds : this.pedestrians) 
    	{	
    		
    		s += "\n- " + peds;
        }
    	
    	return s;
    }
    
}
