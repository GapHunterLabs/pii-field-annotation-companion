package dev.gaphunter.piifieldannotationcompanion.gutter

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProviderDescriptor
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import dev.gaphunter.piifieldannotationcompanion.detect.JavaPiiFieldFinder
import dev.gaphunter.piifieldannotationcompanion.detect.KotlinPiiFieldFinder
import dev.gaphunter.piifieldannotationcompanion.model.PiiFieldHit
import dev.gaphunter.piifieldannotationcompanion.review.ReviewPrompt

class UnclassifiedPiiFieldLineMarkerProvider : LineMarkerProviderDescriptor(), DumbAware {

    override fun getName(): String = "Unclassified PII-looking field"

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? = null

    override fun collectSlowLineMarkers(elements: MutableList<out PsiElement>, result: MutableCollection<in LineMarkerInfo<*>>) {
        val file = elements.firstOrNull()?.containingFile ?: return
        val hits = when (file.language.id) {
            "JAVA" -> JavaPiiFieldFinder.findAll(file)
            "kotlin" -> KotlinPiiFieldFinder.findAll(file)
            else -> emptyList()
        }
        if (hits.isEmpty()) return

        val hitsByElement = hits.associateBy { it.nameElement }
        for (element in elements) {
            val hit = hitsByElement[element] ?: continue
            result.add(buildMarker(hit))

            val path = file.virtualFile?.path ?: continue
            val lineNumber = file.viewProvider.document?.getLineNumber(element.textRange.startOffset) ?: -1
            ReviewPrompt.recordHit(file.project, "$path:$lineNumber")
        }
    }

    private fun buildMarker(hit: PiiFieldHit): LineMarkerInfo<PsiElement> {
        val tooltip = "Field '${hit.fieldName}' looks like it holds PII but has no classification annotation -- " +
            "no @Pii/@Sensitive/@Redact or similar to flag it for masking, redaction, or access-control tooling"
        return LineMarkerInfo(
            hit.nameElement,
            hit.nameElement.textRange,
            PiiIcons.RISK,
            { _: PsiElement -> tooltip },
            null,
            GutterIconRenderer.Alignment.RIGHT,
            { tooltip },
        )
    }
}
