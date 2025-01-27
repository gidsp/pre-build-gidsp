plugins {
    `maven-publish`
    signing
    id("io.github.gradle-nexus.publish-plugin")
    id("org.jetbrains.dokka")
}

val ossrhUsername: String? = System.getenv("OSSRH_USERNAME")
val ossrhPassword: String? = System.getenv("OSSRH_PASSWORD")
val signingPrivateKey: String? = System.getenv("SIGNING_PRIVATE_KEY")
val signingPassword: String? = System.getenv("SIGNING_PASSWORD")

val dokkaHtml = tasks.findByName("dokkaHtml")!!

val dokkaHtmlJar = tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(dokkaHtml)
    from(dokkaHtml.outputs)
    archiveClassifier.set("javadoc")
}

publishing {
    publications.withType<MavenPublication> {
        artifact(dokkaHtmlJar)

        pom {
            name.set("rule-engine")
            description.set(Props.DESCRIPTION)

            organization {
                name.set(Props.ORGANIZATION_NAME)
                url.set(Props.ORGANIZATION_URL)
            }
            developers {
                Props.DEVELOPERS.map {
                    developer {
                        name.set(it.name)
                        email.set(it.email)
                        organization.set(it.organization)
                        organizationUrl.set(it.organizationUrl)
                    }
                }
            }
            licenses {
                license {
                    name.set(Props.LICENSE_NAME)
                    url.set(Props.LICENSE_URL)
                }
            }
            scm {
                url.set("lp:gidsp")
            }
            issueManagement {
                system.set(Props.REPOSITORY_SYSTEM)
                url.set(Props.REPOSITORY_URL)
            }
            url.set(Props.REPOSITORY_URL)
        }
    }
}

nexusPublishing {
    this.repositories {
        sonatype {
            username.set(ossrhUsername)
            password.set(ossrhPassword)
        }
    }
}

signing {
    setRequired({ !version.toString().endsWith("") })
    useInMemoryPgpKeys(signingPrivateKey, signingPassword)
    sign(publishing.publications)
}

// Fix Gradle warning about signing tasks using publishing task outputs without explicit dependencies
// https://github.com/gradle/gradle/issues/26091
tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}
