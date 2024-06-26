package telran.employees;

import java.util.*;

//So far we do consider optimization
public class CompanyMapsImpl implements Company {
	TreeMap<Long, Employee> employees = new TreeMap<>();
	HashMap<String, List<Employee>> employeesDepartment = new HashMap<>();
	TreeMap<Float, List<Manager>> factorManagers = new TreeMap<>();

	@Override
	public Iterator<Employee> iterator() {
		return new CompanyIterator();
	}

	private class CompanyIterator implements Iterator<Employee> {
		private Iterator<Long> keyIterator = employees.keySet().iterator();

	    @Override
	    public boolean hasNext() {
	        return keyIterator.hasNext();
	    }

	    @Override
	    public Employee next() {
	        if (!hasNext()) {
	            throw new NoSuchElementException();
	        }
	        Long key = keyIterator.next();
	        return employees.get(key);
	    }
	}

	@Override
	public void addEmployee(Employee empl) {
		if (!employees.containsKey(empl.getId())) {
			employees.put(empl.getId(), empl);
			employeesDepartment.computeIfAbsent(empl.getDepartment(), k -> new ArrayList<>()).add(empl);
			if(empl instanceof Manager) {
				Manager manager = (Manager) empl;
				factorManagers.computeIfAbsent(manager.factor, k -> new ArrayList<>()).add(manager);
			}
		} else {
			throw new IllegalStateException("This employee already exists");
		}
	}

	@Override
	public Employee getEmployee(long id) {
		return employees.containsKey(id) ? employees.get(id) : null;
	}

	@Override
	public Employee removeEmployee(long id) {
		Employee employeeRemoved = employees.remove(id);
		if(employeeRemoved != null) {
			employeesDepartment.get(employeeRemoved.getDepartment()).remove(employeeRemoved);
		}else {
			throw new NoSuchElementException("This employee is not exists");
		}
		return employeeRemoved;
	}

	@Override
	public int getDepartmentBudget(String department) {
		int budgetOfDepartment = 0;
		List<Employee> departmentEmployees  = employeesDepartment.get(department);
			if(departmentEmployees != null) {
				for(Employee empl: departmentEmployees) {	
					
					if(empl instanceof WageEmployee) {
						WageEmployee wageEmployee = (WageEmployee) empl;
						budgetOfDepartment += wageEmployee.computeSalary();
						
					}else if(empl instanceof Manager) {
						Manager manager = (Manager) empl;						
						budgetOfDepartment +=  manager.computeSalary();
						
					}else if(empl instanceof SalesPerson) {
						SalesPerson salesPerson = (SalesPerson) empl;						
						budgetOfDepartment +=  salesPerson.computeSalary();
					}
				}
			}
		return budgetOfDepartment;
	}

	@Override
	public String[] getDepartments() {
		Set<String> departmentsSet = new TreeSet<>(employeesDepartment.keySet());
		return departmentsSet.toArray(new String[0]);
	}

	@Override
	public Manager[] getManagersWithMostFactor() {
		Manager[] resManagArray;
		if(factorManagers.isEmpty()) {
			resManagArray = new Manager[0];
		}
		Float maxFactor = factorManagers.lastKey();
		List<Manager> managersWithMaxFactor  = factorManagers.get(maxFactor);
		resManagArray = managersWithMaxFactor.toArray(new Manager[managersWithMaxFactor.size()]);
		return resManagArray;
	}

}
