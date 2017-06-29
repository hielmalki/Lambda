import java.awt.Image;
import java.util.*;

import org.simpleframework.xml.Root;
import org.xml.sax.helpers.DefaultHandler;

import turban.utils.*;

//@Root(name = "Person", strict = false)
public class Person extends DefaultHandler implements IGuifiable
{
	private String _strId="";
	private String _strName="";
	private String _strEmail="";
	private HSPersonaltyp _personaltyp=null;
	
	
	// leere Konstruktor für die Aufgabe 9.2 c
	public Person(){
		
	}
	
	public Person(String strId,String strName, HSPersonaltyp personelltype, String strEmail)
	{
		_strId=strId;
		_strName=strName;
		_strEmail=strEmail;
		_personaltyp=personelltype;
	}
	
	
	public String toGuiString()
	{
		return _personaltyp.toGuiString()+" "+ _strName;
	}
	
	public Image getGuiIcon()
	{
		return null;
	}
	
	public String getId()
	{
		return _strId;
	}
	
	public String getName()
	{
		return _strName;
	}
	
	public String getEmail()
	{
		return _strEmail;
	}
	
	public HSPersonaltyp getPersonellType()
	{
		return _personaltyp;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Person) {
			Person p = (Person) obj;
			if (p.getId().equals(this.getId()) && p.getName().equals(this.getName())
					&& p.getEmail().equals(this.getEmail()) && p.getPersonellType() == this.getPersonellType())
				return true;
			return false;
		}
		return false;
	}
	
}