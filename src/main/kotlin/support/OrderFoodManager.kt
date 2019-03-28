package support

import core.generators.DEmpiricGenerator
import core.generators.DEmppiricDataType
import core.generators.DEvenGenerator
import core.generators.DiscreteGenerator

import java.util.*

enum class FoodType {
    CAESAR, PENNE, SPAGETY, TUNIAK;

    fun foodName(): String {
        when(this) {
            CAESAR -> return "Cezar salat"
            PENNE -> return "Pene salat"
            SPAGETY -> return "Spagety"
            TUNIAK -> return "Tuniak"
        }
    }
}

data class OrderSession(val foodType: FoodType, val duration: Int)

class FoodManager(val seedGenerator: Random) {

    val cesarSalattimeGenerator: DEvenGenerator = DEvenGenerator(380, 441, seedGenerator.nextLong())
    val penesarSalattimeGenerator: DEmpiricGenerator
    val spagetyGenerator: DEmpiricGenerator
    val batathiSalatTime: Int = 180
    val probability = Random(seedGenerator.nextLong())

    init {
        val peneDataset =  LinkedList<DEmppiricDataType>()
        peneDataset.add(DEmppiricDataType(0.5,
                DiscreteGenerator(331, 630
                        , seedGenerator.nextLong())))

        peneDataset.add(DEmppiricDataType(0.35,
                DiscreteGenerator(631, 931
                        , seedGenerator.nextLong())))

        peneDataset.add(DEmppiricDataType(0.15,
                DiscreteGenerator(185, 331
                        , seedGenerator.nextLong())))

        this.penesarSalattimeGenerator = DEmpiricGenerator(peneDataset, Random(seedGenerator.nextLong()))

        val spagetyDataset =  LinkedList<DEmppiricDataType>()
        spagetyDataset.add(DEmppiricDataType(0.43,
                DiscreteGenerator(357, 541
                        , seedGenerator.nextLong())))

        spagetyDataset.add(DEmppiricDataType(0.37,
                DiscreteGenerator(541, 601
                        , seedGenerator.nextLong())))

        spagetyDataset.add(DEmppiricDataType(0.2,
                DiscreteGenerator(290, 357
                        , seedGenerator.nextLong())))

        spagetyGenerator = DEmpiricGenerator(spagetyDataset, Random(seedGenerator.nextLong()))
    }

    fun prepareOrder(): OrderSession {
        val prob = probability.nextDouble()
        var pTemp = 0.35

        if (prob < pTemp) {
            return OrderSession(FoodType.PENNE, penesarSalattimeGenerator.nextInt())
        }
        pTemp += 0.3

        if(prob < pTemp) {
            return OrderSession(FoodType.CAESAR, cesarSalattimeGenerator.nextInt())
        }
        pTemp += 0.2

        if(prob < pTemp) {
            return OrderSession(FoodType.SPAGETY, spagetyGenerator.nextInt())
        }

        return OrderSession(FoodType.TUNIAK, batathiSalatTime)
    }

}