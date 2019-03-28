class C {

    companion object {
        val DEBUG = true

        fun message(text: String) {
            if (DEBUG) {
                println(text)
            }
        }
    }

}