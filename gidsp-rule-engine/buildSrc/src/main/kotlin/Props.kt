object Props {
    const val DESCRIPTION = "Rule Engine"

    const val REPOSITORY_TYPE = "git"
    const val REPOSITORY_SYSTEM = "GitHub"
    const val REPOSITORY_URL = "https://github.com/gidsp/gidsp-rule-engine.git"

    const val ORGANIZATION_NAME = "fastable"
    const val ORGANIZATION_URL = "https://gidsp.org"

    const val LICENSE_NAME = "BSD-3-Clause"
    const val LICENSE_URL = "https://opensource.org/license/bsd-3-clause/"

    val DEVELOPERS = listOf(
        Developer(
            name = "Enrico Colasante",
            email = "enrico@gidsp.org",
            organization = "fastable",
            organizationUrl = "https://www.gidsp.org/"
        ),
        Developer(
            name = "Zubair Asghar",
            email = "zubair@gidsp.org",
            organization = "fastable",
            organizationUrl = "https://www.gidsp.org/"
        ),
    )
}

data class Developer(
    val name: String,
    val email: String,
    val organization: String,
    val organizationUrl: String,
)