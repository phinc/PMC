package by.phinc.pmc.model.beans;

import java.util.Collection;
import java.util.TreeSet;

import by.phinc.pmc.secure.GrantedAuthority;

public class Employee implements UserDetails, Comparable<Employee> {
	
	private Integer id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String login;
	
	private String password;
	
	private String post;
	
	private Collection<GrantedAuthority> authorities = new TreeSet<GrantedAuthority>();
	
	
	public Employee() {
		super();
	}

	public Employee(String login) {
		super();
		this.login = login;
	}

	
	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer pId) {
		this.id = pId;		
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Override
	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPost() {
		return post;
	}
	
	public String getName() {
		return getLastName() + " " + getFirstName();
	}

	public void setPost(String post) {
		this.post = post;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Employee)) {
			return false;
		}
		Employee employee = (Employee)obj;
		return id.equals(employee.getId());
	}

	@Override
	public int compareTo(Employee employee) {
		return id.compareTo(employee.getId());
	}
	
	
}
