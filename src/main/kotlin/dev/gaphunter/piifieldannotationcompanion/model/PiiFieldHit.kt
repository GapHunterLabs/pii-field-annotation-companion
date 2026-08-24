package dev.gaphunter.piifieldannotationcompanion.model

import com.intellij.psi.PsiElement

/** One class field whose name looks like PII, with no annotation of any kind on it. */
data class PiiFieldHit(val nameElement: PsiElement, val fieldName: String)
