/**
* This class is the basis of the simulation and shall be used to create a variety
* of scenarios. To guarantee a balanced set of scenarios, it is crucial to randomize as many elements as
* possible, including the number and characteristics of persons and animals involved in each scenario as
* well as the scenario itself.
* 
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

package ethicalengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.Profession;

public class ScenarioGenerator 
{
	//initialization of defaults
	private final int DEF_MAX_CHARACTER =5;
	private final int DEF_MIN_CHARACTER =1;
	private final int DEF_MIN_AGE =1;
	private final int DEF_MAX_AGE_PERSON = 100;
	private final int DEF_MAX_AGE_ANIMAL = 20;
	private final int DEF_SEED=10; 
	
	//initialization of ratios
	private final float CHANCES_ISHUMAN = 0.8f;
	private final float CHANCES_ISYOU = 0.5f;
	private final float CHANCES_ISPET = 0.5f; 
	private final float CHANCES_ISPREGNANT = 0.2f;
	private final static float CHANCES_LEGALCROSS = 0.8f;
	
	//variable initializations
	private int pedestrianCount;
	private int passengerCount;
	private int pedestrianCountMinimum;
	private int pedestrianCountMaximum;
	private int passengerCountMinimum;
	private int passengerCountMaximum;
	private int randomIndex;
	private long seed; 
	private boolean legalCrossing;
	private boolean containHuman;
	
	private ArrayList <Character> passengers = new ArrayList<Character>();
	private ArrayList <Character> pedestrians = new ArrayList<Character>();
	private Random rnd;

	//private Random rnd = new Random();
	
	/**
	* A constructor to create a truly random Scenario
	*/
	public ScenarioGenerator()
	{
		this.rnd = new Random();
		this.pedestrianCountMinimum =DEF_MIN_CHARACTER;
		this.pedestrianCountMaximum = DEF_MAX_CHARACTER;
		this.passengerCountMinimum = DEF_MIN_CHARACTER;
		this.passengerCountMaximum = DEF_MAX_CHARACTER;
	}
	
	/**
	* A constructor to generate a scenario
	* @param the seed is set with a predefined value
	*/
	public ScenarioGenerator(long seed)
	{
		this.rnd = new Random();
		this.rnd.setSeed(seed);
		
		this.pedestrianCountMinimum =DEF_MIN_CHARACTER;
		this.pedestrianCountMaximum = DEF_MAX_CHARACTER;
		this.passengerCountMinimum = DEF_MIN_CHARACTER;
		this.passengerCountMaximum = DEF_MAX_CHARACTER;
		
	}
	
	/**
	* A constructor to generate a scenario
	* @param the seed is set with a predefined value, as well as the
	* minimum and maximum number of both passengers and pedestrians
	* with values
	*/
	public ScenarioGenerator(long seed, int passengerCountMinimum, int passengerCountMaximum, 
			int pedestrianCountMinimum, int pedestrianCountMaximum)
	{
		this.rnd = new Random();
		this.rnd.setSeed(seed);
		
		this.pedestrianCountMinimum =pedestrianCountMinimum;
		this.pedestrianCountMaximum = pedestrianCountMaximum;
		this.passengerCountMinimum = passengerCountMinimum;
		this.passengerCountMaximum = passengerCountMaximum;
		
	}
	
	/**
	* Returns a newly created instance of Scenario containing a random number of passengers
	* and pedestrians with random characteristics as well as a randomly red or green light condition
	* with you(the user) being either in the car, on the street, or absent.
	*/
	public Scenario generate()
	{
		this.passengerCount= randomNumberGenerator(this.passengerCountMinimum,this.passengerCountMaximum);
		this.pedestrianCount= randomNumberGenerator(this.pedestrianCountMinimum,this.pedestrianCountMaximum);
		
		this.passengers = getRandomCharacter(this.passengerCount);
		this.pedestrians = getRandomCharacter(this.pedestrianCount);
		
		assignYou(this.passengerCount, this.pedestrianCount);
		assignPet(this.passengerCount, this.pedestrianCount);
		
		this.legalCrossing = randomBooleanGenerator(CHANCES_LEGALCROSS);
		
		Character [] passengerArray = new Character[passengers.size()];
		Character [] pedestrianArray = new Character[pedestrians.size()];
		
		passengerArray = this.passengers.toArray(passengerArray);
		pedestrianArray = this.pedestrians.toArray(pedestrianArray);
	
		return new Scenario(passengerArray,pedestrianArray,legalCrossing);
	}
	
	//A function to generate a random number 
	public int randomNumberGenerator(int min,int max)
	{
		int val = this.rnd.nextInt((max-min)+1);
		return min + val;
	}
	
	//A function to generate a random boolean value
	public boolean randomBooleanGenerator(float p)
	{	
		return this.rnd.nextFloat()<p;
	}
	
	//A function to generate a random String value
	public String randomString(String[] array) 
	{
	    int index = this.rnd.nextInt(array.length);
	    return array[index];
	}
	
	/**
	* Sets the minimum number of car passengers for each scenario
	* @param defines the minimum value
	*/
	public void setPassengerCountMin(int passengerCountMinimum)
	{
		this.passengerCountMinimum = passengerCountMinimum;
	}
	
	/**
	* Sets the maximum number of car passengers for each scenario
	* @param defines the maximum value
	*/
	public void setPassengerCountMax(int passengerCountMaximum)
	{
		this.passengerCountMinimum = passengerCountMaximum; 
	}
	
	/**
	* Sets the minimum number of pedestrians for each scenario
	* @param defines the minimum value
	*/
	public void setPedestrianCountMin(int pedestrianCountMinimum)
	{
		this.pedestrianCountMinimum = pedestrianCountMinimum;
	}
	
	/**
	* Sets the maximum number of pedestrians for each scenario
	* @param defines the maximum value
	*/
	public void setPedestrianCountMax(int pedestrianCountMaximum)
	{
		this.pedestrianCountMaximum = pedestrianCountMaximum;
	}
	
	//A function to return an ArrayList of random characters
	public ArrayList<Character> getRandomCharacter(int num)
	{
		 ArrayList<Character> array = new ArrayList<Character>();
	     for (int i = 0; i < num; i++) 
	     {
	        array.add(getRandomChar());
	     }
	     return array;
	}
	
	//A function that returns a character
	public Character getRandomChar()
	{
		boolean isHuman; 
		Character character;
		
		isHuman = randomBooleanGenerator(CHANCES_ISHUMAN);
		if(isHuman == true)
		{
			character= getRandomPerson();	
		}
		else
		{
			character = getRandomAnimal();
		}
		return character; 
		
	}
	
	//A function that returns a random person
	public Person getRandomPerson()
	{
		int age = randomNumberGenerator(DEF_MIN_AGE,DEF_MAX_AGE_PERSON);
		BodyType type = getRandomBodyType();
		Gender gender = getRandomGender();
		Profession profession = getRandomProfession();
		boolean pregnant = randomBooleanGenerator(CHANCES_ISPREGNANT);
		
		return new Person(age,profession,gender,type,pregnant);
	}
	
	//A function that returns a random animal
	public Animal getRandomAnimal()
	{
		final String [] SPECIES = {"cat", "dog","anaconda"};
		String species = randomString(SPECIES);
		
		return new Animal(species);
	}
	
	//A function that returns a random gender
	public Gender getRandomGender()
	{
		Gender [] arrayGender = Gender.values();
		return arrayGender[this.rnd.nextInt(arrayGender.length-1)];
		
	}
	
	//A function that returns a random body type
	public BodyType getRandomBodyType()
	{
		BodyType [] arrayBodyType = BodyType.values();
		return arrayBodyType[this.rnd.nextInt(arrayBodyType.length)];
		
	}
	
	//A function that returns a random profession
	public Profession getRandomProfession()
	{
		Profession [] arrayProfession = Profession.values();
		return arrayProfession[this.rnd.nextInt(arrayProfession.length)];
	}
	
	//A function that assigns a random you
	public void assignYou(int passengerCount, int pedestrianCount)
	{	
		boolean hasYou = false;
		hasYou = Scenario.checkIsYou(this.passengers,this.pedestrians);
		
		if(hasYou==false)
		{
			boolean youInCar = randomBooleanGenerator(CHANCES_ISYOU);
			if (youInCar == true)
			{
				 randomIndex = this.rnd.nextInt(passengerCountMinimum);
				 if(this.passengers.get(randomIndex).getClass().equals(Person.class))
				 {	 
					 ((Person) this.passengers.get(randomIndex)).setAsYou(true);
				 }
			}
			else
			{
				 randomIndex = this.rnd.nextInt(pedestrianCountMinimum);
				 if(this.pedestrians.get(randomIndex).getClass().equals(Person.class))
				 {	
					 ((Person) this.pedestrians.get(randomIndex)).setAsYou(true);
				 }
			}
		}
	}
	
	
	//A function to assign a random pet
	public void assignPet(int passengerCount, int pedestrianCount)
	{
		boolean petsScenario = randomBooleanGenerator(CHANCES_ISPET);
		
		if (petsScenario == true)
		{
			randomIndex = this.rnd.nextInt(passengerCountMinimum);
			if(this.passengers.get(randomIndex).getClass().equals(Animal.class))
			{
				((Animal) this.passengers.get(randomIndex)).setPet(true);
			}
		}
		else
		{
			randomIndex = this.rnd.nextInt(pedestrianCountMinimum);
			if(this.pedestrians.get(randomIndex).getClass().equals(Animal.class))
			{
		
				((Animal) this.pedestrians.get(randomIndex)).setPet(true);
			}
		}
	}	
	
}
