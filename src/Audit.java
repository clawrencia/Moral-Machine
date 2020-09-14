
/**
* The class Audit is used for the inspection of the algorithm with the goal
* of revealing inherent biases that may be built in as an (un)intended consequence.
* 
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.io.BufferedWriter;
import ethicalengine.Animal;
import ethicalengine.Character;
import ethicalengine.Character.Gender;
import ethicalengine.Person;
import ethicalengine.Scenario;
import ethicalengine.ScenarioGenerator;


public class Audit 
{
	private final String DEFAULT_TYPE = "Unspecified";
	
	//Initialising of variable
	private String auditType;
	private boolean runAudit = false; 
	private int runs=0;
	private int green, red;
	private int totalGreen, totalRed;
	private float averageAge;
	
	ArrayList <AuditStats> stats = new ArrayList<AuditStats>();
	ArrayList <AuditStats> statsTotal = new ArrayList<AuditStats>();
	
	/**
	* An empty constructor 
	*/
	public Audit()
	{
		this.auditType = DEFAULT_TYPE;
	}
	
	/**
	* Sets the name of the audit type
	* @param the name of the audit type in string 
	*/
	public void setAuditType(String auditType)
	{
		this.auditType = auditType;
	}
	
	/**
	* A public method that returns the name of the audit
	*/
	public String getAuditType()
	{
		return auditType;
	}
	
	/**
	* Runs the simulation depending on the scenario list
	* @param the scenario list that shall be simulated
	*/
	public void run(ArrayList <Scenario> scenarioList) 
	{
		runAudit = true;
		ArrayList <Character> survivors = new ArrayList <Character>();
		ArrayList <Character> appendCharacters = new ArrayList<Character>();
		
		//Run until the size of the scenario list
		for(int i=0; i < scenarioList.size();i++)
		{
			this.runs=i+1;
			EthicalEngine.decide(scenarioList.get(i));
			
			List<Character> newListPassenger = Arrays.asList(scenarioList.get(i).getPassengers());
			List<Character> newListPedestrian = Arrays.asList(scenarioList.get(i).getPedestrians());
			
			appendCharacters.addAll(newListPassenger);
			appendCharacters.addAll(newListPedestrian);
			
			//Decide based on the decide algorithm in ethical engine
			if(EthicalEngine.decide(scenarioList.get(i)).equals(EthicalEngine.Decision.PASSENGERS))
			{
				survivors.addAll(newListPassenger);
				
				//calculate green or red
				if(scenarioList.get(i).isLegalCrossing()==true)
				{
					this.green = scenarioList.get(i).getPassengerCount();
				}
				else
				{
					this.red = scenarioList.get(i).getPassengerCount();
				}
			}
			else 
			{
				survivors.addAll(newListPedestrian);	
				//Calculate green or red
				if(scenarioList.get(i).isLegalCrossing()==true)
				{
					this.green = scenarioList.get(i).getPedestrianCount();
				}
				else
				{
					this.red = scenarioList.get(i).getPedestrianCount();
				}
			}
			
			//calculate all the stats
			collectStats(appendCharacters, statsTotal);
			collectStats(survivors, stats);
			
			//calculate the stats for red and green
			statsRed(survivors);
			statsGreen(survivors);
			addTotalRed(appendCharacters);
			addTotalGreen(appendCharacters);
			
			//calculate the average age of the survivors
			this.averageAge=avgAge(survivors);
			
			//calculate the ratio for each characteristic
			for(AuditStats st: statsTotal)
			{
				float ratioCalc = calcRatio(stats,statsTotal,st.getCharacteristics());
				st.setRatio(ratioCalc);
			}
			
			CompareDesc(statsTotal);
			
			if(EthicalEngine.consentAnswer.contentEquals("yes"))
			{
				try {
					printToFile(EthicalEngine.file_path);
				} catch (IOException e) {
					System.out.println("ERROR: could not print results. Target directory does not exist.");
					System.exit(0);
				}
			}
		} 
	}
	
	/**
	* Runs the simulation depending on the scenario list
	* @param the scenario that shall be simulated, along with the characters 
	*/
	public void run(ArrayList <Character> survive, Scenario scenario,ArrayList<Character> allCharacter)
	{
		runAudit = true;
		
		//Calculate the stats
		collectStats(survive, stats);
		collectStats(allCharacter, statsTotal);
		statsRed(survive);
		statsGreen(survive);
		addTotalRed(allCharacter);
		addTotalGreen(allCharacter);
		
		//Calculate the average
		this.averageAge=avgAge(survive);
		
		//Calculate the ratio for each characteristics
		for(AuditStats st: statsTotal)
		{
			float ratioCalc = calcRatio(stats,statsTotal,st.getCharacteristics());
			st.setRatio(ratioCalc);
		}
		
		CompareDesc(statsTotal);
	
		
		if(EthicalEngine.consentAnswer.contentEquals("yes"))
		{
			try {
				printToFile(EthicalEngine.file_path);
			} catch (IOException e) {
				System.out.println("ERROR: could not print results. Target directory does not exist.");
				System.exit(0);
			}
		}
	
	}
	
	/**
	* runs the simulation by creating N = runs scenarios and running
	* each scenario through the EthicalEngine using its decide(Scenario scenario) method.
	*/
	public void run()
	{
		final int DEF_MAX =5;
		final int DEF_MIN=1;
		
		//Create a new random instance
		Random rnd = new Random();
		int i=0;
		this.runs = rnd.nextInt((DEF_MAX-DEF_MIN)+1);
		this.runs = this.runs+DEF_MIN;

		ArrayList <Character> appendCharacters = new ArrayList<Character>();
		ArrayList <Character> survive = new ArrayList<Character>();
		runAudit = true; 
		
		//Do while it is smaller than runs 
		while(i< this.runs)
		{
			ScenarioGenerator scenGen = new ScenarioGenerator();
			Scenario scenario = scenGen.generate();
			EthicalEngine.decide(scenario);
			
			//calculate total appearance for each characteristic
			List<Character> newListPassenger = Arrays.asList(scenario.getPassengers());
			List<Character> newListPedestrian = Arrays.asList(scenario.getPedestrians());
			
			appendCharacters.addAll(newListPassenger);
			appendCharacters.addAll(newListPedestrian);
			collectStats(appendCharacters, statsTotal);
		
			//do calculation for the surviving array
			if(EthicalEngine.decide(scenario).equals(EthicalEngine.Decision.PASSENGERS))
			{
				//Add to the passenger list
				survive.addAll(newListPassenger);
				if(scenario.isLegalCrossing()==true)
				{
					this.green = scenario.getPassengerCount();
				}
				else
				{
					this.red = scenario.getPassengerCount();
				}
			}
			
			else 
			{
				//Add to the pedestrian list
				survive.addAll(newListPedestrian);
				
				if(scenario.isLegalCrossing()==true)
				{
					this.green = scenario.getPassengerCount();
				}
				else
				{
					this.red = scenario.getPassengerCount();
				}
			}
			
			i++;
		} 
		
		//Calculate the stats
		collectStats(survive, stats);
		collectStats(appendCharacters, statsTotal);
		statsRed(survive);
		statsGreen(survive);
		addTotalRed(appendCharacters);
		addTotalGreen(appendCharacters);
		
		//Calculate average age
		this.averageAge=avgAge(survive);
	
		//Calculate the ratio for each stats
		for(AuditStats st: statsTotal)
		{
			float ratioCalc = calcRatio(stats,statsTotal,st.getCharacteristics());
			st.setRatio(ratioCalc);

		}
		CompareDesc(statsTotal);
		
		if(EthicalEngine.consentAnswer.contentEquals("yes"))
		{
			try {
				printToFile(EthicalEngine.file_path);
			} catch (IOException e) {
				System.out.println("ERROR: could not print results. Target directory does not exist.");
				System.exit(0);
			}
		}
		
	}
	
	/**
	* runs the simulation by creating N = runs scenarios and running
	* each scenario through the EthicalEngine using its decide(Scenario scenario) method.
	* @param an integer to determine the total amount of run time
	*/
	public void run(int runs)
	{
		//Initialise the variable 
		int i=0;
		ArrayList <Character> appendCharacters = new ArrayList<Character>();
		ArrayList <Character> survive = new ArrayList<Character>();
		runAudit = true; 
		this.runs = this.runs+runs; 
	
		while(i< this.runs)
		{
			ScenarioGenerator scenGen = new ScenarioGenerator();
			Scenario scenario = scenGen.generate();
			EthicalEngine.decide(scenario);
			
			//calculate total appearance for each characteristic
			
			List<Character> newListPassenger = Arrays.asList(scenario.getPassengers());
			List<Character> newListPedestrian = Arrays.asList(scenario.getPedestrians());
			
			appendCharacters.addAll(newListPassenger);
			appendCharacters.addAll(newListPedestrian);
			collectStats(appendCharacters, statsTotal);
		
			//do calculation for the surviving array
			if(EthicalEngine.decide(scenario).equals(EthicalEngine.Decision.PASSENGERS))
			{
				//Add to the passenger list
				survive.addAll(newListPassenger);
				if(scenario.isLegalCrossing()==true)
				{
					this.green = scenario.getPassengerCount();
				}
				else
				{
					this.red = scenario.getPassengerCount();
				}
			}
			
			else 
			{
				//Add to the pedestrian list 
				survive.addAll(newListPedestrian);
				
				if(scenario.isLegalCrossing()==true)
				{
					this.green = scenario.getPassengerCount();
				}
				else
				{
					this.red = scenario.getPassengerCount();
				}
			}
	
			i++;
		} 
		
		//Calculate the stats
		collectStats(survive, stats);
		collectStats(appendCharacters, statsTotal);
		statsRed(survive);
		statsGreen(survive);
		addTotalRed(appendCharacters);
		addTotalGreen(appendCharacters);
		
		//Calculate average age
		this.averageAge=avgAge(survive);
		
		//Calculate ratio for each characteristic
		for(AuditStats st: statsTotal)
		{
			float ratioCalc = calcRatio(stats,statsTotal,st.getCharacteristics());
			st.setRatio(ratioCalc);
		}
		
		CompareDesc(statsTotal);

		if(EthicalEngine.consentAnswer.contentEquals("yes"))
		{
			try {
				printToFile(EthicalEngine.file_path);
			} catch (IOException e) {
				System.out.println("ERROR: could not print results. Target directory does not exist.");
				System.exit(0);
			}
		}
		
	}
	
	//Setter for green 
	public void setGreen(int green)
	{
		this.green = green;
	}
	
	//Setter for red
	public void setRed(int red)
	{
		this.red = red;
	}
	
	//Getter for red
	public int getRed()
	{
		return this.red;
	}
	
	//Getter for green 
	public int getGreen()
	{
		return this.green;
	}
	
	//Setter for total green
	public void setTotalGreen(int greenTotal)
	{
		this.totalGreen = greenTotal;
	}
	
	//Setter for total red
	public void setTotalRed(int redTotal)
	{
		this.totalRed = redTotal;
	}
	
	//Getter for total red
	public int getTotalRed()
	{
		return this.totalRed;
	}
	
	//Getter for total green
	public int getTotalGreen()
	{
		return this.totalGreen;
	}
	
	//Setter for run time
	public void setRunTime(int run)
	{
		this.runs = run;
	}
	
	//Getter for run time 
	public int getRunTime()
	{
		return this.runs;
	}
	
	//A function to collect the characteristic stats
	private void collectStats(ArrayList<Character> characters, ArrayList<AuditStats> statsList)
	{
		for(AuditStats s:statsList)
		{
			s.setCount(0);
		}
		statsGender(characters,statsList);
		statsbodyType(characters,statsList);
		statsPregnant(characters,statsList);
		statsYou(characters,statsList);
		statsPet(characters,statsList);
		statsProfession(characters,statsList);
		statsAgeCategory(characters,statsList);
		statsSpecies(characters,statsList);
		statsClassType(characters,statsList);
		
	}
	
	//A function to collect the characteristic for gender
	private void statsGender(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		
		//loop through the character array
		for(Character p: array)
		{
			//Only calculate if it is not animal
			if(!p.getClass().equals(Animal.class))
			{
				String characteristic = p.getGender().name();
				int age = p.getAge();
				Gender unknown = Gender.UNKNOWN;
				
				if(!characteristic.contentEquals(unknown.name()))
				{
					boolean found = false;
					found = checkDuplicate(characteristic,statistics,found);
					if(found==true)
					{
						for (AuditStats i : statistics)
						{		
							if(i.getCharacteristics().equals(characteristic))
							{	
								//Increment the characteristic if found
								count =i.getCount()+1;
								age = i.getAgeTotal() + age; 
								i.setCount(count);
								i.setAgeTotal(age);
								break;
							}
						}
					}
					
					else 
					{
						//Create a new stats if not found
						count =1;
						statistics.add(new AuditStats(characteristic,count,age));
						
					}	
				}
			}
		}
	}
	
	//A function to check duplicate characteristic
	private boolean checkDuplicate(String characteristics, ArrayList <AuditStats> array, boolean found)
	{
		//Traverse the entire array
		for (AuditStats i : array)
		{	
			//Process if array is not null
			if(i!=null && i.getCharacteristics()!=null)
			{
				if(i.getCharacteristics().equals(characteristics))
				{
					found = true;
					break;
				}			
			}	
			
		}
		return found;
	}
	
	//A function to collect the characteristic for bodytype
	private void statsbodyType(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect stats for non animal 
			if(!p.getClass().equals(Animal.class))
			{
				String characteristic = p.getBodyType().name();
				int age = p.getAge();
				
				boolean found = false; 				
				found = checkDuplicate(characteristic,statistics,found);
				if(found==true)
				{
					for (AuditStats i : statistics)
					{	
						if(i.getCharacteristics().equals(characteristic))
						{	
							//increment if character is already present within the array
							count =i.getCount()+1;
							age = i.getAgeTotal() + age; 
							i.setCount(count);
							i.setAgeTotal(age);
							break;
						}
					}
				}
				
				else 
				{
					//create a new instance if no duplicate is present
					count =1;
					statistics.add(new AuditStats(characteristic,count,age));
					
				}	
			}
		}

	}
	
	//A function to collect the characteristic for pregnant 
	private void statsPregnant(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is non animal
			if(!p.getClass().equals(Animal.class))
			{
				if(((Person) p).isPregnant()==true)
				{
					String characteristic = "pregnant";
					int age = p.getAge();
	
					boolean found = false; 				
					found = checkDuplicate(characteristic,statistics,found);
					if(found==true)
					{
						for (AuditStats i : statistics)
						{	
							if(i.getCharacteristics().equals(characteristic))
							{		
								//Increment if found duplicate
								count =i.getCount()+1;
								age = i.getAgeTotal()+age;
								i.setCount(count);
								i.setAgeTotal(age);
								break;
							}
						}
					}
				
					else if (found==false)
					{
						//Create new instance if not present in the array
						count =1;
						statistics.add(new AuditStats(characteristic,count,age));
						
					}
				}					
			}
		}

	}
	
	//A function to collect the characteristic for you
	private void statsYou(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is non animal
			if(!p.getClass().equals(Animal.class))
			{
				if(((Person) p).isYou()==true)
				{
					String characteristic = "you";
					int age = p.getAge();
					
					boolean found = false; 				
					found = checkDuplicate(characteristic,statistics,found);
					if(found==true)
					{
						for (AuditStats i : statistics)
						{		
							if(i.getCharacteristics().equals(characteristic))
							{	
								//Increment if found duplicate
								count =i.getCount()+1;
								age = i.getAgeTotal() + age;
								i.setCount(count);
								i.setAgeTotal(age);
								break;
								
							}
						}
						
					}
				
					else 
					{
						//Create new instance if not present in the array
						count =1;
						statistics.add(new AuditStats(characteristic,count,age));
					}
				}					
			}
		}
	}
	
	//A function to collect the characteristic for pet
	private void statsPet(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is non person
			if(!p.getClass().equals(Person.class))
			{
				if(((Animal) p).isPet()==true)
				{
					String characteristic = "pet";
					
					boolean found = false; 				
					found = checkDuplicate(characteristic,statistics,found);
					if(found==true)
					{
						for (AuditStats i : statistics)
						{		
							if(i.getCharacteristics().equals(characteristic))
							{	
								//Increment if found duplicate
								count =i.getCount()+1;
								i.setCount(count);
								break;
								
							}
						}
						
					}
				
					else 
					{
						//Create new instance if not present in the array
						count =1;
						statistics.add(new AuditStats(characteristic,count,0));
					}
				}					
			}
		}
	}
	
	
	//A function to collect the characteristic for profession
	private void statsProfession(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is non animal
			if(!p.getClass().equals(Animal.class))
			{
				String characteristic = ((Person) p).getProfession().name();
				int age = p.getAge();
				
				boolean found = false; 				
				found = checkDuplicate(characteristic,statistics,found);
				if(found==true)
				{
					for (AuditStats i : statistics)
					{	
						if(i.getCharacteristics().equals(characteristic))
						{	
							//Increment if found duplicate
							count =i.getCount()+1;
							age = i.getAgeTotal() + age; 
							i.setCount(count);
							i.setAgeTotal(age);
							break;
						}
					}
				}
				
				else 
				{
					//Create new instance if not present in the array
					count =1;
					statistics.add(new AuditStats(characteristic,count,age));
					
				}	
			}
		
		}
	}
	
	//A function to collect the characteristic for age category
	private void statsAgeCategory(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{	
		int count=0;

		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is non animal
			if(!p.getClass().equals(Animal.class))
			{
				String characteristic = ((Person) p).getAgeCategory().name();
				int age = p.getAge();
				
				boolean found = false; 				
				found = checkDuplicate(characteristic,statistics,found);
				if(found==true)
				{
					for (AuditStats i : statistics)
					{	
						if(i.getCharacteristics().equals(characteristic))
						{	
							//Increment if found duplicate
							count =i.getCount()+1;
							age = i.getAgeTotal() + age; 
							i.setCount(count);
							i.setAgeTotal(age);
							break;
						}
					}
				}
				else 
				{
					//Create new instance if not present in the array
					count =1;
					statistics.add(new AuditStats(characteristic,count,age));
				}	
			}
		}
	}
	
	//A function to collect the characteristic for species
	private void statsSpecies(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		//Loop through the entire character array
		for(Character p: array)
		{
			//Only collect if it is animal
			if(p.getClass().equals(Animal.class))
			{
				String characteristic = ((Animal) p).getSpecies();

				boolean found = false; 				
				found = checkDuplicate(characteristic,statistics,found);
				if(found==true)
				{
					for (AuditStats i : statistics)
					{	
						if(i.getCharacteristics().equals(characteristic))
						{		
							//Increment if found duplicate
							count =i.getCount()+1;
							i.setCount(count);
							break;
						}
					}
				}
				else 
				{
					//Create new instance if not present in the array
					count =1;
					statistics.add(new AuditStats(characteristic,count,0));
					
				}	
			}
		}
	}
	
	//A function to collect the characteristic for class type
	private void statsClassType(ArrayList <Character> array, ArrayList <AuditStats> statistics)
	{
		int count=0;
		//Loop through the entire character array
		for(Character p: array)
		{
			String characteristic="";
			int age = p.getAge();
			if(p.getClass().equals(Animal.class))
			{
				characteristic = "animal";
			}
			else
			{
				characteristic = "person";
			}

			boolean found = false; 				
			found = checkDuplicate(characteristic,statistics,found);
			if(found==true)
			{
				for (AuditStats i : statistics)
				{			
					if(i.getCharacteristics().equals(characteristic))
					{	
						//Increment if found duplicate
						age = i.getAgeTotal()+age;
						count =i.getCount()+1;
						i.setCount(count);
						i.setAgeTotal(age);
						break;
					}
				}
			}
			else 
			{
				//Create new instance if not present in the array
				count =1;
				statistics.add(new AuditStats(characteristic,count,age));
				
			}	
		}
	}
	
	//A function to calculate total survive during illegal cross
	private void statsRed(ArrayList <Character> survive)
	{
		String characteristic = "red";
		int age=0;
		
		//Calculate age 
		for(Character s: survive)
		{
			age = s.getAge()+age;
		}
		
		boolean found = false; 	
		//Find duplicate
		found = checkDuplicate(characteristic,this.stats,found);
		if(found==true)
		{
			for (AuditStats i : this.stats)
			{			
				if(i.getCharacteristics().equals(characteristic))
				{	
					//Increment if found duplicate
					i.setCount(this.red);
					i.setAgeTotal(age);
					break;
				}
			}
		}
		else if(this.red!=0)
		{
			//Create a new instance if duplicate not found
			this.stats.add(new AuditStats(characteristic,this.red,age));
			
		}	
		
	}
	
	//A function to calculate total survive during legal cross
	private void statsGreen(ArrayList <Character> survive)
	{
		String characteristic = "green";
		
		int age=0;
		
		//Calculate age
		for(Character s: survive)
		{
			age = s.getAge()+age;
		}
		
		boolean found = false; 		
		//Find duplicate
		found = checkDuplicate(characteristic,this.stats,found);
		if(found==true)
		{
			for (AuditStats i : this.stats)
			{			
				if(i.getCharacteristics().equals(characteristic))
				{		
					i.setCount(this.green);
					i.setAgeTotal(age);
					break;
				}
			}
		}
		else if(this.green!=0)
		{
			//Create a new instance if duplicate not found
			this.stats.add(new AuditStats(characteristic,this.green,age));
		}	
	}
	
	//A function to calculate total character during illegal cross
	private void addTotalRed(ArrayList<Character>allChar)
	{
		String characteristic = "red";
		int age=0;
		
		//Calculate age
		for(Character s: allChar)
		{
			age = s.getAge()+age;
		}
		
		boolean found = false; 		
		//Find duplicate
		found = checkDuplicate(characteristic,this.statsTotal,found);
		if(found==true)
		{
			for (AuditStats i : this.statsTotal)
			{			
				if(i.getCharacteristics().equals(characteristic))
				{	
					//Increment if found duplicate
					i.setCount(this.totalRed);
					i.setAgeTotal(age);
					break;
				}
			}
		}
		else if (totalRed!=0)
		{
			//Create a new instance if duplicate not found
			this.statsTotal.add(new AuditStats(characteristic,this.totalRed,age));
			
		}	
		
		
	}
	
	//A function to calculate total character during legal cross
	private void addTotalGreen(ArrayList <Character> allChar)
	{
		String characteristic = "green";
		int age=0;
		
		//Calculate age
		for(Character s: allChar)
		{
			age = s.getAge()+age;
		}
		boolean found = false; 		
		//find duplicate
		found = checkDuplicate(characteristic,this.statsTotal,found);
		if(found==true)
		{
			for (AuditStats i : this.statsTotal)
			{			
				if(i.getCharacteristics().equals(characteristic))
				{		
					//Increment if found duplicate
					i.setCount(this.totalGreen);
					i.setAgeTotal(age);
					break;
				}
			}
		}
		else if(totalGreen!=0)
		{
			//Create a new instance if duplicate not found
			this.statsTotal.add(new AuditStats(characteristic,this.totalGreen,age));
			
		}	
		
	}
	
	//method to compare the array in descending format
	private void CompareDesc(ArrayList<AuditStats> statsList)
	{
		
		Comparator<AuditStats> compareDesc = Comparator.comparingDouble(AuditStats::getRatio);
		Comparator<AuditStats> compareReverse = compareDesc.reversed();
		Comparator<AuditStats> readNull = Comparator.nullsLast(compareReverse);

		Collections.sort(statsList,readNull);
	}
	
	//Method to find the average age of surviving characters
	private float avgAge(ArrayList<Character> survive)
	{
		float totalCount=0;
		float avgAge=0;
		float totalAge=0; 
		
		//Loop through the array of surviving characters
		for(Character st: survive)
		{
			//Calculate for person only
			if(st.getClass().equals(Person.class))
			{
				totalCount += 1;
				totalAge += st.getAge();
			}
		}
		
		avgAge = totalAge/ totalCount; 
		return avgAge;
	}
	
	//Method to calculate ratio 
	public float calcRatio(ArrayList<AuditStats> statsLists, ArrayList<AuditStats> 
		statsTotal, String characteristic)
	{
		float ratio=0;
		float totalAppearance=0;
		float totalCount=0;
		
		if(!characteristic.contentEquals("green")||!characteristic.contentEquals("red"))
		{
			for (AuditStats i : statsLists)
			{	
				//Process if array is not null
				if(i!=null && i.getCharacteristics()!=null)
				{
					if(i.getCharacteristics().equals(characteristic))
					{
						totalCount = i.getCount();
						break;
					}			 
				}	
				
			}
			
			for(AuditStats j : statsTotal)
			{
				//Process if array is not null
				if(j!=null && j.getCharacteristics()!=null)
				{
					if(j.getCharacteristics().equals(characteristic))
					{
						totalAppearance = j.getCount();
						break;
					}			
				}	
			}
			
			ratio = totalCount/totalAppearance;
			
		}
		else
		{
			for (AuditStats i : statsLists)
			{	
				//Process if array is not null
				if(i!=null && 
						(i.getCharacteristics().contentEquals("green")||i.getCharacteristics().contentEquals("red")))
				{
					if(i.getCharacteristics().equals(characteristic))
					{
						totalCount = i.getCount();
						break;
					}			
				}	
				
			}
			
			for(AuditStats j : statsTotal)
			{
				//Process if array is not null
				if(j!=null && 
						(j.getCharacteristics().contentEquals("green")||j.getCharacteristics().contentEquals("red")))
				{
					if(j.getCharacteristics().equals(characteristic))
					{
						totalAppearance = j.getCount();
						break;
					}			
				}	
			}
			ratio = totalCount/this.runs;
		}
		return ratio;
	}
	
	@Override
	public String toString()
	{
		String s="no audit available";
	
		if(runAudit==true||this.runs>0)
		{
			s="";
			s+="======================================\n";
			s+="# "+getAuditType()+" Audit\n";
			s+="======================================\n";
			s+="-% SAVED AFTER "+this.runs+" RUNS\n";
			
			for(int i=0; i < statsTotal.size();i++)
			{	
				String ratio = Float.toString(statsTotal.get(i).getRatio());
				s+= statsTotal.get(i).toString()+ratio.substring(0,3)+"\n";
			}
			
			String avg = Float.toString(this.averageAge);
			s+="--\n";
			s+="average age: "+avg.substring(0,4);	
		}
		return s;
	}
	
	/**
	* A public method that prints the summary returned by toString() method
	* to the command line
	*/
	public void printStatistics()
	{
		System.out.println(toString());
	}
	
	/**
	* A public method that prints the summary for printStatistics to a file, if a consent is given
	* @param the file path as to where the file will be saved.
	* @throws IO Exception 
	*/
	public void printToFile(String filePath) throws IOException
	{
		try 
		{
			BufferedWriter writer = new BufferedWriter(
                    new FileWriter(filePath, true));
			
			//write for each file
		
			writer.write(toString());	
			
			writer.close();
		}
		catch(NotDirectoryException directoryException)
		{
			System.out.println("ERROR: could not print results. Target directory does not exist.");
			System.exit(0);
		}
	
	}

}
