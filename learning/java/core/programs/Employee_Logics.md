## Employee List Questions

- **Employee** :: Employee(String firstName, String lastName, int employeeNumber, float employeeSal, int employeePincode, DepartmentCode department)
```java
// Populate Employee Data to employeeList
List<Employee> employeeList = new ArrayList<Employee>();
List<Employee> empDet = new ArrayList<Employee>();
```

```java
// Highest Salary
Employee highestSal = employeeList.stream().collect(Collectors.maxBy(Comparator.comparing(Employee::geteSal))).get();
Employee highestSal = employeeList.stream().max(Comparator.comparing(Employee::geteSal)).get();

// Lowest Salary
Employee lowestSalary = employeeList.stream().collect(Collectors.minBy(Comparator.comparing(Employee::geteSal))).get();
Employee lowestSalary = employeeList.stream().min(Comparator.comparing(Employee::geteSal)).get();

// Nth Highest Salary
int rank=2;
Employee nthHighestSal = 
     employeeList.stream()
                 .sorted(Comparator.comparing(Employee::geteSal).reversed())
                 .collect(Collectors.toList())
                 .get(rank-1);

// Nth Highest Salary in Each Department, First create Reversed Comparator 
Comparator<Employee> salaryDesc = Comparator.comparing(Employee::geteSal).reversed();

employeeList.stream()
    .collect(Collectors.groupingBy(Employee::getDepartment))
    .forEach((x,y)->{
            System.out.print("Department: "+x);
            System.out.print(y.stream().sorted(salaryDesc).collect(Collectors.toList()).get(rank-1).toString()+"\n");
    });

/**** Sum of Salary *****/
Double sal = empDet.stream().filter(i->i.getEmpAge()<30).collect(Collectors.summingDouble(EmployeeExt::geteSal));
Double bySummingDouble = empDet.stream().collect(Collectors.summingDouble(EmployeeExt::geteSal));
Double byReduceFunc = empDet.stream().mapToDouble(EmployeeExt::geteSal).reduce(0, (s1, s2)->s1+s2);
Double bySummarizingDouble = empDet.stream().collect(Collectors.summarizingDouble(EmployeeExt::geteSal)).getSum();
float s = empData.stream().map(m->m.geteSal()).reduce((float) 0, (a,b)->a+b);

/**** Employees count by Gender ***/ 
  empDet.stream()
        .sorted((i1, i2)->i1.getEmpGender().compareTo(i2.getEmpGender()))
        .collect(Collectors.groupingBy(EmployeeExt::getEmpGender, Collectors.counting()))
        .forEach((x, y)->{System.out.println(x+" Employees Count : "+y);});

/*** Employees Avg salary by Gender ***/ 
  empDet.stream()
        .collect(Collectors.groupingBy(EmployeeExt::getEmpGender, Collectors.averagingDouble(EmployeeExt::geteSal)))
        .forEach((x, y)->{System.out.println(x+" Employees Avg Salary : "+y);});	

  
/* Find the employee in each branch who‘s is salary greater than 10000 **/
        empDet.stream()
		 	.collect(Collectors.groupingBy(Employee::getDepartment))
        .forEach((key,value)->{
         
             //key
                System.out.println("\n"+key+" Department");
        
            //value 
               value.stream()
                    .filter(s->s.geteSal()>salary)
                    .sorted((a,b)->a.getfName().compareTo(b.getfName()))
                    .collect(Collectors.toList())
                    .forEach(i->System.out.println(i.toStringSpecific()));
        });
        
        
/*** Print all the Employee Names by Gender ***/
empDet.stream()
      .collect(Collectors.groupingBy(EmployeeExt::getEmpGender))
      .forEach((x,y)->{
            System.out.println(
                    x + 
                    " Department Emp Names: " + 
                    y.stream().map(i->"["+i.getfName()+" "+i.getlName()+"]").collect(Collectors.joining(", "))
            );
      });

/******** Convert List to Map *********/
Map<Integer, EmployeeExt> eMap =
empDet.stream()
      .sorted((i1, i2)->i1.geteNumber()-i2.geteNumber())
      .collect(Collectors.toMap(EmployeeExt::geteNumber, Function.identity(), (e1, e2)->e1,LinkedHashMap::new));

/*** Salary in Range :: Print [EmpNumber-EmpSalary] Format ****/
Predicate<Employee> salaryInRange50K = e -> e.geteSal() >= 50000;
Predicate<Employee> salaryInRange100K = e -> e.geteSal() <= 100000;
Predicate<Employee> salaryInRange = salaryInRange50K.and(salaryInRange50K);

String collect = 
employeeList.stream().filter(salaryInRange)
            .collect(Collectors.toList())
            .stream().sorted(Comparator.comparing(Employee::geteSal).reversed())
            .map(m -> m.geteNumber() + "--" + m.geteSal())
            .collect(Collectors.joining(" "));

/* Get Department Count */
empData.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment))
        .forEach( (k,v) -> System.out.println("Dept: " + k + " -- " +v.stream().count()));

/*** Department salary summation ***/
 empData.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment))
        .forEach( (x,y) -> 
            System.out.println(
                x+
                " Department Total Salary is " +
                y.stream().collect(Collectors.summarizingDouble(Employee::geteSal)).getSum()
            )
        );

/* Departmentwise highest salary */
 empData.stream()
        .collect(Collectors.groupingBy(Employee::getDepartment))
        .forEach( (x,y) -> 
            System.out.println(
                x + 
                " Department Highest Salary is " + 
                y.stream()
                 .collect(Collectors.maxBy(Comparator.comparing(Employee::geteSal)))
                 .map(z->"[EmpID: "+z.geteNumber()+"]: "+z.geteSal())
                 .get()
            )
        );
 
/* Sort Employees using Comparator */

Comparator<Employee> comparator = Comparator.comparing(Employee::getfName)
                                            .thenComparing(Employee::getlName)
                                            .reversed();

empData.stream().distinct().sorted(comparator);

/* Adding List to HashMap */
Map<Object, Object> hMap = 
        emp.stream()
           .collect(Collectors.toMap(Employee::geteNumber, Function.identity(), (x, y) -> x, HashMap::new));

/* Adding List to TreeMap */
Map<Integer, Employee> tMap = 
        emp.stream()
           .collect(Collectors.toMap(Employee::geteNumber, Function.identity(), (e1, e2) -> e1, TreeMap::new));

/* Adding List to LinkedHashMap */
Map<Integer, Object> linkedHashMap = 
        emp.stream()
           .collect(Collectors.toMap(Employee::geteNumber, Function.identity(), (e1, e2) -> e1, LinkedHashMap::new));
 
/*** Employees FirstName with S ***/
    emp.stream()
       .filter(x -> x.getfName().startsWith("S"))
       .collect(Collectors.toList())
       .forEach(x -> System.out.println(x.geteNumber() + " - " + x.getfName()));

/*** Employees Salary > 50000 ***/
ArrayList<Employee> eList = 
        (ArrayList<Employee>) empDistinct.stream()
                                         .filter(x -> x.geteSal() > 50000)
                                         .collect(Collectors.toList());

/*** Sorting By firstName, lastName, Department ***/
Comparator<Employee> empComparator = 
        Comparator
            .comparing(Employee::getfName)
            .thenComparing(Employee::getlName)
            .thenComparing(Employee::getDepartment)
            .thenComparing(Employee::geteNumber)
            .reversed();

ArrayList<Employee> sorted =  (ArrayList<Employee>) 
         empDistinct.stream()
                    .sorted(empComparator)
                    .collect(Collectors.toList());


```
