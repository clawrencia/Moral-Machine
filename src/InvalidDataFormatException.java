/**
* An exception class that is thrown when there is an invalid data while parsing
* This class extends the Exception class
*
* @author  Clarisca Lawrencia
* @username clawrencia
* @studentID 1152594
* @version 1.0
* @since 2020-06-24 
*/

public class InvalidDataFormatException extends Exception
{
	/**
	*Invalid input exception that will be called when there is an invalid data while parsing
	* @param the message that will be shown 
	*/
	public InvalidDataFormatException (String InvalidDataFormatMessage)
	{
		super(InvalidDataFormatMessage);
	}
}
