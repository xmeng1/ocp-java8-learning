package science.mengxin.java.ocp;

// which is similar with the Predicate
@FunctionalInterface
public interface DogQuerier {
    public boolean test(Dog dog);
}
