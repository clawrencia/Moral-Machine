/**
* Character is an Abstract Class from which all character types inherit.
* The purpose of this class is to create a Character (can be Human or Animal) for the scenario 
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

package ethicalengine;

public abstract class Character {
	
	private int DEFAULT_AGE = 1;
	
	/**
	* An enum for the gender of a character
	*/
	public enum Gender
	{
		FEMALE("FEMALE"),
		MALE("MALE"),
		UNKNOWN("UNKNOWN");
		
		final String name;
	    Gender(String name) 
	    { 
	    	this.name = name; 
	    }
	    Gender() 
	    { 
	    	this(null); 
	    }
	   
	    
	    @Override
	    public String toString() {
	        return name == null ? super.toString() : name;
	    }
	}
	
	/**
	* An enum for the bodytype of a character
	*/
	public  enum BodyType
	{
		AVERAGE("AVERAGE"),
		ATHLETIC("ATHLETIC"),
		OVERWEIGHT("OVERWEIGHT"),
		UNSPECIFIED("UNSPECIFIED");
		
		final String name;
	    BodyType(String name) 
	    { 
	    	this.name = name; 
	    }
	    BodyType() 
	    { 
	    	this(null); 
	    }
	   
	    
	    @Override
	    public String toString() {
	        return name == null ? super.toString() : name;
	    }
	}
	
	//initialising variable
	private int age;
	private BodyType bodyType;
	private Gender gender;
	
	//default constructor
	public Character() 
	{
		this.age = DEFAULT_AGE;
		this.bodyType = bodyType.UNSPECIFIED;
		this.gender = gender.UNKNOWN;
	}
	
	public Character(int age, Gender gender, BodyType bodyType)
	{
		if (age <= 0)
		{
			this.age = DEFAULT_AGE;
		} 
		else
		{
			this.age = age;
		}
		
		this.gender = gender;
		this.bodyType = bodyType;
	}
	
	//copy constructor
	public Character(Character otherCharacter)
	{
		this.age = otherCharacter.age;
		this.gender = otherCharacter.gender;
		this.bodyType = otherCharacter.bodyType;
	}
	
	
	public int getAge() 
	{
		return age;
	}
	
	public Gender getGender()
	{
		return gender;
	}
	
	public BodyType getBodyType()
	{
		return bodyType;
	}
	
	//setter for age
	public void setAge(int age)
	{
		int MIN_AGE =0;
		if (age <= MIN_AGE)
		{
			this.age = DEFAULT_AGE;
		}
		else
		{
			this.age = age;
		}
	}
	
	//setter for gender
	public void setGender(Gender gender)
	{
		this.gender = gender;
	}
	
	//setter for bodytype
	public void setBodyType(BodyType bodyType)
	{
		this.bodyType = bodyType;
	}
	
	//abstract toString function 
	@Override	
	public abstract String toString();
}
