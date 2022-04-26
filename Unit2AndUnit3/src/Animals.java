import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author osys
 */
public class Animals {
    Ark ark;
    Species species;
    Gender gender;

    /**
     * 加载动物对 载体
     * @param candidates 候选动物
     * @return 动物对数
     */
    public int loadTheArk(Collection<Animal> candidates) {
        SortedSet<Animal> animals;
        int numPairs = 0;
        // 候选
        Animal candidate = null;

        animals = new TreeSet<Animal>(new SpeciesGenderComparator());
        animals.addAll(candidates);
        // 依次循环所有候选动物，存在同物种，不同性别的动物，加入动物对中
        for (Animal animal : animals) {
            if (candidate == null || !candidate.isPotentialMate(animal)) {
                candidate = animal;
            } else {
                // 同物种，不同性别。向【动物对】载体中添加这两个动物
                ark.load(new AnimalPair(candidate, animal));
                ++numPairs;
                candidate = null;
            }
        }
        // 动物对数
        return numPairs;
    }


    /** 动物 */
    static class Animal {
        /** 物种 */
        Species species;
        /** 性别 */
        Gender gender;

        /** 是否是同物种，不同性别 */
        public boolean isPotentialMate(Animal other) {
            return this.species == other.species && this.gender != other.gender;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Animal)) {
                return false;
            }
            Animal animal = (Animal) o;
            return species == animal.species && gender == animal.gender;
        }

        @Override
        public int hashCode() {
            return Objects.hash(species, gender);
        }
    }

    /** 物种 */
    enum Species {

        // 土豚、孟加拉虎、驯鹿、野狗、大象、青蛙、GNU、土狼、
        AARDVARK, BENGAL_TIGER, CARIBOU, DINGO, ELEPHANT, FROG, GNU, HYENA,
        // 鬣蜥、美洲虎、猕猴桃、美洲豹、马斯塔顿、蝾螈、章鱼、
        IGUANA, JAGUAR, KIWI, LEOPARD, MASTADON, NEWT, OCTOPUS,
        // 食人鱼、格查尔、犀牛、蝾螈、三趾树懒、
        PIRANHA, QUETZAL, RHINOCEROS, SALAMANDER, THREE_TOED_SLOTH,
        // 独角兽、毒蛇、狼人、黄蜂、牦牛、斑马
        UNICORN, VIPER, WEREWOLF, XANTHUS_HUMMINBIRD, YAK, ZEBRA
    }

    /** 性别 */
    enum Gender {
        // 雄性、雌性
        MALE, FEMALE
    }

    /**一对动物 */
    static class AnimalPair {
        private final Animal one, two;

        public AnimalPair(Animal one, Animal two) {
            this.one = one;
            this.two = two;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof AnimalPair)) {
                return false;
            }
            AnimalPair that = (AnimalPair) o;
            return Objects.equals(one, that.one) && Objects.equals(two, that.two);
        }

        @Override
        public int hashCode() {
            return Objects.hash(one, two);
        }
    }

    /** 物种性别比较 */
    static class SpeciesGenderComparator implements Comparator<Animal> {
        /**
         * 先进行物种比较，物种比较相同，再进行性别比较
         * @param one 动物1
         * @param two 动物2
         * @return 0为物种相同，且
         */
        @Override
        public int compare(Animal one, Animal two) {
            // Enum.compareTo()
            // 将此枚举与订单的指定对象进行比较。当此对象小于、等于或大于指定对象时，返回负整数、零或正整数。
            // 枚举常量只能与同一枚举类型的其他枚举常量进行比较。该方法实现的自然顺序是常量的声明顺序。
            int speciesCompare = one.species.compareTo(two.species);
            return (speciesCompare != 0)
                    ? speciesCompare
                    : one.gender.compareTo(two.gender);
        }
    }

    /** 动物对 载体 */
    static class Ark {
        private final Set<AnimalPair> loadedAnimals = new HashSet<>();

        /** 向 【动物对】 载体中添加 【动物对】 */
        public void load(AnimalPair pair) {
            loadedAnimals.add(pair);
        }
    }
}