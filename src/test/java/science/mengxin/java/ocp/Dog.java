package science.mengxin.java.ocp;

import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Data
public class Dog {

    private String name;
    private int age;
    private int weight;

    public Dog(String name, int age, int weight) {
        this.name = name;
        this.age = age;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                '}';
    }

    public String bark() {
        String bark = "Woof!";
        System.out.println(bark);
        return bark;
    }
}
