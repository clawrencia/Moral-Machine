/**
* The Animal class is a child class from the parent class Character
* This class represents animals in the scenarios
* The purpose of this class is to create an instance of Animal Character
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

package ethicalengine;

public class Animal extends Character 
{
	private String species; 
	private boolean isPet;
	
	//Default Constructor
	public Animal()
	{
		super();
		this.species = "UNKNOWN";
		this.isPet = false; 
	}
	
	/**
	* A constructor class that creates an Animal instance
	* @param A string that determines the 'species' of the Animal
	*/
	public Animal(String species)
	{
		super();
		this.species = species; 
		this.isPet = false;
	}
	
	/**
	* A copy constructor class that creates an Animal instance
	* @param Another Animal instance
	*/
	public Animal(Animal otherAnimal)
	{
		super();
		this.species = otherAnimal.species;
		this.isPet = otherAnimal.isPet;
	}
	
	/**
	* Sets the value returned by isPet()
	* @param a boolean that determines whether isPet is true or false
	*/
	public void setPet(boolean isPet)
	{
		this.isPet = isPet;
	}
	
	/**
	* Returns a boolean value depending whether the animal is a pet 
	* or wild animal
	*/
	public boolean isPet()
	{
		return isPet;
	}
	
	/**
	* Sets the value returned by getSpecies()
	* @param A string that determines the 'species' of the Animal
	*/
	public void setSpecies(String species)
	{
		this.species = species;
	}
	
	/**
	* Returns a String indicating what type of species the animal 
	* represents
	*/
	public String getSpecies()
	{
		return species; 
	}
	
	@Override
	public String toString()
	{
		String petString;
		
		if(isPet== true)
		{
			petString = this.species +" is pet";
		}
		else
		{
			petString = this.species;
		}
		return petString;
	}
}
