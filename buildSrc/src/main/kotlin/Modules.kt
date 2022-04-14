object Modules {
    const val App = ":app"

    object Data {
        private const val prefix = ":data"

        const val Base = "$prefix:base"
    }

    object Domain {
        private const val prefix = ":domain"

        const val Base = "$prefix:base"
    }

    object Features {
        private const val prefix = ":features"

        const val Base = "$prefix:base"
    }
}