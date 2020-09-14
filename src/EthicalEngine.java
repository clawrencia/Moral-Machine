/**
* This class holds the main method and manages the program executions.
* This class also takes care of the program parameters as well as the user input.
* The decision method is located in this class, which implements the decision-making algorithm
* outputting either PEDESTRIANS or PASSENGERS.
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
import java.util.Scanner; 
import ethicalengine.*;
import ethicalengine.Character;
import ethicalengine.Character.BodyType;
import ethicalengine.Character.Gender;
import ethicalengine.Person.Profession;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class EthicalEngine 
{
	//Default values
	int CHARACTER_TYPE=0;
	int FILE_COLUMN = 10;
	int CHARACTER_PASS_PEDS=9;
	
	static final int KEYBOARD_SIZE =3;
	static String consentAnswer;
	static Scanner keyboard = new Scanner(System.in);
	
	static String file_path;
	
	/**
	* An enum for the decision made
	*/
	public enum Decision
	{
		PASSENGERS,
		PEDESTRIANS
	}
	
	public static void main (String [] args) throws Exception, InvalidInputException, IOException
	{
		//Initialising the magic numbers
		int FIRST_COMMAND=0;
		int ONE_INPUT =1;
		int TWO_INPUT=2;
		int THREE_INPUT =3;
		int PATH_KEYBOARD=1;
		
		//Create a new instance of Ethical Engine
		String quit="n";
		EthicalEngine engine = new EthicalEngine();
		ArrayList <String> arguments = new ArrayList<String>();
		
		//Arguments from command line
		for(int i=0; i<args.length;i++)
		{
			arguments.add(args[i]);
		}
		
		String command="";
		String path;
	
		Boolean interactive=false; 
		
		//load ascii
		engine.loadASCII();
		consentAnswer = engine.consent(consentAnswer);
		if(consentAnswer.contentEquals("yes"))
		{
			file_path = "user.log";
		}
		
		while(quit.equals("n"))
		{	
			if(arguments.size()!=0)
			{
				command=arguments.get(FIRST_COMMAND);
			}
			
			if(command.contentEquals("--config")||command.contentEquals("-c"))
			{
				//load random scenario
				if(arguments.size() == ONE_INPUT)
				{
					engine.loadRandom(consentAnswer);
				}
				//load config scenario
				else if(arguments.size()==TWO_INPUT)
				{
					path = arguments.get(PATH_KEYBOARD);
				
					engine.loadConfig(arguments.get(PATH_KEYBOARD+1),interactive,consentAnswer);
				}
			}
			else if(command.contentEquals("-r")||command.contentEquals("--result"))
			{
				//Assign the file path
				file_path = arguments.get(ONE_INPUT);
			}
			else if(command.contentEquals("--help")||command.equals("-h")||command.isEmpty())
			{
				//Display help
				engine.displayHelp();
			}
			else if(command.contentEquals("--interactive")||command.contentEquals("-i"))
			{		
				//interactive mode
				interactive = true;

				if(arguments.size()== ONE_INPUT)
				{
					//random interactive
					engine.loadRandomInteractive(consentAnswer);
				}
				else if(arguments.size()==THREE_INPUT)
				{
					//config interactive
					String config = arguments.get(TWO_INPUT-1);
					if(config.contentEquals("-c")||config.contentEquals("--config"))
					{	
						path = arguments.get(THREE_INPUT-1);
						engine.loadConfig(path, interactive, consentAnswer);
					}
				}
			}
			else
			{
				engine.displayHelp();
			}
		}
		
	}
	
	/**
	* The decision algorithm that returns an Enum value of decision
	* @param the scenario upon where the decision will be decided
	*/
	public static Decision decide(Scenario scenario)
	{
		
		EthicalEngine obj = new EthicalEngine();
        int scorePassengers = obj.calcScorePassengers(scenario);
        int scorePedestrians= obj.calcScorePedestrians(scenario);

        if(scorePassengers >= scorePedestrians)
        {
        	return Decision.PASSENGERS;
        }
        else
        {
        	return Decision.PEDESTRIANS;
        }
        
        
	}
	
	/**
	* A function to load the welcome.ascii file
	* @throws IOException class when the file is not found
	*/
	private void loadASCII() throws IOException
	{
		try
		{
			BufferedReader asciiReader = new BufferedReader(new FileReader("welcome.ascii"));
			String line = null;
			while ((line = asciiReader.readLine()) != null)
			{
			    System.out.println(line);
			}
			
			asciiReader.close();
		}
		catch (FileNotFoundException notFound)
		{
			System.out.println("File Not Found");
			System.exit(0);
		}

	}
	
	//A function to collect the consent from the user
	private String consent(String consent)
	{
		consent ="no";
		boolean wrongInput=true; 
		System.out.println("\nDo you consent to have your decisions saved to a file? (yes/no)");
		
		while(wrongInput==true)
		{
			try
			{
				consent = keyboard.nextLine();
			
				if(consent.contentEquals("yes")||consent.contentEquals("no"))
				{
					wrongInput =false;
					break;
				}
				else
				{
					//throw an invalid input excpetion if the answer is not "yes" or "no"
					throw new InvalidInputException("Invalid response. Do you consent to "
							+ "have your decisions saved to a file? (yes/no)");
				}
			}
			catch (InvalidInputException invalidInput)
			{
				System.out.println(invalidInput.getMessage());
			}
		}
		
		return consent;
	}
	
	//A function that will print 3 scenarios at a time
	private void printThree(int counter, int index, ArrayList <Scenario> scenarioList,Audit auditor)
	{
		int MAX_THREE=3;
		int auditRun=0;
		
		//When the counter hits 2 (inclusive with 0 )
		if(counter==MAX_THREE-1)
		{
			auditRun = auditor.getRunTime();
			auditRun += 3;
			auditor.setRunTime(auditRun);
		} 
		//When it reaches the final scenario
		else if(index==scenarioList.size()-1)
		{
			auditRun = auditor.getRunTime();
			auditRun +=1;
			auditor.setRunTime(auditRun);
		}
		
		auditor.printStatistics();
		String input;
		
		if(counter==MAX_THREE-1)
		{
			if(index==scenarioList.size()-1)
			{
				System.out.println("That's all. Press enter to quit");
				input = keyboard.nextLine();
				System.exit(0);
			}	
			System.out.println("Would you like to continue? (yes/no)");
			boolean inputCorrect = true;
			
			while(inputCorrect=true)
			{
				input = keyboard.nextLine();
			
				try
				{
					if(input.contentEquals("yes"))
					{
						inputCorrect = false;
						break;
					}
					else if(input.contentEquals("no"))
					{
						System.exit(0);
					}
					else
					{
						//throw an invalid input exception 
						throw new InvalidInputException("Invalid input");
					}
				}
				catch(InvalidInputException invalid)
				{
					System.out.println(invalid.getMessage());
				}
			}
				
		} 
	}
	
	//A function to check the consent 
	private void checkConsent(String consent, Audit auditor) throws IOException
	{
		if(consent.contentEquals("yes"))
		{
			auditor.printToFile(file_path);
		}
	}
	
	//A function to calculate the score of pedestrian
	private int calcScorePedestrians(Scenario scenario)
	{
		int numPassengers = scenario.getPassengerCount();
        int numPedestrians = scenario.getPedestrianCount();
        
        ArrayList<Character> newListPedestrian = new ArrayList<Character>();
        Collections.addAll(newListPedestrian, scenario.getPedestrians());
	 
        int scorePedestrians = 0;
        
        if (numPedestrians > numPassengers) 
        {
        	scorePedestrians++;
        } 
         
        if(containBabyandChild(newListPedestrian)==true)
        {
        	scorePedestrians++;
        }

        if(containPregnant(newListPedestrian)==true)
        {
        	scorePedestrians++;
        }
    
        if(hasCriminal(newListPedestrian)==true)
        {
        	scorePedestrians --;
        } 
      
        if(scenario.isLegalCrossing()==true)
        {
        	scorePedestrians ++;
        }
        
        if(containUnknownGender(newListPedestrian)==true)
        {
        	scorePedestrians--;
        }
        
        return scorePedestrians;
	}
	
	//A function to calculate the score of passengers
	private int calcScorePassengers(Scenario scenario)
	{
		int numPassengers = scenario.getPassengerCount();
        int numPedestrians = scenario.getPedestrianCount();
        
        ArrayList<Character> newListPassenger= new ArrayList<Character>();
        Collections.addAll(newListPassenger, scenario.getPassengers());
        
        int scorePassengers = 0;
        
        if (numPassengers > numPedestrians) 
        {
        	scorePassengers ++;
        } 
             
        if(containBabyandChild(newListPassenger)==true)
        {
        	scorePassengers ++;
        }
      
        
        if(containPregnant(newListPassenger)==true)
        {
        	scorePassengers ++;
        }
    
        if(hasCriminal(newListPassenger)==true)
        {
        	scorePassengers --;
        } 
      
        
        if(scenario.isLegalCrossing()==true)
        {
        	scorePassengers --;
        }
        
        if(containUnknownGender(newListPassenger)==true)
        {
        	scorePassengers --;
        }
        return scorePassengers;
	}
	
	//A function to check if a baby is within the arraylist of characters
	private boolean containBabyandChild(ArrayList <Character> array)
	{
		int MAX_AGE_CHILD = 16;
		boolean containBabynChild =false; 
		for (Character p : array)
		{
			if(p.getAge() <= MAX_AGE_CHILD)
			{
				containBabynChild = true;
			}
		}
		return containBabynChild;
	}
	
	//A function to check if a pregnant woman is within the arraylist of characters
	private boolean containPregnant(ArrayList <Character> array)
	{
		boolean containPreg =false; 
		for (Character p : array)
		{
			if(p.getClass().equals(Person.class))
			{
				if(((Person)p).isPregnant()==true)
				{
					containPreg = true;
				}
			}
		}
		return containPreg;
	}
	
	//A function to check if a criminal is within the arraylist of characters
	private boolean hasCriminal(ArrayList <Character> array)
	{
		boolean hasCriminal = false;
		for(Character p :array)
		{
			if(p.getClass().equals(Person.class))
			{
				if(((Person)p).getProfession().equals(Profession.CRIMINAL))
				{
					hasCriminal = true;
					break;
				}
			}
		}
		return hasCriminal; 
	}
	
	//A function to check if an unknown gender is within the arraylist of characters
	private boolean containUnknownGender(ArrayList <Character> array)
	{
		boolean hasUnknown = false;
		for(Character p:array)
		{
			if(p.getClass().equals(Person.class))
			{
				if(p.getGender().equals(Gender.UNKNOWN))
				{
					hasUnknown = true;
					break;
				}
			}
		}
		return hasUnknown;
	}
	

	//A function to display help to the command line
	private void displayHelp()
	{
		System.out.println("\nEthical Engine - COMP90041 - Final Project\n");
		System.out.println("Usage: java EthicalEngine [arguments]\n");
		System.out.println("Arguments:");
		
		System.out.printf("%5s %15s%n","-c or --config" ,"Optional: path to config file");
		System.out.printf("%5s %15s%n","-h or --help" ,"Print Help (this message) and exit");
		System.out.printf("%5s %15s%n","-r or --results","Optional: path to results log file"); 
		System.out.printf("%5s %15s%n","-i or --interactive","Optional: launches interactive mode"); 
	}
	
	//A function that loads random scenario with maximum size 10
	private void loadRandom(String consentAnswer) throws IOException
	{
		int MAX_SIZE=10;
		ArrayList <Scenario> random = new ArrayList<Scenario>();
		Audit auditor = new Audit();	
		auditor.setAuditType("Algorithm");
		
		for(int i=0; i<MAX_SIZE;i++)
		{
			ScenarioGenerator randomScenario = new ScenarioGenerator();
			Scenario newScenario = randomScenario.generate();		
			random.add(newScenario);
		}
		auditor.run(random);
	}
	
	//A function that loads random scenario with maximum size 10 for interactive mode
	private void loadRandomInteractive(String consentAnswer) throws IOException
	{
		int MAX_COUNTER=3;
		int MAX_SIZE=10;
		Random rnd = new Random();
		
		//Create an arraylist of scenario and for characters
		ArrayList <Scenario> random = new ArrayList<Scenario>();
		ArrayList <Character> survive = new ArrayList<Character>();
		ArrayList <Character> allCharacter = new ArrayList<Character>();
		
		Audit auditor = new Audit();
		auditor.setAuditType("User");
		
		int counter =0;
		int i=0;
		
		//Load scenario and interactive mode until the last scenario 
		while(i<MAX_SIZE && counter <MAX_COUNTER)
		{
			ScenarioGenerator randomScenario = new ScenarioGenerator();
			Scenario newScenario = randomScenario.generate();
			System.out.println(newScenario);
			random.add(newScenario);
		
			interactiveMode(survive, random.get(i),auditor, allCharacter);	
			
			if(counter==MAX_COUNTER-1 || i==MAX_SIZE-1)
			{
				printThree(counter, i,random,auditor);
				counter = -1;
			}
			counter++;
			i++;	
		}	
		
	}
	
	//Interactive mode function 
	private void interactiveMode(ArrayList<Character>survivor, Scenario scenario, 
			Audit auditor, ArrayList<Character>allCharacter) throws NotDirectoryException, IOException
	{
		//Initialise variable
		boolean correctInput = false;
		int green=0;
		int red=0;
		int totalRed =0;
		int totalGreen =0; 
		
		while(correctInput==false)
		{
			try
			{
				ArrayList <Character> tempChar = new ArrayList <Character>();
				ArrayList <Character> tempCharAll = new ArrayList <Character>();
				
				List<Character> newListPassenger = Arrays.asList(scenario.getPassengers());
				List<Character> newListPedestrian = Arrays.asList(scenario.getPedestrians());
			
				System.out.println("Who should be saved? (passenger(s) [1] or pedestrian(s) [2])");
				String input = keyboard.nextLine();
				
				if(input.equals("1")||input.contentEquals("passengers")||input.equals("passenger"))
				{	
					//Add the survivors to a list of arrays
					survivor.addAll(newListPassenger);
					tempChar.addAll(newListPassenger);
					
					//Add all characters to a list of arrays
					tempCharAll.addAll(newListPedestrian);
					tempCharAll.addAll(newListPassenger);

					if(scenario.isLegalCrossing()==true)
					{
						//calculate legal 
						calcGreen(green, tempChar, auditor);
						calcTotalGreen(totalGreen, tempCharAll, auditor);
					}
					else
					{
						calcRed(red, tempChar, auditor);
						calcTotalRed(totalRed, tempCharAll, auditor);
					}
					
					allCharacter.addAll(tempChar);
					allCharacter.addAll(newListPedestrian);

					correctInput=true;
					break;
				
				}
				else if(input.contentEquals("2")||input.contentEquals("pedestrians")||input.contentEquals("pedestrian"))
				{
					//Add all survive to the list of character array
					survivor.addAll(newListPedestrian);
					tempChar.addAll(newListPedestrian);
					
					tempCharAll.addAll(newListPassenger);
					tempCharAll.addAll(newListPedestrian);
					
					if(scenario.isLegalCrossing()==true)
					{
						//calculate legal
						calcGreen(green, tempChar, auditor);
						calcTotalGreen(totalGreen, tempCharAll, auditor);
					}
					else
					{
						calcRed(red, tempChar, auditor);
						calcTotalRed(totalRed, tempCharAll, auditor);
					}
					
					allCharacter.addAll(newListPassenger);
					allCharacter.addAll(tempChar);
					
					correctInput = true;
					break;
				}
				else
				{
					throw new InvalidInputException("Invalid response. Please insert the correct answer");
				}
			}
			catch(InvalidInputException invalidException)
			{
				System.out.println(invalidException.getMessage());
			}
		}
	}
	
	//A function to load interative mode for config files 
	private void loadConfigInteractive(ArrayList <Scenario> scenarioList, String consent)
		throws InvalidCharacteristicException, InvalidDataFormatException, IOException
	{
		//Create an arraylist for characters
		ArrayList <Character> survive = new ArrayList<Character>();
		ArrayList <Character> allCharacter = new ArrayList<Character>();
		Audit auditor = new Audit();
		auditor.setAuditType("User");
		
		int counter =0;
		int i=0;
	
		while(i<scenarioList.size() && counter <3)
		{
			System.out.println(scenarioList.get(i));
			
			interactiveMode(survive, scenarioList.get(i), auditor, allCharacter);
			
			if(counter==2 || i==scenarioList.size()-1)
			{
				auditor.run(survive, scenarioList.get(i),allCharacter);
				printThree(counter, i,scenarioList,auditor);
				counter = -1;
			}
			counter++;
			i++;	
		}	
		
		
	}
	
	//A function to parse the config file
	private void loadConfig(String path, Boolean interactive, String consent) 
			throws InvalidCharacteristicException, InvalidDataFormatException, IOException
	{
		ArrayList <Scenario> scenarioList = new ArrayList<Scenario>();
	
		EthicalEngine engine = new EthicalEngine();
		try
		{	
			//user a buffered reader to read the csv
			BufferedReader csvReader = new BufferedReader(new FileReader(path));
			ArrayList<String> lines = new ArrayList<>();
						
			String line = null;
			//read while the line is not null 
			while ((line = csvReader.readLine()) != null)
			{
			    lines.add(line);
			}
			
			csvReader.close();
			
			//Skip the first column as we dont want to read the header 
			for(int i=1; i<lines.size();i++)
			{
				//Split by the commas
				String [] data = lines.get(i).split(",");
				Boolean isLegal = false;
				Boolean stop = false;
				
				//Find the scenario start
				if(!data[CHARACTER_TYPE].contentEquals("person")&&!data[CHARACTER_TYPE].contentEquals("animal"))
				{
					//Check legal
					isLegal = engine.checkLegal(data);
				
					boolean scenarioFound = true;
					boolean endofLine = false;
					
					while(scenarioFound=true)
					{
						//Create an arraylist of passengers and pedestrians
						ArrayList <Character> passengers= new ArrayList<>();
						ArrayList <Character> pedestrians = new ArrayList<>();
						
						for(int j=i+1; j<lines.size();j++)
						{	
							//Parse the characters into an array of strings
							String[]characters = lines.get(j).split(",");

							int lineChar =0;
							while(lineChar<lines.size())
							{
								try
								{
									if(characters[CHARACTER_TYPE].equals("animal")||
											characters[CHARACTER_TYPE].contentEquals("person"))
									{
										//Check the data format 
										if(characters.length>FILE_COLUMN||characters.length<FILE_COLUMN)
										{
											int error = j+1;
											throw new InvalidDataFormatException("WARNING: invalid data "
													+ "format in config file line "+error);
										}
									}
								}
								catch (InvalidDataFormatException invalidData)
								{
									//Skip the following line if it is an error
									j=j+1;
									System.out.println(invalidData.getMessage());
									characters= lines.get(j).split(",");
									
								}
								lineChar++;
							}
							if(endofLine == true)
							{
								scenarioFound=false;
								break;
							}
							//Load the character into the arraylist
							engine.loadCharacter(characters,j,pedestrians,passengers);
							endofLine = engine.checkLine(characters);
						
						}
						
						Character [] passengerArray = new Character[passengers.size()];
						Character [] pedestrianArray = new Character[pedestrians.size()];
						
					
						passengerArray = passengers.toArray(passengerArray);
						pedestrianArray = pedestrians.toArray(pedestrianArray);
						
						//Create a new scenario
						Scenario scenarioConfig = new Scenario(passengerArray,pedestrianArray,isLegal);
						scenarioList.add(scenarioConfig);
						
						break;
					}
				}			
			}
			//if interactive mode is false we will audit the scenario directly
			if(interactive==false)
			{
				Audit auditConfig = new Audit();
				auditConfig.setAuditType("Algorithm");
				auditConfig.run(scenarioList);
				auditConfig.printStatistics();
				System.exit(0);
			}
			else
			{
				loadConfigInteractive(scenarioList,consent);
			}
			
		}
		catch(FileNotFoundException e)
		{
			System.out.println("ERROR: could not find config file.");
			System.exit(0);
		}
	}
	
	//A function to check the legality of the scenario 
	private boolean checkLegal(String [] data)
	{
		//Initialising variable
		int LEGAL_COLOUR=1;
		boolean legal=false;
		String [] legalString = data[LEGAL_COLOUR-1].split(":");
		
		if(legalString[LEGAL_COLOUR].contentEquals("green"))
		{
			legal=true;
		} 
		else if(legalString[LEGAL_COLOUR].contentEquals("red"))
		{
			legal=false;
		}
		return legal;	
	}
	
	//A function to check end of line in a config 
	private boolean checkLine(String[]characters)
	{
		boolean endofLine;
		if(characters[CHARACTER_TYPE].contentEquals("person")||
				characters[CHARACTER_TYPE].contains("animal"))
		{
			endofLine = false;
		}
		else
		{
			endofLine = true;
		}
		return endofLine;
	}
	
	//A function to load the characters
	private void loadCharacter(String [] characters, int index, ArrayList<Character>pedestrians,
			ArrayList<Character>passengers) throws NumberFormatException, InvalidCharacteristicException
	{
		//default magic numbers
		int AGE_POSITION=2;
		int GENDER_POSITION=1;
		
		//variable initialisation 
		int age;
		Gender g = Gender.UNKNOWN;
		String gen;
		int lineError = index +1;
		boolean foundGender = false;
		Gender arrayGender [] = Gender.values();
		
		try
		{
			//if the character type is person or animal 
			if(characters[CHARACTER_TYPE].equals("person")||characters[CHARACTER_TYPE].equals("animal"))
			{
				//parse age
				age = Integer.parseInt(characters[AGE_POSITION]);
				
				if(!characters[GENDER_POSITION].isEmpty())
				{
					//parse gender
					gen = characters[GENDER_POSITION].toUpperCase();
					foundGender = findGender(arrayGender, gen);
					if(foundGender==true)
					{
						g = Gender.valueOf(characters[GENDER_POSITION].toUpperCase());
						if(g.equals(Gender.FEMALE)||g.equals(Gender.MALE)||g.equals(Gender.UNKNOWN))
						{
							if(characters[CHARACTER_TYPE].equals("person"))
							{
								loadPerson(characters, index, age, g,pedestrians, passengers);
							}
							else if(characters[CHARACTER_TYPE].equals("animal"))
							{
								loadAnimal(characters,index,age,g, pedestrians,passengers);
							}
						}
					}
					else
					{
						throw new InvalidCharacteristicException("WARNING: invalid characteristic "
								+ "in config file in "+lineError);
					}
				}
			}
		} 
		catch(NumberFormatException numException)
		{	
			//Set age to default if there is an error 
			System.out.println("WARNING: invalid number format in config file in line "+lineError);
			age = 1;
		}
		catch(InvalidCharacteristicException charException)
		{
			//Set gender as unknown if an error is found
			System.out.println(charException.getMessage());
			g = Gender.UNKNOWN;
		}
	}
	
	//A function to load a character type animal
	private void loadAnimal(String [] characters, int index, int age, Gender gender, ArrayList<Character>pedestrians,
			ArrayList<Character>passengers)
	{
		int SPECIES_POSITION =7;
		int PET_POSITION =8;
		
		String species = characters[SPECIES_POSITION];
		Boolean pet = Boolean.parseBoolean(characters[PET_POSITION]);
		
		//Create a new animal 
		Animal a =new Animal(species);
		a.setGender(gender);
		a.setAge(age);
		//parse pet
		if(pet==true)
		{
			a.setPet(pet);
		}
		
		//add to pedestrian or passenger
		if(characters[CHARACTER_PASS_PEDS].contentEquals("pedestrian"))
		{
			pedestrians.add(a);	
		}
		else if(characters[CHARACTER_PASS_PEDS].contentEquals("passenger"))
		{
			passengers.add(a);
		}
	}
	
	//A function to load a character type person
	private void loadPerson(String [] characters, int index, int age, Gender gender,ArrayList<Character>pedestrians,
			ArrayList<Character>passengers)
	{
		//Default positioning 
		int PROFESSION_POSITION =4;
		int BODYTYPE_POSITION=3;
		int PREGNANT_POSITION =5;
		int ISYOU_POSITION = 6;
		
		//Parse pregnant and you
		Boolean pregnant = Boolean.parseBoolean(characters[PREGNANT_POSITION]);
		Boolean you = Boolean.parseBoolean(characters[ISYOU_POSITION]);
		
		Profession profession= Profession.UNKNOWN;
		BodyType bodyTypeVal = BodyType.UNSPECIFIED;
		
		boolean foundProf=false;
		boolean foundBT = false;
	
		//Get the enum value of profession and body type
		Profession arrayProfession[] = Profession.values();
		BodyType arrayBodyType []= BodyType.values();
		
		int lineError = index +1;
		try
		{
			String prof = characters[PROFESSION_POSITION].toUpperCase();
			String bType = characters[BODYTYPE_POSITION].toUpperCase();
			
			if(prof.isEmpty())
			{
				//Set as unknown if it is empty
				profession = Profession.UNKNOWN;
				foundProf = true; 
			}
			else
			{
				//Find the profession 
				foundProf = findProfession(arrayProfession, prof);
				if(foundProf==true)
				{
					profession = Profession.valueOf(prof);
				}
			}
			
			if(bType.isEmpty())
			{
				//Set as unspecified if it is empty
				bodyTypeVal = BodyType.UNSPECIFIED;
				foundBT=true;
			}
			else
			{
				//Find the bodytype
				foundBT = findBodyType(arrayBodyType, bType);
				if(foundBT==true)
				{
					bodyTypeVal = BodyType.valueOf(bType);
				}
			}
			
			//If the value is not in the enum throw invalid characteristic exception 
			if(foundProf==false||foundBT==false)
			{
				throw new InvalidCharacteristicException("WARNING: invalid characteristic in config file in" + 
						" line " +lineError);
			}
			
			//Create a new person instance
			Person p = new Person(age,profession,gender,bodyTypeVal,pregnant);
			
			//Check if person isYou
			Boolean hasYou = Scenario.checkIsYou(passengers, pedestrians);
			if(hasYou==false)
			{
				if(you==true)
				{
					p.setAsYou(you);
				}
			};	
			
			//Add to pedestrian or passenger
			if(characters[CHARACTER_PASS_PEDS].contentEquals("pedestrian"))
			{
				pedestrians.add(p);
			}
			else if(characters[CHARACTER_PASS_PEDS].contentEquals("passenger"))
			{
				passengers.add(p);
			}
		}
		
		catch(InvalidCharacteristicException invalidCharacter)
		{
			System.out.println(invalidCharacter.getMessage());	
		}
	}
	
	//Find the specific gender in the enum gender
	private boolean findGender(Gender [] genderArray, String genderText)
	{
		boolean found =false; 
		for(Gender gr: genderArray)
		{
			if(gr.toString().equals(genderText))
			{
				found = true; 
				break;
			}
		}
		return found; 
	}
	
	//Find the specific profession the enum profession 
	private boolean findProfession(Profession [] professionArray, String profText)
	{
		boolean found =false; 
		for(Profession pr: professionArray)
		{
			if(pr.toString().equals(profText))
			{
				found = true; 
				break;
			}
		}
		return found; 
	}
	
	//Find the specific bodytype in the enum bodytype
	private boolean findBodyType(BodyType [] bodyTypeArray, String btText)
	{
		boolean found = false;
		for (BodyType bt: bodyTypeArray)
		{
			if(bt.toString().equals(btText))
			{
				found = true;
				break;
			}
		}
		return found;
	}
	
	//A function to calculate the number of survivor during an illegal crossing
	private void calcRed(int red, ArrayList <Character> Char, Audit auditor)
	{
		red = auditor.getRed();
		//Add to the counter of red for each survivor
		for(Character c: Char)
		{
			red++;
		}

		auditor.setRed(red);
	}
	
	//A function to calculate the total characters during an illegal crossing
	private void calcTotalRed(int totalRed, ArrayList <Character> allChar, Audit auditor)
	{
		totalRed = auditor.getTotalRed();
		//Add to the counter of total red for each character in the scenario 
		for(Character c: allChar)
		{
			totalRed++;
		}
	
		auditor.setTotalRed(totalRed);
	}
	
	//A function to calculate the number of survivor during a legal crossing 
	private void calcGreen(int green, ArrayList <Character> Char, Audit auditor)
	{
		green = auditor.getGreen();
		//Add to the counter of green for each survivor
		for(Character c: Char)
		{
			green++;
		}
		
		auditor.setGreen(green);
	}
	
	//A function to calculate the total characters during a legal crossing 
	private void calcTotalGreen(int totalGreen, ArrayList <Character> allChar, Audit auditor)
	{
		totalGreen = auditor.getTotalGreen();
		//Add to the total green for each character
		for(Character c: allChar)
		{
			totalGreen++;
		}
	
		auditor.setTotalGreen(totalGreen);
	}
}
