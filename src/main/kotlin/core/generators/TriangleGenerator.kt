package core.generators

class TriangleGenerator(val min: Double, val max: Double, val modus: Double, seed: Long? = null): Generator(seed) {

    fun nextDouble(): Double {
        val r = randomGenerator.nextDouble()

        if (equalWithPrecision(r, ((modus - min) / (max - min)), 10.toDouble())) {
            return modus
        } else if (r < ((modus - min) / (max - min))) {
            return min + Math.sqrt((r * (max - min) * (modus - min)))
        } else {
            return max - Math.sqrt(((1 - r) * (max - min) * (max - modus)))
        }
    }

    private fun equalWithPrecision(a: Double, b: Double, precision: Double): Boolean {
        return Math.abs(a - b) <= Math.pow(10.0, -precision);
    }

}