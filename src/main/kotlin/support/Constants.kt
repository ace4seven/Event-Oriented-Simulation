class C {

    companion object {
        val DEBUG = false

        fun message(text: String) {
            if (DEBUG) {
                println(text)
            }
        }
    }

}