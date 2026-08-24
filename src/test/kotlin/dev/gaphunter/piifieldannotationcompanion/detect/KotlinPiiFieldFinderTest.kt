package dev.gaphunter.piifieldannotationcompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class KotlinPiiFieldFinderTest : BasePlatformTestCase() {

    fun `test an unannotated PII-looking property is flagged`() {
        val file = myFixture.configureByText(
            "User.kt",
            """
            class User {
                var email: String = ""
            }
            """.trimIndent(),
        )
        assertEquals(1, KotlinPiiFieldFinder.findAll(file).size)
    }

    fun `test a primary-constructor PII property is flagged`() {
        val file = myFixture.configureByText(
            "User.kt",
            """
            class User(val phoneNumber: String)
            """.trimIndent(),
        )
        assertEquals(1, KotlinPiiFieldFinder.findAll(file).size)
    }

    fun `test an annotated property is not flagged`() {
        val file = myFixture.configureByText(
            "User.kt",
            """
            class User(@Sensitive val phoneNumber: String)
            """.trimIndent(),
        )
        assertTrue(KotlinPiiFieldFinder.findAll(file).isEmpty())
    }

    fun `test a constructor parameter without val or var is never flagged`() {
        val file = myFixture.configureByText(
            "User.kt",
            """
            class User(email: String)
            """.trimIndent(),
        )
        assertTrue(KotlinPiiFieldFinder.findAll(file).isEmpty())
    }

    fun `test a non-PII property is never flagged`() {
        val file = myFixture.configureByText(
            "User.kt",
            """
            class User {
                var username: String = ""
            }
            """.trimIndent(),
        )
        assertTrue(KotlinPiiFieldFinder.findAll(file).isEmpty())
    }
}
