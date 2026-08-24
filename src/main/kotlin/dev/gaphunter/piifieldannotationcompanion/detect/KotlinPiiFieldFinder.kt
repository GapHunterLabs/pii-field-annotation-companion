package dev.gaphunter.piifieldannotationcompanion.detect

import com.intellij.psi.PsiFile
import dev.gaphunter.piifieldannotationcompanion.model.PiiFieldHit
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.KtTreeVisitorVoid

/**
 * Kotlin counterpart of [JavaPiiFieldFinder]. Covers both a regular
 * class-body `val`/`var` property and a primary-constructor property
 * parameter (`class User(val email: String)`) -- the common real shape
 * for a Kotlin data class.
 */
object KotlinPiiFieldFinder {

    fun findAll(file: PsiFile): List<PiiFieldHit> {
        if (file !is KtFile) return emptyList()
        val hits = mutableListOf<PiiFieldHit>()
        file.accept(object : KtTreeVisitorVoid() {
            override fun visitProperty(property: KtProperty) {
                super.visitProperty(property)
                if (!property.isMember) return
                val name = property.name ?: return
                if (!PiiFieldSignals.looksLikePii(name)) return
                if (property.annotationEntries.isNotEmpty()) return
                val nameIdentifier = property.nameIdentifier ?: return
                hits += PiiFieldHit(nameIdentifier, name)
            }

            override fun visitParameter(parameter: KtParameter) {
                super.visitParameter(parameter)
                if (!parameter.hasValOrVar()) return
                val name = parameter.name ?: return
                if (!PiiFieldSignals.looksLikePii(name)) return
                if (parameter.annotationEntries.isNotEmpty()) return
                val nameIdentifier = parameter.nameIdentifier ?: return
                hits += PiiFieldHit(nameIdentifier, name)
            }
        })
        return hits
    }
}
