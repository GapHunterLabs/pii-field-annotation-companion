# Demo data for screenshots

`User.java` — `email` has no annotation (flagged), `phoneNumber` is
classified with `@Sensitive` (not flagged), `username` isn't
PII-shaped (not flagged).

## How to get the screenshot

1. `./gradlew runIde` from `pii-field-annotation-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `User.java` — a warning icon should appear only
   on `email`.
3. Screenshot with all 3 fields visible, save into
   `pii-field-annotation-companion/docs/screenshots/`. Close the
   sandbox.
