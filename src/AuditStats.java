/**
* This class is used to store characteristics for audit class
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

import java.util.ArrayList;
import java.util.Collections;
import ethicalengine.Person;

public class AuditStats {

	//Initialising variable
	private String characteristics;
	private int count;
	private int ageTotal; 
	private float ratio;

	//Default constructor
	public AuditStats()
	{
		this.characteristics="";
		this.count=0;
		this.ageTotal=0;
		this.ratio =0;
	
	}
	
	public AuditStats(String characteristics)
	{
		this.characteristics = characteristics;
		this.count=0;
		this.ageTotal=0;
		this.ratio = ratio; 
		
	}
	
	public AuditStats(String characteristics,int count, int ageTotal)
	{
		this.characteristics = characteristics;
		this.count=count;		
		this.ageTotal = ageTotal;
		this.ratio = ratio;
	
	}
	
	//Copy constructor
	public AuditStats(AuditStats otherAuditStats)
	{
		this.characteristics = otherAuditStats.characteristics;
		this.count = otherAuditStats.count;
		this.ageTotal = otherAuditStats.ageTotal;
		this.ratio = otherAuditStats.ratio;
	}
	
	//A getter to return the ratio
	public float getRatio()
	{
		return ratio;
	}
	
	//A setter to set the ratio
	public void setRatio(float ratio)
	{
		this.ratio = ratio;

	}

	//A getter to return the total age
	public int getAgeTotal()
	{
		return ageTotal;
	}
	
	//A getter to return the characteristics
	public String getCharacteristics()
	{
		return characteristics;
	}
	
	//A getter to return the count of each characteristic
	public int getCount()
	{
		return count;
	}
	
	//A setter to set the total age
	public void setAgeTotal(int ageTotal)
	{
		this.ageTotal = ageTotal; 
	}
	
	//A setter to set the count 
	public void setCount(int count)
	{
		this.count = count;
	}
	
	//A setter to set the characteristics
	public void setCharacteristics(String characteristics)
	{
		this.characteristics = characteristics;
	}

	@Override 
	public String toString()
	{
		return this.characteristics.toLowerCase()+" ";
	}

	
}
