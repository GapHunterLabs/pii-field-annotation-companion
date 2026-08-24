package dev.gaphunter.piifieldannotationcompanion.detect

import com.intellij.psi.JavaRecursiveElementWalkingVisitor
import com.intellij.psi.PsiField
import com.intellij.psi.PsiFile
import dev.gaphunter.piifieldannotationcompanion.model.PiiFieldHit

/**
 * Finds Java class fields whose name looks like PII
 * ([PiiFieldSignals]) with no annotation of any kind on the field --
 * any real classification annotation (`@Pii`, `@Sensitive`,
 * `@Redact`, a Bean Validation constraint, a Jackson view, anything)
 * is accepted as "classified"; this plugin doesn't require a specific
 * annotation type since there's no single standard one across teams.
 */
object JavaPiiFieldFinder {

    fun findAll(file: PsiFile): List<PiiFieldHit> {
        val hits = mutableListOf<PiiFieldHit>()
        file.accept(object : JavaRecursiveElementWalkingVisitor() {
            override fun visitField(field: PsiField) {
                super.visitField(field)
                val name = field.name
                if (!PiiFieldSignals.looksLikePii(name)) return
                if (field.modifierList?.annotations?.isNotEmpty() == true) return
                val nameIdentifier = field.nameIdentifier ?: return
                hits += PiiFieldHit(nameIdentifier, name)
            }
        })
        return hits
    }
}
