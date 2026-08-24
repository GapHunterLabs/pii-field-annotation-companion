package dev.gaphunter.piifieldannotationcompanion.detect

/**
 * Field-name fragments this plugin treats as "looks like it holds PII"
 * -- deliberately broad substring matches (not resolved calls), same
 * "match a known name, don't resolve a symbol" discipline as
 * `SqlSignalNames`/`CorrelationSignals` elsewhere in this catalog.
 * Only the most common, unambiguous PII categories are included --
 * this is not meant to be an exhaustive privacy-law taxonomy (GDPR/
 * CCPA definitions of "personal data" are far broader and inherently
 * contextual), only the concrete field-name shapes a human reviewer
 * would flag on sight.
 */
object PiiFieldSignals {

    private val PII_NAME_FRAGMENTS = listOf(
        "email",
        "ssn",
        "socialsecurity",
        "phonenumber",
        "phone_number",
        "dateofbirth",
        "date_of_birth",
        "creditcard",
        "credit_card",
        "passportnumber",
        "passport_number",
        "nationalid",
        "national_id",
        "taxid",
        "tax_id",
    )

    /** Bare, short field names that need an exact match, not substring -- avoids matching e.g. "dobsonUsername". */
    private val PII_EXACT_NAMES = setOf("dob", "phone", "ssn")

    fun looksLikePii(fieldName: String): Boolean {
        val lower = fieldName.lowercase()
        if (lower in PII_EXACT_NAMES) return true
        return PII_NAME_FRAGMENTS.any { lower.contains(it) }
    }
}
