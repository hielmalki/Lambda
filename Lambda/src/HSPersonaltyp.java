
import java.awt.Image;
import turban.utils.IGuifiable;

public enum HSPersonaltyp implements IGuifiable{
	Student("Student",true,false,false,false), 
	Profesor("Proffesor",false,true,false,false), 
	Tutor ("Tutor",false,true,false,false), 
	Praesident("Präsident",false,true,true,false),
	Dekan ("Dekan",false,true,true,false), 
	ITAdmin("IT Admin",false,false,false,true), 
	Sekretaer("Sekretär",false,false,false,true);

	public String toGuiString;
	
	boolean receivesLessons;
	boolean givesLessons;
	boolean hasOrgResp;
	boolean isAdminStaff;

	
	
	private HSPersonaltyp(String toGuiString,boolean receivesLessons,
			boolean givesLessons,boolean hasOrgResp,boolean isAdminStaff
			) {
	
		this.toGuiString=toGuiString; 
		this.receivesLessons=receivesLessons; 
		this.givesLessons=givesLessons;
		this.hasOrgResp=hasOrgResp; 
		this.isAdminStaff=isAdminStaff; 
		
	
		
		
			}

		public String toGuiString() {
			
			return toGuiString;
		}
		// Get Methods for UniTest
		public boolean getReceivesLessons( ) {
			
			return  receivesLessons;
		
		
		
		
		}
		
		public boolean  getGivesLessons  ( ) {
			
			
			return givesLessons;
		}
		
		public boolean  getHasOrgResp () {
			
			return hasOrgResp;
		}
		
		public boolean getIsAdminStaff () {
			return isAdminStaff;
		}
		
		
		
		public Image getGuiIcon() {
			// TODO Auto-generated method stub
			return null;
		}

}