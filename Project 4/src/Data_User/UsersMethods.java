package Data_User;

import java.util.List;

public interface UsersMethods {
	public static final String ALIGNMENT_EXTENSION = ".alignment.txt";
	public static final String REPORTS_EXTENSION = ".score.txt";
	
	//Every user has one Leader (except for the leader themselves)
	void setMyTeam(List<Users> users); //This method is used to link the user with their own team
	String getLeadBy();
	boolean isAccess();
	String genomeOutput();
	String reportOutput();
	
	
}
