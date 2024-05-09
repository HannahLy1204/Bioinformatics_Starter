package Data_User;

import java.util.List;

import Data_Genome.Repository;
import _RunMe.Address;


public abstract class Users implements UsersMethods {
	//Attributes of a users
	private String type;
	private String name;
	private int yearsOfExperience;
	protected List<Users> myTeam; //Share for all members (in the same team), thus, protected
	protected TeamLead leadBy; //Every user has one Team Leader
	
	//Static attributes...
	//The same Repository for all users' instances, thus, static.
	private static Repository repository= new Repository();
	private static Address output = Address.FILES_GENOME;
	private static Address reports = Address.FILES_REPORT;

	//default Constructor
	public Users() {
		this.setType("UNDEFINED");
		this.setName("UNDEFINED");
		this.setYearsOfExperience(0);
	}

	//Constructor
	public Users(String function, String name, int year) {
		this.setType(function);
		this.setName(name);
		this.setYearsOfExperience(year);
		this.isAccess();
		
	}
	
	//Methods from the interface
	@Override
	public boolean isAccess() {
		if (this instanceof TeamLead) {
			return true;
		} else if (this instanceof TechnicalSupport) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String genomeOutput() {
		return output.getDirectory()+ this.getName() + ALIGNMENT_EXTENSION;
	}

	@Override
	public String reportOutput() {
		return reports.getDirectory()+ this.getName() + REPORTS_EXTENSION;
	}

	//Getters and Setters
	public String getLeadBy() {
		return "Lead by: " + leadBy.getName();
	}
	
	public void setMyTeam(List<Users> users) {
		if(!users.isEmpty()) {
			if(users.contains(this)) {
				myTeam = users;
				for (Users u : myTeam) {
					if (u instanceof TeamLead) {
						leadBy = (TeamLead) u;
					}
				}
			}else {
				throw new IllegalArgumentException("This user is not available in the input!");
			}
		} else {
			throw new IllegalArgumentException("The input is empty!");
		}
		
	}
	
	//Check access of Users
	public Repository getRepository() throws IllegalArgumentException{
		if(isAccess()) {
			return repository;
		}else {
			throw new IllegalArgumentException("This user is not allowed to access Repository!");
		}
	}
	
	public void setRepository(Repository optimal) {
		if(isAccess()) {
			Users.repository=optimal;
		}else {
			throw new IllegalArgumentException("Only Leader is allowed to set Repository!");
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getYearsOfExperience() {
		return yearsOfExperience;
	}

	public void setYearsOfExperience(int yearsOfExperience) {
		this.yearsOfExperience = yearsOfExperience;
	}
	
	public List<Users> getMyTeam(){
		return myTeam;
	}

}
