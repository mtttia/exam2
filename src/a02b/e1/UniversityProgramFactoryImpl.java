package a02b.e1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import a02b.e1.UniversityProgram.Sector;

public class UniversityProgramFactoryImpl implements UniversityProgramFactory {

    class Course
    {
        public String name; 
        public UniversityProgram.Sector sector; 
        public int credits;

        public Course(String name, UniversityProgram.Sector sector, int credits)
        {
            this.name = name;
            this.sector = sector;
            this.credits = credits;
        }
    }

    UniversityProgram general(Predicate<List<UniversityProgramFactoryImpl.Course>> isOk)
    {
        return new UniversityProgram() {
            Map<String, Course> courses = new HashMap<>();

            @Override
            public void addCourse(String name, Sector sector, int credits) {
                courses.put(name, new Course(name,sector, credits));
            }

            @Override
            public boolean isValid(Set<String> courseNames) {
                var list = courses.entrySet().stream().filter((i) -> courseNames.contains(i.getKey())).map((i) -> i.getValue()).toList();
                return isOk.test(list);
            }
            
        };
    }

    int sumCredits(List<UniversityProgramFactoryImpl.Course> list)
    {
        int sum = 0;
        for(var item:list)
        {
            sum += item.credits;
        }

        return sum;
    }

    int getSubjectCredit(List<UniversityProgramFactoryImpl.Course> list, Set<UniversityProgram.Sector> course)
    {
        int c = 0;
        for(var item:list)
        {
            if(course.contains(item.sector))
            {
                c += item.credits;
            }
        }

        return c;
    }

    @Override
    public UniversityProgram flexible() {
        return general(
            (c) -> sumCredits(c) == 60
        );
    }

    @Override
    public UniversityProgram scientific() {
        return general(
            (c) -> sumCredits(c) == 60 
            && getSubjectCredit(c, Set.of(Sector.MATHEMATICS)) >= 12 
            && getSubjectCredit(c, Set.of(Sector.COMPUTER_SCIENCE)) >= 12 
            && getSubjectCredit(c, Set.of(Sector.PHYSICS)) >= 12
        );
    }

    @Override
    public UniversityProgram shortComputerScience() {
        return general(
            (c) -> sumCredits(c) >= 48 && getSubjectCredit(c, Set.of(Sector.COMPUTER_SCIENCE, Sector.COMPUTER_ENGINEERING)) >= 30
        );
    }

    @Override
    public UniversityProgram realistic() {
        return general(
            (c) -> sumCredits(c) == 120 
            && getSubjectCredit(c, Set.of(Sector.COMPUTER_SCIENCE, Sector.COMPUTER_ENGINEERING)) >= 60 
            && getSubjectCredit(c, Set.of(Sector.MATHEMATICS, Sector.PHYSICS)) <= 18 
            && getSubjectCredit(c, Set.of(Sector.THESIS)) >= 24
        );
    }
    
}
