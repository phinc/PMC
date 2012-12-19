package by.phinc.pmc.model.beans;

public class TeamMember implements IModel<Integer>, Comparable<TeamMember>{
	
	private Integer id;
	
	private Project project;
	
	private Employee employee;
	
	private String role;

	public TeamMember() {
		super();
	}
	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer pId) {
		this.id = pId;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public String getFirstName() {
		return (employee != null) ? employee.getFirstName() : "unknown";
	}
	
	public String getLastName() {
		return (employee != null) ? employee.getLastName() : "unknown";
	}
	
	public String getPost() {
		return (employee != null) ? employee.getPost() : "unknown";
	}

	@Override
	public int compareTo(TeamMember member) {
		return id.compareTo(member.getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof TeamMember)) {
			return false;
		}
		TeamMember member = (TeamMember)obj;
		return id.equals(member.getId());
	}
	
}
