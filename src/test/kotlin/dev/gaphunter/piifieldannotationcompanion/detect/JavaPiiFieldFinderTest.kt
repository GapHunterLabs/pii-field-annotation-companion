package dev.gaphunter.piifieldannotationcompanion.detect

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class JavaPiiFieldFinderTest : BasePlatformTestCase() {

    fun `test an unannotated PII-looking field is flagged`() {
        val file = myFixture.configureByText(
            "User.java",
            """
            class User {
                String email;
            }
            """.trimIndent(),
        )
        assertEquals(1, JavaPiiFieldFinder.findAll(file).size)
    }

    fun `test a field with any annotation at all is not flagged`() {
        val file = myFixture.configureByText(
            "User.java",
            """
            class User {
                @NotNull
                String email;
            }
            """.trimIndent(),
        )
        assertTrue(JavaPiiFieldFinder.findAll(file).isEmpty())
    }

    fun `test a non-PII field name is never flagged`() {
        val file = myFixture.configureByText(
            "User.java",
            """
            class User {
                String username;
            }
            """.trimIndent(),
        )
        assertTrue(JavaPiiFieldFinder.findAll(file).isEmpty())
    }

    fun `test exact-match short names require exact match`() {
        val file = myFixture.configureByText(
            "User.java",
            """
            class User {
                String dobsonUsername;
            }
            """.trimIndent(),
        )
        assertTrue(JavaPiiFieldFinder.findAll(file).isEmpty())
    }
}
