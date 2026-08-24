# PII Field Annotation Companion

Gutter warning icon on any Java/Kotlin class field whose name looks
like it holds PII (`email`, `ssn`, `phoneNumber`, `dateOfBirth`,
`creditCard`, `passportNumber`, `nationalId`, `taxId`, and similar)
with no annotation of any kind on it — no way for masking, redaction,
or access-control tooling built around annotations to ever pick it up.

## Why it exists

Many teams build masking/redaction/audit tooling around a
classification annotation on sensitive fields — but nothing enforces
that every field that *should* have one actually does. A field added
later, or copy-pasted from an unrelated DTO, is easy to miss. Nothing
in the IDE flags an obviously PII-shaped field with zero annotations
today.

## Why built this way

- **100% static text/PSI analysis** — matches the field name by simple
  text, so it works whether the real annotation library is on the
  classpath or not. Java and Kotlin, including Kotlin
  primary-constructor properties.
- **Accepts any annotation as "classified"** — there's no single
  standard PII annotation across teams (Bean Validation, Jackson
  views, a custom `@Pii`/`@Sensitive`, anything counts); this plugin
  only flags the *complete absence* of any annotation.

## v0.1 scope — stated honestly, not exhaustively

Covers the most common, unambiguous PII field-name shapes only — not
an exhaustive privacy-law taxonomy (GDPR/CCPA "personal data" is far
broader and inherently contextual). A field with an unusual name that
still holds PII isn't covered.

## Usage

Open any Java/Kotlin class. An unannotated field whose name looks like
PII shows a warning icon.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
