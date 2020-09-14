/**
* The Person class is a child class from the parent class Character
* This class represents a human in the scenarios.
* The purpose of this class is to create an instance of Person Character
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/
package ethicalengine;


public class Person extends Character 
{
	/**
	* An enum for the age category of a Person Instance
	*/
	public enum AgeCategory
	{
		BABY("BABY"),
		CHILD("CHILD"),
		ADULT("ADULT"),
		SENIOR("SENIOR");
		
		final String name;
	    AgeCategory(String name) 
	    { 
	    	this.name = name; 
	    }
	    AgeCategory() 
	    { 
	    	this(null); 
	    }
	   
	    
	    @Override
	    public String toString() {
	        return name == null ? super.toString() : name;
	    };
	}
	
	/**
	* An enum for the Profession of a Person Instance
	*/
	public enum Profession
	{ 
		DOCTOR("DOCTOR"),
		CEO("CEO"),
		CRIMINAL("CRIMINAL"),
		HOMELESS("HOMELESS"),
		UNEMPLOYED("UNEMPLOYED"),
		STUDENT("STUDENT"),
		NONE("NONE"),
		UNKNOWN("UNKNOWN");
		
		
		final String name;
	    Profession(String name) 
	    { 
	    	this.name = name; 
	    }
	    Profession() 
	    { 
	    	this(null); 
	    }
	   
	    
	    @Override
	    public String toString() {
	        return name == null ? super.toString() : name;
	    }
		
	}
	
	//Variable initialization
	private Profession profession;
	private AgeCategory ageCategory;
	private boolean isPregnant; 
	private boolean isYou;

	//default constructor
	public Person()
	{
		super();
		this.profession = Profession.UNKNOWN;
		// by default all person are not pregnant
		this.isPregnant = false;
		
		//by default a person is not you
		this.isYou = false; 
		
		
	}
	
	/**
	* A constructor to create the instance of a Person
	* @param the age, gender, and bodytype are determined here
	*/
	public Person(int age,  Gender gender, BodyType bodytype)
	{
		super(age, gender, bodytype);
		
		if(super.getGender().equals(Gender.MALE))
		{
			this.isPregnant = false;
		}
		else
		{
			this.isPregnant = isPregnant;
		}
		
		if(getAgeCategory()!=AgeCategory.ADULT)
		{
			this.profession = Profession.UNKNOWN;
		}
		else
		{
			this.profession = profession;
		}
		
		this.isYou = false;
		
	}
	
	/**
	* A constructor to create the instance of a Person
	* @param the age, gender, and bodytype, profession
	* and isPregnant are determined here
	*/
	public Person(int age, Profession profession, Gender gender, BodyType bodytype, boolean isPregnant)
	{
		super(age, gender, bodytype);
		
		if(super.getGender().equals(Gender.MALE))
		{
			this.isPregnant = false;
		}
		else
		{
			this.isPregnant = isPregnant;
		}
		
		if(getAgeCategory()!=AgeCategory.ADULT)
		{
			this.profession = Profession.UNKNOWN;
		}
		else
		{
			this.profession = profession;
		}
		
		this.isYou = false;
		
	}
	
	/**
	* A copy constructor to create the instance of a Person
	* @param an instance of another Person
	*/
	public Person(Person otherPerson)
	{
		super(otherPerson);

		if(otherPerson.getGender().equals(Gender.FEMALE))
		{
			this.isPregnant = otherPerson.isPregnant;
		}
		else
		{
			this.isPregnant = false;
		}
		
		
		if(getAgeCategory()!=AgeCategory.ADULT)
		{
			this.profession = Profession.UNKNOWN;
		}
		else
		{
			this.profession = otherPerson.profession;
		}
		
		this.isYou = otherPerson.isYou;
		
	}
	
	/**
	* Returns a boolean indicating whether the person is representative
	* of the user
	*/
	public boolean isYou()
	{
		return this.isYou;
	}
	
	/**
	* Sets the value of whether the person is representative of the user
	* @param isYou is a boolean that determines true or false
	*/
	public void setAsYou(boolean isYou)
	{
		this.isYou = isYou;
	}
	
	/**
	* A constructor to create the instance of a Person
	* @param the age, gender, and bodytype, profession
	* and isPregnant are determined here
	*/
	public boolean isPregnant()
	{
		boolean pregnant;
		//male can't be pregnant
		if(super.getGender() == Gender.MALE)
		{
			pregnant = false;
		}
		else
		{
			pregnant = this.isPregnant;
		}
		
		return pregnant;
	}
	
	/**
	* Sets the value returned by isPregnant() while preventing
	* invalid states, such as a pregnant male
	* @param the boolen pregnant sets the condition to true or false
	*/
	public void setPregnant(boolean pregnant)
	{
		if(super.getGender().equals(Gender.FEMALE))
		{
			this.isPregnant = pregnant;
		}
		else
		{
			this.isPregnant = false;
		}
	}
		
	/**
	* Returns an enumeration value of the type Profession 
	*/
	public Profession getProfession()
	{
		return  this.profession;
	}
	
	/**
	* Returns an enumeration value of the type AgeCategory
	* depending on the Person's age
	*/
	public AgeCategory getAgeCategory()
	{
		if(super.getAge() >= 0 && super.getAge() <= 4)
		{
			ageCategory=  AgeCategory.BABY;
		}
		
		else if(super.getAge() >= 5 && super.getAge() <= 16)
		{
			ageCategory =AgeCategory.CHILD;
		}
		
		else if(super.getAge() >= 17 && super.getAge() <= 68)
		{
			ageCategory= AgeCategory.ADULT;
		}
		
		else if(super.getAge() >= 69)
		{
			ageCategory = AgeCategory.SENIOR;
		}
		
		return ageCategory;
	}
	
	@Override
	public String toString()
	{
		String s="";
		String you="";
		String pregnant="";
		String profession="";
		
		if(isYou==true)
		{
			you = "you ";
			s+= you;
		}
		
		s+=getBodyType()+" ";
		s+=getAgeCategory()+" ";
		
		if(getAgeCategory().equals(AgeCategory.ADULT))
		{
			profession = getProfession().toString();
			s+=profession+" ";
		}
		s+=getGender();
		if(isPregnant==true)
		{
			pregnant = " pregnant";
			s+=pregnant;
		}

		return s.toLowerCase();
	}
	
}
